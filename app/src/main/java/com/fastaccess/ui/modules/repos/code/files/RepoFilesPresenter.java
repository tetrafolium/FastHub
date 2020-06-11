package com.fastaccess.ui.modules.repos.code.files;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.fastaccess.R;
import com.fastaccess.data.dao.CommitRequestModel;
import com.fastaccess.data.dao.RepoPathsManager;
import com.fastaccess.data.dao.model.RepoFile;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.repos.code.commit.history.FileCommitHistoryActivity;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 15 Feb 2017, 10:10 PM
 */

class RepoFilesPresenter
    extends BasePresenter<RepoFilesMvp.View> implements RepoFilesMvp.Presenter {
  private ArrayList<RepoFile> files = new ArrayList<>();
  private RepoPathsManager pathsModel = new RepoPathsManager();
  @com.evernote.android.state.State String repoId;
  @com.evernote.android.state.State String login;
  @com.evernote.android.state.State String path;
  @com.evernote.android.state.State String ref;

  @Override
  public void onItemClick(final int position, final View v,
                          final RepoFile item) {
    if (getView() == null)
      return;
    if (v.getId() != R.id.menu) {
      getView().onItemClicked(item);
    } else {
      getView().onMenuClicked(position, item, v);
    }
  }

  @Override
  public void onItemLongClick(final int position, final View v,
                              final RepoFile item) {
    FileCommitHistoryActivity.Companion.startActivity(
        v.getContext(), login, repoId, ref, item.getPath(), isEnterprise());
  }

  @Override
  public void onError(final @NonNull Throwable throwable) {
    onWorkOffline();
    super.onError(throwable);
  }

  @NonNull
  @Override
  public ArrayList<RepoFile> getFiles() {
    return files;
  }

  @Override
  public void onWorkOffline() {
    if ((repoId == null || login == null) || !files.isEmpty())
      return;
    manageDisposable(
        RxHelper.getObservable(RepoFile.getFiles(login, repoId).toObservable())
            .flatMap(response -> {
              if (response != null) {
                return Observable.fromIterable(response)
                    .filter(repoFile
                            -> repoFile != null && repoFile.getType() != null)
                    .sorted((repoFile, repoFile2)
                                -> repoFile2.getType().compareTo(
                                    repoFile.getType()));
              }
              return Observable.empty();
            })
            .toList()
            .subscribe(models -> {
              files.addAll(models);
              sendToView(RepoFilesMvp.View::onNotifyAdapter);
            }));
  }

  @Override
  public void onCallApi(final @Nullable RepoFile toAppend) {
    if (repoId == null || login == null)
      return;
    makeRestCall(RestProvider.getRepoService(isEnterprise())
                     .getRepoFiles(login, repoId, path, ref)
                     .flatMap(response -> {
                       if (response != null && response.getItems() != null) {
                         return Observable.fromIterable(response.getItems())
                             .filter(repoFile -> repoFile.getType() != null)
                             .sorted((repoFile, repoFile2)
                                         -> repoFile2.getType().compareTo(
                                             repoFile.getType()));
                       }
                       return Observable.empty();
                     })
                     .toList()
                     .toObservable(),
                 response -> {
                   files.clear();
                   if (response != null) {
                     manageObservable(RepoFile.save(response, login, repoId));
                     pathsModel.setFiles(ref, path, response);
                     files.addAll(response);
                   }
                   sendToView(view -> {
                     view.onNotifyAdapter();
                     view.onUpdateTab(toAppend);
                   });
                 });
  }

  @Override
  public void
  onInitDataAndRequest(final @NonNull String login,
                       final @NonNull String repoId, final @NonNull String path,
                       final @NonNull String ref, final boolean clear,
                       final @Nullable RepoFile toAppend) {
    if (clear)
      pathsModel.clear();
    this.login = login;
    this.repoId = repoId;
    this.ref = ref;
    this.path = path;
    List<RepoFile> cachedFiles = getCachedFiles(path, ref);
    if (cachedFiles != null && !cachedFiles.isEmpty()) {
      files.clear();
      files.addAll(cachedFiles);
      sendToView(view -> {
        view.onNotifyAdapter();
        view.onUpdateTab(toAppend);
      });
    } else {
      onCallApi(toAppend);
    }
  }

  @Nullable
  @Override
  public List<RepoFile> getCachedFiles(final @NonNull String url,
                                       final @NonNull String ref) {
    return pathsModel.getPaths(url, ref);
  }

  @Override
  public void onDeleteFile(final @NonNull String message,
                           final @NonNull RepoFile item,
                           final @NonNull String branch) {
    CommitRequestModel body =
        new CommitRequestModel(message, null, item.getSha(), branch);
    makeRestCall(
        RestProvider.getContentService(isEnterprise())
            .deleteFile(login, repoId, item.getPath(), ref, body),
        gitCommitModel
        -> sendToView(SwipeRefreshLayout.OnRefreshListener::onRefresh));
  }
}
