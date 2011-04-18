package com.weibo.utils;

import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class WeiboUtils {

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

			Log.v("Getting data.", "end");

			bmpUserHead = BitmapFactory.decodeStream(is);

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
}
