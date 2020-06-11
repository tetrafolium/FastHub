package com.fastaccess.ui.widgets.recyclerview.layout_manager;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Kosh on 17 May 2016, 10:02 PM
 */
public class GridManager extends GridLayoutManager {

    private int iconSize;

    public GridManager(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GridManager(final Context context, final int spanCount) {
        super(context, spanCount);
    }

    public GridManager(final Context context, final int spanCount, final int orientation, final boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
            updateCount();
        } catch (Exception ignored) { }
    }

    @Override public void onMeasure(final RecyclerView.Recycler recycler, final RecyclerView.State state, final int widthSpec, final int heightSpec) {
        try {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        } catch (Exception ignored) { }
    }

    private void updateCount() {
        if (iconSize > 1) {
            int spanCount = Math.max(1, getWidth() / iconSize);
            if (spanCount < 1) {
                spanCount = 1;
            }
            this.setSpanCount(spanCount);
        }
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(final int iconSize) {
        this.iconSize = iconSize;
        updateCount();
    }
}
