package com.shang.admin.detail;

import android.app.Application;
import android.content.Context;

import com.example.base.BaseAppInit;


public class DetailInit implements BaseAppInit {

    public static Application application;

    @Override
    public void InitApplicationSpeed(Application application) {
        this.application = application;
    }

    public static Context getContext(){
        return application.getApplicationContext();
    }

}
