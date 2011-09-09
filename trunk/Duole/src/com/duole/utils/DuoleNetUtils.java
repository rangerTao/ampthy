package com.duole.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DuoleNetUtils {

	/**
	 * down load content.
	 * @param url
	 * @return
	 */
	public static String connect(String url) {

		try {
			URL urlCon = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) urlCon
					.openConnection();

			conn.setRequestMethod("GET");

			conn.setConnectTimeout(5 * 1000);

			InputStream inStream = conn.getInputStream();
			byte[] data = DuoleUtils.readFromInput(inStream);

			String html = new String(data, "gbk");

			return html;
		} catch (Exception e) {
			Log.v("TAG", "Connect " + e.getMessage());
			return "";
		}
	}
	
	/** 
     * check whether network is aviable.
     */  
    public static boolean isNetworkAvailable(Activity mActivity){  
    	
        Context context = mActivity.getApplicationContext();  
        ConnectivityManager connectivity =(ConnectivityManager)  
        context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if(connectivity == null){  
            return false;  
        }else {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if(info != null){  
                for(int i= 0;i<info.length;i++){  
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
    
}
