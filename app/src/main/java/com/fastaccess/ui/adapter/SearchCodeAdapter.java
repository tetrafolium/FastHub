package com.fastaccess.ui.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.fastaccess.data.dao.SearchCodeModel;
import com.fastaccess.ui.adapter.viewholder.SearchCodeViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class SearchCodeAdapter extends BaseRecyclerAdapter<
    SearchCodeModel, SearchCodeViewHolder,
    BaseViewHolder.OnItemClickListener<SearchCodeModel>> {

  private boolean showRepoName;

  public SearchCodeAdapter(final @NonNull List<SearchCodeModel> data) {
    super(data);
    this.showRepoName = showRepoName;
  }

  @Override
  protected SearchCodeViewHolder viewHolder(final ViewGroup parent,
                                            final int viewType) {
    return SearchCodeViewHolder.newInstance(parent, this);
  }

  @Override
  protected void onBindView(final SearchCodeViewHolder holder,
                            final int position) {
    holder.bind(getItem(position), showRepoName);
  }

  public void showRepoName(final boolean showRepoName) {
    this.showRepoName = showRepoName;
    notifyDataSetChanged();
  }
}
