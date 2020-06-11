package com.fastaccess.ui.modules.pinned.pullrequest;

import android.view.View;
import androidx.annotation.NonNull;
import com.fastaccess.data.dao.model.PinnedPullRequests;
import com.fastaccess.data.dao.model.PullRequest;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.provider.scheme.SchemeParser;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import java.util.ArrayList;

/**
 * Created by Kosh on 25 Mar 2017, 8:00 PM
 */

public class PinnedPullRequestPresenter
    extends BasePresenter<PinnedPullRequestMvp.View>
    implements PinnedPullRequestMvp.Presenter {
  private ArrayList<PullRequest> pullRequests = new ArrayList<>();

  @Override
  protected void onAttachView(final @NonNull PinnedPullRequestMvp.View view) {
    super.onAttachView(view);
    if (pullRequests.isEmpty()) {
      onReload();
    }
  }

  @NonNull
  @Override
  public ArrayList<PullRequest> getPinnedPullRequest() {
    return pullRequests;
  }

  @Override
  public void onReload() {
    manageDisposable(PinnedPullRequests.getMyPinnedPullRequests().subscribe(
        repos
        -> sendToView(view -> view.onNotifyAdapter(repos)),
        throwable -> sendToView(view -> view.onNotifyAdapter(null))));
  }

  @Override
  public void onItemClick(final int position, final View v,
                          final PullRequest item) {
    SchemeParser.launchUri(v.getContext(),
                           !InputHelper.isEmpty(item.getHtmlUrl())
                               ? item.getHtmlUrl()
                               : item.getUrl());
  }

  @Override
  public void onItemLongClick(final int position, final View v,
                              final PullRequest item) {
    if (getView() != null) {
      getView().onDeletePinnedPullRequest(item.getId(), position);
    }
  }
}
