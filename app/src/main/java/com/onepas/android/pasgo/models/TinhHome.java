package com.onepas.android.pasgo.models;

public class TinhHome {
	private String ten;
	private int id;
	private double viDo=0;
	private double kinhDo=0;
	private boolean isCheck;
	private boolean isParent;
	public TinhHome() {
	}

	public TinhHome(String ten, int id) {
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

	public double getViDo() {
		return viDo;
	}

	public void setViDo(double viDo) {
		this.viDo = viDo;
	}

	public double getKinhDo() {
		return kinhDo;
	}

	public void setKinhDo(double kinhDo) {
		this.kinhDo = kinhDo;
	}
}
