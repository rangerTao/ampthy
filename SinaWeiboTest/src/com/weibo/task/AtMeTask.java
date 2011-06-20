package com.weibo.task;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.AtMeAdapter;
import com.weibo.utils.Constant;

import weibo4andriod.Paging;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;

public class AtMeTask extends AsyncTask{

	public AtMeAdapter atMeAdapter;

	
	@Override
	protected Object doInBackground(Object... params) {
		if(Constant.getMsg){
			getAtMe(Constant.atMe_PageIndex);
		}
		Constant.getMsg = false;
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		
		if(IndexActivity.lvHomeTimeLine.getAdapter() == null){
			IndexActivity.lvHomeTimeLine.removeAllViewsInLayout();
			IndexActivity.lvHomeTimeLine.setAdapter(atMeAdapter);
		}else{
			atMeAdapter.notifyDataSetChanged();
		}
		IndexActivity.appref.setProgressBarIndeterminate(false);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		IndexActivity.appref.setProgressBarIndeterminate(true);
		atMeAdapter = new AtMeAdapter();
		super.onPreExecute();
	}
	
	
	private void getAtMe(int page_index) {
		Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();
		try {
			Constant.statuses = new ArrayList<weibo4andriod.Status>();
			Constant.statuses = weibo.getMentions(new Paging(page_index));
			for(weibo4andriod.Status tempStatus : Constant.statuses){
				Constant.atMeList.add(tempStatus);
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "Getting metions error", 2000).show();
		}
	}
	
	


}