package com.weibo.activity;

import java.util.List;

import weibo4andriod.Status;
import weibo4andriod.User;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.R;
import com.weibo.daos.DBAdapter;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.UserImpl;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.pojo.adapter.TopMenuAdapter;
import com.weibo.utils.Constant;

public class IndexActivity extends Activity {

	String token;
	String tokenSecret;
	String access;
	String accessSecret;

	ProgressDialog pDialog;
	ListView lvHomeTimeLine;
	ListView lvTopMenu;
	StringBuffer sbAll = new StringBuffer();
	HorizontalScrollView hs;

	public static TextView tvHeaderUserName;

	public static Handler handler = new Handler();
	public static String[] strTopMenus;

	public static IndexActivity appref;

	public static List<User> friends;
	public static List<Status> statuses;

	public Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();

	HomeTimeLineAdapter htla;
	TopMenuAdapter tma;
	FriendTask ft;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.index_activity);
		
		lvHomeTimeLine = (ListView) findViewById(R.id.lvHomeTimeLine);
		//initData();
		initHeader();

		
		appref = this;
		
		ft = new FriendTask();
		ft.execute();
		
		initButtonAction();

	}
	
	public void initButtonAction(){
		LinearLayout llAtMe = (LinearLayout)findViewById(R.id.llAtMe_TopMenu);
		llAtMe.setOnClickListener(new OnClickListener(){


			public void onClick(View v) {
				Log.v("TAG", "Start AtMe");
				startActivity(new Intent(appref,AtMeActivity.class));
			}
		});
	}

	public void initHeader() {

		ImageView ibtnHeaderWrite = (ImageView) findViewById(R.id.ibtnHeaderWrite);
		ImageView ibtnHeaderRefresh = (ImageView) findViewById(R.id.ibtnHeaderRefresh);
		tvHeaderUserName = (TextView) findViewById(R.id.tvHeaderUserName);
		
		tvHeaderUserName.setText(UserImpl.getUserScreenName());
		
		ibtnHeaderWrite.setImageBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.writer));
		ibtnHeaderRefresh.setImageBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.refresh_btn));

		ibtnHeaderWrite.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				final LinearLayout llEditTwitter = (LinearLayout) findViewById(R.id.llEditTwitter);
				llEditTwitter.setVisibility(View.VISIBLE);

				final EditText etTwitter = (EditText) findViewById(R.id.etBoradcast);

				TextView tvSend = (TextView) findViewById(R.id.btnSend);
				tvSend.setOnClickListener(new OnClickListener(){

					@SuppressWarnings("deprecation")
					public void onClick(View v) {
						ProgressDialog pdSend = null;
						try {
							pdSend = ProgressDialog.show(IndexActivity.appref, "Sending", "please wait");
							if(etTwitter.getText().toString().length()>0){
								weibo.update(etTwitter.getText().toString());
							}
						} catch (WeiboException e) {
							Toast.makeText(appref, "Updating error", 2000).show();
							e.printStackTrace();
						} finally{
							llEditTwitter.setVisibility(View.GONE);
							pdSend.dismiss();
						}
						
					}
					
				});

				TextView tvCancel = (TextView) findViewById(R.id.btnCancel);
				tvCancel.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						etTwitter.setText("");
						llEditTwitter.setVisibility(View.GONE);
					}
				});
			}

		});
		
		ibtnHeaderRefresh.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				ft = new FriendTask();
				ft.execute();
			}
			
		});
	}

	private void initData() {


		strTopMenus = this.getResources().getStringArray(R.array.top_menu);
		Log.v("TAG", strTopMenus.length + "");
		DBAdapter dba = new DBAdapter(this, Constant.dbName, Constant.dbVersion);
		dba.open();
		Cursor cr = dba.query(null, "", "", "", "", "");

		if (cr != null && cr.getCount() > 0) {
			cr.moveToFirst();

			token = cr.getString(1);
			tokenSecret = cr.getString(2);
			access = cr.getString(3);
			accessSecret = cr.getString(4);

			SharedPreferences sPre = this.getSharedPreferences(
					Constant.app_name, MODE_WORLD_WRITEABLE);
			Editor editor = sPre.edit();
			editor.putString(Constant.token, token);
			editor.putString(Constant.tokenSecret, tokenSecret);
			editor.putString(Constant.ACCESSTOKEN, access);
			editor.putString(Constant.ACCESSTOKENSECRET, accessSecret);
			editor.commit();
		}
		cr.close();
		dba.close();
		Constant._access = access;
		Constant._accessSecret = accessSecret;
		Constant._token = token;
		Constant._tokenSecret = tokenSecret;
		
	}

	private void getFriends()
			throws org.apache.commons.httpclient.util.TimeoutController.TimeoutException {

		try {
			weibo.setOAuthConsumer(Constant.CONSUMER_KEY,
					Constant.CONSUMER_SECRET);
			weibo.setToken(Constant.ACCESSTOKEN, Constant.ACCESSTOKENSECRET);
			weibo.setOAuthAccessToken(Constant.token, Constant.tokenSecret);

			statuses = weibo.getHomeTimeline();

		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}

	private class FriendTask extends AsyncTask {

		@Override
		protected void onPreExecute() {
			pDialog = ProgressDialog.show(IndexActivity.appref, appref
					.getResources().getString(R.string.progress_title), appref
					.getResources().getString(R.string.progress_content));
			pDialog.setCancelable(true);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pDialog.dismiss();
			lvHomeTimeLine.setAdapter(htla);
			User user = null;
			try {
				user = weibo.getAuthenticatedUser();
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				getFriends();
			} catch (org.apache.commons.httpclient.util.TimeoutController.TimeoutException e) {
				Toast.makeText(appref, "time out", 2000).show();
			}
			htla = new HomeTimeLineAdapter();
			return null;
		}
	}
}
