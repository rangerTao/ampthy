package com.ranger.comic.adapter;

import com.ranger.comic.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class RemoteItemListAdapter extends BaseAdapter {

	Context mContext;

	public RemoteItemListAdapter(Context context) {
		mContext = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
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

		RemoteItem rItem;
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.remote_item,
					null);
			rItem = new RemoteItem();

			rItem.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);

			view.setTag(rItem);
			convertView = view;
		} else {
			rItem = (RemoteItem) convertView.getTag();
		}

//		rItem.ivIcon.setImageResource(R.drawable.icon);

		return convertView;
	}

	class RemoteItem {
		ImageView ivIcon;
		boolean isDownloaded;
	}

}
