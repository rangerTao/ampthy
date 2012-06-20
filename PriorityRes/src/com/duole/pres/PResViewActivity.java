package com.duole.pres;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.duole.pres.util.PRApplication;
import com.duole.pres.util.XmlUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;

public class PResViewActivity extends BaseActivity {

	public static PResViewActivity appref;
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
		// webView = (WebView) this.findViewById(R.id.webView);
		// webView.getSettings().setAllowFileAccess(true);//
		// webView.getSettings().setJavaScriptEnabled(true);
		// webView.getSettings().setSupportZoom(false);
		// webView.getSettings().setAppCacheEnabled(false);
		// webView.getSettings().setAllowFileAccess(true);
		// webView.addJavascriptInterface(new ContactJavaScript(this, handler,
		// basepath), "duole");// js
		// webView.loadDataWithBaseURL(baseUrl, url, "text/html", "utf-8",
		// null);
		// webView.loadUrl("file://" + url);
		// webView.getSettings().setPluginsEnabled(true);
		// webView.getSettings().setPluginState(PluginState.ON);
		//
		// webView.getSettings().setLightTouchEnabled(true);
		// java-->js

		Intent intent = getIntent();
		String action = intent.getAction();
		if (intent.ACTION_VIEW.equals(action)) {
			String input = intent.getDataString();

			basepath = input.split(",")[0];
			frontid = input.split(",")[1];
			pkgname = input.split(",")[2];

		}

		String url = basepath + frontid + "/index.html";//

		pra.setBasePath(basepath + frontid);

		pra.setPkgname(pkgname);

		Log.d("TAG", "base path is :" + pra.getBasePath());

		// File file = new File(url + " dsfdsf");
		File file = new File(url);
		if (!file.exists()) {

			// If it is a new type of priority resource.
			if (new File(pra.getBasePath() + "/config.xml").exists()) {
				if (XmlUtil.getPR(getApplicationContext(), pra, pra.getBasePath() + "/config.xml")) {
					finish();
				}
			} else if (pkgname != null && !pkgname.equals("")) {
				startActivityByPkgName(pkgname);
				finish();
			} else {
				setResult(2);
				finish();
			}

		} else {

			Intent intent2 = new Intent(this, PriorityResActivity.class);

			intent2.putExtra("path", url);
			intent2.putExtra("packagename", pkgname);

			startActivity(intent2);
		}

	}

	@Override
	protected void onPause() {

		android.os.Process.killProcess(android.os.Process.myPid());
		super.onPause();
	}

	void startActivityByPkgName(String pkgname) {

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

		try {
			List<ResolveInfo> lri = findActivitiesForPackage(PResViewActivity.appref, pkgname);

			Log.d("TAG", pkgname);
			if (lri.size() > 0) {
				for (ResolveInfo ri : lri) {
					intent.setComponent(new ComponentName(pkgname, ri.activityInfo.name));
					PResViewActivity.appref.startActivity(intent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Query the package manager for MAIN/LAUNCHER activities in the supplied
	 * package.
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
