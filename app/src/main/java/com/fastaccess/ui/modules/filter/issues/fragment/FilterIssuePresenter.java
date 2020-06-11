package com.fastaccess.ui.modules.filter.issues.fragment;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fastaccess.data.dao.model.Issue;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import java.util.ArrayList;

/**
 * Created by Kosh on 09 Apr 2017, 7:10 PM
 */

public class FilterIssuePresenter extends BasePresenter<FilterIssuesMvp.View>
    implements FilterIssuesMvp.Presenter {

  private ArrayList<Issue> issues = new ArrayList<>();
  private int page;
  private int previousTotal;
  private int lastPage = Integer.MAX_VALUE;

  @Override
  public void onItemClick(final int position, final View v, final Issue item) {
    if (getView() != null) {
      getView().onItemClicked(item);
    }
  }

  @Override
  public void onItemLongClick(final int position, final View v,
                              final Issue item) {
    if (getView() != null)
      getView().onShowPopupDetails(item);
  }

  @NonNull
  @Override
  public ArrayList<Issue> getIssues() {
    return issues;
  }

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
  public boolean onCallApi(final int page, final @Nullable String parameter) {
    if (page == 1 || parameter == null) {
      lastPage = Integer.MAX_VALUE;
      sendToView(view -> view.getLoadMore().reset());
    }
    if (page > lastPage || lastPage == 0 || parameter == null) {
      sendToView(FilterIssuesMvp.View::hideProgress);
      return false;
    }
    setCurrentPage(page);
    makeRestCall(
        RestProvider.getSearchService(isEnterprise())
            .searchIssues(parameter, page),
        issues -> {
          lastPage = issues.getLast();
          if (getCurrentPage() == 1) {
            sendToView(view -> view.onSetCount(issues.getTotalCount()));
          }
          sendToView(view -> view.onNotifyAdapter(issues.getItems(), page));
        });
    return true;
  }
}
