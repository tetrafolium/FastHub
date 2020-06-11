package com.fastaccess.ui.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.model.RepoFile;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 18 Feb 2017, 2:53 AM
 */

public class RepoFilePathsViewHolder extends BaseViewHolder<RepoFile> {

    @BindView(R.id.pathName) FontTextView pathName;

    private RepoFilePathsViewHolder(final @NonNull View itemView, final @NonNull BaseRecyclerAdapter baseAdapter) {
        super(itemView, baseAdapter);
    }

    public static RepoFilePathsViewHolder newInstance(final ViewGroup viewGroup, final BaseRecyclerAdapter adapter) {
        return new RepoFilePathsViewHolder(getView(viewGroup, R.layout.file_path_row_item), adapter);
    }

    @Override public void bind(final @NonNull RepoFile filesModel) {
        pathName.setText(filesModel.getName());
    }
}
