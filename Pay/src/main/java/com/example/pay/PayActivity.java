package com.example.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/pay/PayActivity")
public class PayActivity extends Activity {

    private GridView grid;
    private List<Integer> sourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay);
        grid = findViewById(getResources().getIdentifier("grid", "id", getPackageName()));

        sourceList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            sourceList.add(R.drawable.one);
        }
        grid.setAdapter(new GridAdapter());
    }

    class ViewHolder{
        ImageView photo;
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int i) {
            return sourceList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View mView;
            ViewHolder holder;
            if (view == null){
                holder = new ViewHolder();
                mView = View.inflate(PayActivity.this, R.layout.item_image, null);
                holder.photo = mView.findViewById(getResources().getIdentifier("pay_image", "id", getPackageName()));
                mView.setTag(holder);
            }else{
                mView = view;
                holder = (ViewHolder) mView.getTag();
            }
            holder.photo.setImageResource(sourceList.get(i));
            return mView;
        }
    }
}
