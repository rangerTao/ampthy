package com.duole.player;

import java.io.File;

import com.duole.R;
import com.duole.activity.PlayerBaseActivity;
import com.duole.utils.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;

public class FlashPlayerActivity extends PlayerBaseActivity{

	WebView wvPlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.SetFullScreen();
		setContentView(R.layout.flashplayer);
		
		wvPlay = (WebView) findViewById(R.id.wvFlash);
		
		Intent intent = getIntent();
		
		String filename = intent.getStringExtra("filename");
		String url = "";
		if(filename.startsWith("http")){
			url = filename;
		}else{
			url = "file://" + Constants.CacheDir + "/game/" + filename;
		}
		
		File file = new File(Constants.CacheDir + "/game/" + filename);
		
		if(file.exists()){
			wvPlay.getSettings().setPluginsEnabled(true);
			wvPlay.getSettings().setPluginState(PluginState.ON);
			
			wvPlay.loadUrl(url);
		}else{
			wvPlay.setBackgroundResource(R.drawable.bg86);
		}
		
		
		
		wvPlay.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.indexOf("duoleyuan.com") != -1){
            		view.loadUrl(url);
            		return true;
            	}else{
            		return false;
            	}
			}
			
		});
		
		wvPlay.setLongClickable(false);
		wvPlay.setOnLongClickListener(new View.OnLongClickListener(){

			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	}
	
	

	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}



	@Override
	protected void onDestroy() {
		wvPlay.destroy();
		super.onDestroy();
	}
	
}
