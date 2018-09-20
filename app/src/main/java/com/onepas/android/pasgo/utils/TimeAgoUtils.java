package com.onepas.android.pasgo.utils;

import java.util.Date;

import android.content.Context;
import android.content.res.Resources;

import com.onepas.android.pasgo.R;

public final class TimeAgoUtils {

	protected Context context;

	public TimeAgoUtils(Context context) {
		this.context = context;
	}

	public String timeAgo(Date date) {
		return timeAgo(date.getTime());
	}

	public String timeAgo(long millis) {
		long dateTime = new Date().getTime();
		long diff = dateTime - millis;
		//long diff = millis;

		Resources r = context.getResources();

		String prefix = r.getString(R.string.time_ago_prefix);
		String suffix = r.getString(R.string.time_ago_suffix);

		double seconds = Math.abs(diff) / 1000;
		double minutes = seconds / 60;
		double hours = minutes / 60;
		int days = (int) (hours / 24);
		int weeks = (int) (days / 7);
		int month = (int) (days / 30);
		int years = (int) (days / 365);

		String words;

		if (seconds < 45) {
			words = r.getString(R.string.time_ago_seconds, Math.round(seconds));
		} else if (seconds < 90) {
			words = r.getString(R.string.time_ago_minute, 1);
		} else if (minutes < 45) {
			words = r.getString(R.string.time_ago_minutes, Math.round(minutes));
		} else if (minutes < 90) {
			words = r.getString(R.string.time_ago_hour, 1);
		} else if (hours < 24) {
			words = r.getString(R.string.time_ago_hours, Math.round(hours));
		} else if (hours < 42) {
			words = r.getString(R.string.time_ago_day, 1);
		} else if (days < 2) {
			words = r.getString(R.string.time_ago_day, 1);
		} else if (days < 7) {
			words = r.getString(R.string.time_ago_days, days);
		} else if (days < 14) {
			words = r.getString(R.string.time_ago_week, 1);
		} else if (days < 31) {
			words = r.getString(R.string.time_ago_weeks, weeks);
		} else if (days < 62) {
			words = r.getString(R.string.time_ago_month, 1);
		} else if (days < 365) {
			words = r.getString(R.string.time_ago_months, month);
		} else if (years < 1.5) {
			words = r.getString(R.string.time_ago_year, 1);
		} else {
			words = r.getString(R.string.time_ago_years, years);
		}

		StringBuilder sb = new StringBuilder();

		if (prefix != null && prefix.length() > 0) {
			sb.append(prefix).append(" ");
		}

		sb.append(words);

		if (suffix != null && suffix.length() > 0) {
			sb.append(" ").append(suffix);
		}

		return sb.toString().trim();
	}

	public static String getTime(Context context, long sec) {
		String time = sec / 60 + " "
				+ context.getResources().getString(R.string.time_minutes);
		return time;
	}
}
