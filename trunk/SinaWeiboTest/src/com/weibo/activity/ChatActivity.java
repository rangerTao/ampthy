package com.weibo.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4android.Comment;
import weibo4android.Paging;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.weibo.R;
import com.weibo.pojo.OAuthConstant;
import com.weibo.pojo.UserImpl;
import com.weibo.pojo.adapter.CommentsToMeAdapter;
import com.weibo.utils.Constant;

public class ChatActivity extends Activity implements OnItemClickListener {

	Weibo weibo = OAuthConstant.getInstance().getWeibo();

	ChatActivity appref;
	// Page index
	int page_index = 1;

	ListView lvComments;

	List<Comment> statuses = new ArrayList<Comment>();
	List<Long> idList = new ArrayList<Long>();
	Map<Long, Comment> comMap = new HashMap<Long, Comment>();
	
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		appref = this;
		super.onCreate(savedInstanceState);
		try {

			statuses = weibo.getCommentsByMe(new Paging(page_index));
			for (int i = 0; i < statuses.size(); i++) {
				idList.add(statuses.get(i).getId());
				comMap.put(statuses.get(i).getId(), statuses.get(i));
			}
			statuses = weibo.getCommentsToMe(new Paging(page_index));
			for (int i = 0; i < statuses.size(); i++) {
				idList.add(statuses.get(i).getId());
				comMap.put(statuses.get(i).getId(), statuses.get(i));
			}
			Collections.sort(idList, Collections.reverseOrder());

			setContentView(R.layout.friend_activity);
			lvComments = (ListView) findViewById(R.id.lvFriendListView);

			for (int i = 0; i < idList.size(); i++) {
				Constant.comList.add(comMap.get(idList.get(i)));
			}
			CommentsToMeAdapter ctma = new CommentsToMeAdapter();

			lvComments.setAdapter(ctma);

			lvComments.setOnItemClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Comment status = Constant.comList.get(position);

		Log.v("TAG", status.getId()+"");
		if (!(status.getUser().getId() + "").equalsIgnoreCase(UserImpl.getID())) {
			final EditText etReply = new EditText(this);
			etReply.setHint(R.string.text_hint);
			new AlertDialog.Builder(this).setTitle(appref.getResources().getString(R.string.reply_title)).setView(etReply)
					.setPositiveButton(R.string.passwd_set_confirm,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
										if(etReply.getText().toString().length() <= 0){
											showToast(appref.getResources().getString(R.string.toast_too_short));
											return;
										}else if(etReply.getText().toString().getBytes().length > 140){
											showToast(appref.getResources().getString(R.string.toast_too_long));
											return;
										}
										handler.post(new Runnable(){

											@Override
											public void run() {
												try {
													weibo.updateComment(URLEncoder.encode(etReply.getText().toString(),"UTF-8"), 
															status.getUser().getId()+"", status.getId()+"",0);
												} catch (UnsupportedEncodingException e) {
													
												} catch (WeiboException e) {
													showToast(appref.getResources().getString(R.string.neterror));
												}
												showToast(appref.getResources().getString(R.string.reply_success));												
											}
											
										});
								}
							}).setNegativeButton(R.string.passwd_set_cancel,
							null).show();
		}else{
			Toast.makeText(appref, appref.getResources().getString(R.string.toast_cannot_reply_to_self), 1000).show();
		}

	}
	
	private void showToast(final String in){
		handler.post(new Runnable(){

			@Override
			public void run() {
				Toast.makeText(appref, in, 2000).show();
			}
			
		});
	}

}
