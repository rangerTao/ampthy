package com.weibo.activity;

import java.util.List;
import java.util.concurrent.TimeoutException;

import weibo4andriod.Status;
import weibo4andriod.User;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.weibo.R;
import com.weibo.daos.DBAdapter;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.FriendsStatusAdapter;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.pojo.adapter.TopMenuAdapter;
import com.weibo.utils.Contants;

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
	
	public static Handler handler = new Handler();
	public static String[] strTopMenus;

	public static IndexActivity appref;

	public static List<User> friends;
	public static List<Status> statuses;

	public Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();
	
	HomeTimeLineAdapter htla;
	TopMenuAdapter tma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.index_activity);

		lvHomeTimeLine = (ListView) findViewById(R.id.lvHomeTimeLine);
		initData();
		
		FriendTask ft = new FriendTask();
		ft.execute();
		
	}

	private void initData() {
		appref = this;
		
		strTopMenus = this.getResources().getStringArray(R.array.top_menu);
		Log.v("TAG", strTopMenus.length + "");
		DBAdapter dba = new DBAdapter(this, Contants.dbName, Contants.dbVersion);
		dba.open();
		Cursor cr = dba.query(null, "", "", "", "", "");

		if (cr != null && cr.getCount() > 0) {
			cr.moveToFirst();

			token = cr.getString(1);
			tokenSecret = cr.getString(2);
			access = cr.getString(3);
			accessSecret = cr.getString(4);

			SharedPreferences sPre = this.getSharedPreferences(
					Contants.app_name, MODE_WORLD_WRITEABLE);
			Editor editor = sPre.edit();
			editor.putString(Contants.token, token);
			editor.putString(Contants.tokenSecret, tokenSecret);
			editor.putString(Contants.ACCESSTOKEN, access);
			editor.putString(Contants.ACCESSTOKENSECRET, accessSecret);
			editor.commit();
		}
		cr.close();
		dba.close();
		hs = (HorizontalScrollView) findViewById(R.id.hsTopMenu);
		hs.setHorizontalScrollBarEnabled(false);
		hs.setClickable(true);
		TableRow trMenu = (TableRow) findViewById(R.id.trTopMenu);
		trMenu.setClickable(true);
		//
		for (int i = 0; i < strTopMenus.length; i++) {
			LayoutInflater lInflater = LayoutInflater.from(this);
			View view = lInflater.inflate(R.layout.top_menu, null);
			TextView tvTitle = (TextView)view.findViewById(R.id.tvMenuItem);
			ImageView ivTopMenu = (ImageView)view.findViewById(R.id.ivMenuImage);
			ivTopMenu.setImageBitmap(Contants.imageMenu[i]);
			tvTitle.setText(strTopMenus[i].toString());
			view.setPadding(8, 0, 0, 0);
			view.setClickable(true);
			trMenu.addView(view);
		}
		//tma = new TopMenuAdapter();
		//lvTopMenu.setAdapter(tma);
	}

	private void getFriends() throws org.apache.commons.httpclient.util.TimeoutController.TimeoutException {

		try {
			weibo.setOAuthConsumer(Contants.CONSUMER_KEY,
					Contants.CONSUMER_SECRET);
			weibo.setToken(access, accessSecret);
			weibo.setOAuthAccessToken(token, tokenSecret);
			
			statuses = weibo.getHomeTimeline();

			Log.v("TAG", statuses.size()+"");
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
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pDialog.dismiss();
			lvHomeTimeLine.setAdapter(htla);
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
