package com.onepas.android.pasgo.ui.reserve;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ReserveSearch;
import com.onepas.android.pasgo.models.SearchNews;
import com.onepas.android.pasgo.models.TagModel;
import com.onepas.android.pasgo.models.TagSpecial;
import com.onepas.android.pasgo.models.TinKhuyenMaiDoiTac;
import com.onepas.android.pasgo.models.Tinh;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.account.AccountManagerActivity;
import com.onepas.android.pasgo.ui.announcements.AnnouncementsDetail3Activity;
import com.onepas.android.pasgo.ui.announcements.WebviewActivity;
import com.onepas.android.pasgo.ui.calleddrivers.CalledDriverActivity;
import com.onepas.android.pasgo.ui.category.CategoryActivity;
import com.onepas.android.pasgo.ui.guid.GuidActivity;
import com.onepas.android.pasgo.ui.home.FavoriteActivity;
import com.onepas.android.pasgo.ui.home.FilterActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.ui.setting.SettingActivity;
import com.onepas.android.pasgo.ui.share.ShareActivity;
import com.onepas.android.pasgo.ui.successfultrips.SuccessfulTripsActivity;
import com.onepas.android.pasgo.ui.termsandpolicies.TermsAndPoliciesActivity;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.ExpandableHeightListView;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.TimeAgoUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentSearch extends BaseFragmentCheckIn implements View.OnClickListener {

    private ExpandableHeightListView mLvGoiY, mLvSpecial;
    private View mRoot;
    private EditText mEdtSearch;
    private ImageView mIvSearch;
    private ArrayList<ReserveSearch> mReserveSearches;
    private int mTagClickId =0;
    private String mTextSearch="";
    private JSONObject mObjGoiY;
    private String mTinhName="";
    private RecyclerView mLvTag;
    public StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private ArrayList<TagModel> mTagAlls = new ArrayList<>();
    private static final int KEY_SET_NHOM_KM =1;
    private static final int KEY_SET_TAG_SPECIAL =2;
    private static final int KEY_SET_TIN_KM =3;
    private static final int KEY_SET_GOI_Y =4;

    private int mTagId=1;
    private TagSearchAdapter mTagAdapter;
    private LinearLayout mLnTinh;
    private TextView mTvTinh;

    private ArrayList<TinhHome> mTinhs;
    private Dialog mDialogTinh;
    private TinhAdapter mTinhAdapter;
    private ArrayList<SearchNews> mSearchNews;
    private ArrayList<TagSpecial> mTagSpecials;
    private LinearLayout mLnSearchNews;
    private LinearLayout mLnGoiY;
    private LinearLayout mLnLoading, mLnDisconnect, mLnData;
    private Button mBtnTryAgain;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mRoot==null) {
            String tinh = Pasgo.getInstance().prefs.getTinhMain();
            if(!StringUtils.isEmpty(tinh))
            {
                try {
                    JSONObject jsonObjectTinh = new JSONObject(tinh);
                    ArrayList<TinhHome> tinhs = ParserUtils.getAllTinhHome(jsonObjectTinh);

                    for(TinhHome item: tinhs)
                    {
                        if(item.getId()==Pasgo.getInstance().mTinhId)
                        {
                            mTinhName = item.getTen();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //
            mRoot = inflater.inflate(R.layout.fragment_layout_reserve_search, container, false);
            mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
            mToolbar.setTitle("");
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            initializeMenu();
            mLvGoiY = (ExpandableHeightListView) mRoot.findViewById(R.id.goi_y_lv);
            mLvGoiY.setExpanded(true);
            mLvSpecial = (ExpandableHeightListView) mRoot.findViewById(R.id.tag_special_lv);
            mLvSpecial.setExpanded(true);
            mEdtSearch = (EditText) mRoot.findViewById(R.id.search_edt);

            mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
            mLnTinh = (LinearLayout)mRoot.findViewById(R.id.tinh_ln);
            mTvTinh = (TextView)mRoot.findViewById(R.id.tinh_tv);
            mLvTag = (RecyclerView)mRoot.findViewById(R.id.tag_lv);
            mStaggeredGridLayoutManager =new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            mLvTag.setLayoutManager(mStaggeredGridLayoutManager);

            mLnLoading = (LinearLayout)mRoot.findViewById(R.id.loading_ln);
            mLnDisconnect = (LinearLayout)mRoot.findViewById(R.id.lnDisconnect);
            mLnData = (LinearLayout)mRoot.findViewById(R.id.lnData);
            mRoot.findViewById(R.id.btnTryAGain).setOnClickListener(v->{
                tuKhoaTimKiem();
            });
            mRoot.findViewById(R.id.filter_rl).setOnClickListener(v->{
                gotoActivity(mActivity, FilterActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft(mActivity);
            });
            AppBarLayout appBarLayout = (AppBarLayout)mRoot.findViewById(R.id.appbar);
            ViewGroup.LayoutParams paramsAppbar=appBarLayout.getLayoutParams();
            int heightScreen = Utils.getHeightScreen(mActivity) - paramsAppbar.height - 200;
            ViewGroup.LayoutParams paramsLoading=mLnLoading.getLayoutParams();
            paramsLoading.height = heightScreen;
            mLnLoading.setLayoutParams(paramsLoading);

            ViewGroup.LayoutParams paramsDisconnect=mLnDisconnect.getLayoutParams();
            paramsDisconnect.height = heightScreen;
            mLnDisconnect.setLayoutParams(paramsDisconnect);

            mLnSearchNews = (LinearLayout) mRoot.findViewById(R.id.searchnews_ln);
            mLnSearchNews.setVisibility(View.GONE);
            mIvSearch = (ImageView) mRoot.findViewById(R.id.search);
            mIvSearch.setOnClickListener(v -> {
                mTagClickId = 0;
                mTextSearch =  mEdtSearch.getText().toString();
                gotoSearchResult();
            });
            mLnGoiY = (LinearLayout) mRoot.findViewById(R.id.goi_y_ln);
            mLnGoiY.setVisibility(View.GONE);
            mLnTinh.setOnClickListener(view -> showDialogTinh());
            mLvGoiY.setOnItemClickListener((parent, view, position, id) -> {
                mTagClickId = 0;
                mTextSearch = mReserveSearches.get(position).getTuKhoa();
                gotoSearchResult();
            });
            mLvSpecial.setOnItemClickListener((parent, view, position, id) -> {
                mTagClickId = mTagSpecials.get(position).getId();
                mTextSearch = mTagSpecials.get(position).getTen();
                gotoSearchResult();
            });
            mEdtSearch
                    .setOnEditorActionListener((v, actionId, event) -> {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            mTagClickId = 0;
                            mTextSearch =  mEdtSearch.getText().toString();
                            gotoSearchResult();
                            return true;
                        }
                        return false;
                    });
            mEdtSearch.requestFocus();
        }

        if(mObjGoiY==null)
            tuKhoaTimKiem();
        else
            handlerUpdate.sendEmptyMessage(0);
        onNetworkChanged();
        for (TagModel item : mTagAlls)
            item.setIsCheck(false);
        if(mTagAdapter!=null)
            mTagAdapter.updateList(mTagAlls);
        onNetworkChanged();
        // nhan tinhId
        handlerUpdateTinhUI.sendEmptyMessage(0);
        return mRoot;
    }

    private void initializeMenu() {
        mToolbar.setNavigationIcon(R.drawable.icon_menu);
        HomeActivity activity = (HomeActivity) getActivity();
        activity.mNavigationDrawerFragment.setUp(R.id.drawer_fragment, activity.mDrawerLayout, mToolbar);
        activity.mNavigationDrawerFragment.menuDatXe.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            activity.goToDatXe(activity.KEY_SERVICE_TAXI);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.menuCategory.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, CategoryActivity.class);
        });
        activity.mNavigationDrawerFragment.menuTaiTro.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, ThePasgoActivity.class);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.menuLichSuChuyenDi.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, SuccessfulTripsActivity.class);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.menuHuongDanSuDung.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, GuidActivity.class);
            ourLeftInLeft(mActivity);
        });

        activity.mNavigationDrawerFragment.menuQuyDinhChung.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, TermsAndPoliciesActivity.class);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.menuGioiThieuBanBe.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, ShareActivity.class);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.menuThietLap.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUNDLE_LANGUAGE_BEFOR, Pasgo.getInstance().language);
            bundle.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, activity.mIsShowKhuyenMai);
            gotoActivity(mActivity, SettingActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.menuThoat.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            activity.menuThoat();
        });
        activity.mNavigationDrawerFragment.menuYeuThich.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, FavoriteActivity.class);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.dat_truoc_rl.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, CalledDriverActivity.class);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.check_in_rl.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, ReservedHistoryActivity.class);
            ourLeftInLeft(mActivity);
        });
        activity.mNavigationDrawerFragment.lnAccount.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, AccountManagerActivity.class);
            ourLeftInLeft(mActivity);
        });

    }

    private void showDialogTinh() {
        if (getActivity() == null || mTinhs==null || mTinhs.size()==0)
            return;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.vi_tri));
        alertDialog.setView(dialog);
        final ListView lv = (ListView)dialog.findViewById(R.id.listView);
        mTinhAdapter=new TinhAdapter(getActivity(), mTinhs, new TinhAdapter.TinhListenner() {
            @Override
            public void check(int position) {
                for(TinhHome item: mTinhs)
                {
                    item.setIsCheck(false);
                }
                mTinhs.get(position).setIsCheck(true);
                Pasgo.getInstance().mTinhId = mTinhs.get(position).getId();
                if(Pasgo.getInstance().mTinhId>0) {
                    Pasgo.getInstance().prefs.putTinhId(Pasgo.getInstance().mTinhId);
                    updateTinhSelected();
                }
                mTinhName = mTinhs.get(position).getTen();
                mTvTinh.setText(mTinhName);
                mTinhAdapter.notifyDataSetChanged();
                lv.invalidateViews();
                lv.refreshDrawableState();
                tuKhoaTimKiem();
                mDialogTinh.dismiss();
            }
        });
        lv.setAdapter(mTinhAdapter);
        if(mDialogTinh ==null)
            mDialogTinh = alertDialog.create();
        if(mDialogTinh !=null && !mDialogTinh.isShowing()&&!getActivity().isFinishing()) {
            mDialogTinh = alertDialog.create();
            mDialogTinh.show();
        }
    }
    private void gotoSearchResult()
    {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY__SEARCH_TAG_ID, mTagClickId);
        bundle.putString(Constants.BUNDLE_KEY_SEARCH_TEXT, mTextSearch);
        gotoActivity(getActivity(), SearchResultActivity.class, bundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft(mActivity);
    }

    private void tuKhoaTimKiem() {
        String url = WebServiceUtils
                .URL_TU_KHOA_TIM_KIEM(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        if (Pasgo.getInstance().userId == null)
            Pasgo.getInstance().userId = "";
        try {
            if (StringUtils.isEmpty(Pasgo.getInstance().userId))
                Pasgo.getInstance().userId = "";
            DeviceUuidFactory factory = new DeviceUuidFactory(mActivity);
            String deviceId = factory.getDeviceUuid();
            jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("deviceId", deviceId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setViewUI(1);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        setViewUI(2);
                        mObjGoiY = response;
                        handlerUpdate.sendEmptyMessage(0);
                    }

                    @Override
                    public void onError(int maloi) {
                        setViewUI(3);
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setViewUI(3);
                    }
                });

    }
    protected android.os.Handler handlerUpdate = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mReserveSearches = ParserUtils.getTuKhoaTimKiem(mActivity, mObjGoiY);
                    mTagAlls = ParserUtils.getTagModels(mActivity, mObjGoiY);
                    mSearchNews = ParserUtils.getSearchNews(mObjGoiY);
                    mTagSpecials = ParserUtils.getTagSpecials(mObjGoiY);

                    handleUpdateUI.sendEmptyMessage(KEY_SET_NHOM_KM);
                    handleUpdateUI.sendEmptyMessage(KEY_SET_TAG_SPECIAL);
                    handleUpdateUI.sendEmptyMessage(KEY_SET_TIN_KM);
                    handleUpdateUI.sendEmptyMessage(KEY_SET_GOI_Y);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void setViewUI(int i)
    {
        switch (i)
        {
            case 1:
                mLnLoading.setVisibility(View.VISIBLE);
                mLnDisconnect.setVisibility(View.GONE);
                mLnData.setVisibility(View.GONE);
                break;
            case 2:
                mLnLoading.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.GONE);
                mLnData.setVisibility(View.VISIBLE);
                break;
            case 3:
                mLnLoading.setVisibility(View.GONE);
                mLnDisconnect.setVisibility(View.VISIBLE);
                mLnData.setVisibility(View.GONE);
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEdtSearch.getWindowToken(), 0);
    }
    private final Handler handleUpdateUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case KEY_SET_NHOM_KM:
                        if(mTagAlls.size()>0) {

                            if(mTagAdapter ==null )
                            {
                                mTagAdapter = new TagSearchAdapter(mActivity, mTagAlls, new TagSearchAdapter.TagListenner() {
                                    @Override
                                    public void onClick(int position) {
                                        if (mTagAlls.size() - 1 < position) return;
                                        mTagId = mTagAlls.get(position).getId();
                                        //load data
                                        mTagClickId = mTagId;
                                        mTextSearch = mTagAlls.get(position).getTen();
                                        gotoSearchResult();
                                    }
                                });
                                mLvTag.setAdapter(mTagAdapter);
                            }
                            mTagAdapter.updateList(mTagAlls);
                        }else{
                            //showView(KEY_NO_DATA_KM);
                        }

                        break;
                    case KEY_SET_TAG_SPECIAL:
                        TagSpecialAdapter tagSpecialAdapter = new TagSpecialAdapter(mActivity, mTagSpecials);
                        mLvSpecial.setAdapter(tagSpecialAdapter);
                        break;
                    case KEY_SET_TIN_KM:
                        if(mRoot!=null) {
                            TimeAgoUtils timeAgoUtils = new TimeAgoUtils(mActivity);
                            TextView thong_bao1_tv;
                            TextView date_tv;
                            TextView description_tv;
                            SimpleDraweeView imgLogo;
                            RelativeLayout mRlRowKhuyenMai;
                            TextView thong_bao2_tv;
                            TextView thong_bao3_tv;
                            thong_bao1_tv = (TextView) mRoot.findViewById(R.id.thong_bao1_tv);
                            date_tv = (TextView) mRoot.findViewById(R.id.date_tv);
                            description_tv = (TextView) mRoot.findViewById(R.id.description_tv);
                            imgLogo = (SimpleDraweeView) mRoot.findViewById(R.id.icon_img);
                            mRlRowKhuyenMai = (RelativeLayout) mRoot.findViewById(R.id.khuyen_mai_rl);
                            thong_bao2_tv = (TextView) mRoot.findViewById(R.id.thong_bao2_tv);
                            thong_bao3_tv = (TextView) mRoot.findViewById(R.id.thong_bao3_tv);
                            mRlRowKhuyenMai.setOnClickListener(view -> {
                                searchNewsClick(0);
                            });
                            thong_bao2_tv.setOnClickListener(view -> {
                                searchNewsClick(1);
                            });
                            thong_bao3_tv.setOnClickListener(view -> {
                                searchNewsClick(2);
                            });
                            if(mSearchNews.size()>0)
                            {
                                mLnSearchNews.setVisibility(View.VISIBLE);
                                thong_bao1_tv.setText(mSearchNews.get(0).getTieuDe());
                                long dateLong = DatehepperUtil.convertDatetimeToLongDate(mSearchNews.get(0).getNgayBatDau(), DatehepperUtil.ddMMyyyyHHmmss);
                                date_tv.setText(timeAgoUtils.timeAgo(dateLong));
                                String mota  = mSearchNews.get(0).getMoTa();
                                if(StringUtils.isEmpty(mota))
                                    description_tv.setVisibility(View.GONE);
                                else
                                    description_tv.setVisibility(View.VISIBLE);
                                description_tv.setText(mota);
                                String urlImage = mSearchNews.get(0).getAnh();
                                if (!StringUtils.isEmpty(urlImage)) {
                                    imgLogo.setImageURI(urlImage);
                                } else {
                                    imgLogo.setBackgroundResource(R.drawable.logo_partnerdf);
                                }

                                if(mSearchNews.size()>1)
                                {
                                    thong_bao2_tv.setText(mSearchNews.get(1).getTieuDe());
                                    thong_bao2_tv.setVisibility(View.VISIBLE);
                                }else
                                    thong_bao2_tv.setVisibility(View.GONE);
                                if(mSearchNews.size()>2)
                                {
                                    thong_bao3_tv.setText(mSearchNews.get(2).getTieuDe());
                                    thong_bao3_tv.setVisibility(View.VISIBLE);
                                }else
                                    thong_bao3_tv.setVisibility(View.GONE);

                            }else
                                mLnSearchNews.setVisibility(View.GONE);
                        }
                        break;
                    case KEY_SET_GOI_Y:
                        if(mReserveSearches.size()==0)
                            mLnGoiY.setVisibility(View.GONE);
                        else
                            mLnGoiY.setVisibility(View.VISIBLE);
                        GoiYAdapter goiYAdapter = new GoiYAdapter(mActivity, mReserveSearches);
                        mLvGoiY.setAdapter(goiYAdapter);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
            super.handleMessage(msg);
        }
    };
    private void searchNewsClick(int position)
    {
        SearchNews item = mSearchNews.get(position);
        switch (item.getLoaiTinKhuyenMai())
        {
            case 1:
                Bundle mBundle = new Bundle();
                mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_ID, item.getId());
                mBundle.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, item.getTieuDe());
                gotoActivity(mActivity,WebviewActivity.class,mBundle,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft(mActivity);
                break;
            case 2:
                getContentKM(item.getId());
                break;
            case 3:
                Bundle bundle3 = new Bundle();
                bundle3.putString(Constants.BUNDLE_KEY_TIN_KM_ID, item.getId());
                bundle3.putString(Constants.BUNDLE_KEY_TIN_KM_TITLE, item.getTieuDe());
                gotoActivity(mActivity,AnnouncementsDetail3Activity.class,bundle3,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft(mActivity);
                break;
            case 4:
                Bundle bundle4 = new Bundle();
                bundle4.putInt(Constants.KEY_TYPE_PUSH_NOTIFICATION, Constants.KEY_ACTIVITY_THE_PASGO);
                Constants.ThePasgoTabNumber =0;
                gotoActivity(mActivity,ThePasgoActivity.class,bundle4,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft(mActivity);
                break;
            case 5:
                Bundle bundle5 = new Bundle();
                bundle5.putInt(Constants.KEY_TYPE_PUSH_NOTIFICATION, Constants.KEY_ACTIVITY_THE_PASGO);
                Constants.ThePasgoTabNumber =1;
                gotoActivity(mActivity,ThePasgoActivity.class,bundle5,Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft(mActivity);
                break;
        }
    }
    private void getContentKM(String iD) {
        Double lat=0.0,lng=0.0;
        if (Pasgo.getInstance().prefs != null
                && Pasgo.getInstance().prefs.getLatLocationRecent() != null) {
            lat = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLatLocationRecent());
            lng = Double.parseDouble(Pasgo.getInstance().prefs
                    .getLngLocationRecent());
        }
        DeviceUuidFactory factory = new DeviceUuidFactory(mActivity);
        String url = WebServiceUtils.URL_GET_CHI_TIET_DOI_TAC_KM(Pasgo
                .getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("viDo", lat);
            jsonParams.put("kinhDo", lng);
            jsonParams.put("khuyenMaiId", iD);
            jsonParams.put("deviceId", factory.getDeviceUuid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialog();
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dismissDialog();
                        ArrayList<TinKhuyenMaiDoiTac> tinKhuyenMaiDoiTacs = ParserUtils.getTinKhuyenMaiDoiTacs(response);
                        if (tinKhuyenMaiDoiTacs.size() > 0) {
                            lvClickItemDoiTac(tinKhuyenMaiDoiTacs.get(0));
                        }
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
    protected void lvClickItemDoiTac(TinKhuyenMaiDoiTac item) {
        if (item != null) {
            String doiTacKMID = item.getDoiTacKhuyenMaiId();
            String title = item.getTen();
            String chiNhanhDoiTacId = item.getId();
            String nhomCnDoiTac = item.getNhomCnDoiTacId();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_DI_XE_FREE, Constants.KEY_INT_XE_FREE);
            bundle.putInt(Constants.KEY_TEN_TINH_ID, 1);
            bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
            bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, doiTacKMID);
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
            bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
            bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, item.getNhomKhuyenMaiId());
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getDiaChi());
            bundle.putString(Constants.BUNDLE_KEY_NHOM_CHI_NHANH_DOI_TAC_ID, nhomCnDoiTac);
            bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
            bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
            bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
            gotoActivityForResult(mActivity, DetailActivity.class, bundle,
                    Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft(mActivity);
        }
    }
    //endregion

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onNetworkChanged() {
        if (getActivity() == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(getActivity()).isNetworkAvailable())
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        else
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
    }
    // đoạn này xử lý cho tỉnh
    Handler handlerUpdateTinhUI = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    String tinh = Pasgo.getInstance().prefs.getTinhMain();
                    if(!StringUtils.isEmpty(tinh))
                    {
                        try {
                            JSONObject jsonObjectTinh = new JSONObject(tinh);
                            mTinhs = ParserUtils.getTinhAllV1(jsonObjectTinh);
                            for(TinhHome item1: mTinhs)
                            {
                                item1.setIsCheck(false);
                            }
                            for(TinhHome item: mTinhs)
                            {
                                if(item.getId()==Pasgo.getInstance().mTinhId)
                                {
                                    mTinhName = item.getTen();
                                    mTvTinh.setText(mTinhName);
                                    item.setIsCheck(true);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    getTinhAll();
                    break;
                case 2:
                    if(!StringUtils.isEmpty(mTinhName))
                        mTvTinh.setText(mTinhName);
                    //handleLoadData.sendEmptyMessage(KEY_TINH);
                    break;
                default:
                    break;
            }
        };
    };
    private void getTinhAll() {
        String url = WebServiceUtils
                .URL_GET_TINH_ALLV1(Pasgo.getInstance().token);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        JSONObject jsonParams = new JSONObject();

        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Pasgo.getInstance().prefs.putTinhMain(response.toString());
                        if(mTinhs==null|| mTinhs.size()==0) {
                            mTinhs = ParserUtils.getTinhAllV1(response);
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

        }
    }
    //endregin
}