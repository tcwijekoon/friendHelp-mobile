package com.tw.friendhelp.model.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tw.friendhelp.HelpFragment;
import com.tw.friendhelp.MyLocationMapFragment;
import com.tw.friendhelp.SettingsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new HelpFragment();
		case 1:
			// Games fragment activity
			return new MyLocationMapFragment();
		case 2:
			// Movies fragment activity
			return new SettingsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
