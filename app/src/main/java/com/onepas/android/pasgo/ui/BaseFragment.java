package com.onepas.android.pasgo.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.utils.BaseProgressDialogAllUtils;
import com.onepas.android.pasgo.utils.DeviceUuidFactory;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;
import com.onepas.android.pasgo.widgets.ProgressDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseFragment extends Fragment implements
		NetworkChangedListener, View.OnClickListener {
	private static final String TAG="BaseFragment";
	protected Activity mActivity;
	private Display display;
	protected int screenWidth, screenHeight;
	protected ProgressDialogFragment mProgressDialog;
	protected static final String DIALOG_TAG = "dialog";
	protected static final String MESSAGE_CONNECT = "message connect";
	protected LinearLayout mLnErrorConnectNetwork;
	protected LinearLayout mLnNotiConnectChannel;
	protected TextView mTvTitleStateConnectChannel;
	protected TextView mTvNetworkError;
	private boolean mIsXLargeDevice;
	protected boolean mFirstLaunch = false;
	protected BaseProgressDialogAllUtils progressDialog;
	protected Toolbar mToolbar;
	protected boolean mIsErrorChannel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4)
			mIsXLargeDevice = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	protected void initControl() {
	}

	protected void initView() {
	}
	protected boolean isCheckLoginUser() {
		return Pasgo.isLogged() && !StringUtils.isEmpty(Pasgo.getInstance().userId);
	}
	protected void calculateScreen() {
		display = getActivity().getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		if (screenWidth > screenHeight) {
			int temp = screenHeight;
			screenHeight = screenWidth;
			screenWidth = temp;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentNetworkFilter = new IntentFilter(
				Constants.BROADCAST_ACTION_CHANGE_NETWORK);
		getActivity().registerReceiver(broadcastReceiverChangeNetwork,
				intentNetworkFilter);
	}

	@Override
	public void onPause() {
		getActivity().unregisterReceiver(broadcastReceiverChangeNetwork);
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private final BroadcastReceiver broadcastReceiverChangeNetwork = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			handleNetworkChange.sendEmptyMessage(0);
		}
	};



	protected Handler handleNetworkChange = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onNetworkChanged();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void doNegativeClickPopupTool(DialogInterface dialog) {
	}

	public void doPositiveClick(DialogInterface arg0) {
	}

	public void doNegativeClick(DialogInterface arg0) {
	}

	public void doNegativeClickWaiting(DialogInterface dialog) {
	}

	protected void showDialog()
	{
		String message = Utils.getStringByResourse(getActivity(),
				R.string.loading_data);
		if (progressDialog == null) {
			progressDialog = new BaseProgressDialogAllUtils(getActivity());
			progressDialog.setCancelable(false);
			progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						dialog.dismiss();
					}
					return false;
				}
			});
			progressDialog.show();
		} else {
			if (!progressDialog.isShowing())
				progressDialog.show();
		}
	}
	public void dismissDialog() {

		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog.cancel();
				progressDialog = null;
			}
		} catch (Exception e) {
			Utils.Log(TAG, e.toString());
		}
	}
	public void finishOurLeftInLeft(Activity activity)
	{
		activity.finish();
		activity.overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
	}
	public void finishToRightToLeft(Activity activity)
	{
		activity.finish();
		activity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
	}
	public void finishPushDownInPushDownOut(Activity activity)
	{
		activity.finish();
		activity.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
	public void ourLeftInLeft(Activity activity)
	{
		activity.overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
	}
	public void pushDownInPushDownOut(Activity activity)
	{
		activity.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
	public void gotoActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
	public void gotoActivity(Context context, Class<?> cla, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
	public void gotoActivity(Context context, Class<?> cla,Bundle bundle,int flag ) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		intent.setFlags(flag);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
	public void gotoActivityClearTop(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityWithDelay(intent);
	}
	public void gotoActivityClearTop(Context context, Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtras(bundle);
		startActivityWithDelay(intent);
	}
	protected void startActivityWithDelay(final Intent i) {
		if (mIsXLargeDevice
				&& getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			startActivity(i);
		} else {
			if (mFirstLaunch) {
				startActivity(i);
				return;
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(i);
				}
			}, 500);
		}
	}

	public void gotoActivityForResult(Context context, Class<?> cla,
									  Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}
	public void gotoActivityForResult(Context context, Class<?> cla,
									  int requestCode) {
		Intent intent = new Intent(context, cla);
		startActivityForResult(intent, requestCode);

	}
	public void gotoActivityForResult(Context context, Class<?> cla,
									  int requestCode, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		startActivityForResult(intent, requestCode);
	}
	public void gotoActivityForResult(Context context, Class<?> cla,
									  Bundle bundle, int requestCode, int flag) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		intent.setFlags(flag);
		startActivityForResult(intent, requestCode);
	}
	@Override
	public void onNetworkChanged() {
	}

	@Override
	public void onClick(View v) {

	}
	public void updateTinhSelected() {
		String url = WebServiceUtils
				.URL_UPDATE_TINH_SELECTED(Pasgo.getInstance().token);
		DeviceUuidFactory factory = new DeviceUuidFactory(mActivity);
		if (StringUtils.isEmpty(Pasgo.getInstance().userId))
			Pasgo.getInstance().userId = "";
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("nguoiDungId", Pasgo.getInstance().userId);
			jsonParams.put("deviceId", factory.getDeviceUuid());
			jsonParams.put("tinhId", Pasgo.getInstance().mTinhId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Pasgo.getInstance().addToRequestQueue(url, jsonParams,
				new Pasgo.PWListener() {

					@Override
					public void onResponse(JSONObject response) {

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