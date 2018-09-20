package com.onepas.android.pasgo.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by nbradbury on 6/20/13.
 * Provides a simplified way to show toast messages without having to create the toast, set the
 * desired gravity, etc.
 */
public class ToastUtils {
    public enum Duration {SHORT, LONG}

    private ToastUtils() {
        throw new AssertionError();
    }

    public static void showToast(Context context, int stringResId) {
        showToast(context, stringResId, Duration.SHORT);
    }
    public static void showToast(Context context, int stringResId, Duration duration) {
        showToast(context, context.getString(stringResId), duration);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Duration.SHORT);
    }
    
    
    public static void showToast(Context context, String text, Duration duration) {
        Toast toast = Toast.makeText(context, text, (duration== Duration.SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastSuccess(Context context, String text) {
        Toasty.success(context,text,Toast.LENGTH_SHORT).show();
    }

    public static void showToastError(Context context, String text) {
        Toasty.error(context,text,Toast.LENGTH_SHORT).show();
    }
    public static void showToastWaring(Context context, String text) {
        Toasty.warning(context,text,Toast.LENGTH_SHORT).show();
    }

    public static void showToastSuccess(Context context, int stringResId) {
        Toasty.success(context,context.getString(stringResId),Toast.LENGTH_SHORT).show();
    }

    public static void showToastError(Context context, int stringResId) {
        Toasty.error(context,context.getString(stringResId),Toast.LENGTH_SHORT).show();
    }
    public static void showToastWaring(Context context, int stringResId) {
        Toasty.warning(context,context.getString(stringResId),Toast.LENGTH_SHORT).show();
    }
}
