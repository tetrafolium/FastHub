package com.fastaccess.ui.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import com.fastaccess.R;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

/**
 * Created by Kosh on 31 Dec 2016, 3:12 PM
 */

public class SimpleViewHolder<O> extends BaseViewHolder<O> {

  @BindView(R.id.title) FontTextView title;

  public SimpleViewHolder(final @NonNull View itemView,
                          final @Nullable BaseRecyclerAdapter adapter) {
    super(itemView, adapter);
  }

  @Override
  public void bind(final @NonNull O o) {
    title.setText(o.toString());
  }
}
