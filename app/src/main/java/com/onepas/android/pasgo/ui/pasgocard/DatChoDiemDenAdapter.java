package com.onepas.android.pasgo.ui.pasgocard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.pasgocard.holder.DatChoDiemDenItemViewHolder;
import com.onepas.android.pasgo.ui.recyclerView.RecyclerHeaderViewHolder;
import com.onepas.android.pasgo.utils.StringUtils;

import java.util.ArrayList;

public class DatChoDiemDenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 2;
	private static final int TYPE_ITEM = 1;
	private Context mContext;
	private ArrayList<DatChoDiemDenModel> mLists;
	private DatCHoDiemDenListener mListener;
	private String mDoiTacKhuyenMaiId;
	public interface DatCHoDiemDenListener {
		void checkIn(int position);
		void detail(int position);
	}

	public DatChoDiemDenAdapter(Context context, String doiTacKhuyenMaiId, ArrayList<DatChoDiemDenModel> lists, DatCHoDiemDenListener listListener) {
		this.mContext = context;
		this.mLists = lists;
		this.mListener =listListener;
		this.mDoiTacKhuyenMaiId = doiTacKhuyenMaiId;
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		if (viewType == TYPE_ITEM) {
			final View view =  LayoutInflater.from(context).inflate(R.layout.row_dat_cho_diem_den, parent, false);
			return  DatChoDiemDenItemViewHolder.newInstance(view);
		} else if (viewType == TYPE_HEADER) {
			final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
			return new RecyclerHeaderViewHolder(view);
		}
		throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		if (!isPositionHeader(position)) {
			DatChoDiemDenItemViewHolder holder = (DatChoDiemDenItemViewHolder) viewHolder;
			if(!StringUtils.isEmpty(mDoiTacKhuyenMaiId) && mLists.size()>1 && position==1)
				holder.tvDoiTacLienQuan.setVisibility(View.VISIBLE);
			else
				holder.tvDoiTacLienQuan.setVisibility(View.GONE);
			holder.btnCheckIn.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.checkIn(position -1);
            });
			holder.lnDetail.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.detail(position - 1);
            });
			holder.imgLogo.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.detail(position - 1);
            });
			holder.imgLogoDefault.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.detail(position-1);
            });
			holder.uu_dai_wv.setOnTouchListener((view, motionEvent) -> {
                if (mListener != null && motionEvent.getAction() == MotionEvent.ACTION_UP)
                    mListener.detail(position-1);
                return false;
            });
			DatChoDiemDenModel item = mLists.get(position - 1); // header
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

