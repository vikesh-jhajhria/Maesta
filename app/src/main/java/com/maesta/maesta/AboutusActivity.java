package com.maesta.maesta;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
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
public class AboutusActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        setToolbar();
        applyFont();
        new GetAboutusTask().execute();
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
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_about), Config.REGULAR);
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
                    ((TextView) findViewById(R.id.txt_about)).setText(Html.fromHtml(data.getString("description")));
                    Glide.with(getApplicationContext()).load(data.getString("image")).asBitmap()
                            .placeholder(R.drawable.banner_1).fitCenter().into((ImageView) findViewById(R.id.img_about));
                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(AboutusActivity.this);
                }
                else {
                    Toast.makeText(AboutusActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}