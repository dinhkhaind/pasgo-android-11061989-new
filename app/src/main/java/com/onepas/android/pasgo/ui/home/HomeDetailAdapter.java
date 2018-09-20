package com.onepas.android.pasgo.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.home.holder.ReserverItemViewHolder;
import com.onepas.android.pasgo.ui.recyclerView.RecyclerHeaderViewHolder;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;

import java.util.ArrayList;

public class HomeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private Context mContext;
    private ArrayList<DiemDenModel> mLists;
    private DiemDenListener mListener;

    public interface DiemDenListener {
        void checkIn(int position);

        void detail(int position);

        void ship(int position);
    }

    public HomeDetailAdapter(Context context, ArrayList<DiemDenModel> lists, DiemDenListener listListener) {
        this.mContext = context;
        this.mLists = lists;
        this.mListener = listListener;
    }

    public void updateList(ArrayList<DiemDenModel> data) {
        if(data==null)
            return;
        mLists = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(context).inflate(R.layout.row_home_detail, parent, false);
            return ReserverItemViewHolder.newInstance(view);
        } else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
            return new RecyclerHeaderViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        //if (!isPositionHeader(position)) {
        ReserverItemViewHolder holder = (ReserverItemViewHolder) viewHolder;
        DiemDenModel item = mLists.get(position); // header
        holder.setItemValue(item, mContext);

        holder.btnCheckIn.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (mListener != null)
                mListener.checkIn(position);
        });
        holder.lnDetail.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (mListener != null)
                mListener.detail(position);
        });
        holder.imgLogo.setOnClickListener(v -> {
            if (mListener != null)
                mListener.detail(position);
        });
        holder.imgLogoDefault.setOnClickListener(v -> {
            if (mListener != null)
                mListener.detail(position);
        });

        //}
    }

    public int getBasicItemCount() {
        return mLists == null ? 0 : mLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount();
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}

