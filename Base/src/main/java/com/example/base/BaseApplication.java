package com.example.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shangyulin on 2018/9/4.
 */

public class BaseApplication extends Application {

    public static Activity context;

    public static List<Activity> list = new ArrayList();

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                context = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                context = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public static Activity getTopActivity(){
        return context;
    }

    public static void addActivityToStack(Activity activity){
        list.add(activity);
    }

    public static void exitApp(){
        for (Activity activity : list){
            if (!activity.isDestroyed()){
                activity.finish();
            }
        }
    }
}
