package com.example.mifone_lib.listener;

import com.example.mifone_lib.model.other.RegistrationState;
import com.example.mifone_lib.model.other.State;


public interface MifoneCoreListener {
    public void onResultConfigAccount(boolean isSuccess,String message);
    public void onIncomingCall(State state, String message);
    public void onRegistrationStateChanged(RegistrationState state, String message);
//    public void onLogCollectionUploadStateChanged(Core.LogCollectionUploadState state, String info);
}
