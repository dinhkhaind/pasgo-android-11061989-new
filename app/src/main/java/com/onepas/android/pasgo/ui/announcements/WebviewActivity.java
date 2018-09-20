package com.onepas.android.pasgo.ui.announcements;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WebviewActivity extends BaseAppCompatActivity {

    private WebView mWvDetail;
    private String mTieuDe;
    private String mId;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_announcement_detail1);
        Bundle bundle=getIntent().getExtras();
        if(bundle==null)
            bundle = savedInstanceState;
        if(bundle!=null)
        {
            mTieuDe = bundle.getString(Constants.BUNDLE_KEY_TIN_KM_TITLE,"");
            mId = bundle.getString(Constants.BUNDLE_KEY_TIN_KM_ID,"");
        }
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(mTieuDe);//
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mWvDetail = (WebView)findViewById(R.id.wvCheckIn);
        mWvDetail.getSettings().setBuiltInZoomControls(false);
        mWvDetail.getSettings().setDisplayZoomControls(false);
        mWvDetail.getSettings().setJavaScriptEnabled(true);
        mWvDetail.setWebViewClient(new WebViewClient() {

        });
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, mTieuDe);
        outState.putString(Constants.BUNDLE_KEY_TIN_KM_ID, mId);
    }

    @Override
    protected void initControl() {
        super.initControl();
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        getContentKM(mId);
    }
    private void getContentKM(String iD) {
        Double lat=0.0,lng=0.0;
        if (Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
            lat = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            lng = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        DeviceUuidFactory factory = new DeviceUuidFactory(mContext);
        String url = WebServiceUtils.URL_GET_CHI_TIET_DOI_TAC_KM(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("viDo", lat);
            jsonParams.put("kinhDo", lng);
            jsonParams.put("khuyenMaiId", iD);
            jsonParams.put("deviceId", factory.getDeviceUuid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        try {
                            JSONObject jsonObject = ParserUtils
                                    .getJsonObject(response, "Item");
                            String noiDung = jsonObject
                                    .getString("NoiDung");
                            setNoiDung(noiDung);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                }, error -> mProgressToolbar.setVisibility(ProgressBar.GONE));

    }

    protected void setNoiDung(String noiDung) {
        WebSettings webSettings = mWvDetail.getSettings();
        webSettings.setDefaultFontSize(8);
        webSettings.setBuiltInZoomControls(true);
        noiDung += "<input type=\"submit\" name=\"submit\" id=\"submit_id\" onclick=\"BtnLogin.performClick();\" />";
        String customHtml = "<html><body>" + "<p>" + "<font face="
                + "sans-serif" + " size= 6>" + noiDung + "</font>"

                + "</body></html>";
        mWvDetail.setWebViewClient(new MyBrowser());
        mWvDetail.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8",
                null);

    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

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

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
}