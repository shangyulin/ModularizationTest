package com.example.base;

import android.content.Context;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * Created by shangyulin on 2018/8/20.
 */

public class PermissionUtils {

    private PermissionUtils(){

    }

    public static class PermissionHolder{
        public static PermissionUtils utils = new PermissionUtils();
    }

    public static PermissionUtils getInstance() {
        return PermissionHolder.utils;
    }

    public static void requestPermission(final Context context, String[] requestPermission, final ResultCallback callback) {
        AndPermission.with(context).runtime().permission(requestPermission).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> strings) {
                callback.onSuccessed();
            }
        }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> strings) {
                callback.onFail(strings);
            }
        }).start();
    }
}
