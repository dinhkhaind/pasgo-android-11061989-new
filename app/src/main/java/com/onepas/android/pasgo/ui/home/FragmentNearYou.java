package com.onepas.android.pasgo.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.onepas.android.pasgo.BuildConfig;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.PTIcon;
import com.onepas.android.pasgo.PTService;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.CategoryHome;
import com.onepas.android.pasgo.models.DiaChi;
import com.onepas.android.pasgo.models.DiaChiHangXe;
import com.onepas.android.pasgo.models.FilterCategoryItems;
import com.onepas.android.pasgo.models.FilterParent;
import com.onepas.android.pasgo.models.FilterView;
import com.onepas.android.pasgo.models.ItemDiaChiChiNhanh;
import com.onepas.android.pasgo.models.ItemNhomKM;
import com.onepas.android.pasgo.models.LocationMessageDriver;
import com.onepas.android.pasgo.models.PTLocationInfo;
import com.onepas.android.pasgo.models.TagModel;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.RateApp;
import com.onepas.android.pasgo.ui.TagAdapter;
import com.onepas.android.pasgo.ui.account.AccountManagerActivity;
//import com.onepas.android.pasgo.ui.calldriver.TheoDoiTaiXeActivity;
import com.onepas.android.pasgo.ui.calleddrivers.CalledDriverActivity;
import com.onepas.android.pasgo.ui.category.CategoryActivity;
import com.onepas.android.pasgo.ui.guid.GuidActivity;
import com.onepas.android.pasgo.ui.partner.DestinationFilterAdapter;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;
import com.onepas.android.pasgo.ui.reserve.ReserveDetailActivity;
import com.onepas.android.pasgo.ui.reserve.TagDialogAdapter;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.ui.search.SearchActivity;
import com.onepas.android.pasgo.ui.setting.SettingActivity;
import com.onepas.android.pasgo.ui.share.ShareActivity;
import com.onepas.android.pasgo.ui.successfultrips.SuccessfulTripsActivity;
import com.onepas.android.pasgo.ui.termsandpolicies.TermsAndPoliciesActivity;
import com.onepas.android.pasgo.util.mapnavigator.MapUtil;
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

public class FragmentNearYou extends BaseFragment implements View.OnClickListener, Animation.AnimationListener, OnMapReadyCallback
        , GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {
    private final static String  TAG = "FragmentNearYou";
    private View mRoot;
    private GoogleMap mGoogleMap;
    private HashMap<String, Marker> mMarkerTaxi;// taxi
    private HashMap<String, Marker> mMarkersMienPhi;// mien phi
    private Marker mMarkerCurrent;
    private Marker mMarkerSearch;
    private Button mBtnMyLocation;
    private LocationMessageDriver mLocationMessageDriver;
    private Animation mAnimSlideRotate;
    protected LinearLayout mLnCheckGPS;
    private TextView mTvThongBaoGPS;

    private HashMap<String, Integer> mImageReason;
    private String mData = "", mReasonId = "";
    private CustomInfoWindowAdapter mCustomInfoWindowAdapter;
    private HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> mMapDiaChiChiNhanh;
    private HashMap<String, DiaChiHangXe> mMapDiaChiHangXe;
    private ArrayList<ItemNhomKM> mReasonGroup;
    private HashMap<String, ItemDiaChiChiNhanh> mItemDiaChiChiNhanhs;
    private int mPosition;
    private ReasonGroupTask mReasonGroupTask;
    //private double mLat = 0.0, mLng = 0.0;
    private double mLatSearch = 0.0, mLngSearch = 0.0;
    private String mStartName, mStartAddress;
    private boolean mOpenPopUpGPS = false;
    private boolean mGoToFromChangeLanguage = false;
    private final int KEY_MINUTES_LOADMAP_CURRENT_LOCATION = 2000;
    private final int KEY_MAP_ZOOM = 15;
    private final int KEY_MAP_ZOOM_SMALL = 12;

    public StaggeredGridLayoutManager mTagStaggeredGridLayoutManager;
    private AppBarLayout mAppbarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private LinearLayout mLnLayoutData;
    private Button mBtnArrowTop, mBtnArrowDown;
    private LinearLayout mLnTop;

    private RecyclerView mRcKetQua;
    private DiemDenHomeAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsLoadAl;
    private int mPageSize = 50;
    private int mPageNumber = 1;
    private int mTagId = 0;
    private int mCategoryId = 0;
    protected boolean flag_loading;
    private ArrayList<CategoryHome> mCategoryHomes = new ArrayList<>();
    private ArrayList<TagModel> mTagAlls = new ArrayList<>();
    private ArrayList<TagModel> mTags = new ArrayList<>();
    private static final int KEY_SET_TAG = 10;
    private static final int KEY_SET_TAG_BY_CATEGORY = 2;
    private static final int KEY_SET_CATEGORY = 1;
    private static final int KEY_SET_MARKET_BYCATEGORY = 2;
    private ArrayList<DiemDenModel> mListDiemDen = new ArrayList<>();
    private RelativeLayout mFooterView;
    private int mfirstVisibleItem = 0;
    private Marker mMarkerClick;

    private final int NUMBER_ITEM_TAG = 5;
    private TagAdapter mTagAdapter;
    private RecyclerView mLvTag;

    private final int KEY_LOAD_DATA_DIEM_DEN = 1;
    private final int KEY_NO_DATA_DIEM_DEN = 2;
    private final int KEY_DISCONNECT_DIEM_DEN = 3;
    private static final int KEY_DATA_DIEM_DEN = 4;
    private TagDialogAdapter mTagDialogAdapter;
    private Dialog mDialogTag;

    private Dialog mDialogDanhMuc;
    private CategoryNearYouAdapter mCategoryNearYouAdapter;
    private TextView mTvCategory;
    private LinearLayout mLnCategory;
    private TextView mTvSearchPlace;
    // search place
    private static final String RESULTS = "results";
    private static final String TYPES = "types";
    private static final String POLITICAL = "political";
    private static final String ADDRESS_COMPONENTS = "address_components";
    private static final String LONG_NAME = "long_name";
    private static final String STREET_NUMBER = "street_number";// S??? nh????
    private static final String ROUTE = "route";// T??n ???????ng
    private static final String SUBLOCALITY = "sublocality";// Ph?????ng x??
    private static final String LOCALITY = "locality";// Th??nh ph???
    private static final String ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2";// Qu???n
    private static final String ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";// T???nh
    private static final String COUNTRY = "country";// N?????c
    // filter
    private DestinationFilterAdapter mDiaDiemFilterAdapter;
    private AlertDialog mDialogFilter;
    // danh sach filter lay tren server ve
    private ArrayList<FilterParent> mFiltersList;
    private int mFilterParentNhaHangLoaiHinhId = -1000;
    // danh sach đầu tiên dùng cho phần popup popup: sau khi chọn nhóm khuyến mại sẽ lấy
    private ArrayList<FilterView> mFilterViews = new ArrayList<>();
    //Chỉ dùng để hiện thị popup sau đó ko dùng
    private ArrayList<FilterView> mFilterViewsTam = new ArrayList<>();
    // Clone: để hiện thị view, khi nào nhấn "OK" thì mFilterViews= mFilterViewsClone
    private ArrayList<FilterView> mFilterViewsClone = new ArrayList<FilterView>();
    public String mSFilter = "";//
    private String mFilterFromSerer;
    private final int KEY_FILTER = 1;
    private boolean mIsFilter;
    private static final int KEY_LOAD_FILTER_BY_NHOM_KM_ID = 2;
    //
    private boolean mIsSearchLocation = false;
    private LinearLayout mLnSearch;
    private int mTinhOld = Pasgo.getInstance().mTinhId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRoot == null) {
            PTService.mLocationMsgDriverMap = new HashMap<>();
            mRoot = inflater.inflate(R.layout.fragment_home_near_you, container, false);
            mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
            mToolbar.setTitle("");
            ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            initializeMenu();
            initView();
            getBundle();
            initControl();
            if (savedInstanceState != null) {
                mData = savedInstanceState.getString(Constants.KEY_DATA_JSON, "");
                mReasonId = savedInstanceState.getString(Constants.KEY_MARKER_ID, "0");
                mGoToFromChangeLanguage = savedInstanceState.getBoolean(Constants.BUNDLE_GO_TO_FROM_CHANG_LANGUAGE);
                mOpenPopUpGPS = savedInstanceState.getBoolean("opengps");
            }
            RateApp.app_launched(mActivity);
            if (!mOpenPopUpGPS && !mGoToFromChangeLanguage)
                if (!NetworkUtils.getInstance(mActivity).isGpsEnabled()) {
                    showDialogGPS();
                    mOpenPopUpGPS = true;
                }
            onNetworkChanged();
            mTinhOld = Pasgo.getInstance().mTinhId;
        }
        if(mTinhOld != Pasgo.getInstance().mTinhId || mCategoryHomes.size() == 0)
        {
            mTinhOld = Pasgo.getInstance().mTinhId;
            mAdapter = null;
            mTagAdapter = null;
            mTagDialogAdapter = null;
            mTagAlls.clear();
            mTags.clear();
            mListDiemDen.clear();
            mLvTag.removeAllViews();
            mPageNumber = 1;
            mIsLoadAl = true;
            mTagId = -1;
            getDoiTacGanBan();
        }

        return mRoot;
    }

    private void initializeMenu() {
        mToolbar.setNavigationIcon(R.drawable.icon_menu);
        HomeActivity activity = (HomeActivity) getActivity();
        activity.mNavigationDrawerFragment.setUp(R.id.drawer_fragment, activity.mDrawerLayout, mToolbar);
        activity.mNavigationDrawerFragment.menuDatXe.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            activity.goToDatXe(activity.KEY_SERVICE_TAXI);
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

    }

    @Override
    protected void initView() {
        super.initView();
        mTvThongBaoGPS = (TextView) mRoot.findViewById(R.id.tvThongBaoGPS);
        mTvThongBaoGPS.setSingleLine(true);
        mTvThongBaoGPS.setSelected(true);
        mLnCheckGPS = (LinearLayout) mRoot.findViewById(R.id.lnCheckGPS);
        mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
        mBtnMyLocation = (Button) mRoot.findViewById(R.id.btnMyLocation);
        mTvNetworkError = (TextView) mRoot.findViewById(R.id.tvTitleStateNetwork);
        mAnimSlideRotate = AnimationUtils.loadAnimation(mActivity,
                R.anim.together);
        mAppbarLayout = (AppBarLayout) mRoot.findViewById(R.id.appbar);
        mCoordinatorLayout = (CoordinatorLayout) mRoot.findViewById(R.id.main_content);
        mAnimSlideRotate.setAnimationListener(this);
        mLnLayoutData = (LinearLayout) mRoot.findViewById(R.id.nested_ln);
        mBtnArrowTop = (Button) mRoot.findViewById(R.id.arrow_top_btn);
        mBtnArrowDown = (Button) mRoot.findViewById(R.id.arrow_down_btn);
        mLnTop = (LinearLayout) mRoot.findViewById(R.id.top_ln);
        mFooterView = (RelativeLayout) mRoot.findViewById(R.id.load_more_footer);
        mLvTag = (RecyclerView) mRoot.findViewById(R.id.tag_lv);
        mTagStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        mLvTag.setLayoutManager(mTagStaggeredGridLayoutManager);
        mRcKetQua = (RecyclerView) mRoot.findViewById(R.id.my_recycler_view);
        mTvCategory = (TextView) mToolbar.findViewById(R.id.category_tv);
        mLnCategory = (LinearLayout) mToolbar.findViewById(R.id.category_ln);
        mToolbar.findViewById(R.id.filter_rl).setOnClickListener(v -> {
            showDialogFilter();
        });
        mBtnMyLocation.setOnClickListener(this);
        mTvSearchPlace = (TextView) mRoot.findViewById(R.id.serach_place_tv);
        mStartAddress = StringUtils.getStringByResourse(mActivity,
                R.string.dia_diem_hien_tai);
        handleUpdateUIMap.sendEmptyMessage(2);
        mLnSearch =(LinearLayout) mRoot.findViewById(R.id.layoutSearch);
        mLnSearch.setOnClickListener(v -> {
            double lat =0.0, lng =0.0;
            if (Pasgo.getInstance().prefs.getLatLocationRecent() != null
                    && !"".equals(Pasgo.getInstance().prefs
                    .getLatLocationRecent())
                    && mPageNumber == 1) {
                lat = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLatLocationRecent());
                lng = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLngLocationRecent());
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.KEY_IS_DIEMDON, true);
            bundle.putDouble(Constants.BUNDLE_LAT, lat);
            bundle.putDouble(Constants.BUNDLE_LNG, lng);
            bundle.putBoolean(Constants.BUNDLE_SEARCH_GO_TO_HOME,true);
            gotoActivityForResult(mActivity, SearchActivity.class, bundle,
                    Constants.kEY_CHON_DIEM_DON, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        });
    }

    private void getBundle() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            mGoToFromChangeLanguage = bundle.getBoolean(Constants.BUNDLE_GO_TO_FROM_CHANG_LANGUAGE);
        }
    }

    @Override
    protected void initControl() {
        super.initControl();
        mImageReason = new HashMap<>();
        mMapDiaChiChiNhanh = new HashMap<>();
        mReasonGroup = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRcKetQua.setLayoutManager(mLayoutManager);
        mReasonId = "";
        mData = null;
        checkNetworkGPS();
        showAllMarker();
        mBtnArrowTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });
        mBtnArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideList(false);
            }
        });
        showList();

        mRcKetQua.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                mfirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                Utils.Log(Pasgo.TAG, "visibleItemCount" + visibleItemCount);
                Utils.Log(Pasgo.TAG, "totalItemCount" + totalItemCount);
                Utils.Log(Pasgo.TAG, "mfirstVisibleItem" + mfirstVisibleItem);
                if (!flag_loading && NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
                    if (mfirstVisibleItem + visibleItemCount == totalItemCount
                            && totalItemCount > mPageSize * (mPageNumber - 1) && totalItemCount % mPageSize == 0) {

                        mFooterView.setVisibility(View.VISIBLE);
                        flag_loading = true;
                        mPageNumber += 1;
                        getDoiTacGanBan();
                    }
                }
            }
        });
        mLnCategory.setOnClickListener(v -> {
            showDialogDanhMuc();
        });
        // init map
        MySupportMapFragment fragment = (MySupportMapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        getIconReason();
    }

    private void showList() {

        mLnLayoutData.setVisibility(View.VISIBLE);
        mBtnArrowTop.setVisibility(View.GONE);
        mBtnArrowDown.setVisibility(View.VISIBLE);
        mLnTop.setVisibility(View.GONE);
        mBtnMyLocation.setVisibility(View.GONE);
        int heightAppbar = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAppbarLayout.getLayoutParams();
        lp.height = heightAppbar;
        mAppbarLayout.setLayoutParams(lp);
        zoomMap(false);
        if (Pasgo.getInstance() != null
                && Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null
                ) {
            mLatSearch = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            mLngSearch = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        refreshLocation();
    }

    private void hideList(boolean isMarkerClick) {
        if (mLnLayoutData.getVisibility() == View.GONE)
            return;
        // Xóa marker trên bản đồ
        if (mGoogleMap != null)
            mGoogleMap.clear();
        if (mMapDiaChiChiNhanh != null)
            mMapDiaChiChiNhanh.clear();
        if (mMapDiaChiHangXe != null)
            mMapDiaChiHangXe.clear();
        if (mMarkersMienPhi != null)
            mMarkersMienPhi.clear();
        mMarkerCurrent = null;
        mMarkerSearch = null;
        // ẩn layout
        mLnLayoutData.setVisibility(View.GONE);
        mBtnArrowTop.setVisibility(View.VISIBLE);
        mBtnArrowDown.setVisibility(View.GONE);
        mLnTop.setVisibility(View.VISIBLE);
        mBtnMyLocation.setVisibility(View.VISIBLE);
        zoomMap(isMarkerClick);
        //lấy lại vị trí đầu tiên trước khi tính toán
        mAppbarLayout.setExpanded(true, true);
        // hiện thị full màn hình
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int heightbottom = (int) getResources().getDimension(R.dimen.main_function_bottom);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAppbarLayout.getLayoutParams();
        lp.height = height - heightbottom;
        mAppbarLayout.setLayoutParams(lp);
        // remove nestedScroolView
        final AppBarLayout.Behavior appBarBehavior =
                (AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) mAppbarLayout.getLayoutParams()).getBehavior();
        assert appBarBehavior != null;
        appBarBehavior.onStopNestedScroll(mCoordinatorLayout, mAppbarLayout, mLnLayoutData, ViewCompat.TYPE_TOUCH);
        // hiện thị lại market lên bản đồ
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONObject json = null;
                try {
                    if (!StringUtils.isEmpty(mData)) {
                        json = new JSONObject(mData);
                        setMarkerCurrent();
                        new LoadDataTask(json, mMapDiaChiChiNhanh).execute();
                        new LoadDataHangXeTask(json, mMapDiaChiHangXe).execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 1400);

    }

    private void zoomMap(boolean isMarkerClick) {
        if (mGoogleMap == null) return;
        VisibleRegion visibleRegion = mGoogleMap.getProjection()
                .getVisibleRegion();

        Point x = mGoogleMap.getProjection().toScreenLocation(
                visibleRegion.farRight);

        Point y = mGoogleMap.getProjection().toScreenLocation(
                visibleRegion.nearLeft);

        Point centerPoint = new Point(x.x / 2, y.y / 2);

        LatLng centerFromPoint = mGoogleMap.getProjection().fromScreenLocation(
                centerPoint);
        if (isMarkerClick && mMarkerClick != null)
            centerFromPoint = mMarkerClick.getPosition();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(centerFromPoint).zoom(mLnLayoutData.getVisibility() == View.GONE ? KEY_MAP_ZOOM : KEY_MAP_ZOOM_SMALL)
                .build();
        mGoogleMap
                .animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
    }

    protected void showAllMarker() {

        if (mGoogleMap == null) {
            return;
        }
        getMarkerFree();
        getMarkerTaxi();
        setMarkerCurrent();
    }

    private void setMarkerCurrent() {
        if(true)
            return;
        if (mGoogleMap != null && Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
            double lat =Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            double lng = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
            if(mIsSearchLocation){
                lat = mLatSearch;
                lng = mLngSearch;
            }
            LatLng latLng = new LatLng(lat, lng);

            if (mMarkerCurrent == null)
                mMarkerCurrent = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.vi_tri_nguoi_dung))
                        .snippet("-2"));
            else
                mMarkerCurrent.setPosition(latLng);
        }
    }


    private void getMarkerTaxi() {
        for (int i = 0; i < PTService.mLocationMsgDriverMap.size(); i++) {
            handleUpdateUI.sendEmptyMessage(0);
        }
    }

    private void getMarkerFree() {
        LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
        if (bounds != null) {
            mMarkersMienPhi.clear();
            getDoiTacGanViTri(bounds);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.KEY_DATA_JSON, mData);
        outState.putBoolean("opengps", mOpenPopUpGPS);
        outState.putBoolean(Constants.BUNDLE_GO_TO_FROM_CHANG_LANGUAGE, mGoToFromChangeLanguage);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mOpenPopUpGPS = savedInstanceState.getBoolean("opengps");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

        }

        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
        mActivity.unregisterReceiver(broadcastReceiverUpdateDriver);
        mActivity.unregisterReceiver(broadcastReceiverUpdateLocation);
        mActivity.unregisterReceiver(broadcastReceiverCheckGPS);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mItemDiaChiChiNhanhs = new HashMap<>();
        mMarkersMienPhi = new HashMap<>();
        mMarkerTaxi = new HashMap<>();

        if (mActivity.getBaseContext() == null)
            return;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapClickListener(arg0 -> hideList(false));
        mGoogleMap.setOnMarkerClickListener(marker -> {

            if (mLnLayoutData.getVisibility() == View.VISIBLE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideList(true);
                    }
                }, 150);
            }
            String reasonId = marker.getSnippet();
            if (reasonId != null
                    && ((mItemDiaChiChiNhanhs != null && mItemDiaChiChiNhanhs.containsKey(reasonId)) || ((mMapDiaChiHangXe != null) && mMapDiaChiHangXe.containsKey(reasonId)))) {
                return false;
            }

            return true;
        });
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap
                .setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String reasonId = marker.getSnippet();
                        if (reasonId != null
                                && mItemDiaChiChiNhanhs != null && mItemDiaChiChiNhanhs
                                .containsKey(reasonId)) {
                            ItemDiaChiChiNhanh reason = mItemDiaChiChiNhanhs
                                    .get(reasonId);
                            mReasonId = reasonId;
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BUNDLE_KEY_ID,
                                    reason.getId());
                            bundle.putString(
                                    Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,
                                    reason.getDoiTacKhuyenMaiId());
                            bundle.putString(
                                    Constants.BUNDLE_KEY_TEN_DOI_TAC_KM,
                                    reason.getTen());
                            bundle.putString(Constants.BUNDLE_KEY_DIA_CHI,
                                    reason.getDiaChi());
                            bundle.putString(
                                    Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID,
                                    reason.getNhomCnDoiTacId());
                            bundle.putString(Constants.BUNDLE_KEY_MO_TA,
                                    reason.getMoTa());
                            bundle.putDouble(Constants.BUNDLE_KEY_KINH_DO,
                                    reason.getKinhDo());
                            bundle.putDouble(Constants.BUNDLE_KEY_VI_DO,
                                    reason.getViDo());
                            bundle.putString(
                                    Constants.BUNDLE_KEY_LINK_WEBSITE,
                                    reason.getWebsite());
                            bundle.putBoolean(Constants.BUNDLE_KEY_MAP,
                                    true);
                            bundle.putBoolean(
                                    Constants.BUNDLE_KEY_DAT_TRUOC,
                                    reason.isDatTruoc());
                            gotoActivityForResult(mActivity.getApplicationContext(),
                                    DetailActivity.class, bundle,
                                    Constants.KEY_GO_TO_HOME, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ourLeftInLeft(mActivity);
                        }
                    }
                });

        handleUpdateLocation.sendEmptyMessage(2);
        handleUpdateLocation.sendEmptyMessage(0);


        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
    }


    @Override
    public void onCameraMove() {
        if(startFindLocation !=null)
            startFindLocation.cancel(true);

    }

    @Override
    public void onCameraIdle() {
        startFindLocation =new StartFindLocation();
        startFindLocation.execute();
    }
    private StartFindLocation startFindLocation;
    public class StartFindLocation extends AsyncTask<Void, Integer, Integer> {
        private boolean isCancel = false;
        @Override
        protected void onCancelled() {
            isCancel = true;
        }
        @Override
        protected Integer doInBackground(Void... second) {
            try {
                Thread.sleep(200L);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            if(!isCancel) {
                Utils.Log(TAG, "second " + progress[0]);
            }
        }

        @Override
        protected void onPostExecute(Integer second) {
            onUpdateMapAfterUserInterection();
        }

    }
    private void showDialogGPS() {
        if (mActivity.isFinishing()) return;
        final Dialog dialogCheckLogin;
        dialogCheckLogin = new Dialog(mActivity);
        dialogCheckLogin.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        dialogCheckLogin.setContentView(R.layout.layout_popup_dang_ki);
        TextView tvHoTen = (TextView) dialogCheckLogin
                .findViewById(R.id.tvHoTen);
        tvHoTen.setText(StringUtils.getStringByResourse(mActivity,
                R.string.plz_check_gps));
        dialogCheckLogin.setCancelable(false);
        Button btnDaGap, btnChuaGap;
        btnDaGap = (Button) dialogCheckLogin.findViewById(R.id.btnDaGap);
        btnDaGap.setOnClickListener(v -> {
            dialogCheckLogin.dismiss();
            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, 222);
        });
        btnChuaGap = (Button) dialogCheckLogin.findViewById(R.id.btnChuaGap);
        btnChuaGap.setOnClickListener(v -> dialogCheckLogin.dismiss());
        dialogCheckLogin.show();
    }


    private void getIconReason() {
        LoadIconReasonTask loadIconReasonTask = new LoadIconReasonTask();
        loadIconReasonTask.execute();
    }

    private class LoadIconReasonTask extends
            AsyncTask<Void, Void, HashMap<String, Integer>> {

        public LoadIconReasonTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, Integer> doInBackground(Void... params) {
            return PTIcon.getInstanceIconReason();
        }

        @Override
        protected void onPostExecute(HashMap<String, Integer> result) {
            super.onPostExecute(result);
            if (mActivity.getBaseContext() != null && result != null) {
                mImageReason = result;
            }
        }
    }

    private void getReasonGroup(String jsonData) {
        if (!StringUtils.isEmpty(jsonData)) {
            mReasonGroupTask = new ReasonGroupTask(jsonData);
            mReasonGroupTask.execute();
        }
    }

    class ReasonGroupTask extends AsyncTask<Void, Void, ArrayList<ItemNhomKM>> {
        public ReasonGroupTask(String jsonData) {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ItemNhomKM> doInBackground(Void... params) {
            ArrayList<ItemNhomKM> reasonGroups = null;
            if (Pasgo.getInstance().mDatabase != null) {
                reasonGroups = Pasgo.getInstance().mDatabase
                        .getReasonGroups();
            }
            return reasonGroups;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemNhomKM> result) {
            super.onPostExecute(result);
            if (result != null) {
                mReasonGroup = result;
            } else {
                getNhomKhuyenMai();
            }
        }
    }

    private void getNhomKhuyenMai() {
        String url = WebServiceUtils.URL_NHOM_KM(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.Log("response ", "response  khuyen mai" + response);
                        try {
                            mReasonGroup = new ArrayList<>();
                            JSONArray array = response.getJSONArray("Items");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    ItemNhomKM item = new ItemNhomKM();
                                    JSONObject jsonObject = array
                                            .getJSONObject(i);
                                    String ten = jsonObject.getString("Ten");
                                    String ma = jsonObject.getString("Ma");
                                    int id = jsonObject.getInt("Id");
                                    item.setId(id);
                                    item.setMa(ma);
                                    item.setTen(ten);
                                    mReasonGroup.add(item);
                                }
                            }

                        } catch (JSONException e) {
                        }
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

    class LoadDataTask
            extends
            AsyncTask<Void, Void, HashMap<String, HashMap<String, ItemDiaChiChiNhanh>>> {
        private HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> mapDiaChiChiNhanh;
        private JSONObject jsonData;

        public LoadDataTask(
                JSONObject jsonData,
                HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> mapDiaChiChiNhanh) {
            this.jsonData = jsonData;
            this.mapDiaChiChiNhanh = mapDiaChiChiNhanh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> doInBackground(
                Void... params) {
            HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> map = new HashMap<String, HashMap<String, ItemDiaChiChiNhanh>>();
            if (jsonData == null)
                return null;
            try {
                map = ParserUtils.getReasonToMap(jsonData, mapDiaChiChiNhanh);
            } catch (Exception e) {
                return null;
            }
            return map;
        }

        @Override
        protected void onPostExecute(
                HashMap<String, HashMap<String, ItemDiaChiChiNhanh>> result) {
            super.onPostExecute(result);
            if (result != null && result.size() > 0) {
                mMapDiaChiChiNhanh = mapDiaChiChiNhanh;
            }
            if (mPosition != 1) {
                handleUpdateUIMap.sendEmptyMessage(0);
            }
        }
    }

    class LoadDataHangXeTask
            extends
            AsyncTask<Void, Void, HashMap<String, DiaChiHangXe>> {
        private HashMap<String, DiaChiHangXe> mapDiaChiHangXe;
        private JSONObject jsonData;

        public LoadDataHangXeTask(
                JSONObject jsonData,
                HashMap<String, DiaChiHangXe> mapDiaChiHangXe) {
            this.jsonData = jsonData;
            this.mapDiaChiHangXe = mapDiaChiHangXe;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, DiaChiHangXe> doInBackground(
                Void... params) {
            HashMap<String, DiaChiHangXe> map;
            if (jsonData == null)
                return null;
            try {
                map = ParserUtils.getMapDiaChiHangXes(jsonData, mapDiaChiHangXe);
            } catch (Exception e) {
                return null;
            }
            return map;
        }

        @Override
        protected void onPostExecute(
                HashMap<String, DiaChiHangXe> result) {
            super.onPostExecute(result);
            if (result != null && result.size() > 0) {
                mMapDiaChiHangXe = result;
            }
            if (mPosition != 1) {
                handleUpdateUIMap.sendEmptyMessage(1);
            }
        }
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private View view;

        public CustomInfoWindowAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.layout_infor_window,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            mMarkerClick = marker;
            try {
                if (Integer.parseInt(marker.getSnippet()) < 0)
                    view.setVisibility(View.GONE);
            } catch (Exception e) {
            }
            LinearLayout popHangXe, popDiaDiemMienPhi;
            popHangXe = (LinearLayout) view
                    .findViewById(R.id.popHangXe);
            popDiaDiemMienPhi = (LinearLayout) view
                    .findViewById(R.id.popDiaDiemMienPhi);
            String reasonId = marker.getSnippet();
            final LatLng latLng = marker.getPosition();
            String contentReason = "";
            String address = "";
            String namePosition = marker.getTitle();
            if (reasonId != null && mItemDiaChiChiNhanhs != null && mItemDiaChiChiNhanhs.containsKey(reasonId)) {
                popHangXe.setVisibility(View.GONE);
                popDiaDiemMienPhi.setVisibility(View.VISIBLE);

                if (mItemDiaChiChiNhanhs != null && reasonId != null
                        && !"".equals(reasonId)
                        && mItemDiaChiChiNhanhs.containsKey(reasonId)) {
                    ItemDiaChiChiNhanh reason = mItemDiaChiChiNhanhs.get(reasonId);
                    contentReason = reason.getTitleKM();
                    address = reason.getDiaChi();
                } else {
                    namePosition = address;
                }
                if (StringUtils.isEmpty(address))
                    address = MapUtil.getCompleteAddressString(mActivity,
                            latLng.latitude, latLng.longitude);

                final TextView tvNamePosition = (TextView) view
                        .findViewById(R.id.tvNamePosition);
                final TextView tvAddress = ((TextView) view
                        .findViewById(R.id.tvAddress));
                final TextView tvContentReason = ((TextView) view
                        .findViewById(R.id.tvContentReason));
                tvNamePosition.setText(namePosition);
                tvAddress.setText(address);
                tvContentReason.setText(contentReason);
            } else {
                popHangXe.setVisibility(View.VISIBLE);
                popDiaDiemMienPhi.setVisibility(View.GONE);
                final TextView tvHangXeName = (TextView) view
                        .findViewById(R.id.tvHangXeName);
                String hangxe = String.format("  %s %s  ", getString(R.string.hang_xe_pop), namePosition);
                tvHangXeName.setText(hangxe);
            }
            return view;
        }
    }

    protected void removeItemsToMap() {
        if (mActivity.getBaseContext() == null || mGoogleMap == null
                || mMarkersMienPhi == null)
            return;
        ArrayList<String> reasonIds = new ArrayList<String>(mMarkersMienPhi.keySet());
        if (mItemDiaChiChiNhanhs != null && mMapDiaChiHangXe != null)
            for (int i = 0; i < reasonIds.size(); i++) {
                if (!mItemDiaChiChiNhanhs.containsKey(reasonIds.get(i)) &&
                        !mMapDiaChiHangXe.containsKey(reasonIds.get(i))) {
                    mMarkersMienPhi.get(reasonIds.get(i)).remove();
                    mMarkersMienPhi.remove(reasonIds.get(i));
                }
            }
        else if (mItemDiaChiChiNhanhs != null && mMapDiaChiHangXe == null)
            for (int i = 0; i < reasonIds.size(); i++) {
                if (!mItemDiaChiChiNhanhs.containsKey(reasonIds.get(i))) {
                    mMarkersMienPhi.get(reasonIds.get(i)).remove();
                    mMarkersMienPhi.remove(reasonIds.get(i));
                }
            }
        else if (mItemDiaChiChiNhanhs == null && mMapDiaChiHangXe != null)
            for (int i = 0; i < reasonIds.size(); i++) {
                if (!mMapDiaChiHangXe.containsKey(reasonIds.get(i))) {
                    mMarkersMienPhi.get(reasonIds.get(i)).remove();
                    mMarkersMienPhi.remove(reasonIds.get(i));
                }
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(StringUtils.isEmpty(mFilterFromSerer))
            handleUpdateFilter.sendEmptyMessage(KEY_LOAD_FILTER_BY_NHOM_KM_ID);
        /*if (mLnLayoutData.getVisibility() == View.GONE)
            hideList(false);
        else
            showList();*/
        // Kiểm tra nếu chưa update googleService mới nhất thì cho ẩn thanh search đi: để hiện thị text thông báo của googleMap
        if (!Utils.googleServicesConnected()) {
            mLnSearch.setVisibility(View.GONE);
        }
        else
            mLnSearch.setVisibility(View.VISIBLE);

        if (!Pasgo.isLogged() && !Pasgo.getInstance().prefs.getIsTrial()) {
            finishOurLeftInLeft(mActivity);
        }
        onNetworkChanged();
        checkNetworkGPS();
        mActivity.startService(new Intent(mActivity, PTService.class));
        IntentFilter intentUpdateDriverFilter = new IntentFilter(
                Constants.BROADCAST_ACTION_UPDATE_DRIVER);
        mActivity.registerReceiver(broadcastReceiverUpdateDriver,
                intentUpdateDriverFilter);
        IntentFilter intentUpdateLocationFilter = new IntentFilter(
                Constants.BROADCAST_ACTION_UPDATE_LOCATION);
        mActivity.registerReceiver(broadcastReceiverUpdateLocation,
                intentUpdateLocationFilter);
        mActivity.registerReceiver(broadcastReceiverCheckGPS, new IntentFilter(
                LocationManager.PROVIDERS_CHANGED_ACTION));
        //move screen
        IntentFilter intentMoveMapFilter = new IntentFilter(
                Constants.BROADCAST_ACTION_MOVE_MAP);
        getActivity().registerReceiver(broadcastReceiverMoveMap,
                intentMoveMapFilter);
        if (mGoogleMap != null) {
            float currentZoom = mGoogleMap.getCameraPosition().zoom;
            if (currentZoom < 10) {
                handleUpdateLocation.sendEmptyMessage(2);
                handleUpdateLocation.sendEmptyMessage(0);
            }
        }

        if ((Pasgo.getInstance().isUserNotNull && StringUtils.isEmpty(Pasgo
                .getInstance().userId))
                || (!Pasgo.getInstance().isUserNotNull && !StringUtils
                .isEmpty(Pasgo.getInstance().userId))) {
            Pasgo.getInstance().isUserNotNull = !Pasgo.getInstance().isUserNotNull;
            Intent intent = mActivity.getIntent();
            startActivity(intent);
            finishOurLeftInLeft(mActivity);
        }
        // network
        if (mActivity.getBaseContext() == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(mActivity.getBaseContext()).isNetworkAvailable()
                && !mIsErrorChannel) {
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
    }

    private final BroadcastReceiver broadcastReceiverMoveMap = new BroadcastReceiver() {
        @Override
        synchronized public void onReceive(Context context, Intent intent) {
            onUpdateMapAfterUserInterection();
        }
    };

    Handler handleUpdateStateChannel = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onNetworkChanged();
            super.handleMessage(msg);
        }
    };

    private final BroadcastReceiver broadcastReceiverUpdateDriver = new BroadcastReceiver() {
        @Override
        synchronized public void onReceive(Context context, Intent intent) {
            if (intent == null)
                return;
            Bundle bundle = intent.getExtras();
            if (bundle == null)
                return;
            if (!bundle.containsKey(Constants.BROADCAST_ACTION))
                return;
            if (bundle.getString(Constants.BROADCAST_ACTION).equals(
                    Constants.BROADCAST_ACTION_ADD_DRIVER)) {
                if (PTService.mLocationMsgDriverMap != null
                        && PTService.mLocationMsgDriverMap.size() > 0) {
                    mLocationMessageDriver = PTService.mLocationMsgDriverMap
                            .get(bundle
                                    .getString(Constants.KEY_DRIVER_ID_BROADCAST));

                    if (mPosition != 2) {
                        handleUpdateUI.sendEmptyMessage(0);
                    }
                }
                return;
            }

            if (bundle.getString(Constants.BROADCAST_ACTION).equals(
                    Constants.BROADCAST_ACTION_UPDATE_STATE_DRIVER)) {
                if (PTService.mLocationMsgDriverMap != null
                        && PTService.mLocationMsgDriverMap.size() > 0) {
                    mLocationMessageDriver = PTService.mLocationMsgDriverMap
                            .get(bundle
                                    .getString(Constants.KEY_DRIVER_ID_BROADCAST));
                    if (mPosition != 2) {
                        handleUpdateUI.sendEmptyMessage(1);
                    }
                }
                return;
            }

            if (bundle.getString(Constants.BROADCAST_ACTION).equals(
                    Constants.BROADCAST_ACTION_REMOVE_STATE_DRIVER)) {
                if (PTService.mLocationMsgDriverMap != null
                        && PTService.mLocationMsgDriverMap.size() > 0) {
                    mLocationMessageDriver = PTService.mLocationMsgDriverMap
                            .get(bundle
                                    .getString(Constants.KEY_DRIVER_ID_BROADCAST));

                    handleUpdateUI.sendEmptyMessage(2);
                }
                return;
            }
        }
    };

    private final BroadcastReceiver broadcastReceiverUpdateLocation = new BroadcastReceiver() {
        @Override
        synchronized public void onReceive(Context context, Intent intent) {
            handleUpdateLocation.sendEmptyMessage(0);
        }
    };

    private final BroadcastReceiver broadcastReceiverCheckGPS = new BroadcastReceiver() {
        @Override
        synchronized public void onReceive(Context context, Intent intent) {
            checkNetworkGPS();
        }
    };

    private void checkNetworkGPS() {
        if (NetworkUtils.getInstance(mActivity).isGpsEnabled()) {
            handleUpdateUIGPS.sendEmptyMessage(0);
        } else {
            handleUpdateUIGPS.sendEmptyMessage(1);
        }
    }

    private final Handler handleUpdateUIGPS = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        if (mActivity.getBaseContext() == null || mLnCheckGPS == null)
                            return;
                        mLnCheckGPS.setVisibility(View.GONE);
                        break;
                    case 1:
                        if (mActivity.getBaseContext() == null || mLnCheckGPS == null)
                            return;
                        mLnCheckGPS.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
            super.handleMessage(msg);
        }
    };

    private final Handler handleUpdateLocation = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                double lat =0.0, lng =0.0;
                if (Pasgo.getInstance().prefs.getLatLocationRecent() != null
                        && !"".equals(Pasgo.getInstance().prefs
                        .getLatLocationRecent())
                        && mPageNumber == 1) {
                    lat = Double.parseDouble(Pasgo.getInstance().prefs
                            .getLatLocationRecent());
                    lng = Double.parseDouble(Pasgo.getInstance().prefs
                            .getLngLocationRecent());
                }
                switch (msg.what) {
                    case 0:
                        if (mActivity.getBaseContext() == null || mGoogleMap == null)
                            return;
                        if (mGoogleMap != null) {
                            // khi load xong map mới zoom đến điểm my location
                            double finalLat = lat;
                            mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                public void onMapLoaded() {

                                    if (Pasgo.getInstance() != null
                                            && Pasgo.getInstance().prefs != null
                                            && Pasgo.getInstance().prefs
                                            .getLatLocationRecent() != null) {

                                        //load lại danh sách đối tác theo Tag
                                        // nếu đã load xong và không có điểm đến nào
                                        if ((mListDiemDen.size() == 1 && mListDiemDen.get(0).isDisconnect() == DiemDenHomeAdapter.KEY_NODATA && !flag_loading) || finalLat == 0) {
                                            mPageNumber = 1;
                                            mIsLoadAl = false;
                                            mListDiemDen.clear();
                                            mIsSearchLocation = false;
                                            refreshLocation();
                                            getDoiTacGanBan();
                                        }

                                    }
                                    //refreshLocation();
                                }
                            });
                        }
                        showAction();
                        break;
                    case 1:
                        showAction();
                        break;
                    case 2:
                        if (mActivity.getBaseContext() == null || mGoogleMap == null || lat == 0.0)
                            return;
                        // cho bản đồ chạy đến gần điểm đó rồi mới load
                        LatLng latLng = new LatLng(lat, lng);
                        if (Pasgo.getInstance() != null
                                && Pasgo.getInstance().prefs != null
                                && Pasgo.getInstance().prefs
                                .getLatLocationRecent() != null) {

                            latLng = new LatLng(
                                    Double.parseDouble(Pasgo.getInstance().prefs
                                            .getLatLocationRecent()),
                                    Double.parseDouble(Pasgo.getInstance().prefs
                                            .getLngLocationRecent()));
                        }
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(KEY_MAP_ZOOM - 5).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition), 1,
                                new GoogleMap.CancelableCallback() {

                                    @Override
                                    public void onFinish() {
                                        Utils.Log(Pasgo.TAG,"animateCamera");
                                        LatLngBounds bounds = mGoogleMap
                                                .getProjection()
                                                .getVisibleRegion().latLngBounds;
                                        if (bounds != null) {
                                            getDoiTacGanViTri(bounds);
                                            getReasonGroup(mData);
                                            getDoiTacGanViTri(bounds);
                                            handleUpdateUIMoverMap.sendEmptyMessage(0);
                                        }
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
            super.handleMessage(msg);
        }
    };

    private final Handler handleUpdateUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                if (mActivity.getBaseContext() == null || mGoogleMap == null
                        || mLocationMessageDriver == null)
                    return;
                PTLocationInfo locationInfo = mLocationMessageDriver
                        .getLocation();
                if (locationInfo == null)
                    return;
                LatLng latLng = new LatLng(locationInfo.getLat(),
                        locationInfo.getLng());
                switch (msg.what) {
                    case 0:

                        MarkerOptions markerOp;
                        if (mLocationMessageDriver.isFree()) {
                            markerOp = new MarkerOptions().position(latLng).icon(
                                    BitmapDescriptorFactory
                                            .fromResource(R.drawable.taxi_ranh)
                            ).snippet("-3");

                        } else {
                            markerOp = new MarkerOptions().position(latLng).icon(
                                    BitmapDescriptorFactory
                                            .fromResource(R.drawable.taxi_ban)
                            ).snippet("-3");
                        }

                        Marker marker = mGoogleMap.addMarker(markerOp);
                        mMarkerTaxi.put(mLocationMessageDriver.getId(), marker);
                        break;
                    case 1:

                        Marker markerUpdate = mMarkerTaxi
                                .get(mLocationMessageDriver.getId());

                        if (mLocationMessageDriver.isFree())
                            markerUpdate.setIcon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.taxi_ranh));
                        else
                            markerUpdate.setIcon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.taxi_ban));

                        markerUpdate.setPosition(latLng);

                        break;
                    case 2:

                        if (PTService.mLocationMsgDriverMap != null
                                && PTService.mLocationMsgDriverMap
                                .containsKey(mLocationMessageDriver.getId())) {

                            PTService.mLocationMsgDriverMap
                                    .remove(mLocationMessageDriver.getId());
                        }

                        if (mMarkerTaxi != null
                                && mMarkerTaxi.containsKey(mLocationMessageDriver
                                .getId())) {

                            mMarkerTaxi.get(mLocationMessageDriver.getId())
                                    .remove();
                            mMarkerTaxi.remove(mLocationMessageDriver.getId());
                        }

                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                Utils.Log(Pasgo.TAG, e.toString());
            }
            super.handleMessage(msg);
        }
    };


    private final Handler handleUpdateUIMap = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        mActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (mActivity.getBaseContext() == null || mGoogleMap == null
                                        || mMapDiaChiChiNhanh == null
                                        || mReasonGroup == null
                                        || mImageReason == null)
                                    return;

                                mItemDiaChiChiNhanhs = new HashMap<String, ItemDiaChiChiNhanh>();

                                ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();
                                for (ItemNhomKM itemNhomKM : mReasonGroup) {
                                    HashMap<String, ItemDiaChiChiNhanh> itemDiaChiChiNhanhs = new HashMap<String, ItemDiaChiChiNhanh>();
                                    if (mMapDiaChiChiNhanh.containsKey(itemNhomKM
                                            .getMa())) {
                                        itemDiaChiChiNhanhs = mMapDiaChiChiNhanh
                                                .get(itemNhomKM.getMa());
                                    }

                                    if (itemDiaChiChiNhanhs == null
                                            || itemDiaChiChiNhanhs.size() <= 0
                                            || !mImageReason.containsKey(itemNhomKM
                                            .getMa())) {
                                        continue;
                                    }

                                    int drawable = mImageReason.get(itemNhomKM
                                            .getMa());

                                    mItemDiaChiChiNhanhs
                                            .putAll(itemDiaChiChiNhanhs);

                                    ArrayList<String> reasonIds = new ArrayList<String>(
                                            itemDiaChiChiNhanhs.keySet());

                                    for (int i = 0; i < reasonIds.size(); i++) {
                                        ItemDiaChiChiNhanh reason = itemDiaChiChiNhanhs
                                                .get(reasonIds.get(i));

                                        if (reason == null)
                                            continue;

                                        lstLatLng.add(new LatLng(reason.getViDo(),
                                                reason.getKinhDo()));

                                        if (!mMarkersMienPhi.containsKey(reason
                                                .getId())) {
                                            BitmapDescriptor icon = null;
                                            Bitmap largeIcon = BitmapFactory
                                                    .decodeResource(getResources(),
                                                            drawable);
                                            icon = BitmapDescriptorFactory
                                                    .fromBitmap(largeIcon);
                                            LatLng latLng = new LatLng(reason
                                                    .getViDo(), reason.getKinhDo());
                                            MarkerOptions markerOptions = new MarkerOptions()
                                                    .position(latLng)
                                                    .title(reason.getTen())
                                                    .snippet(reason.getId())
                                                    .icon(icon);
                                            Marker marker = mGoogleMap
                                                    .addMarker(markerOptions);
                                            String reasonId = reasonIds.get(i);
                                            mMarkersMienPhi.put(reasonId, marker);
                                            if (mReasonId != null
                                                    && mReasonId.equals(reason
                                                    .getId())) {
                                                marker.showInfoWindow();
                                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                                        .target(latLng).zoom(mLnLayoutData.getVisibility() == View.GONE ? KEY_MAP_ZOOM : KEY_MAP_ZOOM_SMALL)
                                                        .build();
                                                mGoogleMap
                                                        .animateCamera(CameraUpdateFactory
                                                                .newCameraPosition(cameraPosition));
                                                mReasonId = null;
                                            }
                                        }
                                    }
                                }

                                removeItemsToMap();
                                if (mCustomInfoWindowAdapter == null) {
                                    mCustomInfoWindowAdapter = new CustomInfoWindowAdapter();
                                    mGoogleMap
                                            .setInfoWindowAdapter(mCustomInfoWindowAdapter);
                                }
                            }
                        });
                        break;
                    case 1:
                        mActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (mActivity.getBaseContext() == null || mGoogleMap == null
                                        || mMapDiaChiHangXe == null)
                                    return;

                                ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();
                                int drawable = R.drawable.taxi_hangxe;
                                ArrayList<String> reasonIds = new ArrayList<String>(
                                        mMapDiaChiHangXe.keySet());
                                for (int i = 0; i < reasonIds.size(); i++) {
                                    DiaChiHangXe reason = mMapDiaChiHangXe
                                            .get(reasonIds.get(i));

                                    if (reason == null)
                                        continue;

                                    lstLatLng.add(new LatLng(reason.getViDo(),
                                            reason.getKinhDo()));

                                    if (!mMarkersMienPhi.containsKey(reason
                                            .getId())) {
                                        BitmapDescriptor icon = null;
                                        Bitmap largeIcon = BitmapFactory
                                                .decodeResource(getResources(),
                                                        drawable);
                                        icon = BitmapDescriptorFactory
                                                .fromBitmap(largeIcon);
                                        LatLng latLng = new LatLng(reason
                                                .getViDo(), reason.getKinhDo());
                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .position(latLng)
                                                .title(reason.getTen())
                                                .snippet(reason.getId())
                                                .icon(icon);
                                        Marker marker = mGoogleMap
                                                .addMarker(markerOptions);
                                        String reasonId = reasonIds.get(i);
                                        mMarkersMienPhi.put(reasonId, marker);
                                        if (mReasonId != null
                                                && mReasonId.equals(reason
                                                .getId())) {
                                            marker.showInfoWindow();
                                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                                    .target(latLng).zoom(mLnLayoutData.getVisibility() == View.GONE ? KEY_MAP_ZOOM : KEY_MAP_ZOOM_SMALL)
                                                    .build();
                                            mGoogleMap
                                                    .animateCamera(CameraUpdateFactory
                                                            .newCameraPosition(cameraPosition));
                                            mReasonId = null;
                                        }
                                    }
                                }

                                removeItemsToMap();
                                if (mCustomInfoWindowAdapter == null) {
                                    mCustomInfoWindowAdapter = new CustomInfoWindowAdapter();
                                    mGoogleMap
                                            .setInfoWindowAdapter(mCustomInfoWindowAdapter);
                                }
                            }
                        });
                        break;
                    case 2:
                        mTvSearchPlace.setText(mStartAddress);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Utils.Log(Pasgo.TAG, e.toString());
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onAnimationEnd(Animation animation) {
        Utils.Log(Pasgo.TAG, "onAnimationEnd");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        animation.cancel();
        Utils.Log(Pasgo.TAG, "onAnimationRepeat");
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Utils.Log(Pasgo.TAG, "onAnimationStart");
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.btnMyLocation:
                mIsSearchLocation = false;
                refreshLocation();
                handleUpdateUIMoverMap.sendEmptyMessage(1);
                break;

            default:
                break;
        }
    }

    private void refreshLocation() {
        double lat = 0.0;
        double lng = 0.0;
        if (Pasgo.getInstance() != null
                && Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null
                ) {
            lat = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            lng = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        if(mIsSearchLocation)
        {
            lat = mLatSearch;
            lng = mLngSearch;
        }
        final LatLng latLng = new LatLng(lat, lng);
        if (mGoogleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(mLnLayoutData.getVisibility() == View.GONE ? KEY_MAP_ZOOM : KEY_MAP_ZOOM_SMALL).build();
            mGoogleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    KEY_MINUTES_LOADMAP_CURRENT_LOCATION, new GoogleMap.CancelableCallback() {

                        @Override
                        public void onFinish() {
                            LatLngBounds bounds = mGoogleMap
                                    .getProjection().getVisibleRegion().latLngBounds;
                            if (bounds != null) {
                                setMarkerCurrent();
                                getReasonGroup(mData);
                                getDoiTacGanViTri(bounds);
                                handleUpdateUIMoverMap.sendEmptyMessage(0);
                            }
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
        }
    }

    private void showAction() {
        if (mActivity.getBaseContext() == null || Pasgo.getInstance() == null
                || Pasgo.getInstance().prefs == null)
            return;
        if (Pasgo.getInstance().prefs.getLatLocationRecent() == null
                || "".equals(Pasgo.getInstance().prefs
                .getLatLocationRecent())) {
            ToastUtils.showToast(
                    mActivity,
                    getResources().getString(
                            R.string.title_not_get_current_location));
        }
        if (NetworkUtils.getInstance(mActivity.getBaseContext()).isGpsEnabled())
            mLnCheckGPS.setVisibility(View.GONE);
        else
            mLnCheckGPS.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.kEY_BACK_FORM_GOOGLEPLAYSERVICE) {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
            if (result != ConnectionResult.SUCCESS) {
                googleAPI.getErrorDialog(getActivity(), result,
                        Constants.kEY_BACK_FORM_GOOGLEPLAYSERVICE).show();

            } else {
                //stopServiceLocation();
                Intent refresh = new Intent(mActivity, HomeActivity.class);
                refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refresh);
                finishOurLeftInLeft(mActivity);
            }
        }
        Utils.Log(Pasgo.TAG, "requestCode: " + requestCode + "resultCode: " + resultCode);
        if (requestCode != resultCode)
            return;
        Bundle bundle;
        switch (requestCode) {
            case Constants.kEY_CHON_DIEM_DON:
                if (data == null) {
                    return;
                }
                bundle = data.getExtras();
                if (bundle != null) {
                    mIsSearchLocation = true;
                    mLatSearch = bundle.getDouble(Constants.BUNDLE_LAT);
                    mLngSearch = bundle.getDouble(Constants.BUNDLE_LNG);
                    mStartName = bundle.getString(Constants.KEY_NAME);
                    mStartAddress = bundle.getString(Constants.KEY_VICINITY);
                    handleUpdateUIMap.sendEmptyMessage(2);
                    refreshLocation();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetworkChanged() {
        if (mActivity == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()
                && !mIsErrorChannel) {
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
    }

    public void onUpdateMapAfterUserInterection() {
        if (mGoogleMap == null)
            return;
        if(mReasonGroup !=null || mReasonGroup.size()==0)
            getNhomKhuyenMai();
        if (mPosition != 1) {
            LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
            if (bounds != null)
                getDoiTacGanViTri(bounds);
        }
        mIsSearchLocation = true;
        handleUpdateUIMoverMap.sendEmptyMessage(1);
    }


    private void getDoiTacGanViTri(LatLngBounds bounds) {

        double latSouthWest = bounds.southwest.latitude;
        double longSouthWest = bounds.southwest.longitude;
        double latNorthEast = bounds.northeast.latitude;
        double longNorthEast = bounds.northeast.longitude;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("categoryId", mCategoryId);
            jsonParams.put("latSouthWest", latSouthWest);
            jsonParams.put("longSouthWest", longSouthWest);
            jsonParams.put("latNorthEast", latNorthEast);
            jsonParams.put("longNorthEast", longNorthEast);
        } catch (Exception e) {
        }
        Pasgo.getInstance().cancelPendingRequests(Pasgo.TAG);
        if (Pasgo.getInstance().prefs == null)
            return;
        String url = WebServiceUtils.URL_GET_DOI_TAC_GAN_VI_TRI(Pasgo.getInstance().token);
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
            try {
                Pasgo.getInstance().addToRequestQueueWithTag(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    mData = json.toString();
                                }
                                if (mGoogleMap == null) {
                                    return;
                                }
                                getMarkerTaxi();
                                setMarkerCurrent();
                                new LoadDataTask(json, mMapDiaChiChiNhanh).execute();
                                new LoadDataHangXeTask(json, mMapDiaChiHangXe).execute();
                            }

                            @Override
                            public void onError(int maloi) {
                            }

                        }, new Pasgo.PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }, Pasgo.TAG);
            } catch (Exception e) {
            }
        } else {

        }
    }
    private final Handler handlerCategory = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case KEY_SET_CATEGORY:
                    if(mCategoryHomes.size()>0) {
                        CategoryHome categoryHome = mCategoryHomes.get(0);
                        categoryHome.setCheck(true);
                        mCategoryId = categoryHome.getId();
                        mTvCategory.setText(categoryHome.getTen());
                        if(mCategoryNearYouAdapter == null)
                        {
                            mCategoryNearYouAdapter = new CategoryNearYouAdapter(mActivity, mCategoryHomes, new CategoryNearYouAdapter.DanhMucListenner() {
                                @Override
                                public void check(int position) {
                                    for(CategoryHome item:mCategoryHomes)
                                            item.setCheck(false);
                                    //set check
                                    CategoryHome categoryHome = mCategoryHomes.get(position);
                                    categoryHome.setCheck(true);
                                    mCategoryId = categoryHome.getId();
                                    mTvCategory.setText(categoryHome.getTen());
                                    // get Tag
                                    mTagAlls = categoryHome.getTagModels();
                                    handleTag.sendEmptyMessage(KEY_SET_TAG);
                                    handleTag.sendEmptyMessage(KEY_SET_TAG_BY_CATEGORY);
                                    if(mDialogDanhMuc!=null && mDialogDanhMuc.isShowing())
                                        mDialogDanhMuc.dismiss();
                                    //
                                    handlerCategory.sendEmptyMessage(KEY_SET_MARKET_BYCATEGORY);
                                }
                            });
                        }
                    }
                    break;
                case KEY_SET_MARKET_BYCATEGORY:
                    if(mMarkersMienPhi!=null)
                        mMarkersMienPhi.clear();
                    if(mMapDiaChiChiNhanh!=null)
                        mMapDiaChiChiNhanh.clear();
                    if(mItemDiaChiChiNhanhs != null)
                        mItemDiaChiChiNhanhs.clear();
                    mData = "";
                    mMarkerCurrent = null;
                    mMarkerSearch = null;
                    if(mGoogleMap !=null) {
                        LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
                        mGoogleMap.clear();
                        if (bounds != null)
                            getDoiTacGanViTri(bounds);
                    }
                    break;

            }
        }
    };
    private final Handler handleTag = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case KEY_SET_TAG:
                        if (mTagAlls.size() > 0) {
                            if (mTagAlls.size() <= NUMBER_ITEM_TAG) {
                                mTags = cloneListTag(mTagAlls);
                            } else {
                                mTags.clear();
                                for (int j = 0; j < NUMBER_ITEM_TAG; j++) {
                                    TagModel model = cloneTagModel(mTagAlls.get(j));
                                    mTags.add(model);
                                }
                                mTags.add(new TagModel(Constants.KEY_TAG_THEM, getString(R.string.de_xuat_xem_them)));
                            }
                            if (mTags.size() > 0) {
                                mTags = tagSetSelected(0, mTags);
                                mTagId = mTags.get(0).getId();
                            }
                            if (mTagAdapter == null) {
                                mTagAdapter = new TagAdapter(mActivity, mTags, new TagAdapter.TagListenner() {
                                    @Override
                                    public void onClick(int position) {
                                        if (mTags.size() - 1 < position || flag_loading) return;
                                        if (mTagId == mTags.get(position).getId())
                                            return;
                                        if (mTags.get(position).getId() == Constants.KEY_TAG_THEM) {
                                            showDialogTag();
                                            return;
                                        }
                                        mTagId = mTags.get(position).getId();
                                        // update ListTag
                                        mTags = tagSetSelected(position, mTags);
                                        mTagAdapter.updateList(mTags);
                                        // update Dialog list tag
                                        for (TagModel tagModel : mTagAlls) {
                                            if (tagModel.getId() == mTagId) {
                                                mTagAlls = tagSetSelected(tagModel, mTagAlls);
                                                mTagDialogAdapter.updateList(mTagAlls);
                                                break;
                                            }
                                        }
                                        mPageNumber = 1;
                                        mIsLoadAl = false;
                                        mListDiemDen.clear();
                                        getDoiTacGanBan();
                                    }
                                });
                                mLvTag.setAdapter(mTagAdapter);
                            }
                            mTagAdapter.updateList(mTags);
                            //dialogTag
                            if (mTagDialogAdapter == null) {
                                if (mTagAlls.size() > 0)
                                    mTagAlls.get(0).setIsCheck(true);
                                mTagDialogAdapter = new TagDialogAdapter(mActivity, mTagAlls, new TagDialogAdapter.TagDialogListenner() {
                                    @Override
                                    public void check(int position) {
                                        if (mTagAlls.size() - 1 < position) {
                                            mDialogTag.dismiss();
                                            return;
                                        }
                                        if (mTagId == mTagAlls.get(position).getId()) {
                                            mDialogTag.dismiss();
                                            return;
                                        }
                                        mTagAlls = tagSetSelected(position, mTagAlls);
                                        mPageNumber = 1;
                                        mTagId = mTagAlls.get(position).getId();
                                        mIsLoadAl = false;
                                        mListDiemDen.clear();
                                        getDoiTacGanBan();
                                        //update lai dialog tag
                                        mTagDialogAdapter.updateList(mTagAlls);
                                        //update lai Tag
                                        TagModel model = cloneTagModel(mTagAlls.get(position));
                                        if (!tagIsExist(model, mTags)) {
                                            for (TagModel item : mTags)
                                                item.setIsCheck(false);
                                            model.setIsCheck(true);
                                            mTags.add(mTags.size() - 1, model);
                                            mTagAdapter.updateList(mTags);
                                            mLvTag.scrollToPosition(mTags.size() - 1);
                                        } else {
                                            mTags = tagSetSelected(model, mTags);
                                            mTagAdapter.updateList(mTags);
                                            mLvTag.scrollToPosition(tagGetPosition(mTagId, mTags));
                                        }
                                        mDialogTag.dismiss();
                                    }
                                });
                            }
                            mTagDialogAdapter.updateList(mTagAlls);
                        } else {
                            mTags.clear();
                            mTagAdapter.updateList(mTags);
                            mListDiemDen.clear();
                            setListAdapter(mListDiemDen,DiemDenHomeAdapter.KEY_NODATA);
                        }
                        break;
                    case KEY_SET_TAG_BY_CATEGORY:
                        mPageNumber = 1;
                        mIsLoadAl = false;
                        mListDiemDen.clear();
                        getDoiTacGanBan();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Utils.Log(Pasgo.TAG, e.toString());
            }
            super.handleMessage(msg);
        }
    };

    private void showDialogTag() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.loai_hinh));
        alertDialog.setView(dialog);
        final ListView lv = (ListView) dialog.findViewById(R.id.listView);
        if (mTagDialogAdapter == null) return;
        lv.setAdapter(mTagDialogAdapter);
        if (mDialogTag == null)
            mDialogTag = alertDialog.create();
        if (mDialogTag != null && !mDialogTag.isShowing()) {
            mDialogTag = alertDialog.create();
            mDialogTag.show();
        }
    }

    private void setListAdapter(ArrayList<DiemDenModel> listItemAdressFree, int keyData) {
        if (keyData == DiemDenHomeAdapter.KEY_DISCONNECT || keyData == DiemDenHomeAdapter.KEY_LOADING) {
            listItemAdressFree.clear();
            listItemAdressFree.add(new DiemDenModel(keyData));
        } else {
            if (listItemAdressFree.size() == 0) {
                keyData = DiemDenHomeAdapter.KEY_NODATA;
                listItemAdressFree.add(new DiemDenModel(DiemDenHomeAdapter.KEY_NODATA));
            }
        }
        showView(KEY_DATA_DIEM_DEN);
        flag_loading = false;
        mAdapter = new DiemDenHomeAdapter(mActivity, listItemAdressFree,
                new DiemDenHomeAdapter.DiemDenHomeListener() {
                    @Override
                    public void checkIn(int position) {
                        // TODO Auto-generated method stub
                        DiemDenModel item = null;
                        if (mListDiemDen.size() >= position) {
                            item = mListDiemDen.get(position);
                            if(item.getLoaiHopDong()>1) return;
                            initCheckIn(item.getNhomCNDoiTacId(), item.getTen(), item.getTrangThai(),item.getDiaChi());
                        }
                    }
                    @Override
                    public void detail(int position) {
                        DiemDenModel item = null;
                        if (mListDiemDen.size() >= position) {
                            item = mListDiemDen.get(position);
                        }
                        if (item != null) {
                            String doiTacKMID = item.getDoiTacKhuyenMaiId();
                            String title = item.getTen();
                            String chiNhanhDoiTacId = item.getId();
                            String nhomCnDoiTac = item.getNhomCNDoiTacId();
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.KEY_DI_XE_FREE, Constants.KEY_INT_XE_FREE);
                            bundle.putInt(Constants.KEY_TEN_TINH_ID, 0);
                            bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
                            bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, doiTacKMID);
                            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
                            bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
                            bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, item.getNhomKhuyenMaiId());
                            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getDiaChi());
                            bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCnDoiTac);
                            bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
                            bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
                            bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
                            gotoActivityForResult(mActivity, DetailActivity.class, bundle,
                                    Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ourLeftInLeft(mActivity);
                        }
                    }

                    @Override
                    public void disconnect() {
                        getDoiTacGanBan();
                    }
                }, keyData);
        mRcKetQua.setAdapter(mAdapter);
        mRcKetQua.setLayoutManager(mLayoutManager);
        mRcKetQua.setAdapter(mAdapter);
        if (mPageNumber > 1)
            mRcKetQua.getLayoutManager().scrollToPosition(mfirstVisibleItem);


    }
    private boolean mIsClickCheckIn = false;
    public synchronized void initCheckIn(String nhomCNDoiTacId, String tenDoiTac, int trangThai, String diaChi)
    {
        if(!mIsClickCheckIn)
        {
            mIsClickCheckIn = true;
            Bundle bundle =new Bundle();
            bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID,nhomCNDoiTacId);
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM,tenDoiTac);
            bundle.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, trangThai);
            bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, diaChi);
            gotoActivityForResult(mActivity, ReserveDetailActivity.class, bundle, Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft(mActivity);
            mIsClickCheckIn = false;
        }
    }
    private void showDialogDanhMuc() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.danh_muc_chinh));
        alertDialog.setView(dialog);
        final ListView lv = (ListView)dialog.findViewById(R.id.listView);
        if(mCategoryNearYouAdapter==null) return;
        lv.setAdapter(mCategoryNearYouAdapter);
        if(mDialogDanhMuc ==null)
            mDialogDanhMuc = alertDialog.create();
        if(mDialogDanhMuc !=null && !mDialogDanhMuc.isShowing()&&!getActivity().isFinishing()) {
            mDialogDanhMuc.show();
        }
    }

    private void getDoiTacGanBan() {
        // nếu có danh sách điểm đến rồi, mà danh sách tag chưa có thì load lại: trường hợp này thường là thay đổi tọa độ
        if(mListDiemDen.size()>0 &&mTagAlls.size() ==0)
        {
            mPageNumber =1;
        }
        flag_loading = true;
        String url = WebServiceUtils
                .URL_GET_DOI_TAC_GAN_BAN(Pasgo.getInstance().token);
        double lat =0.0, lng =0.0;
        if (Pasgo.getInstance().prefs.getLatLocationRecent() != null
                && !"".equals(Pasgo.getInstance().prefs
                .getLatLocationRecent())
                && mPageNumber == 1) {
            lat = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            lng = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        final int pageNumber = mPageNumber;
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("viDo", lat);
            jsonParams.put("kinhDo", lng);
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("tagId", mTagId);
            jsonParams.put("categoryId",mCategoryId);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("pageNumber", mPageNumber);
            jsonParams.put("pageSize", mPageSize);
            jsonParams.put("filter",!mIsFilter?"":mSFilter);
            jsonParams.put("loadDanhMuc", mIsLoadAl ? 1 : 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mPageNumber == 1)
            showView(KEY_LOAD_DATA_DIEM_DEN);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        flag_loading = false;
                        ArrayList<DiemDenModel> mList = ParserUtils.getSearchDiemDensV1(response);
                        if (mIsLoadAl) {
                            mCategoryHomes = ParserUtils.getCategoryHomes(response);
                            handlerCategory.sendEmptyMessage(KEY_SET_CATEGORY);
                            if(mCategoryHomes.size()>0)
                                mTagAlls = mCategoryHomes.get(0).getTagModels();
                            handleTag.sendEmptyMessage(KEY_SET_TAG);
                        }
                        if (pageNumber == 1)
                            mListDiemDen.clear();
                        mListDiemDen.addAll(mList);
                        if (mPageNumber == 1) {
                            setListAdapter(mListDiemDen, DiemDenHomeAdapter.KEY_DATA);
                        } else
                            mAdapter.notifyDataSetChanged();
                        mFooterView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                        flag_loading = false;
                        if (mPageNumber > 1)
                            mPageNumber--;
                        else {
                            mListDiemDen.clear();
                            showView(KEY_DISCONNECT_DIEM_DEN);
                        }
                    }

                }, error -> {
                    flag_loading = false;
                    if (mPageNumber > 1)
                        mPageNumber--;
                    else {
                        mListDiemDen.clear();
                        showView(KEY_DISCONNECT_DIEM_DEN);
                    }
                });
    }

    // end
    private void showView(final int i) {
        switch (i) {
            case KEY_LOAD_DATA_DIEM_DEN:
                setListAdapter(mListDiemDen, DiemDenHomeAdapter.KEY_LOADING);
                mRcKetQua.setVisibility(View.VISIBLE);
                mFooterView.setVisibility(View.GONE);
                break;
            case KEY_NO_DATA_DIEM_DEN:
                mRcKetQua.setVisibility(View.VISIBLE);
                mFooterView.setVisibility(View.GONE);
                break;
            case KEY_DISCONNECT_DIEM_DEN:
                mRcKetQua.setVisibility(View.VISIBLE);
                mFooterView.setVisibility(View.GONE);
                setListAdapter(mListDiemDen, DiemDenHomeAdapter.KEY_DISCONNECT);
                break;
            case KEY_DATA_DIEM_DEN:
                mRcKetQua.setVisibility(View.VISIBLE);
                mFooterView.setVisibility(View.GONE);
                break;

        }

    }
    // filter

    private synchronized void getFilterVoiNhonKhuyenMaiId() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nhomKhuyenMaiId", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(WebServiceUtils.URL_GET_FILTER_VOI_NHOM_KHUYEN_MAI_ID(Pasgo
                            .getInstance().token), jsonParams,
                    new Pasgo.PWListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject objItemAll = ParserUtils.getJsonObject(response, "Item");
                            mFilterFromSerer = ParserUtils.getStringValue(objItemAll,"filter");
                            handleUpdateFilter.sendEmptyMessage(KEY_LOAD_FILTER_BY_NHOM_KM_ID);
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

    private final Handler handleUpdateFilter = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case KEY_LOAD_FILTER_BY_NHOM_KM_ID:
                        try {
                            if(mFiltersList !=null) {
                                mFiltersList.clear();
                            }
                            if(StringUtils.isEmpty(mFilterFromSerer))
                            {
                                getFilterVoiNhonKhuyenMaiId();
                                return;
                            }
                            JSONObject objFilter = new JSONObject(mFilterFromSerer);
                            mFiltersList = ParserUtils.getAllFilters(objFilter);
                            for(FilterParent parent:mFiltersList)
                            {
                                if(parent.getCode().toLowerCase().equals(Constants.mCodeDisableLoaiHinhNhaHang.toLowerCase()))
                                {
                                    mFilterParentNhaHangLoaiHinhId = parent.getId();
                                }
                            }
                            // xóa mưc ưu đãi, giảm giá
                            if (mFiltersList.size() > 0) {
                                ArrayList<FilterCategoryItems> filterCategoryItems = mFiltersList.get(0).getFilterCategoryItems();
                                for (int i = 0; i < filterCategoryItems.size(); i++) {
                                    if (filterCategoryItems.get(i).getId() == (BuildConfig.DEBUG ? 156 : 2))
                                        filterCategoryItems.remove(i);
                                }
                            }

                            mFilterViews.clear();
                            for (FilterParent filterParent : mFiltersList) {
                                String parentName = filterParent.getNameEn();
                                if (Pasgo.getInstance().prefs.getLanguage().equals(Constants.LANGUAGE_VIETNAM)) {
                                    parentName = filterParent.getNameVn();
                                }

                                mFilterViews.add(new FilterView(-1, filterParent.getId(), parentName, false, true, filterParent.getChoiceType()));

                                for (FilterCategoryItems filterCategoryItems : filterParent.getFilterCategoryItems()) {
                                    String name = filterCategoryItems.getNameEn();
                                    if (Pasgo.getInstance().prefs.getLanguage().equals(Constants.LANGUAGE_VIETNAM)) {
                                        name = filterCategoryItems.getNameVn();
                                    }
                                    //mFilterViews.add(new FilterView(filterCategoryItems.getId(), filterParent.getId(), name, filterCategoryItems.isSelected(), false, filterParent.getChoiceType()));
                                    mFilterViews.add(new FilterView(filterCategoryItems.getId(), filterParent.getId(), name, false, false, filterParent.getChoiceType()));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Utils.Log(Pasgo.TAG, e.toString());
            }
            super.handleMessage(msg);
        }
    };

    private void showDialogFilter() {
        if (mFilterViews.size()==0 || flag_loading)
            return;
        mFilterViewsTam.clear();
        ArrayList<FilterView> itemsFirst=new ArrayList<>();
        for(int i=0;i<mFilterViews.size();i++)
        {
            if(mFilterViews.get(i).getParentId() == mFilterParentNhaHangLoaiHinhId)
            {
                if(mTagId ==1)
                    itemsFirst.add(mFilterViews.get(i));
            }else
                itemsFirst.add(mFilterViews.get(i));
        }
        mFilterViewsTam= itemsFirst;

        mFilterViewsClone = cloneList(mFilterViewsTam);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setIcon(R.drawable.app_pastaxi);
        View dialog = inflater.inflate(R.layout.popup_dia_diem_filter, null);

        alertDialogBuilder.setView(dialog)
                .setPositiveButton(R.string.dong_y, (dialog1, id) -> {
                    //mFilterViews = mFilterViewsClone;
                    for(int i=0;i<mFilterViews.size();i++)
                    {
                        int idView =mFilterViews.get(i).getId();
                        for(FilterView filterViewClone: mFilterViewsClone)
                        {
                            if(idView == filterViewClone.getId())
                                mFilterViews.get(i).setCheck(filterViewClone.isCheck());
                        }
                    }
                    // lưu lại danh sách check theo id của filterParent + category
                    HashMap<String,Boolean> hashMaplist =new HashMap<String, Boolean>();
                    if (mFilterViews.size() > 0) {
                        for (int i=0;i< mFilterViews.size();i++)
                        {
                            hashMaplist.put(mFilterViews.get(i).getParentId()+""+mFilterViews.get(i).getId(),mFilterViews.get(i).isCheck());
                        }
                    }
                    // kiểm tra lại danh sách filtet lúc đầu, set lại giá trị selected
                    for (FilterParent filterParent: mFiltersList)
                    {
                        for (FilterCategoryItems filterCategoryItems: filterParent.getFilterCategoryItems())
                        {
                            String key = filterParent.getId()+""+filterCategoryItems.getId();
                            if (hashMaplist.containsKey(key)) {
                                filterCategoryItems.setSelected(hashMaplist.get(key));
                            }
                        }
                    }
                    handleLoadData.sendEmptyMessage(KEY_FILTER);
                })
                .setNegativeButton(R.string.huy, (dialogInterface, i) -> {
                });
        // set view
        final ListView lvFilter =(ListView)dialog.findViewById(R.id.lvFilter);

        mDiaDiemFilterAdapter =new DestinationFilterAdapter(mActivity, mFilterViewsClone, (position, isCheck) -> {
            int choiceType =mFilterViewsClone.get(position).getChoiceType();
            int parentId =mFilterViewsClone.get(position).getParentId();
            if(choiceType==1&& isCheck)// ==1 là chỉ được chọn 1
            {
                for(FilterView item: mFilterViewsClone)
                {
                    if(item.getParentId() ==parentId)
                        item.setCheck(false);
                }
            }
            mFilterViewsClone.get(position).setCheck(isCheck);
            mDiaDiemFilterAdapter.notifyDataSetChanged();
            lvFilter.invalidateViews();
            lvFilter.refreshDrawableState();
        },false);
        lvFilter.setAdapter(mDiaDiemFilterAdapter);
        // create dialog and show
        if(mDialogFilter==null) {
            mDialogFilter = alertDialogBuilder.create();
        }
        if(mDialogFilter!=null && !mDialogFilter.isShowing()) {
            mDialogFilter = alertDialogBuilder.create();
            mDialogFilter.show();
        }
    }
    protected Handler handleLoadData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            mSFilter = (mFiltersList == null || mFiltersList.size() == 0) ? "" : "{\"Items\":" + gson.toJson(mFiltersList) + "}";
            switch (msg.what) {
                case KEY_FILTER:
                    mPageNumber = 1;
                    mIsFilter = true;
                    getDoiTacGanBan();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private ArrayList<FilterView> cloneList(ArrayList<FilterView> cList) {
        ArrayList<FilterView> clonedList = new ArrayList<FilterView>(cList.size());
        for (FilterView dog : cList) {
            clonedList.add(new FilterView(dog.getId(),dog.getParentId(),dog.getName(),dog.isCheck(),dog.isParent(),dog.getChoiceType()));
        }
        return clonedList;
    }
    private ArrayList<TagModel> cloneListTag(ArrayList<TagModel> cList) {
        ArrayList<TagModel> clonedList = new ArrayList<TagModel>(cList.size());
        for (TagModel tag : cList) {
            clonedList.add(new TagModel(tag.getId(), tag.getTen(), tag.getMa(), tag.isCheck()));
        }
        return clonedList;
    }

    private TagModel cloneTagModel(TagModel tag) {
        return new TagModel(tag.getId(), tag.getTen(), tag.getMa(), tag.isCheck());
    }

    private boolean tagIsExist(int tagId, ArrayList<TagModel> arrayList) {
        for (TagModel item : mTags) {
            item.setIsCheck(false);
            if (mTagId == item.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean tagIsExist(TagModel item, ArrayList<TagModel> arrayList) {
        for (TagModel model : arrayList) {
            if (model.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<TagModel> tagSetSelected(int position, ArrayList<TagModel> arrayList) {
        for (TagModel model : arrayList)
            model.setIsCheck(false);
        arrayList.get(position).setIsCheck(true);
        return arrayList;
    }

    private ArrayList<TagModel> tagSetSelected(TagModel item, ArrayList<TagModel> arrayList) {
        for (TagModel model : arrayList)
            model.setIsCheck(false);
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getId() == item.getId()) {
                arrayList.get(i).setIsCheck(true);
                break;
            }
        }
        return arrayList;
    }

    private int tagGetPosition(int tagId, ArrayList<TagModel> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getId() == tagId)
                return i;
        }
        return 0;
    }
    private final Handler handleUpdateUIMoverMap = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        if (Pasgo.getInstance().prefs != null
                                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
                            mLatSearch = Double.parseDouble(Pasgo.getInstance().prefs
                                    .getLatLocationRecent());
                            mLngSearch =Double.parseDouble(Pasgo.getInstance().prefs
                                    .getLngLocationRecent());

                        }
                        handleUpdateUIMoverMap.sendEmptyMessage(1);
                        break;
                    case 1:
                        if (getActivity() == null || mGoogleMap == null)
                            return;
                        VisibleRegion visibleRegion = mGoogleMap.getProjection()
                                .getVisibleRegion();
                        Point x = mGoogleMap.getProjection().toScreenLocation(
                                visibleRegion.farRight);
                        Point y = mGoogleMap.getProjection().toScreenLocation(
                                visibleRegion.nearLeft);
                        Point centerPoint = new Point(x.x / 2, y.y / 2);
                        LatLng centerFromPoint = mGoogleMap.getProjection()
                                .fromScreenLocation(centerPoint);
                        mLatSearch = centerFromPoint.latitude;
                        mLngSearch = centerFromPoint.longitude;
                        handleUpdateUIMoverMap.sendEmptyMessage(2);
                        setMarkerCurrent();
                        break;
                    case 2:
                        mStartAddress = MapUtil.getCompleteAddressString(mActivity,
                                mLatSearch, mLngSearch);
                        if (!StringUtils.isEmpty(mStartAddress)) {
                            handleUpdateUIMap.sendEmptyMessage(2);
                        } else {
                            String url = "https://maps.googleapis.com/maps/api/geocode/json?"
                                    + "latlng=" + mLatSearch + "," + mLngSearch
                                    + "&sensor=true&language=vi&key=" + Constants.API_MAP;
                            Pasgo.getInstance().addToRequestGet(url, new Pasgo.PWListener() {
                                @Override
                                public void onResponse(JSONObject json) {
                                    if (json != null) {
                                        Utils.Log(Pasgo.TAG, json.toString());
                                        try {
                                            DiaChi diaChi = new DiaChi();

                                            JSONArray responseArray = json.getJSONArray(RESULTS);
                                            JSONObject firstResponse = responseArray.getJSONObject(0);
                                            JSONArray addressComponentArray = firstResponse
                                                    .getJSONArray(ADDRESS_COMPONENTS);

                                            for (int k = 0; k < addressComponentArray.length(); k++) {
                                                JSONObject jsonObject = addressComponentArray
                                                        .getJSONObject(k);

                                                JSONArray jsonArrayTypes = jsonObject.getJSONArray(TYPES);
                                                ArrayList<String> lstType = new ArrayList<String>();
                                                for (int m = 0; m < jsonArrayTypes.length(); m++) {
                                                    lstType.add(jsonArrayTypes.getString(m));
                                                }

                                                if (lstType.contains(STREET_NUMBER)) {
                                                    diaChi.setSoNha(jsonObject.getString(LONG_NAME));
                                                }

                                                if (lstType.contains(ROUTE)) {
                                                    diaChi.setTenDuong(jsonObject.getString(LONG_NAME));
                                                }

                                                if (lstType.contains(SUBLOCALITY)
                                                        && lstType.contains(POLITICAL)) {
                                                    diaChi.setPhuongXa(jsonObject.getString(LONG_NAME));
                                                }

                                                if (lstType.contains(LOCALITY)
                                                        && lstType.contains(POLITICAL)) {
                                                    diaChi.setThanhPho(jsonObject.getString(LONG_NAME));
                                                }

                                                if (lstType.contains(ADMINISTRATIVE_AREA_LEVEL_2)
                                                        && lstType.contains(POLITICAL)) {
                                                    diaChi.setQuanHuyen(jsonObject.getString(LONG_NAME));
                                                }

                                                if (lstType.contains(ADMINISTRATIVE_AREA_LEVEL_1)
                                                        && lstType.contains(POLITICAL)) {
                                                    diaChi.setTinh(jsonObject.getString(LONG_NAME));
                                                }

                                                if (lstType.contains(COUNTRY)
                                                        && lstType.contains(POLITICAL)) {
                                                    diaChi.setNuoc(jsonObject.getString(LONG_NAME));
                                                }
                                            }
                                            mStartAddress = diaChi.getFullAddress();
                                            handleUpdateUIMap.sendEmptyMessage(2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
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
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Utils.Log(Pasgo.TAG, e.toString());
            }
            super.handleMessage(msg);
        }
    };
}