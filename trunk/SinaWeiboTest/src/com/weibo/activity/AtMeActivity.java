package com.weibo.activity;

import java.util.ArrayList;
import java.util.List;

import weibo4andriod.Status;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.weibo.pojo.OAuthConstant;
import com.weibo.utils.Constant;

public class AtMeActivity extends Activity {

	Weibo4sina weibo;
	List<Status> statuses = new ArrayList<Status>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("TAG", "AtMe started");
		weibo = OAuthConstant.getInstance().getWeibo();
		getAtMe();
		finish();
	}

	private void getAtMe(){
		
		try {
			weibo.setOAuthConsumer(Constant.CONSUMER_KEY,
					Constant.CONSUMER_SECRET);
			weibo.setToken(Constant._access, Constant._accessSecret);
			weibo.setOAuthAccessToken(Constant._token, Constant._tokenSecret);

			statuses = weibo.getMentions();
			for(Status temp : statuses){
				Log.v("TAG", temp.toString());
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}
}
