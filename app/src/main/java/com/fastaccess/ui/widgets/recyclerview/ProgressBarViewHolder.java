package com.fastaccess.ui.widgets.recyclerview;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.fastaccess.R;

/**
 * Created by kosh on 03/08/2017.
 */

public class ProgressBarViewHolder extends BaseViewHolder {

  private ProgressBarViewHolder(final @NonNull View itemView) {
    super(itemView);
  }

  public static ProgressBarViewHolder newInstance(final ViewGroup viewGroup) {
    return new ProgressBarViewHolder(
        getView(viewGroup, R.layout.progress_layout));
  }

  @Override
  public void bind(final @NonNull Object o) {}
}
