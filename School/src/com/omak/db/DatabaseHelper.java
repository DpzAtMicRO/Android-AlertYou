package com.omak.db;

import com.omak.school.StaffModel;
import com.omak.school.Student;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "school.db";
	public static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(Student.createStudentDb());
		database.execSQL(StaffModel.createStaffDb());
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		dropAllTables(database);
		onCreate(database);
	}
	
	private void dropAllTables(SQLiteDatabase database) {
		database.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_STUDENT_DB);
		database.execSQL("DROP TABLE IF EXISTS " + StaffModel.TABLE_STAFF_DB);
	}

}
