
package com.fastaccess.provider.tasks.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.annimon.stream.Stream;
import com.fastaccess.R;
import com.fastaccess.data.dao.model.Comment;
import com.fastaccess.data.dao.model.Login;
import com.fastaccess.data.dao.model.Notification;
import com.fastaccess.data.dao.model.NotificationQueue;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.provider.markdown.MarkDownProvider;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.modules.notification.NotificationActivity;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by Kosh on 19 Feb 2017, 6:32 PM
 */

public class NotificationSchedulerJobTask extends JobService {
  private final static String JOB_ID = "fasthub_notification";
  private final static String SINGLE_JOB_ID = "single_fasthub_notification";

  private final static int THIRTY_MINUTES = 30 * 60;
  private static final String NOTIFICATION_GROUP_ID = "FastHub";

  @Override
  public boolean onStartJob(final JobParameters job) {
    if ((!SINGLE_JOB_ID.equalsIgnoreCase(job.getTag())) && (PrefGetter.getNotificationTaskDuration() == -1)) {
      scheduleJob(this, -1, false);
      finishJob(job);
      return true;
    }
    Login login = null;
    try {
      login = Login.getUser();
    } catch (Exception ignored) {
    }
    if (login != null) {
      RestProvider.getNotificationService(PrefGetter.isEnterprise())
          .getNotifications(ParseDateFormat.getLastWeekDate())
          .subscribeOn(Schedulers.io())
          .subscribe(item -> {
            AppHelper.cancelAllNotifications(getApplicationContext());
            if (item != null) {
              onSave(item.getItems(), job);
            } else {
              finishJob(job);
            }
          }, throwable -> jobFinished(job, true));
    }
    return true;
  }

  @Override
  public boolean onStopJob(final JobParameters jobParameters) {
    return false;
  }

  public static void scheduleJob(final @NonNull Context context) {
    int duration = PrefGetter.getNotificationTaskDuration();
    scheduleJob(context, duration, false);
  }

  public static void scheduleJob(final @NonNull Context context,
                                 final int duration, final boolean cancel) {
    if (AppHelper.isGoogleAvailable(context)) {
      FirebaseJobDispatcher dispatcher =
          new FirebaseJobDispatcher(new GooglePlayDriver(context));
      dispatcher.cancel(SINGLE_JOB_ID);
      if (cancel)
        dispatcher.cancel(JOB_ID);
      if (duration == -1) {
        dispatcher.cancel(JOB_ID);
        return;
      }
      duration = duration <= 0 ? THIRTY_MINUTES : duration;
      Job.Builder builder =
          dispatcher.newJobBuilder()
              .setTag(JOB_ID)
              .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
              .setLifetime(Lifetime.FOREVER)
              .setRecurring(true)
              .setConstraints(Constraint.ON_ANY_NETWORK)
              .setTrigger(Trigger.executionWindow(duration / 2, duration))
              .setService(NotificationSchedulerJobTask.class);
      dispatcher.mustSchedule(builder.build());
    }
  }

  public static void scheduleOneTimeJob(final @NonNull Context context) {
    if (AppHelper.isGoogleAvailable(context)) {
      FirebaseJobDispatcher dispatcher =
          new FirebaseJobDispatcher(new GooglePlayDriver(context));
      Job.Builder builder = dispatcher.newJobBuilder()
                                .setTag(SINGLE_JOB_ID)
                                .setReplaceCurrent(true)
                                .setRecurring(false)
                                .setTrigger(Trigger.executionWindow(30, 60))
                                .setConstraints(Constraint.ON_ANY_NETWORK)
                                .setService(NotificationSchedulerJobTask.class);
      dispatcher.mustSchedule(builder.build());
    }
  }

  private void onSave(final
                      @Nullable List<Notification> notificationThreadModels,
                      final JobParameters job) {
    if (notificationThreadModels != null) {
      Notification.save(notificationThreadModels);
      onNotifyUser(notificationThreadModels, job);
    }
  }

  private void
  onNotifyUser(final @NonNull List<Notification> notificationThreadModels,
               final JobParameters job) {
    long count = Stream.of(notificationThreadModels)
                     .filter(Notification::isUnread)
                     .count();
    if (count == 0) {
      AppHelper.cancelAllNotifications(getApplicationContext());
      finishJob(job);
      return;
    }
    Context context = getApplicationContext();
    int accentColor = ContextCompat.getColor(this, R.color.material_blue_700);
    Notification first = notificationThreadModels.get(0);
    Observable.fromIterable(notificationThreadModels)
        .subscribeOn(Schedulers.io())
        .filter(notification
                -> notification.isUnread() &&
                       first.getId() != notification.getId() &&
                       !NotificationQueue.exists(notification.getId()))
        .take(10)
        .flatMap(
            notification
            -> {
              if (notification.getSubject() != null &&
                  notification.getSubject().getLatestCommentUrl() != null) {
                return RestProvider
                    .getNotificationService(PrefGetter.isEnterprise())
                    .getComment(notification.getSubject().getLatestCommentUrl())
                    .subscribeOn(Schedulers.io());
              } else {
                return Observable.empty();
              }
            },
            (thread, comment) -> {
              CustomNotificationModel customNotificationModel =
                  new CustomNotificationModel();
              String url;
              if (comment != null && comment.getUser() != null) {
                url = comment.getUser().getAvatarUrl();
                if (!InputHelper.isEmpty(
                        thread.getSubject().getLatestCommentUrl())) {
                  customNotificationModel.comment = comment;
                  customNotificationModel.url = url;
                }
              }
              customNotificationModel.notification = thread;
              return customNotificationModel;
            })
        .subscribeOn(Schedulers.io())
        .subscribe(
            custom
            -> {
              if (custom.comment != null) {
                getNotificationWithComment(context, accentColor,
                                           custom.notification, custom.comment,
                                           custom.url);
              } else {
                showNotificationWithoutComment(context, accentColor,
                                               custom.notification, custom.url);
              }
            },
            throwable
            -> finishJob(job),
            () -> {
              if (!NotificationQueue.exists(first.getId())) {
                android.app.Notification grouped = getSummaryGroupNotification(
                    first, accentColor, notificationThreadModels.size() > 1);
                showNotification(first.getId(), grouped);
              }
              NotificationQueue.put(notificationThreadModels)
                  .subscribe(aBoolean
                             -> {/*do nothing*/},
                             Throwable::printStackTrace, () -> finishJob(job));
            });
  }

  private void finishJob(final JobParameters job) { jobFinished(job, false); }

  private void showNotificationWithoutComment(final Context context,
                                              final int accentColor,
                                              final Notification thread,
                                              final String iconUrl) {
    withoutComments(thread, context, accentColor);
  }

  private void withoutComments(final Notification thread, final Context context,
                               final int accentColor) {
    android.app.Notification toAdd =
        getNotification(thread.getSubject().getTitle(),
                        thread.getRepository().getFullName(),
                        thread.getRepository() != null
                            ? thread.getRepository().getFullName()
                            : "general")
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                                       R.mipmap.ic_launcher))
            .setContentIntent(
                getPendingIntent(thread.getId(), thread.getSubject().getUrl()))
            .addAction(
                R.drawable.ic_github, context.getString(R.string.open),
                getPendingIntent(thread.getId(), thread.getSubject().getUrl()))
            .addAction(R.drawable.ic_eye_off,
                       context.getString(R.string.mark_as_read),
                       getReadOnlyPendingIntent(thread.getId(),
                                                thread.getSubject().getUrl()))
            .setWhen(thread.getUpdatedAt() != null
                         ? thread.getUpdatedAt().getTime()
                         : System.currentTimeMillis())
            .setShowWhen(true)
            .setColor(accentColor)
            .setGroup(NOTIFICATION_GROUP_ID)
            .build();
    showNotification(thread.getId(), toAdd);
  }

  private void getNotificationWithComment(final Context context,
                                          final int accentColor,
                                          final Notification thread,
                                          final Comment comment,
                                          final String url) {
    withComments(comment, context, thread, accentColor);
  }

  private void withComments(final Comment comment, final Context context,
                            final Notification thread, final int accentColor) {
    android.app.Notification toAdd =
        getNotification(comment.getUser() != null ? comment.getUser().getLogin()
                                                  : "",
                        MarkDownProvider.stripMdText(comment.getBody()),
                        thread.getRepository() != null
                            ? thread.getRepository().getFullName()
                            : "general")
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                                       R.mipmap.ic_launcher))
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(
                new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(comment.getUser() != null
                                            ? comment.getUser().getLogin()
                                            : "")
                    .bigText(MarkDownProvider.stripMdText(comment.getBody())))
            .setWhen(comment.getCreatedAt().getTime())
            .setShowWhen(true)
            .addAction(
                R.drawable.ic_github, context.getString(R.string.open),
                getPendingIntent(thread.getId(), thread.getSubject().getUrl()))
            .addAction(R.drawable.ic_eye_off,
                       context.getString(R.string.mark_as_read),
                       getReadOnlyPendingIntent(thread.getId(),
                                                thread.getSubject().getUrl()))
            .setContentIntent(
                getPendingIntent(thread.getId(), thread.getSubject().getUrl()))
            .setColor(accentColor)
            .setGroup(NOTIFICATION_GROUP_ID)
            .build();
    showNotification(thread.getId(), toAdd);
  }

  private android.app.Notification
  getSummaryGroupNotification(final @NonNull Notification thread,
                              final int accentColor,
                              final boolean toNotificationActivity) {
    PendingIntent pendingIntent = PendingIntent.getActivity(
        getApplicationContext(), 0,
        new Intent(getApplicationContext(), NotificationActivity.class),
        PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder builder =
        getNotification(thread.getSubject().getTitle(),
                        thread.getRepository().getFullName(),
                        thread.getRepository() != null
                            ? thread.getRepository().getFullName()
                            : "general")
            .setContentIntent(
                toNotificationActivity
                    ? pendingIntent
                    : getPendingIntent(thread.getId(),
                                       thread.getSubject().getUrl()))
            .addAction(
                R.drawable.ic_github, getString(R.string.open),
                getPendingIntent(thread.getId(), thread.getSubject().getUrl()))
            .addAction(R.drawable.ic_eye_off, getString(R.string.mark_as_read),
                       getReadOnlyPendingIntent(thread.getId(),
                                                thread.getSubject().getUrl()))
            .setWhen(thread.getUpdatedAt() != null
                         ? thread.getUpdatedAt().getTime()
                         : System.currentTimeMillis())
            .setShowWhen(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(accentColor)
            .setGroup(NOTIFICATION_GROUP_ID)
            .setGroupSummary(true);
    if (PrefGetter.isNotificationSoundEnabled()) {
      builder.setDefaults(NotificationCompat.DEFAULT_ALL)
          .setSound(PrefGetter.getNotificationSound(),
                    AudioManager.STREAM_NOTIFICATION);
    }
    return builder.build();
  }

  private NotificationCompat.Builder
  getNotification(final @NonNull String title, final @NonNull String message,
                  final @NonNull String channelName) {
    return new NotificationCompat.Builder(this, channelName)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true);
  }

  private void showNotification(final long id,
                                final android.app.Notification notification) {
    NotificationManager notificationManager =
        (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    if (notificationManager != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel notificationChannel = new NotificationChannel(
            notification.getChannelId(), notification.getChannelId(),
            NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setShowBadge(true);
        notificationManager.createNotificationChannel(notificationChannel);
      }
      notificationManager.notify(InputHelper.getSafeIntId(id), notification);
    }
  }

  private PendingIntent getReadOnlyPendingIntent(final long id,
                                                 final @NonNull String url) {
    Intent intent =
        ReadNotificationService.start(getApplicationContext(), id, url, true);
    return PendingIntent.getService(getApplicationContext(),
                                    InputHelper.getSafeIntId(id) / 2, intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private PendingIntent getPendingIntent(final long id,
                                         final @NonNull String url) {
    Intent intent =
        ReadNotificationService.start(getApplicationContext(), id, url);
    return PendingIntent.getService(getApplicationContext(),
                                    InputHelper.getSafeIntId(id), intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private static class CustomNotificationModel {
    public String url;
    public Notification notification;
    public Comment comment;
  }
}
