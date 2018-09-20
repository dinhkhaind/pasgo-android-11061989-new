package com.onepas.android.pasgo.ui.partner;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhList;

import java.util.List;

public class ImageListPagerAdapter extends PagerAdapter {

    private List<AnhList> images;
    private View layout;
    private SimpleDraweeView imIntro;
    private Activity activity;
    private ImageViewPageListener pageListener;
    public interface ImageViewPageListener{
        void imageClick(int position);
    }
    public ImageListPagerAdapter(List<AnhList> images, Activity activity, ImageViewPageListener pageListener) {
        this.images = images;
        this.activity = activity;
        this.pageListener = pageListener;
    }

    public int getCount() {
        return images.size();
    }

    private View createViewForPosition(ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.item_image_list,
                viewGroup, false);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layout = createViewForPosition(container);
        imIntro = (SimpleDraweeView) layout.findViewById(R.id.im_intro);
        try {
            setIcon(imIntro, images.get(position).getAnh());
            imIntro.setOnClickListener(v -> pageListener.imageClick(position));
        } catch (OutOfMemoryError e) {

        }
        ((ViewPager) container).addView(layout, 0);
        return layout;
    }

    private void setIcon(SimpleDraweeView imageView, String urlImage) {
        imageView.setImageURI(urlImage);

    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
