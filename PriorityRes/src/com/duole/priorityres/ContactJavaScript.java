package com.duole.priorityres;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;

public class ContactJavaScript {
	private Handler handler;

	private MediaPlayer mp;
	private String basepath;
	
	public ContactJavaScript(Context context, Handler handler, String basepath) {
		this.handler = handler;
		this.basepath = basepath;
	}
	
	public void exitwhenright(){
		new Thread(){

			@Override
			public void run() {
				try {
					Thread.sleep(3500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String pkgname = PriorityResActivity.appref.pkgname;
				if(pkgname != null && !pkgname.equals("")){
					PriorityResActivity.appref.startActivityByPkgName(pkgname);
				}else{
					PriorityResActivity.appref.setResult(2);
				}
				android.os.Process.killProcess(android.os.Process.myPid());
				super.run();
			}
			
		}.start();
		
	}

	public void playMusic(final String path){
		handler.post(new Runnable(){

			@Override
			public void run() {
				mp = new MediaPlayer();
				try {
					mp.setDataSource(basepath + path);
					mp.prepare();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mp.setOnPreparedListener(new OnPreparedListener(){

					@Override
					public void onPrepared(MediaPlayer mp) {
						mp.start();
					}
					
				});
			}
			
		});
	}
	
	public void setReturnValue(int clicks){
		
	}
	

}
