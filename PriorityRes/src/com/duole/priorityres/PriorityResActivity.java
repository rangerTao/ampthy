package com.duole.priorityres;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.duole.priorityres.util.PRApplication;
import com.duole.priorityres.util.XmlUtil;

import android.R.anim;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;

public class PriorityResActivity extends Activity {
	
	public static PriorityResActivity appref;
	PRApplication pra;
	
	private WebView webView;
	public Handler handler = new Handler();
	String basepath = "file:///mnt/sdcard/test/";
	
	String frontid;
	
	String pkgname;
	
    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		pra = (PRApplication) getApplication();
		
		appref = this;
		webView = (WebView) this.findViewById(R.id.webView);
		webView.getSettings().setAllowFileAccess(true);//
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setAppCacheEnabled(false);
		webView.getSettings().setAllowFileAccess(true);
		webView.addJavascriptInterface(new ContactJavaScript(this, handler,
				basepath), "duole");// js
		// java-->js
		
		Context otherContext = null;
		try {
			otherContext = createPackageContext("com.duole",
					Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		SharedPreferences sp = otherContext.getSharedPreferences("com.duole",
				MODE_WORLD_READABLE);
		String frontid = sp.getString("id", "");
		pkgname = sp.getString("package", "");
		if (!frontid.equals("")) {
			basepath = sp.getString("base", "");
		}
		
		String url = basepath + frontid + "/index.html";//
		
		pra.setBasePath(basepath + frontid);

		pra.setBasePath("/sdcard/test/");
		
		File file = new File(url);
		if(!file.exists()){
			
			url = "/sdcard/test/index.html";
			
			//If it is a new type of priority resource.
			if(new File(pra.getBasePath() + "/config.xml").exists()){
				if(XmlUtil.getPR(pra.getBasePath() + "/config.xml")){
					finish();
				}
			}
			if(pkgname != null && !pkgname.equals("")){
				startActivityByPkgName(pkgname);
			}else{
				setResult(2);
			}
			finish();
		}
		
		
		String baseUrl = basepath;
		
		webView.loadDataWithBaseURL(baseUrl, url, "text/html", "utf-8", null);
		webView.loadUrl("file://" + url);
		
		webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		
		webView.getSettings().setLightTouchEnabled(true);
		
		Log.v("TAG", url);
	}
	
    @Override
	protected void onPause() {

		android.os.Process.killProcess(android.os.Process.myPid());
		super.onPause();
	}
    
	void startActivityByPkgName(String pkgname){
		
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		try{
			List<ResolveInfo> lri = findActivitiesForPackage(PriorityResActivity.appref, pkgname);

			if (lri.size() > 0) {
				for (ResolveInfo ri : lri) {
					intent.setComponent(new ComponentName(pkgname,
							ri.activityInfo.name));
					PriorityResActivity.appref.startActivity(intent);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
    /**
     * Query the package manager for MAIN/LAUNCHER activities in the supplied package.
     */
    public List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);

        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }
	
}