package com.omak.school;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {
	
	private static Handler splashHandler;
	private static boolean isForground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_splash);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		isForground = true;
		splashHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (isForground) {
					startActivity(new Intent(Splash.this,
							HomeScreen.class));
					finish();
				}
			}
		};
		splashHandler.sendMessageDelayed(new Message(),1 * 1000);

	}

	@Override
	public void onPause() {
		super.onPause();
		isForground = false;
	}

}
