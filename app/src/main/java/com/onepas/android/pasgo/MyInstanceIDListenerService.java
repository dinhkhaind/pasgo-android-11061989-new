package com.onepas.android.pasgo;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.utils.Utils;


public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Utils.Log(Pasgo.TAG, "fcm token: " + fcmToken);
        PastaxiPref pasWayPref = new PastaxiPref(this);
        pasWayPref.putFcmToken(fcmToken);
    }
}
