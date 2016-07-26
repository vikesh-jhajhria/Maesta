package com.maesta.maesta;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by saloni.bhansali on 7/20/2016.
 */
public class ProfileActivity extends BaseActivity
{
    AppPreferences mPrefs;
TextView name,mobileno,adress,current_cat_level,nxt_cat_level,target;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mPrefs = AppPreferences.getAppPreferences(ProfileActivity.this);
        setToolbar();
        applyFont();
        new ProfileTask().execute();
        name=(TextView)findViewById(R.id.textview_username);
        mobileno=(TextView)findViewById(R.id.textview_mobile_no);
        adress=(TextView)findViewById(R.id.textview_billing_address);
        current_cat_level=(TextView)findViewById(R.id.textview_catgory_name);
        nxt_cat_level=(TextView)findViewById(R.id.textview_next_cat_level);
        target=(TextView)findViewById(R.id.textview_remaning_target_level);


    }
    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_name), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_username), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_mobile), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_mobile_no), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_billing_deliver), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_billing_address), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_current_cat), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_catgory_name), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_next_cat), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_next_cat_level), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_remaning_target), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.textview_remaning_target_level), Config.REGULAR);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
onBackPressed();
            return true;
        }
        if(item.getItemId() == R.id.search){

            return true;
        }

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_profile, menu);

        return true;
    }
    class ProfileTask extends AsyncTask<String, Void, String> {
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

            return HTTPUrlConnection.getInstance().load(Config.CUSTOMER_PROFILE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject productData = object.getJSONObject("data");
                    name.setText(productData.getString("name"));
                    adress.setText(productData.getString("address"));
                    mobileno.setText(productData.getString("mobile"));
                    current_cat_level.setText(productData.getString("current_category_level"));
                    nxt_cat_level.setText(productData.getString("next_category_level"));
                    target.setText(productData.getString("remaining_target"));


                } else {
                    Toast.makeText(ProfileActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
