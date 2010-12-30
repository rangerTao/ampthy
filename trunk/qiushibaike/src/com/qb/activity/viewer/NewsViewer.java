package com.qb.activity.viewer;

import com.qb.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsViewer extends Activity{
	
	public static NewsViewer appRef;
	// The main of the activity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appRef = getApp();
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.newsviewer);
		//WebViewClient wvc = new Callback();
		
		setProgressBarIndeterminateVisibility(true);
		
		WebView wView = (WebView) findViewById(R.id.wvNews);
		
		Bundle bundle = this.getIntent().getExtras();
		String urlString = bundle.getString("url");
		setTitle(urlString);

		setProgressBarIndeterminateVisibility(false);

		wView.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		wView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});
		wView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});

		wView.loadUrl(urlString);
		wView.setWebViewClient(new Callback());
		
	}
	
	public static NewsViewer getApp(){
		return NewsViewer.appRef;
	}
	
	private class Callback extends WebViewClient{

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			if(url.endsWith(".xml")){
//				new AlertDialog.Builder(NewsViewer.this).setTitle("添加RSS源").setMessage(url).setPositiveButton("添加", new DialogInterface.OnClickListener() {
//					
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//					}
//				}).setNegativeButton("不添加", new DialogInterface.OnClickListener() {
//					
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//					}
//				}).show();
//				return true;
//			}else{
//				return false;
//			}
			new AlertDialog.Builder(NewsViewer.this).setTitle("添加RSS源").setMessage(url).setPositiveButton("添加", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).setNegativeButton("不添加", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
			return true;
		}
	}
	
	
	
	

}
