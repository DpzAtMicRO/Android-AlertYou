package com.omak.school;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.omak.db.ContentProviderDb;

public class StaffModel {
	
	public static final String TABLE_STAFF_DB = "TABLE_STAFF_DB";
	
	public String _id;
	public static final String ID = "_id";
	
	public String firstName;
	public static final String FIRST_NAME = "firstName";
	
	public String lastName;
	public static final String LAST_NAME = "lastName";
	
	public String address;
	public static final String ADDRESS = "address";
	
	public String contactNumber;
	public static final String NUMBER = "contactNumber";
	
	
	public static final String createStaffDb() {
		StringBuilder createStatment = new StringBuilder("CREATE TABLE ").append(TABLE_STAFF_DB).append(" (").append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(FIRST_NAME).append(" TEXT,").append(LAST_NAME).append(" TEXT,")
				.append(ADDRESS).append(" TEXT,").append(NUMBER).append(" TEXT").append(")");
		return createStatment.toString();
	}

//	private static final String[] profileDbProjections = new String[] { ID, PROFILE_ID, PROFILE_NAME, PROFILE_TYPE, CHECKIN, RUNS, USER_TYPE, IMAGE_URL, IMAGE_THUMBNAIL_URL};

	private static ContentValues convertToContentValues(StaffModel stud) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(FIRST_NAME, stud.firstName);
		contentvalues.put(LAST_NAME, stud.lastName);
		contentvalues.put(ADDRESS, stud.address);
		contentvalues.put(NUMBER, stud.contactNumber);
		return contentvalues;
	}

	private static StaffModel getValueFromCursor(Cursor cursor) {
		StaffModel model = new StaffModel();
		model._id = cursor.getString(cursor.getColumnIndex(ID));
		model.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME));
		model.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME));
		model.address = cursor.getString(cursor.getColumnIndex(ADDRESS));
		model.contactNumber = cursor.getString(cursor.getColumnIndex(NUMBER));
		
		return model;
	}

	public static final long insertStaff(Context context, StaffModel stud) {
		ContentValues initialvalues = convertToContentValues(stud);
		Uri contentUri = Uri.withAppendedPath(ContentProviderDb.CONTENT_URI, TABLE_STAFF_DB);
		Uri resultUri = context.getContentResolver().insert(contentUri, initialvalues);
		return Long.parseLong(ContentProviderDb.getPath(resultUri));
	}
	
	public static ArrayList<StaffModel> getAllStaffList(Context context) {
		ArrayList<StaffModel> staffList = new ArrayList<StaffModel>();
		Uri contentUri = Uri.withAppendedPath(ContentProviderDb.CONTENT_URI, TABLE_STAFF_DB);
		Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				staffList.add(getValueFromCursor(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return staffList;
	}
	
	
	public static final long updateDetails(Context context,StaffModel s) {
		ContentValues initialvalues = convertToContentValues(s);
		Uri contentUri = Uri.withAppendedPath(ContentProviderDb.CONTENT_URI, TABLE_STAFF_DB);
		int resultUri = context.getContentResolver().update(contentUri, initialvalues, ID+"=?", new String[] {s._id});
		return resultUri;
	}
	
	public static StaffModel getStaffDetails(Context context,String id) {
		StaffModel staff = new StaffModel();
		Uri contentUri = Uri.withAppendedPath(ContentProviderDb.CONTENT_URI, TABLE_STAFF_DB);
		String selection = ID+ " = '" +id+"'";
		Cursor cursor = context.getContentResolver().query(contentUri, null, selection, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			staff = getValueFromCursor(cursor);
		}
		cursor.close();
		return staff;
	}
	
	public static int deleteStaff(Context context,StaffModel s) {
		Uri contentUri = Uri.withAppendedPath(ContentProviderDb.CONTENT_URI, TABLE_STAFF_DB);
		int resultUri = context.getContentResolver().delete(contentUri, ID+"=?", new String[] {s._id});
		return resultUri;
	}

}
