package com.onepas.android.pasgo.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.home.holder.DienDenHomeItemViewHolder;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;

import java.util.ArrayList;

public class DiemDenHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DiemDenModel> mLists;
    private DiemDenHomeListener mDiemDenHomeListener;
    private int mKey;
    public static final int KEY_LOADING = 4;
    public static final int KEY_NODATA = 1;
    public static final int KEY_DISCONNECT = 2;
    public static final int KEY_DATA = 3;

    public interface DiemDenHomeListener {
        void checkIn(int position);

        void detail(int position);

        void disconnect();
    }

    public DiemDenHomeAdapter(Context context, ArrayList<DiemDenModel> lists, DiemDenHomeListener diemDenHomeListener, int key) {
        this.mContext = context;
        this.mLists = lists;
        this.mDiemDenHomeListener = diemDenHomeListener;
        this.mKey = key;
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
        final View view = LayoutInflater.from(context).inflate(R.layout.row_diem_den_gan_ban, parent, false);
        return DienDenHomeItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        DienDenHomeItemViewHolder holder = (DienDenHomeItemViewHolder) viewHolder;
        DiemDenModel item = mLists.get(position); // header
        holder.setItemValue(mContext, item, mKey);
        holder.btnCheckIn.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (mDiemDenHomeListener != null)
                mDiemDenHomeListener.checkIn(position);
        });
        holder.rlDetail.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (mDiemDenHomeListener != null)
                mDiemDenHomeListener.detail(position);
        });
        holder.imgLogo.setOnClickListener(v -> {
            if (mDiemDenHomeListener != null)
                mDiemDenHomeListener.detail(position);
        });
        holder.imgLogoDefault.setOnClickListener(v -> {
            if (mDiemDenHomeListener != null)
                mDiemDenHomeListener.detail(position);
        });
        holder.btnTryAGainDoiTac.setOnClickListener(v -> {
            if (mDiemDenHomeListener != null)
                mDiemDenHomeListener.disconnect();
        });
    }

    public int getBasicItemCount() {
        return mLists == null ? 0 : mLists.size();
    }


    @Override
    public int getItemCount() {
        return getBasicItemCount();
    }
}
