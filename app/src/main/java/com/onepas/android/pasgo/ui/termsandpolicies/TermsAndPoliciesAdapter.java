package com.onepas.android.pasgo.ui.termsandpolicies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DanhSachQuyDinh;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class TermsAndPoliciesAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<DanhSachQuyDinh> mLists;
	public TermsAndPoliciesAdapter(Context context, ArrayList<DanhSachQuyDinh> lists) {
		this.mContext = context;
		this.mLists = lists;
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
		return layoutInflater.inflate(R.layout.row_danh_sach_quy_dinh, viewGroup,
				false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        DanhSachQuyDinh item = mLists.get(position);
		if (convertView == null) {
			convertView = createViewForPosition(parent);
		}
		ImageView imgView = (ImageView) convertView.findViewById(R.id.imgView);
		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		// set Data
		Utils.setBackground(imgView,Utils.getDrawable(mContext,item.getImage()));
        tvName.setText(item.getName());
		return convertView;
	}

}
