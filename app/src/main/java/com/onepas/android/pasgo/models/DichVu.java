package com.onepas.android.pasgo.models;

public class DichVu {
	private int dichVuId;
    private String tenDichVu;
    private boolean isCheck;
    private String giamGia;
    private int datTruoc;

    public int getDichVuId() {
        return dichVuId;
    }

    public void setDichVuId(int dichVuId) {
        this.dichVuId = dichVuId;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(String giamGia) {
        this.giamGia = giamGia;
    }

    public int getDatTruoc() {
        return datTruoc;
    }

    public void setDatTruoc(int datTruoc) {
        this.datTruoc = datTruoc;
    }

}
