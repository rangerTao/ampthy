package com.weibo.utils;

import com.weibo.R;
import com.weibo.Weibo;
import com.weibo.activity.IndexActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constant {

	/**
	 * DB
	 */
	public static String tag = "SinaWeibo";
	public static String dbName = "sina_weibo.db";
	public static final int dbVersion = 2;
	public static String tableName = "weibo";
	
	public static String app_name = tag;
	public static String token = "Token";
	public static String tokenSecret = "TokenSecret";
	public static final String ACCESSTOKEN = "AccessToken";
	public static final String ACCESSTOKENSECRET = "AccessTokenSecret";
	
	public static final String CONSUMER_KEY="2902988107";
	public static final String CONSUMER_SECRET = "2fce81acf8fc9afb51ffc533688fa553";
	public static String _access = "";
	public static String _accessSecret = "";
	public static String _token = "";
	public static String _tokenSecret = "";
	
	public static Bitmap[] imageMenu;
	
	static{
		imageMenu = new Bitmap[11];
		imageMenu[0] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.home);
		imageMenu[1] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.twitter);
		imageMenu[2] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.at);
		imageMenu[3] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.favourite);
		imageMenu[4] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.comment);
		imageMenu[5] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.mail);
		imageMenu[6] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.focus);
		imageMenu[7] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.fans);
		imageMenu[8] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.chat);
		imageMenu[9] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.search);
		imageMenu[10] = BitmapFactory.decodeResource(Weibo.appref.getResources(), R.drawable.broadcast);
	}

}