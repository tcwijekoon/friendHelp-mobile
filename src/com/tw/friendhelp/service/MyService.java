package com.tw.friendhelp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.tw.friendhelp.model.ConfirmDialog;

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

		JSONObject jobj = new JSONObject();
		try {
			jobj.put("user_id", 3);
			jobj.put("gps_lat", loc.getLatitude());
			jobj.put("gps_lon", loc.getLongitude());
			jobj.put("location_address", "Niungama");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		new UpdateUserLocation().execute(jobj.toString());

	}

	class UpdateUserLocation extends AsyncTask<String, Void, JSONArray> {
		JSONArray jaa;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONArray doInBackground(String... arg0) {
			List<NameValuePair> signup = new ArrayList<NameValuePair>(1);
			signup.add(new BasicNameValuePair("UpdateMyLocation", arg0[0]));

			JSONArray jarray = new DbConnect().workingMethod("UpdateMyLocation", signup);
			return jarray;

		}

		@Override
		protected void onPostExecute(JSONArray result) {
			Log.i("json", result.toString());
			super.onPostExecute(result);
			JSONObject jobj;
			try {
				jobj = result.getJSONObject(0);
				boolean success = jobj.getBoolean("success");
				String msg;
				if (success) {
					msg = jobj.getString("message");
				} else {
					msg = jobj.getString("message");
					final ConfirmDialog cd = new ConfirmDialog(MyService.this, null);
					cd.setContents("Sign up failed.", msg);
					cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							cd.dismiss();
						}
					});
					cd.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
