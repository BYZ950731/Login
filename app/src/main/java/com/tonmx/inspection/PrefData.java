package com.tonmx.inspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PrefData {

    public static final String TAG = "PrefData";
    private static PrefData mInstance = null;
    private static Context mContext = null;
    private SharedPreferences mSharedPreferences = null;
    private static final String PREF_NAME = "configs";


    private static final String LOGIN_STATUS = "login_status";                                 //登录状态
    private static final String HISTORY_USERID = "history_userid";                                 //记录账号id
    private static final String HISTORY_USERNAME = "history_username";                                 //记录账号
    private static final String HISTORY_PASSWORD = "history_password";                                 //记录密码
    private static final String HISTORY_IP = "history_ip";                                 //记录IP
    private static final String HISTORY_TOKEN = "history_token";                                 //token
    private static final String HISTORY_FULLNAME = "history_fullname";                                 //用户全名
    private static final String DEPART_NAME = "depart_name";                                 //部门名称
    private static final String REVIEWER_INSPECTION = "reviewer_inspection";                     //复核人列表
    private static final String REVIEWER_SELECT = "reviewer_select";                     //复核人选择
    private static final String REVIEWER_SELECT_ID = "reviewer_select_id";                     //复核人ID选择
    private PrefData(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        get(context);
    }

    private static PrefData get(Context context) {
        if (mInstance == null) {
            synchronized (PrefData.class) {
                if (mInstance == null) {
                    mInstance = new PrefData(context);
                }
            }
        }
        return mInstance;
    }

    public static SharedPreferences getPref() {
        if (mContext == null) {
            throw new RuntimeException("please call PrefData.init(context) first");
        }
        return get(mContext).mSharedPreferences;
    }

    public static boolean setLoginStatus(int value) {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putInt(LOGIN_STATUS, value);
        return editor.commit();
    }

    public static int getLoginStatus() {
        int defValue = 0;
        return getPref().getInt(LOGIN_STATUS, defValue);
    }

    public static boolean setHisName(String name){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(HISTORY_USERNAME,name);
        return editor.commit();
    }

    public static String getHisName() {
        String defValue = "";
        return getPref().getString(HISTORY_USERNAME, defValue);
    }

    public static boolean setHisPwd(String pwd){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(HISTORY_PASSWORD,pwd);
        return editor.commit();
    }

    public static String getHisPwd() {
        String defValue = "";
        return getPref().getString(HISTORY_PASSWORD, defValue);
    }

    public static boolean setHisIP(String ip){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(HISTORY_IP,ip);
        return editor.commit();
    }

    public static String getHisIP() {
        String defValue = "";
        return getPref().getString(HISTORY_IP, defValue);
    }

    public static boolean setUserId(int userid){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putInt(HISTORY_USERID,userid);
        return editor.commit();
    }

    public static int getUserId() {
        return getPref().getInt(HISTORY_USERID,-1);
    }

    public static boolean setToken(String ip){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(HISTORY_TOKEN,ip);
        return editor.commit();
    }

    public static String getToken() {
        String defValue = "";
        return getPref().getString(HISTORY_TOKEN, defValue);
    }

    public static boolean setFullName(String ip){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(HISTORY_FULLNAME,ip);
        return editor.commit();
    }

    public static String getFullName() {
        String defValue = "111111";
        return getPref().getString(HISTORY_FULLNAME, defValue);
    }

    public static boolean setDepartName(String ip){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(DEPART_NAME,ip);
        return editor.commit();
    }

    public static String getDepartName() {
        String defValue = "部门A";
        return getPref().getString(DEPART_NAME, defValue);
    }

    public static List<ReviewerItem> getReviewerInspection() {
        List<ReviewerItem> numberList = null;
        String numberJson = getPref().getString(REVIEWER_INSPECTION, "");
        if (!TextUtils.isEmpty(numberJson)) {
            Type type = new TypeToken<ArrayList<ReviewerItem>>() {
            }.getType();
            numberList = GsonUtil.getInstance().getGson().fromJson(numberJson, type);
        }
        return numberList;
    }

    public static void setReviewerInspection(List<ReviewerItem> list) {
        SharedPreferences.Editor editor = getPref().edit();
        if (list == null || list.size() == 0) {
            editor.putString(REVIEWER_INSPECTION, "");
        } else {
            String numberJson = GsonUtil.getInstance().getGson().toJson(list);
            editor.putString(REVIEWER_INSPECTION, numberJson);
        }
        editor.apply();
    }


    public static String getReviewerSelectName() {
        String defValue = "111111";
        return getPref().getString(REVIEWER_SELECT, defValue);
    }

    public static boolean setReviewerSelectName(String ip){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(REVIEWER_SELECT,ip);
        return editor.commit();
    }


    public static int getReviewerSelectId() {
        int defValue = -1;
        return getPref().getInt(REVIEWER_SELECT_ID,defValue);
    }

    public static boolean setReviewerSelectId(int id){
        SharedPreferences.Editor editor = getPref().edit();
        editor.putInt(REVIEWER_SELECT_ID,id);
        return editor.commit();
    }

}
