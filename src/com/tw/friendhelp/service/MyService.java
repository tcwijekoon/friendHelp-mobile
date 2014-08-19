package com.tw.friendhelp.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements ILocationListner {
	private static final String TAG = "MyService";
	MyLocation myLocation;

	// MediaPlayer player;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");

		myLocation = new MyLocation(this);
		if (myLocation.getLocation())
			myLocation.registerListner(this);

		// player = MediaPlayer.create(this, R.raw.braincandy);
		// player.setLooping(false); // Set looping
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		myLocation.cancelTimer();
		// player.stop();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");

		// LocationResult locationResult = new LocationResult() {
		// @Override
		// public void gotLocation(Location location) {
		// // Got the location!
		// // Toast.makeText(MyService.this,
		// // "lat : " + location.getLatitude(), Toast.LENGTH_LONG)
		// // .show();
		// // Toast.makeText(MyService.this,
		// // "lon : " + location.getLongitude(), Toast.LENGTH_LONG)
		// // .show();
		//
		// Log.d(TAG, "lat : " + location.getLatitude());
		// Log.d(TAG, "lon : " + location.getLongitude());
		// }
		// };
		// myLocation.getLocation(this, locationResult);
		// player.start();
	}

	@Override
	public void listenLocation(Location loc) {
		Log.d(TAG, "lat : " + loc.getLatitude());
		Log.d(TAG, "lon : " + loc.getLongitude());
	}
}
