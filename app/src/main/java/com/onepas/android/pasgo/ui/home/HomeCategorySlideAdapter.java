package com.onepas.android.pasgo.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;
import com.onepas.android.pasgo.ui.home.holder.HomeCategorySlideItemViewHolder;

import java.util.ArrayList;

public class HomeCategorySlideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<HomeCategory> mLists;
    private HomeCategoryListener mListener;
    public static final int KEY_DATA =3;
    public interface HomeCategoryListener {
        void detail(int position);
    }
    public HomeCategorySlideAdapter(Context context, ArrayList<HomeCategory> lists, HomeCategoryListener listener) {
        this.mContext = context;
        this.mLists = lists;
        this.mListener = listener;
    }

    public void updateList(ArrayList<HomeCategory> data) {
        if(data==null)
            return;
        mLists = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.row_home_category_slide, parent, false);
        return HomeCategorySlideItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        HomeCategorySlideItemViewHolder holder = (HomeCategorySlideItemViewHolder) viewHolder;
        HomeCategory item = mLists.get(position); // header
        holder.setItemValue(mContext,item);
        holder.imgLogo.setOnClickListener(v->{
            mListener.detail(position);
        });
        holder.lnView.setOnClickListener(v->{
            mListener.detail(position);
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
