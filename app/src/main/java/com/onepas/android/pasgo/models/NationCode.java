package com.onepas.android.pasgo.models;

public class NationCode {

	private String id;
	private String ma;
	private String ten;
    private String tenHienThi;

	public NationCode() {
		super();
	}
    public NationCode( String id, String ten, String ma, String tenHienThi )
    {
        this.id = id;
        this.ten = ten ;
        this.ma = ma;
        this.tenHienThi = tenHienThi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    public void setTenHienThi(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }
}
