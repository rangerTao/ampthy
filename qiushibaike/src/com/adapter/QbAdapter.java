package com.adapter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qb.R;
import com.qb.qbMain;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QbAdapter extends BaseAdapter{

	private qbMain qMain;
	private Node node;
	
	public QbAdapter(qbMain qMain){
		super();
		this.qMain = qMain;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return qbMain.nodeList.size();
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
		Log.v("set qbAdapter", "start");
		final qbMain am = qbMain.getApp();
		Element el = am.nodeList.get(position);
		view = am.getLayoutInflater().inflate(R.layout.qblist, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
		TextView tvPubDate = (TextView) view.findViewById(R.id.tvPubDate);
		Node meta = el.getElementsByTagName("meta").item(0);
		NodeList nlmeta = meta.getChildNodes();
		for (int i = 0; i < nlmeta.getLength(); i++) {
			Node elmeta = nlmeta.item(i);
			if (elmeta.getNodeName().equalsIgnoreCase("title")) {
				tvTitle.setText(elmeta.getFirstChild()==null?"":elmeta.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("author")) {
				tvAuthor.setText(elmeta.getFirstChild().getNodeValue().substring(0, 10));
			}
		}
		Node n3 = el.getElementsByTagName("pubDate").item(0);
		tvPubDate.setText(el.getElementsByTagName("pubDate").item(0)
				.getFirstChild().getNodeValue());

		Log.v("Set Adapter View", "end");
		return view;
	}

}
