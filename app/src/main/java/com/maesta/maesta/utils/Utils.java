package com.maesta.maesta.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class Utils {

    public static HashMap getDeviceSize(Activity activity) {
        HashMap hashMap = new HashMap();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        hashMap.put("Height", displaymetrics.heightPixels);
        hashMap.put("Width", displaymetrics.widthPixels);
        return hashMap;
    }

    public static void setTypeface(Context context, TextView textview, String style) {
        if (style != null && style.equalsIgnoreCase(Config.BOLD)) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "font/Roboto_Bold.ttf");
            textview.setTypeface(face);
        }
        if (style != null && style.equalsIgnoreCase(Config.REGULAR)) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "font/Roboto_Regular.ttf");
            textview.setTypeface(face);
        }
        if (style != null && style.equalsIgnoreCase(Config.MEDIUM)) {
            Typeface face = Typeface.createFromAsset(context.getAssets(), "font/Roboto_Medium.ttf");
            textview.setTypeface(face);
        }

    }

    public static boolean isNetworkConnected(Context context, boolean showToast) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        boolean isConnected = netInfo != null && netInfo.isConnected();
        if (!isConnected && showToast)
            Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show();
        return (netInfo != null && netInfo.isConnected());
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
