package com.onepas.android.pasgo.ui.pasgocard.holder;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.pasgocard.DatChoDiemDenModel;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class DatChoDiemDenItemViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout lnDetail;
    public Button btnCheckIn;
    private TextView tvTenDoiTac;
    private TextView tvTenDuong;
    private TextView tvKhoangCach;
    public SimpleDraweeView imgLogo;
    public SimpleDraweeView imgLogoDefault;
    private RatingBar ratingChatLuong;
    public WebView uu_dai_wv;
    public Button btnThePasgo;
    private TextView tvChuyenMon;
    public TextView tvDoiTacLienQuan;
    private TextView tvRating;
    private TextView tvNoiDungTaiTro;
    private TextView tvGiaTrungBinh;

    public DatChoDiemDenItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        lnDetail = (LinearLayout) convertView.findViewById(R.id.lnDetail);
        tvTenDoiTac = (TextView) convertView.findViewById(R.id.tvTenDoiTac);
        tvTenDuong = (TextView) convertView.findViewById(R.id.tvTenDuong);
        tvKhoangCach = (TextView) convertView.findViewById(R.id.tvKhoangCach);
        btnCheckIn = (Button) convertView.findViewById(R.id.btnCheckIn);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        ratingChatLuong = (RatingBar) convertView.findViewById(R.id.ratingChatLuong);
        uu_dai_wv =  (WebView) convertView.findViewById(R.id.uu_dai_wv);
        uu_dai_wv.setWebViewClient(new WebViewClient() {

        });
        btnThePasgo =(Button) convertView.findViewById(R.id.btnThePasgo);
        tvChuyenMon =(TextView) convertView.findViewById(R.id.tvChuyenMon);
        tvDoiTacLienQuan = (TextView)convertView.findViewById(R.id.de_xuat_tuong_ung_tv);
        imgLogoDefault = (SimpleDraweeView) convertView.findViewById(R.id.imgLogoDefault);
        tvRating = (TextView) convertView.findViewById(R.id.tvRating);
        tvNoiDungTaiTro = (TextView) convertView.findViewById(R.id.tvNoiDungTaiTro);
        tvGiaTrungBinh = (TextView)convertView.findViewById(R.id.gia_trung_binh_tv);
    }

    public static DatChoDiemDenItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.reserved_parent_ln);
        return new DatChoDiemDenItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(DatChoDiemDenModel item, Context context) {
        uu_dai_wv.setBackgroundColor(Color.parseColor("#00000000"));
        if (Build.VERSION.SDK_INT >= 11) {
            uu_dai_wv.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        setWebView(uu_dai_wv, item.getDieuKienMobile());
        Utils.setTextViewHtml(tvTenDoiTac,item.getTenChiNhanh());
        tvTenDuong.setText(item.getDiaChi());
        tvKhoangCach.setText(item.getKm() + " Km");
        ratingChatLuong.setRating((float) item.getChatLuong());
        tvRating.setText(item.getDanhGia() + "");
        Utils.setTextViewHtml(tvNoiDungTaiTro,item.getTaiTro());
        Utils.setTextViewHtml(tvGiaTrungBinh, item.getGiaTrungBinh());
        tvChuyenMon.setText(item.getChuyenMon());
        String urlLogo = item.getLogo();
        if (!StringUtils.isEmpty(urlLogo)) {
            imgLogo.setImageURI(urlLogo);
            imgLogo.setVisibility(View.VISIBLE);
            imgLogoDefault.setVisibility(View.GONE);
        } else {
            imgLogo.setVisibility(View.GONE);
            imgLogoDefault.setVisibility(View.VISIBLE);
        }
        btnThePasgo.setVisibility(View.GONE);
        btnCheckIn.setText(R.string.reserve);
    }

    private void setWebView(WebView webView, String noiDung) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(8);
        webSettings.setBuiltInZoomControls(false);
        String customHtml = "<html><body>" + "<p>" + "<font face="
                + "sans-serif" + " size= 5>" + noiDung.trim() + "</font>"

                + "</body></html>";

        webView.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8",
                null);
    }

}
