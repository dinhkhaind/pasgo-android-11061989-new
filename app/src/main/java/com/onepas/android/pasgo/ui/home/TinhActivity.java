package com.onepas.android.pasgo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.account.LoginActivity;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TinhActivity extends BaseAppCompatActivity implements
		OnClickListener {
	private ListView mLvLoaiXe;
	private ArrayList<TinhHome> mTinhs;
	private TinhHomeAdapter tinhAdapter;
	private LinearLayout mLnLoadDing, mLnError, mLnData;
	private Button mBtnThuLai;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chon_tinh);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.khu_vuc));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(null);
		this.initView();
		this.initControl();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mTinhs = new ArrayList<>();
		mLvLoaiXe = (ListView) findViewById(R.id.lvLoaiXe);
		mLnLoadDing = (LinearLayout) findViewById(R.id.lnLoadDing);
		mLnError = (LinearLayout) findViewById(R.id.lnError);
		mBtnThuLai = (Button) findViewById(R.id.btnThuLai);
		mLnData = (LinearLayout) findViewById(R.id.data_ln);
		mBtnThuLai.setOnClickListener(v -> getTinhAll());
	}
	Handler handlerUpdateUI = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					String tinh = Pasgo.getInstance().prefs.getTinhMain();
					if(!StringUtils.isEmpty(tinh))
					{
						try {
							JSONObject jsonObjectTinh = new JSONObject(tinh);
							mTinhs = ParserUtils.getTinhAllV1(jsonObjectTinh);
							viewLayout(3);
							setListview();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					getTinhAll();
					break;
				default:
					break;
			}
		};
	};
	private void getTinhAll() {
		String url = WebServiceUtils
				.URL_GET_TINH_ALLV1(Pasgo.getInstance().token);
		if(StringUtils.isEmpty(Pasgo.getInstance().prefs.getTinhMain()))
			viewLayout(1);
		if (StringUtils.isEmpty(Pasgo.getInstance().userId))
			Pasgo.getInstance().userId = "";
		JSONObject jsonParams = new JSONObject();

		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {
						Pasgo.getInstance().prefs.putTinhMain(response.toString());
						if (mTinhs == null || mTinhs.size() == 0) {
							mTinhs = ParserUtils.getTinhAllV1(response);
							setListview();
						}
						viewLayout(3);
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

	private void setListview()
	{
		tinhAdapter =new TinhHomeAdapter(mActivity, mTinhs, new TinhHomeAdapter.TinhHomeListenner() {
			@Override
			public void check(int position) {
				int tinhId = mTinhs.get(position).getId();
				updateTinhSelected(tinhId);
				Pasgo.getInstance().prefs.putTinhId(tinhId);
				Intent intent = new Intent(mContext,
						LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finishOurLeftInLeft();
			}
		});
		mLvLoaiXe.setAdapter(tinhAdapter);
	}
	private void updateTinhSelected(int tinhId) {
		String url = WebServiceUtils
				.URL_UPDATE_TINH_SELECTED(Pasgo.getInstance().token);
		DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
		if (StringUtils.isEmpty(Pasgo.getInstance().userId))
			Pasgo.getInstance().userId = "";
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("deviceId", factory.getDeviceUuid());
			jsonParams.put("tinhId", tinhId);
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
	@Override
	protected void initControl() {
		// TODO Auto-generated method stub
		super.initControl();
	    handlerUpdateUI.sendEmptyMessage(0);
	}

	private void viewLayout(int numLayout)
	{
		mLnLoadDing.setVisibility(View.GONE);
		mLnError.setVisibility(View.GONE);
		mLnData.setVisibility(View.GONE);
		switch (numLayout) {
		case 1:
			mLnLoadDing.setVisibility(View.VISIBLE);
			mLnError.setVisibility(View.GONE);
			mLnData.setVisibility(View.GONE);
			break;
		case 2:
			mLnLoadDing.setVisibility(View.GONE);
			mLnError.setVisibility(View.VISIBLE);
			mLnData.setVisibility(View.GONE);
			break;
		case 3:
			mLnLoadDing.setVisibility(View.GONE);
			mLnError.setVisibility(View.GONE);
			mLnData.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getSupportMenuInflater().inflate(R.menu.menu_lo_trinh, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finishOurLeftInLeft();
			return true;
		}
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
}