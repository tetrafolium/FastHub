package com.fastaccess.provider.timeline.handler;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import com.zzhoujay.markdown.style.FontSpan;
import net.nightwhistler.htmlspanner.TagNodeHandler;
import org.htmlcleaner.TagNode;

/**
 * Created by Kosh on 06 May 2017, 11:02 AM
 */

public class ItalicHandler extends TagNodeHandler {

  public void handleTagNode(final TagNode node,
                            final SpannableStringBuilder builder,
                            final int start, final int end) {
    builder.setSpan(new FontSpan(1, Typeface.ITALIC), start, builder.length(),
                    33);
  }
}
