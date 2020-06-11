package com.zzhoujay.markdown.style;

import android.annotation.SuppressLint;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.StyleSpan;

/**
 * Created by zhou on 2016/11/10.
 * FontSpan
 */
@SuppressLint("ParcelCreator")
public class FontSpan extends StyleSpan implements ParcelableSpan {

private final float size;
private final int color;

public FontSpan(final float size, final int style) {
	super(style);
	this.size = size;
	this.color = -1;
}

public FontSpan(final float size, final int style, final int color) {
	super(style);
	this.size = size;
	this.color = color;
}

@Override
public void updateMeasureState(final TextPaint p) {
	super.updateMeasureState(p);
	p.setTextSize(p.getTextSize() * size);
}

@Override
public void updateDrawState(final TextPaint tp) {
	super.updateDrawState(tp);
	updateMeasureState(tp);
	if (color != -1) tp.setColor(color);
}
}
