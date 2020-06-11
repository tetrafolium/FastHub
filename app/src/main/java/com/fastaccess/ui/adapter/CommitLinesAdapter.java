package com.fastaccess.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ViewGroup;

import com.fastaccess.data.dao.CommitLinesModel;
import com.fastaccess.ui.adapter.viewholder.CommitLinesViewHolder;
import com.fastaccess.ui.adapter.viewholder.SimpleViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;

import java.util.List;

public class CommitLinesAdapter extends BaseRecyclerAdapter<CommitLinesModel, CommitLinesViewHolder,
	                                                    SimpleViewHolder.OnItemClickListener<CommitLinesModel> > {

public CommitLinesAdapter(final @NonNull List<CommitLinesModel> data, final @Nullable CommitLinesViewHolder.OnItemClickListener<CommitLinesModel> listener) {
	super(data, listener);
}

@Override protected CommitLinesViewHolder viewHolder(final ViewGroup parent, final int viewType) {
	return CommitLinesViewHolder.newInstance(parent, this);
}

@Override protected void onBindView(final CommitLinesViewHolder holder, final int position) {
	holder.bind(getItem(position));
}
}
