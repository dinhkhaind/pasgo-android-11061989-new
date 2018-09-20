package com.onepas.android.pasgo.models;

import java.util.ArrayList;

/**
 * Created by pasgo on 11/29/2016.
 */

public class HomeGroupCategory {
    private int loaiGiaoDien;
    private String groupName;
    private int groupId;
    private int sapXep;
    private int soLanXuatHien;
    private int xemThemId;

    private ArrayList<HomeCategory> homeCategories;

    public int getLoaiGiaoDien() {
        return loaiGiaoDien;
    }

    public void setLoaiGiaoDien(int loaiGiaoDien) {
        this.loaiGiaoDien = loaiGiaoDien;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSapXep() {
        return sapXep;
    }

    public void setSapXep(int sapXep) {
        this.sapXep = sapXep;
    }

    public ArrayList<HomeCategory> getHomeCategories() {
        return homeCategories;
    }

    public void setHomeCategories(ArrayList<HomeCategory> homeCategories) {
        this.homeCategories = homeCategories;
    }

    public int getXemThemId() {
        return xemThemId;
    }

    public void setXemThemId(int xemThemId) {
        this.xemThemId = xemThemId;
    }

    public int getSoLanXuatHien() {
        return soLanXuatHien;
    }

    public void setSoLanXuatHien(int soLanXuatHien) {
        this.soLanXuatHien = soLanXuatHien;
    }
}
