package com.weibo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class GetImageThread implements Runnable {

	private static ArrayList<URL> imageTask = new ArrayList<URL>();

	public void run() {

		while (imageTask.size() > 0) {
			try {
				URL tempUrl = popImageTask();
				Bitmap bmpTemp = WeiboUtils.getImage(tempUrl);

				Constant.imageMap.put(tempUrl.toString(), bmpTemp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void pushImageTask(URL url) {
		// synchronized (imageTask) {
		imageTask.add(url);
		// }

	}

	public synchronized URL popImageTask() throws InterruptedException {
			if (imageTask.size() > 0) {
				URL temp = imageTask.get(0);
				imageTask.remove(0);
				return temp;
			} else {
				this.wait();
			}
			return null;
	}

}
