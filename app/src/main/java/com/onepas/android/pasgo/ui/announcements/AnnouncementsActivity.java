package com.onepas.android.pasgo.ui.announcements;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ItemTinKhuyenMai;
import com.onepas.android.pasgo.models.TinKhuyenMaiDoiTac;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.search.DatabaseHandler;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnnouncementsActivity extends BaseAppCompatActivity {
	private ArrayList<ItemTinKhuyenMai> mListKM = new ArrayList<ItemTinKhuyenMai>();
	private LinearLayout mLayoutLoading;
	private RelativeLayout mLayoutKoDuLieu;
	private ListView mListView;
	private DatabaseHandler mDatabaseHandler;
	private TextView mTvNoData;
	private String mTinKhuyenMaiId;
	private String mPushNotificationId;
	private int mKeyPush;
	private boolean mIsFromPushNotification;
	private boolean mIsPause=false;
	private int mNumberResume=0;
	private LinearLayout mLnDisconnect;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_announcement);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.tin_khuyen_mai));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> backActivity());
		this.initView();
		// kiểm tra trường hợp đi từ Push Notification
		Bundle extra =getIntent().getExtras();
		if (extra != null) {
			mTinKhuyenMaiId = extra.getString(Constants.BUNDLE_KEY_TIN_KHUYEN_MAI_ID);
			mPushNotificationId = extra.getString(Constants.BUNDLE_KEY_THONG_BAO_NOTIFICATION_ID);
			mKeyPush = extra.getInt(Constants.KEY_TYPE_PUSH_NOTIFICATION);
			mIsFromPushNotification = extra.getBoolean(Constants.BUNDLE_KEY_GO_TO_PUSH_NOTIFICATION_ACTIVITY, false);
		}
		this.initControl();
		this.onNetworkChanged();
		if(mIsFromPushNotification)
		{
			boolean tableExists = mDatabaseHandler
					.existsTable();
			if (tableExists) {
				mDatabaseHandler.insertItemReadTinKM(mTinKhuyenMaiId, 1);
			}
			if(mKeyPush == 1 || mKeyPush == 4 || mKeyPush == 5)
			{
				Bundle mBundle = new Bundle();
				mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_ID, mTinKhuyenMaiId);
				mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, getString(R.string.tin_khuyen_mai));
				gotoActivity(mContext,WebviewActivity.class,mBundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ourLeftInLeft();
			}else
			if(mKeyPush == 2)
			{
				getContentKM(mTinKhuyenMaiId);
			}else
			if(mKeyPush == 3)
			{
				Bundle mBundle = new Bundle();
				mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_ID, mTinKhuyenMaiId);
				mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, getString(R.string.tin_khuyen_mai));
				gotoActivity(mContext,AnnouncementsDetail3Activity.class,mBundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ourLeftInLeft();
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event != null && keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			backActivity();
		}
		return super.onKeyDown(keyCode, event);
	}
	private void backActivity()
	{
		if(Utils.checkStartApp(mActivity))
		{
			gotoActivityClearTop(mActivity, HomeActivity.class);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finishToRightToLeft();
				}
			}, Constants.KEY_BACK_ACTIVITY_DELAY);
		}else
			finishToRightToLeft();
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mDatabaseHandler = new DatabaseHandler(mActivity);
		mLayoutKoDuLieu = (RelativeLayout) findViewById(R.id.lyKhongCoThongBao);
		mLayoutLoading = (LinearLayout) findViewById(R.id.lyLoading);
		mListView = (ListView)findViewById(R.id.fragment_detail);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mTvNoData = (TextView)findViewById(R.id.tvNoData);
		mTvNoData.setMovementMethod(LinkMovementMethod.getInstance());
		mLnDisconnect = (LinearLayout)findViewById(R.id.lnDisconnect);
		findViewById(R.id.btnTryAGain).setOnClickListener(v->{
			handleUpdateUI.sendEmptyMessage(1);
		});
	}

	@Override
	protected void initControl() {
		// TODO Auto-generated method stub
		super.initControl();
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mNumberResume ++;
		if(mNumberResume>1 && mIsFromPushNotification)
		{
			handleUpdateUI.sendEmptyMessage(1);
			mIsFromPushNotification = false;
		}
	}

	public void onNetworkChanged() {
		if (getBaseContext() == null
				|| mLnErrorConnectNetwork == null)
			return;
		if (NetworkUtils.getInstance(getBaseContext())
				.isNetworkAvailable()) {
			if (mListKM.size() == 0)
				getListKhuyenMai();
			mLnErrorConnectNetwork.setVisibility(View.GONE);
		}
		else
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
	}

	protected void setListAdapter(ArrayList<ItemTinKhuyenMai> listKM) {
		mListKM = listKM;
		final AnnouncementsAdapter adapter = new AnnouncementsAdapter(mActivity,
				listKM, R.layout.item_tin_khuyenmai);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Constants.Back_Fg = 0;

            ItemTinKhuyenMai item = mListKM.get(position);
            int readMes = item.getRead();
            if (readMes == 0) {
                mDatabaseHandler.insertItemReadTinKM(item.getId(), 1);
            }
            mListKM.get(position).setRead(1);
            adapter.notifyDataSetChanged();
            mListView.invalidateViews();
            mListView.refreshDrawableState();
            switch (item.getLoaiTinKhuyenMai())
            {
                case 1:
                    Bundle mBundle = new Bundle();
                    mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_ID, item.getId());
                    mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, item.getTieuDe());
                    gotoActivity(mContext,WebviewActivity.class,mBundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ourLeftInLeft();
                    break;
                case 2:
                    getContentKM(item.getId());
                    break;
                case 3:
                    Bundle bundle3 = new Bundle();
                    bundle3.putString(Constants.BUNDLE_KEY_TIN_KM_ID, item.getId());
                    bundle3.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, item.getTieuDe());
                    gotoActivity(mContext,AnnouncementsDetail3Activity.class,bundle3,Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ourLeftInLeft();
                    break;
                case 4:
                    Bundle bundle4 = new Bundle();
                    bundle4.putInt(Constants.KEY_TYPE_PUSH_NOTIFICATION, Constants.KEY_ACTIVITY_THE_PASGO);
                    Constants.ThePasgoTabNumber =0;
                    gotoActivity(mContext,ThePasgoActivity.class,bundle4,Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ourLeftInLeft();
                    break;
                case 5:
                    Bundle bundle5 = new Bundle();
                    bundle5.putInt(Constants.KEY_TYPE_PUSH_NOTIFICATION, Constants.KEY_ACTIVITY_THE_PASGO);
                    Constants.ThePasgoTabNumber =1;
                    gotoActivity(mContext,ThePasgoActivity.class,bundle5,Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ourLeftInLeft();
                    break;
            }
        });
	}

	private void getContentKM(String iD) {
		Double lat=0.0,lng=0.0;
		if (Pasgo.getInstance().prefs != null
				&& Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
			lat = Double.parseDouble(Pasgo.getInstance().prefs
					.getLatLocationRecent());
			lng = Double.parseDouble(Pasgo.getInstance().prefs
					.getLngLocationRecent());
		}
		DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
		String url = WebServiceUtils.URL_GET_CHI_TIET_DOI_TAC_KM(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("viDo", lat);
			jsonParams.put("kinhDo", lng);
			jsonParams.put("khuyenMaiId", iD);
			jsonParams.put("deviceId", factory.getDeviceUuid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		showProgressDialogAllNoCancel();
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						closeProgressDialogAll();
						ArrayList<TinKhuyenMaiDoiTac> tinKhuyenMaiDoiTacs = ParserUtils.getTinKhuyenMaiDoiTacs(response);
						if (tinKhuyenMaiDoiTacs.size() > 0) {
							lvClickItem(tinKhuyenMaiDoiTacs.get(0));
						}
					}

					@Override
					public void onError(int maloi) {
						closeProgressDialogAll();
					}
				}, error -> closeProgressDialogAll());

	}
	protected void lvClickItem(TinKhuyenMaiDoiTac item) {
		if (item != null) {
			String doiTacKMID = item.getDoiTacKhuyenMaiId();
			String title = item.getTen();
			String chiNhanhDoiTacId = item.getId();
			String nhomCnDoiTac = item.getNhomCnDoiTacId();
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.KEY_DI_XE_FREE, Constants.KEY_INT_XE_FREE);
			bundle.putInt(Constants.KEY_TEN_TINH_ID, 1);
			bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
			bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, doiTacKMID);
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
			bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
			bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, item.getNhomKhuyenMaiId());
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getDiaChi());
			bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCnDoiTac);
			bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
			bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
			bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
			gotoActivityForResult(mActivity, DetailActivity.class, bundle,
					Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ourLeftInLeft();
		}
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
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onStartMoveScreen() {

	}

	@Override
	public void onUpdateMapAfterUserInterection() {
		// TODO Auto-generated method stub

	}

	private void getListKhuyenMai() {
		setLayoutView(0);
		String url = WebServiceUtils
				.URL_Tin_Khuyen_Mai(Pasgo.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
		try {
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("deviceId", factory.getDeviceUuid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						ArrayList<ItemTinKhuyenMai> mListKM = new ArrayList<>();
						try {
							JSONArray array = ParserUtils.getJsonArray(
									response, "Items");
							if (array.length() > 0) {
								for (int i = 0; i < array.length(); i++) {
									JSONObject jsonObject = array
											.getJSONObject(i);
									int read = 0;
									String ngayBatDau =ParserUtils.getStringValue(jsonObject,"NgayBatDau");
									String tieuDe =ParserUtils.getStringValue(jsonObject,"TieuDe");
									String id =ParserUtils.getStringValue(jsonObject,"Id");
									String moTa =ParserUtils.getStringValue(jsonObject,"MoTa");
									int loaiTinKhuyenMai = ParserUtils.getIntValue(jsonObject, "LoaiTinKhuyenMai");
									String anh =ParserUtils.getStringValue(jsonObject, "Anh");
									boolean tableExists = mDatabaseHandler
											.existsTable();
									if (tableExists) {
										read = mDatabaseHandler
												.getReadTinKM(id);
									}
									ItemTinKhuyenMai item = new ItemTinKhuyenMai();
									item.setId(id);
									item.setTieuDe(tieuDe);
									item.setNgayBatDau(ngayBatDau);
									item.setMoTa(moTa);
									item.setLoaiTinKhuyenMai(loaiTinKhuyenMai);
									item.setRead(read);
									item.setAnh(anh);
									mListKM.add(item);
								}

								if (mListKM.size() > 0) {
									setLayoutView(2);
									setListAdapter(mListKM);
								} else {
									setLayoutView(1);
								}
							} else {
								setLayoutView(1);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(int maloi) {
						handleUpdateUI.sendEmptyMessage(0);
					}

				}, error -> handleUpdateUI.sendEmptyMessage(0));
	}

	Handler handleUpdateUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					mLnDisconnect.setVisibility(View.VISIBLE);
					break;
				case 1:
					mLnDisconnect.setVisibility(View.GONE);
					getListKhuyenMai();
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	private void setLayoutView(int i) {
		mLayoutKoDuLieu.setVisibility(View.GONE);
		mLayoutLoading.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		switch (i) {
			case 0:
				mLayoutLoading.setVisibility(View.VISIBLE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);

				break;
			case 1:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);

				break;
			case 2:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);

				break;

			default:
				break;
		}
	}
}