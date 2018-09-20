package com.onepas.android.pasgo.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.onepas.android.pasgo.BuildConfig;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.account.ActivationActivity;
import com.onepas.android.pasgo.ui.account.LoginActivity;
import com.onepas.android.pasgo.ui.account.UpdatePasswordActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseAppCompatActivity {

	private int TIME_SPLASH = 1200;
    private Context context;
    private Dialog mDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        context=this;
		if(!BuildConfig.DEBUG){
            Fabric.with(this, new Crashlytics());
		}
		setContentView(R.layout.page_splash);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	@Override
	protected void onResume() {
		super.onResume();
        Pasgo.getInstance().isPopupDatCho=false;
        Pasgo.getInstance().isUpdateImage=false;
        Pasgo.getInstance().isUpdatePassWord=false;
        Pasgo.getInstance().isGotoUpdatePassWordActivity=false;
		getTokenPrefs();
		if (isNetWork()) {
            if(mDialog!=null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog.cancel();
            }
            startApp();
		} else {
			showYesNoDialog(R.string.message_enable_network, onEnableNetwork, huy);
		}
	}

    private void startApp()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Pasgo.getInstance().prefs != null)
                    Pasgo.getInstance().prefs.putTimeCall(null);
                if(isCheckLogin()) {
                    // nếu chưa update mật khẩu, và đã vào đến màn hình updatePasswordActivity thì cho
                    if(!Pasgo.getInstance().isUpdatePassWord && Pasgo.getInstance().isGotoUpdatePassWordActivity) {
                        // ko dung thu
                        Pasgo.getInstance().prefs.putIsTrial(false);
                        Intent intent = new Intent(SplashActivity.this,
                                UpdatePasswordActivity.class);
                        // nếu là từ màn hình register thì sau khi update mật khẩu nó sẽ sang màn hình nhập người giới thiệu
                        intent.putExtra(Constants.BUNDLE_KEY_IS_REGISTER, Pasgo.getInstance().prefs.getCheckIfGoToRegisterActivity());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                        finishOurLeftInLeft();
                    // nếu đã đến màn hình nhập mã người giới thiệu thì cho lại vào màn hình đó
                    }
                    // bỏ phần này vì ko cần nhập mã giới thiệu(123456) nữa
                    /*else if(Pasgo.getInstance().prefs.getIsPresenterActivity())
                    {
                        // ko dung thu
                        Pasgo.getInstance().prefs.putIsTrial(false);
                        Intent intent = new Intent(SplashActivity.this,
                                PresenterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }*/
                    else
                    {
                        Pasgo.getInstance().prefs.putIsTrial(false);
                        Intent intent = new Intent(SplashActivity.this,
                                HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finishOurLeftInLeft();
                    }
                }else
                {
                    // nếu đang ở màn hình nhập mã kích hoạt thì quay trở lại màn hình nhập mã
                    if(Pasgo.getInstance().prefs.getIsKichHoatActivity())
                    {
                        // ko dung thu
                        Pasgo.getInstance().prefs.putIsTrial(false);
                        Intent intent = new Intent(SplashActivity.this,
                                ActivationActivity.class);
                        intent.putExtra(Constants.BUNDLE_KEY_IS_REGISTER, Pasgo.getInstance().prefs.getCheckIfGoToRegisterActivity());
                        intent.putExtra(Constants.BUNDLE_KEY_GO_TO_FROM_SPLASH_ACTIVITY,true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }else {
                        // nếu chưa chọn tỉnh
                        if(Pasgo.getInstance().prefs.getTinhId()<1) {
                            Intent intent = new Intent(SplashActivity.this,
                                    TinhActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                        else
                        {// nếu đã chọn tỉnh thì vào LoginPresenter lu
                            Intent intent = new Intent(SplashActivity.this,
                                    LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                        finishOurLeftInLeft();
                    }
                }
            }
        }, TIME_SPLASH);
    }


    private void showYesNoDialog(int messageId,final View.OnClickListener onOKClick,
                                             final View.OnClickListener onHuyClick) {
        if(mDialog==null) {
            mDialog = new Dialog(context);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
            mDialog.setCancelable(false);
            TextView tvThongBaoPopup=(TextView) mDialog.findViewById(R.id.tvThongBaoPopup);
            Button btnDongY,btnHuy;
            btnDongY = (Button)mDialog.findViewById(R.id.btnDongY);
            btnHuy = (Button)mDialog.findViewById(R.id.btnHuy);
            btnDongY.setOnClickListener(onOKClick);
            btnHuy.setOnClickListener(onHuyClick);
            btnDongY.setText(R.string.dong_y);
            tvThongBaoPopup.setText(context.getString(messageId));
        }
        if(!isFinishing() && !mDialog.isShowing())
            mDialog.show();
    }

	public boolean isNetWork() {
        return NetworkUtils.getInstance(this)
                .isNetworkAvailable();
	}

	private void getTokenPrefs() {
		if (Pasgo.getInstance().prefs == null)
			return;
		Pasgo.getInstance().token = Pasgo.getInstance().prefs.getToken();
		Pasgo.getInstance().userId = Pasgo.getInstance().prefs.getUserId();
		Pasgo.getInstance().sdt = Pasgo.getInstance().prefs.getSdt();
		Pasgo.getInstance().maKichHoat = Pasgo.getInstance().prefs
				.getMakichhoat();
		Pasgo.getInstance().ma = Pasgo.getInstance().prefs.getMa();
        Pasgo.getInstance().username = Pasgo.getInstance().prefs.getUserName();
        Pasgo.getInstance().isUpdatePassWord = Pasgo.getInstance().prefs.getisUpdatePassword();
        Pasgo.getInstance().isGotoUpdatePassWordActivity = Pasgo.getInstance().prefs.getIsUpdatePasswordActivity();
        Pasgo.getInstance().urlAnh = Pasgo.getInstance().prefs.getUrlAnh();
        if(StringUtils.isEmpty(Pasgo.getInstance().prefs.getNationCode())||StringUtils.isEmpty(Pasgo.getInstance().prefs.getNationName()))
        {
            Pasgo.getInstance().prefs.putNationCode(Constants.KEY_VIETNAM_CODE);
            Pasgo.getInstance().prefs.putNationName(Constants.KEY_VIETNAM_NAME);
        }
		if(StringUtils.isEmpty(Pasgo.getInstance().userId))
			Pasgo.getInstance().isUserNotNull=false;
		else
			Pasgo.getInstance().isUserNotNull=true;

		setupLanguage();
	}
	private void setupLanguage()
	{
		String language= Pasgo.getInstance().prefs.getLanguage();
		if(StringUtils.isEmpty(language))
		{
			String locale = Locale.getDefault().getLanguage();
			if(locale.equals(Constants.LANGUAGE_VIETNAM))
				language=Constants.LANGUAGE_VIETNAM;
			else
				language=Constants.LANGUAGE_ENGLISH;
		}
		Pasgo.getInstance().language=language;
		Pasgo.getInstance().prefs.putLanguage(language);
		Utils.changeLocalLanguage(language);
	}

	OnClickListener huy = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDialog!=null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog.cancel();
            }
            startApp();
        }
    };
    OnClickListener onEnableNetwork = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent settings = new Intent(
                    android.provider.Settings.ACTION_WIFI_SETTINGS);
            startActivity(settings);
        }
    };

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }
}
