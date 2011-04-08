package com.weibo;

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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weibo.daos.DBAdapter;
import com.weibo.pojo.UserInfo.User;
import com.weibo.utils.Contants;

public class Weibo extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	EditText etName;
	EditText etPasswd;
	Button btnLogin;
	Button btnCancel;
	
	Weibo appref;
	public final static String tag = "TAG";
	//the instance of OAuth

	CommonsHttpOAuthConsumer httpOauthConsumer;
	OAuthProvider httpOauthprovider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initView();
    }
    
    public void initView(){
    	appref = this;
    	
    	
    	//init the compenent in the view;
    	etName = (EditText)findViewById(R.id.etUsername);
    	etPasswd = (EditText)findViewById(R.id.etPasswd);
    	
    	btnLogin = (Button)findViewById(R.id.btnLogin);
    	btnCancel = (Button)findViewById(R.id.btnCancel);
    	
    	btnLogin.setOnClickListener(this);
    	btnCancel.setOnClickListener(this);
    }

	public void onClick(View arg0) {
		if(arg0.getId() == btnLogin.getId()){
			DBAdapter dba = new DBAdapter(this,Contants.dbName,Contants.dbVersion);
			dba.open();
			Cursor cr = dba.query(null, "", "", "", "", "");
			if(cr != null){
				Log.v("TAG", "Data Exists");
			}else{
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

					int b = 4;

				} catch (Exception e) {
					String s = e.getMessage();
					Log.v("TAG", s);
				}
			}
		}else if (arg0.getId() == btnCancel.getId()){
			System.exit(0);
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
		String[] columns = {
				User.ID,
				User.TOKEN,
				User.TOKENSECRET
		};
		Toast.makeText(this, "Get the Token", 3000);
	}
}