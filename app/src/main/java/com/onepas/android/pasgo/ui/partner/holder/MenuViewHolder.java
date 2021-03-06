package com.onepas.android.pasgo.ui.partner.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhBangGia;

public class MenuViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView imgLogo;
    public MenuViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
    }

    public static MenuViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.row_detail_image_ln);
        return new MenuViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(AnhBangGia item, Context context) {
        String urlLogo =item.getAnh()+"&width=400";
        imgLogo.setImageURI(urlLogo);
    }
}
