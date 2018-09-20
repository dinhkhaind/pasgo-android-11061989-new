package com.onepas.android.pasgo.ui.reserve;

public class DiemDenModel {
	private boolean isMoTa;
	private int nhomKhuyenMaiId;
	private int trangThai;
	private String taiTro;
	private String nhomCNDoiTacId;
	private String tieuDeKhuyenMai;
	private String logo;
	private boolean coThePASGO;
	private String tongDaiPASGO="";
	private double km;
	private String doiTacKhuyenMaiId;
	private String diaChi;
	private String ten;
	private String id;
	private double chatLuong;
	private String chuyenMon;
	private double danhGia;
	private int luotThich;
	private int soBinhLuan;
	private int gia;
	private int daThich;
	private String ma;
	private int soCheckIn;
	private int chietKhau;
	private String chietKhauHienThi;
	private String giaTrungBinh;
	private int disconnect = 3;
	private String motaTag;
	private int loaiHopDong;
	public DiemDenModel(){}

	public DiemDenModel(int disconnect){
		this.disconnect = disconnect;
	}
	public int getNhomKhuyenMaiId() {
		return nhomKhuyenMaiId;
	}

	public void setNhomKhuyenMaiId(int nhomKhuyenMaiId) {
		this.nhomKhuyenMaiId = nhomKhuyenMaiId;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

	public String getNhomCNDoiTacId() {
		return nhomCNDoiTacId;
	}

	public void setNhomCNDoiTacId(String nhomCNDoiTacId) {
		this.nhomCNDoiTacId = nhomCNDoiTacId;
	}

	public String getTieuDeKhuyenMai() {
		return tieuDeKhuyenMai;
	}

	public void setTieuDeKhuyenMai(String tieuDeKhuyenMai) {
		this.tieuDeKhuyenMai = tieuDeKhuyenMai;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public double getKm() {
		return km;
	}

	public void setKm(double km) {
		this.km = km;
	}

	public String getDoiTacKhuyenMaiId() {
		return doiTacKhuyenMaiId;
	}

	public void setDoiTacKhuyenMaiId(String doiTacKhuyenMaiId) {
		this.doiTacKhuyenMaiId = doiTacKhuyenMaiId;
	}

	public int getLuotThich() {
		return luotThich;
	}

	public void setLuotThich(int luotThich) {
		this.luotThich = luotThich;
	}

	public int getSoBinhLuan() {
		return soBinhLuan;
	}

	public void setSoBinhLuan(int soBinhLuan) {
		this.soBinhLuan = soBinhLuan;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public int getGia() {
		return gia;
	}

	public void setGia(int gia) {
		this.gia = gia;
	}

	public int getDaThich() {
		return daThich;
	}

	public void setDaThich(int daThich) {
		this.daThich = daThich;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getDanhGia() {
		return danhGia;
	}

	public void setDanhGia(double danhGia) {
		this.danhGia = danhGia;
	}

	public String getMa() {
		return ma;
	}

	public void setMa(String ma) {
		this.ma = ma;
	}

	public int getSoCheckIn() {
		return soCheckIn;
	}

	public void setSoCheckIn(int soCheckIn) {
		this.soCheckIn = soCheckIn;
	}

	public int getChietKhau() {
		return chietKhau;
	}

	public void setChietKhau(int chietKhau) {
		this.chietKhau = chietKhau;
	}

	public boolean isCoThePASGO() {
		return coThePASGO;
	}

	public void setCoThePASGO(boolean coThePASGO) {
		this.coThePASGO = coThePASGO;
	}

	public String getChietKhauHienThi() {
		return chietKhauHienThi;
	}

	public void setChietKhauHienThi(String chietKhauHienThi) {
		this.chietKhauHienThi = chietKhauHienThi;
	}

	public String getChuyenMon() {
		return chuyenMon;
	}

	public void setChuyenMon(String chuyenMon) {
		this.chuyenMon = chuyenMon;
	}

	public String getTongDaiPASGO() {
		return tongDaiPASGO;
	}

	public void setTongDaiPASGO(String tongDaiPASGO) {
		this.tongDaiPASGO = tongDaiPASGO;
	}

	public int isDisconnect() {
		return disconnect;
	}

	public void setDisconnect(int disconnect) {
		this.disconnect = disconnect;
	}

	public String getTaiTro() {
		return taiTro;
	}

	public void setTaiTro(String taiTro) {
		this.taiTro = taiTro;
	}

	public double getChatLuong() {
		return chatLuong;
	}

	public void setChatLuong(double chatLuong) {
		this.chatLuong = chatLuong;
	}

	public int getDisconnect() {
		return disconnect;
	}

	public boolean isMoTa() {
		return isMoTa;
	}

	public void setMoTa(boolean moTa) {
		isMoTa = moTa;
	}

	public String getGiaTrungBinh() {
		return giaTrungBinh;
	}

	public void setGiaTrungBinh(String giaTrungBinh) {
		this.giaTrungBinh = giaTrungBinh;
	}

	public String getMotaTag() {
		return motaTag;
	}

	public void setMotaTag(String motaTag) {
		this.motaTag = motaTag;
	}

	public int getLoaiHopDong() {
		return loaiHopDong;
	}

	public void setLoaiHopDong(int loaiHopDong) {
		this.loaiHopDong = loaiHopDong;
	}
}

