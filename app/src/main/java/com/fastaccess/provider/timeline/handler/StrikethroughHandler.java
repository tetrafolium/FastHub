package com.fastaccess.provider.timeline.handler;

import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;

import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

public class StrikethroughHandler extends TagNodeHandler {

@Override public void handleTagNode(final TagNode node, final SpannableStringBuilder builder, final int start, final int end) {
	builder.setSpan(new StrikethroughSpan(), start, end, 33);
}

}
