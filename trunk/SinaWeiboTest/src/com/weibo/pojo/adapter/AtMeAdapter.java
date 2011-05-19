package com.weibo.pojo.adapter;

import java.net.MalformedURLException;
import java.net.URL;

import weibo4andriod.Status;
import weibo4andriod.User;

import com.weibo.R;
import com.weibo.R.id;
import com.weibo.activity.AtMeActivity;
import com.weibo.activity.IndexActivity;
import com.weibo.utils.Constant;
import com.weibo.utils.WeiboUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AtMeAdapter extends BaseAdapter{

	public int getCount() {
		return AtMeActivity.atMeList.size();
	}

	public Object getItem(int position) {
		return AtMeActivity.atMeList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(AtMeActivity.appref);
		View view = layoutInflater.inflate(R.layout.atme_adapter, null);
		Status status = AtMeActivity.atMeList.get(position);
		final User user = status.getUser();
		TextView tvUserName = (TextView) view.findViewById(R.id.tvUserName);
		TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
		TextView tvMention = (TextView) view.findViewById(R.id.tvMention);
		TextView tvSourceAndTime = (TextView) view.findViewById(R.id.tvTimeAndResouce);
		ImageView ivUserHead = (ImageView) view.findViewById(R.id.ivUserHead);
		ImageView ivMentionPic = (ImageView) view.findViewById(R.id.ivMentionPic);
		
		tvUserName.setText(user.getScreenName());
		tvLocation.setText(user.getLocation());
		tvMention.setText(status.getText());
		tvSourceAndTime.setText(Html.fromHtml(status.getSource().toString()));
		if (Constant.imageMap.containsKey(user.getId() + "") == false) {
			Constant.imageMap.put(user.getId() + "", null);
			Bitmap tempBitmap = WeiboUtils.getImage(user.getProfileImageURL());
			WeiboUtils.setImage(IndexActivity.appref.handler, ivUserHead,
					tempBitmap);
			Constant.imageMap.put(user.getId() + "", tempBitmap);
		} else {
			ivUserHead.setImageBitmap(Constant.imageMap.get(user.getId() + ""));
		}
		
		if (Constant.imageMap.containsKey(status.getThumbnail_pic() + "") == false) {
			Constant.imageMap.put(status.getThumbnail_pic() + "", null);
			Bitmap tempBitmap;
			try {
				tempBitmap = WeiboUtils.getImage(new URL(status.getThumbnail_pic()));
				WeiboUtils.setImage(AtMeActivity.appref.handler, ivMentionPic,
						tempBitmap);
				Constant.imageMap.put(status.getThumbnail_pic() + "", tempBitmap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
		} else {
			ivUserHead.setImageBitmap(Constant.imageMap.get(user.getId() + ""));
		}
		return view;
	}

}
