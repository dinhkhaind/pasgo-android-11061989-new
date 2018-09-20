package com.onepas.android.pasgo.ui.partner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ObjHistoryDatXe;
import com.onepas.android.pasgo.utils.Utils;

public class HistoryAdapter extends ArrayAdapter<ObjHistoryDatXe> {

	List<ObjHistoryDatXe> listItemAdressFree = new ArrayList<ObjHistoryDatXe>();

	private Context context;

	ViewHolder holder = null;
	int textViewResourceId;
	int type;

	public HistoryAdapter(Context context, int textViewResourceId,
						  List<ObjHistoryDatXe> listItemAdressFree, int type) {
		super(context, textViewResourceId, listItemAdressFree);
		this.listItemAdressFree = listItemAdressFree;
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.type = type;
	}

	private class ViewHolder {
		TextView ten;
		TextView diaChi;
		TextView thoiGian;
		TextView tvNgay;
		TextView giaTien;
		ImageView imLeft;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater vi = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(textViewResourceId, null);
			holder.imLeft = (ImageView) convertView.findViewById(R.id.im_left);
			holder.ten = (TextView) convertView.findViewById(R.id.tvTen);
			holder.diaChi = (TextView) convertView.findViewById(R.id.tvDiaChi);
			holder.giaTien = (TextView) convertView.findViewById(R.id.gia_tien);
			holder.thoiGian = (TextView) convertView
					.findViewById(R.id.tvThoiGian);
			holder.tvNgay = (TextView) convertView.findViewById(R.id.tvNgay);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (listItemAdressFree.size() > 0) {
			boolean daXacNhan = listItemAdressFree.get(position).isDaXacNhan();
			String giaUocTinh = listItemAdressFree.get(position)
					.getGiaUocTinh();
			String kmUocTinh = listItemAdressFree.get(position).getKmUocTinh();
			String uocTinh = kmUocTinh + "Km" + " ~ " + giaUocTinh + " "
					+ context.getResources().getString(R.string.vnd);

			if (daXacNhan) {
				holder.imLeft.setImageResource(R.drawable.ic_lichsukm);
			} else {
				holder.imLeft.setImageResource(R.drawable.ic_lichsukm2);
			}
			holder.giaTien.setText(uocTinh);
			holder.ten.setText(listItemAdressFree.get(position)
					.getTenChiNhanhDoiTac());
			holder.diaChi.setText(listItemAdressFree.get(position)
					.getDiaDiemDen());
			String thoiGian = listItemAdressFree.get(position).getThoiGian();
			// String converNgay = converNgay(thoiGian);
			// holder.thoiGian.setText(converNgay);
			setTextThoiGian(thoiGian);
		}

		return convertView;
	}

	private void setTextThoiGian(String thoiGian) {
		String strNgay = "";
		String ngay = "";
		String gio = "";
		try {
			String[] arrThoiGian = thoiGian.split(" ");
			ngay = arrThoiGian[0];
			gio = arrThoiGian[1];
		} catch (Exception e) {
			Utils.Log(Pasgo.TAG, "" + e.toString());
		}
		if (ngay == null || ngay.equals("")) {

		} else {
			try {
				String[] arrThoiGian = ngay.split("/");
				String ngayStr = arrThoiGian[2];
				int intNgay = Integer.parseInt(ngayStr);

				Calendar c = Calendar.getInstance();
				int day = c.get(Calendar.DAY_OF_MONTH);
				if (intNgay == day) {
					strNgay = context.getResources()
							.getString(R.string.hom_nay);
				} else if ((day - intNgay) == 1) {
					strNgay = context.getResources()
							.getString(R.string.hom_qua);
				} else {
					strNgay = ngay;
				}
//				strNgay = gio + "\n" + strNgay;
				holder.thoiGian.setText(gio);
				holder.tvNgay.setText(strNgay);

			} catch (Exception e) {
				Utils.Log("Exception Convert Day", "" + e.toString());
			}
		}
		// return strNgay;
	}

}
