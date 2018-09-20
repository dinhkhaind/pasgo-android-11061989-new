package com.onepas.android.pasgo.presenter.account.login;

import com.onepas.android.pasgo.presenter.base.MvpView;

public interface LoginMvpView extends MvpView {
    void loginSuccess();
    void loginError();
    void onTryNow();
    void onRegister();
    void showDialog();
    void hiddenDialog();
    void onForgotPassword();
    void showDialogConfirmPhoneNumber();
    void sendActivationCodeSuccess();
}
