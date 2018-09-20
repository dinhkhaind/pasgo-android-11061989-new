package com.onepas.android.pasgo.presenter.base;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {
    private V mMvpView;

    @Override
    public void onAttach(V mvpView) {
        this.mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        this.mMvpView = null;
    }

    public V getMvpView() {
        return mMvpView;
    }
    protected void registerFCM() {
        DeviceUuidFactory factory = new DeviceUuidFactory(Pasgo.getInstance());
        PastaxiPref pasWayPref = new PastaxiPref(Pasgo.getInstance());
        String regId = pasWayPref.getFcmToken();
        String url = WebServiceUtils.URL_RegisterPushNotification();
        JSONObject jsonParams1 = new JSONObject();
        try {
            jsonParams1.put("deviceId", factory.getDeviceUuid());
            jsonParams1.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams1.put("deviceType", 0);
            jsonParams1.put("registrationId", regId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams1,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // finish();
                        Utils.Log("", "" + response);
                    }

                    @Override
                    public void onError(int maloi) {
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

    }


}
