package com.onepas.android.pasgo.presenter.account.login;

import android.content.Context;

public interface ILoginModel {
    String getPhoneNumber();
    String getPassword();
    String getNationCode();
    int isValidData();
}
