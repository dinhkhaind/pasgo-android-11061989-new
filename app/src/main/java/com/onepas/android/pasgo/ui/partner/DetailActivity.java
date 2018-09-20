package com.onepas.android.pasgo.ui.partner;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.PTIcon;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhBangGia;
import com.onepas.android.pasgo.models.AnhList;
import com.onepas.android.pasgo.models.BinhLuan;
import com.onepas.android.pasgo.models.ChiTietDoiTac;
import com.onepas.android.pasgo.models.DoiTacLienQuan;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.calldriver.DatXeActivity;
import com.onepas.android.pasgo.ui.chat.ChatActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserve.ReserveDetailActivity;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.ExpandableHeightListView;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;
import com.onepas.android.pasgo.widgets.MySupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DetailActivity extends BaseAppCompatActivity implements
        OnClickListener, OnMapReadyCallback {
    private final String TAG = "t";
    private String mDoiTacKMID;
    private String mChiNhanhDoiTacId;
    private String mTenDoiTacKM="";
    protected String mGhiChu;
    private boolean mIsMap = false;
    private boolean mIsLichSu = false;
    private boolean mIsListDiaDiemKM = false;
    private boolean mIsDiemTaiTroNguoiDung = false;
    private boolean mIsDiemTaiTroNguoiDungTuDo = false;
    private boolean mIsDanhSachDiemDen = false;
    private String mDiaChi;
    private String mWebsite;
    private String mNhomCNDoiTacId;
    private String mMoTa;
    private double mKinhDo;
    private double mViDo;
    private int mNhomKMId;
    private int mTinhId;
    private String mGhiChuFromLS;
    private int mDiXeFree;
    private Dialog dialog;
    private Dialog dialogCheckInExist;
    //private ChiTietKhuyenMai chiTietKhuyenMai;
    private ChiTietDoiTac mChiTietDoiTac;
    private ArrayList<DoiTacLienQuan> mDoiTacLienQuans;
    private TextView mTvComment;
    private RatingBar mRatingChatLuong;
    private ImageView mImgYeuThich;
    private ViewPager myPager;
    private TextView mTvChiDuong;
    private TextView mTvAddress;
    private LinearLayout mLnUuDai;
    private LinearLayout mLnGioiThieu;
    private LinearLayout mLnDiemNoiBat;
    private TextView mTvRating;
    private TextView mTvGioPhucVu, mTvLoaiHinh, mTvKhoangGia;
    private RecyclerView mRcMenu;
    private RecyclerView mRcDoiTacQuanTam;
    private ExpandableHeightListView mLvBinhLuan;
    private RecyclerView mRcImage;
    private TextView mTvImageAll;
    private TextView mTvMenuAll;
    private RelativeLayout mRlUuDaiDocTiep, mRlChiTietDocTiep;
    private FloatingActionMenu mMenuBotton;
    private NestedScrollView mNestedScroll;
    private LinearLayout mLnDoiTacLienQuan;

    private boolean mIscheckDestroy = false;
    private boolean mIsDatTruoc = false;
    private String mMaKhuyenMai = "";
    private String mDatXeId = "";
    private LinearLayout mLnCall;
    private LinearLayout mLnChatNgay;
    private LinearLayout mLnReserve;
    private String mGiamGiaId = "";
    private String mGiamGiaMa = "";
    private String mGiamGia = "";
    private String mUuDai ="";
    private String mGioiThieu="";
    private String mDiemNoiBat ="";
    private GoogleMap mGoogleMap;
    private static final int PERMISSION_REQUEST_CODE_PHONE =1;
    private HashMap<String, Integer> mImageReason;
    private Marker mMarkerCurrent;
    private TextView mTvTitle;

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, mGiamGiaId);
        outState.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, mGiamGiaMa);
        outState.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, mGiamGia);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGiamGiaId = savedInstanceState.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID);
        mGiamGiaMa = savedInstanceState.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA);
        mGiamGia = savedInstanceState.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_detail);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mProgressToolbar = (ProgressBar) mToolbar.findViewById(R.id.toolbar_progress_bar);
        initView();

        if (savedInstanceState != null)
            mIscheckDestroy = true;
        mProgressToolbar.setVisibility(ProgressBar.GONE);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mDiXeFree = extra.getInt(Constants.KEY_DI_XE_FREE);
            mChiNhanhDoiTacId = extra.getString(Constants.BUNDLE_KEY_ID);
            mDoiTacKMID = extra.getString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID);
            mTenDoiTacKM = extra.getString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM);
            mTinhId = extra.getInt(Constants.KEY_TEN_TINH_ID);
            mNhomKMId = extra.getInt(Constants.BUNDLE_KEY_NHOM_KM_ID);
            mDiaChi = extra.getString(Constants.BUNDLE_KEY_DIA_CHI);
            mWebsite = extra.getString(Constants.BUNDLE_KEY_LINK_WEBSITE);
            mNhomCNDoiTacId = extra.getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID);
            mMoTa = extra.getString(Constants.BUNDLE_KEY_MO_TA);
            mKinhDo = extra.getDouble(Constants.BUNDLE_KEY_KINH_DO);
            mViDo = extra.getDouble(Constants.BUNDLE_KEY_VI_DO);
            mIsMap = extra.getBoolean(Constants.BUNDLE_KEY_MAP);
            mIsListDiaDiemKM = extra.getBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM);
            mIsLichSu = extra.getBoolean(Constants.BUNDLE_KEY_IS_LICH_SU);
            mGhiChuFromLS = extra.getString(Constants.BUNDLE_KEY_GHI_CHU);
            mIsDatTruoc = extra.getBoolean(Constants.BUNDLE_KEY_DAT_TRUOC);
            mIsDiemTaiTroNguoiDung = extra.getBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG);
            mIsDiemTaiTroNguoiDungTuDo = extra.getBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO);
            mIsDanhSachDiemDen = extra.getBoolean(Constants.BUNDLE_KEY_DANH_SACH_DIEM_DEN, false);
            mMaKhuyenMai = extra.getString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI);
            mGiamGiaId = extra.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, "");
            mGiamGiaMa = extra.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, "");
            mGiamGia = extra.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, "");
            TextView tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
            Utils.setTextViewHtml(tvRestaurantName,mTenDoiTacKM);
            mImageReason = PTIcon.getInstanceIconReason();
            MySupportMapFragment fragment = (MySupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            fragment.getMapAsync(this);
            getChiTietDoiTac(mDoiTacKMID);
            getDoiTacLienQuan(mDoiTacKMID);
            TextView tvDiemDoiBatTitle = (TextView)findViewById(R.id.diem_noi_bat_title_tv);
            tvDiemDoiBatTitle.setText(getString(R.string.diem_noi_bat_title));
            mTvTitle = (TextView)mToolbar.findViewById(R.id.tvTitleMenu);
            Utils.setTextViewHtml(mTvTitle,mTenDoiTacKM);
            mTvTitle.setVisibility(View.GONE);
        }

        initControl();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finishToRightToLeft();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void initView() {
        super.initView();

        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mTvComment = (TextView) findViewById(R.id.tvCommentAll);
        mImgYeuThich = (ImageView)findViewById(R.id.imgYeuThich);
        mLnCall = (LinearLayout) findViewById(R.id.lnCall);
        mLnChatNgay = (LinearLayout) findViewById(R.id.chat_ngay_ln);
        mLnReserve = (LinearLayout) findViewById(R.id.reserve_ln);
        mRatingChatLuong = (RatingBar) findViewById(R.id.ratingChatLuong);
        myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
        mTvChiDuong = (TextView) findViewById(R.id.tvChiDuong);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mLnUuDai = (LinearLayout) findViewById(R.id.uu_dai_ln);
        mLnGioiThieu = (LinearLayout) findViewById(R.id.gioi_thieu_ln);
        mLnDiemNoiBat = (LinearLayout) findViewById(R.id.dien_noi_bat_ln);
        mTvRating = (TextView)findViewById(R.id.rating_tv);
        mTvGioPhucVu = (TextView)findViewById(R.id.gio_phuc_vu_tv);
        mTvLoaiHinh = (TextView)findViewById(R.id.loai_hinh_tv);
        mTvKhoangGia = (TextView)findViewById(R.id.khoang_gia_tv);
        mRlUuDaiDocTiep = (RelativeLayout)findViewById(R.id.uu_dai_doc_tiep_rl);
        mRlChiTietDocTiep = (RelativeLayout)findViewById(R.id.gioi_thieu_chi_tiet_doc_tiep_rl);
        mRcMenu = (RecyclerView)findViewById(R.id.menu_lv);
        mRcDoiTacQuanTam = (RecyclerView)findViewById(R.id.doi_tac_lien_quan_lv);
        mLvBinhLuan = (ExpandableHeightListView) findViewById(R.id.comment_lv);
        mLvBinhLuan.setExpanded(true);
        mRcImage = (RecyclerView)findViewById(R.id.image_lv);
        mTvImageAll = (TextView)findViewById(R.id.image_all_tv);
        mTvMenuAll = (TextView)findViewById(R.id.menu_all_tv);
        mMenuBotton = (FloatingActionMenu)findViewById(R.id.menu_red);
        mMenuBotton.setVisibility(View.VISIBLE);
        mMenuBotton.setClosedOnTouchOutside(true);
        mNestedScroll = (NestedScrollView)findViewById(R.id.nested_nt);
        mLnDoiTacLienQuan = (LinearLayout)findViewById(R.id.doi_tac_lien_quan_ln);
        mLnDoiTacLienQuan.setVisibility(View.GONE);

        mRlUuDaiDocTiep.setOnClickListener(this);
        mRlChiTietDocTiep.setOnClickListener(this);
        findViewById(R.id.view_all_map_tv).setOnClickListener(this);
        findViewById(R.id.tvCommentAll).setOnClickListener(this);
        findViewById(R.id.comment_all_ln).setOnClickListener(this);
        findViewById(R.id.fabChiaSe).setOnClickListener(this);
        findViewById(R.id.fabMenuNhaHang).setOnClickListener(this);
        findViewById(R.id.fabBanDo).setOnClickListener(this);
        findViewById(R.id.fabAnh).setOnClickListener(this);
        findViewById(R.id.fabDatXe).setOnClickListener(this);
        mTvImageAll.setOnClickListener(this);
        mTvMenuAll.setOnClickListener(this);
        mLnCall.setOnClickListener(this);
        mLnChatNgay.setOnClickListener(this);
        mLnReserve.setOnClickListener(this);
        mImgYeuThich.setOnClickListener(this);
        findViewById(R.id.derection_rl).setOnClickListener(v->gotoDirection());
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Utils.Log(Pasgo.TAG,"Facebook onSuccess");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        mNestedScroll.fullScroll(View.FOCUS_UP);
    }

    @Override
    protected void initControl() {
        super.initControl();
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mTvTitle.getVisibility() != View.VISIBLE)
                        mTvTitle.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if(isShow) {
                    if (mTvTitle.getVisibility() != View.GONE)
                        mTvTitle.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });
        //Pasgo.disConnectSocket();
    }

    private void callClick() {
        String bookingTitle = String.format(getString(R.string.call_title_thongbao), "<b>" + mTenDoiTacKM + "</b>");
        final Dialog dialog;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_call);
        TextView tvTitle = (TextView) dialog
                .findViewById(R.id.tvTitle);
        Utils.setTextViewHtml(tvTitle,bookingTitle);
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
        if (!isFinishing())
            dialog.show();
    }

    protected void gotoPhoneCallPage(String telNumber) {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(i);
    }

    private void checkInClick() {
        if (mChiTietDoiTac == null || mChiTietDoiTac.getLoaiHopDong()>1)
            return;
        if (mChiTietDoiTac.isDaCheckin()) {
            if(dialogCheckInExist==null)
                dialogCheckInExist = DialogUtils.showYesNoDialog(mContext,String.format(getString(R.string.check_in_exist),mTenDoiTacKM), R.string.view_detail, R.string.huy
                        , v -> {
                            dialogCheckInExist.dismiss();
                        }, v -> {
                            gotoActivity(mContext, ReservedHistoryActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            dialogCheckInExist.dismiss();
                        });
            if(dialogCheckInExist!=null && !dialogCheckInExist.isShowing())
                dialogCheckInExist.show();
            return;
        }
        if(StringUtils.isEmpty(mNhomCNDoiTacId))
            return;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, mGiamGiaId);
        bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, mGiamGiaMa);
        bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, mGiamGia);
        bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, mNhomCNDoiTacId);
        bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, mTenDoiTacKM);
        bundle.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, mChiTietDoiTac.getTrangThai());
        bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, mChiTietDoiTac.getDiaChi());
        gotoActivityForResult(mActivity, ReserveDetailActivity.class, bundle, Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onNetworkChanged();
    }

    private void bookingCilck() {
        if (isFinishing()) return;
        String bookingTitle = String.format(getString(R.string.booking_title_confirm), StringUtils.fromHtml(mTenDoiTacKM).toString());
        final Dialog dialogBooking;
        dialogBooking = new Dialog(mContext);
        dialogBooking.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        dialogBooking.setContentView(R.layout.layout_popup_dang_ki);
        TextView tvHoTen = (TextView) dialogBooking
                .findViewById(R.id.tvHoTen);
        tvHoTen.setText(bookingTitle);
        dialogBooking.setCancelable(true);
        Button btnDaGap, btnChuaGap;
        btnDaGap = (Button) dialogBooking.findViewById(R.id.btnDaGap);
        btnDaGap.setText(mContext.getString(R.string.continue_login));
        btnDaGap.setOnClickListener(v -> {
            dialogBooking.dismiss();
            booking();
        });
        btnChuaGap = (Button) dialogBooking.findViewById(R.id.btnChuaGap);
        btnChuaGap.setOnClickListener(v -> dialogBooking.dismiss());

        dialogBooking.show();
    }

    private void booking() {
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            if (mIsMap) {
                gotoDatXe(mChiNhanhDoiTacId, mTenDoiTacKM, mDiaChi,
                        mWebsite, mNhomCNDoiTacId, mGhiChu, mMoTa,
                        mKinhDo, mViDo, mIsDatTruoc);
            } else if (mIsLichSu) {
                gotoDatXe(mChiNhanhDoiTacId, mTenDoiTacKM, mDiaChi,
                        mWebsite, mNhomCNDoiTacId, mGhiChuFromLS,
                        mMoTa, mKinhDo, mViDo, mIsDatTruoc);
            } else if (mIsDanhSachDiemDen) {
                gotoDatXe(mChiNhanhDoiTacId, mTenDoiTacKM, mDiaChi,
                        mWebsite, mNhomCNDoiTacId, mGhiChu, mMoTa,
                        mKinhDo, mViDo, mIsDatTruoc);
            } else {
                // mChiNhanhDoiTacId chính là DoiTacId trong trường hợp này ""
                // đến từ 2 màn hình ListDiaDiem và ReserveActivity2

                getChiNhanhDoiTac(mChiNhanhDoiTacId, mDoiTacKMID, mTinhId);
            }
        }
    }

    @Override
    public void onNetworkChanged() {

        if (mLnErrorConnectNetwork != null) {

            if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable())
                mLnErrorConnectNetwork.setVisibility(View.GONE);
            else
                mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
    }

    protected void gotoDatXe(String chiNhanhDoiTacId, String tenDoiTacKM,
                             String diaChi, String website, String nhomCNDoiTacId, String ghiChu,
                             String moTa, double kinhDo, double viDo, boolean datTruoc) {

        if (chiNhanhDoiTacId == null || chiNhanhDoiTacId.isEmpty() || tenDoiTacKM == null
                || tenDoiTacKM.isEmpty() || nhomCNDoiTacId == null
                || nhomCNDoiTacId.isEmpty()) {
            return;
        }
        Intent intent = new Intent(mContext, DatXeActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_NOI_DUNG_KM, true);
        intent.putExtra(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
        intent.putExtra(Constants.BUNDLE_KEY_TEN, tenDoiTacKM);
        intent.putExtra(Constants.BUNDLE_KEY_DIA_CHI, diaChi);
        intent.putExtra(Constants.BUNDLE_KEY_LINK_WEBSITE, website);
        intent.putExtra(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCNDoiTacId);
        intent.putExtra(Constants.BUNDLE_KEY_GHI_CHU, ghiChu);
        intent.putExtra(Constants.BUNDLE_KEY_MO_TA, moTa);
        intent.putExtra(Constants.BUNDLE_KEY_KINH_DO, kinhDo);
        intent.putExtra(Constants.BUNDLE_KEY_VI_DO, viDo);
        intent.putExtra(Constants.KEY_DI_XE_FREE, mDiXeFree);
        intent.putExtra(Constants.BUNDLE_KEY_DAT_TRUOC, mIsDatTruoc);
        intent.putExtra(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, mIsDiemTaiTroNguoiDung);
        intent.putExtra(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, mIsDiemTaiTroNguoiDungTuDo);
        intent.putExtra(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, mMaKhuyenMai);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, Constants.KEY_GO_TO_MAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(Pasgo.TAG, "NoiDungKm requestCode" + requestCode);
        Utils.Log(Pasgo.TAG, "NoiDungKm resultCode" + resultCode);
        Utils.Log(Pasgo.TAG, "NoiDungKm data" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.KEY_GO_TO_MAP:
                    // load lai du lieu neu dat cho theo Doi tac km
                    if (data == null) {
                        setResult(RESULT_OK);
                        finishToRightToLeft();
                        return;
                    }
                    Bundle bundle = data.getExtras();
                    int complete = 0;
                    if (bundle != null) {
                        complete = bundle.getInt(Constants.KEY_COMPLETE);
                    }

                    if (complete == Constants.KEY_INT_COMPLETE) {
                        Intent intent = null;
                        if (mIsLichSu) {
                            intent = new Intent(mActivity, HistoryActivity.class);
                        }
                        if (mIsListDiaDiemKM) {
                            intent = new Intent(mActivity, DestinationActivity.class);
                        }
                        if (intent != null) {
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt(Constants.KEY_COMPLETE,
                                    Constants.KEY_INT_COMPLETE);
                            intent.putExtras(bundle1);
                            setResult(Constants.MENU_TO_LIST_C1, intent);

                            finishToRightToLeft();
                        } else {
                            setResult(RESULT_OK);
                            finishToRightToLeft();
                        }
                    } else {
                        setResult(RESULT_OK);
                        finishToRightToLeft();
                    }
                    break;
                //dat cho - diem den
                case Constants.KEY_BACK_BY_DAT_CHO:
                    setResult(RESULT_OK);
                    finishToRightToLeft();
                    break;

                default:
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
        if (requestCode == Constants.MENU_TO_LIST_C1) {
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            int complete = 0;
            if (bundle != null) {
                complete = bundle.getInt(Constants.KEY_COMPLETE);
            }

            if (complete == Constants.KEY_INT_COMPLETE) {
                Intent intent = null;
                if (mIsLichSu) {
                    intent = new Intent(mActivity, HistoryActivity.class);
                }
                if (mIsListDiaDiemKM) {
                    intent = new Intent(mActivity, DestinationActivity.class);
                }
                if (intent != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(Constants.KEY_COMPLETE,
                            Constants.KEY_INT_COMPLETE);
                    intent.putExtras(bundle1);
                    setResult(Constants.MENU_TO_LIST_C1, intent);

                    finishToRightToLeft();
                } else {
                    setResult(RESULT_OK);
                    finishToRightToLeft();
                }

            } else {
                finishToRightToLeft();
            }
        }
        if (requestCode == Constants.kEY_COMMENT_KHUYEM_MAI) {
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            if (mIscheckDestroy || mChiTietDoiTac == null)
                return;
            if (bundle != null) {
                int num = bundle.getInt(Constants.BUNDLE_COMMENT_ADD);
                //khaivd phần này sau khi bình luận quay lại set lại sô number bình luận
                //mChiTietDoiTac.setBinhLuans(new BinhLuan());
                setTextAlterCheckOrComment();
            }

        }
    }

    protected void getChiNhanhDoiTac(String idDoiTac, String doiTacKMID,
                                     int tinhId) {
        String url = WebServiceUtils.URL_GetChiNhanhDoiTacV1(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("tinhId", tinhId);
            jsonParams.put("doiTacId", idDoiTac);
            jsonParams.put("doiTacKhuyenMaiId", doiTacKMID);
            jsonParams.put("nhomKhuyenMaiId", mNhomKMId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("Items");
                            Utils.Log("response ", "response  khuyen mai"
                                    + array);
                            JSONObject jsonObject = array.getJSONObject(0);
                            mTenDoiTacKM = ParserUtils.getStringValue(jsonObject, "Ten");
                            String chiNhanhId = ParserUtils.getStringValue(jsonObject, "Id");
                            mDiaChi = ParserUtils.getStringValue(jsonObject, "DiaChi");
                            mNhomCNDoiTacId = ParserUtils.getStringValue(jsonObject, "NhomCnDoiTacId");
                            mMoTa = ParserUtils.getStringValue(jsonObject, "MoTa");
                            mWebsite = ParserUtils.getStringValue(jsonObject, "Website");
                            mKinhDo = ParserUtils.getDoubleValue(jsonObject, "KinhDo");
                            mViDo = ParserUtils.getDoubleValue(jsonObject, "ViDo");
                            mIsDatTruoc = ParserUtils.getBooleanValue(jsonObject, "DatTruoc");
                            gotoDatXe(chiNhanhId, mTenDoiTacKM, mDiaChi, mWebsite,
                                    mNhomCNDoiTacId, mGhiChu, mMoTa, mKinhDo,
                                    mViDo, mIsDatTruoc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressToolbar.setVisibility(ProgressBar.GONE);
                        }
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showAlertMangYeu(2);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }

    private void getChiTietDoiTac(String doiTacKMID) {
        String url = WebServiceUtils
                .URL_GET_CHI_TIET_DOI_TAC(Pasgo.getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        double viDo=0.0, kinhDo=0.0;
        if (Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
            viDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("viDo", viDo);
            jsonParams.put("kinhDo", kinhDo);
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("doiTacKhuyenMaiId", doiTacKMID);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mChiTietDoiTac = ParserUtils.getChiTietDoiTac(response);
                        mNhomCNDoiTacId = mChiTietDoiTac.getNhomCnDoiTacId();
                        //mChiNhanhDoiTacId = mChiTietDoiTac.ge
                        mNhomKMId = mChiTietDoiTac.getNhomKhuyenMaiId();
                        mViDo = mChiTietDoiTac.getViDo();
                        mKinhDo = mChiTietDoiTac.getKinhDo();
                        mDiaChi = mChiTietDoiTac.getDiaChi();
                        handleUpdateUI.sendEmptyMessage(0);
                        new Handler().postDelayed(() -> viewMap(),1000);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showAlertMangYeu(1);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }
    private void getDoiTacLienQuan(String doiTacKMID) {
        String url = WebServiceUtils
                .URL_GET_DOI_TAC_LIEN_QUAN(Pasgo.getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        double viDo=0.0, kinhDo=0.0;
        if (Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
            viDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("viDo", viDo);
            jsonParams.put("kinhDo", kinhDo);
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("doiTacKhuyenMaiId", doiTacKMID);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDoiTacLienQuans = ParserUtils.getDoiTacLienQuans(response);
                        if(mDoiTacLienQuans.size()>0) {
                            viewDoiTacLienQuan(mDoiTacLienQuans);
                            mLnDoiTacLienQuan.setVisibility(View.VISIBLE);
                        }else
                        {
                            mLnDoiTacLienQuan.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showAlertMangYeu(1);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }
    Handler handleUpdateUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                {
                    mProgressToolbar.setVisibility(ProgressBar.GONE);
                    setTextUI();
                    ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(mChiTietDoiTac.getAnhSlides(), mActivity, new ImageViewPagerAdapter.ImageViewPageListener() {
                        @Override
                        public void imageClick(int position) {
                            gotoImageView(position);
                        }
                    });
                    myPager.setAdapter(adapter);
                    myPager.setCurrentItem(0);
                    myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int position) {
                            Utils.Log(TAG, "position : -->" + position);
                        }

                        @Override
                        public void onPageScrolled(int arg0, float arg1, int arg2) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onPageScrollStateChanged(int arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
                    break;
                }
            }
        }
    };
    private void setTextUI() {
        if (mChiTietDoiTac != null) {
            mTvGioPhucVu.setText(mChiTietDoiTac.getGioPhucVu());
            mTvLoaiHinh.setText(mChiTietDoiTac.getChuyenMon());
            mTvKhoangGia.setText(mChiTietDoiTac.getKhoangGia());
            Utils.setTextViewHtml(mTvChiDuong, mChiTietDoiTac.getDiaChi());
            Utils.setTextViewHtml(mTvAddress, mChiTietDoiTac.getDiaChi());
            mTvRating.setText(mChiTietDoiTac.getDanhGia()+"");
            // loại hợp đồng
            if(mChiTietDoiTac.getLoaiHopDong()==1)
                mLnReserve.setBackgroundResource(R.drawable.selector_bg_transparent);
            else
                mLnReserve.setBackgroundResource(R.color.transparent_selected);
            //
            int numberComment = 0;
            if(mChiTietDoiTac.getBinhLuans().size()>0)
                numberComment = mChiTietDoiTac.getBinhLuans().get(0).getTongSo();
            mTvComment.setText(String.format(getString(R.string.xem_tat_ca_number), numberComment+""));
            int numberImage =0;
            if(mChiTietDoiTac.getAnhLists().size()>0)
                numberImage = mChiTietDoiTac.getAnhLists().get(0).getTongSo();
            mTvImageAll.setText(String.format(getString(R.string.xem_tat_ca_number), numberImage+""));
            int numberMenu = 0;
            if(mChiTietDoiTac.getAnhBangGias().size()>0)
                numberMenu = mChiTietDoiTac.getAnhBangGias().get(0).getTongSo();
            mTvMenuAll.setText(String.format(getString(R.string.xem_tat_ca_number), numberMenu+""));

            if (mChiTietDoiTac.getDaYeuThich() == 0)
                mImgYeuThich.setImageResource(R.drawable.like_trang);
            else
                mImgYeuThich.setImageResource(R.drawable.like_do);
            if(mChiTietDoiTac.getAnhSlides().size()==0)
                myPager.setVisibility(View.GONE);
            else
                myPager.setVisibility(View.VISIBLE);
            String datTrung = mChiTietDoiTac.getDacTrung().trim();
            String chiTiet = mChiTietDoiTac.getMoTaGioiThieu().trim();
            String uuDai = mChiTietDoiTac.getMoTaUuDai().trim();
            if(uuDai.contains("@@@@"))
                mRlUuDaiDocTiep.setVisibility(View.VISIBLE);
            else
                mRlUuDaiDocTiep.setVisibility(View.GONE);
            if(chiTiet.contains("@@@@"))
                mRlChiTietDocTiep.setVisibility(View.VISIBLE);
            else
                mRlChiTietDocTiep.setVisibility(View.GONE);
            setWebViewUuDai(uuDai.replace("@@@@",""));
            setWebViewDacTrung(datTrung);
            setWebViewGioiThieu(chiTiet.replace("@@@@",""));
            mRatingChatLuong.setRating((float) mChiTietDoiTac.getChatLuong());
            //
            if(mChiTietDoiTac.getAnhBangGias().size()>0)
                viewMenu(mChiTietDoiTac.getAnhBangGias());
            if(mChiTietDoiTac.getAnhLists().size()>0)
                viewAnhList(mChiTietDoiTac.getAnhLists());
            if(mChiTietDoiTac.getBinhLuans().size()>0)
                viewBinhLuan(mChiTietDoiTac.getBinhLuans());
            mNestedScroll.fullScroll(View.FOCUS_UP);

        }
    }

    private void setTextAlterCheckOrComment() {
        if (mChiTietDoiTac != null) {
            int numberComment = 0;
            if(mChiTietDoiTac.getBinhLuans().size()>0)
                numberComment = mChiTietDoiTac.getBinhLuans().get(0).getTongSo();
            mTvComment.setText(String.format(getString(R.string.xem_tat_ca_number), numberComment+""));
        }
    }

    private void setWebViewUuDai(String noiDung) {
        mUuDai = noiDung.replace("line-height","");
        mLnUuDai.removeAllViews();
        mHandlerUpdateWebview.sendMessageDelayed(mHandlerUpdateWebview.obtainMessage(0), 200);
    }
    private void setWebViewGioiThieu(String noiDung) {
        mGioiThieu = noiDung.replace("line-height","");
        mLnGioiThieu.removeAllViews();
        mHandlerUpdateWebview.sendMessageDelayed(mHandlerUpdateWebview.obtainMessage(1), 200);
    }
    private void setWebViewDacTrung(String noiDung) {
        if(noiDung.contains("</table>"))
            mDiemNoiBat = noiDung.replace("line-height","");
        else
            mDiemNoiBat = noiDung;
        mLnDiemNoiBat.removeAllViews();
        mHandlerUpdateWebview.sendMessageDelayed(mHandlerUpdateWebview.obtainMessage(2), 200);
    }
    private Handler mHandlerUpdateWebview = new Handler(){
        public void handleMessage(Message msg){
            WebView webView =new WebView(mContext);
            webView.setBackgroundColor(Color.parseColor("#ffffff"));
            WebSettings webSettings = webView.getSettings();
            webSettings.setBuiltInZoomControls(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDefaultFontSize(7);
            String customHtml="";
            webView.setWebViewClient(new WebViewClient() {

            });
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            // no copy
            webView.setOnLongClickListener(v -> true);
            webView.setLongClickable(false);
            // disable touch
            //webView.setOnTouchListener((v, event) -> true);
            switch (msg.what)
            {
                case 0:
                    customHtml = formatHtml(mUuDai.trim());
                    webView.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8",
                            null);
                    mLnUuDai.addView(webView, 0);
                    break;
                case 1:
                    customHtml = formatHtml(mGioiThieu.trim());
                    webView.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8",
                            null);
                    mLnGioiThieu.addView(webView, 0);
                    break;
                case 2:
                    customHtml = formatHtmlTomTat(mDiemNoiBat.trim());
                    webView.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8",
                            null);
                    mLnDiemNoiBat.addView(webView, 0);
                    break;
            }


        }
    };
    private String formatHtmlTomTat(String noiDung)
    {
        return "<html><head><style> "
                + "body{text-align:justify;}"
                + "img{width:100%;}"
                + "iframe {width:100%;}"
                + "table{text-align:left;text-align:right; font-size:13px; width: 100%;border-collapse: collapse; }"
                + "</style></head> "
                + "<body>"
                + noiDung
                + "</body></html>";
    }
    private String formatHtml(String noiDung)
    {
        return "<html><head><style> "
                + "body{text-align:justify;}"
                + "img{width:100%;}"
                + "iframe {width:100%;}"
                + "table{text-align:left;text-align:right; font-size:13px; width: 100%;border-collapse: collapse; }"
                + "h2{margin:0;line-height:22px;font-size:18px;font-weight:700;color:#d02028}"
                + "h3{margin:10px 0;font-size:11pt;font-weight:700}"
                + "h4{margin:16px 0;font-size:10pt;font-weight:700}"
                + "h5{text-align:center;font-size:9pt;font-weight:400;line-height:22px;margin:2px 0px 3px 0px}"
                + "</style></head> "
                + "<body>"
                + "<p>" + "<font face="
                + "sans-serif" + " size= 6>" + noiDung + "</font>"
                + "</body></html>";
    }
    private void showAlertMangYeu(final int i) {
        if (mIsDestroy || getBaseContext() == null)
            return;
        if (!isFinishing()) {
            if (dialog == null) {
                dialog = new Dialog(DetailActivity.this);
                dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_mang_yeu);
            }
            Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);// ket_noi_mang_yeu_and_connecttoserver
            Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
            TextView tv = (TextView) dialog.findViewById(R.id.content);
            tv.setText(getString(R.string.ket_noi_mang_yeu_and_connecttoserver));
            dialogBtThuLai.setOnClickListener(v -> {
                switch (i) {
                    case 1:
                        getChiTietDoiTac(mDoiTacKMID);
                        break;
                    case 2:
                        getChiNhanhDoiTac(mChiNhanhDoiTacId, mDoiTacKMID, mTinhId);
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
            if (dialog != null && !dialog.isShowing() && !isFinishing()) {
                dialog.show();
            }
        }
    }


    private void setYeuThichDoiTacKhuyenMai() {
        if(Pasgo.getInstance().prefs.getIsTrial())
            return;
        String url = WebServiceUtils
                .URL_SET_YEU_THICH_DTKM(Pasgo
                        .getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("doiTacKhuyenMaiId", mDoiTacKMID);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("thich", mChiTietDoiTac.getDaYeuThich() == 0 ? 1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject objItem = ParserUtils.getJsonObject(
                                response, "Item");
                        mChiTietDoiTac.setDaYeuThich(ParserUtils.getIntValue(
                                objItem, "DaThich"));
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        if (mChiTietDoiTac.getDaYeuThich() == 0)
                            mImgYeuThich.setImageResource(R.drawable.like_trang);
                        else
                            mImgYeuThich.setImageResource(R.drawable.like_do);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showAlertMangYeu(1);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishToRightToLeft();
                return true;
        }
        return true;
    }

    @Override
    public void onUpdateMapAfterUserInterection() {
    }
    private void gotoChatNgay()
    {
        /*Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_LINK,Constants.URL_CHAT_NGAY);
        bundle.putString(Constants.BUNDLE_KEY_ACTIONBAR_NAME,getString(R.string.support));
        gotoActivityForResult(mContext, WebviewChatNgayActivity.class, bundle,
                Constants.kEY_COMMENT_KHUYEM_MAI);
        ourLeftInLeft();*/

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(Constants.BUNDLE_KEY_LOAI_TIN_NHAN,2);
                intent.putExtra(Constants.BUNDLE_KEY_CHAT_URL,"App-Android-"+mTenDoiTacKM);
                intent.putExtra(Constants.BUNDLE_KEY_GO_TO_DETAIL,true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                ourLeftInLeft();
            }
        }, 500);

    }
    private void gotoComment()
    {
        if (mChiTietDoiTac == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_DOI_TAC_KHUYEN_MAI,
                mDoiTacKMID);
        gotoActivityForResult(mContext, CommentActivity.class, bundle,
                Constants.kEY_COMMENT_KHUYEM_MAI);
        ourLeftInLeft();
    }
    private void gotoDirection()
    {
        if (mChiTietDoiTac == null)
            return;
        Bundle bundle1 = new Bundle();
        bundle1.putDouble(Constants.BUNDLE_KEY_KINH_DO, mKinhDo);
        bundle1.putDouble(Constants.BUNDLE_KEY_VI_DO, mViDo);
        bundle1.putString(Constants.BUNDLE_KEY_MA_NHOM_KHUYEN_MAI, mChiTietDoiTac.getMaNhomKhuyenMai());
        gotoActivity(mContext, DirectionActivity.class, bundle1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
    }
    private void gotoImageList()
    {
        if (mChiTietDoiTac == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, mChiTietDoiTac.getId());
        gotoActivity(mContext, ImageListActivity.class, bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
    }
    private void gotoMenuList()
    {
        if (mChiTietDoiTac == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, mChiTietDoiTac.getId());
        gotoActivity(mContext, MenuActivity.class, bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.img1:
                gotoImageView(0);
                break;
            case R.id.lnCall:
                checkPemissionCall();
                break;
            case R.id.chat_ngay_ln:
                gotoChatNgay();
                break;
            case R.id.comment_all_ln:
                gotoComment();
                break;
            case R.id.tvCommentAll:
                gotoComment();
                break;
            case R.id.image_all_tv:
                gotoImageList();
                break;
            case R.id.reserve_ln:
                checkInClick();
                break;
            case R.id.imgYeuThich:
                if (mChiTietDoiTac == null)
                    return;
                if(!isCheckLogin())
                {
                    Intent broadcastRegister = new Intent();
                    broadcastRegister.setAction(Constants.BROADCAST_ACTION_REQUEST_LOGIN);
                    sendBroadcast(broadcastRegister);
                    return;
                }
                setYeuThichDoiTacKhuyenMai();
                break;

            case R.id.menu_all_tv:
                gotoMenuList();
                break;
            case R.id.rlPriceList:

                break;
            case R.id.view_all_map_tv:
                gotoDirection();
                break;
            case R.id.uu_dai_doc_tiep_rl:
                if (mChiTietDoiTac == null)
                    return;
                Bundle bundleUuDai = new Bundle();
                bundleUuDai.putInt(Constants.KEY_TYPE_WEBVIEW, WebviewChiTietActivity.UU_DAI);
                bundleUuDai.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, mChiTietDoiTac.getId());
                gotoActivity(mContext, WebviewChiTietActivity.class, bundleUuDai,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft();
                break;
            case R.id.gioi_thieu_chi_tiet_doc_tiep_rl:
                if (mChiTietDoiTac == null)
                    return;
                Bundle bundleGioThieu = new Bundle();
                bundleGioThieu.putInt(Constants.KEY_TYPE_WEBVIEW, WebviewChiTietActivity.GIOI_THIEU);
                bundleGioThieu.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, mChiTietDoiTac.getId());
                gotoActivity(mContext, WebviewChiTietActivity.class, bundleGioThieu,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft();
                break;
            case R.id.fabChiaSe:
                if (mChiTietDoiTac == null)
                    return;
                String name = String.format(getString(R.string.noi_dung_khuyen_mai_share), StringUtils.fromHtml(mTenDoiTacKM).toString());
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.putExtra(Intent.EXTRA_SUBJECT, "");
                share.putExtra(Intent.EXTRA_TEXT, name);
                startActivity(Intent.createChooser(share, mChiTietDoiTac.getUrlContent()));
                mMenuBotton.toggle(false);
                break;
            case R.id.fabMenuNhaHang:
                gotoMenuList();
                mMenuBotton.toggle(false);
                break;
            case R.id.fabBanDo:
                gotoDirection();
                mMenuBotton.toggle(false);
                break;
            case R.id.fabAnh:
                gotoImageList();
                mMenuBotton.toggle(false);
                break;
            case R.id.fabDatXe:
                bookingCilck();
                mMenuBotton.toggle(false);
                break;

            default:
                break;
        }
    }

    private void gotoImageView(int number) {
        Bundle bundle = new Bundle();
        String img = "";
        for (int i = 0; i < mChiTietDoiTac.getAnhSlides().size(); i++) {
            if (i < mChiTietDoiTac.getAnhSlides().size() - 1)
                img += mChiTietDoiTac.getAnhSlides().get(i).getAnh() + ",";
            else
                img += mChiTietDoiTac.getAnhSlides().get(i).getAnh();
        }
        bundle.putInt(Constants.BUNDLE_IMAGE_VIEW_POSITION, number);
        bundle.putString(Constants.BUNDLE_IMAGE_VIEW, img);
        gotoActivity(mContext, ImageViewActivity.class, bundle,
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
    }
    private void viewMenu(final  ArrayList<AnhBangGia> lists)
    {
        DetailMenuAdapter mAdapter;
        mAdapter = new DetailMenuAdapter(mActivity, lists, position -> {
            if (mChiTietDoiTac == null)
                return;
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, mChiTietDoiTac.getId());
            bundle.putInt(Constants.IMAGE_LIST_NUMBER, position);
            gotoActivity(mContext, MenuDetailActivity.class, bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        });
        mRcMenu.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mRcMenu.setAdapter(mAdapter);
    }
    private void viewAnhList(final  ArrayList<AnhList> lists)
    {
        DetailImageAdapter mAdapter;
        mAdapter = new DetailImageAdapter(mActivity, lists, position -> {
            if (mChiTietDoiTac == null)
                return;
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, mChiTietDoiTac.getId());
            bundle.putInt(Constants.IMAGE_LIST_NUMBER, position);
            gotoActivity(mContext, ImageListDetailActivity.class, bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        });
        mRcImage.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mRcImage.setAdapter(mAdapter);
    }
    private void viewBinhLuan(final  ArrayList<BinhLuan> lists)
    {
        BinhLuanAdapter goiYAdapter = new BinhLuanAdapter(mActivity, lists);
        mLvBinhLuan.setAdapter(goiYAdapter);
    }
    private void viewDoiTacLienQuan(final  ArrayList<DoiTacLienQuan> lists)
    {
        DetailDiemQuanTamAdapter mAdapter;
        mAdapter = new DetailDiemQuanTamAdapter(mActivity, lists,
                new DetailDiemQuanTamAdapter.DetailDiemQuanTamListener() {
                    @Override
                    public void checkIn(int position) {
                        DoiTacLienQuan item = null;
                        if(lists.size()>=position)
                        {
                            item = lists.get(position);
                            if(item.getLoaiHopDong()==1)
                                initCheckIn(item);
                        }
                    }

                    @Override
                    public void detail(int position) {
                        DoiTacLienQuan item = null;
                        if(lists.size()>=position)
                        {
                            item = lists.get(position);
                        }
                        if(item!=null) {
                            lvClickItem(item, mTinhId, "", false, false);
                        }
                    }

                    @Override
                    public void thePasgo(int position) {
                        DoiTacLienQuan item = null;
                        if(lists.size()>=position)
                        {
                            item = lists.get(position);
                        }
                        if(item!=null)
                        {
                            Bundle bundle =new Bundle();
                            bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,item.getDoiTacKhuyenMaiId());
                            gotoActivityForResult(mContext, ThePasgoActivity.class, bundle, Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ourLeftInLeft();
                        }
                    }
                });

        mRcDoiTacQuanTam.setLayoutManager(new StaggeredGridLayoutManager(lists.size(), StaggeredGridLayoutManager.HORIZONTAL));
        mRcDoiTacQuanTam.setAdapter(mAdapter);
    }
    private boolean mIsClickCheckIn;
    public synchronized void initCheckIn(DoiTacLienQuan item )
    {
        if(!mIsClickCheckIn)
        {
            mIsClickCheckIn = true;
            Bundle bundle =new Bundle();
            bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID,item.getNhomCnDoiTacId());
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM,item.getTen());
            bundle.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, item.getTrangThai());
            bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaChi());
            gotoActivityForResult(mActivity, ReserveDetailActivity.class, bundle, Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
            mIsClickCheckIn = false;
        }
    }
    protected void lvClickItem(DoiTacLienQuan item, int tinhId, String maKhuyenMai, boolean isDiemTaiTroNguoiDung, boolean isDiemTaiTroNguoiDungTuDo) {
        if (item != null) {
            String doiTacKMID = item.getDoiTacKhuyenMaiId();
            String title = item.getTen();
            String chiNhanhDoiTacId = item.getId();
            String nhomCnDoiTac = item.getNhomCnDoiTacId();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_DI_XE_FREE, Constants.KEY_INT_XE_FREE);
            bundle.putInt(Constants.KEY_TEN_TINH_ID, tinhId);
            bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
            bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, doiTacKMID);
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
            bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
            bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, item.getNhomKhuyenMaiId());
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getDiaChi());
            bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCnDoiTac);
            bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, maKhuyenMai);
            bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, isDiemTaiTroNguoiDung);
            bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, isDiemTaiTroNguoiDungTuDo);
            gotoActivityForResult(mActivity, DetailActivity.class, bundle,
                    Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        }
    }
    @Override
    public void onStartMoveScreen() {

    }

    //region permission Call
    protected void checkPemissionCall()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            callClick();
        } else {
            requestPermission(Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_CODE_PHONE, getApplicationContext(), DetailActivity.this);
        }
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
                    ToastUtils.showToastWaring(mContext,"Permission was denied");
                }
                return;
            }
        }
    }
    private void fetchCallData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            callClick();
        }
    }

    // map chỉ đường
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.setOnMapClickListener(arg0 -> gotoDirection());
        mGoogleMap.setOnMarkerClickListener(marker -> {
            gotoDirection();
            return true;
        });
        viewMap();
    }
    private void viewMap()
    {
        if(mGoogleMap!=null && mChiTietDoiTac !=null)
        {
            handleUpdateMap.sendEmptyMessage(0);
            handleUpdateMap.sendEmptyMessage(1);
        }
    }
    Handler handleUpdateMap = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                {
                    final LatLng latLng = new LatLng(mViDo, mKinhDo);
                    if (mGoogleMap != null) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(15).build();
                        mGoogleMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(cameraPosition),
                                5, new GoogleMap.CancelableCallback() {

                                    @Override
                                    public void onFinish() {

                                        LatLngBounds bounds = mGoogleMap
                                                .getProjection().getVisibleRegion().latLngBounds;
                                        if (bounds != null) {
                                        }
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                    }
                    break;
                }
                case 1: {
                    setMarkerCurrent();
                    break;
                }
            }
        }
    };
    private void setMarkerCurrent() {
        if(!mImageReason.containsKey(mChiTietDoiTac.getMaNhomKhuyenMai())) return;
        if (getBaseContext() == null || mGoogleMap == null
                || mImageReason == null)
            return;
        int drawable = mImageReason.get(mChiTietDoiTac.getMaNhomKhuyenMai());
        LatLng latLng = new LatLng(mViDo,mKinhDo);
        if (mMarkerCurrent == null)
            mMarkerCurrent = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_map_detail))
                    .snippet("-2"));
        else
            mMarkerCurrent.setPosition(latLng);
    }
}