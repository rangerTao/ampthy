package com.duole.wizard.setup;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.duole.wizard.setup.utils.SetupUtils;
import com.duole.wizard.setup.widget.OnScrolledListener;
import com.duole.wizard.setup.widget.ScrollLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class RegisterUserActivity extends Activity {

	RegisterUserActivity appref;
	
	ScrollLayout mScrollLayout;

	LayoutInflater mInflater;
	
	Button btnIgnore;
	Button btnNextStep;
	Button btnPreStep;

	View uView;
	View pView;
	View bView;
	View phoneView;
	
	View rebindView;
	
	TextView tvAlert;

	EditText tvEmail_user;
	EditText tvEmail_domain;
	TextView tvEmail_at;

	CheckBox cbShowPassword;
	EditText etPassword;
	EditText etPasswordConfirm;
	TextView tvPassConfirm;

	DatePicker dpBabyBirthday;
	Button btnBirthConfirm;

	EditText etPhoneNum;
	
	TextView tvInfo_user;
	TextView tvInfo_birth;
	TextView tvInfo_phone;
	TextView tvInfo_androidid;
	
	ProgressBar pbRegister;
	
	//Rebind machine.
	EditText etRebindUsernam;
	EditText etRebindDomain;
	EditText etRebindPasswd;
	
	AlertDialog adBindAndroidId;
	
	public final static int SHOW_ALERT = 1;
	public final static int HIDE_ALERT = 2;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_ALERT:
				
				String message = (String) msg.obj;
				
				tvAlert.setText(message);
				
				Message hide = new Message();
				hide.what = HIDE_ALERT;
				sendMessageDelayed(hide, 2000);
				
				break;
				
			case HIDE_ALERT:
				
				tvAlert.setText("");
				
				break;

			}
			
			super.handleMessage(msg);
		}
		
		
		
	};
	
	private void showAlert(String message){
		
		Message msg = new Message();
		msg.what = SHOW_ALERT;
		msg.obj = message;
		mHandler.sendMessage(msg);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appref = this;
		
		String exists = getIntent().getExtras().getString("exists");
		
		mInflater = LayoutInflater.from(getApplicationContext());
		
		setContentView(R.layout.registeruser);
		
		tvAlert = (TextView) findViewById(R.id.tvAlert);
		tvAlert.setTextColor(Color.RED);
		
		mScrollLayout = (ScrollLayout) findViewById(R.id.mScroll);
		
		initControlButtons();
		
		if(exists.equals("1")){
			
			rebindView = mInflater.inflate(R.layout.rebindmachine, null);
			
			mScrollLayout.addView(rebindView);
			
			initRebindView();
			
		}else{

			uView = mInflater.inflate(R.layout.usernameinput, null);
			pView = mInflater.inflate(R.layout.passwordinput, null);
			bView = mInflater.inflate(R.layout.birthdayselect, null);
			phoneView = mInflater.inflate(R.layout.phonenuminput, null);

			mScrollLayout.addView(uView);
			mScrollLayout.addView(pView);
			mScrollLayout.addView(bView);
			mScrollLayout.addView(phoneView);

			initViewCompnent();
			
		}
	}
	
	private void initControlButtons(){
		
		btnIgnore = (Button) findViewById(R.id.btnNextTime);
		btnNextStep = (Button) findViewById(R.id.btnNext);
		btnPreStep = (Button) findViewById(R.id.btnPre);
		
		btnIgnore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final View view = mInflater.inflate(R.layout.confirmexit,
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

										android.os.Process.killProcess(android.os.Process.myPid());
									}

								}).create();
				confirm.show();
			}
		});
		
		btnPreStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mScrollLayout.getCurScreen() > 0){
					mScrollLayout.snapToScreen(mScrollLayout.getCurScreen() - 1);
					
					hideIme();
					
					btnNextStep.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(mScrollLayout.getCurScreen() < mScrollLayout.getChildCount()){
								mScrollLayout.snapToScreen(mScrollLayout.getCurScreen() + 1);
							}
						}
					});
					
				}else{
					Intent intent = new Intent(appref,SetupWizardMainActivity.class);
					startActivity(intent);
					appref.finish();
				}
			}
		});
		
	}

	private void initRebindView(){
		
		tvEmail_at = (TextView) rebindView.findViewById(R.id.rmAt);
		tvEmail_at.setText("@");
		
		etRebindUsernam = (EditText) rebindView.findViewById(R.id.rmUsername);
		etRebindDomain = (EditText) rebindView.findViewById(R.id.rmEmailDomain);
		etRebindPasswd = (EditText) rebindView.findViewById(R.id.etRebindPassword);
		
		etRebindUsernam.requestFocus();
		
//		btnNextStep.setText(R.string.btn_bind);
		btnNextStep.setBackgroundResource(R.drawable.next);
		btnNextStep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideIme();

				if (valueRebindCheck()) {

					View bindView = mInflater.inflate(R.layout.bind_android_id,
							null);
					TextView tvResult = (TextView) bindView
							.findViewById(R.id.tvResult);
					adBindAndroidId = new AlertDialog.Builder(appref)
							.setTitle(R.string.machine_binding)
							.setView(bindView)
							.setNegativeButton(R.string.btnPositive, null)
							.create();

					adBindAndroidId.show();

					try {
						String result = SetupUtils.dopost(
								getApplicationContext(),
								"http://wvw.duoleyuan.com/e/enews/",
								etRebindUsernam.getText().toString() + "@"
										+ etRebindDomain.getText().toString(),
								etRebindPasswd.getText().toString());

						JSONObject jsonObject = new JSONObject(result);

						String status = jsonObject.getString("status");
						String stastr = jsonObject.getString("stastr");

						if (status.equals("1")) {
							adBindAndroidId.dismiss();
							new AlertDialog.Builder(appref)
									.setTitle("")
									.setMessage(R.string.bind_successful)
									.setNegativeButton(
											R.string.btnPositive,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													try {
														ContentResolver cr = appref
																.getContentResolver();
														Uri config = Uri
																.parse("content://com.duole.provider/config");
														ContentValues cv = new ContentValues();
														cv.put("name", "setup");
														cv.put("value", "1");
														cr.insert(config, cv);
													} catch (Exception e) {
														e.printStackTrace();
													}

													android.os.Process
															.killProcess(android.os.Process
																	.myPid());
												}
											}).create().show();
						}
						if (status.equals("0")) {
							adBindAndroidId.dismiss();
							new AlertDialog.Builder(appref)
									.setTitle("")
									.setMessage(stastr + getString(R.string.contact_cc) + getString(R.string.device_id) + ":" + SetupUtils.getAndroidId(appref))
									.setNegativeButton(
											R.string.btnPositive,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
												}
											}).create().show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						tvAlert.setText(R.string.bind_failed);
						adBindAndroidId.dismiss();
					}

				}
			}
		});
		
	}
	
	private void initViewCompnent() {

		tvEmail_at = (TextView) uView.findViewById(R.id.tvAt);
		tvEmail_at.setText("@");
		
		tvEmail_user = (EditText) uView.findViewById(R.id.etEmailUserName);
		tvEmail_user.setOnEditorActionListener(emailOnEditorActionListener);
//		tvEmail_user.setOnEditorActionListener(enterActionListener);
		tvEmail_domain = (EditText) uView.findViewById(R.id.etEmailDomain);
		tvEmail_domain.setOnEditorActionListener(emailOnEditorActionListener);

		cbShowPassword = (CheckBox) pView.findViewById(R.id.cbPasswordShow);
		tvPassConfirm = (TextView) pView.findViewById(R.id.tvPassConfirm);
		
		cbShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					etPasswordConfirm.setEnabled(false);
					etPasswordConfirm.setVisibility(View.INVISIBLE);
					etPasswordConfirm.setText("");
					tvPassConfirm.setVisibility(View.INVISIBLE);
				}else{
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
					etPasswordConfirm.setEnabled(true);
					etPasswordConfirm.setVisibility(View.VISIBLE);
					tvPassConfirm.setVisibility(View.VISIBLE);
				}
			}
		});
		
		etPassword = (EditText) pView.findViewById(R.id.etPassword);
		
		etPassword.setOnEditorActionListener(passwordOnEditorActionListener);
		etPasswordConfirm = (EditText) pView
				.findViewById(R.id.etPasswordRepeat);
		etPasswordConfirm.setOnEditorActionListener(passwordOnEditorActionListener);
		

		dpBabyBirthday = (DatePicker) bView.findViewById(R.id.dpBirthday);

		etPhoneNum = (EditText) phoneView.findViewById(R.id.etPhoneNumber);
		etPhoneNum.setOnEditorActionListener(enterActionListener);
		
		btnNextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mScrollLayout.getCurScreen() < mScrollLayout.getChildCount()){
					mScrollLayout.snapToScreen(mScrollLayout.getCurScreen() + 1);
				}
				hideIme();
			}
		});
		
		mScrollLayout.setOnScrolledListener(new OnScrolledListener() {
			
			@Override
			public void scrolled(int last, int mCurScreen) {

				if(mCurScreen == mScrollLayout.getChildCount() - 1){
					btnNextStep.setBackgroundResource(R.drawable.register);
					btnNextStep.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (valueCheck()) {
								
								hideIme();
								
								View infoDetailView = mInflater.inflate(
										R.layout.infoandregister, null);
								
								tvInfo_user = (TextView) infoDetailView.findViewById(R.id.tvUserName);
								tvInfo_birth = (TextView) infoDetailView.findViewById(R.id.tvBirth);
								tvInfo_phone = (TextView) infoDetailView.findViewById(R.id.tvMobilePhone);
								tvInfo_androidid = (TextView) infoDetailView.findViewById(R.id.tvAndroidId);
								
								pbRegister = (ProgressBar) infoDetailView.findViewById(R.id.pbRegister);
								
								tvInfo_user.setText(tvEmail_user.getText().toString() + "@" + tvEmail_domain.getText().toString());
								tvInfo_birth.setText(dpBabyBirthday.getYear() + "-" + dpBabyBirthday.getMonth() + "-" + dpBabyBirthday.getDayOfMonth());
								tvInfo_phone.setText(etPhoneNum.getText().toString());
								tvInfo_androidid.setText(getString(R.string.device_id) + ":" + SetupUtils.getAndroidId(appref));
								
								new AlertDialog.Builder(appref)
										.setTitle(R.string.userInfo)
										.setView(infoDetailView)
										.setPositiveButton(
												R.string.btnRegister,
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														tvInfo_birth.setVisibility(View.INVISIBLE);
														tvInfo_phone.setVisibility(View.INVISIBLE);
														tvInfo_user.setVisibility(View.INVISIBLE);
														tvInfo_androidid.setVisibility(View.INVISIBLE);
														
														pbRegister.setVisibility(View.VISIBLE);
														
														String result = SetupUtils.dopost(appref,"http://wvw.duoleyuan.com/e/enews/indexM.php", tvInfo_user.getText().toString(), etPassword.getText().toString(), tvInfo_birth.getText().toString(), tvInfo_phone.getText().toString());
														
														try {
															JSONObject jsonObject = new JSONObject(result);
															
															String status = jsonObject.getString("status");
															String stastr = jsonObject.getString("stastr");
															
															if(status.equals("1")){
																new AlertDialog.Builder(appref).setTitle("").setMessage(stastr).setNegativeButton(R.string.btnPositive, new DialogInterface.OnClickListener() {
																	
																	@Override
																	public void onClick(DialogInterface dialog, int which) {
																		try{
																			ContentResolver cr = appref
																					.getContentResolver();
																			Uri config = Uri
																					.parse("content://com.duole.provider/config");
																			ContentValues cv = new ContentValues();
																			cv.put("name", "setup");
																			cv.put("value", "1");
																			cr.insert(config, cv);
																		}catch (Exception e) {
																			e.printStackTrace();
																		}
																		
																		
																		android.os.Process.killProcess(android.os.Process.myPid());
																	}
																}).create().show();
															}
															if(status.equals("0")){
																new AlertDialog.Builder(appref)
																		.setTitle(R.string.register_fail)
																		.setMessage(stastr + getString(R.string.contact_cc) + "\n" + getText(R.string.device_id) + ":" + SetupUtils.getAndroidId(appref))
																		.setNegativeButton(R.string.btnPositive,
																				new DialogInterface.OnClickListener() {

																					@Override
																					public void onClick(
																							DialogInterface dialog,
																							int which) {
																					}
																				})
																		.create()
																		.show();
															}
														} catch (JSONException e) {
															new AlertDialog.Builder(appref)
															.setTitle(R.string.register_fail)
															.setMessage(R.string.connect_error)
															.setNegativeButton(R.string.btnPositive,
																	new DialogInterface.OnClickListener() {

																		@Override
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																		}
																	})
															.create()
															.show();
														}
														
													}
												})
										.setNegativeButton(R.string.btnNegative, null)
										.create().show();
							}
						}
					});
				}else{
//					hideIme();
//					btnNextStep.setText(getString(R.string.btnNext));
					btnNextStep.setBackgroundResource(R.drawable.next);
					btnNextStep.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(mScrollLayout.getCurScreen() < mScrollLayout.getChildCount()){
								mScrollLayout.snapToScreen(mScrollLayout.getCurScreen() + 1);
							}
						}
					});
				}
			}
		});
		
	}
	
	private boolean valueCheck(){
		if(tvEmail_user.getText().toString().equals("") || tvEmail_domain.getText().toString().equals("") || !tvEmail_domain.getText().toString().contains(".")){
			mScrollLayout.snapToScreen(0);
			showAlert(getString(R.string.email_incorrect));
			return false;
		}
		
		if(etPassword.getText().toString().equals("") || etPassword.getText().toString().length() < 6){
			mScrollLayout.snapToScreen(1);
			showAlert(getString(R.string.passwrod_incorrect));
			return false;
		}
		
		if(!cbShowPassword.isChecked()){
			if(!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())){
				mScrollLayout.snapToScreen(1);
				showAlert(getString(R.string.passwrod_incorrect));
				return false;
			}
		}
		
		int yearSelected = dpBabyBirthday.getYear();
		int monthSelected = dpBabyBirthday.getMonth();
		int daySelected = dpBabyBirthday.getDayOfMonth();
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		if (yearSelected > year
				|| (yearSelected >= year && monthSelected > month)
				|| (yearSelected >= year && monthSelected >= month && daySelected > day)) {
			showAlert(getString(R.string.birthday_incorrect));
			mScrollLayout.snapToScreen(2);
			return false;
		} 
		
		if(etPhoneNum.getText().toString().equals("")){
			showAlert(getString(R.string.phone_null));
			return false;
		}
		
		if(!etPhoneNum.getText().toString().startsWith("1") || etPhoneNum.getText().toString().length() != 11){
			showAlert(getString(R.string.phone_incorrect));
			return false;
		}
		
		return true;
		
	}
	
	private boolean valueRebindCheck(){
		
		if(etRebindUsernam.getText().toString().equals("") || etRebindDomain.getText().toString().equals("") || !etRebindDomain.getText().toString().contains(".")){
			showAlert(getString(R.string.email_incorrect));
			return false;
		}
		
		if(etRebindPasswd.getText().toString().equals("")){
			showAlert(getString(R.string.password_wrong));
			return false;
		}
		
		return true;
	}
	
	OnEditorActionListener passwordOnEditorActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			
			if(etPassword.hasFocus()){
				if(cbShowPassword.isChecked()){
					if(etPassword.getText().toString().length() < 6){
						showAlert(getString(R.string.password_short));
						etPassword.setText("");
					}else{
						hideIme();
						mScrollLayout.snapToScreen(mScrollLayout.getCurScreen() + 1);
					}
					
				}else{
					
					if(etPassword.getText().toString().length() < 6){
						showAlert(getString(R.string.password_short));
						etPassword.setText("");
					}else {
						etPasswordConfirm.requestFocus();
						return true;
					}
					
				}
				
			}
			
			if(etPasswordConfirm.hasFocus()){
				if(!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())){
					showAlert(getString(R.string.passwrod_incorrect));
					etPassword.requestFocus();
					etPassword.setText("");
					etPasswordConfirm.setText("");
				}else{
					hideIme();
					mScrollLayout.snapToScreen(mScrollLayout.getCurScreen() + 1);
				}
			}
			
			
			
			return true;
		}
	};
	
	private void hideIme(){
		InputMethodManager imm = (InputMethodManager) appref.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(appref.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	OnEditorActionListener emailOnEditorActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			
			if(tvEmail_domain.hasFocus()){
				if(!tvEmail_user.getText().toString().equals("") && !tvEmail_domain.getText().toString().equals("")){
					InputMethodManager imm = (InputMethodManager) appref.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(appref.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					mScrollLayout.snapToScreen(mScrollLayout.getCurScreen() + 1);
				}else{
					if(tvEmail_domain.getText().toString().equals("") ){
						tvEmail_domain.requestFocus();
					}
					if( !tvEmail_domain.getText().toString().contains(".")){
						tvEmail_domain.requestFocus();
					}
					if(tvEmail_user.getText().toString().equals("")){
						tvEmail_user.requestFocus();
					}
					showAlert(getString(R.string.email_incorrect));
				}
			}
			if(tvEmail_user.hasFocus()){
				tvEmail_domain.requestFocus();
			}
			
			return true;
		}
	};
	
	OnEditorActionListener enterActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			InputMethodManager imm = (InputMethodManager) appref.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(appref.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			return true;
		}
	};
	
}
