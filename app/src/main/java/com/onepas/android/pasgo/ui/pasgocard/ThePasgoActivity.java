package com.onepas.android.pasgo.ui.pasgocard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class ThePasgoActivity extends BaseAppCompatActivity {
    private FragmentTabHost mTabHost;
    private Context mContext;
    public int mNotificationCheck;
    private int mTabNumber =0;
    private boolean mIsGotoDatXe=false;
    public String mDoiTacKhuyenMaiId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_pasgo_card);

        mContext =this;
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.diem_den_tai_tro, R.drawable.the_pasgo_tab_one)),
                DatChoFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.di_xe, R.drawable.the_pasgo_tab_two)),
                DatXeFragment.class, null);
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().setDividerDrawable(null);
            if(i==0) {
                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textView); //Unselected Tabs
                tv.setTextColor(Utils.getColor(mContext, R.color.red));
            }
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                mTabHost.refreshDrawableState();
                //mTabHost.removeAllViews();
                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
                        .setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textView); //Unselected Tabs
                    tv.setTextColor(Utils.getColor(mContext, R.color.tab_text_unselect));
                }
                TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(R.id.textView); //for Selected Tab
                tv.setTextColor(Utils.getColor(mContext, R.color.tab_text_selected));
            }
        });
        Bundle extra =getIntent().getExtras();
        if(savedInstanceState!=null) extra = savedInstanceState;
        if (extra != null) {
            mNotificationCheck = extra.getInt(Constants.KEY_TYPE_PUSH_NOTIFICATION);
            mTabNumber = extra.getInt(Constants.BUNDLE_TAB_NUMBER, 0);
            mIsGotoDatXe =extra.getBoolean(Constants.BUNDLE_IS_GO_TO_DATXE, false);
            mDoiTacKhuyenMaiId = extra.getString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID);
            if(savedInstanceState==null)
                mTabNumber= Constants.ThePasgoTabNumber;
            if(mTabNumber>0)
                mTabHost.setCurrentTab(mTabNumber);
        }
        // neu DoiTacKhuyenMaiId # null
        //mTabHost.setVisibility(View.GONE);
        if(!StringUtils.isEmpty(mDoiTacKhuyenMaiId)) {
            //mTabHost.setCurrentTab(0);
            mTabHost.removeAllViews();
        }
    }

    private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout_the_pasgo, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(Constants.KEY_TYPE_PUSH_NOTIFICATION, mNotificationCheck);
        bundle.putInt(Constants.BUNDLE_TAB_NUMBER, mTabNumber);
        bundle.putBoolean(Constants.BUNDLE_IS_GO_TO_DATXE, mIsGotoDatXe);
        bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID,mDoiTacKhuyenMaiId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(Pasgo.TAG, "ThePASGO requestCode" + requestCode);
        Utils.Log(Pasgo.TAG, "ThePASGO resultCode" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.KEY_BACK_BY_MA_DAT_XE:
                    finishOurLeftInLeft();
                    break;
                default:
                    break;
            }
        }
    }
    private void backActivity()
    {
        if((mNotificationCheck == Constants.KEY_ACTIVITY_THE_PASGO ) && Utils.checkStartApp(mActivity))
        {
            gotoActivityClearTop(mActivity, HomeActivity.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishOurLeftInLeft();
                }
            }, Constants.KEY_BACK_ACTIVITY_DELAY);
        }else
            finishOurLeftInLeft();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.danh_sach_tai_tro_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backActivity();
                return true;
            case R.id.setting:
                gotoActivityClearTop(mContext,MaTaiTroInfoActivity.class);
                ourLeftInLeft();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }

}