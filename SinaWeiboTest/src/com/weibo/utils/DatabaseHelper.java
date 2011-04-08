package com.weibo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.weibo.pojo.UserInfo.User;

public class DatabaseHelper extends SQLiteOpenHelper{

	public DatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context) {
		super(context, Contants.dbName, null, Contants.dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Contants.tableName + " (" + User.ID
				+ " TEXT PRIMARY KEY," + User.TOKEN + " TEXT,"
				+ User.TOKENSECRET + " TEXT," + User.CREATE_TIME
				+ " TEXT," + User.MODIFY_TIME + " TEXT" + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TAG", "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + Contants.tableName);
		onCreate(db);
	}


}
