package com.weibo.activity;

import java.util.List;

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
import android.util.Log;
import android.widget.ListView;

import com.weibo.daos.DBAdapter;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.FriendsStatusAdapter;
import com.weibo.utils.Contants;

public class IndexActivity extends Activity {

	String token;
	String tokenSecret;
	String access;
	String accessSecret;

	ProgressDialog pDialog;
	ListView lvPublicTimeLine;
	StringBuffer sbAll = new StringBuffer();

	public static IndexActivity appref;

	public static List<User> friends;

	FriendsStatusAdapter fsa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		lvPublicTimeLine = new ListView(this);

		setContentView(lvPublicTimeLine);

		initData();

		FriendTask ft = new FriendTask();
		ft.execute();
	}

	private void initData() {
		appref = this;

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
	}

	private void getFriends() {

		System.out.println("Showing public timeline.");
		try {

			Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();
			weibo.setOAuthConsumer(Contants.CONSUMER_KEY,
					Contants.CONSUMER_SECRET);
			weibo.setToken(access, accessSecret);
			weibo.setOAuthAccessToken(token, tokenSecret);

			friends = weibo.getFriendsStatuses();
			for (User user : friends) {
				Log.v("TAG", user.toString());
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}

	private class FriendTask extends AsyncTask {

		@Override
		protected void onPreExecute() {
			pDialog = ProgressDialog
					.show(IndexActivity.appref, "正在获取信息", "请稍等");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pDialog.dismiss();
			lvPublicTimeLine.setAdapter(fsa);
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			getFriends();
			fsa = new FriendsStatusAdapter();
			return null;
		}
	}
}
