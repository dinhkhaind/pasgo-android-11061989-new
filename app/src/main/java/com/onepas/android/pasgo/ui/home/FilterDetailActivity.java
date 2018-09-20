package com.onepas.android.pasgo.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;
import com.onepas.android.pasgo.ui.reserve.ReserveDetailActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterDetailActivity extends BaseAppCompatActivity{
    private static final String TAG = "HomeDetailActivity";

    private RecyclerView mLvKetQua;
    private int mPageSize = 30;
    private int mPageNumber = 1;
    private double mLat=0.0;
    private double mLng=0.0;
    protected boolean flag_loading;
    private TextView mTvNoData;
    private ArrayList<DiemDenModel> mListDiemDen = new ArrayList<DiemDenModel>();
    private HomeDetailAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mfirstVisibleItem=0;
    private LinearLayout mLnLoadDing;
    private final int KEY_LOADING =1;
    private final int KEY_DATA =2;
    private final int KEY_NO_DATA =3;
    private final int KEY_DISCONNECT =4;
    public String mSFilter="";
    private int mQuanHuyenId=0;
    private int mDanhMucId=0;
    private int mFilterNumber=0;
    private String mFilterSearch="";
    private String mFilterViTri = "";
    private final int KEY_FILTER=1;
    private TextView mToolbarTitle;
    private TextView mToolbarResult;
    private RelativeLayout mFooterView;

    private LinearLayout mLnDisconnect;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home_filter_detail);
        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            bundle = savedInstanceState;
        if(bundle != null)
        {
            mSFilter = bundle.getString(Constants.BUNDLE_KEY_FILTER_JSON,"");
            mQuanHuyenId = bundle.getInt(Constants.BUNDLE_KEY_QUAN_ID,0);
            mDanhMucId = bundle.getInt(Constants.BUNDLE_KEY_DANH_MUC_ID,0);
            mFilterNumber = bundle.getInt(Constants.BUNDLE_KEY_FILTER_NUMBER,0);
            mFilterSearch = bundle.getString(Constants.BUNDLE_KEY_FILTER_SEARCH,"");
            mFilterViTri = bundle.getString(Constants.BUNDLE_KEY_FILTER_VITRI,"");
        }
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        mToolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        mToolbarResult = (TextView)mToolbar.findViewById(R.id.toolbar_result);
        mToolbarTitle.setText(getString(R.string.kq_loc));
        mToolbarTitle.setSingleLine(true);
        mToolbarTitle.setSelected(true);
        TextView tvFilterNumber = (TextView) mToolbar.findViewById(R.id.filter_number_tv);
        tvFilterNumber.setText(mFilterNumber+"");
        TextView tvFilterSearch =(TextView)findViewById(R.id.filter_search_tv);
        tvFilterSearch.setText(mFilterSearch);
        TextView tvFilterViTri =(TextView)findViewById(R.id.filter_vitri_tv);
        tvFilterViTri.setText(mFilterViTri);
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
        mFooterView = (RelativeLayout) findViewById(R.id.load_more_footer);
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
        mLvKetQua.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                mfirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (!flag_loading && NetworkUtils.getInstance(mContext).isNetworkAvailable()) {
                    if (mfirstVisibleItem + visibleItemCount == totalItemCount
                            && totalItemCount > mPageSize * (mPageNumber - 1) && totalItemCount % mPageSize == 0) {
                        mFooterView.setVisibility(View.VISIBLE);
                        flag_loading = true;
                        mPageNumber += 1;
                        getChiTietDanhMuc();
                    }
                }
            }
        });

        mTvNoData = (TextView) findViewById(R.id.tvNoData);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mLnLoadDing = (LinearLayout) findViewById(R.id.lnLoadDing);
        findViewById(R.id.filter_rl).setOnClickListener(v->{
            finishToRightToLeft();
        });
        mLnDisconnect = (LinearLayout)findViewById(R.id.lnDisconnect);
        findViewById(R.id.btnTryAGain).setOnClickListener(v->{
            initControl();
        });
    }

    @Override
    protected void initControl() {
        super.initControl();
        handleLoadData.sendEmptyMessage(KEY_FILTER);
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.BUNDLE_KEY_FILTER_JSON, mSFilter);
        bundle.putInt(Constants.BUNDLE_KEY_QUAN_ID,mQuanHuyenId);
        bundle.putInt(Constants.BUNDLE_KEY_DANH_MUC_ID,mDanhMucId);
        bundle.putInt(Constants.BUNDLE_KEY_FILTER_NUMBER, mFilterNumber);
        bundle.putString(Constants.BUNDLE_KEY_FILTER_SEARCH, mFilterSearch);
        bundle.putString(Constants.BUNDLE_KEY_FILTER_VITRI, mFilterViTri);
    }
    private synchronized void getChiTietDanhMuc() {
        if(mPageNumber==1)
            if (Pasgo.getInstance().prefs != null
                    && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
                mLat = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLatLocationRecent());
                mLng = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLngLocationRecent());
            }

        String url = WebServiceUtils.URL_GET_CHI_TIET_BO_LOC(Pasgo
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
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("quanId",mQuanHuyenId);
            jsonParams.put("danhMucId",mDanhMucId);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("filter",mSFilter);
            jsonParams.put("pageNumber", pageNumber);
            jsonParams.put("pageSize", mPageSize);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(pageNumber==1)
            setViewUI(KEY_LOADING, mPageNumber);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        flag_loading =false;
                        ArrayList<DiemDenModel> mList = ParserUtils.getHomeDetailDiemDens(response);
                        if(pageNumber ==1)
                            mListDiemDen.clear();
                        mListDiemDen.addAll(mList);
                        if (mListDiemDen.size() == 0) {
                            setViewUI(KEY_NO_DATA, pageNumber);
                        } else {
                            if(pageNumber==1) {
                                mLvKetQua.setAdapter(null);
                                setListAdapter(mListDiemDen);
                            }
                            else
                                mAdapter.updateList(mListDiemDen);
                            setViewUI(KEY_DATA,pageNumber);
                        }
                        JSONObject objItem = ParserUtils.getJsonObject(response,"Item");
                        mToolbarResult.setText(String.format(getString(R.string.ket_qua_title),ParserUtils.getIntValue(objItem,"TongSo")+""));
                    }

                    @Override
                    public void onError(int maloi) {
                        try {
                            mFooterView.setVisibility(View.GONE);
                            if(mPageNumber>2) mPageNumber--;
                            flag_loading =false;
                            if(mPageNumber==1)
                                setViewUI(KEY_DISCONNECT, mPageNumber);
                        }catch (Exception e)
                        {
                            Utils.Log(Pasgo.TAG,"load data error");
                        }
                    }

                }, error -> {
                    try {
                        mFooterView.setVisibility(View.GONE);
                        if(mPageNumber>2) mPageNumber--;
                        flag_loading =false;
                        if(mPageNumber==1)
                            setViewUI(KEY_DISCONNECT, mPageNumber);
                    }catch (Exception e)
                    {
                        Utils.Log(Pasgo.TAG,"load data error");
                    }
                });
    }

    private void setViewUI(final int i, int pageNumber){
        switch (i)
        {
            case KEY_LOADING:
                if(pageNumber ==1) {
                    mLnLoadDing.setVisibility(View.VISIBLE);
                    mFooterView.setVisibility(View.GONE);
                    mLvKetQua.setVisibility(View.GONE);
                }else
                {
                    mLvKetQua.setVisibility(View.VISIBLE);
                    mLnLoadDing.setVisibility(View.GONE);
                    mFooterView.setVisibility(View.VISIBLE);
                }
                mTvNoData.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.GONE);
                break;
            case KEY_NO_DATA:
                mLnLoadDing.setVisibility(View.GONE);
                mFooterView.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.VISIBLE);
                mLnDisconnect.setVisibility(View.GONE);
                break;
            case KEY_DATA:
                mLnLoadDing.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.VISIBLE);
                mFooterView.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.GONE);
                break;
            case KEY_DISCONNECT:
                mLnLoadDing.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.GONE);
                mFooterView.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setListAdapter(ArrayList<DiemDenModel> listItemAdressFree) {
        int sizeList = listItemAdressFree.size();
        if (sizeList > 0) {
            mTvNoData.setVisibility(View.GONE);
            mLvKetQua.setVisibility(View.VISIBLE);
            flag_loading = false;
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
                                gotoActivityForResult(getApplicationContext(), ThePasgoActivity.class, bundle, Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ourLeftInLeft();
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
            mFooterView.setVisibility(View.GONE);
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
                    getChiTietDanhMuc();
                    break;
                case Constants.KEY_BACK_BY_DAT_CHO:
                    mPageNumber = 1;
                    mListDiemDen.clear();
                    mLvKetQua.setAdapter(null);
                    getChiTietDanhMuc();
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

    protected Handler handleLoadData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY_FILTER:
                    mPageNumber = 1;
                    mLvKetQua.setVisibility(View.GONE);
                    getChiTietDanhMuc();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }


}
