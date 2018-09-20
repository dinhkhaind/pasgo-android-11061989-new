package com.onepas.android.pasgo.ui.share;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.LinkEnabledTextView;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.TextLinkClickListener;
import com.onepas.android.pasgo.utils.Utils;

import java.util.List;

public class ShareActivity extends BaseAppCompatActivity implements
        OnClickListener, TextLinkClickListener {

    boolean mCheckEdit = false;

    private LinearLayout mLayout, mLayout2;
    private ImageView mImChiaSeFB, mImChiaSeEmail, mImChiaSeSMS, mImChiaSeKhac;
    String invite = StringUtils.isEmpty(Pasgo.getInstance().ma) ? "123456" : Pasgo.getInstance().ma;
    String mUrlLink = "https://pasgo.vn/app";// "http://pasgo.vn/invite/" + invite;
    private TextView mTvVersion, mTvShareText2;

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_share);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.gioi_thieu_ban_be));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mImChiaSeFB = (ImageView) findViewById(R.id.chiaSe_FB);
        mImChiaSeEmail = (ImageView) findViewById(R.id.chiaSe_Email);
        mImChiaSeSMS = (ImageView) findViewById(R.id.chiaSe_SMS);
        mImChiaSeKhac = (ImageView) findViewById(R.id.chiaSe_Khac);
        mTvShareText2 = (TextView) findViewById(R.id.share_text_2_tv);

        mImChiaSeFB.setOnClickListener(this);
        mImChiaSeEmail.setOnClickListener(this);
        mImChiaSeSMS.setOnClickListener(this);
        mImChiaSeKhac.setOnClickListener(this);

        mLayout = (LinearLayout) findViewById(R.id.line_add_text);
        mLayout2 = (LinearLayout) findViewById(R.id.line_add_text2);

        String web = getString(R.string.website) + " "
                + getString(R.string.pastaxi_vn);
        LinkEnabledTextView check1 = setLinkEnable(web);
        mLayout.addView(check1);

        String dieuKhoanSD = getString(R.string.dieu_khoan_su_dung);
        LinkEnabledTextView check2 = setLinkEnable(dieuKhoanSD);
        mLayout2.addView(check2);

        String versionName = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            versionName = pInfo.versionName;
        } catch (NameNotFoundException e1) {
            Log.e("", "Name not found", e1);
        }

        mTvVersion.setText("" + "Version " + versionName);
        Utils.setTextViewHtml(mTvShareText2, getString(R.string.share_text_2));

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Utils.Log(Pasgo.TAG, "Facebook onSuccess");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
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
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private LinkEnabledTextView setLinkEnable(String text) {
        LinkEnabledTextView check;
        check = new LinkEnabledTextView(this, null);
        check.setOnTextLinkClickListener(this);
        check.gatherLinksForText(text);
        check.setTextColor(Color.BLACK);
        check.setLinkTextColor(Color.BLUE);

        MovementMethod m = check.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (check.getLinksClickable()) {
                check.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        return check;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chiaSe_FB:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    String tieuDe = getString(R.string.share_facebook_email_content);
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(tieuDe)
                            .setContentDescription("")
                            .setContentUrl(Uri.parse(mUrlLink))
                            .build();

                    shareDialog.show(linkContent);
                }
                break;
            case R.id.chiaSe_Email:
                initShareIntent("gmail");
                break;
            case R.id.chiaSe_SMS:
                sheareLinkPasgoViaSMS();
                break;
            case R.id.chiaSe_Khac:
                sheareLinkPasgo();
                break;

            default:
                break;
        }
    }

    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
                share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type)
                        || info.activityInfo.name.toLowerCase().contains(type)) {
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_slogan_share_email));
                    share.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.share_facebook_email_content) + "\n" + getString(R.string.download_app)+ mUrlLink);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;

            startActivity(Intent.createChooser(share, "Select"));
        }
    }

    private void sheareLinkPasgoViaSMS() {
        Intent share = new Intent(android.content.Intent.ACTION_VIEW);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setType("vnd.android-dir/mms-sms");
        share.putExtra("sms_body", getString(R.string.share_sms_content)+ "\n"
                + getString(R.string.download_app_sms) + mUrlLink);
        startActivity(Intent.createChooser(share, "Share link Pasgo!"));
    }

    private void sheareLinkPasgo() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_SUBJECT, "");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_sms_content) + "\n" + getString(R.string.download_app_sms)
                + mUrlLink);
        startActivity(Intent.createChooser(share,
                getResources().getString(R.string.title_share_pastaxi)));
    }

    @Override
    public void onTextLinkClick(View textView, String clickedString) {
        if (clickedString.equals(getString(R.string.pastaxi_vn))) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Constants.KEY_PASGO));
            startActivity(i);

        } else if (clickedString.equals(getString(R.string.dieu_khoan_su_dung))) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            String language = Pasgo.getInstance().prefs.getLanguage();
            String dieuKhoan;
            if (language.equals(Constants.LANGUAGE_VIETNAM)) {
                dieuKhoan = Constants.KEY_DIEU_KHOAN_VN;
            } else {
                dieuKhoan = Constants.KEY_DIEU_KHOAN_EN;
            }
            i.setData(Uri.parse(dieuKhoan));
            startActivity(i);

        } else if (clickedString.equals(getString(R.string.chinh_sach_bao_mat))) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Constants.KEY_BAO_MAT));
            startActivity(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
}