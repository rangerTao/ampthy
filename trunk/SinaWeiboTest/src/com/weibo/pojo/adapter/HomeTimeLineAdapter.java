package com.weibo.pojo.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import weibo4andriod.RetweetDetails;
import weibo4andriod.Status;
import weibo4andriod.User;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.activity.MsgDetail;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class HomeTimeLineAdapter extends BaseAdapter {

	ImageView ivUserHead;
	URL urlString;
	Bitmap bmpUserHead;
	

	public int getCount() {
		return IndexActivity.statuses.size();
	}

	public Object getItem(int position) {
		return getView(position,null,null);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		Status status = IndexActivity.statuses.get(position);
		final User user = status.getUser();
		LayoutInflater layoutInflater = LayoutInflater
				.from(IndexActivity.appref);
		View view = layoutInflater.inflate(R.layout.friendstimeline_adapter,
				null);
		ivUserHead = (ImageView) view.findViewById(R.id.ivUserHead);
		TextView tvUserNameTextView = (TextView) view
				.findViewById(R.id.tvUserName);
		TextView tvUserLocationg = (TextView) view
				.findViewById(R.id.tvUserLocation);
		TextView tvUserDesc = (TextView) view.findViewById(R.id.tvUserDesc);
		tvUserDesc.setVisibility(View.GONE);
		TextView tvUserStatus = (TextView) view.findViewById(R.id.tvStatus);
		TextView tvTimeCreate = (TextView)view.findViewById(R.id.tvTimeCreate);
		TextView tvSource = (TextView)view.findViewById(R.id.tvSource);
//		TextView tvRewardBy = (TextView)view.findViewById(R.id.tvRewardBy);
//		TextView tvCommentBy = (TextView)view.findViewById(R.id.tvCommentBy);
		tvUserStatus.setPadding(10, 5, 0, 0);
		tvUserNameTextView.setText(user.getScreenName());
		tvUserLocationg.setText(user.getLocation());
		
		if (user.getDescription() == null || user.getDescription().equals("")) {
			tvUserDesc.setVisibility(View.GONE);
		} else {
			tvUserDesc.setText(user.getDescription().toString());
		}

		urlString = user.getProfileImageURL();
		ivUserHead.setImageBitmap(BitmapFactory.decodeResource(
				IndexActivity.appref.getResources(), R.drawable.loading));
		if (Constant.imageMap.containsKey(user.getProfileImageURL().toString()) == false) {
			Constant.imageMap.put(user.getProfileImageURL().toString() + "", null);
			Bitmap tempBitmap = WeiboUtils.getImage(user.getProfileImageURL());
			WeiboUtils.setImage(IndexActivity.appref.handler, ivUserHead,
					tempBitmap);
			Constant.imageMap.put(user.getProfileImageURL().toString() + "", tempBitmap);
		} else {
			ivUserHead.setImageBitmap(Constant.imageMap.get(user.getProfileImageURL().toString() + ""));
		}
		tvUserStatus.setText(status.getText().toString());
		//tvSource.setText(status.getSource().toString());
		tvSource.setText(Html.fromHtml(status.getSource().toString()));
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm  ");
		tvTimeCreate.setText(sdf.format(status.getCreatedAt()));
		
		ImageView ivStatusImage = (ImageView) view
				.findViewById(R.id.ivThumbail);
		if (status.getThumbnail_pic() != null
				&& status.getThumbnail_pic() != "") {
			if (Constant.imageMap.containsKey(status.getThumbnail_pic()) == false) {
				Constant.imageMap.put(status.getThumbnail_pic(), null);
				ivStatusImage.setImageBitmap(BitmapFactory
						.decodeResource(IndexActivity.appref.getResources(),
								R.drawable.refresh));
				try {
					Bitmap tempBitmap = WeiboUtils.getImage(new URL(status
							.getThumbnail_pic()));
					WeiboUtils.setImage(IndexActivity.appref.handler,
							ivStatusImage, tempBitmap);
					Constant.imageMap.put(status.getThumbnail_pic(), tempBitmap);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			} else {
				ivStatusImage.setImageBitmap(Constant.imageMap
						.get(status.getThumbnail_pic()));
			}

		} else {
			ivStatusImage.setVisibility(View.GONE);
		}
		ivStatusImage.setPadding(10, 2, 0, 0);
		view.setPadding(0, 3, 0, 0);
		RetweetDetails statusRetweet = status.getRetweetDetails();
		RelativeLayout rlayout = new RelativeLayout(IndexActivity.appref);
		if (statusRetweet != null && statusRetweet.getRetweetId() != 0) {
			View viewRetweet = new View(IndexActivity.appref);
			TextView tvRetweetUserName = new TextView(IndexActivity.appref);
			TextView tvRetweetDetail = new TextView(IndexActivity.appref);
			tvRetweetUserName
					.setText(statusRetweet.getRetweetingUser() == null ? statusRetweet
							.getRetweetingUser().getScreenName().toString()
							: "");

			Log.v("TAG", statusRetweet.toString());

			// rlayout.addView(tvRetweetUserName);
			// rlayout.addView(tvRetweetDetail);
		}
		// RelativeLayout rl = (RelativeLayout) view
		// .findViewById(R.id.rlFriendsTimeLine);
		// rl.addView(rlayout);

		return view;
	}
}
