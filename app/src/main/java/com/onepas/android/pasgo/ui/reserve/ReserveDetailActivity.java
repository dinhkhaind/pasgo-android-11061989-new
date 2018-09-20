package com.onepas.android.pasgo.ui.reserve;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.DialogUtils;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.ToastUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ReserveDetailActivity extends BaseAppCompatActivity implements
        OnClickListener {
    private String mTenDoiTacKM;
    private String mThoiGianDonXe;
    private TextView mTvSoNguoi, mTvSoNguoiClild;
    private TextView mTvDay, mTvThu, mTvTime, mTvTen;
    private LinearLayout mLnSoNguoiLeft, mLnSoNguoiRight, mLnSoNguoiCenter;
    private LinearLayout mLnSoNguoiChildLeft, mLnSoNguoiChildRight, mLnSoNguoiChildCenter;
    private LinearLayout mLnNgayThangLeft, mLnNgayThangRight, mLnNgayThangCenter;
    private LinearLayout mLnThoiGianLeft, mLnThoiGianRight, mLnThoiGianCenter;
    private EditText mEdtGhiChu;
    private ImageView mImgCall;
    private Dialog mDialogCheckInComment;
    private TextView mTvAddress;

    private String mDatXeId = "";
    private String mGiamGiaId = "";
    private String mGiamGiaMa = "";
    private String mGiamGia = "";

    private String mNhomCNDoiTacId;
    private Button mBtnReserve;
    private int mTrangThai;
    private int mDay, mMonth, mYear, mHour, mMinute;
    private int mSoNguoiDen = 1;
    private int mSoNguoiDenChild = 0;
    private static final int KEY_UPDATE_SO_NGUOI = 0;
    private static final int KEY_UPDATE_DAY = 1;
    private static final int KEY_UPDATE_TIME = 2;
    private static final int KEY_UPDATE_SO_NGUOI_CHILD = 3;
    private boolean mCheckClick=false;
    private static final int PERMISSION_REQUEST_CODE_PHONE =1;
    private String mAddress;
    private Dialog dialogCheckInExist;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, mGiamGiaId);
        outState.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, mGiamGiaMa);
        outState.putString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, mGiamGia);
        outState.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, mNhomCNDoiTacId);
        outState.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, mTenDoiTacKM);
        outState.putString(Constants.BUNDLE_KEY_DIA_CHI, mAddress);
        outState.putInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, mTrangThai);
        Utils.setTextViewHtml(mTvTen,mTenDoiTacKM);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGiamGiaId = savedInstanceState.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID);
        mGiamGiaMa = savedInstanceState.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA);
        mGiamGia = savedInstanceState.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA);
        mNhomCNDoiTacId = savedInstanceState.getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID);
        mTenDoiTacKM = savedInstanceState.getString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM);
        mTrangThai = savedInstanceState.getInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_reserve_detail);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> {
            finishToRightToLeft();
        });
        mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
        mImgCall = (ImageView)findViewById(R.id.call_img);
        mImgCall.setOnClickListener(this);
        initView();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mNhomCNDoiTacId = extra.getString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, "");
            mTenDoiTacKM = extra.getString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM);
            mTrangThai = extra.getInt(Constants.BUNDLE_KEY_TRANG_THAI_DOI_TAC_KM, mTrangThai);
            mGiamGiaId = extra.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_ID, "");
            mGiamGiaMa = extra.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_MA, "");
            mGiamGia = extra.getString(Constants.BUNDLE_KEY_MA_DAT_CHO_GIAM_GIA, "");
            mAddress = extra.getString(Constants.BUNDLE_KEY_DIA_CHI, "");
            Utils.setTextViewHtml(mTvTen,mTenDoiTacKM);
        }
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
        mLnErrorConnectNetwork.setVisibility(View.GONE);
        mBtnReserve = (Button) findViewById(R.id.reserve_btn);
        mBtnReserve.setOnClickListener(this);
        mTvSoNguoi = (TextView) findViewById(R.id.so_nguoi_tv);
        mTvSoNguoiClild = (TextView) findViewById(R.id.so_nguoi_child_tv);

        mTvDay = (TextView) findViewById(R.id.day_tv);
        mTvThu = (TextView) findViewById(R.id.thu_tv);
        mTvTime = (TextView) findViewById(R.id.time_tv);
        mEdtGhiChu = (EditText) findViewById(R.id.ghi_chu_edt);
        mTvTen = (TextView) findViewById(R.id.ten_tv);
        mLnSoNguoiLeft = (LinearLayout) findViewById(R.id.so_nguoi_arr_left_ln);
        mLnSoNguoiRight = (LinearLayout) findViewById(R.id.so_nguoi_arr_right_ln);
        mLnSoNguoiCenter = (LinearLayout) findViewById(R.id.so_nguoi_child_center_ln);
        mLnSoNguoiChildLeft = (LinearLayout) findViewById(R.id.so_nguoi_child_arr_left_ln);
        mLnSoNguoiChildRight = (LinearLayout) findViewById(R.id.so_nguoi_child_arr_right_ln);
        mLnSoNguoiChildCenter = (LinearLayout) findViewById(R.id.so_nguoi_center_ln);

        mLnNgayThangLeft = (LinearLayout) findViewById(R.id.ngay_thang_arr_left_ln);
        mLnNgayThangRight = (LinearLayout) findViewById(R.id.ngay_thang_arr_right_ln);
        mLnNgayThangCenter = (LinearLayout) findViewById(R.id.ngay_thang_center_ln);
        mLnThoiGianLeft = (LinearLayout) findViewById(R.id.thoi_gian_arr_left_ln);
        mLnThoiGianRight = (LinearLayout) findViewById(R.id.thoi_gian_arr_right_ln);
        mLnThoiGianCenter = (LinearLayout) findViewById(R.id.thoi_gian_center_ln);
        mTvAddress = (TextView)findViewById(R.id.address_tv);
        mLnSoNguoiLeft.setOnClickListener(this);
        mLnSoNguoiRight.setOnClickListener(this);
        mLnSoNguoiCenter.setOnClickListener(this);
        mLnSoNguoiChildLeft.setOnClickListener(this);
        mLnSoNguoiChildRight.setOnClickListener(this);
        mLnSoNguoiChildCenter.setOnClickListener(this);
        mLnNgayThangLeft.setOnClickListener(this);
        mLnNgayThangRight.setOnClickListener(this);
        mLnNgayThangCenter.setOnClickListener(this);
        mLnThoiGianLeft.setOnClickListener(this);
        mLnThoiGianRight.setOnClickListener(this);
        mLnThoiGianCenter.setOnClickListener(this);
    }

    @Override
    protected void initControl() {
        super.initControl();
        if(Pasgo.getInstance().prefs.getLanguage().toLowerCase().equals(Constants.LANGUAGE_VIETNAM.toLowerCase()))
        {
            mImgCall.setImageResource(R.drawable.goi_dat_xe_vn);
        }else {
            mImgCall.setImageResource(R.drawable.goi_dat_xe_eng);
        }
        // lấy thời gian hiện tại
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // làm trong số phút 0,30,60
        c = DatehepperUtil.getTimeAdd(mYear, mMonth, mDay, mHour, mMinute, 30);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_DAY);
        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_TIME);
        mTvAddress.setText(mAddress);
    }

    private void checkInClick() {
        if(!mCheckClick) {
            mCheckClick = true;
            verifyCheckInBefore();
        }
    }

    private void verifyCheckInBefore() {

        String url = WebServiceUtils
                .URL_VERIFY_CHECKIN_BEFORE(Pasgo.getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            if (!checkTimeBeforBooking()) {
                mCheckClick = false;
                return;
            }
            if (StringUtils.isEmpty(mTvSoNguoi.getText().toString())) {
                DialogUtils.alert(mActivity, getString(R.string.thong_bao), getString(R.string.plz_nhap_so_nguoi_den));
                mCheckClick = false;
                return;
            }
            try {
                mSoNguoiDen = Integer.valueOf(mTvSoNguoi.getText().toString());
            } catch (Exception e) {
                mSoNguoiDen = 0;
            }
            if (mSoNguoiDen < 1 || mSoNguoiDen > 200) {
                DialogUtils.alert(mActivity, getString(R.string.thong_bao), getString(R.string.plz_so_nguoi_den_lon_hon));
                mCheckClick = false;
                return;
            }

            jsonParams.put("nhomCNDoiTacId", mNhomCNDoiTacId);
            jsonParams.put("khachHangId", Pasgo.getInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        int maloi = ParserUtils.getIntValueResponse(response,
                                "MaLoi");
                        if (maloi == Constants.KEY_CHECKIN_MAX) {
                            mCheckClick = false;
                            DialogUtils.alert(mActivity,getString(R.string.thong_bao), getString(R.string.gioi_han_check_in));
                        } else if (maloi == Constants.KEY_SUCCESS_RESPONSE) {
                            verifyCheckInAfter();
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        mCheckClick = false;
                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        mCheckClick = false;
                    }
                });
    }

    @Override
    public void onNetworkChanged() {

        if (mLnErrorConnectNetwork != null) {

            if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable())
                mLnErrorConnectNetwork.setVisibility(View.GONE);
            else
                mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkTimeBeforBooking() {
        boolean check = true;
        Long dateCurrent = DatehepperUtil.convertDatetimeToLongDate(
                DatehepperUtil.getCurrentDate(DatehepperUtil.yyyyMMddHHmmss),
                DatehepperUtil.yyyyMMddHHmmss);
        Long dateSelect = DatehepperUtil.convertDatetimeToLongDate(
                mThoiGianDonXe, DatehepperUtil.yyyyMMddHHmmss);

        if (dateSelect + 60000 < (dateCurrent + 15 * 60000)) {
            ToastUtils.showToast(mContext, R.string.tb_thoi_gian_checkIn);
            check = false;
        } else
            check = true;
        return check;
    }

    private void verifyCheckInAfter() {
        String url = WebServiceUtils
                .URL_VERIFY_CHECKIN_AFTER(Pasgo.getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        int soNguoiLon = 0;
        int soTreEm = 0;
        try {
            soNguoiLon = Integer.parseInt(mTvSoNguoi.getText().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        try {
            soTreEm = Integer.parseInt(mTvSoNguoiClild.getText().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        try {
            jsonParams.put("nhomCNDoiTacId", mNhomCNDoiTacId);
            jsonParams.put("thoiGian", mThoiGianDonXe);
            jsonParams.put("trangThai", mTrangThai);
            jsonParams.put("giamGiaId", mGiamGiaId);
            jsonParams.put("soNguoi", soNguoiLon + soTreEm);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        int maloi = ParserUtils.getIntValueResponse(response,
                                "MaLoi");
                        JSONObject jsonItem = ParserUtils.getJsonObject(response, "Item");
                        String noiDung = ParserUtils.getStringValue(jsonItem, "NoiDung");
                        // lỗi quá giờ phục vụ Hoặc quá số người phục vụ
                        if (maloi == Constants.KEY_CHECKIN_NGOAI_GIO_PHUC_VU) {
                            mCheckClick = false;
                            String noiDungAlert;
                            noiDungAlert =  ParserUtils.getStringValue(response,"ThongDiep");
                            DialogUtils.alert(mActivity,getString(R.string.dat_cho_that_bai_title) ,noiDungAlert);
                        }else{
                            if (mDialogCheckInComment != null && mDialogCheckInComment.isShowing()) {
                                mDialogCheckInComment.cancel();
                                mDialogCheckInComment.dismiss();
                            }
                            String giamGiaFormat = "";
                            if (!StringUtils.isEmpty(mGiamGia))
                                giamGiaFormat = Utils.formatMoney(mContext, mGiamGia) + " " + getString(R.string.vnd_up);
                            // get content
                            String noiDungMacdinh = String.format(getString(R.string.checkin_xacnhan_1), mTenDoiTacKM);
                            String noiDungMacdinh2= String.format(getString(R.string.checkin_xacnhan_2), mTenDoiTacKM);

                            // check content from server
                            //if(!StringUtils.isEmpty(noiDung))
                            //    noiDungMacdinh2 = noiDung;
                            // check discount
                            if (!StringUtils.isEmpty(giamGiaFormat))
                                noiDungMacdinh2 += String.format(getString(R.string.checkin_xacnhan_3), giamGiaFormat);
                            noiDungMacdinh2 = StringUtils.formatStringNewLine(noiDungMacdinh2);
                            //show dialog
                            mDialogCheckInComment = DialogUtils.showYesNoDialogCheckIn(mActivity, noiDungMacdinh, noiDungMacdinh2
                                    , mTvSoNguoi.getText().toString()
                                    , mTvSoNguoiClild.getText().toString()
                                    , mTvDay.getText().toString()
                                    , mTvTime.getText().toString(),
                                    R.string.confirm, R.string.huy
                                    , new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mDialogCheckInComment != null && mDialogCheckInComment.isShowing()) {
                                                mDialogCheckInComment.cancel();
                                                mDialogCheckInComment.dismiss();
                                            }
                                        }
                                    }, new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mDialogCheckInComment != null && mDialogCheckInComment.isShowing()) {
                                                mDialogCheckInComment.cancel();
                                                mDialogCheckInComment.dismiss();
                                            }
                                            mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setCheckIn();
                                                }
                                            }, 2000);
                                        }
                                    });
                            if (mDialogCheckInComment != null && !mDialogCheckInComment.isShowing() && !isFinishing())
                                mDialogCheckInComment.show();
                            mCheckClick = false;
                        }

                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        mCheckClick = false;
                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                        mCheckClick = false;
                    }
                });
    }

    private void setCheckIn() {
        String url = WebServiceUtils
                .URL_ADD_CHECKIN(Pasgo.getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nhomCNDoiTacId", mNhomCNDoiTacId);
            jsonParams.put("khachHangId", Pasgo.getInstance().userId);
            jsonParams.put("soNguoiDen", mTvSoNguoi.getText().toString());
            jsonParams.put("soTreEm", mTvSoNguoiClild.getText().toString());
            jsonParams.put("thoiGianDen", mThoiGianDonXe);
            jsonParams.put("giamGiaId", mGiamGiaId);
            jsonParams.put("maGiamGia", mGiamGiaMa);
            jsonParams.put("ghiChu", mEdtGhiChu.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject objItem = ParserUtils.getJsonObject(
                                response, "Item");
                        boolean success = ParserUtils.getBooleanValue(objItem,
                                "success");
                        if (success) {
                            mDatXeId = ParserUtils.getStringValue(objItem, "datXeId");
                            handleReserveUI.sendEmptyMessage(0);
                        } else {
                            if (!isFinishing())
                            {
                                if(dialogCheckInExist==null)
                                    dialogCheckInExist = DialogUtils.showYesNoDialog(mContext,String.format(getString(R.string.check_in_exist),mTenDoiTacKM), R.string.view_detail, R.string.huy
                                        , v -> {
                                            dialogCheckInExist.dismiss();
                                        }, v -> {
                                                gotoActivity(mContext, ReservedHistoryActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            dialogCheckInExist.dismiss();
                                        });
                                if(dialogCheckInExist!=null && !dialogCheckInExist.isShowing())
                                    dialogCheckInExist.show();
                            }
                        }
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);

                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }

    private int checkKhungGioDatCho()
    {
        int khungGio=2;// 1: trước 8h, 2: từ 8h - 20.30, 3: sau 20.30

        Calendar currentDate = Calendar.getInstance();
        int currentYear =  currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        String date8h = String.format("%s/%s/%s %s:%s:00",currentYear, DatehepperUtil.formatDate(currentMonth+1),DatehepperUtil.formatDate(currentDay)
                ,"08","00");
        String time20h30 = String.format("%s/%s/%s %s:%s:00",currentYear,DatehepperUtil.formatDate(currentMonth+1),DatehepperUtil.formatDate(currentDay)
                ,"20","30");
        Long longDateCurrent = DatehepperUtil.getTimeInMillis();
        Long longDate8h = DatehepperUtil.convertDatetimeToLongDate(date8h, DatehepperUtil.yyyyMMddHHmmss);
        Long longDate20h30 = DatehepperUtil.convertDatetimeToLongDate(time20h30, DatehepperUtil.yyyyMMddHHmmss);
        if(longDate8h <= longDateCurrent && longDateCurrent <= longDate20h30)
        {
            khungGio = 2;
        }else{
            if(longDateCurrent < longDate8h)
                khungGio = 1;
            else
                khungGio = 3;
        }
        Utils.Log(Pasgo.TAG,"khungGio _"+khungGio);
        Utils.Log(Pasgo.TAG,"longDateCurrent _"+longDateCurrent);
        Utils.Log(Pasgo.TAG,"longDate8h _"+longDate8h);
        Utils.Log(Pasgo.TAG,"longDate20h30 _"+longDate20h30);
        return khungGio;
    }

    private final Handler handleReserveUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isFinishing()) return;
                    String[] arrCheckInSuccess1 = getResources().getStringArray(
                            R.array.check_in_success1);
                    if(checkKhungGioDatCho() == 2) {
                        arrCheckInSuccess1[0] = String.format(arrCheckInSuccess1[0], StringUtils.fromHtml(mTenDoiTacKM)+" - "+ StringUtils.fromHtml(mAddress));
                        arrCheckInSuccess1[1] = String.format(arrCheckInSuccess1[1], Pasgo.getInstance().sdt);
                    }else//Từ 20h30 đến 8h sáng hôm sau
                    {
                        arrCheckInSuccess1 = getResources().getStringArray(
                                R.array.check_in_success1_khunggio1va3);
                        Calendar cDateView;
                        if(checkKhungGioDatCho() == 1)
                            cDateView = DatehepperUtil.getCalenDarDate(0);
                        else
                            cDateView = DatehepperUtil.getCalenDarDate(1);
                        int year = cDateView.get(Calendar.YEAR);
                        int month = cDateView.get(Calendar.MONTH);
                        int day = cDateView.get(Calendar.DAY_OF_MONTH);
                        String thoiGian = String.format("%s/%s/%s",DatehepperUtil.formatDate(day),DatehepperUtil.formatDate(month+1), year);
                        arrCheckInSuccess1[0] = String.format(arrCheckInSuccess1[0], StringUtils.fromHtml(mTenDoiTacKM)+" - "+ StringUtils.fromHtml(mAddress));
                        arrCheckInSuccess1[1] = String.format(arrCheckInSuccess1[1], Pasgo.getInstance().sdt,thoiGian);
                    }
                    String checkInSuccess2 = getString(R.string.check_in_success2);

                    if (!isFinishing())
                        showDialog(mActivity,getString(R.string.dat_cho_thanh_cong_title),arrCheckInSuccess1, StringUtils.formatStringNewLine(checkInSuccess2), R.string.dong, (dialogInterface, i) -> {
                            setResult(RESULT_OK);
                            finishToRightToLeft();
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            dialogInterface.dismiss();
                        });

                    //pubChannelOnepasCheckIn(mDatXeId, mNhomCNDoiTacId);
                    //pubChannelParnerCheckIn(mDatXeId, mNhomCNDoiTacId);
                    //handleCheckInChange.sendEmptyMessage(0);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private  void showDialog(Activity activity, String title, String[] arrCheckInSuccess1, String message2,
                                            int OkTextId, final DialogInterface.OnClickListener onOKClick) {
        if (activity != null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setCancelable(false);
            alertDialog.setIcon(R.drawable.app_pastaxi);
            View dialog = inflater.inflate(R.layout.layout_reserver_done, null);
            alertDialog.setView(dialog).setPositiveButton(OkTextId, onOKClick);
            TextView tvThongBaoPopup1=(TextView) dialog.findViewById(R.id.tvThongBaoPopup1);
            TextView tvThongBaoPopup12=(TextView) dialog.findViewById(R.id.tvThongBaoPopup12);
            TextView tvThongBaoPopup13=(TextView) dialog.findViewById(R.id.tvThongBaoPopup13);
            TextView tvThongBaoPopup14=(TextView) dialog.findViewById(R.id.tvThongBaoPopup14);
            TextView tvThongBaoPopup15=(TextView) dialog.findViewById(R.id.tvThongBaoPopup15);
            Utils.setTextViewHtml(tvThongBaoPopup1,StringUtils.formatStringNewLine(arrCheckInSuccess1[0]));
            Utils.setTextViewHtml(tvThongBaoPopup12,StringUtils.formatStringNewLine(arrCheckInSuccess1[1]));
            Utils.setTextViewHtml(tvThongBaoPopup13,StringUtils.formatStringNewLine(arrCheckInSuccess1[2]));
            Utils.setTextViewHtml(tvThongBaoPopup14,StringUtils.formatStringNewLine(arrCheckInSuccess1[3]));
            Utils.setTextViewHtml(tvThongBaoPopup15,StringUtils.formatStringNewLine(arrCheckInSuccess1[4]));

            TextView tvThongBaoPopup2=(TextView) dialog.findViewById(R.id.tvThongBaoPopup2);
            Utils.setTextViewHtml(tvThongBaoPopup2,message2);
            TextView tvTitle = (TextView)dialog.findViewById(R.id.tvTitle);
            Utils.setTextViewHtml(tvTitle,title);
            alertDialog.show();
        }
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
    public void onUpdateMapAfterUserInterection() {
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.reserve_btn:
                checkInClick();
                break;
            case R.id.so_nguoi_arr_left_ln:
                if(mCheckClick) return;
                if (mSoNguoiDen > 1) {
                    mSoNguoiDen--;
                    handleUpdateUI.sendEmptyMessage(KEY_UPDATE_SO_NGUOI);
                }
                break;
            case R.id.so_nguoi_arr_right_ln:
                if(mCheckClick) return;
                if (mSoNguoiDen < 200) {
                    mSoNguoiDen++;
                    handleUpdateUI.sendEmptyMessage(KEY_UPDATE_SO_NGUOI);
                }
                break;
            case R.id.so_nguoi_center_ln:
                if(mCheckClick) return;
                dialogSoNguoi(false);
                break;

            case R.id.so_nguoi_child_arr_left_ln:
                if(mCheckClick) return;
                if (mSoNguoiDenChild > 0) {
                    mSoNguoiDenChild--;
                    handleUpdateUI.sendEmptyMessage(KEY_UPDATE_SO_NGUOI_CHILD);
                }
                break;
            case R.id.so_nguoi_child_arr_right_ln:
                if(mCheckClick) return;
                if (mSoNguoiDenChild < 200) {
                    mSoNguoiDenChild++;
                    handleUpdateUI.sendEmptyMessage(KEY_UPDATE_SO_NGUOI_CHILD);
                }
                break;
            case R.id.so_nguoi_child_center_ln:
                if(mCheckClick) return;
                dialogSoNguoi(true);
                break;

            case R.id.ngay_thang_arr_left_ln:
                if(mCheckClick) return;
                ngayThangLeft();
                break;
            case R.id.ngay_thang_arr_right_ln:
                if(mCheckClick) return;
                ngayThangRight();
                break;
            case R.id.ngay_thang_center_ln:
                if(mCheckClick) return;
                dialogNgayThang();
                break;
            case R.id.thoi_gian_arr_left_ln:
                if(mCheckClick) return;
                minuteLeft();
                break;
            case R.id.thoi_gian_arr_right_ln:
                if(mCheckClick) return;
                minuteRight();
                break;
            case R.id.thoi_gian_center_ln:
                if(mCheckClick) return;
                dialogThoiGian();
                break;
            case R.id.call_img:
                if(mCheckClick) return;
                checkPemissionCall();
                break;
            default:
                break;
        }
    }
    protected void gotoPhoneCallPage() {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.SDT_TONG_DAI));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(i);
    }
    private void ngayThangLeft()
    {
        Calendar cDateView = DatehepperUtil.getWeekDayAdd(mYear,mMonth,mDay,0);
        Calendar currentDate = Calendar.getInstance();
        if (DatehepperUtil.isSameDay(currentDate,cDateView))
            return;
        Calendar cDateLeft = DatehepperUtil.getWeekDayAdd(mYear,mMonth,mDay,-1);
        mYear = cDateLeft.get(Calendar.YEAR);
        mMonth = cDateLeft.get(Calendar.MONTH);
        mDay = cDateLeft.get(Calendar.DAY_OF_MONTH);
        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_DAY);
    }
    private void ngayThangRight()
    {
        Calendar currentDate = Calendar.getInstance();
        Calendar cDateRigth = DatehepperUtil.getWeekDayAdd(mYear, mMonth, mDay, 1);
        // chỉ cho chọn qua 1 năm, nếu là 2 năm thì dừng lại
        if(cDateRigth.get(Calendar.YEAR) > currentDate.get(Calendar.YEAR)+ 1 ) return;
        mYear = cDateRigth.get(Calendar.YEAR);
        mMonth = cDateRigth.get(Calendar.MONTH);
        mDay = cDateRigth.get(Calendar.DAY_OF_MONTH);
        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_DAY);
    }
    private void minuteLeft()
    {
        Calendar cTimeMinus = DatehepperUtil.getTimeAdd(mYear, mMonth, mDay, mHour,mMinute, -30);
        mHour = cTimeMinus.get(Calendar.HOUR_OF_DAY);
        mMinute = cTimeMinus.get(Calendar.MINUTE);
        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_TIME);
    }
    private void minuteRight()
    {
        Calendar cTimeadd = DatehepperUtil.getTimeAdd(mYear, mMonth, mDay, mHour,mMinute,30);
        mHour = cTimeadd.get(Calendar.HOUR_OF_DAY);
        mMinute = cTimeadd.get(Calendar.MINUTE);
        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_TIME);
    }

    private void dialogSoNguoi(boolean isChild) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        builderSingle.setTitle(isChild?getString(R.string.tre_em):getString(R.string.so_nguoi));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.select_dialog_item);
        for (int i=isChild?0:1;i<=200;i++)
            arrayAdapter.add(i + "");
        builderSingle.setNegativeButton(
                getString(R.string.huy),
                (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(
                arrayAdapter,
                (dialog, which) -> {
                    String strName = arrayAdapter.getItem(which);
                    if(isChild)
                    {
                        mSoNguoiDenChild = Integer.parseInt(strName);
                        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_SO_NGUOI_CHILD);
                    }else {
                        mSoNguoiDen = Integer.parseInt(strName);
                        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_SO_NGUOI);
                    }
                });
        builderSingle.show();
    }

    private void dialogNgayThang()
    {
        DatePickerDialog dpd = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Do something with the date chosen by the user
                        Calendar currentDate = Calendar.getInstance();
                        // chỉ cho chọn qua 1 năm, nếu là 2 năm thì dừng lại
                        if(year > currentDate.get(Calendar.YEAR)+ 1 ) return;
                        mDay = dayOfMonth;
                        mMonth = monthOfYear;
                        mYear = year;
                        handleUpdateUI.sendEmptyMessage(KEY_UPDATE_DAY);
                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle(getString(R.string.ngay_thang));
        dpd.show();
    }

    private void dialogThoiGian()
    {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mHour = selectedHour;
                mMinute= selectedMinute;
                handleUpdateUI.sendEmptyMessage(KEY_UPDATE_TIME);
            }
        }, mHour, mMinute, true);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.thoi_gian_dat_cho));
        mTimePicker.show();
    }

    private final Handler handleUpdateUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY_UPDATE_SO_NGUOI:
                    mTvSoNguoi.setText(mSoNguoiDen + "");
                    break;
                case KEY_UPDATE_SO_NGUOI_CHILD:
                    mTvSoNguoiClild.setText(mSoNguoiDenChild + "");
                    break;
                case  KEY_UPDATE_DAY:
                    mTvDay.setText(DatehepperUtil.formatDate(mDay)+"/"+DatehepperUtil.formatDate((mMonth+1))+"/"+mYear);
                    String ngay= DatehepperUtil.getWeekDay(mYear,mMonth,mDay,mActivity);
                    mTvThu.setText(ngay);
                    mThoiGianDonXe =String.format("%s/%s/%s %s:%s:00",mYear,DatehepperUtil.formatDate(mMonth+1),DatehepperUtil.formatDate(mDay)
                            ,DatehepperUtil.formatDate(mHour),DatehepperUtil.formatDate(mMinute));
                    break;
                case KEY_UPDATE_TIME:
                    mTvTime.setText(DatehepperUtil.formatDate(mHour)+":"+DatehepperUtil.formatDate(mMinute));
                    mThoiGianDonXe =String.format("%s/%s/%s %s:%s:00",mYear,DatehepperUtil.formatDate(mMonth+1),DatehepperUtil.formatDate(mDay)
                            ,DatehepperUtil.formatDate(mHour),DatehepperUtil.formatDate(mMinute));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onStartMoveScreen() {

    }
    private void callClick() {
        final Dialog dialog;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_call);
        TextView tvContentConfirm = (TextView) dialog.findViewById(R.id.tvContentConfirm);
        TextView tvTitle = (TextView) dialog
                .findViewById(R.id.tvTitle);

        Utils.setTextViewHtml(tvTitle,String.format(getString(R.string.call_title_thongbao),mTenDoiTacKM));
        tvContentConfirm.setText(getString(R.string.call_center_support));
        dialog.setCancelable(true);
        Button btnHuy;
        LinearLayout lnCall1;
        lnCall1 = (LinearLayout) dialog.findViewById(R.id.lncall1);
        lnCall1.setOnClickListener(v -> {
            dialog.dismiss();
            gotoPhoneCallPage();
        });
        btnHuy = (Button) dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(v -> dialog.dismiss());
        if (!isFinishing())
            dialog.show();
    }
    //region permission Call
    protected void checkPemissionCall()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            callClick();
        } else {
            requestPermission(Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_CODE_PHONE, getApplicationContext(), ReserveDetailActivity.this);
        }
    }
    public static void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            // bật lại thông báo lần 2 sau khi tắt lần đầu
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        } else {
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchCallData();
                } else {
                    ToastUtils.showToast(mContext,"Permission was denied");
                }
                return;
            }
        }
    }
    private void fetchCallData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            callClick();;
        }
    }
    //endregion
}