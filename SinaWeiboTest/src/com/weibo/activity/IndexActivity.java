package com.weibo.activity;

import java.util.List;

import weibo4andriod.Status;
import weibo4andriod.Weibo4sina;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.weibo.R;
import com.weibo.daos.DBAdapter;
import com.weibo.utils.Contants;

public class IndexActivity extends Activity{

	String token;
	String tokenSecret;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.index_activity);
		
		initData();
		
		getFriends();
	}
	
	private void initData(){
		DBAdapter dba = new DBAdapter(this,Contants.dbName,Contants.dbVersion);
		dba.open();
		Cursor cr = dba.query(null, "", "", "", "", "");
		
		if(cr!=null){
			cr.moveToFirst();

			token = cr.getString(0);
			tokenSecret = cr.getString(1);
			
			SharedPreferences sPre = this.getSharedPreferences(Contants.app_name, MODE_WORLD_WRITEABLE);
			Editor editor = sPre.edit();
			editor.putString(Contants.token, token);
			editor.putString(Contants.tokenSecret, tokenSecret);
			editor.commit();
		}
		dba.close();

	}

	private void getFriends(){

        System.out.println("Showing public timeline.");
        try {

            // Other methods require authentication
        	Weibo4sina weibo = new Weibo4sina();
            weibo.setOAuthConsumer(token, tokenSecret);
            List<Status> statuses = weibo.getPublicTimeline();
            for (Status status : statuses) {
            	Log.v("TAG", status.getUser().getName() + ":" +
                                   status.getText());
            }

            statuses = weibo.getFriendsTimeline();
            Log.v("TAG", "------------------------------");
            Log.v("TAG", "Showing friends timeline.");
            for (Status status : statuses) {
            	Log.v("TAG", status.getUser().getName() + ":" +
                                   status.getText());
            }
            statuses = weibo.getUserTimeline("1230928585");
            Log.v("TAG", "------------------------------");
            Log.v("TAG", "Showing timeline.");
            for (Status status : statuses) {
            	Log.v("TAG", status.getUser().getName() + ":" +
                                   status.getText());
            }
        } catch (WeiboException te) {
        	Log.v("TAG", "Failed to get timeline: " + te.getMessage());
            System.exit( -1);
        }
    
	}
	
	private class FriendTask extends AsyncTask{


		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}


		@Override
		protected Object doInBackground(Object... arg0) {
			getFriends();
			return null;
		}
		
	}
	
}
