package com.onepas.android.pasgo.ui.termsandpolicies;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class DetailActivity extends BaseAppCompatActivity {

    private WebView mWvDetail;
    private static final String TAG="DetailActivity";
    private int quyDinhId;
    private String quyDinhName;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_check_in_intro);
        Bundle bundle=getIntent().getExtras();
        if(savedInstanceState!=null)
            bundle = savedInstanceState;
        quyDinhId=bundle.getInt(Constants.BUNDLE_QUY_DINH_ID,1);
        quyDinhName=bundle.getString(Constants.BUNDLE_QUY_DINH_NAME,getString(R.string.term_and_policies1));
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        Utils.setTextViewHtml(toolbarTitle,quyDinhName);
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
        String language= Pasgo.getInstance().prefs.getLanguage();
        String link1, link2, link3, link4, link5, link6;
        if(language.equals(Constants.LANGUAGE_VIETNAM))
        {
            link1=Constants.KEY_TERM_AND_POLICIES1_VN;
            link2=Constants.KEY_TERM_AND_POLICIES2_VN;
            link3=Constants.KEY_TERM_AND_POLICIES3_VN;
            link4=Constants.KEY_TERM_AND_POLICIES4_VN;
            link5=Constants.KEY_TERM_AND_POLICIES5_VN;
            link6=Constants.KEY_TERM_AND_POLICIES6_VN;
        }else
        {
            link1=Constants.KEY_TERM_AND_POLICIES1_EN;
            link2=Constants.KEY_TERM_AND_POLICIES2_EN;
            link3=Constants.KEY_TERM_AND_POLICIES3_EN;
            link4=Constants.KEY_TERM_AND_POLICIES4_EN;
            link5=Constants.KEY_TERM_AND_POLICIES5_EN;
            link6=Constants.KEY_TERM_AND_POLICIES6_EN;
        }
        MyWebViewClient wvc;
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        switch (quyDinhId)
        {
            case 1:
                wvc = new MyWebViewClient();
                mWvDetail.setWebViewClient(wvc);
                mWvDetail.loadUrl(link1);
                break;
            case 2:
                wvc = new MyWebViewClient();
                mWvDetail.setWebViewClient(wvc);
                mWvDetail.loadUrl(link2);
                break;
            case 3:
                wvc = new MyWebViewClient();
                mWvDetail.setWebViewClient(wvc);
                mWvDetail.loadUrl(link3);
                break;
            case 4:
                wvc = new MyWebViewClient();
                mWvDetail.setWebViewClient(wvc);
                mWvDetail.loadUrl(link4);
                break;
            case 5:
                wvc = new MyWebViewClient();
                mWvDetail.setWebViewClient(wvc);
                mWvDetail.loadUrl(link5);
                break;
            case 6:
                wvc = new MyWebViewClient();
                mWvDetail.setWebViewClient(wvc);
                mWvDetail.loadUrl(link6);
                break;
            default:
                break;
        }
    }

    @Override
	protected void onSaveInstanceState(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(bundle);
        bundle.putInt(Constants.BUNDLE_QUY_DINH_ID,quyDinhId);
        bundle.putString(Constants.BUNDLE_QUY_DINH_NAME,quyDinhName);
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