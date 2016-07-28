package com.maesta.maesta.utils;

/**
 * Created by vikesh.kumar on 7/18/2016.
 */
public class Config {
    public final static int SPLASH_TIME = 2000;
    public final static String MEDIUM = "MEDIUM";
    public final static String BOLD = "BOLD";
    public final static String REGULAR = "REGULAR";
    public final static String ROBOTO = "ROBOTO";


    public static boolean APP_IN_FRONT;

    /*Web Services start*/
    public static String BASEURL = "http://demo.vertexplus.com/maesta/webservice/";
    public static String HOME = BASEURL+"home";
    public static String CATEGORY = BASEURL+"category";
    public static String PRODUCT = BASEURL+"products";
    public static String PRODUCT_DETAIL = BASEURL+"product_detail";
    public static String ADD_TO_COLLECTION = BASEURL+"add_to_collection";
    public static String UPDATE_COLLECTION = BASEURL+"update_collection";
    public static String REMOVE_COLLECTION = BASEURL+"remove_collection";
    public static String MY_COLLECTION = BASEURL+"my_collection";
    public static String ORDER_PLACE = BASEURL+"order_place";
    public static String ORDER_HISTORY = BASEURL+"order_history";
    public static String ORDER_HISTORY_DETAIL = BASEURL+"order_history_detail";
    public static String PAGES = BASEURL+"pages";
    public static String SHOP_SETTING = BASEURL+"shop_setting";
    public static String CONTACT_US = BASEURL+"contact_us";
    public static String LOGIN = BASEURL+"login";
    public static String CUSTOMER_PROFILE = BASEURL+"customer_profile";
    public static String CHANGE_PASSWORD = BASEURL+"change_password";
    public static String FORGOT_PASSWORD = BASEURL+"forgot_password";
    public static String RESET_PASSWORD = BASEURL+"reset_password";
    public static String VERIFY_CODE = BASEURL+"verify_code";

}
