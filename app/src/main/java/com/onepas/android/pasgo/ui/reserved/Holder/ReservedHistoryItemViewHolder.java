package com.onepas.android.pasgo.ui.reserved.Holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.CheckedIn;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class ReservedHistoryItemViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout lnDetail;
    public Button btnShare;
    public Button btnCallSuport;
    public Button btnDirection;
    public Button btnDeals;
    private TextView tvTenDoiTac;
    private TextView tvTenDuong;
    public SimpleDraweeView imgLogo;
    public SimpleDraweeView imgLogoDefault;
    private TextView tvSoNguoiDen;
    private TextView tvThoiGianDen;
    private ImageView imgChuaDen;
    private ImageView imgDaDen;
    private ImageView imgChietKhau;
    private TextView tvChietKhau;
    private TextView tvRating;
    private RatingBar ratingChatLuong;
    private LinearLayout lnChuaDen;
    public Button btnReserve;
    private TextView tvNoiDungTaiTro;
    private TextView tvChuyenMon;
    private LinearLayout lnPeople;
    private TextView tvTreEm;
    private TextView tvGiaTrungBinh;


    public ReservedHistoryItemViewHolder(final View parent, LinearLayout convertView) {
        super(parent);
        tvTenDoiTac = (TextView)convertView.findViewById(R.id.tvTenDoiTac);
        lnDetail = (LinearLayout) convertView.findViewById(R.id.lnDetail);
        tvTenDoiTac = (TextView) convertView.findViewById(R.id.tvTenDoiTac);
        tvTenDuong = (TextView) convertView.findViewById(R.id.tvTenDuong);
        btnShare = (Button) convertView.findViewById(R.id.share_btn);
        btnCallSuport = (Button) convertView.findViewById(R.id.btnCallSuport);
        btnDirection = (Button) convertView.findViewById(R.id.btnDirection);
        btnDeals = (Button) convertView.findViewById(R.id.btnDeals);
        imgLogo = (SimpleDraweeView) convertView.findViewById(R.id.imgLogo);
        imgLogoDefault = (SimpleDraweeView) convertView.findViewById(R.id.imgLogoDefault);
        tvSoNguoiDen = (TextView) convertView.findViewById(R.id.tvSoNguoiDen);
        tvThoiGianDen = (TextView) convertView.findViewById(R.id.tvThoiGianDen);
        imgChuaDen = (ImageView) convertView.findViewById(R.id.chua_den_img);
        imgDaDen = (ImageView) convertView.findViewById(R.id.da_den_img);
        imgChietKhau = (ImageView) convertView.findViewById(R.id.imgChietKhau);
        tvChietKhau = (TextView) convertView.findViewById(R.id.tvChietKhau);
        tvRating = (TextView) convertView.findViewById(R.id.tvRating);
        ratingChatLuong = (RatingBar) convertView.findViewById(R.id.ratingChatLuong);
        lnChuaDen =(LinearLayout)convertView.findViewById(R.id.chua_den_ln);
        btnReserve = (Button)convertView.findViewById(R.id.reserve_btn);
        tvNoiDungTaiTro = (TextView) convertView.findViewById(R.id.tvNoiDungTaiTro);
        tvChuyenMon = (TextView) convertView.findViewById(R.id.tvChuyenMon);
        lnPeople = (LinearLayout) convertView.findViewById(R.id.lnPeople);
        tvTreEm = (TextView) convertView.findViewById(R.id.tvTreEm);
        tvGiaTrungBinh =(TextView)convertView.findViewById(R.id.gia_trung_binh_tv);
    }

    public static ReservedHistoryItemViewHolder newInstance(View parent) {
        LinearLayout reserved_parent_ln = (LinearLayout) parent.findViewById(R.id.reserved_parent_ln);

        return new ReservedHistoryItemViewHolder(parent, reserved_parent_ln);
    }

    public void setItemValue(CheckedIn item, Context context) {
        Utils.setTextViewHtml(tvTenDoiTac,item.getTenChiNhanh());
        tvTenDuong.setText(item.getDiaChi());
        if(item.getTrangThaiDatCho()==1) {
            imgChuaDen.setVisibility(View.VISIBLE);
            imgDaDen.setVisibility(View.GONE);
            btnReserve.setVisibility(View.GONE);
            lnChuaDen.setVisibility(View.VISIBLE);
            tvNoiDungTaiTro.setVisibility(View.GONE);
            tvChuyenMon.setVisibility(View.GONE);
            tvThoiGianDen.setVisibility(View.VISIBLE);
            lnPeople.setVisibility(View.VISIBLE);
        }
        else {
            imgChuaDen.setVisibility(View.GONE);
            imgDaDen.setVisibility(View.VISIBLE);
            btnReserve.setVisibility(View.GONE);
            btnReserve.setVisibility(View.VISIBLE);
            lnChuaDen.setVisibility(View.GONE);
            tvNoiDungTaiTro.setVisibility(View.VISIBLE);
            tvChuyenMon.setVisibility(View.VISIBLE);
            tvThoiGianDen.setVisibility(View.GONE);
            lnPeople.setVisibility(View.GONE);
        }
        String urlLogo = item.getLogo();
        tvRating.setText(item.getDanhGia() + "");
        ratingChatLuong.setRating((float) item.getChatLuong());
        if (!StringUtils.isEmpty(urlLogo)) {
            imgLogo.setImageURI(urlLogo);
            imgLogo.setVisibility(View.VISIBLE);
            imgLogoDefault.setVisibility(View.GONE);
        } else {
            imgLogo.setVisibility(View.GONE);
            imgLogoDefault.setVisibility(View.VISIBLE);
        }
        if(item.getChietKhau()<=0)
            tvChietKhau.setVisibility(View.GONE);
        else
        {
            tvChietKhau.setVisibility(View.VISIBLE);
            tvChietKhau.setText(item.getChietKhauHienThi());
        }
        Utils.setTextViewHtml(tvGiaTrungBinh,item.getGiaTrungBinh());
        String soNguoiDen=String.format("%s",item.getSoNguoiDen());
        tvSoNguoiDen.setText(soNguoiDen);
        String thoiGianDen=String.format("%s ",item.getThoiGianDen());
        tvThoiGianDen.setText(thoiGianDen);
        if(item.getTienKhuyenMai()>0)
        {
            imgChietKhau.setVisibility(View.VISIBLE);
            tvChietKhau.setVisibility(View.VISIBLE);
            tvChietKhau.setText(""+ Utils.formatMoney(context, item.getTienKhuyenMai() + "")+""+ context.getString(R.string.vnd_up));
        }else{
            imgChietKhau.setVisibility(View.GONE);
            tvChietKhau.setVisibility(View.GONE);
        }
        Utils.setTextViewHtml(tvNoiDungTaiTro,item.getTaiTro());
        Utils.setTextViewHtml(tvChuyenMon,item.getChuyenMon());
        tvTreEm.setText(String.format("%s",item.getSoTreEm()));
        if(item.getLoaiHopDong()==1)
            btnReserve.setBackgroundResource(R.drawable.selector_white);
        else
            btnReserve.setBackgroundResource(R.color.transparent_selected);
    }

}
