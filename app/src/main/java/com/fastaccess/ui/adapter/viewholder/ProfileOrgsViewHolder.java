package com.fastaccess.ui.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import butterknife.BindView;
import com.fastaccess.R;
import com.fastaccess.data.dao.model.User;
import com.fastaccess.provider.scheme.LinkParserHelper;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

/**
 * Created by Kosh on 21 May 2017, 12:27 PM
 */

public class ProfileOrgsViewHolder extends BaseViewHolder<User> {

  @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
  @BindView(R.id.name) FontTextView name;

  @Override
  public void onClick(final View v) {
    avatarLayout.callOnClick();
  }

  private ProfileOrgsViewHolder(final @NonNull View itemView) {
    super(itemView);
  }

  public static ProfileOrgsViewHolder newInstance(final
                                                  @NonNull ViewGroup parent) {
    return new ProfileOrgsViewHolder(
        getView(parent, R.layout.profile_org_row_item));
  }

  @Override
  public void bind(final @NonNull User user) {
    name.setText(user.getLogin());
    avatarLayout.setUrl(user.getAvatarUrl(), user.getLogin(), true,
                        LinkParserHelper.isEnterprise(user.getUrl()));
  }
}
