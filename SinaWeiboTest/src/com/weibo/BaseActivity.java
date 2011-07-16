package com.weibo;

import com.weibo.activity.IndexActivity;

import android.app.Activity;
import android.app.ProgressDialog;

public class BaseActivity extends Activity{

	public static ProgressDialog pd ;
	
	public void showProgressDialog(){
		if(pd  == null){
			pd = ProgressDialog.show(IndexActivity.appref, this
					.getResources().getString(R.string.progress_title), this
					.getResources().getString(R.string.progress_content));
			pd.setCancelable(true);
		}else{
			pd.show();
		}
	}
	
	public void dismissProgressDialog(){
		if(pd != null || pd.isShowing()){
			pd.dismiss();
		}
	}
}
