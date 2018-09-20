package com.onepas.android.pasgo.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.onepas.android.pasgo.R;

public final class BaseProgressDialogAllUtils extends Dialog {

	public BaseProgressDialogAllUtils(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(
				R.drawable.bg_black_transparent);
		setCancelable(false);
		setContentView(R.layout.layout_progress_dialog);
	}
}
