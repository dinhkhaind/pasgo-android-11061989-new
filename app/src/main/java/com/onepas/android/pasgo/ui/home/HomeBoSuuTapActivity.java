package com.onepas.android.pasgo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeBoSuuTapActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "HomeBoSuuTapActivity";

    private RecyclerView mLvKetQua;
    private final int mColumn = 2;
    private int mPageSize = 30;
    private int mPageNumber = 1;
    protected boolean flag_loading;
    private TextView mTvNoData;
    private ArrayList<HomeCategory> mCategories = new ArrayList<HomeCategory>();
    private HomeCategoryGridBoSuuTapAdapter mAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private int mfirstVisibleItem=0;
    private int mTinhId =0;
    private String mTinhName="";
    private LinearLayout mLnLoadDing;
    private final int KEY_LOADING =1;
    private final int KEY_DATA =2;
    private final int KEY_NO_DATA =3;
    private final int KEY_DISCONNECT =4;

    private int mGroupId;
    private String mGroupName;
    private LinearLayout mLnDisconnect;
    private RelativeLayout mFooterView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home_bo_suu_tap);
        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            bundle = savedInstanceState;
        if(bundle != null)
        {
            mGroupId = bundle.getInt(Constants.BUNDLE_KEY_GROUP_ID,0);
            mGroupName = bundle.getString(Constants.BUNDLE_KEY_GROUP_NAME,"");
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
        TextView mToolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(mGroupName);
        mToolbarTitle.setSingleLine(true);
        mToolbarTitle.setSelected(true);

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
        mLvKetQua.setNestedScrollingEnabled(false);
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
                int totalItemCount = mStaggeredGridLayoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    mfirstVisibleItem = firstVisibleItems[0]*mColumn;
                }
                if (!flag_loading && NetworkUtils.getInstance(mContext).isNetworkAvailable()) {
                    if (mfirstVisibleItem + visibleItemCount == totalItemCount
                            && totalItemCount > mPageSize * (mPageNumber - 1) && totalItemCount % mPageSize == 0) {
                        mFooterView.setVisibility(View.VISIBLE);
                        flag_loading = true;
                        mPageNumber += 1;
                        getDanhSachBoSuuTap();
                    }
                }
            }
        });

        mTvNoData = (TextView) findViewById(R.id.tvNoData);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(mColumn, StaggeredGridLayoutManager.VERTICAL);
        mLnLoadDing = (LinearLayout) findViewById(R.id.lnLoadDing);
        mLnDisconnect = (LinearLayout)findViewById(R.id.lnDisconnect);
        findViewById(R.id.btnTryAGain).setOnClickListener(v->{
            initControl();
        });
        findViewById(R.id.home_filter_ln).setOnClickListener(v->{
            gotoActivity(mActivity, FilterActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        });
    }

    @Override
    protected void initControl() {
        super.initControl();
        mPageNumber =1;
        getDanhSachBoSuuTap();
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(Constants.BUNDLE_KEY_GROUP_ID, mGroupId);
        bundle.putString(Constants.BUNDLE_KEY_GROUP_NAME, mGroupName);
    }
    private synchronized void getDanhSachBoSuuTap() {
        String url = WebServiceUtils.URL_GET_DANH_SACH_BO_SUU_TAP(Pasgo
                .getInstance().token);
        final int pageNumber =mPageNumber;
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            jsonParams.put("groupId",mGroupId);
            jsonParams.put("tinhId",mTinhId);
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
                        ArrayList<HomeCategory> mList = ParserUtils.getBoSuTaps(response);
                        if(pageNumber ==1)
                            mCategories.clear();
                        mCategories.addAll(mList);
                        if (mCategories.size() == 0) {
                            setViewUI(KEY_NO_DATA, pageNumber);
                        } else {
                            if(pageNumber==1) {
                                mLvKetQua.setAdapter(null);
                                setListAdapter(mCategories);
                            }
                            else
                                mAdapter.updateList(mCategories);
                            // set Data
                            mLvKetQua.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                                int numRow = mCategories.size() / mColumn;
                                if (mCategories.size() % mColumn > 0)
                                    numRow += 1;
                                calculateSwipeRefreshFullHeight(mLvKetQua, numRow);
                            });
                            setViewUI(KEY_DATA,pageNumber);
                        }
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

    private void setListAdapter(ArrayList<HomeCategory> listItems) {
        int sizeList = listItems.size();
        if (sizeList > 0) {
            mTvNoData.setVisibility(View.GONE);
            mLvKetQua.setVisibility(View.VISIBLE);
            flag_loading = false;
            mAdapter = new HomeCategoryGridBoSuuTapAdapter(mActivity, listItems, new HomeCategoryGridBoSuuTapAdapter.HomeCategoryListener() {
                @Override
                public void detail(int position) {
                    selectHomeCategoryItem(listItems.get(position));
                }
            });
            mLvKetQua.setLayoutManager(mStaggeredGridLayoutManager);
            mLvKetQua.setAdapter(mAdapter);
        } else {
            mLvKetQua.setVisibility(View.GONE);
            mFooterView.setVisibility(View.GONE);
        }
    }
    private void selectHomeCategoryItem(HomeCategory item) {
        if (item.isDoiTacKhuyenMai()) {
            String doiTacKMID = item.getDoiTacKhuyenMaiId();
            String title = item.getTieuDe();
            String chiNhanhDoiTacId = "";
            int nhomKhuyenMai = 3;//item.getNhomKhuyenMaiId()
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_DI_XE_FREE, Constants.KEY_INT_XE_FREE);
            bundle.putInt(Constants.KEY_TEN_TINH_ID, mTinhId);
            bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
            bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, doiTacKMID);
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
            bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
            bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, nhomKhuyenMai);
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getMoTa());
            bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
            bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
            bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
            gotoActivityForResult(mActivity, DetailActivity.class, bundle,
                    Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            gotoByTagOrText(item.getTagId(),item.getTieuDe(),item.getMoTa());
        }

    }
    private void gotoByTagOrText(int tagId, String tieuDe, String mota){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY_TAG_ID, tagId);
        bundle.putString(Constants.BUNDLE_KEY_SEARCH_TEXT, tieuDe);
        bundle.putString(Constants.BUNDLE_KEY_MO_TA, mota);
        gotoActivity(mActivity, HomeDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void calculateSwipeRefreshFullHeight(RecyclerView recyclerView, int column) {
        if(recyclerView ==null) return;
        int height = 0;
        for (int idx = 0; idx < column; idx++ ) {
            View v = recyclerView.getChildAt(idx);
            if(v!=null)
                height += v.getHeight();
        }
        ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
        params.height = height;
        recyclerView.setLayoutParams(params);
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
            case R.id.filter_rl:

                break;
        }
    }

}
