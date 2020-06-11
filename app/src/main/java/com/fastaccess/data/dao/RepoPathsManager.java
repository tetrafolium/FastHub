package com.fastaccess.data.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fastaccess.data.dao.model.RepoFile;
import java.util.HashMap;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * Created by Kosh on 03 Mar 2017, 10:43 PM
 */

@NoArgsConstructor
public class RepoPathsManager {
  private HashMap<String, List<RepoFile>> files = new HashMap<>();

  @Nullable
  public List<RepoFile> getPaths(final @NonNull String url,
                                 final @NonNull String ref) {
    return files.get(ref + "/" + url);
  }

  public void setFiles(final @NonNull String ref, final @NonNull String path,
                       final @NonNull List<RepoFile> paths) {
    files.put(ref + "/" + path, paths);
  }

  public void clear() { files.clear(); }
}
