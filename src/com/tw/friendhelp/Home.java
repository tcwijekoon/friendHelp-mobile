package com.tw.friendhelp;

import static com.tw.friendhelp.model.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.tw.friendhelp.model.CommonUtilities.EXTRA_MESSAGE;
import static com.tw.friendhelp.model.CommonUtilities.SENDER_ID;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.internal.fn;
import com.tw.friendhelp.HelpFragment.CancelRequest;
import com.tw.friendhelp.HelpFragment.RequestHelp;
import com.tw.friendhelp.model.ConfirmDialog;
import com.tw.friendhelp.model.HelpApplication;
import com.tw.friendhelp.model.WakeLocker;
import com.tw.friendhelp.model.adapter.TabsPagerAdapter;
import com.tw.friendhelp.service.DbConnect;
import com.tw.friendhelp.service.MyService;
import com.tw.friendhelp.service.ServerUtilities;

public class Home extends FragmentActivity implements ActionBar.TabListener {

	public static android.support.v4.app.FragmentManager fragmentManager;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Help", "My Location", "Settings" };

	public String userId;
	ProgressDialog progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		progressDlg = new ProgressDialog(this);
		progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDlg.setCanceledOnTouchOutside(false);
		progressDlg.setCancelable(false);

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userId = extras.getString("user_id");
			if (extras.containsKey("request_help")) {

				String request_help_id = extras.getString("request_help_id");
				String requester_fname = extras.getString("requester_fname");
				String requester_address = extras.getString("requester_address");

				final ConfirmDialog cd = new ConfirmDialog(this);
				cd.setContents(requester_fname + " requesting help", requester_fname + " is requesting help from " + requester_address
						+ ". Do you like to help?");
				cd.cdnegativeButton.setText("No");
				cd.cdpoitiveButton.setText("Yes");
				cd.cdnegativeButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						cd.dismiss();
					}
				});
				cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						JSONObject jobj = new JSONObject();
						try {
							jobj.put("user_id", userId);
							jobj.put("request_help_id", userId);
							jobj.put("status", userId);

						} catch (JSONException e) {
							e.printStackTrace();
						}
						new AcceptHelp().execute(jobj.toString());

						cd.dismiss();
					}
				});
				cd.show();
			}
		}

		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {
				// Try to register again, but not in the UI thread.
				final Context context = this;
				ServerUtilities.register(context, userId, regId);
			}
		}
		if (!checkServiceRunning()) {
			Toast.makeText(this, "Service is not started.Go to settings and start the service", Toast.LENGTH_SHORT).show();
			// startService(new Intent(this, MyService.class));
		}

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
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
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

		// actionBar.setSelectedNavigationItem(2);
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

	boolean checkServiceRunning() {
		HelpApplication helpApp = (HelpApplication) this.getApplication();
		return helpApp.isMyServiceRunning(MyService.class);
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	class AcceptHelp extends AsyncTask<String, Void, JSONArray> {
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
			signup.add(new BasicNameValuePair("AcceptHelp", arg0[0]));

			JSONArray jarray = new DbConnect().workingMethod("AcceptHelp", signup);
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
					if (success) {
						final String accept_help_id = jobj.getString("accept_help_id");
						final ConfirmDialog cd = new ConfirmDialog(Home.this, null);
						cd.setContents("Help Accepted.", "To cancel the acceptance press cancel");
						cd.cdpoitiveButton.setText("Cancel");
						cd.cdpoitiveButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								JSONObject jobj = new JSONObject();
								try {
									jobj.put("accept_help_id", accept_help_id);

								} catch (JSONException e) {
									e.printStackTrace();
								}
								new AcceptCancel().execute(jobj.toString());
								cd.dismiss();
							}
						});
						cd.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				final ConfirmDialog cd = new ConfirmDialog(Home.this, null);
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

	class AcceptCancel extends AsyncTask<String, Void, JSONArray> {
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
			signup.add(new BasicNameValuePair("AcceptCancel", arg0[0]));

			JSONArray jarray = new DbConnect().workingMethod("AcceptCancel", signup);
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

					if (success) {
						final ConfirmDialog cd = new ConfirmDialog(Home.this, null);
						cd.setContents("Accept cancellation success.", "Notiffed friend as help acceptance cancelled");
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
				final ConfirmDialog cd = new ConfirmDialog(Home.this, null);
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
