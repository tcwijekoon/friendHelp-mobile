package com.tw.friendhelp;

import com.tw.friendhelp.model.HelpApplication;
import com.tw.friendhelp.service.MyService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment implements OnClickListener {

	Button btnLogout, btnStopService;
	boolean serviceRunning = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		btnLogout = (Button) view.findViewById(R.id.btnLogOut);
		btnStopService = (Button) view.findViewById(R.id.btnStopService);

		btnLogout.setOnClickListener(this);
		btnStopService.setOnClickListener(this);

		if (checkServiceRunning()) {
			btnStopService.setText(getString(R.string.stop_service));
		} else {
			btnStopService.setText(getString(R.string.start_service));
		}

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogOut:
			HelpApplication helpApp = (HelpApplication) getActivity().getApplication();
			helpApp.signOut();
			getActivity().finish();
			Intent act = new Intent(getActivity(), Login.class);
			startActivity(act);
			break;
		case R.id.btnStopService:
			if (checkServiceRunning()) {
				getActivity().stopService(new Intent(getActivity(), MyService.class));
				btnStopService.setText(getString(R.string.start_service));
			} else {
				getActivity().startService(new Intent(getActivity(), MyService.class));
				btnStopService.setText(getString(R.string.stop_service));
			}

			break;

		default:
			break;
		}
	}

	boolean checkServiceRunning() {
		HelpApplication helpApp = (HelpApplication) getActivity().getApplication();
		return helpApp.isMyServiceRunning(MyService.class);
	}

}
