package com.onepas.android.pasgo.ui.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.Category;
import com.onepas.android.pasgo.ui.category.holder.CategoryItemViewHolder;
import com.onepas.android.pasgo.ui.recyclerView.RecyclerHeaderViewHolder;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 2;
	private static final int TYPE_ITEM = 1;
	private Context mContext;
	private ArrayList<Category> mLists;
	private DiemDenListener mListener;
	public interface DiemDenListener {
		void detail(int position);
	}
	public CategoryAdapter(Context context, ArrayList<Category> lists, DiemDenListener listListener) {
		this.mContext = context;
		this.mLists = lists;
		this.mListener =listListener;
	}
	public void updateList(ArrayList<Category> data) {
		if(data==null)
			return;
		mLists = data;
		notifyDataSetChanged();
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		if (viewType == TYPE_ITEM) {
			final View view =  LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
			return  CategoryItemViewHolder.newInstance(view);
		} else if (viewType == TYPE_HEADER) {
			final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
			return new RecyclerHeaderViewHolder(view);
		}
		throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		if (!isPositionHeader(position)) {
			CategoryItemViewHolder holder = (CategoryItemViewHolder) viewHolder;
			holder.lnChild.setOnClickListener(v -> {
				// TODO Auto-generated method stub
				if (mListener != null)
					mListener.detail(position - 1);
			});
			Category item = mLists.get(position - 1); // header
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

