package com.duole.wizard.setup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper mInstance = null;

	/** 数据库名称 **/

	public static final String DATABASE_NAME = "wizard.db";

	/** 数据库版本号 **/
	private static final int DATABASE_VERSION = 1;

	/** 数据库SQL语句 添加一个表 **/
	private static final String NAME_TABLE_CREATE = "create table wizard("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "finished INTEGER DEFAULT 0,"
			+ "modified TEXT);";

	public static synchronized DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NAME_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
