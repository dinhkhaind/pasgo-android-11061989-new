package com.onepas.android.pasgo.ui.announcements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ItemTinKhuyenMai;
import com.onepas.android.pasgo.models.TinKhuyenMaiDoiTac;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.reserve.ReserveDetailActivity;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnnouncementsDetail3Activity extends BaseAppCompatActivity {
	private ArrayList<ItemTinKhuyenMai> mListKM = new ArrayList<ItemTinKhuyenMai>();
	private LinearLayout mLayoutLoading;
	private LinearLayout mLayoutKoDuLieu;
	private RecyclerView mLvData;
	private String mTieuDe;
	private String mId;
	private String mNoiDung;
	private LinearLayout mLnNoiDung;
	private ArrayList<TinKhuyenMaiDoiTac> mTinKhuyenMaiDoiTacs=new ArrayList<>();
	private AnnounCementsDetail3Adapter mAdapter;
	private StaggeredGridLayoutManager mLinearLayoutManager;
	private boolean mIsClickCheckIn = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_announcement_detail3);
		Bundle bundle=getIntent().getExtras();
		if(bundle==null)
			bundle = savedInstanceState;
		if(bundle!=null)
		{
			mTieuDe = bundle.getString(Constants.BUNDLE_KEY_TIN_KM_TITLE,"");
			mId = bundle.getString(Constants.BUNDLE_KEY_TIN_KM_ID,"");
		}
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(mTieuDe);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());

		this.initView();
		this.initControl();
		this.onNetworkChanged();
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
		// TODO Auto-generated method stub
		super.initView();
		mLayoutKoDuLieu = (LinearLayout) findViewById(R.id.lyKhongCoThongBao);
		mLayoutLoading = (LinearLayout) findViewById(R.id.lyLoading);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLvData = (RecyclerView) findViewById(R.id.listData);
		mLvData.setNestedScrollingEnabled(false);
		mLnNoiDung =(LinearLayout) findViewById(R.id.noi_dung_ln);

	}

	@Override
	protected void initControl() {
		// TODO Auto-generated method stub
		super.initControl();
		getContentKM(mId);
	}

	public void onNetworkChanged() {
		if (getBaseContext() == null
				|| mLnErrorConnectNetwork == null)
			return;
		if (NetworkUtils.getInstance(getBaseContext())
				.isNetworkAvailable()) {
			if (mListKM.size() == 0)
				getContentKM(mId);
			mLnErrorConnectNetwork.setVisibility(View.GONE);
		}
		else
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
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
		outState.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, mTieuDe);
		outState.putString(Constants.BUNDLE_KEY_TIN_KM_ID, mId);
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
		setLayoutView(0);
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						setLayoutView(2);
						JSONObject jsonObject = ParserUtils
                                .getJsonObject(response, "Item");
						mTinKhuyenMaiDoiTacs = ParserUtils.getTinKhuyenMaiDoiTacs(response);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								setListAdapter(mTinKhuyenMaiDoiTacs);
							}
						},800);
						setWebViewNoiDung(ParserUtils.getStringValue(jsonObject,"NoiDung"));

					}

					@Override
					public void onError(int maloi) {
						setLayoutView(1);
					}
				}, new Pasgo.PWErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						setLayoutView(1);
					}
				});

	}
	private void setListAdapter(ArrayList<TinKhuyenMaiDoiTac> tinKhuyenMaiDoiTacs) {
		int sizeList = tinKhuyenMaiDoiTacs.size();
		if (sizeList > 0 && tinKhuyenMaiDoiTacs.size()>0) {
			setLayoutView(2);
			mAdapter = new AnnounCementsDetail3Adapter(mActivity, tinKhuyenMaiDoiTacs,
					new AnnounCementsDetail3Adapter.AnnounCementsDetail3Listener() {
						@Override
						public void checkIn(int position) {
							// TODO Auto-generated method stub
							TinKhuyenMaiDoiTac item = null;
							if(mTinKhuyenMaiDoiTacs.size()>=position)
							{
								item = mTinKhuyenMaiDoiTacs.get(position);
								if(item.getLoaiHopDong()>1) return;
								initCheckIn(item);
							}
						}

						@Override
						public void detail(int position) {
							TinKhuyenMaiDoiTac item = null;
							if(mTinKhuyenMaiDoiTacs.size()>=position)
							{
								item = mTinKhuyenMaiDoiTacs.get(position);
							}
							if(item!=null) {
								lvClickItem(item);
							}
						}
					});
			mLvData.setAdapter(mAdapter);
			mLinearLayoutManager = new StaggeredGridLayoutManager(sizeList, StaggeredGridLayoutManager.HORIZONTAL);
			mLvData.setLayoutManager(mLinearLayoutManager);
			mLvData.setAdapter(mAdapter);
		} else {
			setLayoutView(1);
		}
	}

	public synchronized void initCheckIn(TinKhuyenMaiDoiTac item)
	{
		if(!mIsClickCheckIn)
		{
			mIsClickCheckIn = true;
			Bundle bundle =new Bundle();
			bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID,item.getNhomCnDoiTacId());
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, item.getTen());
			bundle.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, item.getTrangThai());
			bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaChi());
			gotoActivityForResult(mActivity, ReserveDetailActivity.class, bundle, Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ourLeftInLeft();
			mIsClickCheckIn = false;
		}
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
	private void setWebViewNoiDung(String noiDung) {
		mNoiDung = noiDung.replace("line-height","");
		mLnNoiDung.removeAllViews();
		mHandlerUpdateWebview.sendMessageDelayed(mHandlerUpdateWebview.obtainMessage(0), 100);
	}
	private Handler mHandlerUpdateWebview = new Handler(){
		public void handleMessage(Message msg){
			WebView webView =new WebView(mContext);
			webView.setBackgroundColor(Color.parseColor("#f6f6f6"));
			WebSettings webSettings = webView.getSettings();
			webSettings.setBuiltInZoomControls(false);
			webView.getSettings().setJavaScriptEnabled(true);
			webSettings.setDefaultFontSize(8);
			String customHtml="";
			switch (msg.what)
			{
				case 0:
					customHtml = formatHtml(mNoiDung.trim());
					webView.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8",
							null);
					webView.setWebViewClient(new WebViewClient() {

					});
					mLnNoiDung.addView(webView, 0);
					break;
			}


		}
	};
	private String formatHtml(String noiDung)
	{
		return "<html><head><style> "
				+ "table{text-align:left;text-align:right; font-size:16px; width: 100%;}"
				+ "</style></head> "
				+ "<body>"
				+ "<p>" + "<font face="
				+ "sans-serif" + " size= 6>" + noiDung + "</font>"
				+ "</body></html>";
	}
	private void setLayoutView(int i) {
		mLayoutKoDuLieu.setVisibility(View.GONE);
		mLayoutLoading.setVisibility(View.GONE);
		mLvData.setVisibility(View.GONE);
		switch (i) {
			case 0:
				mLayoutLoading.setVisibility(View.VISIBLE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mLvData.setVisibility(View.GONE);

				break;
			case 1:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.VISIBLE);
				mLvData.setVisibility(View.GONE);

				break;
			case 2:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mLvData.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
	}
}