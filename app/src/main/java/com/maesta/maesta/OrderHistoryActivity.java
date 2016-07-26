package com.maesta.maesta;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.maesta.maesta.adapter.OrderHistoryAdapter;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class OrderHistoryActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<Order> orderList;
    private OrderHistoryAdapter orderAdapter;
    AppPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycleview);
        {
            mPrefs = AppPreferences.getAppPreferences(OrderHistoryActivity.this);
            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            orderList = new ArrayList<>();

            setToolbar();

            orderAdapter = new OrderHistoryAdapter(orderList, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(orderAdapter);
            new OrderHistoryTask().execute();
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Order History");
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
    class OrderHistoryTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("page", "1");


            return HTTPUrlConnection.getInstance().load(Config.ORDER_HISTORY, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray orderArray = object.getJSONArray("data");
                    for(int i= 0; i < orderArray.length(); i++) {
                        Order order = new Order();
                        order.order_id = ((JSONObject)orderArray.get(i)).getString("order_id");
                        order.invoice_number = ((JSONObject)orderArray.get(i)).getString("invoice_number");
                        order.order_place = ((JSONObject)orderArray.get(i)).getString("order_place");
                        order.order_status = ((JSONObject)orderArray.get(i)).getString("order_status");
                        order.total_amount = ((JSONObject)orderArray.get(i)).getString("total_amount");
                        orderList.add(order);
                    }
                    orderAdapter.notifyDataSetChanged();
                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(OrderHistoryActivity.this);
                }
                else {
                    Toast.makeText(OrderHistoryActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    }
