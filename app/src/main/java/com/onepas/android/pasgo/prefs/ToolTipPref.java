package com.onepas.android.pasgo.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.onepas.android.pasgo.Constants;

public class ToolTipPref {

	public static final String TOOLTIP_MAIN_GOI_XE = "main_goi_xe";
	public static final String TOOLTIP_MAIN_KHUYEN_MAI = "main_khuyen_mai";
	public static final String TOOLTIP_THONG_TIN_DAT_XE_DAT_XE = "thong_tin_dat_xe_dat_xe";
	public static final String TOOLTIP_XAC_NHAN_TIEP_TUC = "xac_nhan_tiep_tuc";
	public static final String TOOLTIP_TIM_TAI_XE_HUY_GOI_XE = "tim_tai_xe_huy_goi_xe";
	public static final String TOOLTIP_TIM_TAI_XE_THU_LAI = "tim_tai_xe_thu_lai";
	public static final String TOOLTIP_TIM_TAI_XE_GOI_HANG = "tim_tai_xe_goi_hang";
	public static final String TOOLTIP_THEO_DOI_TAI_XE_GOI_DI_DONG = "theo_doi_tai_xe_goi_di_dong";
	public static final String TOOLTIP_THEO_DOI_TAI_XE_XONG = "theo_doi_tai_xe_xong";
	private static final String PREF_NAME = "ToolTipPref";
	private int PRIVATE_MODE = 0;
	public static final String isKichHoat = "isKichHoat";
	private Context context;

	public ToolTipPref(Context context) {
		this.context = context;
		context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
	}

	public void putMainGoiXe(boolean isB) {
		putBooleanValue(TOOLTIP_MAIN_GOI_XE, isB);
	}

	public boolean getMainGoiXe() {
		return getBooleanValue(TOOLTIP_MAIN_GOI_XE);

	}

	public void putMainKhuyenMai(boolean isB) {
		putBooleanValue(TOOLTIP_MAIN_KHUYEN_MAI, isB);
	}

	public boolean getMainKhuyenMai() {
		return getBooleanValue(TOOLTIP_MAIN_KHUYEN_MAI);

	}

	public void putThongTinDatXe(boolean isB) {
		putBooleanValue(TOOLTIP_THONG_TIN_DAT_XE_DAT_XE, isB);
	}

	public boolean getThongTinDatXe() {
		return getBooleanValue(TOOLTIP_THONG_TIN_DAT_XE_DAT_XE);

	}

	public void putXacNhanTiepTuc(boolean isB) {
		putBooleanValue(TOOLTIP_XAC_NHAN_TIEP_TUC, isB);
	}

	public boolean getXacNhanTiepTuc() {
		return getBooleanValue(TOOLTIP_XAC_NHAN_TIEP_TUC);

	}

	public void putTimTaiXeHuyGoiXe(boolean isB) {
		putBooleanValue(TOOLTIP_TIM_TAI_XE_HUY_GOI_XE, isB);
	}

	public boolean getTimTaiXeHuyGoiXe() {
		return getBooleanValue(TOOLTIP_TIM_TAI_XE_HUY_GOI_XE);

	}

	public void putTimTaiXeThuLai(boolean isB) {
		putBooleanValue(TOOLTIP_TIM_TAI_XE_THU_LAI, isB);
	}

	public boolean getTimTaiXeThuLai() {
		return getBooleanValue(TOOLTIP_TIM_TAI_XE_THU_LAI);

	}

	public void putTimTaiXeGoiHang(boolean isB) {
		putBooleanValue(TOOLTIP_TIM_TAI_XE_GOI_HANG, isB);
	}

	public boolean getTimTaiXeGoiHang() {
		return getBooleanValue(TOOLTIP_TIM_TAI_XE_GOI_HANG);

	}

	public void putTheoDoiTaiXeGoiDiDong(boolean isB) {
		putBooleanValue(TOOLTIP_THEO_DOI_TAI_XE_GOI_DI_DONG, isB);
	}

	public boolean getTheoDoiTaiXeGoiDiDong() {
		return getBooleanValue(TOOLTIP_THEO_DOI_TAI_XE_GOI_DI_DONG);

	}

	public void putTheoDoiTaiXeXong(boolean isB) {
		putBooleanValue(TOOLTIP_THEO_DOI_TAI_XE_XONG, isB);
	}

	public boolean getTheoDoiTaiXeXong() {
		return getBooleanValue(TOOLTIP_THEO_DOI_TAI_XE_XONG);

	}

	public void putBooleanValue(String key, boolean s) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, s);
		editor.commit();
	}

	public boolean getBooleanValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	public void putStringValue(String key, String s) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, s);
		editor.commit();
	}

	public String getStringValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		return pref.getString(key, "");
	}

}