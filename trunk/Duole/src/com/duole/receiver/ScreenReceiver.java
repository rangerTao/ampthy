package com.duole.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		Log.v("TAG", arg1.getAction());
		
	}

}
