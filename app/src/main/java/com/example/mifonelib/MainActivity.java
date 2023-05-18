package com.example.mifonelib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mifone_lib.core.Factory;
import com.example.mifone_lib.core.MifoneAPI;
import com.example.mifone_lib.core.MifoneCore;
import com.example.mifone_lib.listener.MifoneCoreListener;
import com.example.mifone_lib.model.other.ConfigMifoneCore;
import com.example.mifone_lib.model.other.RegistrationState;
import com.example.mifone_lib.model.other.State;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = "TAG";
    EditText editText;
    Button button,btnCancel;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t0,ts,tsh;
    MifoneCore core;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextNumber);
        button = findViewById(R.id.button);
        btnCancel = findViewById(R.id.btn_cancel);

        t1 = findViewById(R.id.num_1);
        t2 = findViewById(R.id.num_2);
        t3 = findViewById(R.id.num_3);
        t4 = findViewById(R.id.num_4);
        t5 = findViewById(R.id.num_5);
        t6 = findViewById(R.id.num_6);
        t7 = findViewById(R.id.num_7);
        t8 = findViewById(R.id.num_8);
        t9 = findViewById(R.id.num_9);
        t0 = findViewById(R.id.num_0);
        ts = findViewById(R.id.star);
        tsh = findViewById(R.id.sharp);

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        t0.setOnClickListener(this);
        ts.setOnClickListener(this);
        tsh.setOnClickListener(this);
        core = new MifoneCore() {
            @Override
            public void initMifoneCore(Context context, ConfigMifoneCore configMifoneCore) {

            }

            @Override
            public void callOut(String phoneNumber) {

            }

            @Override
            public void cancelCurrentCall() {

            }

            @Override
            public void configMifoneCore() {

            }

            @Override
            public void registerListener(MifoneCoreListener mifoneCoreListener) {

            }
        };
        button.setOnClickListener(v -> {
            Factory.makeCall(editText.getText().toString());
        });
        btnCancel.setOnClickListener(v -> {
            Factory.cancelCall();
        });
        Factory.registerListener(new MifoneCoreListener() {
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
                Log.d("TAG", "onRegistrationStateChanged: code state: "+state.toInt()+", message: "+state.toMessage());
            }

            @Override
            public void onError(String message) {
                Log.d("TAG", "onError: "+message);
            }

            @Override
            public void onExpiredAccessToken() {

            }

            @Override
            public void onResultConfigProxy(boolean isSuccess) {

            }
        });
        ConfigMifoneCore configMifoneCore = new ConfigMifoneCore(5,"","");
        Factory.createMifoneCore(getApplicationContext(),configMifoneCore);
        Factory.configCore();
//        Factory.getMifoneCore().configMifoneCore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.num_0:
                editText.setText(editText.getText().toString()+"0");
                break;
            case R.id.num_1:
                editText.setText(editText.getText().toString()+"1");
                break;
            case R.id.num_2:
                editText.setText(editText.getText().toString()+"2");
                break;
            case R.id.num_3:
                editText.setText(editText.getText().toString()+"3");
                break;
            case R.id.num_4:
                editText.setText(editText.getText().toString()+"4");
                break;
            case R.id.num_5:
                editText.setText(editText.getText().toString()+"5");
                break;
            case R.id.num_6:
                editText.setText(editText.getText().toString()+"6");
                break;
            case R.id.num_7:
                editText.setText(editText.getText().toString()+"7");
                break;
            case R.id.num_8:
                editText.setText(editText.getText().toString()+"8");
                break;
            case R.id.num_9:
                editText.setText(editText.getText().toString()+"9");
                break;
            case R.id.sharp:
                editText.setText(editText.getText().toString()+"*");
                break;
            case R.id.star:
                editText.setText(editText.getText().toString()+"#");
                break;
        }
    }
}