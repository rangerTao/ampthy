package com.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qb.R;
import com.qb.qbMain;
import com.qb.activity.viewer.FeedViewer;

public class FeedList extends BaseAdapter{
	
	private qbMain qbTemp;
	public FeedList(qbMain qbMain){
		this.qbTemp = qbMain;
	}

	public int getCount() {
		return qbTemp.feedList.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View view, ViewGroup parent) {
		
		final int position_temp = position;
		view = qbTemp.getLayoutInflater().inflate(R.layout.feedlist,null);
		TextView tvName = (TextView) view.findViewById(R.id.tvName);
		
		String name = qbTemp.feedList[position].split(";")[0].toString();
		tvName.setText(name);
		return view;
	}
}
