package com.example.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.base.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by shangyulin on 2018/9/12.
 */

public abstract class BaseUiListener implements IUiListener {

    private Activity context;

    public BaseUiListener(Activity context) {
        this.context = context;
    }

    @Override
    public void onComplete(Object response) {
        if (null == response) {
            Util.showResultDialog(context, "返回为空", "登录失败");
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            Util.showResultDialog(context, "返回为空", "登录失败");
            return;
        }
        showResultDialog(context, response.toString(), "登录成功", this);
    }

    protected abstract void doComplete(String values);

    @Override
    public void onError(UiError e) {
        Util.toastMessage(context, "onError: " + e.errorDetail);
        Util.dismissDialog();
    }

    @Override
    public void onCancel() {
        Util.toastMessage(context, "onCancel: ");
        Util.dismissDialog();

    }

    public void showResultDialog(Context context, final String msg, String title, final BaseUiListener listener) {
        if(msg == null) return;
        String rmsg = msg.replace(",", "\n");
        new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
                .setPositiveButton("回到首页", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ARouter.getInstance().build("/main/MainActivity").navigation();
                    }
                })
                .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.doComplete(msg);
                    }
                }).create().show();
    }
}
