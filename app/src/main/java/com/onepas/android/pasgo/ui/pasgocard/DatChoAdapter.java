package com.onepas.android.pasgo.ui.pasgocard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.MaDatCho;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class DatChoAdapter extends BaseAdapter {
	private viewHolder holder;
	public interface maDatChoListener {
		void suDung(int position);

	}

	private Context mContext;
	private ArrayList<MaDatCho> mLists;
	private maDatChoListener mListener;

	public DatChoAdapter(Context context,
						 ArrayList<MaDatCho> lists, maDatChoListener listener) {
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
		return layoutInflater.inflate(R.layout.row_ma_diem_den,
				viewGroup, false);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = createViewForPosition(parent);
			holder = new viewHolder();
			holder.tvNhaPhatHanh = (TextView) convertView
					.findViewById(R.id.tvNhaPhatHanh);
			holder.tvMaTaiTro = (TextView) convertView
					.findViewById(R.id.tvMaTaiTro);
			holder.tvHanSuDung = (TextView) convertView.findViewById(R.id.tvHanSuDung);
			holder.tvMenhGia = (TextView) convertView.findViewById(R.id.tvMenhGia);
			holder.tvQuangCao= (TextView) convertView.findViewById(R.id.tvQuangCao);
			holder.btnSuDung = (Button) convertView.findViewById(R.id.btnSuDung);
			holder.rlUse = (RelativeLayout) convertView.findViewById(R.id.rlUse);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		if (mLists.size()>position) {
			MaDatCho item = mLists.get(position);
			holder.tvNhaPhatHanh.setText(item.getNhaPhatHanh());
			holder.tvMaTaiTro.setText(item.getMaTaiTro());
			holder.tvHanSuDung.setText(Utils.ddMMyyyyHHmmssToHHmmddMMyyyy(item.getNgayBatDau()) +" - "+Utils.ddMMyyyyHHmmssToHHmmddMMyyyy(item.getNgayKetThuc()));
			holder.tvMenhGia.setText(" " + Utils.formatMoney(mContext, item.getGiamGia() + ""));
			holder.tvQuangCao.setText(item.getQuangCao());
			holder.tvQuangCao.setSelected(true);
			holder.btnSuDung.setOnClickListener(view -> {
                if (mListener != null)
                    mListener.suDung(position);
            });
			holder.rlUse.setOnClickListener(view -> {
                if (mListener != null)
                    mListener.suDung(position);
            });
		}

		return convertView;
	}
	public class viewHolder {
		TextView tvNhaPhatHanh;
		TextView tvMaTaiTro;
		TextView tvHanSuDung;
		TextView tvMenhGia;
		TextView tvQuangCao;
		Button btnSuDung;
		RelativeLayout rlUse;
	}
}
