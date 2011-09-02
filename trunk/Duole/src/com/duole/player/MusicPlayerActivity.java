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
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.LinearLayout;

import com.duole.R;
import com.duole.activity.PlayerBaseActivity;
import com.duole.pojos.adapter.AssetItemAdapter;
import com.duole.utils.Constants;

public class MusicPlayerActivity extends PlayerBaseActivity implements OnFocusChangeListener, OnItemSelectedListener {

	MediaPlayer mp;
	String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicplayer);
		Intent intent = getIntent();

		Gallery gallery = (Gallery)findViewById(R.id.musicGallery);
		
		gallery.setOnFocusChangeListener(this);

		gallery.setOnItemSelectedListener(this);
		
		gallery.setAdapter(new AssetItemAdapter(Constants.MusicList));
		
		mp = new MediaPlayer();
		
		int index = Integer.parseInt(intent.getStringExtra("index"));
		
		gallery.setSelection(index - 1);
		
		setMusicData(index -1 );

	}

	public void setMusicData(int index) {
		String filename = Constants.MusicList.get(index).getUrl();

		if (filename.startsWith("http:")) {
		} else {
			filename = filename.substring(filename.lastIndexOf("/"));
		}

		if (filename.startsWith("http")) {
			url = filename;
		} else {
			url = Constants.CacheDir + Constants.RES_AUDIO + filename;
		}

		LinearLayout llMusicPlayer = (LinearLayout) findViewById(R.id.llMusicPlayer);

		if (!Constants.bgRestUrl.equals("")) {
			File file = new File(Constants.CacheDir
					+ Constants.bgRestUrl.substring(Constants.bgRestUrl
							.lastIndexOf("/")));
			if (file.exists()) {
				llMusicPlayer.setBackgroundDrawable(Drawable
						.createFromPath(file.getAbsolutePath()));
			}
		}

		try {
			
			mp.setDataSource(this, Uri.fromFile(new File(url)));
			mp.prepare();
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			
			if (!Constants.SLEEP_TIME && !Constants.ENTIME_OUT) {
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

	public void onFocusChange(View view, boolean value) {
		
				Log.v("TAG", "Focus changed");
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		mp.stop();
		mp.release();
		mp = new MediaPlayer();

		setMusicData(arg2);

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
