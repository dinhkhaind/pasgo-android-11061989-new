package com.onepas.android.pasgo.ui.successfultrips;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Button;
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

public class SuccessfulTripsDetailActivity extends BaseAppCompatActivity implements
		OnClickListener {

	private TextView mTenTaiXe, mBienSoXe, mHangXe, mLoaiXe, mDiemDon,
			mDiemDen, mThoiGian;
	private TextView mTvMoTa,mTvMaTaiTro, mTvTienTaiTro;
	private TextView mTvTenLoaiHinh;
	private static String mSTenTaiXe="", mSDiDong = "";
	private String iDLichSu;
	private LinearLayout mLnTaiTro;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(com.actionbarsherlock.view.Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_successful_trips_detail);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.lich_su_chuyen_di1));
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

		mTenTaiXe = (TextView) findViewById(R.id.ten_tai_xe);
		mBienSoXe = (TextView) findViewById(R.id.bien_so_xe);
		mHangXe = (TextView) findViewById(R.id.hang_xe);
		mLoaiXe = (TextView) findViewById(R.id.loai_xe);
		mDiemDon = (TextView) findViewById(R.id.diem_don);
		mDiemDen = (TextView) findViewById(R.id.diem_den);
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

	protected void setTextInfo(String tenLaiXe, String bienSoXe,
			String tenHangXe, String tenLoaiXe, String diaChiDonXe,
			String diaChiDen, String thoiGianDonXe, String giaTien, String moTa
			, String maTaiTro, String tienTaiTro,String tenLoaiHinh, int loaiHinhId) {

		mTenTaiXe.setText(tenLaiXe);
		mBienSoXe.setText(bienSoXe);
		mHangXe.setText(tenHangXe);
		mLoaiXe.setText(tenLoaiXe);
		mDiemDon.setText(diaChiDonXe);
		mDiemDen.setText(diaChiDen);
		mThoiGian.setText(thoiGianDonXe);
		mTvMoTa.setText(moTa);
		mTvMaTaiTro.setText(maTaiTro);
		mTvTienTaiTro.setText(tienTaiTro);
		mTvTenLoaiHinh.setText(ParserUtils.getTenLoaiHinh(mContext,loaiHinhId));
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
						String bienSoXe = ParserUtils.getStringValue(jsonObj,
								"BienSoXe");
						String DiaChiDonXe = ParserUtils.getStringValue(
								jsonObj, "DiaChiDonXe");
						String TenLaiXe = ParserUtils.getStringValue(jsonObj,
								"TenLaiXe");
						String ThoiGianDonXe = ParserUtils.getStringValue(
								jsonObj, "ThoiGianDonXe");
						String TenHangXe = ParserUtils.getStringValue(jsonObj,
								"TenHangXe");
						String Km = ParserUtils.getStringValue(jsonObj, "Km");
						String DiaChiDen = ParserUtils.getStringValue(jsonObj,
								"DiaChiDen");
						String TenLoaiXe = ParserUtils.getStringValue(jsonObj,
								"TenLoaiXe");
						String GiaTien = ParserUtils.getStringValue(jsonObj,
								"GiaTien");
						String Id = ParserUtils.getStringValue(jsonObj, "Id");
						String MoTa = ParserUtils.getStringValue(jsonObj,
								"MoTa");
						String Sdt = ParserUtils.getStringValue(jsonObj, "DienThoai");
						String maKhuyenMai = ParserUtils.getStringValue(jsonObj, "MaKhuyenMai");
						String tienKhuyenMai = ParserUtils.getStringValue(jsonObj, "TienKhuyenMai");
						String tenLoaiHinh = ParserUtils.getStringValue(jsonObj, "TenLoaiHinh");
						int loaiHinhId = ParserUtils.getIntValue(jsonObj, "LoaiHinhId");
                        mSTenTaiXe=TenLaiXe;
						mSDiDong = Sdt;
						int isKhuyenMai =ParserUtils.getIntValue(jsonObj,"IsKhuyenMai");
						if(isKhuyenMai==1)
						{
							mLnTaiTro.setVisibility(View.VISIBLE);
						}else{
							mLnTaiTro.setVisibility(View.GONE);
						}
						setTextInfo(TenLaiXe.trim(), bienSoXe.trim(), TenHangXe.trim(), TenLoaiXe.trim(),
								DiaChiDonXe.trim(), DiaChiDen.trim(), ThoiGianDonXe.trim()
								, GiaTien.trim(),MoTa.trim(),maKhuyenMai.trim(),tienKhuyenMai.trim(),tenLoaiHinh.trim(),loaiHinhId);
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
        if(StringUtils.isEmpty(mSDiDong)) return;
        showDialogCallDriver(mContext,taxi);
	}

    public static void showDialogCallDriver(final Context context,
                                      final Taxi taxi) {

        final Dialog mDialog;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.popup_confirm_call_driver);
        mDialog.setTitle(R.string.rate_pastxi_title);
        Button btnCallDriver, btnMessage, btnCancel;
        TextView tvHoTen,tvDiDong;
        btnCallDriver = (Button) mDialog.findViewById(R.id.btnCallDriver);
        btnMessage = (Button) mDialog.findViewById(R.id.btnMessage);
        btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
        tvHoTen = (TextView) mDialog.findViewById(R.id.tvHoTen);
        tvDiDong = (TextView) mDialog.findViewById(R.id.tvDiDong);
        tvHoTen.setText(mSTenTaiXe);
        tvDiDong.setText(mSDiDong);
        btnCallDriver.setOnClickListener(v -> {
            mDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                    + mSDiDong));
            context.startActivity(intent);
        });
        btnMessage.setOnClickListener(v -> {
            mDialog.dismiss();
            Intent share = new Intent(Intent.ACTION_VIEW);
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.setType("vnd.android-dir/mms-sms");
            share.putExtra("address", mSDiDong);
            share.putExtra("sms_body", "");
            context.startActivity(Intent.createChooser(share, "Share link Pasgo!"));
        });
        btnCancel.setOnClickListener(v -> mDialog.dismiss());
        mDialog.show();
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