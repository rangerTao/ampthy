package com.weibo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import weibo4android.Status;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class WeiboUtils {


	private static File dstFolder;
	/**
	 * To check the format of email
	 */
	public static boolean isEmail(String email) {
		if (email.indexOf("@") == -1) {
			return false;
		} else if (email.indexOf(".") == -1) {
			return false;
		}
		return true;
	}

	public synchronized static Bitmap getImage(URL url) {
		Bitmap bmpUserHead = null;
		DefaultHttpClient hc = new DefaultHttpClient();
		hc.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
				10000);
		hc.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000);

		try {
			HttpGet gm = new HttpGet(url.toURI());
			HttpResponse hr = hc.execute(gm);
			InputStream is = hr.getEntity().getContent();

			bmpUserHead = BitmapFactory.decodeStream(is);
			WeiboUtils.writeFileToSD(bmpUserHead, url.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmpUserHead;

	}

	public synchronized static void setImage(Handler handler, final ImageView iv,
			final Bitmap bm) {
		handler.post(new Thread(){
			public void run() {
				iv.setImageBitmap(bm);
			}
		});
	}
	
	
	public static void writeFileToSD(Bitmap bmp,String filename) throws IOException{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			dstFolder = new File(Constant.image_cache_dir);
			if(!dstFolder.exists()){
				dstFolder.mkdir();
			}
			String fileName = filename.replace("http://", "http_").replace("/", "_");
			if(fileName.endsWith(".jpg")){
				fileName = fileName.replace(".jpg", ".jpga");
			}else if(fileName.endsWith(".JPG")){
				fileName = fileName.replace(".JPG", ".JPGA");
			}else if(fileName.endsWith(".png")){
				fileName = fileName.replace(".png", ".pnga");
			}else if(fileName.endsWith(".PNG")){
				fileName = fileName.replace(".PNG", ".PNGA");
			}else if(filename.endsWith(".gif")){
				filename = fileName.replace(".gif", ".gifa");
			}else if(fileName.endsWith(".GIF")){
				fileName = fileName.replace(".GIF", ".GIFA");
			}
			File outFile = new File(dstFolder.getAbsolutePath() +"/" + fileName);
			if(!outFile.exists()){
				outFile.createNewFile();
				
				FileOutputStream fos = new FileOutputStream(outFile);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
				
				fos.write(os.toByteArray());
				
				fos.close();
			}else{
			}
			
		}
	}


}
