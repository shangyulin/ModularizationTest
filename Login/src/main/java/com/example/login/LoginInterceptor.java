package com.example.login;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.example.base.BaseApplication;
import com.example.base.PermissionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
                    PermissionUtils.getInstance().requestPermission(BaseApplication.getTopActivity(), 200, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_SMS}, new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            AndPermission.rationaleDialog(BaseApplication.getTopActivity(), rationale).show();
                        }
                    }, new PermissionListener() {
                        @Override
                        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                            // 成功获取到所有权限，放行
                            callback.onContinue(postcard);
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            // 是否有不再提示并拒绝的权限。
                            if (AndPermission.hasAlwaysDeniedPermission(BaseApplication.getTopActivity(), deniedPermissions)) {
                                // 第一种：用AndPermission默认的提示语。
                                AndPermission.defaultSettingDialog(BaseApplication.getTopActivity(), 300).show();
                            }
                        }
                    });
                }
            });
        }else if(postcard.getPath().equals("/detail/DetailActivity")){
            BaseApplication.getTopActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PermissionUtils.getInstance().requestPermission(BaseApplication.getTopActivity(), 200, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            AndPermission.rationaleDialog(BaseApplication.getTopActivity(), rationale).show();
                        }
                    }, new PermissionListener() {
                        @Override
                        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                            // 成功获取到所有权限，放行
                            callback.onContinue(postcard);
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            // 是否有不再提示并拒绝的权限。
                            if (AndPermission.hasAlwaysDeniedPermission(BaseApplication.getTopActivity(), deniedPermissions)) {
                                // 第一种：用AndPermission默认的提示语。
                                AndPermission.defaultSettingDialog(BaseApplication.getTopActivity(), 300).show();
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
