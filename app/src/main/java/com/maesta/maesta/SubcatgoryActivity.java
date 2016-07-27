package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.maesta.maesta.adapter.SubCategoryAdapter;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.SubCategoryVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class SubcatgoryActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<SubCategoryVO> subcat;
    private SubCategoryAdapter subcategoryAdapter;
    AppPreferences mPrefs;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_subcatogary);
        {
            mPrefs = AppPreferences.getAppPreferences(SubcatgoryActivity.this);
            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            subcat = new ArrayList<>();
            /*SubCategoryVO collections = new SubCategoryVO ();
            collections.productName ="Rounded sass'n ";


            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
*/
            setToolbar();
            subcategoryAdapter = new SubCategoryAdapter(subcat, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(subcategoryAdapter);
            new SubCategoryTask().execute();
            categoryId = getIntent().getIntExtra("categoryId", 0);
            new SubCategoryTask().execute(categoryId + "");
        }
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
                 /*   JSONObject data = object.getJSONObject("data");*/

                    startActivity(new Intent(getApplicationContext(), ListingActivity.class));
                    finishAffinity();
                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(SubcatgoryActivity.this);
                }
                else {
                    Toast.makeText(SubcatgoryActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return false;
    }
    }
