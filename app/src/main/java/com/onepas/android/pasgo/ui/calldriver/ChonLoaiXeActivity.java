package com.onepas.android.pasgo.ui.calldriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.LoaiXe;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChonLoaiXeActivity extends BaseAppCompatActivity implements
		OnClickListener {
	private static final String TAG = "ChonLoaiXeActivity";
	private ListView mLvLoaiXe;
	private ArrayList<LoaiXe> loaiXes;
	private ChonLoaiXeAdapter chomLoaiXeAdapter;
	private LinearLayout mLnLoadDing, mLnError;
	private Button mBtnThuLai;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_chon_loai_xe);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.chon_loai_xe));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		this.initView();
		this.initControl();
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
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		loaiXes = new ArrayList<LoaiXe>();
		mLvLoaiXe = (ListView) findViewById(R.id.lvLoaiXe);
		mLnLoadDing = (LinearLayout) findViewById(R.id.lnLoadDing);
		mLnError = (LinearLayout) findViewById(R.id.lnError);
		mBtnThuLai = (Button) findViewById(R.id.btnThuLai);
		mBtnThuLai.setOnClickListener(v -> getAllNhomXe());
	}

	private void getAllNhomXe() {
		String url = WebServiceUtils
				.URL_GET_NHOM_XE_ALL(Pasgo.getInstance().token);
		JSONObject jsonParams = new JSONObject();
		viewLayout(1);
		try {
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject json) {
							if (json != null) {
								viewLayout(3);
								loaiXes = ParserUtils.getAllLoaiXe(json);
								chomLoaiXeAdapter = new ChonLoaiXeAdapter(
										mContext, loaiXes);
								mLvLoaiXe.setAdapter(chomLoaiXeAdapter);
								mLvLoaiXe
										.setOnItemClickListener((arg0, arg1, position, arg3) -> {
                                            LoaiXe loaiXe = loaiXes
                                                    .get(position);
                                            Intent intent = new Intent();
                                            intent.putExtra(
                                                    Constants.BUNDLE_DICH_VU_ID,
                                                    loaiXe.getId());
                                            intent.putExtra(
                                                    Constants.BUNDLE_LOAIXE_NAME,
                                                    loaiXe.getName());
                                            intent.putExtra(
                                                    Constants.BUNDLE_LOAIXE_IMAGE,
                                                    loaiXe.getImage());
                                            setResult(
                                                    Constants.kEY_CHON_LOAI_XE,
                                                    intent);
											finishToRightToLeft();
                                        });
							}
						}

						@Override
						public void onError(int maloi) {
							closeProgressDialog();
						}

					}, new PWErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							viewLayout(2);
							Utils.Log(TAG, error.toString());
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			viewLayout(2);
		}
	}

	@Override
	protected void initControl() {
		// TODO Auto-generated method stub
		super.initControl();
	    this.getAllNhomXe();
	}

	private void viewLayout(int numLayout)
	{
		mLnLoadDing.setVisibility(View.GONE);
		mLnError.setVisibility(View.GONE);
		mLvLoaiXe.setVisibility(View.GONE);
		switch (numLayout) {
		case 1:
			mLnLoadDing.setVisibility(View.VISIBLE);
			mLnError.setVisibility(View.GONE);
			mLvLoaiXe.setVisibility(View.GONE);
			break;
		case 2:
			mLnLoadDing.setVisibility(View.GONE);
			mLnError.setVisibility(View.VISIBLE);
			mLvLoaiXe.setVisibility(View.GONE);
			break;
		case 3:
			mLnLoadDing.setVisibility(View.GONE);
			mLnError.setVisibility(View.GONE);
			mLvLoaiXe.setVisibility(View.VISIBLE);
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