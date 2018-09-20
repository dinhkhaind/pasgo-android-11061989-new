package com.onepas.android.pasgo.ui.guid;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onepas.android.pasgo.R;

public class ViewPagerAdapter extends PagerAdapter {

	int imageArray[];
	View layout;
	TextView titleIntro;
	ImageView imIntro;

	RelativeLayout circle1;
	RelativeLayout circle2;
	RelativeLayout circle3;
	RelativeLayout circle4;
	RelativeLayout circle5;
	RelativeLayout circle6;
	private String[] mtitleIntro;
	private Activity activity;
	private Button btnSkip;
	public ViewPagerAdapter(int[] imgArra, String[] title, Activity activity) {
		imageArray = imgArra;
		this.mtitleIntro = title;
		this.activity = activity;
	}

	public int getCount() {
		return imageArray.length;
	}

	private View createViewForPosition(ViewGroup viewGroup) {
		LayoutInflater layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.introduction,
				viewGroup, false);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		layout = createViewForPosition(container);
		titleIntro = (TextView) layout.findViewById(R.id.title_intro);
		imIntro = (ImageView) layout.findViewById(R.id.im_intro);
		circle1 = (RelativeLayout) layout.findViewById(R.id.circle1);
		circle2 = (RelativeLayout) layout.findViewById(R.id.circle2);
		circle3 = (RelativeLayout) layout.findViewById(R.id.circle3);
		circle4 = (RelativeLayout) layout.findViewById(R.id.circle4);
		circle5 = (RelativeLayout) layout.findViewById(R.id.circle5);
		circle6 = (RelativeLayout) layout.findViewById(R.id.circle6);
		btnSkip = (Button) layout.findViewById(R.id.skip_btn);
		btnSkip.setOnClickListener(v -> finishOurLeftInLeft(activity));
        try {
            titleIntro.setText(mtitleIntro[position]);
			setIcon(imIntro,imageArray[position]);
            setNumberPager(position);
        }catch (OutOfMemoryError e){

        }
		((ViewPager) container).addView(layout, 0);
		return layout;
	}
	public void finishOurLeftInLeft(Activity activity)
	{
		activity.finish();
		activity.overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
	}
    private void setIcon(ImageView imageView, int imageSource) {
		imageView.setBackgroundResource(imageSource);
    }


    private int imageNumber[] = { R.drawable.oval_white, R.drawable.oval_red };

	private void setNumberPager(int position) {
		circle1.setBackgroundResource(imageNumber[0]);
		circle2.setBackgroundResource(imageNumber[0]);
		circle3.setBackgroundResource(imageNumber[0]);
		circle4.setBackgroundResource(imageNumber[0]);
		circle5.setBackgroundResource(imageNumber[0]);
		circle6.setBackgroundResource(imageNumber[0]);
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
		default:
			break;
		}
	}

	@Override
	public void destroyItem(ViewGroup  arg0, int arg1, Object arg2) {
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
