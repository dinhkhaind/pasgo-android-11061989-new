package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;

import java.util.ArrayList;

public class ImageViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> mLists;

	public interface LikeCommentListenner {
		void likeComment(int position);
	}

	public ImageViewAdapter(Context context, ArrayList<String> lists) {
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
		return layoutInflater
				.inflate(R.layout.row_image_view, viewGroup, false);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final String item = mLists.get(position);
		if (convertView == null) {
			convertView = createViewForPosition(parent);
		}
		SimpleDraweeView imgLogo = (SimpleDraweeView) convertView
				.findViewById(R.id.img1);
		imgLogo.setImageURI(item);
		return convertView;
	}
}
