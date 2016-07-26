package com.maesta.maesta;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.maesta.maesta.adapter.ListingAdapter;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.vo.ListingVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saloni.bhansali on 7/20/2016.
 */
public class ListingActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<ListingVO> productList;
    private ListingAdapter listingAdapter;
    private int categoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        productList = new ArrayList<>();
        ListingVO productLists = new ListingVO();
        productLists.textTitile = "Rounded sass'n Class series of Maseta italia's Eye Wear Section";
        productLists.textcollection = "Rs.20,000";
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        listingAdapter = new ListingAdapter(productList, this);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listingAdapter);

        categoryId = getIntent().getIntExtra("ID", 0);
        new GetProductsTask().execute(categoryId + "");
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;


        }

        if (item.getItemId() == R.id.search) {

            return true;
        }
        if (item.getItemId() == R.id.check) {

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


    class GetProductsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("category_id", params[0]);
            postDataParams.put("page", "1");

            return HTTPUrlConnection.getInstance().load(Config.PRODUCT, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");

                } else {
                    Toast.makeText(ListingActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
