package com.fastaccess.ui.modules.profile.org.teams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.TeamsModel;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.profile.org.teams.details.TeamPagerActivity;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

class OrgTeamPresenter extends BasePresenter<OrgTeamMvp.View> implements OrgTeamMvp.Presenter {

    private ArrayList<TeamsModel> users = new ArrayList<>();
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
        super.onError(throwable);
    }

    @Override public boolean onCallApi(final int page, final @Nullable String parameter) {
        if (parameter == null) {
            throw new NullPointerException("Username is null");
        }
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(OrgTeamMvp.View::hideProgress);
            return false;
        }
        makeRestCall(RestProvider.getOrgService(isEnterprise()).getOrgTeams(parameter, page),
        response -> {
            lastPage = response.getLast();
            sendToView(view -> view.onNotifyAdapter(response.getItems(), page));
        });
        return true;
    }

    @NonNull @Override public ArrayList<TeamsModel> getTeams() {
        return users;
    }

    @Override public void onWorkOffline(final @NonNull String login) {
        //TODO
    }

    @Override public void onItemClick(final int position, final View v, final TeamsModel item) {
        Logger.e(item.getUrl());
        TeamPagerActivity.startActivity(v.getContext(), item.getId(), item.getName());
    }

    @Override public void onItemLongClick(final int position, final View v, final TeamsModel item) { }
}
