package com.tonmx.inspection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil{
    private Gson mGson = null;
    private static GsonUtil mInstance = null;

    private GsonUtil(){
        mGson = new GsonBuilder().serializeNulls().setLenient().create();
    }

    public static GsonUtil getInstance(){
        if(mInstance == null){
            synchronized (GsonUtil.class){
                if(mInstance == null){
                    mInstance = new GsonUtil();
                }
            }
        }
        return mInstance;
    }

    public Gson getGson(){
        return mGson;
    }
}
