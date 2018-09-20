package com.onepas.android.pasgo.ui.home;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;

public class NavigationDrawerFragment extends Fragment {
    private LinearLayout mDrawerLinnerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    public LinearLayout lnAccount;
    public TextView menuDatXe;
    public TextView menuCategory;
    public TextView menuTaiTro;
    public TextView menuCheckIn;
    public TextView menuLichSuChuyenDi;
    public TextView menuHuongDanSuDung;
    public TextView menuQuyDinhChung;
    public TextView menuGioiThieuBanBe;
    public TextView menuThietLap;
    public TextView menuThoat;
    public TextView menuYeuThich;
    public RelativeLayout dat_truoc_rl;
    public RelativeLayout check_in_rl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDrawerLinnerLayout = (LinearLayout)inflater.inflate(R.layout.menu_scrollview, container, false);
        menuDatXe = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuDatXe);
        menuCategory = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuCategory);
        menuTaiTro = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuTaiTro);
        menuCheckIn = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuCheckIn);
        menuLichSuChuyenDi = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuLichSuChuyenDi);
        menuHuongDanSuDung = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuHuongDanSuDung);
        menuQuyDinhChung = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuTongQuanVePasGo);
        menuGioiThieuBanBe = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuGioiThieuBanBe);
        menuThietLap = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuThietLap);
        menuThoat = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuThoat);
        dat_truoc_rl = (RelativeLayout) mDrawerLinnerLayout.findViewById(R.id.called_driver_rl);
        check_in_rl = (RelativeLayout) mDrawerLinnerLayout.findViewById(R.id.reserved_rl);
        lnAccount = (LinearLayout) mDrawerLinnerLayout.findViewById(R.id.lnAccount);
        menuYeuThich = (TextView) mDrawerLinnerLayout.findViewById(R.id.menuYeuThich);
        return mDrawerLinnerLayout;
    }
    public void setUp(int fragmentId,DrawerLayout drawerLayout,Toolbar toolbar){
        setActionBar(toolbar);
        drawerLayout.setDrawerShadow(R.color.day_layout_bg, GravityCompat.START);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,android.R.string.no,android.R.string.no);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);
    }
    private void setActionBar(Toolbar toolbar){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }
    private ActionBar getActionBar(){
        return ((AppCompatActivity)getActivity()).getSupportActionBar();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mActionBarDrawerToggle.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}