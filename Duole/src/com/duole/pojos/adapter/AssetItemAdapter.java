package com.duole.pojos.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duole.R;
import com.duole.pojos.adapter.AppAdapter.AppItem;
import com.duole.pojos.asset.Asset;
import com.duole.utils.Constants;

public class AssetItemAdapter extends BaseAdapter{
	
	private ArrayList<Asset> mList;
	private Context mContext;
	
	public AssetItemAdapter(Context context, List<Asset> list, int page) {
		mContext = context;

		mList = new ArrayList<Asset>();
		int i = page * Constants.APP_PAGE_SIZE;
		int iEnd = i + Constants.APP_PAGE_SIZE;
		while ((i < list.size()) && (i < iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}
	
	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		Asset asset = mList.get(position);
		
		AssetItem assItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);
			
			assItem = new AssetItem();
			assItem.ivAssetThumb = (ImageView)v.findViewById(R.id.ivAppIcon);
			assItem.tvAssetName = (TextView)v.findViewById(R.id.tvAppName);
			
			v.setTag(assItem);
			convertView = v;
		} else {
			assItem = (AssetItem)convertView.getTag();
		}
		// set the icon
		assItem.ivAssetThumb.setImageBitmap(BitmapFactory.decodeFile(Constants.CacheDir + "/thumbnail/" + asset.getThumbnail()));
		Log.v("TAG", Constants.CacheDir + "/thumbnail/" + asset.getThumbnail());
		// set the app name
		assItem.tvAssetName.setText(asset.getName());
		
		return convertView;
	}
	
	class AssetItem{
		ImageView ivAssetThumb;
		TextView tvAssetName;
	}

}
