package com.onepas.android.pasgo.ui.announcements.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.TinKhuyenMaiDoiTac;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class Detail3ItemViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlDetail;
    public Button btnCheckIn;
    private TextView tvTenDoiTac;
    private TextView tvTenDuong;
    public SimpleDraweeView imgLogo;
    public SimpleDraweeView imgLogoDefault;
    private RatingBar ratingChatLuong;
    private TextView tvKhoangCach;
    private TextView tvChuyenMon;
    private TextView tvRating;
    private TextView tvNoiDungTaiTro;
    private TextView tvGiaTrungBinh;

    public Detail3ItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        rlDetail = (RelativeLayout) convertView.findViewById(R.id.rlDetail);
        tvTenDoiTac = (TextView) convertView.findViewById(R.id.tvTenDoiTac);
        tvTenDuong = (TextView) convertView.findViewById(R.id.tvTenDuong);
        btnCheckIn = (Button) convertView.findViewById(R.id.btnCheckIn);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        imgLogoDefault = (SimpleDraweeView) convertView.findViewById(R.id.imgLogoDefault);
        ratingChatLuong = (RatingBar) convertView.findViewById(R.id.ratingChatLuong);
        tvKhoangCach = (TextView) convertView.findViewById(R.id.tvKhoangCach);
        tvChuyenMon = (TextView) convertView.findViewById(R.id.tvChuyenMon);
        tvRating = (TextView) convertView.findViewById(R.id.tvRating);
        tvNoiDungTaiTro = (TextView) convertView.findViewById(R.id.tvNoiDungTaiTro);
        tvGiaTrungBinh = (TextView)convertView.findViewById(R.id.gia_trung_binh_tv);
    }

    public static Detail3ItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.reserved_parent_ln);
        return new Detail3ItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(TinKhuyenMaiDoiTac item, Context context) {
        Utils.setTextViewHtml(tvTenDoiTac,item.getTen());
        tvTenDuong.setText(item.getDiaChi());
        ratingChatLuong.setRating((float) item.getChatLuong());
        tvRating.setText(item.getDanhGia() + "");
        tvChuyenMon.setText(item.getChuyenMon());
        Utils.setTextViewHtml(tvNoiDungTaiTro,item.getTaiTro());
        tvChuyenMon.setText(item.getChuyenMon());
        tvKhoangCach.setText(item.getKm() + " Km");
        String urlLogo = item.getLogo();
        if (!StringUtils.isEmpty(urlLogo)) {
            imgLogo.setImageURI(urlLogo);
            imgLogo.setVisibility(View.VISIBLE);
            imgLogoDefault.setVisibility(View.GONE);
        } else {
            imgLogo.setVisibility(View.GONE);
            imgLogoDefault.setVisibility(View.VISIBLE);
        }
        if(item.getLoaiHopDong()==1)
            btnCheckIn.setBackgroundResource(R.drawable.selector_white);
        else
            btnCheckIn.setBackgroundResource(R.color.transparent_selected);
    }

}
