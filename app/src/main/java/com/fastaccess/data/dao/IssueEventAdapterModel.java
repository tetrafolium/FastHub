package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.annimon.stream.Stream;
import com.fastaccess.data.dao.model.Issue;
import com.fastaccess.data.dao.model.IssueEvent;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Kosh on 10 Dec 2016, 3:34 PM
 */

@Getter @Setter
public class IssueEventAdapterModel implements Parcelable {

public static final int HEADER = 1;
public static final int ROW = 2;
private int type;

private IssueEvent issueEvent;
private Issue issueModel;

private IssueEventAdapterModel(final int type, final IssueEvent model) {
	this.type = type;
	this.issueEvent = model;
}

public IssueEventAdapterModel(final int type, final Issue issueModel) {
	this.type = type;
	this.issueModel = issueModel;
}

public static ArrayList<IssueEventAdapterModel> addEvents(final @Nullable List<IssueEvent> modelList) {
	ArrayList<IssueEventAdapterModel> models = new ArrayList<>();
	if (modelList == null || modelList.isEmpty()) return models;
	Stream.of(modelList).forEach(issueEventModel->models.add(new IssueEventAdapterModel(ROW, issueEventModel)));
	return models;
}

public IssueEventAdapterModel() {
}

@Override public int describeContents() {
	return 0;
}

@Override public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeInt(this.type);
	dest.writeParcelable(this.issueEvent, flags);
	dest.writeParcelable(this.issueModel, flags);
}

private IssueEventAdapterModel(final Parcel in) {
	this.type = in.readInt();
	this.issueEvent = in.readParcelable(IssueEvent.class.getClassLoader());
	this.issueModel = in.readParcelable(Issue.class.getClassLoader());
}

public static final Creator<IssueEventAdapterModel> CREATOR = new Creator<IssueEventAdapterModel>() {
	@Override public IssueEventAdapterModel createFromParcel(final Parcel source) {
		return new IssueEventAdapterModel(source);
	}

	@Override public IssueEventAdapterModel[] newArray(final int size) {
		return new IssueEventAdapterModel[size];
	}
};
}
