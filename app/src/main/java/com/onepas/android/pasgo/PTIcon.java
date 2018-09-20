package com.onepas.android.pasgo;

import java.util.HashMap;

import android.content.Context;

public class PTIcon {
	private static HashMap<String, Integer> iconIncident;

	public static HashMap<String, Integer> getInstanceIconReason() {
		if (iconIncident == null)
			iconIncident = getIconReason();
		return iconIncident;
	}

	public static final HashMap<String, Integer> getIconReason() {
		HashMap<String, Integer> imageSuco = new HashMap<String, Integer>();
		imageSuco.put("01", R.drawable.ic_map_cafe);
		imageSuco.put("02", R.drawable.ic_map_khachsan);
		imageSuco.put("03", R.drawable.ic_map_amthuc);
		imageSuco.put("04", R.drawable.ic_map_thoitrang);
		imageSuco.put("05", R.drawable.ic_map_lamdep);
		imageSuco.put("06", R.drawable.ic_map_khac);
		imageSuco.put("07", R.drawable.ic_map_giaitri);
		imageSuco.put("08", R.drawable.ic_map_suckhoe);
		imageSuco.put("09", R.drawable.ic_map_sanbay);
		imageSuco.put("10", R.drawable.ic_map_oto);
		return imageSuco;
	}

	public static String[] getArray(Context context, Integer arr) {
		return context.getResources().getStringArray(arr);
	}
}