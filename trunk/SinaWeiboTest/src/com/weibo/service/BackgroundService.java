package com.weibo.service;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;

import weibo4android.Count;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service{

	Weibo weibo;
	boolean isRunning = true;
	NotificationManager nm;
	int notification_id=24617195;
	Notification notification;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		
		weibo = OAuthConstant.getInstance().getWeibo();
		
		nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Log.v("TAG", "Service start");
		new Thread(new Runnable(){

			public void run() {
				while(isRunning){
					try {
						Log.v("TAG", "Service will sleep for 10 minutes!");
						Thread.sleep(10000);
						
						Count count = weibo.getUnread();
						StringBuffer sb = new StringBuffer();
						if(count.getRt() != 0){
							sb.append("" + count.getRt());
							Log.v("TAG", "Service get " + count.getRt() +" new records.");
							showNotification(R.drawable.icon,"test","title",sb.toString());
						}

					} catch (WeiboException e) {
						
					} catch (InterruptedException e) {
						Log.v("TAG", e.getMessage());
					}
					
					
				}
				
			}
			
		}).start();
		
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		isRunning = false;
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	public void showNotification(int icon,String tickertext,String title,String content){
    	notification.defaults=Notification.DEFAULT_ALL; 
    	PendingIntent pt=PendingIntent.getActivity(this, 0, new Intent(this,IndexActivity.class), 0);
    	notification.setLatestEventInfo(this,title,content,pt);
    	nm.notify(notification_id, notification);
 
    }
}
