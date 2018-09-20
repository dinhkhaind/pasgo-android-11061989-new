package com.onepas.android.pasgo.ui.successfultrips;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.LichSuChuyenDiItem;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.NetworkChangedListener;
import com.onepas.android.pasgo.ui.account.LoginActivity;
import com.onepas.android.pasgo.ui.search.DatabaseHandler;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuccessfulTripsActivity extends BaseAppCompatActivity implements
        NetworkChangedListener, View.OnClickListener {
    private ListView mLvTinKhuyenMai;
    private HashMap<Integer,ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>>> mListLichSu = new HashMap<Integer,ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>>>();
    private ArrayList<LichSuChuyenDiItem> mList=new ArrayList<LichSuChuyenDiItem>();
    private Button btnRegister;
    private LinearLayout mLayoutKoDuLieu, mLayoutLoading, mLayoutLichSu,
            llRegister;
    protected LinearLayout mLnErrorConnectNetwork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_successful_trips);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.lich_su_chuyen_di));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        this.onNetworkChanged();
        mLayoutKoDuLieu = (LinearLayout)findViewById(R.id.lyKhongCoThongBao);
        mLayoutLoading = (LinearLayout) findViewById(R.id.lyLoading);
        mLayoutLichSu = (LinearLayout) findViewById(R.id.llLichSuChuyenDi);

        new DatabaseHandler(mContext);

        llRegister = (LinearLayout) findViewById(R.id.llRegister);
        mLayoutLichSu = (LinearLayout)findViewById(R.id.llLichSuChuyenDi);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        mLvTinKhuyenMai = (ListView)
                findViewById(R.id.lv_tinkhuyenmai);
        mLvTinKhuyenMai.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (NetworkUtils.getInstance(mContext)
                        .isNetworkAvailable()) {
                    LichSuChuyenDiItem item = mList.get(position);
                    if(item.getKey()>0) return;
                    Intent intent;
                    String idLichSu = item.getiD();
                    if(!item.isHangXe()) {
                        intent = new Intent(mContext,
                                SuccessfulTripsDetailActivity.class);
                    }else{
                        intent = new Intent(mContext,
                                SuccessfulTripsDetailAdapter.class);
                    }
                    intent.putExtra(Constants.KEY_LICH_SU_ID, idLichSu);
                    startActivity(intent);
                }

            }
        });

        btnRegister.setOnClickListener(this);
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
    protected void onResume() {
        super.onResume();
        if(mList.size()==0)
            loadControls();
    }

    @Override
    public void onNetworkChanged() {

        if (mLnErrorConnectNetwork != null) {

            if (NetworkUtils.getInstance(mContext).isNetworkAvailable()) {
                mLnErrorConnectNetwork.setVisibility(View.GONE);
            } else {
                mLnErrorConnectNetwork.setVisibility(View.VISIBLE);

                if (mLayoutKoDuLieu != null && mLayoutLoading != null
                        && mLayoutLichSu != null) {
                    setLayoutView(1);
                }
            }

        }

        if (llRegister != null && mLayoutLichSu != null) {
            this.loadControls();
            this.getLichSuChuyenDi();
        }
    }

    private void getLichSuChuyenDi() {
        if (Pasgo.isLogged() && !this.isShowingRegister()
                && this.getCount() <= 0) {
            String url = WebServiceUtils
                    .URL_LICH_SU_DAT_XE_BY_NGUOIDUNG(Pasgo.getInstance().token);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("nguoiDungId", Pasgo.getInstance().userId);
            JSONObject jsonParams = new JSONObject(params);
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mListLichSu=ParserUtils.getLichSuChuyenDis(response);
                            setListAdapter(mListLichSu);
                        }

                        @Override
                        public void onError(int maloi) {
                        }

                    }, new Pasgo.PWErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
        } else if (this.getCount() > 0) {
            setLayoutView(2);
        }
    }

    private void setLayoutView(int i) {
        mLayoutKoDuLieu.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutLichSu.setVisibility(View.GONE);
        switch (i) {
            case 0:
                mLayoutLoading.setVisibility(View.VISIBLE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mLayoutLichSu.setVisibility(View.GONE);

                break;
            case 1:
                mLayoutLoading.setVisibility(View.GONE);
                mLayoutKoDuLieu.setVisibility(View.VISIBLE);
                mLayoutLichSu.setVisibility(View.GONE);

                break;
            case 2:
                mLayoutLoading.setVisibility(View.GONE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mLayoutLichSu.setVisibility(View.VISIBLE);

                break;
            case 3:
                mLayoutLoading.setVisibility(View.GONE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mLayoutLichSu.setVisibility(View.GONE);

                break;

            default:
                break;
        }
    }

    protected void setListAdapter(HashMap<Integer,ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>>>  listKM) {
        mList.clear();
        List <Integer> keyList = new ArrayList<Integer>(listKM.keySet());
        for(int year=keyList.size()-1;year>=0;year--)
        {
            ArrayList<HashMap<Integer,ArrayList<LichSuChuyenDiItem>>> listMonth = listKM.get(keyList.get(year));
            if(listMonth==null) continue;
            for(HashMap<Integer,ArrayList<LichSuChuyenDiItem>> itemMonth:listMonth)
            {
                for(int i=12;i>=1;i--)
                {
                    ArrayList<LichSuChuyenDiItem> lichSuChuyenDis=itemMonth.get(i);
                    if(lichSuChuyenDis !=null)
                    {
                        mList.add(new LichSuChuyenDiItem(i,lichSuChuyenDis.get(0).getYear()));
                        mList.addAll(lichSuChuyenDis);
                    }
                }
            }

        }

        if(mList.size()>0){
            SuccessfulTripsAdapter adapter = new SuccessfulTripsAdapter(
                    mContext, mList, R.layout.item_lich_su_chuyendi_all);
            mLvTinKhuyenMai.setAdapter(adapter);
            setLayoutView(2);
        } else {
            setLayoutView(1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void loadControls() {

        if (!Pasgo.isLogged()) {
            llRegister.setVisibility(View.VISIBLE);
            mLayoutLichSu.setVisibility(View.GONE);
            setLayoutView(3);

        } else {
            llRegister.setVisibility(View.GONE);
            mLayoutLichSu.setVisibility(View.VISIBLE);
            setLayoutView(0);
            this.getLichSuChuyenDi();
        }

        if (!NetworkUtils.getInstance(mContext).isNetworkAvailable()) {
            llRegister.setVisibility(View.GONE);
            mLayoutLichSu.setVisibility(View.VISIBLE);
            setLayoutView(1);
        }
    }

    private boolean isShowingRegister() {
        return llRegister.getVisibility() == View.VISIBLE;
    }

    private int getCount() {
        return mListLichSu.size();
    }
    @Override
    public void onStartMoveScreen() {

    }
    @Override
    public void onUpdateMapAfterUserInterection() {
    }
}