package com.onepas.android.pasgo.models;

public class PTLocationInfo {
	private String address;
	private double lat;
	private double lng;

	public PTLocationInfo() {
		super();
	}

	public PTLocationInfo(String address, double lat, double lng) {
		super();
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}

	public String toString() {
		return "location [lat=" + lat + ", lng=" + lng + ", address=" + address
				+ "]";
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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