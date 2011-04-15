package com.weibo.pojo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weibo.R;
import com.weibo.activity.IndexActivity;

public class PublicTimeLineAdapter extends BaseAdapter{

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
//		Status status = IndexActivity.statuses.get(position);
//		tvAll.setText(status.getUser().getName() + ":"
//				+ status.getText());
		return view;
	}

}
