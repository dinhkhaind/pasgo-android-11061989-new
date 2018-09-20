package com.onepas.android.pasgo.ui.search;

import android.util.Log;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.models.Place;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PlacesService {
	private String TAG = "PlacesService";
	private String API_KEY;
	private String txtSearch;
	private boolean b;
	private int neaBySearch;
	// private ArrayList<Place> arrayList;
	private static final String PLACES_TEXT_SEARCH = "https://maps.googleapis.com/maps/api/place/textsearch/json";

	public PlacesService(String apikey, String txtSearch, boolean b, int neaBySearch) {
		this.API_KEY = apikey;
		try {
			if (StringUtils.isEmpty(txtSearch)) {

			} else {
				this.txtSearch = URLEncoder.encode(txtSearch, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			this.txtSearch = txtSearch;
		}
		this.b = b;
		this.neaBySearch = neaBySearch;
	}

	public void setApiKey(String apikey) {
		this.API_KEY = apikey;
	}

	public ArrayList<Place> findPlaces(double latitude, double longitude) {
		String urlString = "";
		if (txtSearch == null) {
			urlString = makeUrl(latitude, longitude, txtSearch);
		} else {
			urlString =  makeUrlTextSearch(latitude, longitude, txtSearch);
		}

		ArrayList<Place> arrayList = null;
		String json = getJSON(urlString);
		System.out.println(json);
		JSONObject object;
		try {
			object = new JSONObject(json);
			arrayList = parseJsonObj(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return arrayList;
	}

	private String makeUrlTextSearch(double latitude, double longitude,
			String txtsearch) {
		StringBuilder urlString = new StringBuilder(PLACES_TEXT_SEARCH);
		txtsearch = txtsearch.replace(" ", "%20");
		urlString.append("?query=" + txtsearch);
		urlString.append("&location=");
		urlString.append(Double.toString(latitude));
		urlString.append(",");
		urlString.append(Double.toString(longitude));
		urlString.append("&radius=5000");
		urlString.append("&sensor=false&language=vi&key=" + API_KEY);

		return urlString.toString();
	}

	private String makeUrl(double latitude, double longitude, String txtsearch) {
		StringBuilder urlString = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		if (txtsearch == null) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=500");
			// urlString.append("&types="+place);
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			txtsearch = txtsearch.replace(" ", "%20");
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&name=" + txtsearch);
			urlString.append("&radius=20000");
			// urlString.append("&types="+place);
			urlString.append("&sensor=false&key=" + API_KEY);

		}
		return urlString.toString();
	}

	private ArrayList<Place> getContent(String url) {
		// final ArrayList<Place> arrayList = new ArrayList<Place>();
		JSONObject jsonParams = new JSONObject();
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						ArrayList<Place> arrayList = parseJsonObj(response);
						Utils.Log(TAG, "List place :" + arrayList + "");

					}

					@Override
					public void onError(int maloi) {
						Utils.Log("response", "Error");
					}

				}, new PWErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Utils.Log("VolleyError",
								" VolleyError : " + error.toString());
					}
				});
		return null;
	}

	private ArrayList<Place> parseJsonObj(JSONObject response) {
		JSONArray array = ParserUtils.getJsonArray(response, "results");

		ArrayList<Place> arrayList = new ArrayList<Place>();
		for (int i = 0; i < array.length(); i++) {
			try {
				Place place = Place.jsonToPontoReferencia(
						(JSONObject) array.get(i), b, neaBySearch);
				Log.v("Places Services ", "" + place);
                if (place.getName() == null || place.getName().trim().equalsIgnoreCase("")
                        || place.getName().equalsIgnoreCase(null)) {
                    continue;
                }
				arrayList.add(place);
			} catch (Exception e) {
			}
		}
		return arrayList;
	}

	protected String getJSON(String url) {
		return getUrlContents(url);
	}

	private String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			Utils.Log("getUrlContents", "Error converting result "
					+ e.toString());
		}
		return content.toString();
	}
}