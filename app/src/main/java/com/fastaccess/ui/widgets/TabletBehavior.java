package com.fastaccess.ui.widgets;

import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import it.sephiroth.android.library.bottomnavigation.VerticalScrollingBehavior;

public class TabletBehavior extends VerticalScrollingBehavior<BottomNavigation> {

    public TabletBehavior(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLayoutValues(final int bottomNavWidth, final int topInset, final boolean translucentStatus) { }

    public boolean layoutDependsOn(final CoordinatorLayout parent, final BottomNavigation child, final View dependency) {
        return AppBarLayout.class.isInstance(dependency) || Toolbar.class.isInstance(dependency);
    }

    public boolean onDependentViewChanged(final CoordinatorLayout parent, final BottomNavigation child, final View dependency) {
        return true;
    }

    public void onDependentViewRemoved(final CoordinatorLayout parent, final BottomNavigation child, final View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    public boolean onLayoutChild(final CoordinatorLayout parent, final BottomNavigation child, final int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    public void onNestedVerticalOverScroll(final CoordinatorLayout coordinatorLayout, final BottomNavigation child, final int direction, final int currentOverScroll, final int
                                           totalOverScroll) {
    }

    public void onDirectionNestedPreScroll(final CoordinatorLayout coordinatorLayout, final BottomNavigation child, final View target, final int dx, final int dy, final int[]
                                           consumed, final int scrollDirection) {
    }

    protected boolean onNestedDirectionFling(final CoordinatorLayout coordinatorLayout, final BottomNavigation child, final View target, final float velocityX, final float
            velocityY, final int scrollDirection) {
        return false;
    }
}
