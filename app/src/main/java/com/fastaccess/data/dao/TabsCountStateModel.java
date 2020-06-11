package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Created by Kosh on 27 Apr 2017, 6:10 PM
 */

public class TabsCountStateModel implements Parcelable, Serializable {
private int count;
private int tabIndex;
@DrawableRes private int drawableId;

public int getCount() {
	return count;
}

public void setCount(final int count) {
	this.count = count;
}

public int getTabIndex() {
	return tabIndex;
}

public void setTabIndex(final int tabIndex) {
	this.tabIndex = tabIndex;
}

public int getDrawableId() {
	return drawableId;
}

public void setDrawableId(final int drawableId) {
	this.drawableId = drawableId;
}

@Override public boolean equals(final Object o) {
	if (this == o) return true;
	if (o == null || getClass() != o.getClass()) return false;

	TabsCountStateModel model = (TabsCountStateModel) o;

	return tabIndex == model.tabIndex;
}

@Override public int hashCode() {
	return tabIndex;
}

public TabsCountStateModel() {
}

@Override public int describeContents() {
	return 0;
}

@Override public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeInt(this.count);
	dest.writeInt(this.tabIndex);
	dest.writeInt(this.drawableId);
}

protected TabsCountStateModel(final Parcel in) {
	this.count = in.readInt();
	this.tabIndex = in.readInt();
	this.drawableId = in.readInt();
}

public static final Creator<TabsCountStateModel> CREATOR = new Creator<TabsCountStateModel>() {
	@Override public TabsCountStateModel createFromParcel(final Parcel source) {
		return new TabsCountStateModel(source);
	}

	@Override public TabsCountStateModel[] newArray(final int size) {
		return new TabsCountStateModel[size];
	}
};
}
