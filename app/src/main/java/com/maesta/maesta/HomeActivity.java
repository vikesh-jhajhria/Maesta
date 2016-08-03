package com.maesta.maesta;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.maesta.maesta.adapter.BannerAdapter;
import com.maesta.maesta.adapter.CategoriesAdapter;
import com.maesta.maesta.adapter.HomeExpandableListAdapter;
import com.maesta.maesta.adapter.NewArrivalAdapter;
import com.maesta.maesta.fragment.BannerFragment;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Banner;
import com.maesta.maesta.vo.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    AppPreferences mPrefs;
    private ViewPager bannerViewPager;
    private RecyclerView newArrivalRV, catetoriesRV;
    private Handler handler;
    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<RadioButton> pagerIndicatorList;
    TextView user_name;
    int categordId;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPrefs = AppPreferences.getAppPreferences(HomeActivity.this);


        RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.rv_header);
        bannerViewPager = (ViewPager) findViewById(R.id.pager_banner);
        newArrivalRV = (RecyclerView) findViewById(R.id.rv_new_arrival);
        catetoriesRV = (RecyclerView) findViewById(R.id.rv_categories);
        handler = new Handler();

        findViewById(R.id.btn_toggle).setOnClickListener(this);

        prepareNewArrival();
        prepareCategories();
        header.attachTo(catetoriesRV);

        RelativeLayout rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        Log.v("width>>>", ((int) Utils.getDeviceSize(this).get("Width")) + "");
        ViewGroup.LayoutParams params = rl_banner.getLayoutParams();
        params.height = (1000 * ((int) Utils.getDeviceSize(this).get("Width"))) / 2057;
        rl_banner.setLayoutParams(params);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.layout_nav_header, null, false);
        mExpandableListView.addHeaderView(listHeaderView);
        user_name = (TextView) findViewById(R.id.txt_username);
        View listFooterView = inflater.inflate(R.layout.layout_nav_footer, null, false);
        mExpandableListView.addFooterView(listFooterView);
        findViewById(R.id.txt_my_profile).setOnClickListener(this);
        findViewById(R.id.txt_terms).setOnClickListener(this);
        findViewById(R.id.txt_my_order).setOnClickListener(this);
        findViewById(R.id.txt_about_us).setOnClickListener(this);
        findViewById(R.id.txt_contact_us).setOnClickListener(this);
        findViewById(R.id.my_collection).setOnClickListener(this);
        //findViewById(R.id.img_search).setOnClickListener(this);
        findViewById(R.id.txt_logout).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);

        if (categoryList.size() == 0) {
            if (Utils.isNetworkConnected(this, false))
                new HomeTask().execute();
            else
                startActivityForResult(new Intent(this, NetworkActivity.class), Config.NETWORK_ACTIVITY);
        }
        addDrawerItems();
        applyFont();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.NETWORK_ACTIVITY) {
            if (Utils.isNetworkConnected(this, false))
                new HomeTask().execute();
            else
                onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to close Maesta", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new HomeExpandableListAdapter(this, categoryList);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Product selectedItem = categoryList.get(i);

                if (selectedItem.haveSubCategories) {
                    Intent intent = new Intent(getApplicationContext(), SubcatgoryActivity.class);
                    intent.putExtra("ID", selectedItem.id);
                    intent.putExtra("HEADER_IMAGE", selectedItem.thumbURL);
                    intent.putExtra("TITLE", selectedItem.title);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ListingActivity.class);
                    intent.putExtra("ID", selectedItem.id);
                    intent.putExtra("TITLE", selectedItem.title);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                }
                return false;
            }
        });
    }


    private void prepareBanner() {
        pagerIndicatorList = new ArrayList<>();
        bannerAdapter = new BannerAdapter(getSupportFragmentManager());
        ((RadioGroup) findViewById(R.id.pager_indicator_group)).removeAllViews();
        for (int i = 0; i < bannerList.size(); i++) {
            RadioButton btn = createDot();
            pagerIndicatorList.add(btn);
            ((RadioGroup) findViewById(R.id.pager_indicator_group)).addView(btn);
            bannerAdapter.addFragment(BannerFragment.newInstance(bannerList.get(i)), "");
        }
        pagerIndicatorList.get(0).setChecked(true);
        bannerViewPager.setAdapter(bannerAdapter);

        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerIndicatorList.get(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private RadioButton createDot() {
        RadioButton btn = new RadioButton(this);
        btn.setLayoutParams(new ViewGroup.MarginLayoutParams(20, 20));
        btn.setBackground(getResources().getDrawable(R.drawable.radio_selector));
        btn.setButtonDrawable(new StateListDrawable());
        btn.setClickable(false);

        return btn;
    }

    private void prepareNewArrival() {
        newArrivalAdapter = new NewArrivalAdapter(this, newArrivalList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        newArrivalRV.setLayoutManager(linearLayoutManager);
        newArrivalRV.setAdapter(newArrivalAdapter);

    }

    private void prepareCategories() {
        categoriesAdapter = new CategoriesAdapter(this, categoryList);
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
                String UserName = mPrefs.getStringValue(AppPreferences.USER_NAME);
                user_name.setText(UserName);

                break;
            case R.id.img_user:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;

            case R.id.txt_my_profile:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.txt_terms:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), TermConditionActivity.class));
                break;
            case R.id.txt_contact_us:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
                break;
            case R.id.txt_about_us:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), AboutusActivity.class));
                break;
            case R.id.txt_my_order:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class));
                break;
            case R.id.my_collection:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), MyCollectionActivity.class));
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
            case R.id.search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class)
                        .putExtra("ID", ""));
                break;

        }
    }

    class HomeTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            String apikey = mPrefs.getStringValue(AppPreferences.API_KEY);
            postDataParams.put("api_key", apikey);
            return HTTPUrlConnection.getInstance().load(Config.HOME, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject bannerData = object.getJSONObject("banner");
                    if (bannerData.getBoolean("status")) {
                        JSONArray bannerArray = bannerData.getJSONArray("data");
                        bannerList.clear();
                        for (int i = 0; i < bannerArray.length(); i++) {
                            Banner banner = new Banner();
                            banner.url = ((JSONObject) bannerArray.get(i)).getString("image");
                            bannerList.add(banner);
                        }
                    }
                    JSONObject productData = object.getJSONObject("product");
                    if (productData.getBoolean("status")) {
                        JSONArray productArray = productData.getJSONArray("data");
                        newArrivalList.clear();
                        for (int i = 0; i < productArray.length(); i++) {
                            Product product = new Product();
                            product.id = ((JSONObject) productArray.get(i)).getInt("id");
                            product.price = ((JSONObject) productArray.get(i)).getString("price");
                            product.thumbURL = ((JSONObject) productArray.get(i)).getString("image");
                            product.title = ((JSONObject) productArray.get(i)).getString("name");
                            newArrivalList.add(product);
                        }
                        findViewById(R.id.txt_new_arrival).setVisibility(View.VISIBLE);
                    }
                    JSONObject categoryData = object.getJSONObject("category");
                    if (categoryData.getBoolean("status")) {
                        JSONArray categoryArray = categoryData.getJSONArray("data");
                        categoryList.clear();
                        for (int i = 0; i < categoryArray.length(); i++) {
                            Product product = new Product();
                            product.id = ((JSONObject) categoryArray.get(i)).getInt("id");
                            categordId = ((JSONObject) categoryArray.get(i)).getInt("id");
                            product.thumbURL = ((JSONObject) categoryArray.get(i)).getString("image");
                            product.iconURL = ((JSONObject) categoryArray.get(i)).getString("icon");
                            product.title = ((JSONObject) categoryArray.get(i)).getString("name");
                            product.haveSubCategories = (((JSONObject) categoryArray.get(i))
                                    .getJSONArray("sub_category")).length() > 0;
                            categoryList.add(product);
                        }
                    }
                    newArrivalAdapter.notifyDataSetChanged();
                    categoriesAdapter.notifyDataSetChanged();
                    prepareBanner();
                    //swipeView.setRefreshing(false);
                } else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(HomeActivity.this);
                } else {
                    Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_new_arrival), Config.REGULAR);

    }


}


