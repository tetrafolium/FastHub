package com.fastaccess.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ViewGroup;

import com.fastaccess.data.dao.ReviewCommentModel;
import com.fastaccess.ui.adapter.callback.OnToggleView;
import com.fastaccess.ui.adapter.callback.ReactionsCallback;
import com.fastaccess.ui.adapter.viewholder.ReviewCommentsViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class ReviewCommentsAdapter extends BaseRecyclerAdapter<ReviewCommentModel, ReviewCommentsViewHolder, BaseViewHolder
	                                                       .OnItemClickListener<ReviewCommentModel> > {

private final OnToggleView onToggleView;
private final ReactionsCallback reactionsCallback;
private final String repoOwner;
private final String poster;

public ReviewCommentsAdapter(final @NonNull List<ReviewCommentModel> data,
                             final @Nullable BaseViewHolder.OnItemClickListener<ReviewCommentModel> listener,
                             final OnToggleView onToggleView, final ReactionsCallback reactionsCallback, final String repoOwner, final String poster) {
	super(data, listener);
	this.onToggleView = onToggleView;
	this.reactionsCallback = reactionsCallback;
	this.repoOwner = repoOwner;
	this.poster = poster;
}


@Override protected ReviewCommentsViewHolder viewHolder(final ViewGroup parent, final int viewType) {
	return ReviewCommentsViewHolder.newInstance(parent, this, onToggleView,
	                                            reactionsCallback, repoOwner, poster);
}

@Override protected void onBindView(final ReviewCommentsViewHolder holder, final int position) {
	holder.bind(getItem(position));
}
}
