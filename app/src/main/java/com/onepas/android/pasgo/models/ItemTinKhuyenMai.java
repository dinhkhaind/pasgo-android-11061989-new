package com.onepas.android.pasgo.models;

import com.onepas.android.pasgo.utils.DatehepperUtil;

public class ItemTinKhuyenMai {
	private String ngayBatDau;
	private String tieuDe;
	private String id;
	private String moTa;
	private int loaiTinKhuyenMai;
	private String anh;
	int read = 0;


	public ItemTinKhuyenMai() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public String getTieuDe() {
		return tieuDe;
	}

	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}

	public String getNgayBatDau() {
		return ngayBatDau;
	}
	public void setNgayBatDau(String ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public int getLoaiTinKhuyenMai() {
		return loaiTinKhuyenMai;
	}

	public void setLoaiTinKhuyenMai(int loaiTinKhuyenMai) {
		this.loaiTinKhuyenMai = loaiTinKhuyenMai;
	}

	public String getAnh() {
		return anh;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}
}
