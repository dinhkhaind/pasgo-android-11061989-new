package com.onepas.android.pasgo.ui.pasgocard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
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
import com.onepas.android.pasgo.listener.HidingScrollListener;
import com.onepas.android.pasgo.models.FilterView;
import com.onepas.android.pasgo.models.NhomKhuyenMai;
import com.onepas.android.pasgo.models.Tinh;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.reserve.ReserveDetailActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatChoDiemDenActivity extends BaseAppCompatActivity implements
		OnClickListener {
    private static final String TAG="DatChoDiemDenActivity";
	private RecyclerView mListview;
    private RelativeLayout mRlFilter;
    private ImageView mImgFilter;
	private ArrayList<DatChoDiemDenModel> mList = new ArrayList<DatChoDiemDenModel>();
	protected boolean flag_loading;
	private RelativeLayout mFooterView;
	private int mPageSize = 30;
	private int mPageNumber = 1;
    private TextView mTvNoData;
	private Dialog dialog;
	private DatChoDiemDenAdapter mAdapter;
    private int mPosition=0;
    private String mNhomCNDoiTacId="";
    private String mTenDoiTac;
    private String mGiamGiaId="";
    private String mGiamGiaMa="";
    private String mGiamGia="";
    // filter
    private FilterTinhAdapter mTinhAdapter;
    private FilterNhomKhuyenMaiAdapter mNhomKhuyenMaiAdapter;
    private Dialog mDialogFilter;
    // danh sach filter lay tren server ve
    private ArrayList<Tinh> mListTinhs=new ArrayList<Tinh>();
    private ArrayList<NhomKhuyenMai> mNhomKhuyenMais=new ArrayList<NhomKhuyenMai>();

    //
    private double mLat=0.0;
    private double mLng=0.0;
    private int mTinhId=0;
    private String mNhomKhuyenMaiIds="";
    private String mDoiTacKhuyenMaiId="";
    private LinearLayoutManager mLinearLayoutManager;
    private int mfirstVisibleItem=0;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
    // kiểm tra đã click search chưa
    private boolean mIsSearched = false;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void actionLoadData() {
		mList.clear();
        mListview.setAdapter(null);
        mPageNumber =1;
		timKiemDiaDiemCheckin();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_dat_cho_diem_den);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.diem_den_tai_tro));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mProgressToolbar =(ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
		mListview = (RecyclerView) findViewById(R.id.lvData);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mFooterView = (RelativeLayout) findViewById(R.id.load_more_footer);
        mProgressToolbar.setVisibility(ProgressBar.GONE);
        mTvNoData = (TextView) findViewById(R.id.tvNoData);
        mRlFilter = (RelativeLayout)findViewById(R.id.filter_rl);
        mImgFilter = (ImageView)findViewById(R.id.filter_img);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRlFilter.setOnClickListener(this);

        Bundle bundle =getIntent().getExtras();
        if(savedInstanceState!=null)
            bundle = savedInstanceState;
        if(bundle!=null)
        {
            mGiamGiaId = bundle.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID);
            mGiamGiaMa = bundle.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA);
            mGiamGia = bundle.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA);
            mDoiTacKhuyenMaiId = bundle.getString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID);
        }
        mImgFilter.setBackgroundResource(R.drawable.ic_filter);

        if(StringUtils.isEmpty(mDoiTacKhuyenMaiId))
            mRlFilter.setVisibility(View.VISIBLE);
        else
            mRlFilter.setVisibility(View.GONE);
        handlerUpdateUI.sendEmptyMessage(0);
        mListview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                mfirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                //Utils.Log(Pasgo.TAG, "firstVisibleItem1_" + mfirstVisibleItem);
                if (!flag_loading && NetworkUtils.getInstance(mActivity).isNetworkAvailable()) {
                    int totalItemCountTruHeader = totalItemCount - 1;
                    if (mfirstVisibleItem + visibleItemCount == totalItemCount
                            && totalItemCountTruHeader > mPageSize * (mPageNumber - 1) && totalItemCountTruHeader % mPageSize == 0) {
                        mFooterView.setVisibility(View.VISIBLE);
                        flag_loading = true;
                        mPageNumber += 1;
                        timKiemDiaDiemCheckin();
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
        bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, mGiamGiaId);
        bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA,mGiamGiaMa);
        bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA,mGiamGia);
        bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, mDoiTacKhuyenMaiId);
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


	private void setListAdapter(ArrayList<DatChoDiemDenModel> listItemAdressFree) {
		int sizeList = listItemAdressFree.size();
		if (sizeList > 0) {
            mTvNoData.setVisibility(View.GONE);
			mListview.setVisibility(View.VISIBLE);
			flag_loading = false;
            //neu DoiTacKhuyenMaiId!=null thi cho doi tac do len dau tien
            DatChoDiemDenModel itemTam = null;
            for(DatChoDiemDenModel item:listItemAdressFree)
            {
                if(item.getDoiTacKhuyenMaiId().equals(mDoiTacKhuyenMaiId))
                {
                    itemTam = item;
                    listItemAdressFree.remove(item);
                    break;
                }
            }
            if(itemTam!=null)
            {
                listItemAdressFree.add(0,itemTam);
            }
			mAdapter = new DatChoDiemDenAdapter(mContext, mDoiTacKhuyenMaiId, listItemAdressFree,
                    new DatChoDiemDenAdapter.DatCHoDiemDenListener() {
                       	@Override
						public void checkIn(int position) {
							// TODO Auto-generated method stub
                            DatChoDiemDenModel item = null;
                            if(mList.size()>=position)
							{
                                item = mList.get(position);
                                mPosition =position;
                                mNhomCNDoiTacId=item.getNhomCNDoiTacId();
                                mTenDoiTac=item.getTenChiNhanh();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, mGiamGiaId);
                                bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, mGiamGiaMa);
                                bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, mGiamGia);
                                bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, mNhomCNDoiTacId);
                                bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, mList.get(mPosition).getTenChiNhanh());
                                bundle.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, mList.get(mPosition).getTrangThai());
                                bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaChi());
                                gotoActivityForResult(mActivity, ReserveDetailActivity.class, bundle, Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ourLeftInLeft();
                            }
						}

                        @Override
                        public void detail(int position) {
                            DatChoDiemDenModel item = null;
                            if(mList.size()>=position)
                            {
                                item = mList.get(position);
                            }
                            if(item!=null)
                                lvClickItem(item);
                        }
                    });
            mListview.setAdapter(mAdapter);
            mListview.setLayoutManager(new LinearLayoutManager(mActivity));
            mListview.setLayoutManager(mLinearLayoutManager);
            mListview.setAdapter(mAdapter);
            if(mPageNumber>1)
                mListview.getLayoutManager().scrollToPosition(mfirstVisibleItem);
            mListview.addOnScrollListener(new HidingScrollListener() {
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
			mListview.setVisibility(View.GONE);
			mFooterView.setVisibility(View.GONE);
		}
	}
    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        mLnErrorConnectNetwork.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        //mLnToolbar.setVisibility(View.GONE);
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mLnErrorConnectNetwork.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        //mLnToolbar.setVisibility(View.VISIBLE);
    }
    private void lvClickItem(DatChoDiemDenModel item) {

        if (item != null) {
            String doiTacKMID = item.getDoiTacKhuyenMaiId();
            String title = item.getTenChiNhanh();
            String chiNhanhDoiTacId = item.getId();
            String nhomCnDoiTac = item.getNhomCNDoiTacId();
            Bundle bundle = new Bundle();
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

            bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, mGiamGiaId);
            bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, mGiamGiaMa);
            bundle.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, mGiamGia);
            gotoActivityForResult(mContext, DetailActivity.class, bundle,
                    Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        }
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(Pasgo.TAG, "diemDen requestCode" + requestCode);
        Utils.Log(Pasgo.TAG, "diemDen resultCode" + resultCode);
        Utils.Log(Pasgo.TAG, "diemDen data" + data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //dat cho - diem den
                case Constants.KEY_BACK_BY_DAT_CHO:
                    Intent broadcast = new Intent();
                    broadcast.setAction(Constants.BROADCAST_ACTION_LOAD_DATA_RESERVE);
                    sendBroadcast(broadcast);
                    finishToRightToLeft();
                    break;
                default:
                    break;
            }
        }
	}
    Handler handlerUpdateUI = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
                        mLnErrorConnectNetwork.setVisibility(View.GONE);
                    } else {
                        mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
                    }
                    actionLoadData();
                    giamGiaDatChoGetTinh();
                    giamGiaDatChoGetNhomKhuyenMai(mTinhId);
                    break;
                default:
                    break;
            }
        };
    };

	Handler handlerNetwork = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				showAlertMangYeu();
				break;

			default:
				break;
			}
		};
	};

	private void showAlertMangYeu() {
        if (getBaseContext() == null)
            return;
		if (!isFinishing()) {
			if (dialog == null) {
				dialog = new Dialog(DatChoDiemDenActivity.this);
                dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_mang_yeu);
                dialog.setCancelable(false);
			}
			Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);
			Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
			TextView tv = (TextView) dialog.findViewById(R.id.content);
			tv.setText(getString(R.string.ket_noi_mang_yeu_and_connecttoserver));

			dialogBtThuLai.setOnClickListener(v -> {
                timKiemDiaDiemCheckin();
                dialog.dismiss();
            });

			dialogBtHuy.setOnClickListener(v -> {
                dialog.dismiss();
                finishToRightToLeft();
            });

			if (!isFinishing() && !dialog.isShowing()) {
				dialog.show();
			}
		}
	}

	private void showKoDL() {

		mFooterView.setVisibility(View.GONE);
		mListview.setVisibility(View.GONE);
        String textView =getString(R.string.no_data_ma_tai_tro_diem_den);
        mTvNoData.setVisibility(View.VISIBLE);
        mTvNoData.setText(textView);
        mProgressToolbar.setVisibility(ProgressBar.GONE);
	}

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
    private synchronized void timKiemDiaDiemCheckin() {
        Gson gson = new Gson();
        if(mPageNumber==1)
            if (Pasgo.getInstance().prefs != null
                    && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
                mLat = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLatLocationRecent());
                mLng = Double.parseDouble(Pasgo.getInstance().prefs
                        .getLngLocationRecent());
            }

        String url = WebServiceUtils.URL_GIAM_GIA_DAT_CHO_GET_DANH_SACH_DOI_TAC(Pasgo
                .getInstance().token);
        final int pageNumber =mPageNumber;
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            if (StringUtils.isEmpty(Pasgo.getInstance().userId))
                Pasgo.getInstance().userId = "";
            jsonParams.put("giamGiaId", mGiamGiaId);
            jsonParams.put("tinhId", mTinhId);
            jsonParams.put("nhomKhuyenMaiIds", mNhomKhuyenMaiIds);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("viDo", mLat);
            jsonParams.put("kinhDo", mLng);
            jsonParams.put("pageNumber", pageNumber);
            jsonParams.put("pageSize", mPageSize);
            jsonParams.put("doiTacKhuyenMaiId",mDoiTacKhuyenMaiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            if(pageNumber==1)
                mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new PWListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            flag_loading =false;
                            ArrayList<DatChoDiemDenModel> list = ParserUtils.getDatChoDiemDens(response);
                            if(pageNumber ==1)
                                mList.clear();
                            mList.addAll(list);
                            if (mList.size() == 0) {
                                mListview.setVisibility(View.GONE);
                            } else {
                                mListview.setVisibility(View.VISIBLE);
                            }
                            if (mList.size() > 0) {
                                mProgressToolbar.setVisibility(ProgressBar.GONE);
                                setListAdapter(mList);
                                mFooterView.setVisibility(View.GONE);
                            } else {
                                showKoDL();
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
    // region Filter
    private void giamGiaDatChoGetTinh() {
        String url = WebServiceUtils
                .URL_GIAM_GIA_DAT_CHO_GET_TINH(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("giamGiaId", mGiamGiaId);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mListTinhs = ParserUtils.getAllTinhGiaGiaFilters(mContext, response);
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

    private void giamGiaDatChoGetNhomKhuyenMai(int tinhId) {
        String url = WebServiceUtils
                .URL_GIAM_GIA_DAT_CHO_GET_NHOM_KHUYEN_MAI(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
            mLat = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            mLng = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        try {
            jsonParams.put("giamGiaId", mGiamGiaId);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("tinhId",tinhId);
            jsonParams.put("viDo",mLat);
            jsonParams.put("kinhDo",mLng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new PWListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mNhomKhuyenMais = ParserUtils.getNhomKhuyenMaiFilters(mContext, response);
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
    private void showDialogFilter() {
        if (isFinishing() || getBaseContext() == null  || flag_loading)
            return;
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setIcon(R.drawable.app_pastaxi);
        View dialog = inflater.inflate(R.layout.popup_dat_cho_diem_don_filter, null);
        alertDialog.setView(dialog)
                .setPositiveButton(R.string.dong_y, (dialog1, id) -> {
                    for (Tinh item : mListTinhs)
                        if (item.isCheck()) {
                            mTinhId = item.getId();
                            break;
                        }
                    mNhomKhuyenMaiIds = "";
                    for (NhomKhuyenMai item : mNhomKhuyenMais)
                        if (item.isCheck()) {
                            if (StringUtils.isEmpty(mNhomKhuyenMaiIds))
                                mNhomKhuyenMaiIds +=item.getId();
                            else
                                mNhomKhuyenMaiIds =mNhomKhuyenMaiIds + ";" +item.getId();
                        }
                    actionLoadData();
                })
                .setNegativeButton(R.string.huy, (dialogInterface, i) -> {
                });
        // set view
        final ListView lvFilter =(ListView)dialog.findViewById(R.id.lvFilter);
        mTinhAdapter =new FilterTinhAdapter(mContext, mListTinhs, (position, isCheck) -> {

            if(mListTinhs.size()>position) {
                for (Tinh item:mListTinhs)
                {
                    item.setIsCheck(false);
                }
                mListTinhs.get(position).setIsCheck(isCheck);
                mTinhAdapter.notifyDataSetChanged();
                lvFilter.invalidateViews();
                lvFilter.refreshDrawableState();
            }
        });
        lvFilter.setAdapter(mTinhAdapter);
        // nhom khuyen mai
        final ListView lvNhomKhuyenMaiFilter =(ListView)dialog.findViewById(R.id.lvNhomKhuyenMaiFilter);
        mNhomKhuyenMaiAdapter =new FilterNhomKhuyenMaiAdapter(mContext, mNhomKhuyenMais, (position, isCheck) -> {
            if(mNhomKhuyenMais.size()>position) {
                mNhomKhuyenMais.get(position).setIsCheck(isCheck);
                mNhomKhuyenMaiAdapter.notifyDataSetChanged();
                lvFilter.invalidateViews();
                lvFilter.refreshDrawableState();
            }
        });
        lvNhomKhuyenMaiFilter.setAdapter(mNhomKhuyenMaiAdapter);
        // create dialog and show
        if(mDialogFilter==null)
            mDialogFilter = alertDialog.create();
        if(mDialogFilter!=null && !mDialogFilter.isShowing()&&!isFinishing()) {
            mDialogFilter = alertDialog.create();
            mDialogFilter.show();
        }
    }
    public ArrayList<FilterView> cloneList(ArrayList<FilterView> cList) {
        ArrayList<FilterView> clonedList = new ArrayList<FilterView>(cList.size());
        for (FilterView dog : cList) {
            clonedList.add(new FilterView(dog.getId(),dog.getParentId(),dog.getName(),dog.isCheck(),dog.isParent(),dog.getChoiceType()));
        }
        return clonedList;
    }
    // endregion
    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
            case R.id.filter_rl:
                showDialogFilter();
                break;
			default:
			break;
		}
	}
}