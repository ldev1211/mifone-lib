package com.example.mifone_lib.model.other;

public class RegistrationState {
    public static int NONE = 0;
    public static int PROGRESS = 1;
    public static int OK = 2;
    public static int CLEARED = 3;
    public static int FAILED = 4;

    private int mValue;

    public RegistrationState(int value) {
        mValue = value;
    }

    public int toInt(){
        return mValue;
    }

}