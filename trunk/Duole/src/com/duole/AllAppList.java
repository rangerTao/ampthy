package com.duole;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.duole.activity.FlashActivity;
import com.duole.layout.ScrollLayout;
import com.duole.pojos.adapter.AssetItemAdapter;
import com.duole.pojos.asset.Asset;
import com.duole.utils.Constants;
import com.duole.utils.parseXML;

public class AllAppList extends Activity {
	
	private static final String TAG = "TAG";
	private ScrollLayout mScrollLayout;
	private Context mContext;
	AllAppList appref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.main);
		
		mScrollLayout = (ScrollLayout)findViewById(R.id.ScrollLayoutTest);
		appref = this;
		try {
			initViews();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initViews() throws IOException, TransformerException, SAXException {
		// get all apps 
        Constants.AssetList = parseXML.readXML(null, Constants.CacheDir + "itemlist.xml");
        Log.v("TAG",Constants.AssetList.size() +"");
        // the total pages
        int PageCount = (int)Math.ceil(Constants.AssetList.size()/Constants.APP_PAGE_SIZE);
        if(PageCount == 0){
        	PageCount = 1;
        }
        Log.v("TAG",PageCount +"");
        for (int i=0; i<PageCount; i++) {
        	GridView appPage = new GridView(this);
        	// get the "i" page data
        	appPage.setAdapter(new AssetItemAdapter(this, Constants.AssetList, i));
        	
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
			Asset assItem = (Asset)parent.getItemAtPosition(position);
			
			Intent intent = new Intent(appref,FlashActivity.class);
			
			try {
				// launcher the package
				
				intent.putExtra("filename", assItem.getName());
				mContext.startActivity(intent);
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

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			finish();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
}
