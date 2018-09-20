package com.onepas.android.pasgo.ui.partner;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WebviewChiTietActivity extends BaseAppCompatActivity {
    public static final int UU_DAI  = 1;
    public static final int GIOI_THIEU  = 2;
    private WebView mWvDetail;
    private int mType;
    private String mName="";
    private String mDoiTacKhuyenMaiId;
    private String mNoiDung;
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
            mType = bundle.getInt(Constants.KEY_TYPE_WEBVIEW, WebviewChiTietActivity.UU_DAI);
            if(mType==UU_DAI) {
                mName = getString(R.string.detail_uu_dai_title);
            }else
            if(mType==GIOI_THIEU) {
                mName = getString(R.string.detail_gioi_thieu_title);
            }
            mDoiTacKhuyenMaiId = bundle.getString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID,"");
        }
        setContentView(R.layout.page_webview);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        Utils.setTextViewHtml(toolbarTitle,mName);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mWvDetail = (WebView)findViewById(R.id.wvCheckIn);
        mWvDetail.getSettings().setBuiltInZoomControls(false);
        mWvDetail.getSettings().setDisplayZoomControls(false);
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
        outState.putInt(Constants.KEY_TYPE_WEBVIEW,mType);
        outState.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID, mDoiTacKhuyenMaiId);
    }

    @Override
    protected void initControl() {
        super.initControl();
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        if(mType==UU_DAI) {
            getChiTietUuDai();
        }else
        if(mType==GIOI_THIEU) {
            getChiTietGioiThieu();
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

    protected void setNoiDung(String noiDung) {
        WebSettings webSettings = mWvDetail.getSettings();
        webSettings.setDefaultFontSize(7);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        String customHtml = formatHtml(noiDung);
        mWvDetail.setBackgroundColor(Color.parseColor("#ffffff"));
        mWvDetail.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8",
                null);
    }
    private String formatHtml(String noiDung)
    {
        return "<html><head><style> "
                + "body{text-align:justify;}"
                + "img{width:100%}"
                + "table{text-align:left;text-align:right; font-size:13px; width: 100%;border-collapse: collapse; }"
                + "h2{margin:0;line-height:22px;font-size:18px;font-weight:700;color:#d02028}"
                + "h3{margin:10px 0;font-size:11pt;font-weight:700}"
                + "h4{margin:16px 0;font-size:10pt;font-weight:700}"
                + "h5{text-align:center;font-size:9pt;font-weight:400;line-height:22px;margin:2px 0px 3px 0px}"
                + "</style></head> "
                + "<body>"
                + "<p>" + "<font face="
                + "sans-serif" + " size= 6>" + noiDung + "</font>"
                + "</body></html>";
    }
    protected void getChiTietUuDai() {
        String url = WebServiceUtils.URL_GET_CHI_TIET_UU_DAI(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("doiTacKhuyenMaiId", mDoiTacKhuyenMaiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = ParserUtils.getJsonObject(response,"Item");
                        mNoiDung = ParserUtils.getStringValue(object,"NoiDung");
                        mNoiDung = mNoiDung.replace("line-height","");
                        setNoiDung(mNoiDung);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }
    protected void getChiTietGioiThieu() {
        String url = WebServiceUtils.URL_GET_CHI_TIET_GIOI_THIEU(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("doiTacKhuyenMaiId", mDoiTacKhuyenMaiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = ParserUtils.getJsonObject(response,"Item");
                        mNoiDung = ParserUtils.getStringValue(object,"NoiDung");
                        setNoiDung(mNoiDung);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }
    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}
}