package com.onepas.android.pasgo.ui.guid;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.utils.Utils;

public class GuidActivity extends BaseAppCompatActivity {
	private static final String TAG="GuidActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.page_guid);
		String[] titleIntro = getResources().getStringArray(
				R.array.title_introduction);
		int imageArra[] = { R.drawable.huongdan1, R.drawable.huongdan2,
				R.drawable.huongdan3, R.drawable.huongdan4 , R.drawable.huongdan5, R.drawable.huongdan6 };

		ViewPagerAdapter adapter = new ViewPagerAdapter(imageArra, titleIntro, this);
		ViewPager myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(0);
		myPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				Utils.Log(TAG, "position : -->" + position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finishOurLeftInLeft();
			return true;
		}
		return true;
	}


	@Override
	public void onStartMoveScreen() {

	}

	@Override
	public void onUpdateMapAfterUserInterection() {

	}
}