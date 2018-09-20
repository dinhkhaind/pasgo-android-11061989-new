package com.onepas.android.pasgo.ui;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.Window;
import android.widget.Button;

import com.onepas.android.pasgo.R;

import java.util.List;

public class RateApp {

	// Insert your Application Title
	private final static String TITLE = "PASTAXI";
	// Insert your Application Package Name
	private final static String PACKAGE_NAME = "com.onepas.android.pasgo";
	// Day until the Rate Us Dialog Prompt(Default 2 Days)
	private final static int DAYS_UNTIL_PROMPT = 2;
	// Rate Us Dialog Prompt when user launch app second time
	private final static int LAUNCHES_UNTIL_PROMPT = 2;

	public static void app_launched(Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("rateapp", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}
		// if (StringUtils.isEmpty(Pastaxi.getInstance().prefs.getToken()))
		// return;
		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}
		// Wait at least n days before opening
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			if (System.currentTimeMillis() >= date_firstLaunch
					+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
				showRateDialog(mContext, editor);
			}
		}
		editor.apply();
	}

	public static void showRateDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		final Dialog mDialog;
		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.layout_popup_rate);
		mDialog.setTitle(R.string.rate_pastxi_title);
		Button btnRate, btnRemind, btnNoRating;
		btnRate = (Button) mDialog.findViewById(R.id.btnRatePasTaxi);
		btnRemind = (Button) mDialog.findViewById(R.id.btnRemindMeLater);
		btnNoRating = (Button) mDialog.findViewById(R.id.btnNoRating);
		btnRate.setOnClickListener(v -> {
			openAppRating(mContext);
            mDialog.dismiss();
        });
		btnRemind.setOnClickListener(v -> mDialog.dismiss());
		btnNoRating.setOnClickListener(v -> {
            if (editor != null) {
                editor.putBoolean("dontshowagain", true);
                editor.commit();
            }
            mDialog.dismiss();
        });
		mDialog.show();
	}

	public static void openAppRating(Context context) {
		Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
		boolean marketFound = false;

		// find all applications able to handle our rateIntent
		final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
		for (ResolveInfo otherApp: otherApps) {
			// look for Google Play application
			if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

				ActivityInfo otherAppActivity = otherApp.activityInfo;
				ComponentName componentName = new ComponentName(
						otherAppActivity.applicationInfo.packageName,
						otherAppActivity.name
				);
				rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				rateIntent.setComponent(componentName);
				context.startActivity(rateIntent);
				marketFound = true;
				break;

			}
		}

		// if GP not present on device, open web browser
		if (!marketFound) {
			Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+context.getPackageName()));
			context.startActivity(webIntent);
		}
	}
}
