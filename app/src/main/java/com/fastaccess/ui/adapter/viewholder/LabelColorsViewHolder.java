package com.fastaccess.ui.adapter.viewholder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import com.fastaccess.R;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

/**
 * Created by Kosh on 02 Apr 2017, 5:19 PM
 */

public class LabelColorsViewHolder extends BaseViewHolder<String> {

  @BindView(R.id.color) FontTextView color;

  private LabelColorsViewHolder(final @NonNull View itemView,
                                final @Nullable BaseRecyclerAdapter adapter) {
    super(itemView, adapter);
  }

  public static LabelColorsViewHolder
  newInstance(final ViewGroup parent, final BaseRecyclerAdapter adapter) {
    return new LabelColorsViewHolder(
        getView(parent, R.layout.simple_color_row_item), adapter);
  }

  @Override
  public void bind(final @NonNull String labelModel) {
    int labelColor = Color.parseColor(labelModel);
    itemView.setBackgroundColor(labelColor);
    color.setTextColor(ViewHelper.generateTextColor(labelColor));
    color.setText(labelModel);
  }
}
