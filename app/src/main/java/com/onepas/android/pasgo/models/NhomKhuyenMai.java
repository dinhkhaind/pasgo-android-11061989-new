package com.onepas.android.pasgo.models;

public class NhomKhuyenMai {
	private String ten;
	private String ma;
	private int id;
	private boolean isCheck;
	private boolean isParent;
	public NhomKhuyenMai() {
	}

	public NhomKhuyenMai(String ten, String ma, int id) {
		this.ten = ten;
		this.ma = ma;
		this.id = id;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getMa() {
		return ma;
	}

	public void setMa(String ma) {
		this.ma = ma;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
}
