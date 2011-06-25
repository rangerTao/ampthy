package com.weibo.pojo.adapter;

import java.net.URL;
import java.text.SimpleDateFormat;

import weibo4andriod.Comment;
import weibo4andriod.DirectMessage;
import weibo4andriod.User;

import com.weibo.R;
import com.weibo.activity.IndexActivity;
import com.weibo.pojo.adapter.CommentsToMeAdapter.ViewHolder;
import com.weibo.utils.Constant;

import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MailToMeAdapter extends BaseAdapter{
	URL urlString;
	public int getCount() {
		return Constant.mailList.size();
	}

	public Object getItem(int arg0) {
		return Constant.mailList.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int arg0, View view, ViewGroup arg2) {
		DirectMessage com = (DirectMessage) getItem(arg0);
		User user = com.getSender();
		ViewHolder holder;
		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(IndexActivity.appref);
			view = inflater.inflate(R.layout.friendstimeline_adapter, null);
			holder = new ViewHolder();
			
			holder.tvUserNameTextView = (TextView) view.findViewById(R.id.tvUserName);
			holder.tvUserLocationg = (TextView) view.findViewById(R.id.tvUserLocation);
			holder.tvUserDesc = (TextView) view.findViewById(R.id.tvUserDesc);
			holder.tvUserDesc.setVisibility(View.GONE);
			holder.tvUserStatus = (TextView) view.findViewById(R.id.tvStatus);
			holder.tvTimeCreate = (TextView)view.findViewById(R.id.tvTimeCreate);
			holder.tvSource = (TextView)view.findViewById(R.id.tvSource);
			holder.ivUserHead = (ImageView) view.findViewById(R.id.ivUserHead);
			
			holder.ivStatusImage = (ImageView) view.findViewById(R.id.ivThumbail);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.tvUserStatus.setPadding(10, 5, 0, 0);
		holder.tvUserNameTextView.setText(user.getScreenName());
		holder.tvUserLocationg.setText(user.getLocation());
		
		urlString = user.getProfileImageURL();
		holder.ivUserHead.setImageBitmap(BitmapFactory.decodeResource(
				IndexActivity.appref.getResources(), R.drawable.loading));
		if (Constant.imageMap.containsKey(user.getProfileImageURL().toString()) == false) {
			Constant.sit.pushImageTask(user.getProfileImageURL(), holder.ivUserHead);
		} else {
			holder.ivUserHead.setImageBitmap(Constant.imageMap.get(user.getProfileImageURL().toString() + ""));
		}
		holder.tvUserStatus.setText(com.getText().toString());
		//tvSource.setText(status.getSource().toString());
		holder.tvSource.setVisibility(View.GONE);
		holder.ivStatusImage.setVisibility(View.GONE);
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm  ");
		holder.tvTimeCreate.setText(sdf.format(com.getCreatedAt()));
		holder.ivStatusImage.setVisibility(View.GONE);
		
		return view;
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
