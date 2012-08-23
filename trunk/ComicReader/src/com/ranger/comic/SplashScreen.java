package com.ranger.comic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashScreen extends Activity {

	SplashScreen appref;

	private final static int SKIPTOMAIN = 999;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SKIPTOMAIN:
				Intent intent = new Intent(appref, ComicReader.class);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appref = this;
		setContentView(R.layout.splashscreen);

		Message msg = new Message();
		msg.what = SKIPTOMAIN;
		mHandler.sendMessageDelayed(msg, 1000);

	}

}
