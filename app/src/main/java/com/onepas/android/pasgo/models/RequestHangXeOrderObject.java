package com.onepas.android.pasgo.models;

import java.util.ArrayList;

public class RequestHangXeOrderObject {
	private String datXeId;
	private String khachHangId;
	private String tenKhachHang;
	private int nhomXeId;
	private String sdt;	
	private String maGiamGia;
	private String tienGiamGia;
	private String diaChiDonXe;
	private double viDoDonXe;
	private double kinhDoDonXe;
	private String thoiGianDonXe;
	private String diaChiDen;
	private double viDoDen;
	private double kinhDoDen;
	private String moTa;
	private double kmUocTinh;
	private String giaUocTinh;
	private ArrayList<HangXe> hangXes;
	private boolean datTruoc;
    private int soGiay;

	public RequestHangXeOrderObject() { 
	}
	public RequestHangXeOrderObject(String datXeId, String khachHangId,
			String tenKhachHang,
			int nhomXeId, String sdt,String maGiamGia,
			String tienGiamGia, String diaChiDonXe, double viDoDonXe,
			double kinhDoDonXe, String thoiGianDonXe
			, String diaChiDen, double viDoDen, double kinhDoDen,
			String moTa, double kmUocTinh, String giaUocTinh,ArrayList<HangXe> hangXes,boolean datTruoc, int soGiay) {
		super();
		this.datXeId = datXeId;
		this.khachHangId = khachHangId;
		this.tenKhachHang=tenKhachHang;
		this.nhomXeId = nhomXeId;		
		this.sdt = sdt;
		this.maGiamGia = maGiamGia;
		this.tienGiamGia = tienGiamGia;
		this.diaChiDonXe = diaChiDonXe;
		this.viDoDonXe = viDoDonXe;
		this.kinhDoDonXe = kinhDoDonXe;
		this.thoiGianDonXe = thoiGianDonXe;
		this.diaChiDen = diaChiDen;
		this.viDoDen = viDoDen;
		this.kinhDoDen = kinhDoDen;
		this.moTa = moTa;
		this.kmUocTinh = kmUocTinh;
		this.giaUocTinh = giaUocTinh;
		this.hangXes = hangXes;
		this.datTruoc=datTruoc;
        this.soGiay = soGiay;
	}

	public String toString() {
		return "RequestHangXeOrderObject [datXeId=" + datXeId 
				+ ", khachHangId=" + khachHangId 
				+ ", tenKhachHang=" + tenKhachHang
				+ ", nhomXeId=" + nhomXeId
				+ ", sdt=" + sdt
				+ ", maGiamGia=" + maGiamGia
				+ ", tienGiamGia=" + tienGiamGia
				+ ", diaChiDonXe="+ diaChiDonXe				
				+ ", viDoDonXe=" + viDoDonXe
				+ ", kinhDoDonXe="+ kinhDoDonXe 
				+ ", thoiGianDonXe=" + thoiGianDonXe
				+ ", diaChiDen=" + diaChiDen
				+ ", viDoDen=" + viDoDen
				+ ", kinhDoDen=" + kinhDoDen
				+ ", moTa=" + moTa
				+ ", kmUocTinh=" + kmUocTinh
                + ", soGiay=" + soGiay
				+ ", giaUocTinh=" + giaUocTinh + "]";
	}

    public int getSoGiay() {
        return soGiay;
    }

    public void setSoGiay(int soGiay) {
        this.soGiay = soGiay;
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

	public int getNhomXeId() {
		return nhomXeId;
	}

	public void setNhomXeId(int nhomXeId) {
		this.nhomXeId = nhomXeId;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getMaGiamGia() {
		return maGiamGia;
	}

	public void setMaGiamGia(String maGiamGia) {
		this.maGiamGia = maGiamGia;
	}

	public String getTienGiamGia() {
		return tienGiamGia;
	}

	public void setTienGiamGia(String tienGiamGia) {
		this.tienGiamGia = tienGiamGia;
	}

	public String getDiaChiDonXe() {
		return diaChiDonXe;
	}

	public void setDiaChiDonXe(String diaChiDonXe) {
		this.diaChiDonXe = diaChiDonXe;
	}

	public double getViDoDonXe() {
		return viDoDonXe;
	}

	public void setViDoDonXe(double viDoDonXe) {
		this.viDoDonXe = viDoDonXe;
	}

	public double getKinhDoDonXe() {
		return kinhDoDonXe;
	}

	public void setKinhDoDonXe(double kinhDoDonXe) {
		this.kinhDoDonXe = kinhDoDonXe;
	}

	public String getThoiGianDonXe() {
		return thoiGianDonXe;
	}

	public void setThoiGianDonXe(String thoiGianDonXe) {
		this.thoiGianDonXe = thoiGianDonXe;
	}

	public String getDiaChiDen() {
		return diaChiDen;
	}

	public void setDiaChiDen(String diaChiDen) {
		this.diaChiDen = diaChiDen;
	}

	public double getViDoDen() {
		return viDoDen;
	}

	public void setViDoDen(double viDoDen) {
		this.viDoDen = viDoDen;
	}

	public double getKinhDoDen() {
		return kinhDoDen;
	}

	public void setKinhDoDen(double kinhDoDen) {
		this.kinhDoDen = kinhDoDen;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public double getKmUocTinh() {
		return kmUocTinh;
	}

	public void setKmUocTinh(double kmUocTinh) {
		this.kmUocTinh = kmUocTinh;
	}

	public String getGiaUocTinh() {
		return giaUocTinh;
	}

	public void setGiaUocTinh(String giaUocTinh) {
		this.giaUocTinh = giaUocTinh;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public ArrayList<HangXe> getHangXes() {
		return hangXes;
	}

	public void setHangXes(ArrayList<HangXe> hangXes) {
		this.hangXes = hangXes;
	}

	public boolean isDatTruoc() {
		return datTruoc;
	}

	public void setDatTruoc(boolean datTruoc) {
		this.datTruoc = datTruoc;
	}
	
	

}