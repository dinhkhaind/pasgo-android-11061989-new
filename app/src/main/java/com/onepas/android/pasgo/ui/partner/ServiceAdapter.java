package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DichVu;
import com.onepas.android.pasgo.utils.StringUtils;

import java.util.ArrayList;

public class ServiceAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DichVu> mServiceBookingChild;
    private ServiceBookingChildListener serviceBookingChildListener;
    public interface ServiceBookingChildListener{
        void checked(int position);
    }

    public ServiceAdapter(Context context,ArrayList<DichVu> mServiceBookingChild, ServiceBookingChildListener serviceBookingChildListener){
        super();
        this.context = context;
        this.mServiceBookingChild = mServiceBookingChild;
        this.serviceBookingChildListener =serviceBookingChildListener;
    }

    public int getCount() {
        return mServiceBookingChild.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_child, null);
        TextView tvServiceName1 = (TextView) retval.findViewById(R.id.tvServiceName);
        TextView tvServiceName2 = (TextView) retval.findViewById(R.id.tvServiceName2);
        TextView tvServiceDiscount = (TextView) retval.findViewById(R.id.tvServiceDiscount);
        LinearLayout lnDiscount = (LinearLayout) retval.findViewById(R.id.lnDiscount);
        LinearLayout button = (LinearLayout) retval.findViewById(R.id.lnServiceParent);
        ImageView imgSelected =  (ImageView) retval.findViewById(R.id.imgSelected);
        button.setOnClickListener(view -> {
            if(serviceBookingChildListener!=null)
            {
                serviceBookingChildListener.checked(position);
            }
        });
        // set data
        DichVu item = mServiceBookingChild.get(position);
        if(item!=null)
        {
            tvServiceName1.setText(item.getTenDichVu());
            if(mServiceBookingChild.get(position).getDichVuId() ==0) {
                lnDiscount.setVisibility(View.GONE);
                tvServiceName2.setVisibility(View.VISIBLE);
                tvServiceName1.setText(context.getString(R.string.tat_ca));
                tvServiceName2.setText(context.getString(R.string.hang_xe_name));
            }
            else {
                tvServiceName2.setVisibility(View.GONE);
                tvServiceName2.setText("");
                String giamGia = mServiceBookingChild.get(position).getGiamGia();
                if(StringUtils.isEmpty(giamGia))
                    lnDiscount.setVisibility(View.GONE);
                else
                {
                    lnDiscount.setVisibility(View.VISIBLE);
                    tvServiceDiscount.setText(giamGia);
                }
            }
            if(mServiceBookingChild.get(position).isCheck())
            {
                imgSelected.setImageResource(R.drawable.datxe_checked);
            }else
            {
                imgSelected.setImageResource(R.drawable.uncheck_dat_xe);
            }
        }

        return retval;
    }

};