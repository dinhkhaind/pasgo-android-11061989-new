package com.onepas.android.pasgo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.onepas.android.pasgo.models.LocationMessageClient;
import com.onepas.android.pasgo.models.LocationMessageDriver;
import com.onepas.android.pasgo.models.PTLocationInfo;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.util.mapnavigator.MapUtil;
import com.onepas.android.pasgo.utils.NetworkUtil;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class PTService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	private static final String TAG = "PTService";
	private final int ADD_DRIVER = 0;
	private final int UPDATE_DRIVER = 1;
	private final int REMOVE_DRIVER = 2;

	private Location mLocation;
	public static HashMap<String, LocationMessageDriver> mLocationMsgDriverMap;
	private static LocationMessageDriver mLocationMessageDriver;
	private Location mLocationDriver;
	protected GoogleApiClient mGoogleApiClient;
	protected LocationRequest mLocationRequest;
	protected Location mCurrentLocation;
	// UI Widgets.
	protected Boolean mRequestingLocationUpdates;

	private final Handler handlerTimeProgress = new Handler();
	public void onCreate() {
		super.onCreate();
		mLocationMsgDriverMap = new HashMap<String, LocationMessageDriver>();
		if (Pasgo.getInstance() == null)
			this.onDestroy();
		else if (Pasgo.getInstance().prefs == null)
			Pasgo.getInstance().prefs = new PastaxiPref(getBaseContext());
		if (!NetworkUtils.isConnected(getBaseContext())) {
			final Toast toast = Toast.makeText(getBaseContext(), getResources()
							.getString(R.string.connect_error_check_network),
					Toast.LENGTH_SHORT);
			toast.show();
		}
		try {
			if (Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
				mLocation = new Location("begin");
				mLocation
						.setLatitude(Double.parseDouble(Pasgo.getInstance().prefs
								.getLatLocationRecent()));
				mLocation
						.setLongitude(Double.parseDouble(Pasgo.getInstance().prefs
								.getLngLocationRecent()));
			}
		} catch (Exception e) {
		}
		mRequestingLocationUpdates = false;
		buildGoogleApiClient();
		// nếu tất cả các Activity tắt đi đúng 10 phút thì Stop Activity đi
		IntentFilter intentLoginFilter = new IntentFilter(
				Constants.BROADCAST_ACTION_LIFECYCLE);
		registerReceiver(broadcastReceiverLifecycleCallBacks, intentLoginFilter);
	}
	//region phần này dành cho tắt service
	private final BroadcastReceiver broadcastReceiverLifecycleCallBacks = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if(bundle!=null) {
				if(bundle.getBoolean(Constants.BUNDLE_KEY_LIFECYCLE_RESUME))
					handlerLogin.sendEmptyMessage(0);
				else
					handlerLogin.sendEmptyMessage(1);
			}
		}
	};

	Handler handlerLogin = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					Log.e(Pasgo.TAG, ": Resume");
					cancelTimerStopService();
					break;
				case 1:
					startTimerStopService();
					Log.e(Pasgo.TAG, ": Pause");
					break;
				default:
					break;
			}
		};
	};
	// Xử lý thời gian tắt
	private PendingIntent mPendingIntent;
	private AlarmManager mAlarmManager;
	private void startTimerStopService() {
		if (getBaseContext() == null)
			return;
		registerReceiver(broadcastReceiverStopService, new IntentFilter(
				Constants.KEY_ACTION_STOP_SERVICE));
		mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				Constants.KEY_ACTION_STOP_SERVICE), 0);
		mAlarmManager = (AlarmManager) (this
				.getSystemService(Context.ALARM_SERVICE));
		mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() +
						Constants.MINUTE_STOP_SERVICE, mPendingIntent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
		Log.e(Pasgo.TAG, " PTService.this.stopSelf()");
		PTService.this.stopSelf();
		cancelTimerStopService();
	}

	private void cancelTimerStopService() {
		try {
			boolean alarmUp = (PendingIntent.getBroadcast(getBaseContext(), 0,
					new Intent(Constants.KEY_ACTION_STOP_SERVICE),
					PendingIntent.FLAG_NO_CREATE) != null);
			Utils.Log(Pasgo.TAG, "alarmUp" + alarmUp);
			if (alarmUp) {
				mAlarmManager.cancel(mPendingIntent);
				unregisterReceiver(broadcastReceiverStopService);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
	private final BroadcastReceiver broadcastReceiverStopService = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(Pasgo.TAG, " PTService.this.stopSelf()??");
			handlerTimeProgress.post(new Runnable() {
				public void run() {
					// tắt service
					Log.e(Pasgo.TAG, " PTService.this.stopSelf()");
					PTService.this.stopSelf();
					cancelTimerStopService();
				}
			});
		}
	};

	//endregion kết thúc phần tắt service
	//
	protected synchronized void buildGoogleApiClient() {
		Log.i(TAG, "Building GoogleApiClient");
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		createLocationRequest();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL);
		mLocationRequest.setSmallestDisplacement(Constants.SMALLEST_DISPLACEMENT);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mGoogleApiClient.connect();
	}

	public void startUpdatesButtonHandler() {
		if (!mRequestingLocationUpdates) {
			mRequestingLocationUpdates = true;
			startLocationUpdates();
		}
	}

	public void stopUpdatesButtonHandler() {
		if (mRequestingLocationUpdates) {
			mRequestingLocationUpdates = false;
			stopLocationUpdates();
		}
	}

	protected void startLocationUpdates() {
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			PTReceiver.completeWakefulIntent(intent);
			Bundle bundle = intent.getExtras();
			if (bundle != null && bundle.containsKey(Constants.BUNDLE_LAT)
					&& bundle.containsKey(Constants.BUNDLE_LNG)) {
				mLocation = new Location("begin");
				mLocation.setLatitude(bundle.getDouble(Constants.BUNDLE_LAT));
				mLocation.setLongitude(bundle.getDouble(Constants.BUNDLE_LNG));
			} else if (Pasgo.getInstance().prefs != null
					&& Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
				mLocation = new Location("begin");
				mLocation
						.setLatitude(Double.parseDouble(Pasgo.getInstance().prefs
								.getLatLocationRecent()));
				mLocation
						.setLongitude(Double.parseDouble(Pasgo.getInstance().prefs
								.getLngLocationRecent()));
			}
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.e(Pasgo.TAG, "Sevice onDestroy");
		unregisterReceiver(broadcastReceiverLifecycleCallBacks);
		stopUpdatesButtonHandler();
		Intent broadcast = new Intent();
		broadcast.setAction(Constants.BROADCAST_ACTION_UPDATE_LOCATION);
		sendBroadcast(broadcast);
		stopForeground(true);

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(Pasgo.TAG, "onBind");
		return null;
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult arg0) {
		Log.e(Pasgo.TAG, "Connection failed: ConnectionResult.getErrorCode() = " + arg0.getErrorCode());
	}

	@Override
	public void onConnected(Bundle arg0) {
		if (mCurrentLocation == null) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		}
		startUpdatesButtonHandler();
		if (mCurrentLocation != null && Pasgo.getInstance().prefs != null) {
			Pasgo.getInstance().prefs.createLatLocationRecent(mCurrentLocation
					.getLatitude() + "");
			Pasgo.getInstance().prefs.createLngLocationRecent(mCurrentLocation
					.getLongitude() + "");
		}
		updateLocation(mCurrentLocation);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();
		startUpdatesButtonHandler();
	}

	@Override
	public void onLocationChanged(Location location) {
		Utils.Log(TAG, "onChange location service" + location.getLongitude() + " - " + location.getLatitude());
		if (Pasgo.getInstance().prefs == null)
			return;
		Location currentLocation;
		if (Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
			currentLocation = new Location("currentLocation");
			currentLocation.setLatitude(Double.parseDouble(Pasgo
					.getInstance().prefs.getLatLocationRecent()));
			currentLocation.setLongitude(Double.parseDouble(Pasgo
					.getInstance().prefs.getLngLocationRecent()));
			double m = MapUtil.Distance_m(currentLocation, location);
			if (m < Constants.DISTANCE_DISPLACEMENT)
				return;
			if (Pasgo.getInstance().prefs != null) {
				Pasgo.getInstance().prefs.createLatLocationRecent(location
						.getLatitude() + "");
				Pasgo.getInstance().prefs.createLngLocationRecent(location
						.getLongitude() + "");
			}
		} else {
			Pasgo.getInstance().prefs.createLatLocationRecent(location
					.getLatitude() + "");
			Pasgo.getInstance().prefs.createLngLocationRecent(location
					.getLongitude() + "");
		}
		updateLocation(location);
	}

	public void updateLocation(Location location) {
		if (location != null) {
			mCurrentLocation = location;
			Intent broadcast = new Intent();
			broadcast.setAction(Constants.BROADCAST_ACTION_UPDATE_LOCATION);
			broadcast.putExtra(Constants.KEY_LAT_BROADCAST,location.getLatitude());
			broadcast.putExtra(Constants.KEY_LNG_BROADCAST,location.getLongitude());
			sendBroadcast(broadcast);
		}
	}

	public void updateDriver(int action, String locationMessageDriverId) {
		if (mLocationMsgDriverMap != null && mLocationMsgDriverMap.size() > 0) {
			Intent broadcast = new Intent();
			Bundle bundle = new Bundle();
			switch (action) {
			case ADD_DRIVER:
				broadcast.setAction(Constants.BROADCAST_ACTION_UPDATE_DRIVER);
				bundle.putString(Constants.KEY_DRIVER_ID_BROADCAST,
						locationMessageDriverId);
				bundle.putString(Constants.BROADCAST_ACTION,
						Constants.BROADCAST_ACTION_ADD_DRIVER);
				broadcast.putExtras(bundle);
				sendBroadcast(broadcast);
				break;
			case UPDATE_DRIVER:
				broadcast.setAction(Constants.BROADCAST_ACTION_UPDATE_DRIVER);
				bundle.putString(Constants.KEY_DRIVER_ID_BROADCAST,
						locationMessageDriverId);
				bundle.putString(Constants.BROADCAST_ACTION,
						Constants.BROADCAST_ACTION_UPDATE_STATE_DRIVER);
				broadcast.putExtras(bundle);
				sendBroadcast(broadcast);
				break;
			case REMOVE_DRIVER:
				broadcast.setAction(Constants.BROADCAST_ACTION_UPDATE_DRIVER);
				bundle.putString(Constants.KEY_DRIVER_ID_BROADCAST,
						locationMessageDriverId);
				bundle.putString(Constants.BROADCAST_ACTION,
						Constants.BROADCAST_ACTION_REMOVE_STATE_DRIVER);
				broadcast.putExtras(bundle);
				sendBroadcast(broadcast);
				break;
			default:
				break;
			}
		}
	}
}