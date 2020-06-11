package com.fastaccess.provider.tasks.git;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.fastaccess.R;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.provider.rest.RestProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Kosh on 12 Mar 2017, 2:25 PM
 */

public class GithubActionService extends IntentService {

public static final int STAR_REPO = 1;
public static final int UNSTAR_REPO = 2;
public static final int FORK_REPO = 3;
public static final int WATCH_REPO = 4;
public static final int UNWATCH_REPO = 5;
public static final int STAR_GIST = 6;
public static final int UNSTAR_GIST = 7;
public static final int FORK_GIST = 8;
private NotificationCompat.Builder notification;
private NotificationManager notificationManager;
private CompositeDisposable disposable = new CompositeDisposable();

@IntDef({
		STAR_REPO,
		UNSTAR_REPO,
		FORK_REPO,
		WATCH_REPO,
		UNWATCH_REPO,
		STAR_GIST,
		UNSTAR_GIST,
		FORK_GIST,
	})
@Retention(RetentionPolicy.SOURCE) @interface GitActionType { }

public static void startForRepo(final @NonNull Context context, final @NonNull String login, final @NonNull String repo,
                                final @GitActionType int type, final boolean isEnterprise) {
	Intent intent = new Intent(context.getApplicationContext(), GithubActionService.class);
	intent.putExtras(Bundler.start()
	                 .put(BundleConstant.ID, repo)
	                 .put(BundleConstant.EXTRA, login)
	                 .put(BundleConstant.EXTRA_TYPE, type)
	                 .put(BundleConstant.IS_ENTERPRISE, isEnterprise)
	                 .end());
	context.startService(intent);
}

public static void startForGist(final @NonNull Context context, final @NonNull String id, final @GitActionType int type, final boolean isEnterprise) {
	Intent intent = new Intent(context.getApplicationContext(), GithubActionService.class);
	intent.putExtras(Bundler.start()
	                 .put(BundleConstant.ID, id)
	                 .put(BundleConstant.EXTRA_TYPE, type)
	                 .put(BundleConstant.IS_ENTERPRISE, isEnterprise)
	                 .end());
	context.startService(intent);
}

public GithubActionService() {
	super(GithubActionService.class.getName());
}

@Override protected void onHandleIntent(final @Nullable Intent intent) {
	if (intent != null && intent.getExtras() != null) {
		Bundle bundle = intent.getExtras();
		@GitActionType int type = bundle.getInt(BundleConstant.EXTRA_TYPE);
		String id = bundle.getString(BundleConstant.ID);
		String login = bundle.getString(BundleConstant.EXTRA);
		boolean isEnterprise = bundle.getBoolean(BundleConstant.IS_ENTERPRISE);
		switch (type) {
		case FORK_GIST:
			forkGist(id, isEnterprise);
			break;
		case FORK_REPO:
			forkRepo(id, login, isEnterprise);
			break;
		case STAR_GIST:
			starGist(id, isEnterprise);
			break;
		case STAR_REPO:
			starRepo(id, login, isEnterprise);
			break;
		case UNSTAR_GIST:
			unStarGist(id, isEnterprise);
			break;
		case UNSTAR_REPO:
			unStarRepo(id, login, isEnterprise);
			break;
		case UNWATCH_REPO:
			unWatchRepo(id, login, isEnterprise);
			break;
		case WATCH_REPO:
			watchRepo(id, login, isEnterprise);
			break;
		}
	}
}

@Override public void onDestroy() {
	disposable.clear();
	super.onDestroy();
}

private void forkGist(final @Nullable String id, final boolean isEnterprise) {
	if (id != null) {
		String msg = getString(R.string.forking, getString(R.string.gist));
		disposable.add(
			RestProvider.getGistService(isEnterprise)
			.forkGist(id)
			.doOnSubscribe(disposable->showNotification(msg))
			.subscribeOn(Schedulers.io())
			.subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
			);
	}
}

private void forkRepo(final @Nullable String id, final @Nullable String login, final boolean isEnterprise) {
	if (id != null && login != null) {
		String msg = getString(R.string.forking, id);
		disposable.add(
			RestProvider.getRepoService(isEnterprise)
			.forkRepo(login, id)
			.doOnSubscribe(disposable->showNotification(msg))
			.subscribeOn(Schedulers.io())
			.subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
			);
	}
}

private void starGist(final @Nullable String id, final boolean isEnterprise) {
	if (id != null) {
		String msg = getString(R.string.starring, getString(R.string.gist));
		disposable.add(RestProvider.getGistService(isEnterprise)
		               .starGist(id)
		               .doOnSubscribe(disposable->showNotification(msg))
		               .subscribeOn(Schedulers.io())
		               .subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
		               );
	}
}

private void starRepo(final @Nullable String id, final @Nullable String login, final boolean isEnterprise) {
	if (id != null && login != null) {
		String msg = getString(R.string.starring, id);
		disposable.add(RestProvider.getRepoService(isEnterprise)
		               .starRepo(login, id)
		               .doOnSubscribe(disposable->showNotification(msg))
		               .subscribeOn(Schedulers.io())
		               .subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
		               );
	}
}

private void unStarGist(final @Nullable String id, final boolean isEnterprise) {
	if (id != null) {
		String msg = getString(R.string.un_starring, getString(R.string.gist));
		disposable.add(RestProvider.getGistService(isEnterprise)
		               .unStarGist(id)
		               .doOnSubscribe(disposable->showNotification(msg))
		               .subscribeOn(Schedulers.io())
		               .subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
		               );
	}
}

private void unStarRepo(final @Nullable String id, final @Nullable String login, final boolean isEnterprise) {
	if (id != null && login != null) {
		String msg = getString(R.string.un_starring, id);
		disposable.add(RestProvider.getRepoService(isEnterprise)
		               .unstarRepo(login, id)
		               .doOnSubscribe(disposable->showNotification(msg))
		               .subscribeOn(Schedulers.io())
		               .subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
		               );
	}
}

private void unWatchRepo(final @Nullable String id, final @Nullable String login, final boolean isEnterprise) {
	if (id != null && login != null) {
		String msg = getString(R.string.un_watching, id);
		disposable.add(RestProvider.getRepoService(isEnterprise)
		               .unwatchRepo(login, id)
		               .doOnSubscribe(disposable->showNotification(msg))
		               .subscribeOn(Schedulers.io())
		               .subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
		               );
	}
}

private void watchRepo(final @Nullable String id, final @Nullable String login, final boolean isEnterprise) {
	if (id != null && login != null) {
		String msg = getString(R.string.watching, id);
		disposable.add(RestProvider.getRepoService(isEnterprise)
		               .watchRepo(login, id)
		               .doOnSubscribe(disposable->showNotification(msg))
		               .subscribeOn(Schedulers.io())
		               .subscribe(response->{
			}, throwable->hideNotification(msg), ()->hideNotification(msg))
		               );
	}
}

private NotificationCompat.Builder getNotification(final @NonNull String title) {
	if (notification == null) {
		notification = new NotificationCompat.Builder(this, title)
		               .setSmallIcon(R.drawable.ic_sync)
		               .setProgress(0, 100, true);
	}
	notification.setContentTitle(title);
	return notification;
}

private NotificationManager getNotificationManager() {
	if (notificationManager == null) {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	return notificationManager;
}

private void showNotification(final @NonNull String msg) {
	getNotificationManager().notify(msg.hashCode(), getNotification(msg).build());
}

private void hideNotification(final @NonNull String msg) {
	getNotificationManager().cancel(msg.hashCode());
}
}
