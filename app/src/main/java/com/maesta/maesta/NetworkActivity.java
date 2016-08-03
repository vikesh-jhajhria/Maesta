package com.maesta.maesta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;

/**
 * Created by vikesh.kumar on 8/2/2016.
 */

public class NetworkActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_no_network);
        applyFont();
        findViewById(R.id.btn_try_again).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_try_again:
                if(Utils.isNetworkConnected(this,false)) {
                    Intent intent=new Intent();
                    setResult(Config.NETWORK_ACTIVITY,intent);
                    finish();
                }
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_cnt_connect), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_plz_check), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_try_again), Config.REGULAR);
    }
}
