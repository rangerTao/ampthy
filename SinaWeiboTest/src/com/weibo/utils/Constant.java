package com.weibo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4andriod.Comment;
import weibo4andriod.User;

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
	
	public static List<Comment> commentList = new ArrayList<Comment>();
	
	public static Bitmap[] imageMenu;
	public static Map<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
	
	public static GetImageThread git = new GetImageThread();
}