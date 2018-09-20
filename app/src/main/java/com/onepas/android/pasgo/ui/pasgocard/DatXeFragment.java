package com.onepas.android.pasgo.ui.pasgocard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.MaDatXe;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.account.LoginAgainActivity;
import com.onepas.android.pasgo.ui.calldriver.DatXeActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatXeFragment extends BaseFragment implements View.OnClickListener {
	private View mRoot;
	private final String TAG = "DatXeFragment";
	private LinearLayout mLayoutKoDuLieu;
	private ListView mListView;
	private ArrayList<MaDatXe> mLists = new ArrayList<MaDatXe>();
	protected boolean flag_loading;
	private RelativeLayout mFooterView;
	private int mPageSize = 20;
	private int mPageNumber = 1;
	private LinearLayout mLnDisconnect;
	private final int  KEY_DATA=1;
	private final int  KEY_NO_DATA=2;
	private final int  KEY_DISCONNECT =3;
	private Button mBtnTryAGain;
	private LinearLayout mLayoutLoading;
	// de khi quay lai lan 2: thi khong load lai neu co du lieu
	private boolean mIsLoadFromOnCreate = false;
	private Toolbar mToolbar;
	private LinearLayout mLnTrial;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mIsLoadFromOnCreate = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.page_the_dat_xe, container, false);
		mToolbar = (Toolbar)mRoot. findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.menu_tai_tro));
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> backActivity());

		mLnDisconnect = (LinearLayout) mRoot.findViewById(R.id.lnDisconnect);
		mListView = (ListView) mRoot.findViewById(R.id.listData);
		mLayoutKoDuLieu = (LinearLayout) mRoot.findViewById(R.id.lyKhongCoThongBao);
		mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mBtnTryAGain = (Button) mRoot.findViewById(R.id.btnTryAGain);
		mLnTrial = (LinearLayout) mRoot.findViewById(R.id.lnTrial);
		mBtnTryAGain.setOnClickListener(v -> getDanhSachMaKhuyenMai());
		mFooterView = (RelativeLayout) mRoot.findViewById(R.id.load_more_footer);
		mLayoutLoading = (LinearLayout) mRoot.findViewById(R.id.lyLoading);
		mRoot.findViewById(R.id.login_btn).setOnClickListener(this);
		mRoot.findViewById(R.id.register_btn).setOnClickListener(this);
		initControl();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void initView() {
		super.initView();
		onNetworkChanged();
	}

	@Override
	public void onNetworkChanged() {
		if (mLnErrorConnectNetwork != null) {
			if (NetworkUtils.getInstance(getActivity()).isNetworkAvailable()) {
				mLnErrorConnectNetwork.setVisibility(View.GONE);
			} else {
				mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		initView();
		// đoạn này là sau khi dùng trial, đăng nhập xong quay lại
		if(mLnTrial.getVisibility() == View.VISIBLE && isCheckLogin())
			initControl();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void initControl() {
		super.initControl();
		if (!isCheckLogin() & !Pasgo.getInstance().prefs.getIsTrial())
			return;
		if(mLists.size() >0 && !mIsLoadFromOnCreate)
			setListAdapter(mLists);
		else {
			getDanhSachMaKhuyenMai();
		}
		if (NetworkUtils.getInstance(getActivity()).isNetworkAvailable()) {
			mLnErrorConnectNetwork.setVisibility(View.GONE);
		} else {
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
		}

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (NetworkUtils.getInstance(getActivity())
						.isNetworkAvailable()) {
					lvClickItem(position);
				}
			}
		});
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if (!flag_loading) {
					if (firstVisibleItem + visibleItemCount == totalItemCount
							&& totalItemCount > mPageSize * (mPageNumber - 1) && totalItemCount % mPageSize == 0) {
						mFooterView.setVisibility(View.VISIBLE);
						flag_loading = true;
						mPageNumber += 1;
						getDanhSachMaKhuyenMai();
					}
				}
			}
		});

	}
	private boolean isCheckLogin() {
		if (Pasgo.isLogged()) {
			mLnTrial.setVisibility(View.GONE);
			return true;
		}
		else {
			mLnTrial.setVisibility(View.VISIBLE);
			return false;
		}
	}

	private void lvClickItem(int position) {

		MaDatXe item = null;
		ArrayList<MaDatXe> mListsClick = mLists;
		if (mListsClick.size() > 0) {
			item = mListsClick.get(position);
		}

		if (item != null && item.getLoaiKhuyenMai() == 1) {
			if (!item.isHieuLuc()) {
				showDialogPromotionNotActive();
				return;
			}
			String maKhuyenMai = item.getTenMa();
			Bundle bundle = new Bundle();
			bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, maKhuyenMai);
			gotoActivityForResult(mActivity, DatXeDiemDenActivity.class,
					bundle, Constants.KEY_BACK_BY_MA_DAT_XE);
			ourLeftInLeft(mActivity);
		}
	}

	private void setListAdapter(final ArrayList<MaDatXe> listItemAdressFree) {
		int sizeList = listItemAdressFree.size();
		if (sizeList > 0) {
			setView(KEY_DATA);
			flag_loading = false;
			DatXeAdapter adapter = new DatXeAdapter(mActivity,
					listItemAdressFree, new DatXeAdapter.danhSachTaiTroListener() {

				@Override
				public void diemTaiTro(int position) {
					// TODO Auto-generated method stub
					MaDatXe item = listItemAdressFree
							.get(position);
					if (item != null) {
						if (!item.isHieuLuc()) {
							showDialogPromotionNotActive();
							return;
						}
						promotionPlace(item);
					}
				}

				@Override
				public void diNgay(int position) {
					// TODO Auto-generated method stub
					MaDatXe item = listItemAdressFree
							.get(position);
					if (item != null) {
						if (!item.isHieuLuc()) {
							showDialogPromotionNotActive();
							return;
						}
						goNow(item);
					}
				}
			});
			mListView.setAdapter(adapter);
			mListView.setSelectionFromTop(listItemAdressFree.size() - mPageSize, 0);
		} else {
			setView(KEY_NO_DATA);
			mFooterView.setVisibility(View.GONE);
		}
	}

	private void promotionPlace(MaDatXe item) {
		Bundle bundle = new Bundle();
		bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, item.getTenMa());
		bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, true);
		bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, true);
		/*gotoActivityForResult(mActivity, ReserveActivity.class, bundle,
				Constants.KEY_BACK_BY_MA_DAT_XE,
				Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
	}

	private void goNow(MaDatXe item) {
		Bundle bundle = new Bundle();
		bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, item.getTenMa());
		bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, true);
		bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, true);
		gotoActivityForResult(mActivity, DatXeActivity.class, bundle,
				Constants.KEY_BACK_BY_MA_DAT_XE,
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ourLeftInLeft(mActivity);
	}

	private void getDanhSachMaKhuyenMai() {
		if(!isCheckLogin()) return;
		String url = WebServiceUtils.URL_GET_DANH_SACH_MA_KHUYEN_MAI(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		final int pageNumber =mPageNumber;
		if (Pasgo.getInstance().userId == null)
			Pasgo.getInstance().userId = "";
		try {
			jsonParams.put("search", "");
			jsonParams.put("khachHangID",
					Pasgo.getInstance().prefs.getUserId());
			jsonParams.put("pageNumber",pageNumber);
			jsonParams.put("pageSize", mPageSize);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
			if(pageNumber ==1)
				mLayoutLoading.setVisibility(View.VISIBLE);
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new Pasgo.PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							flag_loading =false;
							mIsLoadFromOnCreate =false;
							ArrayList<MaDatXe> lists = ParserUtils
									.getDanhSachMaKhuyenMai(response);
							if(pageNumber ==1)
								mLists.clear();
							mLists.addAll(lists);
							setListAdapter(mLists);
							mFooterView.setVisibility(View.GONE);
							mLnErrorConnectNetwork.setVisibility(View.GONE);
							mLayoutLoading.setVisibility(View.GONE);
						}

						@Override
						public void onError(int maloi) {
						}

					}, new Pasgo.PWErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							try {
								if(getActivity().isFinishing()) return;
								mLayoutLoading.setVisibility(View.GONE);
								setView(KEY_DISCONNECT);
							}catch (Exception e)
							{}
						}
					});
		} else {
			setView(KEY_DISCONNECT);
		}
	}
	private void setView(final int i)
	{
		switch (i)
		{
			case KEY_DATA:
			{
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mLnDisconnect.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mLayoutLoading.setVisibility(View.GONE);
				mFooterView.setVisibility(View.GONE);
				break;
			}
			case KEY_NO_DATA:
			{
				mFooterView.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				mLayoutLoading.setVisibility(View.GONE);
				mFooterView.setVisibility(View.GONE);
				break;
			}
			case KEY_DISCONNECT:
			{
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mLnDisconnect.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				mFooterView.setVisibility(View.GONE);
				break;
			}
		}
	}

	private void showDialogPromotionNotActive() {
		if(!getActivity().isFinishing())
			DialogUtils.alert(mActivity, R.string.promotion_code_is_not_active);
	}
	private void backActivity()
	{
		ThePasgoActivity activity =(ThePasgoActivity)getActivity();
		if((activity.mNotificationCheck == Constants.KEY_ACTIVITY_THE_PASGO ) && Utils.checkStartApp(mActivity))
		{
			gotoActivityClearTop(mActivity, HomeActivity.class);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finishToRightToLeft(mActivity);
				}
			}, Constants.KEY_BACK_ACTIVITY_DELAY);
		}else
			finishToRightToLeft(mActivity);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.login_btn:
				gotoActivity(mActivity, LoginAgainActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ourLeftInLeft(mActivity);
				break;
			case R.id.register_btn:
				Bundle bundle =new Bundle();
				bundle.putBoolean(Constants.BUNDLE_TRIAL_REGISTER,true);
				gotoActivity(mActivity, LoginAgainActivity.class,bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ourLeftInLeft(mActivity);
				break;
			case R.id.search:

				break;
		}
	}
}