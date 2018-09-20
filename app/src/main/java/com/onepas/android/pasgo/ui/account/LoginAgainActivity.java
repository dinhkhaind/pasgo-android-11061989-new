package com.onepas.android.pasgo.ui.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAgainActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG ="LoginActivity";
    private TextView mTvForgorPassword;
    private LinearLayout mLnSelectLanguage;
    private EditText mEdtPhoneNumber, mEdtPassWord;
    private Button mBtnLogin;
    private Button mBtnRegisterPhone;
    protected ProgressDialog progressDialog;
    private String mNationNameView, mNationCode;
    private TextView mTvNationNameView;
    private TextView mTvRegisterPhoneIsExist;
    private boolean mIsCheckPhoneNumber;
    private final String KEY_CHECK_PHONE="check_phone";
    private LinearLayout mLnLogin, mLnButomLogin;
    private boolean mIsForgotPassWord=false;
    private boolean mIsLogin=false;
    //
    private boolean mIsTrialRegisterActivity=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_again);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.login));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
            mIsTrialRegisterActivity = bundle.getBoolean(Constants.BUNDLE_TRIAL_REGISTER,false);
        initView();
        initControl();
        PastaxiPref pasWayPref = new PastaxiPref(LoginAgainActivity.this);
        mNationCode = pasWayPref.getNationCode();
        mNationNameView = pasWayPref.getNationName();
        if(!StringUtils.isEmpty(mNationCode) && !StringUtils.isEmpty(mNationNameView))
        {
            handleUpdateUI.sendEmptyMessage(0);
        }

        if(savedInstanceState!=null)
        {
            mEdtPhoneNumber.setText(savedInstanceState.getString(Constants.KEY_SDT_LOGIN));
            mEdtPassWord.setText(savedInstanceState.getString(Constants.KEY_PASSWORD));
            mIsCheckPhoneNumber = savedInstanceState.getBoolean(KEY_CHECK_PHONE);
        }else
            mIsCheckPhoneNumber=checkPhoneNumberHasBeenRegisteredOnOtherDevices();
        if (mIsTrialRegisterActivity)
        {
            mTvRegisterPhoneIsExist.setVisibility(View.GONE);
            Intent intent = new Intent(LoginAgainActivity.this,
                    InputPhoneNumberActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, Constants.KEY_LOGIN);
        }
    }

    private boolean checkPhoneNumberHasBeenRegisteredOnOtherDevices() {

        PastaxiPref pasWayPref = new PastaxiPref(LoginAgainActivity.this);
        String value = pasWayPref.getStringValue(Constants.KEY_Sign_Up_Changed);

        if (value != null && !value.isEmpty()
                && value.equals(Constants.KEY_Sign_Up_Changed)) {
            pasWayPref.putStringValue(Constants.KEY_Sign_Up_Changed, null);
            return true;
        }
        return false;
    }
    public void initView()
    {
        mTvForgorPassword = (TextView) findViewById(R.id.tvForgorPassword);
        mBtnRegisterPhone = (Button) findViewById(R.id.btnRegisterPhone);
        mLnSelectLanguage = (LinearLayout) findViewById(R.id.lnSelectLanguage);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        mEdtPassWord = (EditText) findViewById(R.id.edtPassword);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mTvNationNameView = (TextView) findViewById(R.id.tvNationNameView);
        mLnLogin =(LinearLayout) findViewById(R.id.lnLogin);
        mLnButomLogin = (LinearLayout)findViewById(R.id.lnButomLogin);
        mTvRegisterPhoneIsExist = (TextView) findViewById(R.id.tvRegisterPhoneIsExist);
    }

    public void initControl()
    {
        SpannableString forgorPassword = new SpannableString(getString(R.string.forget_password));
        forgorPassword.setSpan(new UnderlineSpan(), 0, forgorPassword.length(), 0);
        mTvForgorPassword.setText(forgorPassword);
        mTvForgorPassword.setOnClickListener(this);
        mBtnRegisterPhone.setOnClickListener(this);
        mLnSelectLanguage.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvRegisterPhoneIsExist.setVisibility(View.GONE);
        handleUpdateUI.sendEmptyMessage(1);
        mEdtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvRegisterPhoneIsExist.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleUpdateUI.sendEmptyMessage(1);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utils.Log(TAG,"afterTextChanged");

            }
        });
        mEdtPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvRegisterPhoneIsExist.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleUpdateUI.sendEmptyMessage(1);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEdtPassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    loginDone();
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finishToRightToLeft();
        }
        return super.onKeyDown(keyCode, event);
    }
    Handler handleUpdateUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mTvNationNameView.setText(mNationNameView);
                    break;
                case 1:
                    setBackGroundAlterInputText();
                    if(mEdtPhoneNumber.getText().toString().trim().length()>0||mEdtPassWord.getText().toString().trim().length()>0)
                        setLayoutPaddingWhenOpenKeyboard(true);
                    else
                        setLayoutPaddingWhenOpenKeyboard(false);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void setLayoutPaddingWhenOpenKeyboard(boolean isOpen)
    {
        double screenSize = Utils.checkSizeScreen(LoginAgainActivity.this);
        if(isOpen) {
            int LnpaddingTop = (int) getResources().getDimension(R.dimen.login_margin_top_input);
            int btnpaddingTop = (int) getResources().getDimension(R.dimen.login_button_login_margin_top_input);
            if(screenSize>=5.0 && screenSize<5.4) {
                LnpaddingTop = (int) getResources().getDimension(R.dimen.login_margin_top_5to54inc);
                btnpaddingTop = (int) getResources().getDimension(R.dimen.login_button_login_margin_top_5incto54);
            }else
            if(screenSize>=5.4 && screenSize<=6) {
                LnpaddingTop = (int) getResources().getDimension(R.dimen.login_margin_top_54to6inc);
                btnpaddingTop = (int) getResources().getDimension(R.dimen.login_button_login_margin_top_54incto6);
            }
            mLnLogin.setPadding(0, LnpaddingTop, 0, 0);
            mLnButomLogin.setPadding(0, btnpaddingTop, 0, 0);
        }else
        {
            int LnpaddingTop = (int) getResources().getDimension(R.dimen.login_margin_top);
            int btnpaddingTop = (int) getResources().getDimension(R.dimen.login_button_login_margin_top);
            if(screenSize>=5.0 && screenSize<5.4) {
                LnpaddingTop = (int) getResources().getDimension(R.dimen.login_margin_top_5to54inc);
                btnpaddingTop = (int) getResources().getDimension(R.dimen.login_button_login_margin_top_5incto54);
            }else
            if(screenSize>=5.4 && screenSize<=6) {
                LnpaddingTop = (int) getResources().getDimension(R.dimen.login_margin_top_54to6inc);
                btnpaddingTop = (int) getResources().getDimension(R.dimen.login_button_login_margin_top_54incto6);
            }
            mLnLogin.setPadding(0, LnpaddingTop, 0, 0);
            mLnButomLogin.setPadding(0, btnpaddingTop, 0, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.KEY_SDT_LOGIN,mEdtPhoneNumber.getText().toString());
        bundle.putString(Constants.KEY_PASSWORD, mEdtPassWord.getText().toString());
        bundle.putBoolean(KEY_CHECK_PHONE,mIsCheckPhoneNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mEdtPhoneNumber.isFocused()) {
                View v = (View) findViewById(mEdtPhoneNumber.getId());
                Rect outRect = new Rect();
                mEdtPhoneNumber.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    mEdtPhoneNumber.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

            if (mEdtPassWord.isFocused()) {
                View v = (View) findViewById(mEdtPassWord.getId());
                Rect outRect = new Rect();
                mEdtPassWord.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    mEdtPassWord.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    private void setBackGroundAlterInputText()
    {
        if(mEdtPhoneNumber.getText().length()==0)
        {
            mTvForgorPassword.setTextColor(Utils.getColor(LoginAgainActivity.this, R.color.gray));
            mTvForgorPassword.setBackgroundColor(Utils.getColor(LoginAgainActivity.this, R.color.transparent));
            mIsForgotPassWord =false;
        }else {
            mTvForgorPassword.setTextColor(Utils.getColor(LoginAgainActivity.this, R.color.red));
            Utils.setBackground(mTvForgorPassword, Utils.getDrawable(this, R.drawable.selector_transparent));
            mIsForgotPassWord =true;
        }
        // background btnLogin
        if(mEdtPhoneNumber.getText().length()==0||mEdtPassWord.getText().length()==0)
        {
            mIsLogin = false;
            mBtnLogin.setTextColor(Utils.getColor(LoginAgainActivity.this, R.color.login_color_befor_input));
            Utils.setBackground(mBtnLogin, Utils.getDrawable(this, R.drawable.corner_red_login_select));
        }else {
            mIsLogin = true;
            mBtnLogin.setTextColor(Utils.getColor(LoginAgainActivity.this, R.color.white));
            Utils.setBackground(mBtnLogin, Utils.getDrawable(this, R.drawable.corner_red_login));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lnSelectLanguage:
            {
                mTvRegisterPhoneIsExist.setVisibility(View.GONE);
                Intent intent = new Intent(LoginAgainActivity.this,
                        SelectNationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                startActivityForResult(intent, Constants.KEY_SELECT_LANGUAGE);
                break;
            }
            case R.id.tvForgorPassword:
            {
                if(!mIsForgotPassWord)
                    return;
                mTvRegisterPhoneIsExist.setVisibility(View.GONE);
                String phone = mEdtPhoneNumber.getText().toString().trim();
                if(StringUtils.isEmpty(phone))
                {
                    ToastUtils.showToast(LoginAgainActivity.this,R.string.plz_input_phone_number);
                    return;
                }
                if(phone.length()<9)
                {
                    ToastUtils.showToast(LoginAgainActivity.this,R.string.plz_input_phone_number_format);
                    return;
                }
                verifySdtKichHoat();
                break;
            }
            case R.id.btnRegisterPhone:
            {
                mTvRegisterPhoneIsExist.setVisibility(View.GONE);
                Intent intent = new Intent(LoginAgainActivity.this,
                        InputPhoneNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, Constants.KEY_LOGIN);
                break;
            }
            case R.id.btnLogin:
            {
                loginDone();
                break;
            }

        }
    }
    private void loginDone()
    {
        if(!mIsLogin)
            return;
        mTvRegisterPhoneIsExist.setVisibility(View.GONE);
        String phone = mEdtPhoneNumber.getText().toString().trim();
        if(StringUtils.isEmpty(mNationCode))
        {
            ToastUtils.showToast(LoginAgainActivity.this, R.string.plz_input_language);
            return;
        }
        if(StringUtils.isEmpty(phone))
        {
            ToastUtils.showToast(LoginAgainActivity.this,R.string.plz_input_phone_number);
            return;
        }
        if(phone.length()<9)
        {
            ToastUtils.showToast(LoginAgainActivity.this,R.string.plz_input_phone_number_format);
            return;
        }
        if(StringUtils.isEmpty(mEdtPassWord.getText().toString().trim()))
        {
            ToastUtils.showToast(LoginAgainActivity.this, R.string.plz_input_password);
            return;
        }
        login();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.KEY_GO_TO_KICH_HOAT && resultCode == RESULT_OK) {
            finishToRightToLeft();
        }
        if (requestCode == Constants.KEY_LOGIN && resultCode == RESULT_OK) {
            if (data == null) {
                finishToRightToLeft();
                return;
            }
            Bundle bundle =data.getExtras();
            if(bundle!=null)
            {
                mEdtPhoneNumber.setText(bundle.getString(Constants.BUNDLE_REGISTER_PHONE_IS_EXIST));
                mTvRegisterPhoneIsExist.setVisibility(View.VISIBLE);
            }else
                finishToRightToLeft();
        }
        if (requestCode == Constants.KEY_SELECT_LANGUAGE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if(bundle !=null)
            {
                mNationCode = bundle.getString(Constants.BUNDLE_NATION_CODE);
                mNationNameView = bundle.getString(Constants.BUNDLE_NATION_NAME_VIEW);
                PastaxiPref pasWayPref = new PastaxiPref(LoginAgainActivity.this);
                pasWayPref.putNationCode(mNationCode);
                pasWayPref.putNationName(mNationNameView);
                handleUpdateUI.sendEmptyMessage(0);
            }
        }

    }
    // forgot password
    private void verifySdtKichHoat() {
        String url = WebServiceUtils.URL_VERIFY_SDT_KICHHOAT();
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            showProgressDialog();
            try {
                jsonParams.put("maQuocGia", mNationCode);
                jsonParams.put("sdt", mEdtPhoneNumber.getText().toString().trim());
                jsonParams.put("isRegisted", true);
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    Utils.Log(TAG,json.toString());
                                    closeProgressDialog();
                                    JSONObject jsonObject = null;
                                    try {
                                        closeProgressDialog();
                                        jsonObject = json
                                                .getJSONObject("Item");
                                        String sdt = ParserUtils.getStringValue(jsonObject, "Sdt");
                                        Pasgo.getInstance().sdt = sdt;
                                        PastaxiPref pasWayPref = new PastaxiPref(LoginAgainActivity.this);
                                        pasWayPref.putSdt(Pasgo.getInstance().sdt);
                                        showDialogConfirmPhoneNumber();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                closeProgressDialog();
                            }

                        }, new Pasgo.PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                closeProgressDialog();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                closeProgressDialog();
                ToastUtils.showToast(LoginAgainActivity.this,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }else
        {
            closeProgressDialog();
            ToastUtils.showToast(LoginAgainActivity.this,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

    private void showDialogConfirmPhoneNumber() {
        final Dialog dialogConfirmPhoneNumber;
        dialogConfirmPhoneNumber = new Dialog(LoginAgainActivity.this);
        dialogConfirmPhoneNumber.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirmPhoneNumber.setContentView(R.layout.layout_confirm_phone_number);
        TextView tvPhoneNumber;
        Button btnDongY, btnHuy;
        btnDongY = (Button) dialogConfirmPhoneNumber.findViewById(R.id.btnDongY);
        btnHuy = (Button) dialogConfirmPhoneNumber.findViewById(R.id.btnHuy);
        tvPhoneNumber = (TextView) dialogConfirmPhoneNumber.findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.setText(Pasgo.getInstance().sdt);
        btnDongY.setOnClickListener(v -> {
            sendMaKichHoat();
            dialogConfirmPhoneNumber.dismiss();
        });
        btnHuy.setOnClickListener(v -> dialogConfirmPhoneNumber.dismiss());
        if(!isFinishing())
            dialogConfirmPhoneNumber.show();
    }

    private void sendMaKichHoat() {
        String url = WebServiceUtils.URL_SEND_MA_KICH_HOAT();
        DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            showProgressDialog();
            try {
                jsonParams.put("maQuocGia", mNationCode);
                jsonParams.put("sdt", Pasgo.getInstance().sdt);
                jsonParams.put("imei", factory.getDeviceUuid());
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    closeProgressDialog();
                                    Utils.Log(TAG, json.toString());
                                    Intent intent = new Intent(LoginAgainActivity.this,
                                            ActivationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, Constants.KEY_GO_TO_KICH_HOAT);
                                }
                            }

                            @Override
                            public void onError(int maloi) {
                                closeProgressDialog();
                            }

                        }, new Pasgo.PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                closeProgressDialog();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                closeProgressDialog();
                ToastUtils.showToast(LoginAgainActivity.this,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }else
        {
            closeProgressDialog();
            ToastUtils.showToast(LoginAgainActivity.this,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }
    // login
    private void login() {
        String url = WebServiceUtils
                .URL_LOGIN(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("maQuocGia", mNationCode);
            jsonParams.put("sdt", mEdtPhoneNumber.getText().toString().trim());
            jsonParams.put("matKhau", mEdtPassWord.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            showProgressDialog();
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.Log("response ", "response"
                                    + response);
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject("Item");
                                String NguoiDungId = ParserUtils.getStringValue(jsonObject, "NguoiDungId");
                                String tokenApi = jsonObject.getString("TokenApi");
                                String sdt = ParserUtils.getStringValue(jsonObject, "Sdt");
                                String maNguoiDung = ParserUtils.getStringValue(jsonObject, "MaNguoiDung");
                                String tenNguoiDung = ParserUtils.getStringValue(jsonObject, "TenNguoiDung");
                                String urlAnh = ParserUtils.getStringValue(jsonObject, "UrlAnh");
                                Pasgo.getInstance().userId = NguoiDungId;
                                Pasgo.getInstance().username = tenNguoiDung;
                                Pasgo.getInstance().sdt = sdt;
                                Pasgo.getInstance().ma = maNguoiDung;
                                Pasgo.getInstance().token = tokenApi;
                                Pasgo.getInstance().urlAnh = urlAnh;
                                registerFCM();
                                // save pref
                                PastaxiPref pasWayPref = new PastaxiPref(LoginAgainActivity.this);
                                pasWayPref.putUserId(Pasgo.getInstance().userId);
                                pasWayPref.putUserName(Pasgo.getInstance().username);
                                pasWayPref.putSdt(Pasgo.getInstance().sdt);
                                pasWayPref.putMa(Pasgo.getInstance().ma);
                                pasWayPref.putToken(Pasgo.getInstance().token);
                                pasWayPref.putUrlAnh(Pasgo.getInstance().urlAnh);
                                pasWayPref.putIsUpdatePassword(true);
                                pasWayPref.putIsUpdatePasswordActivity(false);
                                Pasgo.getInstance().isUpdatePassWord = true;
                                closeProgressDialog();
                                finishToRightToLeft();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int maloi) {
                            closeProgressDialog();
                        }

                    }, new Pasgo.PWErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            closeProgressDialog();
                        }
                    });
        }else
        {
            closeProgressDialog();
            ToastUtils.showToast(LoginAgainActivity.this,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

    public void showProgressDialog() {
        String message = Utils.getStringByResourse(LoginAgainActivity.this,
                R.string.loading_data);
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LoginAgainActivity.this);
                progressDialog.setMessage(message);
                progressDialog.show();
                progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                        }
                        return false;
                    }
                });
                progressDialog.setCancelable(false);
            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    public void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog.cancel();
                progressDialog = null;
            }
        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }
}
