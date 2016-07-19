package com.maesta.maesta.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maesta.maesta.R;
import com.maesta.maesta.vo.Banner;

import java.util.ArrayList;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class BannerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Banner> banners;

    public BannerAdapter(Context context, ArrayList<Banner> bannerList) {
        this.context = context;
        this.banners = bannerList;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.layout_banner_fragment, collection, false);
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
