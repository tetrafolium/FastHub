package com.fastaccess.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ViewGroup;

import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.ui.adapter.viewholder.GistFilesViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class GistFilesAdapter extends BaseRecyclerAdapter<FilesListModel, GistFilesViewHolder, BaseViewHolder
    .OnItemClickListener<FilesListModel>> {
    private boolean isOwner;

    public GistFilesAdapter(final @NonNull List<FilesListModel> data,
                            final @Nullable BaseViewHolder.OnItemClickListener<FilesListModel> listener, final boolean isOwner) {
        super(data, listener);
        this.isOwner = isOwner;
    }

    public void setOwner(final boolean owner) {
        isOwner = owner;
    }

    @Override protected GistFilesViewHolder viewHolder(final ViewGroup parent, final int viewType) {
        return GistFilesViewHolder.newInstance(parent, this, isOwner);
    }

    @Override protected void onBindView(final GistFilesViewHolder holder, final int position) {
        holder.bind(getItem(position));
    }
}
