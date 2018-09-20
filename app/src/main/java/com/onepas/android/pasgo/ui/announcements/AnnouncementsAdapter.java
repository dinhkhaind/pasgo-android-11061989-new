/*
 * Copyright (C) 2014 OnePas.
 */
package com.onepas.android.pasgo.ui.announcements;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ItemTinKhuyenMai;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.TimeAgoUtils;

import java.util.ArrayList;
import java.util.List;


public class AnnouncementsAdapter extends BaseAdapter {

	private static final String TAG = "SearchItemAdapter";

	private Context context;
	private List<ItemTinKhuyenMai> listRow = new ArrayList<ItemTinKhuyenMai>();
	viewHolder holder;
	int res;

	public AnnouncementsAdapter(Context context, List<ItemTinKhuyenMai> listRow,
								int res) {
		this.context = context;
		this.listRow = listRow;
		this.res = res;
	}

	@Override
	public int getCount() {
		return listRow.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		TimeAgoUtils timeAgoUtils = new TimeAgoUtils(context);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.item_tin_khuyenmai,
					null);
			holder = new viewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.description =(TextView)convertView.findViewById(R.id.description);
			holder.imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.icon_img);
			holder.imgLogoDefault = (SimpleDraweeView) convertView.findViewById(R.id.icon_default_img);
			holder.mRlRowKhuyenMai =(RelativeLayout)convertView.findViewById(R.id.row_khuyen_mai_rl);
			holder.lnIcon = (LinearLayout)convertView.findViewById(R.id.icon_ln);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		holder.name.setText(listRow.get(position).getTieuDe());
		long dateLong = DatehepperUtil.convertDatetimeToLongDate(listRow.get(position).getNgayBatDau(), DatehepperUtil.ddMMyyyyHHmmss);
		holder.date.setText(timeAgoUtils.timeAgo(dateLong));
		if (listRow.get(position).getRead() == 0) {
			holder.name.setTypeface(null, Typeface.BOLD);
			holder.name.setTextColor(Color.parseColor("#000000"));
			holder.mRlRowKhuyenMai.setBackgroundColor(Color.parseColor("#fff1f4"));
			holder.lnIcon.setBackgroundColor(Color.parseColor("#fff1f4"));
		} else {
			holder.name.setTypeface(null, Typeface.NORMAL);
			holder.name.setTextColor(Color.parseColor("#aaaaaa"));
			holder.mRlRowKhuyenMai.setBackgroundColor(Color.parseColor("#FFFFFF"));
			holder.lnIcon.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
		String mota  = listRow.get(position).getMoTa();
		if(StringUtils.isEmpty(mota))
			holder.description.setVisibility(View.GONE);
		else
			holder.description.setVisibility(View.VISIBLE);
		holder.description.setText(listRow.get(position).getMoTa());
		String urlImage = listRow.get(position).getAnh();
		holder.imgLogo.setImageURI(urlImage);
		if (!StringUtils.isEmpty(urlImage)) {
			holder.imgLogo.setVisibility(View.VISIBLE);
			holder.imgLogoDefault.setVisibility(View.GONE);
		} else {
			holder.imgLogo.setVisibility(View.GONE);
			holder.imgLogoDefault.setVisibility(View.VISIBLE);
			holder.imgLogoDefault.setBackgroundResource(R.drawable.ic_launcher_full);
		}
		return convertView;
	}

	public class viewHolder {
		TextView name;
		TextView date;
		TextView description;
		SimpleDraweeView imgLogo;
		SimpleDraweeView imgLogoDefault;
		RelativeLayout mRlRowKhuyenMai;
		LinearLayout lnIcon;

	}

	public Resources getResources() {
		return null;
	}
}
