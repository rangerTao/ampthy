package com.duole.activity;

import com.duole.R;
import com.duole.utils.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;

public class FlashActivity extends PlayBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.flashplayer);
		
		WebView wvPlay = (WebView) findViewById(R.id.wvFlash);
		
		Intent intent = getIntent();
		
		String url = "file://" + Constants.CacheDir + "/flash/" + intent.getStringExtra("filename");
		
		wvPlay.getSettings().setPluginsEnabled(true);
		wvPlay.getSettings().setPluginState(PluginState.ON);
		
		wvPlay.loadUrl(url);
		
	}

	
	
}
