package com.example.base.MemoryCache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCacheUtils {

    private LruCache<String, Bitmap> mLruCache;

    public MemoryCacheUtils() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;

        mLruCache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 通过url从内存中获取图片
     *
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url) {
        return mLruCache.get(url);
    }

    /**
     * 设置Bitmap到内存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {
        if (getBitmapFromMemory(url) == null) {
            mLruCache.put(url, bitmap); // 设置图片
        }
    }

    /**
     * 从缓存中删除指定的Bitmap
     *
     * @param key
     */
    public void removeBitmapFromMemory(String key) {
        mLruCache.remove(key);
    }
}
