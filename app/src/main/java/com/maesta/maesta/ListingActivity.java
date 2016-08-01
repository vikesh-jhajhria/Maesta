package com.maesta.maesta;

import android.content.Intent;
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
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Collection;
import com.maesta.maesta.vo.ListingVO;

import org.json.JSONArray;
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
    AppPreferences mPrefs;
    private int page = 1;
    private boolean loadNextPage = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        productList = new ArrayList<>();
        ListingVO productLists = new ListingVO();
        mPrefs = AppPreferences.getAppPreferences(ListingActivity.this);

        listingAdapter = new ListingAdapter(productList, this, getIntent().getStringExtra("TITLE"));
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listingAdapter);

        categoryId = getIntent().getIntExtra("ID", 0);


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                    if (loadNextPage) {
                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                            loadNextPage = false;
                            page++;

                            if (Utils.isNetworkConnected(getApplicationContext(), true)) {
                                new GetProductsTask().execute(categoryId + "");
                            }
                        }
                    }
                }
            }
        });
        if (Utils.isNetworkConnected(getApplicationContext(), true)) {
            new GetProductsTask().execute(categoryId + "");
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class)
                    .putExtra("ID", categoryId + ""));
            return true;
        }
        if (item.getItemId() == R.id.check) {
            startActivity(new Intent(getApplicationContext(), MyCollectionActivity.class));
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
            String apikey = mPrefs.getStringValue(AppPreferences.API_KEY);
            postDataParams.put("category_id", params[0]);
            postDataParams.put("page", page + "");
            postDataParams.put("search_text", " ");
            postDataParams.put("api_key", apikey);
            return HTTPUrlConnection.getInstance().load(Config.PRODUCT, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        ListingVO product = new ListingVO();
                        product.id = ((JSONObject) data.get(i)).getInt("id");
                        product.textTitile = ((JSONObject) data.get(i)).getString("name");
                        product.price = ((JSONObject) data.get(i)).getString("price");
                        product.thumbURL = ((JSONObject) data.get(i)).getString("image");

                        productList.add(product);

                        JSONObject pageObject = object.getJSONObject("paging");
                        loadNextPage = pageObject.getBoolean("nextPage");
                    }
                    listingAdapter.notifyDataSetChanged();
                }/*else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(ListingActivity.this);
                }*/ else {
                    Toast.makeText(ListingActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
