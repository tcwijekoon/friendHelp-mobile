package com.tw.friendhelp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.tw.friendhelp.model.adapter.TabsPagerAdapter;
import com.tw.friendhelp.service.MyService;

public class Home extends FragmentActivity implements ActionBar.TabListener {

	public static android.support.v4.app.FragmentManager fragmentManager;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Help", "My Location", "Settings" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		fragmentManager = getSupportFragmentManager();
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

//		startService(new Intent(this, MyService.class));
//		stopService(new Intent(this, MyService.class));
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	// public void createNotification() {
	// // Prepare intent which is triggered if the
	// // notification is selected
	// Intent intent = new Intent(this, Home.class);
	// PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
	//
	// // Build notification
	// // Actions are just fake
	// Notification noti = new Notification.Builder(this)
	// .setContentTitle("New mail from " + "test@gmail.com")
	// .setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
	// .setContentIntent(pIntent)
	// .addAction(R.drawable.ic_launcher, "Call", pIntent)
	// .addAction(R.drawable.ic_launcher, "More", pIntent)
	// .addAction(R.drawable.ic_launcher, "And more", pIntent).build();
	// NotificationManager notificationManager = (NotificationManager)
	// getSystemService(NOTIFICATION_SERVICE);
	// // hide the notification after its selected
	// noti.flags |= Notification.FLAG_AUTO_CANCEL;
	//
	// notificationManager.notify(0, noti);
	//
	// }

}
