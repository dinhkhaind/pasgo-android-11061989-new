package com.onepas.android.pasgo.models;

public class LinhVucQuanTam {
	private String id;
	private String ten;
    private String ma;
    private boolean isCheck = false;

    public LinhVucQuanTam(){}
    public LinhVucQuanTam(String id, String ten, String ma, boolean isCheck)
    {
        this.id = id;
        this.ten = ten;
        this.ma = ma;
        this.isCheck = isCheck;
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

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
