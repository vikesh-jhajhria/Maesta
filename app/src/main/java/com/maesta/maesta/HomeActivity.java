package com.maesta.maesta;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.maesta.maesta.adapter.BannerAdapter;
import com.maesta.maesta.adapter.CategoriesAdapter;
import com.maesta.maesta.adapter.CustomExpandableListAdapter;
import com.maesta.maesta.adapter.NewArrivalAdapter;
import com.maesta.maesta.datasource.ExpandableListDataSource;
import com.maesta.maesta.fragment.BannerFragment;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Banner;
import com.maesta.maesta.vo.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class HomeActivity extends BaseActivity {

    private ArrayList<Banner> bannerList = new ArrayList<>();
    private ArrayList<Product> newArrivalList = new ArrayList<>();
    private ArrayList<Product> categoryList = new ArrayList<>();
    public DrawerLayout mDrawerLayout;
    private BannerAdapter bannerAdapter;
    private NewArrivalAdapter newArrivalAdapter;
    private CategoriesAdapter categoriesAdapter;

    private ViewPager bannerViewPager;
    private RecyclerView newArrivalRV, catetoriesRV;
private Handler handler;
    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private Map<String, List<String>> mExpandableListData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.rv_header);
        bannerViewPager = (ViewPager) findViewById(R.id.pager_banner);
        newArrivalRV = (RecyclerView) findViewById(R.id.rv_new_arrival);
        catetoriesRV = (RecyclerView) findViewById(R.id.rv_categories);
        handler=new Handler();
        findViewById(R.id.btn_toggle).setOnClickListener(this);


        prepareBanner();
        prepareNewArrival();
        prepareCategories();
        header.attachTo(catetoriesRV);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.layout_nav_header, null, false);
        mExpandableListView.addHeaderView(listHeaderView);
        findViewById(R.id.img_user).setOnClickListener(this);
        View listFooterView = inflater.inflate(R.layout.layout_nav_footer, null, false);
        mExpandableListView.addFooterView(listFooterView);
        findViewById(R.id.txt_my_profile).setOnClickListener(this);
        findViewById(R.id.txt_terms).setOnClickListener(this);
        findViewById(R.id.txt_my_order).setOnClickListener(this);
        findViewById(R.id.txt_about_us).setOnClickListener(this);
        findViewById(R.id.txt_contact_us).setOnClickListener(this);
        findViewById(R.id. txt_logout).setOnClickListener(this);
        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        addDrawerItems();
        applyFont();
    }
    private void addDrawerItems() {
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());
                //mSelectedItemView.setText(mExpandableListTitle.get(groupPosition).toString());
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //getSupportActionBar().setTitle("Gener");
                //mSelectedItemView.setText("Selected item");
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                //getSupportActionBar().setTitle(selectedItem);
                //mSelectedItemView.setText(mExpandableListTitle.get(groupPosition).toString() + " -> " + selectedItem);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_toggle:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.img_user:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;

            case R.id.txt_my_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.txt_terms:
                startActivity(new Intent(getApplicationContext(), TermConditionActivity.class));
                break;
            case R.id.txt_contact_us:
                startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
                break;
            case R.id.txt_about_us:
                startActivity(new Intent(getApplicationContext(), AboutusActivity.class));
                break;
            case R.id.txt_my_order:
                startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class));
                break;
            case R.id.txt_logout:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                handler.postDelayed(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        Utils.showDecisionDialog(HomeActivity.this, "Logout", getString(R.string.logout_message), new Utils.AlertCallback() {
                            @Override
                            public void callback() {
                                AppPreferences pref = AppPreferences.getAppPreferences(getApplicationContext());
                                pref.putStringValue(AppPreferences.USER_ID, "");
                                pref.putStringValue(AppPreferences.USER_NAME, "");
                                pref.putStringValue(AppPreferences.USER_PHONE, "");
                                pref.putStringValue(AppPreferences.API_KEY, "");
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finishAffinity();
                            }
                        });
                    }
                }, 50);
                break;

        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_welcome), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_username), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_my_profile), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_my_order), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_about_us), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_contact_us), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_terms), Config.REGULAR);

    }
}
