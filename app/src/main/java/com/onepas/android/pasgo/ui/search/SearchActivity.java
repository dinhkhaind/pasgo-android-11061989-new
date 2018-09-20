package com.onepas.android.pasgo.ui.search;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.PTIcon;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DiaChi;
import com.onepas.android.pasgo.models.ItemAdressFree;
import com.onepas.android.pasgo.models.ItemDiaChiChiNhanhSearchPlace;
import com.onepas.android.pasgo.models.ItemNhomKM;
import com.onepas.android.pasgo.models.Place;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.calldriver.DatXeActivity;
import com.onepas.android.pasgo.ui.partner.NhomKhuyenMaiAdapter;
import com.onepas.android.pasgo.util.mapnavigator.MapUtil;
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
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends BaseAppCompatActivity implements
        OnClickListener, OnFocusChangeListener, AnimationListener, OnMapReadyCallback {
    public static final String TAG = "SearchActivity";
    private static String TAG_FIST = "BeginLoad";
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
    // Huy???n
    private static final String ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";// T???nh
    private static final String COUNTRY = "country";// N?????c
    private ExpandableHeightListView mLvDiaDiem, mLvDiemTaiTro;
    private ScrollView mScrSearchPlace;
    private List<Place> mListPlace = new ArrayList<Place>();
    private List<Place> mListPlaceAll = new ArrayList<Place>();
    private double fromLat = 21.005245;
    private double fromLong = 105.864574;
    private boolean mIsGoToHome = false;

    private EditText mEdtSearch;
    private ImageView mIvSearch;
    private LinearLayout mLayoutDiemQuenThuoc, mLayoutGoogle,
            mLayoutFoursquare, mLayoutKoDuLieu, mLnSearch, mLnGooglemap,
            mLnPopupMarker, mLnLoadingAddress;
    private RelativeLayout mRlGoogleMap;
    private DatabaseHandler mDBAdapter;
    private ImageView mImageViewFoursqare, mImageViewGoogle,
            mImageViewQuenThuoc, mImageGoogleMap;
    private int mGetPlacesByFavorites = 0;
    private Button mBtnMyLocation, mBtnSelect;
    private int selectButton = 1;
    private int keySearch = KEY_SEARCH_FOURSQUARE_SEARCH_NEAR;
    private boolean mIsDiemDon = true;
    public DiemQTAdapter diemQTAdapter;
    private TextView mTvKoCoDL;
    private double mLat, mLng;
    private GoogleMap mGoogleMap;
    private static final double KINH_DO_MAC_DINH = 105.8500000;
    private static final double VI_DO_MAC_DINH = 21.0333330;
    private String mName = "", mVicinity = "";
    // khi chọn Marker thì gắn giá trị cho mChiNhanhDoiTacId, nếu mà move map thì gắn mChiNhanhDoiTacId ="" --- nNhomCNDoiTacId tương tự
    private String mChiNhanhDoiTacId = "";
    private String mNhomCNDoiTacId = "";
    private TextView mTvAddress;
    private TextView mTvNamePosition;
    private ImageView mImgIconMap;
    private boolean mIsDatXe, mIsSearchFromGoogleMap = false;
    private ImageView mImgYeuThich;
    private boolean mIsCheckFavorite = false;
    private boolean mIsMoveScreen = false;
    private LinearLayout mLnFunction;
    private Animation mAnimSlideDown;
    private Animation mAnimSlideUp;
    private RelativeLayout mRlReadMoreDiaDiem, mRlReadMoreDiemTaiTro;
    private final int KEY_SEARCH_FOURSQUARE = 0;
    private final int KEY_SEARCH_GOOGLE = 1;
    private final int KEY_SEARCH_DIEM_QUEN_THUOC = 2;
    private final int KEY_SEARCH_GOOGLE_MAP = 3;
    private final int READMORE_PAGE_SIZE = 3;
    private final int READMORE_DIEMTAITRO_PAGE_SIZE = 15;
    private int mPageNumber = 1;
    protected boolean mFlagLoading;
    private LinearLayout mLnDiaDiemTitle, mLnDiemTaiTroTitle;
    private List<ItemAdressFree> mListItemAdressFree = new ArrayList<ItemAdressFree>();
    private HashMap<String, Integer> mImageReason;
    private HashMap<String, Marker> mMarkersMienPhi;// mien phi
    private ArrayList<ItemNhomKM> mReasonGroup;
    private HashMap<String, ItemDiaChiChiNhanhSearchPlace> mItemDiaChiChiNhanhs;
    private HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> mMapDiaChiChiNhanh;
    private final int mDistantSearchMap =100;
    private final int mMapZoom =15;
    private static int KEY_SEARCH_FOURSQUARE_SEARCH_NEAR =0;
    private static int KEY_SEARCH_FOURSQUARE_SEARCH_BY_TEXT =1;
    private static int KEY_SEARCH_FOURSQUARE_SEARCH_GOOGLE_MAP =2;
    private boolean mIsSelectMarkerPartner =false;// nếu click vào marker rồi thì không lấy địa chỉ từ api nữa
    @Override
    public void onNetworkChanged() {
        if (getBaseContext() == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable())
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        else
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_near);
        Bundle bundle = getIntent().getExtras();
        if(savedInstanceState!=null)
            bundle = savedInstanceState;
        if (bundle != null) {
            mIsDiemDon = bundle.getBoolean(Constants.KEY_IS_DIEMDON);
            mIsGoToHome = bundle.getBoolean(Constants.BUNDLE_SEARCH_GO_TO_HOME,false);
            fromLat = bundle.getDouble(Constants.BUNDLE_LAT);
            fromLong = bundle.getDouble(Constants.BUNDLE_LNG);
            mLat = fromLat;
            mLng = fromLong;
        }
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        if (mIsDiemDon) {
            toolbarTitle.setText(getString(R.string.search_dia_diem_don));
        } else {
            toolbarTitle.setText(getString(R.string.search_dia_diem_den));
        }
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mProgressToolbar =(ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        this.onNetworkChanged();
        mImageReason = new HashMap<String, Integer>();
        getIconReason();
        mMarkersMienPhi = new HashMap<>();
        mMapDiaChiChiNhanh = new HashMap<>();
        mItemDiaChiChiNhanhs = new HashMap<>();
        mDBAdapter = new DatabaseHandler(this);
        mScrSearchPlace = (ScrollView) findViewById(R.id.scrSearchPlace);
        mLnDiaDiemTitle = (LinearLayout) findViewById(R.id.lnDiaDiemTitle);
        mLnDiemTaiTroTitle = (LinearLayout) findViewById(R.id.lnDiemTaiTroTitle);
        mRlReadMoreDiaDiem = (RelativeLayout) findViewById(R.id.rlReadMoreDiaDiem);
        mRlReadMoreDiemTaiTro = (RelativeLayout) findViewById(R.id.rlReadMoreDiemTaiTro);
        mLnSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        mLayoutDiemQuenThuoc = (LinearLayout) findViewById(R.id.linerDiemQuenThuoc);
        mLayoutGoogle = (LinearLayout) findViewById(R.id.linerGoogle);
        mLayoutFoursquare = (LinearLayout) findViewById(R.id.linerFoursquare);
        mLnGooglemap = (LinearLayout) findViewById(R.id.linerGooglemap);
        mLayoutKoDuLieu = (LinearLayout) findViewById(R.id.lyKhongCoThongBao);
        mRlGoogleMap = (RelativeLayout) findViewById(R.id.rlGoogleMap);
        mBtnMyLocation = (Button) findViewById(R.id.btnMyLocation);
        mBtnSelect = (Button) findViewById(R.id.btnSelect);
        mLnPopupMarker = (LinearLayout) findViewById(R.id.lnPopupMarker);
        mImgIconMap = (ImageView) findViewById(R.id.imgIconMap);
        mLayoutFoursquare.setOnClickListener(this);
        mLayoutGoogle.setOnClickListener(this);
        mLnGooglemap.setOnClickListener(this);
        mTvKoCoDL = (TextView) findViewById(R.id.tv_ko_co_dl);
        mLayoutDiemQuenThuoc.setOnClickListener(this);
        mLvDiaDiem = (ExpandableHeightListView) findViewById(R.id.lvDiemSearch);
        mLvDiaDiem.setExpanded(true);
        mLvDiemTaiTro = (ExpandableHeightListView) findViewById(R.id.lvDiemTaiTro);
        mLvDiemTaiTro.setExpanded(true);
        mEdtSearch = (EditText) findViewById(R.id.serach_place);
        mIvSearch = (ImageView) findViewById(R.id.search);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mTvNamePosition = (TextView) findViewById(R.id.tvNamePosition);
        mLnLoadingAddress = (LinearLayout) findViewById(R.id.lnLoadingAddress);
        mImgYeuThich = (ImageView) findViewById(R.id.imgYeuThich);
        mLnFunction = (LinearLayout) findViewById(R.id.lnBt);
        if(mIsGoToHome)
            mLnFunction.setVisibility(View.GONE);
        else
            mLnFunction.setVisibility(View.VISIBLE);
        mAnimSlideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        mAnimSlideDown = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_down);
        mIvSearch.setOnClickListener(this);
        mBtnMyLocation.setOnClickListener(this);
        mAnimSlideUp.setAnimationListener(this);
        mAnimSlideDown.setAnimationListener(this);
        mImgYeuThich.setOnClickListener(v -> {
            if (StringUtils.isEmpty(mName)
                    || StringUtils.isEmpty(mVicinity))
                return;
            mImgYeuThich
                    .setImageResource(R.drawable.icon_sao_quenthuoc_click);
            Place itemSelect = new Place();
            itemSelect.setName(mName);
            itemSelect.setVicinity(mVicinity);
            itemSelect.setCheckStar(true);
            itemSelect.setLatitude(mLat);
            itemSelect.setLongitude(mLng);
            mDBAdapter.insertItemPlace(itemSelect);
            if (mIsCheckFavorite) {
                mImgYeuThich
                        .setImageResource(R.drawable.icon_sao_quenthuoc);
                mDBAdapter.deleteFavoriteItem(itemSelect);
                mIsCheckFavorite = false;
            } else
                mIsCheckFavorite = true;
        });
        mEdtSearch
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            // performSearch();
                            keySearch = KEY_SEARCH_FOURSQUARE_SEARCH_BY_TEXT;
                            String txtSearch = mEdtSearch.getText().toString();
                            actionSearch(txtSearch);
                            return true;
                        }
                        return false;
                    }
                });
        mEdtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        mEdtSearch.requestFocus();
        mImageViewFoursqare = (ImageView) findViewById(R.id.ic_foursquare);
        mImageViewGoogle = (ImageView) findViewById(R.id.im_google);
        mImageViewQuenThuoc = (ImageView) findViewById(R.id.ic_quenthuoc);
        mImageGoogleMap = (ImageView) findViewById(R.id.ic_googleMap);
        mProgressToolbar.setVisibility(ProgressBar.GONE);
        setbackgroundtab(1);
        if (mIsDiemDon == false) {
        } else {
            // search nearby
            new GetPlaces(mContext, null, mGetPlacesByFavorites, true, 1, false)
                    .execute();
        }
        mBtnSelect.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (!mIsDatXe)
                return;
            Intent intent = new Intent(mActivity, DatXeActivity.class);
            intent.putExtra(Constants.BUNDLE_LAT, mLat);
            intent.putExtra(Constants.BUNDLE_LNG, mLng);
            intent.putExtra(Constants.KEY_NAME, mName);
            intent.putExtra(Constants.KEY_VICINITY, mVicinity);
            intent.putExtra(Constants.BUNDLE_KEY_CHI_NHANH_DOI_TAC_ID_TU_SEARCH_PLACE, mChiNhanhDoiTacId);
            intent.putExtra(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, mNhomCNDoiTacId);
            if (mIsDiemDon)
                setResult(Constants.kEY_CHON_DIEM_DON, intent);
            else
                setResult(Constants.kEY_CHON_DIEM_DEN, intent);

            finishToRightToLeft();
        });

        mLvDiaDiem.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Place itemSelect = mListPlace.get(position);
                mLat = itemSelect.getLatitude();
                mLng = itemSelect.getLongitude();
                mName = itemSelect.getName();
                mVicinity = itemSelect.getVicinity();
                mEdtSearch.setText("");
                if (selectButton == KEY_SEARCH_GOOGLE_MAP) {
                    visibilityLoadingMap(false);
                    mTvNamePosition.setText(mName);
                    mTvAddress.setText(mVicinity);
                    final LatLng latLng = new LatLng(mLat, mLng);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(mMapZoom).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    mLvDiaDiem.setVisibility(View.GONE);
                    mScrSearchPlace.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(mActivity, DatXeActivity.class);
                    intent.putExtra(Constants.BUNDLE_LAT, mLat);
                    intent.putExtra(Constants.BUNDLE_LNG, mLng);
                    intent.putExtra(Constants.KEY_NAME, mName);
                    intent.putExtra(Constants.KEY_VICINITY, mVicinity);
                    intent.putExtra(Constants.BUNDLE_KEY_CHI_NHANH_DOI_TAC_ID_TU_SEARCH_PLACE, "");
                    intent.putExtra(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, "");
                    if (mIsDiemDon)
                        setResult(Constants.kEY_CHON_DIEM_DON, intent);
                    else
                        setResult(Constants.kEY_CHON_DIEM_DEN, intent);

                    finishToRightToLeft();
                }
            }
        });
        mRlReadMoreDiaDiem.setOnClickListener(view -> listViewAddByreadMore());
        mRlReadMoreDiemTaiTro.setOnClickListener(view -> {
            if (mFlagLoading == false) {
                mFlagLoading = true;
                mPageNumber += 1;
                if (mListPlaceAll.size() > 0)
                    getDoiTacAroundLocation(mListPlaceAll.get(0));
            }

        });
        mLvDiemTaiTro.setOnItemClickListener((parent, view, position, id) -> {
            if (NetworkUtils.getInstance(getBaseContext())
                    .isNetworkAvailable()) {
                lvClickItem(position);
            }
        });
        // init map
        MySupportMapFragment fragment = (MySupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(Constants.KEY_IS_DIEMDON,mIsDiemDon);
        bundle.putBoolean(Constants.BUNDLE_SEARCH_GO_TO_HOME,mIsGoToHome);
        bundle.putDouble(Constants.BUNDLE_LAT,fromLat);
        bundle.putDouble(Constants.BUNDLE_LNG,fromLong);
    }

    private void lvClickItem(int position) {

        ItemAdressFree item = null;
        if (mListItemAdressFree.size() > 0) {
            item = mListItemAdressFree.get(position);
        }
        if (item != null) {
            Intent intent = new Intent(mActivity, DatXeActivity.class);
            intent.putExtra(Constants.BUNDLE_LAT, item.getViDo());
            intent.putExtra(Constants.BUNDLE_LNG, item.getKinhDo());
            intent.putExtra(Constants.KEY_NAME, item.getTenDoiTac().replaceAll("<(.*?)\\>", ""));
            intent.putExtra(Constants.KEY_VICINITY, item.getTenDuong());
            intent.putExtra(Constants.BUNDLE_KEY_CHI_NHANH_DOI_TAC_ID_TU_SEARCH_PLACE, item.getChiNhanhDoiTacId());
            intent.putExtra(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, item.getNhomCNDoiTacId());
            if (mIsDiemDon)
                setResult(Constants.kEY_CHON_DIEM_DON, intent);
            else
                setResult(Constants.kEY_CHON_DIEM_DEN, intent);
            finishToRightToLeft();
        }
    }

    private void showListView(boolean isShow) {
        if (!isShow) {
            mLvDiaDiem.setVisibility(View.GONE);
            mLvDiemTaiTro.setVisibility(View.GONE);
            mRlReadMoreDiaDiem.setVisibility(View.GONE);
            mRlReadMoreDiemTaiTro.setVisibility(View.GONE);
            mLnDiaDiemTitle.setVisibility(View.GONE);
            mLnDiemTaiTroTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        String txtSearchChange = mEdtSearch.getText().toString();

        GetPlaces getPlaces = new GetPlaces(mContext, txtSearchChange, 0,
                mIsDiemDon, 0, false);
        GetPlaces getPlaces1 = new GetPlaces(mContext, txtSearchChange, 0,
                mIsDiemDon, 1, false);
        getNearbyFoursquare nearbyFoursquare = new getNearbyFoursquare(
                mContext, txtSearchChange, 0);
        switch (v.getId()) {
            case R.id.linerFoursquare:
                mIsSearchFromGoogleMap = false;
                mLnSearch.setVisibility(View.VISIBLE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mTvKoCoDL.setText(getString(R.string.chua_co_diem_nao));
                selectButton = KEY_SEARCH_FOURSQUARE;
                getPlaces.cancel(true);
                // getPlaces1.cancel(true);
                mScrSearchPlace.setVisibility(View.VISIBLE);
                setbackgroundtab(KEY_SEARCH_FOURSQUARE);
                if (mListPlace != null) {
                    mListPlace.clear();
                    DiemQTAdapter diemQTAdapter = new DiemQTAdapter(mContext,
                            R.layout.item_search_near, mListPlace, fromLat,
                            fromLong, mIsGoToHome);
                    mLvDiaDiem.setAdapter(diemQTAdapter);
                }
                if (mIsDiemDon == false) {
                    if (txtSearchChange.equals("") || txtSearchChange == null) {
                        ToastUtils.showToastWaring(mContext,
                                getString(R.string.key_search_empty));
                    } else {
                        nearbyFoursquare.execute();
                    }
                } else {
                    nearbyFoursquare.execute();
                }
                break;
            case R.id.linerGoogle:
                mIsSearchFromGoogleMap = false;
                mLnSearch.setVisibility(View.VISIBLE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mTvKoCoDL.setText(getString(R.string.chua_co_diem_nao));
                mScrSearchPlace.setVisibility(View.VISIBLE);
                selectButton = KEY_SEARCH_GOOGLE;
                nearbyFoursquare.cancel(true);
                if (mListPlace != null) {
                    mListPlace.clear();
                    DiemQTAdapter diemQTAdapter1 = new DiemQTAdapter(mContext,
                            R.layout.item_search_near, mListPlace, fromLat,
                            fromLong,mIsGoToHome);
                    mLvDiaDiem.setAdapter(diemQTAdapter1);
                }
                if (mIsDiemDon == false) {
                    // diem den
                    if (txtSearchChange.equals("") || txtSearchChange == null) {
                        ToastUtils.showToastWaring(mContext,
                                getString(R.string.key_search_empty));
                    } else {
                        new GetPlaces(mContext, txtSearchChange, 0, mIsDiemDon, 0,
                                false).execute();
                    }
                } else {
                    // diem don
                    if (txtSearchChange.equals("") || txtSearchChange == null) {
                        getPlaces1.execute();
                    } else {
                        getPlaces.execute();
                    }

                }
                setbackgroundtab(KEY_SEARCH_GOOGLE);
                break;
            case R.id.linerDiemQuenThuoc:
                mIsSearchFromGoogleMap = false;
                mLnSearch.setVisibility(View.GONE);
                mScrSearchPlace.setVisibility(View.VISIBLE);
                mTvKoCoDL.setText(getString(R.string.khong_co_dia_diem));
                selectButton = KEY_SEARCH_DIEM_QUEN_THUOC;
                setbackgroundtab(KEY_SEARCH_DIEM_QUEN_THUOC);
                getPlaces.cancel(true);
                nearbyFoursquare.cancel(true);
                setListByQuenThuoc();

                break;
            case R.id.linerGooglemap:
                mProgressToolbar.setVisibility(ProgressBar.GONE);
                mLnSearch.setVisibility(View.VISIBLE);
                mScrSearchPlace.setVisibility(View.GONE);
                mImgIconMap.setVisibility(View.GONE);
                mTvKoCoDL.setText(getString(R.string.khong_co_dia_diem));
                selectButton = KEY_SEARCH_GOOGLE_MAP;
                setbackgroundtab(KEY_SEARCH_GOOGLE_MAP);
                getPlaces.cancel(true);
                getPlaces1.cancel(true);
                nearbyFoursquare.cancel(true);
                handleUpdateUIMap.sendEmptyMessage(2);
                break;
            case R.id.search:
                keySearch = KEY_SEARCH_FOURSQUARE_SEARCH_BY_TEXT;
                String txtSearch = mEdtSearch.getText().toString();
                actionSearch(txtSearch);
                break;
            case R.id.btnMyLocation:
                myLocation();
                break;
            default:
                break;
        }
    }

    private void myLocation() {
        if (Pasgo.getInstance() == null
                || Pasgo.getInstance().prefs == null
                || Pasgo.getInstance().prefs.getLatLocationRecent() == null) {
            ToastUtils.showToastWaring(mContext, R.string.tb_xac_dinh_vi_tri);
            return;
        }
        mLat = Double.parseDouble(Pasgo.getInstance().prefs
                .getLatLocationRecent());
        mLng = Double.parseDouble(Pasgo.getInstance().prefs
                .getLngLocationRecent());
        final LatLng latLng = new LatLng(mLat, mLng);
        if (mGoogleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(mMapZoom).build();
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            handleUpdateUIMap.sendEmptyMessage(1);
        }
    }

    private void actionSearch(String txtSearch) {
        showListView(false);
        if (selectButton == KEY_SEARCH_GOOGLE_MAP) {
            searchByName(txtSearch, mIsDiemDon, KEY_SEARCH_GOOGLE_MAP);
        } else {
            if (mIsDiemDon) {
                if (txtSearch.equals("") || txtSearch == null) {
                    searchByName(txtSearch, mIsDiemDon, KEY_SEARCH_GOOGLE);
                } else {
                    searchByName(txtSearch, mIsDiemDon, KEY_SEARCH_FOURSQUARE);
                }
            } else {
                if (txtSearch.equals("") || txtSearch == null) {
                    ToastUtils.showToastWaring(mContext,
                            getString(R.string.key_search_empty));
                } else {
                    searchByName(txtSearch, mIsDiemDon, KEY_SEARCH_FOURSQUARE);
                }
            }
        }
    }

    private void searchByName(String txtSearch, boolean isDiemDon,
                              int searchText) {
        hideKeyBoard();
        if (selectButton == KEY_SEARCH_FOURSQUARE) {
            if (isDiemDon) {
                new getNearbyFoursquare(mContext, txtSearch, 0).execute();
            } else {
                if (txtSearch.length() > 0) {
                    new getNearbyFoursquare(mContext, txtSearch, 0).execute();
                } else {
                    ToastUtils.showToastWaring(mContext,
                            getString(R.string.key_search_empty));
                }
            }
        } else if (selectButton == KEY_SEARCH_GOOGLE) {
            if (isDiemDon) {
                new GetPlaces(mContext, txtSearch, mGetPlacesByFavorites, true,
                        searchText, false).execute();
            } else {
                if (txtSearch.length() > 0) {
                    // search text place google
                    new GetPlaces(mContext, txtSearch, 0, mIsDiemDon,
                            searchText, false).execute();
                } else {
                    ToastUtils.showToastWaring(mContext,
                            getString(R.string.key_search_empty));
                }
            }
        } else if (selectButton == KEY_SEARCH_GOOGLE_MAP) {

            new GetPlaces(mContext, txtSearch, 0, mIsDiemDon, searchText, true)
                    .execute();
        }
    }

    private void setListByQuenThuoc() {
        new GetPlaces(mContext, null, 1, true, 0, false).execute();
    }



    private class getNearbyFoursquare extends
            AsyncTask<Void, Void, List<Place>> {

        String txtSearch;
        private boolean isCancel = false;

        public getNearbyFoursquare(Context context, String txtSearch,
                                   int getPlacesByFavorites) {
            this.txtSearch = txtSearch;
        }

        @Override
        protected void onPostExecute(List<Place> result) {
            super.onPostExecute(result);
            mProgressToolbar.setVisibility(ProgressBar.GONE);
            // isCancel = isCancelled();
            if (isCancel == false) {
                if (result != null) {
                    mListPlace = result;
                }
                handleUpdateList.sendEmptyMessage(1);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            isCancel = true;

        }

        @Override
        protected List<Place> doInBackground(Void... params) {
            List<Place> findPlaces = null;
            // isCancel = isCancelled();
            if (isCancel == false) {
                try {
                    findPlaces = getNearby(fromLat, fromLong, txtSearch);
                } catch (Exception e) {
                    Utils.Log(TAG, "" + e.toString());
                }
            }
            return findPlaces;
        }

    }

    public ArrayList<Place> getNearby(double latitude, double longitude,
                                      String txtSearch) throws Exception {
        ArrayList<ItemFsqVenue> venueList = new ArrayList<ItemFsqVenue>();
        ArrayList<Place> listPlace = new ArrayList<Place>();

        HttpURLConnection urlConnection = null;

        try {
            String ll = String.valueOf(latitude) + ","
                    + String.valueOf(longitude);

            String api_client = "";
            String baseUrl = Constants.API_URL;
            if (keySearch == KEY_SEARCH_FOURSQUARE_SEARCH_NEAR) {
                api_client = Constants.API_PARAM0;
            } else if (keySearch == KEY_SEARCH_FOURSQUARE_SEARCH_BY_TEXT) {
                api_client = Constants.API_PARAM1;
            } else if (keySearch == KEY_SEARCH_FOURSQUARE_SEARCH_GOOGLE_MAP) {
                api_client = Constants.API_PARAM1;
                baseUrl = Constants.FOURSQUARE_API_URL_CHECKIN;
            }
            // txtSearch = txtSearch.replace(" ", "%20");
            txtSearch = URLEncoder.encode(txtSearch, "utf-8");

            String url = baseUrl + ll + api_client + txtSearch
                    + Constants.API_PARAM2 + Constants.API_SECRET
                    + "&v=20130307";
            Utils.Log(TAG,"urlfoursquare"+url);
            URL urlFoursquare = new URL(url);

            urlConnection = (HttpURLConnection) urlFoursquare.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            // urlConnection.setDoOutput(true);

            urlConnection.connect();
            String response = streamToString(urlConnection.getInputStream());
            JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                    .nextValue();

            JSONArray groups = (JSONArray) jsonObj.getJSONObject("response")
                    .getJSONArray("venues");

            int length = groups.length();

            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    JSONObject items = (JSONObject) groups.get(i);

                    String id = items.getString("id");
                    String name = items.getString("name");

                    JSONObject location = items.getJSONObject("location");
                    String address = "";
                    double lat = 0, lng = 0;
                    try {
                        address = location.getString("address");
                        lat = location.getDouble("lat");
                        lng = location.getDouble("lng");
                    } catch (Exception e) {
                    }

                    if (address == null || address.equals("")) {

                    } else {
                        ItemFsqVenue itemFsqVenue = new ItemFsqVenue();
                        itemFsqVenue.setId(id);
                        itemFsqVenue.setName(name);
                        itemFsqVenue.setAddress(address);
                        itemFsqVenue.setLat(lat);
                        itemFsqVenue.setLng(lng);
                        venueList.add(itemFsqVenue);

                        Place place = new Place();
                        place.setId(id);
                        place.setName(name);
                        place.setLatitude(lat);
                        place.setLongitude(lng);
                        place.setVicinity(address);
                        listPlace.add(place);
                    }
                }
            }
        } catch (Exception ex) {
            urlConnection.disconnect();
            throw ex;
        }

        // return venueList;
        return listPlace;
    }

    private class GetPlaces extends AsyncTask<Void, Void, List<Place>> {

        private String txtSearch;
        private int getPlacesByFavorites;
        private boolean b;
        private int neaBySearch;
        private boolean isCancel = false;
        private boolean isSearchFromGoogleMap = false;

        public GetPlaces(Context context, String txtSearch,
                         int getPlacesByFavorites, boolean b, int neaBySearch,
                         boolean isSearchFromGoogleMap) {
            this.txtSearch = txtSearch;
            this.getPlacesByFavorites = getPlacesByFavorites;
            this.b = b;
            this.neaBySearch = neaBySearch;
            this.isSearchFromGoogleMap = isSearchFromGoogleMap;
        }

        @Override
        protected void onCancelled() {
            isCancel = true;
        }

        @Override
        protected void onPostExecute(List<Place> result) {
            super.onPostExecute(result);
            mProgressToolbar.setVisibility(ProgressBar.GONE);
            // isCancel = isCancelled();
            if (isCancel == false) {
                if (result != null) {
                    mListPlace = result;
                }
                mIsSearchFromGoogleMap = isSearchFromGoogleMap;
                handleUpdateList.sendEmptyMessage(0);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected List<Place> doInBackground(Void... arg0) {
            // location=21.005245,105.864574
            List<Place> findPlaces = null;
            // isCancel = isCancelled();
            if (isCancel == false) {
                if (getPlacesByFavorites == 0) {
                    if (txtSearch == null || txtSearch.equals("")) {
                        PlacesService service = new PlacesService(
                                Constants.PLACES_API_KEY, txtSearch, true,
                                neaBySearch);
                        findPlaces = service.findPlaces(fromLat, fromLong);
                    } else {
                        PlacesService service = new PlacesService(
                                Constants.PLACES_API_KEY, txtSearch, b,
                                neaBySearch);
                        findPlaces = service.findPlaces(fromLat, fromLong);
                    }
                } else if (getPlacesByFavorites == 1) {
                    findPlaces = mDBAdapter.getFavorites();
                } else if (getPlacesByFavorites == 3) {
                    findPlaces = mDBAdapter.getFavorites();
                }

            }
            return findPlaces;
        }
    }

    private void setbackgroundtab(int i) {
        hideKeyBoard();
        switch (i) {
            case KEY_SEARCH_FOURSQUARE:
                mImageViewFoursqare.setBackgroundResource(R.drawable.ic_foursquare);
                mImageViewQuenThuoc.setBackgroundResource(R.drawable.ic_love_an);
                mImageViewGoogle.setBackgroundResource(R.drawable.ic_google_an);
                mImageGoogleMap.setBackgroundResource(R.drawable.ic_maps_an);
                showListView(false);
                mLvDiaDiem.setVisibility(View.VISIBLE);
                mRlGoogleMap.setVisibility(View.GONE);
                break;
            case KEY_SEARCH_GOOGLE:
                mImageViewFoursqare
                        .setBackgroundResource(R.drawable.ic_foursquare_an);
                mImageViewQuenThuoc.setBackgroundResource(R.drawable.ic_love_an);
                mImageViewGoogle.setBackgroundResource(R.drawable.ic_google);
                mImageGoogleMap.setBackgroundResource(R.drawable.ic_maps_an);
                showListView(false);
                mLvDiaDiem.setVisibility(View.VISIBLE);
                mRlGoogleMap.setVisibility(View.GONE);
                break;
            case KEY_SEARCH_DIEM_QUEN_THUOC:
                mImageViewFoursqare.setBackgroundResource(R.drawable.ic_foursquare_an);
                mImageViewQuenThuoc.setBackgroundResource(R.drawable.ic_love);
                mImageViewGoogle.setBackgroundResource(R.drawable.ic_google_an);
                mImageGoogleMap.setBackgroundResource(R.drawable.ic_maps_an);
                showListView(false);
                mLvDiaDiem.setVisibility(View.VISIBLE);
                mRlGoogleMap.setVisibility(View.GONE);
                break;
            case KEY_SEARCH_GOOGLE_MAP:
                mImageViewFoursqare
                        .setBackgroundResource(R.drawable.ic_foursquare_an);
                mImageViewQuenThuoc.setBackgroundResource(R.drawable.ic_love_an);
                mImageViewGoogle.setBackgroundResource(R.drawable.ic_google_an);
                mImageGoogleMap.setBackgroundResource(R.drawable.ic_maps);
                showListView(false);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mRlGoogleMap.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    Handler handleUpdateList = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (getBaseContext() == null || mListPlace == null)
                        return;
                    setListPlace(mListPlace, true);
                    break;
                case 1:
                    if (getBaseContext() == null || mListPlace == null)
                        return;
                    setListPlace(mListPlace, false);
                    break;
                case 3:
                    if (getBaseContext() == null || mListPlace == null)
                        return;
                    setListPlace(mListPlace, false);
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void sortList(List<Place> listPlace) {
        if (listPlace != null && listPlace.size() > 0) {
            Collections.sort(listPlace, new Comparator<Place>() {
                @Override
                public int compare(final Place object1, final Place object2) {
                    return object1.getKhoangCach().compareTo(
                            object2.getKhoangCach());
                }
            });
            mLayoutKoDuLieu.setVisibility(View.GONE);
            mLvDiaDiem.setVisibility(View.VISIBLE);
            if (selectButton == KEY_SEARCH_GOOGLE_MAP) {
                mListPlace = listPlace;
                if (!mIsSearchFromGoogleMap) {
                    mLayoutKoDuLieu.setVisibility(View.GONE);
                    mLvDiaDiem.setVisibility(View.GONE);
                    return;
                }
                mScrSearchPlace.setVisibility(View.VISIBLE);
                diemQTAdapter = new DiemQTAdapter(mContext,
                        R.layout.item_search_near, listPlace, fromLat, fromLong,mIsGoToHome);
                mLvDiaDiem.setAdapter(diemQTAdapter);
                mLvDiaDiem.setBackgroundColor(Color.WHITE);

            } else {
                // nếu là tìm kiếm theo google hay foursquare
                mListPlaceAll = listPlace;
                mListPlace.clear();

                mPageNumber = 1;
                if ((selectButton == KEY_SEARCH_FOURSQUARE || selectButton == KEY_SEARCH_GOOGLE) && mListPlaceAll.size() > 0) {
                    listViewAddByreadMore();
                    getDoiTacAroundLocation(mListPlaceAll.get(0));
                } else if (selectButton == KEY_SEARCH_DIEM_QUEN_THUOC && mListPlaceAll.size() > 0) {
                    listViewDiaDiemQuenThuoc();
                }
            }
        } else {
            if (selectButton == KEY_SEARCH_GOOGLE_MAP)
                return;
            showListView(false);
            mLayoutKoDuLieu.setVisibility(View.VISIBLE);
            mLvDiaDiem.setVisibility(View.GONE);
        }
    }

    private void listViewDiaDiemQuenThuoc() {
        mListPlace = mListPlaceAll;
        diemQTAdapter = new DiemQTAdapter(mContext,
                R.layout.item_search_near, mListPlace, fromLat, fromLong, mIsGoToHome);
        mLvDiaDiem.setAdapter(diemQTAdapter);
    }

    // xem thêm phần listview
    private void listViewAddByreadMore() {
        int sizeAll = mListPlaceAll.size();
        int size = mListPlace.size();
        int dem = 0;
        // nếu danh sách trên listview còn nhỏ hơn danh sách trả về thì cho hiển thị thêm "xem thêm"
        for (int i = size; i < sizeAll; i++) {
            if (dem < READMORE_PAGE_SIZE) {
                mListPlace.add(mListPlaceAll.get(i));
                dem++;
            } else
                break;
        }
        sizeAll = mListPlaceAll.size();
        size = mListPlace.size();
        if (size < sizeAll)
            mRlReadMoreDiaDiem.setVisibility(View.VISIBLE);
        else
            mRlReadMoreDiaDiem.setVisibility(View.GONE);
        if (size == 0)
            mLnDiaDiemTitle.setVisibility(View.GONE);
        else
            mLnDiaDiemTitle.setVisibility(View.VISIBLE);
        diemQTAdapter = new DiemQTAdapter(mContext,
                R.layout.item_search_near, mListPlace, fromLat, fromLong,mIsGoToHome);
        mLvDiaDiem.setAdapter(diemQTAdapter);
    }

    synchronized public void setListPlace(List<Place> listPlace,
                                          boolean isGoogle) {
        List<Place> list = new ArrayList<Place>();
        if (listPlace != null && listPlace.size() > 0) {
            int size = listPlace.size();
            if (isGoogle) {
                double[] arrdouble = new double[size];
                for (int i = 0; i < size; i++) {
                    Place place = listPlace.get(i);
                    if (place != null) {
                        double toLat = place.getLatitude();
                        double toLong = place.getLongitude();
                        double khoangCach = calculateDistance(fromLong,
                                fromLat, toLong, toLat);
                        khoangCach = khoangCach / 10;
                        arrdouble[i] = khoangCach;
                        place.setKhoangCach(khoangCach);
                        list.add(place);
                    }
                }
            } else {
                double[] arrdouble = new double[size];
                for (int i = 0; i < size; i++) {
                    Place place = listPlace.get(i);
                    if (place != null) {
                        double toLat = place.getLatitude();
                        double toLong = place.getLongitude();
                        double khoangCach = calculateDistance(fromLong,
                                fromLat, toLong, toLat);
                        khoangCach = khoangCach / 10;
                        arrdouble[i] = khoangCach;
                        place.setKhoangCach(khoangCach);
                        list.add(place);
                    }
                }
            }
        }
        sortList(list);
    }

    private double calculateDistance(double fromLong, double fromLat,
                                     double toLong, double toLat) {
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return (Math.round(d)) / 100;
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtSearch.getWindowToken(), 0);
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && mEdtSearch.getText().toString().trim().length() == 0) {
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int status = googleAPI.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            googleAPI.getErrorDialog(this, status,
                    Constants.kEY_BACK_FORM_GOOGLEPLAYSERVICE).show();
        } else {
            MySupportMapFragment fragment = (MySupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            fragment.setRetainInstance(true);
            //mGoogleMap = fragment.getMap();
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(false);

            mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    String reasonId = marker.getSnippet();
                    if (reasonId != null
                            && ((mItemDiaChiChiNhanhs != null && mItemDiaChiChiNhanhs.containsKey(reasonId)))) {
                        ItemDiaChiChiNhanhSearchPlace itemDiaChi = mItemDiaChiChiNhanhs.get(reasonId);
                        mLat = itemDiaChi.getViDo();
                        mLng = itemDiaChi.getKinhDo();
                        mName = itemDiaChi.getTen();
                        mVicinity = itemDiaChi.getDiaChi();
                        mChiNhanhDoiTacId = itemDiaChi.getChiNhanhDoiTacId();
                        mNhomCNDoiTacId = itemDiaChi.getNhomCnDoiTacId();

                        handleUpdateUIMap.sendEmptyMessage(5);
                        return true;
                    }
                    return true;
                }
            });

            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        }
        handleUpdateLocation.sendEmptyMessage(0);
    }

    private final Handler handleUpdateLocation = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        if (getBaseContext() == null || mGoogleMap == null)
                            return;

                        LatLng latLng = new LatLng(VI_DO_MAC_DINH, KINH_DO_MAC_DINH);

                        if (mGoogleMap != null) {

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
                                    .target(latLng).zoom(mMapZoom).build();
                            mGoogleMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));

                            mGoogleMap.animateCamera(CameraUpdateFactory
                                            .newCameraPosition(cameraPosition), 5,
                                    new CancelableCallback() {

                                        @Override
                                        public void onFinish() {

                                            // handleUpdateUIMap.sendEmptyMessage(1);
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    });

                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onStartMoveScreen() {
        mIsMoveScreen = true;
        if (mGoogleMap == null)
            return;
    }

    @Override
    public void onUpdateMapAfterUserInterection() {
        mIsMoveScreen = false;
        mIsSelectMarkerPartner =false;
        mLnPopupMarker.setVisibility(View.VISIBLE);
        mLnFunction.setVisibility(View.VISIBLE);
        if (mGoogleMap == null)
            return;
        handleUpdateUIMap.sendEmptyMessage(1);

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
            mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected HashMap<String, Integer> doInBackground(Void... params) {
            return PTIcon.getInstanceIconReason();
        }

        @Override
        protected void onPostExecute(HashMap<String, Integer> result) {
            super.onPostExecute(result);
            if (getBaseContext() != null && result != null) {
                mImageReason = result;
            }
        }
    }

    private final Handler handleUpdateUIMap = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        if (getBaseContext() == null || mGoogleMap == null)
                            return;
                        getDoiTacToMap(mLat, mLng);
                        break;
                    case 1:
                        if (getBaseContext() == null || mGoogleMap == null)
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
                        Utils.Log(TAG, "latlng search befor" + mLat + ", " + mLng);
                        mLat = centerFromPoint.latitude;
                        mLng = centerFromPoint.longitude;
                        Utils.Log(TAG, "latlng search" + mLat + ", " + mLng);
                        handleUpdateUIMap.sendEmptyMessage(2);
                        break;
                    case 2:
                        mLnPopupMarker.setVisibility(View.VISIBLE);
                        mImgIconMap.setVisibility(View.VISIBLE);
                        Utils.setBackground(mBtnSelect,
                                Utils.getDrawable(mContext,R.drawable.corner_gray));
                        mIsDatXe = false;
                        visibilityLoadingMap(true);
                        mIsCheckFavorite = false;
                        mImgYeuThich
                                .setImageResource(R.drawable.icon_sao_quenthuoc);
                        mVicinity = MapUtil.getCompleteAddressString(mContext,
                                mLat, mLng);
                        handleUpdateMapSearch.sendEmptyMessage(0);
                        // load marker đối tác để hiển thị ra bản đồ
                        getDoiTacToMap(mLat, mLng);
                        break;
                    case 4:
                        mActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (getBaseContext() == null || mGoogleMap == null
                                        || mMapDiaChiChiNhanh == null
                                        || mReasonGroup == null
                                        || mImageReason == null)
                                    return;
                                ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();
                                for (ItemNhomKM itemNhomKM : mReasonGroup) {
                                    HashMap<String, ItemDiaChiChiNhanhSearchPlace> itemDiaChiChiNhanhs = new HashMap<String, ItemDiaChiChiNhanhSearchPlace>();
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
                                        ItemDiaChiChiNhanhSearchPlace reason = itemDiaChiChiNhanhs
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
                                        }
                                    }
                                }
                            }
                        });
                        break;
                    case 5:
                        mLnPopupMarker.setVisibility(View.VISIBLE);
                        mImgIconMap.setVisibility(View.VISIBLE);
                        mIsDatXe = false;
                        mIsCheckFavorite = false;
                        mImgYeuThich
                                .setImageResource(R.drawable.icon_sao_quenthuoc);
                        if (!StringUtils.isEmpty(mVicinity) && !StringUtils.isEmpty(mName)) {
                            mIsSelectMarkerPartner =true;
                            visibilityLoadingMap(false);
                            Utils.setBackground(mBtnSelect, getResources()
                                    .getDrawable(R.drawable.corner_btn_all));
                            mIsDatXe = true;
                            mTvNamePosition.setText(mName);
                            mTvAddress.setText(mVicinity);
                            LatLng latLng = new LatLng(mLat, mLng);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latLng).zoom(mMapZoom)
                                    .build();
                            mGoogleMap
                                    .animateCamera(CameraUpdateFactory
                                            .newCameraPosition(cameraPosition));
                        }
                        getDoiTacToMap(mLat, mLng);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Utils.Log(TAG, e.toString());
            }
            super.handleMessage(msg);
        }
    };

    private void visibilityLoadingMap(boolean b) {
        if (b) {
            mLnLoadingAddress.setVisibility(View.VISIBLE);
            mTvAddress.setVisibility(View.GONE);
            mTvNamePosition.setText(getString(R.string.searching));
        } else {
            mLnLoadingAddress.setVisibility(View.GONE);
            mTvAddress.setVisibility(View.VISIBLE);

        }
    }

    // region phần Map
    private void getDoiTacToMap(double viDo, double kinhDo) {
        if (Pasgo.getInstance().prefs == null
                || Pasgo.getInstance().prefs.getLatLocationRecent() == null) {
            return;
        }
        String url = WebServiceUtils.URL_GET_DOI_TAC_AROUND_ON_MAP(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("viDo", viDo);
            jsonParams.put("kinhDo", kinhDo);
            if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
                try {
                    Pasgo.getInstance().addToRequestQueueWithTag(url,
                            jsonParams, new Pasgo.PWListener() {

                                @Override
                                public void onResponse(JSONObject json) {
                                    if (json != null) {
                                        getReasonGroup();
                                    }
                                    new LoadDataTask(json, mMapDiaChiChiNhanh).execute();
                                    mProgressToolbar.setVisibility(ProgressBar.GONE);
                                }

                                @Override
                                public void onError(int maloi) {
                                    mProgressToolbar.setVisibility(ProgressBar.GONE);
                                }

                            }, new Pasgo.PWErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    mProgressToolbar.setVisibility(ProgressBar.GONE);
                                }
                            }, TAG_FIST);
                } catch (Exception e) {
                    mProgressToolbar.setVisibility(ProgressBar.GONE);
                }
            } else {
                final Toast toast = Toast.makeText(mActivity, getResources()
                                .getString(R.string.connect_error_check_network),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) {
        }
    }

    private void getReasonGroup() {
        ReasonGroupTask mReasonGroupTask = new ReasonGroupTask();
        mReasonGroupTask.execute();
    }

    class ReasonGroupTask extends AsyncTask<Void, Void, ArrayList<ItemNhomKM>> {
        public ReasonGroupTask() {
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

        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mReasonGroup = new ArrayList<ItemNhomKM>();
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
                            mProgressToolbar.setVisibility(ProgressBar.GONE);
                        }
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }

    //LoadDataTask class
    class LoadDataTask extends
            AsyncTask<Void, Void, HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>>> {
        private HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> mapDiaChiChiNhanh;
        private JSONObject jsonData;

        public LoadDataTask(
                JSONObject jsonData,
                HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> mapDiaChiChiNhanh) {
            this.jsonData = jsonData;
            this.mapDiaChiChiNhanh = mapDiaChiChiNhanh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> doInBackground(
                Void... params) {
            HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> map = new HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>>();
            if (jsonData == null)
                return null;
            try {
                map = ParserUtils.getPartnerToMap(jsonData, mapDiaChiChiNhanh);
            } catch (Exception e) {
                return null;
            }
            return map;
        }

        @Override
        protected void onPostExecute(
                HashMap<String, HashMap<String, ItemDiaChiChiNhanhSearchPlace>> result) {
            super.onPostExecute(result);
            if (result != null && result.size() > 0) {
                mMapDiaChiChiNhanh = mapDiaChiChiNhanh;
            }
            handleUpdateUIMap.sendEmptyMessage(4);
            mProgressToolbar.setVisibility(ProgressBar.GONE);
        }
    }


    private void getDoiTacAroundLocation(Place item) {
        String url = WebServiceUtils
                .URL_GET_DOI_TAC_AROUND_LOCATION(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            jsonParams.put("viDo", item.getLatitude());
            jsonParams.put("kinhDo", item.getLongitude());
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("pageNumber", mPageNumber);
            jsonParams.put("pageSize", READMORE_DIEMTAITRO_PAGE_SIZE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mProgressToolbar.setVisibility(ProgressBar.GONE);
                            if (mPageNumber == 1)
                                mListItemAdressFree.clear();
                            List<ItemAdressFree> mListParser = ParserUtils.getAllAdressFrees(response);
                            if (mListParser.size() == 0 || mListParser.size() % READMORE_DIEMTAITRO_PAGE_SIZE != 0)
                                mRlReadMoreDiemTaiTro.setVisibility(View.GONE);
                            else
                                mRlReadMoreDiemTaiTro.setVisibility(View.VISIBLE);
                            mListItemAdressFree.addAll(mListParser);
                            if (mListItemAdressFree.size() > 0) {
                                mProgressToolbar.setVisibility(ProgressBar.GONE);
                                setListAdapter(mListItemAdressFree);
                                mLnDiemTaiTroTitle.setVisibility(View.VISIBLE);
                            } else {
                                mLnDiemTaiTroTitle.setVisibility(View.GONE);
                                mLvDiemTaiTro.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(int maloi) {
                            mProgressToolbar.setVisibility(ProgressBar.GONE);
                        }

                    }, new Pasgo.PWErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mProgressToolbar.setVisibility(ProgressBar.GONE);
                        }
                    });
        }
    }

    private void setListAdapter(List<ItemAdressFree> listItemAdressFree) {
        if (listItemAdressFree.size() > 0) {
            mLvDiemTaiTro.setVisibility(View.VISIBLE);
            mFlagLoading = false;
            NhomKhuyenMaiAdapter adapter = new NhomKhuyenMaiAdapter(mContext,
                    R.layout.item_di_xe_mien_phi, listItemAdressFree);
            int position = mLvDiemTaiTro.getLastVisiblePosition();
            mLvDiemTaiTro.setAdapter(adapter);
            if (mPageNumber > 1)
                mLvDiemTaiTro.setSelectionFromTop(position, 0);
        }

    }

    Handler handleUpdateMapSearch =new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:{
                    if(mIsSelectMarkerPartner) return;
                    mVicinity = MapUtil.getCompleteAddressString(mContext,
                            mLat, mLng);
                    if (!StringUtils.isEmpty(mVicinity)) {
                            visibilityLoadingMap(false);
                            Utils.setBackground(mBtnSelect, getResources()
                                    .getDrawable(R.drawable.corner_btn_all));
                            mIsDatXe = true;
                            mName = mVicinity.split("-")[0];
                            mTvNamePosition.setText(mName);
                            mTvAddress.setText(mVicinity);
                            mChiNhanhDoiTacId = "";
                            mNhomCNDoiTacId ="";
                        } else {
                            String url = "https://maps.googleapis.com/maps/api/geocode/json?"
                                + "latlng=" + mLat + "," + mLng
                                + "&sensor=true&language=vi&key=" + Constants.API_MAP;
                            Pasgo.getInstance().addToRequestGet(url, new Pasgo.PWListener() {
                                @Override
                                public void onResponse(JSONObject json) {
                                    if (json != null) {
                                        Utils.Log(TAG, json.toString());
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
                                            mVicinity = diaChi.getFullAddress();
                                            mName = mVicinity.split("-")[0];
                                            // set Text
                                            if (!isFinishing()) {
                                                if (!StringUtils.isEmpty(mName)) {
                                                    mTvNamePosition.setText(mName);
                                                    mTvAddress.setText(mVicinity);
                                                    mChiNhanhDoiTacId = "";
                                                    mNhomCNDoiTacId ="";
                                                    visibilityLoadingMap(false);
                                                    mIsDatXe = true;
                                                    Utils.setBackground(mBtnSelect,
                                                            Utils.getDrawable(mContext,R.drawable.corner_btn_all));
                                                } else {
                                                    mTvAddress.setText(StringUtils.getStringByResourse(
                                                            mContext, R.string.chua_co_diem_nao));
                                                    visibilityLoadingMap(false);
                                                    mIsDatXe = false;
                                                    Utils.setBackground(mBtnSelect,
                                                            Utils.getDrawable(mContext,R.drawable.corner_gray));
                                                }
                                            }
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
                }
            }
        }
    };
    // endregion
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (selectButton == KEY_SEARCH_GOOGLE_MAP && mLvDiaDiem.getVisibility() == View.VISIBLE) {
                mEdtSearch.setText("");
                mLvDiaDiem.setVisibility(View.GONE);
                mScrSearchPlace.setVisibility(View.GONE);
            } else {
                hideKeyboard();
                finishToRightToLeft();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEdtSearch.getWindowToken(), 0);
    }
    @Override
    public void onAnimationEnd(Animation arg0) {

    }

    @Override
    public void onAnimationRepeat(Animation arg0) {

    }

    @Override
    public void onAnimationStart(Animation arg0) {

    }

}