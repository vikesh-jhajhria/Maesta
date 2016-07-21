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
                if (((EditText) findViewById(R.id.txt_forget_username)).getText().toString().trim().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.forget_username_input)).setError(getString(R.string.err_login_name));
                    ((EditText) findViewById(R.id.txt_forget_username)).requestFocus();
                }
                   else {
                    startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                }

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


