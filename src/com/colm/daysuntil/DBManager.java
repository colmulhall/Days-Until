package com.colm.daysuntil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager
{
	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE = "event_title";
	public static final String KEY_DATE = "date";
	
	public static final String DATABASE_NAME = "Days Until Database";
	public static final String DATABASE_TABLE = "Events";
	public static final int DATABASE_VERSION = 5;
	
	//create database table
	private static final String SCRIPT_CREATE_DATABASE =
			"create table " + DATABASE_TABLE + " ("
			+ KEY_ID + " integer primary key autoincrement, "
			+ KEY_TITLE + " text not null,"
			+ KEY_DATE + " integer);";
	
	private Context context;
	private DBHelper DBHelper;
	private SQLiteDatabase db;

	public DBManager(Context ctx)
	{
		context = ctx;
	}
	
	//beginning of helper class **************************************************************************
	public class DBHelper extends SQLiteOpenHelper 
	{
		public DBHelper(Context context, String name, CursorFactory factory, int version)
		{
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			 db.execSQL(SCRIPT_CREATE_DATABASE);
		}
		
		//this will be called if I make a change to the database and give it a new version number
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			onCreate(db);
		}
	}
	//end of helper class ***********************************************************************************
	
	public DBManager openToRead() throws SQLException 
	{
		DBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = DBHelper.getReadableDatabase();
		return this;
	}
	
	public DBManager openToWrite() throws SQLException 
	{
		DBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	//close the database
	public void close()
	{
		DBHelper.close();
	}
	
	//insert a new item into the database
	public long insert(String title, String date)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_TITLE, title);
		contentValues.put(KEY_DATE, date);
		
		return db.insert(DATABASE_TABLE, null, contentValues);
	}
	
	//edit an item in the database
	public boolean update(int id, String title, String date)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_TITLE, title);
		contentValues.put(KEY_DATE, date);
		
		return db.update(DATABASE_TABLE, contentValues, KEY_ID + " = " + id, null) > 0;
	}
	
	//delete everything from the database
	public int deleteAll()
	{
		return db.delete(DATABASE_TABLE, null, null);
	}
	
	//delete the database 
	public void deleteDatabase()
	{
		context.deleteDatabase(DATABASE_NAME);
	}
	
	//queue the items in the database
	public Cursor queueAll()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_TITLE,
				KEY_DATE};
		Cursor cursor = db.query(DATABASE_TABLE, columns,
		  null, null, null, null, null);
	
		return cursor;
	}
	
	//order the list by the date in descending order
	public Cursor orderList()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_TITLE,
				KEY_DATE};
		
		Cursor cursor = db.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE + " DESC");
		
		return cursor;
	}

	//getters for each item in the database *********************************************************
	public String getEvent(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"event_title"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getDate(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"date"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getDaysUntil(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"days_until"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	//***************************************************************************************************
	
	//delete a particular event, based on its passed in ID
	public void deleteEvent(int num)
	{
		db.delete(DATABASE_TABLE, KEY_ID + " = " + num, null);
	}
	
}