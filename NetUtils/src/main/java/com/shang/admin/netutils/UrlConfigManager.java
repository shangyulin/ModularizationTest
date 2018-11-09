package com.shang.admin.netutils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class UrlConfigManager {

    private static List<UrlData> urlList = new ArrayList();

    public static UrlData findUrl(Activity activity, String key){
        if (urlList == null || urlList.size() <= 0){
            fetchUrlFromXml();
        }
        for (UrlData data : urlList){
            if (key.equals(data.getKey())){
                return data;
            }
        }
        return null;
    }

    private static void fetchUrlFromXml() {

    }
}
