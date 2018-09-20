package com.onepas.android.pasgo.ui.pasgocard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.MaDatXe;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class DatXeAdapter extends BaseAdapter {

	public interface danhSachTaiTroListener {
		void diNgay(int position);

		void diemTaiTro(int position);
	}

	private Context mContext;
	private ArrayList<MaDatXe> mLists;
	private danhSachTaiTroListener mListener;

	public DatXeAdapter(Context context,
						ArrayList<MaDatXe> lists, danhSachTaiTroListener listener) {
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
		return layoutInflater.inflate(R.layout.row_ma_dat_xe,
				viewGroup, false);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MaDatXe item = mLists.get(position);
		Button btnDiNgay;
		Button btnDiemTaiTro;
		TextView tvMaTaiTro;
		TextView tvMenhGia;
		TextView tvHanSuDung;
		TextView tvDiemDen;
		ImageView imgArrowRight;
		LinearLayout lnKhuyenMaiChiNhanh;
		LinearLayout lnKhuyenMaiTuDo;
		if (convertView == null) {
			convertView = createViewForPosition(parent);
		}
		tvMaTaiTro = (TextView) convertView.findViewById(R.id.tvMaTaiTro);
		tvMenhGia = (TextView) convertView.findViewById(R.id.tvMenhGia);
		tvHanSuDung = (TextView) convertView.findViewById(R.id.tvHanSuDung);
		tvDiemDen = (TextView) convertView.findViewById(R.id.tvDiemDen);
		btnDiNgay = (Button) convertView.findViewById(R.id.btnDiNgay);
		btnDiemTaiTro = (Button) convertView.findViewById(R.id.btnDiemTaiTro);
		imgArrowRight = (ImageView) convertView.findViewById(R.id.imgArrowRight);
		lnKhuyenMaiChiNhanh = (LinearLayout) convertView.findViewById(R.id.lnKhuyenMaiChiNhanh);
		lnKhuyenMaiTuDo = (LinearLayout) convertView.findViewById(R.id.lnKhuyenMaiTuDo);
		tvMaTaiTro.setText(" " + item.getTenMa());
		tvMenhGia.setText(" " + Utils.formatMoney(mContext,item.getSoTien()+""));
		if(item.isHieuLuc())
		{
			tvHanSuDung.setText(" " + item.getHanSuDung());
			tvHanSuDung.setTextColor(Utils.getColor(mContext, android.R.color.darker_gray));
		}else{
			tvHanSuDung.setText(" (" + StringUtils.getStringByResourse(mContext, R.string.not_yet_ready)+")");
			tvHanSuDung.setTextColor(Utils.getColor(mContext, R.color.red));
		}
		if (item.getLoaiKhuyenMai() == 1) {
			tvDiemDen.setText(" " + item.getDiemDen());
			imgArrowRight.setVisibility(View.VISIBLE);
			lnKhuyenMaiChiNhanh.setVisibility(View.VISIBLE);
			lnKhuyenMaiTuDo.setVisibility(View.GONE);
		} else {
			imgArrowRight.setVisibility(View.GONE);
			lnKhuyenMaiChiNhanh.setVisibility(View.GONE);
			lnKhuyenMaiTuDo.setVisibility(View.VISIBLE);
			btnDiNgay.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.diNgay(position);
            });
			btnDiemTaiTro.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if (mListener != null)
                    mListener.diemTaiTro(position);
            });
		}

		return convertView;
	}

}
