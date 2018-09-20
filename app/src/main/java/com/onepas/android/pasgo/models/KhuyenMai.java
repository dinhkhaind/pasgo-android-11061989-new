package com.onepas.android.pasgo.models;

public class KhuyenMai {
	private String id;
	private String noiDung;
	private String phanTram;
	private String tieuDe;
	private double phanTramGiamGia;
    private boolean datTruoc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public String getPhanTram() {
		return phanTram;
	}

	public void setPhanTram(String phanTram) {
		this.phanTram = phanTram;
	}

	public String getTieuDe() {
		return tieuDe;
	}

	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}

	public double getPhanTramGiamGia() {
		return phanTramGiamGia;
	}

	public void setPhanTramGiamGia(double phanTramGiamGia) {
		this.phanTramGiamGia = phanTramGiamGia;
	}

    public boolean isDatTruoc() {
        return datTruoc;
    }

    public void setDatTruoc(boolean datTruoc) {
        this.datTruoc = datTruoc;
    }
}
