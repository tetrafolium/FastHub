package com.fastaccess.ui.modules.repos.pull_requests.pull_request.merge;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.ui.base.BaseDialogFragment;
import com.fastaccess.ui.modules.main.premium.PremiumActivity;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by Kosh on 18 Mar 2017, 12:13 PM
 */

public class MergePullRequestDialogFragment extends BaseDialogFragment<MergePullReqeustMvp.View, MergePullRequestPresenter>
	implements MergePullReqeustMvp.View {

@BindView(R.id.title) TextInputLayout title;
@BindView(R.id.mergeMethod) AppCompatSpinner mergeMethod;

private MergePullReqeustMvp.MergeCallback mergeCallback;

public static MergePullRequestDialogFragment newInstance(final @Nullable String title) {
	MergePullRequestDialogFragment view = new MergePullRequestDialogFragment();
	view.setArguments(Bundler.start()
	                  .put(BundleConstant.EXTRA, title)
	                  .end());
	return view;
}

@Override public void onAttach(final @NotNull Context context) {
	super.onAttach(context);
	if (context instanceof MergePullReqeustMvp.MergeCallback) {
		mergeCallback = (MergePullReqeustMvp.MergeCallback) context;
	} else if (getParentFragment() instanceof MergePullReqeustMvp.MergeCallback) {
		mergeCallback = (MergePullReqeustMvp.MergeCallback) getParentFragment();
	}
}

@Override public void onDetach() {
	mergeCallback = null;
	super.onDetach();
}

@Override protected int fragmentLayout() {
	return R.layout.merge_dialog_layout;
}

@Override protected void onFragmentCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
	if (savedInstanceState == null) {
		String titleMsg = getArguments().getString(BundleConstant.EXTRA);
		if (!InputHelper.isEmpty(titleMsg)) {
			if (title.getEditText() != null) title.getEditText().setText(titleMsg);
		}
	}
}

@NonNull @Override public MergePullRequestPresenter providePresenter() {
	return new MergePullRequestPresenter();
}

@OnClick({
		R.id.cancel, R.id.ok
	}) public void onClick(final View view) {
	if (view.getId() == R.id.ok) {
		boolean isEmpty = InputHelper.isEmpty(title);
		title.setError(isEmpty ? getString(R.string.required_field) : null);
		if (isEmpty) return;
		mergeCallback.onMerge(InputHelper.toString(title), mergeMethod.getSelectedItem().toString().toLowerCase());
	}
	dismiss();
}

@OnItemSelected(R.id.mergeMethod) void onItemSelect(final int position) {
	if (position > 0) {
		if (!PrefGetter.isProEnabled()) {
			mergeMethod.setSelection(0);
			PremiumActivity.Companion.startActivity(getContext());
		}
	}
}
}
