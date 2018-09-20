package com.onepas.android.pasgo.ui.calldriver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DriverRequestObject;
import com.onepas.android.pasgo.models.HangXe;
import com.onepas.android.pasgo.models.MapStep;
import com.onepas.android.pasgo.models.RequestHangXeOrderObject;
import com.onepas.android.pasgo.models.RequestOrderObject;
import com.onepas.android.pasgo.models.RouteMap;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.util.mapnavigator.DistanceListener;
import com.onepas.android.pasgo.util.mapnavigator.Navigator;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;
import com.onepas.android.pasgo.widgets.MySupportMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class XacNhanDatXeActivity extends BaseAppCompatActivity implements
		OnClickListener, AnimationListener,OnMapReadyCallback {
	private String TAG = "XacNhanDatXeActivity";
	private double mEndLat = 0, mEndLng = 0, mStartLat = 0, mStartLng = 0,
			mKm = 0.0, mPhanTramGiamGia, mKhachHangLat, mKhachhangLng;
	private String mGia = "";
	private String mStartAddress, mEndAddress, mThoiGianDonXe,
			mMoTa = "", mLoaiXeName, mMaKhuyenMai = "";
	private int mDichVuId = -1, mDriverNumber;
	private int mLoaiHinhDichVuId;
	private int mNhomXeDichVuId;
	private TextView mTvDiaDiemHienTai, mTvDiaDiemDen, mTvPrice, mTvKm;
	private Button mBtnTiepTuc;
	private GoogleMap mGoogleMap;
	private ArrayList<MarkerOptions> mMarkers;
	private String mStrDirection;

	private RequestOrderObject mRequestOrderObject;
	private String mNhomCNDoiTacId;
	private Boolean mIsGoToMap;
	private int mDatXeFromFree;
	private boolean mIsDatXe = false;
	private TextView mTvPromotion;
	private String mDatXeId = "";
	private LinearLayout mLnThongTinTaiXe;
	private Button mBtnShowLayout;
	private Animation mAnimSlideDown;
	private Animation mAnimSlideUp;
	private Bundle mSaveInstanceState;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xac_nhan_dat_xe);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.xac_nhan_title));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(mActivity, DatXeActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putInt("XacNhanBackToDatXe", 1);
            intent.putExtras(bundle1);
            setResult(Constants.kEY_BACK_FORM_DATXE, intent);
            finishToRightToLeft();
        });
		mProgressToolbar = (ProgressBar) mToolbar.findViewById(R.id.toolbar_progress_bar);
		this.initView();
		this.getBundle();
		this.onNetworkChanged();
		this.toottip();
		mSaveInstanceState = savedInstanceState;
		// init map
		MySupportMapFragment fragment = (MySupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		fragment.getMapAsync(this);
	}

	private void getBundle() {
		Bundle savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {

			mNhomCNDoiTacId = savedInstanceState.getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID);
			mKhachHangLat = savedInstanceState
					.getDouble(Constants.BUNDLE_KHACHHANG_LAT);
			mKhachhangLng = savedInstanceState
					.getDouble(Constants.BUNDLE_KHACHHANG_LNG);
			mDichVuId = savedInstanceState.getInt(Constants.BUNDLE_DICH_VU_ID);
			mLoaiHinhDichVuId = savedInstanceState
					.getInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID);
			mNhomXeDichVuId = savedInstanceState
					.getInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID);
			mStartLat = savedInstanceState
					.getDouble(Constants.BUNDLE_START_LAT);
			mStartLng = savedInstanceState
					.getDouble(Constants.BUNDLE_START_LNG);
			mKm = savedInstanceState.getDouble(Constants.BUNDLE_KM);
			mStartAddress = savedInstanceState
					.getString(Constants.BUNDLE_START_ADDRESS);
			mEndLat = savedInstanceState.getDouble(Constants.BUNDLE_END_LAT);
			mEndLng = savedInstanceState.getDouble(Constants.BUNDLE_END_LNG);
			mEndAddress = savedInstanceState
					.getString(Constants.BUNDLE_END_ADDRESS);
			mPhanTramGiamGia = savedInstanceState
					.getDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA);
			mMoTa = savedInstanceState.getString(Constants.BUNDLE_MOTA);
			mLoaiXeName = savedInstanceState
					.getString(Constants.BUNDLE_LOAIXE_NAME);
			mDriverNumber = savedInstanceState
					.getInt(Constants.BUNDLE_NUMBER_DRIVER);
			mThoiGianDonXe = savedInstanceState
					.getString(Constants.BUNDLE_THOI_GIAN);
			mMaKhuyenMai = savedInstanceState
					.getString(Constants.BUNDLE_KHUYEN_MAI);
			if (StringUtils.isEmpty(mMaKhuyenMai)) {
				mMaKhuyenMai = "";
			}
			mDatXeFromFree = savedInstanceState.getInt(Constants.KEY_GO_TO);
			if (savedInstanceState.containsKey(Constants.KEY_GO_TO))
				mIsGoToMap = true;
			else
				mIsGoToMap = false;
		}
	}

	private String getPromotion() {
		String sPromotion = "";
		if (mPhanTramGiamGia > 0) {
			int phandu = (int) mPhanTramGiamGia % 1000;
			int phannguyen = (int) mPhanTramGiamGia / 1000;
			String soTien = phannguyen + "";
			sPromotion = soTien + "K VND";
			if (phandu > 0) {
				double st = mPhanTramGiamGia / 1000;
				sPromotion = Utils.DoubleFomat(st) + "K VND";
			}
			sPromotion = String.format(StringUtils.getStringByResourse(
					mContext, R.string.promotion_to_driver), sPromotion);
		} else {
			sPromotion = "";
		}
		return sPromotion;
	}

	private void toottip() {
		mRlToolTipBottomCenter = (RelativeLayout) findViewById(R.id.rlToolTipBottomCenter);
		TvBottomCenter = (TextView) findViewById(R.id.tvBottomCenter);
		TvBottomCenter.setText(R.string.tb_tooltip_tieptuc);
		if (mToolTipPref != null) {
			if (mToolTipPref.getXacNhanTiepTuc())
				mRlToolTipBottomCenter.setVisibility(View.GONE);
			else
				mRlToolTipBottomCenter.setVisibility(View.VISIBLE);
		}
	}

	private void startAlarmToolTip() {
		if (mToolTipPref != null) {
			if (!mToolTipPref.getXacNhanTiepTuc())
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
			e.printStackTrace();
		}
	}

	private final BroadcastReceiver broadcastReceiverToolTip = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (mToolTipPref != null) {
					mToolTipPref.putXacNhanTiepTuc(true);
					mRlToolTipBottomCenter.setVisibility(View.GONE);
				}
				cancelAlarmToolTip();
			}
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putInt(Constants.BUNDLE_DICH_VU_ID, mDichVuId);
		bundle.putDouble(Constants.BUNDLE_START_LAT, mStartLat);
		bundle.putDouble(Constants.BUNDLE_START_LNG, mStartLng);
		bundle.putString(Constants.BUNDLE_START_ADDRESS, mStartAddress);
		bundle.putString(Constants.BUNDLE_LOAIXE_NAME, mLoaiXeName);
		bundle.putInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID, mLoaiHinhDichVuId);
		bundle.putInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID, mNhomXeDichVuId);
		bundle.putString(Constants.BUNDLE_THOI_GIAN, mThoiGianDonXe);
		bundle.putDouble(Constants.BUNDLE_END_LAT, mEndLat);
		bundle.putDouble(Constants.BUNDLE_END_LNG, mEndLng);
		bundle.putString(Constants.BUNDLE_END_ADDRESS, mEndAddress);
		bundle.putDouble(Constants.BUNDLE_KM, mKm);
		bundle.putDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA, mPhanTramGiamGia);
		bundle.putString(Constants.BUNDLE_GIA, mGia);
		bundle.putString(Constants.BUNDLE_MOTA, mMoTa);
		bundle.putString(Constants.BUNDLE_KHUYEN_MAI, mMaKhuyenMai);
		bundle.putInt(Constants.BUNDLE_NUMBER_DRIVER, mDriverNumber);
		bundle.putString(Constants.KEY_DIRECTION_JSON, mStrDirection);
		bundle.putDouble(Constants.BUNDLE_KHACHHANG_LAT, mKhachHangLat);
		bundle.putDouble(Constants.BUNDLE_KHACHHANG_LNG, mKhachhangLng);
		bundle.putBoolean(Constants.KEY_GO_TO, mIsGoToMap);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		getValueFromsavedInstanceState(savedInstanceState);
	}

	private void getValueFromsavedInstanceState(Bundle savedInstanceState) {
		mKhachHangLat = savedInstanceState
				.getDouble(Constants.BUNDLE_KHACHHANG_LAT);
		mKhachhangLng = savedInstanceState
				.getDouble(Constants.BUNDLE_KHACHHANG_LNG);
		mDichVuId = savedInstanceState.getInt(Constants.BUNDLE_DICH_VU_ID);
		mStartLat = savedInstanceState.getDouble(Constants.BUNDLE_START_LAT);
		mStartLng = savedInstanceState.getDouble(Constants.BUNDLE_START_LNG);
		mKm = savedInstanceState.getDouble(Constants.BUNDLE_KM);
		mPhanTramGiamGia = savedInstanceState
				.getDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA);
		mGia = savedInstanceState.getString(Constants.BUNDLE_GIA);
		mStartAddress = savedInstanceState
				.getString(Constants.BUNDLE_START_ADDRESS);
		mEndLat = savedInstanceState.getDouble(Constants.BUNDLE_END_LAT);
		mEndLng = savedInstanceState.getDouble(Constants.BUNDLE_END_LNG);
		mEndAddress = savedInstanceState
				.getString(Constants.BUNDLE_END_ADDRESS);
		mMaKhuyenMai = savedInstanceState.getString(Constants.BUNDLE_KHUYEN_MAI);
		mMoTa = savedInstanceState.getString(Constants.BUNDLE_MOTA);
		mLoaiXeName = savedInstanceState
				.getString(Constants.BUNDLE_LOAIXE_NAME);
		mLoaiHinhDichVuId = savedInstanceState
				.getInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID);
		mNhomXeDichVuId = savedInstanceState
				.getInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID);
		mDriverNumber = savedInstanceState
				.getInt(Constants.BUNDLE_NUMBER_DRIVER);
		mThoiGianDonXe = savedInstanceState
				.getString(Constants.BUNDLE_THOI_GIAN);
		mStrDirection = savedInstanceState
				.getString(Constants.KEY_DIRECTION_JSON);
		mIsGoToMap = savedInstanceState.getBoolean(Constants.KEY_GO_TO);
		setText();
	}

	@Override
	protected void initView() {
		super.initView();
		mAnimSlideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
		mAnimSlideDown = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_down);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mTvDiaDiemHienTai = (TextView) findViewById(R.id.tvDiaDiemHienTai);
		mTvDiaDiemDen = (TextView) findViewById(R.id.tvDiaDiemDen);
		mTvPromotion = (TextView) findViewById(R.id.tvPromotion);
		mTvPrice = (TextView) findViewById(R.id.tvPrice);
		mTvKm = (TextView) findViewById(R.id.tvKm);
		mBtnTiepTuc = (Button) findViewById(R.id.btnTiepTuc);
		mLnThongTinTaiXe = (LinearLayout) findViewById(R.id.lnThongTinTaiXe);
		mBtnShowLayout = (Button) findViewById(R.id.btnShowLayout);
		mBtnTiepTuc.setOnClickListener(this);
		mBtnShowLayout.setOnClickListener(this);
		mAnimSlideUp.setAnimationListener(this);
		mAnimSlideDown.setAnimationListener(this);
	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;

		// Enabling Zoom Layer of Google Map
		mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
		// double click
		mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
		// move
		mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
		LatLng latLng = new LatLng(mKhachHangLat, mKhachhangLng);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latLng).zoom(14).build();
		mGoogleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				showOrHideFunction(false);
			}
		});
		// Enabling MyLocation Layer of Google Map
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

		if (mSaveInstanceState != null && !StringUtils.isEmpty(mGia)
				&& !StringUtils.isEmpty(mStrDirection)) {
			getValueFromsavedInstanceState(mSaveInstanceState);
			Utils.setBackground(mBtnTiepTuc,
					Utils.getDrawable(mContext, R.drawable.btn_all));
			mIsDatXe = true;
			loadDirections();
			handlerUocTinhChiPhi.sendEmptyMessage(1);
		} else
			this.initControl();
	}

	synchronized public void loadDirections() {
		if (mGoogleMap == null)
			return;
		mGoogleMap.clear();
		final LatLng startLocation = new LatLng(mStartLat, mStartLng);
		final LatLng endLocation = new LatLng(mEndLat, mEndLng);
		BitmapDescriptor startIcon = null;
		Bitmap startLargeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_nguoidung);
		startIcon = BitmapDescriptorFactory.fromBitmap(startLargeIcon);
		final MarkerOptions startMarker = new MarkerOptions()
				.position(startLocation).title(mStartAddress).icon(startIcon);
		mGoogleMap.addMarker(startMarker);
		BitmapDescriptor endIcon = null;
		Bitmap endLargeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_diemden);
		endIcon = BitmapDescriptorFactory.fromBitmap(endLargeIcon);
		final MarkerOptions endMarker = new MarkerOptions()
				.position(endLocation).title(mEndAddress).icon(endIcon);
		mGoogleMap.addMarker(endMarker);
		Navigator nav = new Navigator(mGoogleMap, startLocation, endLocation);
		nav.DrawSingle(mStrDirection, 0);
		JSONObject obj;
		try {
			obj = new JSONObject(mStrDirection);
			RouteMap distanceGoogleMap = new RouteMap();
			distanceGoogleMap = ParserUtils.parserGoogleMap(obj).get(0);
			List<MapStep> mapSteps = distanceGoogleMap.getMapStepInfos();
			mMarkers = new ArrayList<MarkerOptions>();
			for (MapStep mapStep : mapSteps) {
				double lat = Double.parseDouble(mapStep.getStartLocationLat());
				double lng = Double.parseDouble(mapStep.getStartLocationLng());
				LatLng latLng = new LatLng(lat, lng);
				BitmapDescriptor icon = null;
				Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_map);
				icon = BitmapDescriptorFactory.fromBitmap(largeIcon);
				final MarkerOptions marker = new MarkerOptions()
						.position(latLng).title(mapStep.getHtml_instructions())
						.snippet(mapStep.getTravel_mode()).icon(icon);
				mGoogleMap.addMarker(marker);
				mMarkers.add(marker);
			}
			final LatLngBounds.Builder b = new LatLngBounds.Builder();
			if (mMarkers.size() > 0) {
				for (MarkerOptions m : mMarkers) {
					b.include(m.getPosition());
				}
				final LinearLayout layout = (LinearLayout) findViewById(R.id.lnMap);
				ViewTreeObserver vto = layout.getViewTreeObserver();
				vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						layout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						int width = layout.getMeasuredWidth();
						int height = layout.getMeasuredHeight();
						LatLngBounds boundss = b.build();
						CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(
								boundss, width, height, 50);
						mGoogleMap.animateCamera(cu);
					}
				});

			}
			closeProgressDialogAll();
		} catch (Exception e) {
			if (getBaseContext() != null)
				closeProgressDialogAll();
			Log.i(TAG, e.toString());
		}
	}

	private void setText() {
		mTvDiaDiemHienTai.setText(mStartAddress);
		mTvDiaDiemDen.setText(mEndAddress);
		if (mKm == 0.0)
			mTvKm.setText("... Km");
		else
			mTvKm.setText(mKm + " Km");
		if (StringUtils.isEmpty(mGia))
			mTvPrice.setText("... VND");
		else
			mTvPrice.setText("~" + mGia + " VND");
		if (StringUtils.isEmpty(getPromotion()))
			mTvPromotion.setVisibility(View.GONE);
		else
		{
			mTvPromotion.setText(getPromotion());
			mTvPromotion.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void initControl() {
		super.initControl();
		mTvPromotion.setVisibility(View.GONE);
		setText();
		mIsDatXe = false;
		Utils.setBackground(mBtnTiepTuc,
				Utils.getDrawable(mContext, R.drawable.btn_gray));
		mRequestOrderObject = new RequestOrderObject();
		handlerUocTinhChiPhi.sendEmptyMessage(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		startAlarmToolTip();
	}

	public void onPause() {		
		super.onPause();
		cancelAlarmToolTip();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Utils.Log(TAG, "requestCode: " + requestCode + "resultCode: " + resultCode);
		if (requestCode == Constants.KEY_GO_TO_MAP && resultCode == RESULT_OK) {
			if (data != null) {
				// setResult(RESULT_OK);
				// finishToRightToLeft();
				// return;

				Bundle bundle = data.getExtras();
				int complete = 0;
				if (bundle != null) {
					complete = bundle.getInt(Constants.KEY_COMPLETE);
				}
				if (complete == Constants.KEY_INT_COMPLETE) {
					Intent intent = null;
					if (mDatXeFromFree == Constants.KEY_GO_TO_MAP) {
						intent = new Intent(mActivity,
								DatXeActivity.class);
						Bundle bundle1 = new Bundle();
						bundle1.putInt(Constants.KEY_COMPLETE,
								Constants.KEY_INT_COMPLETE);
						intent.putExtras(bundle1);
						setResult(RESULT_OK, intent);
					} else {
						// intent = new Intent(mActivity, DatXeActivity.class);
						setResult(Constants.kEY_BACK_FORM_DATXE);
					}
					finishToRightToLeft();
				} else {
					finishToRightToLeft();
				}
			}
		}
		if (requestCode == Constants.kEY_BACK_FORM_DATXE) {
			setResult(Constants.kEY_BACK_FORM_DATXE);
		}
		if (requestCode == Constants.KEY_GO_TO_MAP) {
			Intent intent = null;
			if (mDatXeFromFree == Constants.KEY_GO_TO_MAP) {
				intent = new Intent(mActivity, DatXeActivity.class);
				Bundle bundle1 = new Bundle();
				bundle1.putInt(Constants.KEY_COMPLETE,
						Constants.KEY_INT_COMPLETE);
				intent.putExtras(bundle1);
				setResult(RESULT_OK, intent);
				finishToRightToLeft();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_xac_nhan_dat_xe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(mActivity, DatXeActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putInt("XacNhanBackToDatXe", 1);
			intent.putExtras(bundle1);
			setResult(Constants.kEY_BACK_FORM_DATXE, intent);
			finishToRightToLeft();
			return true;
		case R.id.menu_huy_xac_nhan:
			if (mDatXeFromFree == Constants.KEY_GO_TO_MAP) {
				Intent i = null;
				i = new Intent(mActivity, DatXeActivity.class);
				Bundle b = new Bundle();
				b.putInt(Constants.KEY_COMPLETE, Constants.KEY_INT_COMPLETE);
				i.putExtras(b);
				setResult(RESULT_OK, i);
			} else {
				setResult(Constants.kEY_BACK_FORM_DATXE);
			}
			finishToRightToLeft();
			break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTiepTuc:
			if (!mIsDatXe)
				return;
			String khachHangId = Pasgo.getInstance().userId;
			String maGiamGia = mMaKhuyenMai;
			String diaChiDonXe = mStartAddress;
			double viDoDonXe = mStartLat;
			double kinhDoDonXe = mStartLng;
			String diaChiDen = mEndAddress;
			double viDoDen = mEndLat;
			double kinhDoDen = mEndLng;
			String mota = mMoTa;
			if (mToolTipPref != null) {
				mToolTipPref.putXacNhanTiepTuc(true);
				mRlToolTipBottomCenter.setVisibility(View.GONE);
			}
			String sdt = "";
			int loaiDatXe = 0;
			if (Constants.IS_OPERATOR) {
				sdt = Pasgo.getInstance().sdtKhachHang;
				loaiDatXe = 1;
			}
			if (maGiamGia == null) {
				maGiamGia = "";
			}
			mRequestOrderObject = new RequestOrderObject(khachHangId,mNhomXeDichVuId,
					sdt, maGiamGia, diaChiDonXe, viDoDonXe, kinhDoDonXe,
					mThoiGianDonXe, diaChiDen, viDoDen, kinhDoDen, mota, mKm,
					mGia, loaiDatXe, mLoaiHinhDichVuId, mDichVuId);

			loadDriverToServer(mRequestOrderObject);
			break;
        case R.id.btnShowLayout:
            showOrHideFunction(true);
            break;
		default:
			break;
		}
	}

	DialogInterface.OnClickListener onAcceptClick = (dialog, which) -> {
        dialog.dismiss();
		finishToRightToLeft();
    };

	private void loadDriverToServer(RequestOrderObject requestOrderObject) {
		LoadDriverToServer loadDriverToServer = new LoadDriverToServer(
				requestOrderObject);
		loadDriverToServer.execute();
	}



	class LoadDriverToServer extends AsyncTask<Void, Void, JSONObject> {
		RequestOrderObject requestOrderObject;

		public LoadDriverToServer(RequestOrderObject requestOrderObject) {
			this.requestOrderObject = requestOrderObject;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			if (requestOrderObject == null)
				return null;
			ArrayList<DriverRequestObject> driverRequestObjects = new ArrayList<DriverRequestObject>();
			try {
				// Đoạn này đã bỏ phần lấy tài xế để gửi lên, tài xế giờ sẽ lấy trên server
				Gson gson = new Gson();
				requestOrderObject.setDriverRequestObjects(gson
						.toJson(driverRequestObjects));
				gson = new Gson();
				String json = gson.toJson(requestOrderObject);
				JSONObject jsonObject = new JSONObject(json);
				return jsonObject;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			Utils.Log(TAG,result.toString());
			if (result == null) {
				closeProgressDialog();
				ToastUtils.showToast(mContext,
						R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
			} else
				addDatXe(result);
		}
	}

	private void addDatXe(JSONObject jsonObject) {

		String url = WebServiceUtils
				.URL_ADD_DAT_XE(Pasgo.getInstance().token);
		try {
			if(StringUtils.isEmpty(mNhomCNDoiTacId))
				mNhomCNDoiTacId="";
			jsonObject.put("nhomCNDoiTacId", mNhomCNDoiTacId);
		} catch (JSONException e1) {
		}
		Pasgo.getInstance().addToRequestQueue(url, jsonObject,
				new PWListener() {

					@Override
					public void onResponse(JSONObject json) {
						if (json != null) {
							boolean datTruoc = false;
							String driversSorted = null;
							closeProgressDialog();
							Utils.Log(TAG, "json " + json);
							try {
								JSONObject jsonObject = json
										.getJSONObject("Item");
								datTruoc = ParserUtils.getBooleanValue(jsonObject, "DatTruoc"); 
								mDatXeId = ParserUtils.getStringValue(jsonObject, "Id");
								/*if(StringUtils.isEmpty(mNhomCNDoiTacId)) {
									pubChannelOnepasCheckIn(mDatXeId, mNhomCNDoiTacId);
									pubChannelParnerCheckIn(mDatXeId, mNhomCNDoiTacId);
								}
								if (jsonObject.has("LaiXes")) {
									JSONArray jsonArray = jsonObject
											.getJSONArray("LaiXes");
									mDriverNumber = jsonArray.length();
									driversSorted = jsonArray.toString();
								} else {
									mDriverNumber = 0;
								}
								if (Constants.IS_OPERATOR) {
									timTaiXe(json, driversSorted);
								} else {
									if (datTruoc) {
										loadTongDaiHang();
										Intent intentf = new Intent(mContext,
												DialogConfirmOk.class);
										intentf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intentf);
										Intent intent = null;
										if (mDatXeFromFree == Constants.KEY_GO_TO_MAP) {
											intent = new Intent(mActivity, DatXeActivity.class);
											Bundle bundle1 = new Bundle();
											bundle1.putInt(Constants.KEY_COMPLETE,
													Constants.KEY_INT_COMPLETE);
											intent.putExtras(bundle1);
											setResult(RESULT_OK, intent);
											finishToRightToLeft();
										}
									} else {
										timTaiXe(json, driversSorted);
									}
								}*/
								loadTongDaiHang();
								Intent intentf = new Intent(mContext,
										DialogConfirmOk.class);
								intentf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intentf);
								Intent intent = null;
								if (mDatXeFromFree == Constants.KEY_GO_TO_MAP) {
									intent = new Intent(mActivity, DatXeActivity.class);
									Bundle bundle1 = new Bundle();
									bundle1.putInt(Constants.KEY_COMPLETE,
											Constants.KEY_INT_COMPLETE);
									intent.putExtras(bundle1);
									setResult(RESULT_OK, intent);
									finishToRightToLeft();
								}
							} catch (JSONException e) {
								handleUpdateUI.sendEmptyMessage(0);
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

	/*protected void timTaiXe(JSONObject json, String jsonArray) {
		Bundle bundle = new Bundle();
		HashMap<String, String> mapData = ParserUtils.getBookingIdAndTime(json);
		if (mapData.containsKey("Id"))
			bundle.putString(Constants.BUNDLE_DAT_XE_ID, mapData.get("Id"));
		if (mapData.containsKey("ThoiGian"))
			bundle.putString(Constants.BUNDLE_TIME_BOOKING,
					mapData.get("ThoiGian"));
		bundle.putInt(Constants.BUNDLE_DICH_VU_ID, mDichVuId);
        bundle.putInt(Constants.BUNDLE_LOAI_HINH_DICH_VU_ID,mLoaiHinhDichVuId);
        bundle.putInt(Constants.BUNDLE_NHOM_XE_DICH_VU_ID, mNhomXeDichVuId);
		bundle.putDouble(Constants.BUNDLE_START_LAT, mStartLat);
		bundle.putDouble(Constants.BUNDLE_START_LNG, mStartLng);
		bundle.putString(Constants.BUNDLE_START_ADDRESS, mStartAddress);
		bundle.putString(Constants.BUNDLE_LOAIXE_NAME, mLoaiXeName);
		bundle.putString(Constants.BUNDLE_THOI_GIAN, mThoiGianDonXe);
		bundle.putDouble(Constants.BUNDLE_END_LAT, mEndLat);
		bundle.putDouble(Constants.BUNDLE_END_LNG, mEndLng);
		bundle.putString(Constants.BUNDLE_END_ADDRESS, mEndAddress);
		bundle.putDouble(Constants.BUNDLE_KM, mKm);
		bundle.putDouble(Constants.BUNDLE_PHAN_TRAM_GIAM_GIA, mPhanTramGiamGia);
		bundle.putString(Constants.BUNDLE_GIA, mGia);
		bundle.putString(Constants.BUNDLE_MOTA, mMoTa);
		bundle.putInt(Constants.BUNDLE_NUMBER_DRIVER, mDriverNumber);
		bundle.putDouble(Constants.BUNDLE_KHACHHANG_LAT, mKhachHangLat);
		bundle.putDouble(Constants.BUNDLE_KHACHHANG_LNG, mKhachhangLng);
		bundle.putString(Constants.BUNDLE_KHUYEN_MAI, mMaKhuyenMai);
		bundle.putString(Constants.BUNDLE_DRIVERS_SORTED, jsonArray);
		bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC,mNhomCNDoiTacId);
		setResult(Constants.kEY_BACK_FORM_DATXE);
		if (mIsGoToMap) {
			bundle.putInt(Constants.KEY_GO_TO, Constants.KEY_GO_TO_MAP);
			gotoActivityForResult(mContext, TimTaiXeActivity.class, bundle,
					Constants.KEY_GO_TO_MAP, Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ourLeftInLeft();
		} else {
			gotoActivityForResult(mContext, TimTaiXeActivity.class, bundle,
					Constants.KEY_GO_TO_MAP, Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ourLeftInLeft();
		}
	}*/

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

	Handler handleUpdateUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if(!isFinishing())
					DialogUtils.showOkDialog(mActivity,
						R.string.title_no_driver_near_here, R.string.dong_y,
						onAcceptClick);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
    private void showOrHideFunction(boolean isShow)
    {
        if(isShow)
        {
            mLnThongTinTaiXe.startAnimation(mAnimSlideDown);
            mLnThongTinTaiXe.setVisibility(View.VISIBLE);
            mBtnTiepTuc.setVisibility(View.VISIBLE);
            mBtnShowLayout.setVisibility(View.GONE);
            toottip();

        }else
        {
            if (mLnThongTinTaiXe.getVisibility() == View.VISIBLE) {
                mLnThongTinTaiXe.startAnimation(mAnimSlideUp);
            }
            mLnThongTinTaiXe.setVisibility(View.GONE
            );
            mBtnTiepTuc.setVisibility(View.GONE);
            mRlToolTipBottomCenter.setVisibility(View.GONE);
            mBtnShowLayout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStartMoveScreen() {
    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}

	private void uocTinhChiPhiByLocation() {

		LatLng startLatLng = new LatLng(mStartLat, mStartLng);
		LatLng endLatLng = new LatLng(mEndLat, mEndLng);

		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
			mIsDatXe = false;
			Utils.setBackground(mBtnTiepTuc,
					Utils.getDrawable(mContext,R.drawable.btn_gray));
			Navigator navigator = new Navigator(startLatLng, endLatLng,
					new DistanceListener() {

						@Override
						public void loadData(ArrayList<String> arrDistance,
								String strReturn) {
							if (arrDistance.size() == 0) {
								mProgressToolbar.setVisibility(ProgressBar.GONE);
								return;
							}
							String distance = arrDistance.get(0);
							try {
								mStrDirection = strReturn;
								mKm = ParserUtils.getDoubleValue(
										new JSONObject(distance), "value");
								mKm = Utils.DoubleFomat(mKm / 1000);
								handlerUocTinhChiPhi.sendEmptyMessage(1);
								loadDirections();
							} catch (JSONException e) {
								mProgressToolbar.setVisibility(ProgressBar.GONE);
								e.printStackTrace();
							}
							Utils.Log(TAG, "distance" + distance);
						}
					});
			navigator.findDistance(mContext, false);
		} else {
			showAlertMangYeu(1);
		}
	}

	private void getUocTinhChiPhiByKmAndMaGiamGia(double km, int nhomXeId,
			String maGiamGia) {
		String url = WebServiceUtils.URL_GET_UOC_TINH_CHI_PHI_DAT_XE(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();

		try {
			Utils.Log(TAG, "km" + km);
			Utils.Log(TAG, "nhomXeId" + nhomXeId);
			Utils.Log(TAG, "maGiamGia" + maGiamGia);
			jsonParams.put("km", km);
			jsonParams.put("nhomXeId", nhomXeId);
			jsonParams.put("maGiamGia", maGiamGia);

			if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
				Pasgo.getInstance().addToRequestQueue(url, jsonParams,
						new PWListener() {

							@Override
							public void onResponse(JSONObject json) {
								if (json != null) {
									Utils.setBackground(
											mBtnTiepTuc,
											Utils.getDrawable(mContext,
													R.drawable.btn_all));
									mIsDatXe = true;
									JSONObject jsonObject = ParserUtils
											.getJsonObject(json, "Item");
									mPhanTramGiamGia = ParserUtils
											.getDoubleValue(jsonObject,
													"PhanTramGiamGia");
									mGia = ParserUtils.getStringValue(
											jsonObject, "Gia");
									Utils.Log(TAG, "json " + json);
									Utils.Log(TAG, "mPhanTramGiamGia "
											+ mPhanTramGiamGia);
									Utils.Log(TAG, "mGia " + mGia);
									setText();
									mProgressToolbar.setVisibility(ProgressBar.GONE);
								}
							}

							@Override
							public void onError(int maloi) {
								Utils.setBackground(mBtnTiepTuc, getResources()
										.getDrawable(R.drawable.btn_gray));
								mIsDatXe = false;
								mProgressToolbar.setVisibility(ProgressBar.GONE);
							}

						}, new PWErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								ToastUtils
										.showToast(
												mContext,
												R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
								showAlertMangYeu(2);
								Utils.setBackground(mBtnTiepTuc, getResources()
										.getDrawable(R.drawable.btn_gray));
								mIsDatXe = false;
								mProgressToolbar.setVisibility(ProgressBar.GONE);
							}
						});
			} else {
				showAlertMangYeu(2);
				Utils.setBackground(mBtnTiepTuc,
						Utils.getDrawable(mContext, R.drawable.btn_gray));
				mIsDatXe = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.showToast(mContext,
					R.string.tb_khong_the_ket_noi_voi_voi_may_chu);
			Utils.setBackground(mBtnTiepTuc,
					Utils.getDrawable(mContext, R.drawable.btn_gray));
			mIsDatXe = false;
		}
	}

	Handler handlerUocTinhChiPhi = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				uocTinhChiPhiByLocation();
				break;
			case 1:
				mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
				mIsDatXe = false;
				Utils.setBackground(mBtnTiepTuc,
						Utils.getDrawable(mContext,R.drawable.btn_gray));
				getUocTinhChiPhiByKmAndMaGiamGia(mKm, mNhomXeDichVuId, mMaKhuyenMai);
				break;
			default:
				break;
			}
		};
	};

	private void showAlertMangYeu(final int i) {
        if (getBaseContext() == null)
            return;
		final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_mang_yeu);
		Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);
		Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
		dialogBtThuLai.setOnClickListener(v -> {
            switch (i) {
            case 1:
                handlerUocTinhChiPhi.sendEmptyMessage(0);
                break;
            case 2:
                handlerUocTinhChiPhi.sendEmptyMessage(1);
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
		if (!dialog.isShowing()&& !isFinishing()) {
			dialog.show();
		}
	}
	// call hang
	private void loadTongDaiHang() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				getTongDaiHang();
				return null;
			}
		}.execute();
	}

	private void getTongDaiHang() {
		String url = WebServiceUtils.URL_GET_TONG_DAI_HANG(Pasgo
				.getInstance().token);
		Utils.Log(TAG, "url dat xe " + url);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("datXeId", mDatXeId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new PWListener() {

					@Override
					public void onResponse(JSONObject json) {
						if (json != null) {
							
							Utils.Log(TAG, "json " + json);
							ArrayList<HangXe> hangXes = ParserUtils.getHangXes(json);
							if (hangXes.size() == 0)
								insertTrangThaiHang(mDatXeId, 1);
							else {
								insertTrangThaiHang(mDatXeId,2);
								callHangXe(hangXes);
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
	private RequestHangXeOrderObject mRequestHangXeOrderObject;
	
	private void callHangXe(ArrayList<HangXe> hangXes) {
		String khachHangId = Pasgo.getInstance().userId;
		String tenKhachHang = Pasgo.getInstance().username;
		int nhomXeId = mNhomXeDichVuId;
		String maGiamGia = mMaKhuyenMai;
		String tiemGiamGia = mPhanTramGiamGia + "";
		String diaChiDonXe = mStartAddress;
		double viDoDonXe =mStartLat;
		double kinhDoDonXe=mStartLng;
		String diaChiDen = mEndAddress;
		double viDoDen = mEndLat;
		double kinhDoDen = mEndLng;
		String mota = mMoTa;
		String sdt = Pasgo.getInstance().sdt;
		if (maGiamGia == null) {
			maGiamGia = "";
		}
		if(StringUtils.isEmpty(mDatXeId))
			mDatXeId="";
		mRequestHangXeOrderObject = new RequestHangXeOrderObject(mDatXeId,
				khachHangId, tenKhachHang, nhomXeId, sdt, maGiamGia,
				tiemGiamGia, diaChiDonXe, viDoDonXe, kinhDoDonXe,
				mThoiGianDonXe, diaChiDen, viDoDen, kinhDoDen, mota, mKm, mGia,
				hangXes,true,0);
		try {
			Gson gson = new Gson();
			gson = new Gson();
			String json = gson.toJson(mRequestHangXeOrderObject);
			JSONObject jsonObject = new JSONObject(json);
			pubChannelCallHangXe(jsonObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pubChannelCallHangXe(JSONObject jsonObject) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("roomname", Constants.APP_PUBNUB+ Constants.VERSION_HANGXE_ORDER_PUBNUB);
			obj.put("message", jsonObject);
			Pasgo.getSocket().emit(Constants.PG_MESSAGE,obj);
		} catch (Exception e) {
		}
	}
	private void pubChannelCallHangXeDatNgay(JSONObject jsonObject) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("roomname", Constants.APP_PUBNUB + Constants.VERSION_HANGXE_PUBNUB_BOOKING + mDatXeId);
			obj.put("message", jsonObject);
			Pasgo.getSocket().emit(Constants.PG_MESSAGE,obj);
		} catch (Exception e) {
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

}