package com.onepas.android.pasgo.models;

/**
 * Created by VuDinh on 9/14/2016.
 */
public class GoiYTimKiem {
    private String tuKhoa;
    public  GoiYTimKiem(){}
    public  GoiYTimKiem(String ten){
        this.tuKhoa = ten;
    }

    public String getTuKhoa() {
        return tuKhoa;
    }

    public void setTuKhoa(String tuKhoa) {
        this.tuKhoa = tuKhoa;
    }
}
