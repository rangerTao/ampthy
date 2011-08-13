package com.duole;

import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class AllAppList extends Activity {
	private static final String TAG = "TAG";
	private ScrollLayout mScrollLayout;
	private static final float APP_PAGE_SIZE = 9.0f;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.main);
		
		mScrollLayout = (ScrollLayout)findViewById(R.id.ScrollLayoutTest);
		
		initViews();
	}

	public void initViews() {
		final PackageManager packageManager = getPackageManager();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // get all apps 
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        
        // the total pages
        final int PageCount = (int)Math.ceil(apps.size()/APP_PAGE_SIZE);
        Log.e(TAG, "size:"+apps.size()+" page:"+PageCount);
        for (int i=0; i<PageCount; i++) {
        	GridView appPage = new GridView(this);
        	// get the "i" page data
        	appPage.setAdapter(new AppAdapter(this, apps, i));
        	
        	appPage.setNumColumns(3);
        	
        	appPage.setOnItemClickListener(listener);
        	mScrollLayout.addView(appPage);
        }
	}
	
	/**
	 * The item click event of gridview
	 */
	public OnItemClickListener listener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			ResolveInfo appInfo = (ResolveInfo)parent.getItemAtPosition(position);
			Intent mainIntent = mContext.getPackageManager()
				.getLaunchIntentForPackage(appInfo.activityInfo.packageName);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			try {
				// launcher the package
				mContext.startActivity(mainIntent);
			} catch (ActivityNotFoundException noFound) {
				Toast.makeText(mContext, "Package not found!", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
