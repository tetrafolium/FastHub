package com.fastaccess.ui.widgets;

import static android.R.attr.enabled;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by kosh20111 on 10/8/2015.
 * <p/>
 * Viewpager that has scrolling animation by default
 */
public class ViewPagerView extends ViewPager {

  private boolean isEnabled;

  public ViewPagerView(final Context context) { super(context); }

  public ViewPagerView(final @NonNull Context context,
                       final AttributeSet attrs) {
    super(context, attrs);
    int[] attrsArray = {enabled};
    TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
    isEnabled = array.getBoolean(0, true);
    array.recycle();
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.isEnabled = enabled;
    requestLayout();
  }

  @Override
  public void setAdapter(final @Nullable PagerAdapter adapter) {
    super.setAdapter(adapter);
    if (isInEditMode())
      return;
    if (adapter != null) {
      setOffscreenPageLimit(adapter.getCount());
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(final MotionEvent event) {
    try {
      return !isEnabled() || super.onTouchEvent(event);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean onInterceptTouchEvent(final MotionEvent event) {
    try {
      return isEnabled() && super.onInterceptTouchEvent(event);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
    }
    return false;
  }
}
