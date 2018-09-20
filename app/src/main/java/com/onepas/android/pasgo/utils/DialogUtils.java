package com.onepas.android.pasgo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.onepas.android.pasgo.R;

/**
 * DialogUtility
 * 
 * @author Dk8
 */
public final class DialogUtils {
	/**
	 * Show an alert dialog box
	 * 
	 * @param activity
	 * @param message
	 */

	public static void alert(Activity activity, String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		alertDialog.setIcon(R.drawable.app_pastaxi);
        View dialog = inflater.inflate(R.layout.layout_logo_pasgo_ok, null);
        alertDialog.setView(dialog)
                .setPositiveButton(R.string.btn_ok, (dialog1, id) -> {
                });
        TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        Utils.setTextViewHtml(tvThongBaoPopup,message);
		alertDialog.show();
	}
    public static void alert(Activity activity,String title, String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setIcon(R.drawable.app_pastaxi);
        View dialog = inflater.inflate(R.layout.layout_popup_title_ok, null);
        alertDialog.setView(dialog)
                .setPositiveButton(R.string.btn_ok, (dialog1, id) -> {
                });
        TextView tvTitle = (TextView)dialog.findViewById(R.id.tvTitle);
        TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        Utils.setTextViewHtml(tvTitle,title);
        Utils.setTextViewHtml(tvThongBaoPopup,message);
        alertDialog.show();
    }
	// }
	/**
	 * Show an alert dialog box
	 * 
	 * @param activity
	 * @param messageId
	 */
    public static void alert(Activity activity, int messageId) {
        LayoutInflater inflater = activity.getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setIcon(R.drawable.app_pastaxi);
        View dialog = inflater.inflate(R.layout.layout_logo_pasgo_ok, null);
        alertDialog.setView(dialog)
                .setPositiveButton(R.string.btn_ok, (dialog1, id) -> {
                });
        TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        Utils.setTextViewHtml(tvThongBaoPopup,activity.getString(messageId));
        alertDialog.show();
    }


	public static void showOkDialog(Activity activity, int messageId,
			int OkTextId, final DialogInterface.OnClickListener onOKClick) {
		if (activity != null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setIcon(R.drawable.app_pastaxi);
            View dialog = inflater.inflate(R.layout.layout_logo_pasgo_ok, null);
            alertDialog.setView(dialog).setPositiveButton(OkTextId, onOKClick);
            TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
            Utils.setTextViewHtml(tvThongBaoPopup,activity.getString(messageId));
            alertDialog.show();
        }
	}

    public static void showOkDialogNoCancel(Activity activity, int messageId,
                                    int OkTextId, final DialogInterface.OnClickListener onOKClick) {
        if (activity != null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setIcon(R.drawable.app_pastaxi);
            View dialog = inflater.inflate(R.layout.layout_logo_pasgo_ok, null);
            alertDialog.setView(dialog).setPositiveButton(OkTextId, onOKClick);
            TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
            Utils.setTextViewHtml(tvThongBaoPopup,activity.getString(messageId));
            alertDialog.show();
        }
    }

    public static void showOkDialogNoCancel(Activity activity, String message,
                                            int OkTextId, final DialogInterface.OnClickListener onOKClick) {
        if (activity != null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setCancelable(false);
            alertDialog.setIcon(R.drawable.app_pastaxi);
            View dialog = inflater.inflate(R.layout.layout_logo_pasgo_ok, null);
            alertDialog.setView(dialog).setPositiveButton(OkTextId, onOKClick);
            TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
            Utils.setTextViewHtml(tvThongBaoPopup, message);
            alertDialog.show();
        }
    }
    public static void showYesNoDialogCloseApp(final Context context, int messageId,
                                       int OkTextId, int cancelTextId, final View.OnClickListener onOKClick) {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
        TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        Button btnDongY,btnHuy;
        btnDongY = (Button)dialog.findViewById(R.id.btnDongY);
        btnHuy = (Button)dialog.findViewById(R.id.btnHuy);
        btnDongY.setOnClickListener(onOKClick);
        btnDongY.setText(context.getString(OkTextId));
        btnHuy.setText(context.getString(cancelTextId));
        btnHuy.setOnClickListener(view -> dialog.cancel());
        Utils.setTextViewHtml(tvThongBaoPopup,context.getString(messageId));
        tvThongBaoPopup.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.popup_text_size_close_app));
        if(dialog!=null && !dialog.isShowing())
            dialog.show();
    }
    public static void showYesNoDialog(final Context context, int messageId,
                                        int OkTextId, int cancelTextId, final View.OnClickListener onOKClick) {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
        TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        Button btnDongY,btnHuy;
        btnDongY = (Button)dialog.findViewById(R.id.btnDongY);
        btnHuy = (Button)dialog.findViewById(R.id.btnHuy);
        btnDongY.setOnClickListener(onOKClick);
        btnDongY.setText(context.getString(OkTextId));
        btnHuy.setText(context.getString(cancelTextId));
        btnHuy.setOnClickListener(view -> dialog.cancel());
        Utils.setTextViewHtml(tvThongBaoPopup,context.getString(messageId));
        if(dialog!=null && !dialog.isShowing())
            dialog.show();
    }

    public static Dialog showYesNoDialog(final Context context, String message,
                                       int OkTextId, int cancelTextId, final View.OnClickListener onCancelClick  , final View.OnClickListener onOKClick) {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_logo_pasgo_ok_cancel);
        TextView tvThongBaoPopup=(TextView) dialog.findViewById(R.id.tvThongBaoPopup);
        Button btnDongY,btnHuy;
        btnDongY = (Button)dialog.findViewById(R.id.btnDongY);
        btnHuy = (Button)dialog.findViewById(R.id.btnHuy);
        btnDongY.setOnClickListener(onOKClick);
        btnDongY.setText(context.getString(OkTextId));
        btnHuy.setText(context.getString(cancelTextId));
        btnHuy.setOnClickListener(onCancelClick);
        Utils.setTextViewHtml(tvThongBaoPopup,message);
        return  dialog;
    }
    public static Dialog showYesNoDialogCheckIn(final Context context, String message1, String message2, String soNguoi, String soTreEm, String ngayThang, String gio,
                                         int OkTextId, int cancelTextId, final View.OnClickListener onCancelClick  , final View.OnClickListener onOKClick) {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_confirm_checkin);
        TextView tvThongBao1=(TextView) dialog.findViewById(R.id.tvThongBao1);
        TextView tvThongBao2=(TextView) dialog.findViewById(R.id.tvThongBao2);
        TextView tvSoNguoi=(TextView) dialog.findViewById(R.id.so_nguoi_tv);
        TextView tvSoNguoiTreEm=(TextView) dialog.findViewById(R.id.so_nguoi_tre_em_tv);
        TextView tvNgayThang=(TextView) dialog.findViewById(R.id.ngay_tv);
        TextView tvGio=(TextView) dialog.findViewById(R.id.gio_tv);

        Button btnDongY,btnHuy;
        btnDongY = (Button)dialog.findViewById(R.id.btnDongY);
        btnHuy = (Button)dialog.findViewById(R.id.btnHuy);
        btnDongY.setOnClickListener(onOKClick);
        btnDongY.setText(context.getString(OkTextId));
        btnHuy.setText(context.getString(cancelTextId));
        btnHuy.setOnClickListener(onCancelClick);
        Utils.setTextViewHtml(tvThongBao1,message1);
        Utils.setTextViewHtml(tvThongBao2,message2);
        Utils.setTextViewHtml(tvSoNguoi,soNguoi);
        tvSoNguoiTreEm.setText(soTreEm);
        Utils.setTextViewHtml(tvNgayThang,ngayThang);
        Utils.setTextViewHtml(tvGio,gio);
        return  dialog;
    }
}
