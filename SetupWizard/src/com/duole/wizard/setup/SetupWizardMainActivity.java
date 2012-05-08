package com.duole.wizard.setup;

import java.util.List;

import com.duole.wizard.setup.adapter.WifiNetworkAdapter;
import com.duole.wizard.setup.utils.WifiUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SetupWizardMainActivity extends Activity implements
		OnClickListener, OnItemClickListener {

	private static Button btnStartWizard;
	
	LinearLayout llTip;

	Button btnMayBeNextTime;
	Button btnNextStep;

	AlertDialog adWifiScan;
	AlertDialog wifiPass;

	LayoutInflater layoutInflater;

	static SetupWizardMainActivity appref;

	View scanWifi;

	WifiManager wifiManager;
	WifiInfo wifiInfo;
	ListView lvWifiConnections;
	ProgressBar pbScanWifi;
	List<ScanResult> scanResults;

	TextView tvWifiStatus;
	static TextView tvTip;
	static TextView tvIntro;

	Handler mHandler;

	final int BTNSTART = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		appref = this;
		mHandler = new Handler();
		tvTip = (TextView) findViewById(R.id.tvTip);
		tvIntro = (TextView) findViewById(R.id.tvIntro);
		llTip = (LinearLayout) findViewById(R.id.llTip);
		layoutInflater = LayoutInflater.from(appref);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		btnStartWizard = (Button) findViewById(R.id.btnStart);
		btnMayBeNextTime = (Button) findViewById(R.id.btnNextTime);
		btnNextStep = (Button) findViewById(R.id.btnNext);

		btnStartWizard.setId(BTNSTART);

		scanWifi = layoutInflater.inflate(R.layout.scanwifi, null);
		pbScanWifi = (ProgressBar) scanWifi.findViewById(R.id.pbScan);
		lvWifiConnections = (ListView) scanWifi.findViewById(R.id.lvWifi);
		tvWifiStatus = (TextView) findViewById(R.id.tvWifiStatus);

		btnStartWizard.setOnClickListener(this);

		btnMayBeNextTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final View view = layoutInflater.inflate(R.layout.confirmexit,
						null);
				AlertDialog confirm = new AlertDialog.Builder(appref)
						.setTitle(R.string.tip_confirmexit)
						.setView(view)
						.setNegativeButton(R.string.btnNegative, null)
						.setPositiveButton(R.string.btnPositive,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										CheckBox cb = (CheckBox) view
												.findViewById(R.id.cbNeverShow);
										if (cb.isChecked()) {
											ContentResolver cr = appref
													.getContentResolver();
											Uri config = Uri
													.parse("content://com.duole.provider/config");
											ContentValues cv = new ContentValues();
											cv.put("name", "setup");
											cv.put("value", "1");
											cr.insert(config, cv);
										}

										appref.finish();
									}

								}).create();
				confirm.show();
			}
		});

		btnNextStep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!WifiUtils.isNetworkAvailable(appref)) {
					new AlertDialog.Builder(appref)
							.setTitle(R.string.wifi_config)
							.setMessage(R.string.wifi_notconfigured)
							.setNegativeButton(R.string.btnNegative, null)
							.setPositiveButton(R.string.btnPositive,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											startUserRegister();
										}
									}).create().show();
				}else{
					startUserRegister();
				}

			}
		});

		lvWifiConnections.setOnItemClickListener(this);
		
		refreshButtonState();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				tvIntro.setVisibility(View.INVISIBLE);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				llTip.setVisibility(View.VISIBLE);
			}
		}, 2000);
	}
	
	private static void startUserRegister(){

		new AlertDialog.Builder(appref)
				.setTitle(R.string.register_user)
				.setMessage(R.string.user_exists)
				.setPositiveButton(R.string.btn_yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(appref,
										RegisterUserActivity.class);
								intent.putExtra("exists", "0");
								appref.startActivity(intent);
								appref.finish();

							}
						})
				.setNegativeButton(R.string.btn_no,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(appref,
										RegisterUserActivity.class);
								intent.putExtra("exists", "1");
								appref.startActivity(intent);
								appref.finish();
							}
						}).create().show();

	}
	
	@Override
	protected void onDestroy() {
		
		try{
			unregisterReceiver(wifiReceiver);
		}catch (Exception e) {
		}
		
		super.onDestroy();
	}

	
	
	
	@Override
	protected void onResume() {
		
		refreshButtonState();
		
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case BTNSTART:

			if (adWifiScan == null) {
				adWifiScan = new AlertDialog.Builder(appref)
						.setTitle(getText(R.string.net_config))
						.setView(scanWifi)
						.setPositiveButton(getText(R.string.btnClose), null)
						.create();
			}

			adWifiScan.show();

			mHandler.postDelayed((new Runnable() {

				@Override
				public void run() {

					wifiManager.setWifiEnabled(true);
					wifiManager.startScan();
					scanResults = (List<ScanResult>) wifiManager
							.getScanResults();
					if (scanResults == null) {
						wifiManager.startScan();
						scanResults = (List<ScanResult>) wifiManager
								.getScanResults();
					}
					if (scanResults.size() > 0) {
						pbScanWifi.setVisibility(View.GONE);
						lvWifiConnections.setVisibility(View.VISIBLE);
						lvWifiConnections.setAdapter(new WifiNetworkAdapter(
								scanResults, appref));
					}
				}
			}), 2000);

			break;

		default:
			break;
		}

	}

	// When click one of the connections show in the dialog.
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		IntentFilter intentFilter = new IntentFilter(
				"android.net.wifi.WIFI_STATE_CHANGED");
		intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		appref.registerReceiver(wifiReceiver, intentFilter);

		final ScanResult sr = scanResults.get(position);
		final EditText etPassword = new EditText(appref);
		boolean isConfiged = false;
		etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD
				| InputType.TYPE_CLASS_TEXT);
		List<WifiConfiguration> wificonfigs = wifiManager
				.getConfiguredNetworks();

		// find out whether there is any wifi is configured.
		for (WifiConfiguration temp : wificonfigs) {

			if (temp.SSID.equals("\"" + sr.SSID + "\"")) {

				isConfiged = wifiManager.enableNetwork(temp.networkId, true);

				WifiInfo wifiinfo = wifiManager.getConnectionInfo();

				Log.v("TAG", wifiinfo.getNetworkId() + "");
				if (wifiinfo.getNetworkId() == -1) {
					wifiManager.removeNetwork(temp.networkId);
					isConfiged = false;
				}

				if (adWifiScan != null)
					adWifiScan.dismiss();
			}
		}

		// if none
		if (!isConfiged) {

			int res = 0;
			if (WifiUtils.getSecurity(sr) == 0) {
				WifiConfiguration config = new WifiConfiguration();
				config.SSID = "\"" + sr.SSID + "\"";
				config.allowedKeyManagement.set(KeyMgmt.NONE);
				int networkId = wifiManager.addNetwork(config);
				if (networkId != -1) {
					wifiManager.enableNetwork(networkId, false);
					wifiManager.saveConfiguration();
					if (adWifiScan != null) {
						adWifiScan.dismiss();
					}
				}

			} else {

				wifiPass = new AlertDialog.Builder(appref)
						.setTitle(R.string.input_wifi_pass)
						.setView(etPassword)
						.setNegativeButton(R.string.btnNegative,
								new AlertDialog.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {

									}

								})
						.setPositiveButton(R.string.btnPositive,
								new AlertDialog.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										String strWifiPass;

										if (etPassword.getText().toString()
												.length() <= 0) {
											strWifiPass = "";
										} else {
											strWifiPass = etPassword.getText()
													.toString();
										}

										WifiConfiguration wc = new WifiConfiguration();
										// wc.BSSID = sr.BSSID;
										wc.SSID = "\"" + sr.SSID + "\"";

										wc.hiddenSSID = true;

										wc.status = WifiConfiguration.Status.ENABLED;

										WifiUtils.setWifiConfigurationSettings(
												wc, sr.capabilities,
												strWifiPass);
										int res = wifiManager.addNetwork(wc);

										Log.v("TAG", "network id" + res);
										if (res == -1) {
											Toast.makeText(appref,
													R.string.password_wrong,
													2000).show();
										} else {
											if (wifiManager.enableNetwork(res,
													true)) {
												wifiManager.saveConfiguration();
												wifiPass.dismiss();
												if (adWifiScan != null) {
													adWifiScan.dismiss();
												}
											} else {
												Toast.makeText(appref,
														"can not connect", 2000)
														.show();
											}
										}
										// }
									}

								}).create();
				wifiPass.show();
			}

		}

	}

	// a broadcast receiver to receive broadcasts related with wifi.
	BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {
				handleStateChanged(WifiInfo
						.getDetailedStateOf((SupplicantState) intent
								.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)));
			} else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {
				switch (wifiManager.getWifiState()) {
				case WifiManager.WIFI_STATE_DISABLED:
//					btnStartWizard.setText(appref
//							.getString(R.string.wifi_closed));
					break;
				case WifiManager.WIFI_STATE_DISABLING:
//					btnStartWizard.setText(appref
//							.getString(R.string.wifi_closing));
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					wifiInfo = wifiManager.getConnectionInfo();
					if (wifiInfo.getNetworkId() != -1) {

//						btnStartWizard.setText(getString(R.string.wifi_enabled)
//								+ wifiInfo.getSSID());

					} else {
//						btnStartWizard.setText(getString(R.string.wifi_opened));
					}
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					wifiInfo = wifiManager.getConnectionInfo();
//					btnStartWizard.setText(appref
//							.getString(R.string.wifi_enabling)
//							+ wifiInfo.getSSID());
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					break;
				}
			} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {
				handleStateChanged(((NetworkInfo) intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO))
						.getDetailedState());
			}

		}

	};

	public static void refreshButtonState() {
		
		if(WifiUtils.isNetworkAvailable(appref)){
			btnStartWizard.setBackgroundResource(R.drawable.p_user);
			tvTip.setText(R.string.useregister_tip);
			
			btnStartWizard.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!WifiUtils.isNetworkAvailable(appref)) {
						new AlertDialog.Builder(appref)
								.setTitle(R.string.wifi_config)
								.setMessage(R.string.wifi_notconfigured)
								.setNegativeButton(R.string.btnNegative, null)
								.setPositiveButton(R.string.btnPositive,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												startUserRegister();
											}
										}).create().show();
					}else{
						startUserRegister();
					}

				}
			});
			
		}
		
		
	}

	private void handleStateChanged(NetworkInfo.DetailedState state) {
		// WifiInfo is valid if and only if Wi-Fi is enabled.
		// Here we use the state of the check box as an optimization.
		if (state != null) {
			WifiInfo info = wifiManager.getConnectionInfo();
			if (info != null) {
//				btnStartWizard.setText();
				Summary.get(appref, info.getSSID(),
						state);
			}
		}
	}

}

class Summary {
	static String get(Context context, String ssid, DetailedState state) {
		String[] formats = context.getResources().getStringArray(
				(ssid == null) ? R.array.wifi_status
						: R.array.wifi_status_with_ssid);
		int index = state.ordinal();

		if (index >= formats.length || formats[index].length() == 0) {
			return null;
		}
		
		if (index == 5 && WifiUtils.isNetworkAvailable(context)) {

			SetupWizardMainActivity.refreshButtonState();

		}

		return String.format(formats[index], ssid);
	}

	static String get(Context context, DetailedState state) {
		return get(context, null, state);
	}
}