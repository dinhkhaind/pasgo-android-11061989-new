package com.onepas.android.pasgo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.onepas.android.pasgo.ui.announcements.AnnouncementsActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;


public class MyFcmListenerService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage message){
        Utils.Log(Pasgo.TAG,"onMessageReceived");
        if (message.getData().size() > 0) {
            Utils.Log(Pasgo.TAG, "Message data payload: " + message.getData());
        }
        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Utils.Log(Pasgo.TAG, "Message Notification Body: " + message.getNotification().getBody());
        }
        String from = message.getFrom();
        Map data = message.getData();
        if(data!=null){
            String typeNotification = (String) data.get(Constants.KEY_TYPE_PUSH_NOTIFICATION);
            String alert = (String) data.get(Constants.BUNDLE_KEY_THONG_BAO_ALERT);
            String notificationId = (String) data.get(Constants.BUNDLE_KEY_THONG_BAO_NOTIFICATION_ID);
            String tinKhuyenMaiId = (String) data.get(Constants.BUNDLE_KEY_TIN_KHUYEN_MAI_ID);

            DeviceUuidFactory factory = new DeviceUuidFactory(this);
            String deviceId = factory.getDeviceUuid();
            int type=0;
            if(!StringUtils.isEmpty(typeNotification))
                type = Integer.parseInt(typeNotification);
            Log.i(Pasgo.TAG, "alert: " + alert);
            Log.i(Pasgo.TAG, "notificationId: " + notificationId);
            Log.i(Pasgo.TAG, "typeNotification: " + typeNotification);
            Log.i(Pasgo.TAG, "tinKhuyenMaiId: " + tinKhuyenMaiId);
            updateReceivedNotification(deviceId,tinKhuyenMaiId);

            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent notificationIntent = null;
            switch (type)
            {
                case Constants.KEY_PUSH_NOTIFICATION_TIN_HTML:
                    notificationIntent = new Intent(this, AnnouncementsActivity.class);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_TIN_KHUYEN_MAI_ID, tinKhuyenMaiId);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_THONG_BAO_NOTIFICATION_ID, notificationId);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_GO_TO_PUSH_NOTIFICATION_ACTIVITY, true);
                    notificationIntent.putExtra(Constants.KEY_TYPE_PUSH_NOTIFICATION, type);

                    break;
                case Constants.KEY_PUSH_NOTIFICATION_TIN_DOI_TAC:
                    notificationIntent = new Intent(this, AnnouncementsActivity.class);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_TIN_KHUYEN_MAI_ID, tinKhuyenMaiId);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_THONG_BAO_NOTIFICATION_ID, notificationId);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_GO_TO_PUSH_NOTIFICATION_ACTIVITY, true);
                    notificationIntent.putExtra(Constants.KEY_TYPE_PUSH_NOTIFICATION, type);
                    break;
                case Constants.KEY_PUSH_NOTIFICATION_TIN_NHOM_DOI_TAC:
                    notificationIntent = new Intent(this, AnnouncementsActivity.class);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_TIN_KHUYEN_MAI_ID, tinKhuyenMaiId);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_THONG_BAO_NOTIFICATION_ID, notificationId);
                    notificationIntent.putExtra(Constants.BUNDLE_KEY_GO_TO_PUSH_NOTIFICATION_ACTIVITY, true);
                    notificationIntent.putExtra(Constants.KEY_TYPE_PUSH_NOTIFICATION, type);
                    break;
                case Constants.KEY_PUSH_NOTIFICATION_THE_PASGO_DATXE:
                    notificationIntent = new Intent(this, ThePasgoActivity.class);
                    notificationIntent.putExtra(Constants.KEY_TYPE_PUSH_NOTIFICATION, Constants.KEY_ACTIVITY_THE_PASGO);
                    Constants.ThePasgoTabNumber =1;
                    break;
                case Constants.KEY_PUSH_NOTIFICATION_THE_PASGO_DATCHO:
                    notificationIntent = new Intent(this, ThePasgoActivity.class);
                    notificationIntent.putExtra(Constants.KEY_TYPE_PUSH_NOTIFICATION, Constants.KEY_ACTIVITY_THE_PASGO);
                    Constants.ThePasgoTabNumber =0;
                    break;
                default:
                    notificationIntent = new Intent(this, HomeActivity.class);
                    notificationIntent.putExtra(Constants.KEY_TYPE_PUSH_NOTIFICATION, Constants.KEY_PUSH_NOTIFICATION_TIN_HTML);
                    break;
            }
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int dummyuniqueInt = new Random().nextInt(543254);
            PendingIntent contentIntent = PendingIntent.getActivity(this, dummyuniqueInt, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.drawable.app_pastaxi)
                    .setContentTitle(getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(alert))
                    .setContentText(alert).setLights(Color.GREEN, 300, 300)
                    .setVibrate(new long[] { 100, 250 })
                    .setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.drawable.app_pastaxi_transparent);
                mBuilder.setColor(Utils.getColor(this, R.color.background_notification));
            } else {
                mBuilder.setSmallIcon(R.drawable.app_pastaxi);
            }
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(new Random().nextInt(), mBuilder.build());
        }
    }

    private void updateReceivedNotification(String deviceId, String tinKhuyenMaiId) {

        String url = WebServiceUtils.URL_UPDATE_RECEIVED_NOTIFICATION();
        JSONObject jsonParams1 = new JSONObject();
        try {
            jsonParams1.put("deviceId", deviceId);
            jsonParams1.put("tinKhuyenMaiId", tinKhuyenMaiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams1,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.Log("", "response" + response);
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