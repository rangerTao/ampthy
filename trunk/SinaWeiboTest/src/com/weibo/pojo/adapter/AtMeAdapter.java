package com.weibo.pojo.adapter;

import java.net.MalformedURLException;
import java.net.URL;

import weibo4android.Status;
import weibo4android.User;

import com.weibo.R;
import com.weibo.R.id;
import com.weibo.activity.IndexActivity;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AtMeAdapter extends BaseAdapter{

	public int getCount() {
		return Constant.atMeList.size();
	}

	public Object getItem(int position) {
		return Constant.atMeList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		AtMeViewHolder holder;
		if(convertView == null){
			LayoutInflater layoutInflater = LayoutInflater
			.from(IndexActivity.appref);
			convertView = layoutInflater.inflate(R.layout.atme_adapter, null);
			
			holder = new AtMeViewHolder();
			
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
			holder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
			holder.tvMention = (TextView) convertView.findViewById(R.id.tvMention);
			holder.tvSourceAndTime = (TextView) convertView.findViewById(R.id.tvTimeAndResouce);
			holder.ivUserHead = (ImageView) convertView.findViewById(R.id.ivUserHead);
			holder.ivMentionPic = (ImageView) convertView.findViewById(R.id.ivMentionPic);
			
			convertView.setTag(holder);
		}else{
			holder = (AtMeViewHolder) convertView.getTag();
		}
		Status status = Constant.atMeList.get(position);
		final User user = status.getUser();

		
		holder.tvUserName.setText(user.getScreenName());
		holder.tvLocation.setText(user.getLocation());
		holder.tvMention.setText(status.getText());
		holder.tvUserName.setTextColor(Color.BLACK);
		holder.tvLocation.setTextColor(Color.BLACK);
		holder.tvMention.setTextColor(Color.BLACK);
		holder.tvSourceAndTime.setText(Html.fromHtml(status.getSource().toString()));
		if (Constant.imageMap.containsKey(user.getId() + "") == false) {
			Constant.imageMap.put(user.getId() + "", null);
			Bitmap tempBitmap = WeiboUtils.getImage(user.getProfileImageURL());
			WeiboUtils.setImage(IndexActivity.appref.handler, holder.ivUserHead,
					tempBitmap);
			Constant.imageMap.put(user.getId() + "", tempBitmap);
		} else {
			holder.ivUserHead.setImageBitmap(Constant.imageMap.get(user.getId() + ""));
		}
		
		if (Constant.imageMap.containsKey(status.getThumbnail_pic() + "") == false) {
			Constant.imageMap.put(status.getThumbnail_pic() + "", null);
			Bitmap tempBitmap;
			try {
				tempBitmap = WeiboUtils.getImage(new URL(status.getThumbnail_pic()));
				WeiboUtils.setImage(IndexActivity.appref.handler, holder.ivMentionPic,
						tempBitmap);
				Constant.imageMap.put(status.getThumbnail_pic() + "", tempBitmap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
		} else {
			holder.ivMentionPic.setImageBitmap(Constant.imageMap.get(status.getThumbnail_pic() + ""));
		}
		return convertView;
	}

	static class AtMeViewHolder{
		TextView tvUserName;
		TextView tvLocation;
		TextView tvMention;
		TextView tvSourceAndTime;
		ImageView ivUserHead;
		ImageView ivMentionPic;
	}
}
