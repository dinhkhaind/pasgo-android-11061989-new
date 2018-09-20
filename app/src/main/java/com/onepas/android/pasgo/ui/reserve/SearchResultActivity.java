package com.onepas.android.pasgo.ui.reserve;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import com.onepas.android.pasgo.models.QuanHuyen;
import com.onepas.android.pasgo.models.ReserveSearch;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.partner.DestinationFilterAdapter;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.onepas.android.pasgo.ui.reserve.GoiYTimKiemAdapter.*;

public class SearchResultActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private String mTextSearch ="";
    private RecyclerView mLvKetQua, mLvGoiY;
    private int mPageSize = 30;
    private int mPageNumber = 1;
    private double mLat=0.0;
    private double mLng=0.0;
    protected boolean flag_loading;
    private TextView mTvNoData;
    private ArrayList<DiemDenModel> mListDiemDen = new ArrayList<DiemDenModel>();
    private DiemDenAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager, mLinearLayoutManagerGoiy;
    private int mfirstVisibleItem=0;
    private int mTinhId =0;
    private String mTinhName="";
    private LinearLayout mLnLoadDing;
    private final int KEY_LOADING =1;
    private final int KEY_DATA =2;
    private final int KEY_NO_DATA =3;
    private RelativeLayout mRlFilter;
    private int mTagId;
    private int mQuanHuyenId;
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

    private LinearLayout mLnQuanHuyen;
    private TextView mTvQuanHuyen;
    private ArrayList<QuanHuyen> mQuanHuyens=new ArrayList<>();
    private QuanHuyenAdapter mQuanHuyenAdapter;
    private Dialog mDialogQuanHuyen;
    private int mTypeFilter =0;
    private TextView mTv0,mTv1, mTv2, mTv3, mTv4, mTvGoiYTimKiem;
    private ImageView mImg0, mImg1, mImg2, mImg3, mImg4;
    private RelativeLayout mRlLoadMore;
    private ArrayList<ReserveSearch> mGoiYTimKiems;
    private TextView mToolbarTitle;
    private GoiYTimKiemAdapter mGoiYTimKiemAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            bundle = savedInstanceState;
        if(bundle != null)
        {
            mTextSearch = bundle.getString(Constants.BUNDLE_KEY_SEARCH_TEXT,"");
            mTagId = bundle.getInt(Constants.BUNDLE_KEY__SEARCH_TAG_ID,0);
        }
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
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        mToolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(": "+mTextSearch);
        mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        this.initView();
        this.initControl();
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
        mLvKetQua = (RecyclerView) findViewById(R.id.kq_lv);
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

        mLvGoiY = (RecyclerView) findViewById(R.id.goi_y_lv);
        mTvNoData = (TextView) findViewById(R.id.tvNoData);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mLinearLayoutManagerGoiy = new LinearLayoutManager(mActivity);
        mLnLoadDing = (LinearLayout) findViewById(R.id.lnLoadDing);
        mRlFilter = (RelativeLayout)findViewById(R.id.filter_rl);
        mLnQuanHuyen =(LinearLayout)findViewById(R.id.quan_huyen_ln);
        mTvQuanHuyen = (TextView)findViewById(R.id.toolbar_quan_huyen);
        findViewById(R.id.tat_ca_rl).setOnClickListener(this);
        findViewById(R.id.muc_uu_dai_rl).setOnClickListener(this);
        findViewById(R.id.gan_ban_rl).setOnClickListener(this);
        findViewById(R.id.danh_gia_rl).setOnClickListener(this);
        mTv0 = (TextView)findViewById(R.id.tat_ca_tv);
        mTv1 = (TextView)findViewById(R.id.muc_uu_dai_tv);
        mTv2 = (TextView)findViewById(R.id.gan_ban_tv);
        mTv3 = (TextView)findViewById(R.id.danh_gia_tv);
        mTv4 = (TextView)findViewById(R.id.bo_loc_tv);
        mImg0 = (ImageView) findViewById(R.id.img0);
        mImg1 = (ImageView) findViewById(R.id.img1);
        mImg2 = (ImageView) findViewById(R.id.img2);
        mImg3 = (ImageView) findViewById(R.id.img3);
        mImg4 = (ImageView) findViewById(R.id.img4);
        mTvGoiYTimKiem =(TextView)findViewById(R.id.goi_y_tim_kiem_tv);
        mRlLoadMore = (RelativeLayout)findViewById(R.id.load_more_rl);
        mRlLoadMore.setOnClickListener(view -> {
            if (!flag_loading && NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
                int totalItemCountTruHeader = mListDiemDen.size();
                if (totalItemCountTruHeader % mPageSize == 0) {
                    flag_loading = true;
                    mPageNumber += 1;
                    setView(KEY_LOADING,mPageNumber);
                    timKiemDoiTac();
                }
            }
        });
        mTvGoiYTimKiem.setVisibility(View.GONE);
        mRlLoadMore.setVisibility(View.GONE);
        mLnQuanHuyen.setOnClickListener(v -> showDialogQuanHuyen());
        mRlFilter.setOnClickListener(this);
    }

    @Override
    protected void initControl() {
        super.initControl();
        handleUpdateFilter.sendEmptyMessage(KEY_LOAD_FILTER_BY_NHOM_KM_ID);
        mPageNumber =1;
        timKiemDoiTac();
        getQuanHuyenTimKiem();
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(Constants.BUNDLE_KEY__SEARCH_TAG_ID, mTagId);
        bundle.putInt(Constants.BUNDLE_KEY_SEARCH_QUAN_ID, mQuanHuyenId);
        bundle.putString(Constants.BUNDLE_KEY_SEARCH_TEXT, mTextSearch);
    }
    private synchronized void timKiemDoiTac() {
        setViewTag(mTypeFilter);
        if(mPageNumber==1)
            if (Pasgo.getInstance().prefs != null
                    && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
                mLat = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLatLocationRecent());
                mLng = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLngLocationRecent());
            }

        String url = WebServiceUtils.URL_TIM_KIEM_DOI_TAC(Pasgo
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
            jsonParams.put("quanId",mQuanHuyenId);
            jsonParams.put("tagId",mTagId);
            jsonParams.put("keySearch", mTextSearch);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("filter",!mIsFilter?"":mSFilter);
            jsonParams.put("typeFilter", mTypeFilter);
            jsonParams.put("pageNumber", pageNumber);
            jsonParams.put("pageSize", mPageSize);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(pageNumber==1)
            setView(KEY_LOADING, mPageNumber);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        flag_loading =false;
                        ArrayList<DiemDenModel> mList = ParserUtils.getSearchResultDiemDens(response);
                        mGoiYTimKiems = ParserUtils.getGoiYTimKiems(response);
                        if(pageNumber ==1)
                            mListDiemDen.clear();
                        mListDiemDen.addAll(mList);
                        if (mListDiemDen.size() == 0) {
                            setView(KEY_NO_DATA, pageNumber);
                        } else {
                            if(pageNumber==1) {
                                mLvKetQua.setAdapter(null);
                                setListAdapter(mListDiemDen);
                            }
                            else
                                mAdapter.updateList(mListDiemDen);
                            setView(KEY_DATA,pageNumber);
                        }
                        //load more
                        int totalItemCount = mListDiemDen.size();
                        if (totalItemCount > 0 && totalItemCount % mPageSize == 0) {
                            mRlLoadMore.setVisibility(View.VISIBLE);
                        }else
                            mRlLoadMore.setVisibility(View.GONE);
                        // goi y
                        if(mGoiYTimKiems.size()==0)
                            mTvGoiYTimKiem.setVisibility(View.GONE);
                        else
                            mTvGoiYTimKiem.setVisibility(View.VISIBLE);
                        if(mGoiYTimKiemAdapter==null)
                            setListSearch();
                        else
                            mGoiYTimKiemAdapter.updateList(mGoiYTimKiems);
                    }

                    @Override
                    public void onError(int maloi) {
                        try {
                            mProgressToolbar.setVisibility(View.GONE);
                            if(mPageNumber>2) mPageNumber--;
                            flag_loading =false;
                            if(mPageNumber==1)
                                setView(KEY_NO_DATA, mPageNumber);
                        }catch (Exception e)
                        {
                            Utils.Log(Pasgo.TAG,"load data error");
                        }
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            mProgressToolbar.setVisibility(View.GONE);
                            if(mPageNumber>2) mPageNumber--;
                            flag_loading =false;
                            if(mPageNumber==1)
                                setView(KEY_NO_DATA, mPageNumber);
                        }catch (Exception e)
                        {
                            Utils.Log(Pasgo.TAG,"load data error");
                        }
                    }
                });
    }
    private void setViewTag(int i)
    {
        mTv0.setTextColor(Utils.getColor(mContext,R.color.search_reserve_title));
        mTv1.setTextColor(Utils.getColor(mContext,R.color.search_reserve_title));
        mTv2.setTextColor(Utils.getColor(mContext,R.color.search_reserve_title));
        mTv3.setTextColor(Utils.getColor(mContext,R.color.search_reserve_title));
        mTv4.setTextColor(Utils.getColor(mContext,R.color.search_reserve_title));
        mImg0.setVisibility(View.GONE);
        mImg1.setVisibility(View.GONE);
        mImg2.setVisibility(View.GONE);
        mImg3.setVisibility(View.GONE);
        mImg4.setVisibility(View.GONE);
        switch (i)
        {
            case 0:
                mTv0.setTextColor(Utils.getColor(mContext,R.color.text_red_all_app));
                mImg0.setVisibility(View.VISIBLE);
                break;
            case 1:
                mTv1.setTextColor(Utils.getColor(mContext,R.color.text_red_all_app));
                mImg1.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTv2.setTextColor(Utils.getColor(mContext,R.color.text_red_all_app));
                mImg2.setVisibility(View.VISIBLE);
                break;
            case 3:
                mTv3.setTextColor(Utils.getColor(mContext,R.color.text_red_all_app));
                mImg3.setVisibility(View.VISIBLE);
                break;
            case 4:
                mTv4.setTextColor(Utils.getColor(mContext,R.color.text_red_all_app));
                mImg4.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void setView(final int i, int pageNumber){
        switch (i)
        {
            case KEY_LOADING:
                if(pageNumber ==1) {
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
                break;
            case KEY_NO_DATA:
                mLnLoadDing.setVisibility(View.GONE);
                mProgressToolbar.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.VISIBLE);
                break;
            case KEY_DATA:
                mLnLoadDing.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.VISIBLE);
                mProgressToolbar.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                break;
        }
    }

    private void setListSearch()
    {
        mGoiYTimKiemAdapter = new GoiYTimKiemAdapter(mContext,mGoiYTimKiems,new GoiYTimKiemListener(){

            @Override
            public void detail(int position) {
                mTagId = 0;
                mTextSearch = mGoiYTimKiems.get(position).getTuKhoa();
                mPageNumber =1;
                mToolbarTitle.setText(": "+mTextSearch);
                timKiemDoiTac();
            }
        });
        mLvGoiY.setAdapter(null);
        mLvGoiY.setLayoutManager(mLinearLayoutManagerGoiy);
        mLvGoiY.setAdapter(mGoiYTimKiemAdapter);

    }
    private void setListAdapter(ArrayList<DiemDenModel> listItemAdressFree) {

        int sizeList = listItemAdressFree.size();
        if (sizeList > 0) {
            mTvNoData.setVisibility(View.GONE);
            mLvKetQua.setVisibility(View.VISIBLE);
            flag_loading = false;
            mAdapter = new DiemDenAdapter(mActivity, listItemAdressFree,
                    new DiemDenAdapter.DiemDenListener() {
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
                        public void thePasgo(int position) {
                            DiemDenModel item = null;
                            if(mListDiemDen.size()>=position)
                            {
                                item = mListDiemDen.get(position);
                            }
                            if(item!=null)
                            {
                                Bundle bundle =new Bundle();
                                bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,item.getDoiTacKhuyenMaiId());
                                gotoActivityForResult(getApplicationContext(), ThePasgoActivity.class, bundle, Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ourLeftInLeft();
                            }
                        }
                    },false,true);
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
            ourLeftInLeft();
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
                    timKiemDoiTac();
                    break;
                case Constants.KEY_BACK_BY_DAT_CHO:
                    mPageNumber = 1;
                    mListDiemDen.clear();
                    mLvKetQua.setAdapter(null);
                    timKiemDoiTac();
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
            ourLeftInLeft();
            mIsClickCheckIn = false;
        }
    }
    private synchronized void getQuanHuyenTimKiem() {
        JSONObject jsonParams = new JSONObject();
        try {
            double viDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            double kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
            jsonParams.put("viDo", viDo);
            jsonParams.put("kinhDo", kinhDo);
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("tagId", mTagId);
            jsonParams.put("keySearch", mTextSearch);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(WebServiceUtils.URL_GET_QUAN_HUYEN_TIM_KIEM(Pasgo
                            .getInstance().token), jsonParams,
                    new Pasgo.PWListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mQuanHuyens = ParserUtils.getQuanHuyens(response);
                        }

                        @Override
                        public void onError(int maloi) {
                        }

                    }, error -> {

                    });
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
    private void showDialogQuanHuyen() {
        if (mQuanHuyens == null || mQuanHuyens.size() == 0)
            return;
        LayoutInflater inflater = mActivity.getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.quan_huyen));
        alertDialog.setView(dialog);
        final ListView lv = (ListView) dialog.findViewById(R.id.listView);
        mQuanHuyenAdapter = new QuanHuyenAdapter(mActivity, mQuanHuyens, new QuanHuyenAdapter.TinhHomeListenner() {
            @Override
            public void check(int position) {
                mQuanHuyenId = mQuanHuyens.get(position).getId();

                mPageNumber =1;
                timKiemDoiTac();
                mTvQuanHuyen.setText(mQuanHuyens.get(position).getTen());
                mQuanHuyenAdapter.notifyDataSetChanged();
                mDialogQuanHuyen.dismiss();
            }
        });
        lv.setAdapter(mQuanHuyenAdapter);
        if (mDialogQuanHuyen == null)
            mDialogQuanHuyen = alertDialog.create();
        if (mDialogQuanHuyen != null && !mDialogQuanHuyen.isShowing()) {
            mDialogQuanHuyen = alertDialog.create();
            mDialogQuanHuyen.show();
        }
    }
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
                    timKiemDoiTac();
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
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tat_ca_rl:
                mPageNumber = 1;
                mTypeFilter =0;
                timKiemDoiTac();
                break;
            case R.id.muc_uu_dai_rl:
                mPageNumber = 1;
                mTypeFilter =1;
                timKiemDoiTac();
                break;
            case R.id.gan_ban_rl:
                mPageNumber = 1;
                mTypeFilter =2;
                timKiemDoiTac();
                break;
            case R.id.danh_gia_rl:
                mPageNumber = 1;
                mTypeFilter =3;
                timKiemDoiTac();
                break;
            case  R.id.filter_rl:
                showDialogFilter();
                break;
        }
    }
}
