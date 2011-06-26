package com.weibo.pojo.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import weibo4andriod.RetweetDetails;
import weibo4andriod.Status;
import weibo4andriod.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class HomeTimeLineAdapter extends BaseAdapter {

	URL urlString;
	Bitmap bmpUserHead;
	ArrayList<Status> inputList;

	public HomeTimeLineAdapter(List<Status> tmp){
		inputList = (ArrayList<Status>) tmp;
	}

	public int getCount() {
		return inputList.size();
	}

	public Object getItem(int position) {
		return getView(position,null,null);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		Status status = inputList.get(position);
		final User user = status.getUser();
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater layoutInflater = LayoutInflater
			.from(IndexActivity.appref);
			convertView = layoutInflater.inflate(R.layout.friendstimeline_adapter, null);
			
			holder = new ViewHolder();
			
			holder.tvUserNameTextView = (TextView) convertView.findViewById(R.id.tvUserName);
			holder.tvUserLocationg = (TextView) convertView.findViewById(R.id.tvUserLocation);
			holder.tvUserDesc = (TextView) convertView.findViewById(R.id.tvUserDesc);
			holder.tvUserDesc.setVisibility(View.GONE);
			holder.tvUserStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.tvTimeCreate = (TextView)convertView.findViewById(R.id.tvTimeCreate);
			holder.tvSource = (TextView)convertView.findViewById(R.id.tvSource);
			holder.ivUserHead = (ImageView) convertView.findViewById(R.id.ivUserHead);
			
			holder.ivStatusImage = (ImageView) convertView.findViewById(R.id.ivThumbail);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
//		TextView tvRewardBy = (TextView)view.findViewById(R.id.tvRewardBy);
//		TextView tvCommentBy = (TextView)view.findViewById(R.id.tvCommentBy);
		holder.tvUserStatus.setPadding(10, 5, 0, 0);
		holder.tvUserNameTextView.setText(user.getScreenName());
		holder.tvUserLocationg.setText(user.getLocation());
		
		if (user.getDescription() == null || user.getDescription().equals("")) {
			holder.tvUserDesc.setVisibility(View.GONE);
		} else {
			holder.tvUserDesc.setText(user.getDescription().toString());
		}

		urlString = user.getProfileImageURL();
		holder.ivUserHead.setImageBitmap(BitmapFactory.decodeResource(
				IndexActivity.appref.getResources(), R.drawable.loading));
		if (Constant.imageMap.containsKey(user.getProfileImageURL().toString()) == false) {
			Constant.sit.pushImageTask(user.getProfileImageURL(), holder.ivUserHead);
		} else {
			holder.ivUserHead.setImageBitmap(Constant.imageMap.get(user.getProfileImageURL().toString() + ""));
		}
		holder.tvUserStatus.setText(status.getText().toString());

		holder.tvSource.setText(Html.fromHtml(status.getSource().toString()));
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm  ");
		holder.tvTimeCreate.setText(sdf.format(status.getCreatedAt()));

		if (status.getThumbnail_pic() != null
				&& status.getThumbnail_pic() != "") {
			if (Constant.imageMap.containsKey(status.getThumbnail_pic()) == false) {
				holder.ivStatusImage.setImageBitmap(BitmapFactory
						.decodeResource(IndexActivity.appref.getResources(),
								R.drawable.refresh));
				try {
					Constant.sit.pushImageTask(new URL(status.getThumbnail_pic()), holder.ivStatusImage);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			} else {
				holder.ivStatusImage.setImageBitmap(Constant.imageMap
						.get(status.getThumbnail_pic()));
			}
			holder.ivStatusImage.setPadding(10, 2, 0, 0);
			holder.ivStatusImage.setVisibility(View.VISIBLE);
		} else {
			holder.ivStatusImage.setVisibility(View.GONE);
		}
		
		convertView.setPadding(0, 3, 0, 0);
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

		return convertView;
	}
	
	static class ViewHolder{
		TextView tvUserNameTextView;
		TextView tvUserLocationg;
		TextView tvUserDesc;
		TextView tvUserStatus;
		TextView tvTimeCreate;
		TextView tvSource;
		ImageView ivUserHead;
		ImageView ivStatusImage;
	}
}
