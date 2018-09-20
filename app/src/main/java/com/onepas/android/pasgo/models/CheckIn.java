package com.onepas.android.pasgo.models;

public class CheckIn {
	String datXeId;
	String nhomCNDoiTacId;
	public CheckIn(String datXeId, String nhomCNDoiTacId)
	{
		this.datXeId = datXeId;
		this.nhomCNDoiTacId = nhomCNDoiTacId;
	}
	public String getDatXeId() {
		return datXeId;
	}

	public void setDatXeId(String datXeId) {
		this.datXeId = datXeId;
	}
	public String getNhomCNDoiTacId() {
		return nhomCNDoiTacId;
	}
	public void setNhomCNDoiTacId(String nhomCNDoiTacId) {
		this.nhomCNDoiTacId = nhomCNDoiTacId;
	}
	
	
}
