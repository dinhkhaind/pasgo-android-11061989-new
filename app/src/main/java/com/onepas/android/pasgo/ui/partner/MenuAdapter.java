package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhBangGia;
import com.onepas.android.pasgo.ui.partner.holder.MenuViewHolder;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_ITEM = 1;
	private Context mContext;
	private ArrayList<AnhBangGia> mLists;
	private DetailPriceListListener imageListListener;
	public interface DetailPriceListListener{
		void detail(int postion);
	}
	public MenuAdapter(Context context, ArrayList<AnhBangGia> lists, DetailPriceListListener imageListListener) {
		this.mContext = context;
		this.mLists = lists;
		this.imageListListener = imageListListener;
	}
	public void updateList(ArrayList<AnhBangGia> data) {
		if(data==null)
			return;
		mLists = data;
		notifyDataSetChanged();
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		final View view =  LayoutInflater.from(context).inflate(R.layout.row_menu_list, parent, false);
		return  MenuViewHolder.newInstance(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		MenuViewHolder holder = (MenuViewHolder) viewHolder;
		AnhBangGia item = mLists.get(position);
		holder.setItemValue(item,mContext);
		holder.imgLogo.setOnClickListener(v->{
			if(imageListListener!=null)
				imageListListener.detail(position);
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

