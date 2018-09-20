package com.onepas.android.pasgo.ui.pasgocard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.onepas.android.pasgo.models.MaDatCho;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.WebviewActivity;
import com.onepas.android.pasgo.ui.account.LoginAgainActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatChoFragment extends BaseFragment implements View.OnClickListener {
	private View mRoot;
	private final String TAG = "DatChoFragment";
	private LinearLayout mLayoutKoDuLieu;
	private ListView mListView;
	private ArrayList<MaDatCho> mLists = new ArrayList<MaDatCho>();
	protected boolean flag_loading;
	private RelativeLayout mFooterView;
	private int mPageSize = 10;
	private int mPageNumber = 1;

	private LinearLayout mLnDisconnect;
	private final int  KEY_DATA=1;
	private final int  KEY_NO_DATA=2;
	private final int  KEY_DISCONNECT =3;
	private Button mBtnTryAGain;


	private LinearLayout mLayoutLoading;
	private LinearLayout mLnThePasGoIntroToLayout;
	// de khi quay lai lan 2: thi khong load lai neu co du lieu
	private boolean mIsLoadFromOnCreate = false;
	// mDoiTacKhuyenmaiId !=null thì là click từ ThePasgo của đối tác ra
	private String mDoiTacKhuyenMaiId="";
	private LinearLayout mLnTrial;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mIsLoadFromOnCreate = true;
		Bundle bundle =getActivity().getIntent().getExtras();
		if(bundle!=null)
			mDoiTacKhuyenMaiId = bundle.getString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,"");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.page_ma_dat_cho, container, false);
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
		mBtnTryAGain.setOnClickListener(v -> giamGiaDatChoDiemDen());
		mFooterView = (RelativeLayout) mRoot.findViewById(R.id.load_more_footer);
		mLnTrial = (LinearLayout) mRoot.findViewById(R.id.lnTrial);

		mLayoutLoading = (LinearLayout) mRoot.findViewById(R.id.lyLoading);
		mLnThePasGoIntroToLayout = (LinearLayout) mRoot.findViewById(R.id.lnThePasGoIntro);
		mRoot.findViewById(R.id.login_btn).setOnClickListener(this);
		mRoot.findViewById(R.id.register_btn).setOnClickListener(this);

		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.layout_the_pasgo_intro, mListView, false);
		mListView.addHeaderView(header, null, false);
		mLnThePasGoIntroToLayout.setVisibility(View.VISIBLE);
		// add text for intro
		TextView introHeader = (TextView)header.findViewById(R.id.the_pasgo_intro_2_tv);
		Utils.setTextViewHtml(introHeader,getString(R.string.the_pasgo_intro2));
		TextView introLayout = (TextView)mRoot.findViewById(R.id.the_pasgo_intro_2_tv);
		Utils.setTextViewHtml(introLayout,getString(R.string.the_pasgo_intro2));

		TextView viewDetailHeader = (TextView) header.findViewById(R.id.the_pasgo_intro_3_tv);
		Utils.setTextViewHtml(viewDetailHeader,getString(R.string.the_pasgo_intro3));
		TextView viewDetailLayout = (TextView) mRoot.findViewById(R.id.the_pasgo_intro_3_tv);
		Utils.setTextViewHtml(viewDetailLayout,getString(R.string.the_pasgo_intro3));

		Button btnChiSeToLayout, btnChiaSeToListview;
		btnChiSeToLayout = (Button)mRoot.findViewById(R.id.chia_se_ngay_btn);
		btnChiaSeToListview = (Button)header.findViewById(R.id.chia_se_ngay_btn);
		btnChiSeToLayout.setOnClickListener(v -> sheareLinkPasgo());
		btnChiaSeToListview.setOnClickListener(v -> sheareLinkPasgo());

		viewDetailHeader.setOnClickListener(v -> viewDetail());
		viewDetailLayout.setOnClickListener(v -> viewDetail());
		initControl();
		return mRoot;
	}

	private void viewDetail()
	{
		String language= Pasgo.getInstance().prefs.getLanguage();
		String url;
		if(language.toLowerCase().equals(Constants.LANGUAGE_VIETNAM))
		{
			url= Constants.KEY_THE_PASGO_SHARE_VN;
		}else
		{
			url= Constants.KEY_THE_PASGO_SHARE_EN;
		}
		Bundle bundle =new Bundle();
		bundle.putString(Constants.BUNDLE_KEY_LINK, url);
		bundle.putString(Constants.BUNDLE_KEY_ACTIONBAR_NAME, getString(R.string.menu_tai_tro));
		gotoActivity(mActivity, WebviewActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ourLeftInLeft(mActivity);
	}
	private void sheareLinkPasgo() {
		String urlLink = "http://pasgo.vn/invite/"+ Pasgo.getInstance().ma;
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		share.putExtra(Intent.EXTRA_SUBJECT, "");
		share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_sms_content) + "\n"
				+ urlLink);
		startActivity(Intent.createChooser(share,
				getResources().getString(R.string.title_share_pastaxi)));
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

		if (!isCheckLogin())
			return;
		if(mLists.size() >0 && !mIsLoadFromOnCreate)
			setListAdapter(mLists);
		else
			giamGiaDatChoDiemDen();
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
					// tru di 1 la vi listview da addHeader
					int totalItemCountTruHeader = totalItemCount - 1;
					if (firstVisibleItem + visibleItemCount == totalItemCount
							&& totalItemCountTruHeader > mPageSize * (mPageNumber - 1) && totalItemCountTruHeader % mPageSize == 0) {
						mFooterView.setVisibility(View.VISIBLE);
						flag_loading = true;
							mPageNumber += 1;
							giamGiaDatChoDiemDen();

					}
				}

			}
		});
		TextView intro2 = (TextView) mRoot.findViewById(R.id.the_pasgo_intro_2_tv);
		Utils.setTextViewHtml(intro2,getString(R.string.the_pasgo_intro2));
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
		MaDatCho item = null;
		ArrayList<MaDatCho> mListsClick =mLists;
		if (mListsClick.size() > 0) {
			item = mListsClick.get(position);
			if(item!=null)
			{
				useCode(item);
			}
		}
	}

	private void setListAdapter(final ArrayList<MaDatCho> lists) {
		int sizeList = lists.size();
		if (sizeList > 0) {
			setView(KEY_DATA);
			flag_loading = false;
			DatChoAdapter adapter = new DatChoAdapter(mActivity,
					lists, new DatChoAdapter.maDatChoListener() {
				@Override
				public void suDung(int position) {
					MaDatCho item = lists
							.get(position);
					if (item != null) {
						useCode(item);
					}
				}
			});
			mListView.setAdapter(adapter);
			mListView.setSelectionFromTop(lists.size() - mPageSize, 0);
		} else {
			setView(KEY_NO_DATA);
			mFooterView.setVisibility(View.GONE);
		}
	}

	private void useCode(MaDatCho item) {
		Bundle bundle = new Bundle();
		bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, item.getId());
		bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, item.getMaTaiTro());
		bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, item.getGiamGia());
		bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,mDoiTacKhuyenMaiId);
		gotoActivityForResult(mActivity, DatChoDiemDenActivity.class, bundle,
				Constants.KEY_BACK_BY_MA_DAT_CHO,
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ourLeftInLeft(mActivity);
	}

	private void giamGiaDatChoDiemDen() {

		String url = WebServiceUtils.URL_GIAM_GIA_DAT_CHO_DIEM_DEN(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		final int pageNumber =mPageNumber;
		if (Pasgo.getInstance().userId == null)
			Pasgo.getInstance().userId = "";
		try {
			jsonParams.put("nguoiDungId",
					Pasgo.getInstance().prefs.getUserId());
			jsonParams.put("pageNumber",pageNumber);
			jsonParams.put("pageSize", mPageSize);
			jsonParams.put("doiTacKhuyenMaiId",mDoiTacKhuyenMaiId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
			if(pageNumber ==1) {
				mLists.clear();
				mLayoutLoading.setVisibility(View.VISIBLE);
			}
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new Pasgo.PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							ArrayList<MaDatCho> lists = ParserUtils
									.getMaDatChos(response);
							mIsLoadFromOnCreate = false;
							if(pageNumber==1)
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
				mLnThePasGoIntroToLayout.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mLnDisconnect.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mLayoutLoading.setVisibility(View.GONE);
				break;
			}
			case KEY_NO_DATA:
			{
				mLnThePasGoIntroToLayout.setVisibility(View.VISIBLE);
				mFooterView.setVisibility(View.GONE);
				mLayoutKoDuLieu.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				mLayoutLoading.setVisibility(View.GONE);
				break;
			}
			case KEY_DISCONNECT:
			{
				mLnThePasGoIntroToLayout.setVisibility(View.VISIBLE);
				mLayoutKoDuLieu.setVisibility(View.GONE);
				mLnDisconnect.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				break;
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.login_btn:
				gotoActivity(mActivity, LoginAgainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ourLeftInLeft(mActivity);
				break;
			case R.id.register_btn:
				Bundle bundle =new Bundle();
				bundle.putBoolean(Constants.BUNDLE_TRIAL_REGISTER,true);
				gotoActivity(mActivity, LoginAgainActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ourLeftInLeft(mActivity);
				break;
		}
	}

	private final BroadcastReceiver broadcastReceiverLoadData = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(StringUtils.isEmpty(mDoiTacKhuyenMaiId)) {
				mPageNumber = 1;
				giamGiaDatChoDiemDen();
			}else
			{
				// load lai du lieu neu dat cho theo Doi tac km
				Intent resultIntent = new Intent();
				getActivity().setResult(Activity.RESULT_OK, resultIntent);
				finishToRightToLeft(mActivity);
			}
		}
	};
	@Override
	public void onStart() {
		super.onStart();
		IntentFilter intentLoginFilter = new IntentFilter(
				Constants.BROADCAST_ACTION_LOAD_DATA_RESERVE);
		getActivity().registerReceiver(broadcastReceiverLoadData, intentLoginFilter);
	}

	private void backActivity()
	{
		try {
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
		}catch (Exception e) {
			finishToRightToLeft(mActivity);
		}

	}
	public boolean isRunning(Context ctx) {
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

		for (ActivityManager.RunningTaskInfo task : tasks) {
			if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
				return true;
		}

		return false;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			getActivity().unregisterReceiver(broadcastReceiverLoadData);
		}catch (Exception e){}

	}
}