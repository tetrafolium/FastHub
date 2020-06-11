package com.fastaccess.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import com.crashlytics.android.Crashlytics;
import com.fastaccess.helper.TypeFaceHelper;

/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontEditText extends AppCompatEditText {

  public FontEditText(final @NonNull Context context) {
    super(context);
    init();
  }

  public FontEditText(final @NonNull Context context,
                      final AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FontEditText(final @NonNull Context context, final AttributeSet attrs,
                      final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    if (isInEditMode())
      return;
    setInputType(getInputType() | EditorInfo.IME_FLAG_NO_EXTRACT_UI |
                 EditorInfo.IME_FLAG_NO_FULLSCREEN);
    setImeOptions(getImeOptions() | EditorInfo.IME_FLAG_NO_FULLSCREEN);
    TypeFaceHelper.applyTypeface(this);
  }

  @SuppressLint("SetTextI18n")
  public void setText(final CharSequence text, final BufferType type) {
    try {
      super.setText(text, type);
    } catch (Exception e) {
      setText(
          "I tried, but your OEM just sucks because they modify the framework components and therefore causing the app to crash!"
          + ".\nFastHub");
      Crashlytics.logException(e);
    }
  }
}
