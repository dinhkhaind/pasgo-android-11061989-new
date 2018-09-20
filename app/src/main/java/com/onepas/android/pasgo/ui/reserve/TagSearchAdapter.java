package com.onepas.android.pasgo.ui.reserve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.TagModel;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class TagSearchAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<TagModel> mTags;
    private TagListenner mTagListenner;
    public interface TagListenner {
        void onClick(int position);
    }
    public TagSearchAdapter(Context context, ArrayList<TagModel> nhomKhuyenMais, TagListenner nTagListenner){
        super();
        this.mContext = context;
        this.mTags = nhomKhuyenMais;
        this.mTagListenner = nTagListenner;
    }
    public void updateList(ArrayList<TagModel> data) {
        if(data==null)
            return;
        mTags = data;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.row_search_tag, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TextView tvName = (TextView) holder.itemView.findViewById(R.id.tvName);
        final LinearLayout lnNhomKhuyenMai = (LinearLayout)holder.itemView.findViewById(R.id.lnNhomKhuyenMai);
        TagModel item = mTags.get(position);
        if(item!=null)
        {
            tvName.setText(item.getTen());
            tvName.setTextColor(Utils.getColor(mContext, android.R.color.black));
        }
        lnNhomKhuyenMai.setOnClickListener(v -> {
            if(mTagListenner !=null)
                mTagListenner.onClick(position);
        });
    }
    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
        }
    }

};