package com.onepas.android.pasgo.ui.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.presenter.account.login.LoginMvpView;
import com.onepas.android.pasgo.presenter.account.login.LoginPresenter;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;

public class LoginActivity extends BaseAppCompatActivity implements View.OnClickListener, LoginMvpView {

    private static final String TAG ="LoginActivity";
    private TextView mTvForgorPassword;
    private TextView mTvChangeLanguage;
    private LinearLayout mLnSelectLanguage;
    private EditText mEdtPhoneNumber, mEdtPassWord;
    private Button mBtnLogin, mBtnTryNow;
    private Button mBtnRegisterPhone;
    //protected ProgressDialog progressDialog;
    private String mNationNameView, mNationCode;
    private TextView mTvNationNameView;
    private TextView mTvRegisterPhoneIsExist;
    private boolean mIsCheckPhoneNumber;
    private final String KEY_CHECK_PHONE="check_phone";
    private boolean mIsForgotPassWord=false;
    private boolean mIsLogin=false;
    private boolean mIsTrialRegisterActivity=false;
    private TextView mTvDieuKhoan;
    private CheckBox mChkAccept;
    private LoginPresenter<LoginMvpView> mLoginPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
            mIsTrialRegisterActivity = bundle.getBoolean(Constants.BUNDLE_TRIAL_REGISTER,false);
        initView();
        initControl();
        PastaxiPref pasWayPref = new PastaxiPref(LoginActivity.this);
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
            Intent intent = new Intent(LoginActivity.this,
                    InputPhoneNumberActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, Constants.KEY_LOGIN);
        }
    }

    private boolean checkPhoneNumberHasBeenRegisteredOnOtherDevices() {

        PastaxiPref pasWayPref = new PastaxiPref(LoginActivity.this);
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
        mBtnTryNow = (Button) findViewById(R.id.btnTryNow);
        mTvChangeLanguage = (TextView) findViewById(R.id.tvChangeLanguage);
        mLnSelectLanguage = (LinearLayout) findViewById(R.id.lnSelectLanguage);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        mEdtPassWord = (EditText) findViewById(R.id.edtPassword);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mTvNationNameView = (TextView) findViewById(R.id.tvNationNameView);
        mTvRegisterPhoneIsExist = (TextView) findViewById(R.id.tvRegisterPhoneIsExist);
        mTvDieuKhoan = (TextView) findViewById(R.id.tvDieuKhoan);
        Utils.setTextViewHtml(mTvDieuKhoan,getString(R.string.dieukhoan));
        mChkAccept = (CheckBox) findViewById(R.id.chkAccept);
        mLoginPresenter = new LoginPresenter<LoginMvpView>();
        mLoginPresenter.onAttach(LoginActivity.this);

    }

    public void initControl()
    {
        SpannableString forgorPassword = new SpannableString(getString(R.string.forget_password));
        forgorPassword.setSpan(new UnderlineSpan(), 0, forgorPassword.length(), 0);
        mTvForgorPassword.setText(forgorPassword);

        SpannableString changeLanguage = new SpannableString(getString(R.string.change_language));
        changeLanguage.setSpan(new UnderlineSpan(), 0, changeLanguage.length(), 0);
        mTvChangeLanguage.setText(changeLanguage);

        mTvForgorPassword.setOnClickListener(this);
        mBtnRegisterPhone.setOnClickListener(this);
        mBtnTryNow.setOnClickListener(this);
        mTvChangeLanguage.setOnClickListener(this);
        mLnSelectLanguage.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvDieuKhoan.setOnClickListener(this);
        mTvRegisterPhoneIsExist.setVisibility(View.GONE);
        String language =  Pasgo.getInstance().prefs.getLanguage();
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
        mChkAccept.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleUpdateUI.sendEmptyMessage(1);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Pasgo.getInstance().prefs.putIsTrial(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finishOurLeftInLeft();
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
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    protected void onDestroy() {
        mLoginPresenter.onDetach();
        super.onDestroy();
        if (mEdtPhoneNumber.isFocused()) {
            View v = (View) findViewById(mEdtPhoneNumber.getId());
            Rect outRect = new Rect();
            mEdtPhoneNumber.getGlobalVisibleRect(outRect);
            mEdtPhoneNumber.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        if (mEdtPassWord.isFocused()) {
            View v = (View) findViewById(mEdtPassWord.getId());
            Rect outRect = new Rect();
            mEdtPassWord.getGlobalVisibleRect(outRect);
            mEdtPassWord.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void setBackGroundAlterInputText()
    {
        if(mEdtPhoneNumber.getText().length()==0)
        {
            mTvForgorPassword.setTextColor(Utils.getColor(LoginActivity.this, R.color.login_color_befor_input));
            mTvForgorPassword.setBackgroundColor(Utils.getColor(LoginActivity.this, R.color.transparent));
            mIsForgotPassWord =false;
        }else {
            mTvForgorPassword.setTextColor(Utils.getColor(LoginActivity.this, R.color.red));
            Utils.setBackground(mTvForgorPassword, Utils.getDrawable(this, R.drawable.selector_transparent));
            mIsForgotPassWord =true;
        }
        // background btnLogin
        if(mEdtPhoneNumber.getText().length()==0||mEdtPassWord.getText().length()==0 || !mChkAccept.isChecked())
        {
            mIsLogin = false;
            mBtnLogin.setTextColor(Utils.getColor(LoginActivity.this, R.color.login_color_befor_input));
            Utils.setBackground(mBtnLogin, Utils.getDrawable(this, R.drawable.corner_red_login_select));
        }else {
            mIsLogin = true;
            mBtnLogin.setTextColor(Utils.getColor(LoginActivity.this, R.color.white));
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
                Intent intent = new Intent(LoginActivity.this,
                        SelectNationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                startActivityForResult(intent, Constants.KEY_SELECT_LANGUAGE);
                break;
            }
            case R.id.tvForgorPassword:
            {
                mLoginPresenter.onForgetPassword();
                break;
            }
            case R.id.btnRegisterPhone:
            {
                mLoginPresenter.onRegister();
                break;
            }
            case R.id.btnTryNow:
                mLoginPresenter.onTryNow();
                break;
            case R.id.btnLogin:
            {
                loginDone();
                break;
            }
            case R.id.tvChangeLanguage:
            {
                mTvRegisterPhoneIsExist.setVisibility(View.GONE);
                String language;
                if(Pasgo.getInstance().prefs.getLanguage().equals(Constants.LANGUAGE_VIETNAM))
                {
                    language = Constants.LANGUAGE_ENGLISH;
                    Pasgo.getInstance().prefs.putLanguage(language);
                    Utils.changeLocalLanguage(language);
                    Intent intent1 =new Intent(LoginActivity.this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finishOurLeftInLeft();
                }else
                {
                    language = Constants.LANGUAGE_VIETNAM;
                    Pasgo.getInstance().prefs.putLanguage(language);
                    Utils.changeLocalLanguage(language);
                    Intent intent1 =new Intent(LoginActivity.this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finishOurLeftInLeft();
                }
                break;
            }
            case R.id.tvDieuKhoan:
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                String language= Pasgo.getInstance().prefs.getLanguage();
                String dieuKhoan;
                if(language.equals(Constants.LANGUAGE_VIETNAM))
                {
                    dieuKhoan=Constants.KEY_DIEU_KHOAN_VN;
                }else
                {
                    dieuKhoan=Constants.KEY_DIEU_KHOAN_EN;
                }
                i.setData(Uri.parse(dieuKhoan));
                startActivity(i);
                break;
            }
        }
    }
    private void loginDone()
    {
        if(!mIsLogin||!mChkAccept.isChecked())
            return;
        mTvRegisterPhoneIsExist.setVisibility(View.GONE);
        String phone = mEdtPhoneNumber.getText().toString().trim();
        String passWord = mEdtPassWord.getText().toString().trim();
        mLoginPresenter.onServerLoginClick(mNationCode,phone,passWord);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.KEY_GO_TO_KICH_HOAT && resultCode == RESULT_OK) {
            finishOurLeftInLeft();
        }
        if (requestCode == Constants.KEY_LOGIN && resultCode == RESULT_OK) {
            if (data == null) {
                finishOurLeftInLeft();
                return;
            }
            Bundle bundle =data.getExtras();
            if(bundle!=null)
            {
                mEdtPhoneNumber.setText(bundle.getString(Constants.BUNDLE_REGISTER_PHONE_IS_EXIST));
                mTvRegisterPhoneIsExist.setVisibility(View.VISIBLE);
            }else
                finishOurLeftInLeft();
        }
        if (requestCode == Constants.KEY_SELECT_LANGUAGE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if(bundle !=null)
            {
                mNationCode = bundle.getString(Constants.BUNDLE_NATION_CODE);
                mNationNameView = bundle.getString(Constants.BUNDLE_NATION_NAME_VIEW);
                PastaxiPref pasWayPref = new PastaxiPref(LoginActivity.this);
                pasWayPref.putNationCode(mNationCode);
                pasWayPref.putNationName(mNationNameView);
                handleUpdateUI.sendEmptyMessage(0);
            }
        }

    }
    // forgot password
    private void verifySdtKichHoat() {
        mLoginPresenter.verifySdtKichHoat(mNationCode,mEdtPhoneNumber.getText().toString().trim());
    }
    private void sendMaKichHoat() {
        mLoginPresenter.sendMaKichHoat(mNationCode);
    }


    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }

    @Override
    public void sendActivationCodeSuccess() {
        Intent intent = new Intent(LoginActivity.this,
                ActivationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, Constants.KEY_GO_TO_KICH_HOAT);
    }

    @Override
    public void loginSuccess() {
        hideKeyboard();
        Intent intent =new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishOurLeftInLeft();
    }
    @Override
    public void loginError() {
        hiddenDialog();
    }

    @Override
    public void onTryNow() {
        Intent intent = new Intent(LoginActivity.this,
                HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Pasgo.getInstance().prefs.putIsTrial(true);
        startActivityForResult(intent, Constants.KEY_TRIAL);
        finishOurLeftInLeft();
    }

    @Override
    public void onRegister() {
        mTvRegisterPhoneIsExist.setVisibility(View.GONE);
        Intent intent = new Intent(LoginActivity.this,
                InputPhoneNumberActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, Constants.KEY_LOGIN);
    }

    @Override
    public void onForgotPassword() {
        if(!mIsForgotPassWord)
            return;
        mTvRegisterPhoneIsExist.setVisibility(View.GONE);
        String phone = mEdtPhoneNumber.getText().toString().trim();
        if(StringUtils.isEmpty(phone))
        {
            ToastUtils.showToast(LoginActivity.this,R.string.plz_input_phone_number);
            return;
        }
        if(phone.length()<9)
        {
            ToastUtils.showToast(LoginActivity.this,R.string.plz_input_phone_number_format);
            return;
        }
        verifySdtKichHoat();
    }
    @Override
    public void showDialogConfirmPhoneNumber() {
        final Dialog dialogConfirmPhoneNumber;
        dialogConfirmPhoneNumber = new Dialog(LoginActivity.this);
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
    @Override
    public void showDialog() {
        String message = Utils.getStringByResourse(LoginActivity.this,
                R.string.loading_data);
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(message);
                progressDialog.show();
                progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                    }
                    return false;
                });
                progressDialog.setCancelable(false);
            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void hiddenDialog() {
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

    private void hideKeyboard()
    {
        if (mEdtPassWord.isFocused()) {
            View v = (View) findViewById(mEdtPassWord.getId());
            Rect outRect = new Rect();
            mEdtPassWord.getGlobalVisibleRect(outRect);
            mEdtPassWord.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if (mEdtPhoneNumber.isFocused()) {
            View v = (View) findViewById(mEdtPhoneNumber.getId());
            Rect outRect = new Rect();
            mEdtPhoneNumber.getGlobalVisibleRect(outRect);
            mEdtPhoneNumber.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
