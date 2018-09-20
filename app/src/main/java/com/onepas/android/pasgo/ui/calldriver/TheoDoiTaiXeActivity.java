/*
package com.onepas.android.pasgo.ui.calldriver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ChatInfoMessage;
import com.onepas.android.pasgo.models.InfoChat;
import com.onepas.android.pasgo.models.LocationMessageDriver;
import com.onepas.android.pasgo.models.LyDoHuy;
import com.onepas.android.pasgo.models.PTLocationInfo;
import com.onepas.android.pasgo.models.PingMesage;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.announcements.AnnouncementsActivity;
import com.onepas.android.pasgo.ui.home.DialogRate;
import com.onepas.android.pasgo.util.mapnavigator.DistanceListener;
import com.onepas.android.pasgo.util.mapnavigator.MapUtil;
import com.onepas.android.pasgo.util.mapnavigator.Navigator;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;
import com.onepas.android.pasgo.widgets.MySupportMapFragment;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TheoDoiTaiXeActivity extends BaseAppCompatActivity implements
		OnClickListener, AnimationListener, OnMapReadyCallback  {
	private static final String TAG = "TheoDoiTaiXeActivity";
	private GoogleMap mGoogleMap;
	private String mDriverPhoneNumber = "", mDriverName = "";
	private Animation mAnimSlideDown;
	private Animation mAnimSlideUp;
	private Menu mMenu;
	private ImageView mImgChat, mImgViewMap;
	private ListView mLvChat;
	private boolean mIsShowChat = true;
	private LinearLayout mLnErrorConnectDriver, mLnChatComment, mLnAction,
			mLnThongTinTaiXe, mLnMap;
	private RelativeLayout mLnChat, mRlTooltip, mRelXong;
	private ChatAdapter mChatAdapter;
	private Button mBtnShowLayout, mBtnChat, mBtnMyLocation, mBtnMyLocationAll;
	private EditText mEdtChat;
	private String mLyDo, mStartAddress, mEndAddress;
	private ArrayList<LyDoHuy> mLyDoHuys;

	Vibrator mVibrator;
	int dot = 200;
	int dash = 500;
	int short_gap = 200;
	int medium_gap = 500;
	int long_gap = 1000;
	long[] pattern = {0, dot, short_gap, dot, dash, dot, short_gap, dot, dash,
			dot, short_gap, dot, dash};
	private SoundPool mSoundPool;
	private int mSoundID;
	private LocationMessageDriver mLocationMessageDriver;
	private PTLocationInfo mLocationDriver;
	private ArrayList<InfoChat> mInfoChatArray;

	private double mLat = 0, mLng = 0, mKm, mKmTaiXe, mPhanTramGiamGia,
			mSoPhut, mLaiXeLat, mLaiXeLng, mKhachHangLat, mKhachhangLng;
	private String mGia;
	private String mDatXeId, mLoaiXeName, mThoiGianDonXe, mLaiXeID, mURLImage;
	// private int mLoaiXeId = 0;
	private TextView mTvGia, mTvPhanTramGiamGia, mTvPhut, mTvLoaiXe,
			mTvKmLaiXe, mTvHangXe, mTvHoVaTen, mTvBKS, mTvThongbaoKhuyenMai;
	private String mTenLaiXe, mBienSo, mHangXe, mDiDong;
	private SimpleDraweeView mIvAvatar;
	private PendingIntent mPendingIntent;
	private AlarmManager mAlarmManager;
	private PendingIntent mPendingIntentPing;
	private AlarmManager mAlarmManagerPing;
	private boolean mIsShowMenu = true;
	private ImageView mImRate;
	private long mTimeConnectToDriver;

	private boolean mIsCheck;
	private String mDataDriverInfo;
	private Boolean mIsGoToMap = false;
	private boolean mIsTaiXeNhan = false;
	private Dialog mDialogConfirmCancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_theo_doi_tai_xe);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.theo_doi_tai_xe));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> showDialogHuy());
		mProgressToolbar = (ProgressBar) mToolbar.findViewById(R.id.toolbar_progress_bar);

		this.initView();
		this.getBundle();
		this.getLyDoKhachHangAll();
		this.initControl();
		this.showChat();
		this.onNetworkChanged();
		this.toottip();
		// init map
		MySupportMapFragment fragment = (MySupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		fragment.getMapAsync(this);
	}

	private void toottip() {
		mRlToolTipBottomLeft = (RelativeLayout) findViewById(R.id.rlToolTipBottomLeft);
		TvBottomLeft = (TextView) findViewById(R.id.tvBottomLeft);
		mRlToolTipBottomRight = (RelativeLayout) findViewById(R.id.rlToolTipBottomRight);
		TvBottomRight = (TextView) findViewById(R.id.tvBottomRight);
		TvBottomLeft.setText(R.string.tb_tooltip_tdtx_goi_di_dong);
		TvBottomRight.setText(R.string.tb_tooltip_tdtx_xong);
		if (mToolTipPref != null) {
			if (mToolTipPref.getTheoDoiTaiXeGoiDiDong())
				mRlToolTipBottomLeft.setVisibility(View.GONE);
			else
				mRlToolTipBottomLeft.setVisibility(View.VISIBLE);
			if (mToolTipPref.getTheoDoiTaiXeXong())
				mRlToolTipBottomRight.setVisibility(View.GONE);
			else
				mRlToolTipBottomRight.setVisibility(View.VISIBLE);
		}
	}

	private void startAlarmToolTip() {
		if (mToolTipPref != null) {
			if (mToolTipPref.getTheoDoiTaiXeGoiDiDong()
					&& mToolTipPref.getTheoDoiTaiXeXong())
				return;
		}
		if (mActivity == null)
			return;
		registerReceiver(broadcastReceiverToolTip, new IntentFilter(
				Constants.KEY_TOOL_TIP));
		mPendingIntentToolTip = PendingIntent.getBroadcast(this, 0, new Intent(
				Constants.KEY_TOOL_TIP), 0);
		mAlarmManagerToolTip = (AlarmManager) (this
				.getSystemService(Context.ALARM_SERVICE));
		mAlarmManagerToolTip.setRepeating(AlarmManager.RTC_WAKEUP,
				Constants.MINUTE_TOOLTIP, Constants.MINUTE,
				mPendingIntentToolTip);
	}

	private void cancelAlarmToolTip() {
		try {
			boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
					new Intent(Constants.KEY_TOOL_TIP),
					PendingIntent.FLAG_NO_CREATE) != null);
			Utils.Log(TAG, "alarmUp" + alarmUp);
			if (alarmUp) {
				mAlarmManagerToolTip.cancel(mPendingIntentToolTip);
				unregisterReceiver(broadcastReceiverToolTip);
			}
		} catch (Exception e) {
		}
	}

	private final BroadcastReceiver broadcastReceiverToolTip = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (mToolTipPref != null) {
					mToolTipPref.putTheoDoiTaiXeGoiDiDong(true);
					mRlToolTipBottomLeft.setVisibility(View.GONE);
					mToolTipPref.putTheoDoiTaiXeXong(true);
					mRlToolTipBottomRight.setVisibility(View.GONE);
				}
				cancelAlarmToolTip();
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		startAlarmToolTip();
		IntentFilter intentFilterConnectChannel = new IntentFilter(
				Constants.BROADCAST_ACTION_CONNECT_CHANNEL_STATE);
		registerReceiver(bUpdateStateConnectChannel, intentFilterConnectChannel);

	}

	private final BroadcastReceiver bUpdateStateConnectChannel = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null || intent.getExtras() == null)
				return;
			Bundle bundle = intent.getExtras();
			if (bundle
					.containsKey(Constants.BROADCAST_DATA_CONNECT_CHANNEL_STATE))
				handleUpdateStateChannel
						.sendEmptyMessage(bundle
								.getInt(Constants.BROADCAST_DATA_CONNECT_CHANNEL_STATE));
		}
	};

	Handler handleUpdateStateChannel = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Constants.CONNECT_CHANNEL_STATE_CONNECT:
					if (getBaseContext() == null || mLnErrorConnectNetwork == null)
						return;
					if (NetworkUtils.getInstance(getBaseContext())
							.isNetworkAvailable())
						mLnErrorConnectNetwork.setVisibility(View.GONE);
					mIsErrorChannel = false;
					break;
				case Constants.CONNECT_CHANNEL_STATE_DISCONNECT:
					if (getBaseContext() == null || mLnErrorConnectNetwork == null)
						return;
					mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
					mIsErrorChannel = true;
					break;
				case Constants.CONNECT_CHANNEL_STATE_ERROR:
					if (getBaseContext() == null || mLnErrorConnectNetwork == null)
						return;
					//mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
					mIsErrorChannel = true;
					break;
				case Constants.CONNECT_CHANNEL_STATE_RECONNECT:
					if (getBaseContext() == null || mLnErrorConnectNetwork == null)
						return;
					if (NetworkUtils.getInstance(getBaseContext())
							.isNetworkAvailable())
						mLnErrorConnectNetwork.setVisibility(View.GONE);
					mIsErrorChannel = false;
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.KEY_GO_TO_MAP && resultCode == RESULT_OK) {
			finishToRightToLeft();
		}
	}

	@Override
	protected void initView() {
		super.initView();
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mAnimSlideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
		mAnimSlideDown = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_down);
		mImgChat = (ImageView) findViewById(R.id.imgChat);
		mImgViewMap = (ImageView) findViewById(R.id.imgViewMap);

		mImRate = (ImageView) findViewById(R.id.im_rate);
		mImRate.setOnClickListener(this);

		mLvChat = (ListView) findViewById(R.id.lvChat);
		mLnChat = (RelativeLayout) findViewById(R.id.lnChat);
		mRelXong = (RelativeLayout) findViewById(R.id.lys);
		mRlTooltip = (RelativeLayout) findViewById(R.id.rlTooltip);
		mLnThongTinTaiXe = (LinearLayout) findViewById(R.id.lnThongTinTaiXe);
		mLnMap = (LinearLayout) findViewById(R.id.lnMap);
		mLnErrorConnectDriver = (LinearLayout) findViewById(R.id.lnErrorConnectDriver);
		mLnChatComment = (LinearLayout) findViewById(R.id.lnChatComment);
		mLnAction = (LinearLayout) findViewById(R.id.lnAction);
		mBtnShowLayout = (Button) findViewById(R.id.btnShowLayout);
		mBtnChat = (Button) findViewById(R.id.btnChat);
		mBtnMyLocation = (Button) findViewById(R.id.btnMyLocation);
		mBtnMyLocationAll = (Button) findViewById(R.id.btnMyLocationAll);
		mEdtChat = (EditText) findViewById(R.id.edtChat);

		mTvGia = (TextView) findViewById(R.id.tvGia);
		mTvPhanTramGiamGia = (TextView) findViewById(R.id.tvPhanTramGiamGia);
		mTvPhut = (TextView) findViewById(R.id.tvPhut);
		mTvKmLaiXe = (TextView) findViewById(R.id.tvKmLaiXe);
		mTvLoaiXe = (TextView) findViewById(R.id.tvLoaiXe);
		mTvHangXe = (TextView) findViewById(R.id.tvHangXe);
		mTvHoVaTen = (TextView) findViewById(R.id.tvHoVaTen);
		mTvBKS = (TextView) findViewById(R.id.tvBKS);
		mIvAvatar = (SimpleDraweeView) findViewById(R.id.imgLaiXe);
		mTvThongbaoKhuyenMai = (TextView) findViewById(R.id.tvThongbaoKhuyenMai);
		mAnimSlideUp.setAnimationListener(this);
		mAnimSlideDown.setAnimationListener(this);
		mImgChat.setOnClickListener(this);
		mBtnShowLayout.setOnClickListener(this);
		mBtnMyLocation.setOnClickListener(this);
		mBtnMyLocationAll.setOnClickListener(this);
		mBtnChat.setOnClickListener(this);
		mRelXong.setOnClickListener(this);
	}

	private void getBundle() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mDatXeId = bundle.getString(Constants.BUNDLE_DAT_XE_ID);
			// mLoaiXeId = bundle.getInt(Constants.BUNDLE_LOAIXE_ID);
			mLat = bundle.getDouble(Constants.BUNDLE_START_LAT);
			mLng = bundle.getDouble(Constants.BUNDLE_START_LNG);
			mKhachHangLat = bundle.getDouble(Constants.BUNDLE_KHACHHANG_LAT);
			mKhachhangLng = bundle.getDouble(Constants.BUNDLE_KHACHHANG_LNG);
			mKm = bundle.getDouble(Constants.BUNDLE_KM);
			mPhanTramGiamGia = bundle
					.getDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA);
			mGia = bundle.getString(Constants.BUNDLE_GIA);
			mThoiGianDonXe = bundle.getString(Constants.BUNDLE_THOI_GIAN);
			mStartAddress = bundle.getString(Constants.BUNDLE_START_ADDRESS);
			mEndAddress = bundle.getString(Constants.BUNDLE_END_ADDRESS);
			mLaiXeID = bundle.getString(Constants.BUNDLE_LAIXE_ID);
			mLaiXeLat = bundle.getDouble(Constants.BUNDLE_LAIXE_LAT);
			mLaiXeLng = bundle.getDouble(Constants.BUNDLE_LAIXE_LNG);
			mDriverPhoneNumber = bundle.getString(Constants.BUNDLE_LAIXE_PHONE);
			mDriverName = bundle.getString(Constants.BUNDLE_LAIXE_NAME);
			if (bundle.containsKey(Constants.KEY_GO_TO))
				mIsGoToMap = true;
			else
				mIsGoToMap = false;
			handleUpdateUI.sendEmptyMessage(3);
			setText();
		}
	}

	private void setText() {
		mSoPhut = (mKmTaiXe / 30) * 60;
		String kmGia = String.format("%s%s", mKm + " Km", "~" + mGia + " VND");
		mTvGia.setText(kmGia);
		if (mPhanTramGiamGia > 0) {
			mTvPhanTramGiamGia.setVisibility(View.VISIBLE);
			mTvThongbaoKhuyenMai.setText(StringUtils.getStringByResourse(
					mContext, R.string.da_bao_gom_khuyen_mai));
			int phandu = (int) mPhanTramGiamGia % 1000;
			int phannguyen = (int) mPhanTramGiamGia / 1000;
			String soTien = phannguyen + "";
			mTvPhanTramGiamGia.setText(soTien + "K VND");
			if (phandu > 0) {
				double st = mPhanTramGiamGia / 1000;
				mTvPhanTramGiamGia.setText(Utils.DoubleFomat(st) + "K VND");
			}
		} else {
			mTvPhanTramGiamGia.setVisibility(View.GONE);
			mTvThongbaoKhuyenMai.setText(StringUtils.getStringByResourse(
					mContext, R.string.khong_co_khuyen_mai));
		}
		mTvKmLaiXe.setText(mKmTaiXe + "");
		mTvPhut.setText((int) mSoPhut + " ");
		mTvHangXe.setText(mHangXe);
		mTvHoVaTen.setText(mTenLaiXe);
		mTvBKS.setText(mBienSo);
	}

	@Override
	protected void initControl() {
		super.initControl();
		mIsCheck = true;
		mDataDriverInfo = null;
		mEdtChat.setInputType(InputType.TYPE_MASK_CLASS
				| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		mEdtChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					sendChat();
					return true;
				}
				return false;
			}
		});
		mImgViewMap.setVisibility(View.GONE);
		mLnMap.setVisibility(View.VISIBLE);
		mInfoChatArray = new ArrayList<InfoChat>();
		mTimeConnectToDriver = System.currentTimeMillis();
		setDataChat();
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			getLaiXeById(mLaiXeID);
		} else {
			showAlertMangYeu(1);
		}
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			return;
		if (bundle.containsKey(Constants.KEY_FULL_MESSAGE_DRIVER)) {
			mDataDriverInfo = bundle
					.getString(Constants.KEY_FULL_MESSAGE_DRIVER);
			loadDataAndSubChannel(mDataDriverInfo);
		}
		registerReceiver(broadcastReceiverPubnub, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	private void showAlertMangYeu(final int i) {
		if (!mIsDestroy || getBaseContext() == null)
			return;
		final Dialog dialog = new Dialog(TheoDoiTaiXeActivity.this);
		dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_mang_yeu);

		Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);
		Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
		dialogBtThuLai.setOnClickListener(v -> {
            switch (i) {
                case 1:
                    getLaiXeById(mLaiXeID);
                    break;
                case 2:
                    getLyDoKhachHangAll();
                    break;
                case 3:
                    updateLyDoKhachHangHuy(mLyDo);
                    break;

                default:
                    break;
            }
            dialog.dismiss();
        });
		dialogBtHuy.setOnClickListener(v -> {
            dialog.dismiss();
			finishToRightToLeft();
        });
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	private BroadcastReceiver broadcastReceiverPubnub = new BroadcastReceiver() {
		private boolean isLoad = false;

		@Override
		public void onReceive(Context context, Intent intent) {
			if (context == null)
				return;
			if (!isLoad) {
				isLoad = true;
				return;
			}
			if (intent.getAction().equals(
					ConnectivityManager.CONNECTIVITY_ACTION)) {
				if (NetworkUtils.getInstance(context).isNetworkAvailable())
					Pasgo.getInstance().mPubnub.disconnectAndResubscribe();
			}
		}
	};
	private static final String KEY_TAI_XE_NHAN = "tai_xe_nhan";

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString(Constants.BUNDLE_THOI_GIAN, mThoiGianDonXe);
		bundle.putDouble(Constants.BUNDLE_START_LAT, mLat);
		bundle.putDouble(Constants.BUNDLE_START_LNG, mLng);
		bundle.putDouble(Constants.BUNDLE_KM, mKm);
		bundle.putDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA, mPhanTramGiamGia);
		bundle.putString(Constants.BUNDLE_GIA, mGia);
		bundle.putString(Constants.BUNDLE_START_ADDRESS, mStartAddress);
		bundle.putString(Constants.BUNDLE_END_ADDRESS, mEndAddress);
		bundle.putString(Constants.BUNDLE_DAT_XE_ID, mDatXeId);
		bundle.putString(Constants.BUNDLE_LOAIXE_NAME, mLoaiXeName);
		bundle.putDouble(Constants.BUNDLE_LAIXE_LAT, mLaiXeLat);
		bundle.putDouble(Constants.BUNDLE_LAIXE_LNG, mLaiXeLng);
		bundle.putString(Constants.BUNDLE_LAIXE_ID, mLaiXeID);
		bundle.putString(Constants.BUNDLE_LAIXE_NAME, mDriverName);
		bundle.putString(Constants.BUNDLE_LAIXE_PHONE, mDriverPhoneNumber);
		bundle.putDouble(Constants.BUNDLE_KHACHHANG_LAT, mKhachHangLat);
		bundle.putDouble(Constants.BUNDLE_KHACHHANG_LNG, mKhachhangLng);
		bundle.putSerializable("lstInfoChat", (Serializable) mInfoChatArray);
		bundle.putBoolean(Constants.KEY_GO_TO, mIsGoToMap);
		bundle.putBoolean(KEY_TAI_XE_NHAN, mIsTaiXeNhan);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		try {
			mInfoChatArray = (ArrayList<InfoChat>) savedInstanceState
					.getSerializable("lstInfoChat");
			setDataChat();
		} catch (Exception e) {
			Utils.Log(TAG, "show chat error");
		}
		mDatXeId = savedInstanceState.getString(Constants.BUNDLE_DAT_XE_ID);
		mLat = savedInstanceState.getDouble(Constants.BUNDLE_START_LAT);
		mLng = savedInstanceState.getDouble(Constants.BUNDLE_START_LNG);
		mKm = savedInstanceState.getDouble(Constants.BUNDLE_KM);
		mPhanTramGiamGia = savedInstanceState
				.getDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA);
		mGia = savedInstanceState.getString(Constants.BUNDLE_GIA);
		mThoiGianDonXe = savedInstanceState
				.getString(Constants.BUNDLE_THOI_GIAN);
		mLoaiXeName = savedInstanceState
				.getString(Constants.BUNDLE_LOAIXE_NAME);
		mStartAddress = savedInstanceState
				.getString(Constants.BUNDLE_START_ADDRESS);
		mEndAddress = savedInstanceState
				.getString(Constants.BUNDLE_END_ADDRESS);
		mLaiXeID = savedInstanceState.getString(Constants.BUNDLE_LAIXE_ID);
		mLaiXeLat = savedInstanceState.getDouble(Constants.BUNDLE_LAIXE_LAT);
		mLaiXeLng = savedInstanceState.getDouble(Constants.BUNDLE_LAIXE_LNG);
		mDriverPhoneNumber = savedInstanceState
				.getString(Constants.BUNDLE_LAIXE_PHONE);
		mDriverName = savedInstanceState.getString(Constants.BUNDLE_LAIXE_NAME);
		mKhachHangLat = savedInstanceState
				.getDouble(Constants.BUNDLE_KHACHHANG_LAT);
		mKhachhangLng = savedInstanceState
				.getDouble(Constants.BUNDLE_KHACHHANG_LNG);
		mIsGoToMap = savedInstanceState.getBoolean(Constants.KEY_GO_TO);
		mIsTaiXeNhan = savedInstanceState.getBoolean(KEY_TAI_XE_NHAN);
		if (mIsTaiXeNhan)
			mToolbar.setNavigationIcon(null);
	}

	private void loadDataAndSubChannel(String data) {
		LoadDataAndSubChannel loadDataAndSubChannel = new LoadDataAndSubChannel(
				data);
		loadDataAndSubChannel.execute();
	}


	class LoadDataAndSubChannel extends
			AsyncTask<Void, Void, LocationMessageDriver> {
		String data;

		public LoadDataAndSubChannel(String data) {
			this.data = data;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected LocationMessageDriver doInBackground(Void... params) {
			LocationMessageDriver caller = null;
			try {
				JSONObject jsonObject = new JSONObject(data);
				caller = new LocationMessageDriver();
				if (jsonObject.has(Constants.KEY_ID_MESSAGE_DRIVER))
					caller.setId(jsonObject
							.getString(Constants.KEY_ID_MESSAGE_DRIVER));
				if (jsonObject.has(Constants.KEY_FULL_NAME_MESSAGE_DRIVER))
					caller.setFullName(jsonObject
							.getString(Constants.KEY_FULL_NAME_MESSAGE_DRIVER));
				if (jsonObject.has(Constants.KEY_NUMBER_PLATE_MESSAGE_DRIVER))
					caller.setNumberPlate(jsonObject
							.getString(Constants.KEY_NUMBER_PLATE_MESSAGE_DRIVER));
				if (jsonObject.has(Constants.KEY_TAXI_COMPANY_MESSAGE_DRIVER))
					caller.setTaxiCompany(jsonObject
							.getString(Constants.KEY_TAXI_COMPANY_MESSAGE_DRIVER));
				if (jsonObject.has(Constants.KEY_NUMBER_PHONE_MESSAGE_DRIVER))
					caller.setNumberPhone(jsonObject
							.getString(Constants.KEY_NUMBER_PHONE_MESSAGE_DRIVER));
				if (jsonObject.has(Constants.KEY_LOCATION_MESSAGE_DRIVER)) {
					JSONObject jsonLocation = jsonObject
							.getJSONObject(Constants.KEY_LOCATION_MESSAGE_DRIVER);
					PTLocationInfo location = new PTLocationInfo();
					location.setLat(jsonLocation
							.getDouble(Constants.KEY_LOCATION_LAT_MESSAGE));
					location.setLng(jsonLocation
							.getDouble(Constants.KEY_LOCATION_LNG_MESSAGE));
					if (jsonLocation
							.has(Constants.KEY_LOCATION_ADDRESS_MESSAGE))
						location.setAddress(jsonLocation
								.getString(Constants.KEY_LOCATION_ADDRESS_MESSAGE));
					else
						location.setAddress("");
					caller.setLocation(location);
				}
				mTenLaiXe = caller.getFullName();
				mBienSo = caller.getNumberPlate();
				mHangXe = caller.getTaxiCompany();
				mDiDong = caller.getNumberPhone();
				Pasgo.getInstance().mPubnub.subscribe(
						new String[]{
								Constants.APP_PUBNUB + Constants.SPEC_PUBNUB
										+ Pasgo.getInstance().userId
										+ caller.getId(),
								Constants.APP_PUBNUB_P
										+ Pasgo.getInstance().userId},
						new Callback() {

							@Override
							public void successCallback(String channel,
														Object message) {
								Log.i(TAG, message.toString());
								JSONObject jsonObject = (JSONObject) message;
								if (channel != null
										&& channel
										.equals(Constants.APP_PUBNUB_P
												+ Pasgo.getInstance().prefs
												.getUserId())
										&& jsonObject
										.has(Constants.KEY_PONG_MP))
									parserDataPong(jsonObject);
								else
									parserDataMessage(jsonObject);
							}

							@Override
							public void errorCallback(String channel,
													  PubnubError error) {
								if (error.toString().toLowerCase()
										.contains("disconnect")
										&& Pasgo.getInstance().mPubnub
										.getMaxRetries() == Constants.MAX_RETRIES_PUBNUB - 1)
									Pasgo.getInstance().mPubnub
											.setMaxRetries(Constants.MAX_RETRIES_PUBNUB);
							}
						});
			} catch (Exception e) {
				caller = null;
				Log.e(TAG, e.toString());
			}
			return caller;
		}

		@Override
		protected void onPostExecute(LocationMessageDriver result) {
			super.onPostExecute(result);
			if (result != null)
				mLocationMessageDriver = result;
			setText();
			getStateOrder(mDatXeId,
					EnumDatXeUpdate.LAI_XE_HUY_TREN_DUONG_DON_KHACH_HANG
							.getValue(), false);
			showDialogThongTinTaiXe();
			startAlarmPing();
		}
	}

	private void sendMessageChat(ChatInfoMessage chatInfoMessage) {
		SendMessageTask sendMessageTask = new SendMessageTask(chatInfoMessage);
		sendMessageTask.execute();
	}

	class SendMessageTask extends AsyncTask<Void, Void, Void> {
		int resultRequest;
		ChatInfoMessage chatInfoMessage;
		boolean isNetWork = true;

		public SendMessageTask(ChatInfoMessage chatInfoMessage) {
			this.chatInfoMessage = chatInfoMessage;
			isNetWork = NetworkUtils.getInstance(mContext).isNetworkAvailable();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (isNetWork && chatInfoMessage != null) {
				try {
					Gson gson = new Gson();
					String jsonMessage = gson.toJson(chatInfoMessage);
					JSONObject jsonObject;
					jsonObject = new JSONObject(jsonMessage);
					Pasgo.getInstance().mPubnub.publish(Constants.APP_PUBNUB
									+ Constants.SPEC_PUBNUB
									+ Pasgo.getInstance().userId
									+ mLocationMessageDriver.getId(), jsonObject,
							new Callback() {
								@Override
								public void successCallback(String channel,
															Object message) {
								}

								@Override
								public void errorCallback(String channel,
														  PubnubError error) {
									if (mActivity != null)
										mActivity.runOnUiThread(new Runnable() {
											public void run() {
												ToastUtils
														.showToast(
																mActivity,
																mActivity
																		.getResources()
																		.getString(
																				R.string.chat_error_check_network));
											}
										});
								}
							});
				} catch (JSONException e) {
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (!isNetWork)
				handleSendMessage.sendEmptyMessage(1);
		}
	}

	Handler handleSendMessage = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					ToastUtils.showToast(mActivity, mActivity.getResources()
							.getString(R.string.chat_error_check_network));
					break;
				case 2:
					if (!isFinishing())
						DialogUtils.showOkDialogNoCancel(mActivity, R.string.tb_lai_xe_huy_dat_xe,
								R.string.dong_y, (dialog, id) -> finish());

					break;
				case 3:
					if (!isFinishing())
						DialogUtils.showOkDialogNoCancel(mActivity, R.string.tb_toi_da_den_don_ban,
								R.string.dong_y, (dialog, id) -> {
                                });
					break;
				case 4:
					mIsTaiXeNhan = true;
					mToolbar.setNavigationIcon(null);
					if (!isFinishing())
						DialogUtils.showOkDialogNoCancel(mActivity, R.string.tb_khach_len_xe,
								R.string.dong_y, (dialog, id) -> {
                                });

					break;
				case 5:
					if (!isFinishing())
						DialogUtils.showOkDialogNoCancel(mActivity, R.string.tb_khach_xuong_xe,
								R.string.dong_y, (dialog, id) -> {
                                });

					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	private void startAlarmPing() {
		if (getBaseContext() == null)
			return;
		registerReceiver(broadcastReceiverPing, new IntentFilter(
				Constants.BROADCAST_ACTION_PING_DRIVER));
		mPendingIntentPing = PendingIntent.getBroadcast(this, 0, new Intent(
				Constants.BROADCAST_ACTION_PING_DRIVER), 0);
		mAlarmManagerPing = (AlarmManager) (this
				.getSystemService(Context.ALARM_SERVICE));
		mAlarmManagerPing.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), Constants.MINUTE * 10,
				mPendingIntentPing);
	}

	private void cancelAlarmPing() {
		try {
			boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
					new Intent(Constants.BROADCAST_ACTION_PING_DRIVER),
					PendingIntent.FLAG_NO_CREATE) != null);
			Utils.Log(TAG, "alarmUp" + alarmUp);
			if (alarmUp) {
				mAlarmManagerPing.cancel(mPendingIntentPing);
				unregisterReceiver(broadcastReceiverPing);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private void startAlarm() {
		if (mActivity == null)
			return;
		registerReceiver(broadcastReceiverUpdateLocation, new IntentFilter(
				Constants.KEY_ACTION_FIND_TAXI));
		mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				Constants.KEY_ACTION_FIND_TAXI), 0);
		mAlarmManager = (AlarmManager) (this
				.getSystemService(Context.ALARM_SERVICE));
		mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), Constants.MINUTE * 60,
				mPendingIntent);
	}

	private void cancelAlarm() {
		try {
			boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
					new Intent(Constants.KEY_ACTION_FIND_TAXI),
					PendingIntent.FLAG_NO_CREATE) != null);
			Utils.Log(TAG, "alarmUp" + alarmUp);
			if (alarmUp) {
				mAlarmManager.cancel(mPendingIntent);
				unregisterReceiver(broadcastReceiverUpdateLocation);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private final BroadcastReceiver broadcastReceiverPing = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			long now = System.currentTimeMillis();
			Log.i(TAG, "timenow: " + now);
			Log.i(TAG, "mTimeConnectToDriver: " + mTimeConnectToDriver);
			long delay = (now - mTimeConnectToDriver) - (10 * Constants.MINUTE);
			Log.i(TAG, "time delay: " + delay);
			if (delay < 10 * Constants.MINUTE)
				handleUpdateUI.sendEmptyMessage(4);
			else
				handleUpdateUI.sendEmptyMessage(5);
			sendPing(Constants.APP_PUBNUB_P + Pasgo.getInstance().userId);
		}
	};

	private void sendPing(String channelName) {
		PingMesage pingMessage = new PingMesage(System.currentTimeMillis(),
				channelName);
		SendPingTask sendPingTask = new SendPingTask(pingMessage);
		sendPingTask.execute();
	}

	class SendPingTask extends AsyncTask<Void, Boolean, Void> {
		PingMesage pingMessage;
		boolean isNetWork = true;

		public SendPingTask(PingMesage pingMessage) {
			this.pingMessage = pingMessage;
			isNetWork = NetworkUtils.getInstance(mContext).isNetworkAvailable();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (isNetWork && pingMessage != null) {
				try {
					Gson gson = new Gson();
					String jsonMessage = gson.toJson(pingMessage);
					JSONObject jsonObject;
					jsonObject = new JSONObject(jsonMessage);
					Utils.Log(TAG, "channel to driver" + Constants.APP_PUBNUB + Constants.SPEC_PUBNUB
							+ mLocationMessageDriver.getId());
					Pasgo.getInstance().mPubnub.publish(
							Constants.APP_PUBNUB + Constants.SPEC_PUBNUB
									+ mLocationMessageDriver.getId(),
							jsonObject, new Callback() {
								@Override
								public void successCallback(String channel,
															Object message) {
									Utils.Log(TAG, "successCallback ping" + message);
								}

								@Override
								public void errorCallback(String channel,
														  PubnubError error) {
									if (mActivity != null)
										Utils.Log(TAG, "errorCallback ping" + error);
								}
							});
				} catch (JSONException e) {
					Log.e(TAG, e.toString());
				}
			}
			return null;
		}
	}

	private final BroadcastReceiver broadcastReceiverUpdateLocation = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			Utils.Log(TAG, "set location 1");
			if (bundle != null) {
				Utils.Log(TAG, "set location 2");
				handleUpdateUI.sendEmptyMessage(3);
			}
		}
	};

	synchronized private void parserDataPong(JSONObject jsonObject) {
		mTimeConnectToDriver = System.currentTimeMillis();
		Log.i(TAG, "mTimeConnectToDriver" + mTimeConnectToDriver);
		handleUpdateUI.sendEmptyMessage(4);
	}

	synchronized private void parserDataMessage(JSONObject jsonObject) {
		try {
			if (jsonObject != null) {
				int key = jsonObject.getInt(Constants.KEY_KIND_OF_MP);
				switch (key) {
					case Constants.VALUE_KIND_OF_CALL_MP: {
						InfoChat infoChat = new InfoChat();
						JSONObject jsonObjectChat = jsonObject
								.getJSONObject(Constants.KEY_INFO_MP);
						if (jsonObjectChat.has(Constants.KEY_NAME_CALLER_MP))
							infoChat.setName(jsonObjectChat
									.getString(Constants.KEY_NAME_CALLER_MP));
						if (jsonObjectChat.has(Constants.KEY_CONTENT_CALLER_MP))
							infoChat.setContent(jsonObjectChat
									.getString(Constants.KEY_CONTENT_CALLER_MP));
						if (jsonObjectChat.has(Constants.KEY_TIME_CALLER_MP))
							infoChat.setTime(jsonObjectChat
									.getString(Constants.KEY_TIME_CALLER_MP));
						mInfoChatArray.add(infoChat);
						handleUpdateUI.sendEmptyMessage(0);
						break;
					}
					case Constants.VALUE_CANCEL_KIND_OF_CALL_MP: {
						handleSendMessage.sendEmptyMessage(2);
						break;
					}
					case Constants.VALUE_UPDATE_KIND_OF_CALL_MP: {
						mLocationDriver = new PTLocationInfo();
						JSONObject objLocation = ParserUtils.getJsonObject(
								jsonObject, "location");
						mLocationDriver.setLat(objLocation
								.getDouble(Constants.KEY_LOCATION_LAT_MESSAGE));
						mLocationDriver.setLng(objLocation
								.getDouble(Constants.KEY_LOCATION_LNG_MESSAGE));
						mLaiXeLat = mLocationDriver.getLat();
						mLaiXeLng = mLocationDriver.getLng();
						Utils.Log(TAG, "mLaiXeLat" + mLaiXeLat + "mLaiXeLng"
								+ mLaiXeLng);
						handleUpdateUI.sendEmptyMessage(1);
						break;
					}
					case Constants.VALUE_RING_KIND_OF_CALL_MP: {
						this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
						mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
						mSoundPool
								.setOnLoadCompleteListener(new OnLoadCompleteListener() {
									@Override
									public void onLoadComplete(SoundPool soundPool,
															   int sampleId, int status) {
										AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
										float actualVolume = (float) audioManager
												.getStreamVolume(AudioManager.STREAM_MUSIC);
										float maxVolume = (float) audioManager
												.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
										float volume = actualVolume / maxVolume;
										mSoundPool.play(mSoundID, volume, volume,
												1, 0, 1f);
									}
								});
						mSoundID = mSoundPool.load(this, R.raw.sound, 1);
						mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						mVibrator.vibrate(pattern, -1);
						handleSendMessage.sendEmptyMessage(3);
						break;
					}
					case Constants.VALUE_GO_UP_KIND_OF_CALL_MP: {
						handleSendMessage.sendEmptyMessage(4);
						break;
					}
					case Constants.VALUE_DOWNWARD_KIND_OF_CALL_MP: {
						// bo xuong xe
						//handleSendMessage.sendEmptyMessage(5);
						break;
					}
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}


	Handler handleUpdateUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					if (getBaseContext() != null) {
						setDataChat();
						mIsShowChat = false;
						showChat();
					}
					break;

				case 1:
					if (getBaseContext() != null) {
						Utils.Log(TAG, "upadate location" + mLaiXeLat + "lng: "
								+ mLaiXeLng);
						if (mGoogleMap != null) {
							mGoogleMap.clear();
							LatLng latLngDiemDon = new LatLng(mLat, mLng);
							mGoogleMap
									.addMarker(new MarkerOptions()
											.position(latLngDiemDon)
											.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.ic_diemdon_kh))
											.snippet("-2"));
							LatLng latLngKhachHang = new LatLng(mKhachHangLat,
									mKhachhangLng);
							mGoogleMap.addMarker(new MarkerOptions()
									.position(latLngKhachHang)
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.ic_nguoidung))
									.snippet("-4"));
							LatLng latLngTaxi = new LatLng(mLaiXeLat, mLaiXeLng);
							mGoogleMap.addMarker(new MarkerOptions()
									.position(latLngTaxi)
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.taxi_ranh))
									.snippet("-3"));
							if (mLat == 0 || mLng == 0 || mLaiXeLat == 0
									|| mLaiXeLng == 0) {
								return;
							}
						}
					}
					break;

				case 2:
					if (getBaseContext() != null) {
						mTvLoaiXe.setText(mLoaiXeName);
						if (!StringUtils.isEmpty(mURLImage)) {
							mIvAvatar.setImageURI(mURLImage);
						}
					}
					break;
				case 3:
					if (getBaseContext() != null) {
						Utils.Log(TAG, "upadate location" + mLaiXeLat + "lng: "
								+ mLaiXeLng);
						if (mGoogleMap != null) {
							mGoogleMap.clear();
							LatLng latLngDiemDon = new LatLng(mLat, mLng);
							mGoogleMap
									.addMarker(new MarkerOptions()
											.position(latLngDiemDon)
											.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.ic_diemdon_kh))
											.snippet("-2"));
							LatLng latLngKhachHang = new LatLng(mKhachHangLat,
									mKhachhangLng);
							mGoogleMap.addMarker(new MarkerOptions()
									.position(latLngKhachHang)
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.ic_nguoidung))
									.snippet("-4"));
							LatLng latLngTaxi = new LatLng(mLaiXeLat, mLaiXeLng);
							mGoogleMap.addMarker(new MarkerOptions()
									.position(latLngTaxi)
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.taxi_ranh))
									.snippet("-3"));
							if (mGoogleMap != null) {
								mGoogleMap.moveCamera(CameraUpdateFactory
										.newLatLng(latLngDiemDon));
								mGoogleMap.animateCamera(CameraUpdateFactory
										.zoomTo(15));
							}
							if (mLat == 0 || mLng == 0 || mLaiXeLat == 0
									|| mLaiXeLng == 0) {
								return;
							}
							mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
							Navigator navigator = new Navigator(latLngDiemDon,
									latLngTaxi, new DistanceListener() {

								@Override
								public void loadData(
										ArrayList<String> arrDistance,
										String srt) {
									mProgressToolbar.setVisibility(ProgressBar.GONE);
									String distance = arrDistance.get(0);
									try {
										mKmTaiXe = ParserUtils
												.getDoubleValue(
														new JSONObject(
																distance),
														"value");
										mKmTaiXe = Utils
												.DoubleFomat(mKmTaiXe / 1000);
										setText();
									} catch (JSONException e) {
										e.printStackTrace();
									}
									Utils.Log(TAG, "distance" + distance);
								}
							});
							navigator.findDistance(mContext, false);
						}
					}
					break;
				case 4:
					if (getBaseContext() == null || mLnErrorConnectDriver == null)
						return;
					mLnErrorConnectDriver.setVisibility(View.GONE);
					break;
				case 5:
					if (getBaseContext() == null || mLnErrorConnectDriver == null)
						return;
					mLnErrorConnectDriver.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	private void getStateOrder(final String bookingId, int stateId,
							   final boolean isClose) {
		String url = WebServiceUtils.URL_VERIFY_TRANG_THAI_DAT_XE(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("datXeId", bookingId);
			jsonParams.put("trangThaiDatXeId", stateId);
			showProgressDialog();
			if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
				Pasgo.getInstance().addToRequestQueue(url, jsonParams,
						new PWListener() {

							@Override
							public void onResponse(JSONObject json) {
								if (json != null) {
									if (isClose) {
										closeProgressDialog();
										if (ParserUtils.getStateOrder(json)) {
											new SaveDataAsyncTask(null, null,
													null, null, null, null,
													null, null, null, null,
													null, null, null, null,
													null, null, null, null,
													null, null, null).execute();
											handleShowPopup.sendEmptyMessage(0);
										}
									} else {
										if (ParserUtils.getStateOrder(json)) {
											closeProgressDialog();
											new SaveDataAsyncTask(null, null,
													null, null, null, null,
													null, null, null, null,
													null, null, null, null,
													null, null, null, null,
													null, null, null).execute();
											handleShowPopup.sendEmptyMessage(0);
											return;
										}
										getStateOrder(
												bookingId,
												EnumDatXeUpdate.LAI_XE_DA_DE_KHACH_HANG_XUONG_XE_HOAN_THANH_DAT_XE
														.getValue(), true);
									}
								}
							}

							@Override
							public void onError(int maloi) {
							}

						}, new PWErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
							}
						});
			} else {
				ToastUtils.showToast(mContext,
						R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
			}
		} catch (Exception e) {
			ToastUtils.showToast(mContext,
					R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
		}
	}

	class SaveDataAsyncTask extends AsyncTask<Void, Void, Void> {
		String data, thoiGianDonXe, startLat, startLng, km, phanTramGiamGia,
				gia, startAddress, endLat, endLng, khachHangLat, khachhangLng,
				endAddress, datXeId, loaiXeName, laiXeLat, laiXeLng, laiXeId,
				laiXeName, laiXePhone, loaiXeId;

		public SaveDataAsyncTask(String data, String thoiGianDonXe,
								 String startLat, String startLng, String km,
								 String phanTramGiamGia, String gia, String startAddress,
								 String endLat, String endLng, String khachHangLat,
								 String khachhangLng, String endAddress, String datXeId,
								 String loaiXeName, String laiXeLat, String laiXeLng,
								 String laiXeId, String laiXeName, String laiXePhone,
								 String loaiXeId) {
			this.data = data;
			this.thoiGianDonXe = thoiGianDonXe;
			this.startLat = startLat;
			this.startLng = startLng;
			this.km = km;
			this.phanTramGiamGia = phanTramGiamGia;
			this.gia = gia;
			this.startAddress = startAddress;
			this.endLat = endLat;
			this.endLng = endLng;
			this.khachHangLat = khachHangLat;
			this.khachhangLng = khachhangLng;
			this.endAddress = endAddress;
			this.datXeId = datXeId;
			this.loaiXeName = loaiXeName;
			this.laiXeLat = laiXeLat;
			this.laiXeLng = laiXeLng;
			this.laiXeId = laiXeId;
			this.laiXeName = laiXeName;
			this.laiXePhone = laiXePhone;
			this.loaiXeId = loaiXeId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Pasgo.getInstance()
					.getPaswayPref()
					.putDataOrder(data, thoiGianDonXe, startLat + "",
							startLng + "", km + "", phanTramGiamGia + "",
							gia + "", startAddress, endLat, endLng,
							khachHangLat + "", khachhangLng + "", endAddress,
							datXeId, loaiXeName, laiXeLat + "", laiXeLng + "",
							laiXeId, laiXeName, laiXePhone, loaiXeId);
			return null;
		}
	}

	private final Handler handleShowPopup = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					if (!isFinishing())
						DialogUtils.showOkDialog(mActivity,
								R.string.tb_chuyen_di_ket_thuc, R.string.dong_y,
								(dialog, which) -> {
                                    dialog.dismiss();
									finishToRightToLeft();
                                });
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onDestroy() {
		if (mIsCheck)
			new SaveDataAsyncTask(mDataDriverInfo, mThoiGianDonXe, mLat + "",
					mLng + "", mKm + "", mPhanTramGiamGia + "", mGia + "",
					mStartAddress, null, null, mKhachHangLat + "",
					mKhachhangLng + "", mEndAddress, mDatXeId, null, mLaiXeLat
					+ "", mLaiXeLng + "", mLaiXeID, mDriverName,
					mDriverPhoneNumber, null).execute();
		else
			new SaveDataAsyncTask(null, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null,
					null, null, null, null).execute();
		try {
			if (bUpdateStateConnectChannel != null)
				unregisterReceiver(bUpdateStateConnectChannel);
			if (broadcastReceiverPubnub != null)
				unregisterReceiver(broadcastReceiverPubnub);
		} catch (IllegalArgumentException e) {
		}
		if (mLocationMessageDriver != null)
			Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
					+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId
					+ mLocationMessageDriver.getId());
		Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
				+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId);
		Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB_P
				+ Pasgo.getInstance().userId);
		cancelAlarm();
		cancelAlarmPing();
		super.onDestroy();
	}

	;


	private void fullMap(boolean isShow) {
		if (isShow) {
			mLnChatComment.setVisibility(View.GONE);
			mLnThongTinTaiXe.setVisibility(View.GONE);
			mLnAction.setVisibility(View.GONE);
			mRlTooltip.setVisibility(View.GONE);
			mBtnShowLayout.setVisibility(View.VISIBLE);
			mBtnMyLocation.setVisibility(View.GONE);
			mBtnMyLocationAll.setVisibility(View.VISIBLE);
			if (mIsShowMenu) {
				mIsShowMenu = false;
				updateMenuItem(mIsShowMenu);
			}
		} else {
			mLnChatComment.setVisibility(View.VISIBLE);
			mLnThongTinTaiXe.setVisibility(View.VISIBLE);
			mLnAction.setVisibility(View.VISIBLE);
			mRlTooltip.setVisibility(View.VISIBLE);
			mBtnShowLayout.setVisibility(View.GONE);
			mBtnMyLocation.setVisibility(View.VISIBLE);
			mBtnMyLocationAll.setVisibility(View.GONE);
			if (!mIsShowMenu) {
				mIsShowMenu = true;
				updateMenuItem(mIsShowMenu);
			}
		}
	}

	private void setDataChat() {
		mChatAdapter = new ChatAdapter(mContext, mInfoChatArray);
		mLvChat.setAdapter(mChatAdapter);
		mLvChat.setSelection(mInfoChatArray.size() - 1);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_thong_tin_dat_xe, menu);
		this.mMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_Share:
				//ToastUtils.showToast(mContext, R.string.tb_post_facebook);
				return true;
			case R.id.menu_notifi_tin_km:
				gotoActivity(mContext, AnnouncementsActivity.class,
						Intent.FLAG_ACTIVITY_CLEAR_TOP);// TinKhuyenMaiFrg
				ourLeftInLeft();
				break;
			case R.id.menu_show:
				if (!mIsShowChat) {
					mIsShowMenu = !mIsShowMenu;
					updateMenuItem(mIsShowMenu);
				}
				break;
		}
		return true;
	}

	private void updateMenuItem(boolean isShow) {
		try {
			MenuItem item;
			item = mMenu.findItem(R.id.menu_show);
			if (isShow == false) {
				item.setIcon(Utils.getDrawable(mContext, R.drawable.ic_nut_hien));
				mLnThongTinTaiXe.setVisibility(View.VISIBLE);
				mLnThongTinTaiXe.startAnimation(mAnimSlideUp);
			} else {
				item.setIcon(Utils.getDrawable(mContext, R.drawable.ic_nut_an));
				mLnThongTinTaiXe.setVisibility(View.GONE);
				mLnThongTinTaiXe.startAnimation(mAnimSlideDown);
			}
		} catch (Exception e) {
			Utils.Log(TAG, e.toString());
		}
	}

	private void sendChat() {
		if (mEdtChat.getText().toString() == null
				|| mEdtChat.getText().toString().equals("")) {
			Toast.makeText(mContext, R.string.nhap_binh_luan,
					Toast.LENGTH_SHORT).show();
		} else {
			InfoChat infoChat = new InfoChat();
			PTLocationInfo location = new PTLocationInfo();
			ChatInfoMessage chatInfoMessage = new ChatInfoMessage();
			// setData
			infoChat.setChatId(Pasgo.getInstance().userId);
			infoChat.setName(Pasgo.getInstance().username);
			infoChat.setContent(mEdtChat.getText().toString());
			infoChat.setTime(DatehepperUtil
					.getCurrentDate(DatehepperUtil.yyyyMMddHHmmss));

			location.setLat(mLat);
			location.setLng(mLng);
			location.setAddress(mStartAddress);

			chatInfoMessage.setInfo(infoChat);
			chatInfoMessage.setKindOf(Constants.VALUE_KIND_OF_CALL_MP);
			chatInfoMessage.setLocation(location);
			sendMessageChat(chatInfoMessage);
			mEdtChat.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		LatLng latLng;
		switch (v.getId()) {
			case R.id.im_rate:
				showDialogRate();
				break;
			case R.id.imgChat:
				showChat();
				break;
			case R.id.lys:
				break;
			case R.id.btnShowLayout:
				fullMap(false);
				break;
			case R.id.btnChat:
				sendChat();
				break;
			case R.id.btnMyLocation:
				latLng = new LatLng(mLat, mLng);
				if (mGoogleMap != null) {
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}
				break;
			case R.id.btnMyLocationAll:
				latLng = new LatLng(mLat, mLng);
				if (mGoogleMap != null) {
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}
				break;
			default:
				break;
		}
	}

	private String getNoiDungTN() {
		String noiDung = "PASTAXI da tim duoc tai xe cho ban:" + "\n"
				+ "Tai xe: " + mDriverName + "\n" + "SDT: "
				+ mDriverPhoneNumber + "\n" + "BSX: " + mBienSo + "\n"
				+ "Hang xe: " + mHangXe + "\n" + "vi tri: " + "cach ban "
				+ mKmTaiXe + " km";
		return noiDung;
	}

	private void guiTinNhanChoKH(String noiDung) {
		// Uri uri = Uri.parse("smsto:" + Pastaxi.getInstance().sdtKhachHang);
		// Intent share = new Intent(Intent.ACTION_SENDTO, uri);
		Utils.Log("TheoDoiTaiXe", "Noi dung sms :" + noiDung);
		Intent share = new Intent(android.content.Intent.ACTION_VIEW);
		share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		share.putExtra("address", Pasgo.getInstance().sdtKhachHang);
		share.setType("vnd.android-dir/mms-sms");
		share.putExtra("sms_body", noiDung);
		startActivity(Intent.createChooser(share, "Send SMS"));
		Pasgo.getInstance().sdtKhachHang = "";
		finishToRightToLeft();
	}

	@Override
	public void finish() {
		mIsCheck = false;
		setResult(RESULT_OK);
		super.finish();
	}

	private void showChat() {
		if (!mIsShowChat) {
			mLnChat.setVisibility(View.VISIBLE);
			mImgChat.setImageResource(R.drawable.ic_chat_xuong);
			mIsShowChat = !mIsShowChat;
			if (mIsShowMenu) {
				mIsShowMenu = false;
				updateMenuItem(mIsShowMenu);
			}
		} else {
			mLnChat.setVisibility(View.GONE);
			mImgChat.setImageResource(R.drawable.ic_chat_len);
			mIsShowChat = !mIsShowChat;
			if (!mIsShowMenu) {
				mIsShowMenu = true;
				updateMenuItem(mIsShowMenu);
			}
			InputMethodManager imm = (InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEdtChat.getWindowToken(), 0);
		}
	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
		int status = googleAPI.isGooglePlayServicesAvailable(this);
		if (status != ConnectionResult.SUCCESS) {
			int requestCode = 10;
			googleAPI.getErrorDialog(this, status,
					requestCode).show();
		} else {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			mGoogleMap.setMyLocationEnabled(false);
			mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
			mGoogleMap
					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
						@Override
						public void onInfoWindowClick(Marker marker) {
						}
					});
			mGoogleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng arg0) {
					Utils.Log(TAG, "googmap click ");
					fullMap(true);
				}
			});
		}
	}

	public void callOnClickHandler(View v) {
		if (mToolTipPref != null) {
			mToolTipPref.putTheoDoiTaiXeGoiDiDong(true);
			mRlToolTipBottomLeft.setVisibility(View.GONE);
		}
		showDialogGoi();
	}

	public void onClickXong(View v) {
		if (mToolTipPref != null) {
			mToolTipPref.putTheoDoiTaiXeXong(true);
			mRlToolTipBottomRight.setVisibility(View.GONE);
		}
		if (Constants.IS_OPERATOR) {
			String noiDung = getNoiDungTN();
			guiTinNhanChoKH(noiDung);
			Pasgo.getInstance().sdtKhachHang = "";
		} else {
			showDialogXong();
		}
	}

	private void call() {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ mDriverPhoneNumber));
		startActivity(intent);
	}

	private void showDialogThongTinTaiXe() {
		if (isFinishing() || getBaseContext() == null)
			return;
		Location locationKh = new Location("A");
		locationKh.setLatitude(mLat);
		locationKh.setLongitude(mLng);
		Location locationTX = new Location("B");
		locationTX.setLatitude(mLaiXeLat);
		locationTX.setLongitude(mLaiXeLng);
		double km = MapUtil.Distance(locationKh, locationTX);
		final Dialog mDialogThongTinTaiXe;
		mDialogThongTinTaiXe = new Dialog(mContext);
		mDialogThongTinTaiXe.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialogThongTinTaiXe
				.setContentView(R.layout.layout_popup_thong_tin_tai_xe);
		TextView tvTaxi, tvHangXe, tvHoTen, tvPhone, tvKm;
		tvTaxi = (TextView) mDialogThongTinTaiXe.findViewById(R.id.tvTaxi);
		tvHangXe = (TextView) mDialogThongTinTaiXe.findViewById(R.id.tvHangXe);
		tvHoTen = (TextView) mDialogThongTinTaiXe.findViewById(R.id.tvHoTen);
		tvPhone = (TextView) mDialogThongTinTaiXe.findViewById(R.id.tvPhone);
		tvKm = (TextView) mDialogThongTinTaiXe.findViewById(R.id.tvKm);
		tvTaxi.setText(mBienSo);
		tvHangXe.setText(mHangXe);
		tvHoTen.setText(mTenLaiXe);
		tvPhone.setText(mDiDong);
		String textKm = tvKm.getText().toString();
		tvKm.setText(String.format("%s %s %s", textKm, km, "Km"));
		TextView txTheoDoiTaiXe = (TextView) mDialogThongTinTaiXe
				.findViewById(R.id.tvTheoDoiTaiXe);
		txTheoDoiTaiXe.setOnClickListener(v -> mDialogThongTinTaiXe.dismiss());
		mDialogThongTinTaiXe.show();
	}

	private void showDialogGoi() {
		if (isFinishing() || getBaseContext() == null)
			return;
		final Dialog mDialogGoi;
		mDialogGoi = new Dialog(mContext);
		mDialogGoi.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialogGoi.setContentView(R.layout.layout_popup_goi_tai_xe);
		Button btnGoi, btnHuy;
		TextView tvHoTen = (TextView) mDialogGoi.findViewById(R.id.tvHoTen);
		TextView tvPhonePopup = (TextView) mDialogGoi
				.findViewById(R.id.tvPhonePopup);
		tvPhonePopup.setText(" " + mDriverPhoneNumber);
		tvHoTen.setText(mDriverName);
		btnGoi = (Button) mDialogGoi.findViewById(R.id.btnGoi);
		btnGoi.setOnClickListener(v -> {
            call();
            mDialogGoi.dismiss();
        });
		btnHuy = (Button) mDialogGoi.findViewById(R.id.btnHuy);
		btnHuy.setOnClickListener(v -> mDialogGoi.dismiss());
		mDialogGoi.show();
	}

	private void showDialogXong() {
		if (getBaseContext() == null)
			return;
		final Dialog mDialogThongTinTaiXe;
		mDialogThongTinTaiXe = new Dialog(mContext);
		mDialogThongTinTaiXe.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialogThongTinTaiXe.setContentView(R.layout.layout_popup_xong);
		mDialogThongTinTaiXe.setCancelable(false);
		Button btnDaGap, btnChuaGap;
		btnDaGap = (Button) mDialogThongTinTaiXe.findViewById(R.id.btnDaGap);
		btnDaGap.setOnClickListener(v -> {
            sendKindOfMessage(Constants.VALUE_MEET_KIND_OF_CALL_MP);
            DatXeUpdate.updateDatXeByTrangThai(mDatXeId,
                    EnumDatXeUpdate.KHACH_HANG_DA_GAP_LAI_XE.getValue());
            mDialogThongTinTaiXe.dismiss();

            Intent intent = new Intent(mActivity,
                    TimTaiXeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_COMPLETE,
                    Constants.KEY_INT_COMPLETE);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
			finishToRightToLeft();
        });
		btnChuaGap = (Button) mDialogThongTinTaiXe
				.findViewById(R.id.btnChuaGap);
		btnChuaGap.setOnClickListener(v -> mDialogThongTinTaiXe.dismiss());
		if(mDialogThongTinTaiXe!=null && !mDialogThongTinTaiXe.isShowing() && !isFinishing())
			mDialogThongTinTaiXe.show();
	}

	private void showDialogHuy() {
		if(mIsTaiXeNhan){
			mToolbar.setNavigationIcon(null);
			return;
		}else {
			kiemTraTrangThaiDatXe();
		}
	}




	@Override
	public void onAnimationEnd(Animation arg0) {

	}

	@Override
	public void onAnimationRepeat(Animation arg0) {

	}

	@Override
	public void onAnimationStart(Animation arg0) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event != null && keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			// them de bo nut huy
			showDialogHuy();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void getLyDoKhachHangAll() {
		String url = WebServiceUtils.URL_GET_ALL_LY_DO_KHACH_HANG(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			try {
				Pasgo.getInstance().addToRequestQueue(url, jsonParams,
						new PWListener() {

							@Override
							public void onResponse(JSONObject json) {
								if (json != null) {
									Utils.Log(TAG, "json getLyDoKhachHangAll"
											+ json);
									mLyDoHuys = ParserUtils.getLyDoHuys(json);
								}
							}

							@Override
							public void onError(int maloi) {
								closeProgressDialog();
							}

						}, new PWErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Utils.Log(TAG, "khong ket noi may chu");
								showAlertMangYeu(2);
							}
						});
			} catch (Exception e) {
				Utils.Log(TAG, "khong ket noi may chu");
			}
		} else {
			showAlertMangYeu(2);
		}
	}

	private void updateLyDoKhachHangHuy(String lyDo) {
		String url = WebServiceUtils.URL_UPDATE_LY_DO_KHACH_HANG_HUY(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			try {
				jsonParams.put("datXeId", mDatXeId);
				jsonParams.put("lyDo", lyDo);
				Pasgo.getInstance().addToRequestQueue(url, jsonParams,
						new PWListener() {

							@Override
							public void onResponse(JSONObject json) {
								if (json != null) {
									Utils.Log(TAG, "json huy" + json);
									finishToRightToLeft();
								}
							}

							@Override
							public void onError(int maloi) {
								closeProgressDialog();
							}

						}, new PWErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Utils.Log(TAG, "khong ket noi may chu");
								showAlertMangYeu(3);
							}
						});
			} catch (Exception e) {
				Utils.Log(TAG, "khong ket noi may chu");
			}
		} else {
			showAlertMangYeu(3);
		}
	}

	private void sendKindOfMessage(int kindOfMessage) {
		try {
			if(mLocationMessageDriver==null) return;
			Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
					+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId
					+ mLocationMessageDriver.getId());
			Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
					+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId);
			Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB_P
					+ Pasgo.getInstance().userId);
			InfoChat infoChat = new InfoChat();
			ChatInfoMessage chatInfoMessage = new ChatInfoMessage();
			chatInfoMessage.setKindOf(kindOfMessage);
			chatInfoMessage.setInfo(infoChat);
			Gson gson = new Gson();
			String jsonMessage = gson.toJson(chatInfoMessage);
			final JSONObject jsonObject;
			jsonObject = new JSONObject(jsonMessage);
			Pasgo.getInstance().mPubnub.publish(Constants.APP_PUBNUB
							+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId
							+ mLocationMessageDriver.getId(), jsonObject,
					new Callback() {
						@Override
						public void successCallback(String channel,
													Object message) {
						}

						@Override
						public void errorCallback(String channel,
												  PubnubError error) {
							Pasgo.getInstance().mPubnub.publish(
									Constants.APP_PUBNUB
											+ Constants.SPEC_PUBNUB
											+ Pasgo.getInstance().userId
											+ mLocationMessageDriver.getId(),
									jsonObject, new Callback() {
										@Override
										public void successCallback(
												String channel, Object message) {
										}

										@Override
										public void errorCallback(
												String channel,
												PubnubError error) {
											if (mActivity != null)
												mActivity
														.runOnUiThread(new Runnable() {
															public void run() {
																ToastUtils
																		.showToast(
																				mActivity,
																				mActivity
																						.getResources()
																						.getString(
																								R.string.chat_error_check_network));
															}
														});
										}
									});
						}
					});
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

	public void huy() {
		try {
			if (mLocationMessageDriver != null
					&& Pasgo.getInstance().prefs != null) {
				Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
						+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId
						+ mLocationMessageDriver.getId());
				Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB
						+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId);
				Pasgo.getInstance().mPubnub.unsubscribe(Constants.APP_PUBNUB_P
						+ Pasgo.getInstance().userId);
				InfoChat infoChat = new InfoChat();
				ChatInfoMessage chatInfoMessage = new ChatInfoMessage();
				chatInfoMessage
						.setKindOf(Constants.VALUE_CANCEL_KIND_OF_CALL_MP);
				chatInfoMessage.setInfo(infoChat);
				Gson gson = new Gson();
				String jsonMessage = gson.toJson(chatInfoMessage);
				final JSONObject jsonObject;
				jsonObject = new JSONObject(jsonMessage);
				Pasgo.getInstance().mPubnub.publish(Constants.APP_PUBNUB
								+ Constants.SPEC_PUBNUB + Pasgo.getInstance().userId
								+ mLocationMessageDriver.getId(), jsonObject,
						new Callback() {
							@Override
							public void successCallback(String channel,
														Object message) {
							}

							@Override
							public void errorCallback(String channel,
													  PubnubError error) {
								Pasgo.getInstance().mPubnub.publish(
										Constants.APP_PUBNUB
												+ Constants.SPEC_PUBNUB
												+ Pasgo.getInstance().userId
												+ mLocationMessageDriver
												.getId(), jsonObject,
										new Callback() {
											@Override
											public void successCallback(
													String channel,
													Object message) {
											}

											@Override
											public void errorCallback(
													String channel,
													PubnubError error) {
												if (mActivity != null)
													mActivity
															.runOnUiThread(new Runnable() {
																public void run() {
																	ToastUtils
																			.showToast(
																					mActivity,
																					mActivity
																							.getResources()
																							.getString(
																									R.string.chat_error_check_network));
																}
															});
											}
										});
							}
						});
			}
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

	private void getLaiXeById(String laiXeId) {
		String url = WebServiceUtils
				.URL_GET_LAI_XE_BY_ID(Pasgo.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			try {
				jsonParams.put("laiXeId", laiXeId);
				Pasgo.getInstance().addToRequestQueue(url, jsonParams,
						new PWListener() {

							@Override
							public void onResponse(JSONObject json) {
								if (json != null) {
									Utils.Log(TAG, "json lai xe" + json);
									JSONObject objItem = ParserUtils
											.getJsonObject(json, "Item");
									mURLImage = ParserUtils.getStringValue(
											objItem, "AnhLaiXe");
									mLoaiXeName = ParserUtils.getStringValue(
											objItem, "TenLoaiXe");
									handleUpdateUI.sendEmptyMessage(2);
								}
							}

							@Override
							public void onError(int maloi) {
								closeProgressDialog();
							}

						}, new PWErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Utils.Log(TAG, "khong ket noi may chu");
								showAlertMangYeu(1);
							}
						});
			} catch (Exception e) {
				Utils.Log(TAG, "khong ket noi may chu");
			}
		} else {
			showAlertMangYeu(1);
		}
	}

	public void kiemTraTrangThaiDatXe() {
		String url = WebServiceUtils.URL_KIEM_TRA_TRANG_THAI_DAT_XE(Pasgo
				.getInstance().token);
		JSONObject jsonObject =new JSONObject();
		try {
			jsonObject.put("datXeId", mDatXeId);
		} catch (JSONException e1) {
		}
		showProgressDialogAll();
		Pasgo.getInstance().addToRequestQueue(url, jsonObject,
				new PWListener() {

					@Override
					public void onResponse(JSONObject json) {
						if (json != null) {
							closeProgressDialogAll();
							JSONObject jsonObject = ParserUtils.getJsonObject(json, "Item");
							mIsTaiXeNhan = ParserUtils.getIntValue(jsonObject, "IsTaiXeNhan") == 1 ? true : false;
							if (mIsTaiXeNhan) {
								mToolbar.setNavigationIcon(null);
								ToastUtils.showToast(mContext, R.string.tb_khach_len_xe);
								return;
							} else {
								if (isFinishing() || getBaseContext() == null)
									return;
								showYesNoDialog(mContext, R.string.ban_chac_chan_muon_huy,
										R.string.dong_y, R.string.huy);
							}
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
						ToastUtils.showToast(mContext,
								R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
					}
				});
	}

	public void showYesNoDialog(final Context context, int messageId,
								int OkTextId, int cancelTextId) {

		if(mDialogConfirmCancel==null) {
			mDialogConfirmCancel = new Dialog(context);
			mDialogConfirmCancel.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogConfirmCancel.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
		}
		TextView tvThongBaoPopup=(TextView) mDialogConfirmCancel.findViewById(R.id.tvThongBaoPopup);
		Button btnDongY,btnHuy;
		btnDongY = (Button)mDialogConfirmCancel.findViewById(R.id.btnDongY);
		btnHuy = (Button)mDialogConfirmCancel.findViewById(R.id.btnHuy);
		btnDongY.setOnClickListener(view -> {
            if(mIsTaiXeNhan)
            {
                mToolbar.setNavigationIcon(null);
                mDialogConfirmCancel.dismiss();
                mDialogConfirmCancel.cancel();
                return;
            }
            mLyDo = "";
            final Dialog mDialogThongTinTaiXe;
            mDialogThongTinTaiXe = new Dialog(mContext);
            mDialogThongTinTaiXe.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogThongTinTaiXe.setContentView(R.layout.layout_popup_huy);
            Button btnGui;
            Spinner spnChoXe;
            btnGui = (Button) mDialogThongTinTaiXe.findViewById(R.id.btnGui);
            spnChoXe = (Spinner) mDialogThongTinTaiXe
                    .findViewById(R.id.spnChoXe);
            final EditText edtLyDoKhac = (EditText) mDialogThongTinTaiXe
                    .findViewById(R.id.edtLyDoKhac);
            List<String> list = new ArrayList<String>();
            if (mLyDoHuys != null)
                for (LyDoHuy lyDoHuy : mLyDoHuys) {

                    list.add(lyDoHuy.getName());
                }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                    mContext, android.R.layout.simple_spinner_item, list);
            dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChoXe.setAdapter(dataAdapter);
            spnChoXe.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView,
                                           View selectedItemView, int position, long id) {
                    mLyDo = mLyDoHuys.get(position).getName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            btnGui.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(mIsTaiXeNhan)
                    {
                        mToolbar.setNavigationIcon(null);
                        if(mDialogConfirmCancel!= null && mDialogConfirmCancel.isShowing()) {
                            mDialogConfirmCancel.dismiss();
                            mDialogConfirmCancel.cancel();
                        }
                        mDialogThongTinTaiXe.dismiss();
                        mDialogThongTinTaiXe.cancel();
                        return;
                    }
                    if (!StringUtils.isEmpty(edtLyDoKhac))
                        mLyDo = edtLyDoKhac.getText().toString();
                    updateLyDoKhachHangHuy(edtLyDoKhac.getText().toString());
                    DatXeUpdate
                            .updateDatXeByTrangThai(
                                    mDatXeId,
                                    EnumDatXeUpdate.KHACH_HANG_HUY_TREN_DUONG_LAI_XE_DEN_DON_KHACH
                                            .getValue());
                    mDialogThongTinTaiXe.dismiss();
                    huy();
                    finishToRightToLeft();
                }
            });
            mDialogThongTinTaiXe.show();
        });
		btnDongY.setText(context.getString(OkTextId));
		btnHuy.setText(context.getString(cancelTextId));
		btnHuy.setOnClickListener(view -> mDialogConfirmCancel.cancel());
		tvThongBaoPopup.setText(context.getString(messageId));
		if(mDialogConfirmCancel!=null && !mDialogConfirmCancel.isShowing())
			mDialogConfirmCancel.show();
	}
	@Override
	public void onNetworkChanged() {
		if (getBaseContext() == null || mLnErrorConnectNetwork == null)
			return;
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()
				&& !mIsErrorChannel)
			mLnErrorConnectNetwork.setVisibility(View.GONE);
		else
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
	}

	private void showDialogRate() {
		Intent intent = new Intent(mContext, DialogRate.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("DatXeId", mDatXeId);
		startActivity(intent);
	}

	@Override
	public void onStartMoveScreen() {

	}

	@Override
	public void onUpdateMapAfterUserInterection() {
	}
}*/
