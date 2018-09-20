package com.onepas.android.pasgo.ui.calldriver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.utils.StringUtils;

public class DialogInputSDT extends Activity implements OnClickListener {

	private Button mBtnOk;
	private EditText mEdInputSDT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_input_sdt);
		getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL,
				LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
		}

		mEdInputSDT = (EditText) findViewById(R.id.ed_input_sdt);
		mBtnOk = (Button) findViewById(R.id.btnGui);
		mBtnOk.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGui:
			String sdt = mEdInputSDT.getText().toString();
			if (!StringUtils.isEmpty(sdt)) {
				inputSdt(sdt);
			}
			break;
		default:
			break;
		}
	}

	private void inputSdt(String sdt) {
		Pasgo.getInstance().sdtKhachHang = sdt;
		hideKeyBoard();
		finish();
	}

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdInputSDT.getWindowToken(), 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
			return false;
		}
		return super.onTouchEvent(event);
	}
}