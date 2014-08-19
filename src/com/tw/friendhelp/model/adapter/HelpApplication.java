package com.tw.friendhelp.model.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tw.friendhelp.Home;
import com.tw.friendhelp.service.DbConnect;

public class HelpApplication extends Application {

	private ProgressDialog progressDialog = null;
	private String userCredentials = null;
	private SharedPreferences sharedPreferences = null;

	private Activity activity = null;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public void signIn(final String userName, final String pwd)
			throws Exception {

		// sharedPreferences = PreferenceManager
		// .getDefaultSharedPreferences(this.activity);

		progressDialog = new ProgressDialog(HelpApplication.this);

		// userCredentials = sharedPreferences.getString("user_credentials",
		// null);

		// if (userCredentials != null) {

		class sendSignInData extends AsyncTask<Void, Void, JSONArray> {
			JSONArray jaa;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.setMessage("Please wait");
				progressDialog.show();
			}

			@Override
			protected JSONArray doInBackground(Void... arg0) {

				JSONObject jobj = new JSONObject();
				try {
					jobj.put("username", userName);
					jobj.put("password", pwd);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				List<NameValuePair> Login = new ArrayList<NameValuePair>(1);
				Login.add(new BasicNameValuePair("Log", jobj.toString()));
				JSONArray jarray = new DbConnect()
						.workingMethod("Login", Login);
				return jarray;

			}

			@Override
			protected void onPostExecute(JSONArray result) {
				Log.i("json", result.toString());
				super.onPostExecute(result);
				JSONObject jobj;
				try {
					jobj = result.getJSONObject(0);
					String s = jobj.getString("success");
					if (s.equals("true")) {
						Intent i = new Intent(HelpApplication.this, Home.class);
						startActivity(i);
					}
					String msg;
					// if (s.equals("true")) {
					// userId= jobj.getString("userid");
					// MarketApplication.editPreferences().putString("userId",
					// userId).commit();
					// //
					// TaxiGlobal.getInstance().setMsisdn(SplititApplication.preferences.getString("MSISDN",
					// ""));
					// Intent i = new Intent(Login.this, Checkout.class);
					// startActivity(i);
					// } else {
					// msg = jobj.getString("message");
					// etUserName.setText("");
					// final ConfirmDialog cd = new
					// ConfirmDialog(Login.this, null);
					// cd.setContents("Invalid details.", msg);
					// cd.cdpoitiveButton.setOnClickListener(new
					// OnClickListener() {
					// public void onClick(View v) {
					// cd.dismiss();
					// }
					// });
					// cd.show();
					// }
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (progressDialog.isShowing())
					progressDialog.dismiss();
			}
		}

		sendSignInData httpAsyncTask = new sendSignInData();
		httpAsyncTask.execute();

		// }

	}

}
