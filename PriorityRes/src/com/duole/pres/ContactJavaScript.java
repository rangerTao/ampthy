package com.duole.pres;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;

public class ContactJavaScript {
	private Handler handler;

	private MediaPlayer mp;
	private String basepath;
	
	public ContactJavaScript(Context context, Handler handler, String basepath) {
		this.handler = handler;
		this.basepath = basepath;
	}
	
	public void exitwhenright(){
		
		Log.d("TAG", "exit when right start");
		
		new Thread(){

			@Override
			public void run() {
				try {
					Thread.sleep(3500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.d("TAG", "exit when right");
				String pkgname = "";
				
				if(PriorityResActivity.appref != null){
					pkgname = PriorityResActivity.appref.pkgname;
				}else if(PResViewActivity.appref != null){
					pkgname = PResViewActivity.appref.pkgname;
				}
				
				if (pkgname != null && !pkgname.trim().equals("")) {
					if (PriorityResActivity.appref != null) {
						PriorityResActivity.appref
								.startActivityByPkgName(pkgname);
					} else if (PResViewActivity.appref != null) {
						PResViewActivity.appref.startActivityByPkgName(pkgname);
					}
				} else {
					if (PriorityResActivity.appref != null) {
						PriorityResActivity.appref.setResult(2);
					} else if (PResViewActivity.appref != null) {
						PResViewActivity.appref.setResult(2);
					}
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
