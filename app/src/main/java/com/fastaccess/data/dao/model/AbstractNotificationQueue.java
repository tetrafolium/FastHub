package com.fastaccess.data.dao.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import com.fastaccess.App;
import com.fastaccess.helper.RxHelper;
import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;
import java.util.Date;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * Created by Kosh on 03.11.17.
 */

@Entity
@NoArgsConstructor
public abstract class AbstractNotificationQueue implements Parcelable {
  @Key long notificationId;
  Date date;

  public static boolean exists(final long notificationId) {
    return App.getInstance()
               .getDataStore()
               .toBlocking()
               .select(NotificationQueue.class)
               .where(NotificationQueue.NOTIFICATION_ID.eq(notificationId))
               .get()
               .firstOrNull() != null;
  }

  public static Observable<Boolean> put(final
                                        @Nullable List<Notification> models) {
    if (models == null || models.isEmpty()) {
      return Observable.empty();
    }
    return RxHelper.getObservable(Observable.fromPublisher(s -> {
      try {
        BlockingEntityStore<Persistable> dataStore =
            App.getInstance().getDataStore().toBlocking();
        dataStore.delete(NotificationQueue.class).get().value();
        for (Notification entity : models) {
          NotificationQueue notificationQueue = new NotificationQueue();
          notificationQueue.setNotificationId(entity.getId());
          notificationQueue.setDate(entity.getUpdatedAt());
          dataStore.insert(notificationQueue);
        }
        s.onNext(true);
      } catch (Exception e) {
        e.printStackTrace();
        s.onError(e);
      }
      s.onComplete();
    }));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeLong(this.notificationId);
    dest.writeLong(this.date != null ? this.date.getTime() : -1);
  }

  protected AbstractNotificationQueue(final Parcel in) {
    this.notificationId = in.readLong();
    long tmpDate = in.readLong();
    this.date = tmpDate == -1 ? null : new Date(tmpDate);
  }

  public static final Parcelable.Creator<NotificationQueue> CREATOR =
      new Parcelable.Creator<NotificationQueue>() {
        @Override
        public NotificationQueue createFromParcel(final Parcel source) {
          return new NotificationQueue(source);
        }

        @Override
        public NotificationQueue[] newArray(final int size) {
          return new NotificationQueue[size];
        }
      };
}
