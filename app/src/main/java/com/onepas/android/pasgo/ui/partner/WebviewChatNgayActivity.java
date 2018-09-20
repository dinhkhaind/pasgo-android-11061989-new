package com.onepas.android.pasgo.ui.partner;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;

public class WebviewChatNgayActivity extends BaseAppCompatActivity {

    private WebView mWvDetail;
    private String mUrl;
    private String mName;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(bundle==null)
            bundle = savedInstanceState;
        if(bundle!=null)
        {
            mUrl = bundle.getString(Constants.BUNDLE_KEY_LINK);
            mName = bundle.getString(Constants.BUNDLE_KEY_ACTIONBAR_NAME);
        }
        setContentView(R.layout.page_chat_ngay);
        mWvDetail = (WebView)findViewById(R.id.wvCheckIn);
        mWvDetail.getSettings().setBuiltInZoomControls(false);
        mWvDetail.getSettings().setDisplayZoomControls(false);
        mProgressToolbar = (ProgressBar)findViewById(R.id.toolbar_progress_bar);
        findViewById(R.id.close_ln).setOnClickListener(v-> finishOurLeftInLeft());
        initControl();
	}

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(Constants.BUNDLE_KEY_LINK,mUrl);
        outState.putString(Constants.BUNDLE_KEY_ACTIONBAR_NAME, mName);
    }

    @Override
    protected void initControl() {
        super.initControl();
        //mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        showProgressDialog();
        MyWebViewClient wvc;
        wvc = new MyWebViewClient();
        mWvDetail.setWebViewClient(wvc);
        mWvDetail.getSettings().setJavaScriptEnabled(true);
        mWvDetail.loadUrl(mUrl);

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
			finishOurLeftInLeft();
		}
		return true;
	}

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //mProgressToolbar.setVisibility(ProgressBar.GONE);
            closeProgressDialog();
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