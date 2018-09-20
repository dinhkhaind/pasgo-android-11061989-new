package com.onepas.android.pasgo.ui.partner;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;

public class ImageViewActivity extends BaseAppCompatActivity {
	private ListView mLvData;
	private ImageViewAdapter adapter;
	private ArrayList<String> images;
	private int mPosition;
	private String mImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_image_view);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.image_view));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mLvData = (ListView) findViewById(R.id.list_data);
		Bundle bundle;
		images=new ArrayList<String>();
		if (savedInstanceState != null)
			bundle = savedInstanceState;
		else
			bundle = getIntent().getExtras();
		if (bundle != null) {
			mImage = bundle.getString(Constants.BUNDLE_IMAGE_VIEW);
			mPosition = bundle.getInt(Constants.BUNDLE_IMAGE_VIEW_POSITION);
			String[] separated = mImage.split(",");
			for (String string : separated) {
				images.add(string);
			}
		} else
			finishToRightToLeft();
		initControl();
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
	protected void initControl() {
		// TODO Auto-generated method stub
		super.initControl();
		setListAdapter();
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(bundle);
		bundle.putInt(Constants.BUNDLE_IMAGE_VIEW_POSITION, mPosition);
		bundle.putString(Constants.BUNDLE_IMAGE_VIEW, mImage);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setListAdapter() {
		adapter = new ImageViewAdapter(mContext, images);
		mLvData.setAdapter(adapter);
		mLvData.setSelection(mPosition);
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