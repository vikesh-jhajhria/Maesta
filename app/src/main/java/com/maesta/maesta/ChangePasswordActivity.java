package com.maesta.maesta;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ChangePasswordActivity extends BaseActivity {
    Context context = ChangePasswordActivity.this;
    String newpass, confirmpass, verification;
    AppPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
        mPrefs = AppPreferences.getAppPreferences(ChangePasswordActivity.this);
        setToolbar();

        applyFont();

        ((Button) findViewById(R.id.btn_mobile_change)).setOnClickListener(this);

    }


    public void onClick(View v) {
        if (v.getId() == R.id.btn_mobile_change) {
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
            } else if (((EditText) findViewById(R.id.et_verification_code_pass)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout) findViewById(R.id.verification_input)).setError(getString(R.string.password_length_error));
                ((EditText) findViewById(R.id.et_verification_code_pass)).requestFocus();
            } else {
                if (Utils.isNetworkConnected(getApplicationContext(), true)) {
                    newpass = ((EditText) findViewById(R.id.et_new_pass)).getText().toString().trim();
                    confirmpass = ((EditText) findViewById(R.id.et_confirm_pass)).getText().toString().trim();
                    verification = ((EditText) findViewById(R.id.et_verification_code_pass)).getText().toString().trim();
                    new ChangePasswordTask().execute(newpass, confirmpass, verification);
                }

            }
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.et_new_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.et_verification_code_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.et_confirm_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_mobile_change), Config.BOLD);

    }

    class ChangePasswordTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("verification_code", params[2]);
            return HTTPUrlConnection.getInstance().load(Config.RESET_PASSWORD, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Toast.makeText(ChangePasswordActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    JSONObject data = object.getJSONObject("data");
                    AppPreferences pref = AppPreferences.getAppPreferences(getApplicationContext());
                    pref.putStringValue(AppPreferences.USER_ID, data.getString("id"));
                    pref.putStringValue(AppPreferences.USER_NAME, data.getString("name"));
                    pref.putStringValue(AppPreferences.USER_EMAIL, data.getString("email"));
                    pref.putStringValue(AppPreferences.USER_PHONE, data.getString("mobile"));
                    pref.putStringValue(AppPreferences.ADDRESS, data.getString("address"));
                    pref.putStringValue(AppPreferences.API_KEY, data.getString("api_key"));
                    pref.putStringValue(AppPreferences.CURRENT_CATEGORY_LEVEL, data.getString("current_category_level"));
                    pref.putStringValue(AppPreferences.NEXT_CATEGORY_LEVEL, data.getString("next_category_level"));
                    pref.putStringValue(AppPreferences.REMAINING_TARGET, data.getString("remaining_target"));

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finishAffinity();
                }else {
                    Toast.makeText(ChangePasswordActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
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
