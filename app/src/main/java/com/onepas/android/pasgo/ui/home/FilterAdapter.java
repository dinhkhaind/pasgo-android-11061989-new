package com.onepas.android.pasgo.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.FilterView;

import java.util.ArrayList;

public class FilterAdapter extends ArrayAdapter<FilterView> {
	private ArrayList<FilterView> mItems;
    private Context context;
	private static LayoutInflater mInflater = null;
    private DiaDiemFilterListenner diaDiemFilterListenner;

    public  interface DiaDiemFilterListenner{
        void check(int position, boolean isCheck);
    }
	public FilterAdapter(Context context, ArrayList<FilterView> items, DiaDiemFilterListenner diaDiemFilterListenner) {
		super(context, 0, items);
		this.context = context;
        this.mItems = items;
        this.diaDiemFilterListenner = diaDiemFilterListenner;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	public void updateList(ArrayList<FilterView> filterViews)
	{
		mItems = filterViews;
		notifyDataSetChanged();
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
			vi = mInflater.inflate(R.layout.item_home_filter, null);
			holder = new Holder();
			holder.item = mItems.get(position);
			holder.tvName = (TextView) vi.findViewById(R.id.tvName);
            holder.chkCheck = (CheckBox) vi.findViewById(R.id.chkCheck);
            holder.lnChild = (LinearLayout) vi.findViewById(R.id.lnChild);
            holder.lnParent = (LinearLayout) vi.findViewById(R.id.lnParent);
            holder.tvNameParent = (TextView) vi.findViewById(R.id.tvNameParent);
			holder.lnFilterAll = (LinearLayout)vi.findViewById(R.id.filter_all_ln);
			holder.vChild =(View)vi.findViewById(R.id.view_child_v);
			vi.setTag(holder);
		} else {
			holder = (Holder) vi.getTag();
            holder.chkCheck.setOnCheckedChangeListener(null);
		}

		FilterView item =mItems.get(position);
        if(item.isParent())
        {
            holder.lnChild.setVisibility(View.GONE);
            holder.lnParent.setVisibility(View.VISIBLE);
        }else
        {
            holder.lnChild.setVisibility(View.VISIBLE);
            holder.lnParent.setVisibility(View.GONE);
        }
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
		holder.lnChild.setOnClickListener(view -> {
            if (diaDiemFilterListenner != null)
                diaDiemFilterListenner.check(position,!mItems.get(position).isCheck() );
        });
		if(position == mItems.size()-1)
			holder.vChild.setVisibility(View.GONE);
		else
			holder.vChild.setVisibility(View.VISIBLE);
		return vi;
	}

	static class Holder {
		public FilterView item;
		public TextView tvName;
        public CheckBox chkCheck;
        public LinearLayout lnParent;
        public TextView tvNameParent;
        public LinearLayout lnChild;
		public LinearLayout lnFilterAll;
		private View vChild;
	}

	public ArrayList<FilterView> getItems() {
		return mItems;
	}

}