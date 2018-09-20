package com.onepas.android.pasgo.models;

public class RequestOrderObject {
	private String khachHangId;
	private int nhomXeId;
	private String sdt;
	private String maGiamGia;
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
	private String laiXe;
	private int loaiDatXe;
	private String datXeId;
    private int loaiHinhDichVuId;
    private int dichVuId;
	public RequestOrderObject() {
	}

	public RequestOrderObject(String datXeId) {
		this.datXeId = datXeId;
	}

	public RequestOrderObject(String khachHangId, int nhomXeDichVuId, String sdt,
			String maGiamGia, String diaChiDonXe, double viDoDonXe,
			double kinhDoDonXe, String thoiGianDonXe, String diaChiDen,
			double viDoDen, double kinhDoDen, String moTa, double kmUocTinh,
			String giaUocTinh, int loaiDatXe, int loaiHinhDichVuId, int dichVuId) {
		super();
		this.khachHangId = khachHangId;
		this.nhomXeId = nhomXeDichVuId;
		this.sdt = sdt;
		this.maGiamGia = maGiamGia;
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
		this.loaiDatXe = loaiDatXe;
        this.loaiHinhDichVuId = loaiHinhDichVuId;
        this.dichVuId = dichVuId;
	}

	public String toString() {
		return "RequestOrderObject [khachHangId=" + khachHangId + ", nhomXeId="
				+ nhomXeId + ", sdt=" + sdt + ", maGiamGia=" + maGiamGia
				+ ", diaChiDonXe=" + diaChiDonXe + ", viDoDonXe=" + viDoDonXe
				+ ", kinhDoDonXe=" + kinhDoDonXe + ", thoiGianDonXe="
				+ thoiGianDonXe + ", diaChiDen=" + diaChiDen + ", viDoDen="
				+ viDoDen + ", kinhDoDen=" + kinhDoDen + ", moTa=" + moTa
				+ ", kmUocTinh=" + kmUocTinh + ", giaUocTinh=" + giaUocTinh
				+ ", laiXe=" + laiXe.toString() + ", loaiDatXe=" + loaiDatXe
				+ ", datXeId=" + datXeId + "]";
	}

	public RequestOrderObject(String khachHangId, int nhomXeId,
			String maGiamGia, String diaChiDonXe, double viDoDonXe,
			double kinhDoDonXe, String thoiGianDonXe, String diaChiDen,
			double viDoDen, double kinhDoDen, String moTa, double kmUocTinh,
			String giaUocTinh, String driverRequestObjects) {
		this.khachHangId = khachHangId;
		this.nhomXeId = nhomXeId;
		this.maGiamGia = maGiamGia;
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
		this.laiXe = driverRequestObjects;
	}

	public String getDriverRequestObjects() {
		return laiXe;
	}

	public void setDriverRequestObjects(String driverRequestObjects) {
		this.laiXe = driverRequestObjects;
	}

	public String getGiaUocTinh() {
		return giaUocTinh;
	}

	public void setGiaUocTinh(String giaUocTinh) {
		this.giaUocTinh = giaUocTinh;
	}

	public double getKmUocTinh() {
		return kmUocTinh;
	}

	public void setKmUocTinh(double kmUocTinh) {
		this.kmUocTinh = kmUocTinh;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public double getKinhDoDen() {
		return kinhDoDen;
	}

	public void setKinhDoDen(double kinhDoDen) {
		this.kinhDoDen = kinhDoDen;
	}

	public double getViDoDen() {
		return viDoDen;
	}

	public void setViDoDen(double viDoDen) {
		this.viDoDen = viDoDen;
	}

	public String getDiaChiDen() {
		return diaChiDen;
	}

	public void setDiaChiDen(String diaChiDen) {
		this.diaChiDen = diaChiDen;
	}

	public String getThoiGianDonXe() {
		return thoiGianDonXe;
	}

	public void setThoiGianDonXe(String thoiGianDonXe) {
		this.thoiGianDonXe = thoiGianDonXe;
	}

	public double getKinhDoDonXe() {
		return kinhDoDonXe;
	}

	public void setKinhDoDonXe(double kinhDoDonXe) {
		this.kinhDoDonXe = kinhDoDonXe;
	}

	public double getViDoDonXe() {
		return viDoDonXe;
	}

	public void setViDoDonXe(double viDoDonXe) {
		this.viDoDonXe = viDoDonXe;
	}

	public String getDiaChiDonXe() {
		return diaChiDonXe;
	}

	public void setDiaChiDonXe(String diaChiDonXe) {
		this.diaChiDonXe = diaChiDonXe;
	}

	public String getMaGiamGia() {
		return maGiamGia;
	}

	public void setMaGiamGia(String maGiamGia) {
		this.maGiamGia = maGiamGia;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public int getNhomXeId() {
		return nhomXeId;
	}

	public void setNhomXeId(int nhomXeId) {
		this.nhomXeId = nhomXeId;
	}

	public String getKhachHangId() {
		return khachHangId;
	}

	public void setKhachHangId(String khachHangId) {
		this.khachHangId = khachHangId;
	}

	public int getLoaiDatXe() {
		return loaiDatXe;
	}

	public void setLoaiDatXe(int loaiDatXe) {
		this.loaiDatXe = loaiDatXe;
	}

	public String getDatXeId() {
		return datXeId;
	}

	public void setDatXeId(String datXeId) {
		this.datXeId = datXeId;
	}

    public String getLaiXe() {
        return laiXe;
    }

    public void setLaiXe(String laiXe) {
        this.laiXe = laiXe;
    }

    public int getLoaiHinhDichVuId() {
        return loaiHinhDichVuId;
    }

    public void setLoaiHinhDichVuId(int loaiHinhDichVuId) {
        this.loaiHinhDichVuId = loaiHinhDichVuId;
    }

    public int getDichVuId() {
        return dichVuId;
    }

    public void setDichVuId(int loaiHangId) {
        this.dichVuId = loaiHangId;
    }
}