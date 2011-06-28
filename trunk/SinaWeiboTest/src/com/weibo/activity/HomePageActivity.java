package com.weibo.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class HomePageActivity extends Activity implements OnScrollListener{

	HomePageActivity appref;
	//The friend
	static User user;
	
	//listview
	ListView lvFriendHome;
	
	ImageView ivFriendHeader;
	TextView tvFriendName;
	TextView tvLocation;
	TextView tvFriendStatus;
	TextView tvFollowerCount;
	TextView tvFansCount;
	TextView tvIsFocused;
	TextView tvLoading;
	
	int page_index = 1;
	
	ArrayList<Status> statuss = new ArrayList<Status>();
	Status status;
	
	Weibo weibo = OAuthConstant.getInstance().getWeibo();
	
	View view;
	
	HomeTimeLineAdapter htla;
	FriendStatusTask fst;
	
	//Scroll event
	int last_item = 0;
	int visiableFirstItem = 0;
	int visiableItemCount = 0;
	ProgressDialog pDialog;
	
	Handler handler = new Handler();
	
	public static void setUser(User friend){
		user = friend;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		appref = this;
		Bundle index = this.getIntent().getExtras();
		int detailIndex = index.getInt("index");
		user = IndexActivity.statuses.get(detailIndex).getUser();
		setContentView(R.layout.friend_activity);
		
		initHeader();
		
		lvFriendHome = (ListView)findViewById(R.id.lvFriendListView);
		
		lvFriendHome.addHeaderView(view);
		lvFriendHome.setAdapter(null);
		lvFriendHome.setOnScrollListener(this);
		fst = new FriendStatusTask();
		fst.execute();
	}
	
	public void initHeader(){
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.friend_header, null);
		ivFriendHeader = (ImageView) view.findViewById(R.id.ivFriendHeader);
		tvFriendName = (TextView) view.findViewById(R.id.tvFriendName);
		tvLocation = (TextView) view.findViewById(R.id.tvLocation);
		tvFriendStatus = (TextView) view.findViewById(R.id.tvFriendStatus);
		tvFollowerCount = (TextView)view.findViewById(R.id.tvFollwerCount);
		tvFansCount = (TextView)view.findViewById(R.id.tvFansCount);
		tvIsFocused = (TextView)view.findViewById(R.id.tvFocused);
		tvLoading = (TextView)view.findViewById(R.id.tvLoading);
		
		if (Constant.imageMap.containsKey(user.getProfileImageURL().toString()) == false) {
			Constant.sit.pushImageTask(user.getProfileImageURL(), ivFriendHeader);
		} else {
			ivFriendHeader.setImageBitmap(Constant.imageMap.get(user.getProfileImageURL().toString() + ""));
		}
		
		tvFriendName.setText(user.getScreenName());
		tvLocation.setText(user.getLocation());
		if(user.getDescription() != ""){
			tvFriendStatus.setText(user.getDescription());
			tvFriendStatus.setVisibility(View.VISIBLE);
		}
		
		tvFollowerCount.setText("¹Ø×¢:" + user.getFavouritesCount());
		tvFansCount.setText("·ÛË¿:" + user.getFollowersCount());
		
		if(user.isStatusTruncated()){
			tvIsFocused.setText("ÒÑ¹Ø×¢");
		}else{
			tvIsFocused.setText("Î´¹Ø×¢");
		}
		
		tvIsFocused.setTextColor(Color.GRAY);
		

	}
	
	private void getFriendStatus() throws MalformedURLException{
		try {
			weibo.setOAuthConsumer(Constant.CONSUMER_KEY,
					Constant.CONSUMER_SECRET);
			weibo.setToken(Constant._token, Constant._tokenSecret);
			weibo.setOAuthAccessToken(Constant._access, Constant._accessSecret);


			if(!Constant.isRunning){
				List<Status> temp = weibo.getUserTimeline(user.getId() +"", new Paging(page_index));

				for (Status tmpStatus : temp) {
					statuss.add(tmpStatus);
				}
				
			}
			for(int i=0;i<statuss.size()/2;i++){
				User user = statuss.get(i).getUser();
				if(user.getProfileImageURL() != null && user.getProfileImageURL().toString().startsWith("http")){
					if(Constant.imageMap.get(user.getProfileImageURL().toString())==null){
						Constant.git.pushImageTask(user.getProfileImageURL());
					}
				}
				Status status = statuss.get(i);
				if(status.getThumbnail_pic() !=null && status.getThumbnail_pic().startsWith("http")){
					if(Constant.imageMap.get(status.getThumbnail_pic())==null){
						Constant.git.pushImageTask(new URL(status.getThumbnail_pic()));
					}
				}
			}
			Constant.git.run();
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "ï¿½ï¿½ï¿½Ó´ï¿½ï¿½ï¿½",2000).show();
		}
	}
	
	class FriendStatusTask extends AsyncTask{

		@Override
		protected void onPreExecute() {
			tvLoading.setText(R.string.friend_status_loading);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			
			IndexActivity.handler.post(new Runnable(){
				public void run() {
					if (lvFriendHome.getChildCount() == 1) {
						htla = new HomeTimeLineAdapter(statuss);
						lvFriendHome.setAdapter(htla);
					} else {
						htla.notifyDataSetChanged();
					}
				}
			});
			tvLoading.setVisibility(View.GONE);
			if(pDialog != null && pDialog.isShowing()){
				pDialog.dismiss();
			}
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {

					getFriendStatus();

				Constant.getMsg = false;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		last_item = firstVisibleItem + visibleItemCount;
		visiableFirstItem = firstVisibleItem;
		visiableItemCount = visibleItemCount;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (last_item == htla.getCount() + 1
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			pDialog = ProgressDialog.show(appref, appref
					.getResources().getString(R.string.progress_title), appref
					.getResources().getString(R.string.progress_content));
			pDialog.setCancelable(true);
			pDialog.show();
			page_index += 1;
			Constant.getMsg = true;
			fst = new FriendStatusTask();
			fst.execute();
		}
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			for (int i = visiableFirstItem; i < visiableFirstItem
					+ visiableItemCount - 1; i++) {

				View viewTemp = (View) htla.getItem(i);

				final Status statusTemp = statuss.get(i);

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

}
