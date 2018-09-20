package com.onepas.android.pasgo.models;

import java.util.ArrayList;

/**
 * Created by pasgo on 11/15/2016.
 */

public class Category {
    private int id;
    private String ten;
    private int parentId;
    private int tagId;
    private String doiTacKhuyenMaiId;
    private boolean isDoiTacKhuyenMai;
    private String anh;
    private int countChild;
    private boolean isTitle;
    public Category(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getDoiTacKhuyenMaiId() {
        return doiTacKhuyenMaiId;
    }

    public void setDoiTacKhuyenMaiId(String doiTacKhuyenMaiId) {
        this.doiTacKhuyenMaiId = doiTacKhuyenMaiId;
    }

    public boolean isDoiTacKhuyenMai() {
        return isDoiTacKhuyenMai;
    }

    public void setDoiTacKhuyenMai(boolean doiTacKhuyenMai) {
        isDoiTacKhuyenMai = doiTacKhuyenMai;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public int getCountChild() {
        return countChild;
    }

    public void setCountChild(int countChild) {
        this.countChild = countChild;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}
