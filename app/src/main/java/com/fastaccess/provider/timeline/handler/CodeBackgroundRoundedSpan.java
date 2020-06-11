package com.fastaccess.provider.timeline.handler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;

public class CodeBackgroundRoundedSpan extends MetricAffectingSpan implements LeadingMarginSpan, LineBackgroundSpan {
    private final int color;

    private final RectF rect = new RectF();

    CodeBackgroundRoundedSpan(final int color) {
        this.color = color;
    }

    @Override public void updateMeasureState(final TextPaint paint) {
        apply(paint);
    }

    @Override public void updateDrawState(final TextPaint paint) {
        apply(paint);
    }

    private void apply(final TextPaint paint) {
        paint.setTypeface(Typeface.MONOSPACE);
    }

    @Override public void drawBackground(final Canvas c, final Paint p, final int left, final int right, final int top, final int baseline, final int bottom,
                                         final CharSequence text, final int start, final int end, final int lnum) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();
        p.setStyle(Paint.Style.FILL);
        p.setColor(this.color);
        rect.set(left, top, right, bottom);
        c.drawRect(rect, p);
        p.setColor(color);
        p.setStyle(style);
    }

    @Override public int getLeadingMargin(final boolean first) {
        return 30;
    }

    @Override public void drawLeadingMargin(final Canvas c, final Paint p, final int x, final int dir, final int top, final int baseline, final int bottom,
                                            final CharSequence text, final int start, final int end, final boolean first, final Layout layout) { }
}
