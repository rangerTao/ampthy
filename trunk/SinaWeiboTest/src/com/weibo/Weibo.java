package com.weibo;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.weibo.OAuth.OAuthActivity;
import com.weibo.activity.IndexActivity;
import com.weibo.activity.SettingPre;
import com.weibo.daos.DBAdapter;
import com.weibo.utils.Contants;

public class Weibo extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	EditText etName;
	EditText etPasswd;
	Button btnLogin;
	Button btnCancel;

	public static Weibo appref;
	public final static String tag = "TAG";
	// the instance of OAuth

	CommonsHttpOAuthConsumer httpOauthConsumer;
	OAuthProvider httpOauthprovider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		initView();
	}

	public void initView() {
		appref = this;

		// init the compenent in the view;
//		etName = (EditText) appref.findViewById(R.id.etUsername);
//		etPasswd = (EditText) appref.findViewById(R.id.etPasswd);

		btnLogin = (Button) appref.findViewById(R.id.btnLogin);
		btnCancel = (Button) appref.findViewById(R.id.btnCancel);

		btnLogin.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	public void onClick(View arg0) {
		DBAdapter dba = new DBAdapter(this, Contants.dbName, Contants.dbVersion);
		dba.open();
		if (arg0.getId() == btnLogin.getId()) {
			Cursor cr = dba.query(null, "", "", "", "", "");
			Log.v("TAG", cr.getCount() + "");
			if (cr != null && cr.getCount() > 0) {
				Log.v("TAG", "authorized");
				cr.close();
				startActivity(new Intent(this, IndexActivity.class));
				dba.close();
				finish();
			} else {
				cr.close();
				dba.close();
				Log.v("TAG", "not authorized");
				startActivity(new Intent(this, OAuthActivity.class));
			}
		} else if (arg0.getId() == btnCancel.getId()) {
			// System.exit(0);
			if (dba.deleteData(null, null)) {
				Toast.makeText(appref, "delete ok", 2000);
			}
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