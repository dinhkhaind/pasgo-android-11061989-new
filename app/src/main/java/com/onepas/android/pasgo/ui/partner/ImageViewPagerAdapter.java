package com.onepas.android.pasgo.ui.partner;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhSlide;

import java.util.List;

public class ImageViewPagerAdapter extends PagerAdapter {

    private List<AnhSlide> images;
    private View layout;
    private SimpleDraweeView imIntro;
    private ImageView circle1;
    private ImageView circle2;
    private ImageView circle3;
    private ImageView circle4;
    private ImageView circle5;
    private ImageView circle6;
    private ImageView circle7;
    private ImageView circle8;
    private ImageView circle9;
    private ImageView circle10;
    private Activity activity;
    private ImageViewPageListener pageListener;
    public interface ImageViewPageListener{
        void imageClick(int position);
    }
    public ImageViewPagerAdapter(List<AnhSlide> images, Activity activity, ImageViewPageListener pageListener) {
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
        return layoutInflater.inflate(R.layout.item_imageview,
                viewGroup, false);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layout = createViewForPosition(container);
        imIntro = (SimpleDraweeView) layout.findViewById(R.id.im_intro);
        circle1 = (ImageView) layout.findViewById(R.id.circle1);
        circle2 = (ImageView) layout.findViewById(R.id.circle2);
        circle3 = (ImageView) layout.findViewById(R.id.circle3);
        circle4 = (ImageView) layout.findViewById(R.id.circle4);
        circle5 = (ImageView) layout.findViewById(R.id.circle5);
        circle6 = (ImageView) layout.findViewById(R.id.circle6);
        circle7 = (ImageView) layout.findViewById(R.id.circle7);
        circle8 = (ImageView) layout.findViewById(R.id.circle8);
        circle9 = (ImageView) layout.findViewById(R.id.circle9);
        circle10 = (ImageView) layout.findViewById(R.id.circle10);
        circle1.setVisibility(View.GONE);
        circle2.setVisibility(View.GONE);
        circle3.setVisibility(View.GONE);
        circle4.setVisibility(View.GONE);
        circle5.setVisibility(View.GONE);
        circle6.setVisibility(View.GONE);
        circle7.setVisibility(View.GONE);
        circle8.setVisibility(View.GONE);
        circle9.setVisibility(View.GONE);
        circle10.setVisibility(View.GONE);
        if(1<=images.size())
            circle1.setVisibility(View.VISIBLE);
        if(2<=images.size())
            circle2.setVisibility(View.VISIBLE);
        if(3<=images.size())
            circle3.setVisibility(View.VISIBLE);
        if(4<=images.size())
            circle4.setVisibility(View.VISIBLE);
        if(5<=images.size())
            circle5.setVisibility(View.VISIBLE);
        if(6<=images.size())
            circle6.setVisibility(View.VISIBLE);
        if(7<=images.size())
            circle7.setVisibility(View.VISIBLE);
        if(8<=images.size())
            circle8.setVisibility(View.VISIBLE);
        if(9<=images.size())
            circle9.setVisibility(View.VISIBLE);
        if(10<=images.size())
            circle10.setVisibility(View.VISIBLE);
        try {
            setIcon(imIntro, images.get(position).getAnh());
            setNumberPager(position);
            imIntro.setOnClickListener(v -> pageListener.imageClick(position));
        } catch (OutOfMemoryError e) {

        }
        ((ViewPager) container).addView(layout, 0);
        return layout;
    }

    private void setIcon(SimpleDraweeView imageView, String urlImage) {
        imageView.setImageURI(urlImage);

    }


    private int imageNumber[] = {R.drawable.oval_dot_1, R.drawable.oval_dot_2};

    private void setNumberPager(int position) {
        circle1.setBackgroundResource(imageNumber[0]);
        circle2.setBackgroundResource(imageNumber[0]);
        circle3.setBackgroundResource(imageNumber[0]);
        circle4.setBackgroundResource(imageNumber[0]);
        circle5.setBackgroundResource(imageNumber[0]);
        circle6.setBackgroundResource(imageNumber[0]);
        circle7.setBackgroundResource(imageNumber[0]);
        circle8.setBackgroundResource(imageNumber[0]);
        circle9.setBackgroundResource(imageNumber[0]);
        circle10.setBackgroundResource(imageNumber[0]);
        switch (position) {
            case 0:
                circle1.setBackgroundResource(imageNumber[1]);
                break;
            case 1:
                circle2.setBackgroundResource(imageNumber[1]);
                break;
            case 2:
                circle3.setBackgroundResource(imageNumber[1]);
                break;
            case 3:
                circle4.setBackgroundResource(imageNumber[1]);
                break;
            case 4:
                circle5.setBackgroundResource(imageNumber[1]);
                break;
            case 5:
                circle6.setBackgroundResource(imageNumber[1]);
                break;
            case 6:
                circle7.setBackgroundResource(imageNumber[1]);
                break;
            case 7:
                circle8.setBackgroundResource(imageNumber[1]);
                break;
            case 8:
                circle9.setBackgroundResource(imageNumber[1]);
                break;
            case 9:
                circle10.setBackgroundResource(imageNumber[1]);
                break;
            default:
                break;
        }
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
