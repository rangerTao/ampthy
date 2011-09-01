package com.duole.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.duole.asynctask.ItemListTask;
import com.duole.utils.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BackgroundRefreshReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Constants.Refresh_Start)) {

			Log.v("TAG", "Background refresh " + new SimpleDateFormat("yyyy MM dd HH mm ss")
					.format(new Date(System.currentTimeMillis())));
			if(!Constants.DOWNLOAD_RUNNING){
				Constants.DOWNLOAD_RUNNING = true;
				new ItemListTask().execute();
			}
		}

	}

}
