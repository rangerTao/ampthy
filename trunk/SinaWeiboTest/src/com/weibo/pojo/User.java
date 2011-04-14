package com.weibo.pojo;

import android.net.Uri;
import android.provider.BaseColumns;

public final class User implements BaseColumns {
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.weibo.SinaWeiboContentProvider");

	public static final String ID = "ID";
	public static final String TOKEN = "TOKEN";
	public static final String TOKENSECRET = "TOKENSECRET";
	public static final String ACCESSTOKEN = "ACCESSTOKEN";
	public static final String ACCESSTOKENSECRET = "ACCESSTOKENSECRET";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String MODIFY_TIME = "MODIFY_TIME";
}
