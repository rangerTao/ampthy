package com.weibo.pojo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weibo.activity.IndexActivity;

public class TopMenuAdapter extends BaseAdapter{

	public int getCount() {
		return IndexActivity.strTopMenus.length;
	}

	public Object getItem(int position) {
		return IndexActivity.strTopMenus[position].toString();
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tvTopMenu = new TextView(IndexActivity.appref);
		tvTopMenu.setText(IndexActivity.strTopMenus[position]);
		return tvTopMenu;
	}

}
