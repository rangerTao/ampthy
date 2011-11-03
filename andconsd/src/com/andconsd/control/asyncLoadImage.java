package com.andconsd.control;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import com.andconsd.Androsd;
import com.andconsd.constants.Constants;

public class asyncLoadImage extends AsyncTask {
	
	Bitmap bmp = null;
	ImageView iv;
	SoftReference<Bitmap> bmpSoftReference;
	@Override
	protected Object doInBackground(Object... params) {
		try{
			GridView gv = (GridView) params[0];
			iv = (ImageView) gv.findViewWithTag(params[1]);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		

		if(iv == null){
			return null;
		}
		String filename = (String) params[1];
		

		if (Constants.ImgCache.containsKey(filename)) {

			publishProgress(iv, Constants.ImgCache.get(filename));

			return null;
		}

		if (filename.endsWith(".mp4") || filename.endsWith(".3gp")) {
			bmp = ThumbnailUtils.extractThumbnail(ThumbnailUtils
					.createVideoThumbnail(filename,
							MediaStore.Video.Thumbnails.MINI_KIND), 130, 130);
		} else {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 12;
				bmp = ThumbnailUtils.extractThumbnail(
						BitmapFactory.decodeFile(filename, options), 130, 130);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

//		Constants.ImgCache.put(filename, new SoftReference<Bitmap>(bmp));
		Constants.ImgCache.put(filename, bmp);
		
		Androsd.appref.handler.post(new Runnable() {
			
			@Override
			public void run() {
				if(iv!=null){
					iv.setImageBitmap(bmp);
				}
				
			}
		});
		
		Log.v("TAG", "task finished");
		ThumbnailAsyncTaskController.removeAThread();
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Object... values) {

		super.onProgressUpdate(values);
	}

}
