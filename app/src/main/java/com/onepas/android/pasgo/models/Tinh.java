package com.onepas.android.pasgo.models;

public class Tinh {
	private String ten;
	private int id;
	private boolean isCheck;
	private boolean isParent;
	public Tinh() {
	}

	public Tinh(String ten, int id) {
		this.ten = ten;
		this.id = id;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
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
