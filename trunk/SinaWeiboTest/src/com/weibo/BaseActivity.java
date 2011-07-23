package com.weibo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.weibo.activity.IndexActivity;

public class BaseActivity extends Activity {

	public static ProgressDialog pd;

	public void initProgressDialog() {
		pd = ProgressDialog.show(IndexActivity.appref, this.getResources()
				.getString(R.string.progress_title), this.getResources()
				.getString(R.string.progress_content));
		pd.setCancelable(true);
		pd.show();
	}
	
	public void dismissPD(){
		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
	}

	public Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {

			case 1:
				if (pd != null)
					pd.dismiss();
				break;
			case 2:
				if (pd != null){
					pd.show();
				}else{
					initProgressDialog();
					pd.show();
				}
				break;
			}

		}

	};

	public void showProgressDialog(){
		Message msg = new Message();
		msg.what = 2;
		mHandler.sendMessage(msg);
		pd = null;
	}
}
