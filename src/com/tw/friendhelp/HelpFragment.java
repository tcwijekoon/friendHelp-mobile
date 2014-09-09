package com.tw.friendhelp;

import java.util.Vector;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tw.friendhelp.vo.SkillsVO;

public class HelpFragment extends Fragment {
//	Spinner spnForWhat;
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

//		spnForWhat = (Spinner) view.findViewById(R.id.spnForWhat);
		btnHelp = (Button) view.findViewById(R.id.btnHelp);

		btnHelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				skill_id = vecSkills.get(spnForWhat.getSelectedItemPosition()).getSkill_id();
			}
		});
//		new loadSkills().execute();

		return view;
	}

//	class loadSkills extends AsyncTask<Void, Void, JSONArray> {
//		JSONArray jaa;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			progressDlg.setMessage("loading skills.please wait.");
//			progressDlg.show();
//		}
//
//		@Override
//		protected JSONArray doInBackground(Void... arg0) {
//			JSONArray jarray = new DbConnect().workingMethod("GetSkills");
//			return jarray;
//
//		}
//
//		@Override
//		protected void onPostExecute(JSONArray ja) {
//			if (ja != null) {
//				try {
//					super.onPostExecute(ja);
//
//					JSONArray res = ja.getJSONObject(0).getJSONArray("result");
//					ArrayList<String> arrSkills = new ArrayList<String>();
//					for (int i = 0; i < res.length(); i++) {
//						Gson g = new Gson();
//						SkillsVO skills = g.fromJson(res.get(i).toString(), SkillsVO.class);
//						vecSkills.add(skills);
//						arrSkills.add(skills.getSkill());
//					}
//					ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrSkills);
//					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					spnForWhat.setAdapter(adapter2);
//
//					if (progressDlg.isShowing())
//						progressDlg.dismiss();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}
