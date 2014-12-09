package com.springlabs.msulife;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.example.alexey.msulife.R;


/**
 * Created by Alexey on 29.10.2014.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> articleItems;
    private LayoutInflater inflater;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> articleItems) {
        this._activity = activity;
        this.articleItems = articleItems;
    }

    @Override
    public int getCount() {
        return this.articleItems.size();
    }

    public Object getItem(int location) {
        return articleItems.get(location);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FeedImageView imgDisplay;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (FeedImageView) viewLayout.findViewById(R.id.imgDisplay);

        imgDisplay.setImageUrl(articleItems.get(position), imageLoader);
        imgDisplay.setVisibility(View.VISIBLE);
        imgDisplay.setResponseObserver(new FeedImageView.ResponseObserver() {
            @Override
            public void onError() {
            }
            @Override
            public void onSuccess() {
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }

}