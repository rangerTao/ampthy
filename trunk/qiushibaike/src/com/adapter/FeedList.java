package com.adapter;

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
		// TODO Auto-generated method stub
		Log.v("debug", qbTemp.feedList.length+"");
		return qbTemp.feedList.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View view, ViewGroup parent) {
		
		final int position_temp = position;
		view = qbTemp.getLayoutInflater().inflate(R.layout.feedlist,null);
		TextView tvName = (TextView) view.findViewById(R.id.tvName);
		tvName.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent newFeedIntent = new Intent(qbTemp,FeedViewer.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", qbTemp.feedList[position_temp].split(";")[1].toString());
				bundle.putString("name", qbTemp.feedList[position_temp].split(";")[0].toString());
				Log.v("debug", qbTemp.feedList[position_temp].split(";")[1].toString());
				newFeedIntent.putExtras(bundle);
				qbTemp.startActivity(newFeedIntent);
			}
		});
		
		String name = qbTemp.feedList[position].split(";")[0].toString();
		tvName.setText(name);
		
		return view;
		
	}

}
