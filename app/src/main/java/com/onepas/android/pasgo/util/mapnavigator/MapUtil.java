package com.onepas.android.pasgo.util.mapnavigator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.models.DiaChi;
import com.onepas.android.pasgo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapUtil {
	private static final String TAG = "MapUtil";
	private static final String RESULTS = "results";
	private static final String TYPES = "types";
	private static final String POLITICAL = "political";
	private static final String ADDRESS_COMPONENTS = "address_components";
	private static final String LONG_NAME = "long_name";
	private static final String STREET_NUMBER = "street_number";// S??? nh????
	private static final String ROUTE = "route";// T??n ???????ng
	private static final String SUBLOCALITY = "sublocality";// Ph?????ng x??
	private static final String LOCALITY = "locality";// Th??nh ph???
	private static final String ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2";// Qu???n
																							// Huy???n
	private static final String ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";// T???nh
	private static final String COUNTRY = "country";// N?????c

	public interface NavigationListener {
		void loadData(Directions directions);
	}

	public static double Distance(Location locationA, Location locationB) {
		double distance = locationA.distanceTo(locationB) / 1000;
		DecimalFormat df = new DecimalFormat("#0.00");
		String dx = df.format(distance);
		dx = dx.replaceAll(",", ".");
		distance = Double.valueOf(dx);
		return distance;
	}

	public static double Distance_m(Location locationA, Location locationB) {
		double distance = locationA.distanceTo(locationB);
		return distance;
	}

	public static float DistanceFloat_m(Location locationA, Location locationB) {
		double distance = locationA.distanceTo(locationB);
		return (float) distance;
	}

    public static void googleNavigation(Context context,String lat, String lng)
    {
        String url= String.format("google.navigation:q=%s,%s",lat,lng);
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

	public static LatLng getLatLngCenterScreen(GoogleMap googleMap) {
		VisibleRegion visibleRegion = googleMap.getProjection()
				.getVisibleRegion();

		Point x = googleMap.getProjection().toScreenLocation(
				visibleRegion.farRight);

		Point y = googleMap.getProjection().toScreenLocation(
				visibleRegion.nearLeft);

		Point centerPoint = new Point(x.x / 2, y.y / 2);

		LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(
				centerPoint);
		return centerFromPoint;
	}

	public static String getCompleteAddressString(Context context,
			double LATITUDE, double LONGITUDE) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());

		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,
					LONGITUDE, 1);
			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");

				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									"\n");
					if (i != returnedAddress.getMaxAddressLineIndex() - 1)
						strReturnedAddress.append(" - ");
				}
				strAdd = strReturnedAddress.toString();
				//"Unnamed Road"
				Log.i("My Current address",
						"" + strReturnedAddress.toString());
			} else {
				Log.i("My Current address", "No Address returned!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.Log("test", "Canont get Address!");
		}
		return strAdd.replaceAll("\n", "");
	}

	public static void getLocationNearestRoute(double lat, double lng,
			final EventListener eventListener) {

		String url = "https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&sensor=true&language=vi&key="
				+ Constants.API_MAP;
		url = String.format(url, String.valueOf(lat), String.valueOf(lng),
				String.valueOf(lat), String.valueOf(lng));
		Utils.Log("test", "url: " + url);

		Pasgo.getInstance().addToRequestQueue(url, null, new PWListener() {

			@Override
			public void onResponse(JSONObject json) {

				if (json != null) {
					try {

						Utils.Log("test", "ket qua: " + json.toString());

						JSONArray routeArray = json.getJSONArray("routes");
						JSONObject firstRoute = routeArray.getJSONObject(0);
						JSONArray stepArray = firstRoute.getJSONArray("legs");
						JSONObject firstStep = stepArray.getJSONObject(0);
						JSONObject startLocation = firstStep
								.getJSONObject("start_location");

						Location location = new Location("MyLocation");
						location.setLatitude(startLocation.getDouble("lat"));
						location.setLongitude(startLocation.getDouble("lng"));

						eventListener.location(location);
						Utils.Log("test", String.format("Vi do: %s, Kinh do: %s",
								String.valueOf(location.getLatitude()),
								String.valueOf(location.getLongitude())));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(int maloi) {
			}

		}, new PWErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
	}

	public static void getAddressFromLocation(double lat, double lng,
			final EventListener addressListener) {
		String url = "https://maps.googleapis.com/maps/api/geocode/json?"
				+ "latlng=" + lat + "," + lng + "&sensor=true&language=vi&key="
				+ Constants.API_MAP;
		Log.i("AAAAAA", url);
		Pasgo.getInstance().addToRequestQueue(url, null, new PWListener() {

			@Override
			public void onResponse(JSONObject json) {

				if (json != null) {
					try {
						DiaChi diaChi = new DiaChi();

						JSONArray responseArray = json.getJSONArray(RESULTS);
						JSONObject firstResponse = responseArray
								.getJSONObject(0);
						JSONArray addressComponentArray = firstResponse
								.getJSONArray(ADDRESS_COMPONENTS);

						for (int k = 0; k < addressComponentArray.length(); k++) {
							JSONObject jsonObject = addressComponentArray
									.getJSONObject(k);

							JSONArray jsonArrayTypes = jsonObject
									.getJSONArray(TYPES);
							ArrayList<String> lstType = new ArrayList<String>();
							for (int m = 0; m < jsonArrayTypes.length(); m++) {
								lstType.add(jsonArrayTypes.getString(m));
							}

							if (lstType.contains(STREET_NUMBER)) {
								diaChi.setSoNha(jsonObject.getString(LONG_NAME));
							}

							if (lstType.contains(ROUTE)) {
								diaChi.setTenDuong(jsonObject
										.getString(LONG_NAME));
							}

							if (lstType.contains(SUBLOCALITY)
									&& lstType.contains(POLITICAL)) {
								diaChi.setPhuongXa(jsonObject
										.getString(LONG_NAME));
							}

							if (lstType.contains(LOCALITY)
									&& lstType.contains(POLITICAL)) {
								diaChi.setThanhPho(jsonObject
										.getString(LONG_NAME));
							}

							if (lstType.contains(ADMINISTRATIVE_AREA_LEVEL_2)
									&& lstType.contains(POLITICAL)) {
								diaChi.setQuanHuyen(jsonObject
										.getString(LONG_NAME));
							}

							if (lstType.contains(ADMINISTRATIVE_AREA_LEVEL_1)
									&& lstType.contains(POLITICAL)) {
								diaChi.setTinh(jsonObject.getString(LONG_NAME));
							}

							if (lstType.contains(COUNTRY)
									&& lstType.contains(POLITICAL)) {
								diaChi.setNuoc(jsonObject.getString(LONG_NAME));
							}
						}

						addressListener.address(diaChi);
						addressListener.cancel(false);
						Utils.Log("test", "Dia chi: " + diaChi.getFullAddress());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(int maloi) {
			}

		}, new PWErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
	}

}
