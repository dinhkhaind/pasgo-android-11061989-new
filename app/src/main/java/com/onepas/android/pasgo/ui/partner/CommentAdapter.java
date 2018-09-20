package com.onepas.android.pasgo.ui.partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.BinhLuanKhuyenMai;
import com.onepas.android.pasgo.utils.DatehepperUtil;
import com.onepas.android.pasgo.utils.TimeAgoUtils;
import com.onepas.android.pasgo.utils.Utils;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<BinhLuanKhuyenMai> mLists;
	private LikeCommentListenner likeCommentListenner;
	private TimeAgoUtils timeAgoUtils;

	public interface LikeCommentListenner {
		void likeComment(int position);
	}

	public CommentAdapter(Context context,
						  ArrayList<BinhLuanKhuyenMai> lists,
						  LikeCommentListenner likeCommentListenner) {
		this.mContext = context;
		this.mLists = lists;
		timeAgoUtils = new TimeAgoUtils(context);
		this.likeCommentListenner = likeCommentListenner;
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
		return layoutInflater.inflate(R.layout.item_binh_luan_khuyen_mai,
				viewGroup, false);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final BinhLuanKhuyenMai item = mLists.get(position);
		if (convertView == null) {
			convertView = createViewForPosition(parent);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		TextView tvComment = (TextView) convertView
				.findViewById(R.id.tvComment);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
		TextView tvNumberLike = (TextView) convertView
				.findViewById(R.id.tvNumberLike);
		ImageView imgLikeComment = (ImageView) convertView
				.findViewById(R.id.imgLikeComment);
		LinearLayout lnLikeComment = (LinearLayout) convertView
				.findViewById(R.id.lnLikeComment);
        RatingBar ratingBar = (RatingBar) convertView
                .findViewById(R.id.ratingBar);
		Long time = DatehepperUtil.convertDatetimeToLongDate(
				item.getThoiGian(), DatehepperUtil.yyyyMMddHHmmss);
		// set Data
		String NameAndcomment="<b>"+item.getTenNguoiDung()+"</b> ";
		Utils.setTextViewHtml(tvName,NameAndcomment);
		tvComment.setText(item.getNoiDung());
		tvTime.setText(" "+ timeAgoUtils.timeAgo(time));
		tvNumberLike.setText(item.getLuotThich());
		if (item.getDaThich() == 0)
			imgLikeComment.setImageResource(R.drawable.ic_unlikefb);
		else
			imgLikeComment.setImageResource(R.drawable.ic_likefb_cm);
        ratingBar.setRating((float) item.getDanhGia());
		lnLikeComment.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (likeCommentListenner != null) {
                likeCommentListenner.likeComment(position);
            }
        });
		return convertView;
	}
}
