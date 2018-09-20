package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DoiTacLienQuan;
import com.onepas.android.pasgo.ui.partner.holder.DiemQuanTamViewHolder;

import java.util.ArrayList;

public class DetailDiemQuanTamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_ITEM = 1;
	private Context mContext;
	private ArrayList<DoiTacLienQuan> mLists;
	private DetailDiemQuanTamListener mListener;
	public interface DetailDiemQuanTamListener {
		void checkIn(int position);
		void detail(int position);
		void thePasgo(int position);
	}
	public DetailDiemQuanTamAdapter(Context context, ArrayList<DoiTacLienQuan> lists, DetailDiemQuanTamListener listListener) {
		this.mContext = context;
		this.mLists = lists;
		this.mListener =listListener;
	}
	public void updateList(ArrayList<DoiTacLienQuan> data) {
		if(data==null)
			return;
		mLists = data;
		notifyDataSetChanged();
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		final View view =  LayoutInflater.from(context).inflate(R.layout.row_diem_den_gan_ban, parent, false);
		return  DiemQuanTamViewHolder.newInstance(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		DiemQuanTamViewHolder holder = (DiemQuanTamViewHolder) viewHolder;
		holder.btnCheckIn.setOnClickListener(v -> {
			// TODO Auto-generated method stub
			if (mListener != null)
				mListener.checkIn(position);
		});
		holder.rlDetail.setOnClickListener(v -> {
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
		DoiTacLienQuan item = mLists.get(position);
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

