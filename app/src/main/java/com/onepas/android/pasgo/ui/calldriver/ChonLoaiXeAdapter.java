package com.onepas.android.pasgo.ui.calldriver;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.LoaiXe;
import com.onepas.android.pasgo.utils.Utils;

public class ChonLoaiXeAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<LoaiXe> mLists;

	public ChonLoaiXeAdapter(Context context, ArrayList<LoaiXe> lists) {
		this.mContext = context;
		this.mLists = lists;
	}

	@Override
	public int getCount() {
		return mLists.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private View createViewForPosition(ViewGroup viewGroup) {
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.row_chon_loai_xe, viewGroup,
				false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LoaiXe item = mLists.get(position);
		if (convertView == null) {
			convertView = createViewForPosition(parent);
		}
		ImageView imgTaxi = (ImageView) convertView.findViewById(R.id.imgTaxi);
		TextView tvTaxi = (TextView) convertView.findViewById(R.id.tvTaxi);
		if (position == 0) {
			tvTaxi.setTypeface(null, Typeface.BOLD);
		}
		// set Data
		Utils.setBackground(imgTaxi,
				Utils.getDrawable(mContext, item.getImage()));
		tvTaxi.setText(item.getName());
		return convertView;
	}

}
