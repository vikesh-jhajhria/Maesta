package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ForgetPasswordActivity extends BaseActivity {
    String email;
    AppPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mPrefs = AppPreferences.getAppPreferences(ForgetPasswordActivity.this);
        setToolbar();
        ((Button) findViewById(R.id.btn_submit)).setOnClickListener(this);

    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Forget Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        {
            if ((v.getId() == R.id.btn_submit)) {
                email = ((EditText) findViewById(R.id.txt_forget_username)).getText().toString().trim();
                if (((EditText) findViewById(R.id.txt_forget_username)).getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.forget_username_input)).setError(getString(R.string.err_login_name));
                    ((EditText) findViewById(R.id.txt_forget_username)).requestFocus();
                }
                   else {
                   new ForgetPasswordTask().execute();
                }

            }
        }
    }

    class ForgetPasswordTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("email_address", email);
            postDataParams.put("api_key", apikey);
            postDataParams.put("customer_id", UserId);

            return HTTPUrlConnection.getInstance().load(Config.LOGIN, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                    finishAffinity();
                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(ForgetPasswordActivity.this);
                }
                else {
                    Toast.makeText(ForgetPasswordActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



        public boolean onOptionsItemSelected (MenuItem item){
                    if (item.getItemId() == android.R.id.home) {
                        onBackPressed();
                        return true;
                    }

                    return false;
                }
            }


