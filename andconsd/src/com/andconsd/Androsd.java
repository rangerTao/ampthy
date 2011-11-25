package com.andconsd;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.andconsd.adapter.ImageThumbAdapter;
import com.andconsd.constants.Constants;
import com.andconsd.http.RequestListenerThread;
import com.andconsd.template.UiTemplate;
import com.andconsd.utils.AndconsdUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Androsd extends Activity{

	public static Androsd appref;
	String ip = "";

	GridView gl;
	ToggleButton btnService;
	TextView tvIp;
	ImageView ivHelp;
	TextView tvHelp;
	
	//AlertDialog
	Button btnCheck;
	ProgressBar pbCheckIp;
	TextView tvCheckResult;
	EditText etIpEditText;
	
	AlertDialog ad;
	
	String wifiLockString = "Androsd";

	ImageThumbAdapter fa;
	
	WifiManager wifiManager;
	WifiLock wifiLock;

	Thread t;
	
	PopupWindow popupDelete;
	
	public Handler handler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * Init the all apk.
	 */
	private void init() {
		new UiTemplate(this);
		appref = this;
		setContentView(R.layout.main);
		
		//Init the gridview.
		initPicGrid();

		//Init the service controller.
		initController();
	}

	/**
	 * Init the service controller.
	 */
	private void initController() {

		btnService = (ToggleButton) findViewById(R.id.btnService);
		tvIp = (TextView) findViewById(R.id.tvIpAddress);
		ivHelp= (ImageView)findViewById(R.id.ivHelp);
		tvHelp = (TextView)findViewById(R.id.tvHelp);
		tvHelp.setTextColor(Color.BLUE);
		
		
		//Set the service button.
		btnService.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					try {
						wifiManager = (WifiManager) appref.getSystemService(Context.WIFI_SERVICE);
						WifiInfo wifiInfo = wifiManager.getConnectionInfo();
						wifiInfo.getMacAddress();
						if(!wifiManager.isWifiEnabled()){
							Toast.makeText(appref, "wifi not conntected", 2000).show();
						}
						String ip = getLocalIpAddress();
						Constants.service_running = true;
						if (ip != null) {
							t = new RequestListenerThread(
									Constants.DEFAULT_PORT, Constants.ROOT_DIR);
							t.setDaemon(false);

							t.start();

							tvIp.setTextColor(Color.BLACK);
							tvIp.setText(ip + ":" + Constants.DEFAULT_PORT);

						} else {
							Toast.makeText(appref, "network error", 2000)
									.show();
						}
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(appref, e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					if(t!= null){
						t.interrupt();
						stopServerSafty();
					}
					
				}
			}
		});
		
		tvHelp.setOnClickListener(helpOnClickListener);
		ivHelp.setOnClickListener(helpOnClickListener);
	
	}
	
	//the on click listener.
	OnClickListener helpOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			View view = LayoutInflater.from(appref).inflate(R.layout.howtouse, null);
			ad = new AlertDialog.Builder(appref).setView(view).setNegativeButton(R.string.btnClose, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {}
				
			}).create();
			
			ad.show();
		}
	};

	/**
	 * Get the local IP address
	 * @return IP address
	 */
	public String getLocalIpAddress() {
		try {
			
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("TAG", ex.toString());
		}
		return null;
	}

	private void initPicGrid() {
		gl = (GridView) findViewById(R.id.gl);

		fa = new ImageThumbAdapter(Constants.files,gl);

		gl.setAdapter(fa);
		
		gl.setNumColumns(4);

		gl.setPadding(30, 10, 30, 10);

		gl.setVerticalSpacing(30);
		gl.setColumnWidth(110);

		gl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(appref, PicViewer.class);
				intent.putExtra("index", position);
				startActivity(intent);
			}
		});
		
		gl.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(popupDelete!= null && popupDelete.isShowing()){
					popupDelete.dismiss();
				}
			}
		});
		
		gl.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
				
				if(popupDelete != null && popupDelete.isShowing()){
					popupDelete.dismiss();
				}
				
				View deleteView = LayoutInflater.from(appref).inflate(R.layout.popupdelete, null);
				
				popupDelete = new PopupWindow(deleteView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

				ImageView ivDelete = (ImageView) deleteView.findViewById(R.id.ivDelete);
				
				ivDelete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						File file = (File)parent.getAdapter().getItem(position);
						if(AndconsdUtils.deleteFileByName(file.getName())){

							appref.handler.post(new Runnable(){

								@Override
								public void run() {
									appref.notifyDatasetChanged();
								}
								
							});
							Toast.makeText(appref, file.getName() + "  " + appref.getString(R.string.deletesuccess), 2000).show();
						}
					}
				});
				
				popupDelete.showAsDropDown(view, (view.getWidth() / 4) * 3, -view.getHeight());
				return true;
			}
		});
	}
	

	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			stopServerSafty();
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.gaojice.intent.filter.WebService");
		stopService(serviceIntent);
		super.onDestroy();
	}

	public void notifyDatasetChanged(){
		Constants.files.clear();
		File[] filess = new File(Constants.ROOT_DIR).listFiles();
		
		for(File temp : filess){
			Constants.files.add(temp);
		}
		fa.notifyDataSetChanged();
		
	}
	
	private void stopServerSafty(){
		try {
			Socket soc = new Socket("127.0.0.1",
					Constants.DEFAULT_PORT);

			tvIp.setText("");
			t = null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean checkIp(String ip){
		
		String[] ips = ip.split("\\.");
		if(ips.length < 4){
			return false;
		}
		
		for(String ipeach : ips){
			try{
				int ipe = Integer.parseInt(ipeach);
				if(ipe < 0 || ipe > 255){
					return false;
				}
			}catch(Exception e){
				return false;
			}
		}
		return true;
	}
}