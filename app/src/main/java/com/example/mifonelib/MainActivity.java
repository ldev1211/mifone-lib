package com.example.mifonelib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mifone_lib.core.MifoneCore;
import com.example.mifone_lib.listener.MifoneCoreListener;
import com.example.mifone_lib.model.other.RegistrationState;
import com.example.mifone_lib.model.other.State;
import com.example.mifone_lib.model.other.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = new User("luongdien1211@gmail.com","Luongdien1211@","sf");
        MifoneCore.initMifoneCore(user,getApplicationContext());
        if(MifoneCore.getInstance() != null) {
            Log.d("TAG", "onCreate: yah!");
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
            });
        }
        MifoneCore.configAccount();
    }
}