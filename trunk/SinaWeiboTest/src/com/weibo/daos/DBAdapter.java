package com.weibo.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.weibo.pojo.User;
import com.weibo.utils.Contants;
import com.weibo.utils.DatabaseHelper;

public class DBAdapter {

	DatabaseHelper dbHelper;
	SQLiteDatabase db;

	public DBAdapter(Context ctx, String base_name, int version) {
		dbHelper = new DatabaseHelper(ctx);
	}

	public DBAdapter open() {
		db = dbHelper.getWritableDatabase();

		Log.v("TAG", "DB OPEN");
		return this;
	}

	public boolean close() {
		Log.v("TAG", "DB close");
		db.close();
		return true;
	}

	public long insertData(String id, String token, String tokenSecret,String access, String accessSecret) {
		ContentValues cv = new ContentValues();
		cv.put(User.ID, id);
		cv.put(User.TOKEN, token);
		cv.put(User.TOKENSECRET, tokenSecret);
		cv.put(User.ACCESSTOKEN, access);
		cv.put(User.ACCESSTOKENSECRET, accessSecret);
		Cursor cr = query(null,User.ID,id,null,null,null);
		if(cr != null && cr.getCount() > 0){
			Log.v("TAG", "Data Exists");
			return 0;
		}else{
			return db.insert(Contants.tableName, "_id", cv);
		}
		
	}

	public boolean deleteData(String where, String[] whereargs) {
		db.delete(Contants.tableName, where, whereargs);
		return true;
	}

	public Cursor query(String[] columns, String selection,
			String selectionArgs, String groupBy, String having, String orderBy) {
		return db.query(Contants.tableName, columns, null, null, "", "", "");
	}
}
