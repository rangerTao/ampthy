package com.qb.activity.viewer;

import com.qb.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class IndexViewer extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsviewer);
		
		WebView wView = (WebView)findViewById(R.id.wvNews);
		
		wView.getSettings().setJavaScriptEnabled(true);
		wView.addJavascriptInterface(this, "javatojs");
		
		wView.loadUrl("file:///android_asset/index.html");
	}
	
	public void login(String name,String passwd){
		
		Toast.makeText(this, name + "   " + passwd, 5000).show();
	}
	
}
