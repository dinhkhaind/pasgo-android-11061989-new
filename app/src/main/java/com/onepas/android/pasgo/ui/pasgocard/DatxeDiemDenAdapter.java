package com.onepas.android.pasgo.ui.pasgocard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DanhSachTaiTroDiemDen;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class DatxeDiemDenAdapter extends BaseAdapter {
	private ViewHolder holder = null;
	public interface DanhSachTaiTroDienDenListener {
		void diemDonOrDen(int position,boolean isDen);
		void xemThem(int position);
	}

	private Context mContext;
	private ArrayList<DanhSachTaiTroDiemDen> mLists;
	private DanhSachTaiTroDienDenListener mListener;

	public DatxeDiemDenAdapter(Context context,
							   ArrayList<DanhSachTaiTroDiemDen> lists, DanhSachTaiTroDienDenListener listener) {
		this.mContext = context;
		this.mLists = lists;
		this.mListener = listener;
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
		return layoutInflater.inflate(R.layout.row_dat_xe_diem_den,
				viewGroup, false);
	}
	private class ViewHolder {
		LinearLayout lnDetail;
		Button btnDiemDon;
		Button btnDiemDen;
		TextView tvTenDoiTac;
		TextView tvTenDuong;
		TextView tvKhoangCach;
		SimpleDraweeView imgLogo;
		SimpleDraweeView imgLogoDefault;
		RatingBar ratingChatLuong;
		TextView tvChuyenMon;
		TextView tvNoiDungTaiTro;
		TextView tvRating;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		DanhSachTaiTroDiemDen item = mLists.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = createViewForPosition(parent);
			holder.tvTenDoiTac = (TextView) convertView.findViewById(R.id.tvTenDoiTac);
			holder.tvTenDuong = (TextView) convertView.findViewById(R.id.tvTenDuong);
			holder.tvKhoangCach = (TextView) convertView.findViewById(R.id.tvKhoangCach);
			holder.lnDetail = (LinearLayout) convertView.findViewById(R.id.lnDetail);
			holder.btnDiemDon = (Button) convertView.findViewById(R.id.btnDiemDon);
			holder.btnDiemDen = (Button) convertView.findViewById(R.id.btnDiemDen);
			holder.imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
			holder.imgLogoDefault =(SimpleDraweeView) convertView.findViewById(R.id.imgLogoDefault);
			holder.ratingChatLuong = (RatingBar) convertView.findViewById(R.id.ratingChatLuong);
			holder.tvChuyenMon = (TextView) convertView.findViewById(R.id.tvChuyenMon);
			holder.tvNoiDungTaiTro = (TextView)convertView.findViewById(R.id.tvNoiDungTaiTro);
			holder.tvRating = (TextView) convertView.findViewById(R.id.tvRating);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			/*holder.imgLogo.setImageUrl(null, Pasgo.getInstance()
					.getImageLoader());*/
		}

		holder.tvTenDoiTac.setText(" " + item.getTenChiNhanh());
		holder.tvTenDuong.setText(" " + item.getDiaChi());
		holder.tvTenDuong.setText(" " + item.getDiaChi());
		holder.tvKhoangCach.setText("" + item.getKm()+" Km");
		holder.tvRating.setText(item.getDanhGia() + "");
		Utils.setTextViewHtml(holder.tvNoiDungTaiTro,item.getTaiTro());
		String urlLogo=item.getLogo();

		if (!StringUtils.isEmpty(urlLogo)) {
			holder.imgLogo.setImageURI(urlLogo);
			holder.imgLogo.setVisibility(View.VISIBLE);
			holder.imgLogoDefault.setVisibility(View.GONE);
		} else {
			holder.imgLogo.setVisibility(View.GONE);
			holder.imgLogoDefault.setVisibility(View.VISIBLE);
		}
		holder.ratingChatLuong.setRating((float) item.getChatLuong());
		holder.tvChuyenMon.setText(item.getChuyenMon());
		holder.lnDetail.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (mListener != null)
                mListener.xemThem(position);
        });
		holder.btnDiemDon.setOnClickListener(v -> {
            if (mListener != null)
                mListener.diemDonOrDen(position,false);
        });
		holder.btnDiemDen.setOnClickListener(v -> {
            if (mListener != null)
                mListener.diemDonOrDen(position,true);
        });
		return convertView;
	}

}
