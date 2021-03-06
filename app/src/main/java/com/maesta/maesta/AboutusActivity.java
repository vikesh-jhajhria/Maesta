package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AboutusActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        setToolbar();
        applyFont();

        ImageView banner = (ImageView) findViewById(R.id.img_about);
        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.height = (1000*((int) Utils.getDeviceSize(this).get("Width"))) / 2057;
        banner.setLayoutParams(params);

        if (Utils.isNetworkConnected(getApplicationContext(), false))
            new GetAboutusTask().execute();
        else
            startActivityForResult(new Intent(this, NetworkActivity.class), Config.NETWORK_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.NETWORK_ACTIVITY) {
            if (Utils.isNetworkConnected(this, false))
                new GetAboutusTask().execute();
            else
                onBackPressed();
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void applyFont() {
        //Utils.setTypeface(getApplicationContext(), (WebView) findViewById(R.id.txt_about), Config.REGULAR);
    }

    class GetAboutusTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("name", "about-us");

            return HTTPUrlConnection.getInstance().load(Config.PAGES, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    ((WebView) findViewById(R.id.txt_about)).loadData(data.getString("description"), "text/html; charset=utf-8", "UTF-8");
                    //.setText(Html.fromHtml(data.getString("description")));
                    Glide.with(getApplicationContext()).load(data.getString("image")).asBitmap()
                            .placeholder(R.drawable.default_image).fitCenter().into((ImageView) findViewById(R.id.img_about));
                } else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(AboutusActivity.this);
                } else {
                    Toast.makeText(AboutusActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}