package com.fastaccess.ui.adapter;

import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.fastaccess.data.dao.model.Release;
import com.fastaccess.ui.adapter.viewholder.ReleasesViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class ReleasesAdapter extends BaseRecyclerAdapter<Release, ReleasesViewHolder, BaseViewHolder.OnItemClickListener<Release> > {

public ReleasesAdapter(final @NonNull List<Release> data) {
	super(data);
}

@Override protected ReleasesViewHolder viewHolder(final ViewGroup parent, final int viewType) {
	return ReleasesViewHolder.newInstance(parent, this);
}

@Override protected void onBindView(final ReleasesViewHolder holder, final int position) {
	holder.bind(getItem(position));
}
}
