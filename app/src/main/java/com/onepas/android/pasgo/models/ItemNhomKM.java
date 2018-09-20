package com.onepas.android.pasgo.models;

import java.util.ArrayList;

public class ItemNhomKM {
	private String ten;
	private String ma;
	private int id;
	private boolean isCheck;
	private int drawableId = 0;
	private int drawableIdSelected =0;
	private ArrayList<TagModel> diemDenTagModelList;
	public ItemNhomKM() {
	}

	public ItemNhomKM(String ten, String ma, int id) {
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

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public int getDrawableIdSelected() {
		return drawableIdSelected;
	}

	public void setDrawableIdSelected(int drawableIdSelected) {
		this.drawableIdSelected = drawableIdSelected;
	}

	public ArrayList<TagModel> getDiemDenTagModelList() {
		return diemDenTagModelList;
	}

	public void setDiemDenTagModelList(ArrayList<TagModel> diemDenTagModelList) {
		this.diemDenTagModelList = diemDenTagModelList;
	}

}
