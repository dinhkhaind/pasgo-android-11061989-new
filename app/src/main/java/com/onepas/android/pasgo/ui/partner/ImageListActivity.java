package com.onepas.android.pasgo.ui.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhList;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageListActivity extends BaseAppCompatActivity {
    private RecyclerView mRcImage;
    private String mDoiTacKhuyenMaiId="";
    private ArrayList<AnhList> mAnhLists;
    private TextView mToolbarTitle;
    private String mJson="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        mRcImage = (RecyclerView)findViewById(R.id.list_image_rc);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mProgressToolbar = (ProgressBar) mToolbar.findViewById(R.id.toolbar_progress_bar);
        mProgressToolbar.setVisibility(ProgressBar.GONE);
        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            bundle = savedInstanceState;
        if(bundle!=null) {
            mDoiTacKhuyenMaiId = bundle.getString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID,"");
            getChiTietAnhDoiTac();
        }
        mToolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("");
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
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID,mDoiTacKhuyenMaiId);
    }

    private void getChiTietAnhDoiTac() {
        String url = WebServiceUtils
                .URL_GET_CHI_TIET_ANH_DOI_TAC(Pasgo
                        .getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("doiTacKhuyenMaiId", mDoiTacKhuyenMaiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mJson = response.toString();
                        JSONArray jsonArray = ParserUtils.getJsonArray(response,"Items");
                        mAnhLists = ParserUtils.getAnhLists(jsonArray);
                        if(mAnhLists.size()>0)
                            mToolbarTitle.setText(String.format(getString(R.string.title_photos),mAnhLists.get(0).getTongSo()+""));
                        ImageListAdapter mAdapter;
                        mAdapter = new ImageListAdapter(mActivity, mAnhLists, new ImageListAdapter.ImageListListener() {
                            @Override
                            public void detail(int position) {
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.IMAGE_LIST_OBJECT, mJson);
                                bundle.putInt(Constants.IMAGE_LIST_NUMBER, position);
                                gotoActivity(mContext, ImageListDetailActivity.class,bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ourLeftInLeft();
                            }
                        });

                        mRcImage.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                        mRcImage.setAdapter(mAdapter);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }
    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }
}
