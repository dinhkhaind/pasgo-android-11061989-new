package com.onepas.android.pasgo.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.PTService;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DiaChi;
import com.onepas.android.pasgo.models.LocationMessageDriver;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.account.AccountManagerActivity;
import com.onepas.android.pasgo.ui.calldriver.ChonLoaiXeActivity;
import com.onepas.android.pasgo.ui.calldriver.DialogInputSDT;
import com.onepas.android.pasgo.ui.calldriver.XacNhanDatXeActivity;
import com.onepas.android.pasgo.ui.calleddrivers.CalledDriverActivity;
import com.onepas.android.pasgo.ui.category.CategoryActivity;
import com.onepas.android.pasgo.ui.guid.GuidActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.partner.ListDiaDiemChiNhanhActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.ui.search.SearchActivity;
import com.onepas.android.pasgo.ui.setting.SettingActivity;
import com.onepas.android.pasgo.ui.share.ShareActivity;
import com.onepas.android.pasgo.ui.successfultrips.SuccessfulTripsActivity;
import com.onepas.android.pasgo.ui.termsandpolicies.TermsAndPoliciesActivity;
import com.onepas.android.pasgo.util.mapnavigator.EventListener;
import com.onepas.android.pasgo.util.mapnavigator.MapUtil;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentCallDriver extends BaseFragment implements View.OnClickListener {
    private final static String TAG="FragmentCallDriver";
    private View mRoot;
    private RelativeLayout mRlDiemDon, mRlDiemDen;
    private LinearLayout mLnDate, mLnChonXe;
    private LinearLayout mLnKhuyenMai;
    protected LinearLayout mLnWarningTimeDatTruoc;
    private Button mbtnDatXe;
    private ImageView mImgDiemDen, mImgTaxi;
    private Dialog mDialogTime;
    private final int KEY_10 = 1, KEY_20 = 2, KEY_30 = 3;
    private int mKey;
    private double mEndLat = 0, mEndLng = 0, mStartLat = 0, mStartLng = 0,
            mKm = 0, mKhachHangLat, mKhachhangLng;
    private String mGia;
    private String mStartName, mEndName, mStartAddress, mEndAddress,
            mThoiGianDonXe;
    private TextView mTvDiaDiemDonName, mTvDiaDiemDenName,
            mTvDiaDiemDonAddress, mTvDiaDiemDenAddress, mTvGoiNgayBayGio;
    private EditText mEdtMoTa;
    private TextView mTvKhuyenMai;
    private int mDriverNumber;
    private boolean mIsDatXe = false;
    private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();
    private String mTen;
    private String mDiaChi;
    private String mNhomCNDoiTacId;
    private double mKinhDo;
    private double mViDo;
    private String mWebsite;
    private int mDiXeFree;
    private boolean mNoiDungKm = false, mChiNhanhKm = false;
    private double mPhanTramGiamGia = 0.0;
    // xác định là đến từ màn hình Main hoặc để check Mã không đến từ màn hình "Điểm khuyến mại"
    private boolean mIsKeyFreeIsDen = true;
    private String mCode;
    private String mChiNhanhDoiTacId;
    private boolean mIsDatTruoc;
    private boolean mIsDiemTaiTroNguoiDung;
    private boolean mIsDiemTaiTroNguoiDungDiTuDo;
    private ImageView mImgChonDiemDen, mImgChonDiemDon;
    private TextView mTvTaxi;
    private ImageView mImgCall;
    private static final int PERMISSION_REQUEST_CODE_PHONE =1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mRoot==null)
        {
            mRoot = inflater.inflate(R.layout.fragment_call_driver, container, false);
            mToolbar = (Toolbar)mRoot.findViewById(R.id.tool_bar);
            mToolbar.setTitle("");
            TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(getString(R.string.goi_xe));
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            mLnErrorConnectNetwork = (LinearLayout)  mRoot.findViewById(R.id.lnErrorConnectNetwork);
            initView();
            Bundle extra = getActivity().getIntent().getExtras();
            getBundle(extra);
            initControl();
            onNetworkChanged();
            setDiaDiem(false);
            Pasgo.getInstance().sdtKhachHang = "";
            initializeMenu();
        }
        return mRoot;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
        {
            onRestoreInstanceState(savedInstanceState);
        }
    }
    private void initializeMenu() {
        try {
            mToolbar.setNavigationIcon(R.drawable.icon_menu);
            HomeActivity activity = (HomeActivity) getActivity();
            activity.mNavigationDrawerFragment.setUp(R.id.drawer_fragment, activity.mDrawerLayout, mToolbar);
            activity.mNavigationDrawerFragment.menuDatXe.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                activity.goToDatXe(HomeActivity.KEY_SERVICE_TAXI);
            });
            activity.mNavigationDrawerFragment.menuCategory.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, CategoryActivity.class);
            });
            activity.mNavigationDrawerFragment.menuTaiTro.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, ThePasgoActivity.class);
            });

            activity.mNavigationDrawerFragment.menuLichSuChuyenDi.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, SuccessfulTripsActivity.class);
            });
            activity.mNavigationDrawerFragment.menuHuongDanSuDung.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, GuidActivity.class);
            });

            activity.mNavigationDrawerFragment.menuQuyDinhChung.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, TermsAndPoliciesActivity.class);
            });
            activity.mNavigationDrawerFragment.menuGioiThieuBanBe.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, ShareActivity.class);
            });
            activity.mNavigationDrawerFragment.menuThietLap.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUNDLE_LANGUAGE_BEFOR, Pasgo.getInstance().language);
                bundle.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, activity.mIsShowKhuyenMai);
                gotoActivity(mActivity, SettingActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            });
            activity.mNavigationDrawerFragment.menuThoat.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                activity.menuThoat();
            });
            activity.mNavigationDrawerFragment.menuYeuThich.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, FavoriteActivity.class);
            });
            activity.mNavigationDrawerFragment.dat_truoc_rl.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, CalledDriverActivity.class);
            });
            activity.mNavigationDrawerFragment.check_in_rl.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, ReservedHistoryActivity.class);
            });
            activity.mNavigationDrawerFragment.lnAccount.setOnClickListener(view -> {
                activity.mDrawerLayout.closeDrawers();
                gotoActivityClearTop(mActivity, AccountManagerActivity.class);
            });
        }catch (Exception e)
        {
            mToolbar.setNavigationIcon(R.drawable.ic_action_back);
            mToolbar.setNavigationOnClickListener(v ->  finishToRightToLeft(getActivity()));
        }


    }
    private void getBundle(Bundle extra) {
        if (extra != null) {
            mChiNhanhDoiTacId = extra.getString(Constants.BUNDLE_KEY_ID);
            mNoiDungKm = extra.getBoolean(Constants.BUNDLE_KEY_NOI_DUNG_KM);
            mChiNhanhKm = extra.getBoolean(Constants.BUNDLE_KEY_CHI_NHANH_KM);
            mDiXeFree = extra.getInt(Constants.KEY_DI_XE_FREE);
            mTen = extra.getString(Constants.BUNDLE_KEY_TEN);
            mDiaChi = extra.getString(Constants.BUNDLE_KEY_DIA_CHI);
            mWebsite = extra.getString(Constants.BUNDLE_KEY_LINK_WEBSITE);
            mNhomCNDoiTacId = extra.getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID);
            mKinhDo = extra.getDouble(Constants.BUNDLE_KEY_KINH_DO);
            mViDo = extra.getDouble(Constants.BUNDLE_KEY_VI_DO);
            mIsDatTruoc = extra.getBoolean(Constants.BUNDLE_KEY_DAT_TRUOC);
            mIsKeyFreeIsDen = extra
                    .getBoolean(Constants.BUNDLE_KEY_FREE_IS_DEN, true);
            mCode = extra.getString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI);
            mIsDiemTaiTroNguoiDung = extra
                    .getBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG);
            mIsDiemTaiTroNguoiDungDiTuDo = extra
                    .getBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);

            mTvKhuyenMai.setText(StringUtils.isEmpty(mCode) ? getString(R.string.dat_xe_ma_khuyen_mai) : mCode);
        }
        try {
            mStartLat = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            mStartLng = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        } catch (Exception e) {
        }
        mKhachHangLat = mStartLat;
        mKhachhangLng = mStartLng;
        mStartName = StringUtils.getStringByResourse(mActivity,
                R.string.dia_diem_hien_tai);
        if (mIsDiemTaiTroNguoiDung && !mIsDiemTaiTroNguoiDungDiTuDo && !mIsKeyFreeIsDen) {
            mStartLng = mKinhDo;
            mStartLat = mViDo;
            mStartName = mTen;
            mStartAddress = mDiaChi;
            setDiaDiem(false);
        } else {
            mEndLng = mKinhDo;
            mEndLat = mViDo;
            mEndName = mTen;
            mEndAddress = mDiaChi;
            if (NetworkUtils.getInstance(mActivity)
                    .isNetworkAvailable()) {
                setDiaChiDon();
            }
        }
    }

    private void setDiaChiDon() {
        mStartAddress = MapUtil.getCompleteAddressString(mActivity, mStartLat,
                mStartLng);
        if (!StringUtils.isEmpty(mStartAddress)) {
            Utils.Log(TAG, "mStartAddress" + mStartAddress);
            mTvDiaDiemDonAddress.setVisibility(View.VISIBLE);
            mTvDiaDiemDonAddress.setText(mStartAddress);
            setDiaDiem(false);
        } else
            MapUtil.getAddressFromLocation(mStartLat, mStartLng,
                    new EventListener() {

                        @Override
                        public void location(Location location) {
                            Utils.Log(TAG, "location" + location);
                        }

                        @Override
                        public void address(DiaChi diaChi) {
                            Utils.Log(TAG, "diaChi" + diaChi);
                            mStartAddress = diaChi.getFullAddress();
                            mTvDiaDiemDonAddress.setVisibility(View.VISIBLE);
                            mTvDiaDiemDonAddress.setText(mStartAddress);
                            setDiaDiem(false);
                        }
                    });
    }

    @Override
    protected void initView() {
        super.initView();
        mLnChonXe = (LinearLayout) mRoot.findViewById(R.id.lnChonXe);
        mLnChonXe.setOnClickListener(this);
        mLnWarningTimeDatTruoc = (LinearLayout) mRoot.findViewById(R.id.lnWarningTimeDatTruoc);
        mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
        mRlDiemDon = (RelativeLayout) mRoot.findViewById(R.id.rlDiemDon);
        mRlDiemDen = (RelativeLayout) mRoot.findViewById(R.id.rlDiemDen);
        mbtnDatXe = (Button) mRoot.findViewById(R.id.btnDatxe);
        mImgDiemDen = (ImageView) mRoot.findViewById(R.id.imgDiemDen);
        mTvGoiNgayBayGio = (TextView) mRoot.findViewById(R.id.tvGoiNgayBayGio);
        mLnDate = (LinearLayout) mRoot.findViewById(R.id.lnDate);
        mTvDiaDiemDonName = (TextView) mRoot.findViewById(R.id.tvDiaDiemDon);
        mTvDiaDiemDenName = (TextView) mRoot.findViewById(R.id.tvDiaDiemDen);
        mTvDiaDiemDonAddress = (TextView) mRoot.findViewById(R.id.tvDiaDiemDonLanCan);
        mTvDiaDiemDenAddress = (TextView) mRoot.findViewById(R.id.tvDiaDenLanCan);
        mEdtMoTa = (EditText) mRoot.findViewById(R.id.edtMoTa);
        mTvKhuyenMai = (TextView) mRoot.findViewById(R.id.tvKhuyenMai);
        mImgChonDiemDen = (ImageView) mRoot.findViewById(R.id.imgChonDiemDen);
        mImgChonDiemDon = (ImageView) mRoot.findViewById(R.id.imgChonDiemDon);
        mImgTaxi = (ImageView) mRoot.findViewById(R.id.imgTaxi);
        mTvTaxi = (TextView) mRoot.findViewById(R.id.tvTaxi);
        mLnKhuyenMai = (LinearLayout) mRoot.findViewById(R.id.lnKhuyenMai);
        mRlDiemDon.setOnClickListener(this);
        mRlDiemDen.setOnClickListener(this);
        mbtnDatXe.setOnClickListener(this);
        mLnDate.setOnClickListener(this);
        mLnKhuyenMai.setOnClickListener(this);
        mImgCall = (ImageView) mRoot.findViewById(R.id.call_img);
        mImgCall.setOnClickListener(this);
    }

    private void showLayoutWarningDattruoc(String thoiGianDonXe) {
        Long dateCurrent = DatehepperUtil.convertDatetimeToLongDate(
                DatehepperUtil.getCurrentDate(DatehepperUtil.yyyyMMddHHmmss),
                DatehepperUtil.yyyyMMddHHmmss);
        Long dateSelect = DatehepperUtil.convertDatetimeToLongDate(
                thoiGianDonXe, DatehepperUtil.yyyyMMddHHmmss);
        if ((dateSelect < dateCurrent + (29 * 60000)) && mIsDatTruoc) {
            mLnWarningTimeDatTruoc.setVisibility(View.VISIBLE);
        } else
            mLnWarningTimeDatTruoc.setVisibility(View.GONE);
    }

    @Override
    protected void initControl() {
        super.initControl();
        if (Pasgo.getInstance().prefs.getLanguage().toLowerCase().equals(Constants.LANGUAGE_VIETNAM.toLowerCase())) {
            mImgCall.setImageResource(R.drawable.goi_dat_xe_vn);
        } else {
            mImgCall.setImageResource(R.drawable.goi_dat_xe_eng);
        }

        mLnWarningTimeDatTruoc.setVisibility(View.GONE);
        mIsDatXe = false;
        Utils.setBackground(mbtnDatXe,
                Utils.getDrawable(mActivity, R.drawable.btn_gray));
        mKey = KEY_10;
        mThoiGianDonXe = "";
        Utils.Log(TAG, "mThoiGianDonXe" + mThoiGianDonXe);
        String thoiGianDonXe = DatehepperUtil
                .getCurrentDate(DatehepperUtil.yyyyMMddHHmmss);
        showLayoutWarningDattruoc(thoiGianDonXe);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean isReceiverRegistered(BroadcastReceiver receiver) {
        boolean registered = receivers.contains(receiver);
        Log.i(TAG, "is receiver " + receiver
                + " registered? " + registered);
        return registered;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void onPause() {
        super.onPause();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key", mKey);
        outState.putBoolean("isDatXe", mIsDatXe);
        if(mEdtMoTa!=null)
            outState.putString(Constants.BUNDLE_MOTA, mEdtMoTa.getText().toString());
        if(mTvKhuyenMai!=null)
            outState.putString("khuyenmai", mTvKhuyenMai.getText().toString());
        outState.putString("thoigian", mThoiGianDonXe);
        if(mTvGoiNgayBayGio!=null)
            outState.putString("thoigiantext", mTvGoiNgayBayGio.getText()
                .toString());
        outState.putDouble(Constants.BUNDLE_START_LAT, mStartLat);
        outState.putDouble(Constants.BUNDLE_START_LNG, mStartLng);
        outState.putDouble(Constants.BUNDLE_KHACHHANG_LAT, mKhachHangLat);
        outState.putDouble(Constants.BUNDLE_KHACHHANG_LNG, mKhachhangLng);
        outState.putDouble(Constants.BUNDLE_KM, mKm);
        outState.putString(Constants.BUNDLE_GIA, mGia);
        outState.putString(Constants.BUNDLE_START_NAME, mStartName);
        outState.putString(Constants.BUNDLE_START_ADDRESS, mStartAddress);
        outState.putString(Constants.BUNDLE_END_ADDRESS, mDiaChi);
        outState.putDouble(Constants.BUNDLE_END_LAT, mEndLat);
        outState.putDouble(Constants.BUNDLE_END_LNG, mEndLng);
        outState.putString(Constants.BUNDLE_END_NAME, mEndName);
        outState.putString(Constants.BUNDLE_END_ADDRESS, mEndAddress);
        outState.putString(Constants.BUNDLE_KEY_ID, mChiNhanhDoiTacId);
        outState.putBoolean(Constants.BUNDLE_KEY_NOI_DUNG_KM, mNoiDungKm);
        outState.putBoolean(Constants.BUNDLE_KEY_CHI_NHANH_KM, mChiNhanhKm);
        outState.putInt(Constants.KEY_DI_XE_FREE, mDiXeFree);
        outState.putString(Constants.BUNDLE_KEY_TEN, mTen);
        outState.putString(Constants.BUNDLE_KEY_DIA_CHI, mDiaChi);
        outState.putString(Constants.BUNDLE_KEY_LINK_WEBSITE, mWebsite);
        outState.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, mNhomCNDoiTacId);
        outState.putBoolean(Constants.BUNDLE_KEY_DAT_TRUOC, mIsDatTruoc);
        outState.putBoolean(Constants.BUNDLE_KEY_FREE_IS_DEN, mIsKeyFreeIsDen);
        outState.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, mIsDiemTaiTroNguoiDung);
        outState.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, mIsDiemTaiTroNguoiDungDiTuDo);

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mKey = savedInstanceState.getInt("key");
        mIsDatXe = savedInstanceState.getBoolean("isDatXe");
        mStartLat = savedInstanceState.getDouble(Constants.BUNDLE_START_LAT);
        mStartLng = savedInstanceState.getDouble(Constants.BUNDLE_START_LNG);
        mKm = savedInstanceState.getDouble(Constants.BUNDLE_KM);
        mGia = savedInstanceState.getString(Constants.BUNDLE_GIA);
        mStartName = savedInstanceState.getString(Constants.BUNDLE_START_NAME);
        mStartAddress = savedInstanceState
                .getString(Constants.BUNDLE_START_ADDRESS);
        mEndName = savedInstanceState
                .getString(Constants.BUNDLE_END_NAME);
        mEndAddress = savedInstanceState
                .getString(Constants.BUNDLE_END_ADDRESS);
        mEdtMoTa.setText(savedInstanceState.getString(Constants.BUNDLE_MOTA));
        mThoiGianDonXe = savedInstanceState.getString("thoigian");
        String code = savedInstanceState.getString("khuyenmai");
        mTvKhuyenMai.setText(StringUtils.isEmpty(code) ? getString(R.string.dat_xe_ma_khuyen_mai) : code);
        mTvGoiNgayBayGio.setText(savedInstanceState.getString("thoigiantext"));
        mKhachHangLat = savedInstanceState
                .getDouble(Constants.BUNDLE_KHACHHANG_LAT);
        mKhachhangLng = savedInstanceState
                .getDouble(Constants.BUNDLE_KHACHHANG_LNG);

        if (StringUtils.isEmpty(mEndAddress))
            mEndAddress = "";
        mTvDiaDiemDenAddress.setText(mDiaChi);
        mEndLat = savedInstanceState.getDouble(Constants.BUNDLE_END_LAT);
        mEndLng = savedInstanceState.getDouble(Constants.BUNDLE_END_LNG);
        mChiNhanhDoiTacId = savedInstanceState.getString(Constants.BUNDLE_KEY_ID);
        mNoiDungKm = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_NOI_DUNG_KM);
        mChiNhanhKm = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_CHI_NHANH_KM);
        mDiXeFree = savedInstanceState.getInt(Constants.KEY_DI_XE_FREE);
        mTen = savedInstanceState.getString(Constants.BUNDLE_KEY_TEN);
        mDiaChi = savedInstanceState.getString(Constants.BUNDLE_KEY_DIA_CHI);
        mWebsite = savedInstanceState.getString(Constants.BUNDLE_KEY_LINK_WEBSITE);
        mNhomCNDoiTacId = savedInstanceState.getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID);
        mIsDatTruoc = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_DAT_TRUOC);
        mIsKeyFreeIsDen = savedInstanceState
                .getBoolean(Constants.BUNDLE_KEY_FREE_IS_DEN, true);
        mIsDiemTaiTroNguoiDung = savedInstanceState
                .getBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG);
        mIsDiemTaiTroNguoiDungDiTuDo = savedInstanceState
                .getBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
        setDiaDiem(true);
    }

    private void setDiaDiem(boolean isRestoreInstanceState) {
        if(getActivity()==null)
            return;
        if (mStartLat == 0 || mStartLng == 0
                || StringUtils.isEmpty(mStartAddress)) {
            mTvDiaDiemDonName.setText(StringUtils.getStringByResourse(mActivity,
                    R.string.dia_diem_hien_tai));
            mTvDiaDiemDonAddress.setVisibility(View.GONE);
        } else {
            mTvDiaDiemDonName.setText(mStartName);
            mTvDiaDiemDonAddress.setVisibility(View.VISIBLE);
            mTvDiaDiemDonAddress.setText(mStartAddress);
        }
        if (mEndLat == 0 || mEndLng == 0 || StringUtils.isEmpty(mEndAddress)) {
            mTvDiaDiemDenName.setVisibility(View.VISIBLE);
            mTvDiaDiemDenName.setText(getString(R.string.chon_dia_diem_den));
            mTvDiaDiemDenAddress.setText(StringUtils.getStringByResourse(mActivity,
                    R.string.dat_xe_nhan_chon_dia_diem));
            mImgDiemDen.setImageResource(R.drawable.ic_diemden_an);
        } else {
            mTvDiaDiemDenName.setVisibility(View.VISIBLE);
            mTvDiaDiemDenName.setText(mEndName);
            mTvDiaDiemDenAddress.setText(mEndAddress);
            mTvDiaDiemDenAddress.setVisibility(View.VISIBLE);
            mImgDiemDen.setImageResource(R.drawable.ic_diemden);
        }

        if ((mStartLat != 0 && mStartLng != 0 && mEndLat != 0 && mEndLng != 0)
                || !StringUtils.isEmpty(mStartAddress)
                || !mTvKhuyenMai.getText().toString().trim().equals(getString(R.string.dat_xe_ma_khuyen_mai))
                ) {
            mIsDatXe = true;
            Utils.setBackground(mbtnDatXe,
                    Utils.getDrawable(mActivity, R.drawable.btn_all));
        } else {
            mIsDatXe = false;
            Utils.setBackground(mbtnDatXe,
                    Utils.getDrawable(mActivity, R.drawable.btn_gray));
        }
        // set backGround diem don, den
        if (mIsDiemTaiTroNguoiDung && !mIsDiemTaiTroNguoiDungDiTuDo) {
            if (!mIsKeyFreeIsDen) {
                mImgChonDiemDon.setVisibility(View.GONE);
                Utils.setBackground(mRlDiemDon,
                        Utils.getDrawable(mActivity, R.drawable.bg_border_gray_datxe));
                mImgChonDiemDen.setVisibility(View.VISIBLE);
                Utils.setBackground(mRlDiemDen,
                        Utils.getDrawable(mActivity, R.drawable.selector_row_action_all));
            } else {
                mImgChonDiemDon.setVisibility(View.VISIBLE);
                mImgChonDiemDen.setVisibility(View.GONE);
                Utils.setBackground(mRlDiemDon,
                        Utils.getDrawable(mActivity, R.drawable.selector_row_action_all));
                Utils.setBackground(mRlDiemDen,
                        Utils.getDrawable(mActivity, R.drawable.bg_border_gray_datxe));
            }
        }
        if (StringUtils.isEmpty(mCode))
            Utils.setBackground(mLnKhuyenMai,
                    Utils.getDrawable(mActivity, R.drawable.selector_row_action_all));
        else
            Utils.setBackground(mLnKhuyenMai,
                    Utils.getDrawable(mActivity, R.drawable.bg_border_gray_datxe));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(TAG, "requestCode: " + requestCode + "resultCode: " + resultCode);
        if (requestCode == Constants.KEY_GO_TO_MAP && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                int complete = 0;
                if (bundle != null) {
                    complete = bundle.getInt(Constants.KEY_COMPLETE);
                }

                if (complete == Constants.KEY_INT_COMPLETE) {
                    Intent intent = new Intent();
                    if (mNoiDungKm) {
                        intent = new Intent(mActivity, DetailActivity.class);
                    } else if (mChiNhanhKm) {
                        intent = new Intent(mActivity,
                                ListDiaDiemChiNhanhActivity.class);
                    }
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(Constants.KEY_COMPLETE,
                            Constants.KEY_INT_COMPLETE);
                    intent.putExtras(bundle1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.setResult(RESULT_OK, intent);
                    finishPushDownInPushDownOut(mActivity);
                } else {
                    finishPushDownInPushDownOut(mActivity);
                }

                return;
            }
        }
        if (requestCode != resultCode)
            return;
        Bundle bundle;
        switch (requestCode) {
            case Constants.KEY_CHON_DIEM_DIEM_TAI_TRO:

                break;
            case Constants.kEY_BACK_FORM_DATXE:
                finishPushDownInPushDownOut(mActivity);
                break;

            case Constants.kEY_CHON_DIEM_DON:
                if (data == null) {
                    return;
                }
                bundle = data.getExtras();
                if (bundle != null) {
                    mStartLat = bundle.getDouble(Constants.BUNDLE_LAT);
                    mStartLng = bundle.getDouble(Constants.BUNDLE_LNG);
                    mStartName = bundle.getString(Constants.KEY_NAME);
                    mStartAddress = bundle.getString(Constants.KEY_VICINITY);
                    mKm = 0;
                    setDiaDiem(false);
                }
                PTService.mLocationMsgDriverMap = new HashMap<String, LocationMessageDriver>();
                Intent intent = new Intent(mActivity, PTService.class);
                intent.putExtras(bundle);
                mActivity.startService(intent);
                break;

            case Constants.kEY_CHON_DIEM_DEN:
                if (data == null) {
                    return;
                }
                bundle = data.getExtras();
                if (bundle != null) {
                    mEndLat = bundle.getDouble(Constants.BUNDLE_LAT);
                    mEndLng = bundle.getDouble(Constants.BUNDLE_LNG);
                    mEndName = bundle.getString(Constants.KEY_NAME);
                    mEndAddress = bundle.getString(Constants.KEY_VICINITY);
                    mChiNhanhDoiTacId = bundle.getString(Constants.BUNDLE_KEY_CHI_NHANH_DOI_TAC_ID_TU_SEARCH_PLACE);
                    mNhomCNDoiTacId = bundle.getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID);
                    mKm = 0;
                    setDiaDiem(false);
                }
                break;
            case Constants.kEY_CHON_LOAI_XE:
                if (data == null) {
                    return;
                }
                bundle = data.getExtras();
                if (bundle != null) {
                    mLoaiXeId = bundle.getInt(Constants.BUNDLE_DICH_VU_ID);
                    mLoaiXeName = bundle.getString(Constants.BUNDLE_LOAIXE_NAME);
                    mLoaiXeImage = bundle.getInt(Constants.BUNDLE_LOAIXE_IMAGE);
                    Utils.Log(TAG, "mLoaiXeId: " + mLoaiXeId + "mLoaiXeName: "
                            + mLoaiXeName);
                    setTaxiName(mLoaiXeName, mLoaiXeImage);
                    setDiaDiem(false);
                }
                break;
            default:
                break;
        }
    }

    private int mLoaiXeId = 0;
    private Integer mLoaiXeImage = 0;
    private String mLoaiXeName;

    private void setTaxiName(String name, Integer image) {
        if (mLoaiXeId == -1 || mLoaiXeImage == null || mLoaiXeImage == 0
                || StringUtils.isEmpty(mLoaiXeName))
            return;
        if (mLoaiXeId >= 0) {
            Utils.setBackground(mImgTaxi, mActivity.getResources()
                    .getDrawable(image));
            mTvTaxi.setText(name);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishPushDownInPushDownOut(mActivity);
                return true;
        }
        return true;
    }

    private boolean checkTimeBeforBooking() {
        boolean check = true;
        Long dateCurrent = DatehepperUtil.convertDatetimeToLongDate(
                DatehepperUtil.getCurrentDate(DatehepperUtil.yyyyMMddHHmmss),
                DatehepperUtil.yyyyMMddHHmmss);
        Long dateSelect = DatehepperUtil.convertDatetimeToLongDate(
                mThoiGianDonXe, DatehepperUtil.yyyyMMddHHmmss);
        if (dateSelect + 60000 < dateCurrent && !mIsDatTruoc) {
            ToastUtils.showToastWaring(mActivity, R.string.tb_thoi_gian_dat_xe);
            check = false;
        } else if ((dateSelect < dateCurrent + (29 * 60000)) && mIsDatTruoc) {
            check = false;
        } else
            check = true;
        showLayoutWarningDattruoc(mThoiGianDonXe);
        return check;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.rlDiemDon:
                if (mIsDiemTaiTroNguoiDung && !mIsDiemTaiTroNguoiDungDiTuDo && !mIsKeyFreeIsDen)
                    return;
                bundle = new Bundle();
                bundle.putBoolean(Constants.KEY_IS_DIEMDON, true);
                bundle.putDouble(Constants.BUNDLE_LAT, mKhachHangLat);
                bundle.putDouble(Constants.BUNDLE_LNG, mKhachhangLng);
                gotoActivityForResult(mActivity, SearchActivity.class, bundle,
                        Constants.kEY_CHON_DIEM_DON, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.rlDiemDen:
                if (mIsDiemTaiTroNguoiDung && !mIsDiemTaiTroNguoiDungDiTuDo && mIsKeyFreeIsDen)
                    return;
                bundle = new Bundle();
                bundle.putBoolean(Constants.KEY_IS_DIEMDON, false);
                bundle.putDouble(Constants.BUNDLE_LAT, mKhachHangLat);
                bundle.putDouble(Constants.BUNDLE_LNG, mKhachhangLng);
                gotoActivityForResult(mActivity, SearchActivity.class, bundle,
                        Constants.kEY_CHON_DIEM_DEN, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;

            case R.id.lnDate:
                showDialogDateTime();
                break;
            case R.id.btnDatxe:
                if ((mStartLat == 0 || mStartLng == 0 || mEndLat == 0
                        || mEndLng == 0
                        || StringUtils.isEmpty(mStartAddress)
                        || StringUtils.isEmpty(mEndAddress)

                ) || !mIsDatXe) {
                    ToastUtils.showToastWaring(mActivity, R.string.plz_nhap_diem_dau_cuoi);
                    return;
                }
                if (mTvGoiNgayBayGio.getText().toString().equals(getString(R.string.dat_xe_goi_ngay_bay_gio)) || StringUtils.isEmpty(mThoiGianDonXe))
                    mThoiGianDonXe = DatehepperUtil
                            .getCurrentDate(DatehepperUtil.yyyyMMddHHmmss);
                if (!checkTimeBeforBooking())
                    return;
                if (Constants.IS_OPERATOR) {
                    if (StringUtils.isEmpty(Pasgo.getInstance().sdtKhachHang)) {
                        showDialogInputSDT();
                    } else {
                        if (!mTvKhuyenMai.getText().toString().trim().equals(getString(R.string.dat_xe_ma_khuyen_mai)))
                            getGiamGiaByMa(mTvKhuyenMai.getText().toString());
                        else
                            datXe();
                    }
                } else {
                    if (!mIsDatXe)
                        return;
                    if (!mTvKhuyenMai.getText().toString().trim().equals(getString(R.string.dat_xe_ma_khuyen_mai)))
                        getGiamGiaByMa(mTvKhuyenMai.getText().toString());
                    else
                        datXe();
                }

                break;
            case R.id.lnKhuyenMai:
                if (StringUtils.isEmpty(mCode)) {
                    // tắt các activity nếu đi từ danh sách khuyến mại
                    Intent intent = new Intent();
                    if (mNoiDungKm) {
                        intent = new Intent(mActivity, DetailActivity.class);
                    } else if (mChiNhanhKm) {
                        intent = new Intent(mActivity,
                                ListDiaDiemChiNhanhActivity.class);
                    }
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(Constants.KEY_COMPLETE,
                            Constants.KEY_INT_COMPLETE);
                    intent.putExtras(bundle1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.setResult(RESULT_OK, intent);
                    // go to đặt xe
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt(Constants.BUNDLE_TAB_NUMBER, 1);
                    bundle2.putBoolean(Constants.BUNDLE_IS_GO_TO_DATXE, true);
                    bundle2.putInt(Constants.BUNDLE_TAB_NUMBER, 1);
                    Constants.ThePasgoTabNumber = 1;
                    gotoActivityForResult(mActivity, ThePasgoActivity.class, bundle2, Constants.KEY_CHON_DIEM_DIEM_TAI_TRO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                break;
            case R.id.lnChonXe:
                gotoActivityForResult(mActivity, ChonLoaiXeActivity.class,
                        Constants.kEY_CHON_LOAI_XE, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.call_img:
                checkPemissionCall();
                break;
            default:
                break;
        }
    }

    private void callClick() {
        final Dialog dialog;
        dialog = new Dialog(mActivity);
        dialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_call);
        TextView tvContentConfirm = (TextView) dialog.findViewById(R.id.tvContentConfirm);
        TextView tvTitle = (TextView) dialog
                .findViewById(R.id.tvTitle);

        tvTitle.setText(getString(R.string.datxe_call_thongbao));
        tvContentConfirm.setText(getString(R.string.call_center_support_booking));
        dialog.setCancelable(true);
        Button btnHuy;
        LinearLayout lnCall1;
        lnCall1 = (LinearLayout) dialog.findViewById(R.id.lncall1);
        lnCall1.setOnClickListener(v -> {
            dialog.dismiss();
            gotoPhoneCallPage(Constants.SDT_TONG_DAI);
        });
        btnHuy = (Button) dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(v -> dialog.dismiss());
        if (!mActivity.isFinishing())
            dialog.show();
    }

    protected void gotoPhoneCallPage(String telNumber) {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(i);
    }

    private void getGiamGiaByMa(String maKhuyenMai) {
        String url = WebServiceUtils.URL_GET_GIAM_GIA_BY_MA(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
            showDialog();
            try {
                jsonParams.put("ma", maKhuyenMai);
                jsonParams.put("sdt", Pasgo.getInstance().sdt);
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    dismissDialog();
                                    int maloi = ParserUtils.getIntValueResponse(json,
                                            "MaLoi");
                                    if (maloi == Constants.KEY_PROMOTION_IS_NOT_VALID &&!mActivity.isFinishing()) {
                                        DialogUtils.alert(mActivity,R.string.tb_ma_tai_tro_khong_hople);
                                    } else {
                                        JSONObject object = ParserUtils.getJsonObject(json, "Item");
                                        mPhanTramGiamGia = ParserUtils.getDoubleValue(object,"PhanTram");
                                        String noiDung =  ParserUtils.getStringValue(object,"NoiDung");
                                        String tieuDe =  ParserUtils.getStringValue(object,"TieuDe");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                mActivity);

                                        builder.setMessage(
                                                noiDung)
                                                .setTitle(tieuDe)
                                                .setCancelable(false)
                                                .setPositiveButton(
                                                        "OK",
                                                        (dialog, id) -> datXe());
                                        AlertDialog alert = builder.create();
                                        if(!mActivity.isFinishing())
                                            alert.show();

                                    }
                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                dismissDialog();
                            }

                        }, new Pasgo.PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dismissDialog();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                dismissDialog();
                ToastUtils.showToastError(mActivity,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }
    }

    private void showDialogInputSDT() {
        Intent intent = new Intent(mActivity, DialogInputSDT.class);
        startActivity(intent);
    }

    private void datXe() {
        if (mStartLat == 0 || mStartLng == 0 || mEndLat == 0 || mEndLng == 0
                ||StringUtils.isEmpty(mStartName)||StringUtils.isEmpty(mEndName)) {
            ToastUtils.showToastWaring(mActivity, R.string.plz_nhap_diem_dau_cuoi);
            return;
        }
        String maKhuyenMai =mTvKhuyenMai.getText().toString().equals(getString(R.string.dat_xe_ma_khuyen_mai))?"":mTvKhuyenMai.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_DICH_VU_ID, 0);
        bundle.putInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID,1);
        bundle.putInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID, mLoaiXeId);
        bundle.putDouble(Constants.BUNDLE_START_LAT, mStartLat);
        bundle.putDouble(Constants.BUNDLE_START_LNG, mStartLng);
        bundle.putString(Constants.BUNDLE_START_ADDRESS, mStartAddress);
        bundle.putString(Constants.BUNDLE_LOAIXE_NAME, mLoaiXeName);
        bundle.putString(Constants.BUNDLE_THOI_GIAN, mThoiGianDonXe);
        bundle.putDouble(Constants.BUNDLE_END_LAT, mEndLat);
        bundle.putDouble(Constants.BUNDLE_END_LNG, mEndLng);
        bundle.putDouble(Constants.BUNDLE_KHACHHANG_LAT, mKhachHangLat);
        bundle.putDouble(Constants.BUNDLE_KHACHHANG_LNG, mKhachhangLng);
        bundle.putString(Constants.BUNDLE_END_ADDRESS, mEndAddress);
        bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, mNhomCNDoiTacId);
        bundle.putDouble(Constants.BUNDLE_KM, mKm);
        bundle.putString(Constants.BUNDLE_GIA, mGia);
        bundle.putString(Constants.BUNDLE_MOTA, mEdtMoTa.getText().toString());
        bundle.putString(Constants.BUNDLE_KHUYEN_MAI, maKhuyenMai);
        mDriverNumber = PTService.mLocationMsgDriverMap.size();
        bundle.putInt(Constants.BUNDLE_NUMBER_DRIVER, mDriverNumber);
        bundle.putInt(Constants.KEY_DI_XE_FREE, mDiXeFree);
        bundle.putInt(Constants.KEY_GO_TO, Constants.KEY_GO_TO_MAP);
        bundle.putDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA, mPhanTramGiamGia);
        gotoActivityForResult(mActivity, XacNhanDatXeActivity.class, bundle,
                Constants.KEY_GO_TO_MAP, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft(mActivity);
    }

    private void showDialogDateTime() {
        if(mDialogTime ==null){
            mDialogTime = new Dialog(mActivity);
            mDialogTime.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            mDialogTime.setContentView(R.layout.layout_popup_time);
        }
        final DatePicker dp = (DatePicker) mDialogTime
                .findViewById(R.id.datePicker1);
        final TimePicker tp = (TimePicker) mDialogTime
                .findViewById(R.id.timePicker1);
        Button btnOk, btnCancel, btnDateOK;
        final Button btnShowDate, btnDateCancel;
        final LinearLayout ln10;
        final LinearLayout ln20;
        final LinearLayout ln30, lnDateTime, lnDateS;
        btnOk = (Button) mDialogTime.findViewById(R.id.btnOk);
        btnCancel = (Button) mDialogTime.findViewById(R.id.btnCancel);
        btnDateOK = (Button) mDialogTime.findViewById(R.id.btnDateOk);
        btnDateCancel = (Button) mDialogTime.findViewById(R.id.btnDateCancel);
        btnShowDate = (Button) mDialogTime.findViewById(R.id.btnShowDate);
        ln10 = (LinearLayout) mDialogTime.findViewById(R.id.ln10);
        ln20 = (LinearLayout) mDialogTime.findViewById(R.id.ln20);
        ln30 = (LinearLayout) mDialogTime.findViewById(R.id.ln30);
        lnDateTime = (LinearLayout) mDialogTime.findViewById(R.id.lnDateTime);
        lnDateS = (LinearLayout) mDialogTime.findViewById(R.id.lnDateS);
        tp.setIs24HourView(true);
        tp.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        btnShowDate.setText(DatehepperUtil
                .getCurrentDate(DatehepperUtil.ddMMyyyy));
        lnDateS.setVisibility(View.GONE);
        lnDateTime.setVisibility(View.VISIBLE);
        try {
            Method m = dp.getClass().getMethod("setCalendarViewShown",
                    boolean.class);
            m.invoke(dp, false);
        } catch (Exception e) {
        }
        final Long dateCurrent = DatehepperUtil.convertDatetimeToLongDate(
                DatehepperUtil.getCurrentDate(DatehepperUtil.yyyyMMddHHmmss),
                DatehepperUtil.yyyyMMddHHmmss);
        btnOk.setOnClickListener(v -> {
            String date = DatehepperUtil.ConvertFormatDateToFormatDate(
                    DatehepperUtil.ddMMyyyy, btnShowDate.getText()
                            .toString(), DatehepperUtil.yyyyMMdd);
            int hour = tp.getCurrentHour();
            int minute = tp.getCurrentMinute();
            String thoiGian = String.format("%s %s:%s:%s", date,
                    DatehepperUtil.formatDate(hour),
                    DatehepperUtil.formatDate(minute), "00");
            Long dateSelect = DatehepperUtil.convertDatetimeToLongDate(
                    thoiGian, DatehepperUtil.yyyyMMddHHmmss);
            Utils.Log(TAG, "dateSelect " + dateSelect + "dateCurrent "
                    + dateCurrent);
            if (dateSelect + 60000 < dateCurrent) {
                ToastUtils
                        .showToast(mActivity, R.string.tb_thoi_gian_dat_xe);
            } else {
                mThoiGianDonXe = thoiGian;
                mTvGoiNgayBayGio.setText(mThoiGianDonXe);
                mDialogTime.dismiss();
                showLayoutWarningDattruoc(mThoiGianDonXe);
            }
            Utils.Log(TAG, "mThoiGianDonXe" + mThoiGianDonXe);

        });
        btnCancel.setOnClickListener(v -> mDialogTime.dismiss());
        ln10.setOnClickListener(v -> {
            mKey = KEY_10;
            mThoiGianDonXe = DatehepperUtil.addMinute(10);
            Utils.Log(TAG, "mThoiGianDonXe" + mThoiGianDonXe);
            mTvGoiNgayBayGio.setText(mThoiGianDonXe);
            mDialogTime.dismiss();
            showLayoutWarningDattruoc(mThoiGianDonXe);
        });
        ln20.setOnClickListener(v -> {
            mKey = KEY_20;
            mThoiGianDonXe = DatehepperUtil.addMinute(30);
            Utils.Log(TAG, "mThoiGianDonXe" + mThoiGianDonXe);
            mTvGoiNgayBayGio.setText(mThoiGianDonXe);
            mDialogTime.dismiss();
            showLayoutWarningDattruoc(mThoiGianDonXe);
        });
        ln30.setOnClickListener(v -> {
            mKey = KEY_30;
            mThoiGianDonXe = DatehepperUtil.addMinute(60);
            Utils.Log(TAG, "mThoiGianDonXe" + mThoiGianDonXe);
            mTvGoiNgayBayGio.setText(mThoiGianDonXe);
            mDialogTime.dismiss();
            showLayoutWarningDattruoc(mThoiGianDonXe);
        });
        btnShowDate.setOnClickListener(v -> {
            lnDateS.setVisibility(View.VISIBLE);
            lnDateTime.setVisibility(View.GONE);
        });
        btnDateOK.setOnClickListener(v -> {
            int day = dp.getDayOfMonth();
            int month = dp.getMonth() + 1;
            int year = dp.getYear();
            String thoiGian = String.format("%s/%s/%s",
                    DatehepperUtil.formatDate(day),
                    DatehepperUtil.formatDate(month), year);
            btnShowDate.setText(thoiGian);
            lnDateS.setVisibility(View.GONE);
            lnDateTime.setVisibility(View.VISIBLE);
        });
        btnDateCancel.setOnClickListener(v -> {
            lnDateS.setVisibility(View.GONE);
            lnDateTime.setVisibility(View.VISIBLE);
        });
        if(mDialogTime!=null && !mDialogTime.isShowing()&& !mActivity.isFinishing())
            mDialogTime.show();
    }

    @Override
    public void onNetworkChanged() {
        if (mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()
                && !mIsErrorChannel)
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        else
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
    }

    //region permission Call
    protected void checkPemissionCall()
    {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            callClick();
        } else
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE_PHONE);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchCallData();
                } else {
                    ToastUtils.showToastWaring(mActivity,"Permission was denied");
                }
                return;
            }
        }
    }
    private void fetchCallData() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            callClick();
        }
    }
    //endregion
}