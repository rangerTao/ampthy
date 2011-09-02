package com.duole.receiver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import com.duole.Duole;
import com.duole.R;
import com.duole.layout.ScrollLayout;
import com.duole.player.FlashPlayerActivity;
import com.duole.player.MusicPlayerActivity;
import com.duole.pojos.adapter.AssetItemAdapter;
import com.duole.pojos.asset.Asset;
import com.duole.service.BackgroundRefreshService;
import com.duole.utils.Constants;
import com.duole.utils.DuoleUtils;
import com.duole.utils.XmlUtils;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RefreshCompeleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Constants.Refresh_Complete)) {

			Log.v("TAG",
					"refresh complete : "
							+ new SimpleDateFormat("yyyy MM dd HH mm ss")
									.format(new Date(System.currentTimeMillis())));

			Duole.appref.mHandler.post(new Runnable() {

				public void run() {

					Intent intent = new Intent(Duole.appref,
							BackgroundRefreshService.class);

					Duole.appref.bindService(intent, Duole.appref.mConnection,
							Context.BIND_AUTO_CREATE);

					// get all apps
					try {
						Constants.AssetList = XmlUtils.readXML(null, Constants.CacheDir
								+ "itemlist.xml");
						
						Duole.appref.getMusicList(Constants.AssetList);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// the total pages
					int PageCount = (int) Math.ceil(Constants.AssetList.size()
							/ Constants.APP_PAGE_SIZE) + 1;
					
					
					ScrollLayout sl = Duole.appref.mScrollLayout;

					for (int i = 0; i < PageCount; i++) {
						if(i > sl.getChildCount() - 1 ){
							GridView appPage = new GridView(Duole.appref);
							// get the "i" page data
							AssetItemAdapter aia = new AssetItemAdapter(Duole.appref, Constants.AssetList,
									i);
							appPage.setAdapter(aia);

							appPage.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
							
							appPage.setNumColumns(3);
							
							appPage.setPadding(0, 10, 0, 0);
							
							appPage.setVerticalSpacing(20);

							appPage.setOnItemClickListener(Duole.appref.listener);
							Duole.appref.mScrollLayout.addView(appPage);

						}else{
							GridView appPage = (GridView) sl.getChildAt(i);
							// get the "i" page data
							AssetItemAdapter aia = new AssetItemAdapter(Duole.appref, Constants.AssetList,
									i);
							appPage.setAdapter(aia);

						}
						
						
					}
					
					DuoleUtils.setChildrenDrawingCacheEnabled(Duole.appref.mScrollLayout,true);
					
					Constants.DOWNLOAD_RUNNING = false;
					
				}

			});

		}
		
		
	}
	
}
