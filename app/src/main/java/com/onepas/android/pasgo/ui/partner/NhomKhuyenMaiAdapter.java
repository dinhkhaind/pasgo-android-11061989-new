package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.ItemAdressFree;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NhomKhuyenMaiAdapter extends ArrayAdapter<ItemAdressFree> {

	List<ItemAdressFree> listItemAdressFree = new ArrayList<ItemAdressFree>();

	private Context context;

	ViewHolder holder = null;
	private int textViewResourceId;

	public NhomKhuyenMaiAdapter(Context context, int textViewResourceId,
								List<ItemAdressFree> listItemAdressFree) {
		super(context, textViewResourceId, listItemAdressFree);
		this.listItemAdressFree = listItemAdressFree;
		this.context = context;
		this.textViewResourceId = textViewResourceId;
	}

	private class ViewHolder {
		SimpleDraweeView imgLogo;
		SimpleDraweeView imgLogoDefault;
		TextView tvTenDoiTac;
		TextView tvTenDuong;
		TextView tvKm;
		TextView tvRadius;
		TextView tvChuyenMon;
		private TextView tvRating;
		private TextView tvNoiDungTaiTro;
		private RatingBar ratingChatLuong;
		private TextView tvGiaTrungBinh;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater vi = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(textViewResourceId, null);

			holder.imgLogo = (SimpleDraweeView) convertView
					.findViewById(R.id.imgLogo);
			holder.imgLogoDefault = (SimpleDraweeView) convertView
					.findViewById(R.id.imgLogoDefault);
			holder.tvTenDoiTac = (TextView) convertView
					.findViewById(R.id.tvTenDoiTac);
			holder.tvTenDuong = (TextView) convertView
					.findViewById(R.id.tvTenDuong);
			holder.tvKm = (TextView) convertView.findViewById(R.id.tvKm);
			holder.tvRadius = (TextView) convertView
					.findViewById(R.id.tvKhoangCach);
            holder.ratingChatLuong = (RatingBar) convertView
                    .findViewById(R.id.ratingChatLuong);
			holder.tvChuyenMon = (TextView) convertView.findViewById(R.id.tvChuyenMon);
			holder.tvRating = (TextView) convertView.findViewById(R.id.tvRating);
			holder.tvNoiDungTaiTro = (TextView) convertView.findViewById(R.id.tvNoiDungTaiTro);
			holder.tvGiaTrungBinh = (TextView)convertView.findViewById(R.id.gia_trung_binh_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			/*holder.imgLogo.setImageUrl(null, Pasgo.getInstance()
					.getImageLoader());*/
		}

		if (listItemAdressFree.size() > 0) {
			ItemAdressFree item = listItemAdressFree.get(position);
			String urlLogo = item.getLogo();
			String tenDoiTac = item.getTenDoiTac();
			String tenDuong = item.getTenDuong();
			String radius = item.getKm() + "Km";
            double danhGia = item.getDanhGia();
			double chatLuong = item.getChatLuong();
			String taiTro= item.getTaiTro();
			if (!StringUtils.isEmpty(urlLogo)) {
				holder.imgLogo.setImageURI(urlLogo+"&width=250");
				holder.imgLogo.setVisibility(View.VISIBLE);
				holder.imgLogoDefault.setVisibility(View.GONE);
			} else {
				holder.imgLogo.setVisibility(View.GONE);
				holder.imgLogoDefault.setVisibility(View.VISIBLE);
			}
			holder.tvChuyenMon.setText(item.getChuyenMon());
			Utils.setTextViewHtml(holder.tvTenDoiTac,tenDoiTac);
			Utils.setTextViewHtml(holder.tvTenDuong,tenDuong);
			holder.tvRadius.setText( radius);
            holder.ratingChatLuong.setRating((float) chatLuong);
			holder.tvRating.setText(danhGia + "");
			Utils.setTextViewHtml(holder.tvNoiDungTaiTro,taiTro);
			Utils.setTextViewHtml(holder.tvGiaTrungBinh, item.getGiaTrungBinh());
		}

		return convertView;
	}
}