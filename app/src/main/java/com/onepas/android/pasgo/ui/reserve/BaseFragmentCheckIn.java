package com.onepas.android.pasgo.ui.reserve;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseFragmentCheckIn extends BaseFragment {
	private static final String TAG ="BaseFragmentCheckIn";
	protected DiemDenModel mItemCheckIn = null;
	protected int mPosition=0;
	protected String mNhomCNDoiTacId="";
	protected String mTenDoiTac;
	protected boolean mIsErrorChannel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	public void updateTinhSelected() {
		String url = WebServiceUtils
				.URL_UPDATE_TINH_SELECTED(Pasgo.getInstance().token);
		DeviceUuidFactory factory = new DeviceUuidFactory(mActivity);
		if (StringUtils.isEmpty(Pasgo.getInstance().userId))
			Pasgo.getInstance().userId = "";
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("deviceId", factory.getDeviceUuid());
			jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {

					}

					@Override
					public void onError(int maloi) {
					}

				}, new Pasgo.PWErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}
	protected void lvClickItem(DiemDenModel item, int tinhId, String maKhuyenMai, boolean isDiemTaiTroNguoiDung, boolean isDiemTaiTroNguoiDungTuDo) {
		if (item != null) {
			String doiTacKMID = item.getDoiTacKhuyenMaiId();
			String title = item.getTen();
			String chiNhanhDoiTacId = item.getId();
			String nhomCnDoiTac = item.getNhomCNDoiTacId();
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.KEY_DI_XE_FREE, Constants.KEY_INT_XE_FREE);
			bundle.putInt(Constants.KEY_TEN_TINH_ID, tinhId);
			bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
			bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, doiTacKMID);
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
			bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
			bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, item.getNhomKhuyenMaiId());
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getDiaChi());
			bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCnDoiTac);
			bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, maKhuyenMai);
			bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, isDiemTaiTroNguoiDung);
			bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, isDiemTaiTroNguoiDungTuDo);
			gotoActivityForResult(getActivity(), DetailActivity.class, bundle,
					Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ourLeftInLeft(mActivity);
		}
	}
	//region phần này dành cho checkin

	private boolean mIsClickCheckIn = false;
	public synchronized void initCheckIn()
	{
		if(!mIsClickCheckIn)
		{
			mIsClickCheckIn = true;
			Bundle bundle =new Bundle();
			bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID,mNhomCNDoiTacId);
			bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM,mTenDoiTac);
			bundle.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, mItemCheckIn.getTrangThai());
			bundle.putString(Constants.BUNDLE_KEY_DIA_CHI, mItemCheckIn.getDiaChi());
			gotoActivityForResult(mActivity, ReserveDetailActivity.class, bundle, Constants.KEY_BACK_BY_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ourLeftInLeft(mActivity);
			mIsClickCheckIn = false;
		}
	}

	//endregion
}