package com.omak.school;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEditStudent extends ActionBarActivity {
	Student s;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_layout);
		if(getIntent().getExtras() != null) {
			s = Student.getStudentDetails(AddEditStudent.this,getIntent().getExtras().getString("ID"));
			findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
			setstudentDetails();
		} else {
			s = new Student();
		}
	}
	
	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.btn_add:
			addstudentDetails();
			break;
			
		case R.id.btn_delete:
			Student.deleteStudent(AddEditStudent.this, s);
			finish();
			break;
			
		}
	}
	
	private void addstudentDetails() {
		EditText edt = (EditText) findViewById(R.id.edt_roll);
		s.rollNo = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_first);
		s.firstName = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_last);
		s.lastName = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_father);
		s.fatherName = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_mother);
		s.motherName = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_address);
		s.address = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_contact);
		s.contactNumber = edt.getText().toString();
		
		Spinner spCls = (Spinner)findViewById(R.id.sp_class);
		Spinner spDiv = (Spinner)findViewById(R.id.sp_div);
		String[] values = getResources().getStringArray(R.array.class_numbers);
		String[] values2 = getResources().getStringArray(R.array.divs);
		s.classDivision = values[spCls.getSelectedItemPosition()] + ":" + values2[spDiv.getSelectedItemPosition()];
		if(s.contactNumber.length() > 0 ) {
			if(s._id == null) {
				Student.insertStudent(AddEditStudent.this, s);
				finish();
			} else {
				Student.updateDetails(AddEditStudent.this, s);
				finish();
			}
		} else {
			Toast.makeText(AddEditStudent.this, "Contact Number is Mandatory", Toast.LENGTH_LONG).show();
		}
		
	}
	
	private void setstudentDetails() {
		EditText edt = (EditText) findViewById(R.id.edt_roll);
		edt.setText(s.rollNo);
		
		edt = (EditText) findViewById(R.id.edt_first);
		edt.setText(s.firstName);
		
		edt = (EditText) findViewById(R.id.edt_last);
		edt.setText(s.lastName);
		
		edt = (EditText) findViewById(R.id.edt_father);
		edt.setText(s.fatherName);
		
		edt = (EditText) findViewById(R.id.edt_mother);
		edt.setText(s.motherName);
		
		edt = (EditText) findViewById(R.id.edt_address);
		edt.setText(s.address);
		
		edt = (EditText) findViewById(R.id.edt_contact);
		edt.setText(s.contactNumber);
		
		Spinner spCls = (Spinner)findViewById(R.id.sp_class);
		Spinner spDiv = (Spinner)findViewById(R.id.sp_div);
		String[] values2 = getResources().getStringArray(R.array.divs);
		List<String> wordList = Arrays.asList(values2);  
		
		String[] values1 = getResources().getStringArray(R.array.class_numbers);
		List<String> classList = Arrays.asList(values1);
		String classDiv[] = s.classDivision.split(":");
		
		spCls.setSelection(classList.indexOf(classDiv[0]));
		spDiv.setSelection(wordList.indexOf(classDiv[1]));
		
		Button btn = (Button)findViewById(R.id.btn_add);
		btn.setText("UPDATE");
		
	}

}
