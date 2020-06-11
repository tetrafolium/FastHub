package com.fastaccess.ui.modules.profile.followers;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fastaccess.data.dao.model.User;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

class ProfileFollowersPresenter extends BasePresenter<ProfileFollowersMvp.View>
    implements ProfileFollowersMvp.Presenter {

  private ArrayList<User> users = new ArrayList<>();
  private int page;
  private int previousTotal;
  private int lastPage = Integer.MAX_VALUE;

  @Override
  public int getCurrentPage() {
    return page;
  }

  @Override
  public int getPreviousTotal() {
    return previousTotal;
  }

  @Override
  public void setCurrentPage(final int page) {
    this.page = page;
  }

  @Override
  public void setPreviousTotal(final int previousTotal) {
    this.previousTotal = previousTotal;
  }

  @Override
  public void onError(final @NonNull Throwable throwable) {
    sendToView(view -> {
      if (view.getLoadMore().getParameter() != null) {
        onWorkOffline(view.getLoadMore().getParameter());
      }
    });
    super.onError(throwable);
  }

  @Override
  public boolean onCallApi(final int page, final @Nullable String parameter) {
    if (parameter == null) {
      throw new NullPointerException("Username is null");
    }
    if (page == 1) {
      lastPage = Integer.MAX_VALUE;
      sendToView(view -> view.getLoadMore().reset());
    }
    setCurrentPage(page);
    if (page > lastPage || lastPage == 0) {
      sendToView(ProfileFollowersMvp.View::hideProgress);
      return false;
    }
    makeRestCall(
        RestProvider.getUserService(isEnterprise())
            .getFollowers(parameter, page),
        response -> {
          lastPage = response.getLast();
          if (getCurrentPage() == 1) {
            manageDisposable(
                User.saveUserFollowerList(response.getItems(), parameter));
          }
          sendToView(view -> view.onNotifyAdapter(response.getItems(), page));
        });
    return true;
  }

  @NonNull
  @Override
  public ArrayList<User> getFollowers() {
    return users;
  }

  @Override
  public void onWorkOffline(final @NonNull String login) {
    if (users.isEmpty()) {
      manageDisposable(
          RxHelper.getSingle(User.getUserFollowerList(login))
              .subscribe(
                  userModels
                  -> sendToView(view -> view.onNotifyAdapter(userModels, 1))));
    } else {
      sendToView(ProfileFollowersMvp.View::hideProgress);
    }
  }

  @Override
  public void onItemClick(final int position, final View v, final User item) {}

  @Override
  public void onItemLongClick(final int position, final View v,
                              final User item) {}
}
