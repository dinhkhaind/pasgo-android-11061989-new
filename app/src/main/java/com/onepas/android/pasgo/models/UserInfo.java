package com.onepas.android.pasgo.models;

import java.util.ArrayList;

/**
 * Created by Khai on 3/19/2015.
 */
public class UserInfo {
    private boolean gioiTinh;
    private String idCode;
    private String id;
    private String email;
    private String Sdt;
    private String tenNguoiDung;
    private String ngaySinh;
    private String urlAnh;
    private ArrayList<LinhVucQuanTam> linhVucQuanTams;

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return Sdt;
    }

    public void setSdt(String sdt) {
        Sdt = sdt;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getUrlAnh() {
        return urlAnh;
    }

    public void setUrlAnh(String urlAnh) {
        this.urlAnh = urlAnh;
    }

    public ArrayList<LinhVucQuanTam> getLinhVucQuanTams() {
        return linhVucQuanTams;
    }

    public void setLinhVucQuanTams(ArrayList<LinhVucQuanTam> linhVucQuanTams) {
        this.linhVucQuanTams = linhVucQuanTams;
    }

}
