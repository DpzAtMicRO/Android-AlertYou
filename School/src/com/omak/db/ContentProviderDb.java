package com.omak.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ContentProviderDb extends ContentProvider {
	
	private DatabaseHelper databaseHelper;
	public static final String AUTHORITY = "com.omak.android.contentProviderAuthorities";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	@Override
	public boolean onCreate() {
		databaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String where, String[] selectionArgs) {
		String table = getPath(uri);
		SQLiteDatabase dataBase = databaseHelper.getWritableDatabase();
		int result = dataBase.delete(table, where, selectionArgs);
		return result;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		Uri result = null;
		String table = getPath(uri);
		SQLiteDatabase dataBase = databaseHelper.getWritableDatabase();
		long value = dataBase.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
		result = Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		if(uri != null) {
			String table = getPath(uri);
			cursor = database.query(table, projection, selection, selectionArgs, null, null, sortOrder);
		} else {
			cursor = database.rawQuery(selection, selectionArgs);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
		int result = 0;
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		String table = getPath(uri);
		result = database.update(table, values, whereClause, whereArgs);
		return result;
	}
	
	public static String getPath(Uri uri) {
    	String value =null;
    	if(null!=uri){
        value = uri.getPath();  
        value = value.replace("/", "");
    	}
        return value;
    }

}
