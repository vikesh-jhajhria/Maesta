package com.maesta.maesta;

import android.content.Intent;
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

import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((Button) findViewById(R.id.btn_login)).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_forgot_pass)).setOnClickListener(this);
        ((EditText) findViewById(R.id.txt_username)).addTextChangedListener(new MyLoginValidation(((EditText) findViewById(R.id.txt_username))));
        ((EditText) findViewById(R.id.txt_password)).addTextChangedListener(new MyLoginValidation(((EditText) findViewById(R.id.txt_password))));



        ((EditText) findViewById(R.id.txt_password)).setTypeface(((EditText) findViewById(R.id.txt_username)).getTypeface());
        applyFont();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        {
            if (v.getId() == R.id.txt_forgot_pass) {
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                finish();
            } else if ((v.getId() == R.id.btn_login)) {

                if (((EditText) findViewById(R.id.txt_username)).getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.login_name_input)).setError(getString(R.string.err_login_name));
                    ((EditText) findViewById(R.id.txt_username)).requestFocus();
                } else if (((EditText) findViewById(R.id.txt_password)).getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.rgr_password_input)).setError(getString(R.string.err_login_pass));
                    ((EditText) findViewById(R.id.txt_password)).requestFocus();

                } else if (((EditText) findViewById(R.id.txt_password)).getText().toString().trim().length() < 6) {
                    ((TextInputLayout) findViewById(R.id.rgr_password_input)).setError(getString(R.string.password_length_error));
                    ((EditText) findViewById(R.id.txt_password)).requestFocus();
                } else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
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
                    if (((EditText) findViewById(R.id.txt_username)).getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) findViewById(R.id.login_name_input)).setError(getString(R.string.err_login_name));
                        ((EditText) findViewById(R.id.txt_username)).requestFocus();
                    }  else {
                        ((TextInputLayout) findViewById(R.id.login_name_input)).setError(null);
                        ((TextInputLayout) findViewById(R.id.login_name_input)).setErrorEnabled(false);
                    }
                    break;
                case R.id.txt_password:
                    if (((EditText) findViewById(R.id.txt_password)).getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) findViewById(R.id.rgr_password_input)).setError(getString(R.string.err_login_pass));
                        ((EditText) findViewById(R.id.txt_password)).requestFocus();
                    } else {
                        ((TextInputLayout) findViewById(R.id.rgr_password_input )).setError(null);
                        ((TextInputLayout) findViewById(R.id.rgr_password_input)).setErrorEnabled(false);
                    }
                    break;

            }
        }
    }
}
