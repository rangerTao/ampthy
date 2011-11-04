package com.andconsd;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.andconsd.constants.Constants;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

public class WelcomeActivity extends Activity{

	Handler handler = new Handler();
	WelcomeActivity appref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appref = this;
		setContentView(R.layout.welcome);
		
		Constants.files = new ArrayList<File>();
		
		new Thread(){

			@Override
			public void run() {


				final File rootFile = new File(Constants.ROOT_DIR);
				if (!rootFile.exists()) {
					rootFile.mkdirs();
				}
				File[] filess = rootFile.listFiles();

				for (File temp : filess) {
					Constants.files.add(temp);
				}
				
				int end = 0;
				
				if(Constants.files.size() > 30){
					end = 30;
				}else{
					end = Constants.files.size();
				}
				
				for(int i = 0; i< end;i++){
//					String filename = Constants.files.get(i).getAbsolutePath();
//					
//					Constants.ImgCache.put(filename,ThumbnailUtils.extractThumbnail(ThumbnailUtils
//							.createVideoThumbnail(filename,
//									MediaStore.Video.Thumbnails.MINI_KIND), 130, 130));
				}
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						
						startActivity(new Intent(appref,Androsd.class));

						finish();
					}
				});
				
				
			
				super.run();
			}
			
		}.start();
		
		
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
	
}
