package com.omak.school;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GeneralSms extends ActionBarActivity {

	ArrayList<String> classList;
	ArrayList<Student> studentList;
	Spinner spClass;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_sms_layout);
		spClass = (Spinner) findViewById(R.id.division_spinner);
		classList = new ArrayList<String>();
		classList = Student.getDistinctClass(GeneralSms.this);
		studentList = new ArrayList<Student>();
		if (classList.size() > 0) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					classList);
			spClass.setAdapter(spinnerArrayAdapter);
		}
		

		spClass.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				studentList = Student.getAllStudentListInClass(GeneralSms.this,classList.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		classList = Student.getDistinctClass(GeneralSms.this);
		if (classList.size() > 0) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					classList);
			spClass.setAdapter(spinnerArrayAdapter);
		}
	}

	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.send:
			EditText msg = (EditText)findViewById(R.id.send_msg);
			if(msg.getText().length() > 0 && studentList.size() > 0) {
				String sms = "msg=" + msg.getText().toString();
				for(int i = 0; i < studentList.size(); i++) {
					if(i == 0) {
						sms = sms + "&to=" +
							(studentList.get(i).contactNumber) ;
					} else {
						sms = sms + "," +
								(studentList.get(i).contactNumber) ;
					}
				}
				if(AttendenceActivity.checkConnectedFlags(GeneralSms.this)) {
					String query = "";
					try {
						final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
						query = Uri.encode(sms, ALLOWED_URI_CHARS);
//						query = URLEncoder.encode(sms, "utf-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
					new sendSms().execute(AttendenceActivity.smsUrl + query);
				} else {
					Toast.makeText(GeneralSms.this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(GeneralSms.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.cancel:

			break;

		}
	}
	
	public class sendSms extends AsyncTask<String, Void, String> {

		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(GeneralSms.this);
			progressDialog.setMessage("Sending...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			return httpGet(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			
		}
		
	}
	
	public static String httpGet(String url) {
		HttpParams httpparameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpparameters, 15000);
		HttpConnectionParams.setSoTimeout(httpparameters, 10000);
		
		HttpClient httpClient = new DefaultHttpClient(httpparameters);
		Log.i("url", ""+url);
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = httpClient.execute(get);
			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return "";

	}
}
