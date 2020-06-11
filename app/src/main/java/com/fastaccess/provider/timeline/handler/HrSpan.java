package com.fastaccess.provider.timeline.handler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.LineHeightSpan;
import android.text.style.ReplacementSpan;
import androidx.annotation.NonNull;

public class HrSpan extends ReplacementSpan implements LineHeightSpan {

  private final int width;
  private final int color;

  HrSpan(final int color, final int width) {
    this.color = color;
    this.width = width;
    Drawable drawable = new ColorDrawable(color);
  }

  @Override
  public int getSize(final @NonNull Paint paint, final CharSequence text,
                     final int start, final int end,
                     final Paint.FontMetricsInt fm) {
    return (int)paint.measureText(text, start, end);
  }

  @Override
  public void draw(final @NonNull Canvas canvas, final CharSequence text,
                   final int start, final int end, final float x, final int top,
                   final int y, final int bottom, final @NonNull Paint paint) {
    final int currentColor = paint.getColor();
    paint.setColor(color);
    paint.setStyle(Paint.Style.FILL);
    int height = 10;
    canvas.drawRect(new Rect(0, bottom - height, (int)x + width, bottom),
                    paint);
    paint.setColor(currentColor);
  }

  @Override
  public void chooseHeight(final CharSequence text, final int start,
                           final int end, final int spanstartv, final int v,
                           final Paint.FontMetricsInt fm) {
    fm.top /= 3;
    fm.ascent /= 3;
    fm.bottom /= 3;
    fm.descent /= 3;
  }
}
