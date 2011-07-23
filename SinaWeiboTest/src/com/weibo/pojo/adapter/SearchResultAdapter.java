package com.weibo.pojo.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.weibo.R;
import com.weibo.activity.SearchActivity;

import weibo4android.SearchResult;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends BaseAdapter{

	List<SearchResult> lsr;
	LayoutInflater inflater = SearchActivity.appref.getLayoutInflater();
	
	public SearchResultAdapter(List<SearchResult> in){
		lsr = in;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsr.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lsr.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		SearchResult sr = lsr.get(arg0);
		
		viewHolder holder;
		if(view == null){
			view = inflater.inflate(R.layout.search_result, null);
			holder = new viewHolder();
			
			holder.tvFromUser = (TextView)view.findViewById(R.id.tvFromUser);
			holder.tvText = (TextView)view.findViewById(R.id.tvResultText);
			holder.tvCreateAt = (TextView) view.findViewById(R.id.tvCreateAt);
			holder.tvSource = (TextView) view.findViewById(R.id.tvSource);

			view.setTag(holder);
		}else{
			holder = (viewHolder) view.getTag();
		}
		
		
		holder.tvFromUser.setText(sr.getFromUser());
		holder.tvText.setText(sr.getText());
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm  ");
		holder.tvCreateAt.setText(sdf.format(sr.getCreatedAt()));
		holder.tvSource.setText(Html.fromHtml(sr.getSource().toString()));
		
		
		return view;
	}
	
	class viewHolder{
		
		TextView tvFromUser;
		TextView tvText;
		TextView tvCreateAt;
		TextView tvSource;
		
	}

}
