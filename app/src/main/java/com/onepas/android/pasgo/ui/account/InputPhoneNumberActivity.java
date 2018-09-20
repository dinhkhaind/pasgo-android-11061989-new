package com.onepas.android.pasgo.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.onepas.android.pasgo.utils.LinkEnabledTextView;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.TextLinkClickListener;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class InputPhoneNumberActivity extends BaseAppCompatActivity implements
		OnClickListener, TextLinkClickListener {
	private static final String TAG = "InputPhoneNumber";
    private LinearLayout mLnSelectLanguage;
    private Button mBtnContinue;
    private EditText mEdtPhoneNumber;
    private String mNationNameView, mNationCode;
    private TextView mTvNationNameView;
    private String mPhonePopup;
    private CheckBox mChkAccept;
    private TextView mTvDieuKhoan;
    private LinearLayout mLayout2;
    private TextView mTvNotifi1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_phone_number);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.register));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		this.initView();
		this.initControl();
        PastaxiPref pasWayPref = new PastaxiPref(mContext);
        mNationCode = pasWayPref.getNationCode();
        mNationNameView = pasWayPref.getNationName();
        if(!StringUtils.isEmpty(mNationCode) && !StringUtils.isEmpty(mNationNameView))
        {
            handleUpdateUI.sendEmptyMessage(0);
        }
        if(savedInstanceState!=null)
        {
            mEdtPhoneNumber.setText(savedInstanceState.getString(Constants.KEY_SDT_LOGIN));
        }
        mLayout2 = (LinearLayout) findViewById(R.id.line_add_text2);
        String dieuKhoanSD = getString(R.string.dieu_khoan_su_dung);
        LinkEnabledTextView check2 = setLinkEnable(dieuKhoanSD);
        mLayout2.addView(check2);
        mLayout2.setVisibility(View.GONE);
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finishToRightToLeft();
        }
        return super.onKeyDown(keyCode, event);
    }
    private LinkEnabledTextView setLinkEnable(String text) {
        LinkEnabledTextView check;
        check = new LinkEnabledTextView(this, null);
        check.setOnTextLinkClickListener(this);
        check.gatherLinksForText(text);
        check.setTextColor(Color.BLACK);
        check.setLinkTextColor(Color.BLUE);

        MovementMethod m = check.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (check.getLinksClickable()) {
                check.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        return check;
    }

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
        mLnSelectLanguage = (LinearLayout) findViewById(R.id.lnSelectLanguage);
        mBtnContinue = (Button) findViewById(R.id.btnContinue);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        mTvNationNameView = (TextView) findViewById(R.id.tvNationNameView);
        mChkAccept = (CheckBox) findViewById(R.id.chkAccept);
        mTvDieuKhoan = (TextView) findViewById(R.id.tvDieuKhoan);
        mTvNotifi1 = (TextView) findViewById(R.id.tvNotifi1);
        Utils.setTextViewHtml(mTvDieuKhoan,getString(R.string.dieukhoan));
        mLnSelectLanguage.setOnClickListener(this);
        mBtnContinue.setOnClickListener(this);
        mTvDieuKhoan.setOnClickListener(this);

	}

    @Override
    protected void onPause() {
        super.onPause();
        if (mEdtPhoneNumber.isFocused()) {
            View v = (View) findViewById(mEdtPhoneNumber.getId());
            Rect outRect = new Rect();
            mEdtPhoneNumber.getGlobalVisibleRect(outRect);
            mEdtPhoneNumber.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
	protected void initControl() {
		// TODO Auto-generated method stub
		super.initControl();
        mEdtPhoneNumber.requestFocus();
        Utils.setTextViewHtml(mTvNotifi1,getString(R.string.register_phone_text_notifi_1));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEdtPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
        mEdtPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    registerPhone();
                }
                return false;
            }
        });
        mChkAccept.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                Utils.setBackground(mBtnContinue,
                        Utils.getDrawable(mContext, R.drawable.corner_red_login));
            else
                Utils.setBackground(mBtnContinue,
                        Utils.getDrawable(mContext, R.drawable.corner_red_login_select));

        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
        switch (arg0.getId())
        {
            case R.id.btnContinue:
            {
                registerPhone();
                break;
            }
            case R.id.lnSelectLanguage:
            {
                Intent intent = new Intent(mContext,
                        SelectNationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, Constants.KEY_SELECT_LANGUAGE);
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

    private void registerPhone()
    {
        if(!mChkAccept.isChecked())
            return;
        String phone = mEdtPhoneNumber.getText().toString().trim();
        if(StringUtils.isEmpty(mNationCode))
        {
            ToastUtils.showToastWaring(mContext,R.string.plz_input_language);
            return;
        }
        if(StringUtils.isEmpty(phone))
        {
            ToastUtils.showToastWaring(mContext,R.string.plz_input_phone_number);
            return;
        }
        if(phone.length()<9)
        {
            ToastUtils.showToastWaring(mContext,R.string.plz_input_phone_number_format);
            return;
        }

        String firstPhone = phone.substring(0, 1);
        if(firstPhone.equals("0"))
            phone = phone.substring(1,phone.length());
        mPhonePopup = mNationCode + phone;
        verifySdtKichHoat();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.KEY_SELECT_LANGUAGE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if(bundle !=null)
            {
                mNationCode = bundle.getString(Constants.BUNDLE_NATION_CODE);
                mNationNameView = bundle.getString(Constants.BUNDLE_NATION_NAME_VIEW);
                PastaxiPref pasWayPref = new PastaxiPref(mContext);
                pasWayPref.putNationCode(mNationCode);
                pasWayPref.putNationName(mNationNameView);
                handleUpdateUI.sendEmptyMessage(0);
            }
        }
        if (requestCode == Constants.KEY_KICH_HOAT && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finishToRightToLeft();
        }
    }
    Handler handleUpdateUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mTvNationNameView.setText(mNationNameView);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.KEY_SDT_LOGIN,mEdtPhoneNumber.getText().toString());
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
    private void showDialogConfirmPhoneNumber() {
        final Dialog dialogConfirmPhoneNumber;
        dialogConfirmPhoneNumber = new Dialog(mContext);
        dialogConfirmPhoneNumber.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirmPhoneNumber.setContentView(R.layout.layout_confirm_phone_number);
        TextView tvPhoneNumber;
        Button btnDongY, btnHuy;
        btnDongY = (Button) dialogConfirmPhoneNumber.findViewById(R.id.btnDongY);
        btnHuy = (Button) dialogConfirmPhoneNumber.findViewById(R.id.btnHuy);
        tvPhoneNumber = (TextView) dialogConfirmPhoneNumber.findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.setText(mPhonePopup);
        btnDongY.setOnClickListener(v -> {
            sendMaKichHoat();
            dialogConfirmPhoneNumber.dismiss();
        });
        btnHuy.setOnClickListener(v -> dialogConfirmPhoneNumber.dismiss());
        if(!isFinishing())
            dialogConfirmPhoneNumber.show();
    }


    private void verifySdtKichHoat() {
        String url = WebServiceUtils.URL_VERIFY_SDT_KICHHOAT();
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            showProgressDialog();
            try {
                jsonParams.put("maQuocGia", mNationCode);
                jsonParams.put("sdt", mEdtPhoneNumber.getText().toString().trim());
                jsonParams.put("isRegisted", false);
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                if (json != null) {
                                    Utils.Log(TAG,json.toString());
                                    closeProgressDialog();
                                    JSONObject jsonObject = null;
                                    try {
                                        int maLoi = ParserUtils.getIntValueResponse(json,"MaLoi");
                                        if(maLoi == Constants.KEY_SUCCESS_RESPONSE) {
                                            jsonObject = json.getJSONObject("Item");
                                            String sdt = ParserUtils.getStringValue(jsonObject, "Sdt");
                                            Pasgo.getInstance().sdt = sdt;
                                            PastaxiPref pasWayPref = new PastaxiPref(mContext);
                                            pasWayPref.putSdt(Pasgo.getInstance().sdt);
                                            showDialogConfirmPhoneNumber();
                                        }
                                        else if(maLoi == Constants.KEY_REGISTER_PHONE_IS_EXIST)
                                        {
                                            Intent intent = new Intent();
                                            intent.putExtra(Constants.BUNDLE_REGISTER_PHONE_IS_EXIST,mEdtPhoneNumber.getText().toString().trim());
                                            setResult(RESULT_OK, intent);
                                            finishToRightToLeft();
                                        }
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
                ToastUtils.showToastError(mContext,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }else
        {
            closeProgressDialog();
            ToastUtils.showToastError(mContext,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
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
                                    Bundle bundle=new Bundle();
                                    bundle.putBoolean(Constants.BUNDLE_KEY_IS_REGISTER,true);
                                    gotoActivityForResult(mContext, ActivationActivity.class, bundle,
                                            Constants.KEY_KICH_HOAT,
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    ourLeftInLeft();
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
                ToastUtils.showToastError(mContext,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }else
        {
            closeProgressDialog();
            ToastUtils.showToastWaring(mContext,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }


    @Override
    public void onTextLinkClick(View textView, String clickedString) {
        if (clickedString.equals(getString(R.string.dieukhoan2))) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Constants.KEY_PASGO));
            startActivity(i);

        }
    }
}