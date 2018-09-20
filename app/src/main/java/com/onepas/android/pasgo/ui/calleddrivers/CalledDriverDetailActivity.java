package com.onepas.android.pasgo.ui.calleddrivers;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class CalledDriverDetailActivity extends BaseAppCompatActivity implements
		OnClickListener {

	private TextView mTenTaiXe, mBienSoXe, mHangXe, mLoaiXe, mDiemDon,
			mDiemDen, mGiaTien, mThoiGian;
	private TextView mTvMoTa,mTvMaTaiTro, mTvTienTaiTro;
	private TextView mTvTenLoaiHinh;
	private static String mSTenTaiXe="", mSDiDong = "";
	private String mDatXeId;
	private LinearLayout mLnHangXe, mLnDriver;
	private TextView mTvTenHangXe, mTvSoHieuXe;
	private LinearLayout mLnChuaCoTaiXe, mLnCoTaiXe;
	private RelativeLayout mRlibCall;
	private boolean mIsCall=false;
	private boolean mIsTaiXe=false;
	private LinearLayout mLnTaiTro;
	private TextView mTvGoiTongDai;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_called_driver_detail);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.dat_truoc));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		mProgressToolbar =(ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
		Bundle intent = getIntent().getExtras();
		if (intent != null) {
			mDatXeId = intent.getString(Constants.BUNDLE_DAT_XE_ID);
		}
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mTenTaiXe = (TextView) findViewById(R.id.ten_tai_xe);
		mBienSoXe = (TextView) findViewById(R.id.bien_so_xe);
		mHangXe = (TextView) findViewById(R.id.hang_xe);
		mLoaiXe = (TextView) findViewById(R.id.loai_xe);
		mDiemDon = (TextView) findViewById(R.id.diem_don);
		mDiemDen = (TextView) findViewById(R.id.diem_den);
		mGiaTien = (TextView) findViewById(R.id.gia_tien);
		mThoiGian = (TextView) findViewById(R.id.thoi_gian);
		mTvMoTa = (TextView) findViewById(R.id.mo_ta);
		mTvMaTaiTro = (TextView) findViewById(R.id.ma_tai_tro);
		mTvTienTaiTro = (TextView) findViewById(R.id.muc_tai_tro);
		mTvTenLoaiHinh = (TextView) findViewById(R.id.ten_loai_hinh_tv);
		mLnHangXe = (LinearLayout) findViewById(R.id.lnHangXe);
		mLnDriver = (LinearLayout) findViewById(R.id.lnDriver);
		mTvTenHangXe = (TextView) findViewById(R.id.ten_hang_xe_tv);
		mTvSoHieuXe = (TextView) findViewById(R.id.so_hieu_xe_tv);
		mLnChuaCoTaiXe = (LinearLayout) findViewById(R.id.lnChuaCoTaiXe);
		mLnCoTaiXe = (LinearLayout) findViewById(R.id.lnCoTaiXe);
		mRlibCall = (RelativeLayout) findViewById(R.id.ibCall);
		mLnTaiTro =(LinearLayout)findViewById(R.id.tai_tro_ln);
		mTvGoiTongDai =(TextView)findViewById(R.id.goi_tong_dai_tv);
		mRlibCall.setOnClickListener(this);
		getChiTietLichSuDatTruoc(mDatXeId);
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
	private void getChiTietLichSuDatTruoc(String datXeId) {
		String url = WebServiceUtils.URL_CHI_TIET_LICH_SU(Pasgo.getInstance().token);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("datXeId", datXeId);
		JSONObject jsonParams = new JSONObject(params);
		mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new PWListener() {
					@Override
					public void onResponse(JSONObject response) {
						mProgressToolbar.setVisibility(ProgressBar.GONE);
						JSONObject jsonObj = ParserUtils.getJsonObject(
								response, "Item");
						String bienSoXe = ParserUtils.getStringValue(jsonObj, "BienSoXe");
						String DiaChiDonXe = ParserUtils.getStringValue(jsonObj, "DiaChiDonXe");
						String tenLaiXe = ParserUtils.getStringValue(jsonObj, "TenLaiXe");
						String ThoiGianDonXe = ParserUtils.getStringValue(jsonObj, "ThoiGianDonXe");
						String TenHangXe = ParserUtils.getStringValue(jsonObj, "TenHangXe");
						String DiaChiDen = ParserUtils.getStringValue(jsonObj, "DiaChiDen");
						String TenLoaiXe = ParserUtils.getStringValue(jsonObj, "TenLoaiXe");
						String GiaTien = ParserUtils.getStringValue(jsonObj, "GiaTien");
						String moTa = ParserUtils.getStringValue(jsonObj, "MoTa");
						String soHieuXe = ParserUtils.getStringValue(jsonObj, "SoHieuXe");
						String Sdt = ParserUtils.getStringValue(jsonObj, "DienThoai");
						String tenLoaiHinh = ParserUtils.getStringValue(jsonObj, "TenLoaiHinh");
						int loaiHinhId = ParserUtils.getIntValue(jsonObj, "LoaiHinhId");
						int isKhuyenMai = ParserUtils.getIntValue(jsonObj, "IsKhuyenMai");
						String tienKhuyenMaiFormat = ParserUtils.getStringValue(jsonObj, "TienKhuyenMaiFormat");
						String maKhuyenMai = ParserUtils.getStringValue(jsonObj, "MaKhuyenMai");
						int isHangXe = ParserUtils.getIntValue(jsonObj, "IsHangXe");
                        mSTenTaiXe=tenLaiXe;
						mSDiDong = Sdt;
						mDiemDon.setText(DiaChiDonXe.trim());
						mDiemDen.setText(DiaChiDen.trim());
						mThoiGian.setText(ThoiGianDonXe.trim());
						mGiaTien.setText(GiaTien.trim() + " " + getString(R.string.vnd));
						mTvMoTa.setText(moTa);
						if(isKhuyenMai==1) {
							mTvMaTaiTro.setText(maKhuyenMai);
							mTvTienTaiTro.setText(tienKhuyenMaiFormat);
							mLnTaiTro.setVisibility(View.VISIBLE);
						}else{
							mLnTaiTro.setVisibility(View.GONE);
						}
						mTvTenLoaiHinh.setText(ParserUtils.getTenLoaiHinh(mContext, loaiHinhId));
						if(isHangXe ==1) {
							mLnHangXe.setVisibility(View.VISIBLE);
							mLnDriver.setVisibility(View.GONE);
							mTvTenHangXe.setText(TenHangXe.trim());
							mTvSoHieuXe.setText(soHieuXe.trim());
							mSDiDong = Constants.SDT_TONG_DAI;
							mIsCall=true;
							mRlibCall.setBackgroundColor(Utils.getColor(mContext, R.color.goididong));
							mTvGoiTongDai.setText(getString(R.string.goi_tong_dai));
							mIsTaiXe=false;
						}else
						{
							mIsTaiXe=true;
							mTvGoiTongDai.setText(getString(R.string.goi_di_dong_cho_tx));
							mLnHangXe.setVisibility(View.GONE);
							mLnDriver.setVisibility(View.VISIBLE);
							mTenTaiXe.setText(tenLaiXe.trim());
							mBienSoXe.setText(bienSoXe.trim());
							mHangXe.setText(TenHangXe.trim());
							mLoaiXe.setText(TenLoaiXe.trim());
							if(StringUtils.isEmpty(mSDiDong))
							{
								mIsCall=false;
								mRlibCall.setBackgroundColor(Utils.getColor(mContext, R.color.gray));
							}else
							{
								mIsCall=true;
								mRlibCall.setBackgroundColor(Utils.getColor(mContext, R.color.goididong));
							}
						}
						if(StringUtils.isEmpty(tenLaiXe))
						{
							mLnChuaCoTaiXe.setVisibility(View.VISIBLE);
							mLnCoTaiXe.setVisibility(View.GONE);
						}else{
							mLnChuaCoTaiXe.setVisibility(View.GONE);
							mLnCoTaiXe.setVisibility(View.VISIBLE);
						}

					}

					@Override
					public void onError(int maloi) {
						closeProgressDialogAll();
					}

				}, new PWErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						mProgressToolbar.setVisibility(ProgressBar.GONE);
					}
				});

	}

    public void showDialogCallDriver(final Context context) {
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
		if(!isFinishing())
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
		switch (arg0.getId())
		{
			case R.id.ibCall:
				if(!mIsCall)return;
				if(StringUtils.isEmpty(mSDiDong)) return;
				if(mIsTaiXe)
					showDialogCallDriver(mContext);
				else
				{
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
							+ mSDiDong));
					startActivity(intent);
				}
				break;
		}
	}

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
}