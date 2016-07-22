package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.maesta.maesta.adapter.ListingAdapter;
import com.maesta.maesta.adapter.MyCollectionAdapter;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.CollectionVO;
import com.maesta.maesta.vo.ListingVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saloni.bhansali on 7/20/2016.
 */
public class ListingActivity extends BaseActivity
{
    private RecyclerView recyclerView;
    private List<ListingVO> productList;
    private ListingAdapter listingAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        productList = new ArrayList<>();
        ListingVO productLists = new ListingVO();
        productLists.textTitile="Rounded sass'n Class series of Maseta italia's Eye Wear Section";
        productLists.textcollection="Rs.20,000";
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        listingAdapter = new ListingAdapter(productList, this);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listingAdapter);
    }
    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

  /*  class ListingTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("email_address", username);
            postDataParams.put("password", password);

            return HTTPUrlConnection.getInstance().load(Config.LOGIN, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    AppPreferences pref = AppPreferences.getAppPreferences(getApplicationContext());
                    pref.putStringValue(AppPreferences.USER_ID, data.getString("id"));
                    pref.putStringValue(AppPreferences.USER_NAME, data.getString("name"));
                    pref.putStringValue(AppPreferences.USER_EMAIL, data.getString("email"));
                    pref.putStringValue(AppPreferences.USER_PHONE, data.getString("mobile"));
                    pref.putStringValue(AppPreferences.ADDRESS, data.getString("address"));
                    pref.putStringValue(AppPreferences.API_KEY, data.getString("address"));
                    pref.putStringValue(AppPreferences.CURRENT_CATEGORY_LEVEL, data.getString("current_category_level"));
                    pref.putStringValue(AppPreferences.NEXT_CATEGORY_LEVEL, data.getString("next_category_level"));
                    pref.putStringValue(AppPreferences.REMAINING_TARGET, data.getString("remaining_target"));

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(ListingActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }*/

    @Override
public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;


        }

    if(item.getItemId() == R.id.search){

        return true;
    }
    if(item.getItemId() == R.id.check){

        return true;
    }
    return false;
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);

        return true;
    }



}
