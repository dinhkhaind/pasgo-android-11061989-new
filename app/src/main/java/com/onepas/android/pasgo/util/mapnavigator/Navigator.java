package com.onepas.android.pasgo.util.mapnavigator;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

public class Navigator {
	private static final String TAG = "Navigation";
	private Context context;
	private LatLng startPosition, endPosition;
	private String mode;
	private boolean showCalc;
	private GoogleMap map;
	private Directions directions;
	private int pathColor = Color.BLUE;
	private int secondPath = Color.CYAN;
	private int thirdPath = Color.RED;
	private float pathWidth = 5;
	private boolean alternatives = false;
	private boolean mIsGetFirst = false;
	private int mColor;
	private long arrivalTime;
	private String avoid;
	private ArrayList<Polyline> lines = new ArrayList<Polyline>();
	private String strReturn, strDuongTrungGian;
	private NavigationListener navigationListener;
	private MapOnItemSelectedListener mapOnItemSelectedListener;
	private DistanceListener distanceListener;

	public Navigator(LatLng startLocation, LatLng endLocation,
			DistanceListener distanceListener) {
		this.startPosition = startLocation;
		this.endPosition = endLocation;
		this.distanceListener = distanceListener;
	}

	public Navigator(GoogleMap map, LatLng startLocation, LatLng endLocation) {
		this.startPosition = startLocation;
		this.endPosition = endLocation;
		this.map = map;
	}

	public Navigator(GoogleMap map, LatLng startLocation, LatLng endLocation,
			NavigationListener navigationListener) {
		this.startPosition = startLocation;
		this.endPosition = endLocation;
		this.map = map;
		this.navigationListener = navigationListener;
	}

	public Navigator(GoogleMap map, LatLng startLocation, LatLng endLocation,
			boolean isGetFirst, int color,
			MapOnItemSelectedListener mapOnItemSelectedListener) {
		this.startPosition = startLocation;
		this.endPosition = endLocation;
		this.map = map;
		this.mIsGetFirst = isGetFirst;
		this.mColor = color;
		this.mapOnItemSelectedListener = mapOnItemSelectedListener;
	}

	public Navigator(GoogleMap map, LatLng startLocation, LatLng endLocation,
			MapOnItemSelectedListener mapOnItemSelectedListener) {
		this.startPosition = startLocation;
		this.endPosition = endLocation;
		this.map = map;
		this.mapOnItemSelectedListener = mapOnItemSelectedListener;
	}

	public Navigator(LatLng startLocation, LatLng endLocation,
			String strDuongTrungGian,
			MapOnItemSelectedListener mapOnItemSelectedListener) {
		this.startPosition = startLocation;
		this.endPosition = endLocation;
		this.strDuongTrungGian = strDuongTrungGian;
		this.mapOnItemSelectedListener = mapOnItemSelectedListener;
	}

	/**
	 * Gets the starting location for the directions
	 * 
	 */
	public LatLng getStartPoint() {
		return startPosition;
	}

	/**
	 * Gets the end location for the directions
	 * 
	 */
	public LatLng getEndPoint() {
		return endPosition;
	}


	public void findDistance(Context context, boolean isShowDialog) {
		this.context = context;
		this.showCalc = isShowDialog;
		new Distance(context).execute();
	}

	public void findDirections(boolean showDialog, boolean findAlternatives) {
		this.alternatives = findAlternatives;
		new PathCreator(mIsGetFirst).execute();
	}

	public void findDirectionsJson(boolean showDialog,
			boolean findAlternatives, String arrayListWaypoints) {
		this.alternatives = findAlternatives;
		new PathCreatorJson(arrayListWaypoints).execute();
	}

	/**
	 * Sets the type of direction you want (driving,walking,biking or mass
	 * transit). This MUST be called before getting the directions If using
	 * "transit" mode you must provide an arrival time
	 * 
	 * @param mode
	 *            The type of directions you want (driving,walking,biking or
	 *            mass transit)
	 * @param arrivalTime
	 *            If selected mode it "transit" you must provide and arrival
	 *            time (milliseconds since January 1, 1970 UTC). If arrival time
	 *            is not given the current time is given and may return
	 *            unexpected results.
	 */
	public void setMode(int mode, long arrivalTime, int avoid) {
		switch (mode) {

		case 0:
			this.mode = "driving";
			break;
		case 1:
			this.mode = "driving"; // "bicycling";
			break;
		case 2:
			this.mode = "transit";
			this.arrivalTime = arrivalTime;
			break;
		case 3:
			this.mode = "walking";
			break;
		default:
			this.mode = "driving";
			break;
		}

		switch (avoid) {
		case 0:
			this.avoid = "tolls";
			break;
		case 1:
			this.avoid = "highways";
			break;
		default:
			break;
		}
	}

	/**
	 * Get all direction information
	 * 
	 * @return
	 */
	public Directions getDirections() {
		return directions;
	}

	/**
	 * Change the color of the path line, must be called before calling
	 * findDirections().
	 * 
	 * @param firstPath
	 *            Color of the first line, default color is blue.
	 * @param secondPath
	 *            Color of the second line, default color is cyan
	 * @param thirdPath
	 *            Color of the third line, default color is red
	 * 
	 */
	public void setPathColor(int firstPath, int secondPath, int thirdPath) {
		pathColor = firstPath;
	}

	/**
	 * Change the width of the path line
	 * 
	 * @param width
	 *            Width of the line, default width is 3
	 */
	public void setPathLineWidth(float width) {
		pathWidth = width;
	}

	private Polyline showPath(Route route, int color) {
		return map.addPolyline(new PolylineOptions().addAll(route.getPath())
				.color(color).width(pathWidth));
	}

	public ArrayList<Polyline> getPathLines() {
		return lines;
	}

	private class PathCreator extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;
		private boolean isGetFirst;

		public PathCreator(boolean isGetFirst) {
			this.isGetFirst = isGetFirst;
		}

		@Override
		protected void onPreExecute() {
			if (showCalc) {
				pd = new ProgressDialog(context);
				pd.setMessage("Getting Directions");
				pd.setIndeterminate(true);
				pd.show();
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			if (mode == null) {
				mode = "driving";
			}

			String url = String
					.format("https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&sensor=false&mode=%s&alternatives=false&language=vi&key="
							+ Constants.API_MAP, startPosition.latitude,
							startPosition.longitude, endPosition.latitude,
							endPosition.longitude, mode);
			if (!StringUtils.isEmpty(strDuongTrungGian))
				url += "&waypoints=" + strDuongTrungGian;

			if (mode.equals("transit")) {
				if (arrivalTime > 0) {
					url += "&arrival_time=" + arrivalTime;
				} else {

					url += "&departure_time=1"; // + System.currentTimeMillis();
				}
			}

			if (avoid != null) {
				url += "&avoid=" + avoid;
			}
			Utils.Log(TAG, "url direction json" + url);


			return url;
		}

		@Override
		protected void onPostExecute(String url) {
			Pasgo.getInstance().addToRequestGet(url, new Pasgo.PWListener() {
				@Override
				public void onResponse(JSONObject json) {
					if (json != null) {
						strReturn = json.toString();
						Directions directions = new Directions(strReturn);
						if (directions != null) {

							Navigator.this.directions = directions;
							if (!isGetFirst)
								for (int i = 0; i < directions.getRoutes().size(); i++) {
									Route r = directions.getRoutes().get(i);
									if (i == 0) {
										lines.add(showPath(r, pathColor));
									} else if (i == 1) {
										lines.add(showPath(r, secondPath));
									} else if (i == 3) {
										lines.add(showPath(r, thirdPath));
									}
								}
							else {
								Route r = directions.getRoutes().get(0);
								lines.add(showPath(r, mColor));
							}
							if (navigationListener != null) {
								navigationListener.loadData(directions, strReturn);
							}
							if (mapOnItemSelectedListener != null)
								mapOnItemSelectedListener.loadData(directions, strReturn);

						}

						if (showCalc && pd != null) {
							pd.dismiss();
						}

					}
				}

				@Override
				public void onError(int maloi) {
				}

			}, new Pasgo.PWErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				}
			});

		}
	}

	private class PathCreatorJson extends AsyncTask<Void, Void, String> {
		private ProgressDialog pd;
		private String mWaypoint;

		private PathCreatorJson(String wayPoint) {
			this.mWaypoint = wayPoint;
		}

		@Override
		protected void onPreExecute() {
			if (showCalc) {
				pd = new ProgressDialog(context);
				pd.setMessage("Getting Directions");
				pd.setIndeterminate(true);
				pd.show();
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			if (mode == null) {
				mode = "driving";
			}
			String url = String
					.format("https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&sensor=false&mode=%s&alternatives=true&language=vi&key=%s",
							startPosition.latitude, startPosition.longitude,
							endPosition.latitude, endPosition.longitude, mode,
							Constants.API_MAP);
			if (!StringUtils.isEmpty(strDuongTrungGian))
				url += "&waypoints=" + Uri.encode(mWaypoint);

			if (mode.equals("transit")) {
				if (arrivalTime > 0) {
					url += "&arrival_time=" + arrivalTime;
				} else {
					long time = System.currentTimeMillis() / 1000;
					url += "&departure_time=" + time;
				}
			}

			if (avoid != null) {
				url += "&avoid=" + avoid;
			}

			return url;
		}

		@Override
		protected void onPostExecute(String url) {
			Pasgo.getInstance().addToRequestGet(url, new Pasgo.PWListener() {
				@Override
				public void onResponse(JSONObject json) {
					if (json != null) {
						strReturn = json.toString();
						Directions directions = new Directions(strReturn);
						if (directions != null) {
							if (directions != null) {
								if (navigationListener != null) {
									navigationListener.loadData(directions, strReturn);
								}
								if (mapOnItemSelectedListener != null)
									mapOnItemSelectedListener.loadData(directions, strReturn);
							}
							if (showCalc && pd != null) {
								pd.dismiss();
							}
						}

						if (showCalc && pd != null) {
							pd.dismiss();
						}

					}
				}

				@Override
				public void onError(int maloi) {
				}

			}, new Pasgo.PWErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				}
			});

		}
	}

	private class Distance extends AsyncTask<Void, Void, String> {
		private ProgressDialog pd;
		Context context;

		public Distance(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			if (showCalc) {
				pd = new ProgressDialog(context);
				pd.setMessage(context.getResources().getString(
						R.string.do_khoang_cach_duong_di));
				pd.setIndeterminate(true);
				pd.show();
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			if (mode == null) {
				mode = "driving";
			}

			String url = String
					.format("https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&sensor=false&mode=%s&alternatives=true&language=vi&key="
							+ Constants.API_MAP, startPosition.latitude,
							startPosition.longitude, endPosition.latitude,
							endPosition.longitude, mode);
			if (!StringUtils.isEmpty(strDuongTrungGian))
				url += "&waypoints=" + strDuongTrungGian;

			if (mode.equals("transit")) {
				if (arrivalTime > 0) {
					url += "&arrival_time=" + arrivalTime;
				} else {

					url += "&departure_time=1"; // + System.currentTimeMillis();
				}
			}

			if (avoid != null) {
				url += "&avoid=" + avoid;
			}
			Utils.Log(TAG, "url direction json" + url);
			return url;
		}

		@Override
		protected void onPostExecute(String url) {
			Pasgo.getInstance().addToRequestGet(url, new Pasgo.PWListener() {
				@Override
				public void onResponse(JSONObject json) {
					if (json != null) {
						strReturn = json.toString();
						Directions directions = new Directions(strReturn);
						if (directions != null) {
                            ArrayList<String> arrayList = directions.parseDistance();
                            distanceListener.loadData(arrayList, strReturn);
                        }
						if (showCalc) {
                            if (pd != null) {
                                pd.dismiss();
                            }
                        }

					}
				}

				@Override
				public void onError(int maloi) {
				}

			}, new Pasgo.PWErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				}
			});
		}
	}

	public void DrawSingle(String strDirections, int position) {
		Directions directions = new Directions(strDirections);
		if (directions != null) {
			Navigator.this.directions = directions;
			for (int i = 0; i < directions.getRoutes().size(); i++) {
				Route r = directions.getRoutes().get(i);
				if (i == position) {
					lines.add(showPath(r, pathColor));
				}
			}
		}
	}
}