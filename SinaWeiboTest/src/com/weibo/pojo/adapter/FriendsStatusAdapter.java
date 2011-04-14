package com.weibo.pojo.adapter;

import weibo4andriod.Status;
import weibo4andriod.User;

import com.weibo.R;
import com.weibo.activity.IndexActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendsStatusAdapter extends BaseAdapter{

	@Override
	public int getCount() {
		return IndexActivity.friends.size();
	}

	@Override
	public Object getItem(int position) {
		return IndexActivity.friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutIn = LayoutInflater.from(IndexActivity.appref);
		View view = layoutIn.inflate(R.layout.timeline_public, null);
		TextView tvAll = (TextView)view.findViewById(R.id.tvAll);
		User status = IndexActivity.friends.get(position);
		tvAll.setText(status.toString());
		return view;
	}

}
