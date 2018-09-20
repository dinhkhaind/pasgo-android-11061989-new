package com.onepas.android.pasgo.ui.partner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ItemDiaChiChiNhanh;

public class ChiNhanhAdapter extends ArrayAdapter<ItemDiaChiChiNhanh> {

	List<ItemDiaChiChiNhanh> listItemAdressFree = new ArrayList<ItemDiaChiChiNhanh>();

	private Context context;

	ViewHolder holder = null;
	int textViewResourceId;
	int type;

	public ChiNhanhAdapter(Context context, int textViewResourceId,
			List<ItemDiaChiChiNhanh> listItemAdressFree, int type) {
		super(context, textViewResourceId, listItemAdressFree);
		this.listItemAdressFree = listItemAdressFree;
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.type = type;
	}

	private class ViewHolder {
		TextView ten;
		TextView diaChi;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater vi = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(textViewResourceId, null);

			if (type == 1) {

			} else if (type == 2) {
				holder.diaChi = (TextView) convertView
						.findViewById(R.id.tv_dia_chi);
			}
			holder.ten = (TextView) convertView.findViewById(R.id.lblTenDoiTac);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (listItemAdressFree.size() > 0) {
			if (type == 1) {
				holder.ten
						.setText(listItemAdressFree.get(position).getDiaChi());
			} else if (type == 2) {
				holder.ten.setText(listItemAdressFree.get(position).getTen());
				holder.diaChi.setText(listItemAdressFree.get(position)
						.getDiaChi());
			}
		}

		return convertView;
	}

}
