package com.fastaccess.ui.widgets.recyclerview;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Kosh on 17 May 2016, 7:13 PM
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

public interface OnItemClickListener<T> {
void onItemClick(int position, View v, T item);

void onItemLongClick(int position, View v, T item);
}

@Nullable protected final BaseRecyclerAdapter adapter;

public static View getView(final @NonNull ViewGroup parent, final @LayoutRes int layoutRes) {
	return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
}

protected BaseViewHolder(final @NonNull View itemView) {
	this(itemView, null);
}

public BaseViewHolder(final @NonNull View itemView, final @Nullable BaseRecyclerAdapter adapter) {
	super(itemView);
	ButterKnife.bind(this, itemView);
	this.adapter = adapter;
	itemView.setOnClickListener(this);
	itemView.setOnLongClickListener(this);
}

@SuppressWarnings("unchecked") @Override public void onClick(final View v) {
	if (adapter != null && adapter.getListener() != null) {
		int position = getAdapterPosition();
		if (position != RecyclerView.NO_POSITION && position < adapter.getItemCount())
			adapter.getListener().onItemClick(position, v, adapter.getItem(position));
	}
}

@SuppressWarnings("unchecked") @Override public boolean onLongClick(final View v) {
	if (adapter != null && adapter.getListener() != null) {
		int position = getAdapterPosition();
		if (position != RecyclerView.NO_POSITION && position < adapter.getItemCount())
			adapter.getListener().onItemLongClick(position, v, adapter.getItem(position));
	}
	return true;
}

public abstract void bind(@NonNull T t);

protected void onViewIsDetaching() {
}

}
