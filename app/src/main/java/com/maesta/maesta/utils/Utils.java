package com.maesta.maesta.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import com.maesta.maesta.BaseActivity;
import com.maesta.maesta.LoginActivity;
import com.maesta.maesta.NetworkActivity;

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

    public static void resetLogin(BaseActivity activity) {
        AppPreferences pref = AppPreferences.getAppPreferences(activity);
        pref.putStringValue(AppPreferences.USER_ID, "");
        pref.putStringValue(AppPreferences.USER_NAME, "");
        pref.putStringValue(AppPreferences.USER_PHONE, "");
        pref.putStringValue(AppPreferences.USER_EMAIL, "");
        pref.putStringValue(AppPreferences.ADDRESS, "");
        pref.putStringValue(AppPreferences.API_KEY, "");
        pref.putStringValue(AppPreferences.CURRENT_CATEGORY_LEVEL, "");
        pref.putStringValue(AppPreferences.NEXT_CATEGORY_LEVEL, "");
        pref.putStringValue(AppPreferences.REMAINING_TARGET, "");

        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finishAffinity();
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

    public static boolean isNetworkConnected(Context context,boolean showWarning) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        boolean isConnected = netInfo != null && netInfo.isConnected();
        if (!isConnected && showWarning) {
            Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show();
        }
        /*if (!isConnected && showWarning) {
            ((BaseActivity)context).startActivity(new Intent(context, NetworkActivity.class));
        }*/
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
    public static void showDecisionDialog(Context context, String title, String message, final AlertCallback callbackListener) {

        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    callbackListener.callback();
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AlertCallback {
        void callback();
    }
}
