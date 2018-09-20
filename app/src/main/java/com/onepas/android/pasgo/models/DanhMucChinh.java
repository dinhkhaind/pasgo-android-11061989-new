package com.onepas.android.pasgo.models;

/**
 * Created by Kai on 12/28/2016.
 */

public class DanhMucChinh {
    private String ten;
    private int id;
    private boolean isCheck;
    public DanhMucChinh() {
    }

    public DanhMucChinh(String ten, int id, boolean check) {
        this.ten = ten;
        this.id = id;
        this.isCheck = check;
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
