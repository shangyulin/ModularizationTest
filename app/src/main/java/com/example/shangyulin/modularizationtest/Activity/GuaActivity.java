package com.example.shangyulin.modularizationtest.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.base.Util;
import com.example.shangyulin.modularizationtest.R;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/main/GuaActivity")
public class GuaActivity extends AppCompatActivity {

    private int startX;
    private int startY;
    private ViewPager viewPager;

    private List<Integer> list = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gua);
        Util.MIUISetStatusBarLightMode(this, true);
        addData();
        viewPager = findViewById(getResources().getIdentifier("vp", "id", getPackageName()));
        viewPager.setAdapter(new MyViewPagerAdapter());
    }

    private void addData() {
        list.add(R.drawable.awaiyi);
        list.add(R.drawable.big2);
        list.add(R.drawable.bwaiyi);
    }

    @SuppressLint("ClickableViewAccessibility")
    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return "第" + (position + 1) + "幅图";
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(GuaActivity.this, R.layout.item_gua,null);
            ImageView src = view.findViewById(R.id.src);
            final ImageView cop = view.findViewById(R.id.cop);
            // 背景图
            src.setBackgroundResource(list.get(position));
            // 添加涂层
            Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
            final Bitmap out = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), bit.getConfig());
            Canvas canvas = new Canvas(out);
            canvas.drawBitmap(bit, new Matrix(), new Paint());

            cop.setBackground(new BitmapDrawable(out));
            cop.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            startX = (int) event.getX();
                            startY = (int) event.getY();

                            for (int i = -20; i < 20; i++) {
                                for (int j = -20; j < 20; j++) {
                                    if (Math.sqrt(i * i + j * j) < 20){
                                        if (startX + i < out.getWidth() && startY + j < out.getHeight() && startY + j > 0 && startX + i > 0){
                                            out.setPixel(startX + i, startY + j, Color.TRANSPARENT);
                                        }
                                    }
                                }
                            }
                            cop.setImageBitmap(out);
                            break;
                    }
                    return true;
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
