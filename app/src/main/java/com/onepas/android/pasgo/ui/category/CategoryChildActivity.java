package com.onepas.android.pasgo.ui.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.listener.HidingScrollListener;
import com.onepas.android.pasgo.models.Category;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryChildActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private RecyclerView mLvKetQua;
    private TextView mTvNoData;
    private LinearLayout mLnLoading;
    private LinearLayout mLnDisconnect;
    private Button mBtnTryAGain;
    // de khi quay lai lan 2: thi khong load lai neu co du lieu
    private CategoryChildAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private final int LOAD_DATA1 = 1;
    private final int NO_DATA = 2;
    private final int DISCONNECT = 3;
    ArrayList<Category> mCategories =new ArrayList<>();
    private int mParentId=0;
    private String mParentName="";
    private String mParentAnh="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home_category_child);
        Bundle bundle = getIntent().getExtras();
        if(bundle ==null)
            bundle = savedInstanceState;
        if(bundle!=null)
        {
            mParentId = bundle.getInt(Constants.BUNDLE_KEY_PARENT_ID,0);
            mParentName = bundle.getString(Constants.BUNDLE_KEY_PARENT_NAME,"");
            mParentAnh =  bundle.getString(Constants.BUNDLE_KEY_PARENT_ANH,"");
        }
        this.initView();
        this.initControl();
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(mParentName);
        mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mLvKetQua = (RecyclerView) findViewById(R.id.kq_lv);
        mTvNoData = (TextView) findViewById(R.id.tvNoData);
        mLnLoading = (LinearLayout) findViewById(R.id.loading_ln);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);

        mLnDisconnect = (LinearLayout) findViewById(R.id.lnDisconnect);
        mBtnTryAGain = (Button) findViewById(R.id.btnTryAGain);
        mBtnTryAGain.setOnClickListener(v -> loadData());
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
        outState.putInt(Constants.BUNDLE_KEY_PARENT_ID, mParentId);
        outState.putString(Constants.BUNDLE_KEY_PARENT_NAME, mParentName);
        outState.putString(Constants.BUNDLE_KEY_PARENT_ANH, mParentAnh);
    }

    @Override
    protected void initControl() {
        super.initControl();
        loadData();
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
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("parentId", mParentId);
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
                            Category model =new Category();
                            model.setTen(getString(R.string.tat_ca));
                            model.setTagId(-1);
                            model.setParentId(mParentId);
                            model.setAnh(mParentAnh);
                            mCategories.add(0,model);
                            setListAdapter(mCategories);
                        }

                        @Override
                        public void onError(int maloi) {
                            try {
                                showKoDL(DISCONNECT);
                            }catch (Exception e)
                            {
                                Utils.Log(Pasgo.TAG,"FragmentLove load data error");
                            }

                        }

                    }, error -> {
                        try {
                            showKoDL(DISCONNECT);
                        }catch (Exception e)
                        {
                            Utils.Log(Pasgo.TAG,"FragmentLove load data error");
                        }

                    });
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
            mAdapter = new CategoryChildAdapter(mActivity, listItem,
                    position -> {
                        Category item = null;
                        if (mCategories.size() >= position) {
                            item = mCategories.get(position);
                        }
                        if (item != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.BUNDLE_KEY_PARENT_ID, item.getParentId());
                            bundle.putString(Constants.BUNDLE_KEY_PARENT_NAME, position==0?mParentName:item.getTen());
                            bundle.putInt(Constants.BUNDLE_KEY_TAG_ID, item.getTagId());
                            gotoActivity(mActivity, CategoryDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ourLeftInLeft();
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
}