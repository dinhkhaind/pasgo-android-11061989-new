package com.onepas.android.pasgo.util.mapnavigator;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Directions {

	private ArrayList<Route> routes = new ArrayList<Route>();
	private String directions;

	public enum DrivingMode {
		DRIVING, MASS_TRANSIT, BYCICLE, WALKING
	}

	public enum Avoid {
		TOLLS, HIGHWAYS, NONE
	}

	public Directions(String directions) {
		this.directions = directions;
		if (directions != null) {
			parseDirections();
		}

	}

	private void parseDirections() {
		try {
			JSONObject json = new JSONObject(directions);

			if (!json.isNull("routes")) {
				JSONArray route = json.getJSONArray("routes");

				for (int k = 0; k < route.length(); k++) {

					JSONObject obj3 = route.getJSONObject(k);
					routes.add(new Route(obj3));
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> parseDistance() {
		ArrayList<String> sDistance = new ArrayList<String>();
		try {
			JSONObject json = new JSONObject(directions);

			if (!json.isNull("routes")) {
				JSONArray route = json.getJSONArray("routes");

				for (int k = 0; k < route.length(); k++) {
					JSONObject objLeg = route.getJSONObject(k);
					JSONArray legs = objLeg.getJSONArray("legs");
					for (int h = 0; h < legs.length(); h++) {
						JSONObject jsonLeg = legs.getJSONObject(h);
						String distance = jsonLeg.getString("distance");
						sDistance.add(distance);
					}
					JSONObject obj3 = route.getJSONObject(k);
					routes.add(new Route(obj3));
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sDistance;
	}

	public ArrayList<Route> getRoutes() {
		return routes;
	}

}
