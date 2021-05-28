package com.example.sinevol;

import android.app.Application;

import com.example.sinevol.Utils.SunmiPrintHelper;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }
    private void init(){
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
    }
}
