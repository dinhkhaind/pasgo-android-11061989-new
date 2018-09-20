package com.onepas.android.pasgo.ui.reserved.Holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.CheckedIn;
import com.onepas.android.pasgo.ui.recyclerView.RecyclerHeaderViewHolder;

import java.util.ArrayList;

public class ReservedHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private Context mContext;
    private ArrayList<CheckedIn> mLists;
    private CheckInListListener mListener;
    public interface CheckInListListener {
        void share(int position);
        void checkIn(int position);
        void detail(int position);
        void callSuport(int position);
        void direction(int position);
        void deals(int position);
    }
    public ReservedHistoryAdapter(Context context, ArrayList<CheckedIn> lists, CheckInListListener listListener) {
        this.mContext = context;
        this.mLists = lists;
        this.mListener =listListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view =  LayoutInflater.from(context).inflate(R.layout.row_reserver_history, parent, false);
            return  ReservedHistoryItemViewHolder.newInstance(view);
        } else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
            return new RecyclerHeaderViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (!isPositionHeader(position)) {
            ReservedHistoryItemViewHolder holder = (ReservedHistoryItemViewHolder) viewHolder;
            holder.btnShare.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.share(position-1);
            });
            holder.btnCallSuport.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.callSuport(position-1);
            });
            holder.lnDetail.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.detail(position - 1);
            });
            holder.imgLogo.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.detail(position-1);
            });
            holder.imgLogoDefault.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.detail(position-1);
            });
            holder.btnReserve.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.checkIn(position-1);
            });
            holder.btnDirection.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.direction(position-1);
            });
            holder.btnDeals.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.deals(position-1);
            });
            CheckedIn item = mLists.get(position - 1); // header
            holder.setItemValue(item,mContext);
        }
    }

    public int getBasicItemCount() {
        return mLists == null ? 0 : mLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount() + 1; // header
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
