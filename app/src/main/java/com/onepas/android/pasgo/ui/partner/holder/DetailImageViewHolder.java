package com.onepas.android.pasgo.ui.partner.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhList;

public class DetailImageViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView imgLogo;
    public DetailImageViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
    }

    public static DetailImageViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.row_detail_image_ln);
        return new DetailImageViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(AnhList item, Context context) {
        String urlLogo =item.getAnh()+"&width=300";
        imgLogo.setImageURI(urlLogo);
    }

}
