package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Kosh on 01 May 2017, 1:05 AM
 */

@Getter @Setter public class SlackResponseModel implements Parcelable {
private boolean ok;
private String error;

@Override public int describeContents() {
	return 0;
}

@Override public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeByte(this.ok ? (byte) 1 : (byte) 0);
	dest.writeString(this.error);
}

public SlackResponseModel() {
}

private SlackResponseModel(final Parcel in) {
	this.ok = in.readByte() != 0;
	this.error = in.readString();
}

public static final Parcelable.Creator<SlackResponseModel> CREATOR = new Parcelable.Creator<SlackResponseModel>() {
	@Override public SlackResponseModel createFromParcel(final Parcel source) {
		return new SlackResponseModel(source);
	}

	@Override public SlackResponseModel[] newArray(final int size) {
		return new SlackResponseModel[size];
	}
};
}
