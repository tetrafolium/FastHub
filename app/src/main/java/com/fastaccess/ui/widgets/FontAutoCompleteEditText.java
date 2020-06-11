package com.fastaccess.ui.widgets;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.fastaccess.helper.TypeFaceHelper;

/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontAutoCompleteEditText extends AppCompatAutoCompleteTextView {

    public FontAutoCompleteEditText(final @NonNull Context context) {
        super(context);
        init();
    }

    public FontAutoCompleteEditText(final @NonNull Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public FontAutoCompleteEditText(final @NonNull Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        if (isInEditMode()) return;
        setInputType(getInputType() | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        setImeOptions(getImeOptions() | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        TypeFaceHelper.applyTypeface(this);
    }
}
