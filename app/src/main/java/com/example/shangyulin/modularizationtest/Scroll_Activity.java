package com.example.shangyulin.modularizationtest;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.base.Util;
@Route(path = "/main/ScrollActivity")
public class Scroll_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        Util.MIUISetStatusBarLightMode(this, true);

    }
}
