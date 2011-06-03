package com.weibo.utils;

import java.net.URL;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Log;

public class GetImageThread implements Runnable {

	private static ArrayList<URL> imageTask = new ArrayList<URL>();
	
	public void run() {
		while(imageTask.size() > 0){

		try{
			URL tempUrl = popImageTask();
			Bitmap bmpTemp = WeiboUtils.getImage(tempUrl);
			Constant.imageMap.put(tempUrl.toString(), bmpTemp);
			Log.v("TAG", "Received image" + tempUrl.toString());
		}catch(Exception e){
			e.printStackTrace();
		}			
		}
	}

	public void pushImageTask(URL url) {
		//synchronized (imageTask) {
			imageTask.add(url);
		//}

	}

	public synchronized URL popImageTask() throws InterruptedException {
		synchronized (imageTask) {
			if(imageTask.size()>0){
				URL temp = imageTask.get(0);
				imageTask.remove(0);
				return temp;
			}else{
				this.wait();
			}
			return null;
			
			
		}

	}

}
