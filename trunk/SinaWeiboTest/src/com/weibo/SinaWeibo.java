package com.weibo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.OAuth.OAuthActivity;
import com.weibo.activity.IndexActivity;
import com.weibo.activity.SettingPre;
import com.weibo.daos.DBAdapter;
import com.weibo.pojo.DBColumns;
import com.weibo.pojo.UserImpl;
import com.weibo.utils.Constant;

public class SinaWeibo extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	EditText etName;
	EditText etPasswd;
	Button btnLogin;
	Button btnCancel;

	public static SinaWeibo appref;
	public final static String tag = "TAG";

	private static boolean existBoolean = false;
	// the instance of OAuth

	private DBAdapter dba;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		Constant.spAll = this.getSharedPreferences("Weibo_ranger", MODE_PRIVATE);
		
		getData();
		
		initView();
		
	}

	public void initView() {
		appref = this;

		// init the compenent in the view;
		btnLogin = (Button) appref.findViewById(R.id.btnLogin);
		btnCancel = (Button) appref.findViewById(R.id.btnCancel);

		ImageView userHead = (ImageView)findViewById(R.id.ivUserHead);
		userHead.setImageBitmap(UserImpl.getUserHead());

		btnLogin.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	public void getData() {
		dba = new DBAdapter(this, Constant.dbName, Constant.dbVersion);
		dba.open();
		Cursor cr = dba.query(null, "", "", "", "", "");
		if (cr != null && cr.getCount() > 0) {
			cr.moveToFirst();
			existBoolean = true;
			UserImpl.setUserScreenName(cr.getString(cr.getColumnIndex(DBColumns.ScreenName)));
			UserImpl.setFromSite(cr.getString(cr.getColumnIndex(DBColumns.SITE)));
			UserImpl.setUserHeadUrl(cr.getString(cr.getColumnIndex(DBColumns.USERHEADURL)));
			byte[] inStream = cr.getBlob(cr.getColumnIndex(DBColumns.USERHEADURI));
			UserImpl.setUserHead(BitmapFactory.decodeByteArray(inStream, 0,inStream.length));
			Constant._access = cr.getString(3);
			Constant._accessSecret = cr.getString(4);
			Constant._token = cr.getString(1);
			Constant._tokenSecret = cr.getString(2);
			cr.close();
			dba.close();
		}
	}

	public void onClick(View arg0) {
		if (arg0.getId() == btnLogin.getId()) {
			if (existBoolean) {
				SharedPreferences.Editor editor = Constant.spAll.edit();
				editor.putInt(Constant.ISRUNNING, Constant._NOTRUNNING);
				editor.commit();
				startActivity(new Intent(this, IndexActivity.class));
				finish();
			}

			else {
				Log.v("TAG", "not authorized");
				startActivity(new Intent(this, OAuthActivity.class));
			}
		} else if (arg0.getId() == btnCancel.getId()) {
			System.exit(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Setting");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			this.startActivity(new Intent(this, SettingPre.class));
			break;
		}
		return true;
	}

}