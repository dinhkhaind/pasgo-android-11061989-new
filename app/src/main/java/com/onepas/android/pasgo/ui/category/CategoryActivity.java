package com.onepas.android.pasgo.ui.category;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.listener.HidingScrollListener;
import com.onepas.android.pasgo.models.Category;
import com.onepas.android.pasgo.models.Tinh;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.reserve.TinhAdapter;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private RecyclerView mLvKetQua;
    private TextView mTvNoData;
    private LinearLayout mLnLoading;
    private LinearLayout mLnDisconnect;
    private Button mBtnTryAGain;
    // de khi quay lai lan 2: thi khong load lai neu co du lieu
    private CategoryAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private final int LOAD_DATA1 = 1;
    private final int NO_DATA = 2;
    private final int DISCONNECT = 3;
    ArrayList<Category> mCategories = new ArrayList<>();
    private String mTinhName = "";
    // tỉnh
    private TextView mTvTinh;
    private ArrayList<TinhHome> mTinhs;
    private Dialog mDialogTinh;
    private TinhAdapter mTinhAdapter;
    private int mTinhId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_category);
        getTinhAll();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());

        mLvKetQua = (RecyclerView) findViewById(R.id.kq_lv);
        mTvNoData = (TextView) findViewById(R.id.tvNoData);
        mLnLoading = (LinearLayout) findViewById(R.id.loading_ln);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLinearLayoutManager = new LinearLayoutManager(mContext);

        mLnDisconnect = (LinearLayout) findViewById(R.id.lnDisconnect);
        mBtnTryAGain = (Button) findViewById(R.id.btnTryAGain);
        mBtnTryAGain.setOnClickListener(v -> loadData());
        findViewById(R.id.tinh_ln).setOnClickListener(v -> {
            showDialogTinh();
        });
        mTvTinh = (TextView) findViewById(R.id.tinh_tv);
    }

    @Override
    public void onResume() {
        super.onResume();
        onNetworkChanged();
        if (Pasgo.getInstance().mTinhId == 0)
            Pasgo.getInstance().mTinhId = Pasgo.getInstance().prefs.getTinhId();
        if (mCategories.size() == 0 || (mTinhId != Pasgo.getInstance().mTinhId)) {
            mCategories.clear();
            loadData();
        } else
            setListAdapter(mCategories);
        handlerUpdateUI.sendEmptyMessage(0);
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
                default:
                    break;
            }
        }

        ;
    };

    private synchronized void loadData() {
        if (mCategories.size() == 0) {
            showKoDL(LOAD_DATA1);
        }

        String url = WebServiceUtils.URL_GET_DANH_SACH_DANH_MUC(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            if (StringUtils.isEmpty(Pasgo.getInstance().userId))
                Pasgo.getInstance().userId = "";
            mTinhId = Pasgo.getInstance().mTinhId;
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("parentId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mLnLoading.setVisibility(View.VISIBLE);
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mCategories = ParserUtils.getCategoryParents(response);
                            setListAdapter(mCategories);
                        }

                        @Override
                        public void onError(int maloi) {
                            try {
                                showKoDL(DISCONNECT);
                            } catch (Exception e) {
                                Utils.Log(Pasgo.TAG, "FragmentLove load data error");
                            }

                        }

                    }, error -> showKoDL(DISCONNECT));
        } else {
            showKoDL(DISCONNECT);
        }
    }

    private void setListAdapter(ArrayList<Category> listItem) {
        int sizeList = listItem.size();
        if (sizeList > 0) {
            mTvNoData.setVisibility(View.GONE);
            mLvKetQua.setVisibility(View.VISIBLE);
            mLnLoading.setVisibility(View.GONE);
            mAdapter = new CategoryAdapter(mActivity, listItem,
                    position -> {
                        Category item = null;
                        if (mCategories.size() > position) {
                            item = mCategories.get(position);
                        }
                        if (item != null) {
                            selectCategoryItem(item);
                        }
                    });
            mLvKetQua.setLayoutManager(mLinearLayoutManager);
            mLvKetQua.setAdapter(mAdapter);
            mLvKetQua.addOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    hideViews();
                }

                @Override
                public void onShow() {
                    showViews();
                }
            });
        } else {
            showKoDL(NO_DATA);
        }
    }

    private void selectCategoryItem(Category item) {

        if (item.getCountChild() > 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.BUNDLE_KEY_PARENT_ID, item.getId());
            bundle.putString(Constants.BUNDLE_KEY_PARENT_NAME, item.getTen());
            bundle.putString(Constants.BUNDLE_KEY_PARENT_ANH, item.getAnh());
            gotoActivity(mActivity, CategoryChildActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        } else {
            if (item.isDoiTacKhuyenMai()) {
                String doiTacKMID = item.getDoiTacKhuyenMaiId();
                String title = item.getTen();
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
                bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getTen());
                bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
                bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
                bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
                gotoActivityForResult(mActivity, DetailActivity.class, bundle,
                        Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_KEY_PARENT_ID, item.getParentId());
                bundle.putString(Constants.BUNDLE_KEY_PARENT_NAME, item.getTen());
                bundle.putInt(Constants.BUNDLE_KEY_TAG_ID, item.getTagId());
                gotoActivity(mActivity, CategoryDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft();
            }
        }
    }

    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
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

    private void showKoDL(final int i) {
        switch (i) {
            case LOAD_DATA1:
                mLnDisconnect.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mLnLoading.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.GONE);
                break;
            case NO_DATA:
                mLvKetQua.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.VISIBLE);
                mLnLoading.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.GONE);
                break;
            case DISCONNECT:
                mLvKetQua.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mLnLoading.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void showDialogTinh() {
        if (mTinhs == null || mTinhs.size() == 0)
            return;
        LayoutInflater inflater = mActivity.getLayoutInflater();
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
            if (Pasgo.getInstance().mTinhId > 0) {
                Pasgo.getInstance().prefs.putTinhId(Pasgo.getInstance().mTinhId);
                updateTinhSelected();
            }
            mTinhName = mTinhs.get(position).getTen();
            mTvTinh.setText(mTinhName);
            mTinhAdapter.notifyDataSetChanged();
            lv.invalidateViews();
            lv.refreshDrawableState();
            // load data
            mCategories.clear();
            if(mAdapter!=null)
                mAdapter.updateList(mCategories);
            loadData();
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

    private void getTinhAll() {
        String url = WebServiceUtils
                .URL_GET_TINH_ALLV1(Pasgo.getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();

        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Pasgo.getInstance().prefs.putTinhMain(response.toString());
                        if (mTinhs == null || mTinhs.size() == 0) {
                            mTinhs = ParserUtils.getTinhAllV1(response);
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

    //endregion
    @Override
    public void onNetworkChanged() {
        if (mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(mActivity).isNetworkAvailable())
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        else
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }
}