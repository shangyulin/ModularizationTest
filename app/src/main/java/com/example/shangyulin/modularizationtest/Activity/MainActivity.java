package com.example.shangyulin.modularizationtest.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.base.BaseApplication;
import com.example.base.PermissionUtils;
import com.example.base.ResultCallback;
import com.example.shangyulin.modularizationtest.R;
import com.example.shangyulin.modularizationtest.RxBus;
import com.example.shangyulin.modularizationtest.View.NewTextView;

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

    private Handler handler = new Handler() {
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

        CheckPermission();

        tv = findViewById(getResources().getIdentifier("textView", "id", getPackageName()));

        findViewById(getResources().getIdentifier("arouter", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发起路由跳转
                ARouter.getInstance().build("/test/LoginActivity").withString("key", "123").navigation();
            }
        });

        // 反射
        findViewById(getResources().getIdentifier("button", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // 反射类
                    Class<?> clazz = Class.forName("com.example.shangyulin.modularizationtest.Bean.People");
                    // 构造方法
                    Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
                    constructor.setAccessible(true);
                    Object haha = constructor.newInstance("haha", 4);
                    Method method = clazz.getDeclaredMethod("getName");
                    String content = (String) method.invoke(haha);
                    Date date = new Date();
                    SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    ARouter.getInstance().build("/main/ImageActivity").navigation();
                }else{
                    Toast.makeText(MainActivity.this, "没存储权限", Toast.LENGTH_LONG).show();
                    CheckPermission();
                }
            }
        });

        // 获取Android manifest中meta-data中的数据
        // getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);

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

        findViewById(getResources().getIdentifier("button3", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/main/GuaActivity").navigation();
            }
        });

        findViewById(getResources().getIdentifier("button4", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/main/CircleImageActivity").navigation();
            }
        });

        findViewById(getResources().getIdentifier("button5", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/main/IconSelectActivity").navigation();
            }
        });
    }

    private void rx_test() {
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
    }

    private void CheckPermission() {

        PermissionUtils.requestPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new ResultCallback() {
            @Override
            public void onSuccessed() {
                Toast.makeText(MainActivity.this, "成功获取权限", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(List<String> list) {
                Toast.makeText(MainActivity.this, "获取权限失败", Toast.LENGTH_LONG).show();
                // 大于6.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
