package com.ranger.comic;

import com.ranger.comic.adapter.LocalItemAdapter;
import com.ranger.comic.adapter.RemoteItemListAdapter;
import com.ranger.comic.widget.ScrollLayout;
import com.ranger.comic.widget.ShelvesGridView;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;

public class ComicReader extends Activity {

	ScrollLayout mScrollLayout;
	View shelve;
	LocalItemAdapter localItemAdapter;
	ShelvesGridView shelveView;
	
	View viewRemote_item_list;
	ListView lvRemote_item_list;
	RemoteItemListAdapter remoteItemListAdapter;

	DisplayMetrics dm = new DisplayMetrics();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		setContentView(R.layout.main);

		mScrollLayout = (ScrollLayout) findViewById(R.id.scrollLayoutMain);

		viewRemote_item_list = getLayoutInflater().inflate(R.layout.remote_item_list, null);
		lvRemote_item_list = (ListView) viewRemote_item_list.findViewById(R.id.lvRemote_item_list);

		remoteItemListAdapter = new RemoteItemListAdapter(getApplicationContext());
		
		lvRemote_item_list.setAdapter(remoteItemListAdapter);
		
		mScrollLayout.addView(viewRemote_item_list);
		
		shelve = getLayoutInflater().inflate(R.layout.local_item_list, null);
		shelveView = (ShelvesGridView) shelve.findViewById(R.id.shelve);

		localItemAdapter = new LocalItemAdapter(getApplicationContext());

		shelveView.setAdapter(localItemAdapter);

		mScrollLayout.addView(shelve);
	}
}