package com.example.shangyulin.modularizationtest.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollerDemoView extends View {


    private Paint paint;
    private Scroller scroller;
    private int x;
    private int y;
    private int lastX;
    private int lastY;
    private int startX;
    private int startY;
    private int endY;
    private int endX;

    public ScrollerDemoView(Context context) {
        this(context, null);
    }

    public ScrollerDemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        paint = new Paint();
        paint.setColor(Color.RED);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(150, 150, 350, 350, paint);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            ((View)getParent()).scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int) event.getX();
        y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                startX = getScrollX();
                startY = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - lastX;
                int dy = y - lastY;
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
                params.leftMargin = getLeft() + dx;
                params.topMargin = getTop() + dy;
                setLayoutParams(params);
                break;
            case MotionEvent.ACTION_UP:
                endY = getScrollY();
                endX = getScrollX();
                int scroll_dy = endY - startY;
                View parent = ((View) getParent());
                if (scroll_dy > 0){
                    scroller.startScroll(parent.getScrollX(), parent.getScrollY(), -endX, -endY);
                }

                break;
        }
        return true;
    }
}
