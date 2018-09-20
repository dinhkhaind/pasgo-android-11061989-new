package com.onepas.android.pasgo.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.Place;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.ExpandableHeightListView;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchGoogle extends BaseAppCompatActivity implements
        OnClickListener, OnFocusChangeListener, AnimationListener {
    public static final String TAG = "SearchGoogle";
    private ExpandableHeightListView mLvDiaDiem;
    private ScrollView mScrSearchPlace;
    private List<Place> mListPlace = new ArrayList<Place>();
    private double fromLat = 21.005245;
    private double fromLong = 105.864574;

    private EditText mEdtSearch;
    private ImageView mIvSearch;
    private LinearLayout mLayoutKoDuLieu, mLnSearch;
    private DatabaseHandler mDBAdapter;
    private int mGetPlacesByFavorites = 0;
    public SearchGoogleAdapter mSearchGoogleAdapter;
    private TextView mTvKoCoDL;
    private double mLat, mLng;
    private String mName = "", mVicinity = "";
    // khi chọn Marker thì gắn giá trị cho mChiNhanhDoiTacId, nếu mà move map thì gắn mChiNhanhDoiTacId ="" --- nNhomCNDoiTacId tương tự
    private LinearLayout mLnDiaDiemTitle;

    @Override
    public void onNetworkChanged() {
        if (getBaseContext() == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable())
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        else
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard();
                finishToRightToLeft();
                return true;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_google);
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null)
            bundle = savedInstanceState;
        if (bundle != null) {
            fromLat = bundle.getDouble(Constants.BUNDLE_LAT);
            fromLong = bundle.getDouble(Constants.BUNDLE_LNG);
            mLat = fromLat;
            mLng = fromLong;
        }
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.search_address));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> {
            hideKeyboard();
            finishToRightToLeft();
        });
        mProgressToolbar = (ProgressBar) mToolbar.findViewById(R.id.toolbar_progress_bar);
        mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
        this.onNetworkChanged();
        mDBAdapter = new DatabaseHandler(this);
        mScrSearchPlace = (ScrollView) findViewById(R.id.scrSearchPlace);
        mLnDiaDiemTitle = (LinearLayout) findViewById(R.id.lnDiaDiemTitle);
        mLnSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        mLayoutKoDuLieu = (LinearLayout) findViewById(R.id.lyKhongCoThongBao);
        mTvKoCoDL = (TextView) findViewById(R.id.tv_ko_co_dl);
        mLvDiaDiem = (ExpandableHeightListView) findViewById(R.id.lvDiemSearch);
        mLvDiaDiem.setExpanded(true);
        mEdtSearch = (EditText) findViewById(R.id.serach_place);
        mIvSearch = (ImageView) findViewById(R.id.search);
        mIvSearch.setOnClickListener(this);
        mEdtSearch
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            String txtSearch = mEdtSearch.getText().toString();
                            actionSearch(txtSearch);
                            return true;
                        }
                        return false;
                    }
                });
        mEdtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        mEdtSearch.requestFocus();
        mProgressToolbar.setVisibility(ProgressBar.GONE);
        setbackgroundtab();
        // search nearby
        new GetPlaces(mContext, null, mGetPlacesByFavorites, true)
                .execute();

        mLvDiaDiem.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Place itemSelect = mListPlace.get(position);
                mLat = itemSelect.getLatitude();
                mLng = itemSelect.getLongitude();
                mName = itemSelect.getName();
                mVicinity = itemSelect.getVicinity();
                mEdtSearch.setText("");
                {
                    Intent intent = new Intent(mActivity, HomeActivity.class);//ReserveActivity
                    intent.putExtra(Constants.BUNDLE_LAT, mLat);
                    intent.putExtra(Constants.BUNDLE_LNG, mLng);
                    intent.putExtra(Constants.KEY_NAME, mName);
                    intent.putExtra(Constants.KEY_VICINITY, mVicinity);
                    intent.putExtra(Constants.BUNDLE_KEY_CHI_NHANH_DOI_TAC_ID_TU_SEARCH_PLACE, "");
                    intent.putExtra(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, "");
                    setResult(Constants.KEY_GOOGLE_SEARCH, intent);
                    finishToRightToLeft();
                }
            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEdtSearch.getWindowToken(), 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putDouble(Constants.BUNDLE_LAT, fromLat);
        bundle.putDouble(Constants.BUNDLE_LNG, fromLong);
    }


    private void showListView(boolean isShow) {
        if (!isShow) {
            mLvDiaDiem.setVisibility(View.GONE);
            mLnDiaDiemTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        String txtSearchChange = mEdtSearch.getText().toString();
        switch (v.getId()) {
            case R.id.linerGoogle:
                mLnSearch.setVisibility(View.VISIBLE);
                mLayoutKoDuLieu.setVisibility(View.GONE);
                mTvKoCoDL.setText(getString(R.string.chua_co_diem_nao));
                mScrSearchPlace.setVisibility(View.VISIBLE);
                if (mListPlace != null) {
                    mListPlace.clear();
                    SearchGoogleAdapter searchGoogleAdapter = new SearchGoogleAdapter(mContext,
                            R.layout.item_search_near, mListPlace, fromLat,
                            fromLong);
                    mLvDiaDiem.setAdapter(searchGoogleAdapter);
                }
            {
                // diem den
                if (txtSearchChange.equals("") || txtSearchChange == null) {
                    ToastUtils.showToast(mContext,
                            getString(R.string.key_search_empty));
                } else {
                    new GetPlaces(mContext, txtSearchChange, 0, true).execute();
                }
            }
            setbackgroundtab();
            break;
            case R.id.search:
                String txtSearch = mEdtSearch.getText().toString();
                actionSearch(txtSearch);
                break;
            default:
                break;
        }
    }

    private void actionSearch(String txtSearch) {
        {

            searchByName(txtSearch, true);
        }
    }

    private void searchByName(String txtSearch, boolean isDiemDon) {
        hideKeyBoard();
        {
            if (isDiemDon) {
                new GetPlaces(mContext, txtSearch, mGetPlacesByFavorites, true).execute();
            } else {
                if (txtSearch.length() > 0) {
                    // search text place google
                    new GetPlaces(mContext, txtSearch, 0, true).execute();
                } else {
                    ToastUtils.showToast(mContext,
                            getString(R.string.key_search_empty));
                }
            }
        }
    }


    private class GetPlaces extends AsyncTask<Void, Void, List<Place>> {

        private String txtSearch;
        private int getPlacesByFavorites;
        private boolean b;
        private int neaBySearch;
        private boolean isCancel = false;

        public GetPlaces(Context context, String txtSearch,
                         int getPlacesByFavorites, boolean b) {
            this.txtSearch = txtSearch;
            this.getPlacesByFavorites = getPlacesByFavorites;
            this.b = b;
        }

        @Override
        protected void onCancelled() {
            isCancel = true;
        }

        @Override
        protected void onPostExecute(List<Place> result) {
            super.onPostExecute(result);
            mProgressToolbar.setVisibility(ProgressBar.GONE);
            // isCancel = isCancelled();
            if (isCancel == false) {
                if (result != null) {
                    mListPlace = result;
                }
                handleUpdateList.sendEmptyMessage(0);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected List<Place> doInBackground(Void... arg0) {
            // location=21.005245,105.864574
            List<Place> findPlaces = null;
            // isCancel = isCancelled();
            if (isCancel == false) {
                if (getPlacesByFavorites == 0) {
                    if (txtSearch == null || txtSearch.equals("")) {
                        PlacesService service = new PlacesService(
                                Constants.PLACES_API_KEY, txtSearch, true,
                                neaBySearch);
                        findPlaces = service.findPlaces(fromLat, fromLong);
                    } else {
                        PlacesService service = new PlacesService(
                                Constants.PLACES_API_KEY, txtSearch, b,
                                neaBySearch);
                        findPlaces = service.findPlaces(fromLat, fromLong);
                    }
                } else if (getPlacesByFavorites == 1) {
                    findPlaces = mDBAdapter.getFavorites();
                } else if (getPlacesByFavorites == 3) {
                    findPlaces = mDBAdapter.getFavorites();
                }

            }
            return findPlaces;
        }
    }

    private void setbackgroundtab() {
        hideKeyBoard();
        showListView(false);
        mLvDiaDiem.setVisibility(View.VISIBLE);
    }

    Handler handleUpdateList = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (getBaseContext() == null || mListPlace == null)
                        return;
                    setListPlace(mListPlace, true);
                    break;
                case 1:
                    if (getBaseContext() == null || mListPlace == null)
                        return;
                    setListPlace(mListPlace, false);
                    break;
                case 3:
                    if (getBaseContext() == null || mListPlace == null)
                        return;
                    setListPlace(mListPlace, false);
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void sortList(List<Place> listPlace) {
        if (listPlace != null && listPlace.size() > 0) {
            Collections.sort(listPlace, new Comparator<Place>() {
                @Override
                public int compare(final Place object1, final Place object2) {
                    return object1.getKhoangCach().compareTo(
                            object2.getKhoangCach());
                }
            });
            mLayoutKoDuLieu.setVisibility(View.GONE);
            mLvDiaDiem.setVisibility(View.VISIBLE);
            mListPlace = listPlace;
            if (mListPlace.size() > 0) {
                listViewAddByreadMore();
            }
        }
    }


    // xem thêm phần listview
    private void listViewAddByreadMore() {
        int size = mListPlace.size();
        if (size == 0)
            mLnDiaDiemTitle.setVisibility(View.GONE);
        else
            mLnDiaDiemTitle.setVisibility(View.VISIBLE);
        mSearchGoogleAdapter = new SearchGoogleAdapter(mContext,
                R.layout.item_search_near, mListPlace, fromLat, fromLong);
        mLvDiaDiem.setAdapter(mSearchGoogleAdapter);
    }

    synchronized public void setListPlace(List<Place> listPlace,
                                          boolean isGoogle) {
        List<Place> list = new ArrayList<Place>();
        if (listPlace != null && listPlace.size() > 0) {
            int size = listPlace.size();
            if (isGoogle) {
                double[] arrdouble = new double[size];
                for (int i = 0; i < size; i++) {
                    Place place = listPlace.get(i);
                    if (place != null) {
                        double toLat = place.getLatitude();
                        double toLong = place.getLongitude();
                        double khoangCach = calculateDistance(fromLong,
                                fromLat, toLong, toLat);
                        khoangCach = khoangCach / 10;
                        arrdouble[i] = khoangCach;
                        place.setKhoangCach(khoangCach);
                        list.add(place);
                    }
                }
            } else {
                double[] arrdouble = new double[size];
                for (int i = 0; i < size; i++) {
                    Place place = listPlace.get(i);
                    if (place != null) {
                        double toLat = place.getLatitude();
                        double toLong = place.getLongitude();
                        double khoangCach = calculateDistance(fromLong,
                                fromLat, toLong, toLat);
                        khoangCach = khoangCach / 10;
                        arrdouble[i] = khoangCach;
                        place.setKhoangCach(khoangCach);
                        list.add(place);
                    }
                }
            }
        }
        sortList(list);
    }

    private double calculateDistance(double fromLong, double fromLat,
                                     double toLong, double toLat) {
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return (Math.round(d)) / 100;
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtSearch.getWindowToken(), 0);
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && mEdtSearch.getText().toString().trim().length() == 0) {
        }
    }


    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            hideKeyboard();
            finishToRightToLeft();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAnimationEnd(Animation arg0) {

    }

    @Override
    public void onAnimationRepeat(Animation arg0) {

    }

    @Override
    public void onAnimationStart(Animation arg0) {

    }

}