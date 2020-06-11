package com.fastaccess.ui.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.view.ContextThemeWrapper;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.evernote.android.state.StateSaver;
import com.fastaccess.App;
import com.fastaccess.R;
import com.fastaccess.helper.AnimHelper;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.widgets.dialog.ProgressDialogFragment;
import net.grandcentrix.thirtyinch.TiDialogFragment;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Kosh on 22 Feb 2017, 7:28 PM
 */

public abstract class BaseDialogFragment<V extends BaseMvp.FAView, P
                                             extends BasePresenter<V>>
    extends TiDialogFragment<P, V> implements BaseMvp.FAView {
  protected BaseMvp.FAView callback;

  @Nullable private Unbinder unbinder;
  protected boolean suppressAnimation = false;

  @LayoutRes protected abstract int fragmentLayout();

  protected abstract void
  onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

  @Override
  public void onAttach(final @NotNull Context context) {
    super.onAttach(context);
    if (context instanceof BaseMvp.FAView) {
      callback = (BaseMvp.FAView)context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    callback = null;
  }

  @Override
  public void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    StateSaver.saveInstanceState(this, outState);
    getPresenter().onSaveInstanceState(outState);
  }

  @Override
  public void onCreate(final @Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NO_TITLE, AppHelper.isNightMode(getResources())
                                 ? R.style.DialogThemeDark
                                 : R.style.DialogThemeLight);
    if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
      StateSaver.restoreInstanceState(this, savedInstanceState);
      getPresenter().onRestoreInstanceState(savedInstanceState);
    }
    getPresenter().setEnterprise(isEnterprise());
  }

  @Override
  public void dismiss() {
    if (suppressAnimation) {
      super.dismiss();
      return;
    }
    if (PrefGetter.isAppAnimationDisabled()) {
      super.dismiss();
    } else {
      AnimHelper.dismissDialog(
          this,
          getResources().getInteger(android.R.integer.config_shortAnimTime),
          new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
              super.onAnimationEnd(animation);
              BaseDialogFragment.super.dismiss();
            }
          });
    }
  }

  @SuppressLint("RestrictedApi")
  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater,
                           final ViewGroup container,
                           final Bundle savedInstanceState) {
    if (fragmentLayout() != 0) {
      final Context contextThemeWrapper =
          new ContextThemeWrapper(getContext(), requireContext().getTheme());
      LayoutInflater themeAwareInflater =
          inflater.cloneInContext(contextThemeWrapper);
      View view =
          themeAwareInflater.inflate(fragmentLayout(), container, false);
      unbinder = ButterKnife.bind(this, view);
      return view;
    }
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(final Bundle savedInstanceState) {
    final Dialog dialog = super.onCreateDialog(savedInstanceState);
    if (!PrefGetter.isAppAnimationDisabled() &&
        !(this instanceof ProgressDialogFragment) && !suppressAnimation) {
      dialog.setOnShowListener(
          dialogInterface
          -> AnimHelper.revealDialog(
              dialog, App.getInstance().getResources().getInteger(
                          android.R.integer.config_longAnimTime)));
    }
    return dialog;
  }

  @Override
  public void onViewCreated(final @NotNull View view,
                            final @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    onFragmentCreated(view, savedInstanceState);
  }

  @Override
  public void showProgress(final @StringRes int resId) {
    callback.showProgress(resId);
  }

  @Override
  public void showBlockingProgress(final int resId) {
    callback.showBlockingProgress(resId);
  }

  @Override
  public void hideProgress() {
    callback.hideProgress();
  }

  @Override
  public void showMessage(final @StringRes int titleRes,
                          final @StringRes int msgRes) {
    callback.showMessage(titleRes, msgRes);
  }

  @Override
  public void showMessage(final @NonNull String titleRes,
                          final @NonNull String msgRes) {
    callback.showMessage(titleRes, msgRes);
  }

  @Override
  public void showErrorMessage(final @NonNull String msgRes) {
    callback.showErrorMessage(msgRes);
  }

  @Override
  public boolean isLoggedIn() {
    return callback.isLoggedIn();
  }

  @Override
  public void onMessageDialogActionClicked(final boolean isOk,
                                           final @Nullable Bundle bundle) {}

  @Override
  public void onRequireLogin() {
    callback.onRequireLogin();
  }

  @Override
  public void onLogoutPressed() {
    callback.onLogoutPressed();
  }

  @Override
  public void onThemeChanged() {
    callback.onThemeChanged();
  }

  @Override
  public void onOpenSettings() {
    callback.onOpenSettings();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (unbinder != null)
      unbinder.unbind();
  }

  @Override
  public void onScrollTop(final int index) {}

  @Override
  public void onDialogDismissed() {}

  @Override
  public boolean isEnterprise() {
    return callback != null && callback.isEnterprise();
  }

  @Override
  public void onOpenUrlInBrowser() {
    callback.onOpenUrlInBrowser();
  }
}
