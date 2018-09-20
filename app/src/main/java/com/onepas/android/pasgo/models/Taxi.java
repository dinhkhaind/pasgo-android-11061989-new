package com.onepas.android.pasgo.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Taxi implements Serializable {
	private String id;
	private String name;
	private String info;
	private String place;
	private String address;
	private String priceOpenDoor;
	private String phoneNumber;
	private String urlPhoto;
	private int sdt;
	String sDT;
	String tenHangXe;
	String thoiGian;

	public Taxi() {
	}

	public Taxi(String name, String priceOpenDoor, String phoneNumber) {
		this.name = name;
		this.priceOpenDoor = priceOpenDoor;
		this.phoneNumber = phoneNumber;
	}

	public String getThoiGian() {
		return thoiGian;
	}

	public void setThoiGian(String thoiGian) {
		this.thoiGian = thoiGian;
	}

	public String getTenHangXe() {
		return tenHangXe;
	}

	public void setTenHangXe(String tenHangXe) {
		this.tenHangXe = tenHangXe;
	}

	public String getsDT() {
		return sDT;
	}

	public void setsDT(String sDT) {
		this.sDT = sDT;
	}

	public int getSdt() {
		return sdt;
	}

	public void setSdt(int sdt) {
		this.sdt = sdt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPriceOpenDoor() {
		return priceOpenDoor;
	}

	public void setPriceOpenDoor(String priceOpenDoor) {
		this.priceOpenDoor = priceOpenDoor;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUrlPhoto() {
		return urlPhoto;
	}

	public void setUrlPhoto(String urlPhoto) {
		this.urlPhoto = urlPhoto;
	}
}


