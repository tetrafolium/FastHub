package com.fastaccess.ui.modules.profile.org.teams.details.members;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.model.User;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

class TeamMembersPresenter extends BasePresenter<TeamMembersMvp.View> implements TeamMembersMvp.Presenter {

private ArrayList<User> users = new ArrayList<>();
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

@Override public boolean onCallApi(final int page, final @Nullable Long parameter) {
	if (parameter == null) {
		throw new NullPointerException("Username is null");
	}
	if (page == 1) {
		lastPage = Integer.MAX_VALUE;
		sendToView(view->view.getLoadMore().reset());
	}
	setCurrentPage(page);
	if (page > lastPage || lastPage == 0) {
		sendToView(TeamMembersMvp.View::hideProgress);
		return false;
	}
	makeRestCall(RestProvider.getOrgService(isEnterprise()).getTeamMembers(parameter, page),
	             response->{
			lastPage = response.getLast();
			sendToView(view->view.onNotifyAdapter(response.getItems(), page));
		});
	return true;
}

@NonNull @Override public ArrayList<User> getFollowers() {
	return users;
}

@Override public void onWorkOffline(final @NonNull String login) {
	//TODO
}

@Override public void onItemClick(final int position, final View v, final User item) {
}

@Override public void onItemLongClick(final int position, final View v, final User item) {
}
}
