package com.fastaccess.ui.modules.repos.issues.issue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.fastaccess.R;
import com.fastaccess.data.dao.PullsIssuesParser;
import com.fastaccess.data.dao.model.Issue;
import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.provider.rest.loadmore.OnLoadMore;
import com.fastaccess.ui.adapter.IssuesAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.modules.repos.RepoPagerMvp;
import com.fastaccess.ui.modules.repos.extras.popup.IssuePopupFragment;
import com.fastaccess.ui.modules.repos.issues.RepoIssuesPagerMvp;
import com.fastaccess.ui.modules.repos.issues.create.CreateIssueActivity;
import com.fastaccess.ui.modules.repos.issues.issue.details.IssuePagerActivity;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;
import com.fastaccess.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller;
import java.util.List;

/**
 * Created by Kosh on 03 Dec 2016, 3:56 PM
 */

public class RepoOpenedIssuesFragment
    extends BaseFragment<RepoIssuesMvp.View, RepoIssuesPresenter>
    implements RepoIssuesMvp.View {
  @BindView(R.id.recycler) DynamicRecyclerView recycler;
  @BindView(R.id.refresh) SwipeRefreshLayout refresh;
  @BindView(R.id.stateLayout) StateLayout stateLayout;
  @BindView(R.id.fastScroller) RecyclerViewFastScroller fastScroller;
  private OnLoadMore<IssueState> onLoadMore;
  private IssuesAdapter adapter;
  private RepoIssuesPagerMvp.View pagerCallback;
  private RepoPagerMvp.TabsBadgeListener tabsBadgeListener;

  public static RepoOpenedIssuesFragment
  newInstance(final @NonNull String repoId, final @NonNull String login) {
    RepoOpenedIssuesFragment view = new RepoOpenedIssuesFragment();
    view.setArguments(Bundler.start()
                          .put(BundleConstant.ID, repoId)
                          .put(BundleConstant.EXTRA, login)
                          .end());
    return view;
  }

  @Override
  public void onAttach(final Context context) {
    super.onAttach(context);
    if (getParentFragment() instanceof RepoIssuesPagerMvp.View) {
      pagerCallback = (RepoIssuesPagerMvp.View)getParentFragment();
    } else if (context instanceof RepoIssuesPagerMvp.View) {
      pagerCallback = (RepoIssuesPagerMvp.View)context;
    }
    if (getParentFragment() instanceof RepoPagerMvp.TabsBadgeListener) {
      tabsBadgeListener = (RepoPagerMvp.TabsBadgeListener)getParentFragment();
    } else if (context instanceof RepoPagerMvp.TabsBadgeListener) {
      tabsBadgeListener = (RepoPagerMvp.TabsBadgeListener)context;
    }
  }

  @Override
  public void onDetach() {
    pagerCallback = null;
    tabsBadgeListener = null;
    super.onDetach();
  }

  @Override
  public void onNotifyAdapter(final @Nullable List<Issue> items,
                              final int page) {
    hideProgress();
    if (items == null || items.isEmpty()) {
      adapter.clear();
      return;
    }
    if (page <= 1) {
      adapter.insertItems(items);
    } else {
      adapter.addItems(items);
    }
  }

  @Override
  protected int fragmentLayout() {
    return R.layout.micro_grid_refresh_list;
  }

  @Override
  protected void onFragmentCreated(final @NonNull View view,
                                   final @Nullable Bundle savedInstanceState) {
    if (getArguments() == null) {
      throw new NullPointerException(
          "Bundle is null, therefore, issues can't be proceeded.");
    }
    stateLayout.setEmptyText(R.string.no_issues);
    recycler.setEmptyView(stateLayout, refresh);
    stateLayout.setOnReloadListener(this);
    refresh.setOnRefreshListener(this);
    adapter = new IssuesAdapter(getPresenter().getIssues(), true);
    adapter.setListener(getPresenter());
    getLoadMore().initialize(getPresenter().getCurrentPage(),
                             getPresenter().getPreviousTotal());
    recycler.setAdapter(adapter);
    recycler.addKeyLineDivider();
    recycler.addOnScrollListener(getLoadMore());
    if (savedInstanceState == null) {
      getPresenter().onFragmentCreated(getArguments(), IssueState.open);
    } else if (getPresenter().getIssues().isEmpty() &&
               !getPresenter().isApiCalled()) {
      onRefresh();
    }
    fastScroller.attachRecyclerView(recycler);
  }

  @Override
  public void onActivityResult(final int requestCode, final int resultCode,
                               final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == BundleConstant.REQUEST_CODE) {
        onRefresh();
        if (pagerCallback != null)
          pagerCallback.setCurrentItem(0, false);
      } else if (requestCode == RepoIssuesMvp.ISSUE_REQUEST_CODE &&
                 data != null) {
        boolean isClose = data.getExtras().getBoolean(BundleConstant.EXTRA);
        boolean isOpened =
            data.getExtras().getBoolean(BundleConstant.EXTRA_TWO);
        if (isClose) {
          if (pagerCallback != null)
            pagerCallback.setCurrentItem(1, true);
          onRefresh();
        } else if (isOpened) {
          onRefresh();
        } // else ignore!
      }
    }
  }

  @NonNull
  @Override
  public RepoIssuesPresenter providePresenter() {
    return new RepoIssuesPresenter();
  }

  @Override
  public void hideProgress() {
    refresh.setRefreshing(false);
    stateLayout.hideProgress();
  }

  @Override
  public void showProgress(final @StringRes int resId) {

    refresh.setRefreshing(true);

    stateLayout.showProgress();
  }

  @Override
  public void showErrorMessage(final @NonNull String message) {
    showReload();
    super.showErrorMessage(message);
  }

  @Override
  public void showMessage(final int titleRes, final int msgRes) {
    showReload();
    super.showMessage(titleRes, msgRes);
  }

  @NonNull
  @Override
  public OnLoadMore<IssueState> getLoadMore() {
    if (onLoadMore == null) {
      onLoadMore = new OnLoadMore<IssueState>(getPresenter()) {
        @Override
        public void onScrolled(final boolean isUp) {
          super.onScrolled(isUp);
          if (pagerCallback != null)
            pagerCallback.onScrolled(isUp);
        }
      };
    }
    onLoadMore.setParameter(IssueState.open);
    return onLoadMore;
  }

  @Override
  public void onAddIssue() {
    String login = getPresenter().login();
    String repoId = getPresenter().repoId();
    if (!InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
      CreateIssueActivity.startForResult(this, login, repoId, isEnterprise());
    }
  }

  @Override
  public void onUpdateCount(final int totalCount) {
    if (tabsBadgeListener != null)
      tabsBadgeListener.onSetBadge(0, totalCount);
  }

  @Override
  public void onOpenIssue(final @NonNull PullsIssuesParser parser) {
    startActivityForResult(
        IssuePagerActivity.createIntent(getContext(), parser.getRepoId(),
                                        parser.getLogin(), parser.getNumber(),
                                        false, isEnterprise()),
        RepoIssuesMvp.ISSUE_REQUEST_CODE);
  }

  @Override
  public void onRefresh(final boolean isLastUpdated) {
    getPresenter().onSetSortBy(isLastUpdated);
    getPresenter().onCallApi(1, IssueState.open);
  }

  @Override
  public void onShowIssuePopup(final @NonNull Issue item) {
    IssuePopupFragment.showPopup(getChildFragmentManager(), item);
  }

  @Override
  public void onRefresh() {
    getPresenter().onCallApi(1, IssueState.open);
  }

  @Override
  public void onClick(final View view) {
    onRefresh();
  }

  @Override
  public void onScrollTop(final int index) {
    super.onScrollTop(index);
    if (recycler != null)
      recycler.scrollToPosition(0);
  }

  private void showReload() {
    hideProgress();
    stateLayout.showReload(adapter.getItemCount());
  }
}
