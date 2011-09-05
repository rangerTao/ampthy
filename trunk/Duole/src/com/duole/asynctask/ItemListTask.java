package com.duole.asynctask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.duole.Duole;
import com.duole.pojos.asset.Asset;
import com.duole.thread.DeleteAssetFilesThread;
import com.duole.utils.Constants;
import com.duole.utils.DownloadFileUtils;
import com.duole.utils.DuoleUtils;
import com.duole.utils.JsonUtils;

import android.content.Intent;
import android.os.AsyncTask;

public class ItemListTask extends AsyncTask {

	ArrayList<Asset> alAsset;

	@Override
	protected Object doInBackground(Object... arg0) {
		try {
			getSourceList();
			
			treatData();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Put all items into a map.To see which is already exists on sdcard.
	 */
	@Override
	protected void onPostExecute(Object result) {
		if ((Boolean) result) {
			
		}

		super.onPostExecute(result);
	}

	public void treatData() {
		HashMap<String, Asset> hmSource = new HashMap<String, Asset>();
		for (int i = 0; i < alAsset.size(); i++) {
			Asset ass = alAsset.get(i);
			if (ass != null) {
				hmSource.put(ass.getId(), ass);
			}
		}

		ArrayList<Asset> alAssetDeleteList = DuoleUtils.getAssetDeleteList(
				hmSource, Constants.AssetList);

		Constants.DownLoadTaskList = new ArrayList<Asset>();
		if (Constants.AssetList != null && Constants.AssetList.size() > 0) {
			for (int i = 0; i < Constants.AssetList.size(); i++) {
				Asset ass = Constants.AssetList.get(i);
				if (hmSource.containsKey(ass.getId())) {
					if (DuoleUtils.checkDownloadNecessary(ass,
							hmSource.get(ass.getId()))) {
						Constants.DownLoadTaskList.add(ass);
					}
				}
				// else {
				// Constants.DownLoadTaskList.add(ass);
				// }
			}
		} else {
			for (Asset asset : alAsset) {
				Constants.DownLoadTaskList.add(asset);
			}
		}

		// if(Constants.DownLoadTaskList.size() > 0){
		DuoleUtils.updateAssetListFile(alAsset);
		// }

		if (alAssetDeleteList.size() > 0) {
			new DeleteAssetFilesThread(alAssetDeleteList).start();
		}

		if (DownloadFileUtils.downloadAll()) {
			Duole.appref.sendBroadcast(new Intent(Constants.Refresh_Complete));
		}
	}

	/**
	 * Get resource list from server.
	 */
	public void getSourceList() {
		try {
			int res = 0;
			URL url = new URL(
//					"http://www.duoleyuan.com/e/member/child/ancJn.php?cc="	+ "7c71f33fce7335e4");
			"http://www.duoleyuan.com/e/member/child/ancJn.php?cc=" + DuoleUtils.getAndroidId());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			conn.setConnectTimeout(5 * 1000);

			InputStream inStream = conn.getInputStream();
			byte[] data = DuoleUtils.readFromInput(inStream);

			String html = new String(data, "gbk");

			alAsset = new ArrayList<Asset>();
			JSONObject jsonObject = new JSONObject(html);

			String error = null;
			try {
				error = jsonObject.getString("errstr");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (error != null) {
				
			} else {
				JsonUtils.parserJson(alAsset, jsonObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
