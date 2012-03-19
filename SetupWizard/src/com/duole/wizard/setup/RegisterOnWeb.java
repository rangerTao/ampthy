package com.duole.wizard.setup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class RegisterOnWeb extends Activity{
	
	ProgressBar pbPageLoad;
	WebView wvDuole;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.register);
		
		pbPageLoad = (ProgressBar) findViewById(R.id.pbWebLoad);
		wvDuole = (WebView) findViewById(R.id.wvDuole);
		
		wvDuole.getSettings().setPluginsEnabled(true);
		wvDuole.getSettings().setPluginState(PluginState.ON);
		
		wvDuole.loadUrl("http://wvw.duoleyuan.com/");
		
		wvDuole.getSettings().setJavaScriptEnabled(true);
		
		wvDuole.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {

				pbPageLoad.setProgress(newProgress);
				
				if(newProgress == 100){
					pbPageLoad.setVisibility(View.GONE);
				}
				
				super.onProgressChanged(view, newProgress);
			}
		});
		
		wvDuole.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
			
		});
		
	}

	
	
}
