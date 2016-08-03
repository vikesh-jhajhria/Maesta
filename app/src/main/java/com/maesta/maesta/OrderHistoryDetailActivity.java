package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.maesta.maesta.adapter.MyCollectionAdapter;
import com.maesta.maesta.adapter.OrderHistoryDetailAdapter;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class OrderHistoryDetailActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<Collection> orderdetail;
    private OrderHistoryDetailAdapter orderAdapter;
    private AppPreferences mPrefs;
    private String orderId = "";
    private int page = 1;
    private boolean loadNextPage = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycleview);
        orderId = getIntent().getStringExtra("ID");
        mPrefs = AppPreferences.getAppPreferences(OrderHistoryDetailActivity.this);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        orderdetail = new ArrayList<>();
        orderAdapter = new OrderHistoryDetailAdapter(orderdetail, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);

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

                            if (Utils.isNetworkConnected(getApplicationContext(),true)) {
                                new OrderHistoryDetailTask().execute(orderId);
                            }
                        }
                    }
                }
            }
        });

        if (Utils.isNetworkConnected(this, false))
            new OrderHistoryDetailTask().execute(orderId);
        else
            startActivityForResult(new Intent(this, NetworkActivity.class), Config.NETWORK_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.NETWORK_ACTIVITY) {
            if (Utils.isNetworkConnected(this, false))
                new OrderHistoryDetailTask().execute(orderId);
            else
                onBackPressed();
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Order Detail");
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

    class OrderHistoryDetailTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("order_id", params[0]);
            postDataParams.put("page", page+"");
            return HTTPUrlConnection.getInstance().load(Config.ORDER_HISTORY_DETAIL, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray orderArray = object.getJSONArray("data");
                    for (int i = 0; i < orderArray.length(); i++) {
                        Collection collection = new Collection();
                        collection.product_name = ((JSONObject) orderArray.get(i)).getString("product_name");
                        collection.quantity_number = ((JSONObject) orderArray.get(i)).getString("quantity");
                        collection.price = ((JSONObject) orderArray.get(i)).getString("amount");
                        collection.thumbURL = ((JSONObject) orderArray.get(i)).getString("image");
                        collection.quantity = ("Quantity");
                        orderdetail.add(collection);

                    }
                orderAdapter.notifyDataSetChanged();
                    JSONObject pageObject= object.getJSONObject("paging");
                    loadNextPage = pageObject.getBoolean("nextPage");
                } else if  (!object.isNull("apistatus") && object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(OrderHistoryDetailActivity.this);
                }else {
                    Toast.makeText(OrderHistoryDetailActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
