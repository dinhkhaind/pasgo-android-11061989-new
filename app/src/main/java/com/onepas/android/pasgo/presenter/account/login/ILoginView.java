package com.onepas.android.pasgo.presenter.account.login;

import com.onepas.android.pasgo.presenter.base.MvpView;

public interface ILoginView extends MvpView {
    void loginSuccess(String msg);
    void loginError(String msg);
    void loginWarning(String msg);
    void onTryNow();
    void onRegister();
    void showDialog();
    void hiddenDialog();
    void onForgotPassword();
    void showDialogConfirmPhoneNumber();
    void sendActivationCodeSuccess();
}
