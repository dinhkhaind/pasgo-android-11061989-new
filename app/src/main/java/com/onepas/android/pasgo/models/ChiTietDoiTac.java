package com.onepas.android.pasgo.models;

import java.util.ArrayList;

/**
 * Created by VuDinh on 9/27/2016.
 */

public class ChiTietDoiTac {
    private String bangGia;
    private String moTaUuDai;
    private String moTaGioiThieu;
    private String tongDaiPASGO;
    private boolean daCheckin;
    private ArrayList<BinhLuan> binhLuans;
    private ArrayList<AnhSlide> anhSlides ;
    private String tenNhaHang;
    private String gioPhucVu;
    private String khoangGia;
    private String diaChi;
    private ArrayList<DoiTacLienQuan> doiTacLienQuans;
    private ArrayList<AnhBangGia> anhBangGias;
    private double viDo;
    private double kinhDo;
    private String id="";
    private ArrayList<AnhList> anhLists;
    private double chatLuong;
    private String chuyenMon;
    private String dacTrung;
    private double danhGia;
    private int daYeuThich;
    private String urlContent;
    private int trangThai;

    private boolean pheDuyet;
    private String nhomCnDoiTacId;
    private String doiTacId;
    private int nhomKhuyenMaiId;
    private String doiTacKhuyenMaiId;
    private String logo;
    private String maNhomKhuyenMai;
    private int loaiHopDong;

    public String getBangGia() {
        return bangGia;
    }

    public void setBangGia(String bangGia) {
        this.bangGia = bangGia;
    }

    public String getMoTaUuDai() {
        return moTaUuDai;
    }

    public void setMoTaUuDai(String moTaUuDai) {
        this.moTaUuDai = moTaUuDai;
    }

    public String getMoTaGioiThieu() {
        return moTaGioiThieu;
    }

    public void setMoTaGioiThieu(String moTaGioiThieu) {
        this.moTaGioiThieu = moTaGioiThieu;
    }

    public String getTongDaiPASGO() {
        return tongDaiPASGO;
    }

    public void setTongDaiPASGO(String tongDaiPASGO) {
        this.tongDaiPASGO = tongDaiPASGO;
    }

    public boolean isDaCheckin() {
        return daCheckin;
    }

    public void setDaCheckin(boolean daCheckin) {
        this.daCheckin = daCheckin;
    }

    public ArrayList<BinhLuan> getBinhLuans() {
        return binhLuans;
    }

    public void setBinhLuans(ArrayList<BinhLuan> binhLuans) {
        this.binhLuans = binhLuans;
    }

    public ArrayList<AnhSlide> getAnhSlides() {
        return anhSlides;
    }

    public void setAnhSlides(ArrayList<AnhSlide> anhSlides) {
        this.anhSlides = anhSlides;
    }

    public String getTenNhaHang() {
        return tenNhaHang;
    }

    public void setTenNhaHang(String tenNhaHang) {
        this.tenNhaHang = tenNhaHang;
    }

    public String getGioPhucVu() {
        return gioPhucVu;
    }

    public void setGioPhucVu(String gioPhucVu) {
        this.gioPhucVu = gioPhucVu;
    }

    public String getKhoangGia() {
        return khoangGia;
    }

    public void setKhoangGia(String khoangGia) {
        this.khoangGia = khoangGia;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public ArrayList<DoiTacLienQuan> getDoiTacLienQuans() {
        return doiTacLienQuans;
    }

    public void setDoiTacLienQuans(ArrayList<DoiTacLienQuan> doiTacLienQuans) {
        this.doiTacLienQuans = doiTacLienQuans;
    }

    public ArrayList<AnhBangGia> getAnhBangGias() {
        return anhBangGias;
    }

    public void setAnhBangGias(ArrayList<AnhBangGia> anhBangGias) {
        this.anhBangGias = anhBangGias;
    }

    public double getViDo() {
        return viDo;
    }

    public void setViDo(double viDo) {
        this.viDo = viDo;
    }

    public double getKinhDo() {
        return kinhDo;
    }

    public void setKinhDo(double kinhDo) {
        this.kinhDo = kinhDo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<AnhList> getAnhLists() {
        return anhLists;
    }

    public void setAnhLists(ArrayList<AnhList> anhLists) {
        this.anhLists = anhLists;
    }

    public double getChatLuong() {
        return chatLuong;
    }

    public void setChatLuong(double chatLuong) {
        this.chatLuong = chatLuong;
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }

    public String getDacTrung() {
        return dacTrung;
    }

    public void setDacTrung(String dacTrung) {
        this.dacTrung = dacTrung;
    }

    public double getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(double danhGia) {
        this.danhGia = danhGia;
    }

    public int getDaYeuThich() {
        return daYeuThich;
    }

    public void setDaYeuThich(int daYeuThich) {
        this.daYeuThich = daYeuThich;
    }

    public String getUrlContent() {
        return urlContent;
    }

    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNhomKhuyenMai() {
        return maNhomKhuyenMai;
    }

    public void setMaNhomKhuyenMai(String maNhomKhuyenMai) {
        this.maNhomKhuyenMai = maNhomKhuyenMai;
    }

    public boolean isPheDuyet() {
        return pheDuyet;
    }

    public void setPheDuyet(boolean pheDuyet) {
        this.pheDuyet = pheDuyet;
    }

    public String getNhomCnDoiTacId() {
        return nhomCnDoiTacId;
    }

    public void setNhomCnDoiTacId(String nhomCnDoiTacId) {
        this.nhomCnDoiTacId = nhomCnDoiTacId;
    }

    public String getDoiTacId() {
        return doiTacId;
    }

    public void setDoiTacId(String doiTacId) {
        this.doiTacId = doiTacId;
    }

    public int getNhomKhuyenMaiId() {
        return nhomKhuyenMaiId;
    }

    public void setNhomKhuyenMaiId(int nhomKhuyenMaiId) {
        this.nhomKhuyenMaiId = nhomKhuyenMaiId;
    }

    public String getDoiTacKhuyenMaiId() {
        return doiTacKhuyenMaiId;
    }

    public void setDoiTacKhuyenMaiId(String doiTacKhuyenMaiId) {
        this.doiTacKhuyenMaiId = doiTacKhuyenMaiId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getLoaiHopDong() {
        return loaiHopDong;
    }

    public void setLoaiHopDong(int loaiHopDong) {
        this.loaiHopDong = loaiHopDong;
    }
}
