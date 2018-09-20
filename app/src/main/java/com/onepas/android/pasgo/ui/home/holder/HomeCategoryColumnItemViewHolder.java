package com.onepas.android.pasgo.ui.home.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.utils.Utils;

public class HomeCategoryColumnItemViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView imgLogo;
    private TextView tvTenDoiTac;
    private TextView tvNoiDungTaiTro;
    private TextView tvDiaChi;
    private TextView tvTag;
    private RatingBar ratingChatLuong;
    private TextView tvRating;
    public  LinearLayout lnView;

    public HomeCategoryColumnItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        tvTenDoiTac = (TextView)convertView.findViewById(R.id.tvTenDoiTac);
        tvNoiDungTaiTro = (TextView)convertView.findViewById(R.id.tvNoiDungTaiTro);
        tvDiaChi = (TextView)convertView.findViewById(R.id.tvDiaChi);
        tvTag = (TextView)convertView.findViewById(R.id.tvTag);
        tvRating = (TextView) convertView.findViewById(R.id.tvRating);
        ratingChatLuong = (RatingBar) convertView.findViewById(R.id.ratingChatLuong);
        lnView = (LinearLayout)convertView.findViewById(R.id.category_view_ln);
    }

    public static HomeCategoryColumnItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.category_view_ln);
        return new HomeCategoryColumnItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(Context context , HomeCategory item) {
        String urlLogo = item.getAnh();
        imgLogo.setImageURI(urlLogo);
        tvTenDoiTac.setText(item.getTieuDe());
        tvTenDoiTac.setSingleLine(true);
        tvDiaChi.setText(item.getMoTa());
        Utils.setTextViewHtml(tvTag,item.getCaption1());
        Utils.setTextViewHtml(tvNoiDungTaiTro,item.getCaption());
        ratingChatLuong.setRating((float) item.getChatLuong());
        tvRating.setText(item.getDanhGia() + "");
    }

}
