package com.tw.friendhelp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocation {
	Timer timer1;
	LocationManager lm;
	// ILocationListner locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	List<ILocationListner> listners = new ArrayList<ILocationListner>();

	public MyLocation(Context context) {
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
	}

	public void registerListner(ILocationListner ilocation) {
		listners.add(ilocation);
	}

	public void unregisterListner(ILocationListner ilocation) {
		listners.remove(ilocation);
	}

	public boolean getLocation() {
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled)
			return false;

		if (gps_enabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
		if (network_enabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		startTimer();
		return true;
	}

	public void startTimer() {
		timer1 = new Timer();
		timer1.schedule(new GetLastLocation(), 5000, 60*10*1000);
	}

	public void cancelTimer() {
		timer1.cancel();
		timer1 = null;
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			// locationResult.gotLocation(location);
			notifyLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			// timer1.cancel();
			// locationResult.gotLocation(location);
			notifyLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	void notifyLocation(Location location) {
		for (ILocationListner listner : listners) {
			listner.listenLocation(location);
		}
	}

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			try {
				lm.removeUpdates(locationListenerGps);
				lm.removeUpdates(locationListenerNetwork);

				Location net_loc = null, gps_loc = null;
				if (gps_enabled)
					gps_loc = lm
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (network_enabled)
					net_loc = lm
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				// if there are both values use the latest one
				if (gps_loc != null && net_loc != null) {
					if (gps_loc.getTime() > net_loc.getTime())
						// locationResult.gotLocation(gps_loc);
						notifyLocation(gps_loc);
					else
						// locationResult.gotLocation(net_loc);
						notifyLocation(net_loc);
					return;
				}

				if (gps_loc != null) {
					// locationResult.gotLocation(gps_loc);
					notifyLocation(gps_loc);
					return;
				}
				if (net_loc != null) {
					// locationResult.gotLocation(net_loc);
					notifyLocation(net_loc);
					return;
				}
				// locationResult.gotLocation(null);
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}
		}
	}
}
