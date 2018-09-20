package com.onepas.android.pasgo.ui.termsandpolicies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DanhSachQuyDinh;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.ExpandableHeightListView;

import java.util.ArrayList;
public class TermsAndPoliciesActivity extends BaseAppCompatActivity implements
		OnClickListener {

    private ExpandableHeightListView mLvDanhSach;
    private TermsAndPoliciesAdapter mAdapter;
    private ArrayList<DanhSachQuyDinh> mLists;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.page_terms_and_policies);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.quy_dinh_chung_menu));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        initView();
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
    protected void initView() {
        super.initView();
        mLvDanhSach = (ExpandableHeightListView)findViewById(R.id.lvDanhSachQuyDinh);

    }

    @Override
    protected void initControl() {
        super.initControl();
        mLists=new ArrayList<DanhSachQuyDinh>();
        mLists.add(new DanhSachQuyDinh(1,getString(R.string.term_and_policies1),R.drawable.ic_quydinh_cs));
        mLists.add(new DanhSachQuyDinh(2,getString(R.string.term_and_policies2),R.drawable.ic_quydinh_mataitro));
        mLists.add(new DanhSachQuyDinh(3,getString(R.string.term_and_policies3),R.drawable.ic_quydinh_checkin));
        mLists.add(new DanhSachQuyDinh(4,getString(R.string.term_and_policies4),R.drawable.ic_quydinh_checkin));
        mLists.add(new DanhSachQuyDinh(5,getString(R.string.term_and_policies5),R.drawable.ic_quydinh_mataitro));
        mLists.add(new DanhSachQuyDinh(6,getString(R.string.term_and_policies6),R.drawable.ic_quydinh_checkin));
        mAdapter=new TermsAndPoliciesAdapter(mContext,mLists);
        mLvDanhSach.setAdapter(mAdapter);
        mLvDanhSach.setOnItemClickListener((adapterView, view, i, l) -> {
            DanhSachQuyDinh item=mLists.get(i);
            Bundle bundle=new Bundle();
            bundle.putInt(Constants.BUNDLE_QUY_DINH_ID,item.getId());
            bundle.putString(Constants.BUNDLE_QUY_DINH_NAME,item.getName());
            gotoActivity(mContext,DetailActivity.class,bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft();
        });
        mLvDanhSach.setExpanded(true);
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
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search:
			break;
		default:
			break;
		}
	}
}