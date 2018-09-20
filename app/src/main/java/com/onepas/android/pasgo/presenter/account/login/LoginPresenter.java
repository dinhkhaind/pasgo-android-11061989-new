package com.onepas.android.pasgo.presenter.account.login;

import android.content.Context;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.presenter.base.BasePresenter;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {
    private Context context;
    public LoginPresenter() {
        this.context = Pasgo.getInstance();
    }

    @Override
    public void onServerLoginClick(String maQuocGia, String sdt, String matKhau) {
        if(StringUtils.isEmpty(maQuocGia))
        {
            ToastUtils.showToast(context, R.string.plz_input_language);
            return;
        }
        if(StringUtils.isEmpty(sdt))
        {
            ToastUtils.showToast(context,R.string.plz_input_phone_number);
            return;
        }
        if(sdt.length()<9)
        {
            ToastUtils.showToast(context,R.string.plz_input_phone_number_format);
            return;
        }
        if(StringUtils.isEmpty(matKhau))
        {
            ToastUtils.showToast(context, R.string.plz_input_password);
            return;
        }
        String url = WebServiceUtils
                .URL_LOGIN(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("maQuocGia", maQuocGia);
            jsonParams.put("sdt", sdt);
            jsonParams.put("matKhau", matKhau);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(context).isNetworkAvailable()) {
            getMvpView().showDialog();
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.Log("response ", "response"
                                    + response);
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject("Item");
                                String NguoiDungId = ParserUtils.getStringValue(jsonObject, "NguoiDungId");
                                String tokenApi = jsonObject.getString("TokenApi");
                                String sdt = ParserUtils.getStringValue(jsonObject, "Sdt");
                                String maNguoiDung = ParserUtils.getStringValue(jsonObject, "MaNguoiDung");
                                String tenNguoiDung = ParserUtils.getStringValue(jsonObject, "TenNguoiDung");
                                String urlAnh = ParserUtils.getStringValue(jsonObject, "UrlAnh");
                                Pasgo.getInstance().userId = NguoiDungId;
                                Pasgo.getInstance().username = tenNguoiDung;
                                Pasgo.getInstance().sdt = sdt;
                                Pasgo.getInstance().ma = maNguoiDung;
                                Pasgo.getInstance().token = tokenApi;
                                Pasgo.getInstance().urlAnh = urlAnh;
                                registerFCM();
                                // save pref
                                PastaxiPref pasWayPref = new PastaxiPref(Pasgo.getInstance());
                                pasWayPref.putUserId(Pasgo.getInstance().userId);
                                pasWayPref.putUserName(Pasgo.getInstance().username);
                                pasWayPref.putSdt(Pasgo.getInstance().sdt);
                                pasWayPref.putMa(Pasgo.getInstance().ma);
                                pasWayPref.putToken(Pasgo.getInstance().token);
                                pasWayPref.putUrlAnh(Pasgo.getInstance().urlAnh);
                                pasWayPref.putIsUpdatePassword(true);
                                pasWayPref.putIsUpdatePasswordActivity(false);
                                Pasgo.getInstance().isUpdatePassWord = true;
                                getMvpView().loginSuccess();
                                getMvpView().hiddenDialog();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int maloi) {
                            getMvpView().loginError();
                            getMvpView().hiddenDialog();
                        }

                    }, new Pasgo.PWErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getMvpView().loginError();
                            getMvpView().hiddenDialog();
                        }
                    });
        }else
        {
            getMvpView().loginError();
            ToastUtils.showToast(context,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

    @Override
    public void verifySdtKichHoat(String maQuocGia, String sdt) {
        String url = WebServiceUtils.URL_VERIFY_SDT_KICHHOAT();
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(context).isNetworkAvailable()) {
            getMvpView().showDialog();
            try {
                jsonParams.put("maQuocGia", maQuocGia);
                jsonParams.put("sdt", sdt);
                jsonParams.put("isRegisted", true);
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    getMvpView().hiddenDialog();
                                    JSONObject jsonObject = null;
                                    try {
                                        getMvpView().hiddenDialog();
                                        jsonObject = json
                                                .getJSONObject("Item");
                                        String sdt = ParserUtils.getStringValue(jsonObject, "Sdt");
                                        Pasgo.getInstance().sdt = sdt;
                                        PastaxiPref pasWayPref = new PastaxiPref(context);
                                        pasWayPref.putSdt(Pasgo.getInstance().sdt);
                                        getMvpView().showDialogConfirmPhoneNumber();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                getMvpView().hiddenDialog();
                            }

                        }, new Pasgo.PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                getMvpView().hiddenDialog();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                getMvpView().hiddenDialog();
                ToastUtils.showToast(context,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }else
        {
            getMvpView().hiddenDialog();
            ToastUtils.showToast(context,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

    @Override
    public void sendMaKichHoat(String maQuocGia) {
        String url = WebServiceUtils.URL_SEND_MA_KICH_HOAT();
        DeviceUuidFactory factory = new DeviceUuidFactory(context);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(context).isNetworkAvailable()) {
            getMvpView().showDialog();
            try {
                jsonParams.put("maQuocGia", maQuocGia);
                jsonParams.put("sdt", Pasgo.getInstance().sdt);
                jsonParams.put("imei", factory.getDeviceUuid());
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    getMvpView().hiddenDialog();
                                    getMvpView().sendActivationCodeSuccess();
                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                getMvpView().hiddenDialog();
                            }

                        }, new Pasgo.PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                getMvpView().hiddenDialog();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                getMvpView().hiddenDialog();
                ToastUtils.showToast(context,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }else
        {
            getMvpView().hiddenDialog();
            ToastUtils.showToast(context,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

    @Override
    public void onTryNow() {
        getMvpView().onTryNow();
    }

    @Override
    public void onRegister() {
        getMvpView().onRegister();
    }

    @Override
    public void onForgetPassword() {
        getMvpView().onForgotPassword();
    }

}
