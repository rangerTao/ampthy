package com.weibo.pojo.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import weibo4andriod.RetweetDetails;
import weibo4andriod.User;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.utils.Constant;

import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendsStatusAdapter extends BaseAdapter{
	
	URL urlString;

	public int getCount() {
		return Constant.friendsList.size();
	}


	public Object getItem(int position) {
		return Constant.friendsList.get(position);
	}


	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		User user = (User) getItem(position);
		ViewHolder holder;
		if(convertView == null){
			
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(IndexActivity.appref);
			convertView = inflater.inflate(R.layout.friendstimeline_adapter, null);
			
			holder.tvUserNameTextView = (TextView) convertView.findViewById(R.id.tvUserName);
			holder.tvUserLocationg = (TextView) convertView.findViewById(R.id.tvUserLocation);
			holder.tvUserDesc = (TextView) convertView.findViewById(R.id.tvUserDesc);
			holder.tvUserDesc.setVisibility(View.GONE);
			holder.tvUserStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.tvTimeCreate = (TextView)convertView.findViewById(R.id.tvTimeCreate);
			holder.tvSource = (TextView)convertView.findViewById(R.id.tvSource);
			holder.ivUserHead = (ImageView) convertView.findViewById(R.id.ivUserHead);
			
			holder.ivStatusImage = (ImageView) convertView.findViewById(R.id.ivThumbail);
			holder.ivStatusImage.setVisibility(View.GONE);
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
				IndexActivity.appref.getResources(), R.drawable.loading));
		if (Constant.imageMap.containsKey(user.getProfileImageURL().toString()) == false) {
			Constant.sit.pushImageTask(user.getProfileImageURL(), holder.ivUserHead);
		} else {
			holder.ivUserHead.setImageBitmap(Constant.imageMap.get(user.getProfileImageURL().toString() + ""));
		}
		holder.tvUserStatus.setText(user.getStatusText().toString());
		//tvSource.setText(status.getSource().toString());
		holder.tvSource.setText(Html.fromHtml(user.getStatusSource().toString()));
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm  ");
		holder.tvTimeCreate.setText(sdf.format(user.getCreatedAt()));
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
