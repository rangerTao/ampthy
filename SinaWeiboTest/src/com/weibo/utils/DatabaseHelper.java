package com.weibo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.weibo.pojo.DBColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context) {
		super(context, Constant.dbName, null, Constant.dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Constant.tableName + " (" + DBColumns.ID
				+ " TEXT PRIMARY KEY," + DBColumns.TOKEN + " TEXT,"
				+ DBColumns.TOKENSECRET + " TEXT," + DBColumns.ACCESSTOKEN
				+ " TEXT, " + DBColumns.ACCESSTOKENSECRET + " TEXT, " + DBColumns.userID + " TEXT, "
				+ DBColumns.ScreenName + " TEXT, " + DBColumns.SITE + " TEXT, "
				+ DBColumns.USERHEADURI + " BLOB, " + DBColumns.USERHEADURL
				+ " TEXT, " + DBColumns.CREATE_TIME + " TEXT,"
				+ DBColumns.MODIFY_TIME + " TEXT" + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TAG", "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + Constant.tableName);
		onCreate(db);
	}

}
