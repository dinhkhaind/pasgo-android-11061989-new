package com.onepas.android.pasgo.ui.announcements;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.TinKhuyenMaiDoiTac;
import com.onepas.android.pasgo.ui.announcements.holder.Detail3ItemViewHolder;

import java.util.ArrayList;

public class AnnounCementsDetail3Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Context mContext;
	private ArrayList<TinKhuyenMaiDoiTac> mLists;
	private AnnounCementsDetail3Listener mListener;
	public interface AnnounCementsDetail3Listener {
		void checkIn(int position);
		void detail(int position);
	}
	public AnnounCementsDetail3Adapter(Context context, ArrayList<TinKhuyenMaiDoiTac> lists, AnnounCementsDetail3Listener listListener) {
		this.mContext = context;
		this.mLists = lists;
		this.mListener =listListener;
	}
	public void updateList(ArrayList<TinKhuyenMaiDoiTac> data) {
		if(data==null)
			return;
		mLists = data;
		notifyDataSetChanged();
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		final View view =  LayoutInflater.from(context).inflate(R.layout.row_tim_km_chi_tiet_3, parent, false);
		return  Detail3ItemViewHolder.newInstance(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
			Detail3ItemViewHolder holder = (Detail3ItemViewHolder) viewHolder;
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
			TinKhuyenMaiDoiTac item = mLists.get(position); // header
			holder.setItemValue(item,mContext);
	}

	public int getBasicItemCount() {
		return mLists == null ? 0 : mLists.size();
	}


	@Override
	public int getItemCount() {
		return getBasicItemCount();
	}
}

