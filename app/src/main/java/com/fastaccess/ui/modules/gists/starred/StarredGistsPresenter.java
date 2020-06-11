package com.fastaccess.ui.modules.gists.starred;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.model.Gist;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.provider.scheme.SchemeParser;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Kosh on 11 Nov 2016, 12:36 PM
 */

class StarredGistsPresenter extends BasePresenter<StarredGistsMvp.View> implements StarredGistsMvp.Presenter {
    private ArrayList<Gist> gistsModels = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;

    @Override public int getCurrentPage() {
        return page;
    }

    @Override public int getPreviousTotal() {
        return previousTotal;
    }

    @Override public void setCurrentPage(final int page) {
        this.page = page;
    }

    @Override public void setPreviousTotal(final int previousTotal) {
        this.previousTotal = previousTotal;
    }

    @Override public void onError(final @NonNull Throwable throwable) {
        sendToView(view -> {
            if (view.getLoadMore().getParameter() != null) {
                onWorkOffline(view.getLoadMore().getParameter());
            }
        });
        super.onError(throwable);
    }

    @Override public boolean onCallApi(final int page, final @Nullable String parameter) {
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(StarredGistsMvp.View::hideProgress);
            return false;
        }
        makeRestCall(RestProvider.getGistService(isEnterprise()).getStarredGists(page),
        listResponse -> {
            lastPage = listResponse.getLast();
            sendToView(view -> view.onNotifyAdapter(listResponse.getItems(), page));
        });
        return true;
    }

    @NonNull @Override public ArrayList<Gist> getGists() {
        return gistsModels;
    }

    @Override public void onWorkOffline(final @NonNull String login) { } // do nothing for now.

    @Override public void onItemClick(final int position, final View v, final Gist item) {
        SchemeParser.launchUri(v.getContext(), item.getHtmlUrl());
    }

    @Override public void onItemLongClick(final int position, final View v, final Gist item) { }
}
