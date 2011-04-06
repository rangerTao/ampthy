package com.weibo.utils;

public class WeiboUtils {

	/**
	 * To check the format of email
	 */
	public static boolean isEmail(String email){
		if(email.indexOf("@") == -1){
			return false;
		}else if(email.indexOf(".") == -1){
			return false;
		}
		return true;
	}
}
