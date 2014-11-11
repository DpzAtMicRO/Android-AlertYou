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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AttendenceActivity extends ActionBarActivity {

	ArrayList<String> classList;
	ListView list;
	StudentAdapter adapter;
	ArrayList<Student> studentList,absenabsenceList;
	AlertDialog dialog;
	EditText number;
	EditText msg;
	TextView name;
	private NetworkReceiver receiver = new NetworkReceiver();
	public static String smsUrl = "http://msg.icelab.in/sendsms?uname=alphasms&pwd=alphasms&senderid=ALPHAE&route=T&";
	public static String token = "09861afa-e180-11e3-9745-26a92508be09";
	String className;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendence_layout);
		Spinner spClass = (Spinner) findViewById(R.id.division_spinner);
		list = (ListView) findViewById(R.id.student_list);
		studentList = new ArrayList<Student>();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		this.registerReceiver(receiver, filter);

		classList = Student.getDistinctClass(AttendenceActivity.this);
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
				className = classList.get(position);
				studentList = Student.getAllStudentListInClass(
						AttendenceActivity.this, classList.get(position));
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						adapter = new StudentAdapter(AttendenceActivity.this,
								R.id.student_list, studentList, true);
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
				studentList.get(position).attStatus = !studentList
						.get(position).attStatus;
				adapter.notifyDataSetChanged();
			}
		});
	}

	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.finish:
			absenabsenceList = new ArrayList<Student>();
			for(int i = 0; i < studentList.size(); i++) {
				if(!studentList.get(i).attStatus) {
					absenabsenceList.add(studentList.get(i));
				}
			}
			if(checkConnectedFlags(AttendenceActivity.this)) {
				if(absenabsenceList.size() > 0) {
					smsView();
				}
			} else {
				Toast.makeText(AttendenceActivity.this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.inform:
			absenabsenceList = new ArrayList<Student>();
			for(int i = 0; i < studentList.size(); i++) {
				if(!studentList.get(i).attStatus) {
					absenabsenceList.add(studentList.get(i));
				}
			}
			if(checkConnectedFlags(AttendenceActivity.this)) {
				if(absenabsenceList.size() > 0) {
					Intent intent = new Intent(AttendenceActivity.this, InformActivity.class);
					intent.putExtra("Count", absenabsenceList.size());
					intent.putExtra("ClassName", className);
					startActivity(intent);
					finish();
				}
			} else {
				Toast.makeText(AttendenceActivity.this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
			}
			break;

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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			this.unregisterReceiver(receiver);
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
	
	public class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			
			if (networkInfo != null
					&& networkInfo.isConnected()) {
				Toast.makeText(context, "Wifi connected", Toast.LENGTH_SHORT)
						.show();
				if(absenabsenceList != null && absenabsenceList.size() > 0) smsView();
			}
		}
	}
	
	public void smsView() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout forgotLayout = (RelativeLayout) inflater.inflate(R.layout.sms_pop_up_layout, null,false);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = (int) (metrics.widthPixels * 0.75);
		int height = (int) (metrics.heightPixels * 0.50);
		forgotLayout.setMinimumHeight(height);
		forgotLayout.setMinimumWidth(width);
		alertDialog.setView(forgotLayout);
		dialog = alertDialog.create();
		dialog.setCanceledOnTouchOutside(true);
		Button send = (Button)forgotLayout.findViewById(R.id.send);
		Button cancel = (Button)forgotLayout.findViewById(R.id.cancel);
		number = (EditText)forgotLayout.findViewById(R.id.send_number);
		msg = (EditText)forgotLayout.findViewById(R.id.send_msg);
		name = (TextView)forgotLayout.findViewById(R.id.send_name);
		number.setText(absenabsenceList.get(0).contactNumber);
		name.setText(absenabsenceList.get(0).firstName);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		msg.setText(absenabsenceList.get(0).firstName + " is absent for the class conducted at Alpha Entrance Academy on " + df.format(c.getTime()));
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(msg.getText().length() > 0 && number.getText().length() > 0) {
//					String sms = "text=" + msg.getText().toString()+"&sender_id=FALERT&msisdn=" +
//							(number.getText().toString())+"&api_token=" +token;
					String sms = "msg=" + msg.getText().toString()+"&to=" +
							(number.getText().toString());
					String query = "";
					try {
						final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
						query = Uri.encode(sms, ALLOWED_URI_CHARS);
//						query = URLEncoder.encode(sms, "utf-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
					new sendSms().execute(smsUrl + query);
					absenabsenceList.remove(0);
					dialog.dismiss();
				} else {
					Toast.makeText(AttendenceActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				absenabsenceList.remove(0);
				if(absenabsenceList.size() > 0) {
					msg.setText("");
					number.setText(absenabsenceList.get(0).contactNumber);
					name.setText(absenabsenceList.get(0).firstName);
				} else {
					dialog.dismiss();
				}
			}
		});
		
		dialog.show();
	}
	
	public class sendSms extends AsyncTask<String, Void, String> {

		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(AttendenceActivity.this);
			progressDialog.setMessage("Sending...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			Log.e("test", params[0]);
			return httpGet(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(absenabsenceList.size() > 0) {
				smsView();
			} else {
				finish();
			}
		}
		
	}
}
