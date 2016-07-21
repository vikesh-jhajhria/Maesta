package com.maesta.maesta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maesta.maesta.adapter.BannerAdapter;
import com.maesta.maesta.fragment.BannerFragment;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Banner;

import java.util.ArrayList;

/**
 * Created by saloni.bhansali on 7/20/2016.
 */
public class ProductDetailActivity extends BaseActivity
{
    private ArrayList<Banner> productImageList = new ArrayList<>();
    private BannerAdapter ProductImageAdapter;
    private ViewPager productViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productViewPager = (ViewPager) findViewById(R.id.pager_product);
setToolbar();
        applyFont();
productBanner();
    }

    private void productBanner() {
        Banner banner = new Banner();
        productImageList.add(banner);
        productImageList.add(banner);
        productImageList.add(banner);

        ProductImageAdapter = new BannerAdapter(getSupportFragmentManager());
        ProductImageAdapter.addFragment(BannerFragment.newInstance(productImageList.get(0)),"");
        ProductImageAdapter.addFragment(BannerFragment.newInstance(productImageList.get(1)),"");
        ProductImageAdapter.addFragment(BannerFragment.newInstance(productImageList.get(2)),"");
        productViewPager.setAdapter(ProductImageAdapter);

    }
    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.product_name_txtview), Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.product_model_txtview), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_product_price), Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_price_detail), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txtview_quantity), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.et_quantity), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_desc), Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_desc_detail), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_add_collection), Config.BOLD);



    }

}
