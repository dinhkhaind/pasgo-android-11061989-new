package com.onepas.android.pasgo.models;

import java.util.ArrayList;

public class LocationMessageClient {
	private String id;
	private String fullName;
	private String timeCome;
	private PTLocationInfo locationTo;
	private PTLocationInfo locationFrom;
	private String near;
	private String type;
	private int kindOfTaxiId;
	private String numberPhone;
	private long timeSend;
	private String distance;
	private String price;
	private String bookingId;
	private String provinceId;
	private String promotion;
	private ArrayList<String> driverNearId;

	public LocationMessageClient(String fullName, String numberPhone,
			String timeCome, PTLocationInfo locationTo,
			PTLocationInfo locationFrom, String near, long timeSend, String id, String promotion) {
		this.id = id;
		this.fullName = fullName;
		this.numberPhone = numberPhone;
		this.timeCome = timeCome;
		this.locationTo = locationTo;
		this.locationFrom = locationFrom;
		this.near = near;
		this.timeSend = timeSend;
		this.promotion=promotion;
	}

	public LocationMessageClient() {
		super();
	}
	

	public String toString() {
		return "LocationMessageClient [id=" + getId() + ", fullName="
				+ fullName + ", timeCome=" + getTimeCome() + ", locationTo="
				+ getLocationTo() + ", locationFrom=" + getLocationFrom()
				+ ", near=" + getNear() + ", type=" + type + ", kindOfTaxiId="
				+ getKindOfTaxiId() + ", numberPhone=" + getNumberPhone()
				+ ", timeSend=" + getTimeSend() 
				+ ", distance=" + distance
				+ ", promotion=" + promotion
				+ ", price=" + price + ", bookingId=" + bookingId
				+ ", driverNearId=" + driverNearId + ", provinceId="
				+ provinceId + "]";
	}
	
	

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNear() {
		return near;
	}

	public void setNear(String near) {
		this.near = near;
	}

	public PTLocationInfo getLocationFrom() {
		return locationFrom;
	}

	public void setLocationFrom(PTLocationInfo locationFrom) {
		this.locationFrom = locationFrom;
	}

	public PTLocationInfo getLocationTo() {
		return locationTo;
	}

	public void setLocationTo(PTLocationInfo locationTo) {
		this.locationTo = locationTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTimeCome() {
		return timeCome;
	}

	public void setTimeCome(String timeCome) {
		this.timeCome = timeCome;
	}

	public String getNumberPhone() {
		return numberPhone;
	}

	public void setNumberPhone(String numberPhone) {
		this.numberPhone = numberPhone;
	}

	public long getTimeSend() {
		return timeSend;
	}

	public void setTimeSend(long timeSend) {
		this.timeSend = timeSend;
	}

	public int getKindOfTaxiId() {
		return kindOfTaxiId;
	}

	public void setKindOfTaxiId(int kindOfTaxiId) {
		this.kindOfTaxiId = kindOfTaxiId;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public ArrayList<String> getDriverNearId() {
		return driverNearId;
	}

	public void setDriverNearId(ArrayList<String> driverNearId) {
		this.driverNearId = new ArrayList<String>();
		this.driverNearId = driverNearId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
}