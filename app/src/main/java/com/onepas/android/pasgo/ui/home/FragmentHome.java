package com.onepas.android.pasgo.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.models.HomeGroupCategory;
import com.onepas.android.pasgo.models.ItemTinKhuyenMai;
import com.onepas.android.pasgo.models.Tinh;
import com.onepas.android.pasgo.models.TinhHome;
import com.onepas.android.pasgo.ui.BaseFragment;
import com.onepas.android.pasgo.ui.account.AccountManagerActivity;
import com.onepas.android.pasgo.ui.announcements.AnnouncementsActivity;
import com.onepas.android.pasgo.ui.calleddrivers.CalledDriverActivity;
import com.onepas.android.pasgo.ui.category.CategoryActivity;
import com.onepas.android.pasgo.ui.guid.GuidActivity;
import com.onepas.android.pasgo.ui.partner.DetailActivity;
import com.onepas.android.pasgo.ui.pasgocard.ThePasgoActivity;
import com.onepas.android.pasgo.ui.reserve.TinhAdapter;
import com.onepas.android.pasgo.ui.reserved.ReservedHistoryActivity;
import com.onepas.android.pasgo.ui.search.DatabaseHandler;
import com.onepas.android.pasgo.ui.setting.SettingActivity;
import com.onepas.android.pasgo.ui.share.ShareActivity;
import com.onepas.android.pasgo.ui.successfultrips.SuccessfulTripsActivity;
import com.onepas.android.pasgo.ui.termsandpolicies.TermsAndPoliciesActivity;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.ExpandableHeightListView;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "FragmentHome";
    private View mRoot;
    private LinearLayout mLnTinh;
    private LinearLayout mLnToolbar;
    private TextView mTvNumberTB;
    private RelativeLayout mRelativeTB;
    public int mTinhId = 0;
    private String mTinhName = "";
    private TextView mTvTinh;
    private LinearLayout mLnDataLoading;
    private ArrayList<TinhHome> mTinhs;
    private Dialog mDialogTinh;
    private TinhAdapter mTinhAdapter;
    private final int KEY_TINH = 1;
    private ArrayList<HomeGroupCategory> mHomeGroupCategories;
    private LinearLayout mLnDisconnect;
    private Button mBtnTryAGain;
    private final int KEY_LOADING =10;
    private final int KEY_DISCONNECT =11;
    private final int KEY_DATA =9;
    // filter
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        onNetworkChanged();
        if (Pasgo.getInstance().mTinhId == 0)
            Pasgo.getInstance().mTinhId = Pasgo.getInstance().prefs.getTinhId();
        if (mTinhs == null || mTinhs.size() == 0)
            getTinhAll();
        handlerUpdateUI.sendEmptyMessage(0);
        if (mHomeGroupCategories==null||mHomeGroupCategories.size() == 0 ||(mTinhId!=Pasgo.getInstance().mTinhId)) {
            if(mHomeGroupCategories!=null)
                mHomeGroupCategories.clear();
            getDanhMucTrangChu();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_layout_home_home, container, false);
            mLnDataLoading = (LinearLayout) mRoot.findViewById(R.id.data_loading_ln);
            mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            mLnToolbar = (LinearLayout) mRoot.findViewById(R.id.tool_bar_ln);
            mLnErrorConnectNetwork = (LinearLayout) mRoot.findViewById(R.id.lnErrorConnectNetwork);
            mLnTinh = (LinearLayout) mRoot.findViewById(R.id.tinh_ln);
            mTvTinh = (TextView) mRoot.findViewById(R.id.tinh_tv);
            mTvNumberTB = (TextView) mRoot.findViewById(R.id.text);
            mRelativeTB = (RelativeLayout) mRoot.findViewById(R.id.circle);
            mRoot.findViewById(R.id.rlthong_bao).setOnClickListener(v->{
                gotoActivityForResult(mActivity, AnnouncementsActivity.class,
                        Constants.KEY_BACK_FROM_TIN_KM);
                ourLeftInLeft(mActivity);
            });
            mRoot.findViewById(R.id.im_thong_bao).setOnClickListener(v->{
                gotoActivityForResult(mActivity, AnnouncementsActivity.class,
                        Constants.KEY_BACK_FROM_TIN_KM);
                ourLeftInLeft(mActivity);
            });
            mRoot.findViewById(R.id.circle).setOnClickListener(v->{
                gotoActivityForResult(mActivity, AnnouncementsActivity.class,
                        Constants.KEY_BACK_FROM_TIN_KM);
                ourLeftInLeft(mActivity);
            });
            mLnDisconnect = (LinearLayout)mRoot.findViewById(R.id.lnDisconnect);
            mBtnTryAGain = (Button)mRoot.findViewById(R.id.btnTryAGain);
            mBtnTryAGain.setOnClickListener(v->{
                getDanhMucTrangChu();
            });
            mTvNumberTB.setOnClickListener(this);
            mRoot.findViewById(R.id.google_play_ln).setOnClickListener(v->{
                openAppRating(mActivity);
            });
            mRoot.findViewById(R.id.google_play_img).setOnClickListener(v->{
                openAppRating(mActivity);
            });
            mRoot.findViewById(R.id.home_filter_ln).setOnClickListener(v->{
                gotoActivity(mActivity, FilterActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ourLeftInLeft(mActivity);
            });
            initializeMenu();
            getListKM();
        }
        return mRoot;
    }
    public void openAppRating(Context context) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+context.getPackageName()));
            context.startActivity(webIntent);
        }
    }
    @Override
    protected void initView() {
        super.initView();
        mLnTinh.setOnClickListener(this);
    }

    private void initializeMenu() {
        mToolbar.setNavigationIcon(R.drawable.icon_menu);
        HomeActivity activity = (HomeActivity) getActivity();
        activity.mNavigationDrawerFragment.setUp(R.id.drawer_fragment, activity.mDrawerLayout, mToolbar);
        activity.mNavigationDrawerFragment.menuDatXe.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            activity.goToDatXe(activity.KEY_SERVICE_TAXI);
        });
        activity.mNavigationDrawerFragment.menuCategory.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, CategoryActivity.class);
        });
        activity.mNavigationDrawerFragment.menuTaiTro.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, ThePasgoActivity.class);
        });
        activity.mNavigationDrawerFragment.menuLichSuChuyenDi.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, SuccessfulTripsActivity.class);
        });
        activity.mNavigationDrawerFragment.menuHuongDanSuDung.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, GuidActivity.class);
        });

        activity.mNavigationDrawerFragment.menuQuyDinhChung.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, TermsAndPoliciesActivity.class);
        });
        activity.mNavigationDrawerFragment.menuGioiThieuBanBe.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, ShareActivity.class);
        });
        activity.mNavigationDrawerFragment.menuThietLap.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUNDLE_LANGUAGE_BEFOR, Pasgo.getInstance().language);
            bundle.putBoolean(Constants.BUNDLE_IS_SHOW_KHUYEN_MAI, activity.mIsShowKhuyenMai);
            gotoActivity(mActivity, SettingActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        });
        activity.mNavigationDrawerFragment.menuThoat.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            activity.menuThoat();
        });
        activity.mNavigationDrawerFragment.menuYeuThich.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, FavoriteActivity.class);
        });
        activity.mNavigationDrawerFragment.dat_truoc_rl.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, CalledDriverActivity.class);
        });
        activity.mNavigationDrawerFragment.check_in_rl.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, ReservedHistoryActivity.class);
        });
        activity.mNavigationDrawerFragment.lnAccount.setOnClickListener(view -> {
            activity.mDrawerLayout.closeDrawers();
            gotoActivityClearTop(mActivity, AccountManagerActivity.class);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                default:
                    break;
            }
        }
        if (requestCode == Constants.KEY_BACK_FROM_TIN_KM) {
            getListKM();
        }
    }

    @Override
    public void onNetworkChanged() {
        if (getActivity() == null || mLnErrorConnectNetwork == null)
            return;
        if (NetworkUtils.getInstance(getActivity()).isNetworkAvailable()) {
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tinh_ln:
                showDialogTinh();
                break;
            default:
                break;
        }
    }

    private synchronized void viewData() {
        // danh sách id của View
        LinearLayout myLayout1 = (LinearLayout) mRoot.findViewById(R.id.layout1);
        LinearLayout myLayout2 = (LinearLayout) mRoot.findViewById(R.id.layout2);
        LinearLayout myLayout3 = (LinearLayout) mRoot.findViewById(R.id.layout3);
        LinearLayout myLayout4 = (LinearLayout) mRoot.findViewById(R.id.layout4);
        LinearLayout myLayout5 = (LinearLayout) mRoot.findViewById(R.id.layout5);
        LinearLayout myLayout6 = (LinearLayout) mRoot.findViewById(R.id.layout6);
        LinearLayout myLayout7 = (LinearLayout) mRoot.findViewById(R.id.layout7);
        LinearLayout myLayout8 = (LinearLayout) mRoot.findViewById(R.id.layout8);
        LinearLayout myLayout9 = (LinearLayout) mRoot.findViewById(R.id.layout9);
        // xóa hết các view trước khi add lại theo danh sách từ server trả về
        myLayout1.removeAllViews();
        myLayout2.removeAllViews();
        myLayout3.removeAllViews();
        myLayout4.removeAllViews();
        myLayout5.removeAllViews();
        myLayout6.removeAllViews();
        myLayout7.removeAllViews();
        myLayout8.removeAllViews();
        myLayout9.removeAllViews();
        //add view theo SapXep từ server
        //1: slide ngang, 2: row, 3: column, 4: tag, 5 grid tag
        for(int i=0;i<mHomeGroupCategories.size();i++)
        {
            HomeGroupCategory item = mHomeGroupCategories.get(i);
            int layoutNumber = i+1;
            if(item.getHomeCategories().size()==0)
                continue;
            switch (layoutNumber)
            {
                case 1:
                    addViewToLayout(myLayout1, item);
                    break;
                case 2:
                    addViewToLayout(myLayout2, item);
                    break;
                case 3:
                    addViewToLayout(myLayout3, item);
                    break;

                case 4:
                    addViewToLayout(myLayout4, item);
                    break;

                case 5:
                    addViewToLayout(myLayout5, item);
                    break;

                case 6:
                    addViewToLayout(myLayout6, item);
                    break;

                case 7:
                    addViewToLayout(myLayout7, item);
                    break;
                case 8:
                    addViewToLayout(myLayout8, item);
                    break;
                case 9:
                    addViewToLayout(myLayout9, item);
                    break;
            }

        }
        initView();
        handleCategory.sendEmptyMessage(KEY_DATA);
    }
    //7 các loại view

    private void addViewToLayout(LinearLayout myLayout, HomeGroupCategory item)
    {
        View itemViewSlide = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_slide, null, false);
        View itemViewTagType21 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_tag_type21, null, false);
        View itemViewTagType22 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_tag_type22, null, false);
        View itemViewColumn31 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_column1, null, false);
        View itemViewColumn32 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_column2, null, false);
        View itemViewColumn33 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_column3, null, false);
        View itemViewTag1 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_tag1, null, false);
        View itemViewTag2 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_tag2, null, false);
        View itemViewGridBoSuuTap = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_grid_bo_suu_tap, null, false);
        View itemViewType81 = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_tag_type81, null, false);
        switch (item.getLoaiGiaoDien())
        {
            case 1:
                if(item.getSoLanXuatHien()==1) {
                    myLayout.addView(itemViewSlide);
                    TextView tvCategorySlide = (TextView) mRoot.findViewById(R.id.category1_tv);
                    ArrayList<HomeCategory> homeCategoriesSlide = item.getHomeCategories();
                    //init view
                    RecyclerView mRcCategorySlide = (RecyclerView) mRoot.findViewById(R.id.category1_lv);
                    mRcCategorySlide.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    //setData
                    HomeCategorySlideAdapter mHomeCategoryAdapter1 = new HomeCategorySlideAdapter(mActivity, homeCategoriesSlide, new HomeCategorySlideAdapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            selectHomeCategoryItem(homeCategoriesSlide.get(position));
                        }
                    });
                    mRcCategorySlide.setAdapter(null);
                    mRcCategorySlide.setAdapter(mHomeCategoryAdapter1);
                    mHomeCategoryAdapter1.updateList(homeCategoriesSlide);
                    String groupName1 = item.getGroupName();
                    if (StringUtils.isEmpty(groupName1))
                        tvCategorySlide.setVisibility(View.GONE);
                    else
                        tvCategorySlide.setVisibility(View.VISIBLE);
                    tvCategorySlide.setText(groupName1);
                }
                break;
            case 2:// tag đặc biệt = xu hướng ẩm thực
                if(item.getSoLanXuatHien()==1) {
                    myLayout.addView(itemViewTagType21);
                    ArrayList<HomeCategory>  homeCategoriesColumn21 = item.getHomeCategories();
                    HomeCategory yeuThich = new HomeCategory();
                    yeuThich.setId(Constants.KEY_YEU_THICH);
                    yeuThich.setTieuDe(getString(R.string.yeu_thich));
                    homeCategoriesColumn21.add(yeuThich);
                    int xemThemId21 = item.getXemThemId();
                    String xemThemText21 =item.getGroupName();
                    //
                    RecyclerView rcCategoryColumn21 = (RecyclerView) mRoot.findViewById(R.id.category2_1_lv);
                    rcCategoryColumn21.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                    mRoot.findViewById(R.id.all_2_1_ln).setOnClickListener(v->{
                        gotoByTagOrText(xemThemId21, xemThemText21,"");
                    });
                    TextView tvNameColumn1 = (TextView)mRoot.findViewById(R.id.category2_1_tv);
                    tvNameColumn1.setText(xemThemText21);
                    rcCategoryColumn21.setNestedScrollingEnabled(false);
                    //setData
                    HomeCategoryTagType2Adapter mHomeCategoryAdapter21 = new HomeCategoryTagType2Adapter(mActivity, homeCategoriesColumn21, new HomeCategoryTagType2Adapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            String id = homeCategoriesColumn21.get(position).getId();
                            if(id.equals(Constants.KEY_YEU_THICH))
                            {
                                if(!isCheckLoginUser())
                                {
                                    Intent broadcastRegister = new Intent();
                                    broadcastRegister.setAction(Constants.BROADCAST_ACTION_REQUEST_LOGIN);
                                    mActivity.sendBroadcast(broadcastRegister);
                                    return;
                                }
                                gotoActivity(mActivity, FavoriteActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }

                            else
                                selectHomeCategoryItem(homeCategoriesColumn21.get(position));
                        }
                    });
                    rcCategoryColumn21.setAdapter(mHomeCategoryAdapter21);
                    mHomeCategoryAdapter21.updateList(homeCategoriesColumn21);
                    rcCategoryColumn21.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                        int numRow = homeCategoriesColumn21.size()/3;
                        if(homeCategoriesColumn21.size()%3>0)
                            numRow +=1;
                        calculateSwipeRefreshFullHeight(rcCategoryColumn21,numRow);
                    });
                }
                else
                if(item.getSoLanXuatHien()==2){// Ẩm thực
                    myLayout.addView(itemViewTagType22);
                    ArrayList<HomeCategory>  homeCategoriesColumn22 = item.getHomeCategories();
                    int xemThemId22 = item.getXemThemId();
                    String xemThemText22 =item.getGroupName();
                    //
                    RecyclerView rcCategoryColumn22 = (RecyclerView) mRoot.findViewById(R.id.category2_2_lv);
                    rcCategoryColumn22.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                    mRoot.findViewById(R.id.all_2_2_ln).setOnClickListener(v->{
                        gotoByTagOrText(xemThemId22, xemThemText22,"");
                    });
                    TextView tvNameColumn22 = (TextView)mRoot.findViewById(R.id.category2_2_tv);
                    tvNameColumn22.setText(xemThemText22);
                    rcCategoryColumn22.setNestedScrollingEnabled(false);
                    //setData
                    HomeCategoryTagType2Adapter mHomeCategoryAdapter22 = new HomeCategoryTagType2Adapter(mActivity, homeCategoriesColumn22, new HomeCategoryTagType2Adapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            selectHomeCategoryItem(homeCategoriesColumn22.get(position));
                        }
                    });
                    rcCategoryColumn22.setAdapter(mHomeCategoryAdapter22);
                    mHomeCategoryAdapter22.updateList(homeCategoriesColumn22);
                    rcCategoryColumn22.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                        int numRow = homeCategoriesColumn22.size()/3;
                        if(homeCategoriesColumn22.size()%3>0)
                            numRow +=1;
                        calculateSwipeRefreshFullHeight(rcCategoryColumn22,numRow);
                    });
                }
                break;
            case 3:
                if(item.getSoLanXuatHien()==1) {
                    myLayout.addView(itemViewColumn31);
                    ArrayList<HomeCategory>  homeCategoriesColumn31 = item.getHomeCategories();
                    int xemThemId31 = item.getXemThemId();
                    String xemThemText31 =item.getGroupName();
                    //
                    RecyclerView rcCategoryColumn31 = (RecyclerView) mRoot.findViewById(R.id.category3_1_lv);
                    rcCategoryColumn31.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                    mRoot.findViewById(R.id.all_3_1_ln).setOnClickListener(v->{
                        gotoByTagOrText(xemThemId31, xemThemText31,"");
                    });
                    TextView tvNameColumn1 = (TextView)mRoot.findViewById(R.id.all_3_1_tv);
                    tvNameColumn1.setText(xemThemText31);
                    rcCategoryColumn31.setNestedScrollingEnabled(false);
                    //setData
                    HomeCategoryColumnAdapter mHomeCategoryAdapter3 = new HomeCategoryColumnAdapter(mActivity, homeCategoriesColumn31, new HomeCategoryColumnAdapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            selectHomeCategoryItem(homeCategoriesColumn31.get(position));
                        }
                    });
                    rcCategoryColumn31.setAdapter(mHomeCategoryAdapter3);
                    mHomeCategoryAdapter3.updateList(homeCategoriesColumn31);
                    rcCategoryColumn31.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                        int numRow = homeCategoriesColumn31.size()/3;
                        if(homeCategoriesColumn31.size()%3>0)
                            numRow +=1;
                        calculateSwipeRefreshFullHeight(rcCategoryColumn31,numRow);
                    });
                }
                else
                if(item.getSoLanXuatHien()==2)
                {
                    myLayout.addView(itemViewColumn32);
                    ArrayList<HomeCategory> homeCategoriesColumn32 = item.getHomeCategories();
                    int xemThemId32 = item.getXemThemId();
                    String xemThemText32 =item.getGroupName();
                    //
                    RecyclerView rcCategoryColumn32 = (RecyclerView) mRoot.findViewById(R.id.category3_2_lv);
                    rcCategoryColumn32.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                    mRoot.findViewById(R.id.all_3_2_ln).setOnClickListener(v->{
                        gotoByTagOrText(xemThemId32, xemThemText32,"");
                    });
                    TextView tvNameColumn2 = (TextView)mRoot.findViewById(R.id.all_3_2_tv);
                    tvNameColumn2.setText(xemThemText32);
                    rcCategoryColumn32.setNestedScrollingEnabled(false);
                    //setData
                    HomeCategoryColumnAdapter mHomeCategoryAdapter5 = new HomeCategoryColumnAdapter(mActivity, homeCategoriesColumn32, new HomeCategoryColumnAdapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            selectHomeCategoryItem(homeCategoriesColumn32.get(position));
                        }
                    });
                    rcCategoryColumn32.setAdapter(mHomeCategoryAdapter5);
                    mHomeCategoryAdapter5.updateList(homeCategoriesColumn32);
                    rcCategoryColumn32.getViewTreeObserver().addOnGlobalLayoutListener(()->{
                            int numRow = homeCategoriesColumn32.size()/3;
                            if(homeCategoriesColumn32.size()%3>0)
                                numRow +=1;
                            calculateSwipeRefreshFullHeight(rcCategoryColumn32,numRow);
                    });
                }else
                    if(item.getSoLanXuatHien()==3){
                        myLayout.addView(itemViewColumn33);
                        ArrayList<HomeCategory> homeCategoriesColumn33 = item.getHomeCategories();
                        int xemThemId33 = item.getXemThemId();
                        String xemThemText33 =item.getGroupName();
                        //
                        RecyclerView rcCategoryColumn33 = (RecyclerView) mRoot.findViewById(R.id.category3_3_lv);
                        rcCategoryColumn33.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                        mRoot.findViewById(R.id.all_3_3_ln).setOnClickListener(v->{
                            gotoByTagOrText(xemThemId33, xemThemText33,"");
                        });
                        TextView tvNameColumn33 = (TextView)mRoot.findViewById(R.id.all_3_3_tv);
                        tvNameColumn33.setText(xemThemText33);
                        rcCategoryColumn33.setNestedScrollingEnabled(false);
                        //setData
                        HomeCategoryColumnAdapter mHomeCategoryAdapter5 = new HomeCategoryColumnAdapter(mActivity, homeCategoriesColumn33, new HomeCategoryColumnAdapter.HomeCategoryListener() {
                            @Override
                            public void detail(int position) {
                                selectHomeCategoryItem(homeCategoriesColumn33.get(position));
                            }
                        });
                        rcCategoryColumn33.setAdapter(mHomeCategoryAdapter5);
                        mHomeCategoryAdapter5.updateList(homeCategoriesColumn33);
                        rcCategoryColumn33.getViewTreeObserver().addOnGlobalLayoutListener(()->{
                            int numRow = homeCategoriesColumn33.size()/3;
                            if(homeCategoriesColumn33.size()%3>0)
                                numRow +=1;
                            calculateSwipeRefreshFullHeight(rcCategoryColumn33,numRow);
                        });
                    }
                break;
            case 4:
                if(item.getSoLanXuatHien()==1) {
                    myLayout.addView(itemViewTag1);
                    ArrayList<HomeCategory> homeCategoriesTag1 = item.getHomeCategories();
                    //
                    RecyclerView rcCategoryTag1 = (RecyclerView) mRoot.findViewById(R.id.category_tag1_lv);
                    rcCategoryTag1.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    rcCategoryTag1.setNestedScrollingEnabled(false);
                    //setData
                    HomeCategoryTagAdapter mHomeCategoryTagAdapter1 = new HomeCategoryTagAdapter(mActivity, homeCategoriesTag1, new HomeCategoryTagAdapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            selectHomeCategoryItem(homeCategoriesTag1.get(position));
                        }
                    });
                    rcCategoryTag1.setAdapter(null);
                    rcCategoryTag1.setAdapter(mHomeCategoryTagAdapter1);
                    mHomeCategoryTagAdapter1.updateList(homeCategoriesTag1);
                }
                else
                if(item.getSoLanXuatHien()==2){
                    myLayout.addView(itemViewTag2);
                    ArrayList<HomeCategory> homeCategoriesTag2 = item.getHomeCategories();
                    //
                    RecyclerView rcCategoryTag2 = (RecyclerView) mRoot.findViewById(R.id.category_tag2_lv);
                    rcCategoryTag2.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    rcCategoryTag2.setNestedScrollingEnabled(false);
                    //setData
                    HomeCategoryTagAdapter mHomeCategoryTagAdapter2 = new HomeCategoryTagAdapter(mActivity, homeCategoriesTag2, new HomeCategoryTagAdapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            selectHomeCategoryItem(homeCategoriesTag2.get(position));
                        }
                    });
                    rcCategoryTag2.setAdapter(null);
                    rcCategoryTag2.setAdapter(mHomeCategoryTagAdapter2);
                    mHomeCategoryTagAdapter2.updateList(homeCategoriesTag2);
                }
                break;
            case 5:
                if(item.getSoLanXuatHien()==1) {
                    myLayout.addView(itemViewGridBoSuuTap);
                    ArrayList<HomeCategory> homeCategoriesGridBooSuuTap = item.getHomeCategories();
                    //
                    RecyclerView rcCategoryGridBoSuuTap = (RecyclerView) mRoot.findViewById(R.id.category_bo_suu_tap_lv);
                    rcCategoryGridBoSuuTap.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    rcCategoryGridBoSuuTap.setNestedScrollingEnabled(false);
                    TextView tvName = (TextView) mRoot.findViewById(R.id.bo_suu_tap_tat_ca_tv);
                    tvName.setText(item.getGroupName());
                    mRoot.findViewById(R.id.all_bo_suu_tap_ln).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.BUNDLE_KEY_GROUP_ID, item.getGroupId());
                        bundle.putString(Constants.BUNDLE_KEY_GROUP_NAME, item.getGroupName());
                        gotoActivity(mActivity, HomeBoSuuTapActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ourLeftInLeft(mActivity);
                    });
                    //setData
                    HomeCategoryGridBoSuuTapAdapter mHomeCategoryAdapter7 = new HomeCategoryGridBoSuuTapAdapter(mActivity, homeCategoriesGridBooSuuTap, new HomeCategoryGridBoSuuTapAdapter.HomeCategoryListener() {
                        @Override
                        public void detail(int position) {
                            selectHomeCategoryItem(homeCategoriesGridBooSuuTap.get(position));
                        }
                    });
                    rcCategoryGridBoSuuTap.setAdapter(null);
                    rcCategoryGridBoSuuTap.setAdapter(mHomeCategoryAdapter7);
                    mHomeCategoryAdapter7.updateList(homeCategoriesGridBooSuuTap);
                    rcCategoryGridBoSuuTap.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                        int numRow = homeCategoriesGridBooSuuTap.size() / 2;
                        if (homeCategoriesGridBooSuuTap.size() % 2 > 0)
                            numRow += 1;
                        calculateSwipeRefreshFullHeight(rcCategoryGridBoSuuTap, numRow);
                    });
                }
                break;
            case 8:
                if(item.getSoLanXuatHien()==1) {
                    myLayout.addView(itemViewType81);
                    ArrayList<HomeCategory>  homeCategories81 = item.getHomeCategories();
                    //
                    ExpandableHeightListView rcCategory81 = (ExpandableHeightListView) mRoot.findViewById(R.id.category8_1_lv);
                    TextView tvName81 = (TextView)mRoot.findViewById(R.id.category8_1_tv);

                    String str81 = item.getGroupName();
                    if(TextUtils.isEmpty(str81)) {
                        tvName81.setText("");
                        tvName81.setVisibility(View.GONE);
                    }else {
                        tvName81.setText(str81);
                        tvName81.setVisibility(View.VISIBLE);
                    }
                    rcCategory81.setExpanded(true);
                    //setData
                    HomeCategory8Adapter mHomeCategoryAdapter81 = new HomeCategory8Adapter(mActivity, homeCategories81);
                    rcCategory81.setAdapter(mHomeCategoryAdapter81);
                    mHomeCategoryAdapter81.updateList(homeCategories81);
                    rcCategory81.setOnItemClickListener((parent, view, position, id) -> {
                        selectHomeCategoryItem(homeCategories81.get(position));
                    });
                }

                break;
        }

    }
    private void calculateSwipeRefreshFullHeight(RecyclerView recyclerView, int column) {
        if(recyclerView ==null) return;
        int height = 0;
        for (int idx = 0; idx < column; idx++ ) {
            View v = recyclerView.getChildAt(idx);
            if(v!=null)
                height += v.getHeight();
        }
        ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
        params.height = height;
        recyclerView.setLayoutParams(params);
    }
    private final Handler handleCategory = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case KEY_DATA:
                        mLnDisconnect.setVisibility(View.GONE);
                        mLnDataLoading.setVisibility(View.GONE);
                        break;
                    case KEY_LOADING:
                        mLnDataLoading.setVisibility(View.VISIBLE);
                        mLnDisconnect.setVisibility(View.GONE);
                        break;
                    case KEY_DISCONNECT:
                        mLnDataLoading.setVisibility(View.GONE);
                        mLnDisconnect.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Utils.Log(Pasgo.TAG, e.toString());
            }
            super.handleMessage(msg);
        }
    };

    private void selectHomeCategoryItem(HomeCategory item) {
        if (item.isDoiTacKhuyenMai()) {
            String doiTacKMID = item.getDoiTacKhuyenMaiId();
            String title = item.getTieuDe();
            String chiNhanhDoiTacId = "";
            int nhomKhuyenMai = 3;//item.getNhomKhuyenMaiId()
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_DI_XE_FREE, Constants.KEY_INT_XE_FREE);
            bundle.putInt(Constants.KEY_TEN_TINH_ID, mTinhId);
            bundle.putBoolean(Constants.BUNDLE_KEY_LIST_DIA_DIEM_KM, true);
            bundle.putString(Constants.BUNDLE_KEY_DOI_TAC_KHUYEN_MAI_ID, doiTacKMID);
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC_KM, title);
            bundle.putString(Constants.BUNDLE_KEY_ID, chiNhanhDoiTacId);
            bundle.putInt(Constants.BUNDLE_KEY_NHOM_KM_ID, nhomKhuyenMai);
            bundle.putString(Constants.BUNDLE_KEY_TEN_DOI_TAC, item.getMoTa());
            bundle.putString(Constants.BUNDLE_KEY_MA_KHUYEN_MAI, "");
            bundle.putBoolean(Constants.BUNDLE_KEY_DIEM_TAI_TRO_NGUOI_DUNG, false);
            bundle.putBoolean(Constants.BUNDLE_KEY_NGUOI_DUNG_DI_TU_DO, false);
            gotoActivityForResult(mActivity, DetailActivity.class, bundle,
                    Constants.KEY_BACK_BY_MA_DAT_CHO, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ourLeftInLeft(mActivity);
        } else {
            gotoByTagOrText(item.getTagId(),item.getTieuDe(),item.getMoTa());
        }

    }
    private void gotoByTagOrText(int tagId, String tieuDe, String mota){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY_TAG_ID, tagId);
        bundle.putString(Constants.BUNDLE_KEY_SEARCH_TEXT, tieuDe);
        bundle.putString(Constants.BUNDLE_KEY_MO_TA, mota);
        gotoActivity(getActivity(), HomeDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ourLeftInLeft(mActivity);
    }
    private void showDialogTinh() {
        if (getActivity() == null || mTinhs == null || mTinhs.size() == 0)
            return;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        View dialog = inflater.inflate(R.layout.layout_listview, null);
        alertDialog.setTitle(getString(R.string.vi_tri));
        alertDialog.setView(dialog);
        final ListView lv = (ListView) dialog.findViewById(R.id.listView);
        mTinhAdapter = new TinhAdapter(getActivity(), mTinhs, position -> {
            for (TinhHome item : mTinhs) {
                item.setIsCheck(false);
            }
            mTinhs.get(position).setIsCheck(true);
            Pasgo.getInstance().mTinhId = mTinhs.get(position).getId();
            if (Pasgo.getInstance().mTinhId > 0)
                Pasgo.getInstance().prefs.putTinhId(Pasgo.getInstance().mTinhId);
            mTinhName = mTinhs.get(position).getTen();
            mTinhAdapter.notifyDataSetChanged();
            lv.invalidateViews();
            lv.refreshDrawableState();
            handlerUpdateUI.sendEmptyMessage(1);
            mDialogTinh.dismiss();
        });
        lv.setAdapter(mTinhAdapter);
        if (mDialogTinh == null)
            mDialogTinh = alertDialog.create();
        if (mDialogTinh != null && !mDialogTinh.isShowing() && !getActivity().isFinishing()) {
            mDialogTinh = alertDialog.create();
            mDialogTinh.show();
        }
    }

    Handler handlerUpdateUI = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String tinh = Pasgo.getInstance().prefs.getTinhMain();
                    if (!StringUtils.isEmpty(tinh)) {
                        try {
                            JSONObject jsonObjectTinh = new JSONObject(tinh);
                            mTinhs = ParserUtils.getTinhAllV1(jsonObjectTinh);
                            for (TinhHome item1 : mTinhs) {
                                item1.setIsCheck(false);
                            }
                            for (TinhHome item : mTinhs) {
                                if (item.getId() == Pasgo.getInstance().mTinhId) {
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

                    break;
                case 1:
                    if (!StringUtils.isEmpty(mTinhName))
                        mTvTinh.setText(mTinhName);
                    handleLoadData.sendEmptyMessage(KEY_TINH);
                    updateTinhSelected();
                    break;
                case 2:
                    viewData();
                    break;
                default:
                    break;
            }
        }

        ;
    };
    protected Handler handleLoadData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY_TINH:
                    getDanhMucTrangChu();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private void getDanhMucTrangChu() {
        handleCategory.sendEmptyMessage(KEY_LOADING);
        String url = WebServiceUtils
                .URL_GET_DANH_MUC_TRANG_CHU(Pasgo.getInstance().token);
        DeviceUuidFactory factory = new DeviceUuidFactory(mActivity);
        if (StringUtils.isEmpty(Pasgo.getInstance().userId))
            Pasgo.getInstance().userId = "";
        double viDo = Double.parseDouble(Pasgo.getInstance().prefs
                .getLatLocationRecent());
        double kinhDo = Double.parseDouble(Pasgo.getInstance().prefs
                .getLngLocationRecent());
        JSONObject jsonParams = new JSONObject();
        mTinhId = Pasgo.getInstance().mTinhId;
        try {
            jsonParams.put("viDo", viDo);
            jsonParams.put("kinhDo", kinhDo);
            jsonParams.put("tinhId", mTinhId);
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("deviceId", factory.getDeviceUuid());
            jsonParams.put("loadNewLetter", 0);
            jsonParams.put("loadAppVersion", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mLnDataLoading.setVisibility(View.VISIBLE);
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mHomeGroupCategories = ParserUtils.getHomeGroupCategories(response);
                        handlerUpdateUI.sendEmptyMessage(2);
                    }

                    @Override
                    public void onError(int maloi) {
                        handleCategory.sendEmptyMessage(KEY_DISCONNECT);
                    }

                }, new Pasgo.PWErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleCategory.sendEmptyMessage(KEY_DISCONNECT);
                    }
                });
    }

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
                        if (mTinhs == null || mTinhs.size() == 0) {
                            mTinhs = ParserUtils.getTinhAllV1(response);
                        }
                        handlerUpdateUI.sendEmptyMessage(0);
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

    public void getListKM() {
        final DatabaseHandler databaseHandler = new DatabaseHandler(mActivity);

        final ArrayList<ItemTinKhuyenMai> mListKM = new ArrayList<ItemTinKhuyenMai>();
        DeviceUuidFactory factory = new DeviceUuidFactory(mActivity);
        String url = WebServiceUtils
                .URL_Tin_Khuyen_Mai(Pasgo.getInstance().token);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
            jsonParams.put("deviceId", factory.getDeviceUuid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Pasgo.getInstance().addToRequestQueue(url, jsonParams,
                new Pasgo.PWListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.Log("", "response tin khuyen mai" + response);
                        try {
                            JSONArray array = ParserUtils.getJsonArray(
                                    response, "Items");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                String Id = jsonObject.getString("Id");
                                String TieuDe = jsonObject.getString("TieuDe");

                                int read = 0;

                                boolean tableExists = databaseHandler
                                        .existsTable();
                                if (tableExists == true) {
                                    read = databaseHandler.getReadTinKM(Id);
                                }

                                ItemTinKhuyenMai item = new ItemTinKhuyenMai();
                                item.setId(Id);
                                item.setTieuDe(TieuDe);
                                item.setRead(read);
                                mListKM.add(item);

                            }
                            int count = 0;
                            for (int i = 0; i < mListKM.size(); i++) {
                                ItemTinKhuyenMai item = mListKM.get(i);
                                int read = item.getRead();
                                if (read == 0) {
                                    count += 1;
                                }
                            }
                            if (count > 0) {
                                mRelativeTB.setVisibility(View.VISIBLE);
                                mTvNumberTB.setText("" + count);
                            } else {
                                mRelativeTB.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
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