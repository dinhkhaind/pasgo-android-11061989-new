package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.BinhLuan;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.TimeAgoUtils;

import java.util.ArrayList;
import java.util.List;

public class BinhLuanAdapter extends BaseAdapter {

	List<BinhLuan> lists = new ArrayList<BinhLuan>();
	private Context context;
	ViewHolder holder = null;
	TimeAgoUtils timeAgoUtils;
	public BinhLuanAdapter(Context context, List<BinhLuan> lists) {
		this.lists = lists;
		this.context = context;
		timeAgoUtils = new TimeAgoUtils(context);
	}

	private class ViewHolder {
		private SimpleDraweeView imgAvata;
		private TextView titName;
		private TextView tvtime;
		private TextView tvRating;
		private TextView tvComment;
		private TextView tvChatLuongTitle;
		private LinearLayout lnDetailRating;
	}

	private View createViewForPosition(ViewGroup viewGroup) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.row_detail_comment,
				viewGroup, false);
	}
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = createViewForPosition(parent);
			holder.imgAvata = (SimpleDraweeView) convertView.findViewById(R.id.imgAvata);
			holder.titName = (TextView) convertView.findViewById(R.id.titName);
			holder.tvtime = (TextView) convertView.findViewById(R.id.tvtime);
			holder.tvRating = (TextView)convertView.findViewById(R.id.rating_tv);
			holder.tvComment = (TextView) convertView.findViewById(R.id.comment_tv);
			holder.tvChatLuongTitle = (TextView) convertView.findViewById(R.id.danh_gia_title_tv);
			holder.lnDetailRating = (LinearLayout)convertView.findViewById(R.id.detail_rating_ln);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BinhLuan item = lists.get(position);
		holder.imgAvata.setImageURI(item.getAnhNguoiDung());
		holder.titName.setText(item.getTenNguoiDung());
		Long time = DatehepperUtil.convertDatetimeToLongDate(
				item.getThoiGian(), DatehepperUtil.yyyyMMddHHmmss);
		holder.tvtime.setText(""+ timeAgoUtils.timeAgo(time));
		holder.tvComment.setText(item.getNoiDung());
		holder.tvRating.setText(item.getDanhGia()+"");
		holder.tvChatLuongTitle.setText(context.getString(R.string.danh_gia).toUpperCase());
		if(item.getDanhGia()>0)
			holder.lnDetailRating.setVisibility(View.VISIBLE);
		else
			holder.lnDetailRating.setVisibility(View.GONE);
		return convertView;
	}

}
