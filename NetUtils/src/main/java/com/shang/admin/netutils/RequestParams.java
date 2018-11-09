package com.shang.admin.netutils;

public class RequestParams {

    private String key;

    private String str_value;

    private int int_value;

    private double dou_value;


    public RequestParams(String key, String str_value) {
        this.key = key;
        this.str_value = str_value;
    }

    public RequestParams(String key, int int_value) {
        this.key = key;
        this.int_value = int_value;
    }

    public RequestParams(String key, double dou_value) {
        this.key = key;
        this.dou_value = dou_value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStr_value() {
        return str_value;
    }

    public void setStr_value(String str_value) {
        this.str_value = str_value;
    }

    public int getInt_value() {
        return int_value;
    }

    public void setInt_value(int int_value) {
        this.int_value = int_value;
    }

    public double getDou_value() {
        return dou_value;
    }

    public void setDou_value(double dou_value) {
        this.dou_value = dou_value;
    }
}
