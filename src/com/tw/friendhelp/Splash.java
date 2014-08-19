package com.tw.friendhelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					interrupt();
					finish();
					Intent act = new Intent(getApplicationContext(),
							Login.class);
					startActivity(act);
				}
			}
		};
		splashTread.start();
	}

	@Override
	public void onBackPressed() {
	};
}
