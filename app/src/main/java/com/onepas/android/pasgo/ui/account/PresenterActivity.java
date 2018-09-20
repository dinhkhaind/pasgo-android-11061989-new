package com.onepas.android.pasgo.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class PresenterActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UpdatePassword";
    private EditText mEdtPresenter;
    private Button mBtnUpdate;
    private TextView mTvError;
    private TextView mTvpresenterDes;
    private TextView mTvpresenterDes2;
    private LinearLayout mLnReadMore;
    private ImageView mImgReadMore;
    private TextView mTvReadMore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_update_persenrer);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.update_presenter));
        setSupportActionBar(mToolbar);
        initView();
        initControl();
    }

    @Override
    protected void initView() {
        super.initView();
        mEdtPresenter = (EditText) findViewById(R.id.edtPresenter);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mTvError = (TextView) findViewById(R.id.tvError);
        mTvpresenterDes =(TextView)findViewById(R.id.presenter_des_tv);
        mTvpresenterDes2 =(TextView)findViewById(R.id.presenter_des_tv_2);
        mLnReadMore =(LinearLayout)findViewById(R.id.read_more_ln);
        mImgReadMore =(ImageView)findViewById(R.id.read_more_img);
        mTvReadMore =(TextView)findViewById(R.id.read_more_tv);
        mBtnUpdate.setOnClickListener(this);
        mLnReadMore.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disKeyboard();
    }

    public void disKeyboard() {
        if (mEdtPresenter.isFocused()) {
            View v = (View) findViewById(mEdtPresenter.getId());
            mEdtPresenter.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    protected void initControl() {
        super.initControl();
        Utils.setTextViewHtml(mTvpresenterDes,getString(R.string.update_presenter_des));
        Utils.setTextViewHtml(mTvpresenterDes2,getString(R.string.update_presenter_des_2));
        mTvpresenterDes2.setVisibility(View.GONE);
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
        mEdtPresenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(bundle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return true;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate: {
                actionUpdate();
                break;
            }
            case R.id.read_more_ln:
                if(mTvpresenterDes2.getVisibility() == View.GONE)
                {
                    mTvpresenterDes2.setVisibility(View.VISIBLE);
                    mImgReadMore.setImageResource(R.drawable.rut_gon_x);
                    mTvReadMore.setText(R.string.presenrer_read_less);
                }else {
                    mTvpresenterDes2.setVisibility(View.GONE);
                    mImgReadMore.setImageResource(R.drawable.xem_them_x);
                    mTvReadMore.setText(R.string.presenrer_read_more);
                }

                break;
        }
    }

    private void actionUpdate() {
        if (StringUtils.isEmpty(mEdtPresenter.getText().toString().trim()))
            ToastUtils.showToastWaring(mContext, R.string.plz_enter_presenter);
        else {
            updatePassword();
        }
    }
    public void updatePassword() {
        DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
        String url = WebServiceUtils
                .URL_UPDATE_MA_NGUOI_GIOI_THIEU(Pasgo.getInstance().token);
        HashMap<String, String> params = new HashMap<String, String>();
        String maNguoiGioiThieu = mEdtPresenter.getText().toString().trim();
        params.put("deviceId", factory.getDeviceUuid());
        params.put("nguoiDungId", Pasgo.getInstance().userId);
        params.put("maNguoiGioiThieu", maNguoiGioiThieu);
        JSONObject jsonParams = new JSONObject(params);
        showDialog(mContext);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dismissDialog();
                        int maLoi = ParserUtils.getIntValueResponse(response, "MaLoi");
                        if(maLoi == Constants.KEY_SUCCESS_RESPONSE) {
                            gotoMain();
                            ToastUtils.showToastSuccess(mContext,getString(R.string.update_presenter_successful));
                        }
                        else if(maLoi == Constants.KEY_PRESENTER_CODE_INVAIL)
                        {
                            mTvError.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        Pasgo.getInstance().isUpdatePassWord = false;
                        dismissDialog();
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();
                        Pasgo.getInstance().isUpdatePassWord = false;
                    }
                });
    }
    private void gotoMain()
    {
        if(!Pasgo.getInstance().prefs.getIsTrial()) {
            gotoActivity(mContext, HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        }
        PastaxiPref pasWayPref = new PastaxiPref(mContext);
        pasWayPref.putIsPresenterActivity(false);
        pasWayPref.putCheckIfGoToRegisterActivity(false);
        // ko dung thu
        Pasgo.getInstance().prefs.putIsTrial(false);
        finishOurLeftInLeft();
    }
}