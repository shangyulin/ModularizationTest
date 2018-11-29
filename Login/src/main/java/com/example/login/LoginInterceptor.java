package com.example.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.example.base.BaseApplication;
import com.example.base.PermissionUtils;
import com.example.base.ResultCallback;

import java.util.List;


/**
 * Created by shangyulin on 2018/9/3.
 */

/**
 * 加载登录页面之前进行拦截
 */
@Interceptor(priority = 1)
public class LoginInterceptor implements IInterceptor {

    private Context mContext;

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        if (postcard.getPath().equals("/test/LoginActivity")){
            BaseApplication.getTopActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PermissionUtils.requestPermission(BaseApplication.getTopActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new ResultCallback() {
                        @Override
                        public void onSuccessed() {
                            // 成功获取到所有权限，放行
                            callback.onContinue(postcard);
                        }

                        @Override
                        public void onFail(List<String> list) {
                            Toast.makeText(BaseApplication.getTopActivity(), "获取权限失败", Toast.LENGTH_LONG).show();
                            // 大于6.0
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.canDrawOverlays(BaseApplication.getTopActivity())) {
                                    Toast.makeText(BaseApplication.getTopActivity(), "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                                    //8.0以上
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        BaseApplication.getTopActivity().startActivity(intent);
                                    }
                                    //6.0-8.0
                                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + BaseApplication.getTopActivity().getPackageName()));
                                        BaseApplication.getTopActivity().startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }else if(postcard.getPath().equals("/detail/DetailActivity")){
            BaseApplication.getTopActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PermissionUtils.requestPermission(BaseApplication.getTopActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new ResultCallback() {
                        @Override
                        public void onSuccessed() {
                            // 成功获取到所有权限，放行
                            callback.onContinue(postcard);
                        }

                        @Override
                        public void onFail(List<String> list) {
                            Toast.makeText(BaseApplication.getTopActivity(), "获取权限失败", Toast.LENGTH_LONG).show();
                            // 大于6.0
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.canDrawOverlays(BaseApplication.getTopActivity())) {
                                    Toast.makeText(BaseApplication.getTopActivity(), "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                                    //8.0以上
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        BaseApplication.getTopActivity().startActivity(intent);
                                    }
                                    //6.0-8.0
                                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + BaseApplication.getTopActivity().getPackageName()));
                                        BaseApplication.getTopActivity().startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }else{
            // 放行
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
