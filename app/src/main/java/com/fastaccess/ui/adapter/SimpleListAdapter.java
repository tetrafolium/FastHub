package com.fastaccess.ui.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fastaccess.R;
import com.fastaccess.ui.adapter.viewholder.SimpleViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;
import java.util.List;

public class SimpleListAdapter<O>
    extends BaseRecyclerAdapter<O, SimpleViewHolder<O>,
                                SimpleViewHolder.OnItemClickListener<O>> {
  public SimpleListAdapter(
      final @NonNull List<O> data,
      final @Nullable SimpleViewHolder.OnItemClickListener<O> listener) {
    super(data, listener);
  }

  @Override
  protected SimpleViewHolder<O> viewHolder(final ViewGroup parent,
                                           final int viewType) {
    return new SimpleViewHolder<>(
        BaseViewHolder.getView(parent, R.layout.simple_row_item), this);
  }

  @Override
  protected void onBindView(final SimpleViewHolder<O> holder,
                            final int position) {
    holder.bind(getItem(position));
  }
}
