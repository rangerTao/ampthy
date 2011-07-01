package com.weibo.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import weibo4android.Comment;
import weibo4android.Paging;
import weibo4android.User;
import weibo4android.Weibo;
import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.UserImpl;
import com.weibo.pojo.adapter.CommentsToMeAdapter;
import com.weibo.utils.Constant;

public class ChatActivity extends Activity{

	Weibo weibo = OAuthConstant.getInstance().getWeibo();
	
	//Page index
	int page_index = 1;
	
	ListView lvComments;
	
	List<Comment> statuses = new ArrayList<Comment>();
	List<Long> idList = new ArrayList<Long>();
	Map<Long, Comment> comMap = new HashMap<Long, Comment>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		try{
			
			statuses = weibo.getCommentsByMe(new Paging(page_index));
			for(int i =0 ;i<statuses.size();i++){
				idList.add(statuses.get(i).getId());
				comMap.put(statuses.get(i).getId(), statuses.get(i));
			}
			Log.v("TAG", statuses.get(0).toString());
			statuses = weibo.getCommentsToMe(new Paging(page_index));
			for(int i =0 ;i<statuses.size();i++){
				idList.add(statuses.get(i).getId());
				comMap.put(statuses.get(i).getId(), statuses.get(i));
			}
			Log.v("TAG", statuses.get(0).toString());
			Collections.sort(idList);
			
			setContentView(R.layout.friend_activity);
			lvComments = (ListView)findViewById(R.id.lvFriendListView);
			
			for(int i = 0; i< idList.size();i++){
				Constant.comList.add(comMap.get(idList.get(i)));
			}
			CommentsToMeAdapter ctma = new CommentsToMeAdapter();
			
			lvComments.setAdapter(ctma);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
	
}
