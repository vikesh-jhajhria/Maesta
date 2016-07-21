package com.maesta.maesta;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startTimer();
    }

    private void startTimer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                AppPreferences preferences = AppPreferences.getAppPreferences(getApplicationContext());
                String api_key = preferences.getStringValue(AppPreferences.API_KEY);
                Log.v(TAG, "api_key=" + api_key);
                if (api_key.isEmpty()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finishAffinity();
                } else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finishAffinity();
                }
            }
        }, Config.SPLASH_TIME);
    }
}
