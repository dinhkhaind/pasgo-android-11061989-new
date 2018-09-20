package com.onepas.android.pasgo.ui.reserve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.TagModel;

import java.util.ArrayList;

public class TagDialogAdapter extends ArrayAdapter<TagModel> {
	private ArrayList<TagModel> mItems;
    private Context context;
	private static LayoutInflater mInflater = null;
	private  TagDialogListenner tinhListenner;
    public  interface TagDialogListenner{
        void check(int position);
    }
	public TagDialogAdapter(Context context, ArrayList<TagModel> items, TagDialogListenner tinhListenner) {
		super(context, 0, items);
		this.context = context;
        this.mItems = items;
		this.tinhListenner = tinhListenner;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void updateList(ArrayList<TagModel> data) {
		if(data==null)
			return;
		mItems = data;
		notifyDataSetChanged();
	}
	public int getCount() {
		return mItems.size();
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public TagModel getItem(int position) {
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

		TagModel item =mItems.get(position);
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
		public TagModel item;
		public TextView tvName;
        public RadioButton rdoCheck;
		public RelativeLayout rlCheck;
	}

	public ArrayList<TagModel> getItems() {
		return mItems;
	}

}