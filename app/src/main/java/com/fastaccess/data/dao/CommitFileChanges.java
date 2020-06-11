package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.annimon.stream.Stream;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Kosh on 20 Jun 2017, 7:32 PM
 */

@Getter
@Setter
public class CommitFileChanges implements Parcelable {

  public List<CommitLinesModel> linesModel;
  public CommitFileModel commitFileModel;

  private CommitFileChanges() {}

  public static Observable<CommitFileChanges>
  constructToObservable(final @Nullable ArrayList<CommitFileModel> files) {
    if (files == null || files.isEmpty())
      return Observable.empty();
    return Observable.fromIterable(construct(files));
  }

  @NonNull
  public static List<CommitFileChanges>
  construct(final @Nullable List<CommitFileModel> files) {
    if (files == null || files.isEmpty()) {
      return new ArrayList<>();
    }
    return Stream.of(files)
        .map(CommitFileChanges::getCommitFileChanges)
        .toList();
  }

  @NonNull
  private static CommitFileChanges
  getCommitFileChanges(final CommitFileModel m) {
    CommitFileChanges model = new CommitFileChanges();
    model.setLinesModel(CommitLinesModel.getLines(m.getPatch()));
    if (m.getPatch() != null) {
      m.setPatch("fake");
    }
    model.setCommitFileModel(m);
    return model;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeTypedList(this.linesModel);
    dest.writeParcelable(this.commitFileModel, flags);
  }

  private CommitFileChanges(final Parcel in) {
    this.linesModel = in.createTypedArrayList(CommitLinesModel.CREATOR);
    this.commitFileModel =
        in.readParcelable(CommitFileModel.class.getClassLoader());
  }

  public static final Creator<CommitFileChanges> CREATOR =
      new Creator<CommitFileChanges>() {
        @Override
        public CommitFileChanges createFromParcel(final Parcel source) {
          return new CommitFileChanges(source);
        }

        @Override
        public CommitFileChanges[] newArray(final int size) {
          return new CommitFileChanges[size];
        }
      };

  public static boolean canAttachToBundle(final CommitFileChanges model) {
    Parcel parcel = Parcel.obtain();
    model.writeToParcel(parcel, 0);
    int size = parcel.dataSize();
    return size < 600000;
  }

  @Override
  public String toString() {
    return "CommitFileChanges{"
        + "linesModel=" + linesModel + ", commitFileModel=" + commitFileModel +
        '}';
  }
}
