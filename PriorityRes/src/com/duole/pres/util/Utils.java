package com.duole.pres.util;

import android.os.Handler;
import android.os.Message;

public class Utils {

	public static void sendMessage(String path,Handler handler,int what){
		Message msg = new Message();
		msg.what = what;
		msg.obj = path;
		handler.sendMessage(msg);
	}
	
	public static void sendMessageTopOfQueue(String path,Handler handler,int what){
		Message msg = new Message();
		msg.what = what;
		msg.obj = path;
		handler.sendMessageAtFrontOfQueue(msg);
	}
}
