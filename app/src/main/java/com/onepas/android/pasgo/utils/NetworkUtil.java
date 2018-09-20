package com.onepas.android.pasgo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;

public final class NetworkUtil {
	private Context context = null;

	private static NetworkUtil instance = null;

	private NetworkUtil(Context context) {
		this.context = context;
	}

	public static NetworkUtil getInstance(Context context) {
		if (instance == null) {
			instance = new NetworkUtil(context);
		}
		return instance;
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null) {
			return false;
		}
		if (!i.isConnected()) {
			return false;
		}
		if (!i.isAvailable()) {
			return false;
		}
		return true;
	}

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	public static final int NETWORK_STATUS_NOT_CONNECTED=0,NETWORK_STAUS_WIFI=1,NETWORK_STATUS_MOBILE=2;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static int getConnectivityStatusString(Context context) {
		int conn = NetworkUtil.getConnectivityStatus(context);
		int status = 0;
		if (conn == NetworkUtil.TYPE_WIFI) {
			status = NETWORK_STAUS_WIFI;
		} else if (conn == NetworkUtil.TYPE_MOBILE) {
			status =NETWORK_STATUS_MOBILE;
		} else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
			status = NETWORK_STATUS_NOT_CONNECTED;
		}
		return status;
	}
}
