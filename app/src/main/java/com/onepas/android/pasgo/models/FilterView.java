package com.onepas.android.pasgo.models;

public class FilterView {
    private int id;
    private int parentId;
    private String name;
    private boolean isCheck;
    private boolean isParent;
    private int choiceType;
    public FilterView() {
    }
    public FilterView(int id, int parentId , String name, boolean isCheck, boolean isParent, int choiceType) {
        this.id =id;
        this.name = name;
        this.isCheck = isCheck;
        this.isParent = isParent;
        this.parentId =parentId;
        this.choiceType =choiceType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(int choiceType) {
        this.choiceType = choiceType;
    }
}


