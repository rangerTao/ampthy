package com.duole.player;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.duole.R;
import com.duole.activity.PlayerBaseActivity;
import com.duole.utils.Constants;

public class MusicPlayerActivity extends PlayerBaseActivity {

	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicplayer);
		Intent intent = getIntent();
		String filename = intent.getStringExtra("filename");
		String url = "";

		mp = new MediaPlayer();

//		if (filename.startsWith("http")) {
//			url = filename;
//		} else {
//			url = Constants.CacheDir + Constants.RES_AUDIO + filename;
//		}
//
//		LinearLayout llMusicPlayer = (LinearLayout) findViewById(R.id.llMusicPlayer);
//
//		if (!Constants.bgRestUrl.equals("")) {
//			File file = new File(Constants.CacheDir
//					+ Constants.bgRestUrl.substring(Constants.bgRestUrl
//							.lastIndexOf("/")));
//			if (file.exists()) {
//				llMusicPlayer.setBackgroundDrawable(Drawable
//						.createFromPath(file.getAbsolutePath()));
//			}
//		}
//
//		try {
//
//			mp.setDataSource(this, Uri.fromFile(new File(url)));
//			mp.prepare();
//			mp.start();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			if (!Constants.ENTIME_OUT) {
				finish();
				sendBroadcast(new Intent(Constants.Event_AppEnd));
			}
			break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		mp.stop();
		super.onDestroy();
	}

}
