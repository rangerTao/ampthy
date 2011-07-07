package com.weibo.activity;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import weibo4android.Comment;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.UserImpl;
import com.weibo.pojo.adapter.ComentsAdapter;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class MsgDetail extends Activity implements OnClickListener, OnItemClickListener {

	Weibo weibo;
	
	TextView tvUserScreenName;
	TextView tvLoc;
	TextView tvDetail;
	ImageView ivUserHead;
	ImageView ivThumb;
	TextView tvLoading;
	Button btnComment;
	Button btnForward;
	Button btnAt;
	
	RelativeLayout rlForward;
	TextView forward_tvUserName;
	TextView forward_tvStatus;
	ImageView forward_ivThumbail;
	Button btnDetai;
	ListView lvComments;
	
	View view;
	ComentsAdapter ca;

	Status status;
	User user;
	Status rd;
	User retweetUser;
	
	Handler handler = new Handler();
	
	public static MsgDetail appref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.msgdetail);
		appref = this;
		status = Constant.tmpStatus;

		if(status.isRetweet()){
			rd = status.getRetweeted_status();
			retweetUser = rd.getUser();
		}
		user = status.getUser();

		initCompent();

		initCompentData();
	}

	private void initCompent() {
		weibo = OAuthConstant.getInstance().getWeibo();
		
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.msgdetail_header, null);
		
		tvUserScreenName = (TextView) view.findViewById(R.id.tvUserScreenName);
		tvLoc = (TextView) view.findViewById(R.id.tvLoc);
		tvDetail = (TextView) view.findViewById(R.id.tvDetail);
		ivUserHead = (ImageView) view.findViewById(R.id.ivUserHead);
		ivThumb = (ImageView) view.findViewById(R.id.ivDetaiThumb);
		tvLoading = (TextView) view.findViewById(R.id.loadingComents);
		
		rlForward = (RelativeLayout)view.findViewById(R.id.rlForward);
		forward_tvUserName = (TextView)view.findViewById(R.id.forward_tvUserName);
		forward_tvStatus = (TextView)view.findViewById(R.id.forward_tvStatus);
		forward_ivThumbail = (ImageView)view.findViewById(R.id.forward_ivThumbail);
		
		btnComment = (Button)view.findViewById(R.id.btnComment);
		btnComment.setOnClickListener(this);
		btnForward = (Button)view.findViewById(R.id.btnForward);
		btnForward.setOnClickListener(this);
		btnAt = (Button)view.findViewById(R.id.btnAtH);
		btnAt.setOnClickListener(this);
		
		lvComments = (ListView) findViewById(R.id.lvComent);
	}

	private void initCompentData() {
		tvUserScreenName.setText(user.getScreenName().toString());
		tvLoc.setText(user.getLocation());
		tvDetail.setText(status.getText());
		tvDetail.setPadding(10, 5, 0, 0);
		if (Constant.imageMap.get(user.getProfileImageURL().toString()) != null) {
			ivUserHead.setImageBitmap(Constant.imageMap.get(user
					.getProfileImageURL().toString()));
		} else {
			Constant.imageMap.put(user.getProfileImageURL().toString(), null);
			ivUserHead.setImageBitmap(BitmapFactory.decodeResource(
					IndexActivity.appref.getResources(), R.drawable.refresh));
			Bitmap tempBitmap = WeiboUtils.getImage(user.getProfileImageURL());
			WeiboUtils.setImage(IndexActivity.appref.handler, ivUserHead,
					tempBitmap);
			Constant.imageMap.put(user.getProfileImageURL().toString(),
					tempBitmap);
		}
		if (status.getThumbnail_pic() != null
				&& status.getThumbnail_pic() != "") {
			if (Constant.imageMap.get(status.getThumbnail_pic()) != null) {
				ivThumb.setImageBitmap(Constant.imageMap.get(status
						.getThumbnail_pic()));
			} else {
				try {
					Constant.imageMap.put(status.getThumbnail_pic(), null);
					ivThumb.setImageBitmap(BitmapFactory.decodeResource(
							IndexActivity.appref.getResources(),
							R.drawable.refresh));

					Bitmap tempBitmap = WeiboUtils.getImage(new URL(status
							.getThumbnail_pic()));
					WeiboUtils.setImage(IndexActivity.appref.handler,
							ivUserHead, tempBitmap);
					Constant.imageMap
							.put(status.getThumbnail_pic(), tempBitmap);
				} catch (Exception e) {
					Toast.makeText(this, "Error", 2000).show();
					Log.v("TAG", e.getMessage());
				}
			}
		}
		
		if(status.isRetweet()){

			rlForward.setVisibility(View.VISIBLE);
			forward_tvUserName.setText(retweetUser.getScreenName().toString());
			forward_tvStatus.setText(rd.getText().toString());
			if(rd.getThumbnail_pic() != null && rd.getThumbnail_pic() != ""){
				forward_ivThumbail.setVisibility(View.VISIBLE);
				if (Constant.imageMap.containsKey(rd.getThumbnail_pic()) == false) {
					forward_ivThumbail.setImageBitmap(BitmapFactory
							.decodeResource(IndexActivity.appref.getResources(),
									R.drawable.refresh));
					try {
						Constant.sit.pushImageTask(new URL(rd.getThumbnail_pic()), forward_ivThumbail);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				} else {
					forward_ivThumbail.setImageBitmap(Constant.imageMap
							.get(rd.getThumbnail_pic()));
				}
			}else{
				forward_ivThumbail.setVisibility(View.GONE);
			}
		}
		
		tvLoading.setText(R.string.gettingcom);
		if(lvComments.getHeaderViewsCount() <= 0){
			lvComments.addHeaderView(view);
		}
		lvComments.setAdapter(null);
		CommentTask ct = new CommentTask();
		ct.execute();
		lvComments.setOnItemClickListener(this);
	}

	public void onClick(View arg0) {
		
		//Comment
		if(arg0.getId() == btnComment.getId()){
			
			final EditText etAtEditText = new EditText(this);
			new AlertDialog.Builder(this).setTitle(R.string.input).setIcon(
				     android.R.drawable.ic_dialog_info).setView(
				    		 etAtEditText).setPositiveButton(R.string.passwd_set_confirm, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface arg0, int arg1) {
							try {
								weibo.updateComment(etAtEditText.getText().toString(), status.getId()+"", null);
								showToast(appref.getResources().getString(R.string.comsuccess, 2000));
								initCompentData();
							} catch (WeiboException e) {
								showToast(appref.getResources().getString(R.string.neterror, 2000));
							}
						}
					})
				     .setNegativeButton(R.string.passwd_set_cancel, null).show();
		
		}
		
		//Forward
		if(arg0.getId() == btnForward.getId()){
			try {
				weibo.retweetStatus(status.getId());
				Toast.makeText(this, R.string.forwardsuccess , 2000).show();
			} catch (WeiboException e) {
				Toast.makeText(this, R.string.neterror , 2000).show();
			}
		}
		
		//At her or him
		if(arg0.getId() == btnAt.getId()){
			final EditText etAtEditText = new EditText(this);
			etAtEditText.append("@"+user.getScreenName() + " ");
			new AlertDialog.Builder(this).setTitle(R.string.input).setIcon(
				     android.R.drawable.ic_dialog_info).setView(
				    		 etAtEditText).setPositiveButton(R.string.passwd_set_confirm, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface arg0, int arg1) {
							try {
								Toast.makeText(appref, R.string.comsuccess, 2000).show();
								weibo.updateStatus(etAtEditText.getText().toString());
							} catch (WeiboException e) {
								Toast.makeText(appref, R.string.neterror, 2000).show();
							}
						}
					})
				     .setNegativeButton(R.string.passwd_set_cancel, null).show();
		}
		
	}
	
	private class CommentTask extends AsyncTask{

		@Override
		protected void onPostExecute(Object result) {
			if(Constant.commentList.size() > 0){
				ca = new ComentsAdapter();
				
				tvLoading.setVisibility(View.GONE);
				lvComments.setAdapter(ca);
			}else{
				tvLoading.setText(R.string.nocomments);
			}
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				Constant.commentList.clear();
				for(Comment temp : weibo.getComments(status.getId() + "")){
					Constant.commentList.add(temp);
				}
				
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return arg0;
		}
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		final Comment comment = Constant.commentList.get(position -1 );

		if (!(comment.getUser().getId() + "").equalsIgnoreCase(UserImpl.getID())) {
			final EditText etReply = new EditText(this);
			etReply.setHint(R.string.text_hint);
			new AlertDialog.Builder(this).setTitle(appref.getResources().getString(R.string.reply_title)).setView(etReply)
					.setPositiveButton(R.string.passwd_set_confirm,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
										if(etReply.getText().toString().length() <= 0){
											showToast(appref.getResources().getString(R.string.toast_too_short));
											return;
										}else if(etReply.getText().toString().getBytes().length > 140){
											showToast(appref.getResources().getString(R.string.toast_too_long));
											return;
										}
										handler.post(new Runnable(){

											public void run() {
												try {
													weibo.updateComment(URLEncoder.encode(etReply.getText().toString(),"UTF-8"), 
															status.getId() +"", comment.getId()+"",0);
													initCompentData();
												} catch (UnsupportedEncodingException e) {
													
												} catch (WeiboException e) {
													showToast(appref.getResources().getString(R.string.neterror));
												}
												showToast(appref.getResources().getString(R.string.reply_success));												
											}
											
										});
								}
							}).setNegativeButton(R.string.passwd_set_cancel,
							null).show();
		}else{
			Toast.makeText(appref, appref.getResources().getString(R.string.toast_cannot_reply_to_self), 1000).show();
		}
	}
	
	private void showToast(final String in){
		handler.post(new Runnable(){

			public void run() {
				Toast.makeText(appref, in, 2000).show();
			}
			
		});
	}
}
