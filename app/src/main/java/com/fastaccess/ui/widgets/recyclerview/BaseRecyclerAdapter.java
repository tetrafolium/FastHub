package com.fastaccess.ui.widgets.recyclerview;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.fastaccess.helper.AnimHelper;
import com.fastaccess.helper.PrefGetter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 17 May 2016, 7:10 PM
 */
public abstract class BaseRecyclerAdapter<
    M, VH extends BaseViewHolder, P
        extends BaseViewHolder.OnItemClickListener<M>>
    extends RecyclerView.Adapter<VH> {

  private final static int PROGRESS_TYPE = 2017;

  @NonNull private List<M> data;
  @Nullable private P listener;
  private int lastKnowingPosition = -1;
  private boolean enableAnimation = PrefGetter.isRVAnimationEnabled();
  private boolean showedGuide;
  private GuideListener guideListener;
  private boolean progressAdded;
  private int rowWidth;

  protected BaseRecyclerAdapter() { this(new ArrayList<>()); }

  protected BaseRecyclerAdapter(final @NonNull List<M> data) {
    this(data, null);
  }

  protected BaseRecyclerAdapter(final @NonNull List<M> data,
                                final @Nullable P listener) {
    this.data = data;
    this.listener = listener;
  }

  protected BaseRecyclerAdapter(final @Nullable P listener) {
    this(new ArrayList<>(), listener);
  }

  protected abstract VH viewHolder(ViewGroup parent, int viewType);

  protected abstract void onBindView(VH holder, int position);

  @NonNull
  public List<M> getData() {
    return data;
  }

  public M getItemByPosition(final int position) { return data.get(position); }

  public M getItem(final int position) { return data.get(position); }

  public int getItem(final M t) { return data.indexOf(t); }

  @SuppressWarnings("unchecked")
  @Override
  public VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
    if (viewType == PROGRESS_TYPE) {
      addSpanLookup(parent);
      return (VH)ProgressBarViewHolder.newInstance(parent);
    } else {
      return viewHolder(parent, viewType);
    }
  }

  @Override
  public void onBindViewHolder(final @NonNull VH holder, final int position) {
    if (holder instanceof ProgressBarViewHolder) {
      if (holder.itemView.getLayoutParams() instanceof
          StaggeredGridLayoutManager.LayoutParams) {
        StaggeredGridLayoutManager.LayoutParams layoutParams =
            (StaggeredGridLayoutManager.LayoutParams)
                holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
      }
    } else if (getItem(position) != null) {
      animate(holder, position);
      onBindView(holder, position);
      onShowGuide(holder, position);
    }
  }

  @Override
  public int getItemViewType(final int position) {
    if (getItem(position) == null) {
      return PROGRESS_TYPE;
    }
    return super.getItemViewType(position);
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  @SuppressWarnings("unchecked")
  private void
  onShowGuide(final @NonNull VH holder,
              final int position) { // give the flexibility to other adapters to
                                    // override this
    if (position == 0 && !isShowedGuide() && guideListener != null) {
      guideListener.onShowGuide(holder.itemView, getItem(position));
      showedGuide = true;
    }
  }

  private void animate(final @NonNull VH holder, final int position) {
    if (isEnableAnimation() && position > lastKnowingPosition) {
      AnimHelper.startBeatsAnimation(holder.itemView);
      lastKnowingPosition = position;
    }
  }

  public void insertItems(final @NonNull List<M> items) {
    data.clear();
    data.addAll(items);
    notifyDataSetChanged();
    progressAdded = false;
  }

  public void addItem(final M item, final int position) {
    data.add(position, item);
    notifyItemInserted(position);
  }

  public void addItem(final M item) {
    removeProgress();
    data.add(item);
    if (data.size() == 0) {
      notifyDataSetChanged();
    } else {
      notifyItemInserted(data.size() - 1);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public void addItems(final @NonNull List<M> items) {
    removeProgress();
    data.addAll(items);
    notifyItemRangeInserted(getItemCount(),
                            (getItemCount() + items.size()) - 1);
  }

  @SuppressWarnings("WeakerAccess")
  public void removeItem(final int position) {
    data.remove(position);
    notifyItemRemoved(position);
  }

  public void removeItem(final M item) {
    int position = data.indexOf(item);
    if (position != -1)
      removeItem(position);
  }

  public void removeItems(final @NonNull List<M> items) {
    int prevSize = getItemCount();
    data.removeAll(items);
    notifyItemRangeRemoved(prevSize, Math.abs(data.size() - prevSize));
  }

  public void swapItem(final @NonNull M model) {
    int index = getItem(model);
    swapItem(model, index);
  }

  public void swapItem(final @NonNull M model, final int position) {
    if (position != -1) {
      data.set(position, model);
      notifyItemChanged(position);
    }
  }

  public void subList(final int fromPosition, final int toPosition) {
    if (data.isEmpty())
      return;
    data.subList(fromPosition, toPosition).clear();
    notifyItemRangeRemoved(fromPosition, toPosition);
  }

  public void clear() {
    progressAdded = false;
    data.clear();
    notifyDataSetChanged();
  }

  public boolean isEmpty() { return data.isEmpty(); }

  public void setEnableAnimation(final boolean enableAnimation) {
    this.enableAnimation = enableAnimation;
    notifyDataSetChanged();
  }

  @SuppressWarnings("WeakerAccess")
  public boolean isEnableAnimation() {
    return enableAnimation;
  }

  @SuppressWarnings("WeakerAccess")
  @Nullable
  public P getListener() {
    return listener;
  }

  public void setListener(final @Nullable P listener) {
    this.listener = listener;
    notifyDataSetChanged();
  }

  public void setGuideListener(final GuideListener guideListener) {
    this.guideListener = guideListener;
  }

  public int getRowWidth() { return rowWidth; }

  public void setRowWidth(final int rowWidth) {
    if (this.rowWidth == 0) {
      this.rowWidth = rowWidth;
      notifyDataSetChanged();
    }
  }

  private boolean isShowedGuide() { return showedGuide; }

  @Override
  public void onViewDetachedFromWindow(final VH holder) {
    holder.onViewIsDetaching();
    super.onViewDetachedFromWindow(holder);
  }

  public void addProgress() {
    if (!progressAdded && !isEmpty()) {
      addItem(null);
      progressAdded = true;
    }
  }

  public boolean isProgressAdded() { return progressAdded; }

  private void removeProgress() {
    if (!isEmpty()) {
      M m = getItem(getItemCount() - 1);
      if (m == null) {
        removeItem(getItemCount() - 1);
      }
      progressAdded = false;
    }
  }

  private void addSpanLookup(final ViewGroup parent) {
    if ((parent instanceof RecyclerView) && (((RecyclerView)parent).getLayoutManager() instanceof
          GridLayoutManager)) {
      GridLayoutManager layoutManager =
          ((GridLayoutManager)((RecyclerView)parent).getLayoutManager());
      layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(final int position) {
          return getItemViewType(position) == PROGRESS_TYPE
              ? layoutManager.getSpanCount()
              : 1;
        }
      });
    }
  }

  public interface GuideListener<M> {
    void onShowGuide(@NonNull View itemView, @NonNull M model);
  }
}
