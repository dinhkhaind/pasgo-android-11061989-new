package com.onepas.android.pasgo.ui.reserve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ReserveSearch;
import com.onepas.android.pasgo.ui.reserve.holder.GoiYTimKiemHolder;

import java.util.ArrayList;

public class GoiYTimKiemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_ITEM = 1;
	private Context mContext;
	private ArrayList<ReserveSearch> mLists;
	private GoiYTimKiemListener mListener;



	public interface GoiYTimKiemListener {
		void detail(int position);
	}
	public GoiYTimKiemAdapter(Context context, ArrayList<ReserveSearch> lists, GoiYTimKiemListener listListener) {
		this.mContext = context;
		this.mLists = lists;
		this.mListener =listListener;
	}
	public void updateList(ArrayList<ReserveSearch> data) {
		if(data==null)
			return;
		mLists = data;
		notifyDataSetChanged();
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		final View view =  LayoutInflater.from(context).inflate(R.layout.row_goi_y, parent, false);
		return  GoiYTimKiemHolder.newInstance(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		GoiYTimKiemHolder holder = (GoiYTimKiemHolder) viewHolder;
		holder.rlGoiY.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (mListener != null)
                mListener.detail(position);
        });
		ReserveSearch item = mLists.get(position);
		holder.setItemValue(item,mContext);
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

