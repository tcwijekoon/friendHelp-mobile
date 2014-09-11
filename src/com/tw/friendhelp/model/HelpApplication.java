package com.tw.friendhelp.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.tw.friendhelp.Home;
import com.tw.friendhelp.service.DbConnect;

public class HelpApplication extends Application {

	private ProgressDialog progressDlg = null;
	private String userCredentials = null;
	private SharedPreferences sharedPreferences = null;

	private Activity activity = null;
	String userName, pwd;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public void signIn(Activity activity) throws Exception {

		this.activity = activity;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.activity);

		progressDlg = new ProgressDialog(this.activity);

		userCredentials = sharedPreferences.getString("user_credentials", null);

		if (userCredentials != null) {
			JSONObject userCredentialsJson = new JSONObject(userCredentials);

			userName = userCredentialsJson.getString("userName");
			pwd = userCredentialsJson.getString("password");

			class sendSignInData extends AsyncTask<Void, Void, JSONArray> {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					progressDlg.setMessage("Please wait");
					progressDlg.show();
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
					Login.add(new BasicNameValuePair("LoginForm", jobj.toString()));
					JSONArray jarray = new DbConnect().workingMethod("Login", Login);
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
							
							Intent homeActivity = new Intent(HelpApplication.this.activity, Home.class);
							homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							homeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							
							String uName = jobj.getString("user_name");
							String userId = jobj.getString("user_Id");
							
							startActivity(homeActivity);
						} else {
							final ConfirmDialog cd = new ConfirmDialog(HelpApplication.this.activity, null);
							cd.setContents("Login failed.", jobj.getString("message"));
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
					if (progressDlg.isShowing())
						progressDlg.dismiss();
				}
			}

			sendSignInData httpAsyncTask = new sendSignInData();
			httpAsyncTask.execute();

		}

	}
	
	public boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

}
