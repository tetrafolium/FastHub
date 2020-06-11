package com.fastaccess.ui.adapter;

import android.view.ViewGroup;

import com.fastaccess.data.dao.model.User;
import com.fastaccess.ui.adapter.viewholder.ProfileOrgsViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

/**
 * Created by Kosh on 21 May 2017, 12:30 PM
 */

public class ProfileOrgsAdapter extends BaseRecyclerAdapter<User, ProfileOrgsViewHolder, BaseViewHolder.OnItemClickListener<User>> {

    @Override protected ProfileOrgsViewHolder viewHolder(final ViewGroup parent, final int viewType) {
        return ProfileOrgsViewHolder.newInstance(parent);
    }

    @Override protected void onBindView(final ProfileOrgsViewHolder holder, final int position) {
        holder.bind(getItem(position));
    }
}
