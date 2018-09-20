package com.onepas.android.pasgo.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;

public class TouchableWrapper extends FrameLayout {
	private long lastTouched = 0;
	private static final long SCROLL_TIME = 200L;
	private UpdateMapAfterUserInterection updateMapAfterUserInterection;

	public TouchableWrapper(Context context) {
		super(context);
		try {
			updateMapAfterUserInterection = (BaseAppCompatActivity) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement UpdateMapAfterUserInterection");
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastTouched = SystemClock.uptimeMillis();
            updateMapAfterUserInterection.onStartMoveScreen();
			break;

		case MotionEvent.ACTION_UP:
			final long now = SystemClock.uptimeMillis();
			if (now - lastTouched > SCROLL_TIME) {
				updateMapAfterUserInterection.onUpdateMapAfterUserInterection();
			}
			//updateMapAfterUserInterection.onUpdateMapAfterUserInterection();
			break;

		}
		return super.dispatchTouchEvent(ev);
	}

	public interface UpdateMapAfterUserInterection {
        public void onStartMoveScreen();
		public void onUpdateMapAfterUserInterection();

	}
}