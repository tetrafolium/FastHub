package com.fastaccess.data.dao.timeline;

import android.os.Parcel;
import android.os.Parcelable;

import com.fastaccess.data.dao.model.Commit;
import com.fastaccess.data.dao.model.Issue;
import com.fastaccess.data.dao.model.PullRequest;
import com.fastaccess.data.dao.model.Repo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kosh on 26/07/2017.
 */

@Getter @Setter public class SourceModel implements Parcelable {

    private String type;
    private Issue issue;
    private PullRequest pullRequest;
    private Commit commit;
    private Repo repository;

    public SourceModel() { }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.type);
        dest.writeParcelable(this.issue, flags);
        dest.writeParcelable(this.pullRequest, flags);
        dest.writeParcelable(this.commit, flags);
        dest.writeParcelable(this.repository, flags);
    }

    private SourceModel(final Parcel in) {
        this.type = in.readString();
        this.issue = in.readParcelable(Issue.class.getClassLoader());
        this.pullRequest = in.readParcelable(PullRequest.class.getClassLoader());
        this.commit = in.readParcelable(Commit.class.getClassLoader());
        this.repository = in.readParcelable(Repo.class.getClassLoader());
    }

    public static final Creator<SourceModel> CREATOR = new Creator<SourceModel>() {
        @Override public SourceModel createFromParcel(final Parcel source) {
            return new SourceModel(source);
        }

        @Override public SourceModel[] newArray(final int size) {
            return new SourceModel[size];
        }
    };
}
