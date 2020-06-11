package com.fastaccess.ui.widgets;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

import com.fastaccess.helper.TypeFaceHelper;


/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontButton extends AppCompatButton {

    public FontButton(final @NonNull Context context) {
        super(context);
        init();
    }

    public FontButton(final @NonNull Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontButton(final @NonNull Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        TypeFaceHelper.applyTypeface(this);
    }
}
