package com.onepas.android.pasgo.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.DanhMucChinh;

import java.util.ArrayList;

public class DanhMucChinhAdapter extends ArrayAdapter<DanhMucChinh> {
	private ArrayList<DanhMucChinh> mItems;
    private Context context;
	private static LayoutInflater mInflater = null;
	private  TinhHomeListenner tinhListenner;
    public  interface TinhHomeListenner{
        void check(int position);
    }
	public DanhMucChinhAdapter(Context context, ArrayList<DanhMucChinh> items, TinhHomeListenner tinhListenner) {
		super(context, 0, items);
		this.context = context;
        this.mItems = items;
		this.tinhListenner = tinhListenner;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mItems.size();
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public DanhMucChinh getItem(int position) {
		return super.getItem(position);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		Holder holder = null;
		if (vi == null) {
			vi = mInflater.inflate(R.layout.item_radio_check, null);
			holder = new Holder();
			holder.item = mItems.get(position);
			holder.tvName = (TextView) vi.findViewById(R.id.tvName);
            holder.rdoCheck = (RadioButton) vi.findViewById(R.id.rdoCheck);
			holder.rlCheck =  (RelativeLayout) vi.findViewById(R.id.rlCheck);
			holder.rdoCheck.setOnCheckedChangeListener(null);
			vi.setTag(holder);
		} else {
			holder = (Holder) vi.getTag();
            holder.rdoCheck.setOnCheckedChangeListener(null);
		}

		DanhMucChinh item =mItems.get(position);
		holder.tvName.setText(item.getTen());
        holder.rdoCheck.setChecked(item.isCheck());
		holder.rlCheck.setOnClickListener(view -> {
            if (tinhListenner != null)
                tinhListenner.check(position);
        });
		holder.rdoCheck.setOnClickListener(view -> {
            if(tinhListenner!=null)
                tinhListenner.check(position);
        });
		return vi;
	}

	static class Holder {
		public DanhMucChinh item;
		public TextView tvName;
        public RadioButton rdoCheck;
		public RelativeLayout rlCheck;
	}

	public ArrayList<DanhMucChinh> getItems() {
		return mItems;
	}

}