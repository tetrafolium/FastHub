package com.fastaccess.provider.timeline.handler;

import androidx.annotation.ColorInt;
import android.text.SpannableStringBuilder;

import com.zzhoujay.markdown.style.MarkDownQuoteSpan;

import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

import lombok.AllArgsConstructor;

/**
 * Created by Kosh on 23 Apr 2017, 11:30 AM
 */

@AllArgsConstructor public class QuoteHandler extends TagNodeHandler {

    @ColorInt private int color;

    @Override
    public void handleTagNode(final TagNode node, final SpannableStringBuilder builder, final int start, final int end) {
        builder.setSpan(new MarkDownQuoteSpan(color), start + 1, builder.length(), 33);
    }
}
