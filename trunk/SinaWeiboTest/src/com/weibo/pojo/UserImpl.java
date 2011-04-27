package com.weibo.pojo;

import android.graphics.Bitmap;

public class UserImpl {

	private static String userScreenName;

	private static String fromSite;

	private static String userHeadUrl;

	private static Bitmap userHead;

	public static String getUserScreenName() {
		return userScreenName;
	}

	public static void setUserScreenName(String userScreenName) {
		UserImpl.userScreenName = userScreenName;
	}

	public static String getFromSite() {
		return fromSite;
	}

	public static void setFromSite(String fromSite) {
		UserImpl.fromSite = fromSite;
	}

	public static String getUserHeadUrl() {
		return userHeadUrl;
	}

	public static void setUserHeadUrl(String userHeadUrl) {
		UserImpl.userHeadUrl = userHeadUrl;
	}

	public static Bitmap getUserHead() {
		return userHead;
	}

	public static void setUserHead(Bitmap userHead) {
		UserImpl.userHead = userHead;
	}

}
