package com.weibo.activity;

import java.net.MalformedURLException;
import java.net.URL;

import weibo4andriod.Status;
import weibo4andriod.User;

import com.weibo.R;
import com.weibo.pojo.UserImpl;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

import android.R.color;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MsgDetail extends Activity {

	TextView tvUserScreenName;
	TextView tvLoc;
	TextView tvDetail;
	ImageView ivUserHead;
	ImageView ivThumb;

	Status status;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.msgdetail);
		Bundle index = this.getIntent().getExtras();
		int detailIndex = index.getInt("index");
		status = IndexActivity.statuses.get(detailIndex);
		user = status.getUser();

		initCompent();

		initCompentData();
	}

	private void initCompent() {
		tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
		tvLoc = (TextView) findViewById(R.id.tvLoc);
		tvDetail = (TextView) findViewById(R.id.tvDetail);
		ivUserHead = (ImageView) findViewById(R.id.ivUserHead);
		ivThumb = (ImageView) findViewById(R.id.ivDetaiThumb);
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
	}
}
