package com.onepas.android.pasgo.ui.reserve;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.Utils;

public class ReserveIntroActivity extends BaseAppCompatActivity {

    private WebView mWvDetail;
    private static final String TAG="ReserveIntroActivity";
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_check_in_intro);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.menu_check_in);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mWvDetail = (WebView)findViewById(R.id.wvCheckIn);
        mWvDetail.getSettings().setBuiltInZoomControls(false);
        mWvDetail.getSettings().setDisplayZoomControls(false);
        mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
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
        super.initControl();
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        String language= Pasgo.getInstance().prefs.getLanguage();
        String checkIn;
        if(language.toLowerCase().equals(Constants.LANGUAGE_VIETNAM))
        {
            checkIn= Constants.KEY_CHECK_IN_INTRODUCTION_VN;
            Utils.Log(TAG, "viet nam");
        }else
        {
            checkIn= Constants.KEY_CHECK_IN_INTRODUCTION_EN;
            Utils.Log(TAG,"language orther");
        }
        MyWebViewClient wvc;
        wvc = new MyWebViewClient();
        mWvDetail.setWebViewClient(wvc);
        mWvDetail.getSettings().setJavaScriptEnabled(true);
        mWvDetail.loadUrl(checkIn);

    }

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(bundle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            finishToRightToLeft();
		}
		return true;
	}

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressToolbar.setVisibility(ProgressBar.GONE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebSettings webSettings = view.getSettings();
            webSettings.setDefaultFontSize(8);
            webSettings.setBuiltInZoomControls(false);
            view.loadUrl(url);
            return true;
        }
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            WebSettings webSettings = view.getSettings();
            webSettings.setDefaultFontSize(8);
            webSettings.setBuiltInZoomControls(false);
            view.loadUrl(request.getUrl().toString());
            return true;
        }

    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
}