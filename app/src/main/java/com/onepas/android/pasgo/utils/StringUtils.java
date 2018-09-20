/*
 * Name: $RCSfile: StringUtility.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 1:54:00 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.onepas.android.pasgo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

/**
 * StringUtility class
 * 
 * @author Dk8
 * 
 */
@SuppressLint("SimpleDateFormat")
public final class StringUtils {
	/**
	 * Check Edit Text input string
	 * 
	 * @param editText
	 * @return
	 */
	public static boolean isEmpty(EditText editText) {
		if (editText == null
				|| editText.getEditableText() == null
				|| editText.getEditableText().toString().trim()
						.equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}
	public  static String formatStringNewLine(String str)
	{
		return str.replaceAll("(\n|\r\n)","<br>");
	}
	public static boolean checkEmail(String email) {

		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);

		return matcher.matches();
	}
	@SuppressWarnings("deprecation")
	public static Spanned fromHtml(String html){
		Spanned result;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
		} else {
			result = Html.fromHtml(html);
		}
		return result;
	}
	/**
	 * Check input string
	 * 
	 * @param editText
	 * @return
	 */
	public static boolean isEmpty(String editText) {
		if (editText == null || editText.trim().equalsIgnoreCase("")
				|| editText.equalsIgnoreCase(null)) {
			return true;
		}
		return false;
	}

	public static String getSubString(String input, int maxLength) {
		String temp = input;
		if (input.length() < maxLength)
			return temp;
		else
			return input.substring(0, maxLength - 1) + "...";
	}

	public static String getStringByResourse(Context context, Integer id) {
		return context.getResources().getString(id);
	}

	/**
	 * Merge all elements of a string array into a string
	 * 
	 * @param strings
	 * @param separator
	 * @return
	 */
	public static String join(String[] strings, String separator) {
		StringBuffer sb = new StringBuffer();
		int max = strings.length;
		for (int i = 0; i < max; i++) {
			if (i != 0)
				sb.append(separator);
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	/**
	 * Convert current date time to string
	 * 
	 * @param updateTime
	 * @return
	 */
	public static String convertNowToFullDateString() {
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));

		// Calendar calendar =
		// Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		// return dateformat.format(calendar.getTime());
		return dateformat.format(new Date());
	}

	public static String convertNowToDateString(String format) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		// dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));

		// Calendar calendar =
		// Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		// return dateformat.format(calendar.getTime());
		return dateformat.format(new Date());
	}

	/**
	 * Initial sync date string
	 * 
	 * @return
	 */
	public static String initDateString() {
		return "1900-01-01 09:00:00";
	}

	/**
	 * Convert a string divided by ";" to multiple xmpp users
	 * 
	 * @param userString
	 * @return
	 */
	public static String[] convertStringToXmppUsers(String userString) {
		return userString.split(";");
	}

	/**
	 * get Unique Random String
	 * 
	 * @return
	 */

	public static String getUniqueRandomString() {

		// return String.valueOf(System.currentTimeMillis());
		UUID uuid = UUID.randomUUID();
		return uuid.toString();

	}

	// public static String getUniqueRandomString() {
	//
	// return UUID.randomUUID().toString();
	//
	// }

	public static int compareDate(String dateFormat, String dateA, String dateB) {
		int result = 0;
		// SmartLog.log("CompareData", "Input date format : " + dateFormat +
		// " : "
		// + dateA + ":" + dateB);
		String aDateFormat = dateFormat.equalsIgnoreCase("") ? "yyyy-MM-dd HH:mm:ss"
				: dateFormat;

		SimpleDateFormat inputFormat = new SimpleDateFormat(aDateFormat);

		Date startDate;
		Date endDate;
		try {
			startDate = inputFormat.parse(dateA);
			endDate = inputFormat.parse(dateB);
			result = (startDate.getTime() - endDate.getTime()) > 0 ? -1 : 1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public static String reformatDateTime(String dateFormat,
			String outputFormat, String dateInput) {
		String outputStr = "";

		String aDateFormat = dateFormat.equalsIgnoreCase("") ? "yyyy-MM-dd HH:mm:ss"
				: dateFormat;
		SimpleDateFormat inputFormat = new SimpleDateFormat(aDateFormat);
		SimpleDateFormat outputForm = new SimpleDateFormat(outputFormat);

		Date startDate;
		try {
			startDate = inputFormat.parse(dateInput);
			outputStr = outputForm.format(startDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStr;

	}

	public static String getDateString(String dateFormat, String longVvalue) {
		String outputStr = "";
		SimpleDateFormat outputForm = new SimpleDateFormat(dateFormat);

		Date startDate;
		startDate = new Date(Long.parseLong(longVvalue));
		outputStr = outputForm.format(startDate);
		return outputStr;

	}

	public static long getLongValueDate(String inputDate, String dateFormat) {
		String aDateFormat = dateFormat.equalsIgnoreCase("") ? "yyyy-MM-dd HH:mm:ss"
				: dateFormat;
		SimpleDateFormat outputForm = new SimpleDateFormat(aDateFormat);
		Date startDate = new Date();
		try {
			startDate = outputForm.parse(inputDate);
		} catch (ParseException e) {
		}
		return startDate.getTime();
	}

	public static String getStringCovertPrice(String data) {
		if (data == null || "".equals(data))
			return "";
		if (data.indexOf(".") == -1)
			return data;
		else
			return data.substring(0, data.indexOf(".") + 1);
	}

	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static long getDistanceTime(long olderTime, long currentTime) {
		return currentTime - olderTime;
	}

	public static String getCurrentTimeString() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getCurrentTimeFormat(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}
}