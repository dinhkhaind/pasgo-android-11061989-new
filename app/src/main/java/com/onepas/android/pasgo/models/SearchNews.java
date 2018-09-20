package com.onepas.android.pasgo.models;

/**
 * Created by VuDinh on 9/14/2016.
 */
public class SearchNews {
    public String id;
    public String tieuDe;
    public String anh;
    public String ngayBatDau;
    public String moTa;
    public int loaiTinKhuyenMai;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
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
}
