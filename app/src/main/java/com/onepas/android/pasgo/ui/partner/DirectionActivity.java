package com.onepas.android.pasgo.ui.partner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.PTIcon;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.util.mapnavigator.MapUtil;
import com.onepas.android.pasgo.widgets.MySupportMapFragment;

import java.util.HashMap;

public class DirectionActivity extends BaseAppCompatActivity implements
        OnClickListener, OnMapReadyCallback {
    private static final String TAG = "DirectionActivity";
    private double mViDo, mKinhDo;
    private GoogleMap mGoogleMap;
    private Marker mMarkerCurrent;
    private String mMaNhomKhuyenMai;
    private HashMap<String, Integer> mImageReason;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_derection);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("");
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.chi_duong));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null)
            bundle = savedInstanceState;
        if (bundle != null) {
            mViDo = bundle.getDouble(Constants.BUNDLE_KEY_VI_DO);
            mKinhDo = bundle.getDouble(Constants.BUNDLE_KEY_KINH_DO);
            mMaNhomKhuyenMai = bundle.getString(Constants.BUNDLE_KEY_MA_NHOM_KHUYEN_MAI,"03");
        }
        this.initView();
        this.initControl();
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
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putDouble(Constants.BUNDLE_KEY_KINH_DO, mKinhDo);
        bundle.putDouble(Constants.BUNDLE_KEY_VI_DO, mViDo);
        bundle.putString(Constants.BUNDLE_KEY_MA_NHOM_KHUYEN_MAI, mMaNhomKhuyenMai);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        mImageReason = new HashMap<String, Integer>();
    }


    @Override
    protected void initControl() {
        // TODO Auto-generated method stub
        super.initControl();
        mImageReason = PTIcon.getInstanceIconReason();
        // init map
        MySupportMapFragment fragment = (MySupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_direction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishToRightToLeft();
                return true;
            case R.id.menu_item_direction:
                MapUtil.googleNavigation(mContext, mViDo + "", mKinhDo + "");
                break;
        }

        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int status = googleAPI.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            googleAPI.getErrorDialog(this, status,
                    Constants.kEY_BACK_FORM_GOOGLEPLAYSERVICE).show();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
        }
        handleUpdateUI.sendEmptyMessage(0);
        handleUpdateUI.sendEmptyMessage(1);
    }
    private void setMarkerCurrent() {
        if(!mImageReason.containsKey(mMaNhomKhuyenMai)) return;
        if (getBaseContext() == null || mGoogleMap == null
                || mImageReason == null)
            return;
            int drawable = mImageReason.get(mMaNhomKhuyenMai);
            LatLng latLng = new LatLng(mViDo,mKinhDo);
            if (mMarkerCurrent == null)
                mMarkerCurrent = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(drawable))
                        .snippet("-2"));
            else
                mMarkerCurrent.setPosition(latLng);
    }

    Handler handleUpdateUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                {
                    final LatLng latLng = new LatLng(mViDo, mKinhDo);
                    if (mGoogleMap != null) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(14).build();
                        mGoogleMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(cameraPosition),
                                5, new GoogleMap.CancelableCallback() {

                                    @Override
                                    public void onFinish() {

                                        LatLngBounds bounds = mGoogleMap
                                                .getProjection().getVisibleRegion().latLngBounds;
                                        if (bounds != null) {
                                        }
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                    }
                }
                case 1: {
                    setMarkerCurrent();
                    break;
                }
            }
        }
    };

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

    @Override
    public void onStartMoveScreen() {

    }

    @Override
	public void onUpdateMapAfterUserInterection() {
	}


}