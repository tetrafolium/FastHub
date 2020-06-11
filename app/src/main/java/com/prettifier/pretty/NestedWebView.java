package com.prettifier.pretty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import androidx.annotation.RequiresApi;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class NestedWebView extends WebView implements NestedScrollingChild {
  private int mLastY;
  private final int[] mScrollOffset = new int[2];
  private final int[] mScrollConsumed = new int[2];
  private int mNestedOffsetY;
  private NestedScrollingChildHelper mChildHelper;
  private boolean firstScroll = true;

  public NestedWebView(final Context context) { this(context, null); }

  public NestedWebView(final Context context, final AttributeSet attrs) {
    this(context, attrs, android.R.attr.webViewStyle);
  }

  public NestedWebView(final Context context, final AttributeSet attrs,
                       final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mChildHelper = new NestedScrollingChildHelper(this);
    setNestedScrollingEnabled(true);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public NestedWebView(final Context context, final AttributeSet attrs,
                       final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(final MotionEvent ev) {
    boolean returnValue;
    MotionEvent event = MotionEvent.obtain(ev);
    final int action = MotionEventCompat.getActionMasked(event);
    if (action == MotionEvent.ACTION_DOWN) {
      mNestedOffsetY = 0;
    }
    int eventY = (int)event.getY();
    event.offsetLocation(0, mNestedOffsetY);
    switch (action) {
    case MotionEvent.ACTION_MOVE:
      int deltaY = mLastY - eventY;
      // NestedPreScroll
      if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
        deltaY -= mScrollConsumed[1];
        mLastY = eventY - mScrollOffset[1];
        event.offsetLocation(0, -mScrollOffset[1]);
        mNestedOffsetY += mScrollOffset[1];
      }
      returnValue = super.onTouchEvent(event);

      // NestedScroll
      if (dispatchNestedScroll(0, mScrollOffset[1], 0, deltaY, mScrollOffset)) {
        event.offsetLocation(0, mScrollOffset[1]);
        mNestedOffsetY += mScrollOffset[1];
        mLastY -= mScrollOffset[1];
      }
      break;
    case MotionEvent.ACTION_DOWN:
      returnValue = super.onTouchEvent(event);
      if (firstScroll) {
        mLastY = eventY - 5;
        firstScroll = false;
      } else {
        mLastY = eventY;
      }
      startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
      break;
    default:
      returnValue = super.onTouchEvent(event);
      // end NestedScroll
      stopNestedScroll();
      break;
    }
    return returnValue;
  }

  @Override
  public void setNestedScrollingEnabled(final boolean enabled) {
    mChildHelper.setNestedScrollingEnabled(enabled);
  }

  @Override
  public boolean isNestedScrollingEnabled() {
    return mChildHelper.isNestedScrollingEnabled();
  }

  @Override
  public boolean startNestedScroll(final int axes) {
    return mChildHelper.startNestedScroll(axes);
  }

  @Override
  public void stopNestedScroll() {
    mChildHelper.stopNestedScroll();
  }

  @Override
  public boolean hasNestedScrollingParent() {
    return mChildHelper.hasNestedScrollingParent();
  }

  @Override
  public boolean
  dispatchNestedScroll(final int dxConsumed, final int dyConsumed,
                       final int dxUnconsumed, final int dyUnconsumed,
                       final int[] offsetInWindow) {
    return mChildHelper.dispatchNestedScroll(
        dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedPreScroll(final int dx, final int dy,
                                         final int[] consumed,
                                         final int[] offsetInWindow) {
    return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed,
                                                offsetInWindow);
  }

  @Override
  public boolean dispatchNestedFling(final float velocityX,
                                     final float velocityY,
                                     final boolean consumed) {
    return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
  }

  @Override
  public boolean dispatchNestedPreFling(final float velocityX,
                                        final float velocityY) {
    return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
  }
}
