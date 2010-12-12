package com.adapter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qb.R;
import com.qb.qbMain;
import com.qb.activity.rssDetail;
import com.qb.activity.viewer.FeedViewer;
import com.qb.activity.viewer.NewsViewer;

public class QbAdapter extends BaseAdapter{

	private FeedViewer qMain;
	private Node node;
	private SharedPreferences mysh;
	
	public QbAdapter(FeedViewer appRef){
		super();
		this.qMain = appRef;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return qMain.nodeList.size();
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
		Log.v("set qbAdapter", "startsss   ");
		final int position_temp = position;
		Node el = qMain.nodeList.get(position);
		view = qMain.getLayoutInflater().inflate(R.layout.qblist, null);
		view.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Bundle bIndex = new Bundle();
				bIndex.putInt("index", position_temp);
				Intent new_detail = new Intent(qMain,rssDetail.class);
				new_detail.putExtras(bIndex);
				qMain.startActivity(new_detail);
			}
			
		});
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
		TextView tvPubDate = (TextView) view.findViewById(R.id.tvPubDate);
		NodeList nlmeta = el.getChildNodes();
		Log.v("debug", "the length of nlmeta"+nlmeta.getLength());
		for (int i = 0; i < nlmeta.getLength(); i++) {
			Node elmeta = nlmeta.item(i);
			if (elmeta.getNodeName().equalsIgnoreCase("title")) {
				tvTitle.setText(elmeta.getFirstChild()==null?"":elmeta.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("author")) {
				tvAuthor.setText(elmeta.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("pubDate")) {
				tvPubDate.setText(elmeta.getFirstChild()==null?"":elmeta.getFirstChild().getNodeValue());
			}
		}
//		Node n3 = el.getElementsByTagName("pubDate").item(0);
//		tvPubDate.setText(el.getElementsByTagName("pubDate").item(0)
//				.getFirstChild().getNodeValue());
		
		Log.v("Set Adapter View", "end");
		return view;
	}

}
