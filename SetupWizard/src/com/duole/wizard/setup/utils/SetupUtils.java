package com.duole.wizard.setup.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.provider.Settings.System;
import android.util.Log;

public class SetupUtils {

	public static String dopost(Context context, String url, String username,
			String pass, String birth, String phone) {

		try {
			URL urlCon = new URL(url);

			DefaultHttpClient dhc = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(urlCon.toURI());

			ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();

			pairs.add(new BasicNameValuePair("email", username));
			pairs.add(new BasicNameValuePair("password", pass));
			pairs.add(new BasicNameValuePair("birthd", birth));
			pairs.add(new BasicNameValuePair("repassword", pass));
			pairs.add(new BasicNameValuePair("cmcode", getAndroidId(context)));
			pairs.add(new BasicNameValuePair("phone", phone));
			pairs.add(new BasicNameValuePair("enews", "register"));

			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
					"utf-8");

			httpPost.setEntity(p_entity);

			HttpResponse hr = dhc.execute(httpPost);
			HttpEntity he = hr.getEntity();
			InputStream is = he.getContent();

			String html = convertStreamToString(is);

			return html;
		} catch (Exception e) {
			Log.v("TAG", "Connect " + e.getMessage());
			return "";
		}
	}
	
	public static String dopost(Context context,String url,String username,String password) throws Exception{
		
		try {
			URL urlCon = new URL(url);

			Log.d("TAG", "bind cmcode url " + url);
			DefaultHttpClient dhc = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(urlCon.toURI());

			ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();

			pairs.add(new BasicNameValuePair("enews", "BindCmcode"));
			pairs.add(new BasicNameValuePair("username", username));
			pairs.add(new BasicNameValuePair("password", password));
			pairs.add(new BasicNameValuePair("cmcode", getAndroidId(context)));

			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
					"utf-8");

			httpPost.setEntity(p_entity);

			HttpResponse hr = dhc.execute(httpPost);
			HttpEntity he = hr.getEntity();
			InputStream is = he.getContent();

			String html = convertStreamToString(is);

			return html;
		} catch (Exception e) {
			Log.v("TAG", "Connect " + e.getMessage());
			throw e;
		}
		
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * Get the android of current device.
	 * 
	 * @return
	 */
	public static String getAndroidId(Context appref) {
		String androidId = System.getString(appref.getContentResolver(),
				System.ANDROID_ID);
		return (androidId + " ");
	}

	/**
	 * down load content.
	 * 
	 * @param url
	 * @return
	 */
	public static String connect(String url) {

		try {
			URL urlCon = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) urlCon
					.openConnection();

			conn.setRequestMethod("GET");

			conn.setConnectTimeout(5 * 1000);

			InputStream inStream = conn.getInputStream();
			byte[] data = readFromInput(inStream);

			String html = new String(data, "gbk");

			return html;
		} catch (Exception e) {
			Log.v("TAG", "Connect " + e.getMessage());
			return "";
		}
	}
	

	/**
	 * Get bytes from input stream.
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readFromInput(InputStream inStream) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];

		int len = 0;

		while ((len = inStream.read(buffer)) != -1) {

			outStream.write(buffer, 0, len);

		}

		inStream.close();

		return outStream.toByteArray();

	}
}
