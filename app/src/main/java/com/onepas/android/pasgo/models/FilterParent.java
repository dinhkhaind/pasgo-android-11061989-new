package com.onepas.android.pasgo.models;

import java.util.ArrayList;

public class FilterParent {
    private int NhomKhuyenMaiId;
    private String Description;
    private String NameVn;
    private int ChoiceType;
    private int TypeTermId;
    private String NameEn;
    private int Order;
    private int Id;
    private String Code;
    private ArrayList<FilterCategoryItems> FilterCategoryItems;
    public FilterParent() {
    }

    public int getNhomKhuyenMaiId() {
        return NhomKhuyenMaiId;
    }

    public void setNhomKhuyenMaiId(int nhomKhuyenMaiId) {
        NhomKhuyenMaiId = nhomKhuyenMaiId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNameVn() {
        return NameVn;
    }

    public void setNameVn(String nameVn) {
        NameVn = nameVn;
    }

    public int getChoiceType() {
        return ChoiceType;
    }

    public void setChoiceType(int choiceType) {
        ChoiceType = choiceType;
    }

    public int getTypeTermId() {
        return TypeTermId;
    }

    public void setTypeTermId(int typeTermId) {
        TypeTermId = typeTermId;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public ArrayList<FilterCategoryItems> getFilterCategoryItems() {
        return FilterCategoryItems;
    }

    public void setFilterCategoryItems(ArrayList<FilterCategoryItems> filterCategoryItems) {
        FilterCategoryItems = filterCategoryItems;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}


