package com.weibo.OAuth;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.daos.DBAdapter;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.User;
import com.weibo.utils.Contants;

public class OAuthActivity extends Activity {

	CommonsHttpOAuthConsumer httpOauthConsumer;
	OAuthProvider httpOauthprovider;

	String token;
	String tokenSecret;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insertresult);
		Log.v("TAG", "start to get the authorized");
		String consumerKey = "2902988107";
		String consumerSecret = "2fce81acf8fc9afb51ffc533688fa553";
		Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();
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

		int userId = accessToken.getUserId();
		String userKey = accessToken.getToken();
		String userSecret = accessToken.getTokenSecret();
		ContentValues cv = new ContentValues();
		cv.put(User.ID, userId);
		cv.put(User.TOKEN, userKey);
		cv.put(User.TOKENSECRET, userSecret);
		Log.v("TAG", userId + "   " + userKey + "   " + userSecret);
		DBAdapter dba = new DBAdapter(this, Contants.dbName, Contants.dbVersion);
		dba.open();
		dba.insertData(userId + "", userKey, userSecret, token, tokenSecret);
		startActivity(new Intent(this, IndexActivity.class));
	}

}
