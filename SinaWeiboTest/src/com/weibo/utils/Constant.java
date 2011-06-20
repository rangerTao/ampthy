package com.weibo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4andriod.Comment;
import weibo4andriod.DirectMessage;
import weibo4andriod.Status;
import weibo4andriod.User;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class Constant {

	/**
	 * DB
	 */
	public static boolean isRunning = false;
	
	public static String tag = "SinaWeibo";
	public static String dbName = "sina_weibo.db";
	public static final int dbVersion = 2;
	public static String tableName = "weibo";
	
	public static String app_name = tag;
	public static String token = "Token";
	public static String tokenSecret = "TokenSecret";
	public static final String ACCESSTOKEN = "AccessToken";
	public static final String ACCESSTOKENSECRET = "AccessTokenSecret";
	public static final String ISRUNNING = "IsRunning";
	
	public static final int _NOTRUNNING = 0;
	public static final int _ISRUNNING = 1;
	
	public static SharedPreferences spAll;
	
	public static final String CONSUMER_KEY="2902988107";
	public static final String CONSUMER_SECRET = "2fce81acf8fc9afb51ffc533688fa553";
	public static String _access = "";
	public static String _accessSecret = "";
	public static String _token = "";
	public static String _tokenSecret = "";
	
	public static int _Back_Count = 0;
	
	public static List<Comment> commentList = new ArrayList<Comment>();
	
	public static Bitmap[] imageMenu;
	public static Map<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
	
	public static GetImageThread git = new GetImageThread();
	public static SetImageThread sit = new SetImageThread();
	
	public static int atMe_PageIndex = 1;
	public static int favour_PageIndex = 1;
	public static int friend_PageIndex = 1;
	public static int mail_PageIndex = 1;
	
	//The flag of channel.
	public static int weiboChannel = 1;
	
	public static final int indexChannel = 1;
	public static final int atMeChannel = 2;
	public static final int favourChannel = 3;
	public static final int commentChannel = 4;
	public static final int mailChannel = 5;
	public static final int friendsChannel = 6;
	
	public static final boolean getMsg = false;
	
	/**
	 * Statuses
	 */
	public static ArrayList<Status> atMeList = new ArrayList<Status>();
	public static List<Comment> comList = new ArrayList<Comment>();
	public static List<Status> favourList = new ArrayList<Status>();
	public static List<User> friendsList = new ArrayList<User>();
	public static List<DirectMessage> mailList = new ArrayList<DirectMessage>();
	
	public static List<Comment> comments = new ArrayList<Comment>();
	public static List<Status> statuses = new ArrayList<Status>();
	public static List<User> useres = new ArrayList<User>();
	public static List<DirectMessage> mails = new ArrayList<DirectMessage>();
}