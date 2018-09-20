package com.onepas.android.pasgo.ui.reserve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.recyclerView.RecyclerHeaderViewHolder;
import com.onepas.android.pasgo.ui.reserve.holder.ReserveItemViewHolder;

import java.util.ArrayList;

public class DiemDenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 2;
	private static final int TYPE_ITEM = 1;
	private Context mContext;
	private ArrayList<DiemDenModel> mLists;
	private DiemDenListener mListener;
	private boolean mIsLoadingNhomKm;
	private boolean mIsActivitySearch;
	public interface DiemDenListener {
		void checkIn(int position);
		void detail(int position);
		void thePasgo(int position);
	}
	public DiemDenAdapter(Context context,ArrayList<DiemDenModel> lists, DiemDenListener listListener, boolean isLoadingNhomKm, boolean isActivitySearch) {
		this.mContext = context;
		this.mLists = lists;
		this.mListener =listListener;
		this.mIsLoadingNhomKm = isLoadingNhomKm;
		this.mIsActivitySearch = isActivitySearch;
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
			final View view =  LayoutInflater.from(context).inflate(R.layout.row_dat_cho_diem_den, parent, false);
			return  ReserveItemViewHolder.newInstance(view);
		} else if (viewType == TYPE_HEADER) {
			if(mIsLoadingNhomKm) {// nếu có nhóm km thì head phải rộng hơn
				final View view = LayoutInflater.from(context).inflate(R.layout.reserve_recycler_header_loading, parent, false);
				return new RecyclerHeaderViewHolder(view);
			}else
			{
				if(mIsActivitySearch) {
					final View view = LayoutInflater.from(context).inflate(R.layout.reserve_recycler_header_search_loading, parent, false);
					return new RecyclerHeaderViewHolder(view);
				}else {
					final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
					return new RecyclerHeaderViewHolder(view);
				}
			}
		}
		throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		if (!isPositionHeader(position)) {
			ReserveItemViewHolder holder = (ReserveItemViewHolder) viewHolder;
			holder.btnCheckIn.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.checkIn(position-1);
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
			holder.btnThePasgo.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.thePasgo(position-1);
            });
			DiemDenModel item = mLists.get(position - 1); // header
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

