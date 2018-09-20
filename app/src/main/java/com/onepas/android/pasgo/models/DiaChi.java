package com.onepas.android.pasgo.models;

public class DiaChi{
	
	private String tenDiem;
	private String soNha;
	private String tenDuong;
	private String phuongXa;
	private String thanhPho;
	private String quanHuyen;
	private String tinh;
	private String nuoc;
	private float viDo;
	private float kinhDo;
	
	public String getTenDiem() {
		return tenDiem;
	}

	public void setTenDiem(String tenDiem) {
		this.tenDiem = tenDiem;
	}
	
	public String getSoNha() {
		return soNha;
	}
	
	public void setSoNha(String soNha) {
		this.soNha = soNha;
	}
	
	public String getTenDuong() {
		return tenDuong;
	}
	
	public void setTenDuong(String tenDuong) {
		this.tenDuong = tenDuong;
	}
	
	public String getPhuongXa() {
		return phuongXa;
	}
	
	public void setPhuongXa(String phuongXa) {
		this.phuongXa = phuongXa;
	}
	
	public String getQuanHuyen() {
		
		StringBuilder stringBuilder = new StringBuilder();	
		if(this.phuongXa != null && !this.phuongXa.isEmpty()){
			stringBuilder.append(",");
			stringBuilder.append(this.phuongXa);
		}
		
		if(this.quanHuyen != null && !this.quanHuyen.isEmpty()){
			stringBuilder.append(",");
			stringBuilder.append(this.quanHuyen);
		}
		
		return stringBuilder.length() > 0 ? stringBuilder.toString().substring(1) : stringBuilder.toString();
	}
	
	public String getThanhPho() {
		return thanhPho;
	}
	
	public void setThanhPho(String thanhPho) {
		this.thanhPho = thanhPho;
	}
	
	public void setQuanHuyen(String quanHuyen) {
		this.quanHuyen = quanHuyen;
	}
	
	public String getTinh() {
		return tinh;
	}
	
	public void setTinh(String tinh) {
		this.tinh = tinh;
	}
	
	public String getNuoc() {
		return nuoc;
	}
	
	public void setNuoc(String nuoc) {
		this.nuoc = nuoc;
	}
	
	public String getTinhTP(){
		
		StringBuilder stringBuilder = new StringBuilder();	
		if(this.thanhPho != null && !this.thanhPho.isEmpty()){
			stringBuilder.append(",");
			stringBuilder.append(this.thanhPho);
		}
		
		if(this.tinh != null && !this.tinh.isEmpty()){
			stringBuilder.append(",");
			stringBuilder.append(this.tinh);
		}
		
		return stringBuilder.length() > 0 ? stringBuilder.toString().substring(1) : stringBuilder.toString();
	}
	
	public void setViDo(float viDo) {
		this.viDo = viDo;
	}

	public float getKinhDo() {
		return kinhDo;
	}

	public void setKinhDo(float kinhDo) {
		this.kinhDo = kinhDo;
	}
	
	public boolean hasAddress()
	{
		return !this.getFullAddress().isEmpty();
	}
	
	public boolean hasTenDuong()
	{
		return this.getTenDuong() != null && !this.getTenDuong().isEmpty();
	}
	
	public boolean hasQuanHuyen()
	{
		return this.getQuanHuyen() != null && !this.getQuanHuyen().isEmpty();
	}
	
	public boolean hasTinh()
	{
		return this.getTinh() != null && !this.getTinh().isEmpty();
	}
	
	public String getFullAddress() {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		if(this.soNha != null && !this.soNha.isEmpty()){
			stringBuilder.append(this.soNha);
		}
		
		if(this.tenDuong != null && !this.tenDuong.isEmpty()){
			stringBuilder.append(" ");
			stringBuilder.append(this.tenDuong);
		}
		
		if(this.phuongXa != null && !this.phuongXa.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.phuongXa);
		}
		
		if(this.thanhPho != null && !this.thanhPho.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.thanhPho);
		}
		
		if(this.quanHuyen != null && !this.quanHuyen.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.quanHuyen);
		}
		
		if(this.tinh != null && !this.tinh.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.tinh);
		}
		
		if(this.nuoc != null && !this.nuoc.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.nuoc);
		}
		
		return stringBuilder.toString();
	}
	
	public String getAddressToMap() {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		if(this.soNha != null && !this.soNha.isEmpty()){
			stringBuilder.append(this.soNha);
		}
		
		if(this.tenDuong != null && !this.tenDuong.isEmpty()){
			stringBuilder.append(" ");
			stringBuilder.append(this.tenDuong);
		}
		
		if(this.phuongXa != null && !this.phuongXa.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.phuongXa);
		}
	
		if(this.quanHuyen != null && !this.quanHuyen.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.quanHuyen);
		}
		
		if(this.tinh != null && !this.tinh.isEmpty()){
			stringBuilder.append(" - ");
			stringBuilder.append(this.tinh);
		}
		
		return stringBuilder.toString();
	}

	public float getViDo() {
		return viDo;
	}
}