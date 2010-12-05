package com.qb;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class rssDetail extends Activity{

	private SharedPreferences mysh;
	
	//The view on the layout
	private TextView tvTitle;
	private TextView tvDate;
	private TextView tvAuthor;
	private TextView tvDetail;
	private TextView tvUrl;
	
	//THe index of the listview
	private int index = 0;
	//OnCreate
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bIndex = this.getIntent().getExtras();
		index = bIndex.getInt("index");
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.detail);
		setTextViewContent();
	}
	
	public void setTextViewContent(){
		
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvDate  = (TextView)findViewById(R.id.tvDate);
		tvAuthor = (TextView)findViewById(R.id.tvAuthor);
		tvDetail = (TextView)findViewById(R.id.tvDetail);
		tvUrl  = (TextView)findViewById(R.id.tvUrl);
		
		//get the intent of the qbMain
		final qbMain am = qbMain.getApp();
		Node el = am.nodeList.get(index);
		NodeList nlmeta = el.getChildNodes();
		Log.v("debug", index+"       " + nlmeta.getLength());
		for (int i = 0; i < nlmeta.getLength(); i++) {
			Node elmeta = nlmeta.item(i);
			if (elmeta.getNodeName().equalsIgnoreCase("title")) {
				String temp = elmeta.getFirstChild()==null?"":elmeta.getFirstChild().getNodeValue();
				tvTitle.setText(temp);
				setTitle(temp);
			} else if (elmeta.getNodeName().equalsIgnoreCase("author")) {
				tvAuthor.setText(elmeta.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("description")) {
				tvDetail.setText(elmeta.getFirstChild()==null?"":elmeta.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("pubDate")) {
				tvDate.setText(elmeta.getFirstChild()==null?"":elmeta.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("link")) {
				tvUrl.setText(elmeta.getFirstChild()==null?"":elmeta.getFirstChild().getNodeValue());
			}
		}
		
		tvUrl.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				WebView wv = new WebView(getApp());
				
				 // Let's display the progress in the activity title bar, like the
				 // browser app does.
				 

				 wv.getSettings().setJavaScriptEnabled(true);

				 final Activity activity = getApp();
				 wv.setWebChromeClient(new WebChromeClient() {
				   public void onProgressChanged(WebView view, int progress) {
				     // Activities and WebViews measure progress with different scales.
				     // The progress meter will automatically disappear when we reach 100%
				     activity.setProgress(progress * 1000);
				   }
				 });
				 wv.setWebViewClient(new WebViewClient() {
				   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				     Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
				   }
				 });
				
				wv.loadUrl(tvUrl.getText().toString());
			}
			
		});
	}
	
	public rssDetail getApp(){
		return this;
	}
	
	
}
