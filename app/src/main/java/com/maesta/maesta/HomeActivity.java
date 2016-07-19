package com.maesta.maesta;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.maesta.maesta.adapter.BannerAdapter;
import com.maesta.maesta.vo.Banner;

import java.util.ArrayList;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class HomeActivity extends BaseActivity {

    private ArrayList<Banner> bannerList = new ArrayList<>();
    private BannerAdapter bannerAdapter;

    private ViewPager bannerViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bannerViewPager = (ViewPager) findViewById(R.id.pager_banner);

        prepareBanner();
    }

    private void prepareBanner() {
        Banner banner = new Banner();
        bannerList.add(banner);
        bannerList.add(banner);
        bannerList.add(banner);

        bannerAdapter = new BannerAdapter(this,bannerList);
        bannerViewPager.setAdapter(bannerAdapter);
    }
}
