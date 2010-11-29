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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class qbMain extends Activity {
	Button btnfresh;
	private static qbMain appRef;
	public static ArrayList<Element> nodeList = new ArrayList<Element>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appRef = this;
		btnfresh = (Button) findViewById(R.id.refresh);
		btnfresh.setOnClickListener(new OnClickListener() {
		StringBuffer result = new StringBuffer();
			public void onClick(View v) {

				Log.v("Initialing", "start");

				String url = "http://feed.feedsky.com/orz_";
				Log.v("URL ", url);
				// HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
				HttpClient hc = new DefaultHttpClient();
				hc.getParams().setIntParameter(
						HttpConnectionParams.CONNECTION_TIMEOUT, 10000);
				hc.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
						10000);
				// hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				// proxy);
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

					Log.v("the size of nodelist", nl.getLength() + "");
					for (int i = 0; i < nl.getLength(); i++) {
						Element el = (Element) nl.item(i);
					}
					is.close();
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
		});
	}
}