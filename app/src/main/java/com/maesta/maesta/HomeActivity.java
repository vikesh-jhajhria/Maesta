package com.maesta.maesta;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.maesta.maesta.adapter.BannerAdapter;
import com.maesta.maesta.adapter.CategoriesAdapter;
import com.maesta.maesta.adapter.NewArrivalAdapter;
import com.maesta.maesta.fragment.BannerFragment;
import com.maesta.maesta.vo.Banner;
import com.maesta.maesta.vo.Product;

import java.util.ArrayList;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class HomeActivity extends BaseActivity {

    private ArrayList<Banner> bannerList = new ArrayList<>();
    private ArrayList<Product> newArrivalList = new ArrayList<>();
    private ArrayList<Product> categoryList = new ArrayList<>();

    private BannerAdapter bannerAdapter;
    private NewArrivalAdapter newArrivalAdapter;
    private CategoriesAdapter categoriesAdapter;

    private ViewPager bannerViewPager;
    private RecyclerView newArrivalRV, catetoriesRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.rv_header);
        bannerViewPager = (ViewPager) findViewById(R.id.pager_banner);
        newArrivalRV = (RecyclerView) findViewById(R.id.rv_new_arrival);
        catetoriesRV = (RecyclerView) findViewById(R.id.rv_categories);



        prepareBanner();
        prepareNewArrival();
        prepareCategories();
        header.attachTo(catetoriesRV);
    }

    private void prepareBanner() {
        Banner banner = new Banner();
        bannerList.add(banner);
        bannerList.add(banner);
        bannerList.add(banner);

        bannerAdapter = new BannerAdapter(getSupportFragmentManager());
        bannerAdapter.addFragment(BannerFragment.newInstance(bannerList.get(0)),"");
        bannerAdapter.addFragment(BannerFragment.newInstance(bannerList.get(1)),"");
        bannerAdapter.addFragment(BannerFragment.newInstance(bannerList.get(2)),"");
        bannerViewPager.setAdapter(bannerAdapter);

    }

    private void prepareNewArrival() {
        Product product = new Product();
        product.title = "T-shirt";
        product.price = "2000";
        newArrivalList.add(product);
        newArrivalList.add(product);
        newArrivalList.add(product);
        newArrivalList.add(product);
        newArrivalList.add(product);
        newArrivalList.add(product);
        newArrivalList.add(product);
        newArrivalList.add(product);

        newArrivalAdapter = new NewArrivalAdapter(this,newArrivalList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        newArrivalRV.setLayoutManager(linearLayoutManager);
        newArrivalRV.setAdapter(newArrivalAdapter);

    }

    private void prepareCategories() {
        Product product = new Product();
        product.title = "T-shirt";
        product.price = "2000";
        categoryList.add(product);
        categoryList.add(product);
        categoryList.add(product);
        categoryList.add(product);
        categoryList.add(product);
        categoryList.add(product);
        categoryList.add(product);
        categoryList.add(product);

        categoriesAdapter = new CategoriesAdapter(this,categoryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        catetoriesRV.setLayoutManager(linearLayoutManager);
        catetoriesRV.setAdapter(categoriesAdapter);

    }
}
