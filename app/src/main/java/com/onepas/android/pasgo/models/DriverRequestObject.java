package com.onepas.android.pasgo.models;

public class DriverRequestObject {
	private String id;
	private double lat;
	private double lng;

	public DriverRequestObject(String id, double lat, double lng) {
		this.id = id;
		this.setLat(lat);
		this.setLng(lng);
	}

	public String toString() {
		return "DriverRequestObject [id=" + id + ", lat=" + lat + ", lng="
				+ lng + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
}