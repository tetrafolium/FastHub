package com.fastaccess.ui.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.fastaccess.data.dao.model.User;
import com.fastaccess.ui.adapter.viewholder.UsersViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;
import java.util.ArrayList;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class UsersAdapter
    extends BaseRecyclerAdapter<User, UsersViewHolder,
                                BaseViewHolder.OnItemClickListener<User>> {

  private boolean isContributor;
  private boolean isFilter;

  public UsersAdapter(final @NonNull ArrayList<User> list) {
    this(list, false);
  }

  public UsersAdapter(final @NonNull ArrayList<User> list,
                      final boolean isContributor) {
    this(list, isContributor, false);
  }

  public UsersAdapter(final @NonNull ArrayList<User> list,
                      final boolean isContributor, final boolean isFilter) {
    super(list);
    this.isContributor = isContributor;
    this.isFilter = isFilter;
  }

  @Override
  protected UsersViewHolder viewHolder(final ViewGroup parent,
                                       final int viewType) {
    return UsersViewHolder.newInstance(parent, this, isFilter);
  }

  @Override
  protected void onBindView(final UsersViewHolder holder, final int position) {
    holder.bind(getItem(position), isContributor);
  }
}
