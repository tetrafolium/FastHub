package com.fastaccess.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.TooltipCompat;
import com.fastaccess.helper.ViewHelper;

public class ForegroundImageView extends AppCompatImageView {

  public ForegroundImageView(final @NonNull Context context) {
    this(context, null);
  }

  public ForegroundImageView(final @NonNull Context context,
                             final AttributeSet attrs) {
    this(context, attrs, 0);
    if (getContentDescription() != null) {
      TooltipCompat.setTooltipText(this, getContentDescription());
    }
  }

  public ForegroundImageView(final @NonNull Context context,
                             final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void tintDrawableColor(final @ColorInt int color) {
    ViewHelper.tintDrawable(getDrawable(), color);
  }
}
