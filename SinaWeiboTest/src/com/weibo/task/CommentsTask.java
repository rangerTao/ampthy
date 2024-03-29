package com.weibo.task;

import java.util.ArrayList;

import weibo4android.Comment;
import weibo4android.Weibo;
import weibo4android.WeiboException;

import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.CommentsToMeAdapter;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.utils.Constant;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class CommentsTask extends AsyncTask{
	CommentsToMeAdapter htla;
	@Override
	protected Object doInBackground(Object... arg0) {
		if(Constant.getMsg){
			getComments();
		}
		Constant.getMsg = false;
		return null;
	}

	
	@Override
	protected void onPostExecute(Object result) {

		htla = new CommentsToMeAdapter();
		if (IndexActivity.lvHomeTimeLine.getAdapter() != null ){
			IndexActivity.lvHomeTimeLine.removeAllViewsInLayout();
			IndexActivity.lvHomeTimeLine.setAdapter(htla);
		}else{
			htla.notifyDataSetChanged();
		}
		super.onPostExecute(result);
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	private void getComments() {
		Weibo weibo = OAuthConstant.getInstance().getWeibo();
		try {
			Constant.comments = new ArrayList<weibo4android.Comment>();
			Constant.comments = weibo.getCommentsToMe();
			for(Comment tempStatus : Constant.comments){
				Constant.comList.add(tempStatus);
				Log.v("TAG", tempStatus.toString());
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "Getting metions error", 2000).show();
		}
	}	
}
