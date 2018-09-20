package com.onepas.android.pasgo.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountManagerActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private RelativeLayout mRlUserInfo, mRlPaswordManager, mRlSponsorCode,mRlRemoveAccount;
    private TextView mTvIdPASGO, mTvNumberPhone, mTvDaGioiThieuNumber;
    private SimpleDraweeView mImgAvata;
    private int mSoNguoiGioiThieu=0;
    private boolean mIsHashPassword=false;
    private static final String KEY_HASK_PASSWORD="keyHaskPassword";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_account_manager);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        if(savedInstanceState !=null)
            mIsHashPassword = savedInstanceState.getBoolean(KEY_HASK_PASSWORD,false);
        initView();
        initControl();
        hashPassword();
        getMemberByNguoiDungId();
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
        super.initView();
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mRlUserInfo = (RelativeLayout)findViewById(R.id.rlUserInfo);
        mRlPaswordManager = (RelativeLayout)findViewById(R.id.rlPaswordManager);
        mRlSponsorCode = (RelativeLayout)findViewById(R.id.rlSponsorCode);
        mRlRemoveAccount = (RelativeLayout)findViewById(R.id.rlRemoveAccount);
        mTvIdPASGO = (TextView)findViewById(R.id.tvIdPASGO);
        mTvNumberPhone = (TextView)findViewById(R.id.tvNumberPhone);
        mImgAvata = (SimpleDraweeView) findViewById(R.id.imgAvata);
        mTvDaGioiThieuNumber = (TextView)findViewById(R.id.tvDaGioiThieuNumber);
        String pSoNguoiGioiThieu = Pasgo.getInstance().prefs.getSoNguoiGioiThieu();
        if(StringUtils.isEmpty(pSoNguoiGioiThieu)) pSoNguoiGioiThieu ="0";
        mTvDaGioiThieuNumber.setText(pSoNguoiGioiThieu);

        mRlUserInfo.setOnClickListener(this);
        mRlPaswordManager.setOnClickListener(this);
        mRlSponsorCode.setOnClickListener(this);
        mRlRemoveAccount.setOnClickListener(this);
    }

    @Override
    protected void initControl() {
        super.initControl();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
        mTvIdPASGO.setText(Pasgo.getInstance().ma);
        mTvNumberPhone.setText(Pasgo.getInstance().sdt);
        if(!StringUtils.isEmpty(Pasgo.getInstance().urlAnh))
            mImgAvata.setImageURI(Pasgo.getInstance().urlAnh + "&t="
                    + System.currentTimeMillis());
        else
            mImgAvata.setBackgroundResource(R.drawable.no_avatar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!StringUtils.isEmpty(Pasgo.getInstance().urlAnh) && Pasgo.getInstance().isUpdateImage)
            mImgAvata.setImageURI(Pasgo.getInstance().urlAnh + "&t="
                    + System.currentTimeMillis());
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(KEY_HASK_PASSWORD,mIsHashPassword);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onSignUpChanged() {
        super.onSignUpChanged();
        finishToRightToLeft();
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
        switch (v.getId())
        {
            case R.id.rlUserInfo:
            {
                if(!Pasgo.getInstance().prefs.getisUpdatePassword() && mIsHashPassword) {
                    onUpdatePasswordChanged();
                    return;
                }else
                if(!Pasgo.getInstance().prefs.getisUpdatePassword())
                    return;
                gotoActivityForResult(mContext,UserInfoActivity.class,Constants.KEY_GO_TO_USERINFO,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft();
                break;
            }
            case R.id.rlPaswordManager:
            {
                if(!Pasgo.getInstance().prefs.getisUpdatePassword() && mIsHashPassword) {
                    onUpdatePasswordChanged();
                    return;
                }else
                if(!Pasgo.getInstance().prefs.getisUpdatePassword())
                    return;
                gotoActivity(mContext,PasswordManagerActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft();
                break;
            }
            case R.id.rlSponsorCode:            {
                gotoActivity(mContext, ThePasgoActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft();
                break;
            }
            case R.id.rlRemoveAccount:
            {
                if(!isFinishing())
                    DialogUtils.showYesNoDialog(mActivity, R.string.tb_change_account,
                        R.string.dong_y, R.string.huy, changAccountListener);
                break;
            }
        }
    }
    View.OnClickListener changAccountListener = view -> {
        //stopServiceLocation();
        finishToRightToLeft();
        changAccount();
    };
    private void changAccount() {
        Pasgo.xoaTaiKhoan();
        gotoActivity(mContext,LoginActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishToRightToLeft();
    }

    protected void hashPassword() {
        if(Pasgo.getInstance().prefs.getisUpdatePassword())
            return;
        String url = WebServiceUtils.URL_HASH_PASSWORD(Pasgo.getInstance().token);
        JSONObject jsonParams1 = new JSONObject();
        try {
            jsonParams1.put("nguoiDungId", Pasgo.getInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams1,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mIsHashPassword=true;
                        int maLoi = ParserUtils.getIntValue(response,"MaLoi");
                        // nếu thành công thì cho phép update pass, userinfo
                        if(maLoi == Constants.KEY_SUCCESS_RESPONSE)
                        {
                            Pasgo.getInstance().prefs.putIsUpdatePassword(true);
                        }
                        //cập nhật mật khẩu trước khi làm các thao tác trên
                        if(maLoi == Constants.KEY_UPDATE_PASSWORD)
                        {
                            onUpdatePasswordChanged();
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

    protected void getMemberByNguoiDungId() {

        String url = WebServiceUtils.URL_GET_MEMBER_BY_NGUOIDUNGID(Pasgo.getInstance().token);
        JSONObject jsonParams1 = new JSONObject();
        try {
            jsonParams1.put("nguoiDungId", Pasgo.getInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams1,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = ParserUtils.getJsonObject(response,"Item");
                        mSoNguoiGioiThieu = ParserUtils.getIntValue(object,"CountMemberBy");
                        mTvDaGioiThieuNumber.setText(mSoNguoiGioiThieu+"");
                        Pasgo.getInstance().prefs.putSoNguoiGioiThieu(mSoNguoiGioiThieu+"");
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

    public void onUpdatePasswordChanged() {
        Intent intentf = new Intent(mContext,
                UpdatePassWordDialog.class);
        intentf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intentf,Constants.KEY_UPDATE_PASSWORD_NEW);
    }

}