package com.onepas.android.pasgo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class FocusabilityImageButton extends ImageButton {
	public FocusabilityImageButton(Context context) {
		super(context);
	}

	public FocusabilityImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusabilityImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (pressed && getParent() instanceof View
				&& ((View) getParent()).isPressed()) {
			return;
		}
		super.setPressed(pressed);
	}
}