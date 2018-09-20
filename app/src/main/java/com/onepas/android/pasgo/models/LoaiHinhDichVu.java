package com.onepas.android.pasgo.models;

import java.util.ArrayList;

public class LoaiHinhDichVu {
    private int loaiHinhDichVuId;
	private String tenLoaiHinh;
    private boolean phanLoaiNhomXe;
    private ArrayList<NhomDichVu> nhomDichVus;

    public int getLoaiHinhDichVuId() {
        return loaiHinhDichVuId;
    }

    public void setLoaiHinhDichVuId(int loaiHinhDichVuId) {
        this.loaiHinhDichVuId = loaiHinhDichVuId;
    }

    public String getTenLoaiHinh() {
        return tenLoaiHinh;
    }

    public void setTenLoaiHinh(String tenLoaiHinh) {
        this.tenLoaiHinh = tenLoaiHinh;
    }

    public boolean isPhanLoaiNhomXe() {
        return phanLoaiNhomXe;
    }

    public void setPhanLoaiNhomXe(boolean phanLoaiNhomXe) {
        this.phanLoaiNhomXe = phanLoaiNhomXe;
    }

    public ArrayList<NhomDichVu> getNhomDichVus() {
        return nhomDichVus;
    }

    public void setNhomDichVus(ArrayList<NhomDichVu> nhomDichVus) {
        this.nhomDichVus = nhomDichVus;
    }
}
