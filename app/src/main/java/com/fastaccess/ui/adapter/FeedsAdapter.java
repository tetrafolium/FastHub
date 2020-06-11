package com.fastaccess.ui.adapter;

import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.fastaccess.data.dao.model.Event;
import com.fastaccess.ui.adapter.viewholder.FeedsViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class FeedsAdapter extends BaseRecyclerAdapter<Event, FeedsViewHolder, BaseViewHolder.OnItemClickListener<Event> > {

private boolean noImage;

public FeedsAdapter(final @NonNull ArrayList<Event> events) {
	this(events, false);
}

public FeedsAdapter(final @NonNull ArrayList<Event> events, final boolean noImage) {
	super(events);
	this.noImage = noImage;
}

@Override protected FeedsViewHolder viewHolder(final ViewGroup parent, final int viewType) {
	return new FeedsViewHolder(FeedsViewHolder.getView(parent, noImage), this);
}

@Override protected void onBindView(final FeedsViewHolder holder, final int position) {
	holder.bind(getItem(position));
}
}
