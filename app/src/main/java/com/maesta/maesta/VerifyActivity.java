package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VerifyActivity extends BaseActivity {
    AppPreferences mPrefs;
    EditText editText1, editText2, editText3;
    String et1, et2, et3, code;
    String apiKey, userId, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mPrefs = AppPreferences.getAppPreferences(VerifyActivity.this);
        setToolbar();
        findViewById(R.id.txtview_resend).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        ((EditText) findViewById(R.id.et_code1)).addTextChangedListener(new MyVerifyValidation(((EditText) findViewById(R.id.et_code1))));
        ((EditText) findViewById(R.id.et_code2)).addTextChangedListener(new MyVerifyValidation(((EditText) findViewById(R.id.et_code2))));
        ((EditText) findViewById(R.id.et_code3)).addTextChangedListener(new MyVerifyValidation(((EditText) findViewById(R.id.et_code3))));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            apiKey = bundle.getString("API_KEY", "");
            userId = bundle.getString("ID", "");
            email = bundle.getString("EMAIL", "");

        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Verify");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txtview_resend:
                new ResendCodeTask().execute(email + "");
                break;
            case R.id.btn_submit:
                if (((EditText) findViewById(R.id.et_code1)).getText().toString().trim().isEmpty()) {
                    ((EditText) findViewById(R.id.et_code1)).setError(getString(R.string.err_code));
                    ((EditText) findViewById(R.id.et_code1)).requestFocus();
                } else if (((EditText) findViewById(R.id.et_code2)).getText().toString().trim().isEmpty()) {
                    ((EditText) findViewById(R.id.et_code2)).setError(getString(R.string.err_code));
                    ((EditText) findViewById(R.id.et_code2)).requestFocus();
                } else if (((EditText) findViewById(R.id.et_code3)).getText().toString().trim().isEmpty()) {
                    ((EditText) findViewById(R.id.et_code3)).setError(getString(R.string.err_code));
                    ((EditText) findViewById(R.id.et_code3)).requestFocus();
                } else if (Utils.isNetworkConnected(this, true)) {
                    et1 = ((EditText) findViewById(R.id.et_code1)).getText().toString().trim();
                    et2 = ((EditText) findViewById(R.id.et_code2)).getText().toString().trim();
                    et3 = ((EditText) findViewById(R.id.et_code3)).getText().toString().trim();
                    code = et1 + et2 + et3;
                    new VerifyCodeTask().execute(code, apiKey + "", userId + "");
                }
                break;
        }

    }

    class ResendCodeTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();

            postDataParams.put("email_address", params[0]);


            return HTTPUrlConnection.getInstance().load(Config.FORGOT_PASSWORD, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    Toast.makeText(VerifyActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(VerifyActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    class VerifyCodeTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("api_key", params[1]);
            postDataParams.put("customer_id", params[2]);
            postDataParams.put("verification_code", params[0]);


            return HTTPUrlConnection.getInstance().load(Config.VERIFY_CODE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    Toast.makeText(VerifyActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), ResetActivity.class).putExtra("API_KEY",apiKey).putExtra("ID",userId));
                    finish();
                } else {
                    Toast.makeText(VerifyActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class MyVerifyValidation implements TextWatcher {
        private View view;

        private MyVerifyValidation(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (view.getId()) {
                case R.id.et_code1:
                    if (((EditText) findViewById(R.id.et_code1)).getText().toString().trim().length() == 2) {

                        ((EditText) findViewById(R.id.et_code2)).requestFocus();
                    }
                    break;
                case R.id.et_code2:
                    if (((EditText) findViewById(R.id.et_code2)).getText().toString().trim().length() == 2) {

                        ((EditText) findViewById(R.id.et_code3)).requestFocus();

                    } else if (((EditText) findViewById(R.id.et_code2)).getText().toString().trim().length() == 0) {

                        ((EditText) findViewById(R.id.et_code1)).requestFocus();
                    }
                    break;
                case R.id.et_code3:
                    if (((EditText) findViewById(R.id.et_code3)).getText().toString().trim().length() == 0) {

                        ((EditText) findViewById(R.id.et_code2)).requestFocus();
                    }
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
