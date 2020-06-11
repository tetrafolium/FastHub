package com.fastaccess.ui.adapter.viewholder;

import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import butterknife.BindView;
import com.fastaccess.R;
import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.ui.adapter.GistFilesAdapter;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.SpannableBuilder;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

/**
 * Created by Kosh on 12 Nov 2016, 3:44 PM
 */

public class GistFilesViewHolder extends BaseViewHolder<FilesListModel> {

  @BindView(R.id.fileName) FontTextView fileName;
  @BindView(R.id.language) FontTextView language;
  @BindView(R.id.size) FontTextView size;
  @BindView(R.id.delete) ForegroundImageView delete;
  @BindView(R.id.edit) ForegroundImageView edit;
  private boolean isOwner;

  private GistFilesViewHolder(final @NonNull View itemView,
                              final GistFilesAdapter adapter,
                              final boolean isOwner) {
    super(itemView, adapter);
    this.isOwner = isOwner;
    if (isOwner) {
      delete.setOnClickListener(this);
      edit.setOnClickListener(this);
    }
  }

  public static GistFilesViewHolder newInstance(final @NonNull ViewGroup parent,
                                                final GistFilesAdapter adapter,
                                                final boolean isOwner) {
    return new GistFilesViewHolder(
        getView(parent, R.layout.gist_files_row_item), adapter, isOwner);
  }

  @Override
  public void bind(final @NonNull FilesListModel filesListModel) {
    fileName.setText(filesListModel.getFilename());
    language.setText(SpannableBuilder.builder().bold(filesListModel.getType()));
    size.setText(
        Formatter.formatFileSize(size.getContext(), filesListModel.getSize()));
    delete.setVisibility(isOwner ? View.VISIBLE : View.GONE);
    edit.setVisibility(isOwner ? View.VISIBLE : View.GONE);
  }
}
