package com.example.base.MemoryCache;

import android.os.Environment;

import com.example.base.BaseApplication;
import com.example.base.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LocalCacheUtils {

    private static final String MOVIE_CACHE = Environment.getExternalStorageDirectory() + "/" + "cache.txt";
    // 将一个对象保存到本地
    public static void save(Object obj){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(new File(MOVIE_CACHE));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            Util.toastMessage(BaseApplication.getTopActivity(), "写入完成");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close(); //最后关闭输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Object read(){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(MOVIE_CACHE));
            ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (ois != null) ois.close();
                if (fis != null) fis.close(); //最后关闭输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
