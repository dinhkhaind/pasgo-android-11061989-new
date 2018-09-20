package com.onepas.android.pasgo.ui.calleddrivers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DatTruoc;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceivedFragment extends BaseFragment {
    private View mRoot;
    private LinearLayout mLnErrorConnectNetwork;
    private LinearLayout mLayoutKoDuLieu;
    private LinearLayout mLayoutLoading;
    private RelativeLayout mRlData;
    private static final int KEY_LOADING = 0;
    private static final int KEY_NO_DATA = 1;
    private static final int KEY_DATA = 2;
    private ArrayList<DatTruoc> datTruocs;
    private ListView mLvData;
    private int mPageSize = 20;
    private int mPageNumber = 1;
    protected boolean mFlagLoading;
    private RelativeLayout mFooterView;
    private boolean mIsLoadFromOnCreate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsLoadFromOnCreate = true;
        setHasOptionsMenu(true);
        initControl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_da_nhan_layout, container, false);
        mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.dat_truoc));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft(mActivity));
        mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
        mLayoutKoDuLieu = (LinearLayout) mRoot.findViewById(R.id.lyKhongCoDuLieu);
        mLayoutLoading = (LinearLayout) mRoot.findViewById(R.id.lyLoading);
        mRlData = (RelativeLayout) mRoot.findViewById(R.id.lnData);
        mLvData = (ListView) mRoot.findViewById(R.id.lvData);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mFooterView = (RelativeLayout) mRoot.findViewById(R.id.load_more_footer);
        if (mRoot == null)
            return null;

        mLvData.setOnItemClickListener((parent, view, position, id) -> {
            if (NetworkUtils.getInstance(getActivity())
                    .isNetworkAvailable()) {
                lvClickItem(position);
            }
        });
        mLvData.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > mPageSize * (mPageNumber - 1) && totalItemCount % mPageSize == 0) {
                    if (mFlagLoading == false) {
                        mFooterView.setVisibility(View.VISIBLE);
                        mFlagLoading = true;
                        mPageNumber += 1;
                        getDanhSachDaNhan();
                    }
                }
            }
        });
        if (!mIsLoadFromOnCreate) {
            if (datTruocs.size() == 0) {
                handlerUpdateUI.sendEmptyMessage(0);
            } else
                setListAdapter(datTruocs);
        } else {
            handlerUpdateUI.sendEmptyMessage(0);
        }
        return mRoot;
    }

    protected android.os.Handler handlerUpdateUI = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mPageNumber = 1;
                    getDanhSachDaNhan();
                    break;
                case 1:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void lvClickItem(int position) {
        DatTruoc item = null;
        item = datTruocs.get(position);
        if (item != null) {
            Intent intent = new Intent(getActivity(), CalledDriverDetailActivity.class);
            intent.putExtra(Constants.BUNDLE_DAT_XE_ID, item.getDatXeId());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        onNetworkChanged();
    }

    @Override
    public void onNetworkChanged() {
        if (mLnErrorConnectNetwork != null) {
            if (NetworkUtils.getInstance(getActivity()).isNetworkAvailable()) {
                mLnErrorConnectNetwork.setVisibility(View.GONE);
            } else {
                mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initControl() {
        super.initControl();
        datTruocs = new ArrayList<DatTruoc>();
    }

    private void getDanhSachDaNhan() {
        String url = WebServiceUtils
                .URL_LICH_SU_DAT_TRUOC_DA_NHAN(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("pageNumber", mPageNumber);
            jsonParams.put("pageSize", mPageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mPageNumber == 1)
            setLayoutView(KEY_LOADING);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mIsLoadFromOnCreate = false;
                        ArrayList<DatTruoc> arrayList = ParserUtils.getDatTruocs(response);
                        if (mPageNumber == 1) datTruocs.clear();
                        datTruocs.addAll(arrayList);
                        setListAdapter(datTruocs);
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

    private void setListAdapter(ArrayList<DatTruoc> datTruocs) {
        int sizeList = datTruocs.size();
        if (sizeList > 0) {
            setLayoutView(KEY_DATA);
            CalledDriverAdapter adapter = new CalledDriverAdapter(getActivity(), datTruocs, true);
            int position = mLvData.getLastVisiblePosition();
            mLvData.setAdapter(adapter);
            if (mPageNumber > 1)
                mLvData.setSelectionFromTop(position, 0);
        } else {
            setLayoutView(KEY_NO_DATA);
        }
    }

    private void setLayoutView(int i) {
        mLayoutKoDuLieu.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mRlData.setVisibility(View.GONE);
        switch (i) {
            case KEY_LOADING:
                mLayoutLoading.setVisibility(View.VISIBLE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mRlData.setVisibility(View.GONE);
                break;
            case KEY_NO_DATA:
                mLayoutLoading.setVisibility(View.GONE);
                mLayoutKoDuLieu.setVisibility(View.VISIBLE);
                mRlData.setVisibility(View.GONE);
                break;
            case KEY_DATA:
                mLayoutLoading.setVisibility(View.GONE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mRlData.setVisibility(View.VISIBLE);
                mFooterView.setVisibility(View.GONE);
                mFlagLoading = false;
                break;
            default:
                break;
        }
    }
}