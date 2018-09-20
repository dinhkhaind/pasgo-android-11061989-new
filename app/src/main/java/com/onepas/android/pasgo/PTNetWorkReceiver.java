package com.onepas.android.pasgo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class PTNetWorkReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent broadcast = new Intent();
		broadcast.setAction(Constants.BROADCAST_ACTION_CHANGE_NETWORK);
		context.sendBroadcast(broadcast);
	}
}