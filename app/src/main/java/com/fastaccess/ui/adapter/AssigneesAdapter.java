package com.fastaccess.ui.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fastaccess.data.dao.model.User;
import com.fastaccess.ui.adapter.viewholder.AssigneesViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class AssigneesAdapter
    extends BaseRecyclerAdapter<User, AssigneesViewHolder,
                                BaseViewHolder.OnItemClickListener<User>> {

  public interface OnSelectAssignee {
    boolean isAssigneeSelected(int position);

    void onToggleSelection(int position, boolean select);
  }

  private final OnSelectAssignee onSelectAssignee;

  public AssigneesAdapter(final @NonNull List<User> data,
                          final @Nullable OnSelectAssignee onSelectAssignee) {
    super(data);
    this.onSelectAssignee = onSelectAssignee;
  }

  @Override
  protected AssigneesViewHolder viewHolder(final ViewGroup parent,
                                           final int viewType) {
    return AssigneesViewHolder.newInstance(parent, onSelectAssignee, this);
  }

  @Override
  protected void onBindView(final AssigneesViewHolder holder,
                            final int position) {
    holder.bind(getItem(position));
  }
}
