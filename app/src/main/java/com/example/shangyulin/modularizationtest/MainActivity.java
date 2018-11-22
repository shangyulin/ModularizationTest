package com.example.shangyulin.modularizationtest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.base.BaseApplication;
import com.example.base.PermissionUtils;
import com.example.shangyulin.modularizationtest.View.NewTextView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/main/MainActivity")
public class MainActivity extends Activity {

    private NewTextView tv;
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Date date = new Date();
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tv.setText(sfd.format(date));
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        BaseApplication.addActivityToStack(this);
        // 不支持背压
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(3);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).distinct()// 去重
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "This is " + integer;
                    }
                }).observeOn(AndroidSchedulers.mainThread())// 下游观察者在主线程中执行
                .subscribeOn(Schedulers.io())// 被观察者在子线程中执行
                .subscribe(RxBus.getInstance());

        tv = findViewById(getResources().getIdentifier("textView", "id", getPackageName()));


        findViewById(getResources().getIdentifier("arouter", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发起路由跳转
                ARouter.getInstance().build("/test/LoginActivity").withString("key", "123").navigation();
            }
        });

        findViewById(getResources().getIdentifier("per", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.getInstance().requestPermission(MainActivity.this, 100, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_SMS}, new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                }, new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Toast.makeText(MainActivity.this, "成功获取权限", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        // 是否有不再提示并拒绝的权限。
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                            // 第一种：用AndPermission默认的提示语。
                            // AndPermission.defaultSettingDialog(MainActivity.this, 400).show();

                            // 第二种：用自定义的提示语。
                            AndPermission.defaultSettingDialog(MainActivity.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，已经没法愉快的玩耍了，请在设置中授权！")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                    }
                });
            }
        });

        // 反射
        findViewById(getResources().getIdentifier("button", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // 反射类
                    Class<?> clazz = Class.forName("com.example.shangyulin.modularizationtest.People");
                    // 构造方法
                    Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
                    constructor.setAccessible(true);
                    Object haha = constructor.newInstance("haha", 4);
                    Method method = clazz.getDeclaredMethod("getName");
                    String content = (String) method.invoke(haha);
                    Date date = new Date();
                    SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
                    tv.setText(sfd.format(date));
                    handler.sendEmptyMessageDelayed(0, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(getResources().getIdentifier("button2", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/pay/PayActivity").navigation();
            }
        });

        findViewById(getResources().getIdentifier("image", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/main/ImageActivity").navigation();
            }
        });

        // 获取Android manifest中meta-data中的数据
        // getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
        // 网络请求
//        findViewById(getResources().getIdentifier("rx", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ARouter.getInstance().build("/detail/DetailActivity").navigation();
//            }
//        });

        findViewById(getResources().getIdentifier("exit", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(getResources().getIdentifier("unload", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri packageURI = Uri.parse("package:".concat(getPackageName()));
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(packageURI);
                startActivity(intent);
            }
        });

        findViewById(getResources().getIdentifier("sv", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/main/ScrollActivity").navigation();
            }
        });

        findViewById(getResources().getIdentifier("huadong", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/main/huadongActivity").navigation();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
