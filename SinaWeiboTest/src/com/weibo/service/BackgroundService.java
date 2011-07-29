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
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(isRunning){
					try {
						Count count = weibo.getUnread();
						StringBuffer sb = new StringBuffer();
						if(count.getRt() != 0){
							sb.append("" + count.getRt());
						}
						
						showNotification(R.drawable.icon,"test","title",sb.toString());
						
						Thread.sleep(10000);
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
    	//设置一个唯一的ID，随便设置
 
    	//Notification管理器
    	Notification notification=new Notification(icon,tickertext,System.currentTimeMillis());
    	//后面的参数分别是显示在顶部通知栏的小图标，小图标旁的文字（短暂显示，自动消失）系统当前时间（不明白这个有什么用）
    	notification.defaults=Notification.DEFAULT_ALL; 
    	//这是设置通知是否同时播放声音或振动，声音为Notification.DEFAULT_SOUND
    	//振动为Notification.DEFAULT_VIBRATE;
    	//Light为Notification.DEFAULT_LIGHTS，在我的Milestone上好像没什么反应
    	//全部为Notification.DEFAULT_ALL
    	//如果是振动或者全部，必须在AndroidManifest.xml加入振动权限
    	PendingIntent pt=PendingIntent.getActivity(this, 0, new Intent(this,IndexActivity.class), 0);
    	//点击通知后的动作，这里是转回main 这个Acticity
    	notification.setLatestEventInfo(this,title,content,pt);
    	nm.notify(notification_id, notification);
 
    }
}
