package com.onepas.android.pasgo.ui.successfultrips;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.Taxi;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class SuccessfulTripsDetailAdapter extends BaseAppCompatActivity implements
		OnClickListener {

	private TextView mTxtHangXe, mTxtSoHieuXe, mTxtTongDaiSdt, mLoaiXe, mDiemDon,
			mDiemDen, mGiaTien, mThoiGian;
	private TextView mTvMoTa,mTvMaTaiTro, mTvTienTaiTro;
	private TextView mTvTenLoaiHinh;
	private String diDong = "";
	private String iDLichSu;
	private LinearLayout mLnTaiTro;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lich_su_chuyen_di_hangxe_chi_tiet);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(R.string.lich_su_chuyen_di1);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		mProgressToolbar =(ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
		Bundle intent = getIntent().getExtras();
		if (intent != null) {
			iDLichSu = intent.getString(Constants.KEY_LICH_SU_ID);
		}

		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);

        mTxtHangXe = (TextView) findViewById(R.id.txtHangXe);
        mTxtSoHieuXe = (TextView) findViewById(R.id.txtSoHieuXe);
        mTxtTongDaiSdt = (TextView) findViewById(R.id.txtTongDaiSdt);
		mLoaiXe = (TextView) findViewById(R.id.loai_xe);
		mDiemDon = (TextView) findViewById(R.id.diem_don);
		mDiemDen = (TextView) findViewById(R.id.diem_den);
		mGiaTien = (TextView) findViewById(R.id.gia_tien);
		mThoiGian = (TextView) findViewById(R.id.thoi_gian);
		mTvMoTa = (TextView) findViewById(R.id.mo_ta);
		mTvMaTaiTro = (TextView) findViewById(R.id.ma_tai_tro);
		mTvTienTaiTro = (TextView) findViewById(R.id.muc_tai_tro);
		mTvTenLoaiHinh = (TextView) findViewById(R.id.ten_loai_hinh_tv);
		mLnTaiTro = (LinearLayout) findViewById(R.id.tai_tro_ln);
		getChiTietLichSu(iDLichSu);

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
	public void onNetworkChanged() {

		if (mLnErrorConnectNetwork != null) {

			if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable())
				mLnErrorConnectNetwork.setVisibility(View.GONE);
			else
				mLnErrorConnectNetwork.setVisibility(View.VISIBLE);

		}

	}

	protected void setTextInfo(String tenHangXe, String soHieuXe,String sdt,
			String tenLoaiXe, String diaChiDonXe,
			String diaChiDen, String thoiGianDonXe, String giaTien, String moTa
			, String maTaiTro, String tienTaiTro,String tenLoaiHinh, int loaiHinhId) {

        mTxtHangXe.setText(tenHangXe);
        mTxtSoHieuXe.setText(soHieuXe);
        mTxtTongDaiSdt.setText(tenHangXe);
		mLoaiXe.setText(tenLoaiXe);
		mDiemDon.setText(diaChiDonXe);
		mDiemDen.setText(diaChiDen);
		mThoiGian.setText(thoiGianDonXe);
		mGiaTien.setText(giaTien + " " + getString(R.string.vnd));

		mTvMoTa.setText(moTa);
		mTvMaTaiTro.setText(maTaiTro);
		mTvTienTaiTro.setText(tienTaiTro);
		mTvTenLoaiHinh.setText(tenLoaiHinh);
		mTvTenLoaiHinh.setText(ParserUtils.getTenLoaiHinh(mContext, loaiHinhId));
	}

	private void getChiTietLichSu(String idLichSu) {
		String url = WebServiceUtils
				.URL_CHI_TIET_LICH_SU(Pasgo.getInstance().token);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("datXeId", idLichSu);
		JSONObject jsonParams = new JSONObject(params);
		mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						mProgressToolbar.setVisibility(ProgressBar.GONE);
						JSONObject jsonObj = ParserUtils.getJsonObject(
								response, "Item");
                        String TenHangXe = ParserUtils.getStringValue(jsonObj,
								"TenHangXe");
						String soHieuXe = ParserUtils.getStringValue(jsonObj,
								"BienSoXe");
						diDong = ParserUtils.getStringValue(jsonObj, "DienThoai");
                        String TenLoaiXe = ParserUtils.getStringValue(jsonObj,
                                "TenLoaiXe");
                        String DiaChiDonXe = ParserUtils.getStringValue(
                                jsonObj, "DiaChiDonXe");
						String DiaChiDen = ParserUtils.getStringValue(jsonObj,
								"DiaChiDen");
                        String ThoiGianDonXe = ParserUtils.getStringValue(
                                jsonObj, "ThoiGianDonXe");
						String GiaTien = ParserUtils.getStringValue(jsonObj,
								"GiaTien");
						String MoTa = ParserUtils.getStringValue(jsonObj,
								"MoTa");
						String maKhuyenMai = ParserUtils.getStringValue(jsonObj, "MaKhuyenMai");
						String tienKhuyenMai = ParserUtils.getStringValue(jsonObj, "TienKhuyenMai");
						String tenLoaiHinh = ParserUtils.getStringValue(jsonObj, "TenLoaiHinh");
						int loaiHinhId = ParserUtils.getIntValue(jsonObj, "LoaiHinhId");
						int isKhuyenMai =ParserUtils.getIntValue(jsonObj,"IsKhuyenMai");
						if(isKhuyenMai==1)
						{
							mLnTaiTro.setVisibility(View.VISIBLE);
						}else{
							mLnTaiTro.setVisibility(View.GONE);
						}
                        if(StringUtils.isEmpty(soHieuXe))
                            soHieuXe=getString(R.string.dang_cap_nhat);
						setTextInfo(TenHangXe.trim(), soHieuXe.trim(), diDong.trim(), TenLoaiXe.trim(),
								DiaChiDonXe.trim(), DiaChiDen.trim(), ThoiGianDonXe.trim(), GiaTien.trim()
								,MoTa.trim(),maKhuyenMai.trim(),tienKhuyenMai.trim(),tenLoaiHinh.trim(),loaiHinhId);
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

	}

	public void callOnClickHandler(View v) {
		Taxi taxi = (Taxi) v.getTag();
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ diDong));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
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
	public void onClick(View arg0) {

	}

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
}