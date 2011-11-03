package com.andconsd.adapter;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.andconsd.Androsd;
import com.andconsd.PicViewer;
import com.andconsd.R;
import com.andconsd.widget.DuoleVideoView;

public class PicViewAdapter extends BaseAdapter implements OnClickListener{

	String[] files;
	int screenwidth = 0;
	int screenheight = 0;
	int position = 0;
	
	public PicViewAdapter(ArrayList<File> filess,int width ,int height){
		files = new String[filess.size()];
		for (int i = 0; i < filess.size(); i++) {
			files[i] = filess.get(i).getAbsolutePath();
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return files.length;
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

		this.position = position;
		PicViewerItem pvi;
		String filepath = files[position];

		View view = LayoutInflater.from(PicViewer.appref).inflate(
				R.layout.picviewitem, null);

		pvi = new PicViewerItem();

		pvi.ivThumb = (ImageView) view.findViewById(R.id.ivThumb);
		pvi.ivPlay = (ImageView) view.findViewById(R.id.ivPlay);

		view.setTag(pvi);
		convertView = view;

		if (filepath.endsWith(".mp4") || filepath.endsWith(".3gp")) {
			Bitmap bm = ThumbnailUtils.createVideoThumbnail(filepath,
					MediaStore.Video.Thumbnails.MINI_KIND);
			pvi.ivThumb.setImageBitmap(bm);
			pvi.ivPlay.setVisibility(View.VISIBLE);
			pvi.ivPlay.setImageResource(R.drawable.videoplay);

		} else {
			pvi.ivThumb.setImageBitmap(getDrawable(filepath));
			pvi.ivPlay.setVisibility(View.INVISIBLE);
		}
		
		if(pvi.ivPlay.getVisibility() == View.VISIBLE){
			pvi.ivPlay.setOnClickListener(this);
		}
		
//		pvi.ivThumb.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				PicViewer.appref.showHideController();
//			}
//		});
		
		return convertView;
	}
	
	class PicViewerItem{
		ImageView ivThumb;
		ImageView ivPlay;
	}
	
	
	private Bitmap getDrawable(String path) {
		if (path == null || path.length() < 1)
			return null;
		File file = new File(path);
		Log.v("TAG", "file length " + file.length());
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();

		if (file.length() < 131072) { // 0-1m
			opts.inSampleSize = 1;
		} else if (file.length() < 262144) { // 1-2m
			opts.inSampleSize = 2;
		} else if (file.length() < 524288) { // 2-4m
			opts.inSampleSize = 4;
		} else if (file.length() < 1048576) { // 4-8m
			opts.inSampleSize = 6;
		} else {
			opts.inSampleSize = 10;
		}
		
		try {
			InputStream is = new DataInputStream(new FileInputStream(file));
			
//			resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
			resizeBmp = BitmapFactory.decodeStream(is,null, opts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return resizeBmp;
	}
	@Override
	public void onClick(View v) {
		PicViewer.appref.handler.post(new Runnable() {
			
			@Override
			public void run() {
				PicViewer.appref.playVideo(position);
			}
		});
	}
	
}
