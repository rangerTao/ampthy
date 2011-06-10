package com.weibo.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import weibo4andriod.Paging;
import weibo4andriod.Status;
import weibo4andriod.User;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.UserImpl;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class IndexActivity extends Activity {

	String token;
	String tokenSecret;
	String access;
	String accessSecret;

	ProgressDialog pDialog;
	ListView lvHomeTimeLine;
	ListView lvTopMenu;
	StringBuffer sbAll = new StringBuffer();
	HorizontalScrollView hs;

	public static TextView tvHeaderUserName;

	public static Handler handler = new Handler();
	public static String[] strTopMenus;

	public static IndexActivity appref;

	public static List<User> friends;
	public static List<Status> statuses = new ArrayList<Status>();

	public Weibo4sina weibo = OAuthConstant.getInstance().getWeibo();

	private int page_index = 1;
	private int last_item = 0;

	private int visiableFirstItem = 0;
	private int visiableItemCount = 0;

	HomeTimeLineAdapter htla;
	FriendTask ft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.index_activity);

		lvHomeTimeLine = (ListView) findViewById(R.id.lvHomeTimeLine);
		// initData();
		initHeader();

		appref = this;
		htla = new HomeTimeLineAdapter();
		ft = new FriendTask();
		ft.execute();
		lvHomeTimeLine.setOnScrollListener(new OnScrollListener() {

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				last_item = firstVisibleItem + visibleItemCount;
				visiableFirstItem = firstVisibleItem;
				visiableItemCount = visibleItemCount;
			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (last_item == htla.getCount()
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					page_index += 1;
					ft = new FriendTask();
					ft.execute();
				}
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for (int i = visiableFirstItem; i < visiableFirstItem
							+ visiableItemCount; i++) {

						View viewTemp = (View) htla.getItem(i);

						final Status statusTemp = statuses.get(i);

						final ImageView ivUserHead = (ImageView) viewTemp
								.findViewById(R.id.ivUserHead);
						ImageView ivStatusImage = (ImageView) viewTemp
								.findViewById(R.id.ivThumbail);
						final User user = statusTemp.getUser();
						new Thread() {

							@Override
							public void run() {
								if (Constant.imageMap.containsKey(user.getId()
										+ "") == false) {
									ivUserHead.setImageBitmap(BitmapFactory
											.decodeResource(
													IndexActivity.appref
															.getResources(),
													R.drawable.loading));
									Constant.imageMap.put(user.getId() + "",
											null);
									Bitmap tempBitmap = WeiboUtils
											.getImage(user.getProfileImageURL());
									WeiboUtils.setImage(
											IndexActivity.appref.handler,
											ivUserHead, tempBitmap);
									Constant.imageMap.put(user.getId() + "",
											tempBitmap);
								} else {
									handler.post(new Runnable() {

										public void run() {
											ivUserHead
													.setImageBitmap(Constant.imageMap
															.get(user.getId()
																	+ ""));
										}

									});

								}
							}

						}.start();

					}
				}

			}
		});

		initButtonAction();

		lvHomeTimeLine.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
						Intent intent = new Intent(IndexActivity.appref,MsgDetail.class);
						Bundle bundle = new Bundle();
						bundle.putInt("index", arg2);
						intent.putExtras(bundle);
						IndexActivity.appref.startActivity(intent);
			}
			
		});
	}

	@Override
	protected void onResume() {
		weibo = OAuthConstant.getInstance().getWeibo();
		super.onResume();
	}

	public void initButtonAction() {
		LinearLayout llAtMe = (LinearLayout) findViewById(R.id.llAtMe_TopMenu);
		llAtMe.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.v("TAG", "Start AtMe");
				startActivity(new Intent(appref, AtMeActivity.class));
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
				tvSend.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					public void onClick(View v) {
						ProgressDialog pdSend = null;
						try {
							pdSend = ProgressDialog.show(IndexActivity.appref,
									"Sending", "please wait");
							if (etTwitter.getText().toString().length() > 0) {
								weibo.update(etTwitter.getText().toString());
							}
						} catch (WeiboException e) {
							Toast.makeText(appref, "Updating error", 2000)
									.show();
							e.printStackTrace();
						} finally {
							llEditTwitter.setVisibility(View.GONE);
							pdSend.dismiss();
						}

					}

				});

				TextView tvCancel = (TextView) findViewById(R.id.btnCancel);
				tvCancel.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						etTwitter.setText("");
						llEditTwitter.setVisibility(View.GONE);
					}
				});
			}

		});

		ibtnHeaderRefresh.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				page_index = 1;
				lvHomeTimeLine.removeAllViewsInLayout();
				statuses = new ArrayList<Status>();
				ft = new FriendTask();
				ft.execute();
				htla.notifyDataSetChanged();
			}

		});
	}

	private void getFriends(int page_index)
			throws org.apache.commons.httpclient.util.TimeoutController.TimeoutException, MalformedURLException {

		try {
			weibo.setOAuthConsumer(Constant.CONSUMER_KEY,
					Constant.CONSUMER_SECRET);
			weibo.setToken(Constant._token, Constant._tokenSecret);
			weibo.setOAuthAccessToken(Constant._access, Constant._accessSecret);

			List<Status> temp = weibo.getHomeTimeline(new Paging(page_index));
			for (Status tmpStatus : temp) {
				statuses.add(tmpStatus);
			}
			for(int i=0;i<statuses.size()/2;i++){
				User user = statuses.get(i).getUser();
				if(user.getProfileImageURL() != null && user.getProfileImageURL().toString().startsWith("http")){
					if(Constant.imageMap.get(user.getProfileImageURL().toString())==null){
						Constant.git.pushImageTask(user.getProfileImageURL());
					}
				}
				Status status = statuses.get(i);
				if(status.getThumbnail_pic() !=null && status.getThumbnail_pic().startsWith("http")){
					if(Constant.imageMap.get(status.getThumbnail_pic())==null){
						Constant.git.pushImageTask(new URL(status.getThumbnail_pic()));
					}
				}
			}
			Constant.git.run();

		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Toast.makeText(IndexActivity.appref, "���Ӵ���",2000).show();
		}
	}

	private class FriendTask extends AsyncTask {

		@Override
		protected void onPreExecute() {
			pDialog = ProgressDialog.show(IndexActivity.appref, appref
					.getResources().getString(R.string.progress_title), appref
					.getResources().getString(R.string.progress_content));
			pDialog.setCancelable(true);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pDialog.dismiss();
			if (lvHomeTimeLine.getAdapter() == null) {
				lvHomeTimeLine.setAdapter(htla);
			} else {
				htla.notifyDataSetChanged();
			}

			User user = null;
			try {
				user = weibo.getAuthenticatedUser();
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				getFriends(page_index);
			} catch (org.apache.commons.httpclient.util.TimeoutController.TimeoutException e) {
				Toast.makeText(appref, "time out", 2000).show();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}