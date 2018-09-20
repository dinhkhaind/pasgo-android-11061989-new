package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.BuildConfig;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.FilterView;

import java.util.ArrayList;

public class DestinationFilterAdapter extends ArrayAdapter<FilterView> {
	private ArrayList<FilterView> mItems;
    private Context context;
	private static LayoutInflater mInflater = null;
    private DiaDiemFilterListenner diaDiemFilterListenner;
	private boolean mNearBy =false;

    public  interface DiaDiemFilterListenner{
        void check(int position, boolean isCheck);
    }
	public DestinationFilterAdapter(Context context, ArrayList<FilterView> items, DiaDiemFilterListenner diaDiemFilterListenner, boolean nearBy) {
		super(context, 0, items);
		this.context = context;
        this.mItems = items;
        this.diaDiemFilterListenner = diaDiemFilterListenner;
		this.mNearBy = nearBy;
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
	public FilterView getItem(int position) {
		return super.getItem(position);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		Holder holder = null;
		if (vi == null) {
			vi = mInflater.inflate(R.layout.item_dia_diem_filter, null);
			holder = new Holder();
			holder.item = mItems.get(position);
			holder.tvName = (TextView) vi.findViewById(R.id.tvName);
            holder.chkCheck = (CheckBox) vi.findViewById(R.id.chkCheck);
            holder.rlChild = (RelativeLayout) vi.findViewById(R.id.rlChild);
            holder.lnParent = (LinearLayout) vi.findViewById(R.id.lnParent);
            holder.tvNameParent = (TextView) vi.findViewById(R.id.tvNameParent);
            holder.view = (View) vi.findViewById(R.id.view);
			holder.lnFilterAll = (LinearLayout)vi.findViewById(R.id.filter_all_ln);
			vi.setTag(holder);
		} else {
			holder = (Holder) vi.getTag();
            holder.chkCheck.setOnCheckedChangeListener(null);
		}

		FilterView item =mItems.get(position);
		//ẩn filter của TagName!=tất cả

		//
        if(item.isParent())
        {
            holder.rlChild.setVisibility(View.GONE);
            holder.lnParent.setVisibility(View.VISIBLE);
        }else
        {
            holder.rlChild.setVisibility(View.VISIBLE);
            holder.lnParent.setVisibility(View.GONE);
        }
        if(position==0)
            holder.view.setVisibility(View.GONE);
        else
            holder.view.setVisibility(View.VISIBLE);
		holder.tvName.setText(item.getName());
        holder.chkCheck.setChecked(item.isCheck());
        holder.tvNameParent.setText(item.getName());
        holder.chkCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (diaDiemFilterListenner != null)
					diaDiemFilterListenner.check(position, b);
			}
		});
		holder.rlChild.setOnClickListener(view -> {
            if (diaDiemFilterListenner != null)
                diaDiemFilterListenner.check(position,!mItems.get(position).isCheck() );
        });
		//
		if(mNearBy) {
			int ganBanId = BuildConfig.DEBUG ? 154 : 1;
			if (item.getId() == ganBanId) {
				holder.chkCheck.setVisibility(View.GONE);
			} else {
				holder.chkCheck.setVisibility(View.VISIBLE);
			}
		}
		return vi;
	}

	static class Holder {
		public FilterView item;
		public TextView tvName;
        public CheckBox chkCheck;
        public LinearLayout lnParent;
        public TextView tvNameParent;
        public RelativeLayout rlChild;
        public View view;
		public LinearLayout lnFilterAll;
	}

	public ArrayList<FilterView> getItems() {
		return mItems;
	}

}