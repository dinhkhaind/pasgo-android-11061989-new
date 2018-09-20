package com.onepas.android.pasgo.models;

import java.util.ArrayList;

public class NhomDichVu {
	private int id;
    private String ten;
    private ArrayList<DichVu> dichVus;

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

    public ArrayList<DichVu> getDichVus() {
        return dichVus;
    }

    public void setDichVus(ArrayList<DichVu> dichVus) {
        this.dichVus = dichVus;
    }
}
