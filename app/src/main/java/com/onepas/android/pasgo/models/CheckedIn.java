package com.onepas.android.pasgo.models;

public class CheckedIn {
	private String nhomCNDoiTacId;
	private String logo;
	private double km;
	private String doiTacKhuyenMaiId;
	private int luotThich;
	private int soBinhLuan;
	private String diaChi;
	private String ten;
	private int soCheckIn;
    private String thoiGianDen;
    private int soNguoiDen;
    private double danhGia;
    private int trangThai;
    private String doiTacId;
    private int nhomKhuyenMaiId;

	private int tongSo;
	private String tieuDeKhuyenMai;
	private String gia;
	private String ma;
	private int daThich;
	private double tienKhuyenMai;
	private int chietKhau;
	private boolean coThePASGO;
	private String chietKhauHienThi;
	private String chuyenMon;

	private String taiTro;
	private double chatLuong;
	private int trangThaiDatCho; //=1 là đang chờ, 2 là hoàn thành
	private int soTreEm;
	private String giaTrungBinh;
	private int loaiHopDong;
	private double viDo;
	private double kinhDo;
	private String maNhomKhuyenMai;
	private String SoDienThoaiFormat;
	private String tenKhachHang;
    public String getDoiTacId() {
        return doiTacId;
    }

    public void setDoiTacId(String id) {
        this.doiTacId = id;
    }

    public String getNhomCNDoiTacId() {
		return nhomCNDoiTacId;
	}
	public void setNhomCNDoiTacId(String nhomCNDoiTacId) {
		this.nhomCNDoiTacId = nhomCNDoiTacId;
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
	public String getTenChiNhanh() {
		return ten;
	}
	public void setTenChiNhanh(String tenChiNhanh) {
		this.ten = tenChiNhanh;
	}
	public int getSoCheckIn() {
		return soCheckIn;
	}
	public void setSoCheckIn(int soCheckIn) {
		this.soCheckIn = soCheckIn;
	}

    public String getThoiGianDen() {
        return thoiGianDen;
    }

    public void setThoiGianDen(String thoiGianDen) {
        this.thoiGianDen = thoiGianDen;
    }

    public int getSoNguoiDen() {
        return soNguoiDen;
    }

    public void setSoNguoiDen(int soNguoiDen) {
        this.soNguoiDen = soNguoiDen;
    }

    public double getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(double danhGia) {
        this.danhGia = danhGia;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getNhomKhuyenMaiId() {
        return nhomKhuyenMaiId;
    }

    public void setNhomKhuyenMaiId(int nhomKhuyenMaiId) {
        this.nhomKhuyenMaiId = nhomKhuyenMaiId;
    }

	public int getTongSo() {
		return tongSo;
	}

	public void setTongSo(int tongSo) {
		this.tongSo = tongSo;
	}

	public String getTieuDeKhuyenMai() {
		return tieuDeKhuyenMai;
	}

	public void setTieuDeKhuyenMai(String tieuDeKhuyenMai) {
		this.tieuDeKhuyenMai = tieuDeKhuyenMai;
	}

	public int getDaThich() {
		return daThich;
	}

	public void setDaThich(int daThich) {
		this.daThich = daThich;
	}

	public String getGia() {
		return gia;
	}

	public void setGia(String gia) {
		this.gia = gia;
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

	public double getTienKhuyenMai() {
		return tienKhuyenMai;
	}

	public void setTienKhuyenMai(double tienKhuyenMai) {
		this.tienKhuyenMai = tienKhuyenMai;
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

	public int getTrangThaiDatCho() {
		return trangThaiDatCho;
	}

	public void setTrangThaiDatCho(int trangThaiDatCho) {
		this.trangThaiDatCho = trangThaiDatCho;
	}

	public int getSoTreEm() {
		return soTreEm;
	}

	public void setSoTreEm(int soTreEm) {
		this.soTreEm = soTreEm;
	}

	public String getGiaTrungBinh() {
		return giaTrungBinh;
	}

	public void setGiaTrungBinh(String giaTrungBinh) {
		this.giaTrungBinh = giaTrungBinh;
	}

	public int getLoaiHopDong() {
		return loaiHopDong;
	}

	public void setLoaiHopDong(int loaiHopDong) {
		this.loaiHopDong = loaiHopDong;
	}

	public double getViDo() {
		return viDo;
	}

	public void setViDo(double viDo) {
		this.viDo = viDo;
	}

	public double getKinhDo() {
		return kinhDo;
	}

	public void setKinhDo(double kinhDo) {
		this.kinhDo = kinhDo;
	}

	public String getMaNhomKhuyenMai() {
		return maNhomKhuyenMai;
	}

	public void setMaNhomKhuyenMai(String maNhomKhuyenMai) {
		this.maNhomKhuyenMai = maNhomKhuyenMai;
	}

	public String getSoDienThoaiFormat() {
		return SoDienThoaiFormat;
	}

	public void setSoDienThoaiFormat(String soDienThoaiFormat) {
		SoDienThoaiFormat = soDienThoaiFormat;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}
}

