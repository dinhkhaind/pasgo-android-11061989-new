package com.onepas.android.pasgo.models;

/**
 * Created by VuDinh on 1/19/2016.
 */
public class TagModel {
    private String ten;
    private int id;
    private String ma;
    private int tagId;
    private boolean isCheck;
    public TagModel(){}
    public TagModel(int id, String ten)
    {
        this.id =id;
        this.ten=ten;
    }
    public TagModel(int id, String ten,String ma, boolean isCheck)
    {
        this.id =id;
        this.ten=ten;
        this.ma = ma;
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

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
