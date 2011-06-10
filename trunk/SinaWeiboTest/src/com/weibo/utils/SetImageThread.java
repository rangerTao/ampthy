package com.weibo.utils;

import java.net.URL;
import java.util.ArrayList;

import com.weibo.activity.IndexActivity;

import android.graphics.Bitmap;
import android.hardware.Camera.Size;
import android.util.Log;
import android.widget.ImageView;

public class SetImageThread extends Thread {

	private static ArrayList<URL> imageTask = new ArrayList<URL>();
	private static ArrayList<ImageView> setView = new ArrayList<ImageView>();

	URL temp;
	ImageView view;

	@Override
	public void run() {
		while (true) {
			try {
				if (imageTask.size() < 1) {
					this.sleep(2000);
					continue;
				}
				popImageTask();
				final Bitmap tmp = WeiboUtils.getImage(this.temp);
				IndexActivity.handler.post(new Runnable() {
					
					public void run() {
						view.setImageBitmap(tmp);
					}
				});
				Constant.imageMap.put(temp.toString(), tmp);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		

	}

	public void pushImageTask(URL url, ImageView view) {
		imageTask.add(url);
		setView.add(view);
	}

	public synchronized void popImageTask() throws InterruptedException {
		synchronized (imageTask) {
			if (imageTask.size() > 0) {
				temp = imageTask.get(0);
				view = setView.get(0);
				imageTask.remove(0);
				setView.remove(0);
			} else {
				this.wait();
			}
		}

	}

}
