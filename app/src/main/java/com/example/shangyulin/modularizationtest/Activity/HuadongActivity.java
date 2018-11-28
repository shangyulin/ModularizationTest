package com.example.shangyulin.modularizationtest.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.base.Util;
import com.example.shangyulin.modularizationtest.R;

@Route(path = "/main/huadongActivity")
public class HuadongActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private Bitmap out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huadong);
        Util.MIUISetStatusBarLightMode(this, true);

    }
}
