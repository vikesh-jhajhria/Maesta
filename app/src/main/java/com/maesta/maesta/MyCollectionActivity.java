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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maesta.maesta.adapter.MyCollectionAdapter;
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
public class MyCollectionActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<Collection> collectionList;
    private MyCollectionAdapter collectionAdapter;
    AppPreferences mPrefs;
    TextView totalprice;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollection);
        {
            mPrefs = AppPreferences.getAppPreferences(MyCollectionActivity.this);
            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            collectionList = new ArrayList<>();
            findViewById(R.id.btn_add_new).setOnClickListener(this);

            setToolbar();
            applyFont();
            collectionAdapter = new MyCollectionAdapter(collectionList, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(collectionAdapter);
            totalprice = (TextView) findViewById(R.id.txtview_total_price);
            findViewById(R.id.btn_place_order).setOnClickListener(this);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isNetworkConnected(getApplicationContext(), false))
            new MyCollectionTask().execute();
        else
            startActivityForResult(new Intent(this, NetworkActivity.class), Config.NETWORK_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.NETWORK_ACTIVITY) {
            if (Utils.isNetworkConnected(this, false))
                new MyCollectionTask().execute();
            else
                onBackPressed();
        }
    }

    public void resetTotal(String totalAmout) {
        if(collectionList.size() == 0)
            findViewById(R.id.layout_no_collection).setVisibility(View.VISIBLE);
        totalprice.setText(totalAmout);
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("My Collection");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txtview_total), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txtview_total_price), Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_place_order), Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_no_collection), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_add_new), Config.BOLD);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_place_order:

                if (Utils.isNetworkConnected(this, true) && collectionList.size() > 0) {
                    new PlaceOrderTask().execute();

                }
                break;
            case R.id.btn_add_new:
                onBackPressed();
                break;

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class)
                    .putExtra("ID", ""));
            return true;
        }
        return false;
    }


    class MyCollectionTask extends AsyncTask<String, Void, String> {
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

            return HTTPUrlConnection.getInstance().load(Config.MY_COLLECTION, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    findViewById(R.id.layout_no_collection).setVisibility(View.GONE);
                    findViewById(R.id.ll_bottom_button).setVisibility(View.VISIBLE);
                    totalprice.setText(object.getString("total"));
                    JSONArray productArray = object.getJSONArray("data");
                    collectionList.clear();
                    for (int i = 0; i < productArray.length(); i++) {
                        Collection collection = new Collection();
                        collection.id = ((JSONObject) productArray.get(i)).getInt("collection_id");
                        categoryId = ((JSONObject) productArray.get(i)).getInt("collection_id");
                        collection.product_name = ((JSONObject) productArray.get(i)).getString("name");
                        collection.quantity_number = ((JSONObject) productArray.get(i)).getString("quantity");
                        collection.price = ((JSONObject) productArray.get(i)).getString("price");
                        collection.thumbURL = ((JSONObject) productArray.get(i)).getString("image");
                        collection.quantity = ("Quantity");
                        collectionList.add(collection);
                    }
                    collectionAdapter.notifyDataSetChanged();
                } else if(object.getString("message").equalsIgnoreCase("No records found.")) {
                    findViewById(R.id.layout_no_collection).setVisibility(View.VISIBLE);
                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(MyCollectionActivity.this);
                } else {
                    Toast.makeText(MyCollectionActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class PlaceOrderTask extends AsyncTask<String, Void, String> {
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

            return HTTPUrlConnection.getInstance().load(Config.ORDER_PLACE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Toast.makeText(MyCollectionActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class));
                    finish();

                } else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(MyCollectionActivity.this);
                } else {
                    Toast.makeText(MyCollectionActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_profile, menu);
        return true;
    }


}
