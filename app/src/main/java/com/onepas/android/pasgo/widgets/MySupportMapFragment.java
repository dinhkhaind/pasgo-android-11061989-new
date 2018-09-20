package com.onepas.android.pasgo.widgets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

public class MySupportMapFragment extends MapFragment {
	public View mOriginalContentView;
	public TouchableWrapper mTouchView;

	public MySupportMapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		mOriginalContentView = super.onCreateView(inflater, parent,
				savedInstanceState);
		mTouchView = new TouchableWrapper(getActivity());
		mTouchView.addView(mOriginalContentView);
		return mTouchView;
	}

	@Override
	public View getView() {
		return mOriginalContentView;
	}
}