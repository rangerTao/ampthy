package com.weibo.pojo.adapter;

import weibo4andriod.User;

import com.weibo.R;
import com.weibo.activity.IndexActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsStatusAdapter extends BaseAdapter{


	public int getCount() {
		return IndexActivity.friends.size();
	}


	public Object getItem(int position) {
		return IndexActivity.friends.get(position);
	}


	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutIn = LayoutInflater.from(IndexActivity.appref);
		View view = layoutIn.inflate(R.layout.friendstimeline_adapter, null);
		ImageView ivUserHeadView = (ImageView)view.findViewById(R.id.ivUserHead);
		TextView tvUserNameView = (TextView)view.findViewById(R.id.tvUserName);
		TextView tvUserLocation = (TextView)view.findViewById(R.id.tvUserLocation);
		TextView tvUserDesc = (TextView)view.findViewById(R.id.tvUserDesc);
		
		User user = IndexActivity.friends.get(position);
		ivUserHeadView.setImageBitmap(null);
		tvUserNameView.setText(user.getName());
		tvUserLocation.setText(user.getLocation());
		tvUserDesc.setText(user.getDescription());
		return view;
	}

}
