package com.example.mifonelib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mifone_lib.core.MifoneCore;
import com.example.mifone_lib.listener.MifoneCoreListener;
import com.example.mifone_lib.model.other.ConfigMifoneCore;
import com.example.mifone_lib.model.other.RegistrationState;
import com.example.mifone_lib.model.other.State;
import com.example.mifone_lib.model.other.User;

import org.linphone.core.Core;
import org.linphone.core.Factory;

public class MainActivity extends AppCompatActivity {
    String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String basePath = getFilesDir().getAbsolutePath();
//        Core core = Factory.instance().createCore(
//                basePath,
//                null,
//                this
//        );
//        if(core == null) Log.d(TAG, "onCreate: nulllllll");
//        else Log.d(TAG, "onCreate: not nulllllll");
        ConfigMifoneCore configMifoneCore = new ConfigMifoneCore(5,"","");
        MifoneCore.initMifoneCore(configMifoneCore,getApplicationContext());
        if(MifoneCore.getInstance() != null) {
            MifoneCore.getInstance().registerListener(new MifoneCoreListener() {
                @Override
                public void onResultConfigAccount(boolean isSuccess, String message) {
                    Log.d("TAG", "onResultConfigAccount: "+isSuccess+", message: "+message);
                }

                @Override
                public void onIncomingCall(State state, String message) {
                    Log.d("TAG", "onIncomingCall: "+state+", message: "+message);
                }

                @Override
                public void onRegistrationStateChanged(RegistrationState state, String message) {
                    Log.d("TAG", "onRegistrationStateChanged: message: "+message);
                }

                @Override
                public void onExpiredAccessToken() {

                }

                @Override
                public void onResultConfigProxy(boolean isSuccess) {

                }
            });
        }
        MifoneCore.configAccount();
    }
}