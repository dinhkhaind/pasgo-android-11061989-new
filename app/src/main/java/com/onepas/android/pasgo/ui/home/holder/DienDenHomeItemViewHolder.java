package com.onepas.android.pasgo.ui.home.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.home.DiemDenHomeAdapter;
import com.onepas.android.pasgo.ui.reserve.DiemDenModel;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class DienDenHomeItemViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlDetail;
    private TextView tvTenDoiTac;
    private TextView tvTenDuong;
    private TextView tvKhoangCach;
    public SimpleDraweeView imgLogo;
    public SimpleDraweeView imgLogoDefault;
    private RatingBar ratingChatLuong;
    private TextView tvChuyenMon;

    private TextView tvNoData;
    private LinearLayout lnData, lnDisconnect;
    public Button btnTryAGainDoiTac;
    private LinearLayout progressbar;
    private TextView tvRating;
    private TextView tvNoiDungTaiTro;
    private TextView mTvGiaTrungBinh;
    public Button btnCheckIn;

    public DienDenHomeItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        rlDetail = (RelativeLayout) convertView.findViewById(R.id.rlDetail);
        tvTenDoiTac = (TextView) convertView.findViewById(R.id.tvTenDoiTac);
        tvTenDuong = (TextView) convertView.findViewById(R.id.tvTenDuong);
        tvKhoangCach = (TextView) convertView.findViewById(R.id.tvKhoangCach);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        imgLogoDefault = (SimpleDraweeView) convertView.findViewById(R.id.imgLogoDefault);
        ratingChatLuong = (RatingBar) convertView.findViewById(R.id.ratingChatLuong);
        tvChuyenMon = (TextView) convertView.findViewById(R.id.tvChuyenMon);
        lnData = (LinearLayout) convertView.findViewById(R.id.data_ln);
        lnDisconnect = (LinearLayout) convertView.findViewById(R.id.disconnect_rl);
        btnTryAGainDoiTac = (Button) convertView.findViewById(R.id.btnTryAGainDoiTac);
        tvNoData = (TextView) convertView.findViewById(R.id.no_data_tv);
        progressbar =(LinearLayout)convertView.findViewById(R.id.progressbar_ln);
        tvRating = (TextView) convertView.findViewById(R.id.tvRating);
        tvNoiDungTaiTro = (TextView) convertView.findViewById(R.id.tvNoiDungTaiTro);
        mTvGiaTrungBinh = (TextView)convertView.findViewById(R.id.gia_trung_binh_tv);
        btnCheckIn = (Button) convertView.findViewById(R.id.btnCheckIn);
    }

    public static DienDenHomeItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.card_view);
        return new DienDenHomeItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(Context context ,DiemDenModel item, final int key) {
        lnData.setVisibility(View.GONE);
        lnDisconnect.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        switch (key)
        {
            case DiemDenHomeAdapter.KEY_LOADING:
                progressbar.setVisibility(View.VISIBLE);
                break;
            case DiemDenHomeAdapter.KEY_DISCONNECT:
                lnDisconnect.setVisibility(View.VISIBLE);
                break;
            case DiemDenHomeAdapter.KEY_NODATA:
                tvNoData.setVisibility(View.VISIBLE);
                break;
            case DiemDenHomeAdapter.KEY_DATA:
                lnData.setVisibility(View.VISIBLE);

                Utils.setTextViewHtml(tvTenDoiTac,item.getTen());
                tvTenDuong.setText(item.getDiaChi());
                tvKhoangCach.setText(item.getKm() + "Km");
                ratingChatLuong.setRating((float) item.getChatLuong());
                tvRating.setText(item.getDanhGia() + "");
                tvChuyenMon.setText(item.getChuyenMon());
                Utils.setTextViewHtml(tvNoiDungTaiTro,item.getTaiTro());
                Utils.setTextViewHtml(mTvGiaTrungBinh, item.getGiaTrungBinh());
                String urlLogo = item.getLogo();

                if (!StringUtils.isEmpty(urlLogo)) {
                    imgLogo.setImageURI(urlLogo+"&width=250");
                    imgLogo.setVisibility(View.VISIBLE);
                    imgLogoDefault.setVisibility(View.GONE);
                } else {
                    imgLogo.setVisibility(View.GONE);
                    imgLogoDefault.setVisibility(View.VISIBLE);
                }
                if(item.getLoaiHopDong()==1)
                    btnCheckIn.setBackgroundResource(R.drawable.selector_white);
                else
                    btnCheckIn.setBackgroundResource(R.color.transparent_selected);
                break;
        }

    }

}
