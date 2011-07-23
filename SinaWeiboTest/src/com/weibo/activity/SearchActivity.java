package com.weibo.activity;

import weibo4android.Query;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.weibo.BaseActivity;
import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.utils.WeiboUtils;

public class SearchActivity extends BaseActivity{

	Spinner spSearchType;
	Button btnSearch;
	EditText etSearchContent;
	Weibo weibo = OAuthConstant.getInstance().getWeibo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchall);

		spSearchType = (Spinner)findViewById(R.id.spChoose);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		etSearchContent = (EditText)findViewById(R.id.etSearch);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(SearchActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				
				SearchTask st = new SearchTask();
				st.execute();
				
			}
		});
	}
	
	
	private class SearchTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... arg0) {
			Query query = new Query();
			query.setQ(etSearchContent.getText().toString());
			switch (spSearchType.getSelectedItemPosition()) {
			case 0:
				
				try {
					Log.v("TAG", weibo.search(query).get(0).toString());
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				
				try {
					Log.v("TAG", weibo.searchUser(query).get(0).toString());
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				
				try {
					Log.v("TAG", weibo.statussearch(query).get(0).toString());
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			dismissPD();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			initProgressDialog();
			super.onPreExecute();
		}
		
		
	}

	
}
