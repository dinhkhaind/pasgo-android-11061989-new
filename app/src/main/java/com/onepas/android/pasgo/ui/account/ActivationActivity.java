package com.onepas.android.pasgo.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.calldriver.ProgressWheel;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivationActivity extends BaseAppCompatActivity implements OnClickListener{
	private static final String TAG = "ActivationActivity";
	private static final String RUNNING = "RUNNING";
	private static final String PROGRESS = "PROGRESS";
	private EditText mEdtCode;
	private Button mBtDangKyNgay;
	ProgressWheel pw_two;
	private boolean mRunning;
	private int mProgress = 0;
	private TextView mTxtGuiLai;
	private LinearLayout mLayout;
	private Runnable run;
	protected boolean checkGL;
    private TextView mTvResendActivationCode;
    private boolean mIsActivationClick=false;
    private TextView mTvResent;
    private boolean mIsRegister=false;
    private boolean mIsGoToFromSplashActivity=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_kich_hoat);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.kich_hoat));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		pw_two = (ProgressWheel) findViewById(R.id.progressBarTwo);
		mLayout = (LinearLayout) findViewById(R.id.line_add_text);
		mTxtGuiLai = (TextView) findViewById(R.id.txt_gui_lai);
        mTvResent = (TextView) findViewById(R.id.tvResent);
        mTvResendActivationCode = (TextView) findViewById(R.id.tvResendActivationCode);
        mTvResendActivationCode.setVisibility(View.GONE);
		mEdtCode = (EditText) findViewById(R.id.edtCode);
		mBtDangKyNgay = (Button) findViewById(R.id.btnDangKyInfo);
		mBtDangKyNgay.setOnClickListener(this);
        SpannableString forgorPassword = new SpannableString(getString(R.string.resend_activation_code));
        forgorPassword.setSpan(new UnderlineSpan(), 0, forgorPassword.length(), 0);
        mTvResendActivationCode.setText(forgorPassword);
		String sResent = getString(R.string.link_gui_lai1) + " "
				+ getString(R.string.link_gui_lai2) + " "
				+ getString(R.string.link_gui_lai3);
        mTvResent.setText(sResent);
        Bundle bundle =getIntent().getExtras();
        if(bundle!=null) {
            mIsRegister = bundle.getBoolean(Constants.BUNDLE_KEY_IS_REGISTER);
            mIsGoToFromSplashActivity = bundle.getBoolean(Constants.BUNDLE_KEY_GO_TO_FROM_SPLASH_ACTIVITY);
        }
		run = new Runnable() {
			public void run() {
				mRunning = true;
				mProgress = 0;
				while (mProgress < 361) {
					Utils.Log(TAG, "progress : " + mProgress);
					pw_two.incrementProgress();
					mProgress++;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				checkGL = true;
				handleUpdateUI.sendEmptyMessage(0);
			}
		};
		
		pw_two.resetCount();
		
		if(savedInstanceState != null){
			mProgress = savedInstanceState.getInt(PROGRESS);
			mRunning = savedInstanceState.getBoolean(RUNNING);
            mIsRegister = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_IS_REGISTER);
            mIsGoToFromSplashActivity = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_GO_TO_FROM_SPLASH_ACTIVITY);
		}
		
		if (!mRunning) {
            if(!mIsGoToFromSplashActivity) {
                Thread s = new Thread(run);
                s.start();
            }else
            {
                handleUpdateUI.sendEmptyMessage(0);
            }
		} else {
		}

		PastaxiPref pasWayPref = new PastaxiPref(mContext);
		pasWayPref.putIsKichHoatActivity(true);
        pasWayPref.putCheckIfGoToRegisterActivity(mIsRegister);
        mEdtCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    kichHoat();
                }
                return false;
            }
        });
        mTvResendActivationCode.setOnClickListener(v -> {
            mProgress = 0;
            getLaiMaKichHoat();
        });

	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mEdtCode.isFocused()) {
                View v = (View) findViewById(mEdtCode.getId());
                Rect outRect = new Rect();
                mEdtCode.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    mEdtCode.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        }
        return super.dispatchTouchEvent(event);
    }

	Handler handleUpdateUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mProgress = 0;
				mTxtGuiLai.setVisibility(View.GONE);
                mTvResendActivationCode.setVisibility(View.VISIBLE);
                mTvResent.setVisibility(View.VISIBLE);
				pw_two.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDangKyInfo:
			kichHoat();
			break;

		default:
			break;
		}
	}
	
	private void kichHoat() {
		String maKichHoat = mEdtCode.getText().toString();
		if (maKichHoat.equals("")) {
			ToastUtils.showToast(mContext, getString(R.string.thieu_thong_tin));
		} else {
				kichHoatInfo(maKichHoat);
		}
	}

    private void sendMaKichHoat() {
        String url = WebServiceUtils.URL_SEND_MA_KICH_HOAT();
        DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            try {
                PastaxiPref pasWayPref = new PastaxiPref(mContext);
                String nationCode = pasWayPref.getNationCode();
                jsonParams.put("maQuocGia", nationCode);
                jsonParams.put("sdt", Pasgo.getInstance().sdt);
                jsonParams.put("imei", factory.getDeviceUuid());
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new Pasgo.PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                mIsActivationClick=false;
                            }

                            @Override
                            public void onError(int maloi) {
                                dismissDialog();
                            }

                        }, new Pasgo.PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dismissDialog();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                dismissDialog();
                ToastUtils.showToast(mContext,
                        R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
            }
        }else
        {
            closeProgressDialog();
            ToastUtils.showToast(mContext,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
    }

	private void getLaiMaKichHoat() {

        mTvResendActivationCode.setVisibility(View.GONE);
		mTxtGuiLai.setVisibility(View.VISIBLE);
        mTvResent.setVisibility(View.GONE);
		pw_two.setVisibility(View.VISIBLE);

		mProgress = 0;
		pw_two.resetCount();
		Thread s = new Thread(run);
		s.start();
		mProgress = 0;
        sendMaKichHoat();
	}

	public void kichHoatInfo(String maKichHoat) {
        if(mIsActivationClick) return;
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            String url = WebServiceUtils
                    .URL_Ma_Kich_Hoat(Pasgo.getInstance().token);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("sdt", Pasgo.getInstance().sdt);
            params.put("maKichHoat", maKichHoat);
            JSONObject jsonParams = new JSONObject(params);
            mIsActivationClick = true;
            showDialog(mContext);
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.Log(TAG, "Kich hoat" + response.toString());
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject("Item");
                                String NguoiDungId = ParserUtils.getStringValue(jsonObject, "NguoiDungId");
                                String tokenApi = jsonObject.getString("TokenApi");
                                String sdt = ParserUtils.getStringValue(jsonObject, "Sdt");
                                String maNguoiDung = ParserUtils.getStringValue(jsonObject, "MaNguoiDung");
                                String urlAnh = ParserUtils.getStringValue(jsonObject, "UrlAnh");
                                Pasgo.getInstance().userId = NguoiDungId;
                                Pasgo.getInstance().sdt = sdt;
                                Pasgo.getInstance().ma = maNguoiDung;
                                Pasgo.getInstance().token = tokenApi;
                                Pasgo.getInstance().urlAnh = urlAnh;
                                registerFCM();
                                // save pref
                                PastaxiPref pasWayPref = new PastaxiPref(mContext);
                                pasWayPref.putUserId(Pasgo.getInstance().userId);
                                pasWayPref.putSdt(Pasgo.getInstance().sdt);
                                pasWayPref.putMa(Pasgo.getInstance().ma);
                                pasWayPref.putToken(Pasgo.getInstance().token);
                                pasWayPref.putUrlAnh(Pasgo.getInstance().urlAnh);
                                pasWayPref.putIsKichHoat(true);
                                pasWayPref.putIsKichHoatActivity(false);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(Constants.BUNDLE_KEY_IS_REGISTER, mIsRegister);
                                gotoActivity(mContext, UpdatePasswordActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Intent intent = new Intent(ActivationActivity.this, LoginActivity.class);
                                setResult(RESULT_OK, intent);
                                finishToRightToLeft();
                                dismissDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int maloi) {
                            mIsActivationClick = false;
                            dismissDialog();
                        }

                    }, new PWErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mIsActivationClick = false;
                            dismissDialog();
                        }
                    });
        }else
        {
            dismissDialog();
            ToastUtils.showToast(mContext,
                    R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            onBackActivity();
		}
		return true;
	}
    private void onBackActivity()
    {
        if(mIsGoToFromSplashActivity)
        {
            Intent intent = new Intent(mContext,
                    LoginActivity.class);
            intent.putExtra(Constants.BUNDLE_KEY_GO_TO_FROM_SPLASH_ACTIVITY,true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        // neu back lai thi: xoa dang ky di
        PastaxiPref pasWayPref = new PastaxiPref(mContext);
        pasWayPref.putIsKichHoatActivity(false);
        pasWayPref.putCheckIfGoToRegisterActivity(false);
        finishToRightToLeft();
    }
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		
		bundle.putInt(PROGRESS, mProgress);
		bundle.putBoolean(RUNNING, mRunning);
		bundle.putBoolean(Constants.BUNDLE_KEY_IS_REGISTER,mIsRegister);
        bundle.putBoolean(Constants.BUNDLE_KEY_GO_TO_FROM_SPLASH_ACTIVITY,mIsActivationClick);
		super.onSaveInstanceState(bundle);
	}

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}


}