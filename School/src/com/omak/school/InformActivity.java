package com.omak.school;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InformActivity extends Activity {
	
	ArrayList<StaffModel> managers = new ArrayList<StaffModel>();
	ArrayList<String> names = new ArrayList<String>();
	Spinner spClass;
	EditText msg;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inform);
		spClass = (Spinner) findViewById(R.id.manager_spinner);
		msg = (EditText) findViewById(R.id.sms);
		int c = getIntent().getIntExtra("Count", 0);
		String className = getIntent().getStringExtra("ClassName");
		/*if(c == 1) {
			msg.setText(c+" student is absent in " + className);
		} else {
			msg.setText(c+" students are absent in " + className);
		}*/
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		msg.setText("Number of students absent at HSSTUP "+ c+", HSSTHS 0" + " on "+  df.format(cal.getTime()));
		managers = StaffModel.getAllStaffList(InformActivity.this);
		
		for(int i = 0; i < managers.size(); i++) {
			names.add(managers.get(i).firstName);
		}
		if (names.size() > 0) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					names);
			spClass.setAdapter(spinnerArrayAdapter);
		}

	}
	
	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.send:
			int p = spClass.getSelectedItemPosition();
			if(checkConnectedFlags(InformActivity.this)) {
				if(msg.getText().length() > 0 && managers.size() > 0) {
					/*String sms = "text=" + msg.getText().toString()+"&sender_id=FALERT&msisdn=" +
							(managers.get(p).contactNumber)+"&api_token=" +token;*/
					
					String sms = "msg=" + msg.getText().toString()+"&to=" +
							(managers.get(p).contactNumber);
					
					String query = "";
					try {
						final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
						query = Uri.encode(sms, ALLOWED_URI_CHARS);
//						query = URLEncoder.encode(sms, "utf-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
					new sendSms().execute(AttendenceActivity.smsUrl + query);
				}
				
			} else {
				Toast.makeText(InformActivity.this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
			}
			break;

		}
	}

	public class sendSms extends AsyncTask<String, Void, String> {

		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(InformActivity.this);
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
	
	public static boolean checkConnectedFlags(Activity act) {
        ConnectivityManager connMgr =
                (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
        	return true;
        }
        return false;
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
