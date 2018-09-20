package com.onepas.android.pasgo.models;

public class FilterCategoryItems {

    private String Description;
    private int FilterCategoryTermId;
    private String NameVn;
    private boolean IsSelected;
    private String NameEn;
    private int FilterCategoryId;
    private int Id;


    public FilterCategoryItems() {
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getFilterCategoryTermId() {
        return FilterCategoryTermId;
    }

    public void setFilterCategoryTermId(int filterCategoryTermId) {
        FilterCategoryTermId = filterCategoryTermId;
    }

    public String getNameVn() {
        return NameVn;
    }

    public void setNameVn(String nameVn) {
        NameVn = nameVn;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean isSelected) {
        IsSelected = isSelected;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public int getFilterCategoryId() {
        return FilterCategoryId;
    }

    public void setFilterCategoryId(int filterCategoryId) {
        FilterCategoryId = filterCategoryId;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


}


