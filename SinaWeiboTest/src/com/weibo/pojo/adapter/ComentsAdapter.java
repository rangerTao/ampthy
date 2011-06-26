package com.weibo.pojo.adapter;

import weibo4android.User;

import com.weibo.R;
import com.weibo.activity.MsgDetail;
import com.weibo.utils.Constant;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComentsAdapter extends BaseAdapter{

	public int getCount() {
		// TODO Auto-generated method stub
		return Constant.commentList.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Constant.commentList.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater li = LayoutInflater.from(MsgDetail.appref);
		
		View view = li.inflate(R.layout.commentdetail, null);
	
		TextView tvUserScreenName = (TextView)view.findViewById(R.id.tvUserScreenName);
		TextView tvCommentDetail = (TextView)view.findViewById(R.id.tvCommentDetail);
		
		User user = Constant.commentList.get(arg0).getUser();
		tvUserScreenName.setText(user.getScreenName().toString());
		tvCommentDetail.setText(Constant.commentList.get(arg0).getText());
		return view;
	}

}
