package com.maesta.maesta;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
public class ContactUsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        findViewById(R.id.btn_mobile_change).setOnClickListener(this);
        setToolbar();
        applyFont();
        new GetContactDetailTask().execute();
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle(getString(R.string.contact_us));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.et_name), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.et_contact), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.et_msg), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_mobile_change), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.phone_txtview), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txtview_phone_no), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.full_adres_txtview1), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.contact_adres_name), Config.MEDIUM);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.contct_txtview), Config.BOLD);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;


        }
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_mobile_change:
                String name = ((EditText)findViewById(R.id.et_name)).getText().toString().trim();
                String number = ((EditText)findViewById(R.id.et_contact)).getText().toString().trim();
                String message = ((EditText)findViewById(R.id.et_msg)).getText().toString().trim();

                if(name.isEmpty()){
                    ((EditText)findViewById(R.id.et_name)).setError(getString(R.string.err_name));
                    ((EditText)findViewById(R.id.et_name)).requestFocus();
                    break;
                } else if(number.isEmpty()){
                    ((EditText)findViewById(R.id.et_contact)).setError(getString(R.string.err_number));
                    ((EditText)findViewById(R.id.et_contact)).requestFocus();
                    break;
                }else if(number.length() < 10){
                    ((EditText)findViewById(R.id.et_contact)).setError(getString(R.string.err_number_length));
                    ((EditText)findViewById(R.id.et_contact)).requestFocus();
                    break;
                } else if(message.isEmpty()){
                    ((EditText)findViewById(R.id.et_msg)).setError(getString(R.string.err_message));
                    ((EditText)findViewById(R.id.et_msg)).requestFocus();
                    break;
                }
               new ContactUsTask().execute(name,number,message);
                break;
        }
    }

    class ContactUsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            AppPreferences mPrefs = AppPreferences.getAppPreferences(getApplicationContext());
            String apikey = mPrefs.getStringValue(AppPreferences.API_KEY);
            postDataParams.put("api_key", apikey);
            postDataParams.put("name", params[0]);
            postDataParams.put("contact_number", params[1]);
            postDataParams.put("message", params[2]);

            return HTTPUrlConnection.getInstance().load(Config.CONTACT_US, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    ((EditText)findViewById(R.id.et_name)).setText("");
                    ((EditText)findViewById(R.id.et_contact)).setText("");
                    ((EditText)findViewById(R.id.et_msg)).setText("");
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    class GetContactDetailTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            AppPreferences mPrefs = AppPreferences.getAppPreferences(getApplicationContext());
            String apikey = mPrefs.getStringValue(AppPreferences.API_KEY);
            postDataParams.put("api_key", apikey);

            return HTTPUrlConnection.getInstance().load(Config.SHOP_SETTING, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    ((TextView) findViewById(R.id.contact_adres_name)).setText(data.getString("company_name"));
                    ((TextView) findViewById(R.id.full_adres_txtview1)).setText(data.getString("company_address"));
                    ((TextView) findViewById(R.id.txtview_phone_no)).setText(data.getString("company_phone"));
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}