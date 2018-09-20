package com.onepas.android.pasgo.ui.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ObjHistoryDatXe;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseAppCompatActivity {
	private ListView mLvChiNhanh;
	private LinearLayout mLayoutKoDuLieu;
	private RelativeLayout mFooterView;
	private List<ObjHistoryDatXe> mlistItemChiNhanh = new ArrayList<ObjHistoryDatXe>();
	private int pageSize = 20;
	private int mPageNumber = 1;
	private TextView mTvKhongCoDuLieu;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.page_address_free);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);

		mFooterView = (RelativeLayout) findViewById(R.id.load_more_footer);
		mFooterView.setVisibility(View.GONE);
		mLvChiNhanh = (ListView) findViewById(R.id.list_adress_free);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.lich_su_));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
		mLayoutKoDuLieu = (LinearLayout) findViewById(R.id.lyKhongCoThongBao);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mTvKhongCoDuLieu=(TextView)findViewById(R.id.tvKhongCoDuLieu);
		mTvKhongCoDuLieu.setText(StringUtils.getStringByResourse(mContext, R.string.khong_co_km));
		mLvChiNhanh.setOnItemClickListener((parent, view, position, id) -> {
            ObjHistoryDatXe item = mlistItemChiNhanh.get(position);
            verifyVerifyDoiTacKhuyenMai(item);
        });
		getLichSuDatXe();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event != null && keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			finishToRightToLeft();
		}
		return super.onKeyDown(keyCode, event);
	}
	protected void verifyVerifyDoiTacKhuyenMai(final ObjHistoryDatXe item) {
		String doiTacKMId = item.getDoiTacKhuyenMaiId();

		String url = WebServiceUtils.URL_Verify(Pasgo.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("doiTacKhuyenMaiId", doiTacKMId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);
							Utils.Log("response ", "response  khuyen mai"
									+ response);
							try {
								boolean veri = response.getBoolean("Item");
								if (veri) {

									gotoNoiDung(item);

								} else {
									ToastUtils.showToastWaring(mContext,
											getString(R.string.het_han));
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onError(int maloi) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);
						}

					}, new PWErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);
						}
					});
		} else {
			mProgressToolbar.setVisibility(ProgressBar.GONE);
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
			mFooterView.setVisibility(View.GONE);
		}
	}

    private void gotoNoiDung(ObjHistoryDatXe item) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_LICH_SU, true);
        intent.putExtra(Constants.BUNDLE_KEY_ID, item.getId());
        intent.putExtra(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, item.getTenChiNhanhDoiTac());
        intent.putExtra(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, item.getDoiTacKhuyenMaiId());
        intent.putExtra(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, item.getNhomCnDoiTacID());
        intent.putExtra(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaDiemDen());
        intent.putExtra(Constants.BUNDLE_KEY_LINK_WEBSITE, item.getWebsite());
        intent.putExtra(Constants.BUNDLE_KEY_GHI_CHU, item.getMoTaKhuyenMai());
        intent.putExtra(Constants.BUNDLE_KEY_MO_TA, item.getMoTaDatXe());
        intent.putExtra(Constants.BUNDLE_KEY_KINH_DO, item.getKinhDo());
        intent.putExtra(Constants.BUNDLE_KEY_VI_DO, item.getViDo());
        intent.putExtra(Constants.BUNDLE_KEY_IS_LICH_SU, true);
        intent.putExtra(Constants.BUNDLE_KEY_DAT_TRUOC, item.isDattruoc());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, Constants.MENU_TO_LIST_C1);
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
		if (requestCode == Constants.KEY_GO_TO_MAP) {
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

	private void getLichSuDatXe() {
		String url = WebServiceUtils.URL_GET_LICH_SU_DOI_TAC(Pasgo
				.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("pageSize", pageSize);
			jsonParams.put("pageNumber", mPageNumber);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);
							Utils.Log("response ", "response  khuyen mai"
									+ response);
							final ArrayList<ObjHistoryDatXe> listHistory = new ArrayList<ObjHistoryDatXe>();
							try {
								JSONArray jsonArray = response
										.getJSONArray("Items");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject = jsonArray
											.getJSONObject(i);
									String thoiGian = jsonObject
											.getString("ThoiGian");
									String nhomCnDoiTacId = jsonObject
											.getString("NhomCnDoiTacId");
									String website = jsonObject
											.getString("Website");
									String DiaChiDen = jsonObject
											.getString("DiaChiDen");
									String doiTacKhuyenMaiId = jsonObject
											.getString("DoiTacKhuyenMaiId");
									String Id = jsonObject.getString("Id");
									String moTaKhuyenMai = jsonObject
											.getString("MoTaKhuyenMai");
									String moTaDatXe = jsonObject
											.getString("MoTaDatXe");
									String tenChiNhanhDoiTac = jsonObject
											.getString("TenChiNhanhDoiTac");
									double viDo = jsonObject.getDouble("ViDo");
									double kinhDo = jsonObject
											.getDouble("KinhDo");
									String giaUocTinh = jsonObject
											.getString("GiaUocTinh");
									String kmUocTinh = jsonObject
											.getString("KmUocTinh");
									boolean daXacNhan = jsonObject
											.getBoolean("DaXacNhan");

									ObjHistoryDatXe objHistory = new ObjHistoryDatXe();

									objHistory.setDiaDiemDen(DiaChiDen);
									objHistory
											.setDoiTacKhuyenMaiId(doiTacKhuyenMaiId);
									objHistory.setId(Id);
									objHistory.setKinhDo(kinhDo);
									objHistory.setMoTaDatXe(moTaDatXe);
									objHistory.setMoTaKhuyenMai(moTaKhuyenMai);
									objHistory
											.setNhomCnDoiTacID(nhomCnDoiTacId);
									objHistory.setThoiGian(thoiGian);
									objHistory.setViDo(viDo);
									objHistory.setWebsite(website);
									objHistory
											.setTenChiNhanhDoiTac(tenChiNhanhDoiTac);
									objHistory.setGiaUocTinh(giaUocTinh);
									objHistory.setKmUocTinh(kmUocTinh);
									objHistory.setDaXacNhan(daXacNhan);
									listHistory.add(objHistory);
								}
								if (listHistory.size() > 0) {
									mLayoutKoDuLieu.setVisibility(View.GONE);
									mLvChiNhanh.setVisibility(View.VISIBLE);
									setListAdapter(listHistory);
								} else {
									mLayoutKoDuLieu.setVisibility(View.VISIBLE);
									mLvChiNhanh.setVisibility(View.GONE);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onError(int maloi) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);
							mLayoutKoDuLieu.setVisibility(View.VISIBLE);
							mLvChiNhanh.setVisibility(View.GONE);
						}

					}, new PWErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);
							mLayoutKoDuLieu.setVisibility(View.VISIBLE);
							mLvChiNhanh.setVisibility(View.GONE);
						}
					});
		} else {
			mProgressToolbar.setVisibility(ProgressBar.GONE);
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
			// mFooterView.setVisibility(View.GONE);
		}
	}

	private void setListAdapter(ArrayList<ObjHistoryDatXe> listHistory) {
		mlistItemChiNhanh = listHistory;
		HistoryAdapter adapter = new HistoryAdapter(mContext,
				R.layout.item_history_partner, listHistory, 2);
		mLvChiNhanh.setAdapter(adapter);
		mProgressToolbar.setVisibility(ProgressBar.GONE);
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
	public void onNetworkChanged() {

		if (mLnErrorConnectNetwork != null) {

			if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable())
				mLnErrorConnectNetwork.setVisibility(View.GONE);
			else
				mLnErrorConnectNetwork.setVisibility(View.VISIBLE);

		}

	}
    @Override
    public void onStartMoveScreen() {

    }
	@Override
	public void onUpdateMapAfterUserInterection() {
	}
}