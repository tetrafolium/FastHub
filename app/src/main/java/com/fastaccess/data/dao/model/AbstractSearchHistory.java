package com.fastaccess.data.dao.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fastaccess.App;
import com.fastaccess.helper.RxHelper;

import java.util.List;

import io.reactivex.Single;
import io.requery.Column;
import io.requery.Entity;

/**
 * Created by Kosh on 01 Jan 2017, 11:20 PM
 */


@Entity
public abstract class AbstractSearchHistory implements Parcelable {
@Column(unique = true) String text;

public Single<SearchHistory> save(final SearchHistory entity) {
	return RxHelper.getSingle(
		App.getInstance().getDataStore()
		.delete(SearchHistory.class)
		.where(SearchHistory.TEXT.eq(entity.getText()))
		.get()
		.single()
		.flatMap(integer->App.getInstance().getDataStore().insert(entity)));
}

public static Single<List<SearchHistory> > getHistory() {
	return App.getInstance().getDataStore()
	       .select(SearchHistory.class)
	       .groupBy(SearchHistory.TEXT.asc())
	       .get()
	       .observable()
	       .toList();
}

@Override public String toString() {
	return text;
}

@Override public boolean equals(final Object o) {
	if (this == o) return true;
	if (o == null || getClass() != o.getClass()) return false;

	AbstractSearchHistory that = (AbstractSearchHistory) o;

	return text != null ? text.equals(that.text) : that.text == null;

}

@Override public int hashCode() {
	return text != null ? text.hashCode() : 0;
}

@Override public int describeContents() {
	return 0;
}

@Override public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeString(this.text);
}

public AbstractSearchHistory() {
}

public AbstractSearchHistory(final String text) {
	this.text = text;
}

protected AbstractSearchHistory(final Parcel in) {
	this.text = in.readString();
}

public static final Creator<SearchHistory> CREATOR = new Creator<SearchHistory>() {
	@Override public SearchHistory createFromParcel(final Parcel source) {
		return new SearchHistory(source);
	}

	@Override public SearchHistory[] newArray(final int size) {
		return new SearchHistory[size];
	}
};

public static void deleteAll() {
	App.getInstance().getDataStore()
	.delete(SearchHistory.class)
	.get()
	.value();
}
}
