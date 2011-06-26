package com.weibo.OAuth;

import java.io.ByteArrayOutputStream;

import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.daos.DBAdapter;
import com.weibo.pojo.DBColumns;
import com.weibo.pojo.OAuthConstant;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class OAuthActivity extends Activity {

	String token;
	String tokenSecret;
	
	Weibo weibo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insertresult);
		Log.v("TAG", "start to get the authorized");
		String consumerKey = "2902988107";
		String consumerSecret = "2fce81acf8fc9afb51ffc533688fa553";
		weibo = OAuthConstant.getInstance().getWeibo();
		RequestToken requestToken;
		try {
			weibo.setOAuthConsumer(consumerKey, consumerSecret);
			requestToken = weibo.getOAuthRequestToken("myapp://OAuthActivity");
			token = requestToken.getToken();
			tokenSecret = requestToken.getTokenSecret();
			Uri uri = Uri.parse(requestToken.getAuthenticationURL());
			OAuthConstant.getInstance().setRequestToken(requestToken);
			startActivity(new Intent(Intent.ACTION_VIEW, uri));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.v("TAG", "Call back");

		Uri uri = intent.getData();
		AccessToken accessToken = null;
		try {
			RequestToken requestToken = OAuthConstant.getInstance()
					.getRequestToken();
			Log.v("TAG", uri == null ? "null" : "not null");
			accessToken = requestToken.getAccessToken(uri
					.getQueryParameter("oauth_verifier"));
			OAuthConstant.getInstance().setAccessToken(accessToken);
		} catch (WeiboException e) {
			e.printStackTrace();
		}

		int userId = (int) accessToken.getUserId();
		String userKey = accessToken.getToken();
		String userSecret = accessToken.getTokenSecret();
		
		Constant._token = token;
		Constant._tokenSecret = this.tokenSecret;
		Constant._access = userKey;
		Constant._accessSecret = userSecret;
		
		weibo.setOAuthConsumer(Constant.CONSUMER_KEY, Constant.CONSUMER_SECRET);
		weibo.setToken(token, tokenSecret);
		weibo.setOAuthAccessToken(userKey, userSecret);
		User user = null;
		try {
			user = weibo.getUserDetail(userId+"");
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ContentValues cv = new ContentValues();
		Bitmap userHead = WeiboUtils.getImage(user.getProfileImageURL());
		if(userHead != null){
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			userHead.compress(Bitmap.CompressFormat.PNG, 100, os);
			cv.put(DBColumns.USERHEADURI, os.toByteArray());
		}
		
		cv.put(DBColumns.ID, userId);
		cv.put(DBColumns.TOKEN, token);
		cv.put(DBColumns.TOKENSECRET, tokenSecret);
		cv.put(DBColumns.ACCESSTOKEN, userKey);
		cv.put(DBColumns.ACCESSTOKENSECRET, userSecret);
		cv.put(DBColumns.ScreenName, user.getScreenName());
		cv.put(DBColumns.SITE, "Sina");
		cv.put(DBColumns.USERHEADURL, user.getProfileImageURL().toString());
		Log.v("TAG", userId + "   " + userKey + "   " + userSecret);
		DBAdapter dba = new DBAdapter(this, Constant.dbName, Constant.dbVersion);
		dba.open();
		dba.insertData(userId + "", cv);
		startActivity(new Intent(this, IndexActivity.class));
	}

}
