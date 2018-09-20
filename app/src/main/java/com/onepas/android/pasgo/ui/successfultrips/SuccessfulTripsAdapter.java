/*
 * Copyright (C) 2014 OnePas.
 */
package com.onepas.android.pasgo.ui.successfultrips;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.LichSuChuyenDiItem;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SuccessfulTripsAdapter extends BaseAdapter {

	private Context context;
	private List<LichSuChuyenDiItem> listRow = new ArrayList<LichSuChuyenDiItem>();
	viewHolder holder;
	int res;

	public SuccessfulTripsAdapter(Context context,
								  List<LichSuChuyenDiItem> listRow, int res) {
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
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(res, null);
			holder = new viewHolder();

			holder.diaChiDon = (TextView) convertView
					.findViewById(R.id.diaChiDon);
			holder.diaChiDen = (TextView) convertView
					.findViewById(R.id.diaChiDen);
			holder.gio = (TextView) convertView.findViewById(R.id.gio);
			holder.ngay = (TextView) convertView.findViewById(R.id.ngay);
            holder.imgHuy = (ImageView) convertView.findViewById(R.id.imgHuy);
            holder.imgAvata = (ImageView) convertView.findViewById(R.id.imgAvata);
            holder.tvMonth= (TextView) convertView.findViewById(R.id.tvMonth);
            holder.rlValue= (RelativeLayout) convertView.findViewById(R.id.rlValue);
			holder.tvTienTaiTro = (TextView) convertView.findViewById(R.id.tv_tai_tro);
			holder.tvLoaiDatXe = (TextView) convertView.findViewById(R.id.tvLoaiDatXe);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		LichSuChuyenDiItem item = listRow.get(position);
        int month = item.getKey();
        if(month==0) {
            holder.tvMonth.setVisibility(View.GONE);
            holder.rlValue.setVisibility(View.VISIBLE);
            String ngayGio = item.getDateTime();
            String[] separated = ngayGio.split(" ");
            String ngay = separated[0];
            String gio = separated[1];

            holder.ngay.setText(ngay);
            holder.gio.setText(gio);
            holder.diaChiDon.setText(item.getDiemDon());
            holder.diaChiDen.setText(item.getDiemDen());
			holder.tvLoaiDatXe.setText(ParserUtils.getTenLoaiHinh(context,item.getLoaiHinhId()));
            if (item.isKhuyenMai()) {
				holder.tvTienTaiTro.setVisibility(View.VISIBLE);
				holder.tvTienTaiTro.setText(context.getString(R.string.tai_tro)+" "+ item.getTienKhuyenMaiFormat());
			}
            else {
				holder.tvTienTaiTro.setVisibility(View.GONE);
			}
            if (item.isHangXe())
                holder.imgAvata.setImageResource(R.drawable.ic_ls_hanhxe);
            else
                holder.imgAvata.setImageResource(R.drawable.ic_ls_taixe);
			if(item.getIsThanhCong()==1)
			{
				holder.imgHuy.setVisibility(View.GONE);
			}else{
				holder.imgHuy.setVisibility(View.VISIBLE);
			}
        }else
        {
            holder.tvMonth.setVisibility(View.VISIBLE);
            holder.rlValue.setVisibility(View.GONE);
            String monthYear=String.format("%s %s/%s",StringUtils.getStringByResourse(context,R.string.thang), month,item.getYear());
            holder.tvMonth.setText(monthYear);
        }
		return convertView;
	}

	public class viewHolder {
		TextView diaChiDon;
		TextView diaChiDen;
		TextView gio;
		TextView ngay;
        ImageView imgHuy;
        ImageView imgAvata;
        TextView tvMonth;
        RelativeLayout rlValue;
		TextView tvTienTaiTro;
		TextView  tvLoaiDatXe;
	}

	public Resources getResources() {
		return null;
	}
}
