package com.onepas.android.pasgo.ui.account;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.LinhVucQuanTam;
import com.onepas.android.pasgo.models.UserInfo;
import com.onepas.android.pasgo.prefs.PastaxiPref;
import com.onepas.android.pasgo.ui.AlbumStorageDirFactory;
import com.onepas.android.pasgo.ui.BaseAlbumDirFactory;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.FroyoAlbumDirFactory;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.PhotoMultipartRequest;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UserInfoActivity extends BaseAppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult> {

    private static final String TAG = "UserInfoActivity";
    private RelativeLayout mRlAreasOfInterest, mRlSex;
    private LinearLayout mLnFacebook;
    private EditText mEdtName, mEdtEmail;
    private TextView mTvBirthDay, mTvLinhVucQuanTam;
    private RadioGroup mRadioGroup;
    private RadioButton mRdoMale, mRdoFeMale;
    private RelativeLayout mRlBirthDay;
    private Button mBtnUpdate;
    private String mName;
    private String mEmail;
    private boolean mGender;
    private String mBirtdDay;
    private String mSLinhVuc = "";
    private ArrayList<LinhVucQuanTam> mLinhVucQuanTamAlls;
    private UserInfo mUserInfo = new UserInfo();
    private String objLinhVucQuanTam;
    private SimpleDraweeView mImgAvata;
    private TextView mTvIdPASGO, mTvNumberPhone;
    protected AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Uri mUriAnh;
    private resizeBitmapAsync resizeBitmapAsync = null;
    private final int  REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String mPathPhoto;
    private final String JPEG_FILE_PREFIX = "IMG_";
    private final String JPEG_FILE_SUFFIX = ".jpg";
    private String mJsonUser;
    private final static String KEY_JSON_USER = "jsonUser";

    private LoginButton loginButton;
    List<String> permissionNeeds= Arrays.asList("public_profile", "email", "user_birthday");
    //facebook callbacks manager
    private CallbackManager mCallbackManager;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.page_user_info);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.user_info));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        initView();
        initControl();

        if (savedInstanceState != null) {
            mName = savedInstanceState.getString(Constants.KEY_username);
            mEmail = savedInstanceState.getString(Constants.KEY_email);
            mBirtdDay = savedInstanceState.getString(Constants.KEY_NGAY_SINH);
            mEdtName.setText(mName);
            mEdtEmail.setText(mEmail);
            mTvBirthDay.setText(mBirtdDay);
            mGender = savedInstanceState.getBoolean(Constants.KEY_SEX);
            mSLinhVuc = savedInstanceState.getString(Constants.KEY_LINH_VUC_QUAN_TAM_TEXT);
            mTvLinhVucQuanTam.setText(mSLinhVuc);
            objLinhVucQuanTam = savedInstanceState.getString(Constants.KEY_LINH_VUC_QUAN_TAM);
            mJsonUser = savedInstanceState.getString(KEY_JSON_USER);
            handlerUpdateUI.sendEmptyMessage(4);

        } else {
            getNguoiDungById();
        }
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
        //instantiate callbacks manager
        mCallbackManager = CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(permissionNeeds);
        loginButton.registerCallback(mCallbackManager, this);
        //
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mRlAreasOfInterest = (RelativeLayout) findViewById(R.id.rlAreasOfInterest);
        mRlSex = (RelativeLayout) findViewById(R.id.rlSex);
        mLnFacebook = (LinearLayout) findViewById(R.id.lnFacebook);
        mEdtName = (EditText) findViewById(R.id.edtName);
        mEdtEmail = (EditText) findViewById(R.id.edtEmail);
        mTvBirthDay = (TextView) findViewById(R.id.tvBirthDay);
        mRdoMale = (RadioButton) findViewById(R.id.rdoMale);
        mRdoFeMale = (RadioButton) findViewById(R.id.rdoFemale);
        mTvLinhVucQuanTam = (TextView) findViewById(R.id.tvLinhVucQuanTam);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mRadioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        mRlBirthDay = (RelativeLayout) findViewById(R.id.rlBirthDay);
        mImgAvata = (SimpleDraweeView) findViewById(R.id.imgAvata);
        mTvIdPASGO = (TextView) findViewById(R.id.tvIdPASGO);
        mTvNumberPhone = (TextView) findViewById(R.id.tvNumberPhone);
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mRlAreasOfInterest.setOnClickListener(this);
        mLnFacebook.setOnClickListener(this);
        mRlSex.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mRlBirthDay.setOnClickListener(this);
        mImgAvata.setOnClickListener(this);
    }

    @Override
    protected void initControl() {
        super.initControl();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
        mRdoMale.setOnClickListener(v -> mRadioGroup.check(mRdoMale.getId()));
        mRdoFeMale.setOnClickListener(v -> mRadioGroup.check(mRdoFeMale.getId()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        mTvIdPASGO.setText(Pasgo.getInstance().ma);
        mTvNumberPhone.setText(Pasgo.getInstance().sdt);
        verifyStoragePermissions(mActivity);
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (permissions.length == 2 &&
                    permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.Log(Pasgo.TAG,"Thành công");

            } else {
                Utils.Log(Pasgo.TAG,"Thất bại");
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.KEY_username, mEdtName.getText().toString());
        bundle.putString(Constants.KEY_email, mEdtEmail.getText().toString());
        bundle.putString(Constants.KEY_NGAY_SINH, mTvBirthDay.getText().toString());
        bundle.putString(Constants.KEY_LINH_VUC_QUAN_TAM_TEXT, mSLinhVuc);
        bundle.putString(Constants.KEY_LINH_VUC_QUAN_TAM, objLinhVucQuanTam);
        bundle.putString(KEY_JSON_USER, mJsonUser);
        if (mRdoMale.isChecked())
            mGender = true;
        else
            mGender = false;
        bundle.putBoolean(Constants.KEY_SEX, mGender);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlAreasOfInterest: {
                if (mLinhVucQuanTamAlls == null || mLinhVucQuanTamAlls.size() == 0 || isFinishing())
                    return;
                onCreateDialog().show();
                break;
            }
            case R.id.lnFacebook: {
                loginFacebook();
                break;
            }
            case R.id.btnUpdate: {
                if (!StringUtils.isEmpty(mPathPhoto)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(
                                Void... params) {
                            uploadImage(mPathPhoto);
                            return null;
                        }
                    }.execute();
                }
                updateUser();
                break;
            }
            case R.id.rlBirthDay: {
                getDatetime();
                break;
            }
            case R.id.imgAvata: {
                selectImage();
                break;
            }
        }
    }
    private void loginFacebook()
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        loginButton.performClick();
    }
    // login facebook
    protected void setFacebookData(final LoginResult loginResult) {
        showDialog(mContext);
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            dismissDialog();
                            Log.i("Response", response.toString());
                            String email = response.getJSONObject().getString("email");
                            String gender = response.getJSONObject().getString("gender");
                            String birdday= response.getJSONObject().getString("birthday");

                            String userId = response.getJSONObject().getString("id");
                            String urlAvata = String.format("https://graph.facebook.com/%s/picture?type=large", userId);


                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");

                            new DownloadFileFromURL().execute(urlAvata);
                            Utils.Log(TAG, urlAvata);
                            mName = firstName +" "+ lastName;
                            mEmail = email;
                            if (gender.equals("male"))
                                mGender = true;
                            else
                                mGender = false;
                            mBirtdDay = birdday;
                            if (!StringUtils.isEmpty(mBirtdDay)) {
                                mBirtdDay = DatehepperUtil.ConvertFormatDateToFormatDate(DatehepperUtil.MMddyyyy, mBirtdDay, DatehepperUtil.yyyyMMdd);
                            }
                            String mToken =loginResult.getAccessToken().getToken();
                            Log.e("mToken","1_ "+ mToken+ " _2");
                            handlerUpdateUI.sendEmptyMessage(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    Handler handlerUpdateUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    mEdtName.setText(mName);
                    mEdtEmail.setText(mEmail);
                    if (mGender) {
                        mRdoMale.setChecked(true);
                        mRdoFeMale.setChecked(false);
                    } else {
                        mRdoMale.setChecked(false);
                        mRdoFeMale.setChecked(true);
                    }
                    if (!StringUtils.isEmpty(mBirtdDay)) {
                        mTvBirthDay.setText(mBirtdDay);
                    }
                    if (!StringUtils.isEmpty(mSLinhVuc)) {
                        mTvLinhVucQuanTam.setText(mSLinhVuc);
                    }
                    if (!StringUtils.isEmpty(Pasgo.getInstance().urlAnh))
                        mImgAvata.setImageURI(Pasgo.getInstance().urlAnh + "&t="
                            + System.currentTimeMillis());
                    else
                        mImgAvata.setBackgroundResource(R.drawable.no_avatar);


                    break;
                }
                case 1: {
                    mName = mUserInfo.getTenNguoiDung();
                    mEmail = mUserInfo.getEmail();
                    mGender = mUserInfo.isGioiTinh();
                    mBirtdDay = mUserInfo.getNgaySinh();
                    PastaxiPref pref = new PastaxiPref(mContext);
                    pref.putUserName(mName);
                    pref.putEmail(mEmail);
                    pref.putUrlAnh(mUserInfo.getUrlAnh());
                    Pasgo.getInstance().urlAnh = mUserInfo.getUrlAnh();
                    Pasgo.getInstance().username = mName;
                    Pasgo.getInstance().email = mEmail;

                    ArrayList<LinhVucQuanTam> list = mUserInfo.getLinhVucQuanTams();
                    if (list.size() > 0) {
                        mSLinhVuc = "";
                        for (LinhVucQuanTam item : list) {
                            if (StringUtils.isEmpty(mSLinhVuc))
                                mSLinhVuc += item.getTen();
                            else
                                mSLinhVuc = mSLinhVuc + ", " + item.getTen();
                        }
                    }
                    handlerUpdateUI.sendEmptyMessage(0);
                    break;
                }
                case 2: {
                    try {
                        if (StringUtils.isEmpty(objLinhVucQuanTam)) return;
                        JSONObject jsonObject = new JSONObject(objLinhVucQuanTam);
                        mLinhVucQuanTamAlls = ParserUtils.getAlLinhVucQuanTams(jsonObject);

                        ArrayList<LinhVucQuanTam> listFromUser = mUserInfo.getLinhVucQuanTams();
                        // so sánh danh sách all với danh sách của user trả về để setCheck = true
                        for (int i = 0; i < mLinhVucQuanTamAlls.size(); i++) {
                            for (LinhVucQuanTam itemFromUser : listFromUser) {
                                if (mLinhVucQuanTamAlls.get(i).getId() == itemFromUser.getId()) {
                                    mLinhVucQuanTamAlls.get(i).setCheck(true);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    if (!StringUtils.isEmpty(mSLinhVuc)) {
                        mTvLinhVucQuanTam.setText(mSLinhVuc);
                    } else
                        mTvLinhVucQuanTam.setText(getString(R.string.areas_of_interest_select));
                    break;
                }
                case 4: {
                    try {
                        if (StringUtils.isEmpty(mJsonUser)) {
                            getNguoiDungById();
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(mJsonUser);
                        mUserInfo = ParserUtils.getUserInfo(jsonObject);
                        handlerUpdateUI.sendEmptyMessage(0);
                        handlerUpdateUI.sendEmptyMessage(2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    };

    private void getDatetime() {
        final DatePicker datePicker = new DatePicker(this);
        Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setCalendarViewShown(false);
        }
        if (!isFinishing())
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.input_birthday))
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        String date = String.format("%s/%s/%s", DatehepperUtil.formatDate(datePicker.getYear())
                                , DatehepperUtil.formatDate((datePicker.getMonth() + 1)), DatehepperUtil.formatDate(datePicker.getDayOfMonth()));
                        mTvBirthDay.setText(date);
                    })
                    .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> Utils.Log("Picker", "Cancelled!")).setView(datePicker).show();
    }

    public ArrayList<LinhVucQuanTam> cloneList(ArrayList<LinhVucQuanTam> dogList) {
        ArrayList<LinhVucQuanTam> clonedList = new ArrayList<LinhVucQuanTam>(dogList.size());
        for (LinhVucQuanTam dog : dogList) {
            clonedList.add(new LinhVucQuanTam(dog.getId(), dog.getTen(), dog.getMa(), dog.isCheck()));
        }
        return clonedList;
    }

    private ArrayList<LinhVucQuanTam> mLinhVucQuanTamPopUp;

    public Dialog onCreateDialog() {
        mLinhVucQuanTamPopUp = cloneList(mLinhVucQuanTamAlls);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        String[] arrLinhVucQuanTam = new String[mLinhVucQuanTamAlls.size()];
        boolean[] arrIsCheck = new boolean[mLinhVucQuanTamAlls.size()];
        for (int i = 0; i < mLinhVucQuanTamAlls.size(); i++) {
            arrLinhVucQuanTam[i] = mLinhVucQuanTamAlls.get(i).getTen();
            arrIsCheck[i] = mLinhVucQuanTamAlls.get(i).isCheck();
        }

        builder.setTitle(R.string.areas_of_interest)
                .setMultiChoiceItems(arrLinhVucQuanTam, arrIsCheck,
                        (dialog, which, isChecked) -> mLinhVucQuanTamPopUp.get(which).setCheck(isChecked))
                .setPositiveButton(R.string.ok, (dialog, id) -> updateLinhVucQuanTam())
        ;

        return builder.create();
    }

    private void updateUser() {
        boolean isGender = true;
        if (mRdoFeMale.isChecked())
            isGender = false;
        if (!StringUtils.isEmpty(mEdtEmail.getText().toString().trim()) && !StringUtils.checkEmail(mEdtEmail.getText().toString().trim())) {
            ToastUtils.showToast(mContext, R.string.email_kott);
            return;
        }
        String birthDay = mTvBirthDay.getText().toString().trim();
        if (StringUtils.isEmpty(birthDay) || birthDay.equals(getString(R.string.input_birthday))) {
            birthDay = "";
        }
        if (!StringUtils.isEmpty(birthDay)) {
            String[] dates = birthDay.split("/");
            if (dates.length == 3) {
                try {
                    int yearBirth = Integer.parseInt(dates[0]);
                    int yearCurrent = DatehepperUtil.getYearCurrent(mContext);
                    if (yearBirth >= yearCurrent || yearBirth + 120 < yearCurrent) {
                        ToastUtils.showToast(mContext, R.string.birth_valid);
                        return;
                    }
                } catch (NumberFormatException nfe) {
                }
            }
        }
        String url = WebServiceUtils.URL_UPDATE_USER(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("tenNguoiDung", mEdtName.getText().toString().trim());
            jsonParams.put("email", mEdtEmail.getText().toString().trim());
            jsonParams.put("ngaySinh", birthDay);
            jsonParams.put("gioiTinh", isGender);

        } catch (Exception e) {
        }
        Utils.Log(TAG, jsonParams.toString());
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject json) {
                            if (json != null) {
                                Utils.Log(TAG, json.toString());
                                ToastUtils.showToast(mContext, getString(R.string.update_user_successfully));
                                PastaxiPref pref = new PastaxiPref(mContext);
                                Pasgo.getInstance().username = mEdtName.getText().toString().trim();
                                pref.putUserName(Pasgo.getInstance().username);
                                Pasgo.getInstance().email = mEdtEmail.getText().toString().trim();
                                pref.putEmail(Pasgo.getInstance().email);

                            }
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
    }

    private void getNguoiDungById() {
        String url = WebServiceUtils.URL_GET_NGUOIDUNGPASGO_BY_ID(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
        } catch (Exception e) {
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject json) {
                            if (json != null) {
                                mJsonUser = json.toString();
                                mUserInfo = ParserUtils.getUserInfo(json);
                                handlerUpdateUI.sendEmptyMessage(1);
                                getLinhVucQuanTamAll();
                            }
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
    }

    private void getLinhVucQuanTamAll() {
        String url = WebServiceUtils.URL_GET_LINH_VUC_QUAN_TAM_ALL(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject json) {
                            if (json != null) {
                                Utils.Log(TAG, json.toString());
                                objLinhVucQuanTam = json.toString();
                                handlerUpdateUI.sendEmptyMessage(2);
                            }
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
    }

    private void updateLinhVucQuanTam() {
        String linhVucQuanTam = "";
        for (LinhVucQuanTam item : mLinhVucQuanTamPopUp) {
            if (item.isCheck()) {
                if (StringUtils.isEmpty(linhVucQuanTam))
                    linhVucQuanTam += item.getId();
                else
                    linhVucQuanTam = linhVucQuanTam + "|" + item.getId();
            }
        }
        Utils.Log(TAG, linhVucQuanTam);
        String url = WebServiceUtils.URL_UPDATE_LINH_VUC_QUAN_TAM(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("linhVucQuanTamIds", linhVucQuanTam);
        } catch (Exception e) {
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                    new Pasgo.PWListener() {

                        @Override
                        public void onResponse(JSONObject json) {
                            if (json != null) {
                                Utils.Log(TAG, json.toString());
                                mLinhVucQuanTamAlls = mLinhVucQuanTamPopUp;
                                if (mLinhVucQuanTamAlls.size() > 0) {
                                    mSLinhVuc = "";
                                    for (LinhVucQuanTam item : mLinhVucQuanTamAlls) {
                                        if (item.isCheck()) {
                                            if (StringUtils.isEmpty(mSLinhVuc))
                                                mSLinhVuc += item.getTen();
                                            else
                                                mSLinhVuc = mSLinhVuc + ", " + item.getTen();
                                        }
                                    }
                                }
                                handlerUpdateUI.sendEmptyMessage(3);
                            }
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.KEY_UPDATE_PASSWORD_NEW:
                    getNguoiDungById();
                    break;
                case REQUEST_CAMERA:
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    if (bitmap == null) return;
                    File file = saveToFile(bitmap);
                    if (file != null) {
                        mUriAnh = Uri.fromFile(file);
                        resizeBitmapAsync = new resizeBitmapAsync(mActivity, bitmap);
                        resizeBitmapAsync.execute();
                    }
                    break;
                case SELECT_FILE:
                    mUriAnh = data.getData();
                    mPathPhoto = getPath(mUriAnh, mActivity);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    final Bitmap bitmap1 = BitmapFactory.decodeFile(mPathPhoto, options);
                    if (bitmap1 != null) {
                        resizeBitmapAsync = new resizeBitmapAsync(mActivity, bitmap1);
                        resizeBitmapAsync.execute();
                    }
                    break;
                default:
                    mCallbackManager.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }

    }

    @Override
    public void onStop() {
        if (this.resizeBitmapAsync != null
                && this.resizeBitmapAsync.getStatus() == Status.RUNNING)
            this.resizeBitmapAsync.cancel(true);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (resizeBitmapAsync != null)
            resizeBitmapAsync.cancel(true);
        resizeBitmapAsync = null;

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        setFacebookData(loginResult);
    }

    @Override
    public void onCancel() {
        Log.e(Pasgo.TAG, "facebook login canceled");
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(Pasgo.TAG, "facebook login error");
    }
    //region từ phần này trở xuống là xử lý về ảnh
    public class resizeBitmapAsync extends AsyncTask<Void, Bitmap, Bitmap> {

        Bitmap bitmap;
        Context context;

        public resizeBitmapAsync(Context context, Bitmap bitmap) {
            this.bitmap = bitmap;
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap resizedBitmap;
            resizedBitmap = resizeBitmap(bitmap);
            return resizedBitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap resizedBitmap) {
            super.onPostExecute(resizedBitmap);
            resizedBitmap = rotateImageIfRequired(mActivity, resizedBitmap,
                    mUriAnh);
            mImgAvata.setBackgroundResource(R.drawable.circle);
            mImgAvata.setImageBitmap(resizedBitmap);
            resizeBitmapAsync = null;
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        int w = 320;
        int h = 320;
        float factorH = h / (float) bitmap.getHeight();
        float factorW = w / (float) bitmap.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        Bitmap bm = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * factorToUse),
                (int) (bitmap.getHeight() * factorToUse), false);
        return bm;
    }

    private Bitmap rotateImageIfRequired(Context context, Bitmap img,
                                         Uri selectedImage) {
        int rotation = getCameraPhotoOrientation(context, selectedImage, mPathPhoto);// getRotation(context, selectedImage);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri,
                                         String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_library),
                getString(R.string.huy)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.change_avata));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_photo))) {
                    capturePhoto();
                } else if (items[item].equals(getString(R.string.choose_from_library))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.select_file)),
                            SELECT_FILE);
                } else if (items[item].equals(getString(R.string.huy))) {
                    dialog.dismiss();
                }
            }
        });
        if (!isFinishing())
            builder.show();
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //chup ảnh
    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    private File saveToFile(Bitmap bitmap) {
        File file = null;
        try {
            file = createImageFile();
            this.mPathPhoto = file.getAbsolutePath();
            FileOutputStream stream = new FileOutputStream(mPathPhoto);
            bitmap.compress(CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {

            this.mPathPhoto = null;
            Log.e(TAG, "Could not save" + e.toString());
            e.printStackTrace();
        }
        return file;
    }

    private File createImageFile() throws IOException {
        String imageFileName = JPEG_FILE_PREFIX + "avatafabook_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
                albumF);

        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory
                    .getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Utils.Log("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name),
                    "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private String getAlbumName() {
        return Constants.DATABASE_ROOT;
    }

    //
    private void uploadImage(String pathPhoto) {

        String url = String.format(
                WebServiceUtils.URL_UPLOAD_SERVICE(Pasgo.getInstance().token),
                Pasgo.getInstance().userId);
        File file;
        file = new File(pathPhoto);
        HashMap<String, String> params = new HashMap<String, String>();
        PhotoMultipartRequest mr = new PhotoMultipartRequest(url, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Utils.Log(TAG, response);
                JSONObject jsonObject = null;
                try {
                    Pasgo.getInstance().isUpdateImage = true;
                    jsonObject = new JSONObject(response);
                    JSONObject item = ParserUtils.getJsonObject(jsonObject, "Item");
                    Pasgo.getInstance().urlAnh = ParserUtils.getStringValue(item, "Value");
                    PastaxiPref pref = new PastaxiPref(mContext);
                    pref.putUrlAnh(Pasgo.getInstance().urlAnh);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }, file, params);

        Volley.newRequestQueue(this).add(mr);
    }

    // download file from url facebook
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mImgAvata.setBackgroundResource(R.drawable.circle);
            mImgAvata.setImageResource(R.drawable.image_loading);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream("/sdcard/facebookavata.jpg");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            mPathPhoto = Environment.getExternalStorageDirectory().toString() + "/facebookavata.jpg";
            // setting downloaded into image view
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            final Bitmap bitmap = BitmapFactory.decodeFile(mPathPhoto, options);
            if (bitmap != null) {
                resizeBitmapAsync = new resizeBitmapAsync(mActivity, bitmap);
                resizeBitmapAsync.execute();
            }
        }
    }
    //endregion
}