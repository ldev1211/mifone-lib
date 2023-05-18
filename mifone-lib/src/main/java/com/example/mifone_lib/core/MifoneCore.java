package com.example.mifone_lib.core;

import android.content.Context;

import com.example.mifone_lib.listener.MifoneCoreListener;
import com.example.mifone_lib.model.other.ConfigMifoneCore;

public interface MifoneCore {
    public void initMifoneCore(Context context, ConfigMifoneCore configMifoneCore);
    public void callOut(String phoneNumber);
    public void cancelCurrentCall();
    public void configMifoneCore();
    public void registerListener(MifoneCoreListener mifoneCoreListener);
}
