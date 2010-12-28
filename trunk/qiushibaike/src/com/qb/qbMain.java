package com.qb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.w3c.dom.Node;

import android.R.integer;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.adapter.FeedList;
import com.qb.activity.feed.AddRss;
import com.qb.activity.viewer.FeedViewer;
import com.qb.activity.viewer.IndexViewer;
import com.qb.parse.parseXML;

public class qbMain extends Activity {

	private static qbMain appRef;
	public static ArrayList<Node> nodeList = new ArrayList<Node>();
	private ListView lvAll;
	private FeedList feedL;
	private static final int cmIndex = Menu.FIRST + 10;
	public static String[] feedList;

	// file
	AssetManager aManager = null;

	// the handler
	private Handler handle = new Handler();

	private static int menu_edit = Menu.FIRST;
	private static int menu_delete = Menu.FIRST + 1;
	private static int menu_add = Menu.FIRST + 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		setTitle("ÃÏÃÏRSS");
		appRef = this;
		lvAll = (ListView) findViewById(R.id.lvAll);

		initlvAllListener();

		feedL = new FeedList(appRef);
		feedL.notifyDataSetChanged();
		refreshAll();

	}

	public static qbMain getApp() {
		return appRef;
	}

	public void initlvAllListener() {

		// context menu
		lvAll.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("");
				menu.add(0, cmIndex, 0, R.string.menu_edit);
				menu.add(0, cmIndex + 1, 1, R.string.menu_delete);
				menu.add(0, cmIndex + 2, 2, R.string.menu_add);
			}
		});

		lvAll.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent newFeedIntent = new Intent(appRef, FeedViewer.class);
				Bundle bundle = new Bundle();
				String ipRegex = "^http://*";
				String url = appRef.feedList[arg2].split(";")[1].toString();

				bundle.putString("url", url);

				bundle.putString("name",
						appRef.feedList[arg2].split(";")[0].toString());
				newFeedIntent.putExtras(bundle);
				appRef.startActivity(newFeedIntent);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu_edit, 0, R.string.menu_refresh);
		menu.add(0, menu_delete, 0, R.string.menu_delete);
		menu.add(0, menu_add, 0, R.string.menu_add);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			refreshAll();
			break;
		case Menu.FIRST + 1:
			startActivity(new Intent(qbMain.this, IndexViewer.class));
			break;
		case Menu.FIRST + 2:
			startActivity(new Intent(qbMain.this, AddRss.class));
			break;
		case cmIndex:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Bundle eIndex = new Bundle();

			eIndex.putString("editIndex", info.position + "");
			Intent editIntent = new Intent(appRef, AddRss.class);
			editIntent.putExtras(eIndex);
			appRef.startActivity(editIntent);
			break;

		case cmIndex + 1:

			break;

		case cmIndex + 2:
			startActivity(new Intent(qbMain.this, AddRss.class));
			break;
		}
		return true;
	}

	public void refreshAll() {
		Log.v("Initialing", "start");

		setProgressBarIndeterminateVisibility(true);
		nodeList.clear();

		aManager = getAssets();
		InputStream iStream;
		try {
			iStream = aManager.open("feed.xml");
			Log.v("test", "the locales"+aManager.getLocales());
			feedList = parseXML.readXML(iStream);
			setTheAdapterOfListView();
			invisTheProcessbar();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// set the content of the listview
	public void setTheAdapterOfListView() {
		handle.post(new Runnable() {

			public void run() {
				lvAll.setAdapter(feedL);
			}
		});
	}

	// Set the processBar as invisiablity
	public void invisTheProcessbar() {
		handle.post(new Runnable() {

			public void run() {
				setProgressBarIndeterminateVisibility(false);
			}
		});

	}

}