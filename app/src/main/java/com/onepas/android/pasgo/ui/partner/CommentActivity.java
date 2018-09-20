package com.onepas.android.pasgo.ui.partner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.BinhLuanKhuyenMai;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.partner.CommentAdapter.LikeCommentListenner;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends BaseAppCompatActivity {
    private static final String TAG = "CommentActivity";
    private EditText mEdtChat;
    private Button mBtnChat;
    private String mDoiTacKMID;
    private boolean mIsBestBinhLuan = true;
    private ArrayList<BinhLuanKhuyenMai> binhLuanKhuyenMais;
    private ArrayList<BinhLuanKhuyenMai> binhLuanKhuyenMaiAlls;
    private CommentAdapter adapter;
    private RelativeLayout mLyKhongCoThongBao;
    private ListView mLvData;
    private Dialog dialog = null;
    private TextView mTvXemThem;
    private String mTime;
    private boolean mIsSeeMore = false;
    private String mNoiDung;
    private int mCommentAdd = 0;
    private RatingBar mRbRating;
    private double mRatingNumberFromServer = 0.0;
    private double mRatingNumberClick = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_comment);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.comment));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> back());
        mProgressToolbar = (ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        mLnErrorConnectNetwork.setVisibility(View.GONE);

        initView();
        initControl();
    }

    private void setLayout(int n) {
        switch (n) {
            case 1:
                mLvData.setVisibility(View.GONE);
                mLyKhongCoThongBao.setVisibility(View.VISIBLE);
                break;
            case 2:
                mLvData.setVisibility(View.VISIBLE);
                mLyKhongCoThongBao.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        mEdtChat = (EditText) findViewById(R.id.edtChat);
        mBtnChat = (Button) findViewById(R.id.btnChat);
        mLyKhongCoThongBao = (RelativeLayout) findViewById(R.id.lyKhongCoThongBao);
        mLvData = (ListView) findViewById(R.id.lvComment);
        mTvXemThem = (TextView) findViewById(R.id.tvXemThem);
        mRbRating = (RatingBar) findViewById(R.id.ratingBar);
        mBtnChat.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            mIsSeeMore = false;
            sendChat();
        });
        mEdtChat.setInputType(InputType.TYPE_MASK_CLASS
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mEdtChat.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mIsSeeMore = false;
                sendChat();
                return true;
            }
            return false;
        });
        mTvXemThem.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            mIsSeeMore = true;
            getComment();
        });

        mRbRating.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float touchPositionX = event.getX();
                float width = mRbRating.getWidth();
                float starsf = (touchPositionX / width) * 5.0f;
                int stars = (int) starsf + 1;
                mRbRating.setRating(stars);
                mRatingNumberClick = stars;
                if(mRatingNumberClick>5)
                    mRatingNumberClick=5;

                showYesNoDialogNoCalcel(mActivity, R.string.rating_confirm,
                        R.string.dong_y, R.string.huy);
                v.setPressed(false);
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setPressed(true);
            }

            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setPressed(false);
            }


            return true;
        });
    }

    public void showYesNoDialogNoCalcel(final Context context, int messageId,
                                        int OkTextId, int cancelTextId) {
        if(!isCheckLogin())
        {
            Intent broadcastRegister = new Intent();
            broadcastRegister.setAction(Constants.BROADCAST_ACTION_REQUEST_LOGIN);
            sendBroadcast(broadcastRegister);
            return;
        }
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
        TextView tvThongBaoPopup = (TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        dialog.setCancelable(false);
        Button btnDongY, btnHuy;
        btnDongY = (Button) dialog.findViewById(R.id.btnDongY);
        btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        btnDongY.setText(context.getString(OkTextId));
        btnHuy.setText(context.getString(cancelTextId));
        tvThongBaoPopup.setText(String.format(context.getString(messageId),(int)mRatingNumberClick));
        btnDongY.setOnClickListener(view -> {
            rating(mRatingNumberClick);
            if (dialog != null && dialog.isShowing())
                dialog.cancel();
        });
        btnHuy.setOnClickListener(view -> {
            if (dialog != null && dialog.isShowing())
                dialog.cancel();
            mRbRating.setRating((float) mRatingNumberFromServer);
        });
        if(dialog!=null && !dialog.isShowing() && !isFinishing())
            dialog.show();
    }

    private void rating(double danhgia) {

        String url = WebServiceUtils.URL_SET_DANH_GIA_DTKM_BY_NGUOIDUNGID(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            try {
                jsonParams.put("doiTacKhuyenMaiId", mDoiTacKMID);
                jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
                jsonParams.put("danhGia", danhgia);
                Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                        new PWListener() {

                            @Override
                            public void onResponse(JSONObject json) {
                                handlerUpdateUI.sendEmptyMessage(0);
                            }

                            @Override
                            public void onError(int maloi) {
                                mRbRating.setRating((float) mRatingNumberFromServer);
                            }

                        }, new PWErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utils.Log(TAG, "khong ket noi may chu");
                                mRbRating.setRating((float) mRatingNumberFromServer);
                            }
                        });
            } catch (Exception e) {
                Utils.Log(TAG, "khong ket noi may chu");
            }
        } else {
            // showAlertMangYeu(1);
        }
        mEdtChat.setText("");
    }

    private void sendChat() {
        if (mEdtChat.getText().toString() == null
                || mEdtChat.getText().toString().equals("")) {
            Toast.makeText(mContext, R.string.nhap_binh_luan,
                    Toast.LENGTH_SHORT).show();
        } else {
            mNoiDung = mEdtChat.getText().toString();
            String url = WebServiceUtils.URL_AddBinhLuanDoiTacKhuyenMai(Pasgo
                    .getInstance().token);
            JSONObject jsonParams = new JSONObject();
            if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
                try {
                    jsonParams.put("doiTacKhuyenMaiId", mDoiTacKMID);
                    jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
                    jsonParams.put("noiDung", mEdtChat.getText().toString());
                    Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                            new PWListener() {

                                @Override
                                public void onResponse(JSONObject json) {
                                    if (json != null) {
                                        mCommentAdd++;
                                        BinhLuanKhuyenMai item = new BinhLuanKhuyenMai();
                                        item.setId(ParserUtils.getStringValue(
                                                json, "Item"));
                                        item.setDaThich(0);
                                        item.setLuotThich("0");
                                        item.setNoiDung(mNoiDung);
                                        String name = Pasgo
                                                .getInstance().username;
                                        if(StringUtils.isEmpty(name))
                                            name = getString(R.string.anonymous);
                                        item.setTenNguoiDung(name);
                                        item.setThoiGian(DatehepperUtil
                                                .getCurrentDate(DatehepperUtil.yyyyMMddHHmmss));
                                        item.setDanhGia(mRatingNumberFromServer);
                                        item.setNguoiDungId(Pasgo.getInstance().userId);
                                        binhLuanKhuyenMaiAlls.add(item);
                                        setListview();
                                    }
                                }

                                @Override
                                public void onError(int maloi) {
                                    closeProgressDialog();
                                }

                            }, new PWErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utils.Log(TAG, "khong ket noi may chu");
                                    // showAlertMangYeu(1);
                                }
                            });
                } catch (Exception e) {
                    Utils.Log(TAG, "khong ket noi may chu");
                }
            } else {
                // showAlertMangYeu(1);
            }
            mEdtChat.setText("");
        }
    }

    private void setListview() {
        adapter = new CommentAdapter(mContext, binhLuanKhuyenMaiAlls,
                new LikeCommentListenner() {

                    @Override
                    public void likeComment(int position) {
                        // TODO Auto-generated method stub
                        String id = binhLuanKhuyenMaiAlls.get(position).getId();
                        int isLike = binhLuanKhuyenMaiAlls.get(position)
                                .getDaThich();
                        setLikeOrUnLike(id, isLike, position);
                    }
                });
        mLvData.setAdapter(adapter);
        setLayout(2);
    }

    @Override
    protected void initControl() {
        // TODO Auto-generated method stub
        super.initControl();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDoiTacKMID = bundle.getString(Constants.BUNDLE_DOI_TAC_KHUYEN_MAI);
            binhLuanKhuyenMaiAlls = new ArrayList<BinhLuanKhuyenMai>();
            mTime = DatehepperUtil
                    .getCurrentDate(DatehepperUtil.yyyyMMddHHmmss);
            getComment();
        } else
            finishToRightToLeft();
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

    private void getComment() {
        String url = WebServiceUtils.URL_GetBinhLuanKhuyenMai(Pasgo
                .getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("doiTacKhuyenMaiId", mDoiTacKMID);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("isBestBinhLuan", mIsBestBinhLuan);
            jsonParams.put("thoiGian", mTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.Log("response ", "response  khuyen mai" + response);
                        JSONObject objItems = ParserUtils.getJsonObject(response, "Item");
                        mRatingNumberFromServer = ParserUtils.getDoubleValue(objItems, "DanhGia");
                        mRbRating.setRating((float) mRatingNumberFromServer);
                        binhLuanKhuyenMais = ParserUtils
                                .getBinhLuanKhuyenMais(response);
                        mIsBestBinhLuan = false;

                        if (binhLuanKhuyenMais.size() == 0) {
                            mTvXemThem.setVisibility(View.GONE);
                            mProgressToolbar.setVisibility(ProgressBar.GONE);
                        }else{
                            if (binhLuanKhuyenMaiAlls.size() == 0)
                                binhLuanKhuyenMaiAlls.addAll(0, binhLuanKhuyenMais);
                            else
                                binhLuanKhuyenMaiAlls.addAll(1, binhLuanKhuyenMais);
                        }
                        if (binhLuanKhuyenMaiAlls.size() == 0) {
                            setLayout(1);
                        } else {
                            setListview();
                        }
                        if (binhLuanKhuyenMais.size() > 0) {
                            if(!binhLuanKhuyenMais.get(0).isBestBinhLuan())
                                mTime = binhLuanKhuyenMais.get(0).getThoiGian();
                            else
                            {
                                if(binhLuanKhuyenMaiAlls.size()>1)
                                    mTime = binhLuanKhuyenMais.get(1).getThoiGian();
                            }
                        }
                        if (!mIsSeeMore) {
                            mLvData.setSelection(binhLuanKhuyenMaiAlls.size() - 1);
                        } else {
                            mLvData.setSelection(binhLuanKhuyenMais.size() - 1);
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
                        showAlertMangYeu(1);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }

    private void setLikeOrUnLike(String id, int isThich, final int position) {
        String url = WebServiceUtils
                .URL_SetUaThichBinhLuanDoiTacKhuyenMaiByNguoiDungId(Pasgo
                        .getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("binhLuanDoiTacKhuyenMaiId", id);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("thich", isThich == 0 ? true : false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.Log(TAG, "comment" + response);
                        JSONObject objItem = ParserUtils.getJsonObject(
                                response, "Item");
                        binhLuanKhuyenMaiAlls.get(position).setDaThich(
                                ParserUtils.getIntValue(objItem, "DaThich"));
                        binhLuanKhuyenMaiAlls.get(position).setLuotThich(
                                ParserUtils
                                        .getStringValue(objItem, "LuotThich"));
                        adapter.notifyDataSetChanged();
                        mLvData.invalidateViews();
                        mLvData.refreshDrawableState();
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(int maloi) {
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }

                }, new PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showAlertMangYeu(1);
                        mProgressToolbar.setVisibility(ProgressBar.GONE);
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isBest", mIsBestBinhLuan);
        bundle.putInt(Constants.BUNDLE_COMMENT_ADD, mCommentAdd);
        bundle.putString(Constants.BUNDLE_DOI_TAC_KHUYEN_MAI, mDoiTacKMID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        mCommentAdd = savedInstanceState.getInt(Constants.BUNDLE_COMMENT_ADD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                return true;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            back();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        Intent intent = new Intent();
        intent.putExtra(Constants.BUNDLE_COMMENT_ADD, mCommentAdd);
        setResult(Constants.kEY_COMMENT_KHUYEM_MAI, intent);
        finishToRightToLeft();
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
    }

    private void showAlertMangYeu(final int i) {
        if (getBaseContext() == null && mIsDestroy)
            return;
        if (!isFinishing()) {

            if (dialog == null) {
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_mang_yeu);
            }
            Button dialogBtThuLai = (Button) dialog.findViewById(R.id.btThulai);// ket_noi_mang_yeu_and_connecttoserver
            Button dialogBtHuy = (Button) dialog.findViewById(R.id.btHuy);
            TextView tv = (TextView) dialog.findViewById(R.id.content);
            tv.setText(getString(R.string.ket_noi_mang_yeu_and_connecttoserver));
            dialogBtThuLai.setOnClickListener(v -> {
                switch (i) {
                    case 1:
                        getComment();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            });
            dialogBtHuy.setOnClickListener(v -> {
                dialog.dismiss();
                finishToRightToLeft();
            });
            if (!dialog.isShowing()&& !isFinishing()) {
                dialog.show();
            }
        }
    }
    Handler handlerUpdateUI =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                {
                    mRatingNumberFromServer = mRatingNumberClick;
                    mRbRating.setRating((float) mRatingNumberFromServer);
                    for(BinhLuanKhuyenMai item: binhLuanKhuyenMaiAlls)
                    {
                        if(item.getNguoiDungId().equals(Pasgo.getInstance().userId))
                        {
                            item.setDanhGia(mRatingNumberFromServer);
                        }
                    }
                    if(adapter ==null || mLvData==null)
                        return;
                    adapter.notifyDataSetChanged();
                    mLvData.invalidateViews();
                    mLvData.refreshDrawableState();
                    break;
                }
            }
        }
    };

}