package com.andconsd.adapter;

import java.io.File;
import java.util.ArrayList;

import com.andconsd.Androsd;
import com.andconsd.R;
import com.andconsd.control.BitmapManager;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageThumbAdapter extends BaseAdapter {

	ArrayList<File> files;
	GridView gv;

	public ImageThumbAdapter(ArrayList<File> als, GridView gridView) {
		this.files = als;
		gv = gridView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return files.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Bitmap bmp;
		File file = files.get(position);
		
		ThumbItem ti;
		if (convertView == null) {
			View view = LayoutInflater.from(Androsd.appref).inflate(
					R.layout.griditem, null);
			ti = new ThumbItem();

			ti.iv = (ImageView) view.findViewById(R.id.gridImageView);
			ti.ivPlay = (ImageView) view.findViewById(R.id.ivPlayButton);

			ti.iv.setImageResource(R.drawable.loading);
			view.setTag(ti);
			convertView = view;
		} else {
			ti = (ThumbItem) convertView.getTag();
		}
		
		if (file.getName().endsWith(".mp4") || file.getName().endsWith(".3gp")) {
			ti.ivPlay.setImageResource(R.drawable.videoplay);
			ti.ivPlay.setVisibility(View.VISIBLE);
		}else{
			ti.ivPlay.setVisibility(View.INVISIBLE);
		}
		
		BitmapManager.INSTANCE.loadBitmap(file.getAbsolutePath(), ti.iv, 130, 130);

		return convertView;
	}

	class ThumbItem {
		ImageView iv;
		ImageView ivPlay;
	}

}