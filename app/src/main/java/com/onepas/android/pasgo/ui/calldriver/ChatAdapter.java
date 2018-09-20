package com.onepas.android.pasgo.ui.calldriver;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.InfoChat;
import com.onepas.android.pasgo.utils.StringUtils;

public class ChatAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<InfoChat> mLists;

	public ChatAdapter(Context context, ArrayList<InfoChat> lists) {
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
		return layoutInflater.inflate(R.layout.row_chat, viewGroup, false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InfoChat item = mLists.get(position);
		if (convertView == null) {
			convertView = createViewForPosition(parent);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		TextView tvComment = (TextView) convertView
				.findViewById(R.id.tvComment);
		// set Data
		String name = "";
		if (item.getFullName()
				.equals(Pasgo.getInstance().prefs.getUserName()))
			name = StringUtils.getStringByResourse(mContext, R.string.toi);
		else
			name = StringUtils.getStringByResourse(mContext, R.string.tai_xe);

		tvName.setText(name);
		tvComment.setText(item.getContent());
		return convertView;
	}
}
