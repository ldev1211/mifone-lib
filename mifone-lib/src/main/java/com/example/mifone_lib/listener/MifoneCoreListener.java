package com.example.mifone_lib.listener;

import org.linphone.core.Core;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;

public interface MifoneCoreListener {
    public void onResultConfigAccount(boolean isSuccess,String message);
    public void onIncomingCall(org.linphone.core.Call.State state,String message);
    public void onRegistrationStateChanged(ProxyConfig proxyConfig, RegistrationState state, String message);
    public void onLogCollectionUploadStateChanged(Core.LogCollectionUploadState state, String info);
}
