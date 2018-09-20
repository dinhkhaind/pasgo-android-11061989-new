package com.onepas.android.pasgo;

import java.io.File;

public final class Constants {
	public static boolean IsDeveloper  = !BuildConfig.DEBUG;
	public static final boolean IS_OPERATOR = false;
	public static final String API_MAP = "AIzaSyAFqGWRbqq-0JPjVLhRE-esr0aNyKdggHc";
	public static final String PLACES_API_KEY ="AIzaSyCPBkYE11knX7mTumsJd1tTkAhk0CQHV3g";
	public static final String CLIENT_ID = "1VMD5NJEWQSKBULDT3IT1BC2R0BCZ0HWO4Y1TDUQZEQH1UA3";
	public static final String CLIENT_SECRET = "FV03RLFFOYEXTEJRJMCWYBSG2QH0JZHEINVEBYZYJ1G2SBQG";
	public static final String SDT_TONG_DAI ="19006005";

	//Socket
	public static final String E_GET_USER_NAME = "sc-get-username";
	public static final String E_CONNECT = "sc-connect";
	public static final String E_MESSAGE = "sc-message";
	public static final String E_ROOM_CONVERSATION = "sc-room-conversation";
	public static final String E_TYPING = "sc-typing";
	public static final String E_TYPING_REMOVE = "sc-typing-remove";
	public static final String E_CLIENT_UPDATE_INFO = "sc-client-update-info";
	public static final String E_SUPPORT_ONLINE = "sc-support-online";
	public static final String IS_KHACH = "KHACH_HANG";
	public static final String IS_TONG_DAI_VIEN = "TONG_DAI_VIEN";
	public static final String IS_PASGO = "PASGO";
	public static int PAGE_SIZE_CHAT =50;
	public static int TYPING_TIMER_LENGTH =400;

    public static final String PG_MESSAGE =  "pg-message";
	// end socket

	public static final String URL_CHAT_NGAY = "https://pasgo.vn/ho-tro-truc-tuyen";
	public static final String KEY_PASGO = "http://www.pasgo.vn/app";
	public static final String KEY_BAO_MAT = "http://pasgo.vn/chinhsachbaomat/";
	public static final String KEY_TYPE_PUSH_NOTIFICATION = "type";
	public static final int KEY_PUSH_NOTIFICATION_TIN_HTML = 1;
	public static final int KEY_PUSH_NOTIFICATION_TIN_DOI_TAC = 2;
	public static final int KEY_PUSH_NOTIFICATION_TIN_NHOM_DOI_TAC = 3;
	public static final int KEY_PUSH_NOTIFICATION_THE_PASGO_DATCHO = 4;
	public static final int KEY_PUSH_NOTIFICATION_THE_PASGO_DATXE = 5;
	public static final int KEY_ACTIVITY_THE_PASGO = 35;

	public static final String KEY_DIEU_KHOAN_VN = "http://pasgo.vn/chinh-sach-va-dieu-khoan/";
	public static final String KEY_DIEU_KHOAN_EN = "http://pasgo.vn/e-chinh-sach-va-dieu-khoan/";

	public static final String KEY_TERM_AND_POLICIES1_VN = "https://pasgo.vn/Common/IntroduceAppVn";
	public static final String KEY_TERM_AND_POLICIES2_VN = "https://pasgo.vn/Common/RegulationAppVn";
	public static final String KEY_TERM_AND_POLICIES3_VN = "https://pasgo.vn/Common/SecurityAppVn";
	public static final String KEY_TERM_AND_POLICIES4_VN = "https://pasgo.vn/Common/ConflictAppVn";
	public static final String KEY_TERM_AND_POLICIES5_VN = "http://pasgo.vn/dieu-khoan-ma-tai-tro/";
	public static final String KEY_TERM_AND_POLICIES6_VN = "https://pasgo.vn/Common/PartnerAppVn";

	public static final String KEY_TERM_AND_POLICIES1_EN = "https://pasgo.vn/Common/IntroduceAppEn";
	public static final String KEY_TERM_AND_POLICIES2_EN = "https://pasgo.vn/Common/RegulationAppEn";
	public static final String KEY_TERM_AND_POLICIES3_EN = "https://pasgo.vn/Common/SecurityAppEn";
	public static final String KEY_TERM_AND_POLICIES4_EN = "https://pasgo.vn/Common/ConflictAppEn";
	public static final String KEY_TERM_AND_POLICIES5_EN = "http://pasgo.vn/e-dieu-khoan-ma-tai-tro/";
	public static final String KEY_TERM_AND_POLICIES6_EN = "https://pasgo.vn/Common/PartnerAppEn";

	public static final String KEY_CHECK_IN_INTRODUCTION_VN = "http://pasgo.vn/huong-dan-check-in/";
	public static final String KEY_CHECK_IN_INTRODUCTION_EN = "http://pasgo.vn/e-huong-dan-check-in/";
	public static final String KEY_MA_TAI_TRO_INTRODUCTION_VN = "http://pasgo.vn/huong-dan-ma-tai-tro/";
	public static final String KEY_MA_TAI_TRO_INTRODUCTION_EN = "http://pasgo.vn/e-huong-dan-ma-tai-tro/";
	public static final String KEY_THE_PASGO_SHARE_VN="http://pasgo.vn/chia-se";
	public static final String KEY_THE_PASGO_SHARE_EN="http://pasgo.vn/e-chia-se";


	public static final String API_URL = "https://api.foursquare.com/v2/venues/search?intent=browse&near=";
	public static final String FOURSQUARE_API_URL_CHECKIN = "https://api.foursquare.com/v2/venues/search?intent=checkin&near=";
	public static final String API_PARAM0 = "&radius=1400&query=";
	public static final String API_PARAM1 = "&radius=5000&query=";
	public static final String API_PARAM2 = "&client_id=" + CLIENT_ID;
	public static final String API_SECRET = "&client_secret=" + CLIENT_SECRET;

	public static final String KEY_Sign_Up_Changed = "Sign_Up_Changed";
	public static final String KEY_LICH_SU_ID = "LichSuId";
	public static final String GOOGLE_SENDER_ID = "734987776088";
	public static final String KEY_DIRECTION_JSON = "str_direction";
	public static final int CONNECT_SOCKET_TIMEOUT = 10000;
	public static final String PACKAGE_NAME = "com.onepas.android.pastaxi";
	public static final String STORAGE_DIRECTORY_DEFAULT = Constants.DATABASE_ROOT
			+ File.separator + Constants.PACKAGE_NAME;
	public static final String DATABASE_ROOT = "com.onepas.android.pastaxi";
	public static final int kEY_CHON_LOAI_XE = 211;
	public static final int kEY_CHON_DIEM_DON = 212;
	public static final int kEY_CHON_DIEM_DEN = 213;
	public static final int KEY_CHON_DIEM_DIEM_TAI_TRO = 214;
	public static final int kEY_BACK_FORM_DATXE = 215;
	public static final int kEY_BACK_FORM_GOOGLEPLAYSERVICE = 216;
	public static final int kEY_COMMENT_KHUYEM_MAI = 217;
	public static final int KEY_BACK_BY_MA_DAT_XE = 220;
	public static final int KEY_SELECT_LANGUAGE = 221;
	// tất cả mọi activity liên quan đến đặt chỗ dùng Key(228) này
	public static final int KEY_BACK_BY_MA_DAT_CHO = 228;
	public static final int KEY_BACK_BY_DAT_CHO = 229;
	public static final int KEY_LOGIN = 222;
	public static final int KEY_TRIAL = 223;
	public static final int KEY_GO_TO_KICH_HOAT = 226;
	public static final int KEY_GO_TO_USERINFO = 227;
	public static final int KEY_UPDATE_PASSWORD_NEW = 225;
	public static final int KEY_GO_TO_MAP = 1990;
	public static final int KEY_GO_TO_HOME = 1512;
	public static final int KEY_KICH_HOAT = 216;
	public static final String KEY_GO_TO = "goToScreen";
	public static final int KEY_BACK_ACTIVITY_DELAY = 1500;

	public static final int KEY_TAG_THEM = -1001;

	public static final int KEY_BACK_FROM_TIN_KM = 21014;
	public static int Back_Fg = 1;

	public static String PROTOCOL_HTTP = "http://";
	public static String PROTOCOL_HTTPS = "https://";
	public static final String APP_PUBNUB = IsDeveloper ? "PASTAXI_DEVELOPER" : "PASTAXI";
	public static final String APP_DOMAIN = IsDeveloper ? PROTOCOL_HTTP + "pastaxi-developer.onepas.vn/service/" : PROTOCOL_HTTPS + "pastaxi.onepas.vn/service/";
	public static final String APP_DOMAIN_UPDATE_FILE = IsDeveloper ? PROTOCOL_HTTP + "pastaxi-developer.onepas.vn/service/" : PROTOCOL_HTTP + "pastaxi.onepas.vn/service/";
	public static final String SOCKET_URL =IsDeveloper ?"http://wschat.pasgo.com.vn:3000":"https://wschat.pasgo.vn:8000";
	// Location Service
	public static final String KEY_TOOL_TIP = "com.onepas.pastaxi.datxe.tooltip";
	public static final String KEY_ACTION_FIND_TAXI = "com.onepas.pastaxi.datxe.fivemin";
	public static final long MINUTE = 1000;
	public static final long MINUTE_TOOLTIP = System.currentTimeMillis()
			+ Constants.MINUTE * 20;
	public static final long MINUTE_STOP_SERVICE =  1 * 60 * 1000;
	public static final String KEY_ACTION_STOP_SERVICE = "com.onepas.pastaxi.android.KEY_ACTION_STOP_SERVICE";

	public static final String BROADCAST_ACTION_SOCKET = "com.onepas.pastaxi.socket";
	public static final String KEY_ACTION_SOCKET_EVENT = "com.onepas.pastaxi.socket.event";
	public static final String KEY_ACTION_SOCKET_VALUE = "com.onepas.pastaxi.socket.value";
	public static final String BROADCAST_ACTION_NUMBER_MESSAGE = "com.onepas.pastaxi.socket.number.message";

	public static final String BROADCAST_ACTION_MOVE_MAP = "com.onepas.android.pastaxi.MOVE_MAP";
	public static final String BROADCAST_ACTION_UPDATE_LOCATION = "com.onepas.android.pastaxi.LOCATION_UPDATE";
	public static final String BROADCAST_ACTION_CHANGE_NETWORK = "com.onepas.pasway.pastaxi.android.NETWORK_CHANGE";
	public static final String BROADCAST_ACTION_SIGNOUT = "com.onepas.android.pastaxi.SIGNOUT";
	public static final String BROADCAST_ACTION_REQUEST_LOGIN = "com.onepas.android.pastaxi.REQUEST_LOGIN";
	public static final String BROADCAST_UPDATE_PASSWORD = "com.onepas.android.pastaxi.UPDATE_PASSWORD";
	public static final String BROADCAST_ACTION_UPDATE_DRIVER = "com.onepas.android.pastaxi.DRIVER_UPDATE";
	public static final String BROADCAST_ACTION_ADD_DRIVER = "com.onepas.android.pastaxi.STATE_DRIVER_ADD";
	public static final String BROADCAST_ACTION_UPDATE_STATE_DRIVER = "com.onepas.android.pastaxi.STATE_DRIVER_UPDATE";
	public static final String BROADCAST_ACTION_REMOVE_STATE_DRIVER = "com.onepas.android.pastaxi.STATE_DRIVER_REMOVE";
	public static final String BROADCAST_ACTION_PING_DRIVER = "com.onepas.android.pastaxi.PING_DRIVER";
	public static final String BROADCAST_ACTION_LOAD_DATA_RESERVE = "com.onepas.android.pastaxi.LOAD_DATA_RESERVE";

	public static final String BROADCAST_ACTION = "broadcastAction";
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
	public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	public static final int FASTEST_INTERVAL_IN_SECONDS = 0;
	public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;
	public static final int SMALLEST_DISPLACEMENT = 0;
	public static final int DISTANCE_DISPLACEMENT = 400;
	// End Location Service
	// Pubnub
	public static final String VERSION_DRIVER_PUBNUB = "_DRIVER";
	public static final String VERSION_HANGXE_ORDER_PUBNUB = "_HANGXE_ORDER_PUBNUB";
	public static final String VERSION_ONEPAS_HANGXE_PUBNUB = "_ONEPAS_HANGXE_PUBNUB";
	public static final String VERSION_HANGXE_PUBNUB_BOOKING = "_HANGXE_PUBNUB_";
	// End Pubnub

	public static final String KEY_IS_DIEMDON = "is_diemdon";
	public static final String UPDATE_TYPE_MESSAGE_CLIENT = "update";
	public static final String KEY_FULL_MESSAGE_DRIVER = "dataDriver";
	public static final String KEY_TIME_CALL = "timeCall";
	// End Json Message Pubnub
	public static final String KEY_VIETNAM_CODE = "+84";
	public static final String KEY_VIETNAM_NAME = "Việt Nam(+84)";

	// Broadcast Update UI
	public static final String KEY_LAT_BROADCAST = "latitude";
	public static final String KEY_LNG_BROADCAST = "longitude";
	// End Broadcast Update UI

	// Broadcast Update Driver
	public static final String KEY_DRIVER_ID_BROADCAST = "locationMessageDriverId";
	// End Radius reason
	// Network
	public static final String KEY_RESPONSE = "d";
	public static final String KEY_ERROR_CODE = "MaLoi";
	public static final String KEY_INFO_RESPONSE = "ThongDiep";
	public static final int KEY_SUCCESS_RESPONSE = 0;
	public static final int KEY_LOGIN_RESPONSE = 101;
	public static final int KEY_REGISTER_RESPONSE = 105;
	public static final int KEY_PROMOTION_IS_NOT_VALID = 1302;
	public static final int KEY_PROMOTION_IS_NOT_VALID_V2 = 603;
	public static final int KEY_UPDATE_PASSWORD = 104;
	public static final int KEY_REGISTER_PHONE_IS_EXIST = 218;
	public static final int KEY_PRESENTER_CODE_INVAIL = 219;
	public static final int KEY_GOOGLE_SEARCH = 230;
	public static final int KEY_CHECKIN_IS_EXISTS = 2201;
	public static final int KEY_CHECKIN_MAX = 2202;
	public static final int KEY_CHECKIN_NGOAI_GIO_PHUC_VU = 2204;
	public static final int KEY_SQL_ERROR = 7;
	// End Network

	// Chon loai xe
	public static final String BUNDLE_LOAI_HINH_DICH_VU_ID = "loai_hinh_dich_vu_id";
	public static final String BUNDLE_NHOM_XE_DICH_VU_ID = "nhom_xe_dich_vu_id";
	public static final String BUNDLE_DICH_VU_ID = "loai_hang_id";
	public static final String BUNDLE_LOAIXE_IMAGE = "loai_xe_image";
	public static final String BUNDLE_LOAIXE_NAME = "loai_xe_name";
	public static final String BUNDLE_CALL_STOP = "callStop";
	// End Chon loai xe

	// Bunble
	public static final String BUNDLE_KEY_GO_TO_FROM_SPLASH_ACTIVITY = "isGoToSplashActivity";
	public static final String BUNDLE_KEY_GO_TO_FROM_RESERVED_HISTORY = "BUNDLE_KEY_GO_TO_FROM_RESERVED_HISTORY";
	public static final String BUNDLE_KEY_TEXT_FROM_RESERVED_HISTORY = "BUNDLE_KEY_TEXT_FROM_RESERVED_HISTORY";

	public static final String BUNDLE_IS_GO_TO_DATXE = "bundle_is_go_to_dat_xe";
	public static final String BUNDLE_TAB_NUMBER = "bundle_tab_number";
	public static final String BUNDLE_LAT = "bun_lat";
	public static final String BUNDLE_LNG = "bun_lng";
	public static final String BUNDLE_SEARCH_GO_TO_HOME = "BUNDLE_SEARCH_GO_TO_HOME";
	public static final String BUNDLE_LOAI_HINH_DV_ID = "loadHinhDichVuId";

	public static final String BUNDLE_TRIAL_REGISTER = "BUNDLE_TRIAL_REGISTER";

	public static final String KEY_NAME = "KEY_NAME";
	public static final String KEY_VICINITY = "KEY_VICINITY";
	public static final String BUNDLE_START_LNG = "start_longitide";
	public static final String BUNDLE_START_LAT = "start_latiude";
	public static final String BUNDLE_END_LNG = "end_longitide";
	public static final String BUNDLE_END_LAT = "end_latiude";
	public static final String BUNDLE_KHACHHANG_LNG = "khachhang_longitide";
	public static final String BUNDLE_KHACHHANG_LAT = "khachhang_latiude";
	public static final String BUNDLE_DRIVERS_SORTED = "driversSorted";
	public static final String BUNDLE_START_ADDRESS = "start_address";
	public static final String BUNDLE_LAIXE_LAT = "laixe_latiude";
	public static final String BUNDLE_LAIXE_LNG = "laixe_longitude";
	public static final String BUNDLE_END_ADDRESS = "end_address";
	public static final String BUNDLE_START_NAME = "start_name";
	public static final String BUNDLE_END_NAME = "end_name";
	public static final String BUNDLE_MOTA = "datxe_mota";
	public static final String BUNDLE_KM = "so_km";
	public static final String BUNDLE_PHAN_TRAM_GIAM_GIA = "phan_tram_giam_gia";
	public static final String BUNDLE_GIA = "gia";
	public static final String BUNDLE_DAT_XE_ID = "dat_xe_id";
	public static final String BUNDLE_THOI_GIAN = "thoi_gian";
	public static final String BUNDLE_LAIXE_ID = "lai_xe_id";
	public static final String BUNDLE_LAIXE_NAME = "lai_xe_name";
	public static final String BUNDLE_LAIXE_PHONE = "lai_xe_phone";
	public static final String BUNDLE_NUMBER_DRIVER = "number_driver";
	public static final String BUNDLE_KHUYEN_MAI = "khuyen_mai";
	public static final String BUNDLE_TIME_BOOKING = "timeBooking";
	public static final String BUNDLE_STATE_CONNECT = "stateConnect";
	public static final String BUNDLE_DOI_TAC_KHUYEN_MAI = "doi_tac_khuyen_mai";
	public static final String BUNDLE_IMAGE_VIEW_POSITION = "bundle_image_view_position";
	public static final String BUNDLE_IMAGE_VIEW = "bundle_image_view";
	public static final String BUNDLE_COMMENT_ADD = "bundle_comment_add";
	public static final String BUNDLE_TINH_ID = "bundle_tinh_id";
	public static final String BUNDLE_TINH_NAME = "BUNDLE_TINH_NAME";
	public static final String BUNDLE_DOI_TAC_ID = "bundle_doitac_id";
	public static final String BUNDLE_DOI_TAC_NAME = "bundle_doitac_name";
	public static final String BUNDLE_KEY_FREE_IS_DEN = "bundle_key_free_is_den";
	public static final String BUNDLE_KEY_DAT_TRUOC = "bundle_key_dat_truoc";
	public static final String BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC = "bundle_key_nhom_cn_doi_tac";
	public static final String BUNDLE_KEY_MA_KHUYEN_MAI = "bundle_ma_khuyen_mai";
	public static final String BUNDLE_KEY_NHOM_KM_ID = "NHOM_KM_ID";
	public static final String BUNDLE_KEY_TEN_DOI_TAC_KM = "TenDoiTacKM";
	public static final String BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM = "TrangThaiDoiTacKM";
	public static final String BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID = "DoiTacKMID";
	public static final String BUNDLE_KEY_TEN_DOI_TAC = "TenDoiTac";
	public static final String BUNDLE_KEY_CHI_NHANH_DOI_TAC_ID_TU_SEARCH_PLACE= "chiNhanhDoiTacIdTuSearchPlace";
	public static final String BUNDLE_KEY_MA_DAT_CHO_ID = "bundle_ma_dat_cho_id";
	public static final String BUNDLE_KEY_MA_DAT_CHO_MA = "bundle_ma_dat_cho_ma";
	public static final String BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA = "bundle_ma_dat_cho_giam_gia";
	// tai tro cua nguoi dung - co di tu do hay khong.
	public static final String BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG = "diem_tai_tro_ngoi_dung";
	public static final String BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO = "isDiTuDo";
	public static final String BUNDLE_KEY_DANH_SACH_DIEM_DEN = "isDanhSachDiemDen";

	public static final String BUNDLE_KEY_NOI_DUNG_KM = "NOI_DUNG_KM";
	public static final String BUNDLE_KEY_TEN = "TEN";
	public static final String BUNDLE_KEY_ID = "ID";
	public static final String BUNDLE_KEY_DIA_CHI = "DIA_CHI";
	public static final String BUNDLE_KEY_LINK_WEBSITE = "LINK_WEBSITE";
	public static final String BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID = "KEY_NHOM_CHI_NHANH_DOI_TAC_ID";
	public static final String BUNDLE_KEY_GHI_CHU = "GHI_CHU";
	public static final String BUNDLE_KEY_MO_TA = "MO_TA";
	public static final String BUNDLE_KEY_VI_DO = "VI_DO";
	public static final String BUNDLE_KEY_KINH_DO = "KINH_DO";
	public static final String BUNDLE_KEY_MA_NHOM_KHUYEN_MAI = "MaNhomKhuyenMai";
	public static final String BUNDLE_KEY_CHI_NHANH_KM = "CHI_NHANH_KM";
	public static final String BUNDLE_KEY_IS_LICH_SU = "IS_LICH_SU";
	public static final String BUNDLE_KEY_LIST_DIA_DIEM_KM = "LIST_DIA_DIEM_KM";
	public static final String BUNDLE_KEY_MAP = "MAP";
	public static final String BUNDLE_KEY_LICH_SU = "LICH_SU";
	public static final String BUNDLE_QUY_DINH_ID= "quyDinhId";
	public static final String BUNDLE_QUY_DINH_NAME = "quyDinhName";
	public static final String BUNDLE_KEY_IS_REGISTER = "KEY_IS_REGISTER";
	public static final String BUNDLE_KEY_GROUP_ID = "BUNDLE_KEY_GROUP_ID";
	public static final String BUNDLE_KEY_GROUP_NAME = "BUNDLE_KEY_GROUP_NAME";

	public static final String BROADCAST_ACTION_LIFECYCLE = "com.onepas.android.pastaxi.BROADCAST_ACTION_LIFECYCLE";
	public  static final String BUNDLE_KEY_LIFECYCLE_RESUME = "BUNDLE_KEY_LIFECYCLE_RESUME";

	public static final String BUNDLE_NATION_NAME_VIEW = "_nation_name_view";
	public static final String BUNDLE_NATION_CODE = "nation_code";
	public static final String BUNDLE_REGISTER_PHONE_IS_EXIST = "register_phone_is_exits";

	public static final String BUNDLE_LANGUAGE_BEFOR = "BUNDLE_LANGUAGE_BEFOR";
	public static final String BUNDLE_IS_SHOW_KHUYEN_MAI = "isShowKhuyenMai";
	public static final String KEY_IS_SHOW_UPDATE_APP = "isShowUpdateApp";
	public static final String BUNDLE_GO_TO_FROM_CHANG_LANGUAGE = "isGoToFromChangLanguage";

	public static final String BUNDLE_KEY_ACTIONBAR_NAME = "keyActionBarName";
	public static final String BUNDLE_KEY_LINK = "keyLink";

	public static final String BUNDLE_KEY_TIN_KM_ID = "BUNDLE_KEY_TIN_KM_ID";
	public static final String BUNDLE_KEY_TIN_KM_TITLE = "BUNDLE_KEY_TIN_KM_TITLE";
	// webview Chi tiết
	public  static final String KEY_TYPE_WEBVIEW="KEY_TYPE_WEBVIEW";
	public  static final String KEY_DOI_TAC_KHUYEN_MAI_ID="KEY_DOI_TAC_KHUYEN_MAI_ID";
	//
	public static final String BUNDLE_KEY_LOAI_TIN_NHAN = "BUNDLE_KEY_LOAI_TIN_NHAN";
	public static final String BUNDLE_KEY_CHAT_URL = "BUNDLE_KEY_CHAT_URL";
	public static final String BUNDLE_KEY_GO_TO_DETAIL = "BUNDLE_KEY_GO_TO_DETAIL";
	// bundle Search
	public static final String BUNDLE_KEY__SEARCH_TAG_ID = "BUNDLE_KEY__SEARCH_TAG_ID";
	public static final String BUNDLE_KEY_SEARCH_QUAN_ID = "BUNDLE_KEY_SEARCH_QUAN_ID";
	public static final String BUNDLE_KEY_SEARCH_TEXT = "BUNDLE_KEY_SEARCH_TEXT";
	//Home
	public  static final String BUNDLE_KEY_TAG_ID="BUNDLE_KEY_TAG_ID";
	public  static final String BUNDLE_KEY_PARENT_ID="BUNDLE_KEY_PARENT_ID";
	public  static final String BUNDLE_KEY_PARENT_NAME="BUNDLE_KEY_PARENT_NAME";
	public  static final String BUNDLE_KEY_PARENT_ANH="BUNDLE_KEY_PARENT_ANH";
	//Filter
	public  static final String BUNDLE_KEY_FILTER_JSON="BUNDLE_KEY_FILTER_JSON";
	public  static final String BUNDLE_KEY_QUAN_ID="BUNDLE_KEY_QUAN_ID";
	public  static final String BUNDLE_KEY_DANH_MUC_ID="BUNDLE_KEY_DANH_MUC_ID";
	public  static final String BUNDLE_KEY_FILTER_NUMBER="BUNDLE_KEY_FILTER_NUMBER";
	public  static final String BUNDLE_KEY_FILTER_VITRI="BUNDLE_KEY_FILTER_VITRI";
	public  static final String BUNDLE_KEY_FILTER_SEARCH="BUNDLE_KEY_FILTER_SEARCH";
	// bundle
	public static final String BUNDLE_KEY_TIN_KHUYEN_MAI_ID = "tinKhuyenMaiId";
	public static final String BUNDLE_KEY_THONG_BAO_NOTIFICATION_ID = "notificationId";
	public static final String BUNDLE_KEY_THONG_BAO_ALERT = "alert";
	public static final String BUNDLE_KEY_GO_TO_PUSH_NOTIFICATION_ACTIVITY = "BUNDLE_KEY_GO_TO_PUSH_NOTIFICATION_ACTIVITY";
	public static final String KEY_SDT_LOGIN = "key_sdt_kichhoat";
	public static final String KEY_PASSWORD = "key_password";
	// image list
	public static final String IMAGE_LIST_OBJECT="IMAGE_LIST_OBJECT";
	public static final String IMAGE_LIST_NUMBER="IMAGE_LIST_NUMBER";

	public static final String KEY_FCM_TOKEN = "KEY_FCM_TOKEN";
	public static final String isKichHoat = "KEY_isKichHoat";
	public static final String isUpdatePassword = "isUpdatePassword";
	public static final String KEY_TOKEN = "KEY_TOKEN";
	public static final String KEY_userId = "KEY_userId";
	public static final String KEY_SDT = "KEY_SDT";
	public static final String KEY_username = "KEY_username";
	public static final String KEY_email = "KEY_email";
	public static final String KEY_NGAY_SINH = "KEY_ngay_sinh";
	public static final String KEY_URL_ANH = "KEY_url_image";
	public static final String KEY_LINH_VUC_QUAN_TAM_TEXT = "keyLinhVucQuanTamText";
	public static final String KEY_LINH_VUC_QUAN_TAM = "keyLinhVucQuanTam";
	public static final String KEY_SEX = "KEY_sex";
	public static final String KEY_MA_ID = "KEY_ma_id";
	public static final String KEY_MA_UUID = "KEY_MA_UUID";
	public static final String KEY_maKichHoat = "KEY_maKichHoat";
	public static final String KEY_ROOM_CHAT = "KEY_ROOM_CHAT";
	public static final String PWNSHARE_PREFERENCES = "MYPREFERENCES";

	public static final String KEY_TEN_TINH_ID = "TINH_ID";
	public static final String KEY_DATA_JSON = "dataJson";
	public static final String KEY_MARKER_ID = "reasonIdShow";
	public static final String KEY_JSONARRAY = "JSONARRAY";

	public static final int MENU_TO_LIST_C1 = 606;
	public static final String KEY_DI_XE_FREE = "DI_XE_FREE";
	public static final int KEY_INT_XE_FREE = 1102;// Complete
	public static final int KEY_INT_COMPLETE = 112;
	public static final String KEY_COMPLETE = "KEY_COMPLETE";
	public static final String LANGUAGE_ENGLISH = "en";
	public static final String LANGUAGE_VIETNAM = "vi";
	public static final String KEY_NATION_CODE_ID = "key_nation_code_id";
	public static int ThePasgoTabNumber=0;

	public static final String mCodeDisableLoaiHinhNhaHang ="loaihinhnhanhang";
	public static final String mCodeSapXepChung = IsDeveloper ?"Sap xep chung":"0001";
	public static final String mCodeGia = IsDeveloper ?"002":"0002";

	public static final String KEY_YEU_THICH = "key_yeu_thich";
}