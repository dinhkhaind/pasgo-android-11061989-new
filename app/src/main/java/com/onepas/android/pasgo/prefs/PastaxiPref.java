package com.onepas.android.pasgo.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.onepas.android.pasgo.Constants;

import java.util.HashMap;

public class PastaxiPref {

	public static final String LANGUAGE = "language";
	public static final String KEY_LAT_LOCATION_RECENT = "latLocationRecent";
	public static final String KEY_LNG_LOCATION_RECENT = "lngLocationRecent";
	private static final String PREF_NAME = "PastaxiPref";
	private static final String KEY_CHECK_STATE_KICHHOAT_ACTIVITY="check_if_kichhoat";
    private static final String KEY_CHECK_STATE_UPDATE_PASSWORD_ACTIVITY="check_if_update_password";
    private static final String KEY_CHECK_IF_GO_TO_REGISTER_ACTIVITY ="if_go_to_register_activity";
    private static final String KEY_CHECK_STATE_PRESENTER_ACTIVITY="check_if_go_to_activity";
    private static final String PREF_NATION_CODE = "nation_code";
    private static final String PREF_NATION_NAME = "nation_name";
    private static final String KEY_SO_GIOI_THIEU = "so_nguoi_gioi_thieu";
	private static final String KEY_NHOM_KHUYEN_MAI="key_nhom_khuyen_MAI";
	private static final String KEY_TINH_MAIN="key_tinh_main";
	private static final String KEY_TINH_ID="KEY_TINH_ID";
	private static final String KEY_DANH_SACH_DOI_TAC_KHUYEN_MAI_ID_DA_CHECK="danhSachKhuyenMaiIdDaCheck";
	private static final String KEY_NHOM_KHUYEN_MAI_ID="nhomKhuyenMaiId";
	private static final String KEY_TRIAL = "KEY_TRIAL";
	private static final String KEY_CHAT_UPDATE_INFO = "KEY_CHAT_UPDATE_INFO";
	private static final String KEY_TIME_OVER_MESSAGE="KEY_TIME_OVER_MESSAGE";
	private SharedPreferences pref;
	private Editor editor;
	private int PRIVATE_MODE = 0;

	private Context context;

	public PastaxiPref(Context context) {
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void putIsKichHoat(boolean token) {
		putBooleanValue(Constants.isKichHoat, token);
	}

	public boolean getisKichHoat() {
		return getBooleanValue(Constants.isKichHoat);
	}

    public void putIsUpdatePassword(boolean token) {
        putBooleanValue(Constants.isUpdatePassword, token);
    }

    public boolean getisUpdatePassword() {
        return getBooleanValue(Constants.isUpdatePassword);
    }
	
	public void putIsKichHoatActivity(boolean b) {
		putBooleanValue(KEY_CHECK_STATE_KICHHOAT_ACTIVITY, b);
	}

	public boolean getIsKichHoatActivity() {
		return getBooleanValue(KEY_CHECK_STATE_KICHHOAT_ACTIVITY);
	}

    public void putIsUpdatePasswordActivity(boolean b) {
        putBooleanValue(KEY_CHECK_STATE_UPDATE_PASSWORD_ACTIVITY, b);
    }

    public boolean getIsUpdatePasswordActivity() {
        return getBooleanValue(KEY_CHECK_STATE_UPDATE_PASSWORD_ACTIVITY);
    }
    // kiển tra nếu nhập mật khẩu xong mà nó đến từ màn hình đăng kí=> cho người ta nhập người giới thiệu
    public void putCheckIfGoToRegisterActivity(boolean b) {
        putBooleanValue(KEY_CHECK_IF_GO_TO_REGISTER_ACTIVITY, b);
    }

    public boolean getCheckIfGoToRegisterActivity() {
        return getBooleanValue(KEY_CHECK_IF_GO_TO_REGISTER_ACTIVITY);
    }

    public void putIsPresenterActivity(boolean b) {
        putBooleanValue(KEY_CHECK_STATE_PRESENTER_ACTIVITY, b);
    }

    public boolean getIsPresenterActivity() {
        return getBooleanValue(KEY_CHECK_STATE_PRESENTER_ACTIVITY);
    }

	public void putIsTrial(boolean b) {
		putBooleanValue(KEY_TRIAL, b);
	}

	public boolean getIsTrial() {
		return getBooleanValue(KEY_TRIAL);
	}
	// String data, String thoiGianDonXe,
	// double startLat, double startLng, double km,
	// double phanTramGiamGia, String gia, String startAddress,
	// double endLat, double endLng, double khachHangLat,
	// double khachhangLng, String endAddress, String datXeId,
	// String loaiXeName, double laiXeLat, double laiXeLng,
	// String laiXeId, String laiXeName, String laiXePhone, String loaiXeId
	public void putDataOrder(String data, String thoiGianDonXe,
			String startLat, String startLng, String km,
			String phanTramGiamGia, String gia, String startAddress,
			String endLat, String endLng, String khachHangLat,
			String khachhangLng, String endAddress, String datXeId,
			String loaiXeName, String laiXeLat, String laiXeLng,
			String laiXeId, String laiXeName, String laiXePhone, String loaiXeId) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(Constants.KEY_FULL_MESSAGE_DRIVER, data);
		editor.putString(Constants.BUNDLE_THOI_GIAN, thoiGianDonXe);
		editor.putString(Constants.BUNDLE_START_LAT, startLat);
		editor.putString(Constants.BUNDLE_START_LNG, startLng);
		editor.putString(Constants.BUNDLE_KM, km);
		editor.putString(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA, phanTramGiamGia
				+ "");
		editor.putString(Constants.BUNDLE_GIA, gia);
		editor.putString(Constants.BUNDLE_START_ADDRESS, startAddress);
		editor.putString(Constants.BUNDLE_END_LAT, endLat);
		editor.putString(Constants.BUNDLE_END_LNG, endLng);
		editor.putString(Constants.BUNDLE_KHACHHANG_LAT, khachHangLat);
		editor.putString(Constants.BUNDLE_KHACHHANG_LNG, khachhangLng);
		editor.putString(Constants.BUNDLE_END_ADDRESS, endAddress);
		editor.putString(Constants.BUNDLE_DAT_XE_ID, datXeId);
		editor.putString(Constants.BUNDLE_LOAIXE_NAME, loaiXeName);
		editor.putString(Constants.BUNDLE_LAIXE_LAT, laiXeLat);
		editor.putString(Constants.BUNDLE_LAIXE_LNG, laiXeLng);
		editor.putString(Constants.BUNDLE_LAIXE_ID, laiXeId);
		editor.putString(Constants.BUNDLE_LAIXE_NAME, laiXeName);
		editor.putString(Constants.BUNDLE_LAIXE_PHONE, laiXePhone);
		editor.putString(Constants.BUNDLE_DICH_VU_ID, loaiXeId);
		editor.commit();
	}

	public HashMap<String, String> getDataOrder() {
		HashMap<String, String> mapData = new HashMap<String, String>();
		try {
			SharedPreferences pref = context.getSharedPreferences(
					Constants.PWNSHARE_PREFERENCES, 0);
			mapData.put(Constants.KEY_FULL_MESSAGE_DRIVER,
					pref.getString(Constants.KEY_FULL_MESSAGE_DRIVER, null));
			mapData.put(Constants.BUNDLE_THOI_GIAN,
					pref.getString(Constants.BUNDLE_THOI_GIAN, null));
			mapData.put(Constants.BUNDLE_START_LAT,
					pref.getString(Constants.BUNDLE_START_LAT, null));
			mapData.put(Constants.BUNDLE_START_LNG,
					pref.getString(Constants.BUNDLE_START_LNG, null));
			mapData.put(Constants.BUNDLE_KM,
					pref.getString(Constants.BUNDLE_KM, null));
			mapData.put(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA,
					pref.getString(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA, null));
			mapData.put(Constants.BUNDLE_GIA,
					pref.getString(Constants.BUNDLE_GIA, null));
			mapData.put(Constants.BUNDLE_START_ADDRESS,
					pref.getString(Constants.BUNDLE_START_ADDRESS, null));
			mapData.put(Constants.BUNDLE_END_LAT,
					pref.getString(Constants.BUNDLE_END_LAT, null));
			mapData.put(Constants.BUNDLE_END_LNG,
					pref.getString(Constants.BUNDLE_END_LNG, null));
			mapData.put(Constants.BUNDLE_KHACHHANG_LAT,
					pref.getString(Constants.BUNDLE_KHACHHANG_LAT, null));
			mapData.put(Constants.BUNDLE_KHACHHANG_LNG,
					pref.getString(Constants.BUNDLE_KHACHHANG_LNG, null));
			mapData.put(Constants.BUNDLE_END_ADDRESS,
					pref.getString(Constants.BUNDLE_END_ADDRESS, null));
			mapData.put(Constants.BUNDLE_DAT_XE_ID,
					pref.getString(Constants.BUNDLE_DAT_XE_ID, null));
			mapData.put(Constants.BUNDLE_LOAIXE_NAME,
					pref.getString(Constants.BUNDLE_LOAIXE_NAME, null));
			mapData.put(Constants.BUNDLE_LAIXE_LAT,
					pref.getString(Constants.BUNDLE_LAIXE_LAT, null));
			mapData.put(Constants.BUNDLE_LAIXE_LNG,
					pref.getString(Constants.BUNDLE_LAIXE_LNG, null));
			mapData.put(Constants.BUNDLE_LAIXE_ID,
					pref.getString(Constants.BUNDLE_LAIXE_ID, null));
			mapData.put(Constants.BUNDLE_LAIXE_NAME,
					pref.getString(Constants.BUNDLE_LAIXE_NAME, null));
			mapData.put(Constants.BUNDLE_LAIXE_PHONE,
					pref.getString(Constants.BUNDLE_LAIXE_PHONE, null));
			mapData.put(Constants.BUNDLE_DICH_VU_ID,
					pref.getString(Constants.BUNDLE_DICH_VU_ID, null));
			return mapData;
		} catch (Exception e) {
			return null;
		}
	}
	public void putTinhMain(String token) {
		putStringValue(KEY_TINH_MAIN, token);
	}

	public String getTinhMain() {
		return getStringValue(KEY_TINH_MAIN);
	}
	public void putDanhSachKhuyenMaiIdDaDatCho(String str) {
		putStringValue(KEY_DANH_SACH_DOI_TAC_KHUYEN_MAI_ID_DA_CHECK, str);
	}

	public int getTinhId() {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		return pref.getInt(KEY_TINH_ID, 0);
	}
	public void putTinhId(int str) {
		putIntValue(KEY_TINH_ID, str);
	}

	public String getDanhSachKhuyenMaiIdDatCho() {
		return getStringValue(KEY_DANH_SACH_DOI_TAC_KHUYEN_MAI_ID_DA_CHECK);
	}
	public void putNhomKhuyenMai(String token) {
		putStringValue(KEY_NHOM_KHUYEN_MAI, token);
	}

	public String getNhomKhuyenMai() {
		return getStringValue(KEY_NHOM_KHUYEN_MAI);
	}

	public void putLanguage(String language) {
		putStringValue(LANGUAGE, language);
	}

	public String getLanguage() {
		return getStringValue(LANGUAGE);
	}

    public void putNationCode(String language) {
        putStringValue(PREF_NATION_CODE, language);
    }

    public String getNationCode() {
        return getStringValue(PREF_NATION_CODE);
    }
    public void putNationName(String language) {
        putStringValue(PREF_NATION_NAME, language);
    }

    public String getNationName() {
        return getStringValue(PREF_NATION_NAME);
    }

	public void putFcmToken(String token) {
		putStringValue(Constants.KEY_FCM_TOKEN, token);
	}

	public String getFcmToken() {
		return getStringValue(Constants.KEY_FCM_TOKEN);
	}

	public void putTimeCall(String value) {
		putStringValue(Constants.KEY_TIME_CALL, value);
	}

	public void putNhomKhuyenMaiId(int nhomKmId) {
		putIntValue(KEY_NHOM_KHUYEN_MAI_ID, nhomKmId);
	}

	public int getNhomKhuyenMaiId() {
		return getIntValue(KEY_NHOM_KHUYEN_MAI_ID);
	}

	public void putTimeUpdateOverMessage(long time) {
		putLongValue(KEY_TIME_OVER_MESSAGE, time);
	}

	public long getTimeUpdateOverMessage() {
		return getLongValue(KEY_TIME_OVER_MESSAGE);
	}

	public long getTimeCall() {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		String value = pref.getString(Constants.KEY_TIME_CALL, null);
		if (value == null || "".equals(value))
			return 0;
		else
			return Long.parseLong(value);
	}

	public String getStringPre(String key) {
		return getStringValue(key);
	}

	public void putToken(String token) {
		putStringValue(Constants.KEY_TOKEN, token);
	}

	public String getToken() {
		return getStringValue(Constants.KEY_TOKEN);
	}

	public void putUserId(String token) {
		putStringValue(Constants.KEY_userId, token);
	}

	public String getUserId() {
		return getStringValue(Constants.KEY_userId);
	}

	public void putUserName(String token) {
		putStringValue(Constants.KEY_username, token);
	}

	public String getUserName() {
		return getStringValue(Constants.KEY_username);
	}

	public void putSdt(String token) {
		putStringValue(Constants.KEY_SDT, token);
	}

	public String getSdt() {
		return getStringValue(Constants.KEY_SDT);
	}

	public void putEmail(String token) {
		putStringValue(Constants.KEY_email, token);
	}

	public String getEmail() {
		return getStringValue(Constants.KEY_email);
	}

    public void putUrlAnh(String str) {
        putStringValue(Constants.KEY_URL_ANH, str);
    }

    public String getUrlAnh() {
        return getStringValue(Constants.KEY_URL_ANH);
    }
	
	public void putMa(String token) {
		putStringValue(Constants.KEY_MA_ID, token);
	}

	public String getMa() {
		return getStringValue(Constants.KEY_MA_ID);
	}

	public void putUUID(String token) {
		putStringValue(Constants.KEY_MA_UUID, token);
	}

	public String getUUID() {
		return getStringValue(Constants.KEY_MA_UUID);
	}

	public void putMakichhoat(String token) {
		putStringValue(Constants.KEY_maKichHoat, token);
	}

	public String getMakichhoat() {
		return getStringValue(Constants.KEY_maKichHoat);
	}
	public void putRoomChat(String token) {
		putStringValue(Constants.KEY_ROOM_CHAT, token);
	}

	public String getRoomChat() {
		return getStringValue(Constants.KEY_ROOM_CHAT);
	}
	public void putChatUpdateInfo(boolean token) {
		putBooleanValue(KEY_CHAT_UPDATE_INFO, token);
	}

	public boolean getChatUpdateInfo() {
		return getBooleanValue(KEY_CHAT_UPDATE_INFO);
	}

    public void putSoNguoiGioiThieu(String token) {
        putStringValue(KEY_SO_GIOI_THIEU, token);
    }

    public String getSoNguoiGioiThieu() {
        return getStringValue(KEY_SO_GIOI_THIEU);
    }

	public void putBooleanValue(String key, boolean s) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, s);
		editor.apply();
	}

	public boolean getBooleanValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}
	public void putIntValue(String key, int s) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, s);
		editor.apply();
	}
	public int getIntValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		return pref.getInt(key, 1);
	}
	public void putLongValue(String key, long s) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, s);
		editor.apply();
	}
	public long getLongValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		return pref.getLong(key, 1);
	}
	public void putStringValue(String key, String s) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, s);
		editor.apply();
	}

	public String getStringValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.PWNSHARE_PREFERENCES, 0);
		return pref.getString(key, "");
	}

	public void createLatLocationRecent(String data) {
		editor.putString(KEY_LAT_LOCATION_RECENT, data);
		editor.apply();
	}

	public String getLatLocationRecent() {
		return pref.getString(KEY_LAT_LOCATION_RECENT, "0");
	}

	public void createLngLocationRecent(String data) {
		editor.putString(KEY_LNG_LOCATION_RECENT, data);
		editor.apply();
	}

	public String getLngLocationRecent() {
		return pref.getString(KEY_LNG_LOCATION_RECENT, "0");
	}
}