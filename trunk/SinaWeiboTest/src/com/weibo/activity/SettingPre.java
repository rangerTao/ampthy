package com.weibo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weibo.R;
import com.weibo.Weibo;

public class SettingPre extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener, OnClickListener {

	CheckBoxPreference cbp;
	Preference btnPre;
	
	Dialog passwdDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.xml.setting_pre);

		initView();
		cbp.setOnPreferenceChangeListener(this);
		btnPre.setOnPreferenceClickListener(this);
	}

	public void initView() {
		cbp = (CheckBoxPreference) findPreference("checkbox_pre");
		cbp.setChecked(false);
		btnPre = (Preference) findPreference("preference_pre");

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		btnPre.setEnabled(!cbp.isChecked());
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.passed_dialog, null);
		passwdDialog = new AlertDialog.Builder(this).setTitle(R.string.passwd_set_title)
				.setCancelable(true).setView(view).show();
		Button confirm = (Button) view.findViewById(R.id.btnConfirm);
		Button cancel = (Button)view.findViewById(R.id.btnCancel);
		final EditText etPasswd = (EditText) view.findViewById(R.id.etPasswd);
		
		confirm.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.v("TAG", etPasswd.getText().toString());
				passwdDialog.cancel();
			}
			
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etPasswd.setText("");
				Toast.makeText(Weibo.appref, "Did not set the Passwd", 3000);
				cbp.setChecked(false);
				btnPre.setEnabled(false);
				passwdDialog.cancel();
				
			}
		});
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case Dialog.BUTTON_POSITIVE:
			break;
		case Dialog.BUTTON_NEUTRAL:
			passwdDialog.cancel();
			break;
		}

	}

}
