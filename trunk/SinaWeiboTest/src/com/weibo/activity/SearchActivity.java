package com.weibo.activity;

import java.util.List;

import weibo4android.Query;
import weibo4android.SearchResult;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.weibo.BaseActivity;
import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.SearchResultAdapter;
import com.weibo.pojo.adapter.SearchUserResultAdapter;
import com.weibo.utils.Constant;

public class SearchActivity extends BaseActivity implements OnItemClickListener{

	Spinner spSearchType;
	Button btnSearch;
	EditText etSearchContent;
	
	public List<User> lsu;
	public static List<SearchResult> lsr;
	
	public int resultType = 0;
	
	ListView lvSearch;
	
	public static SearchActivity appref;
	
	Weibo weibo = OAuthConstant.getInstance().getWeibo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		appref = this;
		setContentView(R.layout.searchall);

		lvSearch = (ListView) findViewById(R.id.lvSearchResult);
		spSearchType = (Spinner)findViewById(R.id.spChoose);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		etSearchContent = (EditText)findViewById(R.id.etSearch);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(SearchActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				initProgressDialog();
				SearchTask st = new SearchTask();
				st.execute();
				
			}
		});
		
		lvSearch.setOnItemClickListener(this);
	}
	
	
	private class SearchTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... arg0) {
			Query query = new Query();
			query.setQ(etSearchContent.getText().toString());
			switch (spSearchType.getSelectedItemPosition()) {
			case 0:
				
				try {
					resultType = 0;
					lsr = weibo.search(query);
				} catch (WeiboException e) {
					appref.toastNetError();
				}
				break;
			case 1:
				resultType = 1;
				try {
					lsu = weibo.searchUser(query);
				} catch (WeiboException e) {
					Log.v("TAG", e.getMessage());
					appref.toastNetError();
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
			if(0== resultType && lsr != null){
				SearchResultAdapter sra = new SearchResultAdapter(lsr);
				lvSearch.setAdapter(sra);
			}
			
			if(1== resultType && lsu != null){
				lvSearch.removeAllViewsInLayout();
				SearchUserResultAdapter sura = new SearchUserResultAdapter(lsu);
				lvSearch.setAdapter(sura);
				sura.notifyDataSetChanged();
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		
	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		switch(resultType){
		case 0:
			showProgressDialog();
			Intent intent = new Intent(SearchActivity.appref,MsgDetail.class);
			try {
				Constant.tmpStatus = weibo.showStatus(lsr.get(position).getId());
				this.dismissPD();
				IndexActivity.appref.startActivity(intent);
			} catch (WeiboException e) {
				this.toastNetError();
			}

			break;
		case 1:
			Intent intent1 = new Intent(SearchActivity.appref,HomePageActivity.class);
			Constant.tmpStatus = null;
			Constant.tmpUser = lsu.get(position);
			appref.startActivity(intent1);
			break;
		}
		
	}
	
}
