package com.onepas.android.pasgo.ui.category.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.Category;
import com.onepas.android.pasgo.utils.StringUtils;

public class CategoryChildItemViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout lnCategory;
    public Button btnCheckIn;
    private TextView tvChild;
    public LinearLayout lnChild;
    public LinearLayout lnViewAll;
    private View vChild;
    public SimpleDraweeView imgLogo;
    public SimpleDraweeView imgLogoDefault;

    public CategoryChildItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        lnCategory = (LinearLayout) convertView.findViewById(R.id.home_category_ln);
        lnViewAll = (LinearLayout) convertView.findViewById(R.id.view_all_ln);
        lnChild = (LinearLayout) convertView.findViewById(R.id.child_ln);
        tvChild = (TextView) convertView.findViewById(R.id.child_tv);
        vChild = (View) convertView.findViewById(R.id.child_v);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.category_img);
        imgLogoDefault = (SimpleDraweeView) convertView.findViewById(R.id.category_default_img);
    }

    public static CategoryChildItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.home_category_ln);
        return new CategoryChildItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(Category item, Context context, int position) {
        tvChild.setText(item.getTen());
        if(!StringUtils.isEmpty(item.getAnh())) {
            imgLogo.setImageURI(item.getAnh());
            imgLogo.setVisibility(View.VISIBLE);
            imgLogoDefault.setVisibility(View.GONE);
        }
        else {
            imgLogoDefault.setBackgroundResource(R.drawable.ic_launcher_full);
            imgLogo.setVisibility(View.INVISIBLE);
            imgLogoDefault.setVisibility(View.VISIBLE);
        }
        if(position==0)
        {
            lnViewAll.setVisibility(View.VISIBLE);
        }else{
            lnViewAll.setVisibility(View.GONE);
        }
    }

}
