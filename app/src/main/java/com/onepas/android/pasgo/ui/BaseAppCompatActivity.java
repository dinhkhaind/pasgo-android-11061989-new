package com.onepas.android.pasgo.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.prefs.ToolTipPref;
import com.onepas.android.pasgo.ui.account.LoginActivity;
import com.onepas.android.pasgo.ui.account.LoginAgainActivity;
import com.onepas.android.pasgo.ui.account.UpdatePassWordDialog;
import com.onepas.android.pasgo.utils.BaseProgressDialogAllUtils;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.KeyboardUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;
import com.onepas.android.pasgo.widgets.TouchableWrapper.UpdateMapAfterUserInterection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class BaseAppCompatActivity extends AppCompatActivity
		implements NetworkChangedListener, SignUpChangedListener,
		UpdateMapAfterUserInterection {
	private static final String TAG = "";
	protected boolean isAnimatingRefreshButton;
	protected boolean mShouldFinish;

	protected boolean mFirstLaunch = false;
	protected boolean mIsErrorChannel;
	protected ProgressDialog progressDialog;
	protected BaseProgressDialogAllUtils progressDialogAll;
	protected Context mContext;
	protected Activity mActivity;
	protected LinearLayout mLnErrorConnectNetwork;
	protected TextView mTvNetworkError;
	protected RelativeLayout mRlToolTipBottomLeft;
	protected TextView TvBottomLeft;
	protected RelativeLayout mRlToolTipBottomRight;
	protected TextView TvBottomRight;
	protected PendingIntent mPendingIntentToolTip;
	protected AlarmManager mAlarmManagerToolTip;
	protected ToolTipPref mToolTipPref;
	protected RelativeLayout mRlToolTipBottomCenter;
	protected TextView TvBottomCenter;
	private boolean mIsXLargeDevice;
	protected Toolbar mToolbar;
	// animation
	protected Animation mAnimSlideDown;
	protected Animation mAnimSlideUp;
	protected ProgressBar mProgressToolbar;
	protected boolean mIsDestroy;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		this.mActivity = this;
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4)
			mIsXLargeDevice = true;
		mToolTipPref = new ToolTipPref(mContext);
        // trường hợp crash app thì kiểm tra xem thông tin có bị xóa không, nếu bị xóa thì load lại
        if(!isCheckLogin()) {
            getTokenPrefs();
        }

	}
	protected void registerFCM() {
		DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
		PastaxiPref pasWayPref = new PastaxiPref(mContext);
		String regId = pasWayPref.getFcmToken();
		String url = WebServiceUtils.URL_RegisterPushNotification();
		JSONObject jsonParams1 = new JSONObject();
		try {
			jsonParams1.put("deviceId", factory.getDeviceUuid());
			jsonParams1.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams1.put("deviceType", 0);
			jsonParams1.put("registrationId", regId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Pasgo.getInstance().addToRequestQueue(url, jsonParams1,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						// finish();
						Utils.Log("", "" + response);
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

    protected boolean isCheckLogin() {
		return Pasgo.isLogged() && !StringUtils.isEmpty(Pasgo.getInstance().userId);
    }

    private void getTokenPrefs() {
        if (Pasgo.getInstance().prefs == null)
            return;
        Pasgo.getInstance().token = Pasgo.getInstance().prefs.getToken();
        Pasgo.getInstance().userId = Pasgo.getInstance().prefs.getUserId();
        Pasgo.getInstance().sdt = Pasgo.getInstance().prefs.getSdt();
        Pasgo.getInstance().maKichHoat = Pasgo.getInstance().prefs
                .getMakichhoat();
        Pasgo.getInstance().ma = Pasgo.getInstance().prefs.getMa();
        Pasgo.getInstance().username = Pasgo.getInstance().prefs.getUserName();
        Pasgo.getInstance().isUpdatePassWord = Pasgo.getInstance().prefs.getisUpdatePassword();
        Pasgo.getInstance().isGotoUpdatePassWordActivity = Pasgo.getInstance().prefs.getIsUpdatePasswordActivity();
        Pasgo.getInstance().urlAnh = Pasgo.getInstance().prefs.getUrlAnh();
        if(StringUtils.isEmpty(Pasgo.getInstance().prefs.getNationCode())||StringUtils.isEmpty(Pasgo.getInstance().prefs.getNationName()))
        {
            Pasgo.getInstance().prefs.putNationCode(Constants.KEY_VIETNAM_CODE);
            Pasgo.getInstance().prefs.putNationName(Constants.KEY_VIETNAM_NAME);
        }
        if(StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().isUserNotNull=false;
        else
            Pasgo.getInstance().isUserNotNull=true;

        setupLanguage();
    }

    private void setupLanguage()
    {
        String language= Pasgo.getInstance().prefs.getLanguage();
        if(StringUtils.isEmpty(language))
        {
            String locale = Locale.getDefault().getLanguage();
            if(locale.equals(Constants.LANGUAGE_VIETNAM))
                language=Constants.LANGUAGE_VIETNAM;
            else
                language=Constants.LANGUAGE_ENGLISH;
        }
        Pasgo.getInstance().language=language;
        Pasgo.getInstance().prefs.putLanguage(language);
        Utils.changeLocalLanguage(language);
    }

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiverChangeNetwork);
		unregisterReceiver(broadcastReceiverLogin);
        unregisterReceiver(broadcastReceiverUpdatePassWord);
		unregisterReceiver(broadcastReceiverRequestLogin);
		if (isAnimatingRefreshButton) {
			isAnimatingRefreshButton = false;
		}
		if (mShouldFinish) {
			overridePendingTransition(0, 0);
			finish();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIsDestroy = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentNetworkFilter = new IntentFilter(
				Constants.BROADCAST_ACTION_CHANGE_NETWORK);
		registerReceiver(broadcastReceiverChangeNetwork, intentNetworkFilter);
        // signout
		IntentFilter intentLoginFilter = new IntentFilter(
				Constants.BROADCAST_ACTION_SIGNOUT);
		registerReceiver(broadcastReceiverLogin, intentLoginFilter);
        // update password
        IntentFilter intentUpdatePasswordFilter = new IntentFilter(
                Constants.BROADCAST_UPDATE_PASSWORD);
        registerReceiver(broadcastReceiverUpdatePassWord, intentUpdatePasswordFilter);
		//yeu cau dang nhap
		IntentFilter intentRequestLoginFilter = new IntentFilter(
				Constants.BROADCAST_ACTION_REQUEST_LOGIN);
		registerReceiver(broadcastReceiverRequestLogin, intentRequestLoginFilter);
		mIsDestroy = false;
	}

	private final BroadcastReceiver broadcastReceiverRequestLogin = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			handlerLogin.sendEmptyMessage(2);
		}
	};
	private final BroadcastReceiver broadcastReceiverChangeNetwork = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			handleNetworkChange.sendEmptyMessage(0);
		}
	};

	private final BroadcastReceiver broadcastReceiverLogin = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			handlerLogin.sendEmptyMessage(0);
		}
	};

    private final BroadcastReceiver broadcastReceiverUpdatePassWord = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handlerLogin.sendEmptyMessage(1);
        }
    };


	protected Handler handleNetworkChange = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onNetworkChanged();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	Handler handlerLogin = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					onSignUpChanged();
					break;
				case 1:
					onUpdatePasswordChanged();
					break;
				case  2:
					showDialogCheckLogin();
					break;
				default:
					break;
			}
		};
	};
	private Dialog mDialogShowCheckIn;
	private void showDialogCheckLogin() {
		if(mDialogShowCheckIn==null) {
			mDialogShowCheckIn = new Dialog(mContext);
			mDialogShowCheckIn.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
			mDialogShowCheckIn.setContentView(R.layout.layout_popup_dang_ki);
			TextView tvHoTen = (TextView) mDialogShowCheckIn
					.findViewById(R.id.tvHoTen);
			tvHoTen.setText(StringUtils.getStringByResourse(Pasgo.getInstance().getBaseContext(),
					R.string.description_register_from_khuyen_mai));
			mDialogShowCheckIn.setCancelable(false);
			Button btnDaGap, btnChuaGap;
			btnDaGap = (Button) mDialogShowCheckIn.findViewById(R.id.btnDaGap);
			btnDaGap.setOnClickListener(v -> {
                mDialogShowCheckIn.dismiss();
                Intent intent = new Intent(mContext, LoginAgainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            });
			btnChuaGap = (Button) mDialogShowCheckIn.findViewById(R.id.btnChuaGap);
			btnChuaGap.setOnClickListener(v -> mDialogShowCheckIn.dismiss());
		}
		if(mDialogShowCheckIn!=null && !mDialogShowCheckIn.isShowing())
			mDialogShowCheckIn.show();
	}


	@Override
	public void onNetworkChanged() {
		if (getBaseContext() == null || mLnErrorConnectNetwork == null)
			return;
	}

    @Override
	public void onSignUpChanged() {

        gotoActivity(mContext, LoginActivity.class,
				Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PastaxiPref pasWayPref = new PastaxiPref(mContext);
		pasWayPref.putStringValue(Constants.KEY_Sign_Up_Changed,
				Constants.KEY_Sign_Up_Changed);

		Pasgo.xoaTaiKhoan();
	}

    @Override
    public void onUpdatePasswordChanged() {
        Intent intentf = new Intent(mContext,
                UpdatePassWordDialog.class);
        intentf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intentf, Constants.KEY_UPDATE_PASSWORD_NEW);
    }

    protected void initView() {
	}

	protected void initControl() {
		mIsErrorChannel = false;
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putBoolean(Constants.BUNDLE_STATE_CONNECT, mIsErrorChannel);
		super.onSaveInstanceState(bundle);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mIsErrorChannel = savedInstanceState
				.getBoolean(Constants.BUNDLE_STATE_CONNECT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	protected BaseProgressDialogAllUtils progressDialogUtil;
	protected void showDialog(Context context)
	{
		String message = Utils.getStringByResourse(context,
				R.string.loading_data);
		if (progressDialogUtil == null) {
			progressDialogUtil = new BaseProgressDialogAllUtils(context);
			progressDialogUtil.setCancelable(false);
			progressDialogUtil.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						dialog.dismiss();
					}
					return false;
				}
			});
			progressDialogUtil.show();
		} else {
			if (!progressDialogUtil.isShowing())
				progressDialogUtil.show();
		}
	}
	public void dismissDialog() {

		try {
			if (progressDialogUtil != null) {
				progressDialogUtil.dismiss();
				progressDialogUtil.cancel();
				progressDialogUtil = null;
			}
		} catch (Exception e) {
			Utils.Log(TAG, e.toString());
		}
	}
	public void finishOurLeftInLeft()
	{
		finish();
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
	}
	public void finishToRightToLeft()
	{
		finish();
		overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
	}
	public void finishPushDownInPushDownOut()
	{
		finish();
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
	public void ourLeftInLeft()
	{
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
	}
	public void pushDownInPushDownOut()
	{
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
	public void gotoActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	public void gotoActivity(Context context, Class<?> cla, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		startActivity(intent);
	}

	public void gotoActivitySingleTop(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityWithDelay(intent);
	}
	public void gotoActivityClearTop(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityWithDelay(intent);
	}

	public void gotoActivityClearTop(Context context, Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtras(bundle);
		startActivityWithDelay(intent);
	}
	protected void startActivityWithDelay(final Intent i) {
		if (mIsXLargeDevice
				&& getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			startActivity(i);
		} else {
			if (mFirstLaunch) {
				startActivity(i);
				finishOurLeftInLeft();
				return;
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(i);
				}
			}, 500);
		}
	}

	public void gotoActivity(Context context, Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void gotoActivity(Context context, Class<?> cla, Bundle bundle,
			int flag) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		intent.setFlags(flag);
		startActivity(intent);
	}

	public void gotoActivityForResult(Context context, Class<?> cla,
			int requestCode) {
		Intent intent = new Intent(context, cla);
		startActivityForResult(intent, requestCode);
	}

	public void gotoActivityForResult(Context context, Class<?> cla,
			int requestCode, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		startActivityForResult(intent, requestCode);
	}

	public void gotoActivityForResult(Context context, Class<?> cla,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}

	public void gotoActivityForResult(Context context, Class<?> cla,
			Bundle bundle, int requestCode, int flag) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		intent.setFlags(flag);
		startActivityForResult(intent, requestCode);
	}

	public void showProgressDialog() {
		String message = Utils.getStringByResourse(mContext,
				R.string.loading_data);
		try {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(mContext);
				progressDialog.setMessage(message);
				progressDialog.show();
				progressDialog.setCancelable(true);
			} else {
				if (!progressDialog.isShowing())
					progressDialog.show();
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
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
			Utils.Log(TAG, e.toString());
		}

	}
	public void showProgressDialogAllNoCancel() {
		if (progressDialogAll == null) {
			progressDialogAll = new BaseProgressDialogAllUtils(mContext);
			progressDialogAll.show();
			progressDialogAll.setCancelable(false);
		} else {
			if (!progressDialogAll.isShowing())
				progressDialogAll.show();
		}
	}
	public void showProgressDialogAll() {
		if (progressDialogAll == null) {
			progressDialogAll = new BaseProgressDialogAllUtils(mContext);
			progressDialogAll.show();
			progressDialogAll.setCancelable(true);
		} else {
			if (!progressDialogAll.isShowing())
				progressDialogAll.show();
		}
	}

	public void closeProgressDialogAll() {

		try {
			if (progressDialogAll != null) {
				progressDialogAll.dismiss();
				progressDialogAll.cancel();
				progressDialogAll = null;
			}
		} catch (Exception e) {
			Utils.Log(TAG, e.toString());
		}
	}

	protected void insertTrangThaiHang(String datXeId, int trangThai) {
		String url = WebServiceUtils.URL_INSERT_TRANG_THAI_HANG(Pasgo
				.getInstance().token);
		Utils.Log(TAG, "url dat xe " + url);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("datXeId", datXeId);
			jsonParams.put("trangThaiId", trangThai);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new PWListener() {

					@Override
					public void onResponse(JSONObject json) {
						if (json != null) {
							Utils.Log(TAG, "json " + json);
						}
					}

					@Override
					public void onError(int maloi) {
						closeProgressDialog();
					}

				}, new PWErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						ToastUtils.showToastError(mContext,
								R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
					}
				});
	}

	public void updateTinhSelected() {
		String url = WebServiceUtils
				.URL_UPDATE_TINH_SELECTED(Pasgo.getInstance().token);
		DeviceUuidFactory factory = new DeviceUuidFactory(mActivity);
		if (StringUtils.isEmpty(Pasgo.getInstance().userId))
			Pasgo.getInstance().userId = "";
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("deviceId", factory.getDeviceUuid());
			jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {

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
}