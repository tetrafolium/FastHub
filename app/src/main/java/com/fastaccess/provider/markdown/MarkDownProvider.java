package com.fastaccess.provider.markdown;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.ViewTreeObserver;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.IntStream;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.markdown.extension.emoji.EmojiExtension;
import com.fastaccess.provider.markdown.extension.mention.MentionExtension;
import com.fastaccess.provider.timeline.HtmlHelper;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kosh on 24 Nov 2016, 7:43 PM
 */

public class MarkDownProvider {

private static final String[] IMAGE_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif", ".svg"};

private static final String[] MARKDOWN_EXTENSIONS = {
	".md", ".mkdn", ".mdwn", ".mdown", ".markdown", ".mkd", ".mkdown", ".ron", ".rst", "adoc"
};

private static final String[] ARCHIVE_EXTENSIONS = {
	".zip", ".7z", ".rar", ".tar.gz", ".tgz", ".tar.Z", ".tar.bz2", ".tbz2", ".tar.lzma", ".tlz", ".apk", ".jar",
	".dmg", ".pdf", ".ico", ".docx", ".doc", ".xlsx", ".hwp", ".pptx", ".show", ".mp3", ".ogg", ".ipynb"
};

private MarkDownProvider() {
}

public static void setMdText(final @NonNull TextView textView, final String markdown) {
	if (!InputHelper.isEmpty(markdown)) {
		int width = textView.getMeasuredWidth();
		if (width > 0) {
			render(textView, markdown, width);
		} else {
			textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
					@Override public boolean onPreDraw() {
					        textView.getViewTreeObserver().removeOnPreDrawListener(this);
					        render(textView, markdown, textView.getMeasuredWidth());
					        return true;
					}
				});
		}
	}
}

public static void setMdText(final @NonNull TextView textView, final String markdown, final int width) {
	if (!InputHelper.isEmpty(markdown)) {
		render(textView, markdown, width);
	}
}

protected static void render(final @NonNull TextView textView, final String markdown, final int width) {
	List<Extension> extensions = Arrays.asList(
		StrikethroughExtension.create(),
		AutolinkExtension.create(),
		TablesExtension.create(),
		InsExtension.create(),
		EmojiExtension.create(),
		MentionExtension.create(),
		YamlFrontMatterExtension.create());
	Parser parser = Parser.builder()
	                .extensions(extensions)
	                .build();
	try {
		Node node = parser.parse(markdown);
		String rendered = HtmlRenderer
		                  .builder()
		                  .extensions(extensions)
		                  .build()
		                  .render(node);
		HtmlHelper.htmlIntoTextView(textView, rendered, (width - (textView.getPaddingStart() + textView.getPaddingEnd())));
	} catch (Exception ignored) {
		HtmlHelper.htmlIntoTextView(textView, markdown, (width - (textView.getPaddingStart() + textView.getPaddingEnd())));
	}
}

public static void stripMdText(final @NonNull TextView textView, final String markdown) {
	if (!InputHelper.isEmpty(markdown)) {
		Parser parser = Parser.builder().build();
		Node node = parser.parse(markdown);
		textView.setText(stripHtml(HtmlRenderer.builder().build().render(node)));
	}
}

@NonNull public static String stripMdText(final String markdown) {
	if (!InputHelper.isEmpty(markdown)) {
		Parser parser = Parser.builder().build();
		Node node = parser.parse(markdown);
		return stripHtml(HtmlRenderer.builder().build().render(node));
	}
	return "";
}

public static String stripHtml(final String html) {
	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
		return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
	} else {
		return Html.fromHtml(html).toString();
	}
}

public static void addList(final @NonNull EditText editText, final @NonNull String list) {
	String tag = list + " ";
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	int selectionEnd = editText.getSelectionEnd();
	String substring = source.substring(0, selectionStart);
	int line = substring.lastIndexOf(10);
	if (line != -1) {
		selectionStart = line + 1;
	} else {
		selectionStart = 0;
	}
	substring = source.substring(selectionStart, selectionEnd);
	String[] split = substring.split("\n");
	StringBuilder stringBuffer = new StringBuilder();
	if (split.length > 0)
		for (String s : split) {
			if (s.length() == 0 && stringBuffer.length() != 0) {
				stringBuffer.append("\n");
				continue;
			}
			if (!s.trim().startsWith(tag)) {
				if (stringBuffer.length() > 0) stringBuffer.append("\n");
				stringBuffer.append(tag).append(s);
			} else {
				if (stringBuffer.length() > 0) stringBuffer.append("\n");
				stringBuffer.append(s);
			}
		}

	if (stringBuffer.length() == 0) {
		stringBuffer.append(tag);
	}
	editText.getText().replace(selectionStart, selectionEnd, stringBuffer.toString());
	editText.setSelection(stringBuffer.length() + selectionStart);
}

public static void addHeader(final @NonNull EditText editText, final int level) {
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	int selectionEnd = editText.getSelectionEnd();
	StringBuilder result = new StringBuilder();
	String substring = source.substring(selectionStart, selectionEnd);
	if (!hasNewLine(source, selectionStart))
		result.append("\n");
	IntStream.range(0, level).forEach(integer->result.append("#"));
	result.append(" ").append(substring);
	editText.getText().replace(selectionStart, selectionEnd, result.toString());
	editText.setSelection(selectionStart + result.length());

}

public static void addItalic(final @NonNull EditText editText) {
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	int selectionEnd = editText.getSelectionEnd();
	String substring = source.substring(selectionStart, selectionEnd);
	String result = "_" + substring + "_ ";
	editText.getText().replace(selectionStart, selectionEnd, result);
	editText.setSelection(result.length() + selectionStart - 2);

}

public static void addBold(final @NonNull EditText editText) {
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	int selectionEnd = editText.getSelectionEnd();
	String substring = source.substring(selectionStart, selectionEnd);
	String result = "**" + substring + "** ";
	editText.getText().replace(selectionStart, selectionEnd, result);
	editText.setSelection(result.length() + selectionStart - 3);

}

public static void addCode(final @NonNull EditText editText) {
	try {
		String source = editText.getText().toString();
		int selectionStart = editText.getSelectionStart();
		int selectionEnd = editText.getSelectionEnd();
		String substring = source.substring(selectionStart, selectionEnd);
		String result;
		if (hasNewLine(source, selectionStart))
			result = "```\n" + substring + "\n```\n";
		else
			result = "\n```\n" + substring + "\n```\n";

		editText.getText().replace(selectionStart, selectionEnd, result);
		editText.setSelection(result.length() + selectionStart - 5);

	} catch (Exception e) {
		e.printStackTrace();
	}
}

public static void addInlinleCode(final @NonNull EditText editText) {
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	int selectionEnd = editText.getSelectionEnd();
	String substring = source.substring(selectionStart, selectionEnd);
	String result = "`" + substring + "` ";
	editText.getText().replace(selectionStart, selectionEnd, result);
	editText.setSelection(result.length() + selectionStart - 2);

}

public static void addStrikeThrough(final @NonNull EditText editText) {
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	int selectionEnd = editText.getSelectionEnd();
	String substring = source.substring(selectionStart, selectionEnd);
	String result = "~~" + substring + "~~ ";
	editText.getText().replace(selectionStart, selectionEnd, result);
	editText.setSelection(result.length() + selectionStart - 3);

}

public static void addQuote(final @NonNull EditText editText) {
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	int selectionEnd = editText.getSelectionEnd();
	String substring = source.substring(selectionStart, selectionEnd);
	String result;
	if (hasNewLine(source, selectionStart)) {
		result = "> " + substring;
	} else {
		result = "\n> " + substring;

	}
	editText.getText().replace(selectionStart, selectionEnd, result);
	editText.setSelection(result.length() + selectionStart);

}

public static void addDivider(final @NonNull EditText editText) {
	String source = editText.getText().toString();
	int selectionStart = editText.getSelectionStart();
	String result;
	if (hasNewLine(source, selectionStart)) {
		result = "-------\n";
	} else {
		result = "\n-------\n";
	}
	editText.getText().replace(selectionStart, selectionStart, result);
	editText.setSelection(result.length() + selectionStart);

}

public static void addPhoto(final @NonNull EditText editText, final @NonNull String title, final @NonNull String link) {
	String result = "![" + InputHelper.toString(title) + "](" + InputHelper.toString(link) + ")";
	insertAtCursor(editText, result);
}

public static void addLink(final @NonNull EditText editText, final @NonNull String title, final @NonNull String link) {
	String result = "[" + InputHelper.toString(title) + "](" + InputHelper.toString(link) + ")";
	insertAtCursor(editText, result);
}

private static boolean hasNewLine(final @NonNull String source, final int selectionStart) {
	try {
		if (source.isEmpty()) return true;
		source = source.substring(0, selectionStart);
		return source.charAt(source.length() - 1) == 10;
	} catch (StringIndexOutOfBoundsException e) {
		return false;
	}
}

public static boolean isImage(final @Nullable String name) {
	if (InputHelper.isEmpty(name)) return false;
	name = name.toLowerCase();
	for (String value : IMAGE_EXTENSIONS) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(name);
		if ((extension != null && value.replace(".", "").equals(extension)) || name.endsWith(value)) return true;
	}
	return false;
}

public static boolean isMarkdown(final @Nullable String name) {
	if (InputHelper.isEmpty(name)) return false;
	name = name.toLowerCase();
	for (String value : MARKDOWN_EXTENSIONS) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(name);
		if ((extension != null && value.replace(".", "").equals(extension))
		    || name.equalsIgnoreCase("README") || name.endsWith(value))
			return true;
	}
	return false;
}

public static boolean isArchive(final @Nullable String name) {
	if (InputHelper.isEmpty(name)) return false;
	name = name.toLowerCase();
	for (String value : ARCHIVE_EXTENSIONS) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(name);
		if ((extension != null && value.replace(".", "").equals(extension)) || name.endsWith(value)) return true;
	}

	return false;
}

public static void insertAtCursor(final @NonNull EditText editText, final @NonNull String text) {
	String oriContent = editText.getText().toString();
	int start = editText.getSelectionStart();
	int end = editText.getSelectionEnd();
	Logger.e(start, end);
	if (start >= 0 && end > 0 && start != end) {
		editText.setText(editText.getText().replace(start, end, text));
	} else {
		int index = editText.getSelectionStart() >= 0 ? editText.getSelectionStart() : 0;
		Logger.e(start, end, index);
		StringBuilder builder = new StringBuilder(oriContent);
		builder.insert(index, text);
		editText.setText(builder.toString());
		editText.setSelection(index + text.length());
	}
}
}
