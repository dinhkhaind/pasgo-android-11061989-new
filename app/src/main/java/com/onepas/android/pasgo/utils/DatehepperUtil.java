package com.onepas.android.pasgo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.DatePicker;

import com.onepas.android.pasgo.R;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatehepperUtil {
	//public static final String format120 = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyyMMddHHmmss = "yyyy/MM/dd HH:mm:ss";
    public static final String ddMMyyyyHHmmss = "dd/MM/yyyy HH:mm:ss";
	public static final String HHmmddMMyyyy = "HH:mm dd/MM/yyyy";
	public static final String ddMMyyyy = "dd/MM/yyyy";
    public static final String MMddyyyy = "MM/dd/yyyy";
	public static final String yyyyMMdd = "yyyy/MM/dd";

	public DatehepperUtil() {
		super();
	}

	public static Long getTimeInMillis() {
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}
	public static Calendar toCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	public static Long convertDatetimeToLongDate(String sDate, String formarDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(formarDate);
		formatter.setLenient(false);
		String oldTime = sDate;
		Date oldDate = null;
		try {
			oldDate = formatter.parse(oldTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long oldMillis = oldDate.getTime();
		return oldMillis;
	}
	/*public static String getDateString()
	{
		String date = "03/26/2012 11:00:00";
		String dateafter = "03/26/2012 11:59:00";
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM/dd/yyyy hh:mm:ss");
		Date convertedDate = new Date();
		Date convertedDate2 = new Date();
		try {
			convertedDate = dateFormat.parse(date);
			convertedDate2 = dateFormat.parse(dateafter);
			if (convertedDate2.after(convertedDate)) {
				txtView.setText("true");
			} else {
				txtView.setText("false");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null)
			return false;
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	public static String formatDate(int day) {
		if (day < 10)
			return String.format("0%s", day);
		return Integer.toString(day);
	}

	public static int formatDateInt(int day) {
		if (day < 10)
			return Integer.parseInt(String.format("0%s", day));
		return day;

	}

    public static int getYearCurrent(Context context)
    {
        final DatePicker datePicker = new DatePicker(context);
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

	public static String getCurrentDate(String formatDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
		return sdf.format(new Date());
	}
	public static Calendar getCalenDarDate(int dayAdd) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, dayAdd);
		return c;
	}
	public static String getWeekDay(int year, int month, int day, Activity activity)
	{
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		String weekDay = "";
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if (Calendar.MONDAY == dayOfWeek) {
			weekDay = activity.getString(R.string.thu2);
		} else if (Calendar.TUESDAY == dayOfWeek) {
			weekDay = activity.getString(R.string.thu3);
		} else if (Calendar.WEDNESDAY == dayOfWeek) {
			weekDay = activity.getString(R.string.thu4);
		} else if (Calendar.THURSDAY == dayOfWeek) {
			weekDay = activity.getString(R.string.thu5);
		} else if (Calendar.FRIDAY == dayOfWeek) {
			weekDay = activity.getString(R.string.thu6);
		} else if (Calendar.SATURDAY == dayOfWeek) {
			weekDay = activity.getString(R.string.thu7);
		} else if (Calendar.SUNDAY == dayOfWeek) {
			weekDay = activity.getString(R.string.thu0);
		}
		return weekDay;
	}
	public static Calendar getWeekDayAdd(int year, int month, int day, int dayAdd)
	{
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		c.add(Calendar.DAY_OF_MONTH, dayAdd);
		return c;

	}
	public static Calendar getTimeAdd(int year, int month, int day, int hour, int minute, int timeAdd)
	{
		if(timeAdd>0) {
			if (minute > 0 && minute <= 15) {
				minute = 0;
				timeAdd = 30;
			} else if ((minute > 15 && minute <= 30)
					|| (minute > 30 && minute <= 45)) {
				minute = 30;
				timeAdd = 30;
			} else if (minute > 45) {
				minute = 59;
				timeAdd =31;
			}
		}else
		{
			if (minute > 0 && minute <= 15) {
				minute = 0;
				timeAdd = -30;
			} else if ((minute > 15 && minute <= 30)
					|| (minute > 30 && minute <= 45)) {
				minute = 30;
				timeAdd = -30;
			} else if (minute > 45) {
				minute = 59;
				timeAdd = -29;
			}
		}

		Calendar c = Calendar.getInstance();
		c.set(year, month, day);

		c.set(Calendar.HOUR_OF_DAY,hour);
		c.set(Calendar.MINUTE,minute);
		c.add(Calendar.MINUTE, timeAdd);
		return c;

	}
	public static String addMinute(int minute) {

		int cday, cmonth, cyear, cminute, mhour, csec;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, minute);
		cday = c.get(Calendar.DAY_OF_MONTH);
		cmonth = c.get(Calendar.MONTH) + 1;
		cyear = c.get(Calendar.YEAR);
		mhour = c.get(Calendar.HOUR_OF_DAY);
		cminute = c.get(Calendar.MINUTE);
		csec = c.get(Calendar.SECOND);
		String newstr = new StringBuilder().append(formatDate(cyear))
				.append("/").append(formatDate(cmonth)).append("/")
				.append(formatDate(cday)).append(" ").append(formatDate(mhour))
				.append(":").append(formatDate(cminute)).append(":")
				.append(formatDate(csec)).toString();
		return newstr;
	}

	public static Date convertStringToDate(String strDate, String format) {
		Date date = new Date();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static String ConvertFormatDateToFormatDate(String formatDateTo,
			String startTime, String formatDateFrom) {
		Date date1 = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatDateTo);
		try {
			date1 = dateFormat.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatDateFrom); // Set your
		String currentData = sdf.format(date1);
		return currentData;
	}



    public static boolean isPackageExpired(String date,String aFormat){
        boolean isExpired=false;
        Date expiredDate = stringToDate(date, aFormat);
        if (new Date().after(expiredDate)) isExpired=true;

        return isExpired;
    }

    public static Date stringToDate(String aDate,String aFormat) {

        if(aDate==null||aDate=="") return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

}
