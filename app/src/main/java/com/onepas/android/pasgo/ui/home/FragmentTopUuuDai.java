package com.onepas.android.pasgo.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.FilterCategoryItems;
import com.onepas.android.pasgo.models.FilterParent;
import com.onepas.android.pasgo.models.FilterView;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.account.AccountManagerActivity;
import com.onepas.android.pasgo.ui.calleddrivers.CalledDriverActivity;
import com.onepas.android.pasgo.ui.category.CategoryActivity;
import com.onepas.android.pasgo.ui.guid.GuidActivity;
import com.onepas.android.pasgo.ui.partner.DestinationFilterAdapter;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;
import com.onepas.android.pasgo.ui.reserve.ReserveDetailActivity;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.ui.setting.SettingActivity;
import com.onepas.android.pasgo.ui.share.ShareActivity;
import com.onepas.android.pasgo.ui.successfultrips.SuccessfulTripsActivity;
import com.onepas.android.pasgo.ui.termsandpolicies.TermsAndPoliciesActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentTopUuuDai extends BaseFragment  {
    private View mRoot;
    private static final String TAG = "HomeDetailActivity";
    private String mTextTitle ="";
    private String mMoTa ="";
    private RecyclerView mLvKetQua;
    private int mPageSize = 30;
    private int mPageNumber = 1;
    private double mLat=0.0;
    private double mLng=0.0;
    protected boolean flag_loading;
    private TextView mTvNoData;
    private ArrayList<DiemDenModel> mListDiemDen = new ArrayList<>();
    private HomeDetailAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mfirstVisibleItem=0;
    private int mTinhId =0;
    private String mTinhName="";
    private LinearLayout mLnLoadDing;
    private final int KEY_LOADING =1;
    private final int KEY_DATA =2;
    private final int KEY_NO_DATA =3;
    private final int KEY_DISCONNECT =4;
    private RelativeLayout mRlFilter;
    private ImageView mImgFilter;
    private int mTagId;
    private static final int KEY_LOAD_FILTER_BY_NHOM_KM_ID =2;
    // filter
    private DestinationFilterAdapter mDiaDiemFilterAdapter;
    private AlertDialog mDialogFilter;
    // danh sach filter lay tren server ve
    private ArrayList<FilterParent> mFiltersList;
    private int mFilterParentNhaHangLoaiHinhId=-1000;
    // danh sach đầu tiên dùng cho phần popup popup: sau khi chọn nhóm khuyến mại sẽ lấy
    private ArrayList<FilterView> mFilterViews =new ArrayList<>();
    //Chỉ dùng để hiện thị popup sau đó ko dùng
    private ArrayList<FilterView> mFilterViewsTam =new ArrayList<>();
    // Clone: để hiện thị view, khi nào nhấn "OK" thì mFilterViews= mFilterViewsClone
    private ArrayList<FilterView> mFilterViewsClone =new ArrayList<FilterView>();
    public String mSFilter="";//
    private String mFilterFromSerer;
    private final int KEY_FILTER=1;
    private boolean mIsFilter;

    private int mTypeFilter =0;
    private TextView mToolbarTitle;
    private TextView mToolbarResult;
    private static final int KEY_DATA_KM = 5;
    private static final int KEY_NO_DATA_KM = 6;
    private boolean mIsLoadAl;

    private LinearLayout mLnDisconnect;
    private RelativeLayout mFooterView;
    protected ProgressBar mProgressToolbar;
    public FragmentTopUuuDai() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mRoot==null)
        {
            mTextTitle = getActivity().getString(R.string.tab_home4);
            mMoTa = "";
            mTagId =-3;
            String tinh = Pasgo.getInstance().prefs.getTinhMain();
            if(!StringUtils.isEmpty(tinh))
            {
                try {
                    JSONObject jsonObjectTinh = new JSONObject(tinh);
                    ArrayList<TinhHome> tinhs = ParserUtils.getAllTinhHome(jsonObjectTinh);

                    for(TinhHome item: tinhs)
                    {
                        if(item.getId()==Pasgo.getInstance().mTinhId)
                        {
                            mTinhName = item.getTen();
                            mTinhId = item.getId();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mRoot = inflater.inflate(R.layout.fragment_top_uu_dai, container, false);
            mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
            mToolbar.setTitle("");
            mToolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
            mToolbarResult = (TextView)mToolbar.findViewById(R.id.toolbar_result);
            mToolbarTitle.setText(mTextTitle);
            mToolbarTitle.setSelected(true);
            mToolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mToolbarTitle.setSingleLine(true);
            mToolbarTitle.setMarqueeRepeatLimit(15);
            mToolbarTitle.setSelected(true);
            mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_action_back);
            mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft(mActivity));
            initView();

            initializeMenu();
        }
        if(mListDiemDen.size() ==0)
            initControl();
        return mRoot;
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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


    @Override
    protected void initView() {
        super.initView();
        mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
        mLvKetQua = (RecyclerView) mRoot.findViewById(R.id.kq_lv);
        mFooterView = (RelativeLayout) mRoot.findViewById(R.id.load_more_footer);
        mLvKetQua.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        mLvKetQua.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                mfirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                //Utils.Log(Pasgo.TAG, "firstVisibleItem1_" + mfirstVisibleItem);
                if (!flag_loading && NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
                    int totalItemCountTruHeader = totalItemCount - 1;
                    if (mfirstVisibleItem + visibleItemCount == totalItemCount
                            && totalItemCountTruHeader > mPageSize * (mPageNumber - 1) && totalItemCountTruHeader % mPageSize == 0) {
                        mProgressToolbar.setVisibility(View.VISIBLE);
                        mFooterView.setVisibility(View.VISIBLE);
                        flag_loading = true;
                        mPageNumber += 1;
                        getChiTietTrangChu();
                    }
                }
            }
        });

        mImgFilter = (ImageView)mRoot.findViewById(R.id.filter_img);
        mTvNoData = (TextView) mRoot.findViewById(R.id.tvNoData);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mLnLoadDing = (LinearLayout) mRoot.findViewById(R.id.lnLoadDing);

        mRlFilter = (RelativeLayout)mRoot.findViewById(R.id.filter_rl);
        mRlFilter.setOnClickListener(this);
        mImgFilter.setBackgroundResource(R.drawable.ic_filter);
        mLnDisconnect = (LinearLayout)mRoot.findViewById(R.id.lnDisconnect);
        mRoot.findViewById(R.id.btnTryAGain).setOnClickListener(v->{
            initControl();
        });
    }

    @Override
    protected void initControl() {
        super.initControl();
        handleUpdateFilter.sendEmptyMessage(KEY_LOAD_FILTER_BY_NHOM_KM_ID);
        mPageNumber =1;
        mIsLoadAl = true;
        getChiTietTrangChu();
    }
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(Constants.BUNDLE_KEY_TAG_ID, mTagId);
        bundle.putString(Constants.BUNDLE_KEY_SEARCH_TEXT, mTextTitle);
        bundle.putString(Constants.BUNDLE_KEY_MO_TA, mMoTa);
    }
    private synchronized void getChiTietTrangChu() {
        if(mPageNumber==1)
            if (Pasgo.getInstance().prefs != null
                    && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
                mLat = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLatLocationRecent());
                mLng = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLngLocationRecent());
            }

        String url = WebServiceUtils.URL_GET_CHI_TIET_TRANG_CHU(Pasgo
                .getInstance().token);
        final int pageNumber =mPageNumber;
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            if (StringUtils.isEmpty(Pasgo.getInstance().userId))
                Pasgo.getInstance().userId = "";
            jsonParams.put("viDo", mLat);
            jsonParams.put("kinhDo", mLng);
            jsonParams.put("tinhId",mTinhId);
            jsonParams.put("tagId",mTagId);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("filter",!mIsFilter?"":mSFilter);
            jsonParams.put("typeFilter", mTypeFilter);
            jsonParams.put("pageNumber", pageNumber);
            jsonParams.put("pageSize", mPageSize);
            jsonParams.put("loadDanhMuc",mIsLoadAl ? 1 : 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        handleUpdateUI.sendEmptyMessage(KEY_LOADING);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        flag_loading =false;
                        mFooterView.setVisibility(View.GONE);
                        ArrayList<DiemDenModel> mList = ParserUtils.getHomeDetailDiemDens(response);
                        if(pageNumber ==1) {
                            mListDiemDen.clear();
                            JSONObject objItem = ParserUtils.getJsonObject(response,"Item");
                            mToolbarResult.setText(String.format(getString(R.string.ket_qua_title),ParserUtils.getIntValue(objItem,"TongSo")+""));
                        }
                        mListDiemDen.addAll(mList);
                        if (mListDiemDen.size() == 0) {
                            handleUpdateUI.sendEmptyMessage(KEY_NO_DATA);
                        } else {
                            if(pageNumber==1) {
                                mLvKetQua.setAdapter(null);
                                setListAdapter(mListDiemDen);
                            }
                            else
                                mAdapter.updateList(mListDiemDen);
                            handleUpdateUI.sendEmptyMessage(KEY_DATA);
                        }

                    }

                    @Override
                    public void onError(int maloi) {
                        try {
                            if(mPageNumber>2) mPageNumber--;
                            flag_loading =false;
                            if(mPageNumber==1)
                            {
                                mLnLoadDing.setVisibility(View.GONE);
                                mLvKetQua.setVisibility(View.GONE);
                                mProgressToolbar.setVisibility(View.GONE);
                                mTvNoData.setVisibility(View.GONE);
                                mLnDisconnect.setVisibility(View.VISIBLE);
                                mFooterView.setVisibility(View.GONE);
                            }
                        }catch (Exception e)
                        {
                            Utils.Log(Pasgo.TAG,"load data error");
                        }
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if(mPageNumber>2) mPageNumber--;
                            flag_loading =false;
                            if(mPageNumber==1)
                            {
                                mLnLoadDing.setVisibility(View.GONE);
                                mLvKetQua.setVisibility(View.GONE);
                                mProgressToolbar.setVisibility(View.GONE);
                                mTvNoData.setVisibility(View.GONE);
                                mLnDisconnect.setVisibility(View.VISIBLE);
                                mFooterView.setVisibility(View.GONE);
                            }
                        }catch (Exception e)
                        {
                            Utils.Log(Pasgo.TAG,"load data error");
                        }
                    }
                });
    }
    private final Handler handleUpdateUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case KEY_LOADING:
                        if(mPageNumber ==1) {
                            mLnLoadDing.setVisibility(View.VISIBLE);
                            mProgressToolbar.setVisibility(View.GONE);
                            mLvKetQua.setVisibility(View.GONE);
                        }else
                        {
                            mLvKetQua.setVisibility(View.VISIBLE);
                            mLnLoadDing.setVisibility(View.GONE);
                            mProgressToolbar.setVisibility(View.VISIBLE);
                        }
                        mTvNoData.setVisibility(View.GONE);
                        mLnDisconnect.setVisibility(View.GONE);
                        break;
                    case KEY_NO_DATA:
                        mLnLoadDing.setVisibility(View.GONE);
                        mProgressToolbar.setVisibility(View.GONE);
                        mLvKetQua.setVisibility(View.GONE);
                        mTvNoData.setVisibility(View.VISIBLE);
                        mLnDisconnect.setVisibility(View.GONE);
                        mFooterView.setVisibility(View.GONE);
                        break;
                    case KEY_DATA:
                        mLnLoadDing.setVisibility(View.GONE);
                        mLvKetQua.setVisibility(View.VISIBLE);
                        mProgressToolbar.setVisibility(View.GONE);
                        mTvNoData.setVisibility(View.GONE);
                        mLnDisconnect.setVisibility(View.GONE);
                        mFooterView.setVisibility(View.GONE);
                        break;
                    case KEY_DISCONNECT:
                        mLnLoadDing.setVisibility(View.GONE);
                        mLvKetQua.setVisibility(View.GONE);
                        mProgressToolbar.setVisibility(View.GONE);
                        mTvNoData.setVisibility(View.GONE);
                        mLnDisconnect.setVisibility(View.VISIBLE);
                        mFooterView.setVisibility(View.GONE);
                        break;
                    case KEY_DATA_KM:
                        mProgressToolbar.setVisibility(View.GONE);
                        break;
                    case KEY_NO_DATA_KM:
                        mProgressToolbar.setVisibility(View.GONE);
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

    private void setListAdapter(ArrayList<DiemDenModel> listItemAdressFree) {
        int sizeList = listItemAdressFree.size();
        if (sizeList > 0) {
            mTvNoData.setVisibility(View.GONE);
            mLvKetQua.setVisibility(View.VISIBLE);
            flag_loading = false;
            //add mota
            DiemDenModel model = new DiemDenModel();
            model.setMoTa(true);
            model.setMotaTag(mMoTa);
            listItemAdressFree.add(0,model);
            mAdapter = new HomeDetailAdapter(mActivity, listItemAdressFree,
                    new HomeDetailAdapter.DiemDenListener() {
                        @Override
                        public void checkIn(int position) {
                            // TODO Auto-generated method stub
                            DiemDenModel item = null;
                            if(mListDiemDen.size()>=position)
                            {
                                item = mListDiemDen.get(position);
                                if(item.getLoaiHopDong()>1) return;
                                initCheckIn(item.getNhomCNDoiTacId(), item.getTen(), item.getTrangThai(), item.getDiaChi());
                            }
                        }

                        @Override
                        public void detail(int position) {
                            DiemDenModel item = null;
                            if(mListDiemDen.size()>=position)
                            {
                                item = mListDiemDen.get(position);
                            }
                            if(item!=null) {
                                lvClickItem(item, 0, "", false, false);
                            }
                        }

                        @Override
                        public void ship(int position) {
                            DiemDenModel item = null;
                            if(mListDiemDen.size()>=position)
                            {
                                item = mListDiemDen.get(position);
                            }
                            if(item!=null)
                            {
                                Bundle bundle =new Bundle();
                                bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,item.getDoiTacKhuyenMaiId());
                                gotoActivityForResult(mActivity.getApplicationContext(), ThePasgoActivity.class, bundle, Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ourLeftInLeft(mActivity);
                            }
                        }
                    });
            mLvKetQua.setLayoutManager(mLinearLayoutManager);
            mLvKetQua.setAdapter(mAdapter);
            if(mPageNumber>1)
                mLvKetQua.getLayoutManager().scrollToPosition(mfirstVisibleItem);
            else {
                mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
                mLvKetQua.getLayoutManager().scrollToPosition(0);
            }
        } else {
            mLvKetQua.setVisibility(View.GONE);
            mProgressToolbar.setVisibility(View.GONE);
        }

    }

    protected void lvClickItem(DiemDenModel item, int tinhId, String maKhuyenMai, boolean isDiemTaiTroNguoiDung, boolean isDiemTaiTroNguoiDungTuDo) {
        if (item != null) {
            String doiTacKMID = item.getDoiTacKhuyenMaiId();
            String title = item.getTen();
            String chiNhanhDoiTacId = item.getId();
            String nhomCnDoiTac = item.getNhomCNDoiTacId();
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
            ourLeftInLeft(mActivity);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.KEY_BACK_BY_MA_DAT_CHO:
                    // load lai du lieu neu dat cho theo Doi tac km
                    mPageNumber = 1;
                    mListDiemDen.clear();
                    mLvKetQua.setAdapter(null);
                    getChiTietTrangChu();
                    break;
                case Constants.KEY_BACK_BY_DAT_CHO:
                    mPageNumber = 1;
                    mListDiemDen.clear();
                    mLvKetQua.setAdapter(null);
                    getChiTietTrangChu();
                    break;

                default:
                    break;
            }
        }
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
        AlertDialog alert = alertDialogBuilder.create();
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
                    mTypeFilter = 4;
                    mIsFilter = true;
                    mLvKetQua.setVisibility(View.GONE);
                    getChiTietTrangChu();
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.filter_rl:
                showDialogFilter();
                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}