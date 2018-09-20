package com.onepas.android.pasgo.ui.reserve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ReserveSearch;

import java.util.ArrayList;
import java.util.List;

public class GoiYAdapter extends BaseAdapter {

	List<ReserveSearch> lists = new ArrayList<ReserveSearch>();
	private Context context;
	ViewHolder holder = null;

	public GoiYAdapter(Context context, List<ReserveSearch> lists) {
		this.lists = lists;
		this.context = context;
	}

	private class ViewHolder {
		TextView tu_khoa_tv;
	}

	private View createViewForPosition(ViewGroup viewGroup) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.row_goi_y,
				viewGroup, false);
	}
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = createViewForPosition(parent);
			holder.tu_khoa_tv = (TextView) convertView.findViewById(R.id.tu_khoa_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tu_khoa_tv.setText(lists.get(position).getTuKhoa());
		return convertView;
	}

}
