package com.onepas.android.pasgo.utils;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;

public final class WebServiceUtils {

	public static String URL_GET_NHOM_XE_ALL(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" + "GetNhomXeAll?TokenAPI=" + token;
	}

	public static String URL_INSERT_TRANG_THAI_HANG(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"InsertTrangThaiHang?TokenAPI=" + token;
	}
	public static String URL_KIEM_TRA_TRANG_THAI_DAT_XE(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"KiemTraTrangThaiDatXe?TokenAPI=" + token;
	}

	public static String URL_ADD_DAT_XE(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" + "AddDatXeV3?TokenAPI=" +
				token;
	}

	public static String URL_GET_GIAM_GIA_BY_MA(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetGiamGiaByMa?TokenAPI=" + token;
	}

	public static String URL_GET_LAI_XE_PRIORITY(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetLaiXePriorityV1?TokenAPI=" + token;
	}
	public static String URL_CHI_TIET_LICH_SU(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetChiTietDatXeKhachHangByDatXeId_V2?TokenAPI=" +
				token;
	}

	public static String URL_GET_UOC_TINH_CHI_PHI_DAT_XE(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetUocTinhChiPhiDatXe?TokenAPI=" + token;
	}

	public static String URL_UPDATE_GIAO_DICH_DAT_XE(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"UpdateGiaoDichDatXe?TokenAPI=" + token;
	}

	public static String URL_UPDATE_LY_DO_KHACH_HANG_HUY(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"UpdateLyDoKhachHangHuy?TokenAPI=" + token;
	}

    public static String URL_GET_THU_LAI_DAT_XE(String token) {
        return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetThuLaiDatXe?TokenAPI=" + token;
    }

	public static String URL_GET_ALL_LY_DO_KHACH_HANG(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetLyDoKhachHangAll?TokenAPI=" + token;
	}

	public static String URL_GET_LAI_XE_BY_ID(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" + "GetLaiXeById?TokenAPI=" +
				token;
	}

    public static String URL_UPDATE_RECEIVED_NOTIFICATION() {
        return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" + "UpdateReceivedNotificationV1?TokenAPI=" + Pasgo.getInstance().token;
    }

	public static String URL_RegisterPushNotification() {
		return Constants.APP_DOMAIN +
				"CommonService.asmx/" +
				"RegisterPushNotification?TokenAPI=" + Pasgo.getInstance().token;
	}

    public static String URL_UPLOAD_SERVICE(String token) {
        return Constants.APP_DOMAIN_UPDATE_FILE + "" +
				"UploadService.ashx?TokenAPI=" + token +
				"&NguoiDungId=%s";
    }

	public static String URL_Rate_Status(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetDanhGiaLaiXeTheoDonHang?TokenAPI=" + token;
	}

	public static String URL_Rate(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"DanhGiaLaiXeTheoDonHang?TokenAPI=" + token;
	}

    public static String URL_GET_FILTER_CATEGORY(String token) {
        return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetFilterCategory?TokenAPI=" + token;
    }

	public static String URL_GIAM_GIA_DAT_CHO_GET_DANH_SACH_DOI_TAC(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GiamGiaDatChoGetDanhSachDoiTacV5?TokenAPI=" + token;
	}

	public static String URL_TIM_KIEM_DOI_TAC(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"TimKiemDoiTacV8?TokenAPI=" + token;
	}
	public static String URL_GET_DOI_TAC_LIEN_QUAN(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetDoiTacLienQuanV2?TokenAPI=" + token;
	}

	public static String URL_DOI_TAC_YEU_THICH(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetDoiTacYeuThichV5?TokenAPI=" + token;
	}

	public static String URL_GIAM_GIA_DAT_CHO_GET_TINH(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GiamGiaDatChoGetTinh?TokenAPI=" + token;
	}
	public static String URL_GET_TINH_ALLV1(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetTinhAllV1?TokenAPI=" + token;
	}

	public static String URL_UPDATE_TINH_SELECTED(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"UpdateTinhSelected?TokenAPI=" + token;
	}
	public static String URL_GIAM_GIA_DAT_CHO_GET_NHOM_KHUYEN_MAI(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GiamGiaDatChoGetNhomKhuyenMai?TokenAPI=" + token;
	}

	public static String URL_TU_KHOA_TIM_KIEM(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"TuKhoaTimKiemV3?TokenAPI=" + token;
	}
	public static String URL_GET_CHI_TIET_ANH_DOI_TAC(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetChiTietAnhDoiTac?TokenAPI=" + token;
	}
	public static String URL_GET_CHI_TIET_BANG_GIA(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetChiTietBangGia?TokenAPI=" + token;
	}

    public static String URL_UPDATE_USER(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" +
				"UpdateUserV1?TokenAPI=" + token;
    }

    public static String URL_HASH_PASSWORD(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" +
				"HasPassword?TokenAPI=" + token;
    }

    public static String URL_GET_MEMBER_BY_NGUOIDUNGID(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" +
				"GetMemberByNguoiDungId?TokenAPI=" + token;
    }

    public static String URL_CHANGE_PASSWORD(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" +
				"ChangePassword?TokenAPI=" + token;
    }

	public static String URL_SO_LUONG_LICH_SU(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"SoLuongLichSu?TokenAPI=" +
				token;
	}

	public static String URL_LICH_SU_DAT_XE_BY_NGUOIDUNG(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetLichSuDatXeByNguoiDungId_V2?TokenAPI=" + token;
	}
	public static String URL_LICH_SU_DAT_TRUOC_DA_NHAN(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"LichSuDatTruocDaNhan?TokenAPI=" + token;
	}

	public static String URL_LICH_SU_DAT_CHO(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"LichSuDatChoV4?TokenAPI=" +
				token;
	}

	public static String URL_LICH_SU_DAT_TRUOC_CHUA_NHAN(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"LichSuDatTruocChuaNhan?TokenAPI=" + token;
	}
	public static String URL_Ma_Kich_Hoat(String token) {
		return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" +
				"VerifyMaKichHoatV1?TokenAPI=" + token;
	}

    public static String URL_GET_NGUOIDUNG_BY_ID(String token) {
        return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"GetNguoiDungById?TokenAPI=" + token;
    }

	public static String URL_GET_CHI_TIET_DOI_TAC_KM(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetChiTietTinKhuyenMaiByIdV4?TokenAPI=" + token;
	}

	public static String URL_Verify(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"VerifyVerifyDoiTacKhuyenMai?TokenAPI=" + token;
	}

	public static String URL_GET_LICH_SU_DOI_TAC(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetLichSuDatXeDoiTac?TokenAPI=" + token;
	}
	
	public static String URL_GET_DANH_SACH_MA_KHUYEN_MAI(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetDanhSachMaKhuyenMaiV2?TokenAPI=" +
				token;
	}

	public static String URL_GIAM_GIA_DAT_CHO_DIEM_DEN(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GiamGiaDatChoDiemDenV1?TokenAPI=" +
				token;
	}
	
	public static String URL_GET_DANH_SACH_DIEM_DEN(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetDanhSachDiemDenV3?TokenAPI=" +
				token;
	}

	public static String URL_GET_FILTER_VOI_NHOM_KHUYEN_MAI_ID(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetFilterVoiNhonKhuyenMaiId?TokenAPI=" + token;
	}
	public static String URL_GET_QUAN_HUYEN_TIM_KIEM(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetQuanHuyenTimKiem?TokenAPI=" + token;
	}

	public static String URL_GET_DOI_TAC(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetDoiTacV5?TokenAPI=" + token;
	}

    public static String URL_GET_DOI_TAC_AROUND_LOCATION(String token) {
        return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetDoiTacAroundLocationV4?TokenAPI=" + token;
    }

    public static String URL_GET_DOI_TAC_AROUND_ON_MAP(String token) {
        return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetDoiTacAroundLocationOnMap?TokenAPI=" + token;
    }

	public static String URL_GetChiNhanhDoiTacV1(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetChiNhanhDoiTac_V1?TokenAPI=" + token;
	}

    public static String URL_VERIFY_CHECKIN_BEFORE(String token) {
        return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"VerifyCheckInBefore?TokenAPI=" + token;
    }

    public static String URL_VERIFY_CHECKIN_AFTER(String token) {
        return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"VerifyCheckInAfterV3?TokenAPI=" + token;
    }

	public static String URL_SET_YEU_THICH_DTKM(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"SetYeuThichDoiTacKhuyenMai?TokenAPI=" +
				token;
	}

	public static String URL_SetUaThichBinhLuanDoiTacKhuyenMaiByNguoiDungId(
			String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"SetUaThichBinhLuanDoiTacKhuyenMaiByNguoiDungId?TokenAPI=" +
				token;
	}

	public static String URL_GetBinhLuanKhuyenMai(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"GetBinhLuanKhuyenMai?TokenAPI=" + token;
	}

    public static String URL_SET_DANH_GIA_DTKM_BY_NGUOIDUNGID(String token) {
        return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"SetDanhGiaDoiTacKhuyenMaiByNguoiDungId?TokenAPI=" + token;
    }
	
	public static String URL_AddBinhLuanDoiTacKhuyenMai(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" +
				"AddBinhLuanDoiTacKhuyenMai?TokenAPI=" + token;
	}

	public static String URL_NHOM_KM(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetNhomKhuyenMaiAll?TokenAPI=" + token;
	}

	//
	public static String URL_GET_CHI_TIET_DOI_TAC(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetChiTietDoiTacV1?TokenAPI=" + token;
	}

	public static String URL_GET_CHI_TIET_UU_DAI(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetChiTietUuDai?TokenAPI=" + token;
	}

	public static String URL_GET_CHI_TIET_GIOI_THIEU(String token) {
		return Constants.APP_DOMAIN +
				"partnerservice.asmx/" + "GetChiTietGioiThieu?TokenAPI=" + token;
	}

	public static String URL_Tin_Khuyen_Mai(String token) {
		return Constants.APP_DOMAIN +
				"goixeservice.asmx/" + "GetTinKhuyenMaiV2?TokenAPI=" + token;
	}
	
	public static String URL_ADD_CHECKIN(String token) {
		return Constants.APP_DOMAIN +
				"goixeservice.asmx/" + "AddCheckInV4?TokenAPI=" + token;
	}
	
	public static String URL_GET_TONG_DAI_HANG(String token) {
		return Constants.APP_DOMAIN +
				"goixeservice.asmx/" + "GetTongDaiHangV1?TokenAPI=" + token;
	}

	public static String URL_UNIX_TIME_NOW(String token) {
		return Constants.APP_DOMAIN +
				"commonservice.asmx/" + "UnixTimeNow?TokenAPI=" +
				token;
	}
    public static String URL_GET_NEWS_LETTER(String token) {
        return Constants.APP_DOMAIN +
				"commonservice.asmx/" + "GetNewsletterV2?TokenAPI=" +
				token;
    }

	public static String URL_GET_APP_VESION(String token) {
		return Constants.APP_DOMAIN +
				"Commonservice.asmx/" + "GetAppVersions?TokenAPI=" +
				token;
	}

	public static String URL_VERIFY_TRANG_THAI_DAT_XE(String token) {
		return Constants.APP_DOMAIN +
				"GoiXeService.asmx/" +
				"VerifyTrangThaiDatXe?TokenAPI=" + token;
	}
	public static String URL_GET_DANH_MUC_TRANG_CHU(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetDanhMucTrangChuV3?TokenAPI=" + token;
	}
	public static String URL_GET_DANH_SACH_BO_SUU_TAP(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetDanhSachBoSuuTap?TokenAPI=" + token;
	}
	public static String URL_GET_CHI_TIET_TRANG_CHU(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetChiTietTrangChuV1?TokenAPI=" + token;
	}
	public static String URL_GET_DOI_TAC_GAN_BAN(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetDoiTacGanBanV1?TokenAPI=" + token;
	}
	public static String URL_GET_DOI_TAC_GAN_VI_TRI(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetDoiTacGanViTriV1?TokenAPI=" + token;
	}
	public static String URL_GET_DANH_SACH_DANH_MUC(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetDanhSachDanhMuc?TokenAPI=" + token;
	}
	public static String URL_GET_CHI_TIET_DANH_MUC(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetChiTietDanhMucV1?TokenAPI=" + token;
	}

	public static String URL_GET_KHU_VUC_BO_LOC(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetKhuVucBoLoc?TokenAPI=" + token;
	}

	public static String URL_GET_CHI_TIET_BO_LOC(String token) {
		return Constants.APP_DOMAIN +
				"PartnerService.asmx/" +
				"GetChiTietBoLocV1?TokenAPI=" + token;
	}

    public static String URL_GET_MA_QUOC_GIA_BY_NAME() {
        return Constants.APP_DOMAIN +
				"Commonservice.asmx/" + "GetMaQuocGiaByName?TokenAPI=" + Pasgo.getInstance().token;
    }

    public static String URL_VERIFY_SDT_KICHHOAT() {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" + "VerifySdtKichHoat?TokenAPI=" + Pasgo.getInstance().token;
    }

    public static String URL_SEND_MA_KICH_HOAT() {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" + "SendMaKichHoatV1?TokenAPI=" + Pasgo.getInstance().token;
    }
    public static String URL_LOGIN(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" + "Login?TokenAPI=" +
				token;
    }

    public static String URL_UPDATE_PASSWORD(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" + "UpdatePassword?TokenAPI=" +
				token;
    }

    public static String URL_GET_LINH_VUC_QUAN_TAM_ALL(String token) {
        return Constants.APP_DOMAIN +
				"commonservice.asmx/" + "GetLinhVucQuanTamAll?TokenAPI=" +
				token;
    }

    public static String URL_GET_NGUOIDUNGPASGO_BY_ID(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" +
				"GetNguoiDungById?TokenAPI=" + token;
    }

    public static String URL_UPDATE_LINH_VUC_QUAN_TAM(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" +
				"UpdateLinhVucQuanTam?TokenAPI=" + token;
    }

    public static String URL_UPDATE_MA_NGUOI_GIOI_THIEU(String token) {
        return Constants.APP_DOMAIN +
				"NguoiDungService.asmx/" + "UpdateMaNguoiGioiThieuV1?TokenAPI=" +
				token;
    }

}