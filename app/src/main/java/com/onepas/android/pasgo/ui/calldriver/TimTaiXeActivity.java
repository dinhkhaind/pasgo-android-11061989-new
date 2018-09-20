/*
package com.onepas.android.pasgo.ui.calldriver;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.PTService;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ArgeeMessage;
import com.onepas.android.pasgo.models.DriverRequestObject;
import com.onepas.android.pasgo.models.HangXe;
import com.onepas.android.pasgo.models.LocationMessageClient;
import com.onepas.android.pasgo.models.LocationMessageDriver;
import com.onepas.android.pasgo.models.LyDoHuy;
import com.onepas.android.pasgo.models.PTLocationInfo;
import com.onepas.android.pasgo.models.RequestHangXeOrderObject;
import com.onepas.android.pasgo.models.RequestOrderObject;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.util.mapnavigator.MapUtil;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;
import com.onepas.android.pasgo.widgets.AnimatedGifImageView;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimTaiXeActivity extends BaseAppCompatActivity implements
        OnClickListener {
    private static final String TAG = "TimTaiXeActivity";
    private Button mBtnComplete;
    private TextView mTvKm, mTvMoney, mTvDiaDiemHienTai, mTvDiaDiemDen,
            mTvHaveDriver, mTvSecondNumber;
    private LinearLayout mLnTimKiemXe;
    private RelativeLayout mRlTimKiemXeThatBai;
    private boolean isTimKiem = true;
    private double myProgress;
    private int mTotalS;

    private HashMap<String, LocationMessageDriver> mDriverIdsAccept;
    private ArrayList<LocationMessageDriver> mLocationMessageDrivers;
    private ArrayList<LocationMessageDriver> mLocationMessageDriversSorted;
    private ArrayList<String> mDriverIdsServer;
    private ArrayList<String> mDriverIdsNotAccept;
    private LocationMessageClient mMessageToDriver;
    private LocationMessageDriver mDrivingInvited;
    private boolean isFullTime;
    private boolean isAccepted;
    private double mEndLat = 0, mEndLng = 0, mStartLat = 0, mStartLng = 0, mKm,
            mPhanTramGiamGia, mKhachHangLat, mKhachhangLng;
    private String mGia;
    private String mStartAddress, mEndAddress, mDatXeId, mLoaiXeName, mMota,
            mThoiGianDonXe, mKhuyenMai;
    private long mTimeBooking;
    private int mDichVuId = 0, mDriverNumber;
    private int mLoaiHinhDichVuId =0;
    private int mNhomXeDichVuId =0;
    private Dialog mDialogHuy;
    private String mDriverIdSorted;
    private String mLyDo;
    private LinearLayout mLnThuLai;
    private ArrayList<LyDoHuy> mLyDoHuys;

    private ImageButton mBtnCall;
    private Boolean mIsGoToMap;
    private int mDiXeFree;

    private RequestOrderObject mRequestOrderObject;
    private RequestHangXeOrderObject mRequestHangXeOrderObject;
    private String mNhomCNDoiTacId = "";
    private TextView mTvThongBaoPhucVu;
    private boolean mCoTaiXeHoacHangPhucVu = false;
    private String hangXeName = "";
    private boolean mIsCallHangXe = false;
    private boolean mIsCallAgain = false;
    private Dialog mDialogThuLai;
    private Dialog mDialogHangXe;
    private boolean mIsCallDriver;
    private AnimatedGifImageView animatedGifImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_tim_tai_xe);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.thong_tin_dat_xe));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(null);
        mToolbar.setNavigationOnClickListener(v -> showDialogHuy());
        this.getBundle();
        this.initView();
        this.initControl();
        this.onNetworkChanged();
        this.toottip();
        animatedGifImageView = ((AnimatedGifImageView)findViewById(R.id.animatedGifImageView));
        animatedGifImageView.setAnimatedGif(R.drawable.run_progress,
                AnimatedGifImageView.TYPE.STREACH_TO_FIT);
    }

    private void toottip() {
        mRlToolTipBottomLeft = (RelativeLayout) findViewById(R.id.rlToolTipBottomLeft);
        TvBottomLeft = (TextView) findViewById(R.id.tvBottomLeft);
        TvBottomLeft.setText(R.string.tb_tooltip_hoanthanh);
        if (mToolTipPref != null) {
            if (mToolTipPref.getTimTaiXeThuLai())
                mRlToolTipBottomLeft.setVisibility(View.GONE);
            else
                mRlToolTipBottomLeft.setVisibility(View.GONE);
        }
    }

    private void loadCallerAll(String driverSorted,
                               HashMap<String, LocationMessageDriver> locationMsgDriverMap,
                               boolean isShow) {
        LoadCaller loadCaller = new LoadCaller(driverSorted,
                locationMsgDriverMap, isShow);
        loadCaller.execute();
    }

    class LoadCaller extends
            AsyncTask<Void, Void, ArrayList<LocationMessageDriver>> {
        String driverSorted;
        HashMap<String, LocationMessageDriver> locationMsgDriverMap;
        boolean isShow;

        public LoadCaller(String driverSorted,
                          HashMap<String, LocationMessageDriver> locationMsgDriverMap,
                          boolean isShow) {
            this.driverSorted = driverSorted;
            this.locationMsgDriverMap = locationMsgDriverMap;
            this.isShow = isShow;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isShow)
                showProgressDialogAll();
        }

        @Override
        protected ArrayList<LocationMessageDriver> doInBackground(
                Void... params) {
            if (Pasgo.getInstance().mPubnub == null) {
                Pasgo.getInstance().mPubnub = new Pubnub(
                        Constants.SUBSCRIBE_KEY_PUBNUB,
                        Constants.PUBLISH_KEY_PUBNUB,
                        Constants.SECRET_KEY_PUBNUB, false);
                Pasgo.getInstance().mPubnub.setResumeOnReconnect(true);
                Pasgo.getInstance().mPubnub
                        .setMaxRetries(Constants.MAX_RETRIES_PUBNUB);
            } else if (isShow) {
                Pasgo.getInstance().mPubnub.disconnectAndResubscribe();
                Pasgo.getInstance().mPubnub
                        .setMaxRetries(Constants.MAX_RETRIES_PUBNUB);
            }
            if (driverSorted == null || "".equals(driverSorted))
                return null;
            ArrayList<LocationMessageDriver> locationMessageDrivers = null;
            try {
                locationMessageDrivers = new ArrayList<LocationMessageDriver>();
                locationMessageDrivers = ParserUtils.getDriverIds(driverSorted,
                        locationMsgDriverMap);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return locationMessageDrivers;
        }

        @Override
        protected void onPostExecute(ArrayList<LocationMessageDriver> result) {
            super.onPostExecute(result);
            mLocationMessageDriversSorted = result;
            // co drive phuc vu
            if (mLocationMessageDriversSorted.size() > 0)
                mCoTaiXeHoacHangPhucVu = true;
            LoadFiveDriverNearest(mLocationMessageDriversSorted, isShow);
        }
    }

    private void LoadFiveDriverNearest(
            ArrayList<LocationMessageDriver> locationMessageDriver,
            boolean isShow) {

        FiveDriverNearestTask oneDriverNearestTask = new FiveDriverNearestTask(
                locationMessageDriver, isShow);
        oneDriverNearestTask.execute();
    }

    class FiveDriverNearestTask extends AsyncTask<Void, Void, ArrayList<String>> {
        ArrayList<LocationMessageDriver> locationMessageDrivers;
        boolean isShow;

        public FiveDriverNearestTask(
                ArrayList<LocationMessageDriver> locationMessageDrivers,
                boolean isShow) {
            this.locationMessageDrivers = locationMessageDrivers;
            this.isShow = isShow;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isShow)
                showProgressDialogAll();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            if (locationMessageDrivers != null
                    && locationMessageDrivers.size() > 0
                    && mMessageToDriver != null) {
                if (mDriverIdsServer != null && mDriverIdsServer.size() == 1) {
                    mDriverIdsNotAccept.add(mDriverIdsServer.get(0));
                }
                ArrayList<String> fiveDriversId = new ArrayList<String>();
                for (int i = 0; i < locationMessageDrivers.size(); i++) {
                    LocationMessageDriver driver = locationMessageDrivers
                            .get(i);
                    if (driver != null
                            && driver.getId() != null
                            && mDriverIdsNotAccept != null
                            && !mDriverIdsNotAccept
                            .contains(driver.getId()))

                        fiveDriversId.add(driver.getId());
                }

                return fiveDriversId;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            if (result == null || result.size() == 0) {
                if (isShow) {
                    closeProgressDialogAll();
                }
                mMessageToDriver.setDriverNearId(null);
                callTongDai(mMessageToDriver, true);
                return;
            }
            if (result != null && mMessageToDriver != null) {
                mDriverIdsServer = result;
                mMessageToDriver.setDriverNearId(mDriverIdsServer);
            }
            if (isShow && Pasgo.getInstance().prefs != null) {
                subChannel(Constants.APP_PUBNUB + Constants.SPEC_PUBNUB
                        + Pasgo.getInstance().prefs.getUserId() + mDatXeId);
                handleUpdateUI.sendEmptyMessage(4);
            }
        }
    }

    private void getBundle() {
        Bundle savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            mDiXeFree = savedInstanceState.getInt(Constants.KEY_DI_XE_FREE);
            mKhachHangLat = savedInstanceState
                    .getDouble(Constants.BUNDLE_KHACHHANG_LAT);
            mKhachhangLng = savedInstanceState
                    .getDouble(Constants.BUNDLE_KHACHHANG_LNG);
            mDatXeId = savedInstanceState.getString(Constants.BUNDLE_DAT_XE_ID);
            mDichVuId = savedInstanceState.getInt(Constants.BUNDLE_DICH_VU_ID);
            mLoaiHinhDichVuId = savedInstanceState
                    .getInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID);
            mNhomXeDichVuId = savedInstanceState
                    .getInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID);
            mStartLat = savedInstanceState
                    .getDouble(Constants.BUNDLE_START_LAT);
            mStartLng = savedInstanceState
                    .getDouble(Constants.BUNDLE_START_LNG);
            mKm = savedInstanceState.getDouble(Constants.BUNDLE_KM);
            mPhanTramGiamGia = savedInstanceState
                    .getDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA);
            mGia = savedInstanceState.getString(Constants.BUNDLE_GIA);
            mStartAddress = savedInstanceState
                    .getString(Constants.BUNDLE_START_ADDRESS);
            mEndLat = savedInstanceState.getDouble(Constants.BUNDLE_END_LAT);
            mEndLng = savedInstanceState.getDouble(Constants.BUNDLE_END_LNG);
            mEndAddress = savedInstanceState
                    .getString(Constants.BUNDLE_END_ADDRESS);
            mKhuyenMai = savedInstanceState
                    .getString(Constants.BUNDLE_KHUYEN_MAI);
            mThoiGianDonXe = savedInstanceState
                    .getString(Constants.BUNDLE_THOI_GIAN);
            mNhomCNDoiTacId = savedInstanceState
                    .getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC);
            if (savedInstanceState.containsKey(Constants.KEY_GO_TO))
                mIsGoToMap = true;
            else
                mIsGoToMap = false;
            try {
                if (savedInstanceState
                        .containsKey(Constants.BUNDLE_TIME_BOOKING))
                    mTimeBooking = Long.parseLong(savedInstanceState
                            .getString(Constants.BUNDLE_TIME_BOOKING));
            } catch (Exception e) {
            }
            mLoaiXeName = savedInstanceState
                    .getString(Constants.BUNDLE_LOAIXE_NAME);
            mMota = savedInstanceState.getString(Constants.BUNDLE_MOTA);
            mDriverNumber = savedInstanceState
                    .getInt(Constants.BUNDLE_NUMBER_DRIVER);
            mDriverIdSorted = savedInstanceState
                    .getString(Constants.BUNDLE_DRIVERS_SORTED);

            String sPromotion = mPhanTramGiamGia + "";

            new RequestOrderObject(mDatXeId);
            PTLocationInfo ptLocationInfoFrom = new PTLocationInfo();
            ptLocationInfoFrom.setLat(mStartLat);
            ptLocationInfoFrom.setLng(mStartLng);
            ptLocationInfoFrom.setAddress(mStartAddress);
            PTLocationInfo ptLocationInfoTo = new PTLocationInfo();
            ptLocationInfoTo.setLat(mEndLat);
            ptLocationInfoTo.setLng(mEndLng);
            ptLocationInfoTo.setAddress(mEndAddress);

            mMessageToDriver = new LocationMessageClient();
            mMessageToDriver.setId(Pasgo.getInstance().userId);
            mMessageToDriver.setTimeCome(mThoiGianDonXe);
            mMessageToDriver.setLocationFrom(ptLocationInfoFrom);
            mMessageToDriver.setLocationTo(ptLocationInfoTo);
            mMessageToDriver.setPromotion(sPromotion);
            mMessageToDriver.setNear(mMota);
            mMessageToDriver.setType(Constants.CALL_TYPE_MESSAGE_CLIENT);
            mMessageToDriver.setKindOfTaxiId(mNhomXeDichVuId);
            if (Constants.IS_OPERATOR) {
                mMessageToDriver.setFullName(getString(R.string.kh_goi_ho));
                mMessageToDriver
                        .setNumberPhone(Pasgo.getInstance().sdtKhachHang);
            } else {
                mMessageToDriver.setFullName(Pasgo.getInstance().username);
                mMessageToDriver.setNumberPhone(Pasgo.getInstance().sdt);
            }

            mMessageToDriver.setTimeSend(mTimeBooking);
            mMessageToDriver.setPrice(mGia + " VND");
            mMessageToDriver.setDistance(mKm + " Km");
            mMessageToDriver.setBookingId(mDatXeId);
        }

    }

    @Override
    protected void initView() {
        super.initView();
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnTimKiemXe = (LinearLayout) findViewById(R.id.lnTimKiemXe);
        mLnThuLai = (LinearLayout) findViewById(R.id.ln_bt_thu_lai);
        mRlTimKiemXeThatBai = (RelativeLayout) findViewById(R.id.rlTimKiemXeThatBai);
        mBtnComplete = (Button) findViewById(R.id.btnComplete);
        mTvKm = (TextView) findViewById(R.id.tvKm);
        mTvMoney = (TextView) findViewById(R.id.tvMoney);
        mTvDiaDiemHienTai = (TextView) findViewById(R.id.tvDiaDiemHienTai);
        mTvDiaDiemDen = (TextView) findViewById(R.id.tvDiaDiemDen);
        mTvHaveDriver = (TextView) findViewById(R.id.tvHaveDriver);
        mTvSecondNumber = (TextView) findViewById(R.id.tvSecondNumber);
        mBtnCall = (ImageButton) findViewById(R.id.btnCall);
        mTvThongBaoPhucVu = (TextView) findViewById(R.id.tvThongBaoPhucVu);
        mBtnComplete.setOnClickListener(this);
        mBtnCall.setOnClickListener(this);
        Utils.setTextViewHtml(mTvSecondNumber, String.format(getString(R.string.have_all_driver_right), "60s"));
        mTvHaveDriver.setText(getResources().getString(R.string.watting_driver));
        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilterConnectChannel = new IntentFilter(
                Constants.BROADCAST_ACTION_CONNECT_CHANNEL_STATE);
        registerReceiver(bUpdateStateConnectChannel, intentFilterConnectChannel);
        if(!isTimKiem)
            handleUpdateUI.sendEmptyMessage(10);
    }

    private final BroadcastReceiver bUpdateStateConnectChannel = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getExtras() == null)
                return;
            Bundle bundle = intent.getExtras();
            if (bundle
                    .containsKey(Constants.BROADCAST_DATA_CONNECT_CHANNEL_STATE))
                handleUpdateStateChannel
                        .sendEmptyMessage(bundle
                                .getInt(Constants.BROADCAST_DATA_CONNECT_CHANNEL_STATE));
        }
    };

    Handler handleUpdateStateChannel = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.CONNECT_CHANNEL_STATE_CONNECT:
                    if (getBaseContext() == null || mLnErrorConnectNetwork == null)
                        return;
                    if (NetworkUtils.getInstance(getBaseContext())
                            .isNetworkAvailable())
                        mLnErrorConnectNetwork.setVisibility(View.GONE);
                    mIsErrorChannel = false;
                    break;
                case Constants.CONNECT_CHANNEL_STATE_DISCONNECT:
                    if (getBaseContext() == null || mLnErrorConnectNetwork == null)
                        return;
                    mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
                    mIsErrorChannel = true;
                    break;
                case Constants.CONNECT_CHANNEL_STATE_ERROR:
                    if (getBaseContext() == null || mLnErrorConnectNetwork == null)
                        return;
                    //mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
                    mIsErrorChannel = true;
                    break;
                case Constants.CONNECT_CHANNEL_STATE_RECONNECT:
                    if (getBaseContext() == null || mLnErrorConnectNetwork == null)
                        return;
                    if (NetworkUtils.getInstance(getBaseContext())
                            .isNetworkAvailable())
                        mLnErrorConnectNetwork.setVisibility(View.GONE);
                    mIsErrorChannel = false;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void setText() {
        mTvKm.setText(mKm + " Km");
        mTvMoney.setText("~" + mGia + " VND");
        mTvDiaDiemHienTai.setText(mStartAddress);
        mTvDiaDiemDen.setText(mEndAddress);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.BUNDLE_THOI_GIAN, mThoiGianDonXe);
        outState.putDouble(Constants.BUNDLE_START_LAT, mStartLat);
        outState.putDouble(Constants.BUNDLE_START_LNG, mStartLng);
        outState.putDouble(Constants.BUNDLE_KM, mKm);
        outState.putDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA,
                mPhanTramGiamGia);
        outState.putString(Constants.BUNDLE_GIA, mGia);
        outState.putString(Constants.BUNDLE_START_ADDRESS, mStartAddress);
        outState.putDouble(Constants.BUNDLE_END_LAT, mEndLat);
        outState.putDouble(Constants.BUNDLE_END_LNG, mEndLng);
        outState.putString(Constants.BUNDLE_END_ADDRESS, mEndAddress);
        outState.putString(Constants.BUNDLE_DAT_XE_ID, mDatXeId);
        outState.putString(Constants.BUNDLE_MOTA, mMota);
        outState.putInt(Constants.BUNDLE_NUMBER_DRIVER, mDriverNumber);
        outState.putDouble(Constants.BUNDLE_KHACHHANG_LAT, mKhachHangLat);
        outState.putDouble(Constants.BUNDLE_KHACHHANG_LNG, mKhachhangLng);
        outState.putString(Constants.BUNDLE_DRIVERS_SORTED, mDriverIdSorted);
        outState.putBoolean(Constants.BUNDLE_CALL_STOP, isTimKiem);
        if (mKhuyenMai == null) {
            mKhuyenMai = "";
        }
        outState.putString(Constants.BUNDLE_KHUYEN_MAI, mKhuyenMai);
        outState.putInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID, mLoaiHinhDichVuId);
        outState.putInt(Constants.BUNDLE_DICH_VU_ID, mDichVuId);
        if (mNhomXeDichVuId == 0 || StringUtils.isEmpty(mLoaiXeName))
            return;
        outState.putInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID, mNhomXeDichVuId);
        outState.putString(Constants.BUNDLE_LOAIXE_NAME, mLoaiXeName);
        outState.putBoolean(Constants.KEY_GO_TO, mIsGoToMap);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mKhachHangLat = savedInstanceState
                .getDouble(Constants.BUNDLE_KHACHHANG_LAT);
        mKhachhangLng = savedInstanceState
                .getDouble(Constants.BUNDLE_KHACHHANG_LNG);
        mDatXeId = savedInstanceState.getString(Constants.BUNDLE_DAT_XE_ID);
        mDichVuId = savedInstanceState.getInt(Constants.BUNDLE_DICH_VU_ID);
        mLoaiHinhDichVuId = savedInstanceState
                .getInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID);
        mNhomXeDichVuId = savedInstanceState
                .getInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID);
        mStartLat = savedInstanceState.getDouble(Constants.BUNDLE_START_LAT);
        mStartLng = savedInstanceState.getDouble(Constants.BUNDLE_START_LNG);
        mKm = savedInstanceState.getDouble(Constants.BUNDLE_KM);
        mPhanTramGiamGia = savedInstanceState
                .getDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA);
        mGia = savedInstanceState.getString(Constants.BUNDLE_GIA);
        mStartAddress = savedInstanceState
                .getString(Constants.BUNDLE_START_ADDRESS);
        mEndLat = savedInstanceState.getDouble(Constants.BUNDLE_END_LAT);
        mEndLng = savedInstanceState.getDouble(Constants.BUNDLE_END_LNG);
        mEndAddress = savedInstanceState
                .getString(Constants.BUNDLE_END_ADDRESS);
        mThoiGianDonXe = savedInstanceState
                .getString(Constants.BUNDLE_THOI_GIAN);
        mLoaiXeName = savedInstanceState
                .getString(Constants.BUNDLE_LOAIXE_NAME);
        mMota = savedInstanceState.getString(Constants.BUNDLE_MOTA);
        mDriverNumber = savedInstanceState
                .getInt(Constants.BUNDLE_NUMBER_DRIVER);
        mDriverIdSorted = savedInstanceState
                .getString(Constants.BUNDLE_DRIVERS_SORTED);
        mKhuyenMai = savedInstanceState.getString(Constants.BUNDLE_KHUYEN_MAI);
        if (mKhuyenMai == null) {
            mKhuyenMai = "";
        }
        isTimKiem = savedInstanceState.getBoolean(Constants.BUNDLE_CALL_STOP);
        new RequestOrderObject(mDatXeId);
        mIsGoToMap = savedInstanceState.getBoolean(Constants.KEY_GO_TO);
        setText();
    }

    @Override
    protected void initControl() {
        super.initControl();
        mIsCallHangXe = false;
        isTimKiem = true;
        isFullTime = false;
        isAccepted = false;
        mIsCallDriver = true;

        mDriverIdsAccept = new HashMap<String, LocationMessageDriver>();
        mLocationMessageDrivers = new ArrayList<LocationMessageDriver>();
        mLocationMessageDriversSorted = new ArrayList<LocationMessageDriver>();

        mLyDoHuys = new ArrayList<LyDoHuy>();
        mDriverIdsServer = new ArrayList<String>();
        mDriverIdsNotAccept = new ArrayList<String>();

        loadCallerAll(mDriverIdSorted, PTService.mLocationMsgDriverMap, true);

        registerReceiver(broadcastReceiverPubnub, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
        mRequestOrderObject = new RequestOrderObject();
        mRequestHangXeOrderObject = new RequestHangXeOrderObject();
        registerReceiver(broadcastReceiverUpdateTimTaiXe, new IntentFilter(
                Constants.KEY_ACTION_FIND_TAXI));
    }

    private BroadcastReceiver broadcastReceiverPubnub = new BroadcastReceiver() {
        private boolean isLoad = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (context == null)
                return;
            if (!isLoad) {
                isLoad = true;
                return;
            }
            if (intent.getAction().equals(
                    ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (NetworkUtils.getInstance(context).isNetworkAvailable())
                    Pasgo.getInstance().mPubnub.disconnectAndResubscribe();
            }
        }
    };

    //region sub and pub
    private void subChannel(String nameChannel) {
        try {
            Pasgo.getInstance().mPubnub.subscribe(
                    new String[]{nameChannel}, new Callback() {
                        @Override
                        public void connectCallback(String channel,
                                                    Object message) {

                            if (Pasgo.getInstance().prefs != null) {
                                handleUpdateUI.sendEmptyMessage(2);
                                pubChannelCall(mMessageToDriver);
                            }
                        }

                        @Override
                        public void disconnectCallback(String channel,
                                                       Object message) {
                            Utils.Log(TAG, message.toString());
                        }

                        @Override
                        public void reconnectCallback(String channel,
                                                      Object message) {
                            Utils.Log(TAG, "reconnectCallback" + message.toString());
                            if (mIsCallAgain) {
                                getUnixTimeNow(false);
                            }
                        }

                        @Override
                        public void successCallback(String channel,
                                                    Object message) {
                            Utils.Log(TAG, "message_call" + message.toString());
                            JSONObject jsonObject = (JSONObject) message;
                            parserDataDriver(jsonObject);

                        }

                        @Override
                        public void errorCallback(String channel,
                                                  PubnubError error) {
                            if (error.toString().toLowerCase()
                                    .contains("disconnect")
                                    && Pasgo.getInstance().mPubnub
                                    .getMaxRetries() == Constants.MAX_RETRIES_PUBNUB - 1)

                                Pasgo.getInstance().mPubnub
                                        .setMaxRetries(Constants.MAX_RETRIES_PUBNUB);
                        }
                    });
        } catch (PubnubException e) {
            Log.e(TAG, e.toString());

        }
    }

    private void pubChannelCall(LocationMessageClient locationMessageClient) {
        try {
            if (locationMessageClient == null)
                return;
            Gson gson = new Gson();
            String message = gson.toJson(locationMessageClient);
            final JSONObject jsonObject = new JSONObject(message);
            Pasgo.getInstance().mPubnub.publish(Constants.APP_PUBNUB
                            + Constants.VERSION_DRIVER_PUBNUB, jsonObject,
                    new Callback() {
                        @Override
                        public void successCallback(String channel,
                                                    Object message) {
                            isFullTime = false;
                            mDriverIdsAccept = new HashMap<String, LocationMessageDriver>();
                            mLocationMessageDrivers = new ArrayList<LocationMessageDriver>();
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void callTongDai(LocationMessageClient locationMessageClient, final boolean isCallhang) {
        try {
            if (locationMessageClient == null)
                return;
            Gson gson = new Gson();
            String message = gson.toJson(locationMessageClient);
            final JSONObject jsonObject = new JSONObject(message);
            Pasgo.getInstance().mPubnub.publish(Constants.APP_PUBNUB
                            + Constants.VERSION_DRIVER_PUBNUB, jsonObject,
                    new Callback() {
                        @Override
                        public void successCallback(String channel,
                                                    Object message) {
                            if (isCallhang)// lần đầu gọi hãng và chạy thanh progress bar
                                handleUpdateUI.sendEmptyMessage(8);
                            else
                                handleUpdateUI.sendEmptyMessage(11);
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void pubChannelAccept(ArgeeMessage argeeMessage) {
        try {
            if (argeeMessage == null)
                return;
            isAccepted = true;
            Gson gson = new Gson();
            String message = gson.toJson(argeeMessage);
            final JSONObject jsonObject = new JSONObject(message);
            String channel=Constants.APP_PUBNUB
                    + Constants.SPEC_PUBNUB + mMessageToDriver.getId()
                    + mMessageToDriver.getBookingId();
            Pasgo.getInstance().mPubnub.publish(channel, jsonObject,
                    new Callback() {
                        @Override
                        public void successCallback(String channel,
                                                    Object message) {
                            isAccepted = true;
                            Utils.Log(TAG, "message accept" + message);
                        }

                        @Override
                        public void errorCallback(String channel,
                                                  PubnubError error) {
                            try {
                                isAccepted = false;
                                mDriverIdsNotAccept.add(mDrivingInvited.getId());
                            } catch (Exception e) {
                            }

                        }
                    });
        } catch (Exception e) {
            isAccepted = false;
            mDriverIdsNotAccept.add(mDrivingInvited.getId());
        }
    }

    //endregion
    synchronized private void parserDataDriver(JSONObject jsonObject) {
        if (getBaseContext() == null)
            return;
        try {
            if (mMessageToDriver != null
                    && mMessageToDriver.getLocationFrom() != null
                    && jsonObject != null
                    && jsonObject.has(Constants.KEY_ID_MESSAGE_DRIVER)) {
                if (jsonObject.has(Constants.KEY_LOCATION_MESSAGE_DRIVER)
                        && !isFullTime) {
                    LocationMessageDriver locationMessageDriver = null;
                    JSONObject jsonObjectLocation = jsonObject
                            .getJSONObject(Constants.KEY_LOCATION_MESSAGE_DRIVER);
                    String addressDriver = "";
                    if (jsonObjectLocation
                            .has(Constants.KEY_LOCATION_ADDRESS_MESSAGE))
                        addressDriver = jsonObjectLocation
                                .getString(Constants.KEY_LOCATION_ADDRESS_MESSAGE);
                    Location locationDriver = new Location(addressDriver);
                    locationDriver.setLatitude(jsonObjectLocation
                            .getDouble(Constants.KEY_LOCATION_LAT_MESSAGE));
                    locationDriver.setLongitude(jsonObjectLocation
                            .getDouble(Constants.KEY_LOCATION_LNG_MESSAGE));
                    if (mDriverIdsAccept == null) {
                        mDriverIdsAccept = new HashMap<String, LocationMessageDriver>();
                        mLocationMessageDrivers = new ArrayList<LocationMessageDriver>();
                    }
                    if (mDriverIdsAccept.containsKey(jsonObject
                            .getString(Constants.KEY_ID_MESSAGE_DRIVER))) {
                        return;
                    } else {
                        PTLocationInfo locationInfoClient = mMessageToDriver
                                .getLocationFrom();
                        Location locationClient = new Location("from");
                        locationClient.setLatitude(locationInfoClient.getLat());
                        locationClient
                                .setLongitude(locationInfoClient.getLng());
                        locationMessageDriver = new LocationMessageDriver();
                        locationMessageDriver.setId(jsonObject
                                .getString(Constants.KEY_ID_MESSAGE_DRIVER));
                        locationMessageDriver
                                .setFullName(jsonObject
                                        .getString(Constants.KEY_FULL_NAME_MESSAGE_DRIVER));

                        locationMessageDriver
                                .setNumberPlate(jsonObject
                                        .getString(Constants.KEY_NUMBER_PLATE_MESSAGE_DRIVER));
                        locationMessageDriver
                                .setTaxiCompany(jsonObject
                                        .getString(Constants.KEY_TAXI_COMPANY_MESSAGE_DRIVER));
                        locationMessageDriver
                                .setNumberPhone(jsonObject
                                        .getString(Constants.KEY_NUMBER_PHONE_MESSAGE_DRIVER));
                        locationMessageDriver
                                .setFree(jsonObject
                                        .getBoolean(Constants.KEY_STATE_MESSAGE_DRIVER));
                        locationMessageDriver.setDistance(MapUtil.Distance(
                                locationDriver, locationClient));
                        PTLocationInfo locationInfo = new PTLocationInfo(
                                locationDriver.getProvider(),
                                locationDriver.getLatitude(),
                                locationDriver.getLongitude());
                        locationMessageDriver.setLocation(locationInfo);
                        mDriverIdsAccept.put(locationMessageDriver.getId(),
                                locationMessageDriver);
                        mLocationMessageDrivers.add(locationMessageDriver);
                    }
                } else if (jsonObject.has(Constants.KEY_OBJECT_MESSAGE)
                        && Constants.VALUE_DRIVER_OBJECT_MESSAGE
                        .equals(jsonObject
                                .getString(Constants.KEY_OBJECT_MESSAGE))) {
                    if (mDrivingInvited != null
                            && mDrivingInvited
                            .getId()
                            .equals(jsonObject
                                    .getString(Constants.KEY_ID_MESSAGE))
                            && jsonObject
                            .getBoolean(Constants.KEY_ACCEPT_MESSAGE)) {
                        isAccepted = true;
                        ArgeeMessage argeeMessage = new ArgeeMessage();
                        argeeMessage.setId(mMessageToDriver.getId());
                        argeeMessage
                                .setObject(Constants.VALUE_CLIENT_OBJECT_MESSAGE);
                        argeeMessage.setPick(true);
                        argeeMessage.setSendTo(mDrivingInvited.getId());
                        argeeMessage.setAccept(true);
                        pubChannelAccept(argeeMessage);
                        Pasgo.getInstance().mPubnub
                                .unsubscribe(Constants.APP_PUBNUB
                                        + Constants.SPEC_PUBNUB
                                        + mMessageToDriver.getId() + mDatXeId);
                        goToChatActivity(mDrivingInvited);
                    }

                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void goToChatActivity(LocationMessageDriver locationMessageDriver)
            throws JSONException {
        Gson gson = new Gson();
        String message = gson.toJson(locationMessageDriver);
        JSONObject jsonObject = new JSONObject(message);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_FULL_MESSAGE_DRIVER,
                jsonObject.toString());
        bundle.putString(Constants.BUNDLE_THOI_GIAN, mThoiGianDonXe);
        bundle.putDouble(Constants.BUNDLE_START_LAT, mStartLat);
        bundle.putDouble(Constants.BUNDLE_START_LNG, mStartLng);
        bundle.putDouble(Constants.BUNDLE_KM, mKm);
        bundle.putDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA, mPhanTramGiamGia);
        bundle.putString(Constants.BUNDLE_GIA, mGia);
        bundle.putString(Constants.BUNDLE_START_ADDRESS, mStartAddress);
        bundle.putDouble(Constants.BUNDLE_END_LAT, mEndLat);
        bundle.putDouble(Constants.BUNDLE_END_LNG, mEndLng);
        bundle.putDouble(Constants.BUNDLE_KHACHHANG_LAT, mKhachHangLat);
        bundle.putDouble(Constants.BUNDLE_KHACHHANG_LNG, mKhachhangLng);
        bundle.putString(Constants.BUNDLE_END_ADDRESS, mEndAddress);
        bundle.putString(Constants.BUNDLE_DAT_XE_ID, mDatXeId);
        bundle.putString(Constants.BUNDLE_LOAIXE_NAME, mLoaiXeName);
        bundle.putDouble(Constants.BUNDLE_LAIXE_LAT, locationMessageDriver
                .getLocation().getLat());
        bundle.putDouble(Constants.BUNDLE_LAIXE_LNG, locationMessageDriver
                .getLocation().getLng());
        bundle.putString(Constants.BUNDLE_LAIXE_ID,
                locationMessageDriver.getId());
        bundle.putString(Constants.BUNDLE_LAIXE_NAME,
                locationMessageDriver.getFullName());
        bundle.putString(Constants.BUNDLE_LAIXE_PHONE,
                locationMessageDriver.getNumberPhone());
        if (mNhomXeDichVuId > 0)
            bundle.putInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID, mNhomXeDichVuId);
        if (mIsGoToMap) {
            bundle.putInt(Constants.KEY_GO_TO, Constants.KEY_GO_TO_MAP);
            gotoActivityForResult(mContext, TheoDoiTaiXeActivity.class, bundle,
                    Constants.KEY_GO_TO_MAP, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        } else {
            gotoActivityForResult(mContext, TheoDoiTaiXeActivity.class, bundle,
                    Constants.kEY_BACK_FORM_DATXE,
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(TAG, "requestCode: " + requestCode + "resultCode: " + resultCode);
        if (requestCode == Constants.KEY_GO_TO_MAP && resultCode == RESULT_OK) {
            // setResult(RESULT_OK);
            // finish();
            if (data == null) {
                Intent intent = new Intent(mActivity,
                        XacNhanDatXeActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt(Constants.KEY_COMPLETE,
                        Constants.KEY_INT_COMPLETE);
                intent.putExtras(bundle1);
                setResult(RESULT_OK, intent);

                finishToRightToLeft();
                return;
            }
            Bundle bundle = data.getExtras();
            int complete = 0;
            if (bundle != null) {
                complete = bundle.getInt(Constants.KEY_COMPLETE);
            }
            if (complete == Constants.KEY_INT_COMPLETE) {
                Intent intent = new Intent(mActivity,
                        XacNhanDatXeActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt(Constants.KEY_COMPLETE,
                        Constants.KEY_INT_COMPLETE);
                intent.putExtras(bundle1);
                setResult(RESULT_OK, intent);

                finishToRightToLeft();
            } else {
                finishToRightToLeft();
            }
        }
        if (requestCode == Constants.kEY_BACK_FORM_DATXE) {
            Intent intent = new Intent(mActivity, XacNhanDatXeActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putInt(Constants.KEY_COMPLETE, Constants.KEY_INT_COMPLETE);
            intent.putExtras(bundle1);
            setResult(Constants.kEY_BACK_FORM_DATXE, intent);
            finishToRightToLeft();
        }
        if (requestCode != resultCode)
            return;
        switch (requestCode) {
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDialogHuy();
                return true;
        }
        return true;
    }

    private void showLayOutTimKiem() {
        if (isTimKiem) {
            mLnTimKiemXe.setVisibility(View.VISIBLE);
            mRlTimKiemXeThatBai.setVisibility(View.GONE);
            mLnThuLai.setVisibility(View.GONE);
            startAlarm();
        } else {
            mLnTimKiemXe.setVisibility(View.GONE);
            mRlTimKiemXeThatBai.setVisibility(View.VISIBLE);
            mLnThuLai.setVisibility(View.VISIBLE);
            setTextPhucVu();
        }
    }

    private void getUnixTimeNow(boolean isShow) {
        String url = WebServiceUtils
                .URL_UNIX_TIME_NOW(Pasgo.getInstance().token);
        Utils.Log(TAG, "url dat xe " + url);
        JSONObject jsonParams = new JSONObject();
        if (isShow)
            showProgressDialogAll();
        try {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new PWListener() {

                        @Override
                        public void onResponse(JSONObject json) {
                            if (json != null) {
                                Utils.Log(TAG, json.toString());
                                handleUpdateUI.sendEmptyMessage(5);
                                mMessageToDriver.setTimeSend(ParserUtils
                                        .getUnixTimeNow(json));
                                DatXeUpdate
                                        .updateDatXeByTrangThai(
                                                mDatXeId,
                                                EnumDatXeUpdate.KHACH_HANG_THU_TIM_LAI_LAI_XE
                                                        .getValue());
                                if (StringUtils.getDistanceTime(StringUtils
                                                .getLongValueDate(mThoiGianDonXe,
                                                        "yyyy/MM/dd HH:mm:ss"),
                                        StringUtils.getCurrentTime()) > 0) {
                                    mThoiGianDonXe = StringUtils
                                            .getCurrentTimeFormat("yyyy/MM/dd HH:mm:ss");
                                    mMessageToDriver
                                            .setTimeCome(mThoiGianDonXe);
                                }
                                Pasgo.getInstance().prefs.putTimeCall(null);
                                handleUpdateUI.sendEmptyMessage(7);
                                handleUpdateUI.sendEmptyMessage(2);
                                pubChannelCall(mMessageToDriver);
                            }
                            closeProgressDialogAll();
                        }

                        @Override
                        public void onError(int maloi) {
                            closeProgressDialog();
                            hoanThanhKhiMatMang();
                        }

                    }, new PWErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            closeProgressDialog();
                            hoanThanhKhiMatMang();
                        }
                    });
        } catch (Exception e) {
            closeProgressDialog();
            ToastUtils.showToast(mContext,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

    private void hoanThanhKhiMatMang()
    {
        ToastUtils
                .showToast(
                        mContext,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu_again);
        hoanThanh();
    }

    private void hoanThanh() {
        if (mDiXeFree == Constants.KEY_INT_XE_FREE) {
            setResult(Constants.KEY_GO_TO_MAP);
        } else {
            Intent intent = new Intent(mActivity,
                    XacNhanDatXeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_COMPLETE,
                    Constants.KEY_INT_COMPLETE);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finishToRightToLeft();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnComplete:
                if (mToolTipPref != null) {
                    mToolTipPref.putTimTaiXeThuLai(true);
                    mRlToolTipBottomLeft.setVisibility(View.GONE);
                }
                hoanThanh();
                break;
            */
/*case R.id.lnCancel:
                if (mLyDoHuys == null || mLyDoHuys.size() == 0)
                    getLyDoKhachHangAll();
                else
                    showDialogLyDoHuy();
                break;*//*

            case R.id.btnCall:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + getResources().getString(R.string.so_dt_tong_dai)));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getLyDoKhachHangAll() {
        String url = WebServiceUtils.URL_GET_ALL_LY_DO_KHACH_HANG(Pasgo
                .getInstance().token);
        showProgressDialog();
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            try {
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                closeProgressDialog();
                                if (json != null) {
                                    mLyDoHuys = ParserUtils.getLyDoHuys(json);
                                    showDialogLyDoHuy();
                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                closeProgressDialog();
                            }

                        }, new PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                showAlertMangYeu(2);
                            }
                        });
            } catch (Exception e) {
            }
        } else {
            showAlertMangYeu(2);
        }
    }

    private void showAlertMangYeu(final int i) {
        final Dialog dialog = new Dialog(TimTaiXeActivity.this);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_mang_yeu);
        Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);
        Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
        dialogBtThuLai.setOnClickListener(v -> {
            switch (i) {
                case 2:
                    getLyDoKhachHangAll();
                    break;
                case 3:
                    updateLyDoKhachHangHuy(mLyDo);
                    break;
                default:
                    break;
            }
            dialog.dismiss();
        });
        dialogBtHuy.setOnClickListener(v -> {
            dialog.dismiss();
            finishToRightToLeft();
        });
        if (!dialog.isShowing() && !mIsDestroy && !isFinishing()) {
            dialog.show();
        }
    }

    private void showDialogLyDoHuy() {
        mLyDo = "";
        final Dialog mDialogThongTinTaiXe;
        mDialogThongTinTaiXe = new Dialog(mContext);
        mDialogThongTinTaiXe.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogThongTinTaiXe.setContentView(R.layout.layout_popup_huy_datxe);
        Button btnGui, btnCancel;
        Spinner spnChoXe;
        btnGui = (Button) mDialogThongTinTaiXe.findViewById(R.id.btnGui);
        btnCancel = (Button) mDialogThongTinTaiXe.findViewById(R.id.btnCancel);
        spnChoXe = (Spinner) mDialogThongTinTaiXe.findViewById(R.id.spnChoXe);
        final EditText edtLyDoKhac = (EditText) mDialogThongTinTaiXe
                .findViewById(R.id.edtLyDoKhac);
        List<String> list = new ArrayList<String>();
        if (mLyDoHuys != null)
            for (LyDoHuy lyDoHuy : mLyDoHuys) {
                list.add(lyDoHuy.getName());
            }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChoXe.setAdapter(dataAdapter);
        spnChoXe.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                mLyDo = mLyDoHuys.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        btnGui.setOnClickListener(v -> {
            if (!StringUtils.isEmpty(edtLyDoKhac))
                mLyDo = edtLyDoKhac.getText().toString();
            updateLyDoKhachHangHuy(edtLyDoKhac.getText().toString());
            huyDatXe();
            mDialogThongTinTaiXe.dismiss();
            setResult(RESULT_OK);
            finishToRightToLeft();
        });
        btnCancel.setOnClickListener(v -> mDialogThongTinTaiXe.dismiss());
        if(!isFinishing())
            mDialogThongTinTaiXe.show();
    }

    private void updateLyDoKhachHangHuy(String lyDo) {
        String url = WebServiceUtils.URL_UPDATE_LY_DO_KHACH_HANG_HUY(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            try {
                jsonParams.put("datXeId", mDatXeId);
                jsonParams.put("lyDo", lyDo);
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    Utils.Log(TAG, "json huy" + json);
                                    setResult(RESULT_OK);
                                    finishToRightToLeft();
                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                closeProgressDialog();
                            }

                        }, new PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utils.Log(TAG, "khong ket noi may chu");
                                showAlertMangYeu(3);
                            }
                        });
            } catch (Exception e) {
                Utils.Log(TAG, "khong ket noi may chu");
            }
        } else {
            showAlertMangYeu(3);
        }
    }

    public class StartFindDriver extends AsyncTask<Integer, Integer, Integer> {
        private boolean isCancel = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTotalS =60;
            myProgress = 100;
            if (Pasgo.getInstance().prefs != null
                    && Pasgo.getInstance().prefs.getTimeCall() != 0) {
                mTotalS = (int) (60 - (StringUtils.getCurrentTime() - Pasgo
                        .getInstance().prefs.getTimeCall()) / 1000);
                if (mTotalS <= 0) {
                    isTimKiem = false;
                    showLayOutTimKiem();
                    return;
                }
            } else if (Pasgo.getInstance().prefs != null) {
                Pasgo.getInstance().prefs.putTimeCall(StringUtils
                        .getCurrentTime() + "");
            }
        }
        @Override
        protected void onCancelled() {
            isCancel = true;
        }
        @Override
        protected Integer doInBackground(Integer... second) {
            try {
                int secondProgress= second[0];
                Utils.Log(TAG,"secondProgress "+secondProgress);
                for (int i=secondProgress;i>=0;i--)
                {
                    publishProgress(secondProgress);
                    secondProgress --;
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            if(!isCancel) {
                // setting progress percentage
                Utils.Log(TAG, "second " + progress[0]);
                mTotalS = progress[0];
                Intent broadcast = new Intent();
                broadcast
                        .setAction(Constants.KEY_ACTION_FIND_TAXI);
                broadcast.putExtra("KEY_TEST","123");
                sendBroadcast(broadcast);
            }
        }

        @Override
        protected void onPostExecute(Integer second) {
            Utils.Log(TAG, "second close"+second);
            startFindDriver.cancel(true);
        }

    }
    private StartFindDriver startFindDriver;
    private void startAlarm() {
        if (mActivity == null)
            return;
        startFindDriver=new StartFindDriver();
        startFindDriver.execute(60);
    }

    @Override
    protected void onDestroy() {
        startFindDriver.cancel(true);
        unregisterReceiver(broadcastReceiverUpdateTimTaiXe);
        unregisterReceiver(bUpdateStateConnectChannel);
        unregisterReceiver(broadcastReceiverPubnub);
        ArgeeMessage argeeMessage = new ArgeeMessage();
        argeeMessage.setId(mMessageToDriver.getId());
        argeeMessage.setObject(Constants.VALUE_CLIENT_OBJECT_MESSAGE);
        argeeMessage.setPick(false);
        argeeMessage.setSendTo("all");
        argeeMessage.setAccept(false);
        pubChannelAccept(argeeMessage);
        Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
                + Constants.SPEC_PUBNUB + Pasgo.getInstance().userId
                + mDatXeId);
        super.onDestroy();

    }

    private LocationMessageDriver getDriverAccept() {

        LocationMessageDriver locationMessageDriver = null;

        for (String driverId : mDriverIdsAccept.keySet()) {
            if (!mDriverIdsNotAccept.contains(driverId)
                    && mDriverIdsAccept.get(driverId) != null) {

                locationMessageDriver = mDriverIdsAccept.get(driverId);
                break;
            }
        }

        return locationMessageDriver;
    }

    private final BroadcastReceiver broadcastReceiverUpdateTimTaiXe = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                handleUpdateUI.sendEmptyMessage(3);
                myProgress = mTotalS * 100 * 1.0 / 60;
                int p = (int) myProgress;
                Utils.Log(TAG, "p" + p);
                if (mTotalS > Constants.TIME_WAIT_SUB_CLIENT) {
                    if (mDriverIdsServer != null && mDriverIdsServer.size() > 0
                            && mDriverIdsAccept != null
                            && mDriverIdsAccept.size() > 0 && !isAccepted && mIsCallDriver) {
                        // Loai bo nhung tai xe da chap nhan nhung confirm
                        // khong duoc trong vong 60s
                        mDrivingInvited = getDriverAccept();
                        if (mDrivingInvited != null) {
                            confirmAgainWithDriver(mDrivingInvited,
                                    mMessageToDriver);
                        }
                    }
                }

                if (mTotalS == Constants.TIME_SHOW_NUM_CLIENT - 1
                        && !isAccepted) {
                    mIsCallDriver = false;
                    loadTongDaiHang();
                }

                if (mTotalS == 0) {
                    isFullTime = true;
                    handleUpdateUI.sendEmptyMessage(0);
                    Utils.setTextViewHtml(mTvSecondNumber, String.format(
                            getResources().getString(R.string.have_all_driver_right),
                            mTotalS + "s"));

                    handleUpdateUI.sendEmptyMessage(10);
                }

                mTotalS--;
            }
        }
    };

    private final Handler handleUpdateUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (getBaseContext() == null || mLocationMessageDrivers == null)
                        return;

                    if (mLocationMessageDrivers.size() == 0) {
                        DatXeUpdate.updateDatXeByTrangThai(mDatXeId,
                                EnumDatXeUpdate.KHACH_HANG_TIM_KHONG_DUOC_LAI_XE
                                        .getValue());

                        isTimKiem = false;
                        showLayOutTimKiem();
                    }

                    break;
                case 1:
                    if (getBaseContext() == null || mLocationMessageDrivers == null)
                        return;
                    mTvHaveDriver.setText(getString(R.string.connect_to_driver));

                    break;
                case 2:
                    isTimKiem = true;
                    showLayOutTimKiem();
                    break;
                case 3:
                    if (getBaseContext() == null || mLocationMessageDrivers == null)
                        return;
                    if (mTotalS >= 0 && !isAccepted)
                        Utils.setTextViewHtml(mTvSecondNumber,String.format(
                            getResources().getString(R.string.have_all_driver_right),
                            mTotalS + "s"));
                    break;
                case 4:
                    if (getBaseContext() == null)
                        return;
                    closeProgressDialogAll();
                    break;
                case 5:
                    if (mTotalS >= 0)
                        Utils.setTextViewHtml(mTvSecondNumber,String.format(
                                getResources().getString(R.string.have_all_driver_right),
                                mTotalS + "s"));
                    break;
                case 6:
                    if(!isFinishing())
                        DialogUtils.showOkDialog(mActivity,
                            R.string.title_no_driver_near_here, R.string.dong_y,
                            onAcceptClick);
                    break;
                case 7:
                    setText();
                    break;
                case 8:
                    handleUpdateUI.sendEmptyMessage(2);
                    DatXeUpdate.updateDatXeByTrangThai(mDatXeId,
                            EnumDatXeUpdate.KHACH_HANG_TIM_KHONG_DUOC_LAI_XE
                                    .getValue());
                    loadTongDaiHang();
                    break;
                case 9:
                    isTimKiem = false;
                    showLayOutTimKiem();
                    showDialogHangXe(mActivity, String.format(getString(R.string.hang_xe_dieu_xe), hangXeName), R.string.dong_y, huyListener);
                    break;
                case 10:
                    // if(!isAccepted)
                    getThuLaiDatXe();
                    break;
                case 11:
                    DatXeUpdate.updateDatXeByTrangThai(mDatXeId,
                            EnumDatXeUpdate.KHACH_HANG_TIM_KHONG_DUOC_LAI_XE
                                    .getValue());
                    isTimKiem = false;
                    showLayOutTimKiem();
                    if (Pasgo.getInstance().prefs != null)
                        Pasgo.getInstance().prefs.putTimeCall(null);
                    handleUpdateUI.sendEmptyMessage(12);
                    break;
                case 12:
                    showDialogThuLai(mActivity, R.string.tb_khong_co_tai_xe_phuc_vu,
                            R.string.thu_lai, R.string.huy, thuLaiListener, huyListener);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void getThuLaiDatXe() {
        String url = WebServiceUtils.URL_GET_THU_LAI_DAT_XE(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        showProgressDialog();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            try {
                jsonParams.put("datXeId", mDatXeId);
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    closeProgressDialog();
                                    Utils.Log(TAG, "json getThuLaiDatXe" + json);
                                    JSONObject jsonObject = ParserUtils.getJsonObject(json, "Item");
                                    hangXeName = ParserUtils.getStringValue(jsonObject, "TenHangXe");
                                    if (!StringUtils.isEmpty(hangXeName)) {
                                        isAccepted = true;
                                        handleUpdateUI.sendEmptyMessage(9);
                                    } else {
                                        handleUpdateUI.sendEmptyMessage(12);
                                    }
                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                closeProgressDialog();
                                handleUpdateUI.sendEmptyMessage(12);
                            }

                        }, new PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                closeProgressDialog();
                                handleUpdateUI.sendEmptyMessage(12);
                            }
                        });
            } catch (Exception e) {
                Utils.Log(TAG, "khong ket noi may chu");
            }
        } else {
            showAlertMangYeu(3);
        }
    }

    public void showDialogHangXe(Context context, String message,
                                 int OkTextId, final View.OnClickListener onOKClick) {
        if (getBaseContext() == null)
            return;
        if (context != null) {
            mDialogHangXe = new Dialog(context);
            mDialogHangXe.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogHangXe.setContentView(R.layout.layout_logo_pasgo_ok_show);
            TextView tvThongBaoPopup=(TextView) mDialogHangXe.findViewById(R.id.tvThongBaoPopup);
            Button btnDongY;
            btnDongY = (Button)mDialogHangXe.findViewById(R.id.btnDongY);
            btnDongY.setText(OkTextId);
            btnDongY.setOnClickListener(onOKClick);
            mDialogHangXe.setCancelable(false);
            tvThongBaoPopup.setText(message);
            if (!mDialogHangXe.isShowing()&& !isFinishing())
                mDialogHangXe.show();
            if (mDialogThuLai != null && mDialogThuLai.isShowing() && !isFinishing()) {
                mDialogThuLai.dismiss();
                mDialogThuLai.cancel();
            }
        }
    }

    public void showDialogThuLai(final Context context, int messageId,
                                 int OkTextId, int cancelTextId,
                                 final View.OnClickListener onOKClick,
                                 final View.OnClickListener onCancelClick) {
        if (context != null) {
            if(mDialogThuLai ==null) {
                mDialogThuLai = new Dialog(context);
                mDialogThuLai.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogThuLai.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
            }
            TextView tvThongBaoPopup=(TextView) mDialogThuLai.findViewById(R.id.tvThongBaoPopup);
            Button btnDongY,btnHuy;
            btnDongY = (Button)mDialogThuLai.findViewById(R.id.btnDongY);
            btnHuy = (Button)mDialogThuLai.findViewById(R.id.btnHuy);
            btnDongY.setText(OkTextId);
            btnHuy.setText(cancelTextId);
            btnDongY.setOnClickListener(onOKClick);
            btnHuy.setOnClickListener(onCancelClick);
            mDialogThuLai.setCancelable(false);
            tvThongBaoPopup.setText(context.getString(messageId));
            if(mDialogThuLai!=null && !mDialogThuLai.isShowing() && !isFinishing())
                mDialogThuLai.show();
        }
    }

    OnClickListener thuLaiListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mDialogThuLai.dismiss();
            tryCallAgain();
        }
    };

    OnClickListener huyListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDialogHangXe!=null && mDialogHangXe.isShowing())
                mDialogHangXe.dismiss();
            if(mDialogThuLai!=null && mDialogThuLai.isShowing())
                mDialogThuLai.dismiss();
            hoanThanh();
        }
    };

    public void finish() {
        Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
                + Constants.SPEC_PUBNUB + Pasgo.getInstance().userId
                + mDatXeId);
        if (Pasgo.getInstance().prefs != null)
            Pasgo.getInstance().prefs.putTimeCall(null);
        super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialogHuy() {
        mDialogHuy = new Dialog(mContext);
        mDialogHuy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogHuy.setContentView(R.layout.layout_popup_xac_nhan);
        TextView tvHoTen = (TextView) mDialogHuy.findViewById(R.id.tvHoTen);
        Button btnDongY, btnHuy;
        btnHuy = (Button) mDialogHuy.findViewById(R.id.btnHuy);
        btnDongY = (Button) mDialogHuy.findViewById(R.id.btnDongY);
        tvHoTen.setText(StringUtils.getStringByResourse(mContext,
                R.string.huy_dat_xe));
        btnDongY.setText(StringUtils.getStringByResourse(mContext,
                R.string.dong_y));
        btnHuy.setText(StringUtils.getStringByResourse(mContext, R.string.huy));
        btnDongY.setOnClickListener(v -> {
            ArgeeMessage argeeMessage = new ArgeeMessage();
            argeeMessage.setId(mMessageToDriver.getId());
            argeeMessage.setObject(Constants.VALUE_CLIENT_OBJECT_MESSAGE);
            argeeMessage.setPick(false);
            argeeMessage.setAccept(false);
            argeeMessage.setSendTo("all");
            pubChannelAccept(argeeMessage);
            DatXeUpdate.updateDatXeByTrangThai(mDatXeId,
                    EnumDatXeUpdate.KHACH_HANG_HUY_XE.getValue());
            Pasgo.getInstance().sdtKhachHang = "";
            Intent intent = new Intent(mActivity,
                    XacNhanDatXeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_COMPLETE,
                    Constants.KEY_INT_COMPLETE);
            intent.putExtras(bundle);
            setResult(Constants.KEY_GO_TO_MAP, intent);
            finishToRightToLeft();
            mDialogHuy.dismiss();
        });
        btnHuy.setOnClickListener(v -> mDialogHuy.dismiss());
        mDialogHuy.show();
    }

    private void huyDatXe() {
        ArgeeMessage argeeMessage = new ArgeeMessage();
        argeeMessage.setId(mMessageToDriver.getId());
        argeeMessage.setObject(Constants.VALUE_CLIENT_OBJECT_MESSAGE);
        argeeMessage.setPick(false);
        argeeMessage.setAccept(false);
        argeeMessage.setSendTo("all");
        pubChannelAccept(argeeMessage);
        DatXeUpdate.updateDatXeByTrangThai(mDatXeId,
                EnumDatXeUpdate.KHACH_HANG_HUY_XE.getValue());
    }

    private void confirmAgainWithDriver(LocationMessageDriver drivingInvited,
                                        LocationMessageClient messageToDriver) {
        handleUpdateUI.sendEmptyMessage(1);
        ArgeeMessage argeeMessage = new ArgeeMessage();
        argeeMessage.setId(messageToDriver.getId());
        argeeMessage.setObject(Constants.VALUE_CLIENT_OBJECT_MESSAGE);
        argeeMessage.setPick(false);
        argeeMessage.setSendTo(drivingInvited.getId());
        argeeMessage.setAccept(true);
        pubChannelAccept(argeeMessage);
    }

    @Override
    public void onNetworkChanged() {
        if (getBaseContext() == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()
                && !mIsErrorChannel)
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        else
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
    }

    DialogInterface.OnClickListener onAcceptClick = (dialog, which) -> dialog.dismiss();

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
    }

    private void callAgain() {
        mDriverIdsServer = new ArrayList<String>();
        mDriverIdsNotAccept = new ArrayList<String>();
        String khachHangId = Pasgo.getInstance().userId;
        String maGiamGia = mKhuyenMai;
        String diaChiDonXe = mStartAddress;
        double viDoDonXe = mStartLat;
        double kinhDoDonXe = mStartLng;
        String diaChiDen = mEndAddress;
        double viDoDen = mEndLat;
        double kinhDoDen = mEndLng;
        String mota = mMota;
        String sdt = "";
        int loaiDatXe = 0;
        if (Constants.IS_OPERATOR) {
            sdt = Pasgo.getInstance().sdtKhachHang;
            loaiDatXe = 1;
        }
        if (maGiamGia == null) {
            maGiamGia = "";
        }
        mRequestOrderObject = new RequestOrderObject(khachHangId, mNhomXeDichVuId,
                sdt, maGiamGia, diaChiDonXe, viDoDonXe, kinhDoDonXe,
                mThoiGianDonXe, diaChiDen, viDoDen, kinhDoDen, mota, mKm, mGia,
                loaiDatXe, mLoaiHinhDichVuId, mDichVuId);

        loadDriverToServer(mRequestOrderObject, PTService.mLocationMsgDriverMap);
    }

    private void loadDriverToServer(RequestOrderObject requestOrderObject,
                                    HashMap<String, LocationMessageDriver> locationMsgDriverMap) {
        LoadDriverToServer loadDriverToServer = new LoadDriverToServer(
                requestOrderObject, locationMsgDriverMap);
        loadDriverToServer.execute();
    }

    class LoadDriverToServer extends AsyncTask<Void, Void, JSONObject> {
        HashMap<String, LocationMessageDriver> locationMsgDriverMap;
        RequestOrderObject requestOrderObject;

        public LoadDriverToServer(RequestOrderObject requestOrderObject,
                                  HashMap<String, LocationMessageDriver> locationMsgDriverMap) {
            this.locationMsgDriverMap = locationMsgDriverMap;
            this.requestOrderObject = requestOrderObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            if (requestOrderObject == null)
                return null;
            ArrayList<DriverRequestObject> driverRequestObjects = new ArrayList<DriverRequestObject>();
            try {
                Gson gson = new Gson();
                requestOrderObject.setDriverRequestObjects(gson
                        .toJson(driverRequestObjects));
                gson = new Gson();
                String json = gson.toJson(requestOrderObject);
                JSONObject jsonObject = new JSONObject(json);

                return jsonObject;
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result == null) {
                ToastUtils.showToast(mContext,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            } else {
                // day lai xe moi len server-> server se sap xep de tra ve so
                // lai xe moi sau do lay ra 7 con
                String laiXe = ParserUtils.getStringValue(result, "laiXe");
                getLaiXePriority(laiXe);
            }
        }
    }

    private void getLaiXePriority(String str) {
        String url = WebServiceUtils.URL_GET_LAI_XE_PRIORITY(Pasgo
                .getInstance().token);

        Utils.Log(TAG, "url dat xe " + url);
        Utils.Log(TAG, "jsonParams dat xe " + str);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("datXeId", mDatXeId);
            jsonParams.put("laiXe", str);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject json) {
                        if (json != null) {
                            String driversSorted = null;
                            Utils.Log(TAG, "json " + json);
                            try {
                                if (json.has("Items")) {
                                    JSONArray jsonArray = json
                                            .getJSONArray("Items");
                                    mDriverNumber = jsonArray.length();
                                    driversSorted = jsonArray.toString();
                                } else {
                                    mDriverNumber = 0;
                                }
                                mLocationMessageDrivers = ParserUtils
                                        .getDriverIds(driversSorted,
                                                PTService.mLocationMsgDriverMap);
                                loadServenDriverNearest(mLocationMessageDrivers,
                                        false);
                            } catch (JSONException e) {
                                handleUpdateUI.sendEmptyMessage(0);
                            }
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        closeProgressDialog();
                    }

                }, new PWErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        ToastUtils.showToast(mContext,
                                R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
                    }
                });
    }

    private void loadServenDriverNearest(
            ArrayList<LocationMessageDriver> locationMessageDriver,
            boolean isShow) {

        SevenDriverNearestTask sevenDriverNearestTask = new SevenDriverNearestTask(
                locationMessageDriver, isShow);
        sevenDriverNearestTask.execute();
    }

    class SevenDriverNearestTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        ArrayList<LocationMessageDriver> locationMessageDrivers;
        boolean isShow;

        public SevenDriverNearestTask(
                ArrayList<LocationMessageDriver> locationMessageDrivers,
                boolean isShow) {
            this.locationMessageDrivers = locationMessageDrivers;
            this.isShow = isShow;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isShow) {
                showProgressDialogAll();
            }
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            if (locationMessageDrivers != null
                    && locationMessageDrivers.size() > 0
                    && mMessageToDriver != null) {

                if (mDriverIdsServer != null && mDriverIdsServer.size() == 1) {
                    mDriverIdsNotAccept.add(mDriverIdsServer.get(0));
                }

                ArrayList<String> sevenDriversId = new ArrayList<String>();
                for (int i = 0; i < locationMessageDrivers.size(); i++) {
                    LocationMessageDriver driver = locationMessageDrivers
                            .get(i);
                    if (driver != null
                            && driver.getId() != null
                            && mDriverIdsNotAccept != null
                            && !mDriverIdsNotAccept
                            .contains(driver.getId()))

                        sevenDriversId.add(driver.getId());
                }

                return sevenDriversId;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            Utils.Log(TAG, "result again" + result);
            if (result == null || result.size() == 0) {
                if (isShow) {
                    closeProgressDialogAll();
                }
                mMessageToDriver.setDriverNearId(null);
                callTongDai(mMessageToDriver, false);
                return;
            }
            if (result != null && mMessageToDriver != null) {
                mDriverIdsServer = result;
                mMessageToDriver.setDriverNearId(result);
            }
            if (isShow && Pasgo.getInstance().prefs != null && !isAccepted) {
                // co drive phuc vu
                if (mLocationMessageDriversSorted.size() > 0)
                    mCoTaiXeHoacHangPhucVu = true;
                pubChannelCall(mMessageToDriver);
                handleUpdateUI.sendEmptyMessage(4);
            }
        }
    }

    private void loadTongDaiHang() {
        // kiểm tra nếu đã gọi hãng xe 1 lần rồi thì thôi không gọi nữa
        if (mIsCallHangXe) return;
		runOnUiThread(new Runnable(){
            public void run() {
                mIsCallHangXe = true;
                getTongDaiHang();
            }
        });
    }

    private void getTongDaiHang() {
        String url = WebServiceUtils.URL_GET_TONG_DAI_HANG(Pasgo
                .getInstance().token);
        Utils.Log(TAG, "url dat xe " + url);
        JSONObject jsonParams = new JSONObject();
        try {
            if (StringUtils.isEmpty(mNhomCNDoiTacId))
                mNhomCNDoiTacId = "";
            jsonParams.put("datXeId", mDatXeId);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {
                    @Override
                    public void onResponse(JSONObject json) {
                        if (json != null) {
                            Utils.Log(TAG, "json " + json);
                            ArrayList<HangXe> hangXes = ParserUtils
                                    .getHangXes(json);
                            if (hangXes.size() == 0) {
                                pubChannelCallOnepasHangXe(mDatXeId, 1);
                                mIsCallDriver = true;
                                callAgain();
                            } else {
                                mCoTaiXeHoacHangPhucVu = true;
                                pubChannelCallOnepasHangXe(mDatXeId, 2);
                                callHangXe(hangXes);
                            }
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        closeProgressDialog();
                    }

                }, new PWErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        ToastUtils.showToast(mContext,
                                R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
                    }
                });
    }

    private void callHangXe(ArrayList<HangXe> hangXes) {
        String khachHangId = Pasgo.getInstance().userId;
        String tenKhachHang = Pasgo.getInstance().username;
        int nhomXeId = mNhomXeDichVuId;
        String maGiamGia = mKhuyenMai;
        String tiemGiamGia =   mPhanTramGiamGia>0?mPhanTramGiamGia + "":"";
        String diaChiDonXe = mStartAddress;
        double viDoDonXe = mStartLat;
        double kinhDoDonXe = mStartLng;
        String diaChiDen = mEndAddress;
        double viDoDen = mEndLat;
        double kinhDoDen = mEndLng;
        String mota = mMota;
        String sdt = Pasgo.getInstance().sdt;
        if (maGiamGia == null) {
            maGiamGia = "";
        }
        mRequestHangXeOrderObject = new RequestHangXeOrderObject(mDatXeId,
                khachHangId, tenKhachHang, nhomXeId, sdt, maGiamGia,
                tiemGiamGia, diaChiDonXe, viDoDonXe, kinhDoDonXe,
                mThoiGianDonXe, diaChiDen, viDoDen, kinhDoDen, mota, mKm, mGia,
                hangXes, false, 60 - mTotalS);
        try {
            Gson gson = new Gson();
            gson = new Gson();
            String json = gson.toJson(mRequestHangXeOrderObject);
            JSONObject jsonObject = new JSONObject(json);
            pubChannelCallHangXe(jsonObject);
            subChannelHangXeBooking(Constants.APP_PUBNUB + Constants.VERSION_HANGXE_PUBNUB_BOOKING + mDatXeId);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void pubChannelCallHangXe(JSONObject jsonObject) {
        try {
            Pasgo.getInstance().mPubnub.publish(Constants.APP_PUBNUB
                            + Constants.VERSION_HANGXE_ORDER_PUBNUB, jsonObject,
                    new Callback() {
                        @Override
                        public void successCallback(String channel,
                                                    Object message) {
                            Pasgo.getInstance().mPubnub
                                    .unsubscribe(Constants.APP_PUBNUB
                                            + Constants.VERSION_HANGXE_ORDER_PUBNUB);
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void subChannelHangXeBooking(String nameChannel) {
        Utils.Log(TAG, "hang xe booking" + nameChannel);
        try {
            Pasgo.getInstance().mPubnub.subscribe(
                    new String[]{nameChannel}, new Callback() {
                        @Override
                        public void successCallback(String channel,
                                                    Object message) {
                            Utils.Log(TAG, message.toString());
                            JSONObject jsonObject = (JSONObject) message;
                            if (ParserUtils.getIntValue(jsonObject, "trangThai") == 3) {
                                hangXeName = ParserUtils.getStringValue(jsonObject, "hangXeName");
                                if (!StringUtils.isEmpty(hangXeName)) {
                                    isAccepted = true;
                                    handleUpdateUI.sendEmptyMessage(9);
                                }
                            }
                        }

                        @Override
                        public void connectCallback(String channel,
                                                    Object message) {

                        }

                        @Override
                        public void disconnectCallback(String channel,
                                                       Object message) {
                            Utils.Log(TAG, message.toString());
                        }

                        @Override
                        public void reconnectCallback(String channel,
                                                      Object message) {
                            Utils.Log(TAG, message.toString());
                        }

                        @Override
                        public void errorCallback(String channel,
                                                  PubnubError error) {
                        }
                    });
        } catch (PubnubException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void setTextPhucVu() {
        if (mCoTaiXeHoacHangPhucVu)
            mTvThongBaoPhucVu.setText(StringUtils.getStringByResourse(mContext,
                    R.string.tb_phuc_vu_1));
        else
            mTvThongBaoPhucVu.setText(StringUtils.getStringByResourse(mContext,
                    R.string.tb_phuc_vu_1));

    }

    //region lấy lại tài xế khi nhấn nút thử lại(Call again)
    private void tryCallAgain() {
        mIsCallAgain = true;
        Pasgo.getInstance().prefs.putTimeCall(null);
        showProgressDialogAllNoCancel();
        //lấy lại danh sách tài xế mới
        String khachHangId = Pasgo.getInstance().userId;
        String maGiamGia = mKhuyenMai;
        String diaChiDonXe = mStartAddress;
        double viDoDonXe = mStartLat;
        double kinhDoDonXe = mStartLng;
        String diaChiDen = mEndAddress;
        double viDoDen = mEndLat;
        double kinhDoDen = mEndLng;
        String mota = mMota;
        String sdt = "";
        int loaiDatXe = 0;
        if (Constants.IS_OPERATOR) {
            sdt = Pasgo.getInstance().sdtKhachHang;
            loaiDatXe = 1;
        }
        if (maGiamGia == null) {
            maGiamGia = "";
        }
        mRequestOrderObject = new RequestOrderObject(khachHangId,mNhomXeDichVuId ,
                sdt, maGiamGia, diaChiDonXe, viDoDonXe, kinhDoDonXe,
                mThoiGianDonXe, diaChiDen, viDoDen, kinhDoDen, mota, mKm, mGia,
                loaiDatXe,mLoaiHinhDichVuId, mDichVuId);

        LoadDriverToServerTryAgain(mRequestOrderObject, PTService.mLocationMsgDriverMap);
    }

    private void LoadDriverToServerTryAgain(RequestOrderObject requestOrderObject,
                                            HashMap<String, LocationMessageDriver> locationMsgDriverMap) {
        LoadDriverToServerTryAgain loadDriverToServer = new LoadDriverToServerTryAgain(
                requestOrderObject, locationMsgDriverMap);
        loadDriverToServer.execute();
    }

    class LoadDriverToServerTryAgain extends AsyncTask<Void, Void, JSONObject> {
        HashMap<String, LocationMessageDriver> locationMsgDriverMap;
        RequestOrderObject requestOrderObject;

        public LoadDriverToServerTryAgain(RequestOrderObject requestOrderObject,
                                          HashMap<String, LocationMessageDriver> locationMsgDriverMap) {
            this.locationMsgDriverMap = locationMsgDriverMap;
            this.requestOrderObject = requestOrderObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            if (requestOrderObject == null)
                return null;
            ArrayList<DriverRequestObject> driverRequestObjects = new ArrayList<DriverRequestObject>();
            try {
                Gson gson = new Gson();
                requestOrderObject.setDriverRequestObjects(gson
                        .toJson(driverRequestObjects));
                gson = new Gson();
                String json = gson.toJson(requestOrderObject);
                JSONObject jsonObject = new JSONObject(json);

                return jsonObject;
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result == null) {
                ToastUtils.showToast(mContext,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
                handleUpdateUI.sendEmptyMessage(4);
                hoanThanh();
            } else {
                // day lai xe moi len server-> server se sap xep de tra ve so
                String laiXe = ParserUtils.getStringValue(result, "laiXe");
                getLaiXePriorityTryAgain(laiXe);
            }
        }
    }

    private void getLaiXePriorityTryAgain(String str) {
        String url = WebServiceUtils.URL_GET_LAI_XE_PRIORITY(Pasgo
                .getInstance().token);

        Utils.Log(TAG, "url dat xe " + url);
        Utils.Log(TAG, "jsonParams dat xe " + str);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("datXeId", mDatXeId);
            jsonParams.put("laiXe", str);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject json) {
                        if (json != null) {
                            String driversSorted = null;
                            Utils.Log(TAG, "json " + json);
                            try {
                                if (json.has("Items")) {
                                    JSONArray jsonArray = json
                                            .getJSONArray("Items");
                                    mDriverNumber = jsonArray.length();
                                    driversSorted = jsonArray.toString();
                                } else {
                                    mDriverNumber = 0;
                                }
                                mDriverIdSorted = driversSorted;
                                //sau khi lấy được tài xế mới mình gọi lại như lần đầu tiên
                                handleUpdateUI.sendEmptyMessage(4);
                                initControl();
                            } catch (JSONException e) {

                            }
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        closeProgressDialog();
                        handleUpdateUI.sendEmptyMessage(12);
                    }

                }, new PWErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        ToastUtils.showToast(mContext,
                                R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
                        handleUpdateUI.sendEmptyMessage(12);
                    }
                });
    }
    //endregion

}*/
