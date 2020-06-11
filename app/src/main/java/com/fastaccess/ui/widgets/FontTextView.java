package com.fastaccess.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.crashlytics.android.Crashlytics;
import com.evernote.android.state.State;
import com.evernote.android.state.StateSaver;
import com.fastaccess.R;
import com.fastaccess.helper.TypeFaceHelper;
import com.fastaccess.helper.ViewHelper;

/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontTextView extends AppCompatTextView {

  @State int tintColor = -1;
  @State boolean selected;

  public FontTextView(final @NonNull Context context) { this(context, null); }

  public FontTextView(final @NonNull Context context,
                      final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FontTextView(final @NonNull Context context, final AttributeSet attrs,
                      final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @Override
  public Parcelable onSaveInstanceState() {
    return StateSaver.saveInstanceState(this, super.onSaveInstanceState());
  }

  @Override
  public void onRestoreInstanceState(final Parcelable state) {
    super.onRestoreInstanceState(StateSaver.restoreInstanceState(this, state));
    tintDrawables(tintColor);
    setSelected(selected);
  }

  @Override
  public void setSelected(final boolean selected) {
    super.setSelected(selected);
    this.selected = selected;
  }

  @Override
  public void setTextCursorDrawable(final
                                    @Nullable Drawable textCursorDrawable) {
    try {
      super.setTextCursorDrawable(textCursorDrawable);
    } catch (Exception e) {
      Crashlytics.logException(e);
    }
  }

  @Override
  public void setTextCursorDrawable(final int textCursorDrawable) {
    try {
      super.setTextCursorDrawable(textCursorDrawable);
    } catch (Exception e) {
      Crashlytics.logException(e);
    }
  }

  private void init(final @NonNull Context context,
                    final @Nullable AttributeSet attributeSet) {
    if (attributeSet != null) {
      TypedArray tp = context.obtainStyledAttributes(attributeSet,
                                                     R.styleable.FontTextView);
      try {
        int color = tp.getColor(R.styleable.FontTextView_drawableColor, -1);
        tintDrawables(color);
      } finally {
        tp.recycle();
      }
    }
    if (isInEditMode())
      return;
    setFreezesText(true);
    TypeFaceHelper.applyTypeface(this);
  }

  public void tintDrawables(final @ColorInt int color) {
    if (color != -1) {
      this.tintColor = color;
      Drawable[] drawables = getCompoundDrawablesRelative();
      for (Drawable drawable : drawables) {
        if (drawable == null)
          continue;
        ViewHelper.tintDrawable(drawable, color);
      }
    }
  }

  public void setEventsIcon(final @DrawableRes int drawableRes) {
    Drawable drawable = ContextCompat.getDrawable(getContext(), drawableRes);
    int width = drawable.getIntrinsicWidth();
    int height = drawable.getIntrinsicHeight();
    drawable.setBounds(0, 0, width / 2, height / 2);
    ScaleDrawable sd = new ScaleDrawable(drawable, Gravity.CENTER, 0.6f, 0.6f);
    sd.setLevel(8000);
    ViewHelper.tintDrawable(drawable,
                            ViewHelper.getTertiaryTextColor(getContext()));
    setCompoundDrawablesWithIntrinsicBounds(sd, null, null, null);
  }
}
