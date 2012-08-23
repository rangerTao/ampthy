package com.ranger.comic.adapter;

import com.ranger.comic.R;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class LocalItemAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater mInflater;
	LinearLayout llRow;

	private int lineNo = 0;

	public LocalItemAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LocalItem localItem;
		View view = null;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.local_item, null);

			localItem = new LocalItem();

			localItem.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
			localItem.tvIndex = (TextView) view.findViewById(R.id.tvIndex);

			view.setTag(localItem);
			convertView = view;
		} else {
			localItem = (LocalItem) convertView.getTag();
		}

		localItem.tvIndex.setText(position + "");
		localItem.ivIcon.setImageResource(R.drawable.icon);

		return convertView;
	}

	class LocalItem {
		ImageView ivIcon;
		TextView tvIndex;
	}

}
