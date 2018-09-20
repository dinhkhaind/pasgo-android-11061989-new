package com.onepas.android.pasgo.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class UpdatePassWordDialog extends Activity implements OnClickListener {

    private final String TAG="UpdatePassWord";
    private EditText mEdtPassword, mEdtPasswordAgain;
    private Button mBtnUpdate;
	private TextView mTvThongBap, mtvDieuKHoan;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_update_password);
		getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL,
				LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
		}

        mEdtPassword = (EditText)findViewById(R.id.edtPassword);
        mEdtPasswordAgain = (EditText)findViewById(R.id.edtPasswordAgain);
        mBtnUpdate = (Button)findViewById(R.id.btnUpdate);
        mTvThongBap =(TextView)findViewById(R.id.tvThongBao);
        Utils.setTextViewHtml(mTvThongBap,getString(R.string.update_content_popup));
        mtvDieuKHoan =(TextView)findViewById(R.id.tvDieuKhoan);
        Utils.setTextViewHtml(mtvDieuKHoan,getString(R.string.update_content_popup2));
        mBtnUpdate.setOnClickListener(this);
        mtvDieuKHoan.setOnClickListener(this);
        mEdtPasswordAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    actionUpdatePassWord();
                }
                return false;
            }
        });
	}
    private void actionUpdatePassWord()
    {
        if(StringUtils.isEmpty(mEdtPassword.getText().toString().trim())||StringUtils.isEmpty(mEdtPasswordAgain.getText().toString().trim()))
            ToastUtils.showToast(UpdatePassWordDialog.this, R.string.thieu_thong_tin);
        else if(mEdtPassword.getText().toString().trim().length()<6)
            ToastUtils.showToast(UpdatePassWordDialog.this,R.string.password_leng);
        else if(!mEdtPassword.getText().toString().trim().equals(mEdtPasswordAgain.getText().toString().trim()))
            ToastUtils.showToast(UpdatePassWordDialog.this,R.string.password_khong_trung_nhau);
        else
        {
            updatePassword();
        }
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUpdate:
            actionUpdatePassWord();
			break;
        case R.id.tvDieuKhoan:
        {
            Intent i = new Intent(Intent.ACTION_VIEW);
            String language= Pasgo.getInstance().prefs.getLanguage();
            String dieuKhoan;
            if(language.equals(Constants.LANGUAGE_VIETNAM))
            {
                dieuKhoan=Constants.KEY_DIEU_KHOAN_VN;
            }else
            {
                dieuKhoan=Constants.KEY_DIEU_KHOAN_EN;
            }
            i.setData(Uri.parse(dieuKhoan));
            startActivity(i);
        }
		default:
			break;
		}
	}
    public void updatePassword() {
        String url = WebServiceUtils
                .URL_UPDATE_PASSWORD(Pasgo.getInstance().token);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("nguoiDungId", Pasgo.getInstance().userId);
        params.put("matKhau", mEdtPassword.getText().toString().trim());
        JSONObject jsonParams = new JSONObject(params);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.Log(TAG, "update password" + response.toString());
                        Pasgo.getInstance().prefs.putIsUpdatePassword(true);
                        finish();
                    }

                    @Override
                    public void onError(int maloi) {
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }



    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
			return false;
		}
		return super.onTouchEvent(event);
	}
}