package br.edsonluis.app.brasileirao.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class RSSItem implements Comparable<RSSItem>, Parcelable {

	public String title;
	public String link;
	private Date pubDate;
	public String description;
	public String content;

	public RSSItem() {

	}

	public RSSItem(Parcel source) {

		Bundle data = source.readBundle();
		title = data.getString("title");
		link = data.getString("link");
		pubDate = (Date) data.getSerializable("pubDate");
		description = data.getString("description");
		content = data.getString("content");

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		Bundle data = new Bundle();
		data.putString("title", title);
		data.putString("link", link);
		data.putSerializable("pubDate", pubDate);
		data.putString("description", description);
		data.putString("content", content);
		dest.writeBundle(data);
	}

	public static final Parcelable.Creator<RSSItem> CREATOR = new Parcelable.Creator<RSSItem>() {
		public RSSItem createFromParcel(Parcel data) {
			return new RSSItem(data);
		}

		public RSSItem[] newArray(int size) {
			return new RSSItem[size];
		}
	};

	@SuppressLint("SimpleDateFormat")
	public String getPubDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(pubDate);
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public void setPubDate(String pubDate) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
			this.pubDate = dateFormat.parse(pubDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(RSSItem another) {
		if (getPubDate() != null && another.getPubDate() != null) {
			return getPubDate().compareTo(another.getPubDate());
		} else {
			return 0;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getDescription() {

		String html = description.replaceAll("\\<.*?\\>", "");
		html = html.replaceAll("<(.*?)\\\n", "");
		html = html.replaceFirst("(.*?)\\>", "");
		html = html.replaceFirst("(.*?)\\/>", "");
		html = html.replaceAll("&nbsp;", "");
		html = html.replaceAll("&amp;", "");
		html = html.replace("Continue reading →", "");

		return html;
	}

}
