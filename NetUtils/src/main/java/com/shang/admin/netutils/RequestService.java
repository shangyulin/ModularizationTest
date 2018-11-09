package com.shang.admin.netutils;

import android.content.Context;

public class RequestService {

    public static class RequestHolder{
        private static final RequestService ourInstance = new RequestService();
    }

    private RequestService() {
    }

    public static RequestService getInstance(){
        return RequestHolder.ourInstance;
    }


    public void invoke(Context context, String name, RequestParams params, RequestCallback callback){

    }
}
