package com.onepas.android.pasgo.widgets;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.onepas.android.pasgo.R;

public class ProgressDialogFragment extends DialogFragment {
	private static ProgressDialogFragment frag;

	public static ProgressDialogFragment newInstance() {
		if (frag == null)
			frag = new ProgressDialogFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawableResource(
				R.drawable.bg_black_transparent);
		View view = inflater
				.inflate(R.layout.layout_progress_dialog, container);
		getDialog().setCancelable(false);
		return view;
	}

}