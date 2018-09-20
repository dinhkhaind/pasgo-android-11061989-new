package com.onepas.android.pasgo.models;

public class LichSuChuyenDiItem {
	private String iD="";
	private String datXeId="";
	private String diemDon="c";
	private String diemDen="c";
	private String dateTime="c";
	private boolean HangXe;
	private boolean KhuyenMai;
	private int month;
	private int year;
	private int key=0;
	private String tienKhuyenMai;
	private String tenLoaiHinh;
	private String tienKhuyenMaiFormat;
	private int loaiHinhId;
	private int isThanhCong;
	public LichSuChuyenDiItem(){}
	public LichSuChuyenDiItem(int key,int year){this.key=key; this.year = year;}
	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public String getDatXeId() {
		return datXeId;
	}

	public void setDatXeId(String datXeId) {
		this.datXeId = datXeId;
	}

	public String getDiemDon() {
		return diemDon;
	}

	public void setDiemDon(String diemDon) {
		this.diemDon = diemDon;
	}

	public String getDiemDen() {
		return diemDen;
	}

	public void setDiemDen(String diemDen) {
		this.diemDen = diemDen;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isHangXe() {
		return HangXe;
	}

	public void setHangXe(boolean hangXe) {
		HangXe = hangXe;
	}

	public boolean isKhuyenMai() {
		return KhuyenMai;
	}

	public void setKhuyenMai(boolean khuyenMai) {
		KhuyenMai = khuyenMai;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getTienKhuyenMai() {
		return tienKhuyenMai;
	}

	public void setTienKhuyenMai(String tienKhuyenMai) {
		this.tienKhuyenMai = tienKhuyenMai;
	}

	public String getTenLoaiHinh() {
		return tenLoaiHinh;
	}

	public void setTenLoaiHinh(String tenLoaiHinh) {
		this.tenLoaiHinh = tenLoaiHinh;
	}

	public String getTienKhuyenMaiFormat() {
		return tienKhuyenMaiFormat;
	}

	public void setTienKhuyenMaiFormat(String tienKhuyenMaiFormat) {
		this.tienKhuyenMaiFormat = tienKhuyenMaiFormat;
	}

	public int getLoaiHinhId() {
		return loaiHinhId;
	}

	public void setLoaiHinhId(int loaiHinhId) {
		this.loaiHinhId = loaiHinhId;
	}

	public int getIsThanhCong() {
		return isThanhCong;
	}

	public void setIsThanhCong(int isThanhCong) {
		this.isThanhCong = isThanhCong;
	}
}
