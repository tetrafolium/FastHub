package com.fastaccess.provider.markdown.extension.emoji.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

public class EmojiNodeRenderer implements NodeRenderer {

  private final HtmlNodeRendererContext context;
  private final HtmlWriter html;

  public EmojiNodeRenderer(final HtmlNodeRendererContext context) {
    this.context = context;
    this.html = context.getWriter();
  }

  @Override
  public Set<Class<? extends Node>> getNodeTypes() {
    return Collections.singleton(
        com.fastaccess.provider.markdown.extension.emoji.Emoji.class);
  }

  @Override
  public void render(final Node node) {
    Map<String, String> attributes =
        context.extendAttributes(node, "emoji", Collections.emptyMap());
    html.tag("emoji", attributes);
    renderChildren(node);
    html.tag("/emoji");
  }

  private void renderChildren(final Node parent) {
    Node node = parent.getFirstChild();
    while (node != null) {
      Node next = node.getNext();
      context.render(node);
      node = next;
    }
  }
}
