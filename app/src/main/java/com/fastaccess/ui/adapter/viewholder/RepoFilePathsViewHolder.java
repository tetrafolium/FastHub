package com.fastaccess.ui.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import butterknife.BindView;
import com.fastaccess.R;
import com.fastaccess.data.dao.model.RepoFile;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

/**
 * Created by Kosh on 18 Feb 2017, 2:53 AM
 */

public class RepoFilePathsViewHolder extends BaseViewHolder<RepoFile> {

  @BindView(R.id.pathName) FontTextView pathName;

  private RepoFilePathsViewHolder(@NonNull View itemView,
                                  @NonNull BaseRecyclerAdapter baseAdapter) {
    super(itemView, baseAdapter);
  }

  public static RepoFilePathsViewHolder
  newInstance(ViewGroup viewGroup, BaseRecyclerAdapter adapter) {
    return new RepoFilePathsViewHolder(
        getView(viewGroup, R.layout.file_path_row_item), adapter);
  }

  @Override
  public void bind(@NonNull RepoFile filesModel) {
    pathName.setText(filesModel.getName());
  }
}
