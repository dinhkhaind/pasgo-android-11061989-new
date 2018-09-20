package com.onepas.android.pasgo.ui.pasgocard;

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

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.Tinh;

import java.util.ArrayList;

public class FilterTinhAdapter extends ArrayAdapter<Tinh> {
	private ArrayList<Tinh> mItems;
    private Context context;
	private static LayoutInflater mInflater = null;
    private DiaDiemFilterListenner diaDiemFilterListenner;
    public  interface DiaDiemFilterListenner{
        void check(int position, boolean isCheck);
    }
	public FilterTinhAdapter(Context context, ArrayList<Tinh> items, DiaDiemFilterListenner diaDiemFilterListenner) {
		super(context, 0, items);
		this.context = context;
        this.mItems = items;
        this.diaDiemFilterListenner = diaDiemFilterListenner;
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
	public Tinh getItem(int position) {
		return super.getItem(position);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		Holder holder = null;
		if (vi == null) {
			vi = mInflater.inflate(R.layout.item_dat_cho_diem_den_filter, null);
			holder = new Holder();
			holder.item = mItems.get(position);
			holder.tvName = (TextView) vi.findViewById(R.id.tvName);
            holder.chkCheck = (CheckBox) vi.findViewById(R.id.chkCheck);
            holder.rlChild = (RelativeLayout) vi.findViewById(R.id.rlChild);
            holder.lnParent = (LinearLayout) vi.findViewById(R.id.lnParent);
            holder.tvNameParent = (TextView) vi.findViewById(R.id.tvNameParent);
            holder.view = (View) vi.findViewById(R.id.view);

			vi.setTag(holder);
		} else {
			holder = (Holder) vi.getTag();
            holder.chkCheck.setOnCheckedChangeListener(null);
		}

		Tinh item =mItems.get(position);
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
		holder.tvName.setText(item.getTen());
        holder.chkCheck.setChecked(item.isCheck());
        holder.tvNameParent.setText(item.getTen());
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
		return vi;
	}

	static class Holder {
		public Tinh item;
		public TextView tvName;
        public CheckBox chkCheck;
        public LinearLayout lnParent;
        public TextView tvNameParent;
        public RelativeLayout rlChild;
        public View view;
	}

	public ArrayList<Tinh> getItems() {
		return mItems;
	}

}