package com.qb.activity;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qb.R;
import com.qb.qbMain;
import com.qb.R.id;
import com.qb.R.layout;
import com.qb.activity.viewer.NewsViewer;


import android.app.Activity;
import android.content.Intent;
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

			public void onClick(View v) {
				String urlString = tvUrl.getText().toString();
				
				Bundle bundle = new Bundle();
				bundle.putString("url", urlString);
				Intent newIntent = new	Intent(getApp(),NewsViewer.class);
				newIntent.putExtras(bundle);
				getApp().startActivity(newIntent);
			}
			
		});
	}
	
	public rssDetail getApp(){
		return this;
	}
	
	
}
