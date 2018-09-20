package com.onepas.android.pasgo.ui.reserve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.TagSpecial;

import java.util.ArrayList;
import java.util.List;

public class TagSpecialAdapter extends BaseAdapter {

	List<TagSpecial> lists = new ArrayList<>();
	private Context context;
	ViewHolder holder = null;

	public TagSpecialAdapter(Context context, List<TagSpecial> lists) {
		this.lists = lists;
		this.context = context;
	}

	private class ViewHolder {
		TextView name_tv;
		TextView description_tv;
	}

	private View createViewForPosition(ViewGroup viewGroup) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.row_tag_special,
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
			holder.description_tv = (TextView)convertView.findViewById(R.id.description_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name_tv.setText(lists.get(position).getTen());
		holder.description_tv.setText(lists.get(position).getMoTa());
		return convertView;
	}

}
