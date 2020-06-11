package com.fastaccess.ui.modules.pinned.issue;

import android.view.View;
import androidx.annotation.NonNull;
import com.fastaccess.data.dao.model.Issue;
import com.fastaccess.data.dao.model.PinnedIssues;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.provider.scheme.SchemeParser;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import java.util.ArrayList;

/**
 * Created by Kosh on 25 Mar 2017, 8:00 PM
 */

public class PinnedIssuePresenter extends BasePresenter<PinnedIssueMvp.View>
    implements PinnedIssueMvp.Presenter {
  private ArrayList<Issue> issues = new ArrayList<>();

  @Override
  protected void onAttachView(final @NonNull PinnedIssueMvp.View view) {
    super.onAttachView(view);
    if (issues.isEmpty()) {
      onReload();
    }
  }

  @NonNull
  @Override
  public ArrayList<Issue> getPinnedIssue() {
    return issues;
  }

  @Override
  public void onReload() {
    manageDisposable(PinnedIssues.getMyPinnedIssues().subscribe(
        repos
        -> sendToView(view -> view.onNotifyAdapter(repos)),
        throwable -> sendToView(view -> view.onNotifyAdapter(null))));
  }

  @Override
  public void onItemClick(final int position, final View v, final Issue item) {
    SchemeParser.launchUri(v.getContext(),
                           !InputHelper.isEmpty(item.getHtmlUrl())
                               ? item.getHtmlUrl()
                               : item.getUrl());
  }

  @Override
  public void onItemLongClick(final int position, final View v,
                              final Issue item) {
    if (getView() != null) {
      getView().onDeletePinnedIssue(item.getId(), position);
    }
  }
}
