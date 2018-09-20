package com.onepas.android.pasgo.ui.reserve.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ReserveSearch;

public class GoiYTimKiemHolder extends RecyclerView.ViewHolder {


    private TextView tvName;
    public RelativeLayout rlGoiY;
    public GoiYTimKiemHolder(final View parent, RelativeLayout convertView) {
        super(parent);
        tvName = (TextView) convertView.findViewById(R.id.tu_khoa_tv);
        rlGoiY = (RelativeLayout) convertView.findViewById(R.id.goi_y_item_rl);
    }

    public static GoiYTimKiemHolder newInstance(View parent) {
        RelativeLayout reserved_parent_ln = (RelativeLayout) parent.findViewById(R.id.goi_y_item_rl);
        return new GoiYTimKiemHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(ReserveSearch item, Context context) {
        tvName.setText(item.getTuKhoa());
    }

}
