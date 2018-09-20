package com.onepas.android.pasgo.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class Tooltips {
	private static final int ESTIMATED_TOAST_HEIGHT_DIPS = 48;

	public static void setup(View view) {
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return showCheatSheet(view, view.getContentDescription());
			}
		});
	}

	public static void setup(View view, final int textResId) {
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return showCheatSheet(view,
						view.getContext().getString(textResId));
			}
		});
	}

	public static void setup(View view, final CharSequence text) {
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return showCheatSheet(view, text);
			}
		});
	}

	public static void remove(final View view) {
		view.setOnLongClickListener(null);
	}

	/**
	 * Internal helper method to show the cheat sheet toast.
	 */
	private static boolean showCheatSheet(View view, CharSequence text) {
		if (TextUtils.isEmpty(text)) {
			return false;
		}

		final int[] screenPos = new int[2]; // origin is device display
		final Rect displayFrame = new Rect(); // includes decorations (e.g.
												// status bar)
		view.getLocationOnScreen(screenPos);
		view.getWindowVisibleDisplayFrame(displayFrame);

		final Context context = view.getContext();
		final int viewWidth = view.getWidth();
		final int viewHeight = view.getHeight();
		final int viewCenterX = screenPos[0] + viewWidth / 2;
		final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		final int estimatedToastHeight = (int) (ESTIMATED_TOAST_HEIGHT_DIPS * context
				.getResources().getDisplayMetrics().density);

		Toast cheatSheet = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		boolean showBelow = screenPos[1] < estimatedToastHeight;
		if (showBelow) {
			// Show below
			// Offsets are after decorations (e.g. status bar) are factored in
			cheatSheet.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
					viewCenterX - screenWidth / 2, screenPos[1]
							- displayFrame.top + viewHeight);
		} else {
			// Show above
			// Offsets are after decorations (e.g. status bar) are factored in
			// NOTE: We can't use Gravity.BOTTOM because when the keyboard is up
			// its height isn't factored in.
			cheatSheet.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
					viewCenterX - screenWidth / 2, screenPos[1]
							- displayFrame.top - estimatedToastHeight);
		}

		cheatSheet.show();
		return true;
	}
}
