package com.maesta.maesta;

import android.annotation.TargetApi;
import android.content.Context;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.maesta.maesta.adapter.BannerAdapter;
import com.maesta.maesta.adapter.CategoriesAdapter;
import com.maesta.maesta.adapter.CustomExpandableListAdapter;
import com.maesta.maesta.adapter.NewArrivalAdapter;
import com.maesta.maesta.datasource.ExpandableListDataSource;
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
    AppPreferences mPrefs;
    private ViewPager bannerViewPager;
    private RecyclerView newArrivalRV, catetoriesRV;
    private Handler handler;
    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private Map<String, List<String>> mExpandableListData;
    private List<RadioButton> pagerIndicatorList;
TextView user_name;
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
        if (Utils.isNetworkConnected(this, true))
            new HomeTask().execute();
        findViewById(R.id.btn_toggle).setOnClickListener(this);

        ((EditText) findViewById(R.id.txt_search)).setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }

        });
        prepareNewArrival();
        prepareCategories();
        header.attachTo(catetoriesRV);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.layout_nav_header, null, false);
        mExpandableListView.addHeaderView(listHeaderView);
        findViewById(R.id.img_user).setOnClickListener(this);
        user_name=(TextView)findViewById(R.id.txt_username) ;
        View listFooterView = inflater.inflate(R.layout.layout_nav_footer, null, false);
        mExpandableListView.addFooterView(listFooterView);
        findViewById(R.id.txt_my_profile).setOnClickListener(this);
        findViewById(R.id.txt_terms).setOnClickListener(this);
        findViewById(R.id.txt_my_order).setOnClickListener(this);
        findViewById(R.id.txt_about_us).setOnClickListener(this);
        findViewById(R.id.txt_contact_us).setOnClickListener(this);
        findViewById(R.id.my_collection).setOnClickListener(this);

        findViewById(R.id.txt_logout).setOnClickListener(this);
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
        pagerIndicatorList = new ArrayList<>();
        bannerAdapter = new BannerAdapter(getSupportFragmentManager());
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
            case R.id.my_collection:
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
                        for (int i = 0; i < bannerArray.length(); i++) {
                            Banner banner = new Banner();
                            banner.url = ((JSONObject) bannerArray.get(i)).getString("image");
                            bannerList.add(banner);
                        }
                    }
                    JSONObject productData = object.getJSONObject("product");
                    if (productData.getBoolean("status")) {
                        JSONArray productArray = productData.getJSONArray("data");
                        for (int i = 0; i < productArray.length(); i++) {
                            Product product = new Product();
                            product.id = ((JSONObject) productArray.get(i)).getInt("id");
                            product.price = ((JSONObject) productArray.get(i)).getString("price");
                            product.thumbURL = ((JSONObject) productArray.get(i)).getString("image");
                            product.title = ((JSONObject) productArray.get(i)).getString("name");
                            newArrivalList.add(product);


                        }
                    }
                    JSONObject categoryData = object.getJSONObject("category");
                    if (categoryData.getBoolean("status")) {
                        JSONArray categoryArray = categoryData.getJSONArray("data");
                        for (int i = 0; i < categoryArray.length(); i++) {
                            Product product = new Product();
                            product.id = ((JSONObject) categoryArray.get(i)).getInt("id");
                            product.thumbURL = ((JSONObject) categoryArray.get(i)).getString("image");
                            product.title = ((JSONObject) categoryArray.get(i)).getString("name");
                            categoryList.add(product);


                        }
                    }
                    newArrivalAdapter.notifyDataSetChanged();
                    categoriesAdapter.notifyDataSetChanged();
                    prepareBanner();
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

    }

}
