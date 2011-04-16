package com.weibo.pojo.adapter;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import weibo4andriod.Status;
import weibo4andriod.User;

import com.weibo.R;
import com.weibo.activity.IndexActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeTimeLineAdapter extends BaseAdapter {

	ImageView ivUserHead;
	String urlString;
	Bitmap bmpUserHead;
	
	public int getCount() {
		Log.v("TAG", "indexActivity.statuses.size()"+IndexActivity.statuses.size());
		return IndexActivity.statuses.size();
	}

	public Object getItem(int position) {
		return IndexActivity.statuses.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.v("TAG", "HomeTimeLineAdapter getView");
		Status status = IndexActivity.statuses.get(position);
		User user = status.getUser();
		LayoutInflater layoutInflater = LayoutInflater
				.from(IndexActivity.appref);
		View view = layoutInflater.inflate(R.layout.friendstimeline_adapter,
				null);
		ivUserHead = (ImageView) view.findViewById(R.id.ivUserHead);
		TextView tvUserNameTextView = (TextView) view
				.findViewById(R.id.tvUserName);
		TextView tvUserLocationg = (TextView) view
				.findViewById(R.id.tvUserLocation);
		TextView tvUserDesc = (TextView) view.findViewById(R.id.tvUserDesc);
		TextView tvUserStatus = (TextView) view.findViewById(R.id.tvStatus);

		tvUserNameTextView.setText(user.getScreenName());
		tvUserLocationg.setText(user.getLocation());
		tvUserDesc.setText(user.getDescription().replace("\r", "").replace("\n", ""));
		
		IndexActivity.handler.post(new Runnable() {
			
			public void run() {
				DefaultHttpClient hc = new DefaultHttpClient();
				hc.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						10000);
				hc.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000);
				// hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);

				try {
					Log.v("Getting data.", "start");
					HttpGet gm = new HttpGet(urlString);
					HttpResponse hr = hc.execute(gm);
					InputStream is = hr.getEntity().getContent();
					// ad1.wait();
					Log.v("Getting data.", "end");

					bmpUserHead = BitmapFactory.decodeStream(is);

					ivUserHead.setImageBitmap(bmpUserHead);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		//tvUserStatus.setText(user.getStatusText().replace("\r", "").replace("\n", ""));
		return view;
	}
	
	class imageTask extends AsyncTask{

		
		@Override
		protected void onPostExecute(Object result) {
			ivUserHead.setImageBitmap(bmpUserHead);
			super.onPostExecute(result);
		}


		@Override
		protected void onPreExecute() {
			ivUserHead.setImageBitmap(BitmapFactory.decodeResource(IndexActivity.appref.getResources(), R.drawable.pic_error));
			super.onPreExecute();
		}


		@Override
		protected Object doInBackground(Object... params) {

			
			return null;
		}
		
	}

}
