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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.tw.friendhelp.model.ConfirmDialog;
import com.tw.friendhelp.model.HelpApplication;
import com.tw.friendhelp.service.DbConnect;

public class Login extends Activity {
	String userName, pwd;
	EditText etUserName, etPwd;
	ProgressDialog progressDlg;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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

					String value = "{\"userName\":\"" + userName + "\"," + "\"password\":\"" + pwd + "\"}";

					Editor editor = sharedPreferences.edit();
					editor.putString("user_credentials", value);
					editor.commit();

					try {

						HelpApplication helpApp = (HelpApplication) Login.this.getApplication();
						helpApp.signIn(Login.this);

					} catch (Exception e) {
						e.printStackTrace();
					}

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
}
