package com.example.base;

import java.util.List;

public interface ResultCallback {

    void onSuccessed();

    void onFail(List<String> list);
}
