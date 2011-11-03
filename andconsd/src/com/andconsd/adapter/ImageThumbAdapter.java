package com.andconsd.adapter;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;

import com.andconsd.Androsd;
import com.andconsd.R;
import com.andconsd.constants.Constants;
import com.andconsd.control.ThumbnailAsyncTaskController;
import com.andconsd.control.asyncLoadImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
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
		return null;
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
		
		if(Constants.ImgCache.size() > 70){
			Iterator iterator = Constants.ImgCache.keySet().iterator();
			Log.v("TAG", "clear more");
			Constants.ImgCache.remove(iterator.next());
//			System.gc();
		}
		
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

			if (Constants.ImgCache.containsKey(file.getAbsolutePath())) {
//				SoftReference<Bitmap> softReference = 
				bmp = Constants.ImgCache.get(file
						.getAbsolutePath());
				if(bmp != null){
					ti.iv.setImageBitmap(bmp);
				}
				
			} else {
				ti.iv.setImageResource(R.drawable.loading);
				ti.iv.setTag(file.getAbsolutePath());
//				new asyncLoadImage().execute(new Object[] { gv,
//						file.getAbsolutePath() });
				ThumbnailAsyncTaskController.doTask(new Object[]{gv,file.getAbsolutePath()});
			}

		} else {
			ti.ivPlay.setVisibility(View.INVISIBLE);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 12;
			if (Constants.ImgCache.containsKey(file.getAbsolutePath())) {
//				SoftReference<Bitmap> softReference = 
				bmp = Constants.ImgCache.get(file
						.getAbsolutePath());
//				bmp = softReference.get();
				if(bmp != null){
					ti.iv.setImageBitmap(bmp);
				}
			} else {
				ti.iv.setImageResource(R.drawable.loading);
				ti.iv.setTag(file.getAbsolutePath());
//				new asyncLoadImage().execute(new Object[] { gv,
//						file.getAbsolutePath() });
				ThumbnailAsyncTaskController.doTask(new Object[]{gv,file.getAbsolutePath()});
			}

		}

		return convertView;
	}

	class ThumbItem {
		ImageView iv;
		ImageView ivPlay;
	}

}