package com.fastaccess.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.fastaccess.BuildConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kosh on 04/12/15 11:52 PM. copyrights @
 */
public class Logger {

    private final static String TAG = Logger.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static void e(final @NonNull String tag, final @Nullable Object text) {
        if (!DEBUG) return;
        Log.e(tag, text != null ? text.toString() : "LOGGER IS NULL"); //avoid null
    }

    private static void d(final @NonNull String tag, final @Nullable Object text) {
        if (!DEBUG) return;
        Log.d(tag, text != null ? text.toString() : "LOGGER IS NULL"); //avoid null
    }

    private static void i(final @NonNull String tag, final @Nullable Object text) {
        if (!DEBUG) return;
        Log.i(tag, text != null ? text.toString() : "LOGGER IS NULL"); //avoid null
    }

    public static void d(final @Nullable Object text) {
        d(getCurrentClassName() + " || " + getCurrentMethodName(), text); //avoid null
    }

    public static void i(final @Nullable Object text) {
        i(getCurrentClassName() + " || " + getCurrentMethodName(), text); //avoid null
    }

    public static void e(final Object... objects) {
        if (objects != null && objects.length > 0) {
            e(getCurrentClassName() + " || " + getCurrentMethodName(), Arrays.toString(objects));
        } else {
            e(getCurrentClassName() + " || " + getCurrentMethodName(), getCurrentMethodName());
        }
    }

    public static void e(final List<Object> objects) {
        if (objects != null) {
            e(getCurrentClassName() + " || " + getCurrentMethodName(), Arrays.toString(objects.toArray()));
        } else {
            e(TAG, null);
        }
    }

    private static String getCurrentMethodName() {
        try {
            return Thread.currentThread().getStackTrace()[4].getMethodName() + "()";
        } catch (Exception ignored) { }
        return TAG;
    }

    private static String getCurrentClassName() {
        try {
            String className = Thread.currentThread().getStackTrace()[4].getClassName();
            String[] temp = className.split("[.]");
            className = temp[temp.length - 1];
            return className;
        } catch (Exception ignored) { }
        return TAG;
    }
}
