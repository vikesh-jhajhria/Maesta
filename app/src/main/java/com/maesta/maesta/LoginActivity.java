package com.maesta.maesta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

        findViewById(R.id.btn_login).setOnClickListener(this);

        applyFont();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                if(validateCredential()) {
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }
                break;
        }
    }

    private boolean validateCredential() {
        //apply validations here
        return true;
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_username), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_forgot_pass), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_password), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.btn_login), Config.REGULAR);

    }
}
