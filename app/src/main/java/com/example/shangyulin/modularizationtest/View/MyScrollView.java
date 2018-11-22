package com.example.shangyulin.modularizationtest.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyScrollView extends ViewGroup {

    private Scroller scroller;

    private int heightPixels;
    private int y;
    private int lastY;
    private int startY;
    private int endY;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        getConfig();
        params.height = getChildCount() * heightPixels;
        setLayoutParams(params);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(l, i * heightPixels, r, (i + 1) * heightPixels);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(0, scroller.getCurrY());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getY();
                startY = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = lastY - y;
//                if (getScrollY() < 0 || getScrollY() > getHeight() - heightPixels) {
//                    dy = 0;
//                }
                scrollBy(0, dy);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                endY = getScrollY();
                int scrollDY = endY - startY;
                if (scrollDY > 0) {
                    if (scrollDY < heightPixels / 3) {
                        scroller.startScroll(0, getScrollY(), 0, -scrollDY);
                    } else {
                        scroller.startScroll(0, getScrollY(), 0, heightPixels - scrollDY);
                    }
                } else {
                    if (-scrollDY > heightPixels / 3) {
                        scroller.startScroll(0, getScrollY(), 0, -scrollDY);
                    } else {
                        scroller.startScroll(0, getScrollY(), 0, -heightPixels - scrollDY);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    /**
     * 获取屏幕宽高
     */
    public void getConfig() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        heightPixels = dm.heightPixels;
    }
}
