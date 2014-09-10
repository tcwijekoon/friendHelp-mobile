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
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tw.friendhelp.SignUp.sendSignUpData;
import com.tw.friendhelp.model.ConfirmDialog;
import com.tw.friendhelp.model.HelpApplication;
import com.tw.friendhelp.service.DbConnect;
import com.tw.friendhelp.vo.SkillsVO;

public class HelpFragment extends Fragment {
	// Spinner spnForWhat;
	ProgressDialog progressDlg;

	Vector<SkillsVO> vecSkills = new Vector<SkillsVO>();
	int skill_id;
	Button btnHelp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_help, container, false);

		progressDlg = new ProgressDialog(getActivity());
		progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setCancelable(false);

		// spnForWhat = (Spinner) view.findViewById(R.id.spnForWhat);
		btnHelp = (Button) view.findViewById(R.id.btnHelp);

		btnHelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				JSONObject jobj = new JSONObject();
				try {
					jobj.put("user_id", 3);
					jobj.put("status", 1);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				new RequestHelp().execute(jobj.toString());
				// skill_id =
				// vecSkills.get(spnForWhat.getSelectedItemPosition()).getSkill_id();
			}
		});
		// new loadSkills().execute();

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
					final ConfirmDialog cd = new ConfirmDialog(getActivity(), null);
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
			if (progressDlg.isShowing())
				progressDlg.dismiss();
		}
	}

	// class loadSkills extends AsyncTask<Void, Void, JSONArray> {
	// JSONArray jaa;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// progressDlg.setMessage("loading skills.please wait.");
	// progressDlg.show();
	// }
	//
	// @Override
	// protected JSONArray doInBackground(Void... arg0) {
	// JSONArray jarray = new DbConnect().workingMethod("GetSkills");
	// return jarray;
	//
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray ja) {
	// if (ja != null) {
	// try {
	// super.onPostExecute(ja);
	//
	// JSONArray res = ja.getJSONObject(0).getJSONArray("result");
	// ArrayList<String> arrSkills = new ArrayList<String>();
	// for (int i = 0; i < res.length(); i++) {
	// Gson g = new Gson();
	// SkillsVO skills = g.fromJson(res.get(i).toString(), SkillsVO.class);
	// vecSkills.add(skills);
	// arrSkills.add(skills.getSkill());
	// }
	// ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
	// android.R.layout.simple_spinner_item, arrSkills);
	// adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// spnForWhat.setAdapter(adapter2);
	//
	// if (progressDlg.isShowing())
	// progressDlg.dismiss();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
}
