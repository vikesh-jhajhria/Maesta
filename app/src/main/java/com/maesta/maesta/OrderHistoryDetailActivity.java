package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.maesta.maesta.adapter.OrderHistoryDetailAdapter;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.vo.OrderHistoryDetailVO;

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
    private List<OrderHistoryDetailVO> orderdetail;
    private OrderHistoryDetailAdapter orderAdapter;
    AppPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycleview);
        {
            mPrefs = AppPreferences.getAppPreferences(OrderHistoryDetailActivity.this);

            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            orderdetail = new ArrayList<>();
            OrderHistoryDetailVO collections = new OrderHistoryDetailVO();
            collections.product_name="Rounded sass'n Class series of Maseta italia's Eye Wear Section";
            collections.price="Rs.20,000.00";
            collections.quantity="Quantity";
            collections.quantity_number="20";

            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            setToolbar();
            orderAdapter = new OrderHistoryDetailAdapter(orderdetail, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(orderAdapter);
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Order History Detail");
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
            String UserId = mPrefs.getStringValue(AppPreferences.USER_ID);
            postDataParams.put("api_key", apikey);
            postDataParams.put("order_id", "");
            postDataParams.put("page", "1");


            return HTTPUrlConnection.getInstance().load(Config.ORDER_HISTORY_DETAIL, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    startActivity(new Intent(getApplicationContext(), OrderHistoryDetailActivity.class));

                }
                else {
                    Toast.makeText(OrderHistoryDetailActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    }
