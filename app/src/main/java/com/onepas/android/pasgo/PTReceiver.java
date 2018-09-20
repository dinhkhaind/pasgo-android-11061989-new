package com.onepas.android.pasgo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class PTReceiver extends  WakefulBroadcastReceiver {
	static final String TAG = "BroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Pasgo.getInstance() != null
				&& Pasgo.getInstance().prefs != null) {
			context.startService(new Intent(context, PTService.class));
		} else {
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Intent gpsTrackerIntent = new Intent(context, PTReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, gpsTrackerIntent, 0);
			alarmManager.cancel(pendingIntent);
		}
	}
}