package com.weibo.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.TimeoutController.TimeoutException;

import weibo4android.Paging;
import weibo4android.Query;
import weibo4android.SearchResult;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.BaseActivity;
import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.UserImpl;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;
import com.weibo.task.AtMeTask;
import com.weibo.task.CommentsTask;
import com.weibo.task.FavourTask;
import com.weibo.task.FriendsTask;
import com.weibo.task.MailTask;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class IndexActivity extends BaseActivity implements OnItemClickListener,
		OnItemLongClickListener {

	String token;
	String tokenSecret;
	String access;
	String accessSecret;

	public ProgressDialog pDialog;
	public static ListView lvHomeTimeLine;
	ListView lvTopMenu;
	StringBuffer sbAll = new StringBuffer();
	HorizontalScrollView hs;

	public static TextView tvHeaderUserName;

	public static Handler handler = new Handler();
	public static String[] strTopMenus;

	public static IndexActivity appref;

	public static List<User> friends;
	public static List<Status> statuses = new ArrayList<Status>();
	public static List<Status> statuses2 = new ArrayList<Status>();
	public static List<Long> statusesIds = new ArrayList<Long>();
	public static Map<Long, Status> statusToId = new HashMap<Long, Status>();

	public Weibo weibo = OAuthConstant.getInstance().getWeibo();

	private int page_index = 1;
	private int last_item = 0;

	private int visiableFirstItem = 0;
	private int visiableItemCount = 0;

	HomeTimeLineAdapter htla;
	FriendTask ft;

	// Button
	Button btnHome;
	Button btnAtMe;
	Button btnFavour;
	Button btnComment;
	Button btnMail;
	Button btnFriends;
	Button btnMore;
	Button btnChat;

	// Popup
	View popupView;
	PopupWindow mPopup;
	LayoutInflater layoutInflater;

	View topPop;
	PopupWindow topPopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.index_activity);
		layoutInflater = LayoutInflater.from(this);
		lvHomeTimeLine = (ListView) findViewById(R.id.lvHomeTimeLine);
		// initData();
		initHeader();
		
		appref = this;

		if (null == Constant.spAll || Constant.spAll.getInt(Constant.ISRUNNING, Constant._NOTRUNNING) != Constant._ISRUNNING) {
			Constant.getMsg = true;
			ft = new FriendTask();
			ft.execute();
		}

		if (!Constant.sit.isAlive()) {
			Constant.sit.start();
		}

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
					Constant.getMsg = true;
					ft = new FriendTask();
					ft.execute();
				}
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (visiableFirstItem > 0 && topPopup == null) {
						showBackTopPop();
					}
					if (visiableFirstItem == 0) {
						dismissTopPop();
					}

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

		lvHomeTimeLine.setOnItemClickListener(this);
		// lvHomeTimeLine.setOnCreateContextMenuListener(this);
		lvHomeTimeLine.setOnItemLongClickListener(this);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}



	private void showBackTopPop() {
		topPop = layoutInflater.inflate(R.layout.back_top, null);
		topPopup = new PopupWindow(topPop, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		topPopup.showAtLocation(appref.findViewById(R.id.main), Gravity.CENTER
				| Gravity.LEFT, 0, 60);

		topPop.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				dismissTopPop();
				lvHomeTimeLine.setSelectionFromTop(0, 0);
			}
		});
	}

	private void dismissTopPop() {
		if (topPopup != null && topPopup.isShowing()) {
			topPopup.dismiss();
			topPop = null;
			topPopup = null;
		}

	}

	@Override
	protected void onResume() {
		weibo = OAuthConstant.getInstance().getWeibo();
		super.onResume();
	}

	protected void onPause() {
		SharedPreferences.Editor editor = Constant.spAll.edit();
		editor.putInt(Constant.ISRUNNING, Constant._ISRUNNING);
		editor.commit();
		super.onPause();
	}

	protected void onStop() {
		SharedPreferences.Editor editor = Constant.spAll.edit();
		editor.putInt(Constant.ISRUNNING, Constant._ISRUNNING);
		editor.commit();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		SharedPreferences.Editor editor = Constant.spAll.edit();
		editor.putInt(Constant.ISRUNNING, Constant._ISRUNNING);
		editor.commit();
		this.dismissPD();
		super.onDestroy();
	}

	public void initButtonAction() {
		btnHome = (Button) findViewById(R.id.btnHome_TopMenu);
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				initProgressDialog();
				lvHomeTimeLine.removeAllViewsInLayout();
				if (statuses.size() < 1) {
					Constant.getMsg = true;
					ft = new FriendTask();
					ft.execute();
				} else {
					lvHomeTimeLine.removeAllViewsInLayout();
					if (htla == null) {
						htla = new HomeTimeLineAdapter(statuses, appref);
					}
					lvHomeTimeLine.setAdapter(htla);
				}
				resetButtonBG();
				Constant.weiboChannel = Constant.indexChannel;
				btnHome.setBackgroundResource(R.drawable.btn_bg);
				lvHomeTimeLine.setOnItemClickListener(IndexActivity.appref);
				dismissPD();
			}

		});

		btnAtMe = (Button) findViewById(R.id.btnAtMe_TopMenu);
		btnAtMe.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				initProgressDialog();
				lvHomeTimeLine.removeAllViewsInLayout();
				if (Constant.atMeList.size() < 1) {
					Constant.getMsg = true;
				}
				AtMeTask atMeTask = new AtMeTask();
				atMeTask.execute();
				Constant.weiboChannel = Constant.atMeChannel;
				resetButtonBG();
				btnAtMe.setBackgroundResource(R.drawable.btn_bg);
				lvHomeTimeLine.setOnItemClickListener(null);

			}
		});

		btnChat = (Button) findViewById(R.id.btnChat_TopMenu);
		btnChat.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent(appref, ChatActivity.class);
				startActivity(intent);
			}
		});

		btnMore = (Button) findViewById(R.id.btnMore_TopMenu);
		btnMore.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				popupView = layoutInflater.inflate(R.layout.more_menu, null);
				mPopup = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
				mPopup.setAnimationStyle(R.style.popupAlpha);
				mPopup.showAtLocation(appref.findViewById(R.id.main),
						Gravity.CENTER, 0, 0);
				mPopup.update();

				Button btnSearch = (Button) popupView
						.findViewById(R.id.btnSearch_TopMenu);
				btnSearch.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						startActivity(new Intent(appref, SearchActivity.class));
						dismissPop();
					}
				});
				
				Button btnSetting = (Button) popupView.findViewById(R.id.btnSetting_TopMenu);
				btnSetting.setOnClickListener(new OnClickListener(){

					public void onClick(View arg0) {
						Intent intent = new Intent(appref,SettingPre.class);
						appref.startActivity(intent);
						dismissPop();
					}
					
				});
				
				btnComment = (Button)popupView.findViewById(R.id.btnComments_TopMenu);
				btnComment.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
				initProgressDialog();
				if(Constant.commentList.size() < 1){
				Constant.getMsg = true;
				}
				CommentsTask ct = new CommentsTask();
				ct.execute();
				dismissPD();
				Constant.weiboChannel = Constant.commentChannel;
				resetButtonBG();
				btnComment.setBackgroundResource(R.drawable.btn_bg);
				}
				});
				
				btnFriends = (Button) popupView.findViewById(R.id.btnFriend_TopMenu);
				btnFriends.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// initProgressDialog();
						if (Constant.friendsList.size() < 1) {
							Constant.getMsg = true;
						}
						FriendsTask ft = new FriendsTask();
						ft.execute();
						dismissPD();
						Constant.weiboChannel = Constant.friendsChannel;
						resetButtonBG();
						btnFriends.setBackgroundResource(R.drawable.btn_bg);
					}
				});
				
				popupView.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						dismissPop();
					}
				});
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
						try {
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(appref
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							appref.showProgressDialog();
							if (etTwitter.getText().toString().length() > 0) {
								weibo.updateStatus(etTwitter.getText().toString());
							}
						} catch (WeiboException e) {
							Toast.makeText(appref, "Updating error", 2000)
									.show();
						} finally {
							llEditTwitter.setVisibility(View.GONE);
							appref.dismissPD();
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
				switch (Constant.weiboChannel) {
				case Constant.indexChannel:
					page_index = 1;
					clearArrayList();
					try {
						FileOutputStream fos = appref.openFileOutput(
								Constant.homeTimeLineCache, MODE_PRIVATE);
						fos.write("".getBytes());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Constant.getMsg = true;
					ft = new FriendTask();
					ft.execute();
					if (htla != null) {
						htla.notifyDataSetChanged();
					}
					break;
				case Constant.atMeChannel:
					Constant.atMe_PageIndex = 1;
					Constant.atMeList.clear();
					Constant.statuses = new ArrayList<Status>();
					Constant.getMsg = true;
					AtMeTask atMeTask = new AtMeTask();
					atMeTask.execute();
					htla.notifyDataSetChanged();
					break;
				case Constant.favourChannel:
					Constant.favour_PageIndex = 1;
					Constant.favourList.clear();
					Constant.statuses = new ArrayList<Status>();
					Constant.getMsg = true;
					FavourTask ftFavourTask = new FavourTask();
					ftFavourTask.execute();
					htla.notifyDataSetChanged();
					break;
				case Constant.commentChannel:
					Constant.comList.clear();
					Constant.statuses = new ArrayList<Status>();
					Constant.getMsg = true;
					CommentsTask ct = new CommentsTask();
					ct.execute();
					htla.notifyDataSetChanged();
					break;
				case Constant.mailChannel:
					Constant.mail_PageIndex = 1;
					// Constant.mailList.clear();
					// Constant.mails = new ArrayList<DirectMessage>();
					Constant.getMsg = true;
					MailTask mt = new MailTask();
					mt.execute();
					htla.notifyDataSetChanged();
					break;
				case Constant.friendsChannel:
					Constant.friend_PageIndex = 1;
					Constant.friendsList.clear();
					Constant.getMsg = true;
					FriendsTask ft = new FriendsTask();
					ft.execute();
					htla.notifyDataSetChanged();
					break;
				}
			}

		});
	}

	public void resetButtonBG() {
		btnHome.setBackgroundResource(R.drawable.btn_bg_normal);
		btnAtMe.setBackgroundResource(R.drawable.btn_bg_normal);
		btnChat.setBackgroundResource(R.drawable.btn_bg_normal);
		if(null != btnFavour){
			btnFavour.setBackgroundResource(R.drawable.btn_bg_normal);
		}
		if(null != btnComment){
			btnComment.setBackgroundResource(R.drawable.btn_bg_normal);
		}
		if(null != btnMail){
			btnMail.setBackgroundResource(R.drawable.btn_bg_normal);
		}
		if(null != btnFriends){
			btnFriends.setBackgroundResource(R.drawable.btn_bg_normal);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mPopup != null && mPopup.isShowing()) {
				dismissPop();
				return true;
			}
			if (Constant._Back_Count == 0) {
				Toast.makeText(this, R.string.toast_back_1, 2000).show();
				Constant._Back_Count++;
				return true;
			} else if (Constant._Back_Count == 1) {
				if(Constant.spAll != null){
					SharedPreferences.Editor editor = Constant.spAll.edit();
					editor.putInt(Constant.ISRUNNING, Constant._NOTRUNNING);
					editor.commit();
				}
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	private void getFriends(int page_index)
			throws org.apache.commons.httpclient.util.TimeoutController.TimeoutException,
			IOException {

		Status out = null;
		try {
			weibo.setOAuthConsumer(Constant.CONSUMER_KEY,
					Constant.CONSUMER_SECRET);
			weibo.setToken(Constant._token, Constant._tokenSecret);
			weibo.setOAuthAccessToken(Constant._access, Constant._accessSecret);

			if (!Constant.isRunning) {
				FileOutputStream file = null;
				List<Status> temp = weibo
						.getHomeTimeline(new Paging(page_index));
				weibo.getFriendsTimeline(new Paging(page_index));

				try {
					file = appref.openFileOutput(Constant.homeTimeLineCache,
							MODE_APPEND);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				for (Status tmpStatus : temp) {
					if (!statusesIds.contains(tmpStatus.getId())) {
						statusToId.put(tmpStatus.getId(), tmpStatus);
						statusesIds.add(tmpStatus.getId());
						out = tmpStatus;
						file.write((tmpStatus.toString() + "\n").getBytes());
					}
				}
				statuses.clear();
				Collections.sort(statusesIds, Collections.reverseOrder());
				for (int k = 0; k < statusesIds.size(); k++) {
					statuses.add(statusToId.get(statusesIds.get(k)));
				}
			}
		} catch (WeiboException te) {
			Log.v("TAG", "Failed to get timeline: " + te.getMessage());
			Looper.prepare();
			Toast.makeText(IndexActivity.appref, "net error", 2000).show();
		}
	}

	private class FriendTask extends AsyncTask {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {

			IndexActivity.handler.post(new Runnable() {
				public void run() {

					if (lvHomeTimeLine.getChildCount() == 0) {
						htla = new HomeTimeLineAdapter(statuses, appref);
						lvHomeTimeLine.setAdapter(htla);
					} else {
						htla.notifyDataSetChanged();
					}

				}
			});

			dismissPD();

			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				try {

					if (statuses.size() <= 0) {
						// Log.v("TAG", statuses.size() + "");
						// FileInputStream fis = appref
						// .openFileInput(Constant.homeTimeLineCache);
						// BufferedReader br = new BufferedReader(
						// new InputStreamReader(fis));
						// String temp = "";
						//
						// while ((temp = br.readLine()) != null) {
						// weibo4android.Status statusCache = new
						// weibo4android.Status(
						// new JSONObject(temp));
						// statusesIds.add(statusCache.getId());
						// statusToId.put(statusCache.getId(), statusCache);
						// }
						// Log.v("TAG", statuses.size() + "");
						// Log.v("TAG", "Read cache");

						File imageCache = new File(Constant.image_cache_dir);
						if (imageCache.exists() && imageCache.isDirectory()) {
							for (File image : imageCache.listFiles()) {
								String imageName = WeiboUtils
										.decodeImageName(image);

								FileInputStream imageFis = new FileInputStream(
										image);
								Bitmap bmp = BitmapFactory
										.decodeStream(imageFis);
								Constant.imageMap.put(imageName, bmp);
							}
						}
					}

				} catch (Exception e) {
					Log.v("TAG", e.getMessage());
				}

				if (Constant.getMsg) {
					getFriends(page_index);
				}
				Constant.getMsg = false;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(IndexActivity.appref, MsgDetail.class);
		Constant.tmpStatus = statuses.get(arg2);
		IndexActivity.appref.startActivity(intent);
	}

	private void dismissPop() {
		mPopup.dismiss();
		popupView = null;
		mPopup = null;
	}

	private void clearArrayList() {
		statuses.clear();
		statuses2.clear();
		statuses2 = new ArrayList<Status>();
		statuses = new ArrayList<Status>();
		if (statusesIds != null) {
			statusesIds.clear();
		} else {
			statusesIds = new ArrayList<Long>();
		}
		if (statusToId != null) {
			statusToId.clear();
		} else {
			statusToId = new HashMap<Long, Status>();
		}
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {

		popupView = layoutInflater.inflate(R.layout.msg_contextmenu, null);
		mPopup = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mPopup.showAtLocation(appref.findViewById(R.id.main), Gravity.CENTER,
				0, 0);
		popupView.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				dismissPop();
			}
		});

		Button btnComment = (Button) popupView.findViewById(R.id.btnComment);
		Button btnForward = (Button) popupView.findViewById(R.id.btnForward);
		Button btnFavour = (Button) popupView.findViewById(R.id.btnFavour);
		Button btnHome = (Button) popupView.findViewById(R.id.btnHome);

		
		btnComment.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				final EditText etAtEditText = new EditText(appref);
				new AlertDialog.Builder(appref)
						.setTitle(R.string.input)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(etAtEditText)
						.setPositiveButton(R.string.passwd_set_confirm,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										try {
											weibo.updateComment(etAtEditText
													.getText().toString(),
													statuses.get(arg2).getId()
															+ "", null);
											Toast.makeText(appref,
													R.string.comsuccess, 2000)
													.show();
										} catch (WeiboException e) {
											Toast.makeText(appref,
													R.string.neterror, 2000)
													.show();
										}
									}
								})
						.setNegativeButton(R.string.passwd_set_cancel, null)
						.show();
				dismissPop();
			}
		});

		btnForward.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				final EditText etAtEditText = new EditText(appref);
				etAtEditText.setText("// "
						+ statuses.get(arg2).getText().toString());
				etAtEditText.setSelection(0, 0);
				new AlertDialog.Builder(appref)
						.setTitle(R.string.input)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(etAtEditText)
						.setPositiveButton(R.string.passwd_set_confirm,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										try {
											if (etAtEditText.getText()
													.toString() == "") {
												weibo.retweetStatus(statuses
														.get(arg2).getId());

											} else {
												weibo.updateStatus(etAtEditText
														.getText().toString(),
														statuses.get(arg2)
																.getId());
											}
											Toast.makeText(appref,
													R.string.forwardsuccess,
													2000).show();
										} catch (WeiboException e) {
											Toast.makeText(appref,
													R.string.neterror, 2000)
													.show();
										}
									}
								})
						.setNegativeButton(R.string.passwd_set_cancel, null)
						.show();
				dismissPop();
			}
		});

		btnFavour.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				showProgressDialog();
				try{
					weibo.createFavorite(statuses.get(arg2).getId());
				}catch(Exception e){
					Toast.makeText(appref, R.string.neterror, 2000).show();
				}finally{
					dismissPD();
					dismissPop();
					Toast.makeText(appref, R.string.favour_success, 2000).show();
				}
			}
		});
		
		btnHome.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// HomePageActivity.setUser(statuses.get(arg2).getUser());
				Intent intent = new Intent(appref, HomePageActivity.class);
				Constant.tmpStatus = statuses.get(arg2);
				dismissPop();
				startActivity(intent);
			}
		});
		return true;
	}

}