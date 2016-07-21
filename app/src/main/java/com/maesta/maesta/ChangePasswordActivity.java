package com.maesta.maesta;

import android.content.Context;
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

import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;

public class ChangePasswordActivity extends BaseActivity {
    Context context = ChangePasswordActivity.this;
    String  newpass,confirmpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
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
            } else {
                if (Utils.isNetworkConnected(getApplicationContext(), true)) {
                    newpass = ((EditText) findViewById(R.id.et_new_pass)).getText().toString().trim();
                    confirmpass = ((EditText) findViewById(R.id.et_confirm_pass)).getText().toString().trim();

                } else {
                    Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.et_new_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.et_confirm_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_mobile_change), Config.BOLD);

    }
    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Change  Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}
