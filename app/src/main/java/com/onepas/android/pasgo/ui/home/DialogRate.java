package com.onepas.android.pasgo.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class DialogRate extends Activity implements OnClickListener {

	private Button mBtnOk;
	private TextView mTvContentConfirm;
	private EditText mEdTTDanhGia;
	private LinearLayout mLnTot, mLnKem;
	private ImageView mImTot, mImKem, mImClose;
	private int typeRate = 2;
	private String mDatXeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_rate);
		getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL,
				LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		mEdTTDanhGia = (EditText) findViewById(R.id.tt_danh_gia);
		mTvContentConfirm = (TextView) findViewById(R.id.tvContentConfirm);
		mTvContentConfirm.setText(getResources().getString(
				R.string.ban_danh_gia_tx));

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			mDatXeId = extra.getString("DatXeId");
		}

		mImClose = (ImageView) findViewById(R.id.close);
		mImClose.setOnClickListener(this);

		mImKem = (ImageView) findViewById(R.id.im_kem);
		mImTot = (ImageView) findViewById(R.id.im_tot);
		mLnKem = (LinearLayout) findViewById(R.id.liner_kem);
		mLnTot = (LinearLayout) findViewById(R.id.liner_tot);
		mLnKem.setOnClickListener(this);
		mLnTot.setOnClickListener(this);

		mBtnOk = (Button) findViewById(R.id.btnGui);
		mBtnOk.setOnClickListener(this);

		getStatusRate();
	}

	private void getStatusRate() {
		String nguoiDungId = Pasgo.getInstance().userId;
		String token = Pasgo.getInstance().token;

		String url = WebServiceUtils.URL_Rate_Status(token);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nguoiDungId", nguoiDungId);
			jsonParams.put("datXeId", mDatXeId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						Utils.Log("", "response rate :" + response);
						JSONObject item = ParserUtils.getJsonObject(response,
								"Item");
						if (item == null) {

						} else {
							try {
								int loai = item.getInt("Loai");
								String noiDungRate = item.getString("NoiDung");
								setRate(loai, noiDungRate);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onError(int maloi) {
						Utils.Log("", "response rate :");
					}

				}, new PWErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Utils.Log("", "response rate :");
						showAlertMangYeu(2);
					}
				});
	}

	protected void setRate(int loai, String noiDungRate) {
		typeRate = loai;
		if (typeRate == 0) {
			danhGiaKem();
		} else {
			danhGiaTot();
		}
		mEdTTDanhGia.setText(noiDungRate);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			finish();
			break;
		case R.id.btnGui:

			if (typeRate == 2) {
				ToastUtils.showToast(getApplicationContext(),
						getString(R.string.ban_chua_danh_gia));
			} else {
				guiDanhGia();
			}
			break;
		case R.id.liner_kem:
			danhGiaKem();
			if (typeRate == 1) {
				// danhGiaKem();
			} else if (typeRate == 0) {
				// danhGiaTot();
				// typeRate = 2;
			}
			break;
		case R.id.liner_tot:
			danhGiaTot();
			if (typeRate == 1) {
				// danhGiaKem();
				// typeRate = 2;
			} else if (typeRate == 0) {
				// danhGiaTot();
			}
			break;
		default:
			break;
		}
	}

	private void guiDanhGia() {
		String contentRate = mEdTTDanhGia.getText().toString();
		String nguoiDungId = Pasgo.getInstance().userId;
		String token = Pasgo.getInstance().token;

		String url = WebServiceUtils.URL_Rate(token);
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nguoiDungId", nguoiDungId);
			jsonParams.put("noiDung", contentRate);
			jsonParams.put("loai", typeRate);
			jsonParams.put("datXeId", mDatXeId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {

			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							Utils.Log("", "response rate :" + response);
							ToastUtils.showToast(getApplicationContext(),
									getString(R.string.danh_gia_thanh_cong));
							finish();
						}

						@Override
						public void onError(int maloi) {
							Utils.Log("", "response rate :");
						}

					}, new PWErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Utils.Log("", "response rate :");
							showAlertMangYeu(1);
						}
					});
		} else {
			showAlertMangYeu(1);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showAlertMangYeu(final int i) {
        if (!isFinishing() || getBaseContext() == null)
            return;
		final Dialog dialog = new Dialog(DialogRate.this);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_mang_yeu);
		Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);
		Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
		dialogBtThuLai.setOnClickListener(v -> {
            switch (i) {
            case 1:
                guiDanhGia();
                break;
            case 2:
                getStatusRate();
                break;
            case 3:
                break;

            default:
                break;
            }
            dialog.dismiss();
        });
		dialogBtHuy.setOnClickListener(v -> {
            dialog.dismiss();
			finish();
        });
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	private void danhGiaTot() {
		mImKem.setBackgroundResource(R.drawable.ic_dislike0);
		mImTot.setBackgroundResource(R.drawable.ic_like1);
		typeRate = 1;
		mLnTot.setBackgroundColor(Utils.getColor(DialogRate.this, R.color.bg_liner_like));
		mLnKem.setBackgroundColor(Utils.getColor(DialogRate.this, R.color.liner_like));
	}

	private void danhGiaKem() {
		mImKem.setBackgroundResource(R.drawable.ic_dislike1);
		mImTot.setBackgroundResource(R.drawable.ic_like0);
		mLnKem.setBackgroundColor(Utils.getColor(DialogRate.this, R.color.bg_liner_like));
		mLnTot.setBackgroundColor(Utils.getColor(DialogRate.this, R.color.liner_like));
		typeRate = 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
			return false;
		}
		return super.onTouchEvent(event);
	}
}