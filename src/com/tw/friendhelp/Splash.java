package com.tw.friendhelp;

import com.tw.friendhelp.model.HelpApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

public class Splash extends Activity {

	private SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					if (sharedPreferences.contains("user_credentials")) {
						runOnUiThread(new Runnable() {
							public void run() {
								try {
									HelpApplication mumblerChat = (HelpApplication) Splash.this.getApplication();
									mumblerChat.signIn(Splash.this);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						interrupt();
						finish();
						Intent act = new Intent(getApplicationContext(), Login.class);
						startActivity(act);
					}
				}
			}
		};
		splashTread.start();
	}

	@Override
	public void onBackPressed() {
	};
}
