package com.qb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.w3c.dom.Node;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.ListView;

import com.adapter.FeedList;
import com.qb.activity.feed.AddRss;
import com.qb.activity.viewer.IndexViewer;
import com.qb.parse.parseXML;

public class qbMain extends Activity {

	private static qbMain appRef;
	public static ArrayList<Node> nodeList = new ArrayList<Node>();
	private ListView lvAll;
	private FeedList feedL;
	public static String[] feedList;
	
	//file
	AssetManager aManager = null;
	
	//the handler
	private Handler handle = new Handler();
	
	private static int menu_edit = Menu.FIRST;
	private static int menu_delete = Menu.FIRST + 2;
	private static int menu_add = Menu.FIRST + 1;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		setTitle("ÃÏÃÏRSS");
		appRef = this;
		lvAll = (ListView)findViewById(R.id.lvAll);

		lvAll.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, menu_edit, 0, R.string.menu_edit);
				menu.add(0, menu_add, 0, R.string.menu_add);
				menu.add(0, menu_delete, 0, R.string.menu_delete);
				
			}
		});
		feedL = new FeedList(appRef);
		feedL.notifyDataSetChanged();
		refreshAll();
	}

	public static qbMain getApp() {
		return appRef;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		
//		menu.add(0, menu_refresh, 0, R.string.menu_refresh);
//		menu.add(0, menu_detail, 0, R.string.menu_detail);
//		menu.add(0, menu_add, 0, R.string.menu_add);
//		
//		return super.onCreateOptionsMenu(menu);
//	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch(item.getItemId()){
		case Menu.FIRST:
			refreshAll();
			break;
		case Menu.FIRST + 1 :
			startActivity(new Intent(qbMain.this,IndexViewer.class));
		case Menu.FIRST + 2 :
			startActivity(new Intent(qbMain.this,AddRss.class));
		}
		return true;
	}
	
	public void refreshAll(){
		Log.v("Initialing", "start");
		
		setProgressBarIndeterminateVisibility(true);
		nodeList.clear();
		
		aManager = getAssets();
		InputStream iStream;
		try {
			iStream = aManager.open("feed.xml");
			feedList = parseXML.readXML(iStream);
			setTheAdapterOfListView();
			invisTheProcessbar();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//set the content of the listview
	public void setTheAdapterOfListView() {
		handle.post(new Runnable() {

			public void run() {
				lvAll.setAdapter(feedL);
			}
		});
	}
	
	//Set the processBar as invisiablity
	public void invisTheProcessbar(){
		handle.post(new Runnable() {

			public void run() {
				setProgressBarIndeterminateVisibility(false);
			}
		});
		
	}
	
	
}