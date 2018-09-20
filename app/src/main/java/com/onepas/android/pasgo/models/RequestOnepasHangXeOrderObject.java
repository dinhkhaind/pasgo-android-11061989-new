package com.onepas.android.pasgo.models;

public class RequestOnepasHangXeOrderObject {
	private String datXeId;
	private String khachHangId;
	private int trangThai;

	public RequestOnepasHangXeOrderObject() {
	}

	public RequestOnepasHangXeOrderObject(String datXeId) {
		this.datXeId = datXeId;
	}

	public RequestOnepasHangXeOrderObject(String datXeId, String khachHangId,
			int trangThai) {
		super();
		this.datXeId = datXeId;
		this.khachHangId = khachHangId;
		this.trangThai = trangThai;
	}

	public String getDatXeId() {
		return datXeId;
	}

	public void setDatXeId(String datXeId) {
		this.datXeId = datXeId;
	}

	public String getKhachHangId() {
		return khachHangId;
	}

	public void setKhachHangId(String khachHangId) {
		this.khachHangId = khachHangId;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

}