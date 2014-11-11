package com.omak.school;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
 
public class CustomCursorAdapter extends CursorAdapter {
 
    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }
 
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.single_row_item, parent, false);
 
        return retView;
    }
 
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
 
        TextView textViewPersonName = (TextView) view.findViewById(R.id.name);
        String name  = cursor.getString(cursor.getColumnIndex(Student.FIRST_NAME)) + cursor.getString(cursor.getColumnIndex(Student.LAST_NAME));
        textViewPersonName.setText(name);
 
        TextView textViewPersonPIN = (TextView) view.findViewById(R.id.parents);
        String parent = "S% " + cursor.getString(cursor.getColumnIndex(Student.FATHER_NAME));
        textViewPersonPIN.setText(parent);
    }
}
