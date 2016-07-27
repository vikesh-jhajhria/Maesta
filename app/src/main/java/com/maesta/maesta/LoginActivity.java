package com.maesta.maesta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by vikesh.kumar on 7/18/2016.
 */
public class LoginActivity extends BaseActivity {

    String username = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((Button) findViewById(R.id.btn_login)).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_forgot_pass)).setOnClickListener(this);
        ((EditText) findViewById(R.id.txt_password)).setTypeface(((EditText) findViewById(R.id.txt_username)).getTypeface());
        ((EditText) findViewById(R.id.txt_username)).addTextChangedListener(new MyLoginValidation(((EditText) findViewById(R.id.txt_username))));
        ((EditText) findViewById(R.id.txt_password)).addTextChangedListener(new MyLoginValidation(((EditText) findViewById(R.id.txt_password))));

        applyFont();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_forgot_pass:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_login:
                username = ((EditText) findViewById(R.id.txt_username)).getText().toString().trim();
                if (username.isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.login_name_input)).setError(getString(R.string.err_login_name));
                    ((EditText) findViewById(R.id.txt_username)).requestFocus();
                    break;
                } else if (!Utils.isEmailValid(username)) {
                    ((TextInputLayout) findViewById(R.id.login_name_input)).setError(getString(R.string.err_invalid_email));
                    ((EditText) findViewById(R.id.txt_username)).requestFocus();
                    break;
                }
                password = ((EditText) findViewById(R.id.txt_password)).getText().toString().trim();
                if (password.isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.rgr_password_input)).setError(getString(R.string.err_login_pass));
                    ((EditText) findViewById(R.id.txt_password)).requestFocus();
                    break;
                } else if (password.length() < 3) {
                    ((TextInputLayout) findViewById(R.id.rgr_password_input)).setError(getString(R.string.password_length_error));
                    ((EditText) findViewById(R.id.txt_password)).requestFocus();
                    break;
                } else {
                    if(Utils.isNetworkConnected(this,true))
                        new LogInTask().execute();
                }
                break;
        }
    }


    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_username), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_forgot_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_password), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.btn_login), Config.REGULAR);
    }

    class MyLoginValidation implements TextWatcher {
        private View view;

        private MyLoginValidation(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.txt_username:
                    username = ((EditText) findViewById(R.id.txt_username)).getText().toString().trim();
                    if (username.isEmpty()) {
                        ((TextInputLayout) findViewById(R.id.login_name_input)).setError(getString(R.string.err_login_name));
                        ((EditText) findViewById(R.id.txt_username)).requestFocus();
                    } else if (!Utils.isEmailValid(username)) {
                        ((TextInputLayout) findViewById(R.id.login_name_input)).setError(getString(R.string.err_invalid_email));
                        ((EditText) findViewById(R.id.txt_username)).requestFocus();
                    } else {
                        ((TextInputLayout) findViewById(R.id.login_name_input)).setError(null);
                        ((TextInputLayout) findViewById(R.id.login_name_input)).setErrorEnabled(false);
                    }
                    break;
                case R.id.txt_password:
                    if (((EditText) findViewById(R.id.txt_password)).getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) findViewById(R.id.rgr_password_input)).setError(getString(R.string.err_login_pass));
                        ((EditText) findViewById(R.id.txt_password)).requestFocus();
                    } else {
                        ((TextInputLayout) findViewById(R.id.rgr_password_input)).setError(null);
                        ((TextInputLayout) findViewById(R.id.rgr_password_input)).setErrorEnabled(false);
                    }
                    break;

            }
        }
    }

    class LogInTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("email_address", username);
            postDataParams.put("password", password);

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
                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(LoginActivity.this);
                }
                else {
                    Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
