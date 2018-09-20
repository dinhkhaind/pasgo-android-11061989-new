package com.onepas.android.pasgo.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DanhMucChinh;
import com.onepas.android.pasgo.models.FilterCategoryItems;
import com.onepas.android.pasgo.models.FilterParent;
import com.onepas.android.pasgo.models.FilterView;
import com.onepas.android.pasgo.models.QuanHuyen;
import com.onepas.android.pasgo.models.Tinh;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.reserve.QuanHuyenAdapter;
import com.onepas.android.pasgo.ui.reserve.TinhAdapter;
import com.onepas.android.pasgo.utils.ExpandableHeightListView;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ExpandableHeightListView mLvKetQua;
    private LinearLayout mLnLoading;
    private LinearLayout mLnDisconnect;
    private final int KEY_LOADING =1;
    private final int KEY_DISCONNECT =2;
    private final int KEY_DATA =3;
    // category
    private CheckBox mChkDistanct, mChkDeals;
    private FilterPriceAdapter mFilterPriceAdapter;
    private RecyclerView mLvTag;
    public StaggeredGridLayoutManager mTagStaggeredGridLayoutManager;
    // filter
    // danh sach filter lay tren server ve
    private ArrayList<FilterParent> mFiltersList;
    // danh sach đầu tiên dùng cho phần popup popup: sau khi chọn nhóm khuyến mại sẽ lấy
    private ArrayList<FilterView> mFilterViews = new ArrayList<>();
    private ArrayList<FilterView> mFilterViewsSapXepChung = new ArrayList<>();
    private ArrayList<FilterView> mFilterViewsGia = new ArrayList<>();
    public String mSFilter = "";//
    private String mFilterFromSerer;
    private static final int KEY_LOAD_FILTER_BY_NHOM_KM_ID = 2;
    private FilterAdapter mFilterAdapter;
    // tỉnh
    private String mTinhName = "";
    private TextView mTvTinh;
    private ArrayList<TinhHome> mTinhs;
    private Dialog mDialogTinh;
    private TinhAdapter mTinhAdapter;
    // Quận
    private int mQuanHuyenId=0;
    private QuanHuyenAdapter mQuanHuyenAdapter;
    private Dialog mDialogQuanHuyen;
    private ArrayList<QuanHuyen> mQuanHuyens;
    private TextView mTvQuanHuyen;
    //// Quận
    private int mDanhMucId=-1;
    private DanhMucChinhAdapter mDanhMucChinhAdapter;
    private Dialog mDialogDanhMuc;
    private ArrayList<DanhMucChinh> mDanhMucChinhs;
    private TextView mTvDanhMucChinh;
    //
    private int mFilterNumber=0;
    private String mFilterViTri="";
    private String mFilterSearch="";
    private final int KEY_TINH = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home_filter);
        this.initView();
        this.initControl();
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        mProgressToolbar = (ProgressBar) mToolbar.findViewById(R.id.toolbar_progress_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mLvKetQua = (ExpandableHeightListView) findViewById(R.id.kq_lv);
        mLvKetQua.setExpanded(true);
        mLnLoading = (LinearLayout) findViewById(R.id.loading_ln);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnDisconnect = (LinearLayout) findViewById(R.id.lnDisconnect);
        mChkDistanct = (CheckBox) findViewById(R.id.check_distanct_chk);
        mChkDeals = (CheckBox) findViewById(R.id.check_deals_chk);
        mTvTinh = (TextView)findViewById(R.id.vi_tri_tv);
        mTvQuanHuyen = (TextView)findViewById(R.id.khu_vuc_tv);
        mTvDanhMucChinh = (TextView)findViewById(R.id.category_tv);
        findViewById(R.id.btnTryAGain).setOnClickListener(v ->
        {
            getFilterVoiNhonKhuyenMaiId();
        });
        findViewById(R.id.rlCheckDistanct).setOnClickListener(v->{
            mChkDistanct.setChecked(!mChkDistanct.isChecked());
            if(mChkDistanct.isChecked())
                mChkDeals.setChecked(!mChkDistanct.isChecked());

        });
        findViewById(R.id.rlCheckDeals).setOnClickListener(v->{
            mChkDeals.setChecked(!mChkDeals.isChecked());
            if(mChkDeals.isChecked())
                mChkDistanct.setChecked(!mChkDeals.isChecked());
        });
        mChkDistanct.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mChkDeals.setChecked(!isChecked);
            if(mFilterViewsSapXepChung.size()>2) {
                mFilterViewsSapXepChung.get(1).setCheck(true);
                mFilterViewsSapXepChung.get(2).setCheck(false);
            }
        });
        mChkDeals.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mChkDistanct.setChecked(!isChecked);
            if(mFilterViewsSapXepChung.size()>2) {
                mFilterViewsSapXepChung.get(1).setCheck(false);
                mFilterViewsSapXepChung.get(2).setCheck(true);
            }
        });
        findViewById(R.id.filter_btn).setOnClickListener(v -> {
            if(mDanhMucId<0)
            {
                getKhuVucBoLoc();
                return;
            }
            setCheckFilter();
            Gson gson = new Gson();
            mSFilter = (mFiltersList == null || mFiltersList.size() == 0) ? "" : "{\"Items\":" + gson.toJson(mFiltersList) + "}";
            mFilterViTri = mTvTinh.getText().toString()+" | " + mTvQuanHuyen.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUNDLE_KEY_FILTER_JSON, mSFilter);
            bundle.putInt(Constants.BUNDLE_KEY_QUAN_ID, mQuanHuyenId);
            bundle.putInt(Constants.BUNDLE_KEY_DANH_MUC_ID, mDanhMucId);
            bundle.putInt(Constants.BUNDLE_KEY_FILTER_NUMBER,mFilterNumber);
            bundle.putString(Constants.BUNDLE_KEY_FILTER_VITRI,mFilterViTri);
            bundle.putString(Constants.BUNDLE_KEY_FILTER_SEARCH,mFilterSearch);
            gotoActivity(mContext, FilterDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        });
        mLvTag = (RecyclerView) findViewById(R.id.price_rc);
        mTagStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        mLvTag.setLayoutManager(mTagStaggeredGridLayoutManager);
        findViewById(R.id.reset_ln).setOnClickListener(v -> {
            mQuanHuyenId = 0;
            mTvQuanHuyen.setText(getString(R.string.tat_ca));
            handleUpdateFilter.sendEmptyMessage(KEY_LOAD_FILTER_BY_NHOM_KM_ID);
        });
        findViewById(R.id.vi_tri_ln).setOnClickListener(v->{
            showDialogTinh();
        });
        findViewById(R.id.khu_vuc_ln).setOnClickListener(v->{
            showDialogQuanHuyen();
        });
        findViewById(R.id.category_ln).setOnClickListener(v->{
            showDialogDanhMuc();
        });
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void initControl() {
        super.initControl();
        handlerUpdateUI.sendEmptyMessage(0);
        getFilterVoiNhonKhuyenMaiId();
        getKhuVucBoLoc();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                default:
                    break;
            }
        }
    }

    private final Handler handleUpdateFilter = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case KEY_LOAD_FILTER_BY_NHOM_KM_ID:
                        try {
                            if (mFiltersList != null) {
                                mFiltersList.clear();
                            }
                            if (StringUtils.isEmpty(mFilterFromSerer)) {
                                getFilterVoiNhonKhuyenMaiId();
                                return;
                            }
                            JSONObject objFilter = new JSONObject(mFilterFromSerer);
                            mFiltersList = ParserUtils.getAllFilters(objFilter);
                            mFilterViews.clear();
                            mFilterViewsSapXepChung.clear();
                            mFilterViewsGia.clear();
                            for (FilterParent filterParent : mFiltersList) {

                                String parentName = filterParent.getNameEn();
                                if (Pasgo.getInstance().prefs.getLanguage().equals(Constants.LANGUAGE_VIETNAM)) {
                                    parentName = filterParent.getNameVn();
                                }
                                ArrayList<FilterView> filterViews = new ArrayList<>();
                                filterViews.add(new FilterView(-1, filterParent.getId(), parentName, false, true, filterParent.getChoiceType()));

                                for (FilterCategoryItems filterCategoryItems : filterParent.getFilterCategoryItems()) {
                                    String name = filterCategoryItems.getNameEn();
                                    if (Pasgo.getInstance().prefs.getLanguage().equals(Constants.LANGUAGE_VIETNAM)) {
                                        name = filterCategoryItems.getNameVn();
                                    }
                                    filterViews.add(new FilterView(filterCategoryItems.getId(), filterParent.getId(), name, false, false, filterParent.getChoiceType()));
                                }
                                if (filterParent.getCode().toLowerCase().equals(Constants.mCodeSapXepChung.toLowerCase())) {
                                    mFilterViewsSapXepChung.addAll(filterViews);
                                } else if (filterParent.getCode().toLowerCase().equals(Constants.mCodeGia.toLowerCase())) {
                                    mFilterViewsGia.addAll(filterViews);
                                } else {
                                    mFilterViews.addAll(filterViews);
                                }
                            }
                            // Sắp xếp chung
                            if(mFilterViewsSapXepChung.size()>2)
                            {
                                TextView tvNameDistanct = (TextView)findViewById(R.id.tvNameDistanct);
                                TextView tvNameDeals = (TextView)findViewById(R.id.tvNameDeals);
                                tvNameDeals.setSingleLine(true);
                                tvNameDistanct.setSingleLine(true);
                                tvNameDeals.setSelected(true);
                                tvNameDistanct.setSelected(true);
                                tvNameDistanct.setText(mFilterViewsSapXepChung.get(1).getName());
                                tvNameDeals.setText(mFilterViewsSapXepChung.get(2).getName());
                                // nếu ko cái nào dc check thì mặc định cho cái đầu tiên check
                                if(!mFilterViewsSapXepChung.get(1).isCheck() && !mFilterViewsSapXepChung.get(2).isCheck())
                                    mFilterViewsSapXepChung.get(1).setCheck(true);
                                mChkDistanct.setChecked(mFilterViewsSapXepChung.get(1).isCheck());
                                mChkDeals.setChecked(mFilterViewsSapXepChung.get(2).isCheck());
                            }
                            //
                            if (mFilterViewsGia.size() > 0) {
                                TextView tvGiaTrinhBinh = (TextView)findViewById(R.id.gia_trung_binh_tv);
                                tvGiaTrinhBinh.setText(mFilterViewsGia.get(0).getName());
                                mFilterViewsGia.remove(0);
                            }
                            if (mFilterPriceAdapter == null) {
                                mFilterPriceAdapter = new FilterPriceAdapter(mActivity, mFilterViewsGia, position -> {
                                    if (mFilterViewsGia.size() - 1 < position) return;
                                    if(mFilterViewsGia.get(position).isCheck())
                                        mFilterViewsGia.get(position).setCheck(false);
                                    else
                                        mFilterViewsGia = tagSetSelected(position, mFilterViewsGia);
                                    mFilterPriceAdapter.updateList(mFilterViewsGia);
                                });
                                mLvTag.setAdapter(mFilterPriceAdapter);
                            } else
                                mFilterPriceAdapter.updateList(mFilterViewsGia);
                            //
                            if (mFilterAdapter == null) {
                                mFilterAdapter = new FilterAdapter(mActivity, mFilterViews, (position, isCheck) -> {
                                    int choiceType = mFilterViews.get(position).getChoiceType();
                                    int parentId = mFilterViews.get(position).getParentId();
                                    if (choiceType == 1 && isCheck)// ==1 là chỉ được chọn 1
                                    {
                                        for (FilterView item : mFilterViews) {
                                            if (item.getParentId() == parentId)
                                                item.setCheck(false);
                                        }
                                    }
                                    mFilterViews.get(position).setCheck(isCheck);
                                    mFilterAdapter.notifyDataSetChanged();
                                    mLvKetQua.invalidateViews();
                                    mLvKetQua.refreshDrawableState();
                                });
                                mLvKetQua.setAdapter(mFilterAdapter);
                            }else
                                mFilterAdapter.updateList(mFilterViews);

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

    private void setCheckFilter()
    {
        mFilterNumber = 0;
        mFilterSearch ="";
        if(mFiltersList ==null || mFiltersList.size() ==0)
            return;
        // lưu lại danh sách check theo id của filterParent + category
        HashMap<String,Boolean> hashMaplist = new HashMap<>();

        if (mFilterViewsSapXepChung.size() > 0) {
            for (int i=0;i< mFilterViewsSapXepChung.size();i++)
            {
                hashMaplist.put(mFilterViewsSapXepChung.get(i).getParentId()+""+mFilterViewsSapXepChung.get(i).getId(),mFilterViewsSapXepChung.get(i).isCheck());
                if(mFilterViewsSapXepChung.get(i).isCheck()) {
                    mFilterNumber++;
                    mFilterSearch += StringUtils.isEmpty(mFilterSearch)?mFilterViewsSapXepChung.get(i).getName():" | "+mFilterViewsSapXepChung.get(i).getName();
                }
            }
        }
        if (mFilterViewsGia.size() > 0) {
            for (int i=0;i< mFilterViewsGia.size();i++)
            {
                hashMaplist.put(mFilterViewsGia.get(i).getParentId()+""+mFilterViewsGia.get(i).getId(),mFilterViewsGia.get(i).isCheck());
                if(mFilterViewsGia.get(i).isCheck()) {
                    mFilterNumber++;
                    mFilterSearch += StringUtils.isEmpty(mFilterSearch)?mFilterViewsGia.get(i).getName():" | "+mFilterViewsGia.get(i).getName();
                }
            }
        }
        if (mFilterViews.size() > 0) {
            for (int i=0;i< mFilterViews.size();i++)
            {
                hashMaplist.put(mFilterViews.get(i).getParentId()+""+mFilterViews.get(i).getId(),mFilterViews.get(i).isCheck());
                if(mFilterViews.get(i).isCheck()) {
                    mFilterNumber++;
                    mFilterSearch += StringUtils.isEmpty(mFilterSearch)?mFilterViews.get(i).getName():" | "+mFilterViews.get(i).getName();
                }
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
    }

    private void showDialogTinh() {
        if (mTinhs == null || mTinhs.size() == 0)
            return;
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.vi_tri));
        alertDialog.setView(dialog);
        final ListView lv = (ListView) dialog.findViewById(R.id.listView);
        mTinhAdapter = new TinhAdapter(mActivity, mTinhs, position -> {
            for (TinhHome item : mTinhs) {
                item.setIsCheck(false);
            }
            mTinhs.get(position).setIsCheck(true);
            Pasgo.getInstance().mTinhId = mTinhs.get(position).getId();
            if (Pasgo.getInstance().mTinhId > 0)
                Pasgo.getInstance().prefs.putTinhId(Pasgo.getInstance().mTinhId);
            mTinhName = mTinhs.get(position).getTen();
            mTinhAdapter.notifyDataSetChanged();
            lv.invalidateViews();
            lv.refreshDrawableState();
            handlerUpdateUI.sendEmptyMessage(1);
            mDialogTinh.dismiss();
        });
        lv.setAdapter(mTinhAdapter);
        if (mDialogTinh == null)
            mDialogTinh = alertDialog.create();
        if (mDialogTinh != null && !mDialogTinh.isShowing()) {
            mDialogTinh = alertDialog.create();
            mDialogTinh.show();
        }
    }

    Handler handlerUpdateUI = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String tinh = Pasgo.getInstance().prefs.getTinhMain();
                    if (!StringUtils.isEmpty(tinh)) {
                        try {
                            JSONObject jsonObjectTinh = new JSONObject(tinh);
                            mTinhs = ParserUtils.getTinhAllV1(jsonObjectTinh);
                            for (TinhHome item1 : mTinhs) {
                                item1.setIsCheck(false);
                            }
                            for (TinhHome item : mTinhs) {
                                if (item.getId() == Pasgo.getInstance().mTinhId) {
                                    mTinhName = item.getTen();
                                    mTvTinh.setText(mTinhName);
                                    item.setIsCheck(true);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case 1:
                    if (!StringUtils.isEmpty(mTinhName))
                        mTvTinh.setText(mTinhName);
                    handleLoadData.sendEmptyMessage(KEY_TINH);
                    updateTinhSelected();
                    break;
                default:
                    break;
            }
        }

        ;
    };
    protected Handler handleLoadData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY_TINH:
                    mProgressToolbar.setVisibility(View.VISIBLE);
                    if(mQuanHuyens!=null)
                        mQuanHuyens.clear();
                    if(mDanhMucChinhs!=null)
                        mDanhMucChinhs.clear();
                    mDanhMucId = -1;
                    mTvDanhMucChinh.setText("...");
                    mQuanHuyenId = 0;
                    mTvQuanHuyen.setText(getString(R.string.tat_ca));
                    getKhuVucBoLoc();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    // quận huyện
    private void showDialogQuanHuyen() {
        if (mQuanHuyens == null || mQuanHuyens.size() == 0)
            return;
        LayoutInflater inflater = mActivity.getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.khu_vuc));
        alertDialog.setView(dialog);
        final ListView lv = (ListView) dialog.findViewById(R.id.listView);
        mQuanHuyenAdapter = new QuanHuyenAdapter(mActivity, mQuanHuyens, position -> {
            mQuanHuyenId = mQuanHuyens.get(position).getId();
            mTvQuanHuyen.setText(mQuanHuyens.get(position).getTen());
            mQuanHuyens.get(position).setIsCheck(true);
            mQuanHuyens = quanHuyenSetSelected(position,mQuanHuyens);
            mQuanHuyenAdapter.notifyDataSetChanged();
            mDialogQuanHuyen.dismiss();
        });
        lv.setAdapter(mQuanHuyenAdapter);
        if (mDialogQuanHuyen == null)
            mDialogQuanHuyen = alertDialog.create();
        if (mDialogQuanHuyen != null && !mDialogQuanHuyen.isShowing()) {
            mDialogQuanHuyen = alertDialog.create();
            mDialogQuanHuyen.show();
        }
    }
    // quận huyện
    private void showDialogDanhMuc() {
        if (mDanhMucChinhs == null || mDanhMucChinhs.size() == 0)
            return;
        LayoutInflater inflater = mActivity.getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.danh_muc_chinh));
        alertDialog.setView(dialog);
        final ListView lv = (ListView) dialog.findViewById(R.id.listView);
        mDanhMucChinhAdapter = new DanhMucChinhAdapter(mActivity, mDanhMucChinhs, position -> {
            mDanhMucId = mDanhMucChinhs.get(position).getId();
            mTvDanhMucChinh.setText(mDanhMucChinhs.get(position).getTen());
            mDanhMucChinhs = danhMucChinhSetSelected(position,mDanhMucChinhs);
            mDanhMucChinhAdapter.notifyDataSetChanged();
            mDialogDanhMuc.dismiss();
        });
        lv.setAdapter(mDanhMucChinhAdapter);
        if (mDialogDanhMuc == null)
            mDialogDanhMuc = alertDialog.create();
        if (mDialogDanhMuc != null && !mDialogDanhMuc.isShowing()) {
            mDialogDanhMuc = alertDialog.create();
            mDialogDanhMuc.show();
        }
    }
    private synchronized void getFilterVoiNhonKhuyenMaiId() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nhomKhuyenMaiId", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setViewUI(KEY_LOADING);
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(WebServiceUtils.URL_GET_FILTER_VOI_NHOM_KHUYEN_MAI_ID(Pasgo
                            .getInstance().token), jsonParams,
                    new Pasgo.PWListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            setViewUI(KEY_DATA);
                            JSONObject objItemAll = ParserUtils.getJsonObject(response, "Item");
                            mFilterFromSerer = ParserUtils.getStringValue(objItemAll, "filter");
                            handleUpdateFilter.sendEmptyMessage(KEY_LOAD_FILTER_BY_NHOM_KM_ID);
                        }

                        @Override
                        public void onError(int maloi) {
                            setViewUI(KEY_DISCONNECT);
                        }

                    }, error -> {
                        setViewUI(KEY_DISCONNECT);
                    });
        }else
            setViewUI(KEY_DISCONNECT);
    }


    private void setViewUI(int i)
    {
        switch (i){
            case KEY_LOADING:
                mLnLoading.setVisibility(View.VISIBLE);
                mLnDisconnect.setVisibility(View.GONE);
                break;
            case KEY_DISCONNECT:
                mLnLoading.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.VISIBLE);
                break;
            case KEY_DATA:
                new Handler().postDelayed(() -> {
                    mLnLoading.setVisibility(View.GONE);
                    mLnDisconnect.setVisibility(View.GONE);
                },1000);
                break;
        }
    }
    private synchronized void getKhuVucBoLoc() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(WebServiceUtils.URL_GET_KHU_VUC_BO_LOC(Pasgo
                            .getInstance().token), jsonParams,
                    new Pasgo.PWListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mProgressToolbar.setVisibility(View.GONE);
                            mQuanHuyens= ParserUtils.getQuanHuyenFilters(mContext,response);
                            mDanhMucChinhs = ParserUtils.getDanhMucChinhs(response);
                            if(mDanhMucChinhs.size()>0) {
                                mDanhMucId = mDanhMucChinhs.get(0).getId();
                                mTvDanhMucChinh.setText(mDanhMucChinhs.get(0).getTen());
                                mDanhMucChinhs.get(0).setIsCheck(true);
                            }

                        }

                        @Override
                        public void onError(int maloi) {
                            mProgressToolbar.setVisibility(View.GONE);
                        }

                    }, error -> {
                        mProgressToolbar.setVisibility(View.GONE);
                    });
        }
    }

    //endregion
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
    public void onClick(View view) {

    }

    private ArrayList<FilterView> tagSetSelected(int position, ArrayList<FilterView> arrayList) {
        for (FilterView model : arrayList)
            model.setCheck(false);
        arrayList.get(position).setCheck(true);
        return arrayList;
    }
    private ArrayList<QuanHuyen> quanHuyenSetSelected(int position, ArrayList<QuanHuyen> arrayList) {
        for (QuanHuyen model : arrayList)
            model.setIsCheck(false);
        arrayList.get(position).setIsCheck(true);
        return arrayList;
    }
    private ArrayList<DanhMucChinh> danhMucChinhSetSelected(int position, ArrayList<DanhMucChinh> arrayList) {
        for (DanhMucChinh model : arrayList)
            model.setIsCheck(false);
        arrayList.get(position).setIsCheck(true);
        return arrayList;
    }
}