package com.duole.service;

import com.duole.receiver.BackgroundRefreshReceiver;
import com.duole.utils.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundRefreshService extends Service{

	BackgroundRefreshService brs;
	AlarmManager am;
	PendingIntent pii;
	@Override
	public IBinder onBind(Intent arg0) {

		am = (AlarmManager) this.getSystemService(ALARM_SERVICE);

		Intent ii = new Intent(Constants.Refresh_Start);
		pii = PendingIntent.getBroadcast(this, 0, ii, 0);

		am.setRepeating(AlarmManager.RTC, Constants.frequence, Constants.frequence, pii);

		return null;
	}
	
	public class LocalBinder extends Binder {
		public BackgroundRefreshService getService() {
                return BackgroundRefreshService.this;
        }
	} 

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
	}
	
	public BackgroundRefreshService getService(){
		brs = this;
		return brs;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		am.cancel(pii);
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		am.cancel(pii);
		return super.onUnbind(intent);
	}
	
	
	
	

}