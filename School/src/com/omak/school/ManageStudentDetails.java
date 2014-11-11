package com.omak.school;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ManageStudentDetails extends ActionBarActivity {
	
	ArrayList<String> classList;
	ListView list;
	StudentAdapter adapter;
	ArrayList<Student> studentList;
	Spinner spClass;
	boolean toMark = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_student_list);
		spClass = (Spinner)findViewById(R.id.division_spinner);
		list = (ListView)findViewById(R.id.student_list);
		toMark = getIntent().getBooleanExtra("ToMark", false);
		classList = Student.getDistinctClass(ManageStudentDetails.this);
		if(classList.size() > 0) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classList);
			spClass.setAdapter(spinnerArrayAdapter);
		} else {
			Toast.makeText(ManageStudentDetails.this, "No Data Available", Toast.LENGTH_LONG).show();
		}
		
		spClass.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				studentList = Student.getAllStudentListInClass(ManageStudentDetails.this,classList.get(position));
				new Handler().post(new Runnable() {
		            @Override
		            public void run() {
		            	adapter = new StudentAdapter(ManageStudentDetails.this, R.id.student_list,studentList,false);
		                list.setAdapter(adapter);
		            }
		        });
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(toMark) {
					Intent intent = new Intent(ManageStudentDetails.this, MarkDetails.class);
					intent.putExtra("ID", studentList.get(position)._id);
					startActivity(intent);
				} else {
					Intent intent = new Intent(ManageStudentDetails.this, AddEditStudent.class);
					intent.putExtra("ID", studentList.get(position)._id);
					startActivity(intent);
				}
			}
		});
	}
	
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		classList = Student.getDistinctClass(ManageStudentDetails.this);
		if(classList.size() > 0) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classList);
			spClass.setAdapter(spinnerArrayAdapter);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add_student:
	    	startActivity(new Intent(ManageStudentDetails.this, AddEditStudent.class));
	    	break;
	    case R.id.add_staff:
	    	startActivity(new Intent(ManageStudentDetails.this, AddEditStaff.class));
	    	break;
	    default:
	      break;
	    }

	    return true;
	 }

}
