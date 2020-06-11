package com.fastaccess.ui.widgets.dialog;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.fastaccess.R;
import com.fastaccess.helper.Bundler;
import com.fastaccess.ui.base.BaseDialogFragment;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import net.grandcentrix.thirtyinch.TiPresenter;

/**
 * Created by Kosh on 09 Dec 2016, 5:18 PM
 */

public class ProgressDialogFragment extends BaseDialogFragment {

  public ProgressDialogFragment() { suppressAnimation = true; }

  public static final String TAG = ProgressDialogFragment.class.getSimpleName();

  @NonNull
  public static ProgressDialogFragment
  newInstance(final @NonNull Resources resources, final @StringRes int msgId,
              final boolean isCancelable) {
    return newInstance(resources.getString(msgId), isCancelable);
  }

  @NonNull
  public static ProgressDialogFragment newInstance(final @NonNull String msg,
                                                   final boolean isCancelable) {
    ProgressDialogFragment fragment = new ProgressDialogFragment();
    fragment.setArguments(Bundler.start()
                              .put("msg", msg)
                              .put("isCancelable", isCancelable)
                              .end());
    return fragment;
  }

  @Override
  protected int fragmentLayout() {
    return R.layout.progress_dialog_layout;
  }

  @Override
  protected void onFragmentCreated(final @NonNull View view,
                                   final @Nullable Bundle savedInstanceState) {}

  @NonNull
  @Override
  public Dialog onCreateDialog(final Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.setCancelable(false);
    setCancelable(false);
    Window window = dialog.getWindow();
    if (window != null) {
      window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      window.setDimAmount(0);
    }
    return dialog;
  }

  @NonNull
  @Override
  public TiPresenter providePresenter() {
    return new BasePresenter();
  }
}
