package com.onepas.android.pasgo.ui.calldriver;

public enum EnumDatXeUpdate {
    DAT_XE(1),
    KHACH_HANG_HUY_XE(2),
    KHACH_HANG_TIM_KHONG_DUOC_LAI_XE(3),
    KHACH_HANG_THU_TIM_LAI_LAI_XE(4),
    KHACH_HANG_GOI_HANG_XE(5),
//    LAI_XE_CHAP_NHAN_YEU_CAU_DAT_XE_CUA_KHACH_HANG(6),
//    LAI_XE_DA_DEN_NOI_DON(7),
    KHACH_HANG_DA_GAP_LAI_XE(8),
//    LAI_XE_DA_DE_KHACH_HANG_LEN_XE(9),
    LAI_XE_DA_DE_KHACH_HANG_XUONG_XE_HOAN_THANH_DAT_XE(10),
    KHACH_HANG_HUY_TREN_DUONG_LAI_XE_DEN_DON_KHACH(11),
    LAI_XE_HUY_TREN_DUONG_DON_KHACH_HANG(12);

    private final int value;

    private EnumDatXeUpdate(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}