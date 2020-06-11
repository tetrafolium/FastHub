package com.fastaccess.ui.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fastaccess.data.dao.model.PinnedRepos;
import com.fastaccess.ui.adapter.viewholder.PinnedReposViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class PinnedReposAdapter extends BaseRecyclerAdapter<
    PinnedRepos, PinnedReposViewHolder,
    BaseViewHolder.OnItemClickListener<PinnedRepos>> {

  private boolean singleLine;

  public PinnedReposAdapter(final boolean singleLine) {
    this.singleLine = singleLine;
  }

  public PinnedReposAdapter(final @NonNull List<PinnedRepos> data,
                            final @Nullable BaseViewHolder
                                .OnItemClickListener<PinnedRepos> listener) {
    super(data, listener);
  }

  @Override
  protected PinnedReposViewHolder viewHolder(final ViewGroup parent,
                                             final int viewType) {
    return PinnedReposViewHolder.newInstance(parent, this, singleLine);
  }

  @Override
  protected void onBindView(final PinnedReposViewHolder holder,
                            final int position) {
    holder.bind(getItem(position));
  }
}
