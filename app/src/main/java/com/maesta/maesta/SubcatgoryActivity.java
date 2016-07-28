package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maesta.maesta.adapter.CustomExpandableListAdapter;
import com.maesta.maesta.adapter.SubCategoryAdapter;
import com.maesta.maesta.adapter.SubCategoryExpandableListAdapter;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class SubcatgoryActivity extends BaseActivity {
    private ArrayList<Object> subCategoryList;
    AppPreferences mPrefs;
    private int categoryId;
    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private ArrayList<Product> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_subcatogary);
        {
            mPrefs = AppPreferences.getAppPreferences(SubcatgoryActivity.this);
            mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
            categoryList = new ArrayList<>();
            subCategoryList = new ArrayList<>();
            Glide.with(this).load(getIntent().getStringExtra("HEADER_IMAGE")).asBitmap()
                    .placeholder(R.drawable.banner_1).centerCrop().into((ImageView) findViewById(R.id.profile_image));

            setToolbar();

            categoryId = getIntent().getIntExtra("ID", 0);
            new SubCategoryTask().execute(categoryId + "");
        }
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new SubCategoryExpandableListAdapter(this, categoryList, subCategoryList);
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
                Product selectedItem = ((ArrayList<Product>) subCategoryList.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(SubcatgoryActivity.this, ListingActivity.class);
                intent.putExtra("ID", (selectedItem.id));
                intent.putExtra("TITLE", selectedItem.title);
                startActivity(intent);
                return false;
            }
        });
    }

    class SubCategoryTask extends AsyncTask<String, Void, String> {
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
            String UserId = mPrefs.getStringValue(AppPreferences.USER_ID);
            postDataParams.put("api_key", apikey);
            postDataParams.put("customer_id", UserId);
            postDataParams.put("category_id", params[0]);
            postDataParams.put("page", "1");


            return HTTPUrlConnection.getInstance().load(Config.CATEGORY, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray categoryArray = object.getJSONArray("data");
                    for (int i = 0; i < categoryArray.length(); i++) {
                        Product product = new Product();
                        product.id = ((JSONObject) categoryArray.get(i)).getInt("id");
                        product.thumbURL = ((JSONObject) categoryArray.get(i)).getString("image");
                        product.iconURL = ((JSONObject) categoryArray.get(i)).getString("icon");
                        product.title = ((JSONObject) categoryArray.get(i)).getString("name");

                        ArrayList<Product> child = new ArrayList<>();
                        if (!((JSONObject) categoryArray.get(i)).isNull("sub_category")) {
                            JSONArray subArray = (((JSONObject) categoryArray.get(i))
                                    .getJSONArray("sub_category"));
                            product.haveSubCategories = subArray.length() > 0;

                            for (int j = 0; j < subArray.length(); j++) {
                                Product subProduct = new Product();
                                subProduct.id = ((JSONObject) subArray.get(i)).getInt("id");
                                subProduct.thumbURL = ((JSONObject) subArray.get(i)).getString("image");
                                subProduct.iconURL = ((JSONObject) subArray.get(i)).getString("icon");
                                subProduct.title = ((JSONObject) subArray.get(i)).getString("name");
                                child.add(subProduct);
                                child.add(subProduct);
                                child.add(subProduct);
                                child.add(subProduct);
                                child.add(subProduct);
                                child.add(subProduct);
                                child.add(subProduct);
                                child.add(subProduct);



                            }
                        }
                        subCategoryList.add(child);
                        subCategoryList.add(child);
                        subCategoryList.add(child);
                        subCategoryList.add(child);
                        subCategoryList.add(child);
                        subCategoryList.add(child);
                        subCategoryList.add(child);
                        subCategoryList.add(child);

                        categoryList.add(product);
                        categoryList.add(product);
                        categoryList.add(product);
                        categoryList.add(product);
                        categoryList.add(product);
                        categoryList.add(product);
                        categoryList.add(product);
                        categoryList.add(product);
                    }
                    addDrawerItems();
                } else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(SubcatgoryActivity.this);
                } else {
                    Toast.makeText(SubcatgoryActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}
