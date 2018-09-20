package com.onepas.android.pasgo.models;

/**
 * Created by pasgo on 11/17/2016.
 */

public class HomeCategory {
    private String id;
    private String tieuDe;
    private String moTa;
    private String caption;
    private String anh;
    private int sapXep;
    private String doiTacKhuyenMaiId;
    private double chatLuong;
    private int tagId;
    private double danhGia;
    private boolean isDoiTacKhuyenMai;
    private String caption1;
    public HomeCategory(){}
    public HomeCategory(String id, String tieuDe, String moTa, String caption, String anh, int sapXep, String doiTacKhuyenMaiId, int a){
        this.id = id;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.anh = anh;
        this.caption = caption;
        this.doiTacKhuyenMaiId = doiTacKhuyenMaiId;
        this.sapXep = sapXep;
    }

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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public int getSapXep() {
        return sapXep;
    }

    public void setSapXep(int sapXep) {
        this.sapXep = sapXep;
    }

    public String getDoiTacKhuyenMaiId() {
        return doiTacKhuyenMaiId;
    }

    public void setDoiTacKhuyenMaiId(String doiTacKhuyenMaiId) {
        this.doiTacKhuyenMaiId = doiTacKhuyenMaiId;
    }

    public double getChatLuong() {
        return chatLuong;
    }

    public void setChatLuong(double chatLuong) {
        this.chatLuong = chatLuong;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public double getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(double danhGia) {
        this.danhGia = danhGia;
    }

    public boolean isDoiTacKhuyenMai() {
        return isDoiTacKhuyenMai;
    }

    public void setDoiTacKhuyenMai(boolean doiTacKhuyenMai) {
        isDoiTacKhuyenMai = doiTacKhuyenMai;
    }

    public String getCaption1() {
        return caption1;
    }

    public void setCaption1(String caption1) {
        this.caption1 = caption1;
    }
}
