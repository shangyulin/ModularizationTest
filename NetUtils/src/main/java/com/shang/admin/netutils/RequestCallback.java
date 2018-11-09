package com.shang.admin.netutils;

public interface RequestCallback {

    void Success(String content);

    void failure(String errorMsg);
}
