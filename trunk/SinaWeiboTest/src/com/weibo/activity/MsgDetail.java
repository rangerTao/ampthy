package com.weibo.activity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import weibo4android.Comment;
import weibo4android.RetweetDetails;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;

import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.adapter.ComentsAdapter;

import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MsgDetail extends Activity implements OnClickListener {

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
	
	ComentsAdapter ca;

	Status status;
	User user;
	Status rd;
	User retweetUser;
	
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
		Bundle index = this.getIntent().getExtras();
		int detailIndex = index.getInt("index");
		status = IndexActivity.statuses.get(detailIndex);

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
		
		tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
		tvLoc = (TextView) findViewById(R.id.tvLoc);
		tvDetail = (TextView) findViewById(R.id.tvDetail);
		ivUserHead = (ImageView) findViewById(R.id.ivUserHead);
		ivThumb = (ImageView) findViewById(R.id.ivDetaiThumb);
		tvLoading = (TextView) findViewById(R.id.loadingComents);
		
		rlForward = (RelativeLayout)findViewById(R.id.rlForward);
		forward_tvUserName = (TextView)findViewById(R.id.forward_tvUserName);
		forward_tvStatus = (TextView)findViewById(R.id.forward_tvStatus);
		forward_ivThumbail = (ImageView)findViewById(R.id.forward_ivThumbail);
		
		btnComment = (Button)findViewById(R.id.btnComment);
		btnComment.setOnClickListener(this);
		btnForward = (Button)findViewById(R.id.btnForward);
		btnForward.setOnClickListener(this);
		btnAt = (Button)findViewById(R.id.btnAtH);
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
			btnDetai = (Button)findViewById(R.id.btnDetail);
			btnDetai.setVisibility(View.VISIBLE);
			btnDetai.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					btnDetai.setVisibility(View.GONE);
					rlForward.setVisibility(View.VISIBLE);
				}
			});
			
			forward_tvUserName.setText(retweetUser.getScreenName().toString());
			forward_tvStatus.setText(rd.getText().toString());
			if(rd.getThumbnail_pic() != null || rd.getThumbnail_pic() != ""){
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
			}
		
			rlForward.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					rlForward.setVisibility(View.GONE);
					btnDetai.setVisibility(View.VISIBLE);
				}
			});
		}
		
		tvLoading.setText(R.string.gettingcom);
		
		CommentTask ct = new CommentTask();
		ct.execute();

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
								Toast.makeText(appref, R.string.comsuccess, 2000).show();
							} catch (WeiboException e) {
								Toast.makeText(appref, R.string.neterror, 2000).show();
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
}
