package com.weibo.activity;

import java.util.ArrayList;
import java.util.List;

import weibo4andriod.Paging;
import weibo4andriod.Status;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.UserImpl;
import com.weibo.pojo.adapter.AtMeAdapter;
import com.weibo.utils.Constant;

public class AtMeActivity extends Activity {

	Weibo4sina weibo;
	List<Status> statuses = new ArrayList<Status>();
	
	private ListView lvAtMe;
	private TextView tvHeaderUserName;
	
	public static AtMeActivity appref;
	
	public static ArrayList<Status> atMeList = new ArrayList<Status>();
	public AtMeAdapter atMeAdapter;
	public Handler handler = new Handler();
	private int page_index = 1;
	private int last_item = 0;
	public AtMeTask at;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		appref = this;

		setContentView(R.layout.index_activity);
		
		lvAtMe = (ListView)findViewById(R.id.lvHomeTimeLine);

		lvAtMe.setOnScrollListener(new OnScrollListener(){

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				last_item = firstVisibleItem + visibleItemCount;
				
			}

			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if(last_item == atMeAdapter.getCount() && arg1 == OnScrollListener.SCROLL_STATE_IDLE){
					Log.v("TAG", "Get next page");
					page_index += 1;
					at = new AtMeTask();
					at.execute();
				}
			}
			
		});
		initHeader();
		
		initButtonAction();
		
		weibo = OAuthConstant.getInstance().getWeibo();
		
		if(!(atMeList.size()>0)){
			at = new AtMeTask();
			at.execute();
		}else{
			Log.v("TAG", "item exists");
			lvAtMe.setAdapter(this.atMeAdapter);
		}
		
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}



	@Override
	protected void onPause() {
		super.onResume();
	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}



	@Override
	protected void onResume() {
		weibo = OAuthConstant.getInstance().getWeibo();
		super.onResume();
	}


	public void initButtonAction(){
		LinearLayout llAtMe = (LinearLayout)findViewById(R.id.llAtMe_TopMenu);
		llAtMe.setOnClickListener(new OnClickListener(){


			public void onClick(View v) {
				Log.v("TAG", "Start AtMe");
				startActivity(new Intent(appref,AtMeActivity.class));
			}
		});
	}
	
	public void initHeader() {

		ImageView ibtnHeaderWrite = (ImageView) findViewById(R.id.ibtnHeaderWrite);
		ImageView ibtnHeaderRefresh = (ImageView) findViewById(R.id.ibtnHeaderRefresh);
		tvHeaderUserName = (TextView) findViewById(R.id.tvHeaderUserName);
		
		tvHeaderUserName.setText(UserImpl.getUserScreenName());
		
		ibtnHeaderWrite.setImageBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.writer));
		ibtnHeaderRefresh.setImageBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.refresh_btn));

		ibtnHeaderWrite.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				final LinearLayout llEditTwitter = (LinearLayout) findViewById(R.id.llEditTwitter);
				llEditTwitter.setVisibility(View.VISIBLE);

				final EditText etTwitter = (EditText) findViewById(R.id.etBoradcast);

				TextView tvSend = (TextView) findViewById(R.id.btnSend);
				tvSend.setOnClickListener(new OnClickListener(){

					@SuppressWarnings("deprecation")
					public void onClick(View v) {
						ProgressDialog pdSend = null;
						try {
							pdSend = ProgressDialog.show(IndexActivity.appref, "Sending", "please wait");
							if(etTwitter.getText().toString().length()>0){
								weibo.update(etTwitter.getText().toString());
							}
						} catch (WeiboException e) {
							Toast.makeText(appref, "Updating error", 2000).show();
							e.printStackTrace();
						} finally{
							llEditTwitter.setVisibility(View.GONE);
							pdSend.dismiss();
						}
						
					}
					
				});

				TextView tvCancel = (TextView) findViewById(R.id.btnCancel);
				tvCancel.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						etTwitter.setText("");
						llEditTwitter.setVisibility(View.GONE);
					}
				});
			}

		});
		
		ibtnHeaderRefresh.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				page_index = 1;
				lvAtMe.removeAllViewsInLayout();
				statuses = new ArrayList<Status>();
				
				at = new AtMeTask();
				at.execute();
				atMeAdapter.notifyDataSetChanged();
			}
			
		});
	}

	private void getAtMe(int page_index) {

		try {
//			weibo.setOAuthConsumer(Constant.CONSUMER_KEY,
//					Constant.CONSUMER_SECRET);
//			weibo.setToken(Constant._token, Constant._tokenSecret);
//			weibo.setOAuthAccessToken(Constant._access, Constant._accessSecret);

			statuses = weibo.getMentions(new Paging(page_index));
			for (Status temp : statuses) {
				atMeList.add(temp);
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}

	private class AtMeTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			getAtMe(page_index);
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			
			if(lvAtMe.getAdapter() == null){
				lvAtMe.setAdapter(atMeAdapter);
			}else{
				atMeAdapter.notifyDataSetChanged();
			}
			appref.setProgressBarIndeterminate(false);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			appref.setProgressBarIndeterminate(true);
			atMeAdapter = new AtMeAdapter();
			super.onPreExecute();
		}
		
		
		
	}
}
