package com.onepas.android.pasgo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;

import com.onepas.android.pasgo.R;

public final class NetworkUtils {
	private Context context = null;
	public final static int TYPE_WIFI = 1;
	public final static int TYPE_MOBILE = 2;
	public final static int TYPE_NOT_CONNECTED = 0;

	private static NetworkUtils instance = null;

	private NetworkUtils(Context context) {
		this.context = context;
	}

	public static NetworkUtils getInstance(Context context) {
		if (instance == null) {
			instance = new NetworkUtils(context);
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

	public boolean isNetworkConnect() {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null) {
			return false;
		}
		if (!i.isConnectedOrConnecting()) {
			return false;
		}
		if (!i.isAvailable()) {
			return false;
		}
		return true;
	}

	// turn on use network for location
	public void turnNetWorkLocationOn(Activity activity) {
		String provider = Settings.Secure.getString(
				activity.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("1"));
			activity.sendBroadcast(poke);
		}
	}

	public boolean isNetWork() {
		boolean isCheck = NetworkUtils.getInstance(context)
				.isNetworkAvailable();
		if (!isCheck)
			ToastUtils.showToast(context,
					context.getString(R.string.connect_error_check_network));
		return isCheck;
	}

	public boolean isGpsEnabled() {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
		else
			return false;
	}

	public static boolean isConnected(Context context) {
		int conn = getConnectivityStatus(context);
		if (conn == NetworkUtils.TYPE_WIFI) {
			return true;
		} else if (conn == NetworkUtils.TYPE_MOBILE) {
			return true;
		} else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
			return false;
		}
		return false;
	}

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}
}