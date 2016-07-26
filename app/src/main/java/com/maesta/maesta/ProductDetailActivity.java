package com.maesta.maesta;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maesta.maesta.adapter.BannerAdapter;
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
 * Created by saloni.bhansali on 7/20/2016.
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Banner> productImageList = new ArrayList<>();
    private BannerAdapter productImageAdapter;
    private ViewPager productViewPager;
    AppPreferences mPrefs;
    private int ProductId;

    String quantity, collectionId, oldquantity;
    TextView txtview_product_name, productmodel_txtview, txtview_price, txtview_desc, txtview_price_detail, txtview_quantity, txtview_desc_detail;
    EditText et_quantity;
    String imageUrl = "",newQuantity="";
    private List<RadioButton> pagerIndicatorList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productViewPager = (ViewPager) findViewById(R.id.pager_product);
        mPrefs = AppPreferences.getAppPreferences(ProductDetailActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ProductId = bundle.getInt("ID", 0);
        }

        setToolbar();
        applyFont();
        findViewById(R.id.btn_add_collection).setOnClickListener(this);
        txtview_desc = (TextView) findViewById(R.id.textview_desc);
        txtview_product_name = (TextView) findViewById(R.id.product_name_txtview);
        productmodel_txtview = (TextView) findViewById(R.id.product_model_txtview);
        txtview_price = (TextView) findViewById(R.id.textview_product_price);
        txtview_price_detail = (TextView) findViewById(R.id.textview_price_detail);
        txtview_quantity = (TextView) findViewById(R.id.txtview_quantity);
        txtview_desc_detail = (TextView) findViewById(R.id.textview_desc_detail);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        ((Button) findViewById(R.id.btn_add_collection)).setOnClickListener(this);


        new ProductDetailTask().execute();

    }

    private void prepareBanner() {
        pagerIndicatorList = new ArrayList<>();
        productImageAdapter = new BannerAdapter(getSupportFragmentManager());
        for (int i = 0; i < productImageList.size(); i++) {
            RadioButton btn = createDot();
            pagerIndicatorList.add(btn);
            ((RadioGroup) findViewById(R.id.pager_indicator_group)).addView(btn);
            productImageAdapter.addFragment(BannerFragment.newInstance(productImageList.get(i)), "");
        }
        pagerIndicatorList.get(0).setChecked(true);
        productViewPager.setAdapter(productImageAdapter);

        productViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_add_collection:
                quantity = ((EditText) findViewById(R.id.et_quantity)).getText().toString().trim();
                if (quantity.isEmpty()||quantity.equalsIgnoreCase("0")) {
                    ((EditText) findViewById(R.id.et_quantity)).setError(getString(R.string.err_quantity));
                    ((EditText) findViewById(R.id.et_quantity)).requestFocus();
                    break;
                } else if (et_quantity.length() <= 0) {
                    ((EditText) findViewById(R.id.et_quantity)).setError(getString(R.string.err_quantity));
                    ((EditText) findViewById(R.id.et_quantity)).requestFocus();
                    break;
                } else {
                    new AddToCollectionTask().execute();

                }
        }
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }


    class ProductDetailTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("product_id", "" + ProductId);
            postDataParams.put("customer_id", UserId);

            return HTTPUrlConnection.getInstance().load(Config.PRODUCT_DETAIL, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject productData = object.getJSONObject("data");
                    txtview_product_name.setText(productData.getString("name"));
                    productmodel_txtview.setText(productData.getString("model"));
                    txtview_desc_detail.setText(productData.getString("description"));
                    txtview_price.setText(productData.getString("price"));
                    imageUrl = productData.getString("image");

                    JSONObject galleryObject = productData.getJSONObject("gallery");
                    if (galleryObject.getBoolean("status")) {
                        JSONArray galleryArray = galleryObject.getJSONArray("data");
                        for (int i = 0; i < galleryArray.length(); i++) {
                            Banner banner = new Banner();
                            banner.url = galleryArray.getString(i);
                            productImageList.add(banner);
                        }
                        //productImageAdapter.notifyDataSetChanged();
                        prepareBanner();
                    }
                    JSONObject oldCollection = object.getJSONObject("collection");
                    if (oldCollection.getBoolean("status")) {
                          JSONObject collectionData = oldCollection.getJSONObject("data");
                        collectionId = collectionData.getString("id");
                        et_quantity.setText(collectionData.getString("quantity"));
                        final String oldQuantity=collectionData.getString("quantity");


                        ((Button) findViewById(R.id.btn_add_collection)).setText("Update Collection");
                        ((Button) findViewById(R.id.btn_add_collection)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newQuantity=et_quantity.getText().toString().trim();
                                if(newQuantity.isEmpty()|| newQuantity.equalsIgnoreCase("0")){
                                    et_quantity.setError("Invalid Quantity");
                                    et_quantity.requestFocus();
                                }else if((oldQuantity).equalsIgnoreCase(newQuantity))
                                {
                                    startActivity(new Intent(getApplicationContext(), MyCollectionActivity.class));
                                    finish();
                                }
                                else {
                                    new UpdateCollectionTask().execute();
                                }
                            }
                        });

                    }


                } else {
                    Toast.makeText(ProductDetailActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class AddToCollectionTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("product_id", "" + ProductId);
            postDataParams.put("quantity", quantity);
            postDataParams.put("customer_id", UserId);

            return HTTPUrlConnection.getInstance().load(Config.ADD_TO_COLLECTION, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    startActivity(new Intent(getApplicationContext(), MyCollectionActivity.class));
                    finish();

                } else {
                    Toast.makeText(ProductDetailActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class UpdateCollectionTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int index;

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
            postDataParams.put("collection_id", collectionId);
            postDataParams.put("quantity",newQuantity);
            return HTTPUrlConnection.getInstance().load(Config.UPDATE_COLLECTION, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();

            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MyCollectionActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}