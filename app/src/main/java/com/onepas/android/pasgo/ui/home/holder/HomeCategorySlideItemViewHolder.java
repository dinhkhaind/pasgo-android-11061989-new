package com.onepas.android.pasgo.ui.home.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.utils.Utils;

public class HomeCategorySlideItemViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView imgLogo;
    private TextView tvTenDoiTac;
    private TextView tvNoiDungTaiTro;
    public LinearLayout lnView;

    public HomeCategorySlideItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        tvTenDoiTac = (TextView)convertView.findViewById(R.id.tvTenDoiTac);
        tvNoiDungTaiTro = (TextView)convertView.findViewById(R.id.tvNoiDungTaiTro);
        lnView = (LinearLayout)convertView.findViewById(R.id.category_view_ln);
    }

    public static HomeCategorySlideItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.category_view_ln);
        return new HomeCategorySlideItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(Context context , HomeCategory item) {
        String urlLogo = item.getAnh();
        imgLogo.setImageURI(urlLogo);
        tvTenDoiTac.setText(item.getTieuDe());
        Utils.setTextViewHtml(tvNoiDungTaiTro,item.getCaption());
    }

}
