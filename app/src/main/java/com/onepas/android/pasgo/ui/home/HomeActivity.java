package com.onepas.android.pasgo.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onepas.android.pasgo.BuildConfig;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.PTService;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.RateApp;
import com.onepas.android.pasgo.ui.account.LoginActivity;
import com.onepas.android.pasgo.ui.calldriver.DatXeActivity;
import com.onepas.android.pasgo.ui.reserve.FragmentSearch;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends BaseAppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    private FragmentTabHost mTabHost;
    private Context mContext;
    public DrawerLayout mDrawerLayout;
    public NavigationDrawerFragment mNavigationDrawerFragment;
    public static final int KEY_SERVICE_TAXI = 1;
    public double mLat = 0.0, mLng = 0.0;
    public boolean mIsShowKhuyenMai = false;
    private boolean mIsShowUpdateApp = false;
    private boolean mCallApiKhuyenMai = false;
    private String mNoiDung, mTieuDe;
    private String mContentVesion;
    private int mVersionCode;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    private SimpleDraweeView mImgAvata;
    private TextView mTvNumberMsg;
    private RelativeLayout mRlNumberMsg;
    private int mNumberMsg = 0;
    private boolean mIsRefreshActivity = false;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home_tab);
        mContext = this;
        initializeMenu();
        initView();
        getBundle();
        initControl();
        //
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab_home1, R.drawable.home_tab_one)),
                FragmentHome.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab_home2, R.drawable.home_tab_two)),
                FragmentNearYou.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab_home3, R.drawable.home_tab_three)),
                FragmentSearch.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab4").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab_home4, R.drawable.home_tab_four)),
                FragmentTopUuuDai.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab5").setIndicator(getTabIndicatorChat(mTabHost.getContext(), R.string.tab_home5, R.drawable.home_tab_five)),
                FragmentChat.class, null);


        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().setDividerDrawable(null);
            if (i == 0) {
                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textView); //Unselected Tabs
                tv.setTextColor(Utils.getColor(this, R.color.tab_text_selected));
            }
        }
        //number chat
        IntentFilter intentLoginFilter = new IntentFilter(
                Constants.BROADCAST_ACTION_NUMBER_MESSAGE);
        registerReceiver(broadcastNumberMessage, intentLoginFilter);
        mRlNumberMsg = (RelativeLayout) mTabHost.getTabWidget().getChildAt(4).findViewById(R.id.number_msg_rl);
        mRlNumberMsg.setVisibility(View.GONE);
        mTvNumberMsg = (TextView) mTabHost.getTabWidget().getChildAt(4).findViewById(R.id.number_msg_tv);
        mTvNumberMsg.setText("0");
        //tab changed
        mTabHost.setOnTabChangedListener(tabId -> {
            // TODO Auto-generated method stub
            mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
                    .setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textView); //Unselected Tabs
                tv.setTextColor(Utils.getColor(mContext, R.color.tab_text_unselect));

            }
            TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(R.id.textView); //for Selected Tab
            tv.setTextColor(Utils.getColor(mContext, R.color.tab_text_selected));
            Utils.Log(Pasgo.TAG,"tab index"+mTabHost.getCurrentTab());
            //number chat
            if(mTabHost.getCurrentTab()==4) {
                mNumberMsg =0;
                mRlNumberMsg.setVisibility(View.GONE);
                mRlNumberMsg.setVisibility(View.GONE);
            }

        });


        if (!mCallApiKhuyenMai)
            getNewsletter();
        RateApp.app_launched(mContext);
        // open get location
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), mActivity);
        }
        if(Pasgo.socket == null ||!Pasgo.socket.connected()){
            Pasgo.socket = null;
            Pasgo.getInstance().initSocket();
        }
        // fibase config
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        //mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        //getAppVersions();
        fetchWelcome();
    }
    public static void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            // bật lại thông báo lần 2 sau khi tắt lần đầu
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        } else {
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }
    @Override
    protected void initView() {
        super.initView();
        mImgAvata = (SimpleDraweeView) findViewById(R.id.imgAvata);

    }

    @Override
    protected void initControl() {
        super.initControl();
        if (!StringUtils.isEmpty(Pasgo.getInstance().urlAnh))
            setAvata();
        else
            mImgAvata.setBackgroundResource(R.drawable.no_avatar);
        // kiem tra neu da login va ma null thi get lai thong tin
        if (Pasgo.isLogged() && StringUtils.isEmpty(Pasgo.getInstance().ma))
            getUserInfo();

    }

    private void initializeMenu() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
    }
    private final BroadcastReceiver broadcastNumberMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(mTabHost.getCurrentTab()!=4 && !Pasgo.getInstance().isOnChatActivity) {
                mNumberMsg ++;
                mRlNumberMsg.setVisibility(View.VISIBLE);
            }else{
                mNumberMsg =0;
                mRlNumberMsg.setVisibility(View.GONE);
            }
            mTvNumberMsg.setText(mNumberMsg+"");
        }
    };
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(Pasgo.TAG,"activity"+requestCode);
        Utils.Log(Pasgo.TAG,"activity"+resultCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         if(!mIsRefreshActivity)
            Pasgo.disConnectSocket();
        unregisterReceiver(broadcastNumberMessage);
    }

    private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }
    private View getTabIndicatorChat(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout_chat, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartMoveScreen() {
        //Utils.Log(Pasgo.TAG, "onStartMoveScreen");
    }

    @Override
    public void onUpdateMapAfterUserInterection() {
        //Utils.Log(Pasgo.TAG, "onUpdateMapAfterUserInterection");
        /*Intent broadcast = new Intent();
        broadcast.setAction(Constants.BROADCAST_ACTION_MOVE_MAP);
        sendBroadcast(broadcast);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAccount();
        if (!mIsShowUpdateApp)
            handlePopup.sendEmptyMessage(2);
        getSoLuongLichSu();
        registerFCM();
        startService(new Intent(mActivity, PTService.class));
        if ((Pasgo.getInstance().isUserNotNull && StringUtils.isEmpty(Pasgo
                .getInstance().userId))
                || (!Pasgo.getInstance().isUserNotNull && !StringUtils
                .isEmpty(Pasgo.getInstance().userId))) {
            Pasgo.getInstance().isUserNotNull = !Pasgo.getInstance().isUserNotNull;
            mIsRefreshActivity = true;
            Intent intent = getIntent();
            finishOurLeftInLeft();
            startActivity(intent);
        }
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mIsShowKhuyenMai = bundle.getBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI);
        }
    }

    private void setAvata() {
        mImgAvata.setImageURI(Pasgo.getInstance().urlAnh + "&t="
                + System.currentTimeMillis());
    }

    private void setAccount() {
        LinearLayout lnMenuHistoryAll;
        LinearLayout lnTrial, lnAccount;
        lnMenuHistoryAll = (LinearLayout) findViewById(R.id.history_menu_all_ln);
        lnTrial = (LinearLayout) findViewById(R.id.lnTrial);
        lnAccount = (LinearLayout) findViewById(R.id.lnAccount);

        LinearLayout lnMaId = (LinearLayout) findViewById(R.id.lnMaId);
        TextView userName = (TextView) findViewById(R.id.userName);
        TextView maId = (TextView) findViewById(R.id.maId);

        findViewById(R.id.login_btn).setOnClickListener(v -> {
            gotoActivity(mContext, LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finishOurLeftInLeft();
        });
        findViewById(R.id.register_btn).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.BUNDLE_TRIAL_REGISTER, true);
            gotoActivity(mContext, LoginActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finishOurLeftInLeft();
        });

        if (!Pasgo.isLogged()) {
            lnMenuHistoryAll.setVisibility(View.GONE);
            lnTrial.setVisibility(View.VISIBLE);
            mNavigationDrawerFragment.menuThoat.setVisibility(View.GONE);
            lnAccount.setVisibility(View.GONE);
            lnMaId.setVisibility(View.GONE);
            userName.setText(R.string.dang_ki_tai_khoan);
        } else {
            lnMenuHistoryAll.setVisibility(View.VISIBLE);
            lnTrial.setVisibility(View.GONE);
            mNavigationDrawerFragment.menuThoat.setVisibility(View.VISIBLE);
            lnAccount.setVisibility(View.VISIBLE);
            lnMaId.setVisibility(View.VISIBLE);
            if (StringUtils.isEmpty(Pasgo.getInstance().sdt))
                userName.setText(getString(R.string.anonymous));
            else
                userName.setText(" " + Pasgo.getInstance().sdt);
            if (!StringUtils.isEmpty(Pasgo.getInstance().urlAnh) && Pasgo.getInstance().isUpdateImage) {
                setAvata();
                Pasgo.getInstance().isUpdateImage = false;
            }
            maId.setText(" " + Pasgo.getInstance().ma);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, mIsShowKhuyenMai);
        outState.putBoolean(Constants.KEY_IS_SHOW_UPDATE_APP, mIsShowUpdateApp);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIsShowKhuyenMai = savedInstanceState.getBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI);
        mIsShowUpdateApp = savedInstanceState.getBoolean(Constants.KEY_IS_SHOW_UPDATE_APP);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // stop service
                stopService(new Intent(mActivity, PTService.class));
                // start service
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startService(new Intent(mActivity, PTService.class));
                    }
                },2000);

            } else {
                // Permission was denied. Display an error message.
                //ToastUtils.showToast(mContext,"Permission was denied");
            }
        }
    }
    Handler handlePopup = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!StringUtils.isEmpty(mNoiDung)) {
                        int KEY_MINUTES_LOAD_THONGBAO = 9000;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinishing() || getBaseContext() == null)
                                    return;
                                showDialogKhuyenMai(mTieuDe, mNoiDung);
                            }
                        }, KEY_MINUTES_LOAD_THONGBAO);
                    }
                    break;
                case 2:
                    int versionCode = 0;
                    try {
                        PackageInfo pInfo = getPackageManager().getPackageInfo(
                                getPackageName(), PackageManager.GET_META_DATA);
                        versionCode = pInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e1) {
                        Log.e("", "Name not found", e1);
                    }
                    if (!StringUtils.isEmpty(mContentVesion) && versionCode < mVersionCode) {
                        int KEY_MINUTES_SHOW_UPDATE_APP = 5000;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinishing() || getBaseContext() == null)
                                    return;
                                showDialogUpdateApp(mContentVesion);
                            }
                        }, KEY_MINUTES_SHOW_UPDATE_APP);
                    }
                    break;

            }
        }
    };
    public void goToDatXe(int loaiHinhId) {
        try {
            if (Pasgo.getInstance().prefs.getLatLocationRecent() != null
                    && !"".equals(Pasgo.getInstance().prefs
                    .getLatLocationRecent())) {
                mLat = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLatLocationRecent());
                mLng = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLngLocationRecent());
            }
            Bundle bundle = new Bundle();
            bundle.putDouble(Constants.BUNDLE_LAT, mLat);
            bundle.putDouble(Constants.BUNDLE_LNG, mLng);
            bundle.putInt(Constants.BUNDLE_LOAI_HINH_DV_ID, loaiHinhId);
            if (PTService.mLocationMsgDriverMap != null) {
                bundle.putInt(Constants.BUNDLE_NUMBER_DRIVER,
                        PTService.mLocationMsgDriverMap.size());
            }
            gotoActivityClearTop(mContext, DatXeActivity.class, bundle);
            ourLeftInLeft();
        } catch (Exception e) {
        }
    }
    private void showDialogUpdateApp(String noiDung) {
        final Dialog dialog;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
        TextView tvThongBaoPopup = (TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        Button btnDongY, btnHuy;
        btnDongY = (Button) dialog.findViewById(R.id.btnDongY);
        btnHuy = (Button) dialog.findViewById(R.id.btnHuy);
        btnDongY.setOnClickListener(view -> {
            final String appPackageName = getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            dialog.cancel();
        });
        btnDongY.setText(getString(R.string.update));
        btnHuy.setText(getString(R.string.btn_cancel));
        btnHuy.setOnClickListener(view -> dialog.cancel());
        Utils.setTextViewHtml(tvThongBaoPopup,noiDung);
        if (!mIsShowUpdateApp)
            dialog.show();
        mIsShowUpdateApp = true;
    }
    private void showDialogKhuyenMai(String tieuDe, String noiDung) {
        if (isFinishing()) return;
        final Dialog mDialog;
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.layout_popup_start_app);
        mDialog.setCancelable(true);
        Button btnOk;
        TextView txTieuDe, tvThongBao;
        btnOk = (Button) mDialog.findViewById(R.id.btnOk);
        txTieuDe = (TextView) mDialog.findViewById(R.id.tvTieuDe);
        tvThongBao = (TextView) mDialog.findViewById(R.id.tvThongBao);
        Utils.setTextViewHtml(txTieuDe,tieuDe.trim());
        Utils.setTextViewHtml(tvThongBao,noiDung.trim());
        btnOk.setOnClickListener(v -> mDialog.dismiss());
        if (!mIsShowKhuyenMai)
            mDialog.show();
        mIsShowKhuyenMai = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !isFinishing()) {
            DialogUtils.showYesNoDialog(mActivity, R.string.tb_thoat_app,
                    R.string.dong_y, R.string.huy, exitsListener);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    View.OnClickListener exitsListener = view -> {
        finish();
    };
    public void menuThoat()
    {
        DialogUtils.showYesNoDialog(mActivity, R.string.tb_change_account,R.string.dong_y, R.string.huy, changAccountListener);
    }
    View.OnClickListener changAccountListener = view -> {
        finish();
        changAccount();
    };
    private void changAccount() {
        Pasgo.xoaTaiKhoan();
        gotoActivity(mContext, LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishPushDownInPushDownOut();
    }
    private void getNewsletter() {
        if (Pasgo.getInstance().prefs == null
                || Pasgo.getInstance().prefs.getLatLocationRecent() == null
                || Pasgo.getInstance().prefs.getLngLocationRecent() == null) {
            return;
        }
        mCallApiKhuyenMai = true;
        String url = WebServiceUtils.URL_GET_NEWS_LETTER(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();

        try {
            DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
            double viDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            double kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
            jsonParams.put("viDo", viDo);
            jsonParams.put("kinhDo", kinhDo);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("deviceId", factory.getDeviceUuid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject objItem = ParserUtils.getJsonObject(response, "Item");
                            int hienThi = ParserUtils.getIntValue(objItem, "HienThi");
                            mTieuDe = ParserUtils.getStringValue(objItem, "TieuDe");
                            mNoiDung = ParserUtils.getStringValue(objItem, "NoiDung");
                            if (hienThi == 0)
                                mIsShowKhuyenMai = true;
                            handlePopup.sendEmptyMessage(1);
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
    private void getAppVersions() {
        //mCallApiKhuyenMai =true;
        String url = WebServiceUtils.URL_GET_APP_VESION(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject objItem = ParserUtils.getJsonObject(response, "Item");
                            JSONObject objAndroid = ParserUtils.getJsonObject(objItem, "PasgoClientAndroid");
                            mContentVesion = ParserUtils.getStringValue(objAndroid, "Content");
                            mVersionCode = ParserUtils.getIntValue(objAndroid, "VersionCode");
                            handlePopup.sendEmptyMessage(2);
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
    private void fetchWelcome() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long cacheExpiration = 3600;
                if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                    cacheExpiration = 0;
                }
                mFirebaseRemoteConfig.fetch(cacheExpiration)
                        .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mFirebaseRemoteConfig.activateFetched();
                                    displayWelcomeMessage();
                                }
                            }
                        });
            }
        });

    }
    private void displayWelcomeMessage() {
        mVersionCode = (int)mFirebaseRemoteConfig.getLong("version_code");
        mContentVesion = mFirebaseRemoteConfig.getString("content");
        handlePopup.sendEmptyMessage(2);
    }
    private void getUserInfo() {

        String url = WebServiceUtils.URL_GET_NGUOIDUNG_BY_ID(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            jsonParams.put("id",
                    Pasgo.getInstance().prefs.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.Log("response ", "response  userInfo"
                                    + response);
                            JSONObject objItem = ParserUtils.getJsonObject(response, "Item");
                            String idCode = ParserUtils.getStringValue(objItem, "IdCode");
                            Pasgo.getInstance().ma = idCode;
                            Pasgo.getInstance().prefs.putMa(idCode);
                            setAccount();
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
    // lay so luong check in va dat truoc
    private void getSoLuongLichSu() {
        String url = WebServiceUtils.URL_SO_LUONG_LICH_SU(Pasgo.getInstance().token);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("nguoiDungId", Pasgo.getInstance().userId);
        JSONObject jsonParams = new JSONObject(params);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObj = ParserUtils.getJsonObject(
                                response, "Item");
                        Pasgo.getInstance().datTruocNhan = ParserUtils.getIntValue(jsonObj, "DatTruocNhan");
                        Pasgo.getInstance().checkInDangCho = ParserUtils.getIntValue(jsonObj, "CheckInDangCho");
                        Pasgo.getInstance().datTruocChuaNhan = ParserUtils.getIntValue(jsonObj, "DatTruocChuaNhan");
                        TextView tvSLDatTruoc = (TextView) findViewById(R.id.so_luong_dat_truoc_tv);
                        tvSLDatTruoc.setText(Pasgo.getInstance().datTruocNhan + "/" + Pasgo.getInstance().datTruocChuaNhan);
                        TextView tvSLCheckIn = (TextView) findViewById(R.id.number_of_reserved_tv);
                        tvSLCheckIn.setText(Pasgo.getInstance().checkInDangCho + "");
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
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {

            default:
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}