package com.hunter123321000.sqllite.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private final static int _DBVersion = 1; // <-- ª©¥»
	private final static String _DBName = "SampleList.db"; // <-- db name
	private final static String _TableName = "MySample"; // <-- table name

	public DBHelper(Context context) {
		super(context, _DBName, null, _DBVersion);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( "
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "_TITLE VARCHAR(50), " + "_CONTENT TEXT,"
				+ "_KIND VARCHAR(10)" + ");";
		arg0.execSQL(SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		final String SQL = "DROP TABLE " + _TableName;
		arg0.execSQL(SQL);	
	}

}
