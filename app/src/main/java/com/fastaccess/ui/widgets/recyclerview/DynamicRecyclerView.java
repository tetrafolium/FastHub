package com.fastaccess.ui.widgets.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.ui.widgets.StateLayout;


/**
 * Created by Kosh on 9/24/2015. copyrights are reserved
 * <p>
 * recyclerview which will showParentOrSelf/showParentOrSelf itself base on adapter
 */
public class DynamicRecyclerView extends RecyclerView {

private StateLayout emptyView;
@Nullable private View parentView;
private BottomPaddingDecoration bottomPaddingDecoration;

@NonNull private AdapterDataObserver observer = new AdapterDataObserver() {
	@Override public void onChanged() {
		showEmptyView();
	}

	@Override public void onItemRangeInserted(final int positionStart, final int itemCount) {
		super.onItemRangeInserted(positionStart, itemCount);
		showEmptyView();
	}

	@Override public void onItemRangeRemoved(final int positionStart, final int itemCount) {
		super.onItemRangeRemoved(positionStart, itemCount);
		showEmptyView();
	}
};

public DynamicRecyclerView(final @NonNull Context context) {
	this(context, null);
}

public DynamicRecyclerView(final @NonNull Context context, final AttributeSet attrs) {
	this(context, attrs, 0);
}

public DynamicRecyclerView(final @NonNull Context context, final AttributeSet attrs, final int defStyle) {
	super(context, attrs, defStyle);
}

@Override public void setAdapter(final @Nullable Adapter adapter) {
	super.setAdapter(adapter);
	if (isInEditMode()) return;
	if (adapter != null) {
		adapter.registerAdapterDataObserver(observer);
		observer.onChanged();
	}
}


public void removeBottomDecoration() {
	if (bottomPaddingDecoration != null) {
		removeItemDecoration(bottomPaddingDecoration);
		bottomPaddingDecoration = null;
	}
}

public void addDecoration() {
	bottomPaddingDecoration = BottomPaddingDecoration.with(getContext());
	addItemDecoration(bottomPaddingDecoration);
}

private void showEmptyView() {
	Adapter<?> adapter = getAdapter();
	if (adapter != null) {
		if (emptyView != null) {
			if (adapter.getItemCount() == 0) {
				showParentOrSelf(false);
			} else {
				showParentOrSelf(true);
			}
		}
	} else {
		if (emptyView != null) {
			showParentOrSelf(false);
		}
	}
}

private void showParentOrSelf(final boolean showRecyclerView) {
	if (parentView != null)
		parentView.setVisibility(VISIBLE);
	setVisibility(VISIBLE);
	emptyView.setVisibility(!showRecyclerView ? VISIBLE : GONE);
}

public void setEmptyView(final @NonNull StateLayout emptyView, final @Nullable View parentView) {
	this.emptyView = emptyView;
	this.parentView = parentView;
	showEmptyView();
}

public void setEmptyView(final @NonNull StateLayout emptyView) {
	setEmptyView(emptyView, null);
}

public void hideProgress(final @NonNull StateLayout view) {
	view.hideProgress();
}

public void showProgress(final @NonNull StateLayout view) {
	view.showProgress();
}

public void addKeyLineDivider() {
	if (canAddDivider()) {
		Resources resources = getResources();
		addItemDecoration(new InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height),
		                                             resources.getDimensionPixelSize(R.dimen.keyline_2), ViewHelper.getListDivider(getContext())));
	}
}

public void addDivider() {
	if (canAddDivider()) {
		Resources resources = getResources();
		addItemDecoration(new InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height), 0,
		                                             ViewHelper.getListDivider(getContext())));
	}
}

public void addNormalSpacingDivider() {
	addDivider();
}

public void addDivider(final @NonNull Class toDivide) {
	if (canAddDivider()) {
		Resources resources = getResources();
		addItemDecoration(new InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height), 0,
		                                             ViewHelper.getListDivider(getContext()), toDivide));
	}
}

private boolean canAddDivider() {
	if (getLayoutManager() != null) {
		if (getLayoutManager() instanceof GridLayoutManager) {
			return ((GridLayoutManager) getLayoutManager()).getSpanCount() == 1;
		} else if (getLayoutManager() instanceof LinearLayoutManager) {
			return true;
		} else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
			return ((StaggeredGridLayoutManager) getLayoutManager()).getSpanCount() == 1;
		}
	}
	return false;
}
}
