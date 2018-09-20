package com.onepas.android.pasgo.ui.reserve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.listener.HidingScrollListener;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
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

public class FragmentLove extends BaseFragmentCheckIn {

    private RecyclerView mLvKetQua;
    private View mRoot;
    private int mPageSize = 30;
    private int mPageNumber = 1;
    private double mLat = 0.0;
    private double mLng = 0.0;
    protected boolean flag_loading;
    private RelativeLayout mFooterView;
    private TextView mTvNoData;
    private LinearLayout mLnLoading;
    private LinearLayout mLnDisconnect;
    private Button mBtnTryAGain;
    // de khi quay lai lan 2: thi khong load lai neu co du lieu
    private ArrayList<DiemDenModel> mListDiemDen = new ArrayList<DiemDenModel>();
    private DiemDenAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mfirstVisibleItem = 0;

    private final int LOAD_DATA1 = 1;
    private final int NO_DATA = 2;
    private final int DISCONNECT = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        onNetworkChanged();
        if (mListDiemDen.size() == 0) {
            mPageNumber = 1;
            doiTacYeuThich();
        } else
            setListAdapter(mListDiemDen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_layout_reserve_love, container, false);
            mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
            mToolbar.setTitle("");
            TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(getString(R.string.yeu_thich));
            ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_action_back);
            mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft(getActivity()));
            mLvKetQua = (RecyclerView) mRoot.findViewById(R.id.kq_lv);
            mFooterView = (RelativeLayout) mRoot.findViewById(R.id.load_more_footer);
            mTvNoData = (TextView) mRoot.findViewById(R.id.tvNoData);
            mLnLoading = (LinearLayout) mRoot.findViewById(R.id.loading_ln);
            mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mLvKetQua.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int visibleItemCount = recyclerView.getChildCount();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    mfirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                    //Utils.Log(Pasgo.TAG, "firstVisibleItem1_" + mfirstVisibleItem);
                    if (!flag_loading && NetworkUtils.getInstance(getActivity()).isNetworkAvailable()) {
                        int totalItemCountTruHeader = totalItemCount - 1;
                        if (mfirstVisibleItem + visibleItemCount == totalItemCount
                                && totalItemCountTruHeader > mPageSize * (mPageNumber - 1) && totalItemCountTruHeader % mPageSize == 0) {
                            mFooterView.setVisibility(View.VISIBLE);
                            flag_loading = true;
                            mPageNumber += 1;
                            doiTacYeuThich();
                        }
                    }
                }
            });
            mLnDisconnect = (LinearLayout) mRoot.findViewById(R.id.lnDisconnect);
            mBtnTryAGain = (Button) mRoot.findViewById(R.id.btnTryAGain);
            mBtnTryAGain.setOnClickListener(v -> {
                mPageNumber = 1;
                doiTacYeuThich();
            });
        }

        return mRoot;
    }


    private synchronized void doiTacYeuThich() {
        if (mPageNumber == 1) {
            showKoDL(LOAD_DATA1);
            if (Pasgo.getInstance().prefs != null
                    && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
                mLat = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLatLocationRecent());
                mLng = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLngLocationRecent());
            }
        }

        String url = WebServiceUtils.URL_DOI_TAC_YEU_THICH(Pasgo
                .getInstance().token);
        final int pageNumber = mPageNumber;
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            if (StringUtils.isEmpty(Pasgo.getInstance().userId))
                Pasgo.getInstance().userId = "";
            jsonParams.put("viDo", mLat);
            jsonParams.put("kinhDo", mLng);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("pageNumber", pageNumber);
            jsonParams.put("pageSize", mPageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (pageNumber == 1)
            mLnLoading.setVisibility(View.VISIBLE);
        if (NetworkUtils.getInstance(getActivity()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            flag_loading = false;
                            ArrayList<DiemDenModel> mList = ParserUtils.getSearchDiemDens(response);
                            if (pageNumber == 1)
                                mListDiemDen.clear();
                            mListDiemDen.addAll(mList);

                            if(mPageNumber==1)
                                setListAdapter(mListDiemDen);
                            else
                                mAdapter.notifyDataSetChanged();
                            mFooterView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(int maloi) {
                            try {
                                mFooterView.setVisibility(View.GONE);
                                flag_loading = false;
                                if (mPageNumber > 2) mPageNumber--;
                                if (mPageNumber == 1)
                                    showKoDL(DISCONNECT);
                            }catch (Exception e)
                            {
                                Utils.Log(Pasgo.TAG,"FragmentLove load data error");
                            }

                        }

                    }, new Pasgo.PWErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                mFooterView.setVisibility(View.GONE);
                                flag_loading = false;
                                if (mPageNumber > 2) mPageNumber--;
                                if (getActivity().isFinishing()) return;
                                if (mPageNumber == 1)
                                    showKoDL(DISCONNECT);
                            }catch (Exception e)
                            {
                                Utils.Log(Pasgo.TAG,"FragmentLove load data error");
                            }

                        }
                    });
        } else {
            if (mPageNumber == 1)
                showKoDL(DISCONNECT);
        }
    }

    private void setListAdapter(ArrayList<DiemDenModel> listItemAdressFree) {
        int sizeList = listItemAdressFree.size();
        if (sizeList > 0) {
            mTvNoData.setVisibility(View.GONE);
            mLvKetQua.setVisibility(View.VISIBLE);
            mLnLoading.setVisibility(View.GONE);
            flag_loading = false;
            mAdapter = new DiemDenAdapter(mActivity, listItemAdressFree,
                    new DiemDenAdapter.DiemDenListener() {
                        @Override
                        public void checkIn(int position) {
                            // TODO Auto-generated method stub
                            DiemDenModel item = null;
                            if (mListDiemDen.size() >= position) {
                                item = mListDiemDen.get(position);
                                mPosition = position;
                                mNhomCNDoiTacId = item.getNhomCNDoiTacId();
                                mTenDoiTac = item.getTen();
                                mItemCheckIn = item;
                                if(item.getLoaiHopDong()>1) return;
                                initCheckIn();
                            }
                        }

                        @Override
                        public void detail(int position) {
                            DiemDenModel item = null;
                            if (mListDiemDen.size() >= position) {
                                item = mListDiemDen.get(position);
                            }
                            if (item != null) {
                                lvClickItem(item, 0, "", false, false);
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
                                gotoActivityForResult(getActivity(), DetailActivity.class, bundle,
                                        Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ourLeftInLeft(mActivity);
                            }
                        }
                        @Override
                        public void thePasgo(int position) {
                            DiemDenModel item = null;
                            if (mListDiemDen.size() >= position) {
                                item = mListDiemDen.get(position);
                            }
                            if (item != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, item.getDoiTacKhuyenMaiId());
                                gotoActivityForResult(getActivity().getApplicationContext(), ThePasgoActivity.class, bundle, Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ourLeftInLeft(mActivity);
                            }
                        }
                    }, false,false);
            mLvKetQua.setAdapter(mAdapter);
            mLvKetQua.setLayoutManager(new LinearLayoutManager(getActivity()));
            mLvKetQua.setLayoutManager(mLinearLayoutManager);
            mLvKetQua.setAdapter(mAdapter);
            if (mPageNumber > 1)
                mLvKetQua.getLayoutManager().scrollToPosition(mfirstVisibleItem);
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
                case Constants.KEY_BACK_BY_MA_DAT_CHO:
                    // load lai du lieu neu dat cho theo Doi tac km
                    mPageNumber = 1;
                    mListDiemDen.clear();
                    mAdapter.notifyDataSetChanged();
                    doiTacYeuThich();
                    break;
                case Constants.KEY_BACK_BY_DAT_CHO:
                    mPageNumber = 1;
                    mListDiemDen.clear();
                    mAdapter.notifyDataSetChanged();
                    doiTacYeuThich();
                    break;

                default:
                    break;
            }
        }
    }

    private void showKoDL(final int i) {
        switch (i) {
            case LOAD_DATA1:
                mFooterView.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mLnLoading.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.GONE);
                break;
            case NO_DATA:
                mFooterView.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.VISIBLE);
                mLnLoading.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.GONE);
                break;
            case DISCONNECT:
                mFooterView.setVisibility(View.GONE);
                mLvKetQua.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mLnLoading.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.VISIBLE);
                break;

        }
    }

    //endregion
    @Override
    public void onNetworkChanged() {
        if (getActivity() == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(getActivity()).isNetworkAvailable())
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        else
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
    }
}