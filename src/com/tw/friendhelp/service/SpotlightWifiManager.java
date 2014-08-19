package com.tw.friendhelp.service;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class SpotlightWifiManager {

	List<WifiStateChangeListener> listeners = new ArrayList<WifiStateChangeListener>();
	Context context;
	static SpotlightWifiManager spotlightWifiManager;
	WifiManager mainWifi;

	public SpotlightWifiManager(Context ctx) {
		context = ctx;

	}

	public void registerWifiStateListner(WifiStateChangeListener listener) {
		listeners.add(listener);
		context.registerReceiver(mConnReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	public void unregisterWifiStateListner(WifiStateChangeListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
			context.unregisterReceiver(mConnReceiver);
		}
	}

	private void notifyWifiStateListner(boolean wifiState,
			boolean connectivityState) {
		for (WifiStateChangeListener listener : listeners) {
			listener.OnWifiStateChanged(wifiState, connectivityState);
		}
	}

	public void unregisterReceiver() {
		context.unregisterReceiver(this.mConnReceiver);
	}

	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		public void onReceive(final Context context, Intent intent) {
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			if (wifi.isWifiEnabled()) {
				// wifi is enabled
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				boolean isConnected = networkInfo.isConnected();
				notifyWifiStateListner(true, isConnected);

			} else {
				notifyWifiStateListner(false, false);
			}
		}
	};
}
