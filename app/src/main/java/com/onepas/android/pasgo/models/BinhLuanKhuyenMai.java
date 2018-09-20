package com.onepas.android.pasgo.models;

public class BinhLuanKhuyenMai {
	private String luotThich="";
	private int daThich;
	private String id="";
	private String thoiGian="";
	private String noiDung="";
	private String tenNguoiDung="";
    private double danhGia;
    private String nguoiDungId="";
    private boolean isBestBinhLuan=false;

	public String getLuotThich() {
		return luotThich;
	}

	public void setLuotThich(String luotThich) {
		this.luotThich = luotThich;
	}

	public int getDaThich() {
		return daThich;
	}

	public void setDaThich(int daThich) {
		this.daThich = daThich;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThoiGian() {
		return thoiGian;
	}

	public void setThoiGian(String thoiGian) {
		this.thoiGian = thoiGian;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public String getTenNguoiDung() {
		return tenNguoiDung;
	}

	public void setTenNguoiDung(String tenNguoiDung) {
		this.tenNguoiDung = tenNguoiDung;
	}

    public double getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(double danhGia) {
        this.danhGia = danhGia;
    }

    public boolean isBestBinhLuan() {
        return isBestBinhLuan;
    }

    public void setBestBinhLuan(boolean isBestBinhLuan) {
        this.isBestBinhLuan = isBestBinhLuan;
    }

    public String getNguoiDungId() {
        return nguoiDungId;
    }

    public void setNguoiDungId(String nguoiDungId) {
        this.nguoiDungId = nguoiDungId;
    }
}
