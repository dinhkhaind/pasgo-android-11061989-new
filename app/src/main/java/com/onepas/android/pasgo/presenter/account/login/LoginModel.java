package com.onepas.android.pasgo.presenter.account.login;

import android.content.Context;

import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.utils.StringUtils;

public class LoginModel implements ILoginModel {
    String phoneNumber;
    String password;
    String nationCode;
    Context context;

    public LoginModel(String phoneNumber, String password, String nationCode) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nationCode = nationCode;
        this.context = Pasgo.getInstance();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getNationCode() {
        return nationCode;
    }

    @Override
    public int isValidData() {
        if(StringUtils.isEmpty(nationCode))
        {
            return 1;
        }
        if(StringUtils.isEmpty(phoneNumber))
        {
            return 2;
        }
        if(phoneNumber.length()<9)
        {
            return 3;
        }
        if(StringUtils.isEmpty(password))
        {
            return 4;
        }
        return -1;
    }

}
