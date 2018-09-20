package com.onepas.android.pasgo.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class SettingActivity extends BaseAppCompatActivity {
    private RelativeLayout mRlLanguage;
    private TextView mTvLanguage;
    private boolean mIsShowKhuyenMai;
    private String mLanguageBefor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_setting);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.setting));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> backToMain());
        this.initView();
        this.initControl();
        this.getBundle();
        this.onNetworkChanged();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mLanguageBefor = bundle.getString(Constants.BUNDLE_LANGUAGE_BEFOR, Constants.LANGUAGE_VIETNAM);
            mIsShowKhuyenMai = bundle.getBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI);
        }
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        mRlLanguage = (RelativeLayout) findViewById(R.id.rlLanguage);
        mTvLanguage = (TextView) findViewById(R.id.tvLanguage);
        mRlLanguage.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            changuageLanguage();
        });
    }

    @Override
    protected void initControl() {
        // TODO Auto-generated method stub
        super.initControl();
        mTvLanguage.setText(StringUtils.getStringByResourse(mContext,
                R.string.language_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToMain();
                return true;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, mIsShowKhuyenMai);
        outState.putString(Constants.BUNDLE_LANGUAGE_BEFOR, mLanguageBefor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        mIsShowKhuyenMai = savedInstanceState.getBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI);
        mLanguageBefor = savedInstanceState.getString(Constants.BUNDLE_LANGUAGE_BEFOR);
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
        // TODO Auto-generated method stub

    }

    private void changuageLanguage() {
        CharSequence colors[] = new CharSequence[]{StringUtils.getStringByResourse(mContext, R.string.language_tiengviet), StringUtils.getStringByResourse(mContext, R.string.language_tienganh)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(StringUtils.getStringByResourse(mContext,
                R.string.language_title));
        builder.setItems(colors, (dialog, which) -> {
            String language;
            Bundle bundle = new Bundle();
            switch (which) {
                case 0:
                    language = Constants.LANGUAGE_VIETNAM;
                    Pasgo.getInstance().prefs.putLanguage(language);
                    Utils.changeLocalLanguage(language);
                    bundle.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, mIsShowKhuyenMai);
                    bundle.putString(Constants.BUNDLE_LANGUAGE_BEFOR, mLanguageBefor);
                    gotoActivity(mContext, SettingActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finishToRightToLeft();
                    break;
                case 1:
                    language = Constants.LANGUAGE_ENGLISH;
                    Pasgo.getInstance().prefs.putLanguage(language);
                    Utils.changeLocalLanguage(language);
                    bundle.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, mIsShowKhuyenMai);
                    bundle.putString(Constants.BUNDLE_LANGUAGE_BEFOR, mLanguageBefor);
                    gotoActivity(mContext, SettingActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finishToRightToLeft();
                    break;
                default:
                    break;
            }
        });
        if (!isFinishing())
            builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backToMain();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backToMain() {
        if (Pasgo.getInstance().prefs.getLanguage().equals(mLanguageBefor)) {
            Pasgo.getInstance().language = Pasgo.getInstance().prefs.getLanguage();
            finishToRightToLeft();
        } else {
            Pasgo.getInstance().language = Pasgo.getInstance().prefs.getLanguage();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, mIsShowKhuyenMai);
            bundle.putBoolean(Constants.BUNDLE_GO_TO_FROM_CHANG_LANGUAGE, true);
            gotoActivity(mContext, HomeActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finishToRightToLeft();
        }
    }


}