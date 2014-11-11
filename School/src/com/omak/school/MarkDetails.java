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

import com.omak.school.GeneralSms.sendSms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MarkDetails extends ActionBarActivity {
	
	ListView markList;
	ArrayList<Mark> marks;
	MarkAdapter adapter;
	Student s;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mark_details_layout);
		s = Student.getStudentDetails(MarkDetails.this,getIntent().getExtras().getString("ID"));
		TextView tx = (TextView)findViewById(R.id.std_name);
		tx.setText(s.firstName + " " + s.lastName);
		markList = (ListView)findViewById(R.id.mark_list);
		marks = new ArrayList<Mark>();
		adapter = new MarkAdapter(MarkDetails.this, R.id.mark_list, marks);
		markList.setAdapter(adapter);
	}
	
	public void onButtonClick(View v) {

		switch (v.getId()) {
		case R.id.add_mark:
			Mark m = new Mark();
			EditText edtSbj = (EditText)findViewById(R.id.sbj);
			EditText edtMrk = (EditText)findViewById(R.id.mark);
			if(edtSbj.getText().length() > 0 && edtMrk.getText().length() > 0) {
				m.subject = edtSbj.getText().toString();
				m.mark = edtMrk.getText().toString();
				marks.add(m);
				adapter.notifyDataSetChanged();
				edtSbj.setText("");
				edtMrk.setText("");
			}
			break;
			
		case R.id.mark_send:
			if(AttendenceActivity.checkConnectedFlags(MarkDetails.this)) {
				sendSms();
			} else {
				Toast.makeText(MarkDetails.this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
	
	private void sendSms() {
		String msg = s.firstName + " " + s.lastName + " scored ";
		for(int i = 0; i < marks.size(); i++) {
			if(i == marks.size()-1) {
				msg = msg + marks.get(i).subject + "-" + marks.get(i).mark;
			} else {
				msg = msg + marks.get(i).subject + "-" + marks.get(i).mark + ",";
			}
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		msg = msg +" on "+ df.format(c.getTime()) + "exam at Alpha Entrance Academy";
		/*String sms = "text=" + msg+"&api_token=" + AttendenceActivity.token+ 
				"&sender_id=FALERT&msisdn=" + (s.contactNumber);*/
		
		String sms = "msg=" + msg +"&to=" +
				(s.contactNumber);
		
		String query = "";
		try {
			final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
			query = Uri.encode(sms, ALLOWED_URI_CHARS);
//			query = URLEncoder.encode(sms, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new sendSms().execute(AttendenceActivity.smsUrl + query);
	}
	
	public class sendSms extends AsyncTask<String, Void, String> {

		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MarkDetails.this);
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
