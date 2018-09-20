package com.onepas.android.pasgo.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.HomeCategory;

import java.util.ArrayList;
import java.util.List;

public class HomeCategory8Adapter extends BaseAdapter {

	List<HomeCategory> lists = new ArrayList<>();
	private Context context;
	ViewHolder holder = null;

	public HomeCategory8Adapter(Context context, List<HomeCategory> lists) {
		this.lists = lists;
		this.context = context;
	}
	public void updateList(ArrayList<HomeCategory> data) {
		if(data==null)
			return;
		this.lists = data;
		notifyDataSetChanged();
	}

	private class ViewHolder {
		TextView name_tv;
	}

	private View createViewForPosition(ViewGroup viewGroup) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.row_home_category_row,
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
			holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name_tv.setText(lists.get(position).getTieuDe());
		return convertView;
	}

}
