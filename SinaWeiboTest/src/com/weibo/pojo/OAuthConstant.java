package com.weibo.pojo;

import weibo4andriod.Weibo4sina;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;


public class OAuthConstant {
	private static Weibo4sina weibo=null;
	private static OAuthConstant instance=null;
	private RequestToken requestToken;
	private AccessToken accessToken;
	private String token;
	private String tokenSecret;
	private OAuthConstant(){};
	public static synchronized OAuthConstant getInstance(){
		if(instance==null)
			instance= new OAuthConstant();
		return instance;
	}
	public Weibo4sina getWeibo(){
		if(weibo==null){
			weibo= new Weibo4sina();
			
		}
			
		return weibo;
	}
	
	public AccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
		this.token=accessToken.getToken();
		this.tokenSecret=accessToken.getTokenSecret();
	}
	public RequestToken getRequestToken() {
		return requestToken;
	}
	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	
}
