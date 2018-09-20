package com.onepas.android.pasgo.ui.reserved;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.listener.HidingScrollListener;
import com.onepas.android.pasgo.models.CheckedIn;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.chat.ChatActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.partner.DirectionActivity;
import com.onepas.android.pasgo.ui.partner.WebviewChiTietActivity;
import com.onepas.android.pasgo.ui.reserve.ReserveDetailActivity;
import com.onepas.android.pasgo.ui.reserved.Holder.ReservedHistoryAdapter;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReservedHistoryFragment extends BaseFragment {
	private View mRoot;
	private LinearLayout mLnErrorConnectNetwork;
	private LinearLayout mLayoutKoDuLieu;
	private LinearLayout mLayoutLoading;
	private RelativeLayout mRlData;
	private static final int KEY_LOADING =0;
	private static final int KEY_NO_DATA =1;
	private static final int KEY_DATA =2;
	private int mPageSize = 20;
	private int mPageNumber = 1;
	protected boolean mFlagLoading;
	private RelativeLayout mFooterView;
	private ArrayList<CheckedIn> mList = new ArrayList<CheckedIn>();
	private int mPositionClick=0;
	//recyclerView
	private RecyclerView mLvData;
	private ReservedHistoryAdapter mAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private int mfirstVisibleItem=0;
	private static final int PERMISSION_REQUEST_CODE_PHONE =1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		initControl();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_reserved, container, false);
		mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.da_check_in));
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft(getActivity()));
		mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
		mLayoutKoDuLieu = (LinearLayout) mRoot.findViewById(R.id.lyKhongCoDuLieu);
		mLayoutLoading = (LinearLayout) mRoot.findViewById(R.id.lyLoading);
		mRlData = (RelativeLayout) mRoot.findViewById(R.id.lnData);

		mLvData = (RecyclerView)mRoot. findViewById(R.id.recyclerView);
		mLinearLayoutManager = new LinearLayoutManager(getActivity());
		mToolbar.getParent().bringChildToFront(mToolbar);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mFooterView = (RelativeLayout) mRoot.findViewById(R.id.load_more_footer);
		if (mRoot == null)
			return null;
		mLvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				int visibleItemCount = recyclerView.getChildCount();
				int totalItemCount = mLinearLayoutManager.getItemCount();
				mfirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
				//Utils.Log(Pasgo.TAG,"firstVisibleItem1_"+mfirstVisibleItem);
				if (!mFlagLoading) {
					int totalItemCountTruHeader = totalItemCount - 1;
					if (mfirstVisibleItem + visibleItemCount == totalItemCount
							&& totalItemCountTruHeader > mPageSize * (mPageNumber - 1) && totalItemCountTruHeader % mPageSize == 0) {
						mFooterView.setVisibility(View.VISIBLE);
						mFlagLoading = true;
						mPageNumber += 1;
						getLichSuCheckIn();
					}
				}
			}
		});
		if (mList.size() == 0)
		{
			handlerUpdateUI.sendEmptyMessage(0);
		}
		else
			setListAdapter(mList);

		return mRoot;
	}
	protected android.os.Handler handlerUpdateUI = new android.os.Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					mPageNumber = 1;
					getLichSuCheckIn();
					break;
				case 1:
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

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
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void initControl() {
		super.initControl();
	}

	private void getLichSuCheckIn() {
		double viDo = 0, kinhDo = 0;
		if (Pasgo.getInstance().prefs != null
				&& Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
			viDo = Double.parseDouble(Pasgo.getInstance().prefs
					.getLatLocationRecent());
			kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
					.getLngLocationRecent());
		}

		String url = WebServiceUtils.URL_LICH_SU_DAT_CHO(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		if (Pasgo.getInstance().userId == null)
			Pasgo.getInstance().userId = "";
		try {
			if (StringUtils.isEmpty(Pasgo.getInstance().userId))
				Pasgo.getInstance().userId = "";
			jsonParams.put("viDo", viDo);
			jsonParams.put("kinhDo", kinhDo);
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("pageNumber", mPageNumber);
			jsonParams.put("pageSize", mPageSize);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(mPageNumber==1)
			setLayoutView(KEY_LOADING);
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {

						mFlagLoading = false;
						List<CheckedIn> listCheckin = ParserUtils
								.getCheckedInLists(response);
						if (mPageNumber == 1) mList.clear();
						mList.addAll(listCheckin);
						setListAdapter(mList);
					}

					@Override
					public void onError(int maloi) {
						mFooterView.setVisibility(View.GONE);
					}

				}, new Pasgo.PWErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (getActivity().isFinishing()) return;
						mFooterView.setVisibility(View.GONE);
					}
				});

	}
	private void setListAdapter(ArrayList<CheckedIn> listItemAdressFree) {
		int sizeList = listItemAdressFree.size();
		if (sizeList > 0) {
			setLayoutView(KEY_DATA);
			mAdapter = new ReservedHistoryAdapter(getActivity(), listItemAdressFree,
					new ReservedHistoryAdapter.CheckInListListener() {

						@Override
						public void share(int position) {
							mPositionClick = position;
							CheckedIn item = mList.get(position);
							if(item!=null)
								sheareKhuyenMai(item);
						}

						@Override
						public void checkIn(int position) {
							mPositionClick = position;
							CheckedIn item = mList.get(position);
							if(item!=null && item.getLoaiHopDong()==1)
								reserve(item);
						}

						@Override
						public void detail(int position) {
							mPositionClick = position;
							CheckedIn item = mList.get(position);
							if(item!=null)
								lvClickItem(item);
						}

						@Override
						public void callSuport(int position) {
							mPositionClick = position;
							checkPemissionCall();
						}

						@Override
						public void direction(int position) {
							mPositionClick = position;
							CheckedIn item = mList.get(position);
							if(item!=null)
								gotoDirection(item.getKinhDo(),item.getViDo(), item.getMaNhomKhuyenMai());
						}

						@Override
						public void deals(int position) {
							mPositionClick = position;
							CheckedIn item = mList.get(position);
							if(item!=null)
								gotoDeals(item);
						}
					});
			mLvData.setAdapter(mAdapter);
			mLvData.setLayoutManager(new LinearLayoutManager(getActivity()));
			mLvData.setLayoutManager(mLinearLayoutManager);
			mLvData.setAdapter(mAdapter);
			if(mPageNumber>1)
				mLvData.getLayoutManager().scrollToPosition(mfirstVisibleItem);
			mLvData.addOnScrollListener(new HidingScrollListener() {
				@Override
				public void onHide() {
					hideViews();
				}

				@Override
				public void onShow() {
					showViews();
				}
			});
		} else {
			setLayoutView(KEY_NO_DATA);
		}
	}
	private void gotoDirection(double kinhDo, double viDo, String maNhomKhuyenMai)
	{
		Bundle bundle1 = new Bundle();
		bundle1.putDouble(Constants.BUNDLE_KEY_KINH_DO, kinhDo);
		bundle1.putDouble(Constants.BUNDLE_KEY_VI_DO, viDo);
		bundle1.putString(Constants.BUNDLE_KEY_MA_NHOM_KHUYEN_MAI, maNhomKhuyenMai);
		gotoActivity(mActivity, DirectionActivity.class, bundle1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ourLeftInLeft(mActivity);
	}
	private void gotoDeals(CheckedIn item)
	{
		Bundle bundleUuDai = new Bundle();
		bundleUuDai.putInt(Constants.KEY_TYPE_WEBVIEW, WebviewChiTietActivity.UU_DAI);
		bundleUuDai.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, item.getDoiTacKhuyenMaiId());//mChiTietDoiTac.getId()
		gotoActivity(mActivity, WebviewChiTietActivity.class, bundleUuDai,Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ourLeftInLeft(mActivity);
	}
	private void callClick() {
		String bookingTitle = getString(R.string.edit_info_content);
		final Dialog dialog;
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_reserved_popup_call);
		TextView tvContentConfirm  = (TextView) dialog
				.findViewById(R.id.tvContentConfirm);
		TextView tvTitle = (TextView) dialog
				.findViewById(R.id.tvTitle);
		tvContentConfirm.setText(getString(R.string.edit_infomation_pop));
		Utils.setTextViewHtml(tvTitle,bookingTitle);
		dialog.setCancelable(true);
		TextView btnHuy;
		LinearLayout lnCall1;
		lnCall1 = (LinearLayout) dialog.findViewById(R.id.lncall1);
		lnCall1.setOnClickListener(v -> {
            dialog.dismiss();
            gotoPhoneCallPage(Constants.SDT_TONG_DAI);
        });
		dialog.findViewById(R.id.btnChat).setOnClickListener(v-> {
			chatNow();
			dialog.dismiss();
		});
		dialog.findViewById(R.id.lnChatNow).setOnClickListener(v-> {
			chatNow();
			dialog.dismiss();
		});
		btnHuy = (TextView) dialog.findViewById(R.id.btnHuy);
		btnHuy.setOnClickListener(v -> dialog.dismiss());
		if (!getActivity().isFinishing())
			dialog.show();
	}
	private void chatNow()
	{
		CheckedIn item = mList.get(mPositionClick);
		String sdt = Pasgo.getInstance().sdt;
		String diemDen = item.getTen();
		String reservedInfo = String.format(getString(R.string.reserved_infomation),sdt,diemDen,item.getSoNguoiDen(),item.getSoTreEm(),item.getThoiGianDen(),item.getDiaChi(),item.getThoiGianDen());
		reservedInfo = StringUtils.formatStringNewLine(reservedInfo);
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.BUNDLE_KEY_GO_TO_FROM_RESERVED_HISTORY,true);
		bundle.putString(Constants.BUNDLE_KEY_TEXT_FROM_RESERVED_HISTORY,reservedInfo);
		gotoActivity(getActivity(),ChatActivity.class,bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);

	}
	protected void gotoPhoneCallPage(String telNumber) {
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
		}
		startActivity(i);
	}
	private void hideViews() {
		mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
		//FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
		//int fabBottomMargin = lp.bottomMargin;
		//mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
	}

	private void showViews() {
		mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
		//mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
	}
	private void lvClickItem(CheckedIn item) {

		if (item != null) {
			String doiTacKMID = item.getDoiTacKhuyenMaiId();
			String title = item.getTenChiNhanh();
			String chiNhanhDoiTacId = item.getDoiTacId();
			String nhomCnDoiTac = item.getNhomCNDoiTacId();
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.KEY_TEN_TINH_ID, 1);
			bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
			bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,
					doiTacKMID);
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
			bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
			bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, item.getNhomKhuyenMaiId());
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getTenChiNhanh());
			bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCnDoiTac);
			bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
			bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
			bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
			Intent intent = new Intent(getActivity(), DetailActivity.class);
			intent.putExtras(bundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, Constants.MENU_TO_LIST_C1);
		}
	}
	private void sheareKhuyenMai(CheckedIn item) {
		String strShare=String.format("%s %s\n%s: %s\n%s: %s \n%s %s \n%s: %s"
				,getString(R.string.sdt)
				,item.getSoDienThoaiFormat()+"/"+item.getTenKhachHang()
				, getString(R.string.diem_tai_tro), StringUtils.fromHtml(item.getTenChiNhanh())
				, getString(R.string.so_nguoi_den), item.getSoNguoiDen()
				, getString(R.string.thoi_gian_den), item.getThoiGianDen()
				, getString(R.string.dia_chi), item.getDiaChi()
		);
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		share.putExtra(Intent.EXTRA_SUBJECT, "");
		share.putExtra(Intent.EXTRA_TEXT, strShare);
		startActivity(Intent.createChooser(share,
				getResources().getString(R.string.title_share_pastaxi)));

	}
	private void reserve(CheckedIn item)
	{
		Bundle bundle =new Bundle();
		bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, item.getNhomCNDoiTacId());
		bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM,item.getTen());
		bundle.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, item.getTrangThai());
		bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaChi());
		gotoActivityForResult(mActivity, ReserveDetailActivity.class, bundle, Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ourLeftInLeft(mActivity);
	}
	private void setLayoutView(int i) {
		mLayoutKoDuLieu.setVisibility(View.GONE);
		mLayoutLoading.setVisibility(View.GONE);
		mRlData.setVisibility(View.GONE);
		switch (i) {
			case KEY_LOADING:
				mLayoutLoading.setVisibility(View.VISIBLE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mRlData.setVisibility(View.GONE);
				break;
			case KEY_NO_DATA:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.VISIBLE);
				mRlData.setVisibility(View.GONE);
				break;
			case KEY_DATA:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mRlData.setVisibility(View.VISIBLE);
				mFooterView.setVisibility(View.GONE);
				mFlagLoading = false;
				break;
			default:
				break;
		}
	}

	//region permission Call
	protected void checkPemissionCall()
	{
		if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE)
				== PackageManager.PERMISSION_GRANTED) {
			callClick();
		} else {
			requestPermission(Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_CODE_PHONE, mActivity.getApplicationContext(), mActivity);
		}
	}
	public static void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

		if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
			// bật lại thông báo lần 2 sau khi tắt lần đầu
			ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
		} else {
			ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE_PHONE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					callClick();
				} else {
					ToastUtils.showToast(mActivity, "Permission was denied");
				}
				return;
			}
		}
	}
	//endregion
}