package com.weibo.task;

import java.util.ArrayList;

import weibo4andriod.User;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.utils.Constant;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class FavourTask extends AsyncTask{

	HomeTimeLineAdapter htla;
	Weibo4sina weibo;
	@Override
	protected Object doInBackground(Object... arg0) {
		getFavours(Constant.favour_PageIndex);
		return null;
	}

	
	@Override
	protected void onPostExecute(Object result) {

		htla = new HomeTimeLineAdapter(Constant.favourList);
		if (IndexActivity.lvHomeTimeLine.getAdapter() == null && Constant.favour_PageIndex == 1) {
			IndexActivity.lvHomeTimeLine.setAdapter(htla);
			
		} else if (Constant.favour_PageIndex == 1){
			IndexActivity.lvHomeTimeLine.removeAllViewsInLayout();
			IndexActivity.lvHomeTimeLine.setAdapter(htla);
			Log.v("TAG", "Test1");
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
			Constant.statuses = new ArrayList<weibo4andriod.Status>();
			Constant.statuses = weibo.getFavorites(page_index);

			for(weibo4andriod.Status tempStatus : Constant.statuses){
				Constant.favourList.add(tempStatus);
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "Getting metions error", 2000).show();
		}
	}
}
