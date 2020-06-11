package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Kosh on 19 Feb 2017, 12:13 PM
 */

@Getter @Setter @NoArgsConstructor
public class CreateIssueModel implements Parcelable {
private String title;
private String body;
private ArrayList<String> labels;
private ArrayList<String> assignees;
private Long milestone;

@Override public int describeContents() {
	return 0;
}

@Override public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeString(this.title);
	dest.writeString(this.body);
	dest.writeStringList(this.labels);
	dest.writeStringList(this.assignees);
	dest.writeValue(this.milestone);
}

protected CreateIssueModel(final Parcel in) {
	this.title = in.readString();
	this.body = in.readString();
	this.labels = in.createStringArrayList();
	this.assignees = in.createStringArrayList();
	this.milestone = (Long) in.readValue(Long.class.getClassLoader());
}

public static final Creator<CreateIssueModel> CREATOR = new Creator<CreateIssueModel>() {
	@Override public CreateIssueModel createFromParcel(final Parcel source) {
		return new CreateIssueModel(source);
	}

	@Override public CreateIssueModel[] newArray(final int size) {
		return new CreateIssueModel[size];
	}
};
}
