package com.onepas.android.pasgo.models;

public class HangXe {
	private String hangXeId;
	private String tenHang;
	public HangXe() {
		
	}
	public HangXe(String hangXeId, String tenHang)
	{
		this.hangXeId=hangXeId;
		this.tenHang=tenHang;
	}
	public String getHangXeId() {
		return hangXeId;
	}
	public void setHangXeId(String hangXeId) {
		this.hangXeId = hangXeId;
	}
	public String getTenHang() {
		return tenHang;
	}
	public void setTenHang(String tenHang) {
		this.tenHang = tenHang;
	}
	public String toString() {
		return "KhachHang [hangXeId=" + hangXeId 
				+ ", tenHang=" + tenHang + "]";
	}
}
