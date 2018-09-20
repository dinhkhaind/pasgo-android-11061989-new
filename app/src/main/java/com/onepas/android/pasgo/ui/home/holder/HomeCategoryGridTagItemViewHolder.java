package com.onepas.android.pasgo.ui.home.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class HomeCategoryGridTagItemViewHolder extends RecyclerView.ViewHolder {

public SimpleDraweeView imgLogo;
private TextView tvTenDoiTac;
private TextView tvUuDai;
private TextView tvTongNhaHang;

public HomeCategoryGridTagItemViewHolder(final View parent, RelativeLayout convertView) {
        super(parent);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        tvTenDoiTac = (TextView)convertView.findViewById(R.id.tvTenDoiTac);
        tvUuDai = (TextView)convertView.findViewById(R.id.tvUuDai);
        tvTongNhaHang = (TextView)convertView.findViewById(R.id.tvTongNhaHang);
        }

public static HomeCategoryGridTagItemViewHolder newInstance(View parent) {
        RelativeLayout reserved_parent_ln = (RelativeLayout) parent.findViewById(R.id.category_view_ln);
        return new HomeCategoryGridTagItemViewHolder(parent, reserved_parent_ln);
        }

public void setItemValue(Context context , HomeCategory item) {
        imgLogo.setImageURI(item.getAnh());
        Utils.setTextViewHtml(tvTenDoiTac,item.getTieuDe());
        Utils.setTextViewHtml(tvUuDai,item.getCaption());
        Utils.setTextViewHtml(tvTongNhaHang, StringUtils.isEmpty(item.getCaption1())?"0":item.getCaption1());
        }

        }
