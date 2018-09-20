package com.onepas.android.pasgo.models;

import java.util.ArrayList;

/**
 * Created by pasgo on 11/30/2016.
 */

public class CategoryHome {
    private int id;
    private int tagId;
    private String ten;
    private boolean isCheck;
    private ArrayList<TagModel> tagModels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public ArrayList<TagModel> getTagModels() {
        return tagModels;
    }

    public void setTagModels(ArrayList<TagModel> tagModels) {
        this.tagModels = tagModels;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
