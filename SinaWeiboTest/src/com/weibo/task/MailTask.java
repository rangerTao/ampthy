package com.weibo.task;

import java.util.ArrayList;

import weibo4andriod.Paging;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;

import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.CommentsToMeAdapter;
import com.weibo.pojo.adapter.MailToMeAdapter;
import com.weibo.utils.Constant;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MailTask extends AsyncTask{
	
	MailToMeAdapter mtma;
	
	@Override
	protected Object doInBackground(Object... arg0) {
		if(Constant.getMsg){
			getMails(Constant.mail_PageIndex);
		}
		return null;
	}

	protected void onPostExecute(Object result) {

		mtma = new MailToMeAdapter();
		if (IndexActivity.lvHomeTimeLine.getAdapter() != null ){
			IndexActivity.lvHomeTimeLine.removeAllViewsInLayout();
			IndexActivity.lvHomeTimeLine.setAdapter(mtma);
		}else{
			mtma.notifyDataSetChanged();
		}
		super.onPostExecute(result);
	}
	private void getMails(int page) {
		Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();
		try {
			Constant.mails = new ArrayList<weibo4andriod.DirectMessage>();
			Constant.mails = weibo.getDirectMessages(new Paging(page));
			for(weibo4andriod.DirectMessage tempStatus : Constant.mails){
				Constant.mailList.add(tempStatus);
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "Getting metions error", 2000).show();
		}
	}

}