package com.weibo.task;

import java.util.ArrayList;

import weibo4android.Weibo;
import weibo4android.WeiboException;

import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.utils.Constant;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class FavourTask extends AsyncTask{

	HomeTimeLineAdapter htla;
	Weibo weibo;
	@Override
	protected Object doInBackground(Object... arg0) {
		if(Constant.getMsg){
			getFavours(Constant.favour_PageIndex);
		}
		Constant.getMsg = false;
		return null;
	}

	
	@Override
	protected void onPostExecute(Object result) {

		htla = new HomeTimeLineAdapter(Constant.favourList , IndexActivity.appref);
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

	private void getFavours(int page_index) {
		weibo = OAuthConstant.getInstance().getWeibo();
		try {
			Constant.statuses = new ArrayList<weibo4android.Status>();
			Constant.statuses = weibo.getFavorites(page_index);

			for(weibo4android.Status tempStatus : Constant.statuses){
				Constant.favourList.add(tempStatus);
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "Getting metions error", 2000).show();
		}
	}
}
