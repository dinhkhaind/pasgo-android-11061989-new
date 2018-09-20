package com.onepas.android.pasgo.ui.calleddrivers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class CalledDriverActivity extends BaseAppCompatActivity {
    private FragmentTabHost mTabHost;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_pasgo_card);
        mContext =this;
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), String.format(getResources().getString(R.string.da_nhan), Pasgo.getInstance().datTruocNhan), R.drawable.reserved_tab_one)),
                ReceivedFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), String.format(getResources().getString(R.string.chua_nhan), Pasgo.getInstance().datTruocChuaNhan), R.drawable.reserved_tab_one)),
                NotReceivedFragment.class, null);
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
            mTabHost.getTabWidget().setDividerDrawable(null);
            if(i==0) {
                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textView); //Unselected Tabs
                tv.setTextColor(Utils.getColor(mContext, R.color.red));
            }
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                mTabHost.refreshDrawableState();
                //mTabHost.removeAllViews();
                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
                        .setBackgroundColor(Utils.getColor(mContext, android.R.color.white));
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textView); //Unselected Tabs
                    tv.setTextColor(Utils.getColor(mContext, R.color.tab_text_unselect));
                }
                TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(R.id.textView); //for Selected Tab
                tv.setTextColor(Utils.getColor(mContext, R.color.tab_text_selected));
            }
        });
        getSoLuongLichSu();
    }

    private View getTabIndicator(Context context, String title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout_reserved, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
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

                finishOurLeftInLeft();
            } else {
                finishOurLeftInLeft();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finishOurLeftInLeft();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.danh_sach_tai_tro_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishOurLeftInLeft();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getSoLuongLichSu() {
        String url = WebServiceUtils.URL_SO_LUONG_LICH_SU(Pasgo.getInstance().token);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("nguoiDungId", Pasgo.getInstance().userId);
        JSONObject jsonParams = new JSONObject(params);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObj = ParserUtils.getJsonObject(
                                response, "Item");
                        Pasgo.getInstance().datTruocNhan = ParserUtils.getIntValue(jsonObj, "DatTruocNhan");
                        Pasgo.getInstance().checkInDangCho = ParserUtils.getIntValue(jsonObj, "CheckInDangCho");
                        Pasgo.getInstance().datTruocChuaNhan = ParserUtils.getIntValue(jsonObj, "DatTruocChuaNhan");
                        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textView);
                            if(i==0)
                                tv.setText(String.format(getResources().getString(R.string.da_nhan), Pasgo.getInstance().datTruocNhan));
                            else
                                tv.setText(String.format(getResources().getString(R.string.chua_nhan), Pasgo.getInstance().datTruocChuaNhan));
                        }
                    }

                    @Override
                    public void onError(int maloi) {
                        closeProgressDialogAll();
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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