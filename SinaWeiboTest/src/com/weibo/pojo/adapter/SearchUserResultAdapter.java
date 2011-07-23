package com.weibo.pojo.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.weibo.R;
import com.weibo.activity.SearchActivity;
import com.weibo.utils.Constant;

import weibo4android.User;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchUserResultAdapter extends BaseAdapter {

	List<User> lsu;
	LayoutInflater inflater;

	public SearchUserResultAdapter(List<User> lsu) {
		this.lsu = lsu;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsu.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lsu.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {

		User user = lsu.get(pos);
		if (user == null) {
			Log.v("TAG", "no user");
			return null;
		}

		ViewHolder holder;
		if (view == null) {
			inflater = SearchActivity.appref.getLayoutInflater();
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.search_user, null);
			holder.ivUserHead = (ImageView) view.findViewById(R.id.ivUserHead);
			holder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
			holder.tvLoc = (TextView) view.findViewById(R.id.tvLocation);
			holder.tvStatus = (TextView) view.findViewById(R.id.tvStatusText);
			holder.btnFans = (TextView) view.findViewById(R.id.btnFans);
			holder.btnFollowers = (TextView) view
					.findViewById(R.id.btnFollowers);
			holder.tvCreateAt = (TextView) view.findViewById(R.id.tvCreateAt);
			holder.tvSource = (TextView) view.findViewById(R.id.tvSource);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (Constant.imageMap.containsKey(user.getProfileImageURL().toString()) == false) {
			Constant.sit.pushImageTask(user.getProfileImageURL(),
					holder.ivUserHead);
		} else {
			holder.ivUserHead.setImageBitmap(Constant.imageMap.get(user
					.getProfileImageURL().toString() + ""));
		}

		holder.tvUserName.setText(user.getScreenName());
		holder.tvStatus.setText(user.getStatusText());
		holder.tvLoc.setText(user.getLocation());
		holder.btnFans.setText(SearchActivity.appref.getResources().getString(
				R.string.friend_fans)
				+ user.getFollowersCount() + "");
		holder.btnFollowers.setText(SearchActivity.appref.getResources()
				.getString(R.string.friend_focus)
				+ user.getFavouritesCount()
				+ "");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm  ");
		holder.tvCreateAt.setText(sdf.format(user.getCreatedAt()));
		holder.tvSource.setText(Html.fromHtml(user.getStatusSource()));

		return view;
	}

}

class ViewHolder {

	ImageView ivUserHead;
	TextView tvUserName;
	TextView tvLoc;
	TextView tvStatus;
	TextView btnFans;
	TextView btnFollowers;
	TextView tvCreateAt;
	TextView tvSource;

}
