package com.maesta.maesta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ForgetPasswordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setToolbar();
        ((Button) findViewById(R.id.btn_submit)).setOnClickListener(this);
        ((EditText) findViewById(R.id.txt_forget_password)).addTextChangedListener(new MyForgetValidation(((EditText) findViewById(R.id.txt_forget_password))));

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
                if (((EditText) findViewById(R.id.txt_forget_password)).getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.forget_password_input)).setError(getString(R.string.err_login_pass));
                    ((EditText) findViewById(R.id.txt_forget_password)).requestFocus();

                } else if (((EditText) findViewById(R.id.txt_forget_password)).getText().toString().trim().length() < 6) {
                    ((TextInputLayout) findViewById(R.id.forget_password_input)).setError(getString(R.string.password_length_error));
                    ((EditText) findViewById(R.id.txt_forget_password)).requestFocus();
                } else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }

            }
        }
    }

    class MyForgetValidation implements TextWatcher {
        private View view;

        private MyForgetValidation(View view) {
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
                case R.id.txt_forget_password:
                    if (((EditText) findViewById(R.id.txt_forget_password)).getText().toString().trim().isEmpty()) {
                        ((TextInputLayout) findViewById(R.id.forget_password_input)).setError(getString(R.string.err_login_pass));
                        ((EditText) findViewById(R.id.txt_forget_password)).requestFocus();
                    } else {
                        ((TextInputLayout) findViewById(R.id.forget_password_input)).setError(null);
                        ((TextInputLayout) findViewById(R.id.forget_password_input)).setErrorEnabled(false);
                    }
                    break;
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


