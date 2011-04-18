package com.weibo.pojo.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
import com.weibo.utils.WeiboUtils;

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
	URL urlString;
	Bitmap bmpUserHead;
	Map<String, Bitmap> imageMap = new HashMap<String, Bitmap>();

	public int getCount() {
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
		Status status = IndexActivity.statuses.get(position);
		final User user = status.getUser();
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
		Log.v("TAG", user.getScreenName());
		tvUserLocationg.setText(user.getLocation());
		tvUserDesc.setSingleLine(true);
		if(user.getDescription() == null || user.getDescription().equals("") ){
			tvUserDesc.setVisibility(View.GONE);
		}else{
			tvUserDesc.setText(user.getDescription().toString());
		}
		
		urlString = user.getProfileImageURL();
		ivUserHead.setImageBitmap(BitmapFactory.decodeResource(
				IndexActivity.appref.getResources(), R.drawable.loading));
		if (imageMap.containsKey(user.getId() + "") == false) {
			imageMap.put(user.getId() + "", null);
			Bitmap tempBitmap = WeiboUtils.getImage(user.getProfileImageURL());
			WeiboUtils.setImage(IndexActivity.appref.handler, ivUserHead,
					tempBitmap);
			imageMap.put(user.getId() + "", tempBitmap);
		} else {
			ivUserHead.setImageBitmap(imageMap.get(user.getId() + ""));
		}
		tvUserStatus.setText(status.getText().toString());
		
		view.setPadding(0, 3, 0, 0);
		return view;
	}
}
