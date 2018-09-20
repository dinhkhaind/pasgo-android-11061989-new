package com.onepas.android.pasgo.models;

import java.util.List;

public class ChiTietKhuyenMai {
	private String tieuDe;
	private String luotThich;
	private String soBinhLuan;
	private int daThich;
	private String id;
	private String gioiThieu;
	private String noiDung;
	private List<String> anhs;
	private String urlContent;
	private int soCheckIn;
    private boolean daCheckin;
    private boolean pheDuyet;
    private String linkBangGia;
    private double viDo;
    private double kinhDo;
    private int trangThai;
    private String maNhomKhuyenMai;
    private String dacTrung;
	private int daYeuThich;
	private String chuyenMon;
	private String moTaNoiDung;
	private String tatCaNoiDung;
	private String moTaGioiThieu;
	private String tatCaGioiThieu;
	private String diaChi;
	private String tongDaiPASGO="";
	private double chatLuong;
	public String getTieuDe() {
		return tieuDe;
	}
	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}
	public String getLuotThich() {
		return luotThich;
	}
	public void setLuotThich(String luotThich) {
		this.luotThich = luotThich;
	}
	private double danhGia=0;
	public String getSoBinhLuan() {
		return soBinhLuan;
	}
	public void setSoBinhLuan(String soBinhLuan) {
		this.soBinhLuan = soBinhLuan;
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
	public String getGioiThieu() {
		return gioiThieu;
	}
	public void setGioiThieu(String gioiThieu) {
		this.gioiThieu = gioiThieu;
	}
	public String getNoiDung() {
		return noiDung;
	}
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	public List<String> getAnhs() {
		return anhs;
	}
	public void setAnhs(List<String> anhs) {
		this.anhs = anhs;
	}
	public String getUrlContent() {
		return urlContent;
	}
	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
	}
	public int getSoCheckIn() {
		return soCheckIn;
	}
	public void setSoCheckIn(int soCheckIn) {
		this.soCheckIn = soCheckIn;
	}

    public boolean isDaCheckin() {
        return daCheckin;
    }

    public void setDaCheckin(boolean daCheckin) {
        this.daCheckin = daCheckin;
    }

    public boolean isPheDuyet() {
        return pheDuyet;
    }

    public void setPheDuyet(boolean pheDuyet) {
        this.pheDuyet = pheDuyet;
    }

    public String getLinkBangGia() {
        return linkBangGia;
    }

    public void setLinkBangGia(String linkBangGia) {
        this.linkBangGia = linkBangGia;
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

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNhomKhuyenMai() {
        return maNhomKhuyenMai;
    }

    public void setMaNhomKhuyenMai(String maNhomKhuyenMai) {
        this.maNhomKhuyenMai = maNhomKhuyenMai;
    }

    public String getDacTrung() {
        return dacTrung;
    }

    public void setDacTrung(String dacTrung) {
        this.dacTrung = dacTrung;
    }

	public int getDaYeuThich() {
		return daYeuThich;
	}

	public void setDaYeuThich(int daYeuThich) {
		this.daYeuThich = daYeuThich;
	}

	public double getDanhGia() {
		return danhGia;
	}

	public void setDanhGia(double danhGia) {
		this.danhGia = danhGia;
	}

	public String getChuyenMon() {
		return chuyenMon;
	}

	public void setChuyenMon(String chuyenMon) {
		this.chuyenMon = chuyenMon;
	}

	public String getMoTaNoiDung() {
		return moTaNoiDung;
	}

	public void setMoTaNoiDung(String moTaNoiDung) {
		this.moTaNoiDung = moTaNoiDung;
	}

	public String getTatCaNoiDung() {
		return tatCaNoiDung;
	}

	public void setTatCaNoiDung(String tatCaNoiDung) {
		this.tatCaNoiDung = tatCaNoiDung;
	}

	public String getMoTaGioiThieu() {
		return moTaGioiThieu;
	}

	public void setMoTaGioiThieu(String moTaGioiThieu) {
		this.moTaGioiThieu = moTaGioiThieu;
	}

	public String getTatCaGioiThieu() {
		return tatCaGioiThieu;
	}

	public void setTatCaGioiThieu(String tatCaGioiThieu) {
		this.tatCaGioiThieu = tatCaGioiThieu;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getTongDaiPASGO() {
		return tongDaiPASGO;
	}

	public void setTongDaiPASGO(String tongDaiPASGO) {
		this.tongDaiPASGO = tongDaiPASGO;
	}

	public double getChatLuong() {
		return chatLuong;
	}

	public void setChatLuong(double chatLuong) {
		this.chatLuong = chatLuong;
	}
}

