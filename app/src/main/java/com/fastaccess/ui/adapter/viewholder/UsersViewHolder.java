package com.fastaccess.ui.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.model.User;
import com.fastaccess.provider.scheme.LinkParserHelper;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 11 Nov 2016, 2:08 PM
 */

public class UsersViewHolder extends BaseViewHolder<User> {

@BindView(R.id.avatarLayout) AvatarLayout avatar;
@BindView(R.id.title) FontTextView title;
@BindView(R.id.date) FontTextView date;
private boolean isFilter;

private UsersViewHolder(final @NonNull View itemView, final @Nullable BaseRecyclerAdapter adapter, final boolean isFilter) {
	super(itemView, adapter);
	this.isFilter = isFilter;
}

public static UsersViewHolder newInstance(final @NonNull ViewGroup parent, final @Nullable BaseRecyclerAdapter adapter, final boolean isFilter) {
	return new UsersViewHolder(getView(parent, isFilter ? R.layout.users_small_row_item : R.layout.feeds_row_item), adapter, isFilter);
}

@Override public void onClick(final View v) {
	if (isFilter) {
		super.onClick(v);
	} else {
		avatar.findViewById(R.id.avatar).callOnClick();
	}
}

@Override public void bind(final @NonNull User user) {
}

public void bind(final @NonNull User user, final boolean isContributor) {
	avatar.setUrl(user.getAvatarUrl(), user.getLogin(), user.isOrganizationType(),
	              LinkParserHelper.isEnterprise(user.getHtmlUrl()));
	title.setText(user.getLogin());
	date.setVisibility(!isContributor ? View.GONE : View.VISIBLE);
	if (isContributor) {
		date.setText(String.format("%s (%s)", date.getResources().getString(R.string.commits), user.getContributions()));
	}
}
}
