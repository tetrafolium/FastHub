package com.fastaccess.ui.adapter;

import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.fastaccess.data.dao.model.RepoFile;
import com.fastaccess.ui.adapter.viewholder.RepoFilesViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class RepoFilesAdapter extends BaseRecyclerAdapter<RepoFile, RepoFilesViewHolder, BaseViewHolder
    .OnItemClickListener<RepoFile>> {


    public RepoFilesAdapter(final @NonNull ArrayList<RepoFile> eventsModels) {
        super(eventsModels);
    }

    @Override protected RepoFilesViewHolder viewHolder(final ViewGroup parent, final int viewType) {
        return RepoFilesViewHolder.newInstance(parent, this);
    }

    @Override protected void onBindView(final RepoFilesViewHolder holder, final int position) {
        holder.bind(getItem(position));
    }
}
