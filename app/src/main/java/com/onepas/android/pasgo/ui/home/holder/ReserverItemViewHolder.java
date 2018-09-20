package com.onepas.android.pasgo.ui.home.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.widgets.JustifyTextView;

public class ReserverItemViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout lnDetail;
    public Button btnCheckIn;
    private TextView tvTenDoiTac;
    private TextView tvTenDuong;
    private TextView tvKhoangCach;
    public SimpleDraweeView imgLogo;
    public SimpleDraweeView imgLogoDefault;
    private RatingBar ratingChatLuong;
    private TextView tvChuyenMon;
    private TextView tvRating;
    private TextView tvNoiDungTaiTro;
    private JustifyTextView tvMoTa;
    private LinearLayout lnData;
    private TextView mTvGiaTrungBinh;

    private ReserverItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        lnDetail = (LinearLayout) convertView.findViewById(R.id.lnDetail);
        tvTenDoiTac = (TextView) convertView.findViewById(R.id.tvTenDoiTac);
        tvTenDuong = (TextView) convertView.findViewById(R.id.tvTenDuong);
        tvKhoangCach = (TextView) convertView.findViewById(R.id.tvKhoangCach);
        btnCheckIn = (Button) convertView.findViewById(R.id.btnCheckIn);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        imgLogoDefault = (SimpleDraweeView) convertView.findViewById(R.id.imgLogoDefault);
        ratingChatLuong = (RatingBar) convertView.findViewById(R.id.ratingChatLuong);
        tvChuyenMon = (TextView) convertView.findViewById(R.id.tvChuyenMon);
        tvRating = (TextView) convertView.findViewById(R.id.tvRating);
        tvNoiDungTaiTro = (TextView) convertView.findViewById(R.id.tvNoiDungTaiTro);
        tvMoTa = (JustifyTextView) convertView.findViewById(R.id.mota_tv);
        lnData = (LinearLayout)convertView.findViewById(R.id.data_ln);
        mTvGiaTrungBinh = (TextView)convertView.findViewById(R.id.gia_trung_binh_tv);
    }

    public static ReserverItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.reserved_parent_ln);
        return new ReserverItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(DiemDenModel item, Context context) {
        tvTenDoiTac.setText(item.getTen());
        if(item.isMoTa())
        {
            tvMoTa.setVisibility(View.VISIBLE);
            lnData.setVisibility(View.GONE);
            if(StringUtils.isEmpty(item.getMotaTag()))
                tvMoTa.setVisibility(View.GONE);
            else {
                tvMoTa.setVisibility(View.VISIBLE);
                tvMoTa.setText(item.getMotaTag());
            }
        }else{
            tvMoTa.setVisibility(View.GONE);
            lnData.setVisibility(View.VISIBLE);

            Utils.setTextViewHtml(tvTenDoiTac,item.getTen());
            tvTenDuong.setText(item.getDiaChi());
            tvKhoangCach.setText(item.getKm() + " Km");
            ratingChatLuong.setRating((float) item.getChatLuong());
            tvRating.setText(item.getDanhGia() + "");
            Utils.setTextViewHtml(tvNoiDungTaiTro, item.getTaiTro());
            tvChuyenMon.setText(item.getChuyenMon());
            String urlLogo = item.getLogo();
            Utils.setTextViewHtml(mTvGiaTrungBinh, item.getGiaTrungBinh());
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
            item.setCoThePASGO(true);
        }
    }

}
