package com.duole.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.duole.player.MusicPlayerActivity;
import com.duole.pojos.DuoleCountDownTimer;
import com.duole.utils.Constants;

public class BaseActivity extends Activity {

	public Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {

			switch(msg.what){
			case Constants.REST_TIME:
				startMusicPlay();
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	public void startMusicPlay(){
		Intent MusicPlay = new Intent(this,MusicPlayerActivity.class);
		MusicPlay.putExtra("index", "1");
		startActivity(MusicPlay);
	}
	
	public void SetFullScreen() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch(keyCode){
		case KeyEvent.KEYCODE_HOME:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

}
