package com.onepas.android.pasgo.ui.partner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhBangGia;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuDetailActivity extends BaseAppCompatActivity {
    private ArrayList<AnhBangGia> mAnhs;
    private String mJSon="";
    private int mPosition=0;
    private String mDoiTacKhuyenMaiId="";
    private ViewPager myPager;
    private TextView mTvNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_image_menu_detail);
        myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
        mTvNumber = (TextView) findViewById(R.id.number_tv);
        findViewById(R.id.close_ln).setOnClickListener(view -> finishToRightToLeft());
        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            bundle = savedInstanceState;
        if(bundle!=null) {
            mJSon = bundle.getString(Constants.IMAGE_LIST_OBJECT,"");
            mPosition = bundle.getInt(Constants.IMAGE_LIST_NUMBER);
            mDoiTacKhuyenMaiId = bundle.getString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID,"");
            if(StringUtils.isEmpty(mDoiTacKhuyenMaiId))
            {
                viewImage();
            }else
                getChiTietBangGia();

        }
    }
    private void getChiTietBangGia() {
        String url = WebServiceUtils
                .URL_GET_CHI_TIET_BANG_GIA(Pasgo
                        .getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("doiTacKhuyenMaiId", mDoiTacKhuyenMaiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showProgressDialog();
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mJSon = response.toString();
                        viewImage();
                        closeProgressDialog();
                    }

                    @Override
                    public void onError(int maloi) {
                        closeProgressDialog();
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                    }
                });
    }
    private void viewImage()
    {
        try {
            JSONObject response = new JSONObject(mJSon);
            Utils.Log(Pasgo.TAG,"mJSon:"+mJSon);
            JSONArray jsonArray = ParserUtils.getJsonArray(response,"Items");
            mAnhs = ParserUtils.getAnhBangGias(jsonArray);
            mTvNumber.setText(mPosition+1+"/"+ mAnhs.size());
            MenuPagerAdapter adapter = new MenuPagerAdapter(mAnhs, mActivity);
            myPager.setAdapter(adapter);
            myPager.setCurrentItem(mPosition);
            myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    Utils.Log(Pasgo.TAG,"onPageSelected" + position);
                    mPosition = position;
                    mTvNumber.setText(mPosition+1+"/"+ mAnhs.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // TODO Auto-generated method stub
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.IMAGE_LIST_OBJECT,mJSon);
        bundle.putInt(Constants.IMAGE_LIST_NUMBER,mPosition);
        bundle.putString(Constants.KEY_DOI_TAC_KHUYEN_MAI_ID,mDoiTacKhuyenMaiId);
    }


    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }
}
