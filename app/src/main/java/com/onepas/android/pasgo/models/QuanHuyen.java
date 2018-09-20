package com.onepas.android.pasgo.models;

/**
 * Created by VuDinh on 6/16/2016.
 */
public class QuanHuyen {
    private String ten;
    private int id;
    private boolean isCheck;
    public QuanHuyen() {
    }

    public QuanHuyen(String ten, int id, boolean isCheck) {
        this.ten = ten;
        this.id = id;
        this.isCheck = isCheck;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
