package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ResetActivity extends BaseActivity {
    String newpass, confirmpass;
    AppPreferences mPrefs;
    String apiKey,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mPrefs = AppPreferences.getAppPreferences(ResetActivity.this);
        setToolbar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            apiKey = bundle.getString("API_KEY", "");
            userId = bundle.getString("ID", "");

        }

        applyFont();
        findViewById(R.id.btn_done).setOnClickListener(this);
        findViewById(R.id.password_show).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_done) {
            newpass = ((EditText) findViewById(R.id.et_new_pass)).getText().toString().trim();
            if (((EditText) findViewById(R.id.et_new_pass)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout) findViewById(R.id.new_pass_input)).setError(getString(R.string.err_new_pass));
                ((EditText) findViewById(R.id.et_new_pass)).requestFocus();

            } else if (((EditText) findViewById(R.id.et_new_pass)).getText().toString().trim().length() < 6) {
                ((TextInputLayout) findViewById(R.id.new_pass_input)).setError(getString(R.string.password_length_error));
                ((EditText) findViewById(R.id.et_new_pass)).requestFocus();
            } else if (((EditText) findViewById(R.id.et_confirm_pass)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout) findViewById(R.id.confirm_pass_input)).setError(getString(R.string.err_confirm_pass));
                ((EditText) findViewById(R.id.et_confirm_pass)).requestFocus();
            } else if (((EditText) findViewById(R.id.et_confirm_pass)).getText().toString().trim().length() < 6) {
                ((TextInputLayout) findViewById(R.id.confirm_pass_input)).setError(getString(R.string.password_length_error));
                ((EditText) findViewById(R.id.et_confirm_pass)).requestFocus();
            }
            else if (!((EditText) findViewById(R.id.et_confirm_pass)).getText().toString().trim().matches(newpass)) {
                ((TextInputLayout) findViewById(R.id.confirm_pass_input)).setError(getString(R.string.password_match_error));
                ((EditText) findViewById(R.id.et_confirm_pass)).requestFocus();
            } else {
                if (Utils.isNetworkConnected(getApplicationContext(), true)) {
                    newpass = ((EditText) findViewById(R.id.et_new_pass)).getText().toString().trim();
                    confirmpass = ((EditText) findViewById(R.id.et_confirm_pass)).getText().toString().trim();

                    new ResetPasswordTask().execute(newpass, confirmpass);
                }

            }
        }
        if (v.getId() == R.id.password_show) {
            if (((EditText) findViewById(R.id.et_new_pass)).getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                ((TextView) findViewById(R.id.password_show)).setText("HIDE");
                ((EditText) findViewById(R.id.et_new_pass)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ((EditText) findViewById(R.id.et_new_pass)).setSelection(((EditText) findViewById(R.id.et_new_pass)).length());

            } else {
                ((TextView) findViewById(R.id.password_show)).setText("SHOW");
                ((EditText) findViewById(R.id.et_new_pass)).setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((EditText) findViewById(R.id.et_new_pass)).setSelection(((EditText) findViewById(R.id.et_new_pass)).length());
            }
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.et_new_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.et_confirm_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.password_show), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_almost_done), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_secure_acc), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_done), Config.BOLD);

    }

    class ResetPasswordTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("api_key", getIntent().getStringExtra("API_KEY"));
            postDataParams.put("customer_id", getIntent().getStringExtra("ID"));
            postDataParams.put("new_password", params[0]);
            postDataParams.put("confirm_password", params[1]);
            return HTTPUrlConnection.getInstance().load(Config.RESET_PASSWORD, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Toast.makeText(ResetActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();


                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(ResetActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Change  Password");
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
}
