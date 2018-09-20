package com.onepas.android.pasgo.models;

public class LocationMessageDriver implements Comparable<LocationMessageDriver> {
	private String id;
	private String fullName;
	private String numberPlate;
	private String taxiCompany;
	private PTLocationInfo location;
	private String numberPhone;
	private boolean isFree;
	private double distance;
    private int loaiHangId;
    private int nhomXeId;

	public LocationMessageDriver() {
		super();
	}

	public String toString() {
		String state = isFree ? "1" : "0";
		return "LocationMessageDriver [id=" + getId() + ", fullName="
				+ fullName + ", numberPlate=" + numberPlate + ", taxiCompany="
				+ getTaxiCompany() + ", location=" + getLocation()
				+ ", isFree=" + state + ", numberPhone=" + numberPhone + "]";
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaxiCompany() {
		return taxiCompany;
	}

	public void setTaxiCompany(String taxiCompany) {
		this.taxiCompany = taxiCompany;
	}

	public PTLocationInfo getLocation() {
		return location;
	}

	public void setLocation(PTLocationInfo location) {
		this.location = location;
	}

	public String getNumberPhone() {
		return numberPhone;
	}

	public void setNumberPhone(String numberPhone) {
		this.numberPhone = numberPhone;
	}

	@Override
	public int compareTo(LocationMessageDriver locationMessageDriver) {
		int distanceTwo = (int) (locationMessageDriver.getDistance() * 10000);
		int distanceOne = (int) (this.distance * 10000);
		return distanceOne - distanceTwo;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

    public int getLoaiHangId() {
        return loaiHangId;
    }

    public void setLoaiHangId(int loaiHangId) {
        this.loaiHangId = loaiHangId;
    }

    public int getNhomXeId() {
        return nhomXeId;
    }

    public void setNhomXeId(int nhomXeId) {
        this.nhomXeId = nhomXeId;
    }
}