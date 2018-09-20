package com.onepas.android.pasgo.ui.partner.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhBangGia;
import com.onepas.android.pasgo.utils.StringUtils;

public class DetailMenuViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView imgLogo;

    public DetailMenuViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
    }

    public static DetailMenuViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.row_detail_menu_ln);
        return new DetailMenuViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(AnhBangGia item, Context context) {
        String urlLogo =  item.getAnh()+"&width=300";
        imgLogo.setImageURI(urlLogo);
    }

}
