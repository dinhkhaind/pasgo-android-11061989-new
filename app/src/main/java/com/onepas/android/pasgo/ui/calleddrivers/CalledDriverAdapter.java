package com.onepas.android.pasgo.ui.calleddrivers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DatTruoc;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class CalledDriverAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<DatTruoc> mLists;
	private boolean mIsDaNhan;
	public CalledDriverAdapter(Context context, ArrayList<DatTruoc> lists, boolean isDaNhan) {
		this.mContext = context;
		this.mLists = lists;
		this.mIsDaNhan = isDaNhan;
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
		return layoutInflater.inflate(R.layout.row_dat_truoc, viewGroup,
				false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DatTruoc item = mLists.get(position);
		if (convertView == null) {
			convertView = createViewForPosition(parent);
		}
		ImageView imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);
		TextView tvDiemDon = (TextView) convertView.findViewById(R.id.tvDiemDon);
		TextView tvDiemDen = (TextView) convertView.findViewById(R.id.tvDiemDen);
		TextView tvThoiGian = (TextView) convertView.findViewById(R.id.tvThoiGian);
		TextView tvLoaiHinh = (TextView) convertView.findViewById(R.id.tvLoaiHinh);
		TextView tvTienTaiTro = (TextView) convertView.findViewById(R.id.muc_tai_tro_tv);
		// set Data
		tvDiemDon.setText(item.getDiaChiDonXe());
		tvDiemDen.setText(item.getDiaChiDen());
		tvThoiGian.setText(item.getThoiGianDonXe());
		tvLoaiHinh.setText(ParserUtils.getTenLoaiHinh(mContext, item.getLoaiHinhId()));
		tvTienTaiTro.setText(mContext.getString(R.string.tai_tro)+" "+ item.getTienKhuyenMaiFormat());
		if(item.getIsKhuyenMai()==1)
		{
			tvTienTaiTro.setVisibility(View.VISIBLE);
		}else{
			tvTienTaiTro.setVisibility(View.GONE);
		}

		if(mIsDaNhan) {
			if (item.getIsHangXe()==1)
				imgLogo.setImageResource(R.drawable.ic_ls_hanhxe);
			else {
				imgLogo.setImageResource(R.drawable.ic_ls_taixe);
			}
		}
		else
			Utils.setBackground(imgLogo,
					Utils.getDrawable(mContext, R.drawable.taxi_chuadon));
		return convertView;
	}

}
