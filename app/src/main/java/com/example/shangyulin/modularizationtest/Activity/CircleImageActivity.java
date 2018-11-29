package com.example.shangyulin.modularizationtest.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.base.Util;
import com.example.shangyulin.modularizationtest.R;

@Route(path = "/main/CircleImageActivity")
public class CircleImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_image);
        Util.MIUISetStatusBarLightMode(this, true);
    }
}
