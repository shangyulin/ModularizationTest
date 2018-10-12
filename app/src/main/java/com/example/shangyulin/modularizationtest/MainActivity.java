package com.example.shangyulin.modularizationtest;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.base.PermissionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

@Route(path = "/main/MainActivity")
public class MainActivity extends Activity {

    private TextView tv;

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
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

        // 支持背压Flowable
        // 支持背压
//        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
//                            @Override
//                            public void subscribe(FlowableEmitter<String> e) throws Exception {
//                                e.onNext("1");
//                                e.onNext("2");
//                                e.onNext("3");
//                                e.onNext("4");
//                                e.onNext("5");
//                                e.onNext("6");
//                                e.onNext("7");
//                                e.onComplete();
//                            }
//                        }
//                , BackpressureStrategy.BUFFER);
//
//        Subscriber subscriber = new Subscriber() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                sub = s;
//                sub.request(1);
//            }
//
//            @Override
//            public void onNext(Object o) {
//                System.out.println((String)o);
//                sub.request(1);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//        flowable.subscribe(subscriber);


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
                    Class<?> clazz = Class.forName("com.example.shangyulin.modularizationtest.People");
                    Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class, int.class);
                    constructor.setAccessible(true);
                    Object haha = constructor.newInstance("haha", 4, 2);
                    Method method = clazz.getDeclaredMethod("getName");
                    String content = (String) method.invoke(haha);
                    tv.setText(content);
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
        findViewById(getResources().getIdentifier("rx", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                MovieService movieService = retrofit.create(MovieService.class);
                movieService.getTop250(0, 20)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<MovieSubject>() {
                            @Override
                            public void onComplete() {

                            }
                            @Override
                            public void onError(Throwable e) {

                            }
                            @Override
                            public void onNext(MovieSubject movieSubject) {
                                
                            }
                        });
            }
        });
    }
}
