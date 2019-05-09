package com.provider.admin.cpepsi_provider.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Admin on 17-10-2015.
 */
public class SessionManager {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String MyPREFERENCES = "MyPrefss";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_STATE = "state";
    public static final String KEY_OP_BAL = "opening_bal";
    public static final String KEY_TYPE = "type";
    public static final String SERVICE_SUB_KEY = "Service_SUB_key";
    public static final String SERVICE_SUB_KEY_ADDRESS = "SERVICE_SUB_KEY_ADDRESS";
    public static final String SERVICE_KEY_name = "Service_key";
    private static final String CITY_ID = "CITY_ID";
    private static final String CLIENT_NAME = "client_name";
    private static final String Consultant_Id = "Consultant";
    private static final String Consultant_name = "Consultant_name";
    private static final String SERVICE_SELECTED = "service_selected";

//    public static String getCityName() {
//        return CITY_NAME;
//    }

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();
        editor = pref.edit();

    }


    public void setSERVICE_SUB_KEY(int strName) {
        editor.putInt(SERVICE_SUB_KEY, strName);
       // editor.clear();
        editor.commit();
    }

    public void setCityId(int strcit) {
        editor.putInt(CITY_ID, strcit);
        editor.commit();
    }

    public void setSERVICE_SUB_KEY_ADDRESS(String strsub_id) {
        editor.putString(SERVICE_SUB_KEY_ADDRESS, strsub_id);
       // editor.clear();
        editor.commit();
    }

    public static String getCityId() {
        return CITY_ID;

    }
//    public  String getSERVICE_SUB_KEY_ADDRESS() {
//        pref.getString(SERVICE_SELECTED, null);
//    }

    public void set_Consult_Id(String strCon) {
        editor.putString(Consultant_Id, strCon);
        editor.commit();
    }

    public void setService_selected(String strservice) {
        editor.putString(SERVICE_SELECTED, strservice);
       // editor.clear();
        editor.commit();
    }

    public void setSERVICE_KEY(int strser) {
        editor.putInt(SERVICE_KEY_name, strser);
        editor.commit();
    }

    public void setConsultant_name(String strCon) {
        editor.putString(Consultant_name, strCon);
        editor.commit();
    }

    public void serverLogin(String strName, String strType, String strState, String strOPBal) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
        editor.putString(KEY_TYPE, strType);
        editor.putString(KEY_STATE, strState);
        editor.putString(KEY_OP_BAL, strOPBal);
        editor.commit();
    }

    //    public void serverEmailLogin(String strName, String strMobile, String strCoustId) {
////        editor.putBoolean(IS_LOGIN, true);
//        editor.putString(KEY_USERNAME, strName);
//        editor.putString(KEY_MOBILE, strMobile);
//        editor.putString(COUS_KEY, strCoustId);
//        editor.commit();
//    }
    public void serverEmailLogin(String strName, String strMobile) {
//        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
        editor.putString(KEY_MOBILE, strMobile);
        editor.commit();
    }
//    public void serverEmailLogin(String strCoust) {
////        editor.putBoolean(IS_LOGIN, true);
//        editor.putString(COUS_KEY, strCoust);
//        editor.commit();
//    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGIN, isLoggedIn);
        editor.commit();
    }

    public void setSkipped(boolean isLoggedIn) {
        editor.putBoolean("", isLoggedIn);
        editor.commit();
    }

    // Get Skipped State
    public boolean isSkipped() {
        return pref.getBoolean("", false);
    }

    public void malegaonLogin() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    // Clearing all data from Shared Preferences
    public void logoutUser() {
        editor.clear();
        editor.commit();

    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getSERVICE_SUB_KEY_ADDRESS() {
        return pref.getString(SERVICE_SUB_KEY_ADDRESS, null);
    }

    public String getMobile() {
        return pref.getString(KEY_MOBILE, null);
    }

    public int getSERVICE_SUB_KEY() {
        return pref.getInt(SERVICE_SUB_KEY, 10);
    }

    public String getServiceSelected() {
        return pref.getString(SERVICE_SELECTED, "null");
    }

    public String getOpBal() {
        return pref.getString(KEY_OP_BAL, null);
    }

    public String getType() {
        return pref.getString(KEY_TYPE, null);
    }

    public String getClientName() {
        return pref.getString(CLIENT_NAME, null);
    }

    public int getServiceKey() {
        return pref.getInt(SERVICE_KEY_name, 3);
    }

    public String get_Consult_Id() {

        return pref.getString(Consultant_Id, null);
    }

//    public String getCoustId() {
//        return pref.getString(COUS_KEY, null);
//    }
}