package com.duole.activity;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.duole.R;
import com.duole.utils.Constants;
import com.duole.utils.DuoleNetUtils;
import com.duole.utils.DuoleUtils;

public class SystemConfigActivity extends PreferenceActivity{

	boolean isGetted = false;
	
	Preference preID;
	Preference preUserName;
	Preference preBabyName;
	Preference preBirthday;
	Preference preSex;
	Preference preGettingUserInfo;
	PreferenceCategory pcUserInfo;
	
	static SystemConfigActivity appref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		appref = this;
		
		this.addPreferencesFromResource(R.xml.systemconfig);
		
		pcUserInfo = (PreferenceCategory) findPreference(Constants.Pre_Pc_UserInfo);
		preGettingUserInfo = this.findPreference(Constants.Pre_GettingUserInfo);
		preID = this.findPreference(Constants.Pre_deviceid);
		preID.setSummary(DuoleUtils.getAndroidId());
		
		getUserInfo();
	}
	
	
	
	@Override
	protected void onResume() {
		getUserInfo();
		super.onResume();
	}



	public void getUserInfo(){
		if(!isGetted){
			if(DuoleNetUtils.isNetworkAvailable(appref)){
				preGettingUserInfo.setKey(Constants.Pre_GettingUserInfo);
				preGettingUserInfo.setTitle(appref.getString(R.string.getting_user_info));
				GetUserInfoTask guit = new GetUserInfoTask();
				guit.execute();
			}else{
				preGettingUserInfo.setTitle(getString(R.string.network_unavailable));
				preGettingUserInfo.setKey(Constants.Pre_network);
			}
		}
		
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
			
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		Intent intent = null;
		if(preference.getKey().equals(Constants.Pre_network)){
			intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		}
		if(preference.getKey().equals(Constants.Pre_volume)){
			intent = new Intent(android.provider.Settings.ACTION_SOUND_SETTINGS);
		}
		if(preference.getKey().equals(Constants.Pre_bright)){
			intent = new Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS);
		}
		
		if(preference.getKey().equals(Constants.Pre_Register)){
			
		}
		
		if(preference.getKey().equals(Constants.Pre_Security_ChangePasswd)){
			intent = new Intent(appref,PasswordActivity.class);
			intent.putExtra("type","1");
		}
		
		if(preference.getKey().equals(Constants.Pre_Security_Exit)){
			android.os.Process.killProcess(android.os.Process.myUid());
		}
		
		if(preference.getKey().equals(Constants.Pre_CheckUpdate)){
			intent = new Intent(appref,CheckUpdateActivity.class);
		}
		
		if(intent != null){
			startActivity(intent);
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
	
	class GetUserInfoTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... arg0) {
			String url = "http://www.duoleyuan.com/e/member/child/ancJinfo.php?cc=" + DuoleUtils.getAndroidId();
			
			String result = DuoleNetUtils.connect(url);
			if(!result.equals("")){
				try {
					JSONObject json = new JSONObject(result);
					
					String username = json.getString("username");
					String babyname = json.getString("truename");
					String birthday = json.getString("birthd");
					String sex = json.getString("sex");
					String userid = json.getString("userid");
					
					if(!userid.equals("null")){
						Preference preUserName = new Preference(appref);
						Preference preBabyName = new Preference(appref);
						Preference preBirthday = new Preference(appref);
						Preference preSex = new Preference(appref);
						preUserName.setEnabled(false);
						preBabyName.setEnabled(false);
						preBirthday.setEnabled(false);
						preSex.setEnabled(false);
						preUserName.setSelectable(false);
						preBabyName.setSelectable(false);
						preBirthday.setSelectable(false);
						preSex.setSelectable(false);
						preUserName.setTitle(R.string.username);
						preUserName.setSummary(username);
						preBabyName.setTitle(R.string.babyname);
						preBabyName.setSummary(babyname);
						preBirthday.setTitle(R.string.birthday);
						preBirthday.setSummary(birthday);
						preSex.setTitle(R.string.sex);
						if(sex.equals("0")){
							sex = appref.getString(R.string.sex_male);
						}else if(sex.equals("1")){
							sex = appref.getString(R.string.sex_female);
						}else{
							sex = appref.getString(R.string.sex_unborn);
						}
						preSex.setSummary(sex);
						
						pcUserInfo.removeAll();
						
						if(appref.findPreference(Constants.Pre_GettingUserInfo) != null){
							pcUserInfo.removePreference(appref.findPreference(Constants.Pre_GettingUserInfo));
						}
						pcUserInfo.addPreference(preID);
						pcUserInfo.addPreference(preUserName);
						pcUserInfo.addPreference(preBabyName);
						pcUserInfo.addPreference(preBirthday);
						pcUserInfo.addPreference(preSex);
						
						appref.isGetted = true;
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				if(pcUserInfo != null){
					Preference getuserinfo = appref.findPreference(Constants.Pre_GettingUserInfo);
					if(getuserinfo != null){
						pcUserInfo.removePreference(getuserinfo);
					}
					
					
					Preference preRegister = new Preference(appref);
					preRegister.setKey(Constants.Pre_Register);
					preRegister.setTitle(appref.getString(R.string.device_active));
					preRegister.setSummary(appref.getString(R.string.register_device));
				}
				
			}
			return null;
		}
		
	}
	
	
	
}
