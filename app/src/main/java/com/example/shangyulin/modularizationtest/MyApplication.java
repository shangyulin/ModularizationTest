package com.example.shangyulin.modularizationtest;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.base.ApplicationInitConfig;
import com.example.base.BaseAppInit;
import com.example.base.BaseApplication;

/**
 * Created by shangyulin on 2018/8/20.
 */

public class MyApplication extends BaseApplication {

    private BaseAppInit init;

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
        InitApplicationSpeed();
    }

    public void InitApplicationSpeed(){
        for (String str : ApplicationInitConfig.Config){
            try {
                Class clazz = Class.forName(str);

                init = (BaseAppInit) clazz.newInstance();

                init.InitApplicationSpeed(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
