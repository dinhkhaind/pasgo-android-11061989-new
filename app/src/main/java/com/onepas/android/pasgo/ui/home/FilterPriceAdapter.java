package com.onepas.android.pasgo.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.FilterView;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class FilterPriceAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<FilterView> mLists;
    private TagListenner mTagListenner;
    public interface TagListenner {
        void onClick(int position);
    }
    public FilterPriceAdapter(Context context, ArrayList<FilterView> lists, TagListenner nTagListenner){
        super();
        this.mContext = context;
        this.mLists = lists;
        this.mTagListenner = nTagListenner;
    }
    public void updateList(ArrayList<FilterView> data) {
        if(data==null)
            return;
        mLists = data;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_filter_price, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TextView tvName = (TextView) holder.itemView.findViewById(R.id.tvName);
        final TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        final LinearLayout lnNhomKhuyenMai = (LinearLayout)holder.itemView.findViewById(R.id.lnNhomKhuyenMai);
        FilterView item = mLists.get(position);
        if(item!=null)
        {
            tvName.setText(item.getName());
            String title="";
            for(int i=0;i<position+1;i++)
                title+="$";
            tvTitle.setText(title);
            if(mLists.get(position).isCheck())
            {
                tvTitle.setTextColor(Utils.getColor(mContext, R.color.red));
                tvName.setTextColor(Utils.getColor(mContext,R.color.red));
            }else
            {
                tvTitle.setTextColor(Utils.getColor(mContext, R.color.nhomkm_lv_text_color));
                tvName.setTextColor(Utils.getColor(mContext, R.color.black));
            }

        }
        lnNhomKhuyenMai.setOnClickListener(v -> {
            if(mTagListenner !=null)
                mTagListenner.onClick(position);
        });
    }
    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvName;
        private ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvName = (TextView) v.findViewById(R.id.tvName);
        }
    }

};