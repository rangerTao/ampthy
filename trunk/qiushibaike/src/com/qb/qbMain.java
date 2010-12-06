package com.qb;

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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.adapter.QbAdapter;

public class qbMain extends Activity {

	private static qbMain appRef;
	public static ArrayList<Node> nodeList = new ArrayList<Node>();
	private ListView lvAll;
	private QbAdapter qba;
	private String url = "";
	
	//the handler
	private Handler handle = new Handler();
	
	private static int menu_refresh = Menu.FIRST;
	private static int menu_detail = Menu.FIRST + 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		appRef = this;
		lvAll = (ListView)findViewById(R.id.lvAll);
		qba = new QbAdapter(appRef);
		qba.notifyDataSetChanged();
		refreshAll();
	}

	public static qbMain getApp() {
		return appRef;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, menu_refresh, 0, R.string.menu_refresh);
		menu.add(0, menu_detail, 0, R.string.menu_detail);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch(item.getItemId()){
		case Menu.FIRST:
			refreshAll();
			break;
		case Menu.FIRST + 1 :
			startActivity(new Intent(qbMain.this,rssDetail.class));
		}
		return true;
	}
	
	public void refreshAll(){
		Log.v("Initialing", "start");
		
		setProgressBarIndeterminateVisibility(true);
		nodeList.clear();
		url = "http://news.163.com/special/00011K6L/rss_newstop.xml";
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
					System.out.println(document.toString());
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
		handle.post(new Runnable() {

			public void run() {
				lvAll.setAdapter(qba);
			}
		});
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