package com.weibo.task;

import java.util.ArrayList;

import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;

import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.utils.Constant;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class FriendsTask extends AsyncTask{

	@Override
	protected Object doInBackground(Object... arg0) {
		getFriendss(Constant.friend_PageIndex);
		return null;
	}

	private void getFriendss(int page) {
		Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();
		try {
			Constant.useres = new ArrayList<weibo4andriod.User>();
			Constant.useres = weibo.getFriends(page);
			for(weibo4andriod.User tempStatus : Constant.useres){
				Constant.friendsList.add(tempStatus);
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "Getting metions error", 2000).show();
		}
	}
}
