package com.weibo.OAuth;

import java.util.SortedSet;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.weibo.R;
import com.weibo.daos.DBAdapter;
import com.weibo.pojo.UserInfo.User;
import com.weibo.utils.Contants;

public class OAuthActivity extends Activity {

	CommonsHttpOAuthConsumer httpOauthConsumer;
	OAuthProvider httpOauthprovider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insertresult);

		String consumerKey = "2902988107";
		String consumerSecret = "2fce81acf8fc9afb51ffc533688fa553";
		String callBackUrl = "myapp://OAuthActivity";

		try {
			httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey,
					consumerSecret);
			httpOauthprovider = new DefaultOAuthProvider(
					"http://api.t.sina.com.cn/oauth/request_token",
					"http://api.t.sina.com.cn/oauth/access_token",
					"http://api.t.sina.com.cn/oauth/authorize");
			String authUrl = httpOauthprovider.retrieveRequestToken(
					httpOauthConsumer, callBackUrl);
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));

		} catch (Exception e) {
			String s = e.getMessage();
			Log.v("TAG", s);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// super.onNewIntent(intent);
		Uri uri = intent.getData();
		String verifier = uri
				.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
		try {
			httpOauthprovider.setOAuth10a(true);
			httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
		} catch (OAuthMessageSignerException ex) {
			ex.printStackTrace();
		} catch (OAuthNotAuthorizedException ex) {
			ex.printStackTrace();
		} catch (OAuthExpectationFailedException ex) {
			ex.printStackTrace();
		} catch (OAuthCommunicationException ex) {
			ex.printStackTrace();
		}
		SortedSet<String> user_id = httpOauthprovider.getResponseParameters()
				.get("user_id");
		String userId = user_id.first();
		String userKey = httpOauthConsumer.getToken();
		String userSecret = httpOauthConsumer.getTokenSecret();
		ContentValues cv = new ContentValues();
		cv.put(User.ID, userId);
		cv.put(User.TOKEN, userKey);
		cv.put(User.TOKENSECRET, userSecret);
		DBAdapter dba = new DBAdapter(this, Contants.dbName, Contants.dbVersion);
		dba.open();
		dba.insertData(userId, userKey, userSecret);
		Toast.makeText(this, "Insert OK", 3000);

		Toast.makeText(this, "Get the Token", 3000);
	}

}
