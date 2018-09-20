package com.onepas.android.pasgo.ui.partner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.AnhBangGia;
import com.theappguruz.imagezoom.ImageViewTouch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MenuPagerAdapter extends PagerAdapter {

    private List<AnhBangGia> images;
    private View layout;
    private Activity activity;
    private ImageViewTouch ivLargeImage;
    public MenuPagerAdapter(List<AnhBangGia> images, Activity activity) {
        this.images = images;
        this.activity = activity;
    }

    public int getCount() {
        return images.size();
    }

    private View createViewForPosition(ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.item_image_menu,
                viewGroup, false);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layout = createViewForPosition(container);
        ivLargeImage = (ImageViewTouch) layout.findViewById(R.id.ivLargeImageView);
        new ImageLoadTask(images.get(position).getAnh(), ivLargeImage).execute();
        container.addView(layout, 0);
        return layout;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageViewTouch imageView;
        public ImageLoadTask(String url, ImageViewTouch imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Uri uri  = Uri.parse(url);
            InputStream inputStream = null;
            Bitmap bitmap = null;
            try {
                bitmap = fetchBitmap(uri);
            } catch (Exception e) {
                // Head in the sand!
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // Head in the sand!
                    }
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            this.imageView.setImageBitmapReset(result,true);
            this.imageView.setVisibility(View.VISIBLE);

            if (result != null) {
                imageView.setImageBitmap(result);
                this.imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.transparent));
            }
        }

    }
    private Bitmap fetchBitmap(Uri uri) throws IOException {
        Bitmap bitmap = null;
        try {
            final URL url = new URL(uri.toString());
            InputStream urlConnection = url.openStream();
            bitmap = BitmapFactory.decodeStream(urlConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        arg0.removeView((View) arg2);
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
