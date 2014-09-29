package com.tw.friendhelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tw.friendhelp.model.ConfirmDialog;
import com.tw.friendhelp.service.DbConnect;
import com.tw.friendhelp.vo.SkillsVO;

public class HelpFragment extends Fragment {
	// Spinner spnForWhat;
	ProgressDialog progressDlg;

	Vector<SkillsVO> vecSkills = new Vector<SkillsVO>();
	int skill_id;
	Button btnHelp;
	String userId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_help, container, false);

		progressDlg = new ProgressDialog(getActivity());
		progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setCancelable(false);

		// spnForWhat = (Spinner) view.findViewById(R.id.spnForWhat);
		btnHelp = (Button) view.findViewById(R.id.btnHelp);

		userId = ((Home)getActivity()).userId;
		btnHelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				JSONObject jobj = new JSONObject();
				try {
					jobj.put("user_id", userId);
					jobj.put("status", 2);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				new RequestHelp().execute(jobj.toString());
			}
		});
		return view;
	}

	class RequestHelp extends AsyncTask<String, Void, JSONArray> {
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
			signup.add(new BasicNameValuePair("RequestHelp", arg0[0]));

			JSONArray jarray = new DbConnect().workingMethod("RequestHelp", signup);
			return jarray;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			if (result != null) {
				Log.i("json", result.toString());
				super.onPostExecute(result);
				JSONObject jobj;
				try {
					jobj = result.getJSONObject(0);
					boolean success = jobj.getBoolean("success");
					String msg;

					if (success) {
						final ConfirmDialog cd = new ConfirmDialog(getActivity(), null);
						msg = "Notification sent to near by friends.To cancel request press cancel";
						cd.setContents("Notifications sent.", msg);
						cd.cdpoitiveButton.setText("Cancel");
						cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								new CancelRequest().execute(userId);
								cd.dismiss();
							}
						});
						cd.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				final ConfirmDialog cd = new ConfirmDialog(getActivity(), null);
				cd.setContents("Requesting help faild.", "Server error. Retry again");
				cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						cd.dismiss();
					}
				});
				cd.show();
			}
			if (progressDlg.isShowing())
				progressDlg.dismiss();
		}
	}

	class CancelRequest extends AsyncTask<String, Void, JSONArray> {
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
			signup.add(new BasicNameValuePair("user_id", userId));

			JSONArray jarray = new DbConnect().workingMethod("CancellRequest", signup);
			return jarray;

		}

		@Override
		protected void onPostExecute(JSONArray result) {
			if (result != null) {
				Log.i("json", result.toString());
				super.onPostExecute(result);
				JSONObject jobj;
				try {
					jobj = result.getJSONObject(0);
					boolean success = jobj.getBoolean("success");
					String msg;

					if (success) {
						final ConfirmDialog cd = new ConfirmDialog(getActivity(), null);
						msg = jobj.getString("message");
						cd.setContents("Request Cancelled.", msg);
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
			} else {
				final ConfirmDialog cd = new ConfirmDialog(getActivity(), null);
				cd.setContents("Requesting help faild.", "Server error. Retry again");
				cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						cd.dismiss();
					}
				});
				cd.show();
			}
			if (progressDlg.isShowing())
				progressDlg.dismiss();
		}
	}
}
