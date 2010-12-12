package com.qb.activity.viewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.adapter.QbAdapter;
import com.qb.R;

public class FeedViewer extends Activity{

	
	private static FeedViewer appRef;
	public static ArrayList<Node> nodeList = new ArrayList<Node>();
	private ListView lvAll;
	
	private QbAdapter qba;	
	
	private String url;
	
	//the handler
	private Handler handle = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.newslist);
		appRef = this;
		lvAll = (ListView)findViewById(R.id.lvAll);
		Bundle bundle = this.getIntent().getExtras();
		url = bundle.getString("url");
		setTitle(bundle.getString("name"));
		
		qba = new QbAdapter(appRef);
		qba.notifyDataSetChanged();
		refreshAll();
	}
	
	public static FeedViewer getApp() {
		return appRef;
	}
	
	public void refreshAll(){
		Log.v("Initialing", "start");
		
		setProgressBarIndeterminateVisibility(true);
		nodeList.clear();
		//url = "http://feed.feedsky.com/orz_";
		Log.v("URL ", url);
		new Thread(new Runnable(){

			public void run() {
				//HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
				HttpClient hc = new DefaultHttpClient();
				hc.getParams().setIntParameter(
						HttpConnectionParams.CONNECTION_TIMEOUT, 10000);
				hc.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
						10000);
				//hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
				HttpGet gm = new HttpGet(url);
				try {
					Log.v("Getting data.", "start");
					HttpResponse hr = hc.execute(gm);
					InputStream is = hr.getEntity().getContent();
					// ad1.wait();
					Log.v("Getting data.", "end");
					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document document = db.parse(is);
					
					NodeList nl = document.getElementsByTagName("item");
					StringBuffer sbResult = new StringBuffer();

					Log.v("the size of nodelist", nl.getLength() + "");
					for (int i = 0; i < nl.getLength(); i++) {
						Node el = nl.item(i);
						nodeList.add(el);
					}
					is.close();
					setTheAdapterOfListView();
					invisTheProcessbar();
				} catch (ClientProtocolException e) {
					Toast.makeText(appRef, "服务器连接不通", 50);

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (ParserConfigurationException e) {

					e.printStackTrace();
				} catch (SAXException e) {

					e.printStackTrace();
				}
				
			}
			
		}).start();
		
	}
	
	//set the content of the listview
	public void setTheAdapterOfListView() {
		Log.v("debug","set adapter start");
		handle.post(new Runnable() {

			public void run() {
				lvAll.setAdapter(qba);
			}
		});
		Log.v("debug","set adapter end");
	}
	
	//Set the processBar as invisiablity
	public void invisTheProcessbar(){
		handle.post(new Runnable() {

			public void run() {
				setProgressBarIndeterminateVisibility(false);
			}
		});
		
	}
	
	
}
