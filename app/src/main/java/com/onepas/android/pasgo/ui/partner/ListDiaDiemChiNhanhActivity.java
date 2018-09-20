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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ItemDiaChiChiNhanh;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.calldriver.DatXeActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListDiaDiemChiNhanhActivity extends BaseAppCompatActivity {
	private JSONArray mArray;
	private ListView mLvChiNhanh;
	private String tenDoiTac, mGhiChu;
	private RelativeLayout mFooterView;
	private List<ItemDiaChiChiNhanh> mlistItemChiNhanh = new ArrayList<ItemDiaChiChiNhanh>();
	private int mDiXeFree;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.page_address_free);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);

		mFooterView = (RelativeLayout) findViewById(R.id.load_more_footer);
		mFooterView.setVisibility(View.GONE);
		mLvChiNhanh = (ListView) findViewById(R.id.list_adress_free);
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			mDiXeFree = extra.getInt(Constants.KEY_DI_XE_FREE);
			mGhiChu = extra.getString(Constants.BUNDLE_KEY_GHI_CHU);
			String array = extra.getString(Constants.KEY_JSONARRAY);
			try {
				mArray = new JSONArray(array);

				if (NetworkUtils.getInstance(getBaseContext())
						.isNetworkAvailable()) {
					getListDiaDiemChiTiet(mArray);
					mLnErrorConnectNetwork.setVisibility(View.GONE);
				} else {
					mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			tenDoiTac = extra.getString(Constants.BUNDLE_KEY_TEN_DOI_TAC);
		}
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		Utils.setTextViewHtml(toolbarTitle,tenDoiTac);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);

		mLvChiNhanh.setOnItemClickListener((parent, view, position, id) -> {
            ItemDiaChiChiNhanh item = mlistItemChiNhanh.get(position);
            Intent intent = new Intent(mContext, DatXeActivity.class);
            intent.putExtra(Constants.BUNDLE_KEY_CHI_NHANH_KM, true);
            intent.putExtra(Constants.BUNDLE_KEY_TEN, item.getTen());
            intent.putExtra(Constants.BUNDLE_KEY_ID, item.getId());
            intent.putExtra(Constants.BUNDLE_KEY_DIA_CHI, item.getDiaChi());
            intent.putExtra(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, item.getNhomCnDoiTacId());
            intent.putExtra(Constants.BUNDLE_KEY_KINH_DO, item.getKinhDo());
            intent.putExtra(Constants.BUNDLE_KEY_VI_DO, item.getViDo());
            intent.putExtra(Constants.BUNDLE_KEY_GHI_CHU, mGhiChu);
            intent.putExtra(Constants.BUNDLE_KEY_MO_TA, item.getMoTa());
            intent.putExtra(Constants.BUNDLE_KEY_LINK_WEBSITE, item.getWebsite());
            intent.putExtra(Constants.KEY_DI_XE_FREE, mDiXeFree);
            // startActivity(intent);
            startActivityForResult(intent, Constants.MENU_TO_LIST_C1);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.KEY_GO_TO_MAP && resultCode == RESULT_OK) {
			setResult(Constants.MENU_TO_LIST_C1);
			finishToRightToLeft();
		}
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
				intent = new Intent(mActivity, DetailActivity.class);
				Bundle bundle1 = new Bundle();
				bundle1.putInt(Constants.KEY_COMPLETE,
						Constants.KEY_INT_COMPLETE);
				intent.putExtras(bundle1);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				setResult(Constants.MENU_TO_LIST_C1, intent);

				finishToRightToLeft();
			} else {
				finishToRightToLeft();
			}
		}
	}

	private void setListAdapter(List<ItemDiaChiChiNhanh> listItemChiNhanh) {
		mlistItemChiNhanh = listItemChiNhanh;
		ChiNhanhAdapter adapter = new ChiNhanhAdapter(mContext,
				R.layout.item_di_xe_mien_phi_chi_tiet, mlistItemChiNhanh, 2);
		mLvChiNhanh.setAdapter(adapter);
	}

	private void getListDiaDiemChiTiet(JSONArray array) {
		Utils.Log("response ", "response  khuyen mai" + array);
		List<ItemDiaChiChiNhanh> listItemChiNhanh = new ArrayList<ItemDiaChiChiNhanh>();
		if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jsonObject = array.getJSONObject(i);
					String ten = jsonObject.getString("Ten");
					String id = jsonObject.getString("Id");
					String diaChi = jsonObject.getString("DiaChi");
					String nhomCnDoiTacId = jsonObject
							.getString("NhomCnDoiTacId");
					String moTa = jsonObject.getString("MoTa");
					String website = jsonObject.getString("Website");
					double kinhDo = jsonObject.getDouble("KinhDo");
					double viDo = jsonObject.getDouble("ViDo");
					ItemDiaChiChiNhanh item = new ItemDiaChiChiNhanh();
					item.setDiaChi(diaChi);
					item.setId(id);
					item.setKinhDo(kinhDo);
					item.setMoTa(moTa);
					item.setNhomCnDoiTacId(nhomCnDoiTacId);
					item.setTen(ten);
					item.setViDo(viDo);
					item.setWebsite(website);
					listItemChiNhanh.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			mLnErrorConnectNetwork.setVisibility(View.GONE);
			setListAdapter(listItemChiNhanh);
		} else {
			mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
		}
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