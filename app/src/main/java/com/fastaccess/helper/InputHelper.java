package com.fastaccess.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kosh20111 on 3/11/2015. CopyRights @
 * <p>
 * Input Helper to validate stuff related to input fields.
 */
public class InputHelper {

public static final String SPACE = "\u202F\u202F";

private static boolean isWhiteSpaces(final @Nullable String s) {
	return s != null && s.matches("\\s+");
}

public static boolean isEmpty(final @Nullable String text) {
	return text == null || TextUtils.isEmpty(text) || isWhiteSpaces(text) || text.equalsIgnoreCase("null");
}

public static boolean isEmpty(final @Nullable Object text) {
	return text == null || isEmpty(text.toString());
}

public static boolean isEmpty(final @Nullable EditText text) {
	return text == null || isEmpty(text.getText().toString());
}

public static boolean isEmpty(final @Nullable TextView text) {
	return text == null || isEmpty(text.getText().toString());
}

public static boolean isEmpty(final @Nullable TextInputLayout txt) {
	return txt == null || isEmpty(txt.getEditText());
}

public static String toString(final @NonNull EditText editText) {
	return editText.getText().toString();
}

public static String toString(final @NonNull TextView editText) {
	return editText.getText().toString();
}

public static String toString(final @NonNull TextInputLayout textInputLayout) {
	return textInputLayout.getEditText() != null ? toString(textInputLayout.getEditText()) : "";
}

@NonNull public static String toNA(final @Nullable String value) {
	return isEmpty(value) ? "N/A" : value;
}

@NonNull public static String toString(final @Nullable Object object) {
	return !isEmpty(object) ? object.toString() : "";
}

public static long toLong(final @NonNull TextView textView) {
	return toLong(toString(textView));
}

public static long toLong(final @NonNull String text) {
	if (!isEmpty(text)) {
		try {
			return Long.valueOf(text.replaceAll("[^0-9]", ""));
		} catch (NumberFormatException ignored) { }
	}
	return 0;
}

public static int getSafeIntId(final long id) {
	return id > Integer.MAX_VALUE ? (int) (id - Integer.MAX_VALUE) : (int) id;
}

public static String capitalizeFirstLetter(final String s) {
	if (isEmpty(s)) {
		return "";
	}
	char first = s.charAt(0);
	if (Character.isUpperCase(first)) {
		return s;
	} else {
		return Character.toUpperCase(first) + s.substring(1);
	}
}
}
