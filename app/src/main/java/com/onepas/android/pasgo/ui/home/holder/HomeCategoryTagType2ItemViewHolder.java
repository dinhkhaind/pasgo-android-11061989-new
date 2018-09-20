package com.onepas.android.pasgo.ui.home.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.utils.Utils;

public class HomeCategoryTagType2ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView tvName;
    public LinearLayout lnDetail;

    public HomeCategoryTagType2ItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        tvName = (TextView)convertView.findViewById(R.id.name_tv);
        lnDetail = (LinearLayout)convertView.findViewById(R.id.category_view_ln);
    }

    public static HomeCategoryTagType2ItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.category_view_ln);
        return new HomeCategoryTagType2ItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(Context context , HomeCategory item) {
        Utils.setTextViewHtml(tvName,item.getTieuDe());
    }

}
