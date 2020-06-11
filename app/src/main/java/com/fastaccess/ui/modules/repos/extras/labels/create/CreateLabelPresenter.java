package com.fastaccess.ui.modules.repos.extras.labels.create;

import android.view.View;
import androidx.annotation.NonNull;
import com.fastaccess.data.dao.LabelModel;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

/**
 * Created by Kosh on 02 Apr 2017, 5:35 PM
 */

public class CreateLabelPresenter extends BasePresenter<CreateLabelMvp.View>
    implements CreateLabelMvp.Presenter {

  @Override
  public void onItemClick(final int position, final View v, final String item) {
    if (getView() != null) {
      getView().onColorSelected(item);
    }
  }

  @Override
  public void onItemLongClick(final int position, final View v,
                              final String item) {}

  @Override
  public void
  onSubmitLabel(final @NonNull String name, final @NonNull String color,
                final @NonNull String repo, final @NonNull String login) {
    LabelModel labelModel = new LabelModel();
    labelModel.setColor(color.replaceAll("#", ""));
    labelModel.setName(name);
    makeRestCall(
        RestProvider.getRepoService(isEnterprise())
            .addLabel(login, repo, labelModel),
        labelModel1
        -> sendToView(view -> view.onSuccessfullyCreated(labelModel1)));
  }
}
