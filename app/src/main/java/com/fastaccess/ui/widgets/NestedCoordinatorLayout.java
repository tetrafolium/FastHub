package com.fastaccess.ui.widgets;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kosh on 13 Aug 2016, 1:11 PM
 */

public class NestedCoordinatorLayout extends CoordinatorLayout implements NestedScrollingChild {

private NestedScrollingChildHelper mChildHelper;

public NestedCoordinatorLayout(final @NonNull Context context) {
	super(context);
	mChildHelper = new NestedScrollingChildHelper(this);
	setNestedScrollingEnabled(true);
}

public NestedCoordinatorLayout(final @NonNull Context context, final AttributeSet attrs) {
	super(context, attrs);
	mChildHelper = new NestedScrollingChildHelper(this);
	setNestedScrollingEnabled(true);
}

public NestedCoordinatorLayout(final @NonNull Context context, final AttributeSet attrs, final int defStyleAttr) {
	super(context, attrs, defStyleAttr);
	mChildHelper = new NestedScrollingChildHelper(this);
	setNestedScrollingEnabled(true);
}

@Override public boolean onStartNestedScroll(final View child, final View target, final int nestedScrollAxes) {
	/* Enable the scrolling behavior of our own children */
	boolean tHandled = super.onStartNestedScroll(child, target, nestedScrollAxes);
	/* Enable the scrolling behavior of the parent's other children  */
	return startNestedScroll(nestedScrollAxes) || tHandled;
}

@Override public void onStopNestedScroll(final View target) {
	/* Disable the scrolling behavior of our own children */
	super.onStopNestedScroll(target);
	/* Disable the scrolling behavior of the parent's other children  */
	stopNestedScroll();
}

@Override public void onNestedPreScroll(final View target, final int dx, final int dy, final int[] consumed) {
	int[][] tConsumed = new int[2][2];
	super.onNestedPreScroll(target, dx, dy, tConsumed[0]);
	dispatchNestedPreScroll(dx, dy, tConsumed[1], null);
	consumed[0] = tConsumed[0][0] + tConsumed[1][0];
	consumed[1] = tConsumed[0][1] + tConsumed[1][1];
}

@Override public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed) {
	super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
	dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null);
}

@Override public boolean onNestedPreFling(final View target, final float velocityX, final float velocityY) {
	boolean tHandled = super.onNestedPreFling(target, velocityX, velocityY);
	return dispatchNestedPreFling(velocityX, velocityY) || tHandled;
}

@Override public boolean onNestedFling(final View target, final float velocityX, final float velocityY, final boolean consumed) {
	boolean tHandled = super.onNestedFling(target, velocityX, velocityY, consumed);
	return dispatchNestedFling(velocityX, velocityY, consumed) || tHandled;
}

@Override public void setNestedScrollingEnabled(final boolean enabled) {
	mChildHelper.setNestedScrollingEnabled(enabled);
}

@Override public boolean isNestedScrollingEnabled() {
	return mChildHelper.isNestedScrollingEnabled();
}

@Override public boolean startNestedScroll(final int axes) {
	return mChildHelper.startNestedScroll(axes);
}

@Override public void stopNestedScroll() {
	mChildHelper.stopNestedScroll();
}

@Override public boolean hasNestedScrollingParent() {
	return mChildHelper.hasNestedScrollingParent();
}

@Override public boolean dispatchNestedScroll(final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed, final int[] offsetInWindow) {
	return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
	                                         dyUnconsumed, offsetInWindow);
}

@Override public boolean dispatchNestedPreScroll(final int dx, final int dy, final int[] consumed, final int[] offsetInWindow) {
	return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
}

@Override public boolean dispatchNestedFling(final float velocityX, final float velocityY, final boolean consumed) {
	return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
}

@Override public boolean dispatchNestedPreFling(final float velocityX, final float velocityY) {
	return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
}
}

