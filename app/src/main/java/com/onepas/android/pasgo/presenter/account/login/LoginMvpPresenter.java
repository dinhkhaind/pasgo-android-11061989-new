package com.onepas.android.pasgo.presenter.account.login;

import com.onepas.android.pasgo.presenter.base.MvpPresenter;

public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {
    void onServerLoginClick(String maQuocGia, String sdt, String matKhau);
    void onTryNow();
    void onRegister();
    void onForgetPassword();
    void verifySdtKichHoat(String maQuocGia, String sdt);
    void sendMaKichHoat(String maQuocGia);
}
