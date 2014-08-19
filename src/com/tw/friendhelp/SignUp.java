package com.tw.friendhelp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.tw.friendhelp.Login.sendSignInData;
import com.tw.friendhelp.model.ConfirmDialog;
import com.tw.friendhelp.service.DbConnect;
import com.tw.friendhelp.vo.BloodGroupVO;
import com.tw.friendhelp.vo.SkillsVO;

public class SignUp extends FragmentActivity {

	static SimpleDateFormat datedf = new SimpleDateFormat("yyyy-MM-dd");
	Button btnDob;
	Spinner spnSkills, spnBldGrp;

	EditText etUserName, etPwd, etRetypePwd, etEmail, etFname, etLName, etmobNo, etLandNo, etStreet, etCity;
	String user_name, password, retype_pwd, first_name = "", last_name = "", email = "", mob_no = "", address_no = "", address_street = "",
			address_city = "";

	Date dob;
	int gender, bld_grp_id, skill_id;
	ProgressDialog progressDlg;

	Vector<SkillsVO> vecSkills = new Vector<SkillsVO>();
	Vector<BloodGroupVO> vecBldGrp = new Vector<BloodGroupVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);

		progressDlg = new ProgressDialog(this);
		progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setCancelable(false);

		etUserName = (EditText) findViewById(R.id.etUserName);
		etPwd = (EditText) findViewById(R.id.etPwd);
		etRetypePwd = (EditText) findViewById(R.id.etRetypePwd);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etFname = (EditText) findViewById(R.id.etFirstName);
		etLName = (EditText) findViewById(R.id.etLastName);
		etmobNo = (EditText) findViewById(R.id.etMobileNo);
		etLandNo = (EditText) findViewById(R.id.etLandNo);
		etStreet = (EditText) findViewById(R.id.etStreet);
		etCity = (EditText) findViewById(R.id.etCity);

		btnDob = (Button) findViewById(R.id.btnDob);
		spnSkills = (Spinner) findViewById(R.id.spnSkills);
		spnBldGrp = (Spinner) findViewById(R.id.spnBldGrp);

		new loadBloodGroup().execute();
		new loadSkills().execute();
	}

	class loadBloodGroup extends AsyncTask<Void, Void, JSONArray> {
		JSONArray jaa;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDlg.setMessage("loading blood groups.please wait.");
			progressDlg.show();
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JSONArray jarray = new DbConnect().workingMethod("GetBloodGroups");
			return jarray;

		}

		@Override
		protected void onPostExecute(JSONArray ja) {
			if (ja != null) {
				try {
					super.onPostExecute(ja);

					JSONArray res = ja.getJSONObject(0).getJSONArray("result");
					ArrayList<String> arrBloodGrp = new ArrayList<String>();
					for (int i = 0; i < res.length(); i++) {
						Gson g = new Gson();
						BloodGroupVO bldGrps = g.fromJson(res.get(i).toString(), BloodGroupVO.class);
						vecBldGrp.add(bldGrps);
						arrBloodGrp.add(bldGrps.getBld_group());
					}
					ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_spinner_item, arrBloodGrp);
					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnBldGrp.setAdapter(adapter2);

					if (progressDlg.isShowing())
						progressDlg.dismiss();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class loadSkills extends AsyncTask<Void, Void, JSONArray> {
		JSONArray jaa;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDlg.setMessage("loading skills.please wait.");
			progressDlg.show();
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JSONArray jarray = new DbConnect().workingMethod("GetSkills");
			return jarray;

		}

		@Override
		protected void onPostExecute(JSONArray ja) {
			if (ja != null) {
				try {
					super.onPostExecute(ja);

					JSONArray res =ja.getJSONObject(0).getJSONArray("result");;
					ArrayList<String> arrSkills = new ArrayList<String>();
					for (int i = 0; i < res.length(); i++) {
						Gson g = new Gson();
						SkillsVO skills = g.fromJson(res.get(i).toString(), SkillsVO.class);
						vecSkills.add(skills);
						arrSkills.add(skills.getSkill());
					}
					ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_spinner_item, arrSkills);
					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnSkills.setAdapter(adapter2);

					if (progressDlg.isShowing())
						progressDlg.dismiss();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	void submitSignUpDetails() {
		user_name = etUserName.getText().toString();
		password = etPwd.getText().toString();
		retype_pwd = etRetypePwd.getText().toString();
		email = etEmail.getText().toString();
		mob_no = etmobNo.getText().toString();
		address_no = etLandNo.getText().toString();
		address_street = etStreet.getText().toString();
		address_no = etCity.getText().toString();
		address_city = etCity.getText().toString();

		if (user_name.length() < 4) {
			final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
			cd.setContents("Invalid user name.", "Please enter a user name more than four characters.");
			cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cd.dismiss();
					etUserName.requestFocus();
				}
			});
			cd.show();
		} else if (password.length() < 4) {
			final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
			cd.setContents("Invalid password.", "Please enter a password more than four characters.");
			cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cd.dismiss();
					etPwd.requestFocus();
				}
			});
			cd.show();
		} else if (mob_no.length() < 10) {
			final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
			cd.setContents("Invalid mobile no.", "Please enter a valid mobile number.");
			cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cd.dismiss();
					etmobNo.requestFocus();
				}
			});
			cd.show();
		} else if (address_no.length() < 1 || address_city.length() < 1) {
			final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
			cd.setContents("Invalid address.", "Please enter your door number and city.");
			cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cd.dismiss();
					etLandNo.requestFocus();
				}
			});
			cd.show();
		} else if (!isValidEmail(email)) {
			final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
			cd.setContents("Invalid email.", "Please enter a valid email.");
			cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cd.dismiss();
					etEmail.requestFocus();
				}
			});
			cd.show();
		} else if (!password.equalsIgnoreCase(retype_pwd)) {

			final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
			cd.setContents("Password not match.", "Please retype the correct password.");
			cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cd.dismiss();
					etRetypePwd.setText("");
					etRetypePwd.requestFocus();
				}
			});
			cd.show();
		} else {
			bld_grp_id = vecBldGrp.get(spnBldGrp.getSelectedItemPosition()).getBld_grp_id();
			skill_id = vecSkills.get(spnBldGrp.getSelectedItemPosition()).getSkill_id();

			// male - gender - 0
			gender = ((RadioButton) findViewById(R.id.rbMale)).isSelected() ? 0 : 1;

			JSONObject jobj = new JSONObject();
			try {
				jobj.put("user_name", user_name);
				jobj.put("password", password);
				jobj.put("first_name", first_name);
				jobj.put("last_name", last_name);
				jobj.put("email", email);
				jobj.put("mob_no", mob_no);
				jobj.put("dob", btnDob.getText());
				jobj.put("gender", gender);
				jobj.put("address_no", address_no);
				jobj.put("address_street", address_street);
				jobj.put("address_city", address_city);
				jobj.put("bld_grp_id", bld_grp_id);
				jobj.put("skill_id", skill_id);
				jobj.put("status_id", 0);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			new sendSignUpData().execute(jobj.toString());
		}
	}

	class sendSignUpData extends AsyncTask<String, Void, JSONArray> {
		JSONArray jaa;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDlg.setMessage("Please wait");
			progressDlg.show();
		}

		@Override
		protected JSONArray doInBackground(String... arg0) {
			List<NameValuePair> signup = new ArrayList<NameValuePair>(1);
			signup.add(new BasicNameValuePair("Log", arg0[0]));

//			JSONArray jarray = new DbConnect().workingMethodForSignUp(signup);
//			return jarray;
			return null;

		}

		@Override
		protected void onPostExecute(JSONArray result) {
			Log.i("json", result.toString());
			super.onPostExecute(result);
			JSONObject jobj;
//			try {
//				jobj = result.getJSONObject(0);
//				String s = jobj.getString("success");
//				String msg;
//				if (s.equals("true")) {
//					userId = jobj.getString("userid");
//					OrderApplication.editPreferences().putString("userId", userId).commit();
//					Intent i = new Intent(SignUp.this, Checkout.class);
//					// i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					startActivity(i);
//				} else {
//					msg = jobj.getString("message");
//					final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
//					cd.setContents("Invalid details.", msg);
//					cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							cd.dismiss();
//						}
//					});
//					cd.show();
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}

			if (progressDlg.isShowing())
				progressDlg.dismiss();
		}
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			Calendar tmp = (Calendar) c.clone();
			tmp.add(Calendar.DAY_OF_MONTH, 1);
			int year = tmp.get(Calendar.YEAR);
			int month = tmp.get(Calendar.MONTH);
			int day = tmp.get(Calendar.DAY_OF_MONTH);
			// int year = c.get(Calendar.YEAR);
			// int month = c.get(Calendar.MONTH);
			// int day = c.get(Calendar.DAY_OF_MONTH);
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			Date date;
			Date dateCompareOne;

			Calendar c = Calendar.getInstance();
			Calendar tmp = (Calendar) c.clone();
			tmp.add(Calendar.DAY_OF_MONTH, 1);

			date = parseDate(year + "-" + (month + 1) + "-" + day);
			// dateCompareOne = parseDate(datedf.format(c.getTime()));
			dateCompareOne = parseDate(datedf.format(tmp.getTime()));

			if (date.before(dateCompareOne)) {
				final ConfirmDialog cd = new ConfirmDialog(SignUp.this, null);
				cd.setContents("Invalid date selection", "Please select a day more than current date.");
				cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						cd.dismiss();
					}
				});
				cd.show();
			} else {
				String formattedDate = datedf.format(date.getTime());
				btnDob.setText(formattedDate);
				// btnDate.setText(year + "-" + (month + 1) + "-" + day);
			}
		}
	}

	private Date parseDate(String date) {
		String inputFormat = "yyyy-MM-dd";
		Date d;
		SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
		try {
			d = inputParser.parse(date);
		} catch (java.text.ParseException e) {
			d = new Date(0);
		}
		return d;
	}

	public final static boolean isValidEmail(String target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
}
