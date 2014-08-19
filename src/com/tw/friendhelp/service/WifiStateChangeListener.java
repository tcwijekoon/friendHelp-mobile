package com.tw.friendhelp.service;


public interface WifiStateChangeListener {
	
	boolean OnWifiStateChanged(boolean wifiState, boolean connectivityState);

}
