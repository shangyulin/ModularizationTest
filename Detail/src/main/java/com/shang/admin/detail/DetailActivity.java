package com.shang.admin.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.example.base.BaseApplication;
import com.example.base.MemoryCache.LocalCacheUtils;
import com.example.base.MovieService;
import com.example.base.MovieSubject;
import com.example.base.Util;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Route(path = "/detail/DetailActivity")
public class DetailActivity extends Activity {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private ListView lv;
    private ProgressBar pb;

    private Observer<MovieSubject> observer = new Observer<MovieSubject>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(MovieSubject t) {
            /**
             * 网络请求返回的数据保存到本地
             */
            LocalCacheUtils.save(t);
            lv.setAdapter(new DetailAdapter(t.getSubjects()));
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        BaseApplication.addActivityToStack(this);
        lv = DetailActivity.this.findViewById(R.id.lv);
        pb = DetailActivity.this.findViewById(R.id.pb);
        // 从本地读取文件
        Object object = LocalCacheUtils.read();
        if (object ==  null){
            // 网络获取
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            MovieService movieService = retrofit.create(MovieService.class);
            movieService.getTop250(0, 20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            Util.toastMessage(this, "网络读取数据");
        }else{
            Util.toastMessage(this, "本地读取数据");
            lv.setAdapter(new DetailAdapter(((MovieSubject)object).getSubjects()));
        }
    }


    class DetailAdapter extends BaseAdapter{

        List<MovieSubject.SubjectsBean> list;

        public DetailAdapter(List<MovieSubject.SubjectsBean> subjectsBeanList){
            this.list = subjectsBeanList;
            if (pb != null){
                pb.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null){
                view = View.inflate(DetailActivity.this, R.layout.item_detail, null);
            }else{
                view = convertView;
            }
            TextView title = view.findViewById(R.id.title);
            ImageView icon = view.findViewById(R.id.icon);
            title.setText(list.get(position).getTitle());
            Glide.with(DetailActivity.this)
                    .load(list.get(position).getImages().getSmall())
                    .into(icon);
            return view;
        }
    }

}
