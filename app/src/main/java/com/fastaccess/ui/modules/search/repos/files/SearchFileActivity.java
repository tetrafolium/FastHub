package com.fastaccess.ui.modules.search.repos.files;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import com.fastaccess.R;
import com.fastaccess.helper.AnimHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.search.code.SearchCodeFragment;
import com.fastaccess.ui.widgets.FontEditText;
import com.fastaccess.ui.widgets.ForegroundImageView;

public class SearchFileActivity
    extends BaseActivity<SearchFileMvp.View, SearchFilePresenter>
    implements SearchFileMvp.View {

  @BindView(R.id.searchEditText) FontEditText searchEditText;
  @BindView(R.id.clear) ForegroundImageView clear;
  @BindView(R.id.searchOptions) AppCompatSpinner searchOptions;
  private boolean onSpinnerTouched;

  private SearchCodeFragment searchCodeFragment;

  public static Intent createIntent(final @NonNull Context context,
                                    final @NonNull String login,
                                    final @NonNull String repoId,
                                    final boolean isEnterprise) {
    Intent intent = new Intent(context, SearchFileActivity.class);
    intent.putExtra(BundleConstant.ID, repoId);
    intent.putExtra(BundleConstant.EXTRA, login);
    intent.putExtra(BundleConstant.IS_ENTERPRISE, isEnterprise);
    return intent;
  }

  @OnTouch(R.id.searchOptions)
  boolean onTouch() {
    onSpinnerTouched = true;
    return false;
  }

  @OnItemSelected(R.id.searchOptions)
  void onOptionSelected() {
    if (onSpinnerTouched) {
      onSearch();
    }
  }

  @OnTextChanged(value = R.id.searchEditText,
                 callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
  void
  onTextChange(final Editable s) {
    String text = s.toString();
    if (text.length() == 0) {
      AnimHelper.animateVisibility(clear, false);
    } else {
      AnimHelper.animateVisibility(clear, true);
    }
  }

  @OnEditorAction(R.id.searchEditText)
  boolean onEditor() {
    onSearch();
    return true;
  }

  @OnClick(value = {R.id.clear})
  void onClear(final View view) {
    if (view.getId() == R.id.clear) {
      searchEditText.setText("");
    }
  }

  @OnClick(R.id.search)
  void onSearchClicked() {
    onSearch();
  }

  @Override
  protected int layout() {
    return R.layout.activity_search_file;
  }

  @Override
  protected boolean isTransparent() {
    return false;
  }

  @Override
  protected boolean canBack() {
    return true;
  }

  @Override
  protected boolean isSecured() {
    return false;
  }

  @NonNull
  @Override
  public SearchFilePresenter providePresenter() {
    return new SearchFilePresenter();
  }

  @Override
  protected void onCreate(final @Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPresenter().onActivityCreated(getIntent().getExtras());
    searchCodeFragment =
        ((SearchCodeFragment)getSupportFragmentManager().findFragmentById(
            R.id.filesFragment));
  }

  @Override
  public void onValidSearchQuery(final @NonNull String query) {
    searchCodeFragment.onSetSearchQuery(query, false);
  }

  private void onSearch() {
    getPresenter().onSearchClicked(
        searchEditText, searchOptions.getSelectedItemPosition() == 0);
  }
}
