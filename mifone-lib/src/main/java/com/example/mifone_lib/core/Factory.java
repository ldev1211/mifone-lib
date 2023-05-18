package com.example.mifone_lib.core;

import android.content.Context;

import com.example.mifone_lib.model.other.ConfigMifoneCore;

public class Factory {
    private static MifoneCoreHandle mifoneCoreHandle;
    private static Factory mInstance;
    private static MifoneCore mifoneCore;
    public Factory() {
    }
    public static void createCore(Context context, ConfigMifoneCore configMifoneCore){
        MifoneCoreHandle.initMifoneCore(context,configMifoneCore);
    }
    public static Factory getInstance(){
        if(mInstance==null) mInstance = new Factory();
        return mInstance;
    }
    public static MifoneCore getMifoneCore(){
        return MifoneCoreHandle.getMifoneCore();
    }
}