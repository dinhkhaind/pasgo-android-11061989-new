package com.onepas.android.pasgo.utils;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;

public final class DeviceUtils {
	Context context;

	public DeviceUtils(Context context) {
		this.context = context;
	}

	public String getMaThietBi() {
		String maThietBi = "";//Utils.getImei(context);
		if (StringUtils.isEmpty(maThietBi)) {
			final TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			final String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							context.getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			maThietBi = deviceUuid.toString();
			if (StringUtils.isEmpty(maThietBi)) {
				maThietBi = UUID.randomUUID().toString();
			}
		}
		return maThietBi;
	}
}
