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

public class HomeCategoryTagItemViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView imgLogo;
    private TextView tvName;
    public LinearLayout lnDetail;

    public HomeCategoryTagItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        tvName = (TextView)convertView.findViewById(R.id.name_tv);
        lnDetail = (LinearLayout)convertView.findViewById(R.id.category_view_ln);
    }

    public static HomeCategoryTagItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.category_view_ln);
        return new HomeCategoryTagItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(Context context , HomeCategory item) {
        imgLogo.setImageURI(item.getAnh());
        Utils.setTextViewHtml(tvName,item.getTieuDe());
    }

}
