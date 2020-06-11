package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Kosh on 29 Mar 2017, 9:50 PM
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionModel implements Parcelable {

  private String content;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(this.content);
  }

  private PostReactionModel(final Parcel in) { this.content = in.readString(); }

  public static final Parcelable.Creator<PostReactionModel> CREATOR =
      new Parcelable.Creator<PostReactionModel>() {
        @Override
        public PostReactionModel createFromParcel(final Parcel source) {
          return new PostReactionModel(source);
        }

        @Override
        public PostReactionModel[] newArray(final int size) {
          return new PostReactionModel[size];
        }
      };
}
