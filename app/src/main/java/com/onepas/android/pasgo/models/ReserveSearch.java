package com.onepas.android.pasgo.models;

/**
 * Created by VuDinh on 10/5/2015.
 */
public class ReserveSearch {
    private String tuKhoa;
    public ReserveSearch()
    {
    }
    public ReserveSearch(String tuKhoa)
    {
        this.tuKhoa = tuKhoa;
    }
    public String getTuKhoa() {
        return tuKhoa;
    }

    public void setTuKhoa(String tuKhoa) {
        this.tuKhoa = tuKhoa;
    }
}
