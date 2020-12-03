package com.fastaccess.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import com.fastaccess.helper.TypeFaceHelper;

/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontButton extends AppCompatButton {

  public FontButton(@NonNull Context context) {
    super(context);
    init();
  }

  public FontButton(@NonNull Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FontButton(@NonNull Context context, AttributeSet attrs,
                    int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    if (isInEditMode())
      return;
    TypeFaceHelper.applyTypeface(this);
  }
}
