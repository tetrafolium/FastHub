package com.fastaccess.ui.widgets.recyclerview.layout_manager;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Kosh on 17 May 2016, 10:02 PM
 */
public class StaggeredManager extends StaggeredGridLayoutManager {

    public StaggeredManager(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public StaggeredManager(final int spanCount, final int orientation) {
        super(spanCount, orientation);
    }

    @Override public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception ignored) { }
    }

    @Override public void onMeasure(final RecyclerView.Recycler recycler, final RecyclerView.State state, final int widthSpec, final int heightSpec) {
        try {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        } catch (Exception ignored) { }
    }

}
