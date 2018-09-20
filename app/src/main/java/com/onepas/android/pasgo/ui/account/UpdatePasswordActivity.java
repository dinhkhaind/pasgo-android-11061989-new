package com.onepas.android.pasgo.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class UpdatePasswordActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UpdatePassword";
    private EditText mEdtPassword, mEdtPasswordAgain;
    private Button mBtnUpdate;
    private boolean mIsRegister=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_update_password);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.update_password));
        setSupportActionBar(mToolbar);

        Bundle bundle =getIntent().getExtras();
        if(bundle!=null) {
            mIsRegister = bundle.getBoolean(Constants.BUNDLE_KEY_IS_REGISTER);
            PastaxiPref pasWayPref = new PastaxiPref(mContext);
            pasWayPref.putCheckIfGoToRegisterActivity(mIsRegister);
        }else
        if(savedInstanceState != null){
            mIsRegister = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_IS_REGISTER);
        }
        initView();
        initControl();

    }

    @Override
    protected void initView() {
        super.initView();
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);
        mEdtPasswordAgain = (EditText) findViewById(R.id.edtPasswordAgain);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnUpdate.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disKeyboard();
    }

    public void disKeyboard() {

        if (mEdtPassword.isFocused()) {
            View v = (View) findViewById(mEdtPassword.getId());
            mEdtPassword.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if (mEdtPasswordAgain.isFocused()) {
            View v = (View) findViewById(mEdtPasswordAgain.getId());

            mEdtPasswordAgain.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }

    @Override
    protected void initControl() {
        super.initControl();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
        PastaxiPref pasWayPref = new PastaxiPref(mContext);
        pasWayPref.putIsUpdatePasswordActivity(true);
        mEdtPasswordAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    actionUpdatePassWord();
                }
                return false;
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
        bundle.putBoolean(Constants.BUNDLE_KEY_IS_REGISTER,mIsRegister);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishOurLeftInLeft();
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
                actionUpdatePassWord();
                break;
            }

        }
    }

    private void actionUpdatePassWord() {
        if (StringUtils.isEmpty(mEdtPassword.getText().toString().trim()) || StringUtils.isEmpty(mEdtPasswordAgain.getText().toString().trim()))
            ToastUtils.showToastWaring(mContext, R.string.thieu_thong_tin);
        else if (mEdtPassword.getText().toString().trim().length() < 6)
            ToastUtils.showToastWaring(mContext, R.string.password_leng);
        else if (!mEdtPassword.getText().toString().trim().equals(mEdtPasswordAgain.getText().toString().trim()))
            ToastUtils.showToastWaring(mContext, R.string.password_khong_trung_nhau);
        else {
            updatePassword();
        }
    }

    public void updatePassword() {
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            String url = WebServiceUtils
                    .URL_UPDATE_PASSWORD(Pasgo.getInstance().token);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("nguoiDungId", Pasgo.getInstance().userId);
            params.put("matKhau", mEdtPassword.getText().toString().trim());
            JSONObject jsonParams = new JSONObject(params);
            showDialog(mContext);
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.Log(TAG, "update password" + response.toString());
                            PastaxiPref pasWayPref = new PastaxiPref(mContext);
                            // đã chạy qua màn hình update mật khẩu
                            pasWayPref.putIsUpdatePasswordActivity(false);
                            //đến từ màn hình register hoặc ko => để khi tắt app và bật lại còn lưu trạng thái
                            pasWayPref.putCheckIfGoToRegisterActivity(mIsRegister);
                            // đã update mật khẩu
                            Pasgo.getInstance().isUpdatePassWord = true;
                            if(!Pasgo.getInstance().prefs.getIsTrial()) {
                                gotoActivity(mContext, HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            pasWayPref.putIsPresenterActivity(false);
                            // ko dung thu
                            Pasgo.getInstance().prefs.putIsTrial(false);
                            dismissDialog();
                            finishOurLeftInLeft();
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
        else
        {
            dismissDialog();
            ToastUtils.showToastError(mContext,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

}