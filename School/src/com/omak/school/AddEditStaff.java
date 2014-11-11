package com.omak.school;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditStaff extends ActionBarActivity {
	StaffModel s;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_staff_layout);
		if(getIntent().getExtras() != null) {
			s = StaffModel.getStaffDetails(AddEditStaff.this,getIntent().getExtras().getString("ID"));
			findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
			setstaffDetails();
		} else {
			s = new StaffModel();
		}
	}
	
	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.btn_add:
			addstaffDetails();
			break;
			
		case R.id.btn_delete:
			StaffModel.deleteStaff(AddEditStaff.this, s);
			finish();
			break;
			
		}
	}
	
	private void addstaffDetails() {
		
		EditText edt = (EditText) findViewById(R.id.edt_first);
		s.firstName = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_last);
		s.lastName = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_address);
		s.address = edt.getText().toString();
		
		edt = (EditText) findViewById(R.id.edt_contact);
		s.contactNumber = edt.getText().toString();
		
		if(s.contactNumber.length() > 0 ) {
			if(s._id == null) {
				StaffModel.insertStaff(AddEditStaff.this, s);
				finish();
			} else {
				StaffModel.updateDetails(AddEditStaff.this, s);
				finish();
			}
		} else {
			Toast.makeText(AddEditStaff.this, "Contact Number is Mandatory", Toast.LENGTH_LONG).show();
		}
		
	}
	
	private void setstaffDetails() {
		
		EditText edt = (EditText) findViewById(R.id.edt_first);
		edt.setText(s.firstName);
		
		edt = (EditText) findViewById(R.id.edt_last);
		edt.setText(s.lastName);
		
		
		edt = (EditText) findViewById(R.id.edt_address);
		edt.setText(s.address);
		
		edt = (EditText) findViewById(R.id.edt_contact);
		edt.setText(s.contactNumber);
		
		Button btn = (Button)findViewById(R.id.btn_add);
		btn.setText("UPDATE");
		
	}

}
