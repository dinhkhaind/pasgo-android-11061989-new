package com.onepas.android.pasgo.ui.pasgocard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DanhSachTaiTroDiemDen;
import com.onepas.android.pasgo.models.MaDatXe;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.calldriver.DatXeActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.DatxeDiemDenAdapter.DanhSachTaiTroDienDenListener;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatXeDiemDenActivity extends BaseAppCompatActivity {
    private final String TAG = "DanhSachDiemDenActivity";
    private LinearLayout mLayoutKoDuLieu;
    private ListView mListView;
    private ArrayList<DanhSachTaiTroDiemDen> mLists = new ArrayList<DanhSachTaiTroDiemDen>();
    private TextView mKoCoDL;
    private Dialog dialog;
    private String mMaKhuyenMai = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_danh_sach_tai_tro_diem_den);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.dat_xe);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mProgressToolbar =(ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null)
            mMaKhuyenMai = savedInstanceState
                    .getString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI);
        if (bundle != null)
            mMaKhuyenMai = bundle.getString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI);
        initView();
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
        // TODO Auto-generated method stub
        super.initView();
        mListView = (ListView) findViewById(R.id.listData);
        mLayoutKoDuLieu = (LinearLayout) findViewById(R.id.lyKhongCoThongBao);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mKoCoDL = (TextView) findViewById(R.id.tvKhongCoDuLieu);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, mMaKhuyenMai);
    }

    @Override
    protected void initControl() {
        // TODO Auto-generated method stub
        super.initControl();

        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            getDanhSachMaKhuyenMai();
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
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

    private void setListAdapter(
            ArrayList<DanhSachTaiTroDiemDen> listItemAdressFree) {
        int sizeList = listItemAdressFree.size();
        if (sizeList > 0) {
            mLayoutKoDuLieu.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            DatxeDiemDenAdapter adapter = new DatxeDiemDenAdapter(
                    mContext, listItemAdressFree,
                    new DanhSachTaiTroDienDenListener() {

                        @Override
                        public void xemThem(int position) {
                            // TODO Auto-generated method stub
                            DanhSachTaiTroDiemDen item = null;
                            if (mLists.size() > 0) {
                                item = mLists.get(position);
                            }
                            viewMore(item);
                        }

                        @Override
                        public void diemDonOrDen(int position, boolean isDiemDen) {
                            // TODO Auto-generated method stub
                            DanhSachTaiTroDiemDen item = null;
                            if (mLists.size() > 0) {
                                item = mLists.get(position);
                            }
                            goNow(item,isDiemDen);
                        }
                    });
            int position = mListView.getLastVisiblePosition();
            mListView.setAdapter(adapter);
            mListView.setSelectionFromTop(position, 0);
        } else {
            mLayoutKoDuLieu.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }

    private void viewMore(DanhSachTaiTroDiemDen item) {
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.BUNDLE_KEY_VI_DO, item.getViDo());
        bundle.putDouble(Constants.BUNDLE_KEY_KINH_DO, item.getKinhDo());
        bundle.putString(Constants.BUNDLE_KEY_ID, item.getChiNhanhId());
        bundle.putString(Constants.BUNDLE_KEY_TEN, item.getTenChiNhanh());
        bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaChi());
        bundle.putString(Constants.BUNDLE_KEY_LINK_WEBSITE, item.getWebsite());
        bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID,
                item.getNhomCNDoiTacId());
        bundle.putInt(Constants.BUNDLE_KEY_DAT_TRUOC, item.getDatTruoc());
        bundle.putBoolean(Constants.BUNDLE_KEY_FREE_IS_DEN, true);
        bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, mMaKhuyenMai);
        bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM,
                item.getTenChiNhanh());
        bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, item.getDoiTacKhuyenMaiId());
        bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, true);
        bundle.putBoolean(Constants.BUNDLE_KEY_DANH_SACH_DIEM_DEN, true);
        gotoActivityForResult(mContext, DetailActivity.class, bundle,
                Constants.KEY_BACK_BY_MA_DAT_XE, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
    }

    private void goNow(DanhSachTaiTroDiemDen item, boolean isDiemDen) {
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.BUNDLE_KEY_VI_DO, item.getViDo());
        bundle.putDouble(Constants.BUNDLE_KEY_KINH_DO, item.getKinhDo());
        bundle.putString(Constants.BUNDLE_KEY_NOI_DUNG_KM, "");
        bundle.putString(Constants.BUNDLE_KEY_ID, item.getChiNhanhId());
        String tenDoiTacKM = item.getTenChiNhanh();
        bundle.putString(Constants.BUNDLE_KEY_TEN, tenDoiTacKM);
        bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaChi());
        bundle.putString(Constants.BUNDLE_KEY_LINK_WEBSITE, item.getWebsite());
        bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID,
                item.getNhomCNDoiTacId());
        bundle.putInt(Constants.BUNDLE_KEY_DAT_TRUOC, item.getDatTruoc());
        bundle.putBoolean(Constants.BUNDLE_KEY_FREE_IS_DEN, isDiemDen);
        bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, mMaKhuyenMai);
        bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, true);
        gotoActivityForResult(mContext, DatXeActivity.class, bundle,
                Constants.KEY_BACK_BY_MA_DAT_XE, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(TAG, "requestCode: " + requestCode + "resultCode: " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle;
            switch (requestCode) {
                case Constants.KEY_BACK_BY_MA_DAT_XE:
                    Intent intent = new Intent(mContext, MaDatXe.class);
                    setResult(RESULT_OK, intent);
                    finishToRightToLeft();
                    break;
                default:
                    break;
            }
        }
    }

    private void getDanhSachMaKhuyenMai() {
        double KINH_DO_MAC_DINH = 105.8500000;
        double VI_DO_MAC_DINH = 21.0333330;
        double viDo, kinhDo;
        viDo = VI_DO_MAC_DINH;
        kinhDo = KINH_DO_MAC_DINH;
        if (Pasgo.getInstance() != null
                && Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
            viDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        String url = WebServiceUtils.URL_GET_DANH_SACH_DIEM_DEN(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            jsonParams.put("viDo", viDo);
            jsonParams.put("kinhDo", kinhDo);
            jsonParams.put("maKhuyenMai", mMaKhuyenMai);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            mProgressToolbar.setVisibility(View.VISIBLE);
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<DanhSachTaiTroDiemDen> lists = ParserUtils
                                    .getDanhSachDiemDen(response);
                            mLists.addAll(lists);
                            if (mLists.size() > 0) {
                                mLayoutKoDuLieu.setVisibility(View.GONE);
                                mListView.setVisibility(View.VISIBLE);
                                mProgressToolbar.setVisibility(View.GONE);
                                setListAdapter(mLists);
                            } else {
                                showKoDL();
                            }
                            mLnErrorConnectNetwork.setVisibility(View.GONE);
                            mProgressToolbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(int maloi) {
                        }

                    }, new PWErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mProgressToolbar.setVisibility(View.GONE);
                            handlerNetwork.sendEmptyMessage(0);
                        }
                    });
        } else {
            showKoDL();
        }
    }



    Handler handlerNetwork = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (isFinishing() || getBaseContext() == null)
                        return;
                    showAlertMangYeu();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private void showAlertMangYeu() {

        if (!isFinishing()) {
            if (dialog == null) {
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_mang_yeu);
                dialog.setCancelable(false);
            }
            Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);// ket_noi_mang_yeu_and_connecttoserver
            Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
            TextView tv = (TextView) dialog.findViewById(R.id.content);
            tv.setText(getString(R.string.ket_noi_mang_yeu_and_connecttoserver));
            dialogBtThuLai.setOnClickListener(v -> {
                getDanhSachMaKhuyenMai();
                dialog.dismiss();
            });

            dialogBtHuy.setOnClickListener(v -> {
                dialog.dismiss();
                finishToRightToLeft();
            });

            if (dialog!=null && !dialog.isShowing()&& !isFinishing()) {
                dialog.show();
            }
        }
    }

    private void showKoDL() {
        mLayoutKoDuLieu.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mKoCoDL.setText("" + getString(R.string.chua_co_diem_nao));
        mProgressToolbar.setVisibility(View.GONE);
    }

    @Override
    public void onNetworkChanged() {

        if (mLnErrorConnectNetwork != null) {

            if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
                mLnErrorConnectNetwork.setVisibility(View.GONE);
            } else {
                mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
    }
}