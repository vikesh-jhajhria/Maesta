package com.maesta.maesta;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class HomeActivity extends BaseActivity {

    private ViewPager bannerViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bannerViewPager = (ViewPager) findViewById(R.id.pager_banner);
    }
}
