package com.weibo.pojo.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.pojo.OAuthConstant;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

public class HomeTimeLineAdapter extends BaseAdapter {

	URL urlString;
	Bitmap bmpUserHead;
	ArrayList<Status> inputList;
	LayoutInflater layoutInflater;
	PopupWindow mPopup;
	View popup;
	ImageView iView;
	Activity appref;

	public HomeTimeLineAdapter(List<Status> tmp, Activity context){
		inputList = (ArrayList<Status>) tmp;
		appref = context;
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
		final Status status = inputList.get(position);
		final User user = status.getUser();
		
		ViewHolder holder;
		if(convertView == null){
			layoutInflater = LayoutInflater
			.from(appref);
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
			
			holder.llForward = (RelativeLayout)convertView.findViewById(R.id.rlForward);
			holder.forward_tvUserName = (TextView)convertView.findViewById(R.id.forward_tvUserName);
			holder.forward_tvStatus = (TextView)convertView.findViewById(R.id.forward_tvStatus);
			holder.forward_ivThumbail = (ImageView)convertView.findViewById(R.id.forward_ivThumbail);
			
			holder.ivStatusImage = (ImageView) convertView.findViewById(R.id.ivThumbail);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
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
				appref.getResources(), R.drawable.loading));
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
						.decodeResource(appref.getResources(),
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
			
			
			holder.ivStatusImage.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					getPopup();
					if(Constant.imageMap.containsKey(status.getBmiddle_pic())){
						iView.setImageBitmap(Constant.imageMap.get(status.getBmiddle_pic()));
					}else{
						try {
							Bitmap bmp = WeiboUtils.getImage(new URL(status.getBmiddle_pic()));
							iView.setImageBitmap(bmp);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					popup.setOnClickListener(new OnClickListener(){

						public void onClick(View v) {
							if(mPopup != null && mPopup.isShowing()){
								mPopup.dismiss();
								popup = null;
								mPopup = null;
							}
							
						}
						
					});

				}

			});
		} else {
			holder.ivStatusImage.setVisibility(View.GONE);
		}
		
		convertView.setPadding(0, 3, 0, 0);

		
		if(status.isRetweet()){
			final Status retweetStatus = status.getRetweeted_status();
			User retweetUser = retweetStatus.getUser();
			holder.llForward.setVisibility(View.VISIBLE);
			holder.forward_tvUserName.setText(retweetUser.getScreenName().toString());
			holder.forward_tvStatus.setText(Html.fromHtml(retweetStatus.getText().toString()));
			if(retweetStatus.getThumbnail_pic() != null && retweetStatus.getThumbnail_pic() != ""){
				holder.forward_ivThumbail.setVisibility(View.VISIBLE);
				if (Constant.imageMap.containsKey(retweetStatus.getThumbnail_pic()) == false) {
					holder.forward_ivThumbail.setImageBitmap(BitmapFactory
							.decodeResource(appref.getResources(),
									R.drawable.refresh));
					try {
						Constant.sit.pushImageTask(new URL(retweetStatus.getThumbnail_pic()), holder.forward_ivThumbail);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				} else {
					holder.forward_ivThumbail.setImageBitmap(Constant.imageMap
							.get(retweetStatus.getThumbnail_pic()));
				}
			}else{
				holder.forward_ivThumbail.setVisibility(View.GONE);
			}
			holder.forward_ivThumbail.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					getPopup();
					if(Constant.imageMap.containsKey(status.getThumbnail_pic())){
						iView.setImageBitmap(Constant.imageMap.get(retweetStatus.getBmiddle_pic()));
					}else{
						try {
							Bitmap bmp = WeiboUtils.getImage(new URL(retweetStatus.getBmiddle_pic()));
							iView.setImageBitmap(bmp);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					popup.setOnClickListener(new OnClickListener(){

						public void onClick(View v) {
							mPopup.dismiss();
							popup = null;
							mPopup = null;
						}
						
					});

				}

			});
		}else{
			holder.llForward.setVisibility(View.GONE);
		}
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
		
		RelativeLayout llForward;
		TextView forward_tvUserName;
		TextView forward_tvStatus;
		ImageView forward_ivThumbail;
	}
	
	private void getPopup(){
		popup = layoutInflater.inflate(R.layout.image_popup,
				null);
		iView = (ImageView) popup.findViewById(R.id.image);
		mPopup = new PopupWindow(popup,
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mPopup.setAnimationStyle(R.style.imagePopScale);
		mPopup.showAtLocation(appref
				.findViewById(R.id.main),
				Gravity.CENTER, 0, 0);
	}
}
