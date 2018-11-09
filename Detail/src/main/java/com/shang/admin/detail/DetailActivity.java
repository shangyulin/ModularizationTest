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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.base.MovieSubject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


@Route(path = "/detail/DetailActivity")
public class DetailActivity extends Activity {

    private ListView lv;
    private ProgressBar pb;
    private static MovieSubject subject;

    public DetailActivity() {
    }

    private Observer<MovieSubject> observer = new Observer<MovieSubject>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(MovieSubject t) {
            Message msg = new Message();
            msg.obj = t;
            handler.sendMessage(msg);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    public Observer<MovieSubject> getObserver() {
        return observer;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            subject = (MovieSubject) msg.obj;
            lv.setAdapter(new DetailAdapter(subject.getSubjects()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        lv = DetailActivity.this.findViewById(R.id.lv);
        pb = DetailActivity.this.findViewById(R.id.pb);
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
            title.setText(list.get(position).getTitle());
            return view;
        }
    }
}
