package com.weibo.task;

import java.util.ArrayList;

import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;

import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.FriendsStatusAdapter;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.utils.Constant;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class FriendsTask extends AsyncTask{

	FriendsStatusAdapter fsa;
	@Override
	protected Object doInBackground(Object... arg0) {
		if(Constant.getMsg){
			getFriendss(Constant.friend_PageIndex);
		}
		Constant.getMsg = false;
		return null;
	}
	
	protected void onPostExecute(Object result) {

		fsa = new FriendsStatusAdapter();
		if (IndexActivity.lvHomeTimeLine.getAdapter() != null ){
			IndexActivity.lvHomeTimeLine.removeAllViewsInLayout();
			IndexActivity.lvHomeTimeLine.setAdapter(fsa);
		}else{
			fsa.notifyDataSetChanged();
		}
		super.onPostExecute(result);
	}
	private void getFriendss(int page) {
		Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();
		try {
			Constant.useres = new ArrayList<weibo4andriod.User>();
			Constant.useres = weibo.getFriendsStatuses();
			for(weibo4andriod.User tempUser : Constant.useres){
				Constant.friendsList.add(tempUser);
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "Getting metions error", 2000).show();
		}
	}
}
