package com.onepas.android.pasgo.ui.partner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.FilterCategoryItems;
import com.onepas.android.pasgo.models.FilterParent;
import com.onepas.android.pasgo.models.FilterView;
import com.onepas.android.pasgo.models.ItemAdressFree;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DestinationActivity extends BaseAppCompatActivity {
	private LinearLayout mLayoutKoDuLieu;
	private ListView mLvAddressFree;
	private List<ItemAdressFree> mListItemAdressFree = new ArrayList<ItemAdressFree>();
	private List<ItemAdressFree> mListSearch = new ArrayList<ItemAdressFree>();
	private String mTenDoiTac;
	protected boolean mFlagLoading;
	private RelativeLayout mFooterView;
	private int mTinhID=0;
	private int mDiXeFree;
	private int mNhomKhuyenMaiId;
	private int mPageSize = 15;
	private int mPageNumber = 1;
	private String mKeySearch = "";
	private EditText mEditsearch;
	private boolean mIsSearch = false;
	private double viDo = 0;
	private double kinhDo = 0;
	private boolean mIsHideMenu = false;
	private TextView mKoCoDL;
	private Menu menu;
	private Dialog mDialog;
	private String mMaKhuyenMaiAll;
	private boolean mIsDiemTaiTroNguoiDung = false;
	private boolean mIsDiemTaiTroNguoiDungTuDo = false;
	private DestinationFilterAdapter mDiaDiemFilterAdapter;
	private Dialog mDialogFilter;
	// danh sach filter lay tren server ve
	private ArrayList<FilterParent> mListFilters;
	// danh sach hien thi len popup
	private ArrayList<FilterView> mFilterViews =new ArrayList<FilterView>();
	// Clone: để hiện thị view, khi nào nhấn "OK" thì mFilterViews= mFilterViewsClone
	private ArrayList<FilterView> mFilterViewsClone =new ArrayList<FilterView>();
	// kiểm tra nếu là Filter
	private boolean mIsFilter=false;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.menu_search_dia_diem, menu);
		if (mIsHideMenu) {
			// menu.getItem(0).setVisible(false);
		}
		mEditsearch = (EditText) menu.findItem(R.id.menu_search).getActionView();
		mEditsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if(s.length()>0) {
					mIsSearch = true;
					String textSearch = s.toString();
					searchItem(textSearch);
				}
				else
				{
					mIsSearch = false;
					setListAdapter(mListItemAdressFree);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		MenuItem menuSearch = menu.findItem(R.id.menu_search);

		menuSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			// Menu Action Collapse
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Empty EditText to remove text filtering
				mKeySearch = "";
				mListSearch.clear();
				setListAdapter(mListItemAdressFree);
				mEditsearch.setText("");
				mEditsearch.clearFocus();
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				return true;
			}

			// Menu Action Expand
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				mEditsearch.requestFocus();
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
		});
		return true;
	}

	private void searchItem(String textSearch) {
		textSearch = textSearch.trim();
		mPageNumber = 1;
		if(mIsSearch)
			getDoiTac(mNhomKhuyenMaiId, mTinhID, mPageNumber, textSearch);
	}

	private void hideOption(int id) {
		if (menu != null) {
			MenuItem item = menu.findItem(R.id.menu_search);
			item.setVisible(false);
		}
	}

	private void showOption(int id) {
		if (menu != null) {
			MenuItem item = menu.findItem(R.id.menu_search);
			item.setVisible(true);
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mEditsearch.isFocused()) {
				View v = (View) findViewById(mEditsearch.getId());
				Rect outRect = new Rect();
				mEditsearch.getGlobalVisibleRect(outRect);
				if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
					mEditsearch.clearFocus();
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		}
		return super.dispatchTouchEvent(event);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_address_free);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		Bundle extra = getIntent().getExtras();
		if(extra ==null)
			extra = savedInstanceState;
		if (extra != null) {
			mTinhID = extra.getInt(Constants.BUNDLE_TINH_ID);
			mDiXeFree = extra.getInt(Constants.KEY_DI_XE_FREE);
			mNhomKhuyenMaiId = extra.getInt(Constants.BUNDLE_DOI_TAC_ID);
			mTenDoiTac = extra.getString(Constants.BUNDLE_DOI_TAC_NAME);
			mMaKhuyenMaiAll = extra
					.getString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
			mIsDiemTaiTroNguoiDung = extra.getBoolean(
					Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
			mIsDiemTaiTroNguoiDungTuDo = extra.getBoolean(
					Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
			Utils.setTextViewHtml(toolbarTitle,mTenDoiTac);

		}
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
		mLvAddressFree = (ListView) findViewById(R.id.list_adress_free);
		mLayoutKoDuLieu = (LinearLayout) findViewById(R.id.lyKhongCoThongBao);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mFooterView = (RelativeLayout) findViewById(R.id.load_more_footer);
		mKoCoDL = (TextView) findViewById(R.id.tvKhongCoDuLieu);
		mProgressToolbar.setVisibility(ProgressBar.GONE);
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			getFilterCategory(mNhomKhuyenMaiId);
			mLnErrorConnectNetwork.setVisibility(View.GONE);
		} else {
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
		}

		mLvAddressFree.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (NetworkUtils.getInstance(getBaseContext())
						.isNetworkAvailable()) {
					lvClickItem(position);
				}
			}
		});
		mLvAddressFree.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if (mIsSearch == false) {
					if (firstVisibleItem + visibleItemCount == totalItemCount
							&& totalItemCount > mPageSize * (mPageNumber - 1) && totalItemCount % mPageSize == 0) {
						if (mFlagLoading == false) {
							mFooterView.setVisibility(View.VISIBLE);
							mFlagLoading = true;
							mPageNumber += 1;
							getDoiTac(mNhomKhuyenMaiId, mTinhID, mPageNumber,
									mKeySearch);
						}
					}
				}
			}
		});
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
	protected void onSaveInstanceState(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(bundle);
		bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, mIsDiemTaiTroNguoiDung);
		bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, mIsDiemTaiTroNguoiDungTuDo);
		bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, mMaKhuyenMaiAll);
		bundle.putInt(Constants.KEY_DI_XE_FREE, mDiXeFree);
		bundle.putInt(Constants.BUNDLE_DOI_TAC_ID, mNhomKhuyenMaiId);
		bundle.putString(Constants.BUNDLE_DOI_TAC_NAME, mTenDoiTac);
		bundle.putInt(Constants.BUNDLE_TINH_ID, mTinhID);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finishToRightToLeft();
				return true;
			case R.id.menu_filter:
				showDialogFilter();
				break;
		}

		return true;
	}

	private void lvClickItem(int position) {

		ItemAdressFree item = null;
		if (mIsSearch) {
			if (mListSearch.size() > 0) {
				item = mListSearch.get(position);
			}
		} else {
			if (mListItemAdressFree.size() > 0) {
				item = mListItemAdressFree.get(position);
			}
		}
		if (item != null) {
			String doiTacKMID = item.getDoiTacKhuyenMaiId();
			String title = item.getTenDoiTac();
			String chiNhanhDoiTacId = item.getId();
			String nhomCnDoiTac = item.getNhomCNDoiTacId();
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.KEY_DI_XE_FREE, mDiXeFree);
			bundle.putInt(Constants.KEY_TEN_TINH_ID, mTinhID);
			bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
			bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,
					doiTacKMID);
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
			bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
			bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, mNhomKhuyenMaiId);
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, mTenDoiTac);
			bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCnDoiTac);
			bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, mMaKhuyenMaiAll);
			bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, mIsDiemTaiTroNguoiDung);
			bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, mIsDiemTaiTroNguoiDungTuDo);
			gotoActivityForResult(mContext, DetailActivity.class, bundle,
					Constants.MENU_TO_LIST_C1,Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ourLeftInLeft();
		}
	}

	private void setListAdapter(List<ItemAdressFree> listItemAdressFree) {
		int sizeList = listItemAdressFree.size();
		if (sizeList > 0) {
			mLayoutKoDuLieu.setVisibility(View.GONE);
			mLvAddressFree.setVisibility(View.VISIBLE);
			mFlagLoading = false;
			NhomKhuyenMaiAdapter adapter = new NhomKhuyenMaiAdapter(mContext,
					R.layout.item_di_xe_mien_phi, listItemAdressFree);

			int position = mLvAddressFree.getLastVisiblePosition();
			mLvAddressFree.setAdapter(adapter);
			if(mPageNumber>1)
				mLvAddressFree.setSelectionFromTop(position, 0);
		} else {
			mLayoutKoDuLieu.setVisibility(View.VISIBLE);
			mLvAddressFree.setVisibility(View.GONE);
			mFooterView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.MENU_TO_LIST_C1) {
			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			int complete = 0;
			if (bundle != null) {
				complete = bundle.getInt(Constants.KEY_COMPLETE);
			}
			if (complete == Constants.KEY_INT_COMPLETE) {
				Intent intent = null;
				intent = new Intent(mActivity, HomeActivity.class);//ReserveActivity
				Bundle bundle1 = new Bundle();
				bundle1.putInt(Constants.KEY_COMPLETE,
						Constants.KEY_INT_COMPLETE);
				intent.putExtras(bundle1);
				setResult(Constants.MENU_TO_LIST_C1, intent);

				finishToRightToLeft();
			} else {
				finishToRightToLeft();
			}
		}
	}

	private synchronized void getDoiTac(int nhomKhuyenMaiId, int tinhID, int pageNumber,
										String txtSearch) {
		Gson gson = new Gson();
		String jsonFilter =(mListFilters==null||mListFilters.size()==0||mIsSearch||!mIsFilter)?"": "{\"Items\":"+gson.toJson(mListFilters)+"}";
		Utils.Log(Pasgo.TAG, "gson" + jsonFilter);
		if (Pasgo.getInstance().prefs != null
				&& Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
			viDo = Double.parseDouble(Pasgo.getInstance().prefs
					.getLatLocationRecent());
			kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
					.getLngLocationRecent());
		}
		String url = WebServiceUtils
				.URL_GET_DOI_TAC(Pasgo.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		if (Pasgo.getInstance().userId == null)
			Pasgo.getInstance().userId = "";
		try {
			jsonParams.put("viDo", viDo);
			jsonParams.put("kinhDo", kinhDo);
			jsonParams.put("nhomKhuyenMaiId", nhomKhuyenMaiId);
			jsonParams.put("tinhId", tinhID);
			jsonParams.put("keySearch", txtSearch);
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("pageNumber", pageNumber);
			jsonParams.put("pageSize", mPageSize);
			jsonParams.put("filter", jsonFilter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			if(mPageNumber==1)
				mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							mListSearch.clear();
							List<ItemAdressFree> mListParser = ParserUtils.getAllAdressFrees(response);
							if (mIsSearch) {
								mListSearch = mListParser;
							} else {
								if(mIsFilter && mPageNumber==1)
									mListItemAdressFree.clear();
								mListItemAdressFree.addAll(mListParser);
							}
							if (mIsSearch) {
								if (mListSearch.size() > 0) {
									mProgressToolbar.setVisibility(ProgressBar.GONE);
									setListAdapter(mListSearch);
									mFooterView.setVisibility(View.GONE);
								} else {
									showKoDL();
								}
							} else {
								if (mListItemAdressFree.size() > 0) {
									mProgressToolbar.setVisibility(ProgressBar.GONE);
									setListAdapter(mListItemAdressFree);
									mFooterView.setVisibility(View.GONE);
								} else {
									hideOption(R.menu.menu_search);
									showKoDL();
								}
							}
							mFooterView.setVisibility(View.GONE);
							mLnErrorConnectNetwork.setVisibility(View.GONE);
							mProgressToolbar.setVisibility(ProgressBar.GONE);
						}

						@Override
						public void onError(int maloi) {
						}

					}, new PWErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);
							handlerNetwork.sendEmptyMessage(0);
						}
					});
		} else {
			showKoDL();
		}
	}

	Handler handlerNetwork = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					if (isFinishing() || getBaseContext() == null)
						return;
					showAlertMangYeu();
					break;

				default:
					break;
			}
		};
	};

	private void showAlertMangYeu() {

		if (!isFinishing() && !mIsDestroy) {
			if (mDialog == null) {
				mDialog = new Dialog(DestinationActivity.this);
				mDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
				mDialog.setContentView(R.layout.alert_mang_yeu);
				mDialog.setCancelable(false);
			}
			Button dialogBtThuLai = (Button) mDialog.findViewById(R.id.btThulai);// ket_noi_mang_yeu_and_connecttoserver
			Button dialogBtHuy = (Button) mDialog.findViewById(R.id.btHuy);
			TextView tv = (TextView) mDialog.findViewById(R.id.content);
			tv.setText(getString(R.string.ket_noi_mang_yeu_and_connecttoserver));

			dialogBtThuLai.setOnClickListener(v -> {
                getDoiTac(mNhomKhuyenMaiId, mTinhID, mPageNumber, mKeySearch);
                mDialog.dismiss();
            });

			dialogBtHuy.setOnClickListener(v -> {
                mDialog.dismiss();
				finishToRightToLeft();
            });

			if (!mDialog.isShowing() && !isFinishing()) {
				mDialog.show();
			}
		}
	}

	private void showKoDL() {

		mFooterView.setVisibility(View.GONE);
		mLayoutKoDuLieu.setVisibility(View.VISIBLE);
		mLvAddressFree.setVisibility(View.GONE);
		if(mIsFilter)
		{
			if (mIsSearch && mIsHideMenu == false) {
				mKoCoDL.setText("" + getString(R.string.chua_co_diem_nao));
			} else {
				mKoCoDL.setText("" + getString(R.string.filter_no_data));
			}
		}else {
			if (mIsSearch && mIsHideMenu == false) {
				mKoCoDL.setText("" + getString(R.string.chua_co_diem_nao));
			} else {
				mKoCoDL.setText("" + getString(R.string.khong_co_kmv1));
			}
		}
		mProgressToolbar.setVisibility(ProgressBar.GONE);
	}
	// region Filter

	private void getFilterCategory(int nhomKhuyenMaiId) {
		String url = WebServiceUtils
				.URL_GET_FILTER_CATEGORY(Pasgo.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nhomKhuyenMaiId", nhomKhuyenMaiId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							mListFilters = ParserUtils.getAllFilters(response);
							mFilterViews.clear();
							for (FilterParent filterParent: mListFilters)
							{
								String parentName= filterParent.getNameEn();
								if(Pasgo.getInstance().prefs.getLanguage()==Constants.LANGUAGE_VIETNAM)
								{
									parentName= filterParent.getNameVn();
								}
								mFilterViews.add(new FilterView(-1, filterParent.getId(), parentName, false, true, filterParent.getChoiceType()));
								for (FilterCategoryItems filterCategoryItems: filterParent.getFilterCategoryItems())
								{
									String name= filterCategoryItems.getNameEn();
									if(Pasgo.getInstance().prefs.getLanguage()==Constants.LANGUAGE_VIETNAM)
									{
										name= filterCategoryItems.getNameVn();
									}
									mFilterViews.add(new FilterView(filterCategoryItems.getId(),filterParent.getId(),name,filterCategoryItems.isSelected(),false,filterParent.getChoiceType()));
								}
							}
							Gson gson = new Gson();
							String json ="{\"Items\":"+gson.toJson(mListFilters)+"}";
							Utils.Log(Pasgo.TAG, "gson" + json);
							getDoiTac(mNhomKhuyenMaiId, mTinhID, mPageNumber, mKeySearch);
						}

						@Override
						public void onError(int maloi) {
						}

					}, new PWErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
						}
					});
		}
	}
	public ArrayList<FilterView> cloneList(ArrayList<FilterView> cList) {
		ArrayList<FilterView> clonedList = new ArrayList<FilterView>(cList.size());
		for (FilterView dog : cList) {
			clonedList.add(new FilterView(dog.getId(),dog.getParentId(),dog.getName(),dog.isCheck(),dog.isParent(),dog.getChoiceType()));
		}
		return clonedList;
	}
	private void showDialogFilter() {
		if (isFinishing() || getBaseContext() == null || mIsSearch || mFilterViews.size()==0)
			return;
		mFilterViewsClone = cloneList(mFilterViews);
		LayoutInflater inflater = getLayoutInflater();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
		alertDialog.setIcon(R.drawable.app_pastaxi);
		View dialog = inflater.inflate(R.layout.popup_dia_diem_filter, null);
		alertDialog.setView(dialog)
				.setPositiveButton(R.string.dong_y, (dialog1, id) -> {
                    mFilterViews = mFilterViewsClone;
                    // lưu lại danh sách check theo id của filterParent + category
                    HashMap<String,Boolean> hashMaplist =new HashMap<String, Boolean>();
                    if (mFilterViews.size() > 0) {
                        for (int i=0;i< mFilterViews.size();i++)
                        {
                            hashMaplist.put(mFilterViews.get(i).getParentId()+""+mFilterViews.get(i).getId(),mFilterViews.get(i).isCheck());
                        }
                    }
                    // kiểm tra lại danh sách filtet lúc đầu, set lại giá trị selected
                    for (FilterParent filterParent: mListFilters)
                    {
                        for (FilterCategoryItems filterCategoryItems: filterParent.getFilterCategoryItems())
                        {
                            String key = filterParent.getId()+""+filterCategoryItems.getId();
                            if (hashMaplist.containsKey(key)) {
                                filterCategoryItems.setSelected(hashMaplist.get(key));
                            }
                        }
                    }
                    mPageNumber = 1;
                    mIsFilter =true;
                    getDoiTac(mNhomKhuyenMaiId, mTinhID, mPageNumber, mKeySearch);
                })
				.setNegativeButton(R.string.huy,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				});
		// set view
		final ListView lvFilter =(ListView)dialog.findViewById(R.id.lvFilter);
		mDiaDiemFilterAdapter =new DestinationFilterAdapter(mContext, mFilterViewsClone,new DestinationFilterAdapter.DiaDiemFilterListenner() {
			@Override
			public void check(int position, boolean isCheck) {
				int choiceType =mFilterViewsClone.get(position).getChoiceType();
				int parentId =mFilterViewsClone.get(position).getParentId();
				if(choiceType==1&& isCheck)// ==1 là chỉ được chọn 1
				{
					for(FilterView item: mFilterViewsClone)
					{
						if(item.getParentId() ==parentId)
							item.setCheck(false);
					}
				}
				mFilterViewsClone.get(position).setCheck(isCheck);
				mDiaDiemFilterAdapter.notifyDataSetChanged();
				lvFilter.invalidateViews();
				lvFilter.refreshDrawableState();
			}
		},false);
		lvFilter.setAdapter(mDiaDiemFilterAdapter);
		// create dialog and show
		if(mDialogFilter==null)
			mDialogFilter = alertDialog.create();
		if(mDialogFilter!=null && !mDialogFilter.isShowing()) {
			mDialogFilter = alertDialog.create();
			mDialogFilter.show();
		}
	}
	// endregion
	@Override
	public void onNetworkChanged() {

		if (mLnErrorConnectNetwork != null) {
			if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
				mLnErrorConnectNetwork.setVisibility(View.GONE);
			} else {
				mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
				mFooterView.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onStartMoveScreen() {

	}
	@Override
	public void onUpdateMapAfterUserInterection() {
	}
}