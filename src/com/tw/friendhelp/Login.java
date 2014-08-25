package com.tw.friendhelp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.tw.friendhelp.model.ConfirmDialog;
import com.tw.friendhelp.model.adapter.HelpApplication;
import com.tw.friendhelp.service.DbConnect;

public class Login extends Activity {
	String userName, pwd;
	EditText etUserName, etPwd;
	ProgressDialog progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		progressDlg = new ProgressDialog(this);
		progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setCancelable(false);

		etUserName = (EditText) findViewById(R.id.etUserName);
		etPwd = (EditText) findViewById(R.id.etpwd);

		Button btnSignIn = (Button) findViewById(R.id.btnLogin);
		Button btnsignUp = (Button) findViewById(R.id.btnSignUp);

		// ImageView ivBack = (ImageView) findViewById(R.id.imgBack);
		//
		// ivBack.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// finish();
		// }
		// });

		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				userName = etUserName.getText().toString();
				pwd = etPwd.getText().toString();
				if (userName.length() != 0 && pwd.length() != 0) {
					new sendSignInData().execute();
					// Intent i = new Intent(Login.this, Home.class);
					// startActivity(i);

					// HelpApplication app = (HelpApplication) Login.this
					// .getApplication();
					// app.signIn(userName, pwd);

				} else {
					final ConfirmDialog cd = new ConfirmDialog(Login.this, null);
					cd.setContents("Invalid data.", "Please enter a valid user name and password.");
					cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							cd.dismiss();
						}
					});
					cd.show();
				}
			}
		});
		btnsignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Login.this, SignUp.class);
				startActivity(i);
			}
		});
	}

	class sendSignInData extends AsyncTask<Void, Void, JSONArray> {
		JSONArray jaa;

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
					Intent i = new Intent(Login.this, Home.class);
					startActivity(i);
				} else {
					final ConfirmDialog cd = new ConfirmDialog(Login.this, null);
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
}
