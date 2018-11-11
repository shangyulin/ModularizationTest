package com.example.shangyulin.modularizationtest.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.example.shangyulin.modularizationtest.R;

public class NewTextView extends AppCompatTextView {

    private Paint paint1;
    private Paint paint2;

    private Context context;
    private TextPaint paint;
    private Matrix matrix;
    private LinearGradient gradient;

    private int mTranslate = 0;
    private int view_width;

    public NewTextView(Context context) {
        this(context, null);
    }

    public NewTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 自定义属性
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewTextView);
//        String data = typedArray.getString(R.styleable.NewTextView_hahaha);
//        Log.d("giant", data);
//        typedArray.recycle();
        this.context = context;
        paint1 = new Paint();
        paint1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        paint1.setStyle(Paint.Style.FILL);

        paint2 = new Paint();
        paint2.setColor(getResources().getColor(getResources().getIdentifier("yellow", "color", context.getPackageName())));
        paint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        view_width = getMeasuredWidth();
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint1);
        canvas.drawRect(10, 10, getMeasuredWidth() - 10, getMeasuredHeight() - 10, paint2);
        canvas.save();
        super.onDraw(canvas);
        if (gradient != null){
            mTranslate += view_width / 5;
            if (mTranslate >= 3 * view_width){
                mTranslate = -view_width;
            }
            matrix.setTranslate(mTranslate, 0);
            gradient.setLocalMatrix(matrix);
            postInvalidateDelayed(150);
        }
        canvas.restore();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        view_width = getMeasuredWidth();
        paint = getPaint();
        gradient = new LinearGradient(0, 0, view_width, 0, new int[]{Color.RED, 0xffffff, Color.RED}, null, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        matrix = new Matrix();
    }
}
