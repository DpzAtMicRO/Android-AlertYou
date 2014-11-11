package com.omak.school;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class HomeScreen extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
	}

	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.manage_studnts:
			Intent intent = new Intent(HomeScreen.this, ManageStudentDetails.class);
			intent.putExtra("ToMark", false);
			startActivity(intent);
			break;

		case R.id.attendence:
			startActivity(new Intent(HomeScreen.this, AttendenceActivity.class));
			break;

		case R.id.general_message:
			startActivity(new Intent(HomeScreen.this, GeneralSms.class));
			break;

		case R.id.mark_details:
			Intent intent2 = new Intent(HomeScreen.this, ManageStudentDetails.class);
			intent2.putExtra("ToMark", true);
			startActivity(intent2);
			break;

		default:
			break;
		}

	}

}
