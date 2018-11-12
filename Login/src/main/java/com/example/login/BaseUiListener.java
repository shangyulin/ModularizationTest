package com.example.login;

import android.app.Activity;
import android.content.Context;

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
        Util.showResultDialog(context, response.toString(), "登录成功");
        //doComplete((JSONObject)response);
    }

    protected abstract void doComplete(JSONObject values);

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
}
