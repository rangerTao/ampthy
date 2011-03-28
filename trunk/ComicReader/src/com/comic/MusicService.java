package com.comic;

import java.io.File;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service{

	private MediaPlayer mplayer;
	public static final String Music_Complete = "Music Complete";
	
	MediaPlayer.OnCompletionListener CompleteListener= new MediaPlayer.OnCompletionListener(){

		@Override
		public void onCompletion(MediaPlayer mp) {
			Intent i = new Intent(Music_Complete);
			sendBroadcast(i);
		}
		
	};
	
	public class LocalBinder extends Binder{
		public MusicService getService(){
			return MusicService.this;
		}
	}
	
	public void onCreate(){
		Log.v("TAG", "Service Create");
		super.onCreate();
	}
	
	public void onStart() throws IllegalArgumentException, IllegalStateException, IOException{
		Log.v("TAG","Service Start");
		File file = new File(Environment.getExternalStorageDirectory(),"priview.mp3");
		
		mplayer = MediaPlayer.create(this,null);
		mplayer.setDataSource(file.getAbsolutePath());
		mplayer.start();
	}
	
	public void onPause(){
		Log.v("TAG", "Service Pause");
		mplayer.pause();
	}
	
	public void onDestory(){
		Log.v("TAG", "Service Destory");
		mplayer.stop();
	}
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
