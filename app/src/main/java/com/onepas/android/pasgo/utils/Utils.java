package com.onepas.android.pasgo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.onepas.android.pasgo.BuildConfig;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public final class Utils {
	private static final String LOG_TAG = "Utils";

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static void writeStringAsFile(final String fileContents, String fileName) {
		Context context = Pasgo.getInstance().getApplicationContext();
		// get the path to sdcard
		File sdcard = Environment.getExternalStorageDirectory();
// to this path add a new directory path
		File dir = new File(sdcard.getAbsolutePath() + "/pasgoDiemDen/");
// create this directory if not already created
		dir.mkdir();
// create the file in which we will write the contents
		File file = new File(dir, "pasgoDiemDen.txt");
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			String data = fileContents;
			os.write(data.getBytes());
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static int getHeightScreen(Activity activity)
	{
		int Measuredwidth = 0;
		int Measuredheight = 0;
		Point size = new Point();
		WindowManager w = activity.getWindowManager();

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
			w.getDefaultDisplay().getSize(size);
			Measuredwidth = size.x;
			Measuredheight = size.y;
		}else{
			Display d = w.getDefaultDisplay();
			Measuredwidth = d.getWidth();
			Measuredheight = d.getHeight();
		}
		return  Measuredheight;
	}
    public static boolean contains( String haystack, String needle ) {
        haystack = haystack == null ? "" : haystack;
        needle = needle == null ? "" : needle;
        return haystack.toLowerCase().contains( needle.toLowerCase() );
    }

	/*
	 * Below function is used to Convert an INPUTSTREAM into the STRING
	 */
	public static String iStream_to_String(InputStream is1) {
		BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String contentOfMyInputStream = sb.toString();
		return contentOfMyInputStream;
	}

	public static double DoubleFomat(double a) {
		DecimalFormat df = new DecimalFormat("#0.0");
		String dx = df.format(a);
		dx = dx.replaceAll(",", ".");
		a = Double.valueOf(dx);
		return a;
	}
	public static String formatMoney(Context context,String promotion) {
		double mPhanTramGiamGia;
		try {
			mPhanTramGiamGia = Double.parseDouble(promotion);
		} catch (Exception e) {
			mPhanTramGiamGia = 0.0;
		}
		String sPromotion = "";
		if (mPhanTramGiamGia > 1000) {
			int phannguyen = (int) mPhanTramGiamGia / 1000;
			while(phannguyen>=1000)
			{
				String sodu= phannguyen%1000+"";
				while (sodu.length()<3) {
					sodu="0"+sodu;
				}
				sPromotion = sPromotion+ "."+ sodu ;
				phannguyen =phannguyen/1000;
			}
			sPromotion = phannguyen + sPromotion + ".000";
		}else
		if(0<mPhanTramGiamGia){
			sPromotion=Integer.parseInt(promotion)+"";
		}else {
			sPromotion = "0";
		}
		return sPromotion;
	}

	public static String ddMMyyyyHHmmssToHHmmddMMyyyy(String formatDateTo) {
		String[] separated = formatDateTo.split(" ");
		String d0= separated[0];
		String d1= separated[1];

		return d1.substring(0, 5)+" "+d0;
	}
	public static String formatMoneyFull(Context context,String promotion) {
		double mPhanTramGiamGia;
		try {
			mPhanTramGiamGia = Double.parseDouble(promotion);
		} catch (Exception e) {
			mPhanTramGiamGia = 0.0;
		}
		String sPromotion = "";
		if (mPhanTramGiamGia > 1000) {
			int phannguyen = (int) mPhanTramGiamGia / 1000;
			while(phannguyen>=1000)
			{
				String sodu= phannguyen%1000+"";
				while (sodu.length()<3) {
					sodu="0"+sodu;
				}
				sPromotion = sPromotion+ "."+ sodu ;
				phannguyen =phannguyen/1000;
			}
			sPromotion = phannguyen + sPromotion + ".000";
		}else
		if(0<mPhanTramGiamGia){
			sPromotion=Integer.parseInt(promotion)+"";
		}else {
			sPromotion = "0";
		}
		return sPromotion+" "+ context.getString(R.string.vnd_up);
	}
	public static boolean checkSDCard() {

		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);

		if (isSDPresent) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void setBackground(Button button, Drawable drawable) {
		if (sdkVersion < 16) {
			button.setBackgroundDrawable(drawable);
		} else {
			button.setBackground(drawable);
		}
	}

    @SuppressWarnings("deprecation")
    public static void setBackground(TextView textView, Drawable drawable) {
        if (sdkVersion < 16) {
            textView.setBackgroundDrawable(drawable);
        } else {
            textView.setBackground(drawable);
        }
    }

	@SuppressWarnings("deprecation")
	public static void setBackground(ImageView imageView, Drawable drawable) {
		if (sdkVersion < 16) {
			imageView.setBackgroundDrawable(drawable);
		} else {
			imageView.setBackground(drawable);
		}
	}

	@SuppressWarnings("deprecation")
	public static void setBackground(View view, Drawable drawable) {
		if (sdkVersion < 16) {
			view.setBackgroundDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}

	@SuppressWarnings("deprecation")
	public static void setBackground(RelativeLayout ob, Drawable drawable) {
		if (sdkVersion < 16) {
			ob.setBackgroundDrawable(drawable);
		} else {
			ob.setBackground(drawable);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void setBackground(LinearLayout ob, Drawable drawable) {
		if (sdkVersion < 16) {
			ob.setBackgroundDrawable(drawable);
		} else {
			ob.setBackground(drawable);
		}
	}
	public static void setTextViewHtml(TextView textview, String text)
	{
		if(!StringUtils.isEmpty(text))
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
				textview.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
			} else {
				textview.setText(Html.fromHtml(text));
			}
		else
			textview.setText("");
	}
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static int sdkVersion = android.os.Build.VERSION.SDK_INT;

	public static String getTimeRecord(Context pContext, String patFile) {
		MediaPlayer mediaPlayer = MediaPlayer.create(pContext,
				Uri.parse(patFile));
		String strSeconds, strMinutes, strTime;
		if (mediaPlayer != null) {
			long duration = mediaPlayer.getDuration();
			int seconds = (int) (duration % 60000 / 1000);

			int minutes = (int) (duration / 60000);

			int hours = (int) (duration / 3600000);
			if (seconds < 10) {
				strSeconds = "0" + seconds;
			} else {
				strSeconds = "" + seconds;
			}
			if (minutes < 10) {
				strMinutes = "0" + minutes;
			} else {
				strMinutes = "" + minutes;
			}
			if (hours == 0)
				strTime = strMinutes + ":" + strSeconds;
			else
				strTime = hours + ":" + strMinutes + ":" + strSeconds;
			return strTime;
		}

		return null;
	}

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String newGuid() {
		String newId = UUID.randomUUID().toString();
		return newId;
	}

	public static Date convertToDateVn(String dateTime) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			Date date = dateFormat.parse(dateTime);

			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getFullDate(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String strDate = dateFormat.format(date);

			return strDate;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getFullDateVN(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			String strDate = dateFormat.format(date);

			return strDate;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Date convertToFullDate(String dateTime) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse(dateTime);

			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Boolean isFileExist(String fullFileName) {
		if (fullFileName != null && fullFileName != "") {
			File file = new File(fullFileName);
			return file.exists();
		}

		return false;
	}

	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}

		return false;
	}

	public static File createStorageDirectory(Context context, String directory) {
		File file = null;
		try {
			if (isExternalStorageWritable()) {
				file = new File(Environment.getExternalStorageDirectory(),
						Constants.STORAGE_DIRECTORY_DEFAULT + File.separator
								+ directory);
				if (!file.mkdirs()) {
					Log.e(LOG_TAG, "Directory not created");
				}
			} else {
				file = new File(context.getFilesDir(),
						Constants.STORAGE_DIRECTORY_DEFAULT + File.separator
								+ directory);
				if (!file.mkdirs()) {
					Log.e(LOG_TAG, "Directory not created");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return file;
	}

	public static String ConvertBitmapToFile(Bitmap bitmap, String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getPathFromURI(Context context, Uri contentUri) {
		String[] filePathColumn = { MediaColumns.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();

		return picturePath;
	}

	/**
	 * @category update display on text view
	 * @param lblDateDisplay
	 *            ,pDay,pMonth,pYear,lblTimeDisplay,pHours,pMinute
	 */
	public static void updateDateDisplay(TextView lblDateDisplay, int pDay,
			int pMonth, int pYear) {
		int newMonth = pMonth + 1;
		String strMonth, strDay;
		if (newMonth < 10)
			strMonth = "0" + newMonth;
		else
			strMonth = "" + newMonth;
		if (pDay < 10)
			strDay = "0" + pDay;
		else
			strDay = "" + pDay;
		lblDateDisplay.setText(strDay + "/" + strMonth + "/" + pYear);
	}

	public static String convertDateToDb(int pDay, int pMonth, int pYear) {
		int newMonth = pMonth + 1;
		String strMonth, strDay;
		if (newMonth < 10)
			strMonth = "0" + newMonth;
		else
			strMonth = "" + newMonth;
		if (pDay < 10)
			strDay = "0" + pDay;
		else
			strDay = "" + pDay;
		String strDate = pYear + "-" + strMonth + "-" + strDay;
		return strDate;
	}

	public static void updateTimeDisplay(TextView lblTimeDisplay, int pHours,
			int pMinute) {
		String strHours, strMinute;
		if (pHours < 10)
			strHours = "0" + pHours;
		else
			strHours = "" + pHours;
		if (pMinute < 10)
			strMinute = "0" + pMinute;
		else
			strMinute = "" + pMinute;
		lblTimeDisplay.setText(strHours + ":" + strMinute);
	}

	public static Bitmap Shrink(String file, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file, options);
		options.inSampleSize = calcSize(options, width, height);
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(file, options);
		return bmp;
	}

	public static Bitmap Shrink(String file) {
		Bitmap bmp = BitmapFactory.decodeFile(file);
		return bmp;
	}

    public static void HideKeyBroad(Context context,EditText editText)
    {
        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

	public static int calcSize(BitmapFactory.Options options, int width,
			int height) {
		final int uHeight = options.outHeight;
		final int uWidth = options.outWidth;
		int inSampleSize = 1;
		if (uHeight > height || uWidth > width) {
			if (uWidth > uHeight) {
				inSampleSize = Math.round((float) uHeight / (float) height);
			} else {
				inSampleSize = Math.round((float) uWidth / (float) width);
			}
		}
		return inSampleSize;
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static String getStringByResourse(Context context, Integer id) {
		return context.getResources().getString(id);
	}

	public static String formatDate(String strCurrentDate,
			String formatCurrent, String formatNew) {
		SimpleDateFormat format = new SimpleDateFormat(formatCurrent);
		String currentDate = format.format(strCurrentDate);

		format = new SimpleDateFormat(formatNew);
		String date = format.format(Date.parse(currentDate));
		return date;
	}
	
	public static void changeLocalLanguage(String language)
	{
		Locale appLoc = new Locale(language);
		Locale.setDefault(appLoc);
		Configuration appConfig = new Configuration();
		appConfig.locale = appLoc;
		Pasgo.getInstance().getBaseContext().getResources().updateConfiguration(appConfig,
		Pasgo.getInstance().getBaseContext().getResources().getDisplayMetrics());
	}

    public static void TextviewReadMore(final TextView tv, final int maxLine, final String expandText, final boolean viewMore)
    {

    }

    public static boolean checkStartApp(Context context){
        final ActivityManager am=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> tasksInfo=am.getRunningTasks(1024);
        if (!tasksInfo.isEmpty()) {
            final String ourAppPackageName=context.getPackageName();
            ActivityManager.RunningTaskInfo taskInfo;
            for (    ActivityManager.RunningTaskInfo aTasksInfo : tasksInfo) {
                taskInfo=aTasksInfo;
                if (ourAppPackageName.equals(taskInfo.baseActivity.getPackageName())) {
                    return taskInfo.numActivities == 1;
                }
            }
        }
        return true;
    }

    public static void Log(final String TAG ,String str)
    {
        if(BuildConfig.DEBUG){
            Log.d(TAG,str);
        }
    }
    public static double checkSizeScreen(Activity activity)
    {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

	public static final int getColor(Context context, int id) {
		final int version = Build.VERSION.SDK_INT;
		if (version >= Build.VERSION_CODES.M) {
			return context.getColor(id);
		} else {
			return context.getResources().getColor(id);
		}
	}
	public static final Drawable getDrawable(Context context, int id) {
		final int version = Build.VERSION.SDK_INT;
		if (version >= Build.VERSION_CODES.LOLLIPOP) {
			return context.getResources().getDrawable(id, context.getTheme());
		} else {
			return context.getResources().getDrawable(id);
		}
	}
	public static boolean isMyServiceRunning( Activity activity,Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}// isMyServiceRunning(MyService.class)
	public static String formatPhoneNumber(Long sdt)
	{
		//long phoneFmt = 123456789L;
		DecimalFormat phoneDecimalFmt = new DecimalFormat("0000000000");
		String phoneRawString= phoneDecimalFmt.format(sdt);

		java.text.MessageFormat phoneMsgFmt=new java.text.MessageFormat("{0}.{1}.{2}");
		String[] phoneNumArr={phoneRawString.substring(0, 3),
				phoneRawString.substring(3,6),
				phoneRawString.substring(6)};
		return phoneMsgFmt.format(phoneNumArr);
	}
	// kiểm tra map đã hợp lệ chưa(Bản mới nhất theo app khai báo chưa)
	public static boolean googleServicesConnected() {
		GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
		int result = googleAPI.isGooglePlayServicesAvailable(Pasgo.getInstance().getApplicationContext());
		return ConnectionResult.SUCCESS == result;
	}
}