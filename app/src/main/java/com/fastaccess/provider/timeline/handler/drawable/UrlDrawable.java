package com.fastaccess.provider.timeline.handler.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.crashlytics.android.Crashlytics;

class UrlDrawable extends BitmapDrawable implements Drawable.Callback {
    private Drawable drawable;

    @SuppressWarnings("deprecation") UrlDrawable() { }

    @Override public void draw(final Canvas canvas) {
        if (drawable != null) {
            try {
                drawable.draw(canvas);
            } catch (Exception e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
            if (drawable instanceof GifDrawable) {
                if (!((GifDrawable) drawable).isRunning()) {
                    ((GifDrawable) drawable).start();
                }
            }
        }
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(final Drawable drawable) {
        if (this.drawable != null) {
            this.drawable.setCallback(null);
        }
        drawable.setCallback(this);
        this.drawable = drawable;
    }

    @Override public void invalidateDrawable(final @NonNull Drawable who) {
        if (getCallback() != null) {
            getCallback().invalidateDrawable(who);
        }
    }

    @Override public void scheduleDrawable(final @NonNull Drawable who, final @NonNull Runnable what, final long when) {
        if (getCallback() != null) {
            getCallback().scheduleDrawable(who, what, when);
        }
    }

    @Override public void unscheduleDrawable(final @NonNull Drawable who, final @NonNull Runnable what) {
        if (getCallback() != null) {
            getCallback().unscheduleDrawable(who, what);
        }
    }
}
