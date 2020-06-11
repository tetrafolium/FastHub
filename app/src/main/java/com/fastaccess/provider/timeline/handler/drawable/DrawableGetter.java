package com.fastaccess.provider.timeline.handler.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.fastaccess.App;
import com.fastaccess.GlideApp;
import com.fastaccess.R;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kosh on 22 Apr 2017, 7:44 PM
 */

public class DrawableGetter implements Html.ImageGetter, Drawable.Callback {
  private WeakReference<TextView> container;
  private final Set<GlideDrawableTarget> cachedTargets;
  private final int width;

  public DrawableGetter(final TextView tv, final int width) {
    tv.setTag(R.id.drawable_callback, this);
    this.container = new WeakReference<>(tv);
    this.cachedTargets = new HashSet<>();
    this.width = width;
  }

  @Override
  public Drawable getDrawable(final @NonNull String url) {
    final UrlDrawable urlDrawable = new UrlDrawable();
    if (container != null && container.get() != null) {
      Context context = container.get().getContext();
      final RequestBuilder load = Glide.with(context)
                                      .load(url)
                                      .placeholder(ContextCompat.getDrawable(
                                          context, R.drawable.ic_image))
                                      .dontAnimate();

      final GlideDrawableTarget target =
          new GlideDrawableTarget(urlDrawable, container, width);
      load.into(target);
      cachedTargets.add(target);
    }
    return urlDrawable;
  }

  @Override
  public void invalidateDrawable(final @NonNull Drawable drawable) {
    if (container != null && container.get() != null) {
      container.get().invalidate();
    }
  }

  @Override
  public void scheduleDrawable(final @NonNull Drawable drawable,
                               final @NonNull Runnable runnable, final long l) {
  }

  @Override
  public void unscheduleDrawable(final @NonNull Drawable drawable,
                                 final @NonNull Runnable runnable) {}

  public void clear(final @NonNull DrawableGetter drawableGetter) {
    if (drawableGetter.cachedTargets != null) {
      for (GlideDrawableTarget target : drawableGetter.cachedTargets) {
        GlideApp.with(App.getInstance()).clear(target);
      }
    }
  }
}
