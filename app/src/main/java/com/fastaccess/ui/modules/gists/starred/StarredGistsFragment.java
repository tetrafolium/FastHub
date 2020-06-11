package com.fastaccess.ui.modules.gists.starred;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.fastaccess.R;
import com.fastaccess.data.dao.model.Gist;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.provider.rest.loadmore.OnLoadMore;
import com.fastaccess.ui.adapter.GistsAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.modules.gists.gist.GistActivity;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;
import com.fastaccess.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller;
import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 12:36 PM
 */

public class StarredGistsFragment
    extends BaseFragment<StarredGistsMvp.View, StarredGistsPresenter>
    implements StarredGistsMvp.View {

  @BindView(R.id.recycler) DynamicRecyclerView recycler;
  @BindView(R.id.refresh) SwipeRefreshLayout refresh;
  @BindView(R.id.stateLayout) StateLayout stateLayout;
  @BindView(R.id.fastScroller) RecyclerViewFastScroller fastScroller;

  private GistsAdapter adapter;
  private OnLoadMore<String> onLoadMore;

  public static StarredGistsFragment newInstance() {
    return new StarredGistsFragment();
  }

  @Override
  protected int fragmentLayout() {
    return R.layout.small_grid_refresh_list;
  }

  @Override
  protected void onFragmentCreated(final @NonNull View view,
                                   final @Nullable Bundle savedInstanceState) {
    stateLayout.setEmptyText(R.string.no_gists);
    refresh.setOnRefreshListener(this);
    stateLayout.setOnReloadListener(this);
    recycler.setEmptyView(stateLayout, refresh);
    adapter = new GistsAdapter(getPresenter().getGists(), true);
    adapter.setListener(getPresenter());
    getLoadMore().initialize(getPresenter().getCurrentPage(),
                             getPresenter().getPreviousTotal());
    recycler.setAdapter(adapter);
    recycler.addOnScrollListener(getLoadMore());
    recycler.addDivider();
    if (getPresenter().getGists().isEmpty() && !getPresenter().isApiCalled()) {
      onRefresh();
    }
    fastScroller.attachRecyclerView(recycler);
  }

  @Override
  public void onRefresh() {
    getPresenter().onCallApi(1, null);
  }

  @Override
  public void onNotifyAdapter(final @Nullable List<Gist> items,
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
  public void showProgress(final @StringRes int resId) {
    refresh.setRefreshing(true);
    stateLayout.showProgress();
  }

  @Override
  public void hideProgress() {
    refresh.setRefreshing(false);
    stateLayout.hideProgress();
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
  public StarredGistsPresenter providePresenter() {
    return new StarredGistsPresenter();
  }

  @NonNull
  @Override
  public OnLoadMore<String> getLoadMore() {
    if (onLoadMore == null) {
      onLoadMore = new OnLoadMore<>(getPresenter(), null);
    }
    return onLoadMore;
  }

  @Override
  public void onStartGistView(final @NonNull String gistId) {
    startActivityForResult(
        GistActivity.createIntent(getContext(), gistId, isEnterprise()),
        BundleConstant.REQUEST_CODE);
  }

  @Override
  public void onActivityResult(final int requestCode, final int resultCode,
                               final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK &&
        requestCode == BundleConstant.REQUEST_CODE) {
      if (data != null && data.getExtras() != null) {
        Gist gistsModel = data.getExtras().getParcelable(BundleConstant.ITEM);
        if (gistsModel != null && adapter != null) {
          adapter.removeItem(gistsModel);
        }
      } else {
        onRefresh();
      }
    }
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
