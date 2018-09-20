package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhList;
import com.onepas.android.pasgo.ui.partner.holder.DetailImageViewHolder;

import java.util.ArrayList;

public class DetailImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_ITEM = 1;
	private Context mContext;
	private ArrayList<AnhList> mLists;
	private DetailImageListenner detailImageListenner;
	public interface DetailImageListenner{
		void detail(int position);
	}
	public DetailImageAdapter(Context context, ArrayList<AnhList> lists, DetailImageListenner detailImageListenner) {
		this.mContext = context;
		this.mLists = lists;
		this.detailImageListenner = detailImageListenner;
	}
	public void updateList(ArrayList<AnhList> data) {
		if(data==null)
			return;
		mLists = data;
		notifyDataSetChanged();
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		final View view =  LayoutInflater.from(context).inflate(R.layout.row_detail_image, parent, false);
		return  DetailImageViewHolder.newInstance(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		DetailImageViewHolder holder = (DetailImageViewHolder) viewHolder;
		AnhList item = mLists.get(position);
		holder.setItemValue(item,mContext);
		holder.imgLogo.setOnClickListener(view -> {
            if(detailImageListenner!=null)
                detailImageListenner.detail(position);
        });

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

}

