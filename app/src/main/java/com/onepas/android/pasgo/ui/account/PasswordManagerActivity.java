package com.onepas.android.pasgo.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class PasswordManagerActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PasswordManager";
    private EditText mEdtPasswordOld, mEdtPasswordNew, mEdtPasswordAgain;
    private Button mBtnUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_password_manager);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.pasword_manager));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        initView();
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
    protected void initView() {
        super.initView();
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mEdtPasswordOld = (EditText) findViewById(R.id.edtPasswordOld);
        mEdtPasswordNew = (EditText) findViewById(R.id.edtPasswordNew);
        mEdtPasswordAgain = (EditText) findViewById(R.id.edtPasswordAgain);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mBtnUpdate.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disKeyboard();
    }

    public void disKeyboard() {

        if (mEdtPasswordOld.isFocused()) {
            View v = (View) findViewById(mEdtPasswordOld.getId());
            mEdtPasswordOld.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if (mEdtPasswordNew.isFocused()) {
            View v = (View) findViewById(mEdtPasswordNew.getId());
            mEdtPasswordNew.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if (mEdtPasswordAgain.isFocused()) {
            View v = (View) findViewById(mEdtPasswordAgain.getId());
            mEdtPasswordAgain.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }


    @Override
    protected void initControl() {
        super.initControl();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            //getListNationCode();
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
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
                return true;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.MENU_TO_LIST_C1) {
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            int complete = 0;
            if (bundle != null) {
                complete = bundle.getInt(Constants.KEY_COMPLETE);
            }
            if (complete == Constants.KEY_INT_COMPLETE) {
                Intent intent = null;
                intent = new Intent(mActivity, HomeActivity.class);//ReserveActivity
                Bundle bundle1 = new Bundle();
                bundle1.putInt(Constants.KEY_COMPLETE,
                        Constants.KEY_INT_COMPLETE);
                intent.putExtras(bundle1);
                setResult(Constants.MENU_TO_LIST_C1, intent);

                finishToRightToLeft();
            } else {
                finishToRightToLeft();
            }
        }
    }

    @Override
    public void onNetworkChanged() {
        if (mLnErrorConnectNetwork != null) {

            if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
                mLnErrorConnectNetwork.setVisibility(View.GONE);
            } else {
                mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
    }

    private void updatePassword() {

        String url = WebServiceUtils.URL_CHANGE_PASSWORD(Pasgo
                .getInstance().token);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("nguoiDungId", Pasgo.getInstance().userId);
        params.put("matKhauCu", mEdtPasswordOld.getText().toString().trim());
        params.put("matKhauMoi", mEdtPasswordNew.getText().toString().trim());
        JSONObject jsonParams = new JSONObject(params);
        Utils.Log(TAG, "Url: " + url + ", Params: " + jsonParams.toString());
        showDialog(mContext);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.Log(TAG, "response" + response.toString());
                        ToastUtils.showToastSuccess(mContext, getString(R.string.update_password_successfully));
                        finishToRightToLeft();
                        dismissDialog();
                    }

                    @Override
                    public void onError(int maloi) {
                        dismissDialog();
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();
                    }
                });
    }

    private void actionUpdatePassWord() {
        if (StringUtils.isEmpty(mEdtPasswordOld.getText().toString().trim())) {
            ToastUtils.showToastWaring(mContext, R.string.plz_input_password_old);
            return;
        }
        if (StringUtils.isEmpty(mEdtPasswordNew.getText().toString().trim())) {
            ToastUtils.showToastWaring(mContext, R.string.plz_input_password_new);
            return;
        }
        if (mEdtPasswordNew.getText().toString().trim().length() < 6) {
            ToastUtils.showToastWaring(mContext, R.string.password_leng);
            return;
        }
        if (StringUtils.isEmpty(mEdtPasswordAgain.getText().toString().trim())) {
            ToastUtils.showToastWaring(mContext, R.string.plz_input_password_new_again);
            return;
        }
        if (!mEdtPasswordNew.getText().toString().trim().equals(mEdtPasswordAgain.getText().toString().trim())) {
            ToastUtils.showToastWaring(mContext, R.string.password_khong_trung_nhau);
            return;
        }
        updatePassword();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate: {
                actionUpdatePassWord();
                break;
            }
        }
    }
}