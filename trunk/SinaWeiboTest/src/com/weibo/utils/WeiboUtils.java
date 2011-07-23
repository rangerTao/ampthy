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
			String fileName = encodeImageName(filename);
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
	
	public static String encodeImageName(String name){
		String filename = name.replace("http://", "http_").replace("/", "_");
		if(filename.endsWith(".jpg")){
			filename = filename.replace(".jpg", ".jpga");
		}else if(filename.endsWith(".JPG")){
			filename = filename.replace(".JPG", ".JPGA");
		}else if(filename.endsWith(".png")){
			filename = filename.replace(".png", ".pnga");
		}else if(filename.endsWith(".PNG")){
			filename = filename.replace(".PNG", ".PNGA");
		}else if(filename.endsWith(".gif")){
			filename = filename.replace(".gif", ".gifa");
		}else if(filename.endsWith(".GIF")){
			filename = filename.replace(".GIF", ".GIFA");
		}
		return filename;
	}
	
	public static String decodeImageName(File image){
		String filename = image.getName().replace("http_",
		"http://").replace("_", "/");
		if(filename.endsWith(".jpga")){
			filename = filename.replace(".jpga", ".jpg");
		}else if(filename.endsWith(".JPGA")){
			filename = filename.replace(".JPGA", ".JPG");
		}else if(filename.endsWith(".pnga")){
			filename = filename.replace(".pnga", ".png");
		}else if(filename.endsWith(".PNGA")){
			filename = filename.replace(".PNGA", ".PNG");
		}else if(filename.endsWith(".gifa")){
			filename = filename.replace(".gifa", ".gif");
		}else if(filename.endsWith(".GIFA")){
			filename = filename.replace(".GIFA", ".GIF");
		}
		return filename;
	}


}
