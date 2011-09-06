package com.duole.asynctask;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.duole.Duole;
import com.duole.R;
import com.duole.pojos.asset.Asset;
import com.duole.thread.DeleteAssetFilesThread;
import com.duole.utils.Constants;
import com.duole.utils.DownloadFileUtils;
import com.duole.utils.DuoleNetUtils;
import com.duole.utils.DuoleUtils;
import com.duole.utils.JsonUtils;

public class ItemListTask extends AsyncTask {

	ArrayList<Asset> alAsset;
	
	static TextView tvDeviceId;
	static TextView tvUserName;
	static TextView tvPassword;
	static EditText etUserName;
	static EditText etPassword;


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
			}
		} else {
			for (Asset asset : alAsset) {
				Constants.DownLoadTaskList.add(asset);
			}
		}

		if (alAssetDeleteList.size() > 0) {
			new DeleteAssetFilesThread(alAssetDeleteList).start();
		}

		if (DownloadFileUtils.downloadAll()) {
			Duole.appref.sendBroadcast(new Intent(Constants.Refresh_Complete));
		}
		
		DuoleUtils.updateAssetListFile(alAsset);
		
	}

	/**
	 * Get resource list from server.
	 */
	public void getSourceList() {
		try {
			String url = //					"http://www.duoleyuan.com/e/member/child/ancJn.php?cc="	+ "7c71f33fce7335e4");
			"http://www.duoleyuan.com/e/member/child/ancJn.php?cc=" + DuoleUtils.getAndroidId();

			Log.v("TAG",DuoleUtils.getAndroidId());
			alAsset = new ArrayList<Asset>();
			JSONObject jsonObject = new JSONObject(DuoleNetUtils.connect(url));

			String error = null;
			try {
				error = jsonObject.getString("errstr");
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			if (error != null) {
				bindDevice();
				getSourceList();
			} else {
				JsonUtils.parserJson(alAsset, jsonObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static boolean bindDevice(){
		
		LayoutInflater inflater = LayoutInflater.from(Duole.appref);
		View inputView = inflater.inflate(R.layout.registerdevice, null);
		
		initBindDeviceView(inputView);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(Duole.appref);
		builder.setView(inputView);
		builder.setTitle(R.string.bindDeviceTitle);
		builder.setPositiveButton(R.string.btnPositive, new OnClickListener(){

			public void onClick(DialogInterface arg0, int arg1) {
				try {
					
					if(checkUserName()){
						String url = "http://www.duoleyuan.com/e/enews/?enews=BindCmcode&username=" +
								etUserName.getText().toString() +
								"&password=" +
								etPassword.getText().toString() +
								"&cmcode=" + DuoleUtils.getAndroidId();
						JSONObject jsonObject = new JSONObject(DuoleNetUtils.connect(url));
						
						String error = null;
						try{
							error = jsonObject.getString("errstr");
							Log.v("TAG", error);
						}catch(Exception e){
							e.printStackTrace();
						}
						
					}


				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
		});
		
		builder.setNegativeButton(R.string.btnNegative, new OnClickListener(){

			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		builder.show();
		
		return true;
	}

	private static boolean checkUserName(){
		
		
		
		return true;
	}
	
	private static void initBindDeviceView(View view){
		
		tvDeviceId = (TextView) view.findViewById(R.id.tvDeviceID);
		tvUserName = (TextView) view.findViewById(R.id.tvUserName);
		tvPassword = (TextView) view.findViewById(R.id.tvPassword);
		etUserName = (EditText) view.findViewById(R.id.etUserName);
		etPassword = (EditText) view.findViewById(R.id.etPassword);
		
		tvDeviceId.setText(Duole.appref.getString(R.string.strDeviceId) + " ��" + DuoleUtils.getAndroidId());
		tvUserName.setText(R.string.strUserName);
		tvPassword.setText(R.string.strPassword);
		
	}
	
}
