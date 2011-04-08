package com.weibo.pojo;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserInfo {

	public static final class User implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.weibo.SinaWeiboContentProvider");

		public static final String ID = "ID";
		public static final String TOKEN = "TOKEN";
		public static final String TOKENSECRET = "TOKENSECRET";
		public static final String CREATE_TIME = "CREATE_TIME";
		public static final String MODIFY_TIME = "MODIFY_TIME";
	}

	private String UserId;
	private String Token;
	private String TokenSecret;

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userID) {
		UserId = userID;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getTokenSecret() {
		return TokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		TokenSecret = tokenSecret;
	}

}
