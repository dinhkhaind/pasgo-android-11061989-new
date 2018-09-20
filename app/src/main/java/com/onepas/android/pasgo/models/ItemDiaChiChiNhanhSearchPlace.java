package com.onepas.android.pasgo.models;

public class ItemDiaChiChiNhanhSearchPlace {
	private String id;
	private String ten;
	private String diaChi;
	private String nhomCnDoiTacId;
	private String moTa;
	private double kinhDo;
	private double viDo;
	private String maNhomKM;
	private String titleKM;
	private String website;
	private String doiTacKhuyenMaiId;
	private boolean datTruoc;
    private String chiNhanhDoiTacId;
    private int distanceWithCenter=0;
	public ItemDiaChiChiNhanhSearchPlace() {
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getNhomCnDoiTacId() {
		return nhomCnDoiTacId;
	}

	public void setNhomCnDoiTacId(String nhomCnDoiTacId) {
		this.nhomCnDoiTacId = nhomCnDoiTacId;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public double getKinhDo() {
		return kinhDo;
	}

	public void setKinhDo(double kinhDo) {
		this.kinhDo = kinhDo;
	}

	public double getViDo() {
		return viDo;
	}

	public void setViDo(double viDo) {
		this.viDo = viDo;
	}

	public String getMaNhomKM() {
		return maNhomKM;
	}

	public void setMaNhomKM(String maNhomKM) {
		this.maNhomKM = maNhomKM;
	}

	public String getTitleKM() {
		return titleKM;
	}

	public void setTitleKM(String titleKM) {
		this.titleKM = titleKM;
	}

	public String getDoiTacKhuyenMaiId() {
		return doiTacKhuyenMaiId;
	}

	public void setDoiTacKhuyenMaiId(String doiTacKhuyenMaiId) {
		this.doiTacKhuyenMaiId = doiTacKhuyenMaiId;
	}

	public boolean isDatTruoc() {
		return datTruoc;
	}

	public void setDatTruoc(boolean datTruoc) {
		this.datTruoc = datTruoc;
	}

    public String getChiNhanhDoiTacId() {
        return chiNhanhDoiTacId;
    }

    public void setChiNhanhDoiTacId(String chiNhanhDoiTacId) {
        this.chiNhanhDoiTacId = chiNhanhDoiTacId;
    }

    public int getDistanceWithCenter() {
        return distanceWithCenter;
    }

    public void setDistanceWithCenter(double distanceWithCenter) {
        this.distanceWithCenter = (int)distanceWithCenter;
    }
}